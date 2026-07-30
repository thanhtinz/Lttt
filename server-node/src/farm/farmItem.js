/** Port của avatar/Farm/farmItem.java (@Builder @Getter). Giữ nguyên tên lớp viết thường. */
export class farmItem {
  /**
   * Java: farmItem(int id) và farmItem(int id, String name, int time, int quantity, int sell).
   */
  constructor(id = 0, name = null, time = 0, quantity = 0, sell = 0) {
    this.id = id | 0;
    this.name = name;
    this.time = time | 0;
    this.quantity = quantity | 0;
    this.sell = sell | 0;
    //init();
  }

  getId() { return this.id; }
  getName() { return this.name; }
  getTime() { return this.time; }
  getQuantity() { return this.quantity; }
  getSell() { return this.sell; }

  static builder() {
    const f = {};
    const b = { build: () => new farmItem(f.id, f.name, f.time, f.quantity, f.sell) };
    for (const k of ['id', 'name', 'time', 'quantity', 'sell']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default farmItem;
