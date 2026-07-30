/**
 * Port của avatar/play/HouseItem.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
export class HouseItem {

  /** Java có 2 constructor: HouseItem() và HouseItem(itemId, x, y, rotate). */
  constructor(itemId, x, y, rotate) {
    if (itemId === undefined) {
      this.itemId = 0;
      this.x = 0;
      this.y = 0;
      this.rotate = 0;
      return;
    }
    this.itemId = (itemId << 16) >> 16;
    this.x = ((Math.trunc(x / 24) << 16) >> 16);
    this.y = ((Math.trunc(y / 24) << 16) >> 16);
    this.rotate = (rotate << 24) >> 24;
  }
}

export default HouseItem;
