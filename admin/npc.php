<?php
require __DIR__ . '/lib/bootstrap.php';
require_admin();
require __DIR__ . '/lib/layout.php';
require __DIR__ . '/lib/crud.php';
crud_page([
    'table'   => 'npc',
    'title'   => 'NPC',
    'search'  => ['name'],
    'note'    => 'Sửa NPC áp dụng khi server nạp lại dữ liệu NPC.',
    'columns' => ['id', 'name', 'map', 'x', 'y', 'star'],
    'fields'  => [
        'name'  => ['label' => 'Tên'],
        'map'   => ['label' => 'Map', 'type' => 'number', 'default' => 0],
        'x'     => ['label' => 'X', 'type' => 'number', 'default' => 0],
        'y'     => ['label' => 'Y', 'type' => 'number', 'default' => 0],
        'star'  => ['label' => 'Star', 'type' => 'number', 'default' => 0],
        'items' => ['label' => 'Items (JSON)', 'type' => 'textarea', 'default' => '[]'],
        'chat'  => ['label' => 'Chat (JSON)', 'type' => 'textarea', 'default' => '[]'],
    ],
]);
