<?php
require __DIR__ . '/lib/bootstrap.php';

if (is_logged_in()) {
    redirect('index.php');
}

$err = '';
if (is_post()) {
    csrf_check();
    // Cho phép chọn máy chủ ngay tại màn login
    if (!empty($_POST['server_id'])) {
        set_current_server((string) $_POST['server_id']);
    }
    $u = trim((string) post('username', ''));
    $p = (string) post('password', '');
    try {
        [$ok, $msg] = admin_login($u, $p);
        if ($ok) {
            redirect('index.php');
        }
        $err = $msg;
    } catch (Throwable $ex) {
        $err = 'Không kết nối được DB máy chủ: ' . $ex->getMessage();
    }
}

$servers = servers_all();
$curId   = current_server()['id'] ?? '';
?><!doctype html>
<html lang="vi"><head>
<meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng nhập · <?= e(config('app_name', 'Admin')) ?></title>
<style>
  body{margin:0;min-height:100vh;display:flex;align-items:center;justify-content:center;background:#0f1522;color:#dfe6f5;font-family:system-ui,Segoe UI,Roboto,Arial,sans-serif}
  .box{background:#18213a;border:1px solid #26304d;border-radius:14px;padding:28px;width:340px}
  h1{font-size:18px;margin:0 0 18px;text-align:center}
  label{display:block;color:#8b97b4;font-size:12px;margin:12px 0 4px}
  input,select{width:100%;background:#0f1626;color:#dfe6f5;border:1px solid #26304d;border-radius:8px;padding:10px;font-size:14px}
  .btn{width:100%;margin-top:18px;background:#4f8cff;color:#fff;border:none;border-radius:8px;padding:11px;font-size:15px;cursor:pointer}
  .err{background:rgba(255,93,108,.15);border:1px solid #ff5d6c;padding:10px;border-radius:8px;margin-bottom:12px;font-size:13px}
</style></head><body>
<form class="box" method="post">
  <h1>🎮 <?= e(config('app_name', 'Admin')) ?></h1>
  <?php if ($err): ?><div class="err"><?= e($err) ?></div><?php endif; ?>
  <?= csrf_field() ?>
  <label>Máy chủ</label>
  <select name="server_id">
    <?php foreach ($servers as $s): ?>
      <option value="<?= e($s['id']) ?>" <?= $s['id'] === $curId ? 'selected' : '' ?>><?= e($s['name']) ?></option>
    <?php endforeach; ?>
  </select>
  <label>Tài khoản admin</label>
  <input name="username" autocomplete="username" autofocus>
  <label>Mật khẩu</label>
  <input type="password" name="password" autocomplete="current-password">
  <button class="btn" type="submit">Đăng nhập</button>
</form>
</body></html>
