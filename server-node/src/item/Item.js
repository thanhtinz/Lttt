/** Port của avatar/item/Item.java (@Getter @Setter, có @Builder trên constructor). */
import { partManager } from './PartManager.js';

// NOTE: avatar.server.Utils chưa được port ⇒ nạp muộn (fire-and-forget) để
// file này import sạch; khi Utils.js có mặt thì writeLog hoạt động như Java.
let _Utils = null;
function writeLog(us, message) {
  if (_Utils) {
    try { _Utils.writeLog(us, message); } catch (e) { /* bỏ qua như bản Java */ }
    return;
  }
  import('../server/Utils.js')
    .then((m) => {
      _Utils = m.Utils ?? m.default ?? null;
      if (_Utils) _Utils.writeLog(us, message);
    })
    .catch(() => { /* chưa có Utils: bỏ qua */ });
}

export class Item {
  /**
   * Java: Item(int id) và Item(int id, long expired, int quantity).
   */
  constructor(id = 0, expired = 0, quantity = 0) {
    this.id = id | 0;
    this.expired = expired;
    this.quantity = quantity | 0;
    this.part = null;
    this.init();
  }

  getId() { return this.id; }
  setId(v) { this.id = v | 0; }
  getExpired() { return this.expired; }
  setExpired(v) { this.expired = v; }
  getQuantity() { return this.quantity; }
  setQuantity(v) { this.quantity = v | 0; }
  getPart() { return this.part; }
  setPart(v) { this.part = v; }

  static builder() {
    const f = {};
    const b = { build: () => new Item(f.id, f.expired, f.quantity) };
    for (const k of ['id', 'expired', 'quantity']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }

  isForever() {
    return this.expired === -1;
  }

  getDay() {
    return Math.trunc((this.expired - Date.now()) / 1000 / 60 / 60 / 24) + 1;
  }

  increase(us, quantity, itemId) {
    // Kiểm tra nếu số lượng yêu cầu là hợp lệ
    if (quantity <= 0) {
      return this.quantity;
    }

    // Áp dụng giới hạn số lượng tối đa là 100
    if (this.quantity + quantity > 20000) {
      writeLog(us, 'quantity, increase ' + quantity + ' by ' + itemId);
      this.quantity = 20000;
    } else {
      this.quantity += quantity;
    }
    return this.quantity;
  }

  reduce(quantity) {
    if (quantity <= 0 || this.quantity - quantity < 0) {
      return this.quantity;
    }
    this.quantity -= quantity;

    return this.quantity;
  }

  expiredString() {
    if (this.isForever()) {
      return '';
    }
    // SimpleDateFormat("dd-MM-yyyy")
    const d = new Date(this.expired);
    const p2 = (n) => String(n).padStart(2, '0');
    return 'Ngày hết hạn: ' + p2(d.getDate()) + '-' + p2(d.getMonth() + 1) + '-' + d.getFullYear();
  }

  reliability() {
    let reliability = Math.trunc((this.getDay() * 100) / 30);
    if (this.isForever()) {
      reliability = 100;
    } else if (reliability > 100) {
      reliability = 100;
    } else if (reliability < 0) {
      reliability = 0;
    }
    return (reliability << 24) >> 24; // (byte)
  }

  init() {
    this.part = partManager.findPartByID(this.id);
  }
}

export default Item;
