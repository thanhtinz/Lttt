<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'items',
    'title'   => 'Vật phẩm',
    'search'  => ['name'],
    'note'    => 'Sửa vật phẩm áp dụng khi server nạp lại dữ liệu.',
    'columns' => ['id', 'name', 'type', 'icon', 'coin', 'gold', 'level', 'gender', 'sell', 'expired_day'],
    'fields'  => [
        'name'        => ['label' => 'Tên'],
        'type'        => ['label' => 'Loại (type)', 'type' => 'number', 'default' => 0],
        'icon'        => ['label' => 'Icon', 'type' => 'number', 'default' => 0],
        'coin'        => ['label' => 'Xu (coin)', 'type' => 'number', 'default' => 0],
        'gold'        => ['label' => 'Lượng (gold)', 'type' => 'number', 'default' => 0],
        'level'       => ['label' => 'Level', 'type' => 'number', 'default' => 0],
        'gender'      => ['label' => 'Giới tính', 'type' => 'number', 'default' => 0],
        'sell'        => ['label' => 'Bán được', 'type' => 'number', 'default' => 0],
        'expired_day' => ['label' => 'Hạn (ngày)', 'type' => 'number', 'default' => 0],
        'zorder'      => ['label' => 'Zorder', 'type' => 'number', 'default' => 0],
        'animation'   => ['label' => 'Animation (JSON)', 'type' => 'textarea', 'default' => '[]'],
    ],
]);
