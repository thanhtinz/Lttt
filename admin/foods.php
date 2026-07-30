<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'foods',
    'title'   => 'Thức ăn',
    'search'  => ['name'],
    'columns' => ['id', 'name', 'img', 'shop', 'percent_health', 'price'],
    'fields'  => [
        'name'           => ['label' => 'Tên'],
        'description'    => ['label' => 'Mô tả', 'type' => 'textarea'],
        'img'            => ['label' => 'Ảnh (img)', 'type' => 'number', 'default' => 0],
        'shop'           => ['label' => 'Shop', 'type' => 'number', 'default' => 0],
        'percent_health' => ['label' => '% Hồi máu', 'type' => 'number', 'default' => 0],
        'price'          => ['label' => 'Giá', 'type' => 'number', 'default' => 0],
    ],
]);
