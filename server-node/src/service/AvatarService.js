/** Port của avatar/service/AvatarService.java */
import fs from 'fs';

import { Cmd } from '../constants/Cmd.js';
import { dbManager } from '../db/DbManager.js';
import { Part } from '../item/Part.js';
import { partManager } from '../item/PartManager.js';
import { MessageHandler } from '../message/MessageHandler.js';
import { gameData } from '../model/GameData.js';
import { foodManager } from '../model/FoodManager.js';
import { Menu } from '../model/Menu.js';
import { DataOutputStream } from '../net/JavaIO.js';
import { Message } from '../net/Message.js';
import { serverManager } from '../server/ServerManager.js';
import { userManager } from '../server/UserManager.js';
import { EffectService } from './EffectService.js';
import { Service } from './Service.js';

/** private static final java.util.Map<Integer, Long> lastActionTimes */
const lastActionTimes = new Map();
/** private static final long ACTION_COOLDOWN_MS = 50; // 2 giây cooldown */
const ACTION_COOLDOWN_MS = 50;

/** Tương đương Avatar.getFile(): đọc cả file, lỗi -> null (không làm sập server). */
function getFile(url) {
  try {
    return fs.readFileSync(url);
  } catch (e) {
    return null;
  }
}

export class AvatarService extends Service {

  constructor(cl) {
    super(cl);
    /** public User user; */
    this.user = null;
  }

  openUIShop(id, name, items) {
    try {
      console.log('openShop lent: ' + items.length);
      const ms = new Message(Cmd.OPEN_SHOP);
      const ds = ms.writer();
      ds.writeByte(id);
      ds.writeUTF(name);
      ds.writeShort(items.length);
      for (const i of items) {
        ds.writeShort(i.getId());
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('doRequestExpicePet ', ex);
    }
  }

  openUIShopEvent(bossShop, items) {
    try {
      console.log('openShop bossShop: ' + items.length);
      const ms = new Message(Cmd.BOSS_SHOP);
      const ds = ms.writer();
      ds.writeByte(bossShop.getTypeShop());
      ds.writeInt(bossShop.getIdBoss());
      ds.writeByte(bossShop.getIdShop());
      ds.writeUTF(bossShop.getName());
      ds.writeShort(items.length);
      for (const item of items) {
        ds.writeShort(item.getItemRequest());
        ds.writeUTF(item.initDialog(bossShop));
        if (bossShop.getTypeShop() === 1) {
          ds.writeUTF(item.initDialog(bossShop));
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('doRequestExpicePet ', ex);
    }
  }

  openUIBossShop(bossShop, items) {
    try {
      console.log('openShop bossShop: ' + items.length);
      const ms = new Message(Cmd.BOSS_SHOP);
      const ds = ms.writer();
      ds.writeByte(bossShop.getTypeShop());
      ds.writeInt(bossShop.getIdBoss());
      ds.writeByte(bossShop.getIdShop());
      ds.writeUTF(bossShop.getName());
      ds.writeShort(items.length);
      for (const item of items) {
        ds.writeShort(item.getItemRequest());
        ds.writeUTF(item.initDialog(bossShop));
        if (bossShop.getTypeShop() === 1) {
          ds.writeUTF(item.initDialog(bossShop));
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('doRequestExpicePet ', ex);
    }
  }

  doRequestExpicePet(mss) {
    try {
      const userID = mss.reader().readInt();
      const ms = new Message(Cmd.REQUEST_EXPICE_PET);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeByte(0);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('doRequestExpicePet ', ex);
    }
  }

  showUICreateChar(type) {
    try {
      const ms = new Message(Cmd.CREATE_CHAR_INFO);
      const ds = ms.writer();
      ds.writeByte(type);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('showUICreateChar ', ex);
    }
  }

  viewChest(chests) {
    try {
      const ms = new Message(Cmd.CONTAINER);
      const ds = ms.writer();
      ds.writeShort(chests.length);
      for (const item of chests) {
        ds.writeShort(item.getId());
        ds.writeByte(100 - item.reliability());
        ds.writeUTF(item.expiredString());
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('viewChest ', e);
    }
  }

  chatTo(sender, content, type) {
    try {
      const ms = new Message(Cmd.CHAT_TO);
      const ds = ms.writer();
      ds.writeInt(type);
      ds.writeUTF(sender);
      ds.writeUTF(content);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('chatTo ', ex);
    }
  }

  chatToUser(ms) {
    try {
      const receiverId = ms.reader().readInt();
      const content = ms.reader().readUTF();
      const senderId = this.session.user.getId(); // ID của người gửi yêu cầu
      const receiver = userManager.find(receiverId);
      // NOTE: giữ nguyên hành vi bản Java (tìm sender bằng receiverId - lỗi bản gốc)
      const sender = userManager.find(receiverId);
      receiver.getAvatarService().chatTo(sender.getUsername(), content, 1);
    } catch (ex) {
      console.error('chatTo ', ex);
    }
  }

  async onLoginSuccess() {
    try {
      const us = this.session.user;
      const wearing = us.getWearing();
      const listCmd = us.getListCmd();
      const listCmdRotate = us.getListCmdRotate();
      const ms5 = new Message(Cmd.LOGIN_SUCESS);
      const ds = ms5.writer();
      ds.writeInt(us.getId());
      ds.writeByte(wearing.length);
      for (const itm of wearing) {
        ds.writeShort(itm.getId());
      }
      ds.writeByte(us.getGender());
      ds.writeByte(us.getLeverMain());
      ds.writeByte(us.getLeverMainPercen());
      ds.writeInt(us.getXu() | 0);
      ds.writeByte(us.getFriendly());
      ds.writeByte(10);//us.getCrazy()
      ds.writeByte(100);//us.getStylish()
      ds.writeByte(100);//us.getHappy()
      ds.writeByte(100 - us.getHunger());
      ds.writeInt(us.getLuong());
      ds.writeByte(us.getStar());
      for (const itm of wearing) {
        ds.writeByte(1);
        ds.writeUTF(itm.expiredString());
      }
      const sql = 'SELECT c.icon, c.description FROM clan_members cm JOIN clans c ON cm.clan_id = c.id WHERE cm.user_id = ? AND cm.accept = 1';
      // NOTE: giữ nguyên hành vi bản Java (lỗi SQL bị bọc thành RuntimeException;
      // ở Java nó thoát khỏi hàm, ở Node bị catch chung bên dưới ghi log)
      const res = await dbManager.queryOne(sql, [us.getId()]);
      if (res != null) {
        // Nếu người dùng tham gia vào một clan, lấy thông tin
        const icon = (Number(res.icon) << 16) >> 16;
        const thongbaonhom = res.description;
        ds.writeShort(icon);  // Ghi ID icon của clan vào DataOutputStream
        us.getAvatarService().SendTabmsg('Thông báo nhóm: ' + thongbaonhom);  // Gửi thông báo nhóm
      } else {
        // Nếu không có kết quả (người dùng không tham gia clan nào)
        ds.writeShort(-1);  // Ghi giá trị mặc định -1 cho icon
        us.getAvatarService().SendTabmsg('Bạn chưa tham gia vào nhóm nào.');  // Gửi thông báo mặc định
      }

      ds.writeByte(listCmd.length);
      for (const cmd of listCmd) {
        ds.writeUTF(cmd.getName());
        ds.writeShort(cmd.getIcon());
      }
      ds.writeByte(listCmdRotate.length);
      for (const cmd of listCmdRotate) {
        ds.writeShort(cmd.getAnthor());
        ds.writeUTF(cmd.getName());
        ds.writeShort(cmd.getIcon());
      }
      ds.writeBoolean(true);// isTour
      for (const cmd of listCmdRotate) {
        ds.writeByte(cmd.getType());
      }
      ds.writeByte(1);
      ds.writeShort(us.getLeverMain());

      //hẹn hò
      if (us.getIdUsHenHo() !== 0 && us.getLevelMarry() === 0) {
        ds.writeShort(2);
        us.setTenNhan('Cặp đôi hẹn hò');
        us.setImginfo(1114);
      } else if (us.getLevelMarry() > 0 && us.getLevelMarry() < 5) {
        ds.writeShort(1153);
        us.setTenNhan('Cặp đôi mới cưới');
        us.setImginfo(1106);
      } else if (us.getLevelMarry() > 4 && us.getLevelMarry() < 10) {
        ds.writeShort(1154);
        us.setTenNhan('Cặp đôi gì đó lv hơn 5 dưới 10');
        us.setImginfo(1107);
      } else if (us.getLevelMarry() > 9 && us.getLevelMarry() < 15) {
        ds.writeShort(1155);
        us.setTenNhan('Cặp đôi gì đó lv hơn 10 dưới 15');
        us.setImginfo(1108);
      } else if (us.getLevelMarry() > 14 && us.getLevelMarry() < 20) {
        ds.writeShort(1156);
        us.setTenNhan('Cặp đôi gì đó lv hơn 15 dưới 20');
        us.setImginfo(1109);
      } else if (us.getLevelMarry() > 19 && us.getLevelMarry() < 24) {
        ds.writeShort(1157);
        us.setTenNhan('Cặp đôi gì đó lv hơn 5 dưới 10');
        us.setImginfo(1110);
      } else {
        ds.writeShort(-1);
      }

      ds.writeBoolean(this.session.isNewVersion());//new version
      if (this.session.isNewVersion()) {
        ds.writeInt(us.getXeng());
      }
      const m = 4;
      ds.writeByte(m);
      const IDAction = [103, 102, 104, 107];
      const actionName = ['Tặng Hoa Violet', 'Hôn', 'Tặng cánh hoa', 'Tặng Hoa Tuyết'];
      const IDIcon = [1124, 1188, 1187, 1173];
      const money = [20000, 2000, 10000, 5];
      const typeMoney = [0, 0, 0, 1];
      for (let i2 = 0; i2 < m; ++i2) {
        ds.writeShort(IDAction[i2]);
        ds.writeUTF(actionName[i2]);
        ds.writeShort(IDIcon[i2]);
        ds.writeInt(money[i2]);
        ds.writeByte(typeMoney[i2]);
      }
      ds.writeInt(us.getLuong());
      ds.writeInt(us.getLuongKhoa());
      ds.writeByte(1);
      ds.writeUTF(us.getUsername());
      ds.flush();
      this.sendMessage(ms5);

      us.getAvatarService().SendTabmsg('donate lần đầu trên 20k nhận được 5.000.000 xu và 10.000 lượng và 200 thẻ quay số miễn phí'
        + ' Và Auto Câu Cá');
      us.getAvatarService().SendTabmsg('update các loại cáo tiên ở shop nâng cấp chay to win, update pet labubu ở shop cày chay.');
      us.getAvatarService().SendTabmsg('update cách ngũ sắc chay và pay ok hết, có thể chọn nâng cấp 5 loại khác nhau');
      us.getAvatarService().SendTabmsg('den bu bao tri gift code : 14tieng');
    } catch (ex) {
      console.error('onLoginSuccess err', ex);
    }
  }

  SendTabmsg(content) {
    const ms = new Message(-6);
    const ds = ms.writer();
    ds.writeInt(1);
    ds.writeUTF('Admin');
    ds.writeUTF(content);
    ds.flush();
    this.session.sendMessage(ms);
  }

  getAvatarPart() {
    try {
      const parts = partManager.getAvatarPart();
      const ms = new Message(Cmd.GET_AVATAR_PART);
      const ds = ms.writer();
      ds.writeShort(parts.length);
      for (const part of parts) {
        ds.writeShort(part.getId());
        ds.writeInt(part.getCoin());
        ds.writeShort(part.getGold());
        const type = part.getType();
        ds.writeShort(type);
        switch (type) {
          case -2:
            ds.writeUTF(part.getName());
            ds.writeByte(part.getSell());
            ds.writeShort(part.getIcon());
            break;

          case -1: {
            ds.writeUTF(part.getName());
            ds.writeByte(part.getSell());
            ds.writeByte(part.getZOrder());
            ds.writeByte(part.getGender());
            ds.writeByte(part.getLevel());
            ds.writeShort(part.getIcon());
            const imgID = part.getImgID();
            const dx = part.getDx();
            const dy = part.getDy();
            for (let i = 0; i < 15; i++) {
              ds.writeShort(imgID[i]);
              ds.writeByte(dx[i]);
              ds.writeByte(dy[i]);
            }
            break;
          }

          default:
            ds.writeShort(part.getIcon());
            break;
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getAvatarPart() ', e);
    }
  }

  inspectMessageData(message) {
    const dis = message.reader();
    if (dis != null) {
      try {
        while (dis.available() > 0) {  // Vòng lặp cho đến khi hết dữ liệu
          try {
            const b = dis.readBoolean();
            console.log('Read int: ' + b);
            const intValue = dis.readInt();  // Thử đọc int
            console.log('Read int: ' + intValue);
          } catch (e) {
            // Nếu không phải int, hãy thử kiểu dữ liệu khác
            try {
              const stringValue = dis.readUTF();  // Thử đọc chuỗi
              console.log('Read string: ' + stringValue);
            } catch (ex) {
              try {
                const byteValue = dis.readByte();  // Thử đọc byte
                console.log('Read byte: ' + byteValue);
              } catch (exc) {
                console.log('Unknown data format or end of data.');
                break;  // Nếu tất cả các thử nghiệm đều thất bại, kết thúc vòng lặp
              }
            }
          }
        }
      } catch (e) {
        console.error('Error reading message data: ' + e.message);
      }
    } else {
      console.error('DataInputStream is null.');
    }
  }

  /**
   * Lấy thông tin item và giá tiền để in lên shop?
   */
  requestJoinAny(ms) {
    const id = ms.reader().readByte();
    const idSelectedMini = ms.reader().readByte();
    const idJoin = ms.reader().readShort();

    switch (idJoin) {
      // case 4:
      //     this.session.user.getAvatarService().serverDialog("đang xây dựng");
      //     break;
      case 5: {//Shop 1 hawai
        // Retrieve the shop items
        const items = Part.shopByPart(partManager.getShop1());

        if (items == null) {
          console.log('Items list is null');
          return; // Handle the null case
        }

        this.session.user.getAvatarService().openUIShop(5, 'shop 1', items);
        break;
      }
      case 9: {
        const itemshop2 = Part.shopByPart(partManager.getShop2());

        if (itemshop2 == null) {
          console.log('Items list is null');
          return; // Handle the null case
        }

        this.session.user.getAvatarService().openUIShop(5, 'shop 2', itemshop2);
        break;
      }
      case 18:
        this.session.user.getAvatarService().serverDialog('Biển Locity đang xây dựng vui lòng quay lại sau !');
        break;
      // Add more cases as needed
      default: {
        this.session.user.getZone().leave(this.session.user);

        ms = new Message(Cmd.JOIN_ONGAME_MINI);
        const ds = ms.writer();
        // ds.writeByte(1);
        // ds.writeByte(0);
        // ds.writeShort(4);
        this.session.sendMessage(ms);
        break;
      }
    }
  }

  requestPartDynaMic(ms) {
    try {
      const itemID = ms.reader().readShort();
      const part = partManager.findPartByID(itemID);
      // cmd -97
      ms = new Message(Cmd.REQUEST_DYNAMIC_PART);
      const ds = ms.writer();
      ds.writeShort(part.getId());
      ds.writeInt(part.getCoin());
      ds.writeShort(part.getGold());
      const type = part.getType();
      ds.writeShort(type);
      switch (type) {
        case -2:
          ds.writeUTF(part.getName());
          ds.writeByte(part.getSell());
          ds.writeShort(part.getIcon());
          break;

        case -1: {
          ds.writeUTF(part.getName());
          ds.writeByte(part.getSell());
          ds.writeByte(part.getZOrder());
          ds.writeByte(part.getGender());
          ds.writeByte(part.getLevel());
          ds.writeShort(part.getIcon());
          const imgID = part.getImgID();
          const dx = part.getDx();
          const dy = part.getDy();
          for (let i = 0; i < 15; i++) {
            ds.writeShort(imgID[i]);
            ds.writeByte(dx[i]);
            ds.writeByte(dy[i]);
          }
          break;
        }
        default:
          ds.writeShort(part.getIcon());
          break;
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('requestPartDynaMic() ', ex);
    }
  }

  enter(z) {
    try {
      const players = z.getPlayers();
      const map = z.getMap();
      const ms = new Message(Cmd.AVATAR_JOIN_PARK);
      const ds = ms.writer();
      ds.writeByte(map.getId());
      ds.writeByte(z.getId());
      ds.writeShort(-1);
      ds.writeShort(-1);
      const numUser = players.length;
      ds.writeByte(numUser);
      for (const pl of players) {
        ds.writeInt(pl.getId());
        ds.writeUTF(pl.getUsername());
        ds.writeByte(pl.getWearing().length);
        for (const item of pl.getWearing()) {
          ds.writeShort(item.getId());
        }
        ds.writeShort(pl.getX());
        ds.writeShort(pl.getY());
        ds.writeByte(pl.getRole());//0 la npc
      }
      for (const pl of players) {
        ds.writeByte(pl.getDirect());
      }
      for (let i = 0; i < numUser; ++i) {
        ds.writeByte(101);
      }
      for (let i = 0; i < numUser; ++i) {
        ds.writeShort(-1);
      }
      ds.writeByte(0);
      ds.writeByte(0);

      const mapItems = map.getMapItems();
      const mapItemTypes = map.getMapItemTypes();
      ds.writeShort(mapItems.length);
      ds.writeByte(mapItemTypes.length);
      for (const mapItemType of mapItemTypes) {
        ds.writeByte(mapItemType.getId());
        ds.writeShort(mapItemType.getImgID());
        ds.writeByte(mapItemType.getIconID());
        ds.writeShort(mapItemType.getDx());
        ds.writeShort(mapItemType.getDy());
        const positions = mapItemType.getListNotTrans();
        ds.writeByte(positions.length);
        for (const position of positions) {
          ds.writeByte(position.getX());
          ds.writeByte(position.getY());
        }
      }
      ds.writeByte(mapItems.length);
      for (const mapItem of mapItems) {
        ds.writeByte(mapItem.getType());
        ds.writeByte(mapItem.getTypeID());
        ds.writeByte(mapItem.getX());
        ds.writeByte(mapItem.getY());
      }
      for (let i = 0; i < numUser; ++i) {
        ds.writeShort(-1);
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('enter() ', ex);
    }
  }

  getImageData() {
    try {
      const imageInfos = gameData.getItemImageDatas();
      const ms = new Message(Cmd.GET_IMAGE);
      const ds = ms.writer();
      ds.writeShort(imageInfos.length);
      for (const imageInfo of imageInfos) {
        ds.writeShort(imageInfo.getId());
        ds.writeShort(imageInfo.getBigImageID());
        ds.writeByte(imageInfo.getX());
        ds.writeByte(imageInfo.getY());
        ds.writeByte(imageInfo.getW());
        ds.writeByte(imageInfo.getH());
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getImageData() ', e);
    }
  }

  getMapItemType() {
    try {
      console.log('get map item type');
      const mapItemTypes = gameData.getMapItemTypes();
      const ms = new Message(Cmd.MAP_ITEM_TYPE);
      const ds = ms.writer();
      ds.writeShort(mapItemTypes.length);
      for (const mapItemType of mapItemTypes) {
        ds.writeShort(mapItemType.getId());
        ds.writeUTF(mapItemType.getName());
        ds.writeUTF(mapItemType.getDes());
        ds.writeShort(mapItemType.getImgID());
        ds.writeShort(mapItemType.getIconID());
        ds.writeByte(mapItemType.getDx());
        ds.writeByte(mapItemType.getDy());
        ds.writeShort(mapItemType.getPriceXu());
        ds.writeShort(mapItemType.getPriceLuong());
        ds.writeByte(mapItemType.getBuy());
        const positions = mapItemType.getListNotTrans();
        ds.writeByte(positions.length);
        for (const p of positions) {
          ds.writeByte(p.getX());
          ds.writeByte(p.getY());
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getMapItemType() ', e);
    }
  }

  getTileMap() {
    try {
      const dat = getFile(this.session.getResourcesPath() + 'house/tile.png');
      if (dat == null) {
        return;
      }
      const ms = new Message(Cmd.GET_TILE_MAP);
      const ds = ms.writer();
      ds.writeShort(21);
      ds.writeInt(dat.length);
      ds.write(dat);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getTileMap() ', e);
    }
  }

  getMapItem() {
    try {
      console.log('get map item');
      const mapItems = gameData.getMapItems();
      const ms = new Message(Cmd.MAP_ITEM);
      const ds = ms.writer();
      ds.writeShort(mapItems.length);
      for (const mapItem of mapItems) {
        ds.writeShort(mapItem.getId());
        ds.writeShort(mapItem.getTypeID());
        ds.writeByte(mapItem.getType());
        ds.writeByte(mapItem.getX());
        ds.writeByte(mapItem.getY());
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getMapItem() ', e);
    }
  }

  getMapItems(ms) {
    try {
      const dat = getFile('res/data/map_item.dat');
      ms = new Message(-41);
      const ds = ms.writer();
      ds.write(dat);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  getMapItemTypes(ms) {
    try {
      const dat = getFile('res/data/map_item_type.dat');
      ms = new Message(-40);
      const ds = ms.writer();
      ds.write(dat);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  getBigImage(ms) {
    try {
      const id = ms.reader().readShort();
      const folder = this.session.getResourcesPath() + 'big/';
      const dat = getFile(folder + id + '.png');
      if (dat == null) {
        return;
      }
      ms = new Message(Cmd.GET_BIG);
      const ds = ms.writer();
      ds.writeShort(id);
      ds.writeShort(dat.length);
      ds.writeShort(dat.length);
      ds.write(dat);
      if (id > 20) {
        ds.writeShort(2);
      } else if (id > 10) {
        ds.writeShort(1);
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getBigImage() ', e);
    }
  }

  getBigData() {
    try {
      const ms = new Message(Cmd.SET_BIG);
      const ds = ms.writer();
      const dir = this.session.getResourcesPath() + 'big/';
      const listFiles = fs.readdirSync(dir);
      ds.writeByte(listFiles.length);
      for (const f of listFiles) {
        const name = f.split('.')[0];
        const id = parseInt(name, 10);
        const size = fs.statSync(dir + f).size | 0;
        ds.writeShort(id);
        ds.writeShort(size);
      }
      ds.writeShort(serverManager.bigImgVersion);
      ds.writeShort(serverManager.partVersion);
      ds.writeShort(serverManager.bigItemImgVersion);
      ds.writeShort(serverManager.itemTypeVersion);
      ds.writeShort(serverManager.itemVersion);
      ds.writeByte(0);
      ds.writeInt(serverManager.objectVersion);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('getBigData() ', ex);
    }
  }

  updateMoney(type) {
    try {
      const ms = new Message(Cmd.UPDATE_MONEY);
      const ds = ms.writer();
      ds.writeInt(this.session.user.xeng);
      ds.writeByte(type);
      ds.writeInt(this.session.user.xu | 0);
      ds.writeInt(this.session.user.luong);
      ds.writeInt(this.session.user.luongKhoa);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('updateMoney ', ex);
    }
  }

  /**
   * Gộp 2 overload của Java:
   *  openMenuOption(int, int, String... menus)
   *  openMenuOption(int, int, List<Menu> menus)
   */
  openMenuOption(userID, menuID, ...menus) {
    try {
      const isList = menus.length === 1 && Array.isArray(menus[0]);
      const list = isList ? menus[0] : menus;
      const ms = new Message(Cmd.MENU_OPTION);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeByte(menuID);
      ds.writeByte(list.length);
      for (const menu of list) {
        ds.writeUTF(isList ? menu.getName() : menu);
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('openMenuOption ', e);
    }
  }

  openUIMenu(userID, menuID, menus, npcName, npcChat) {
    try {
      const ms = new Message(Cmd.MENU_OPTION);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeByte(menuID);
      ds.writeByte(menus.length);
      for (const m of menus) {
        ds.writeUTF(m.getName());
      }
      for (const m of menus) {
        ds.writeShort(m.getId());
      }
      if (npcName != null) {
        ds.writeUTF(npcName);
        ds.writeUTF(npcChat);
        for (const m of menus) {
          ds.writeBoolean(m.isMenu());
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('openMenuOption ', e);
    }
  }

  requestYourInfo(us) {
    try {
      const ms = new Message(-22);
      const ds = ms.writer();
      ds.writeInt(us.getId());
      ds.writeByte(us.getLeverMain());
      ds.writeByte(us.getLeverMainPercen());
      ds.writeByte(us.getFriendly());
      ds.writeByte(0); //us.getCrazy()
      ds.writeByte(us.getStylish());
      ds.writeByte(us.getHappy());
      ds.writeByte(100 - us.getHunger());

      if (us.getIdUsHenHo() !== 0) {
        ds.writeInt(us.getIdUsHenHo());
      } else {
        ds.writeInt(-1);
        ds.writeShort(us.getLeverMain());
        ds.flush();
        this.sendMessage(ms);
        return;
      }

      //User us2 = UserManager.getInstance().find(1);
      ds.writeUTF(us.getNamehh());
      ds.writeByte(us.getWearingMarry().length);
      for (const item of us.getWearingMarry()) {
        ds.writeShort(item.getId());
      }

      ds.writeUTF(us.getTenNhan()); // Slogan
      ds.writeShort(us.getImginfo()); // idImage
      ds.writeByte(us.getLevelMarry()); // Level of avatar3
      ds.writeByte(us.getPerLevelMarry()); // Percent level of avatar3
      ds.writeUTF('text 2'); // Relationship
      ds.writeShort(1); // num23
      ds.writeUTF('text 3'); // Action name if num23 != -1

      ds.writeShort(us.getLeverMain());
      ds.flush();

      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  getFoodData() {
    try {
      const ms = new Message(Cmd.GET_ITEM_INFO);
      const ds = ms.writer();
      const foods = foodManager.getFoods();
      ds.writeShort(foods.length);
      for (const food of foods) {
        ds.writeShort(food.getId());
        ds.writeUTF(food.getName());
        ds.writeUTF(food.getDescription());
        ds.writeInt(food.getPrice());
        ds.writeByte(food.getShop());
        ds.writeShort(food.getIcon());
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('getFoodData ', e);
    }
  }

  customTab(title, content) {
    try {
      const ms = new Message(Cmd.CUSTOM_TAB);
      const ds = ms.writer();
      ds.writeByte(0);
      ds.writeUTF(title);
      ds.writeUTF(content);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('customTab ', ex);
    }
  }

  sellFish(us, idFIsh) {
    const item = us.findItemInChests(idFIsh);
    if (item != null && item.getQuantity() > 0) {
      const sell = item.getPart().getCoin();//*item.getQuantity()
      // NOTE: giữ nguyên hành vi bản Java (String.format có 5 tham số nhưng chỉ 3 chỗ thay)
      const message = `Bạn vừa bán ${item.getQuantity()} ${item.getPart().getName()} với giá = ${item.getPart().getCoin()} xu.`;
      us.removeItem(item.getId(), item.getQuantity());
      us.updateXu(+sell);
      us.getAvatarService().updateMoney(0);
      us.getAvatarService().SendTabmsg(message);
    }
  }

  sendEffectStyle4(id, loopLimit, num, timeStop) {
    try {
      const ms = new Message(Cmd.EFFECT_OBJ);
      const ds = ms.writer();
      ds.writeByte(0);
      ds.writeByte(id);
      ds.writeByte(4);
      ds.writeByte(loopLimit);
      ds.writeShort(num);
      ds.writeByte(timeStop);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('send eff ', ex);
    }
  }

  sendEffectData(mss) {
    try {
      const id = mss.reader().readByte();
      const folder = this.session.getResourcesPath() + 'effect/';
      const imageData = getFile(folder + id + '.png');
      const effData = getFile('res/data/effect/' + id + '.dat');

      const ms = new Message(Cmd.EFFECT_OBJ);
      const ds = ms.writer();
      ds.writeByte(1);
      ds.writeByte(id);
      ds.writeShort(imageData.length);
      ds.write(imageData);
      ds.write(effData);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  async HandlerMENU_ROTATE(us, mss) {
    try {
      const id = mss.reader().readShort();
      const ms = new Message(Cmd.REQUEST_YOUR_INFO);
      const ds = ms.writer();
      ds.writeShort(id);
      switch (id) {
        // case 1: {
        //     us.getAvatarService().openMenuOption(1000, 2,
        //             "Hủy hẹn hò ? : không",
        //             "Hủy hẹn hò ? : Có");
        //     break;
        // }
        case 4: {
          mss = new Message(Cmd.MENU_ROTATE);
          const ds1 = mss.writer();
          const num73 = 3;
          ds1.writeShort(3);
          let newMoney = 0;
          ds1.writeInt(newMoney);
          let typeBuy = 0;
          ds1.writeByte(typeBuy);
          if (num73 !== -1) {
            newMoney = 1;
            ds1.writeInt(newMoney);
            typeBuy = 0;
            ds1.writeByte(typeBuy);
          }
          const text5 = 'text';
          ds1.writeUTF(text5);
          const xu3 = 1;
          ds1.writeInt(xu3);
          const luong3 = 2;
          ds1.writeInt(luong3);
          const luongKhoa = 3;
          ds1.writeInt(luongKhoa);
          ds1.flush();
          this.session.sendMessage(mss);
          break;
        }
        //hẹn hò
        case 36: {
          us.getAvatarService().sendTextBoxPopup(us.getId(), 100, 'gửi lời mới hẹn hò tới ? (ghi tên nhân vật)', 0);
          break;
        }
        case 48: {
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(1)
              .style(0)
              .loopLimit(6)
              .loop(6)//so luong lap lai
              .loopType(1)
              .radius(6)
              .idPlayer(us.getId())
              .send();
          });
          break;
        }
        case 47: {
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(8)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(6)
              .idPlayer(us.getId())
              .send();
          });
          break;
        }
        case 8: {
          const currentTime = Date.now();
          const lastActionTime = lastActionTimes.has(us.getId()) ? lastActionTimes.get(us.getId()) : 0;

          if (currentTime - lastActionTime < ACTION_COOLDOWN_MS) {
            us.getAvatarService().serverDialog('Từ từ thôi bạn!');
            return;
          }
          // Cập nhật thời gian thực hiện hành động
          lastActionTimes.set(us.getId(), currentTime);
          if (us.getLuong() < 5) {
            us.getAvatarService().serverDialog('Bạn phải có trên 5 Lượng');
            return;
          }
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(16)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(6)
              .idPlayer(us.getId())
              .send();
          });

          await us.updateTopPhaoLuong(-5);
          us.getAvatarService().updateMoney(0);
          break;
        }
        case 35: {
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(46)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(5)
              .idPlayer(us.getId())
              .send();
          });
          break;
        }
        case 33: {
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(48)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(5)
              .idPlayer(us.getId())
              .send();
          });
          break;
        }
        case 34: {
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(45)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(5)
              .idPlayer(us.getId())
              .send();
          });
          break;
        }
        case 9: {
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(11)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(5)
              .idPlayer(us.getId())
              .send();
          });
          break;
        }
        case 10: {
          // Java: dựng gói CONTAINER chỉ có 1 int = 0 rồi tự xử lý
          const dos1 = new DataOutputStream();
          dos1.writeInt(0);//x
          dos1.flush();
          const data1 = dos1.toBuffer();
          const msgHandler = new MessageHandler(us.session);
          await msgHandler.onMessage(new Message(Cmd.CONTAINER, data1));
          break;
        }
        case 11: {
          const currentTime = Date.now();
          const lastActionTime = lastActionTimes.has(us.getId()) ? lastActionTimes.get(us.getId()) : 0;

          if (currentTime - lastActionTime < ACTION_COOLDOWN_MS) {
            us.getAvatarService().serverDialog('Từ từ thôi bạn!');
            return;
          }
          // Cập nhật thời gian thực hiện hành động
          lastActionTimes.set(us.getId(), currentTime);
          if (us.getXu() < 20000) {
            us.getAvatarService().serverDialog('Bạn phải có trên 20.000 Xu');
            return;
          }
          us.getZone().getPlayers().forEach((u) => {
            EffectService.createEffect()
              .session(u.session)
              .id(20)
              .style(0)
              .loopLimit(6)
              .loop(1)//so luong lap lai
              .loopType(1)
              .radius(6)
              .idPlayer(us.getId())
              .send();
          });
          await us.updateTopPhaoXu(-20000);
          us.getAvatarService().updateMoney(0);
          break;
        }
        case 23: {
          const ListDacBiet = [];
          ListDacBiet.push(Menu.builder().name('skill mặc định').action(() => {
            us.setUseSkill(0);
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Siêu Anh Hùng').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(1)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(1);
            } else {
              us.getAvatarService().serverDialog('Bạn phải mặc trên 3 món có dame của các set siêu anh hùng');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Cung').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(2)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(2);
            } else {
              us.getAvatarService().serverDialog('Bạn phải sử dụng demo 01001');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Thú Cưỡi').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(3)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(3);
            } else {
              us.getAvatarService().serverDialog('Bạn phải sử dụng demo 01002');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Máy Bay').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(4)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(4);
            } else {
              us.getAvatarService().serverDialog('Bạn phải sử dụng demo 01003');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Thiêu Đốt').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(5)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(5);
            } else {
              us.getAvatarService().serverDialog('Bạn phải sử dụng demo 01004');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Băng').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(6)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(6);
            } else {
              us.getAvatarService().serverDialog('Bạn phải sử dụng demo 01005');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('skill Hô Phong Hoán Vũ').action(() => {
            if (us.getListSkill() != null && us.getListSkill().includes(7)) {
              us.getAvatarService().serverDialog('Đổi thành công');
              us.setUseSkill(7);
            } else {
              us.getAvatarService().serverDialog('Bạn phải sử dụng demo 01005');
            }
          }).build());
          ListDacBiet.push(Menu.builder().name('Thoát').id(0).build());
          us.setMenus(ListDacBiet);
          us.getAvatarService().openMenuOption(0, 0, ListDacBiet);
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }
}

export default AvatarService;
