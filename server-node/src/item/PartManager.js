/** Port của avatar/item/PartManager.java — nạp dữ liệu item từ DB. */
import { dbManager } from '../db/DbManager.js';
import { Part } from './Part.js';
import { Item } from './Item.js';
import { farmItem } from '../farm/farmItem.js';
import { UpgradeItem } from '../model/UpgradeItem.js';

class PartManager {
  constructor() {
    this.farmItems = [];
    this.parts = [];
    this.upgradeItems = [];
    this.Shop0 = []; /// type 19 shop 1
    this.Shop1 = []; /// type 19 shop 1
    this.Shop2 = []; /// type 14 shop 2
  }

  static getInstance() {
    return partManager;
  }

  getFarmItems() { return this.farmItems; }
  getParts() { return this.parts; }
  getUpgradeItems() { return this.upgradeItems; }
  getShop0() { return this.Shop0; }
  getShop1() { return this.Shop1; }
  getShop2() { return this.Shop2; }

  findPartById(id) {
    return this.getParts().find((part) => part.getId() === id) ?? null;
  }

  async load() {
    this.parts.length = 0;
    this.Shop0.length = 0;
    this.Shop1.length = 0;
    this.Shop2.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `items`;');
      for (const rs of rows) {
        const id = rs.id | 0;
        const coin = rs.coin | 0;
        const gold = rs.gold | 0;
        const type = (rs.type << 16) >> 16;
        const name = rs.name;
        const icon = (rs.icon << 16) >> 16;
        const expiredDay = rs.expired_day | 0;
        const level = (rs.level << 24) >> 24;
        const sell = (rs.sell << 24) >> 24;
        const zOrder = (rs.zorder << 24) >> 24;
        const gender = (rs.gender << 24) >> 24;
        const imgID = new Int16Array(15);
        const dx = new Int8Array(15);
        const dy = new Int8Array(15);
        const animation = JSON.parse(rs.animation);
        const size = animation.length;
        for (let i = 0; i < size; i++) {
          const obj = animation[i];
          imgID[i] = obj.img;  // shortValue()
          dx[i] = obj.dx;      // byteValue()
          dy[i] = obj.dy;      // byteValue()
        }
        const mk = () => Part.builder().id(id)
          .coin(coin)
          .gold(gold)
          .type(type)
          .name(name)
          .icon(icon)
          .expiredDay(expiredDay)
          .level(level)
          .sell(sell)
          .zOrder(zOrder)
          .gender(gender)
          .imgID(imgID)
          .dx(dx)
          .dy(dy)
          .build();
        this.parts.push(mk());
        console.log('id: ' + id + ' name: ' + name);
        if (sell === 14 || sell === 8) {
          this.Shop0.push(mk()); // Add the individual Part object
        }
        if (sell === 19) {
          this.Shop1.push(mk());
        }
        if (sell === 17) {
          this.Shop2.push(mk());
        }
      }
    } catch (e) {
      console.error(e);
    }
    await this.loadUpgradeItemData();
    await this.loadFarmItemData();
  }

  async loadFarmItemData() {
    this.farmItems.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `farmitems`;');
      for (const rs of rows) {
        const id = rs.id | 0;
        const name = rs.name;
        const time = rs.so_phut_chin | 0;
        const quantity = rs.san_luong_khi_chin | 0;
        const sell = rs.gia_san_pham | 0;
        this.farmItems.push(farmItem
          .builder()
          .id(id)
          .name(name)
          .time(time)
          .quantity(quantity)
          .sell(sell)
          .build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  async loadUpgradeItemData() {
    this.upgradeItems.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `upgrade_item`;');
      for (const rs of rows) {
        const id = rs.id | 0;
        const itemId = rs.item_id | 0;
        const onlyLuong = (rs.is_only_luong | 0) === 1;
        const ratio = rs.ratio | 0;
        const itemNeed = rs.item_need | 0;
        const luong = rs.luong | 0;
        const xu = rs.xu | 0;
        const scores = rs.scores | 0;
        this.upgradeItems.push(UpgradeItem
          .builder()
          .id(id)
          .itemRequest(itemId)
          .itemNeed(itemNeed)
          .ratio(ratio)
          .luong(luong)
          .isOnlyLuong(onlyLuong)
          .xu(xu)
          .scores(scores)
          .item(new Item(itemId))
          .build());
      }
    } catch (e) {
      console.error(e);
    }
  }

  getAvatarPart() {
    return this.parts.filter((t) => t.getId() < 2000);
  }

  findPartByID(id) {
    for (const part of this.parts) {
      if (part.getId() === id) {
        return part;
      }
    }
    return null;
  }

  findFarmitemByID(id) {
    for (const it of this.farmItems) {
      if (it.getId() === id) {
        return it;
      }
    }
    return null;
  }
}

export const partManager = new PartManager();
export { PartManager };
export default partManager;
