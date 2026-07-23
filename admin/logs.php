<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$tabs = [
  'giaodich_logs' => ['title' => 'Giao dịch', 'cols' => ['id','user','transID','type','amount','log','status','time'], 'search' => ['user','transID'], 'order' => 'id DESC'],
  'betgame'       => ['title' => 'Cược game', 'cols' => ['bet_id','user_id','game_id','bet_type','currency','bet_amount','status'], 'search' => ['user_id'], 'order' => 'bet_id DESC'],
  'atm_lichsu'    => ['title' => 'ATM', 'cols' => null, 'search' => [], 'order' => '1'],
];
$cur = (string) param('t', 'giaodich_logs');
if (!isset($tabs[$cur])) { $cur = 'giaodich_logs'; }
$cfg = $tabs[$cur];
$has = db_table_exists($cur);

// tự lấy cột nếu chưa khai báo
$cols = $cfg['cols'];
if ($has && !$cols) {
    $cols = array_map(fn($r) => $r['Field'], db_all("SHOW COLUMNS FROM `$cur`"));
}
$perPage = (int) config('per_page', 30);
$page = current_page();
$offset = ($page - 1) * $perPage;
$q = trim((string) param('q', ''));
$where = ''; $args = [];
if ($has && $q !== '' && !empty($cfg['search'])) {
    $p = [];
    foreach ($cfg['search'] as $c) { $p[] = "`$c` LIKE ?"; $args[] = '%'.$q.'%'; }
    $where = 'WHERE ' . implode(' OR ', $p);
}
$order = $cfg['order'] ?? '1';
$total = $has ? (int) db_val("SELECT COUNT(*) FROM `$cur` $where", $args) : 0;
$rows  = $has ? db_all("SELECT * FROM `$cur` $where ORDER BY $order LIMIT $perPage OFFSET $offset", $args) : [];

layout_header('Nhật ký');
?>
<h1>Nhật ký</h1>
<div class="card">
  <div class="row">
    <?php foreach ($tabs as $k => $v): ?>
      <a class="btn <?= $k === $cur ? '' : 'ghost' ?>" href="?t=<?= e($k) ?>"><?= e($v['title']) ?></a>
    <?php endforeach; ?>
  </div>
</div>
<?php if (!$has): ?><div class="flash err">Bảng <code><?= e($cur) ?></code> không tồn tại.</div><?php else: ?>
<div class="card">
  <?php if (!empty($cfg['search'])): ?>
  <form method="get" class="row" style="margin-bottom:12px"><input type="hidden" name="t" value="<?= e($cur) ?>">
    <div><label>Tìm</label><input name="q" value="<?= e($q) ?>"></div><button class="btn">Tìm</button></form>
  <?php endif; ?>
  <table><thead><tr><?php foreach ($cols as $c): ?><th><?= e($c) ?></th><?php endforeach; ?></tr></thead>
  <tbody>
  <?php foreach ($rows as $r): ?>
    <tr><?php foreach ($cols as $c): ?><td><?= e(mb_strimwidth((string)($r[$c] ?? ''),0,60,'…')) ?></td><?php endforeach; ?></tr>
  <?php endforeach; ?>
  <?php if (!$rows): ?><tr><td colspan="<?= count($cols) ?>" class="muted">Trống.</td></tr><?php endif; ?>
  </tbody></table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php endif; layout_footer();
