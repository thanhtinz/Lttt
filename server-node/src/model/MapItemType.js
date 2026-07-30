/** Port của avatar/model/MapItemType.java (@Builder @Getter @AllArgsConstructor). */
export class MapItemType {
  constructor(id = 0, imgID = 0, iconID = 0, priceLuong = 0, dx = 0, dy = 0,
              name = null, des = null, priceXu = 0, buy = 0, dir = 0, listNotTrans = null) {
    this.id = (id << 16) >> 16;               // short
    this.imgID = (imgID << 16) >> 16;         // short
    this.iconID = (iconID << 16) >> 16;       // short
    this.priceLuong = (priceLuong << 16) >> 16; // short
    this.dx = (dx << 24) >> 24;               // byte
    this.dy = (dy << 24) >> 24;               // byte
    this.name = name;
    this.des = des;
    this.priceXu = priceXu | 0;
    this.buy = (buy << 24) >> 24;             // byte
    this.dir = (dir << 24) >> 24;             // byte
    this.listNotTrans = listNotTrans;
  }

  getId() { return this.id; }
  getImgID() { return this.imgID; }
  getIconID() { return this.iconID; }
  getPriceLuong() { return this.priceLuong; }
  getDx() { return this.dx; }
  getDy() { return this.dy; }
  getName() { return this.name; }
  getDes() { return this.des; }
  getPriceXu() { return this.priceXu; }
  getBuy() { return this.buy; }
  getDir() { return this.dir; }
  getListNotTrans() { return this.listNotTrans; }

  static builder() {
    const f = {};
    const b = {
      build: () => new MapItemType(f.id, f.imgID, f.iconID, f.priceLuong, f.dx, f.dy,
        f.name, f.des, f.priceXu, f.buy, f.dir, f.listNotTrans),
    };
    for (const k of ['id', 'imgID', 'iconID', 'priceLuong', 'dx', 'dy',
      'name', 'des', 'priceXu', 'buy', 'dir', 'listNotTrans']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default MapItemType;
