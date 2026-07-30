/** Port của avatar/service/ParkService.java */
import { Cmd } from '../constants/Cmd.js';
import { dbManager } from '../db/DbManager.js';
import { Item } from '../item/Item.js';
import { RandomCollection } from '../lib/RandomCollection.js';
import { ParkMsgHandler } from '../message/ParkMsgHandler.js';
import { Fish } from '../model/Fish.js';
import { Message } from '../net/Message.js';
import { userManager } from '../server/UserManager.js';
import { Utils } from '../server/Utils.js';
import { Service } from './Service.js';

/** private static final Fish a = new Fish(); */
const a = new Fish();

export class ParkService extends Service {

  constructor(cl) {
    super(cl);
    this.randomItemList1 = new RandomCollection();
    this.time = 0; // short time;
    this.randomItemList1.add(80, 2383);//nro
    this.randomItemList1.add(20, 2384);
  }

  /// le duong
  WEDDING_BIGINHanlder(user, ms) {
    try {
      const ms1 = new Message(Cmd.WEDDING_BIGIN);
      const ds = ms1.writer();
      ds.writeInt(7);
      ds.writeInt(1);//id girl
      ds.flush();
      user.getZone().getPlayers().forEach((u) => {
        u.session.sendMessage(ms1);
      });
      this.accceptMarry(user.getId());
      user.setLevelMarry(1);
    } catch (e) {
      console.error(e);
    }
  }

  async accceptMarry(IDuserNam) {
    const updateQuery = 'UPDATE marry SET level = ?, perLevel = ? WHERE idNam = ?';
    try {
      await dbManager.executeUpdate(updateQuery, [1, 0, IDuserNam]);
    } catch (e) {
      console.error(e);
    }
  }

  handleAddFriendRequest(ms) {
    try {
      const userId = ms.reader().readInt(); // id người nhận
      const user = userManager.find(userId);
      ms = new Message(-19);
      const ds = ms.writer();
      ds.writeInt(userId);
      ds.writeBoolean(false);
      ds.flush();
      user.session.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  async handleStartFishing(ms) {
    try {
      if (!this.CheckItemAreaFish(460, 'bạn phải vé câu cá mập')) {
        return;
      }
      if (!this.CheckItemAreaFish(446, 'bạn phải có cần câu vip')) {
        return;
      }

      if (this.session.user.AutoFish) {
        const dataQuangCau = Buffer.alloc(0);
        const parkMsgHandler1 = new ParkMsgHandler(this.session);
        await parkMsgHandler1.onMessage(new Message(Cmd.QUANG_CAU, dataQuangCau));
      }

      // if(!CheckItemAreaFish(448,"bạn phải có mồi câu cá")){
      //     return;
      // }
      // Item MoiCau = this.session.user.findItemInChests(448);
      // if(MoiCau!=null){
      //     this.session.user.removeItemFromChests(MoiCau);
      // }
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }

  CheckItemAreaFish(ItemID, messenger) {
    const response = new Message(Cmd.START_CAU_CA);
    const ds = response.writer();
    let isSuccess = true;
    let item = null;
    if (ItemID === 446) {
      item = this.session.user.findItemInWearing(ItemID);
    } else {
      item = this.session.user.findItemInChests(ItemID);
    }
    if (item == null) {
      isSuccess = false;
      ds.writeBoolean(isSuccess);
      ds.writeUTF(messenger);
      ds.flush();
      this.sendMessage(response);
      return false;
    }
    ds.writeBoolean(isSuccess);
    ds.writeUTF('');
    ds.flush();
    this.sendMessage(response);
    return true;
  }

  handleQuangCau(ms) {
    try {
      const item = this.session.user.findItemInChests(448);
      if (item == null) {
        if (this.session.user.AutoFish) {
          const moi = new Item(448, -1, 1);
          this.session.user.addItemToChests(moi);
          this.session.user.updateXu(-30);
          this.session.user.getAvatarService().updateMoney(0);
        } else {
          this.session.user.getAvatarService().serverDialog('Hết mồi rồi sếp');
          return;
        }
      }
      // NOTE: giữ nguyên hành vi bản Java (item có thể null ở đây khi AutoFish)
      this.session.user.removeItemFromChests(item);
      const userID = this.session.user.getId();
      const response = new Message(Cmd.QUANG_CAU);
      const ds = response.writer();
      ds.writeInt(userID);
      ds.flush();
      this.sendMessage(response);
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }

  onStatusFish() {
    try {
      const userID = this.session.user.getId();
      const ms = new Message(Cmd.STATUS_FISH);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeByte(1);//ca can cau
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }

  onCanCau() {
    try {
      //us = UserManager.getInstance().find(this.session.user.getId());
      const idFish = (a.getRandomFishID() << 16) >> 16; // (short)
      this.session.user.setIdFish(idFish);
      this.time = 3000;
      if (this.session.user.AutoFish) {
        this.time = 20;
      }
      if (idFish < 0) {
        this.time = -1;
        this.session.user.setIdFish(idFish);
      }
      const ms = new Message(Cmd.CAN_CAU);
      const ds = ms.writer();
      ds.writeInt(this.session.user.getId());
      ds.writeShort(this.session.user.getIdFish());
      ds.writeShort(this.time);
      // Java: random.nextInt((12 - 6) + 1) + 4  ⇒ nextInt(7) + 4
      const randomNumber = Math.floor(Math.random() * 7) + 4;
      ds.writeByte(randomNumber);
      for (let i = 0; i < randomNumber; i++) {
        const randomIndex = Math.floor(Math.random() * a.images.length);
        const randomImage = a.images[randomIndex];
        ds.writeShort(randomImage.length);
        ds.write(randomImage);
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }

  async CauThanhCong() {
    try {
      const userID = this.session.user.getId();
      const ms = new Message(Cmd.CAU_THANH_CONG);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeShort(this.session.user.getIdFish());
      const IDFISH = this.session.user.getIdFish();
      if (IDFISH > 0) {
        const item = new Item(IDFISH, -1, 1);
        this.session.user.addItemToChests(item);
        this.session.user.getAvatarService().sellFish(this.session.user, item.getId());
        if (IDFISH === 457) {
          const keoAcMa = new Item(6822, -1, 1);
          if (this.session.user.findItemInChests(6822) != null) {
            const quantity = this.session.user.findItemInChests(6822).getQuantity();
            this.session.user.findItemInChests(6822).setQuantity(quantity + 1);
          } else {
            this.session.user.addItemToChests(keoAcMa);
          }
          userManager.users.forEach((user) => {
            user.getAvatarService().serverInfo('Chúc mừng bạn : ' + this.session.user.getUsername() + ' đã câu được 1 Cá Mập');
          });
          Utils.writeLogCaMap(this.session.user, 'bú 1 cá mập');
        }
        this.addVatPhamSuKienFish(this.session.user);
      }
      ds.flush();
      this.sendMessage(ms);

      if (this.session.user.AutoFish) {
        this.CauCaXong();
        const dataQuangCau = Buffer.alloc(0);
        const parkMsgHandler1 = new ParkMsgHandler(this.session);
        await parkMsgHandler1.onMessage(new Message(Cmd.QUANG_CAU, dataQuangCau));
      }
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }

  addVatPhamSuKienFish(us) {
    if (this.session.user.getCrazy() >= 1000) {
      return;
    }
    const ok = (Utils.nextInt(100) < 70) ? 1 : 0;
    if (ok === 1) {
      const chosenItemCollection = this.chooseItemCollection();
      const idItems = chosenItemCollection.next();
      const Nro = new Item(idItems, -1, 1);
      if (us.findItemInChests(idItems) != null) {
        const quantity = us.findItemInChests(idItems).getQuantity();
        us.findItemInChests(idItems).setQuantity(quantity + 1);
      } else {
        us.addItemToChests(Nro);
        us.updateCrazy(+1);
      }
      us.getAvatarService().SendTabmsg('Bạn vừa nhận được 1 ' + ' ' + Nro.getPart().getName());
    }
  }

  chooseItemCollection() {
    const itemCollections = new RandomCollection();
    itemCollections.add(100, this.randomItemList1);
    return itemCollections.next();
  }

  onInfoFish() {
    try {
      const ms = new Message(Cmd.INFO_FISH);
      const ds = ms.writer();
      ds.writeInt(this.session.user.getId());
      ds.writeByte(1);
      ds.writeByte(1);
      ds.writeInt(1);
      ds.writeShort(457);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }

  CauCaXong() {
    try {
      const userID = this.session.user.getId();
      const ms = new Message(Cmd.CAU_CA_XONG);
      const IDFISH = this.session.user.getIdFish();
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeInt(IDFISH);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('handleStartFishing() ', ex);
    }
  }
}

export default ParkService;
