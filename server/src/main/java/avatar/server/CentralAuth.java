package avatar.server;

import avatar.db.DbManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Xác thực tập trung qua cổng game (Gate Game portal).
 *
 * Cấu hình trong config.properties:
 *   auth.enabled=true
 *   auth.url=http://127.0.0.1:8080/api/game-auth/verify
 *   auth.key=<central_auth_key trong admin cổng game>
 *
 * Luồng: đăng nhập game -> hỏi cổng.
 *   OK             -> đồng bộ dòng users trong DB game (tạo nếu chưa có) rồi đăng nhập.
 *   WRONG/LOCKED/UNVERIFIED -> từ chối ngay (tài khoản thuộc cổng / chưa xác minh email).
 *   NOT_FOUND      -> tài khoản cũ chưa có trên cổng: fallback kiểm tra DB game như cũ.
 *   ERROR/DISABLED -> cổng tắt/lỗi: fallback DB game để không chặn người chơi.
 */
public final class CentralAuth {

    public static final int OK = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int LOCKED = 2;
    public static final int NOT_FOUND = 3;
    public static final int UNVERIFIED = 6;
    public static final int DISABLED = 4;
    public static final int ERROR = 5;

    private static boolean enabled = false;
    private static String url = "";
    private static String key = "";
    private static boolean loaded = false;

    private CentralAuth() {
    }

    private static synchronized void loadConfig() {
        if (loaded) {
            return;
        }
        loaded = true;
        try (InputStream is = new FileInputStream("config.properties")) {
            Properties p = new Properties();
            p.load(is);
            enabled = "true".equalsIgnoreCase(p.getProperty("auth.enabled", "false"));
            url = p.getProperty("auth.url", "").trim();
            key = p.getProperty("auth.key", "").trim();
            if (url.isEmpty() || key.isEmpty()) {
                enabled = false;
            }
        } catch (Exception e) {
            enabled = false;
        }
    }

    /** Gọi API cổng để xác thực. Không ném exception. */
    public static int verify(String username, String password) {
        loadConfig();
        if (!enabled) {
            return DISABLED;
        }
        try {
            JSONObject res = postForm(url,
                    "username=" + URLEncoder.encode(username, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8"));
            if (res == null) {
                return ERROR;
            }
            String code = String.valueOf(res.get("code"));
            switch (code) {
                case "ok":
                    return OK;
                case "wrong_password":
                    return WRONG_PASSWORD;
                case "locked":
                    return LOCKED;
                case "not_found":
                    return NOT_FOUND;
                case "unverified":
                    return UNVERIFIED;
                default:
                    return ERROR;
            }
        } catch (Exception e) {
            return ERROR;
        }
    }

    /**
     * Đăng ký tài khoản từ trong game (cổng gửi mail xác minh).
     * Trả về {code, message} hoặc null nếu auth tập trung tắt / cổng lỗi.
     */
    public static String[] register(String username, String password, String email) {
        loadConfig();
        if (!enabled) {
            return null;
        }
        try {
            JSONObject res = postForm(url.replace("/verify", "/register"),
                    "username=" + URLEncoder.encode(username, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8")
                    + "&email=" + URLEncoder.encode(email, "UTF-8"));
            if (res == null) {
                return null;
            }
            return new String[]{String.valueOf(res.get("code")), String.valueOf(res.get("message"))};
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Đổi mật khẩu từ trong game qua cổng.
     * Trả về {code, message} hoặc null nếu auth tập trung tắt / cổng lỗi.
     * code: ok | not_found (fallback local) | wrong_password | error
     */
    public static String[] changePassword(String username, String oldPassword, String newPassword) {
        loadConfig();
        if (!enabled) {
            return null;
        }
        try {
            JSONObject res = postForm(url.replace("/verify", "/change-password"),
                    "username=" + URLEncoder.encode(username, "UTF-8")
                    + "&old_password=" + URLEncoder.encode(oldPassword, "UTF-8")
                    + "&new_password=" + URLEncoder.encode(newPassword, "UTF-8"));
            if (res == null) {
                return null;
            }
            return new String[]{String.valueOf(res.get("code")), String.valueOf(res.get("message"))};
        } catch (Exception e) {
            return null;
        }
    }

    /** POST form tới cổng, trả về JSON (null nếu lỗi). */
    private static JSONObject postForm(String targetUrl, String body) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(targetUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestProperty("X-Auth-Key", key);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
            int status = conn.getResponseCode();
            InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            Object parsed = JSONValue.parse(sb.toString());
            return (parsed instanceof JSONObject) ? (JSONObject) parsed : null;
        } catch (Exception e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Đồng bộ dòng users (+ players) trong DB game sau khi cổng xác thực OK:
     * tạo mới nếu chưa có, cập nhật password (MD5) để câu SELECT cũ khớp.
     */
    public static boolean syncLocalAccount(String username, String passwordMd5) {
        try (Connection con = DbManager.getInstance().getConnection()) {
            int userId = -1;
            try (PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE username = ? LIMIT 1")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                    }
                }
            }
            if (userId > 0) {
                try (PreparedStatement ps = con.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
                    ps.setString(1, passwordMd5);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
                return true;
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password, gmail, vnd, tongnap, active, timeCreate) VALUES (?, ?, '', 0, 0, 1, NOW())",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, passwordMd5);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        userId = keys.getInt(1);
                    }
                }
            }
            if (userId > 0) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO players (user_id, scores) VALUES (?, 0)")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
