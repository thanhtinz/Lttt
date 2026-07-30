<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$roleCol = config('admin_role_column', 'role');
$lockCol = config('lock_column', 'login_lock');
$minRole = (int) config('admin_min_role', 1);

// ----- Xử lý hành động -----
if (is_post()) {
    csrf_check();
    $id     = to_int(post('id'));
    $action = (string) post('action');
    try {
        switch ($action) {
            case 'lock':
                db_exec("UPDATE `users` SET `$lockCol` = 1 WHERE `id` = ?", [$id]);
                flash("Đã khoá tài khoản #$id");
                break;
            case 'unlock':
                db_exec("UPDATE `users` SET `$lockCol` = 0 WHERE `id` = ?", [$id]);
                flash("Đã mở khoá tài khoản #$id");
                break;
            case 'grant_admin':
                db_exec("UPDATE `users` SET `$roleCol` = ? WHERE `id` = ?", [$minRole, $id]);
                flash("Đã cấp admin cho #$id");
                break;
            case 'revoke_admin':
                db_exec("UPDATE `users` SET `$roleCol` = -1 WHERE `id` = ?", [$id]);
                flash("Đã gỡ admin của #$id");
                break;
            case 'activate':
                db_exec("UPDATE `users` SET `active` = 1 WHERE `id` = ?", [$id]);
                flash("Đã kích hoạt #$id");
                break;
            case 'add_vnd':
                $amt = to_int(post('amount'));
                db_exec("UPDATE `users` SET `vnd` = `vnd` + ? WHERE `id` = ?", [$amt, $id]);
                flash("Đã cộng " . money($amt) . " VNĐ cho #$id");
                break;
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect(url_with([]));
}

// ----- Tìm kiếm + phân trang -----
$q       = trim((string) param('q', ''));
$perPage = (int) config('per_page', 30);
$page    = current_page();
$offset  = ($page - 1) * $perPage;

$where = '';
$args  = [];
if ($q !== '') {
    $where = 'WHERE `username` LIKE ? OR `id` = ?';
    $args  = ['%' . $q . '%', to_int($q)];
}

$total = (int) db_val("SELECT COUNT(*) FROM `users` $where", $args);
$rows  = db_all("SELECT * FROM `users` $where ORDER BY `id` DESC LIMIT $perPage OFFSET $offset", $args);

layout_header('Tài khoản');
?>
<h1>Tài khoản</h1>
<div class="card">
  <form method="get" class="row">
    <div><label>Tìm theo tên / ID</label><input name="q" value="<?= e($q) ?>" placeholder="username hoặc id"></div>
    <button class="btn" type="submit">Tìm</button>
    <?php if ($q !== ''): ?><a class="btn ghost" href="accounts.php">Xoá lọc</a><?php endif; ?>
  </form>
</div>
<div class="card">
  <table>
    <thead><tr>
      <th>ID</th><th>Tài khoản</th><th>Quyền</th><th>VNĐ</th><th>Tổng nạp</th>
      <th>Kích hoạt</th><th>Trạng thái</th><th>Hành động</th>
    </tr></thead>
    <tbody>
    <?php foreach ($rows as $u):
        $isAdmin  = (int) ($u[$roleCol] ?? -1) >= $minRole;
        $isLocked = (int) ($u[$lockCol] ?? 0) === 1;
    ?>
      <tr>
        <td><?= (int) $u['id'] ?></td>
        <td><a href="account_edit.php?id=<?= (int) $u['id'] ?>"><?= e($u['username']) ?></a></td>
        <td><?= $isAdmin ? '<span class="pill on">Admin</span>' : '<span class="muted">Thường</span>' ?></td>
        <td><?= money($u['vnd'] ?? 0) ?></td>
        <td><?= money($u['tongnap'] ?? 0) ?></td>
        <td><?= (int) ($u['active'] ?? 0) === 1 ? '✅' : '—' ?></td>
        <td><?= $isLocked ? '<span class="pill off">Khoá</span>' : '<span class="pill on">OK</span>' ?></td>
        <td>
          <div class="row" style="gap:5px">
            <?php if ($isLocked): ?>
              <?= act_btn($u['id'], 'unlock', 'Mở khoá', 'ok sm') ?>
            <?php else: ?>
              <?= act_btn($u['id'], 'lock', 'Khoá', 'err sm') ?>
            <?php endif; ?>
            <?php if ($isAdmin): ?>
              <?= act_btn($u['id'], 'revoke_admin', 'Gỡ admin', 'ghost sm') ?>
            <?php else: ?>
              <?= act_btn($u['id'], 'grant_admin', 'Cấp admin', 'ghost sm') ?>
            <?php endif; ?>
            <a class="btn sm" href="account_edit.php?id=<?= (int) $u['id'] ?>">Sửa</a>
          </div>
        </td>
      </tr>
    <?php endforeach; ?>
    <?php if (!$rows): ?><tr><td colspan="8" class="muted">Không có kết quả.</td></tr><?php endif; ?>
    </tbody>
  </table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php
layout_footer();

/** Nút hành động dạng form POST (kèm CSRF). */
function act_btn(int $id, string $action, string $label, string $cls, string $confirm = ''): string
{
    $c = $confirm ? ' onsubmit="return confirm(\'' . e($confirm) . '\')"' : '';
    return '<form method="post" style="display:inline"' . $c . '>' . csrf_field()
        . '<input type="hidden" name="id" value="' . (int) $id . '">'
        . '<input type="hidden" name="action" value="' . e($action) . '">'
        . '<button class="btn ' . e($cls) . '" type="submit">' . e($label) . '</button></form>';
}
