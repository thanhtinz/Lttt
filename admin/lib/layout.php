<?php
declare(strict_types=1);

/** Cấu trúc menu điều hướng. */
function nav_menu(): array
{
    return [
        'Tổng quan'       => [['index.php', 'Bảng điều khiển']],
        'Người chơi'      => [
            ['accounts.php', 'Tài khoản'],
            ['players.php', 'Nhân vật'],
            ['recharge.php', 'Nạp thẻ'],
            ['gioithieu.php', 'Giới thiệu'],
        ],
        'Quà & Vòng quay' => [
            ['giftcode.php', 'Giftcode'],
            ['dial_lucky.php', 'Vòng quay may mắn'],
        ],
        'Dữ liệu game'    => [
            ['items.php', 'Vật phẩm'],
            ['upgrade_item.php', 'Nâng cấp vật phẩm'],
            ['foods.php', 'Thức ăn'],
            ['house.php', 'Nhà'],
            ['npc.php', 'NPC'],
            ['map_item.php', 'Vật phẩm bản đồ'],
        ],
        'Cộng đồng'       => [
            ['clans.php', 'Bang hội'],
        ],
        'Nhật ký'         => [
            ['logs.php', 'Giao dịch / Cược'],
        ],
        'Điều khiển'      => [
            ['server_control.php', 'Điều khiển server'],
            ['manage_servers.php', 'Máy chủ QL'],
        ],
    ];
}

function layout_header(string $title = ''): void
{
    $app     = e(config('app_name', 'Admin'));
    $admin   = current_admin();
    $servers = servers_all();
    $cur     = current_server();
    $curId   = $cur['id'] ?? '';
    $script  = basename($_SERVER['SCRIPT_NAME'] ?? '');
    ?><!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><?= $title ? e($title) . ' · ' : '' ?><?= $app ?></title>
<style>
  :root{--bg:#0f1522;--panel:#18213a;--line:#26304d;--txt:#dfe6f5;--mut:#8b97b4;--acc:#4f8cff;--ok:#2ecc71;--err:#ff5d6c;--warn:#ffb020}
  *{box-sizing:border-box}
  body{margin:0;font-family:system-ui,Segoe UI,Roboto,Arial,sans-serif;background:var(--bg);color:var(--txt);font-size:14px}
  a{color:var(--acc);text-decoration:none}a:hover{text-decoration:underline}
  header{display:flex;align-items:center;gap:16px;padding:10px 18px;background:var(--panel);border-bottom:1px solid var(--line);position:sticky;top:0;z-index:10}
  header .brand{font-weight:700;font-size:16px}
  header .sp{flex:1}
  header select,header .btn{background:#0f1626;color:var(--txt);border:1px solid var(--line);border-radius:8px;padding:7px 10px}
  .wrap{display:flex;min-height:calc(100vh - 49px)}
  nav{width:220px;background:var(--panel);border-right:1px solid var(--line);padding:12px 8px;flex-shrink:0}
  nav .grp{color:var(--mut);text-transform:uppercase;font-size:11px;letter-spacing:.5px;margin:14px 10px 6px}
  nav a{display:block;padding:8px 12px;border-radius:8px;color:var(--txt)}
  nav a.active{background:var(--acc);color:#fff}
  nav a:hover{background:#1e2a49;text-decoration:none}
  main{flex:1;padding:22px;max-width:1200px}
  h1{font-size:20px;margin:0 0 18px}
  .card{background:var(--panel);border:1px solid var(--line);border-radius:12px;padding:18px;margin-bottom:18px}
  .grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:12px}
  .stat{background:#0f1626;border:1px solid var(--line);border-radius:10px;padding:14px}
  .stat .n{font-size:24px;font-weight:700}.stat .l{color:var(--mut);font-size:12px;margin-top:4px}
  table{width:100%;border-collapse:collapse}
  th,td{text-align:left;padding:9px 10px;border-bottom:1px solid var(--line)}
  th{color:var(--mut);font-weight:600;font-size:12px;text-transform:uppercase}
  tr:hover td{background:#0f1626}
  input,select,textarea{background:#0f1626;color:var(--txt);border:1px solid var(--line);border-radius:8px;padding:8px 10px;font-size:14px;font-family:inherit}
  input:focus,select:focus,textarea:focus{outline:none;border-color:var(--acc)}
  label{display:block;color:var(--mut);font-size:12px;margin:10px 0 4px}
  .btn{display:inline-block;background:var(--acc);color:#fff;border:none;border-radius:8px;padding:8px 14px;cursor:pointer;font-size:14px}
  .btn:hover{filter:brightness(1.1);text-decoration:none}
  .btn.sm{padding:5px 9px;font-size:12px}
  .btn.err{background:var(--err)}.btn.ok{background:var(--ok)}.btn.warn{background:var(--warn);color:#1a1200}.btn.ghost{background:#26304d}
  .row{display:flex;gap:10px;flex-wrap:wrap;align-items:flex-end}
  .flash{padding:10px 14px;border-radius:8px;margin-bottom:12px}
  .flash.ok{background:rgba(46,204,113,.15);border:1px solid var(--ok)}
  .flash.err{background:rgba(255,93,108,.15);border:1px solid var(--err)}
  .flash.warn{background:rgba(255,176,32,.15);border:1px solid var(--warn)}
  .pill{display:inline-block;padding:2px 8px;border-radius:20px;font-size:12px}
  .pill.on{background:rgba(46,204,113,.2);color:var(--ok)}.pill.off{background:rgba(255,93,108,.2);color:var(--err)}
  .muted{color:var(--mut)}
  .pager{display:flex;gap:6px;margin-top:14px}
  .pager a,.pager span{padding:6px 11px;border:1px solid var(--line);border-radius:8px}
  .pager .cur{background:var(--acc);color:#fff}
</style>
</head>
<body>
<header>
  <span class="brand">🎮 <?= $app ?></span>
  <form method="post" action="switch_server.php" style="margin:0">
    <?= csrf_field() ?>
    <select name="server_id" onchange="this.form.submit()" title="Chọn máy chủ">
      <?php foreach ($servers as $s): ?>
        <option value="<?= e($s['id']) ?>" <?= ($s['id'] === $curId) ? 'selected' : '' ?>>
          <?= e($s['name']) ?> (<?= e($s['dbname']) ?>)
        </option>
      <?php endforeach; ?>
    </select>
  </form>
  <span class="sp"></span>
  <?php if ($admin): ?>
    <span class="muted">👤 <?= e($admin['username']) ?></span>
    <a class="btn ghost" href="logout.php">Đăng xuất</a>
  <?php endif; ?>
</header>
<div class="wrap">
  <nav>
    <?php foreach (nav_menu() as $grp => $items): ?>
      <div class="grp"><?= e($grp) ?></div>
      <?php foreach ($items as [$href, $label]): ?>
        <a href="<?= e($href) ?>" class="<?= $script === $href ? 'active' : '' ?>"><?= e($label) ?></a>
      <?php endforeach; ?>
    <?php endforeach; ?>
  </nav>
  <main>
    <?php foreach (take_flash() as $f): ?>
      <div class="flash <?= e($f['type']) ?>"><?= e($f['msg']) ?></div>
    <?php endforeach; ?>
<?php
}

function layout_footer(): void
{
    ?>
  </main>
</div>
</body>
</html>
<?php
}

/** Render phân trang đơn giản. */
function pager(int $page, int $total, int $perPage): void
{
    $pages = (int) ceil($total / max(1, $perPage));
    if ($pages <= 1) {
        return;
    }
    echo '<div class="pager">';
    for ($i = max(1, $page - 3); $i <= min($pages, $page + 3); $i++) {
        if ($i === $page) {
            echo '<span class="cur">' . $i . '</span>';
        } else {
            echo '<a href="' . e(url_with(['page' => $i])) . '">' . $i . '</a>';
        }
    }
    echo '</div>';
}
