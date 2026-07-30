# Quy ước port Java → Node.js

Mục tiêu: **giữ nguyên logic và giao thức** của server Java (`../server`), chỉ đổi ngôn ngữ.
Client cũ phải chạy được không cần sửa ⇒ mọi byte gửi/nhận phải khớp.

## Cấu trúc
```
server-node/
  config.properties, database.properties   # copy từ server Java, cùng định dạng
  src/
    constants/Cmd.js, NpcName.js           # ĐÃ XONG (auto-port)
    net/JavaIO.js                          # ĐÃ XONG — DataInput/OutputStream khớp byte
    net/Message.js                          # ĐÃ XONG
    net/SessionCodec.js                     # ĐÃ XONG — XOR key + framing + handshake
    net/Session.js                          # ĐÃ XONG — TCP
    db/DbManager.js                         # ĐÃ XONG — pool MySQL
    server/ServerManager.js, UserManager.js # ĐÃ XONG
    model/ service/ handler/ message/ play/ item/ lucky/ minigame/ farm/ lib/  # CẦN PORT
```

## Quy tắc bắt buộc

1. **ESM**: `export class X`, `import { Y } from '../net/Message.js'`. Luôn ghi đuôi `.js`.
2. **Giữ nguyên tên** lớp/phương thức/hằng số của Java (kể cả tên tiếng Việt, viết sai chính tả)
   để đối chiếu dễ. Ví dụ `getAvatarService()`, `serverDialog()`, `addExp()`.
3. **Số học đúng kiểu Java**:
   - `byte`: `(v << 24) >> 24` khi cần ép; `short`: `(v << 16) >> 16`; `int`: `v | 0`.
   - Chia số nguyên: `Math.trunc(a / b)` (KHÔNG dùng `/` trực tiếp).
   - Dịch bit giữ trong 32-bit như Java (`>>`, `>>>`, `<<`).
   - `long` (mốc thời gian): dùng `Number` (`Date.now()`); chỉ dùng `BigInt` khi Java thực sự cần 64-bit.
4. **Đọc/ghi gói tin**: chỉ qua `Message`:
   ```js
   const ms = new Message(Cmd.GET_HANDLER);
   ms.writer().writeByte(1); ms.writer().writeUTF('abc');
   session.sendMessage(ms);
   // đọc:
   const b = ms.reader().readByte(); const s = ms.reader().readUTF();
   ```
   Thứ tự và kiểu ghi phải **giống hệt** bản Java (writeByte vs writeShort vs writeInt).
5. **Singleton**: Java `X.getInstance()` → export instance sẵn:
   ```js
   class MapManager { /* ... */ }
   export const mapManager = new MapManager();
   export default mapManager;
   ```
   Giữ thêm `static getInstance()` nếu code khác gọi kiểu đó.
6. **DB**: dùng `dbManager` (`await dbManager.query(sql, params)` / `queryOne` / `executeUpdate` / `insert`).
   Luôn dùng tham số `?`, không nối chuỗi. Giữ nguyên câu SQL của Java.
7. **Bất đồng bộ**: DB là `async`. Hàm nào gọi DB thì thành `async` và `await` ở nơi gọi.
   Không dùng callback lồng nhau.
8. **Đồng bộ hoá**: Node đơn luồng ⇒ bỏ `synchronized`, giữ nguyên logic.
9. **Thread/Timer**: `new Thread(...).start()` + `sleep` → `setInterval`/`setTimeout`
   (gọi `.unref()` nếu là timer nền).
10. **Ngoại lệ**: `try/catch` như Java; đừng biến lỗi thành crash nếu bản Java bỏ qua.
11. **Không thêm tính năng, không "cải tiến" logic.** Nếu bản Java có bug, port y nguyên và
    ghi `// NOTE: giữ nguyên hành vi bản Java (...)`.
12. Comment bằng **tiếng Việt**, ngắn gọn, chỉ ở chỗ khó hiểu.

## Kiểm tra
Sau khi port mỗi file: `node --check <file>` phải sạch.
Cuối cùng: `node -e "import('./src/<file>.js').then(()=>console.log('ok'))"` để chắc import chạy.
Không chạy `git`, không commit.
