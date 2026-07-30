<?php
declare(strict_types=1);

/** Lấy giá trị cấu hình. */
function config(string $key, $default = null)
{
    return $GLOBALS['CONFIG'][$key] ?? $default;
}

/** Escape HTML (chống XSS). */
function e($v): string
{
    return htmlspecialchars((string) ($v ?? ''), ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
}

/** Redirect và dừng. */
function redirect(string $url): void
{
    header('Location: ' . $url);
    exit;
}

/** Lấy tham số GET/POST an toàn. */
function param(string $key, $default = null)
{
    return $_POST[$key] ?? $_GET[$key] ?? $default;
}

function post(string $key, $default = null)
{
    return $_POST[$key] ?? $default;
}

function is_post(): bool
{
    return ($_SERVER['REQUEST_METHOD'] ?? 'GET') === 'POST';
}

/** Flash message qua session. */
function flash(string $msg, string $type = 'ok'): void
{
    $_SESSION['flash'][] = ['msg' => $msg, 'type' => $type];
}

function take_flash(): array
{
    $f = $_SESSION['flash'] ?? [];
    unset($_SESSION['flash']);
    return $f;
}

/** Định dạng số tiền/kim cương. */
function money($n): string
{
    return number_format((float) $n, 0, ',', '.');
}

/** Số trang hiện tại (>=1). */
function current_page(): int
{
    return max(1, (int) ($_GET['page'] ?? 1));
}

/** Tạo link giữ nguyên query hiện tại, ghi đè 1 tham số. */
function url_with(array $overrides): string
{
    $q = array_merge($_GET, $overrides);
    return strtok($_SERVER['REQUEST_URI'], '?') . '?' . http_build_query($q);
}

/** Ép integer từ input. */
function to_int($v, int $default = 0): int
{
    return is_numeric($v) ? (int) $v : $default;
}
