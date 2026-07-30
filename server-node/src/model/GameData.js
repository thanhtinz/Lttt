/** Port của avatar/model/GameData.java — nạp dữ liệu tĩnh từ DB. */
import { dbManager } from '../db/DbManager.js';
import { ImageInfo } from './ImageInfo.js';
import { MapItem } from './MapItem.js';
import { MapItemType } from './MapItemType.js';
import { Position } from './Position.js';
import { farmItem } from '../farm/farmItem.js';

class GameData {
  constructor() {
    this.itemImageDatas = [];
    this.farmImageDatas = [];
    this.mapItems = [];
    this.mapItemTypes = [];
    this.farmItems = [];
  }

  static getInstance() {
    return gameData;
  }

  getItemImageDatas() { return this.itemImageDatas; }
  getFarmImageDatas() { return this.farmImageDatas; }
  getMapItems() { return this.mapItems; }
  getMapItemTypes() { return this.mapItemTypes; }
  getFarmItems() { return this.farmItems; }

  async load() {
    await this.loadItemImageData();
    await this.loadFarmImageData();
    await this.loadMapItem();
    await this.loadMapItemType();
    await this.loadItemfarm();
  }

  async loadItemfarm() {
    this.farmItems.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `farmitems`;');
      for (const rs of rows) {
        const id = rs.id | 0;
        const name = rs.name;
        const time = rs.time | 0;
        const quantity = rs.quantity | 0;
        const sell = rs.sell | 0;
        this.farmItems.push(farmItem.builder().id(id).name(name).time(time)
          .quantity(quantity).sell(sell).build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  async loadItemImageData() {
    this.itemImageDatas.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `avatar_img_data`;');
      for (const rs of rows) {
        const id = rs.item_id | 0;
        const bigImageID = rs.image_id | 0;
        const x = rs.x | 0;
        const y = rs.y | 0;
        const w = rs.w | 0;
        const h = rs.h | 0;
        this.itemImageDatas.push(
          ImageInfo.builder().id(id).bigImageID(bigImageID).x(x).y(y).w(w).h(h).build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  async loadFarmImageData() {
    this.farmImageDatas.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `farm_image_data`;');
      for (const rs of rows) {
        const id = rs.id | 0;
        const bigImageID = rs.image_id | 0;
        const x = rs.x | 0;
        const y = rs.y | 0;
        const w = rs.w | 0;
        const h = rs.h | 0;
        this.farmImageDatas.push(
          ImageInfo.builder().id(id).bigImageID(bigImageID).x(x).y(y).w(w).h(h).build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  async loadMapItem() {
    this.mapItems.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `map_item`;');
      for (const rs of rows) {
        const id = (rs.id << 16) >> 16;
        const typeID = (rs.type_id << 16) >> 16;
        const type = (rs.type << 24) >> 24;
        const x = (rs.x << 24) >> 24;
        const y = (rs.y << 24) >> 24;
        this.mapItems.push(MapItem.builder().id(id).typeID(typeID).type(type).x(x).y(y).build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  async loadMapItemType() {
    this.mapItemTypes.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `map_item_type`;');
      for (const rs of rows) {
        const id = (rs.id << 16) >> 16;
        const name = rs.name;
        const description = rs.description;
        const imageID = (rs.image << 16) >> 16;
        const iconID = (rs.icon << 16) >> 16;
        const priceCoin = (rs.price_coin << 16) >> 16;
        const priceGold = (rs.price_gold << 16) >> 16;
        const buy = (rs.buy << 24) >> 24;
        const dx = (rs.dx << 24) >> 24;
        const dy = (rs.dy << 24) >> 24;
        const jPosition = JSON.parse(rs.position);
        const size = jPosition.length;
        const positions = [];
        for (let i = 0; i < size; i++) {
          const obj = jPosition[i];
          const x = (obj.x << 24) >> 24;
          const y = (obj.y << 24) >> 24;
          const p = Position.builder()
            .x(x)
            .y(y)
            .build();
          positions.push(p);
        }
        this.mapItemTypes.push(MapItemType.builder().id(id).name(name).des(description)
          .imgID(imageID).iconID(iconID)
          .priceXu(priceCoin).priceLuong(priceGold).buy(buy).dx(dx).dy(dy)
          .listNotTrans(positions)
          .build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  findMapItemType(idType) {
    for (const mapItemType of this.mapItemTypes) {
      if (mapItemType.getId() === idType) {
        return mapItemType;
      }
    }
    return null;
  }
}

export const gameData = new GameData();
export { GameData };
export default gameData;
