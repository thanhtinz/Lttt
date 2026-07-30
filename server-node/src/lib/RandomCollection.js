/**
 * Port của avatar/lib/RandomCollection.java.
 * Java dùng NavigableMap<Double,E> (TreeMap); ở đây các mốc `total` luôn tăng dần
 * nên dùng mảng khoá + Map để mô phỏng, `higherEntry` = khoá đầu tiên > value.
 */
export class RandomCollection {
  constructor(random) {
    // Java: new Random(); ở Node dùng Math.random() nếu không truyền vào
    this.random = random || { nextDouble: () => Math.random() };
    this.map = new Map();
    this._keys = [];
    this.total = 0;
  }

  getMap() {
    return this.map;
  }

  add(weight, result) {
    if (weight <= 0) {
      return this;
    }
    this.total += weight;
    this.map.set(this.total, result);
    this._keys.push(this.total);
    return this;
  }

  isEmpty() {
    return this.map.size === 0;
  }

  /** Tìm entry có khoá lớn hơn `value` (NavigableMap.higherEntry). */
  higherEntry(value) {
    let lo = 0;
    let hi = this._keys.length - 1;
    let idx = -1;
    while (lo <= hi) {
      const mid = (lo + hi) >> 1;
      if (this._keys[mid] > value) {
        idx = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    if (idx < 0) return null;
    const key = this._keys[idx];
    return { key, value: this.map.get(key), getKey: () => key, getValue: () => this.map.get(key) };
  }

  next() {
    const value = this.random.nextDouble() * this.total;
    return this.higherEntry(value).getValue();
  }
}

export default RandomCollection;
