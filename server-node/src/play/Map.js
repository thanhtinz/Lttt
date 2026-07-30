/** Port của avatar/play/Map.java */
import { dbManager } from '../db/DbManager.js';
import { GameData } from '../model/GameData.js';
import { MapItem } from '../model/MapItem.js';
import { Zone } from './Zone.js';

export class Map {

  /**
   * NOTE: bản Java gọi load() ngay trong constructor. Ở Node load() đọc DB nên là
   * async -> người tạo Map phải `await map.load()` (hoặc dùng Map.create()).
   */
  constructor(id, type, maxEntrys) {
    this.id = (id << 24) >> 24;
    this.type = (type << 24) >> 24;
    this.name = null;
    this.zones = [];
    this.mapItems = [];
    this.mapItemTypes = [];
    for (let i = 0; i < maxEntrys; ++i) {
      this.zones.push(new Zone(this, (i << 24) >> 24));
    }
  }

  /** Tạo Map giống constructor Java (load() xong mới trả về). */
  static async create(id, type, maxEntrys) {
    const map = new Map(id, type, maxEntrys);
    await map.load();
    return map;
  }

  async load() {
    try {
      const rows = await dbManager.query('SELECT * FROM `map_item` WHERE `map_id` = ?;', [this.id]);
      for (const rs of rows) {
        const id = rs.id | 0;
        const idType = rs.type_id | 0;
        const type = rs.type | 0;
        const x = rs.x | 0;
        const y = rs.y | 0;
        const mapItem = MapItem.builder()
          .id((id << 16) >> 16)
          .type((type << 24) >> 24)
          .typeID((idType << 16) >> 16)
          .x((x << 24) >> 24)
          .y((y << 24) >> 24)
          .build();
        const mapItemType = GameData.getInstance().findMapItemType(idType);
        this.mapItems.push(mapItem);
        this.mapItemTypes.push(mapItemType);
      }
    } catch (ex) {
      console.error('Map.load()', ex);
    }
  }

  update() {

  }

  // ==== getter/setter (lombok @Getter @Setter) ====
  getId() { return this.id; }
  setId(v) { this.id = (v << 24) >> 24; }

  getType() { return this.type; }
  setType(v) { this.type = (v << 24) >> 24; }

  getName() { return this.name; }
  setName(v) { this.name = v; }

  getZones() { return this.zones; }
  setZones(v) { this.zones = v; }

  getMapItems() { return this.mapItems; }
  setMapItems(v) { this.mapItems = v; }

  getMapItemTypes() { return this.mapItemTypes; }
  setMapItemTypes(v) { this.mapItemTypes = v; }
}

export default Map;
