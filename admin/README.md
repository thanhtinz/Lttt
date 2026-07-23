# Avatar — Admin Panel (PHP)

Bảng quản trị chạy **trực tiếp trên MySQL của server game** (`avatar_2x`), hỗ trợ **đa máy chủ**.
Chỉ build cho các bảng **thật sự có** trong DB — không tạo bảng/tính năng ảo.

## Yêu cầu
- PHP 8.0+ với `pdo_mysql`
- Truy cập tới MySQL của (các) máy chủ game

## Cài đặt
1. Đưa thư mục `admin/` lên web server (Apache/Nginx + PHP, hoặc chạy thử: `php -S 0.0.0.0:8080` trong `admin/`).
2. Sửa `config/servers.php` — khai báo kết nối DB từng máy chủ (host/port/dbname/user/pass).
   Có thể thêm/sửa/xoá + **Test kết nối** ngay trong trang **Máy chủ QL** sau khi đăng nhập.
3. Tạo admin đầu tiên: đặt `role >= 1` cho 1 tài khoản trong bảng `users`:
   ```sql
   UPDATE users SET role = 1 WHERE username = 'TEN_TAI_KHOAN';
   ```
   (Ngưỡng admin cấu hình ở `config/config.php` → `admin_min_role`.)
4. Đăng nhập tại `login.php` bằng tài khoản đó (mật khẩu dùng chung với game — băm MD5).

## Bảo mật
- **CSRF token** cho mọi form POST (`lib/csrf.php`).
- **PDO prepared statements** ở mọi truy vấn (`lib/db.php`) — chống SQL injection.
- Session cookie `HttpOnly` + `SameSite=Lax`, tự hết hạn, `session_regenerate_id` khi đăng nhập.
- Chỉ tài khoản `role >= admin_min_role` mới vào được; chặn tài khoản `login_lock=1`.

## Chức năng (theo bảng DB thật)
| Trang | Bảng | Việc |
|-------|------|------|
| Tổng quan | users, players, napthe | Thống kê TK/NV/online/admin/khoá/tổng nạp/lượt nạp |
| Tài khoản | users (+players) | Tìm, khoá/mở (`login_lock`), cấp/gỡ admin (`role`), kích hoạt, cộng VNĐ/vàng/VIP/xèng/điểm |
| Nhân vật | players | Tìm, sửa xu/lượng/xèng/điểm/level, xoá |
| Nạp thẻ | napthe | Xem lịch sử nạp + tổng tiền |
| Giới thiệu | gioithieu | Xem referral |
| Giftcode | giftcode, giftcode_use | Tạo/xoá code + phần thưởng (JSON), xem số lượt dùng |
| Vòng quay | dial_lucky | CRUD phần thưởng vòng quay |
| Vật phẩm | items | CRUD template vật phẩm |
| Nâng cấp VP | upgrade_item | CRUD công thức nâng cấp |
| Thức ăn | foods | CRUD |
| Nhà | house | CRUD |
| NPC | npc | CRUD |
| Vật phẩm bản đồ | map_item | CRUD |
| Bang hội | clans, clan_members | Tìm/sửa/xoá bang, xem & xoá thành viên |
| Nhật ký | giaodich_logs, betgame, atm_lichsu | Xem log (read-only) |
| Điều khiển server | settings | Bảo trì (`bao_tri`), thông báo (`thong_bao`), sửa settings, bump `hash_settings` |
| Máy chủ QL | (config file) | Thêm/sửa/xoá máy chủ + Test kết nối |

## Về "live" / hot-reload
Server Java đọc bảng `settings` (`bao_tri`, `thong_bao`, `hash_settings`) **khi load settings**.
Trang **Điều khiển server** ghi vào các key này và đổi `hash_settings`. Hiệu lực khi server nạp
lại settings (khởi động lại — hoặc nếu bổ sung cơ chế theo dõi `hash_settings` phía server).
Sửa **vật phẩm/NPC/...** áp dụng khi server nạp lại dữ liệu tương ứng.

> Các tính năng cần bảng/tích hợp chưa có trong DB (hòm thư, sự kiện toggle, kỹ năng, forum, chat...)
> **không** được tạo — panel chỉ quản lý dữ liệu đang tồn tại thật.
