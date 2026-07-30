// Danh mục ngoại hình nhân vật (khớp file trong public/assets/char).
// Sheet walk: 8 frame/hướng, 4 hướng (0=down,1=up,2=left,3=right), grid 32x32.
// Các sheet layer xếp nhiều biến thể màu theo chiều ngang, mỗi biến thể 8 cột.

export const CATALOG = {
  // 8 tông da (mỗi file 1 tông, không có biến thể màu)
  base: ['char1', 'char2', 'char3', 'char4', 'char5', 'char6', 'char7', 'char8'],
  // 10 biến thể màu / file
  clothes: ['basic', 'dress', 'overalls', 'pants', 'sailor', 'skirt', 'sporty', 'suit', 'witch'],
  clothesColors: 10,
  // 14 biến thể màu / file
  hair: ['bob', 'braids', 'buzzcut', 'curly', 'emo', 'ponytail', 'spacebuns', 'wavy'],
  hairColors: 14,
  eyesColors: 14,
};

const ri = (n) => Math.floor(Math.random() * n);

/** Ngoại hình ngẫu nhiên hợp lệ. */
export function randomAppearance() {
  return {
    base: ri(CATALOG.base.length),
    clothes: ri(CATALOG.clothes.length),
    clothesColor: ri(CATALOG.clothesColors),
    hair: ri(CATALOG.hair.length),
    hairColor: ri(CATALOG.hairColors),
    eyesColor: ri(CATALOG.eyesColors),
  };
}

/** Kẹp ngoại hình do client gửi vào khoảng hợp lệ (không tin client). */
export function sanitizeAppearance(a = {}) {
  const clamp = (v, n) => {
    const i = Number.isFinite(+v) ? Math.floor(+v) : 0;
    return Math.max(0, Math.min(n - 1, i));
  };
  return {
    base: clamp(a.base, CATALOG.base.length),
    clothes: clamp(a.clothes, CATALOG.clothes.length),
    clothesColor: clamp(a.clothesColor, CATALOG.clothesColors),
    hair: clamp(a.hair, CATALOG.hair.length),
    hairColor: clamp(a.hairColor, CATALOG.hairColors),
    eyesColor: clamp(a.eyesColor, CATALOG.eyesColors),
  };
}
