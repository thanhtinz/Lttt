/** Port của avatar/item/Part.java (@Builder @Getter @AllArgsConstructor). */
import { Item } from './Item.js';

export class Part {
  constructor(id = 0, name = null, coin = 0, gold = 0, icon = 0, type = 0, zOrder = 0,
              sell = 0, level = 0, gender = 0, expiredDay = 0, imgID = null, dx = null, dy = null) {
    this.id = id | 0;
    this.name = name;
    this.coin = coin | 0;
    this.gold = gold | 0;
    this.icon = (icon << 16) >> 16;   // short
    this.type = (type << 16) >> 16;   // short
    this.zOrder = (zOrder << 24) >> 24; // byte
    this.sell = (sell << 24) >> 24;   // byte
    this.level = (level << 24) >> 24; // byte
    this.gender = (gender << 24) >> 24; // byte
    this.expiredDay = expiredDay | 0;
    this.imgID = imgID; // short[]
    this.dx = dx;       // byte[]
    this.dy = dy;       // byte[]
  }

  getId() { return this.id; }
  getName() { return this.name; }
  getCoin() { return this.coin; }
  getGold() { return this.gold; }
  getIcon() { return this.icon; }
  getType() { return this.type; }
  getZOrder() { return this.zOrder; }
  getSell() { return this.sell; }
  getLevel() { return this.level; }
  getGender() { return this.gender; }
  getExpiredDay() { return this.expiredDay; }
  getImgID() { return this.imgID; }
  getDx() { return this.dx; }
  getDy() { return this.dy; }

  static builder() {
    const f = {};
    const b = {
      build: () => new Part(f.id, f.name, f.coin, f.gold, f.icon, f.type, f.zOrder,
        f.sell, f.level, f.gender, f.expiredDay, f.imgID, f.dx, f.dy),
    };
    for (const k of ['id', 'name', 'coin', 'gold', 'icon', 'type', 'zOrder',
      'sell', 'level', 'gender', 'expiredDay', 'imgID', 'dx', 'dy']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }

  static shopByPart(partItems) {
    // Danh sách id của các part
    const partID = partItems.map((p) => p.getId());

    // Lọc + sắp xếp theo thứ tự gốc rồi map sang Item
    const shopPart = partItems
      .filter((part) => partID.includes(part.getId()))
      .slice()
      .sort((a, b) => {
        const ia = partID.indexOf(a.getId());
        const ib = partID.indexOf(b.getId());
        // Integer.MAX_VALUE nếu không tìm thấy
        const va = ia === -1 ? 2147483647 : ia;
        const vb = ib === -1 ? 2147483647 : ib;
        return va - vb;
      })
      .map((part) => new Item(part.getId()));

    return shopPart;
  }
}

export default Part;
