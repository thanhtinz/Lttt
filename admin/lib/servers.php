<?php
declare(strict_types=1);

/** Đọc danh sách máy chủ từ config. */
function servers_all(): array
{
    $list = require BASE_PATH . '/config/servers.php';
    return is_array($list) ? array_values($list) : [];
}

/** Ghi lại danh sách máy chủ ra file config (dùng cho manage_servers). */
function servers_save(array $servers): bool
{
    $php = "<?php\n/**\n * Danh sách máy chủ game (đa máy chủ). Tự sinh bởi manage_servers.php\n */\nreturn "
        . var_export(array_values($servers), true) . ";\n";
    $file = BASE_PATH . '/config/servers.php';
    return file_put_contents($file, $php, LOCK_EX) !== false;
}

/** Máy chủ theo id. */
function server_by_id(?string $id): ?array
{
    foreach (servers_all() as $s) {
        if (($s['id'] ?? null) === $id) {
            return $s;
        }
    }
    return null;
}

/** Máy chủ đang được chọn (session), mặc định máy chủ đầu tiên. */
function current_server(): ?array
{
    $id = $_SESSION['server_id'] ?? null;
    $s  = server_by_id($id);
    if ($s) {
        return $s;
    }
    $all = servers_all();
    return $all[0] ?? null;
}

/** Đặt máy chủ đang chọn. */
function set_current_server(string $id): bool
{
    if (server_by_id($id)) {
        $_SESSION['server_id'] = $id;
        // Reset kết nối PDO đang cache
        $GLOBALS['PDO_CONN'] = null;
        return true;
    }
    return false;
}

/** Thử kết nối tới 1 máy chủ, trả về [ok(bool), message(string)]. */
function server_test(array $s): array
{
    try {
        $dsn = sprintf('mysql:host=%s;port=%d;dbname=%s;charset=utf8mb4',
            $s['host'] ?? '127.0.0.1', (int) ($s['port'] ?? 3306), $s['dbname'] ?? '');
        $pdo = new PDO($dsn, $s['username'] ?? '', $s['password'] ?? '', [
            PDO::ATTR_TIMEOUT => 5,
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        ]);
        $n = (int) $pdo->query('SELECT COUNT(*) FROM `users`')->fetchColumn();
        return [true, "Kết nối OK · users: {$n}"];
    } catch (Throwable $ex) {
        return [false, 'Lỗi: ' . $ex->getMessage()];
    }
}
