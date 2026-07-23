<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'house',
    'title'   => 'Nhà',
    'search'  => ['name'],
    'columns' => ['id', 'name', 'price', 'expire'],
    'fields'  => [
        'name'   => ['label' => 'Tên'],
        'price'  => ['label' => 'Giá', 'type' => 'number', 'default' => 0],
        'expire' => ['label' => 'Hạn (-1 vĩnh viễn)', 'type' => 'number', 'default' => -1],
    ],
]);
