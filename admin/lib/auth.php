<?php
declare(strict_types=1);

/** Băm mật khẩu theo scheme của game để so sánh. */
function hash_password(string $plain): string
{
    return config('password_scheme') === 'md5' ? md5($plain) : $plain;
}

/**
 * Đăng nhập admin: xác thực với bảng `users` của máy chủ đang chọn,
 * chỉ chấp nhận tài khoản có quyền admin (role >= admin_min_role).
 * Trả về [ok(bool), message(string)].
 */
function admin_login(string $username, string $password): array
{
    $roleCol = config('admin_role_column', 'role');
    $minRole = (int) config('admin_min_role', 1);

    $u = db_one('SELECT * FROM `users` WHERE `username` = ? LIMIT 1', [$username]);
    if (!$u) {
        return [false, 'Sai tài khoản hoặc mật khẩu.'];
    }
    if (!hash_equals((string) $u['password'], hash_password($password))) {
        return [false, 'Sai tài khoản hoặc mật khẩu.'];
    }
    if ((int) ($u[$roleCol] ?? -1) < $minRole) {
        return [false, 'Tài khoản không có quyền admin.'];
    }
    // Chặn tài khoản đang bị khoá
    $lockCol = config('lock_column', 'login_lock');
    if (isset($u[$lockCol]) && (int) $u[$lockCol] === 1) {
        return [false, 'Tài khoản admin đang bị khoá.'];
    }

    session_regenerate_id(true);
    $_SESSION['admin'] = [
        'id'       => (int) $u['id'],
        'username' => $u['username'],
        'role'     => (int) ($u[$roleCol] ?? 0),
        'server'   => current_server()['id'] ?? null,
    ];
    $_SESSION['admin_last'] = time();
    return [true, 'OK'];
}

function admin_logout(): void
{
    $_SESSION = [];
    if (ini_get('session.use_cookies')) {
        $p = session_get_cookie_params();
        setcookie(session_name(), '', time() - 42000, $p['path'], $p['domain'], $p['secure'], $p['httponly']);
    }
    session_destroy();
}

function current_admin(): ?array
{
    return $_SESSION['admin'] ?? null;
}

function is_logged_in(): bool
{
    return !empty($_SESSION['admin']);
}

/** Chặn truy cập nếu chưa đăng nhập. Gọi ở đầu mọi trang admin. */
function require_admin(): void
{
    if (!is_logged_in()) {
        redirect('login.php');
    }
}
