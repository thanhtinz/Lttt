<?php
declare(strict_types=1);

/**
 * Trả về PDO tới DB của máy chủ đang chọn.
 * Dùng prepared statements ở mọi nơi để chống SQL injection.
 */
function db(): PDO
{
    if (!empty($GLOBALS['PDO_CONN']) && $GLOBALS['PDO_CONN'] instanceof PDO) {
        return $GLOBALS['PDO_CONN'];
    }
    $s = current_server();
    if (!$s) {
        throw new RuntimeException('Chưa cấu hình máy chủ nào.');
    }
    $dsn = sprintf('mysql:host=%s;port=%d;dbname=%s;charset=utf8mb4',
        $s['host'] ?? '127.0.0.1', (int) ($s['port'] ?? 3306), $s['dbname'] ?? '');
    $pdo = new PDO($dsn, $s['username'] ?? '', $s['password'] ?? '', [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ]);
    return $GLOBALS['PDO_CONN'] = $pdo;
}

/** Query có tham số, trả về mảng tất cả dòng. */
function db_all(string $sql, array $params = []): array
{
    $st = db()->prepare($sql);
    $st->execute($params);
    return $st->fetchAll();
}

/** Query có tham số, trả về 1 dòng (hoặc null). */
function db_one(string $sql, array $params = []): ?array
{
    $st = db()->prepare($sql);
    $st->execute($params);
    $row = $st->fetch();
    return $row === false ? null : $row;
}

/** Lấy 1 giá trị scalar. */
function db_val(string $sql, array $params = [])
{
    $st = db()->prepare($sql);
    $st->execute($params);
    return $st->fetchColumn();
}

/** Thực thi INSERT/UPDATE/DELETE, trả về số dòng ảnh hưởng. */
function db_exec(string $sql, array $params = []): int
{
    $st = db()->prepare($sql);
    $st->execute($params);
    return $st->rowCount();
}

/** Kiểm tra 1 bảng có tồn tại trong DB hiện tại không. */
function db_table_exists(string $table): bool
{
    $n = db_val(
        'SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?',
        [$table]
    );
    return (int) $n > 0;
}
