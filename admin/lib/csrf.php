<?php
declare(strict_types=1);

/** Sinh (hoặc lấy) CSRF token cho phiên. */
function csrf_token(): string
{
    if (empty($_SESSION['csrf'])) {
        $_SESSION['csrf'] = bin2hex(random_bytes(32));
    }
    return $_SESSION['csrf'];
}

/** In field ẩn CSRF cho form. */
function csrf_field(): string
{
    return '<input type="hidden" name="_csrf" value="' . e(csrf_token()) . '">';
}

/**
 * Kiểm tra CSRF cho request POST. Dừng với 403 nếu sai.
 * Gọi ở đầu mọi trang xử lý POST.
 */
function csrf_check(): void
{
    if (($_SERVER['REQUEST_METHOD'] ?? 'GET') !== 'POST') {
        return;
    }
    $token = $_POST['_csrf'] ?? '';
    if (!is_string($token) || !hash_equals($_SESSION['csrf'] ?? '', $token)) {
        http_response_code(403);
        exit('CSRF token không hợp lệ. Tải lại trang và thử lại.');
    }
}
