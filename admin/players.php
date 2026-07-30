<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

if (is_post()) {
    csrf_check();
    $id     = to_int(post('id'));
    $action = (string) post('action');
    try {
        if ($action === 'delete') {
            db_exec('DELETE FROM `players` WHERE `id` = ?', [$id]);
            flash("Đã xoá nhân vật #$id");
        } elseif ($action === 'save') {
            db_exec(
                'UPDATE `players` SET `xu` = ?, `luong` = ?, `xeng` = ?, `scores` = ?, `level_main` = ? WHERE `id` = ?',
                [to_int(post('xu')), to_int(post('luong')), to_int(post('xeng')),
                 to_int(post('scores')), to_int(post('level_main')), $id]
            );
            flash("Đã lưu nhân vật #$id");
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect(url_with([]));
}

$q       = trim((string) param('q', ''));
$perPage = (int) config('per_page', 30);
$page    = current_page();
$offset  = ($page - 1) * $perPage;

$where = '';
$args  = [];
if ($q !== '') {
    $where = 'WHERE u.`username` LIKE ? OR p.`id` = ?';
    $args  = ['%' . $q . '%', to_int($q)];
}
$total = (int) db_val("SELECT COUNT(*) FROM `players` p LEFT JOIN `users` u ON u.id = p.user_id $where", $args);
$rows  = db_all(
    "SELECT p.*, u.username FROM `players` p LEFT JOIN `users` u ON u.id = p.user_id
     $where ORDER BY p.`id` DESC LIMIT $perPage OFFSET $offset", $args);

$edit = isset($_GET['edit']) ? db_one('SELECT p.*, u.username FROM `players` p LEFT JOIN `users` u ON u.id=p.user_id WHERE p.id=?', [to_int($_GET['edit'])]) : null;

layout_header('Nhân vật');
?>
<h1>Nhân vật</h1>
<?php if ($edit): ?>
<div class="card">
  <h3 style="margin-top:0">Sửa nhân vật #<?= (int) $edit['id'] ?> — <?= e($edit['username'] ?? '') ?></h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="save"><input type="hidden" name="id" value="<?= (int) $edit['id'] ?>">
    <div><label>Vàng (xu)</label><input name="xu" value="<?= (int) $edit['xu'] ?>"></div>
    <div><label>VIP (lượng)</label><input name="luong" value="<?= (int) $edit['luong'] ?>"></div>
    <div><label>Xèng</label><input name="xeng" value="<?= (int) $edit['xeng'] ?>"></div>
    <div><label>Điểm</label><input name="scores" value="<?= (int) $edit['scores'] ?>"></div>
    <div><label>Level</label><input name="level_main" value="<?= (int) ($edit['level_main'] ?? 0) ?>"></div>
    <button class="btn" type="submit">Lưu</button>
    <a class="btn ghost" href="players.php">Huỷ</a>
  </form>
</div>
<?php endif; ?>
<div class="card">
  <form method="get" class="row">
    <div><label>Tìm theo tên TK / ID nhân vật</label><input name="q" value="<?= e($q) ?>"></div>
    <button class="btn" type="submit">Tìm</button>
    <?php if ($q !== ''): ?><a class="btn ghost" href="players.php">Xoá lọc</a><?php endif; ?>
  </form>
</div>
<div class="card">
  <table>
    <thead><tr><th>ID</th><th>Tài khoản</th><th>Online</th><th>Level</th><th>Vàng</th><th>VIP</th><th>Điểm</th><th>Clan</th><th>Online cuối</th><th></th></tr></thead>
    <tbody>
    <?php foreach ($rows as $p): ?>
      <tr>
        <td><?= (int) $p['id'] ?></td>
        <td><?= e($p['username'] ?? '—') ?></td>
        <td><?= (int) $p['is_online'] ? '🟢' : '🔴' ?></td>
        <td><?= (int) ($p['level_main'] ?? 0) ?></td>
        <td><?= money($p['xu'] ?? 0) ?></td>
        <td><?= money($p['luong'] ?? 0) ?></td>
        <td><?= money($p['scores'] ?? 0) ?></td>
        <td><?= (int) ($p['clan_id'] ?? 0) ?></td>
        <td class="muted"><?= e($p['last_online'] ?? '') ?></td>
        <td>
          <div class="row" style="gap:5px">
            <a class="btn sm" href="<?= e(url_with(['edit' => (int) $p['id']])) ?>">Sửa</a>
            <form method="post" style="display:inline" onsubmit="return confirm('Xoá nhân vật #<?= (int) $p['id'] ?>?')">
              <?= csrf_field() ?><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<?= (int) $p['id'] ?>">
              <button class="btn err sm" type="submit">Xoá</button>
            </form>
          </div>
        </td>
      </tr>
    <?php endforeach; ?>
    <?php if (!$rows): ?><tr><td colspan="10" class="muted">Không có kết quả.</td></tr><?php endif; ?>
    </tbody>
  </table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php layout_footer();
