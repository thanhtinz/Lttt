#!/usr/bin/env python3
"""
Thay asset tileset map của game sang bộ pixel-art "cozy" mà KHÔNG đổi gameplay.

Nguyên tắc: giữ nguyên kích thước ảnh, vị trí ô, và alpha (hình dáng/viền) của
tileset gốc — chỉ thay TEXTURE từng pixel bằng texture cozy tương ứng loại địa
hình. Nhờ vậy:
  * bố cục map (grid ký tự trong client) không đổi → game y như bản gốc
  * va chạm/toạ độ không lệch một pixel nào
  * mọi client (Java jar, APK, Unity) đều nhận art mới vì server phục vụ file này

Cách hoạt động: mỗi pixel gốc được xếp vào một loại địa hình (cỏ / đất / đá /
nhựa / nước) bằng cách tìm màu tham chiếu gần nhất, rồi lấy màu từ texture cozy
của loại đó (lấy mẫu theo toạ độ để có vân tự nhiên). Độ sáng tương đối của
pixel gốc được giữ lại nên bóng đổ, viền, vạch kẻ đường... vẫn hiện rõ.

Dùng:  python3 build_tilemap.py
Kết quả: out/res/hd/tilemap/1.png và out/res/medium/tilemap/1.png
"""
from pathlib import Path
from PIL import Image

BASE = Path(__file__).resolve().parent
SRC = BASE / 'src'
ORIG = BASE / 'orig'
OUT = BASE / 'out'

# ---- Nguồn tile cozy: (file, hàng, cột) trong tileset 16px ----
COZY_TILES = {
    'grass':   ('farm_tiles.png', 1, 1),    # cỏ
    'grass2':  ('farm_tiles.png', 1, 15),   # cỏ đậm
    'sand':    ('farm_tiles.png', 1, 28),   # đất/cát
    'stone':   ('town_tiles.png', 5, 36),   # đá/lề đường (tile phẳng, không hoa văn)
    'asphalt': ('town_tiles.png', 55, 2),   # đường nhựa
    'water':   ('town_tiles.png', 51, 9),   # nước
}

# ---- Màu tham chiếu của tileset GỐC -> loại địa hình ----
# (lấy từ việc phân tích 24 ô của res/hd/tilemap/1.png)
REFERENCE = [
    ((170, 142, 90), 'sand'),
    ((196, 170, 120), 'sand'),
    ((140, 115, 72), 'sand'),
    ((95, 115, 33), 'grass'),
    ((60, 80, 20), 'grass2'),
    ((130, 160, 60), 'grass'),
    ((31, 31, 31), 'asphalt'),
    ((70, 70, 70), 'asphalt'),
    ((205, 205, 205), 'stone'),   # vạch kẻ đường trắng
    ((132, 132, 132), 'stone'),
    ((100, 100, 100), 'stone'),
    ((36, 112, 176), 'water'),
    ((20, 80, 140), 'water'),
    ((114, 143, 143), 'water'),   # mặt nước sáng / trời phản chiếu
    ((160, 190, 200), 'water'),
]

TILE16 = 16


def load_cozy_tiles():
    """Cắt các tile 16x16 cozy ra thành ảnh RGB."""
    cache, out = {}, {}
    for name, (fname, r, c) in COZY_TILES.items():
        if fname not in cache:
            cache[fname] = Image.open(SRC / fname).convert('RGBA')
        im = cache[fname]
        out[name] = im.crop((c * TILE16, r * TILE16, (c + 1) * TILE16, (r + 1) * TILE16)).convert('RGB')
    return out


def classify(rgb):
    """Trả về loại địa hình có màu tham chiếu gần nhất."""
    r, g, b = rgb
    best, bestd = None, 1 << 30
    for (rr, gg, bb), kind in REFERENCE:
        d = (r - rr) ** 2 + (g - gg) ** 2 + (b - bb) ** 2
        if d < bestd:
            bestd, best = d, kind
    return best


def avg_luma(img):
    px = list(img.getdata())
    return sum(0.299 * p[0] + 0.587 * p[1] + 0.114 * p[2] for p in px) / len(px)


def build(orig_path, out_path):
    orig = Image.open(orig_path).convert('RGBA')
    W, H = orig.size
    tiles = load_cozy_tiles()
    lum = {k: avg_luma(v) for k, v in tiles.items()}

    # luma trung bình của từng loại trong ảnh GỐC, để giữ tương phản
    sums, counts = {}, {}
    op = orig.load()
    for y in range(H):
        for x in range(W):
            r, g, b, a = op[x, y]
            if a == 0:
                continue
            k = classify((r, g, b))
            sums[k] = sums.get(k, 0.0) + (0.299 * r + 0.587 * g + 0.114 * b)
            counts[k] = counts.get(k, 0) + 1
    orig_lum = {k: sums[k] / counts[k] for k in sums}

    out = Image.new('RGBA', (W, H), (0, 0, 0, 0))
    outp = out.load()
    tps = {k: v.load() for k, v in tiles.items()}

    for y in range(H):
        for x in range(W):
            r, g, b, a = op[x, y]
            if a == 0:
                continue  # giữ nguyên vùng trong suốt -> hình dáng ô không đổi
            k = classify((r, g, b))
            cr, cg, cb = tps[k][x % TILE16, y % TILE16]
            # giữ độ sáng tương đối của pixel gốc (bóng, viền, vạch kẻ)
            src_l = 0.299 * r + 0.587 * g + 0.114 * b
            ratio = (src_l + 1e-6) / (orig_lum.get(k, src_l) + 1e-6)
            ratio = max(0.55, min(1.7, ratio))
            f = lambda v: max(0, min(255, int(v * ratio)))
            outp[x, y] = (f(cr), f(cg), f(cb), a)

    out_path.parent.mkdir(parents=True, exist_ok=True)
    out.save(out_path)
    print(f'  {out_path.relative_to(BASE)}  ({W}x{H})  loại dùng: {sorted(counts, key=counts.get, reverse=True)}')


def main():
    print('Dựng tileset cozy (giữ nguyên kích thước + alpha của bản gốc):')
    build(ORIG / 'tilemap_hd_1.png', OUT / 'res/hd/tilemap/1.png')
    build(ORIG / 'tilemap_medium_1.png', OUT / 'res/medium/tilemap/1.png')
    print('\nCopy thư mục out/res/* vào thư mục res/ của server là xong.')


if __name__ == '__main__':
    main()
