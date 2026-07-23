<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'upgrade_item',
    'title'   => 'Nâng cấp vật phẩm',
    'search'  => ['item_id'],
    'columns' => ['id', 'item_id', 'is_only_luong', 'ratio', 'item_need', 'luong', 'xu', 'scores'],
    'fields'  => [
        'item_id'       => ['label' => 'Item ID', 'type' => 'number', 'default' => 0],
        'is_only_luong' => ['label' => 'Chỉ dùng Lượng (0/1)', 'type' => 'number', 'default' => 0],
        'ratio'         => ['label' => 'Tỉ lệ (%)', 'type' => 'number', 'default' => 50],
        'item_need'     => ['label' => 'Item cần', 'type' => 'number', 'default' => -1],
        'luong'         => ['label' => 'Lượng', 'type' => 'number', 'default' => 0],
        'xu'            => ['label' => 'Xu', 'type' => 'number', 'default' => 0],
        'scores'        => ['label' => 'Điểm', 'type' => 'number', 'default' => 0],
    ],
]);
