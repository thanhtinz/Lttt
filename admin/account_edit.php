<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$id = to_int(param('id'));
if ($id <= 0) {
    redirect('accounts.php');
}

if (is_post()) {
    csrf_check();
    try {
        $action = (string) post('action');
        if ($action === 'add_currency') {
            $vnd    = to_int(post('vnd'));
            $xu     = to_int(post('xu'));
            $luong  = to_int(post('luong'));
            $xeng   = to_int(post('xeng'));
            $scores = to_int(post('scores'));
            if ($vnd) {
                db_exec('UPDATE `users` SET `vnd` = `vnd` + ? WHERE `id` = ?', [$vnd, $id]);
            }
            $sets = [];
            $args = [];
            if ($xu)     { $sets[] = '`xu` = `xu` + ?';         $args[] = $xu; }
            if ($luong)  { $sets[] = '`luong` = `luong` + ?';   $args[] = $luong; }
            if ($xeng)   { $sets[] = '`xeng` = `xeng` + ?';     $args[] = $xeng; }
            if ($scores) { $sets[] = '`scores` = `scores` + ?'; $args[] = $scores; }
            if ($sets) {
                $args[] = $id;
                db_exec('UPDATE `players` SET ' . implode(', ', $sets) . ' WHERE `user_id` = ?', $args);
            }
            flash('Đã cập nhật tài sản cho tài khoản #' . $id);
        } elseif ($action === 'save_user') {
            db_exec('UPDATE `users` SET `gmail` = ?, `phone` = ? WHERE `id` = ?',
                [(string) post('gmail', ''), (string) post('phone', ''), $id]);
            flash('Đã lưu thông tin tài khoản.');
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect('account_edit.php?id=' . $id);
}

$u = db_one('SELECT * FROM `users` WHERE `id` = ?', [$id]);
if (!$u) {
    flash('Không tìm thấy tài khoản.', 'err');
    redirect('accounts.php');
}
$p = db_one('SELECT * FROM `players` WHERE `user_id` = ?', [$id]);

layout_header('Sửa tài khoản #' . $id);
?>
<h1>Tài khoản: <?= e($u['username']) ?> <span class="muted">#<?= (int) $u['id'] ?></span></h1>

<div class="card">
  <h3 style="margin-top:0">Cộng tài sản</h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="add_currency">
    <div><label>VNĐ (tài khoản)</label><input name="vnd" value="0"></div>
    <div><label>Vàng / Xu</label><input name="xu" value="0"></div>
    <div><label>VIP / Lượng</label><input name="luong" value="0"></div>
    <div><label>Xèng</label><input name="xeng" value="0"></div>
    <div><label>Điểm (scores)</label><input name="scores" value="0"></div>
    <button class="btn" type="submit">Cộng</button>
  </form>
  <p class="muted" style="margin-bottom:0">Nhập số âm để trừ. Vàng/VIP/Xèng/Điểm áp dụng lên nhân vật của tài khoản.</p>
</div>

<div class="card">
  <h3 style="margin-top:0">Thông tin tài khoản</h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="save_user">
    <div><label>Gmail</label><input name="gmail" value="<?= e($u['gmail'] ?? '') ?>"></div>
    <div><label>SĐT</label><input name="phone" value="<?= e($u['phone'] ?? '') ?>"></div>
    <button class="btn" type="submit">Lưu</button>
  </form>
  <table style="margin-top:14px">
    <tr><th>VNĐ</th><td><?= money($u['vnd'] ?? 0) ?></td><th>Tổng nạp</th><td><?= money($u['tongnap'] ?? 0) ?></td></tr>
    <tr><th>Quyền (role)</th><td><?= (int) ($u['role'] ?? -1) ?></td><th>Kích hoạt</th><td><?= (int) ($u['active'] ?? 0) ?></td></tr>
  </table>
</div>

<div class="card">
  <h3 style="margin-top:0">Nhân vật</h3>
  <?php if ($p): ?>
    <table>
      <tr><th>ID NV</th><td><?= (int) $p['id'] ?></td><th>Online</th><td><?= (int) $p['is_online'] ? '🟢' : '🔴' ?></td></tr>
      <tr><th>Vàng (xu)</th><td><?= money($p['xu'] ?? 0) ?></td><th>VIP (lượng)</th><td><?= money($p['luong'] ?? 0) ?></td></tr>
      <tr><th>Xèng</th><td><?= money($p['xeng'] ?? 0) ?></td><th>Điểm</th><td><?= money($p['scores'] ?? 0) ?></td></tr>
      <tr><th>Level</th><td><?= (int) ($p['level_main'] ?? 0) ?></td><th>Clan</th><td><?= (int) ($p['clan_id'] ?? 0) ?></td></tr>
      <tr><th>Online lần cuối</th><td colspan="3"><?= e($p['last_online'] ?? '') ?></td></tr>
    </table>
    <p style="margin-bottom:0"><a class="btn ghost" href="players.php?q=<?= (int) $p['id'] ?>">Xem chi tiết nhân vật</a></p>
  <?php else: ?>
    <p class="muted">Tài khoản này chưa có nhân vật.</p>
  <?php endif; ?>
</div>
<p><a href="accounts.php">← Về danh sách</a></p>
<?php layout_footer();
