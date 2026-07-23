<?php
/**
 * Bootstrap: nạp cấu hình, khởi tạo session, helper chung.
 * Mọi trang admin đều require file này đầu tiên.
 */
declare(strict_types=1);

define('BASE_PATH', dirname(__DIR__));

$GLOBALS['CONFIG']  = require BASE_PATH . '/config/config.php';

// Session an toàn
if (session_status() !== PHP_SESSION_ACTIVE) {
    session_set_cookie_params([
        'lifetime' => 0,
        'path'     => '/',
        'httponly' => true,
        'samesite' => 'Lax',
    ]);
    session_name('AVT_ADMIN');
    session_start();
}

require BASE_PATH . '/lib/helpers.php';
require BASE_PATH . '/lib/csrf.php';
require BASE_PATH . '/lib/servers.php';
require BASE_PATH . '/lib/db.php';
require BASE_PATH . '/lib/auth.php';

// Hết hạn phiên
$lifetime = (int) config('session_lifetime', 21600);
if (isset($_SESSION['admin_last']) && (time() - (int) $_SESSION['admin_last']) > $lifetime) {
    session_unset();
    session_destroy();
} else {
    $_SESSION['admin_last'] = time();
}
