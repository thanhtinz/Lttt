<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
csrf_check();
if (!empty($_POST['server_id'])) {
    if (set_current_server((string) $_POST['server_id'])) {
        flash('Đã chuyển sang máy chủ: ' . (current_server()['name'] ?? ''));
    }
}
redirect($_SERVER['HTTP_REFERER'] ?? 'index.php');
