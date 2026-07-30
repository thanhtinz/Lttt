/**
 * Port của avatar/model/CreateChar.java.
 * NOTE: Java khởi tạo instance khi class được nạp (sau khi PartManager.load()).
 * Ở Node tạo instance ngay lúc import sẽ đọc parts rỗng ⇒ getInstance() tạo muộn.
 */
import { partManager } from '../item/PartManager.js';

let instance = null;

export class CreateChar {
  constructor() {
    this.listHair = [];
    this.listClothing = [];
    this.listPant = [];
    this.init();
  }

  static getInstance() {
    if (instance == null) {
      instance = new CreateChar();
    }
    return instance;
  }

  init() {
    const parts = partManager.getAvatarPart();
    for (const p of parts) {
      if (p.getLevel() === 0 && p.getGender() <= 2) {
        switch (p.getZOrder()) {
          case 50:
            this.listHair.push(p);
            break;
          case 20:
            this.listClothing.push(p);
            break;
          case 10:
            this.listPant.push(p);
            break;
        }
      }
    }
  }

  /**
   * Java có 2 overload cùng 2 tham số:
   *   check(byte type, Item item)
   *   check(byte gender, List<Item> wearing)
   * ⇒ phân biệt theo tham số thứ 2 có phải mảng (List) hay không.
   */
  check(a, b) {
    if (Array.isArray(b)) {
      return this._checkWearing(a, b);
    }
    const type = a;
    const item = b;
    switch (type) {
      case 0:
        return this.listHair.some((t) => t.getId() === item.getId());
      case 1:
        return this.listClothing.some((t) => t.getId() === item.getId());
      case 2:
        return this.listPant.some((t) => t.getId() === item.getId());
    }
    return false;
  }

  _checkWearing(gender, wearing) {
    const pant = wearing[0];
    if (!this.check(2, pant)) {
      return false;
    }

    const clothing = wearing[1];
    if (!this.check(1, clothing)) {
      return false;
    }
    if (wearing[2].getId() !== 0) {
      return false;
    }

    if (wearing[3].getId() !== 4) {
      return false;
    }
    const hair = wearing[4];
    if (!this.check(0, hair)) {
      return false;
    }
    return true;
  }
}

export default CreateChar;
