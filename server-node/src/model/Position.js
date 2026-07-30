/** Port của avatar/model/Position.java (@Builder @AllArgsConstructor @Getter). */
export class Position {
  constructor(x = 0, y = 0) {
    this.x = (x << 24) >> 24; // byte
    this.y = (y << 24) >> 24; // byte
  }

  getX() { return this.x; }
  getY() { return this.y; }

  static builder() {
    const f = {};
    const b = { build: () => new Position(f.x, f.y) };
    for (const k of ['x', 'y']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default Position;
