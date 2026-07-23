<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

if (is_post()) {
    csrf_check();
    $action = (string) post('action');
    $id     = to_int(post('id'));
    try {
        if ($action === 'delete') {
            db_exec('DELETE FROM `clan_members` WHERE `clan_id` = ?', [$id]);
            db_exec('DELETE FROM `clans` WHERE `id` = ?', [$id]);
            flash("Đã xoá bang #$id và thành viên.");
        } elseif ($action === 'save') {
            db_exec('UPDATE `clans` SET `name` = ?, `icon` = ?, `max_members` = ?, `description` = ? WHERE `id` = ?',
                [(string) post('name'), (string) post('icon'), to_int(post('max_members'), 30), (string) post('description'), $id]);
            flash("Đã lưu bang #$id");
        } elseif ($action === 'kick') {
            db_exec('DELETE FROM `clan_members` WHERE `id` = ?', [to_int(post('member_id'))]);
            flash('Đã xoá thành viên khỏi bang.');
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect(url_with([]));
}

// ---- Xem thành viên ----
if (isset($_GET['members'])) {
    $cid  = to_int($_GET['members']);
    $clan = db_one('SELECT * FROM `clans` WHERE `id` = ?', [$cid]);
    $mems = db_all(
        'SELECT m.*, u.username FROM `clan_members` m LEFT JOIN `users` u ON u.id = m.user_id
         WHERE m.clan_id = ? ORDER BY m.accept DESC, m.points DESC', [$cid]);
    layout_header('Thành viên bang');
    ?>
    <h1>Thành viên: <?= e($clan['name'] ?? ('#' . $cid)) ?></h1>
    <p><a href="clans.php">← Về danh sách bang</a></p>
    <div class="card">
      <table><thead><tr><th>ID</th><th>Tài khoản</th><th>Chức</th><th>Điểm</th><th>Xu</th><th>Lượng</th><th>Duyệt</th><th></th></tr></thead>
      <tbody>
      <?php foreach ($mems as $m): ?>
        <tr><td><?= (int) $m['user_id'] ?></td><td><?= e($m['username'] ?? '—') ?></td>
        <td><?= e($m['role'] ?? '') ?></td><td><?= money($m['points'] ?? 0) ?></td>
        <td><?= money($m['xu'] ?? 0) ?></td><td><?= money($m['luong'] ?? 0) ?></td>
        <td><?= (int) ($m['accept'] ?? 0) === 1 ? '✅' : '⏳' ?></td>
        <td><form method="post" style="display:inline" onsubmit="return confirm('Xoá thành viên này?')">
          <?= csrf_field() ?><input type="hidden" name="action" value="kick"><input type="hidden" name="member_id" value="<?= (int) $m['id'] ?>">
          <button class="btn err sm">Xoá</button></form></td></tr>
      <?php endforeach; ?>
      <?php if (!$mems): ?><tr><td colspan="8" class="muted">Bang chưa có thành viên.</td></tr><?php endif; ?>
      </tbody></table>
    </div>
    <?php layout_footer();
    return;
}

$q       = trim((string) param('q', ''));
$perPage = (int) config('per_page', 30);
$page    = current_page();
$offset  = ($page - 1) * $perPage;
$where = ''; $args = [];
if ($q !== '') { $where = 'WHERE c.`name` LIKE ? OR c.`id` = ?'; $args = ['%' . $q . '%', to_int($q)]; }
$total = (int) db_val("SELECT COUNT(*) FROM `clans` c $where", $args);
$rows  = db_all(
    "SELECT c.*, u.username AS owner,
        (SELECT COUNT(*) FROM `clan_members` m WHERE m.clan_id = c.id AND m.accept = 1) AS members
     FROM `clans` c LEFT JOIN `users` u ON u.id = c.users_id
     $where ORDER BY c.`id` DESC LIMIT $perPage OFFSET $offset", $args);
$edit = isset($_GET['edit']) ? db_one('SELECT * FROM `clans` WHERE `id` = ?', [to_int($_GET['edit'])]) : null;

layout_header('Bang hội');
?>
<h1>Bang hội</h1>
<?php if ($edit): ?>
<div class="card">
  <h3 style="margin-top:0">Sửa bang #<?= (int) $edit['id'] ?></h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="save"><input type="hidden" name="id" value="<?= (int) $edit['id'] ?>">
    <div><label>Tên</label><input name="name" value="<?= e($edit['name']) ?>"></div>
    <div><label>Icon</label><input name="icon" value="<?= e($edit['icon'] ?? '') ?>"></div>
    <div><label>Số TV tối đa</label><input name="max_members" value="<?= (int) ($edit['max_members'] ?? 30) ?>"></div>
    <div><label>Mô tả</label><input name="description" value="<?= e($edit['description'] ?? '') ?>"></div>
    <button class="btn">Lưu</button><a class="btn ghost" href="clans.php">Huỷ</a>
  </form>
</div>
<?php endif; ?>
<div class="card">
  <form method="get" class="row"><div><label>Tìm bang</label><input name="q" value="<?= e($q) ?>"></div>
  <button class="btn">Tìm</button><?php if ($q !== ''): ?><a class="btn ghost" href="clans.php">Xoá lọc</a><?php endif; ?></form>
</div>
<div class="card">
  <table><thead><tr><th>ID</th><th>Tên</th><th>Chủ bang</th><th>TV</th><th>Xu</th><th>Lượng</th><th>Tạo lúc</th><th></th></tr></thead>
  <tbody>
  <?php foreach ($rows as $c): ?>
    <tr><td><?= (int) $c['id'] ?></td><td><?= e($c['name']) ?></td><td><?= e($c['owner'] ?? '—') ?></td>
    <td><?= (int) $c['members'] ?>/<?= (int) ($c['max_members'] ?? 30) ?></td>
    <td><?= money($c['xu'] ?? 0) ?></td><td><?= money($c['luong'] ?? 0) ?></td>
    <td class="muted"><?= e($c['created_at'] ?? '') ?></td>
    <td><div class="row" style="gap:5px">
      <a class="btn sm" href="?members=<?= (int) $c['id'] ?>">Thành viên</a>
      <a class="btn sm ghost" href="<?= e(url_with(['edit' => (int) $c['id']])) ?>">Sửa</a>
      <form method="post" style="display:inline" onsubmit="return confirm('Xoá bang #<?= (int) $c['id'] ?>?')">
        <?= csrf_field() ?><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<?= (int) $c['id'] ?>">
        <button class="btn err sm">Xoá</button></form>
    </div></td></tr>
  <?php endforeach; ?>
  <?php if (!$rows): ?><tr><td colspan="8" class="muted">Không có bang.</td></tr><?php endif; ?>
  </tbody></table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php layout_footer();
