# Avatar 2D — Server + Clients

Nguồn game Avatar 2.5.8 (bản chỉnh sửa từ TeaMobi) gồm **server Java** và các
**client**. Repo này gộp lại để dựng thành server online.

```
server/          Server game (Java + Maven, MySQL), port mặc định 19128
client/java/     Client J2ME (MIDlet) — source + script build ra JAR (chạy PC/emulator)
client/unity/    Client Unity C# — build APK Android + iOS native, đã khoá màn hình ngang
client/android/  APK Android đã đóng gói sẵn (prebuilt)
```

**Android & iOS "khớp 100%, không lật": dùng `client/unity/`** — đây là app native Unity
(không phải bọc emulator J2ME), build được cả Android lẫn iOS từ một codebase, đã set
sẵn **màn hình ngang**. Xem hướng dẫn build trong `client/unity/README.md`.

> ⚠️ Đây là source game của **TeaMobi (Avatar)** đã được cộng đồng chỉnh sửa.
> Dùng cho mục đích học tập / private server. Thương mại hoá có thể vướng bản quyền.

---

## 1. Server

- **Ngôn ngữ:** Java 8+ (build/test bằng JDK 21), Maven
- **Database:** MySQL (file dump: `server/database/avatar_2x.sql`)
- **Main class:** `avatar.server.Avatar`
- **Port:** `19128` (đổi trong `server/config.properties`)

### Cài đặt

```bash
# 1. Tạo DB và import
mysql -u root -p -e "CREATE DATABASE avatar_2x CHARACTER SET utf8mb4;"
mysql -u root -p avatar_2x < server/database/avatar_2x.sql

# 2. Cấu hình kết nối DB
#    Sửa server/database.properties: host/port/dbname/username/password

# 3. Giải nén tài nguyên game vào thư mục server (server đọc res/hd, res/medium)
cd server && unrar x resAndProjectUnityFileGameJar/res.rar .

# 4. Build & chạy
mvn clean package -DskipTests
java -jar target/Avatar2D-1.0-SNAPSHOT.jar
```

Mở firewall cho port `19128` để client bên ngoài kết nối được.

### Ghi chú thay đổi so với bản gốc
- `pom.xml`: sửa `mainClass` `com.kitakeyos.MainClass` → `avatar.server.Avatar`
  (main thật nằm ở đây; giá trị cũ không tồn tại nên jar shade không chạy được).
- `pom.xml`: nâng Lombok `1.18.24` → `1.18.34` để build được trên JDK mới (JDK 17+/21).

---

## 2. Client Java (J2ME / MIDlet)

- **MIDlet:** `main.GameMidlet`, MIDP-2.0 / CLDC-1.0
- **Chạy bằng:** emulator desktop (KEmulator, AngelChip) hoặc máy MIDP thật (sau khi preverify)

### Build lại JAR

```bash
cd client/java
./build.sh          # -> dist/Avatar258.jar  (508 files, ~1 MB)
```

Script compile 329 file nguồn (các stub `javax.microedition` trong `lib/` để trên
*classpath*, `java.*` lấy từ JDK host) rồi đóng gói cùng resource (`normal/`, `icon.png`, ...).

### Trỏ client về server của bạn (bước "online" quan trọng)

Client đang lấy danh sách server từ TeaMobi. Sửa để trỏ về server của bạn:

- `client/java/src/main/GameMidlet.java`
  ```java
  linkGetHost = { "http://teamobi.com/srvips/avatar2.txt", ... }
  ```
  → đổi thành URL/IP của bạn (host 1 file text chứa `IP:19128`), hoặc hardcode
  thẳng IP server vào danh sách server.

- `client/java/src/avt/LoginScr.java`
  ```java
  KEY_API_URL = "http://160.191.242.130/validate_key.php";
  ```
  ⚠️ URL kiểm tra key trỏ tới IP của bên thứ ba — nên **kiểm tra kỹ / thay / bỏ**
  trước khi phát hành để tránh gửi dữ liệu người chơi ra ngoài.

Sau khi sửa, chạy lại `./build.sh`.

---

## 3. Client Android & iOS — dùng Unity (`client/unity/`)

Cách **khớp 100%, không lật** để có app Android + iOS: build từ **`client/unity/`** —
bản port **Unity C#** native của game (198 file C# có logic thật + đủ tài nguyên).

- Build được **cả Android lẫn iOS** từ một codebase.
- Là app native → **không dính lỗi lật sprite** như bọc jar qua emulator J2ME.
- Đã set sẵn **màn hình ngang** (landscape, khoá 2 chiều).
- Kết nối server qua `TcpClient` port `19128` (khớp server trong repo).

Chi tiết build (Android APK/AAB, iOS Xcode, cách trỏ server) xem `client/unity/README.md`.

`client/android/Avatar-PGaming.apk` là APK cũ đóng gói sẵn (giữ để tham khảo).

---

## Trạng thái các phần

| Hạng mục | Trạng thái | Ghi chú |
|----------|-----------|---------|
| Import server lên GitHub | ✅ Xong | source + DB dump + tài nguyên |
| Build lại client Java | ✅ Xong | `client/java/dist/Avatar258.jar` + `build.sh` tái tạo |
| Client Android (Unity) | ✅ Có source build được | `client/unity/` — build APK/AAB bằng Unity Editor |
| Client iOS (Unity) | ✅ Có source build được | `client/unity/` — build Xcode project; cần macOS + Xcode để ra IPA |
| APK J2ME đóng sẵn cũ | ℹ️ Tham khảo | `client/android/Avatar-PGaming.apk` (bọc emulator, không khuyến nghị) |
