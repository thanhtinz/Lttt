/** Port của avatar/model/Gift.java (@AllArgsConstructor @Getter @Setter @Builder @NoArgsConstructor). */
export class Gift {
  constructor(id = 0, type = 0, xu = 0, xp = 0, luong = 0, expireDay = 0) {
    this.id = id | 0;
    this.type = (type << 24) >> 24; // byte
    this.xu = xu | 0;
    this.xp = xp | 0;
    this.luong = luong | 0;
    this.expireDay = expireDay | 0;
  }

  getId() { return this.id; }
  setId(v) { this.id = v | 0; }
  getType() { return this.type; }
  setType(v) { this.type = (v << 24) >> 24; }
  getXu() { return this.xu; }
  setXu(v) { this.xu = v | 0; }
  getXp() { return this.xp; }
  setXp(v) { this.xp = v | 0; }
  getLuong() { return this.luong; }
  setLuong(v) { this.luong = v | 0; }
  getExpireDay() { return this.expireDay; }
  setExpireDay(v) { this.expireDay = v | 0; }

  static builder() {
    const f = {};
    const b = { build: () => new Gift(f.id, f.type, f.xu, f.xp, f.luong, f.expireDay) };
    for (const k of ['id', 'type', 'xu', 'xp', 'luong', 'expireDay']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default Gift;
