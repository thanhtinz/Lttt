/**
 * Port của avatar/play/NpcManager.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
export class NpcManager {

  static getInstance() {
    return npcManager;
  }

  constructor() {
    this.npcs = [];
  }

  add(npc) {
    this.npcs.push(npc);
  }

  remove(npc) {
    const idx = this.npcs.indexOf(npc);
    if (idx >= 0) {
      this.npcs.splice(idx, 1);
    }
  }

  find(map, zone, id) {
    for (const npc of this.npcs) {
      if (npc.getId() === id) {
        const z = npc.getZone();
        if (z != null && z.getId() === zone && z.getMap().getId() === map) {
          return npc;
        }
      }
    }
    return null;
  }

  getNpcs() {
    return this.npcs;
  }
}

export const npcManager = new NpcManager();
export default npcManager;
