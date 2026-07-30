// Port của avatar/lucky/DialLuckyManager.java
import { DialLucky } from './DialLucky.js';

/**
 * @author kitakeyos - Hoàng Hữu Dũng
 */
export class DialLuckyManager {

  static XU = 0;
  static LUONG = 1;
  static MIEN_PHI = 2;

  static instance = null;

  static getInstance() {
    if (DialLuckyManager.instance == null) {
      DialLuckyManager.instance = new DialLuckyManager();
    }
    return DialLuckyManager.instance;
  }

  constructor() {
    this.list = [];
    this.add(new DialLucky(DialLuckyManager.XU));
    this.add(new DialLucky(DialLuckyManager.LUONG));
    this.add(new DialLucky(DialLuckyManager.MIEN_PHI));
  }

  add(dialLucky) {
    this.list.push(dialLucky);
  }

  find(type) {
    for (const dl of this.list) {
      if (dl.getType() === type) {
        return dl;
      }
    }
    return null;
  }

  // Chờ 3 bảng quay đọc xong DB (Java làm đồng bộ trong constructor)
  async load() {
    for (const dl of this.list) {
      await dl.loadPromise;
    }
  }
}

// NOTE: khởi tạo lười (getInstance) để câu SQL trong DialLucky.load() chỉ chạy
// sau khi dbManager.start() được gọi. Java dùng static final instance.
export default DialLuckyManager;
