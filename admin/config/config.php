<?php
/**
 * Cấu hình chung của Admin Panel.
 */
return [
    // Tên hiển thị
    'app_name' => 'Avatar Admin',

    // Cột đánh dấu quyền admin trong bảng `users`.
    // Server dùng `role` (mặc định -1 = thường). Admin = role >= ADMIN_MIN_ROLE.
    'admin_role_column' => 'role',
    'admin_min_role'    => 1,

    // Cột khoá đăng nhập trong bảng `users`.
    'lock_column' => 'login_lock',

    // Kiểu băm mật khẩu tài khoản game: 'md5' (varchar(32)) hoặc 'password_hash'.
    'password_scheme' => 'md5',

    // Bảng key-value mà server đọc cho các cờ runtime (bảo trì, thông báo...).
    'settings_table' => 'settings',

    // Thời gian sống của phiên đăng nhập admin (giây).
    'session_lifetime' => 3600 * 6,

    // Số dòng mỗi trang (phân trang).
    'per_page' => 30,
];
