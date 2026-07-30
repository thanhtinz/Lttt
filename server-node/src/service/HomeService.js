/** Port của avatar/service/HomeService.java */
import { Cmd } from '../constants/Cmd.js';
import { dbManager } from '../db/DbManager.js';
import { Message } from '../net/Message.js';
import { Service } from './Service.js';

/** Lớp nội của Java: HomeService.Tile */
class Tile {
  constructor(name, xu, luong) {
    this.name = name;
    this.xu = xu | 0;
    this.luong = luong | 0;
  }
}

export class HomeService extends Service {

  constructor(cl) {
    super(cl);
  }

  async buyItemHouse(ms) {
    const itemId = ms.reader().readShort();
    const x = ms.reader().readByte();
    const y = ms.reader().readByte();
    const type = ms.reader().readByte();
    this.session.user.updateXu(-2000);
    this.session.getAvatarService().updateMoney(0);
    let result = 0;
    try {
      result = await dbManager.executeUpdate(
        'INSERT INTO `house_player_item` (`user_id`, `house_item_id`, `x`, `y`) VALUES (?, ?, ?, ?)',
        [this.session.user.getId(), itemId, x, y]);
    } catch (e) {
      console.error(e);
      console.error(e.message);
    }
    if (result > 0) {
      ms = new Message(-74);
      const ds = ms.writer();
      ds.writeShort(itemId);
      ds.writeByte(x);
      ds.writeByte(y);
      ds.flush();
      this.session.sendMessage(ms);
    }
  }

  async sortItemHouse(ms) {
    const anchor = ms.reader().readShort();
    const x = ms.reader().readByte();
    const y = ms.reader().readByte();
    const x2 = ms.reader().readByte();
    const y2 = ms.reader().readByte();
    const rotate = ms.reader().readByte();
    const UPDATE_HOUSE_ITEM = 'UPDATE `house_player_item` SET `x` = ?, `y` = ?, `rotate` = ? WHERE `user_id` = ? AND `house_item_id` = ? AND `x` = ? AND `y` = ? LIMIT 1';
    try {
      const result_update = await dbManager.executeUpdate(UPDATE_HOUSE_ITEM,
        [x2, y2, rotate, this.session.user.getId(), anchor, x, y]);
    } catch (e) {
      console.error(e);
      console.error(e.message);
    }
  }

  getTypeHouse(ms) {
    const typeHouse = ms.reader().readByte();
    console.log('typeHouse = ' + typeHouse);
    ms = new Message(-67);
    const ds = ms.writer();
    ds.writeByte(0);
    ds.writeShort(6299);
    ds.writeByte(3);
    ds.flush();
    this.session.sendMessage(ms);
  }

  async delItemHouse(ms) {
    const itemId = ms.reader().readShort();
    console.log('itemId = ' + itemId);
    const x = ms.reader().readByte();
    const y = ms.reader().readByte();
    const rotate = ms.reader().readByte();
    const INSERT_HOUSE_ITEM = 'DELETE FROM `house_player_item` WHERE `user_id` = ? AND `house_item_id` = ? AND `x` = ? AND `y` = ? LIMIT 1';
    try {
      await dbManager.executeUpdate(INSERT_HOUSE_ITEM,
        [this.session.user.getId(), itemId, x, y]);
    } catch (e) {
      console.error(e);
      console.error(e.message);
    }
    ms = new Message(-66);
    const ds = ms.writer();
    ds.writeShort(itemId);
    ds.writeByte(x);
    ds.writeByte(y);
    ds.flush();
    this.session.sendMessage(ms);
  }

  async createHome(ms) {
    try {
      const GET_HOUSE_DATA = 'SELECT * FROM `house_buy` WHERE `user_id` = ? LIMIT 1';
      const res = await dbManager.queryOne(GET_HOUSE_DATA, [this.session.user.getId()]);
      if (res != null) {
        const ja_map = JSON.parse(res.map_data);
        // Java: ((Long) ja_map.get(i)).byteValue() -> ép về byte có dấu
        const map_data = new Int8Array(ja_map.length);
        for (let i = 0; i < ja_map.length; ++i) {
          map_data[i] = ((Number(ja_map[i]) | 0) << 24) >> 24;
        }
        const type = ms.reader().readShort();
        const num = ms.reader().readShort();
        const map_data_new = new Int8Array(num);
        const tileChange = [];
        for (let j = 0; j < num; ++j) {
          map_data_new[j] = ms.reader().readByte();
          // NOTE: giữ nguyên hành vi bản Java (nếu num > độ dài map_data thì Java ném
          // ArrayIndexOutOfBounds; ở JS đọc ngoài mảng trả undefined nên chỉ khác biệt này)
          if (map_data_new[j] !== map_data[j]) {
            if (j < 519 || j > 522) {
              tileChange.push(map_data_new[j]);
              map_data[j] = map_data_new[j];
            }
          }
        }
        ms.reader().readShort();
        for (const tile of tileChange) {
          console.log('Change tile: ' + tile);
        }
        if (type === 1) {
          const ja_map_new = [];
          for (let k = 0; k < map_data.length; ++k) {
            ja_map_new.push(map_data[k]);
          }
          const UPDATE_HOUSE_MAP = 'UPDATE `house_buy` SET `map_data` = ? WHERE `user_id` = ?';
          const result_update = await dbManager.executeUpdate(UPDATE_HOUSE_MAP,
            [JSON.stringify(ja_map_new), this.session.user.getId()]);
          this.session.user.updateXu(-2000);
          this.session.user.getAvatarService().updateMoney(0);
          ms = new Message(-46);
          const ds = ms.writer();
          ds.writeShort(1);
          ds.writeUTF('Bạn đã lát gạch thành công và tốn 2000 xu và 0 lượng.');
          ds.flush();
          this.session.sendMessage(ms);
        } else {
          ms = new Message(-46);
          const ds2 = ms.writer();
          ds2.writeShort(0);
          ds2.writeUTF('Bạn cần 2000 xu và 0 lượng để lát gạch. Bạn có đồng ý không ?');
          ds2.flush();
          this.session.sendMessage(ms);
        }
      }
    } catch (e) {
      console.error(e);
    }
  }

  getImgObjInfo(ms) {
    const tiles = [];
    tiles.push(new Tile('BT', -1, -1));
    tiles.push(new Tile('VH', 100, -1));
    tiles.push(new Tile('VS1', 110, -1));
    tiles.push(new Tile('VS2', 150, -1));
    tiles.push(new Tile('CN', 120, -1));
    tiles.push(new Tile('CT1', 200, -1));
    tiles.push(new Tile('CT2', 220, -1));
    tiles.push(new Tile('GH', 240, -1));
    tiles.push(new Tile('TBN', 250, -1));
    tiles.push(new Tile('T', -1, 1));
    tiles.push(new Tile('D', -1, -1));
    tiles.push(new Tile('LT', -1, -1));
    tiles.push(new Tile('CS', -1, -1));
    tiles.push(new Tile('GN', 1000, -1));
    tiles.push(new Tile('GD', -1, 1));
    tiles.push(new Tile('KX1', 1000, -1));
    tiles.push(new Tile('KX2', -1, -1));
    tiles.push(new Tile('DR', -1, 1));
    tiles.push(new Tile('BT', 1500, -1));
    tiles.push(new Tile('KT', -1, -1));
    tiles.push(new Tile('KX3', -1, -1));
    tiles.push(new Tile('tV', 100, -1));
    tiles.push(new Tile('ctV', 500, -1));
    tiles.push(new Tile('tX', 150, -1));
    tiles.push(new Tile('ctX', 700, -1));
    tiles.push(new Tile('tH', 200, -1));
    tiles.push(new Tile('ctH', 900, -1));
    tiles.push(new Tile('tXD', 250, -1));
    tiles.push(new Tile('ctXD', 1100, -1));
    tiles.push(new Tile('tXR', -1, -1));
    tiles.push(new Tile('ctXR', -1, -1));
    tiles.push(new Tile('tXB', -1, -1));
    tiles.push(new Tile('ctXB', -1, -1));
    tiles.push(new Tile('gạch x', -1, -1));
    tiles.push(new Tile('', -1, -1));
    tiles.push(new Tile('', -1, -1));
    tiles.push(new Tile('', -1, -1));
    tiles.push(new Tile('', -1, -1));
    tiles.push(new Tile('', -1, -1));
    tiles.push(new Tile('', -1, -1));
    tiles.push(new Tile('', -1, -1));
    ms = new Message(-43);
    const ds = ms.writer();
    ds.writeShort(tiles.length);
    for (const tile of tiles) {
      ds.writeUTF(tile.name);
      ds.writeInt(tile.xu);
      ds.writeInt(tile.luong);
    }
    ds.flush();
    this.session.sendMessage(ms);
  }

  onCustomChest(ms) {
    const lstChest = this.session.user.chests;

    const lstChestHome = this.session.user.chestsHome;

    ms = new Message(Cmd.CUSTOM_CHEST);
    const ds = ms.writer();
    ds.writeShort(lstChest.length);
    for (const item of lstChest) {
      if (item.getId() !== 40) {
        ds.writeShort(item.getId());
        ds.writeByte(0);
        ds.writeUTF('');
      }
    }
    ds.writeInt(0);
    ds.writeByte(1);

    ds.writeShort(lstChestHome.length);
    for (const item of lstChestHome) {
      ds.writeShort(item.getId());
      ds.writeByte(0);
      ds.writeUTF('');
    }

    ds.flush();
    this.session.sendMessage(ms); // Gửi thông điệp tới client
  }

  transPartChest(ms) {
    const i = ms.reader().readByte();// 0 chest to chesthome , 1 = chestHomeToChest
    const ii = ms.reader().readShort();// index
    const Itemid = ms.reader().readShort();
    if (i === 0) {
      const Home = this.session.user.findItemInChestsHome(Itemid);
      const chest = this.session.user.findItemInChests(Itemid);

      if (chest == null || chest.getId() === 593 || chest.getId() === 683 || chest.getPart().getType() === -2) {
        this.session.user.getAvatarService().serverDialog('error -004');
        return;
      }
      // NOTE: giữ nguyên hành vi bản Java (nhánh này là code chết vì đã return ở trên)
      if (chest == null) {
        this.session.user.getAvatarService().serverDialog('error -001');
        return;
      }

      if (chest != null && Home != null) {
        this.session.user.getAvatarService().serverDialog('error -002');
        return;
      }
      if (chest.getQuantity() > 1 || chest.getExpired() !== -1) {
        this.session.user.getAvatarService().serverDialog('error -003');
        return;
      }

      const Slot = this.session.user.getChestHomeSlot() <= this.session.user.chestsHome.length ? false : true;
      if (Slot) {
        this.session.user.removeItemFromChests(chest);
        this.session.user.addItemToChestsHome(chest);
      }

      ms = new Message(Cmd.TRANS_PART_CHEST);
      const ds = ms.writer();
      ds.writeBoolean(Slot);
      ds.writeUTF('Rương nhà đã đầy !');
      ds.flush();
      this.session.sendMessage(ms); // Gửi thông điệp tới client
    } else {
      const Home = this.session.user.findItemInChestsHome(Itemid);
      const chest = this.session.user.findItemInChests(Itemid);

      if (chest != null && Home != null) {
        this.session.user.getAvatarService().serverDialog('error -2');
        return;
      }

      const Slot = this.session.user.getChestSlot() <= this.session.user.chests.length ? false : true;
      if (Slot) {
        // NOTE: giữ nguyên hành vi bản Java (Home có thể null ở đây)
        this.session.user.removeItemFromChestsHome(Home);
        this.session.user.addItemToChests(Home);
      }

      ms = new Message(Cmd.TRANS_PART_CHEST);
      const ds = ms.writer();
      ds.writeBoolean(Slot);
      ds.writeUTF('Rương đồ đã đầy!');
      ds.flush();
      this.session.sendMessage(ms); // Gửi thông điệp tới client
    }
  }

  upgradeChestHome(ms) {
    const chestSlots = [10, 15, 20, 25, 30, 35, 40, 45, 50, 55]; // Cấp 1 = 10 ô, cấp 2 = 15 ô, ..., cấp 5 = 30 ô
    const chestUpgradeCostXu = [10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000, 3000000, 5000000]; // Chi phí nâng cấp bằng xu cho từng cấp
    const chestUpgradeCostLuong = [10, 20, 30, 40, 50, 100, 200, 500, 800, 1200]; // Chi phí nâng cấp bằng lượng cho từng cấp

    // Giả sử bạn có phương thức để lấy cấp độ rương hiện tại

    const currentChestLevel = this.session.user.getChestLevel(); // Ví dụ: cấp 1, 2, 3, ...

    if (currentChestLevel >= chestSlots.length - 1) {
      this.session.getAvatarService().serverDialog('Rương của bạn đã được nâng cấp tối đa!');
      return;
    }

    const type = ms.reader().readByte();
    if (type === 0) {
      const nextLevel = currentChestLevel + 1; // Cấp độ rương tiếp theo
      const nextSlots = chestSlots[nextLevel - 1]; // Số ô sau khi nâng cấp
      const upgradeCostXu = chestUpgradeCostXu[nextLevel - 1]; // Giá xu cần nâng cấp
      const upgradeCostLuong = chestUpgradeCostLuong[nextLevel - 1]; // Giá lượng cần nâ

      ms = new Message(Cmd.UPGRADE_CHEST);
      const ds = ms.writer();
      ds.writeByte(0);
      ds.writeUTF('Bạn có muốn nâng cấp rương nhà từ cấp ' + currentChestLevel + ' lên cấp ' + nextLevel
        + ' (' + nextSlots + ' ô) bằng ' + upgradeCostXu + ' xu và ' + upgradeCostLuong + ' lượng không?');
      ds.flush();
      this.session.sendMessage(ms); // Gửi thông điệp tới client
    } else {
      // Kiểm tra cấp độ rương hiện tại

      const upgradeCostXu = chestUpgradeCostXu[currentChestLevel]; // Giá xu cần nâng cấp
      const upgradeCostLuong = chestUpgradeCostLuong[currentChestLevel]; // Giá lượng cần nâng cấp

      // Kiểm tra người chơi có đủ xu hoặc lượng để nâng cấp không
      if (this.session.user.xu >= upgradeCostXu && this.session.user.luong >= upgradeCostLuong) {
        this.session.user.updateXu(-upgradeCostXu); // Trừ xu người chơi
        this.session.user.getAvatarService().updateMoney(0);
        this.session.user.updateLuong(-upgradeCostLuong); // Trừ xu người chơi
        this.session.user.getAvatarService().updateMoney(0);
        this.session.user.updateChest_homeSlot(+5);
        this.session.getAvatarService().serverDialog('Đã nâng cấp thành công rương cấp ' + (currentChestLevel + 1) + ' (' + chestSlots[currentChestLevel] + ' ô)');
      } else {
        this.session.getAvatarService().serverDialog('Bạn không đủ xu hoặc lượng để nâng cấp rương!');
      }
    }
  }
}

export default HomeService;
