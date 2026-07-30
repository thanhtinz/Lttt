<?php
declare(strict_types=1);

/**
 * Trang CRUD generic cho 1 bảng. Xử lý list + tìm kiếm + phân trang + thêm/sửa/xoá.
 *
 * $opts:
 *   table   : tên bảng (string)
 *   title   : tiêu đề trang
 *   pk      : khoá chính (mặc định 'id')
 *   search  : mảng cột để tìm LIKE
 *   columns : mảng cột hiển thị ở bảng danh sách
 *   fields  : map cột => ['label'=>..,'type'=>'text|number|textarea','default'=>..]
 *             (các cột dùng cho form thêm/sửa; KHÔNG gồm pk)
 *   note    : ghi chú hiển thị (optional)
 *   order   : ORDER BY (mặc định "`pk` DESC")
 */
function crud_page(array $opts): void
{
    $table   = $opts['table'];
    $title   = $opts['title'] ?? $table;
    $pk      = $opts['pk'] ?? 'id';
    $search  = $opts['search'] ?? [];
    $columns = $opts['columns'] ?? [];
    $fields  = $opts['fields'] ?? [];
    $order   = $opts['order'] ?? "`$pk` DESC";
    $perPage = (int) config('per_page', 30);

    if (!db_table_exists($table)) {
        layout_header($title);
        echo '<h1>' . e($title) . '</h1><div class="flash err">Bảng <code>' . e($table) . '</code> không tồn tại trong DB máy chủ này.</div>';
        layout_footer();
        return;
    }

    // ----- POST: thêm / sửa / xoá -----
    if (is_post()) {
        csrf_check();
        $action = (string) post('_action');
        try {
            if ($action === 'delete') {
                db_exec("DELETE FROM `$table` WHERE `$pk` = ?", [to_int(post('_id'))]);
                flash('Đã xoá #' . to_int(post('_id')));
            } elseif ($action === 'save') {
                $id   = to_int(post('_id'));
                $cols = array_keys($fields);
                $vals = [];
                foreach ($cols as $c) {
                    $vals[$c] = post($c);
                }
                if ($id > 0) {
                    $set = implode(', ', array_map(fn($c) => "`$c` = ?", $cols));
                    $args = array_values($vals);
                    $args[] = $id;
                    db_exec("UPDATE `$table` SET $set WHERE `$pk` = ?", $args);
                    flash('Đã lưu #' . $id);
                } else {
                    $ph = implode(', ', array_fill(0, count($cols), '?'));
                    $cn = implode(', ', array_map(fn($c) => "`$c`", $cols));
                    db_exec("INSERT INTO `$table` ($cn) VALUES ($ph)", array_values($vals));
                    flash('Đã thêm mới');
                }
            }
        } catch (Throwable $ex) {
            flash('Lỗi: ' . $ex->getMessage(), 'err');
        }
        redirect(url_with([]));
    }

    // ----- Dữ liệu đang sửa -----
    $editRow = null;
    if (isset($_GET['edit'])) {
        $editRow = db_one("SELECT * FROM `$table` WHERE `$pk` = ?", [to_int($_GET['edit'])]);
    }

    // ----- Tìm kiếm + phân trang -----
    $q = trim((string) param('q', ''));
    $where = '';
    $args  = [];
    if ($q !== '' && $search) {
        $parts = [];
        foreach ($search as $c) {
            $parts[] = "`$c` LIKE ?";
            $args[]  = '%' . $q . '%';
        }
        // cho phép tìm theo pk nếu là số
        if (is_numeric($q)) {
            $parts[] = "`$pk` = ?";
            $args[]  = (int) $q;
        }
        $where = 'WHERE ' . implode(' OR ', $parts);
    }
    $page   = current_page();
    $offset = ($page - 1) * $perPage;
    $total  = (int) db_val("SELECT COUNT(*) FROM `$table` $where", $args);
    $rows   = db_all("SELECT * FROM `$table` $where ORDER BY $order LIMIT $perPage OFFSET $offset", $args);

    layout_header($title);
    ?>
    <h1><?= e($title) ?></h1>
    <?php if (!empty($opts['note'])): ?><div class="flash warn"><?= e($opts['note']) ?></div><?php endif; ?>

    <div class="card">
      <h3 style="margin-top:0"><?= $editRow ? 'Sửa #' . e($editRow[$pk]) : 'Thêm mới' ?></h3>
      <form method="post">
        <?= csrf_field() ?>
        <input type="hidden" name="_action" value="save">
        <input type="hidden" name="_id" value="<?= $editRow ? e($editRow[$pk]) : '' ?>">
        <div class="row">
        <?php foreach ($fields as $col => $f):
            $val = $editRow[$col] ?? ($f['default'] ?? ''); ?>
          <div style="min-width:150px">
            <label><?= e($f['label'] ?? $col) ?></label>
            <?php if (($f['type'] ?? 'text') === 'textarea'): ?>
              <textarea name="<?= e($col) ?>" rows="2" style="width:260px"><?= e($val) ?></textarea>
            <?php else: ?>
              <input name="<?= e($col) ?>" type="<?= e($f['type'] ?? 'text') ?>" value="<?= e($val) ?>">
            <?php endif; ?>
          </div>
        <?php endforeach; ?>
          <button class="btn" type="submit"><?= $editRow ? 'Lưu' : 'Thêm' ?></button>
          <?php if ($editRow): ?><a class="btn ghost" href="<?= e(strtok($_SERVER['REQUEST_URI'], '?')) ?>">Huỷ</a><?php endif; ?>
        </div>
      </form>
    </div>

    <div class="card">
      <?php if ($search): ?>
      <form method="get" class="row" style="margin-bottom:14px">
        <div><label>Tìm kiếm</label><input name="q" value="<?= e($q) ?>"></div>
        <button class="btn" type="submit">Tìm</button>
        <?php if ($q !== ''): ?><a class="btn ghost" href="<?= e(strtok($_SERVER['REQUEST_URI'], '?')) ?>">Xoá lọc</a><?php endif; ?>
      </form>
      <?php endif; ?>
      <table>
        <thead><tr><?php foreach ($columns as $c): ?><th><?= e($c) ?></th><?php endforeach; ?><th></th></tr></thead>
        <tbody>
        <?php foreach ($rows as $r): ?>
          <tr>
            <?php foreach ($columns as $c): ?><td><?= e(mb_strimwidth((string) ($r[$c] ?? ''), 0, 60, '…')) ?></td><?php endforeach; ?>
            <td>
              <div class="row" style="gap:5px">
                <a class="btn sm" href="<?= e(url_with(['edit' => $r[$pk]])) ?>">Sửa</a>
                <form method="post" style="display:inline" onsubmit="return confirm('Xoá #<?= e($r[$pk]) ?>?')">
                  <?= csrf_field() ?>
                  <input type="hidden" name="_action" value="delete">
                  <input type="hidden" name="_id" value="<?= e($r[$pk]) ?>">
                  <button class="btn err sm" type="submit">Xoá</button>
                </form>
              </div>
            </td>
          </tr>
        <?php endforeach; ?>
        <?php if (!$rows): ?><tr><td colspan="<?= count($columns) + 1 ?>" class="muted">Không có dữ liệu.</td></tr><?php endif; ?>
        </tbody>
      </table>
      <?php pager($page, $total, $perPage); ?>
    </div>
    <?php
    layout_footer();
}
