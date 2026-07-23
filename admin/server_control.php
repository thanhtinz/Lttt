<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$T = config('settings_table', 'settings');

/** Lấy 1 setting theo name. */
function get_setting(string $name, ?string $default = null): ?string
{
    $v = db_val('SELECT `value` FROM `' . config('settings_table', 'settings') . '` WHERE `name` = ? LIMIT 1', [$name]);
    return $v === false || $v === null ? $default : (string) $v;
}

/** Upsert 1 setting. */
function set_setting(string $name, string $value): void
{
    $t = config('settings_table', 'settings');
    $exists = db_val("SELECT `id` FROM `$t` WHERE `name` = ? LIMIT 1", [$name]);
    if ($exists !== false && $exists !== null) {
        db_exec("UPDATE `$t` SET `value` = ? WHERE `name` = ?", [$value, $name]);
    } else {
        db_exec("INSERT INTO `$t` (`name`, `value`) VALUES (?, ?)", [$name, $value]);
    }
}

/** Đổi hash_settings để báo server nạp lại (nếu server có theo dõi). */
function bump_hash(): void
{
    set_setting('hash_settings', substr(md5((string) mt_rand()), 0, 12));
}

if (!db_table_exists($T)) {
    layout_header('Điều khiển server');
    echo '<h1>Điều khiển server</h1><div class="flash err">Bảng <code>' . e($T) . '</code> không tồn tại.</div>';
    layout_footer();
    return;
}

if (is_post()) {
    csrf_check();
    $action = (string) post('action');
    try {
        switch ($action) {
            case 'maintenance':
                set_setting('bao_tri', post('bao_tri') === '1' ? 'true' : 'false');
                bump_hash();
                flash('Đã cập nhật trạng thái bảo trì.');
                break;
            case 'notify':
                set_setting('thong_bao', (string) post('thong_bao'));
                bump_hash();
                flash('Đã cập nhật thông báo in-game.');
                break;
            case 'set':
                $name = trim((string) post('name'));
                if ($name !== '') {
                    set_setting($name, (string) post('value'));
                    bump_hash();
                    flash("Đã lưu setting: $name");
                }
                break;
            case 'del':
                db_exec("DELETE FROM `$T` WHERE `id` = ?", [to_int(post('id'))]);
                flash('Đã xoá setting.');
                break;
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect(url_with([]));
}

$baoTri  = get_setting('bao_tri', 'false') === 'true';
$notify  = get_setting('thong_bao', '');
$allRows = db_all("SELECT * FROM `$T` ORDER BY `name`");

layout_header('Điều khiển server');
?>
<h1>Điều khiển server — <?= e(current_server()['name'] ?? '') ?></h1>
<div class="flash warn">Các cờ này được server đọc từ bảng <code><?= e($T) ?></code>. Hiệu lực khi server nạp lại settings (khởi động lại, hoặc nếu server có theo dõi <code>hash_settings</code>).</div>

<div class="card">
  <h3 style="margin-top:0">Bảo trì</h3>
  <p>Trạng thái hiện tại:
    <?= $baoTri ? '<span class="pill off">ĐANG BẢO TRÌ</span>' : '<span class="pill on">Đang mở</span>' ?></p>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="maintenance">
    <input type="hidden" name="bao_tri" value="<?= $baoTri ? '0' : '1' ?>">
    <button class="btn <?= $baoTri ? 'ok' : 'err' ?>" type="submit"><?= $baoTri ? 'Tắt bảo trì (mở server)' : 'Bật bảo trì' ?></button>
  </form>
</div>

<div class="card">
  <h3 style="margin-top:0">Thông báo in-game</h3>
  <form method="post">
    <?= csrf_field() ?><input type="hidden" name="action" value="notify">
    <textarea name="thong_bao" rows="2" style="width:100%"><?= e($notify) ?></textarea>
    <button class="btn" type="submit" style="margin-top:10px">Cập nhật thông báo</button>
  </form>
</div>

<div class="card">
  <h3 style="margin-top:0">Tất cả settings</h3>
  <form method="post" class="row" style="margin-bottom:12px">
    <?= csrf_field() ?><input type="hidden" name="action" value="set">
    <div><label>Tên (name)</label><input name="name" placeholder="vd: heso_exp"></div>
    <div style="flex:1"><label>Giá trị</label><input name="value" style="width:100%"></div>
    <button class="btn" type="submit">Lưu / Thêm</button>
  </form>
  <table><thead><tr><th>ID</th><th>Name</th><th>Value</th><th></th></tr></thead>
  <tbody>
  <?php foreach ($allRows as $s): ?>
    <tr><td><?= (int) $s['id'] ?></td><td><b><?= e($s['name']) ?></b></td>
    <td><?= e(mb_strimwidth((string) ($s['value'] ?? ''), 0, 80, '…')) ?></td>
    <td><form method="post" style="display:inline" onsubmit="return confirm('Xoá setting <?= e($s['name']) ?>?')">
      <?= csrf_field() ?><input type="hidden" name="action" value="del"><input type="hidden" name="id" value="<?= (int) $s['id'] ?>">
      <button class="btn err sm">Xoá</button></form></td></tr>
  <?php endforeach; ?>
  <?php if (!$allRows): ?><tr><td colspan="4" class="muted">Chưa có setting nào.</td></tr><?php endif; ?>
  </tbody></table>
</div>
<?php layout_footer();
