/**
 * Port của avatar/play/MapManager.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
export class MapManager {

  static getInstance() {
    return mapManager;
  }

  constructor() {
    this.maps = [];
  }

  add(map) {
    this.maps.push(map);
  }

  remove(map) {
    const idx = this.maps.indexOf(map);
    if (idx >= 0) {
      this.maps.splice(idx, 1);
    }
  }

  find(id) {
    for (const map of this.maps) {
      if (map.getId() === id) {
        return map;
      }
    }
    return null;
  }

  getMaps() {
    return this.maps;
  }

  update() {
    this.maps.forEach((t) => {
      t.update();
    });
  }
}

export const mapManager = new MapManager();
export default mapManager;
