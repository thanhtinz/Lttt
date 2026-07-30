<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'dial_lucky',
    'title'   => 'Vòng quay may mắn',
    'search'  => ['item_id'],
    'columns' => ['id', 'item_id', 'xu', 'luong', 'free', 'ratio'],
    'fields'  => [
        'item_id' => ['label' => 'Item ID', 'type' => 'number', 'default' => 0],
        'xu'      => ['label' => 'Xu', 'type' => 'number', 'default' => 0],
        'luong'   => ['label' => 'Lượng', 'type' => 'number', 'default' => 0],
        'free'    => ['label' => 'Free (0/1)', 'type' => 'number', 'default' => 0],
        'ratio'   => ['label' => 'Tỉ lệ', 'type' => 'number', 'default' => 0],
    ],
]);
