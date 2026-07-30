<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$T = config('settings_table', 'settings');

function get_setting(string $name, ?string $default = null): ?string
{
    $v = db_val('SELECT `value` FROM `' . config('settings_table', 'settings') . '` WHERE `name` = ? LIMIT 1', [$name]);
    return $v === false || $v === null ? $default : (string) $v;
}

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

/** Đổi hash_settings để watcher server nạp lại (chỉnh là chạy). */
function bump_hash(): void
{
    set_setting('hash_settings', substr(md5((string) mt_rand()) . microtime(), 0, 16));
}

/** Gửi lệnh một-lần cho server (server watcher đọc & thực thi rồi xoá). */
function send_command(string $cmd): void
{
    set_setting('cmd', $cmd);
    bump_hash();
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
            case 'exp':
                $rate = (float) post('heso_exp', 1);
                if ($rate <= 0) { $rate = 1; }
                set_setting('heso_exp', (string) $rate);
                bump_hash();
                flash('Đã đặt hệ số EXP = x' . $rate);
                break;
            case 'broadcast':
                $msg = trim((string) post('message'));
                if ($msg !== '') {
                    send_command('broadcast:' . $msg);
                    flash('Đã gửi thông báo tới tất cả người chơi.');
                }
                break;
            case 'reset_boss':
                send_command('reset_boss');
                flash('Đã gửi lệnh reset boss.');
                break;
            case 'restart':
                send_command('restart');
                flash('Đã gửi lệnh khởi động lại server.', 'warn');
                break;
            case 'set':
                $name = trim((string) post('name'));
                if ($name !== '') { set_setting($name, (string) post('value')); bump_hash(); flash("Đã lưu setting: $name"); }
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
$expRate = (float) (get_setting('heso_exp', '1') ?: 1);
$hb      = (int) (get_setting('heartbeat', '0') ?: 0);
$startTs = (int) (get_setting('server_start', '0') ?: 0);
$now     = (int) (microtime(true) * 1000);
$alive   = $hb > 0 && ($now - $hb) < 20000; // heartbeat < 20s
$uptime  = $startTs > 0 ? $now - $startTs : 0;

function fmt_uptime(int $ms): string
{
    if ($ms <= 0) { return '—'; }
    $s = intdiv($ms, 1000);
    $d = intdiv($s, 86400); $s %= 86400;
    $h = intdiv($s, 3600);  $s %= 3600;
    $m = intdiv($s, 60);
    return ($d ? "{$d}d " : '') . sprintf('%02d:%02d', $h, $m);
}

$allRows = db_all("SELECT * FROM `$T` ORDER BY `name`");

layout_header('Điều khiển server');
?>
<h1>Điều khiển server — <?= e(current_server()['name'] ?? '') ?></h1>
<div class="flash <?= $alive ? 'ok' : 'warn' ?>">
  Server có watcher đọc bảng <code><?= e($T) ?></code> mỗi 5s → các thay đổi bên dưới
  <b>áp dụng ngay không cần restart</b> (khi server đang chạy bản có SettingsWatcher).
</div>

<div class="card">
  <h3 style="margin-top:0">Trạng thái sống</h3>
  <div class="grid">
    <div class="stat"><div class="n"><?= $alive ? '🟢 Online' : '🔴 Offline' ?></div><div class="l">Heartbeat <?= $hb ? '(' . max(0, intdiv($now - $hb, 1000)) . 's trước)' : '' ?></div></div>
    <div class="stat"><div class="n"><?= e(fmt_uptime($uptime)) ?></div><div class="l">Uptime</div></div>
    <div class="stat"><div class="n">x<?= e(rtrim(rtrim(number_format($expRate, 2), '0'), '.')) ?></div><div class="l">Hệ số EXP</div></div>
    <div class="stat"><div class="n"><?= $baoTri ? 'BẢO TRÌ' : 'Mở' ?></div><div class="l">Trạng thái</div></div>
  </div>
</div>

<div class="card">
  <h3 style="margin-top:0">Bảo trì</h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="maintenance"><input type="hidden" name="bao_tri" value="<?= $baoTri ? '0' : '1' ?>">
    <button class="btn <?= $baoTri ? 'ok' : 'err' ?>" type="submit"><?= $baoTri ? 'Tắt bảo trì (mở server)' : 'Bật bảo trì' ?></button>
  </form>
</div>

<div class="card">
  <h3 style="margin-top:0">Hệ số EXP</h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="exp">
    <div><label>Hệ số (x)</label><input name="heso_exp" type="number" step="0.1" min="0.1" value="<?= e($expRate) ?>"></div>
    <button class="btn" type="submit">Đặt hệ số EXP</button>
  </form>
</div>

<div class="card">
  <h3 style="margin-top:0">Thông báo tới tất cả (in-game, gửi ngay)</h3>
  <form method="post" class="row">
    <?= csrf_field() ?><input type="hidden" name="action" value="broadcast">
    <div style="flex:1"><label>Nội dung</label><input name="message" style="width:100%" placeholder="Sự kiện nhân đôi EXP bắt đầu!"></div>
    <button class="btn ok" type="submit">Gửi ngay</button>
  </form>
  <hr style="border-color:#26304d;margin:14px 0">
  <h4 style="margin:0 0 8px">Thông báo/news (lưu, hiện khi đăng nhập)</h4>
  <form method="post">
    <?= csrf_field() ?><input type="hidden" name="action" value="notify">
    <textarea name="thong_bao" rows="2" style="width:100%"><?= e($notify) ?></textarea>
    <button class="btn" type="submit" style="margin-top:10px">Cập nhật</button>
  </form>
</div>

<div class="card">
  <h3 style="margin-top:0">Lệnh nhanh</h3>
  <div class="row">
    <form method="post" onsubmit="return confirm('Reset boss trên các map?')"><?= csrf_field() ?><input type="hidden" name="action" value="reset_boss"><button class="btn warn">Reset Boss</button></form>
    <form method="post" onsubmit="return confirm('KHỞI ĐỘNG LẠI server? Người chơi sẽ bị ngắt.')"><?= csrf_field() ?><input type="hidden" name="action" value="restart"><button class="btn err">Khởi động lại server</button></form>
  </div>
  <p class="muted" style="margin-bottom:0">Lệnh được server watcher đọc & thực thi trong ~5s.</p>
</div>

<div class="card">
  <h3 style="margin-top:0">Tất cả settings</h3>
  <form method="post" class="row" style="margin-bottom:12px">
    <?= csrf_field() ?><input type="hidden" name="action" value="set">
    <div><label>Tên</label><input name="name"></div>
    <div style="flex:1"><label>Giá trị</label><input name="value" style="width:100%"></div>
    <button class="btn" type="submit">Lưu / Thêm</button>
  </form>
  <table><thead><tr><th>ID</th><th>Name</th><th>Value</th><th></th></tr></thead>
  <tbody>
  <?php foreach ($allRows as $s): ?>
    <tr><td><?= (int) $s['id'] ?></td><td><b><?= e($s['name']) ?></b></td>
    <td><?= e(mb_strimwidth((string) ($s['value'] ?? ''), 0, 80, '…')) ?></td>
    <td><form method="post" style="display:inline" onsubmit="return confirm('Xoá <?= e($s['name']) ?>?')">
      <?= csrf_field() ?><input type="hidden" name="action" value="del"><input type="hidden" name="id" value="<?= (int) $s['id'] ?>">
      <button class="btn err sm">Xoá</button></form></td></tr>
  <?php endforeach; ?>
  <?php if (!$allRows): ?><tr><td colspan="4" class="muted">Chưa có setting.</td></tr><?php endif; ?>
  </tbody></table>
</div>
<?php layout_footer();
