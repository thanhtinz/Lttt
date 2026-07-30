/**
 * Port của avatar/play/offline/MapOfflineManager.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
import { ObjPremium } from './ObjPremium.js';
import { ObjPet } from './ObjPet.js';

export class MapOfflineManager {

  static getInstance() {
    return mapOfflineManager;
  }

  constructor() {
    this.maps = [];
    this.add(new ObjPremium(3));
    this.add(new ObjPet(4));
  }

  add(mapOffline) {
    this.maps.push(mapOffline);
  }

  remove(mapOffline) {
    const idx = this.maps.indexOf(mapOffline);
    if (idx >= 0) {
      this.maps.splice(idx, 1);
    }
  }

  find(id) {
    for (const mapOffline of this.maps) {
      if (mapOffline.getId() === id) {
        return mapOffline;
      }
    }
    return null;
  }
}

export const mapOfflineManager = new MapOfflineManager();
export default mapOfflineManager;
