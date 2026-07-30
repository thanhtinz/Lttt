<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

$testResult = null;

if (is_post()) {
    csrf_check();
    $action  = (string) post('action');
    $servers = servers_all();
    try {
        if ($action === 'save') {
            $entry = [
                'id'       => trim((string) post('id')) ?: ('sv_' . substr(md5((string) mt_rand()), 0, 6)),
                'name'     => trim((string) post('name')) ?: 'Máy chủ',
                'host'     => trim((string) post('host')) ?: '127.0.0.1',
                'port'     => to_int(post('port'), 3306),
                'dbname'   => trim((string) post('dbname')),
                'username' => (string) post('username'),
                'password' => (string) post('password'),
            ];
            $found = false;
            foreach ($servers as &$s) {
                if ($s['id'] === $entry['id']) { $s = $entry; $found = true; break; }
            }
            unset($s);
            if (!$found) { $servers[] = $entry; }
            servers_save($servers);
            flash('Đã lưu máy chủ: ' . $entry['name']);
        } elseif ($action === 'delete') {
            $id = (string) post('id');
            if (count($servers) <= 1) {
                throw new RuntimeException('Phải còn ít nhất 1 máy chủ.');
            }
            $servers = array_values(array_filter($servers, fn($s) => $s['id'] !== $id));
            servers_save($servers);
            flash('Đã xoá máy chủ.');
        } elseif ($action === 'test') {
            $s = server_by_id((string) post('id'));
            if ($s) {
                [$ok, $msg] = server_test($s);
                flash(($ok ? '✅ ' : '❌ ') . $s['name'] . ': ' . $msg, $ok ? 'ok' : 'err');
            }
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect(url_with([]));
}

$servers = servers_all();
$edit    = null;
if (isset($_GET['edit'])) {
    $edit = server_by_id((string) $_GET['edit']);
}

layout_header('Máy chủ QL');
?>
<h1>Máy chủ QL</h1>
<div class="card">
  <h3 style="margin-top:0"><?= $edit ? 'Sửa máy chủ' : 'Thêm máy chủ' ?></h3>
  <form method="post">
    <?= csrf_field() ?><input type="hidden" name="action" value="save">
    <div class="row">
      <div><label>ID (khoá, không dấu)</label><input name="id" value="<?= e($edit['id'] ?? '') ?>" <?= $edit ? 'readonly' : '' ?> placeholder="vd: sv2"></div>
      <div><label>Tên hiển thị</label><input name="name" value="<?= e($edit['name'] ?? '') ?>"></div>
      <div><label>Host</label><input name="host" value="<?= e($edit['host'] ?? '127.0.0.1') ?>"></div>
      <div><label>Port</label><input name="port" type="number" value="<?= e($edit['port'] ?? 3306) ?>"></div>
    </div>
    <div class="row">
      <div><label>Database</label><input name="dbname" value="<?= e($edit['dbname'] ?? 'avatar_2x') ?>"></div>
      <div><label>Username</label><input name="username" value="<?= e($edit['username'] ?? 'root') ?>"></div>
      <div><label>Password</label><input name="password" type="password" value="<?= e($edit['password'] ?? '') ?>"></div>
      <button class="btn" type="submit"><?= $edit ? 'Lưu' : 'Thêm' ?></button>
      <?php if ($edit): ?><a class="btn ghost" href="manage_servers.php">Huỷ</a><?php endif; ?>
    </div>
  </form>
</div>
<div class="card">
  <table><thead><tr><th>ID</th><th>Tên</th><th>Host:Port</th><th>Database</th><th>User</th><th></th></tr></thead>
  <tbody>
  <?php foreach ($servers as $s): ?>
    <tr>
      <td><?= e($s['id']) ?><?= (current_server()['id'] ?? '') === $s['id'] ? ' <span class="pill on">đang dùng</span>' : '' ?></td>
      <td><?= e($s['name']) ?></td>
      <td><?= e($s['host']) ?>:<?= (int) $s['port'] ?></td>
      <td><?= e($s['dbname']) ?></td>
      <td><?= e($s['username']) ?></td>
      <td><div class="row" style="gap:5px">
        <form method="post" style="display:inline"><?= csrf_field() ?><input type="hidden" name="action" value="test"><input type="hidden" name="id" value="<?= e($s['id']) ?>"><button class="btn sm ok">Test</button></form>
        <a class="btn sm ghost" href="?edit=<?= e($s['id']) ?>">Sửa</a>
        <form method="post" style="display:inline" onsubmit="return confirm('Xoá máy chủ <?= e($s['name']) ?>?')"><?= csrf_field() ?><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<?= e($s['id']) ?>"><button class="btn sm err">Xoá</button></form>
      </div></td>
    </tr>
  <?php endforeach; ?>
  </tbody></table>
</div>
<?php layout_footer();
