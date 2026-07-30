/** Port của avatar/play/offline/CharacterInfo.java */

// Khai báo Map là static để có thể truy cập từ nhiều class khác nhau
const characterMap = new globalThis.Map();

/** Entry giống Map.Entry của Java (botPlayer gọi getKey()/getValue()). */
class Entry {
  constructor(key, value) {
    this.key = key;
    this.value = value;
  }
  getKey() { return this.key; }
  getValue() { return this.value; }
}

export class CharacterInfo {

  // Method thêm tên và giới tính vào Map
  static addCharacter(name, gender) {
    characterMap.set(name, gender);
  }

  static shuffleCharacterMap() {
    const entries = [...characterMap.entries()];
    // Xáo trộn danh sách các entry (Collections.shuffle)
    for (let i = entries.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      const tmp = entries[i];
      entries[i] = entries[j];
      entries[j] = tmp;
    }
    // Xóa các phần tử cũ và đưa vào các phần tử đã xáo trộn
    characterMap.clear();
    for (const [k, v] of entries) {
      characterMap.set(k, v);
    }
  }

  // Method lấy tên ngẫu nhiên và xóa nó khỏi Map
  static getRandomAndRemove() {
    if (characterMap.size === 0) {
      return null; // Trả về null nếu Map rỗng
    }
    // Lấy một phần tử bất kỳ từ Map
    const first = characterMap.entries().next().value;
    const entry = new Entry(first[0], first[1]);
    // Xóa phần tử này khỏi Map
    characterMap.delete(entry.getKey());
    // Trả về phần tử đã lấy
    return entry;
  }

  // Method kiểm tra xem Map có rỗng không
  static isEmpty() {
    return characterMap.size === 0;
  }
}

// ==== static block của Java ====
// Khởi tạo sẵn các phần tử (tên và giới tính) vào Map
// 1 đại diện cho nam 2 nữ
characterMap.set('viethung', 1);
characterMap.set('nghien96', 1);
characterMap.set('siiidoo', 1);
characterMap.set('vuarong99', 1);
characterMap.set('besen2k2', 2);
characterMap.set('nhimeomeo', 2);
characterMap.set('baodzvl', 1);
characterMap.set('taixiu', 1);
characterMap.set('shizuka', 2);
characterMap.set('trienn1', 1);
characterMap.set('soaicavn', 1);
characterMap.set('bestgamer', 1);
characterMap.set('hoahaiduong123', 2);
characterMap.set('cafesua12', 2);
characterMap.set('phuthuynho121', 2);
characterMap.set('giangcute199', 2);
characterMap.set('duylongok', 1);
characterMap.set('ngoctiem9x', 2);

characterMap.set('souuth', 1);
characterMap.set('sadboy', 1);
characterMap.set('dunglk2', 1);
characterMap.set('girldeptrai', 2);
characterMap.set('bedung2kk', 2);
characterMap.set('meomeo212', 2);
characterMap.set('Baodzvl1', 1);
characterMap.set('taixiu1', 1);
characterMap.set('shizukane', 2);
characterMap.set('nobinobi12', 1);
characterMap.set('soaicavn1', 1);
characterMap.set('nerverdje', 1);
characterMap.set('hoahuongduong198', 2);
characterMap.set('girlxinhjj', 2);
characterMap.set('x0000x', 2);
characterMap.set('giangcute1991', 2);
characterMap.set('lplplp0', 1);
characterMap.set('baongoc91', 2);

characterMap.set('bengoc097', 2);
characterMap.set('adugamevip', 1);
characterMap.set('0987888712', 2);
CharacterInfo.shuffleCharacterMap();

export default CharacterInfo;
