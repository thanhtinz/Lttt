# Thay asset sang pixel-art "cozy" (giữ nguyên game gốc)

Mục tiêu: **đổi hình, không đổi game**. Bố cục map, va chạm, toạ độ, gameplay,
protocol — tất cả giữ nguyên; chỉ ảnh được thay.

## Vì sao làm ở phía SERVER

Client (Java jar / APK / Unity) **không chứa** tileset của map — nó **tải từ server**
rồi cache lại:

| Asset | Server phục vụ từ | Lệnh |
|---|---|---|
| Tileset map | `res/hd/tilemap/<id>.png`, `res/medium/tilemap/<id>.png` | `REQUEST_TILE_MAP` (-94) |
| Tile nhà | `res/hd/house/tile.png` | `GET_TILE_MAP` |
| Ảnh vật phẩm | `res/hd/item/<id>.png` | `GET_BIG` / `GET_IMAGE` |
| Vật thể map | `res/hd/object/<id>.png` | — |
| Nông trại | `res/hd/farm/<id>.png` | — |
| Sprite nhân vật | `res/p/part_<id>.dat` | `REQUEST_IMAGE_PART` |

⇒ **Thay ảnh trong `res/` là đổi asset cho MỌI client, không cần build lại client.**

## Cách hoạt động (`build_tilemap.py`)

Không vẽ lại tileset từ đầu (sẽ lệch map). Thay vào đó:

1. Giữ **nguyên kích thước ảnh** và **nguyên kênh alpha** của tileset gốc
   ⇒ hình dáng từng ô, viền, ô trống giống hệt ⇒ map không lệch một pixel.
2. Mỗi pixel gốc được xếp loại địa hình (cỏ / đất / đá / nhựa / nước) bằng màu
   tham chiếu gần nhất, rồi lấy màu từ **texture cozy** của loại đó.
3. Giữ lại **độ sáng tương đối** của pixel gốc ⇒ bóng đổ, vạch kẻ đường, cụm cỏ
   trên lề... vẫn hiện đúng.

```
python3 build_tilemap.py
# -> out/res/hd/tilemap/1.png      (144x384, ô 48px)
# -> out/res/medium/tilemap/1.png  (72x192,  ô 24px)
```

## Áp dụng lên server

```bash
cp -r out/res/* /duong/dan/server/res/
```
Rồi khởi động lại server (hoặc để client tải lại — client cache theo version ảnh).

## Đã kiểm chứng

`server-node/test/e2e-tilemap.mjs` đăng nhập bằng protocol thật rồi gửi
`REQUEST_TILE_MAP`, so từng byte ảnh server trả về với file cozy trên đĩa:

```
→ đăng nhập OK, xin tileset id=1
← nhận tileset id=1, 8621 byte
   PNG magic: PNG | khớp file cozy trên đĩa: CÓ
✅ THAY ASSET HOẠT ĐỘNG
```

## Phạm vi hiện tại & phần còn lại

Chỉ đổi được asset **thật sự có nội dung** trong `res.rar` đi kèm. Thống kê gói này:

| Nhóm | File có nội dung |
|---|---|
| `hd/tilemap` | 1/5 ✅ **đã đổi** |
| `hd/item` | 14.644/15.772 |
| `hd/object` | 2.230/4.111 |
| `hd/farm` | 176/235 |
| `hd/effect` | 56/71 |
| `p` (sprite nhân vật) | 1.631/4.827 |
| `hd/house` | 0/1 (rỗng) |
| `map/*.dat`, `map/imgTileMap` | 0 byte (rỗng) |

⚠️ Lưu ý thật: bộ cozy **không có** 14.644 art tương ứng cho từng vật phẩm. Muốn
đổi tiếp cần chọn hướng:
- **Theo nhóm**: map theo `items.type` (mũ / áo / tóc / cây / quái...) → 1 art cozy cho cả nhóm.
- **Thủ công**: chọn art cho từng vật phẩm quan trọng, còn lại giữ art gốc.

## Nguồn art

`src/` chứa tileset nguồn từ các pack cozy do người dùng cung cấp
(farm "full version", town, nature). Bản quyền thuộc tác giả gốc của pack.
