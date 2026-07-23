<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$q = trim((string) param('q', ''));
$perPage = (int) config('per_page', 30);
$page = current_page();
$offset = ($page - 1) * $perPage;

$has = db_table_exists('napthe');
$where = ''; $args = [];
if ($has && $q !== '') {
    $where = 'WHERE `user_nap` LIKE ? OR `serial` LIKE ? OR `code` LIKE ?';
    $args = ['%' . $q . '%', '%' . $q . '%', '%' . $q . '%'];
}
$total = $has ? (int) db_val("SELECT COUNT(*) FROM `napthe` $where", $args) : 0;
$sum   = $has ? (int) db_val("SELECT COALESCE(SUM(`amount`),0) FROM `napthe` $where", $args) : 0;
$rows  = $has ? db_all("SELECT * FROM `napthe` $where ORDER BY `id` DESC LIMIT $perPage OFFSET $offset", $args) : [];

layout_header('Nạp thẻ');
?>
<h1>Nạp thẻ</h1>
<?php if (!$has): ?><div class="flash err">Bảng <code>napthe</code> không tồn tại.</div><?php else: ?>
<div class="card"><div class="grid">
  <div class="stat"><div class="n"><?= money($total) ?></div><div class="l">Lượt nạp</div></div>
  <div class="stat"><div class="n"><?= money($sum) ?></div><div class="l">Tổng tiền (lọc hiện tại)</div></div>
</div></div>
<div class="card">
  <form method="get" class="row"><div><label>Tìm (user / serial / code)</label><input name="q" value="<?= e($q) ?>"></div>
  <button class="btn">Tìm</button><?php if ($q !== ''): ?><a class="btn ghost" href="recharge.php">Xoá lọc</a><?php endif; ?></form>
</div>
<div class="card">
  <table><thead><tr><th>ID</th><th>Người nạp</th><th>Nhà mạng</th><th>Serial</th><th>Mã thẻ</th><th>Mệnh giá</th><th>Trạng thái</th><th>Thời gian</th></tr></thead>
  <tbody>
  <?php foreach ($rows as $r): ?>
    <tr><td><?= (int) $r['id'] ?></td><td><?= e($r['user_nap']) ?></td><td><?= e($r['telco']) ?></td>
    <td><?= e($r['serial']) ?></td><td><?= e($r['code']) ?></td><td><?= money($r['amount']) ?></td>
    <td><?= e($r['status']) ?></td><td class="muted"><?= e($r['created_at']) ?></td></tr>
  <?php endforeach; ?>
  <?php if (!$rows): ?><tr><td colspan="8" class="muted">Chưa có lượt nạp.</td></tr><?php endif; ?>
  </tbody></table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php endif; layout_footer();
