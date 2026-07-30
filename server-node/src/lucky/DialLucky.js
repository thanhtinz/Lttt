// Port của avatar/lucky/DialLucky.java
import { itemConverter } from '../convert/ItemConverter.js';
import { dbManager } from '../db/DbManager.js';
import { Item } from '../item/Item.js';
import { RandomCollection } from '../lib/RandomCollection.js';
import { Gift } from '../model/Gift.js';
import { Utils } from '../server/Utils.js';
import { DialLuckyManager } from './DialLuckyManager.js';

/**
 * @author kitakeyos - Hoàng Hữu Dũng
 */
export class DialLucky {

  static ITEM = 1;
  static XU = 2;
  static XP = 3;
  static LUONG = 4;
  static ITEM2 = 5;

  constructor(type) {
    this.type = type;
    this.randomType = new RandomCollection();
    this.randomItem = new RandomCollection();
    this.randomItem2 = new RandomCollection();

    //randomType.add(55, ITEM2);
    this.randomType.add(18, DialLucky.ITEM);
    this.randomType.add(45, DialLucky.XU);
    this.randomType.add(36, DialLucky.XP);
    this.randomType.add(1, DialLucky.LUONG);

    //randomItem2.add
    // NOTE: giữ nguyên hành vi bản Java (constructor gọi load()); ở Node load() là async
    // nên chỉ kích hoạt và giữ promise để nơi khác có thể await (xem DialLuckyManager.load()).
    this.loadPromise = this.load().catch((ex) => {
      console.error('DialLucky.load', ex);
    });
  }

  getType() {
    return this.type;
  }

  async load() {
    let text = null;
    switch (this.type) {
      case DialLuckyManager.XU:
        text = 'SELECT * FROM `dial_lucky` WHERE `xu` = 1;';
        break;

      case DialLuckyManager.LUONG:
        text = 'SELECT * FROM `dial_lucky` WHERE `luong` = 1;';
        break;

      case DialLuckyManager.MIEN_PHI:
        text = 'SELECT * FROM `dial_lucky` WHERE `free` = 1;';
        break;
    }
    try {
      const rows = await dbManager.query(text);
      for (const rs of rows) {
        const itemID = rs.item_id | 0;
        const ratio = rs.ratio | 0;
        const item = new Item(itemID);
        this.randomItem.add(ratio, item);//Item add qs shop
      }
    } catch (ex) {
      console.error('DialLucky', ex);
    }
  }

  show(us) {
    const service = us.getAvatarService();
    const map = this.randomItem.getMap();
    const items = [];
    for (const item of map.values()) {
      const gender = item.getPart().getGender();
      if (!((gender === 2 || gender === 1) && (us.getGender() !== gender))) {
        items.push(item);
      }
    }
    service.openUIShop(100, 'Quay số', items);
  }

  doDial(us, itemID, degree) {
    const gifts = [];
    for (let i = 0; i < 3; i++) {
      const type = this.randomType.next();
      const gift = new Gift();
      gift.setType(type);

      if (type === DialLucky.ITEM) {
        let item = this.randomItem.next();

        const itemchestUser = us.findItemInChests(item.getId());
        if (itemchestUser != null && itemchestUser.getExpired() === -1) {
          console.error('Lỗi khi duyệt danh sách người dùng: ');
          break;
        }
        item = itemConverter.newItem(item);//Item item = new Item(itemCode, -1, 0)
        gift.setId(item.getId());

        if (item.getId() === itemID) {
          item.setExpired(-1);
          gift.setExpireDay(-1);
        } else {
          const time = Utils.getRandomInArray([3, 7, 15, 30]);
          item.setExpired(Date.now() + (86400000 * time));
          gift.setExpireDay(time);
        }
        us.addItemToChests(item);

      } else if (type === DialLucky.XU) {
        const xu = Utils.nextInt(1, 300) * 10;

        gift.setXu(xu);
        us.updateXu(xu);
        us.getAvatarService().updateMoney(0);
      } else if (type === DialLucky.XP) {
        const xp = Utils.nextInt(1, 10) * 10;
        gift.setXp(xp);
        us.addExp(xp);
      } else if (type === DialLucky.LUONG) {
      }

      gifts.push(gift);
    }
    us.getMapService().dialLucky(us, (degree << 16) >> 16, gifts);
  }
}

export default DialLucky;
