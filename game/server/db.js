// Lớp DB: tái dùng MySQL avatar_2x. Tùy chọn — nếu không kết nối được thì
// server chạy chế độ khách (không lưu), không crash.
import mysql from 'mysql2/promise';
import { config } from './config.js';

let pool = null;
let ready = false;

export async function initDb() {
  if (!config.db.enabled) {
    console.log('[db] Tắt DB (DB_ENABLED=0) — chạy chế độ khách.');
    return false;
  }
  try {
    pool = mysql.createPool({
      host: config.db.host,
      port: config.db.port,
      user: config.db.user,
      password: config.db.password,
      database: config.db.database,
      waitForConnections: true,
      connectionLimit: 10,
    });
    await pool.query('SELECT 1');
    ready = true;
    console.log(`[db] Kết nối MySQL ${config.db.database} OK`);
    return true;
  } catch (e) {
    console.warn('[db] Không kết nối được MySQL, chạy chế độ khách:', e.message);
    pool = null;
    ready = false;
    return false;
  }
}

export const dbReady = () => ready;

// Xác thực bằng bảng users (mật khẩu MD5 như game gốc). Trả về user hoặc null.
export async function authUser(username, passwordMd5) {
  if (!ready) return null;
  const [rows] = await pool.query(
    'SELECT id, username, `role` FROM `users` WHERE `username` = ? AND `password` = ? LIMIT 1',
    [username, passwordMd5]
  );
  return rows[0] || null;
}

// Lấy hoặc tạo nhân vật cho user, đọc vị trí đã lưu (dùng cột x/y nếu thêm sau).
export async function loadPlayer(userId) {
  if (!ready) return null;
  const [rows] = await pool.query('SELECT * FROM `players` WHERE `user_id` = ? LIMIT 1', [userId]);
  return rows[0] || null;
}

// Lưu vị trí (best-effort; bỏ qua nếu cột chưa tồn tại).
export async function savePosition(userId, x, y) {
  if (!ready) return;
  try {
    await pool.query('UPDATE `players` SET `pos_x` = ?, `pos_y` = ? WHERE `user_id` = ?', [x, y, userId]);
  } catch { /* cột pos_x/pos_y có thể chưa có — bỏ qua */ }
}
