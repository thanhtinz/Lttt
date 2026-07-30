<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';

if (is_post()) {
    csrf_check();
    $action = (string) post('action');
    try {
        if ($action === 'create') {
            $code = trim((string) post('code'));
            if ($code === '') {
                throw new RuntimeException('Chưa nhập code.');
            }
            $data = trim((string) post('data', '[]'));
            db_exec(
                'INSERT INTO `giftcode` (`code`, `message`, `data`, `start_time`, `end_time`, `num`, `create_by`, `create_time`)
                 VALUES (?, ?, ?, ?, ?, ?, ?, NOW())',
                [$code, (string) post('message'), $data,
                 post('start_time') ?: date('Y-m-d H:i:s'),
                 post('end_time') ?: date('Y-m-d H:i:s', time() + 30 * 86400),
                 to_int(post('num'), 1), (int) (current_admin()['id'] ?? 0)]
            );
            flash("Đã tạo giftcode: $code");
        } elseif ($action === 'delete') {
            $id = to_int(post('id'));
            db_exec('DELETE FROM `giftcode_use` WHERE `giftcode_id` = ?', [$id]);
            db_exec('DELETE FROM `giftcode` WHERE `id` = ?', [$id]);
            flash("Đã xoá giftcode #$id");
        }
    } catch (Throwable $ex) {
        flash('Lỗi: ' . $ex->getMessage(), 'err');
    }
    redirect(url_with([]));
}

$perPage = (int) config('per_page', 30);
$page    = current_page();
$offset  = ($page - 1) * $perPage;
$total   = (int) db_val('SELECT COUNT(*) FROM `giftcode`');
$rows    = db_all(
    "SELECT g.*, (SELECT COUNT(*) FROM `giftcode_use` gu WHERE gu.giftcode_id = g.id) AS used
     FROM `giftcode` g ORDER BY g.`id` DESC LIMIT $perPage OFFSET $offset");

layout_header('Giftcode');
?>
<h1>Giftcode</h1>
<div class="card">
  <h3 style="margin-top:0">Tạo giftcode</h3>
  <form method="post">
    <?= csrf_field() ?><input type="hidden" name="action" value="create">
    <div class="row">
      <div><label>Code</label><input name="code" placeholder="VD: TANTHU2026" required></div>
      <div><label>Số lượt dùng</label><input name="num" type="number" value="100"></div>
      <div><label>Bắt đầu</label><input name="start_time" type="datetime-local"></div>
      <div><label>Kết thúc</label><input name="end_time" type="datetime-local"></div>
    </div>
    <label>Lời nhắn (message)</label>
    <input name="message" style="width:100%" placeholder="Nhập giftcode nhận quà tân thủ!">
    <label>Phần thưởng (data — JSON)</label>
    <textarea name="data" rows="3" style="width:100%" placeholder='[{"id":123,"quantity":1},{"type":"xu","amount":10000}]'>[]</textarea>
    <p class="muted">Định dạng <code>data</code> theo đúng cấu trúc server đọc (item id / số lượng / xu / lượng). Tra Item ID ở trang <a href="items.php">Vật phẩm</a>.</p>
    <button class="btn" type="submit">Tạo</button>
  </form>
</div>
<div class="card">
  <table><thead><tr><th>ID</th><th>Code</th><th>Lời nhắn</th><th>Đã dùng / Tổng</th><th>Bắt đầu</th><th>Kết thúc</th><th></th></tr></thead>
  <tbody>
  <?php foreach ($rows as $g): ?>
    <tr>
      <td><?= (int) $g['id'] ?></td>
      <td><b><?= e($g['code']) ?></b></td>
      <td class="muted"><?= e(mb_strimwidth((string) ($g['message'] ?? ''), 0, 40, '…')) ?></td>
      <td><?= (int) $g['used'] ?> / <?= (int) $g['num'] ?></td>
      <td class="muted"><?= e($g['start_time']) ?></td>
      <td class="muted"><?= e($g['end_time']) ?></td>
      <td><form method="post" style="display:inline" onsubmit="return confirm('Xoá giftcode <?= e($g['code']) ?>?')">
        <?= csrf_field() ?><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<?= (int) $g['id'] ?>">
        <button class="btn err sm">Xoá</button></form></td>
    </tr>
  <?php endforeach; ?>
  <?php if (!$rows): ?><tr><td colspan="7" class="muted">Chưa có giftcode.</td></tr><?php endif; ?>
  </tbody></table>
  <?php pager($page, $total, $perPage); ?>
</div>
<?php layout_footer();
