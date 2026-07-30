<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'map_item',
    'title'   => 'Vật phẩm bản đồ',
    'search'  => ['type_id'],
    'columns' => ['id', 'type_id', 'type', 'x', 'y'],
    'fields'  => [
        'type_id' => ['label' => 'Type ID', 'type' => 'number', 'default' => 0],
        'type'    => ['label' => 'Type', 'type' => 'number', 'default' => 0],
        'x'       => ['label' => 'X', 'type' => 'number', 'default' => 0],
        'y'       => ['label' => 'Y', 'type' => 'number', 'default' => 0],
    ],
]);
