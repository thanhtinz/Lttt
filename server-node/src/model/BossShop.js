/** Port của avatar/model/BossShop.java (@Getter @Setter @Builder). */
export class BossShop {
  constructor(typeShop = 0, idBoss = 0, idShop = 0, name = null) {
    this.typeShop = (typeShop << 24) >> 24; // byte
    this.idBoss = idBoss | 0;
    this.idShop = (idShop << 24) >> 24;     // byte
    this.name = name;
  }

  getTypeShop() { return this.typeShop; }
  setTypeShop(v) { this.typeShop = (v << 24) >> 24; }
  getIdBoss() { return this.idBoss; }
  setIdBoss(v) { this.idBoss = v | 0; }
  getIdShop() { return this.idShop; }
  setIdShop(v) { this.idShop = (v << 24) >> 24; }
  getName() { return this.name; }
  setName(v) { this.name = v; }

  static builder() {
    const f = {};
    const b = { build: () => new BossShop(f.typeShop, f.idBoss, f.idShop, f.name) };
    for (const k of ['typeShop', 'idBoss', 'idShop', 'name']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default BossShop;
