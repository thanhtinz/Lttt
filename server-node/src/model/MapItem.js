/** Port của avatar/model/MapItem.java (@Builder @AllArgsConstructor @Getter). */
export class MapItem {
  constructor(id = 0, typeID = 0, type = 0, x = 0, y = 0) {
    this.id = (id << 16) >> 16;      // short
    this.typeID = (typeID << 16) >> 16; // short
    this.type = (type << 24) >> 24;   // byte
    this.x = (x << 24) >> 24;         // byte
    this.y = (y << 24) >> 24;         // byte
  }

  getId() { return this.id; }
  getTypeID() { return this.typeID; }
  getType() { return this.type; }
  getX() { return this.x; }
  getY() { return this.y; }

  static builder() {
    const f = {};
    const b = { build: () => new MapItem(f.id, f.typeID, f.type, f.x, f.y) };
    for (const k of ['id', 'typeID', 'type', 'x', 'y']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default MapItem;
