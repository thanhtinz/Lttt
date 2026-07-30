<?php
/**
 * Danh sách máy chủ game (đa máy chủ).
 * Mỗi máy chủ = 1 kết nối MySQL tới DB của server đó.
 * File này được trang "Máy chủ QL" (manage_servers.php) đọc/ghi.
 *
 * Trường: id, name, host, port, dbname, username, password
 * `id` là khoá dùng để chọn máy chủ (lưu trong session).
 */
return [
    [
        'id'       => 'main',
        'name'     => 'Máy chủ chính',
        'host'     => '127.0.0.1',
        'port'     => 3306,
        'dbname'   => 'avatar_2x',
        'username' => 'root',
        'password' => '',
    ],
];
