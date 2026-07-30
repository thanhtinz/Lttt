<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$roleCol = config('admin_role_column', 'role');
$lockCol = config('lock_column', 'login_lock');
$minRole = (int) config('admin_min_role', 1);

$stats = [];
try {
    $stats['Tài khoản']   = (int) db_val('SELECT COUNT(*) FROM `users`');
    $stats['Nhân vật']    = (int) db_val('SELECT COUNT(*) FROM `players`');
    $stats['Đang online'] = (int) db_val('SELECT COUNT(*) FROM `players` WHERE `is_online` = 1');
    $stats['Admin']       = (int) db_val("SELECT COUNT(*) FROM `users` WHERE `$roleCol` >= ?", [$minRole]);
    $stats['Bị khoá']     = (int) db_val("SELECT COUNT(*) FROM `users` WHERE `$lockCol` = 1");
    $stats['Tổng nạp (VNĐ)'] = (int) db_val('SELECT COALESCE(SUM(`tongnap`),0) FROM `users`');
    $stats['Lượt nạp']    = db_table_exists('napthe') ? (int) db_val('SELECT COUNT(*) FROM `napthe`') : 0;
    $dbErr = '';
} catch (Throwable $ex) {
    $dbErr = $ex->getMessage();
}

layout_header('Tổng quan');
?>
<h1>Tổng quan — <?= e(current_server()['name'] ?? '') ?></h1>
<?php if (!empty($dbErr)): ?>
  <div class="flash err">Lỗi DB: <?= e($dbErr) ?></div>
<?php else: ?>
<div class="card">
  <div class="grid">
    <?php foreach ($stats as $label => $n): ?>
      <div class="stat">
        <div class="n"><?= $label === 'Tổng nạp (VNĐ)' ? money($n) : money($n) ?></div>
        <div class="l"><?= e($label) ?></div>
      </div>
    <?php endforeach; ?>
  </div>
</div>
<div class="card">
  <h3 style="margin-top:0">Lối tắt</h3>
  <div class="row">
    <a class="btn" href="accounts.php">Quản lý tài khoản</a>
    <a class="btn ghost" href="giftcode.php">Tạo giftcode</a>
    <a class="btn warn" href="server_control.php">Điều khiển server</a>
  </div>
</div>
<?php endif; ?>
<?php layout_footer();
