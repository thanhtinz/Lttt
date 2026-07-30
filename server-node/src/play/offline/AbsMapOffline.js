/**
 * Port của avatar/play/offline/AbsMapOffline.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
export class AbsMapOffline {

  constructor(id) {
    this.id = 0;
    this.npcs = [];
    this.setId(id);
    this.init();
  }

  /** abstract */
  init() {
    throw new Error('AbsMapOffline.init() is abstract');
  }

  addNpc(npc) {
    this.npcs.push(npc);
  }

  removeNpc(npc) {
    const idx = this.npcs.indexOf(npc);
    if (idx >= 0) {
      this.npcs.splice(idx, 1);
    }
  }

  // ==== getter/setter (lombok) ====
  getId() { return this.id; }
  setId(v) { this.id = v | 0; }

  getNpcs() { return this.npcs; }
}

export default AbsMapOffline;
