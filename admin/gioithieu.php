<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$perPage = (int) config('per_page', 30);
$page = current_page();
$offset = ($page - 1) * $perPage;
$has = db_table_exists('gioithieu');
$total = $has ? (int) db_val('SELECT COUNT(*) FROM `gioithieu`') : 0;
$rows = $has ? db_all(
  "SELECT g.*, u1.username AS uname, u2.username AS refname
   FROM `gioithieu` g
   LEFT JOIN `users` u1 ON u1.id = g.user
   LEFT JOIN `users` u2 ON u2.id = g.user_ref
   ORDER BY g.`id` DESC LIMIT $perPage OFFSET $offset") : [];

layout_header('Giới thiệu');
?>
<h1>Giới thiệu (referral)</h1>
<?php if (!$has): ?><div class="flash err">Bảng <code>gioithieu</code> không tồn tại.</div><?php else: ?>
<div class="card">
  <table><thead><tr><th>ID</th><th>Người được GT</th><th>Người giới thiệu</th><th>IP</th><th>Thời gian</th></tr></thead>
  <tbody>
  <?php foreach ($rows as $r): ?>
    <tr><td><?= (int) $r['id'] ?></td>
    <td><?= e($r['uname'] ?? $r['user']) ?></td>
    <td><?= e($r['refname'] ?? $r['user_ref']) ?></td>
    <td class="muted"><?= e($r['ip']) ?></td><td class="muted"><?= e($r['date']) ?></td></tr>
  <?php endforeach; ?>
  <?php if (!$rows): ?><tr><td colspan="5" class="muted">Chưa có dữ liệu.</td></tr><?php endif; ?>
  </tbody></table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php endif; layout_footer();
