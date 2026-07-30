/**
 * Port phần XỬ LÝ LỆNH GAME của avatar/network/Session.java.
 *
 * Phần MẠNG (constructor socket, sendMessage, handshake, readKey/writeKey...) đã
 * nằm ở net/Session.js — file này chỉ kế thừa và thêm các phương thức mà bộ
 * phân phối (message/MessageHandler.js) gọi trên session.
 *
 * Quy ước: giữ nguyên tên phương thức Java (kể cả tên tiếng Việt), giữ nguyên
 * thứ tự/kiểu đọc-ghi gói tin, giữ nguyên SQL. Hàm nào chạm DB thì thành async.
 */
import fs from 'fs';

import { Cmd } from '../constants/Cmd.js';
import { NpcName } from '../constants/NpcName.js';
import dbManager from '../db/DbManager.js';
import { Message } from './Message.js';
import { Session } from './Session.js';
import serverManager from '../server/ServerManager.js';
import userManager from '../server/UserManager.js';
import { Utils } from '../server/Utils.js';

import { User } from '../model/User.js';
import { Npc } from '../model/Npc.js';
import { Menu } from '../model/Menu.js';
import { CreateChar } from '../model/CreateChar.js';
import { foodManager } from '../model/FoodManager.js';
import { Item } from '../item/Item.js';
import { partManager } from '../item/PartManager.js';
import { HouseItem } from '../play/HouseItem.js';
import { mapManager } from '../play/MapManager.js';
import { npcManager } from '../play/NpcManager.js';
import { mapOfflineManager } from '../play/offline/MapOfflineManager.js';
import { DialLuckyManager } from '../lucky/DialLuckyManager.js';

import { AvatarService } from '../service/AvatarService.js';
import { FarmService } from '../service/FarmService.js';
import { HomeService } from '../service/HomeService.js';
import { ParkService } from '../service/ParkService.js';
import { Service } from '../service/Service.js';
import { EffectService } from '../service/EffectService.js';

import { GlobalHandler } from '../handler/GlobalHandler.js';
import { UpgradeItemHandler } from '../handler/UpgradeItemHandler.js';
import { BossShopHandler } from '../handler/BossShopHandler.js';
import { NpcHandler } from '../handler/NpcHandler.js';

import { MessageHandler } from '../message/MessageHandler.js';
import { AvatarMsgHandler } from '../message/AvatarMsgHandler.js';
import { CasinoMsgHandler } from '../message/CasinoMsgHandler.js';
import { FarmMsgHandler } from '../message/FarmMsgHandler.js';
import { HomeMsgHandler } from '../message/HomeMsgHandler.js';
import { ParkMsgHandler } from '../message/ParkMsgHandler.js';

/* ============================ helper ============================ */

/** Java: private static final Map<Integer, Long> lastActionTimes = new HashMap<>(); */
const lastActionTimes = new Map();
/** Java: private static final long ACTION_COOLDOWN_MS = 100; */
const ACTION_COOLDOWN_MS = 100;

/** Tương đương Avatar.getFile(): đọc cả file thành byte[], lỗi -> null. */
function getFile(url) {
  try {
    return fs.readFileSync(url);
  } catch (e) {
    return null;
  }
}

/** Mô phỏng java.text.MessageFormat.format: {n} + số có phân cách nghìn. */
function messageFormat(pattern, ...args) {
  return String(pattern).replace(/\{(\d+)\}/g, (m, i) => {
    const v = args[Number(i)];
    if (v === undefined) return m;
    if (typeof v === 'number') {
      return v.toLocaleString('en-US', { maximumFractionDigits: 3 });
    }
    return String(v);
  });
}

/** Tương đương JSONValue.parse: lỗi thì null. */
function JSONParse(text) {
  if (text == null) return null;
  try {
    return JSON.parse(text);
  } catch {
    return null;
  }
}

const toByte = (v) => ((v | 0) << 24) >> 24;
const toShort = (v) => ((v | 0) << 16) >> 16;
const idiv = (a, b) => Math.trunc(a / b);

/* ============================ GameSession ============================ */

export class GameSession extends Session {
  constructor(socket, id) {
    super(socket, id);

    // Java: public final Object obj = new Object(); (dùng cho wait/notify)
    this.obj = {};

    // Java khởi tạo 5 service ngay trong constructor; ở Node tạo muộn để tránh
    // phụ thuộc vòng giữa net/ và service/.
    this.avatarService = null;
    this.farmService = null;
    this.homeService = null;
    this.parkService = null;
    this.service = null;

    /** @type {GlobalHandler|null} gán trong enter() như bản Java */
    this.handler = null;

    // NOTE: giữ nguyên hành vi bản Java (field `upgradeHandler` được khai báo
    // nhưng không bao giờ gán/dùng; UpgradeItemHandler toàn bộ là static).
    this.upgradeHandler = UpgradeItemHandler;

    this.isCharCreatedPopup = false;

    // Java: setHandler(new MessageHandler(this)) trong constructor
    this.setHandler(new MessageHandler(this));
  }

  /* ------------------- @Getter của các service ------------------- */

  getAvatarService() {
    if (this.avatarService == null) this.avatarService = new AvatarService(this);
    return this.avatarService;
  }

  getFarmService() {
    if (this.farmService == null) this.farmService = new FarmService(this);
    return this.farmService;
  }

  getHomeService() {
    if (this.homeService == null) this.homeService = new HomeService(this);
    return this.homeService;
  }

  getParkService() {
    if (this.parkService == null) this.parkService = new ParkService(this);
    return this.parkService;
  }

  getService() {
    if (this.service == null) this.service = new Service(this);
    return this.service;
  }

  /* ------------------------- ngắt kết nối ------------------------- */

  /**
   * Java Session.close(): dọn user rồi mới dọn socket. Phần dọn socket đã có ở
   * lớp cha; ở đây thêm phần game (user.close + UserManager.remove + disconnect).
   */
  async close() {
    if (this._gameClosed) {
      super.close();
      return;
    }
    this._gameClosed = true;
    try {
      if (this.user != null) {
        await this.user.close();
        userManager.remove(this.user);
      }
      serverManager.disconnect(this);
    } catch (e) {
      console.error(e);
    }
    super.close();
  }

  closeMessage() {
    if (this.isConnected()) {
      if (this.messageHandler != null) {
        this.messageHandler.onDisconnected();
      }
      this.close();
    }
  }

  /* --------------------------- getHandler --------------------------- */

  getHandler(ms) {
    const index = ms.reader().readByte();
    console.log('getHandler: ' + index);

    if (index === 8) {
      const zone = this.user.getZone();
      zone.leave(this.user);
    }
    ms = new Message(Cmd.GET_HANDLER);
    const ds2 = ms.writer();
    ds2.writeByte(index);
    ds2.flush();
    this.sendMessage(ms);
    switch (index) {
      case 3:
        this.setHandler(new CasinoMsgHandler(this));
        break;
      case 8: {
        this.setHandler(new AvatarMsgHandler(this));
        break;
      }
      case 9: {
        this.setHandler(new ParkMsgHandler(this));
        break;
      }
      case 10: {
        this.setHandler(new FarmMsgHandler(this));
        break;
      }
      case 11: {
        this.setHandler(new HomeMsgHandler(this));
        break;
      }
      default: {
        this.setHandler(new MessageHandler(this));
        break;
      }
    }
  }

  /* ------------------------- ảnh / tile map ------------------------- */

  doGetImgIcon(ms) {
    const imageID = ms.reader().readShort();
    const folder = this.getResourcesPath() + 'object/';
    const dat = getFile(folder + imageID + '.png');
    if (dat == null) {
      return;
    }
    ms = new Message(Cmd.GET_IMG_ICON);
    const ds = ms.writer();
    ds.writeShort(imageID);
    ds.writeShort(dat.length);
    ds.write(dat);
    ds.flush();
    this.sendMessage(ms);
  }

  // -98 cmd
  requestImagePart(ms) {
    const imageID = ms.reader().readShort();
    const folder = this.getResourcesPath() + 'item/';
    const dat = getFile(folder + imageID + '.png');
    if (dat == null) {
      return;
    }
    ms = new Message(Cmd.REQUEST_IMAGE_PART);
    const ds = ms.writer();
    ds.writeShort(imageID);
    ds.writeShort(dat.length);
    ds.write(dat);
    ds.flush();
    this.sendMessage(ms);
  }

  requestTileMap(ms) {
    const idTileImg = ms.reader().readByte();
    console.log('map = ' + idTileImg);
    const dat = getFile(this.getResourcesPath() + 'tilemap/' + idTileImg + '.png');
    if (dat == null) {
      return;
    }
    ms = new Message(Cmd.REQUEST_TILE_MAP);
    const ds = ms.writer();
    ds.writeByte(idTileImg);
    ds.write(dat);
    ds.flush();
    this.sendMessage(ms);
  }

  doRequestExpicePet(ms) {
    const userID = ms.reader().readInt();
    ms = new Message(-70);
    const ds = ms.writer();
    ds.writeInt(userID);
    ds.writeByte(0);
    ds.flush();
    this.sendMessage(ms);
  }

  /* --------------------------- client info --------------------------- */

  clientInfo(ms) {
    const provider = ms.reader().readByte();
    //        if(provider!=9) {
    //            Utils.writeLog(this.user,"login infoFail : provider = "+provider);
    //            ...
    //        }
    const memory = ms.reader().readInt();
    const platform = ms.reader().readUTF();
    this.platform = platform;
    const rmsSize = ms.reader().readInt();
    const width = ms.reader().readInt();
    const height = ms.reader().readInt();
    const aaaaa = ms.reader().readBoolean();
    const resource = ms.reader().readByte();
    this.resourceType = resource;
    const version = ms.reader().readUTF();
    if (ms.reader().available() > 0) {
      ms.reader().readUTF();
      ms.reader().readUTF();
      ms.reader().readUTF();
    }
  }

  agentInfo(ms) {
    const agent = ms.reader().readUTF();
    console.log('agentInfo: ' + agent);
  }

  /* ------------------------- doRequestService ------------------------- */

  doRequestService(ms) {
    const id = ms.reader().readByte();
    //String msg = ms.reader().readUTF();
    switch (id) {
      case 0: {
        ms = new Message(Cmd.UPDATE_CONTAINER);
        const ds = ms.writer();
        const content = this.user.getUpgradeRequirements();
        ds.writeByte(0);
        ds.writeUTF(content);
        ds.flush();
        this.sendMessage(ms);
        break;
      }
      case 1: {
        ms = new Message(Cmd.UPDATE_CONTAINER);
        const ds = ms.writer();
        const content = this.user.upgradeChest();
        ds.writeByte(1);
        ds.writeUTF(content);
        ds.flush();
        this.sendMessage(ms);
        break;
      }
      case 6: {
        ms = new Message(-10);
        const ds = ms.writer();
        ds.writeUTF(`Bạn đang đăng nhập vào thành phố ${serverManager.cityName}. Dân số ${serverManager.clients.length}  người.`);
        ds.flush();
        this.sendMessage(ms);
        break;
      }
      case 3: {
        ms = new Message(-10);
        const ds = ms.writer();
        ds.writeUTF('Chưa có game khác bạn ơiiiii !');
        ds.flush();
        this.sendMessage(ms);
        break;
      }
    }
  }

  /* ----------------------------- đăng nhập ----------------------------- */

  async doLogin(ms) {
    if (this.login) {
      return;
    }
    const username = ms.reader().readUTF().trim();
    const password = ms.reader().readUTF().trim();
    const version = ms.reader().readUTF().trim();
    this.versionARM = version;
    const us = new User();
    us.setUsername(username);
    us.setPassword(password);
    us.setSession(this);
    const result = await us.login();
    if (result) {
      this.login = true;
      this.user = us;
      await this.enter();
    } else {
      this.login = false;
    }
  }

  async enter() {
    if (await this.user.loadData()) {
      await dbManager.executeUpdate(
        'UPDATE `players` SET `is_online` = ?, `client_id` = ? , `ip_address` = ? WHERE `user_id` = ? LIMIT 1;',
        [1, this.id, this.ip, this.user.getId()]);
      this.user.initAvatar();
      this.handler = new GlobalHandler(this.user);
      userManager.add(this.user);
      this.getAvatarService().onLoginSuccess();
      this.getAvatarService().serverDialog('Chào mừng bạn đã đến với Avatar Thanh Pho lo');
      //getAvatarService().serverInfo("");
      //getAvatarService().serverInfo("");
      this.getAvatarService().serverInfo('donate mb 0110121112002 , thanks : soucre share đem bán làm chó , ');
      await this.checkThuongNapLanDau();

      await this.checkThuongNapSet();

      const diamondsPerThousand = 5; // Tặng 5 kim cương vũ trụ cho mỗi 2.000 VND đã nạp
      const tongNap = await this.getTotalDeposited(this.user); // Tổng số tiền người chơi đã nạp
      const nhanthuongTongNap = await this.getNhanThuongTongNap(); // Số tiền nạp đã nhận thưởng
      // Tính phần thưởng dựa trên tổng tiền nạp chưa nhận
      const rewardableAmount = idiv(tongNap - nhanthuongTongNap, 2000);

      if (rewardableAmount > 0) {
        // Tặng số kim cương tương ứng
        const Kimcuong = new Item(5389, -1, rewardableAmount * diamondsPerThousand);
        this.user.addItemToChests(Kimcuong);
        this.user.getAvatarService().SendTabmsg('Bạn vừa donate nhận được ' + (rewardableAmount * diamondsPerThousand) + ' Hoa Ngũ Sắc');
        // Cập nhật lại nhanthuongTongNap trong cơ sở dữ liệu
        const newNhanThuong = nhanthuongTongNap + (rewardableAmount * 2000);
        await this.updateNhanThuongTongNap(newNhanThuong);
      }

      //            NhanThuongEventluong();
      //            NhanThuongEventXuBoss();
    } else {
      if (this.isCharCreatedPopup) {
        this.getAvatarService().serverDialog('Có lỗi xảy ra!');
        await this.close();
        return;
      }
      this.isCharCreatedPopup = true;
      await dbManager.executeUpdate(
        'INSERT INTO `players`(`user_id`, `level_main`, `gender`, `scores`) VALUES (?, ?, ?,?);',
        [this.user.getId(), 1, 0, 0]);
      await this.enter();
    }
  }

  async getNhanThuongTongNap() {
    const sql = 'SELECT nhanthuongTongNap FROM users WHERE id = ?';
    let nhanthuongTongNap = 0; // Mặc định là 0 nếu không có dữ liệu
    try {
      const rs = await dbManager.queryOne(sql, [this.user.getId()]);
      if (rs != null) {
        nhanthuongTongNap = Number(rs.nhanthuongTongNap) | 0;
      }
    } catch (e) {
      console.error(e);
    }
    return nhanthuongTongNap;
  }

  async updateNhanThuongTongNap(newNhanThuong) {
    const sql = 'UPDATE users SET nhanthuongTongNap = ? WHERE id = ?';
    try {
      await dbManager.executeUpdate(sql, [newNhanThuong, this.user.getId()]);
    } catch (e) {
      console.error(e);
    }
  }

  //paytowin
  async getTotalDeposited(us) {
    const sql = 'SELECT tongNap FROM users WHERE id = ?';
    let totalDeposited = 0; // Mặc định tổng nạp là 0 nếu không tìm thấy
    try {
      // NOTE: giữ nguyên hành vi bản Java (tham số `us` bị bỏ qua, dùng this.user)
      const rs = await dbManager.queryOne(sql, [this.user.getId()]);
      if (rs != null) {
        totalDeposited = Number(rs.tongNap) | 0;
      }
    } catch (e) {
      console.error(e);
    }
    return totalDeposited; // Trả về tổng tiền nạp
  }

  /* ------------------------ event xu boss ------------------------ */

  //chay
  async NhanThuongEventXuBoss() {
    const TopXuboss = await this.user.getService().getUserRankXuBoss(this.user);

    if (await this.checkXemNhanThuongXuboss(this.user)) {
      console.log('Người chơi ' + this.user.getUsername() + ' đã nhận thưởng. kill boss');
      return; // Nếu đã nhận thưởng, kết thúc hàm
    }

    if (TopXuboss > 5) {
      return;
    }

    const TOP5XUBOSS = [];
    TOP5XUBOSS.push(new Item(2740, -1, 1));//the vip

    if (TopXuboss === 1) {
      // Trao thưởng top 1
      if (this.user.chests.length >= this.user.getChestSlot() - 1) {
        this.user.getAvatarService().SendTabmsg('Bạn phải có ít nhất 2 ô trống trong rương đồ để nhận thưởng top 1');
        return;
      }
      Utils.writeLogSystem(this.user, 'Nhận Thưởng TOP 1 click boss :');
      const theCaoCao = new Item(2740, -1, 2);
      this.user.addItemToChests(theCaoCao);
      Utils.writeLog(this.user, theCaoCao.getPart().getName());
    } else if (TopXuboss === 2 || TopXuboss === 3 || TopXuboss === 4 || TopXuboss === 5) {
      if (this.user.chests.length >= this.user.getChestSlot() - 1) {
        this.user.getAvatarService().SendTabmsg('Bạn phải có ít nhất 2 ô trống trong rương đồ để nhận thưởng top lượng ' + TopXuboss);
        return;
      }
      Utils.writeLogSystem(this.user, 'Nhận Thưởng TOP xu boss :');
      for (const item of TOP5XUBOSS) {
        this.user.addItemToChests(item);
        Utils.writeLogSystem(this.user, item.getPart().getName());
      }
    }
    await this.UpdateDaNhanThuongEventXuboss(this.user);
    this.user.getAvatarService().SendTabmsg('Bạn đã Nhận thưởng top ' + TopXuboss);
  }

  async UpdateDaNhanThuongEventXuboss(us) {
    const sql = 'UPDATE players SET thuongXuBoss = TRUE WHERE user_id = ?';
    try {
      await dbManager.executeUpdate(sql, [us.getId()]);
    } catch (e) {
      console.error(e);
    }
  }

  async checkXemNhanThuongXuboss(us) {
    const sql = 'SELECT thuongXuBoss FROM players WHERE user_id = ?';
    try {
      const rs = await dbManager.queryOne(sql, [us.getId()]);
      if (rs != null) {
        return !!rs.thuongXuBoss;
      }
    } catch (e) {
      console.error(e);
    }
    return false; // Mặc định trả về false nếu có lỗi
  }

  async UpdateDaNhanThuongEventPhaoXu(us) {
    const sql = 'UPDATE players SET thuongPhaoXu = TRUE WHERE user_id = ?';
    try {
      await dbManager.executeUpdate(sql, [us.getId()]);
    } catch (e) {
      console.error(e);
    }
  }

  async checkXemNhanThuongPhaoXu(us) {
    const sql = 'SELECT thuongPhaoXu FROM players WHERE user_id = ?';
    try {
      const rs = await dbManager.queryOne(sql, [us.getId()]);
      if (rs != null) {
        return !!rs.thuongPhaoXu;
      }
    } catch (e) {
      console.error(e);
    }
    return false; // Mặc định trả về false nếu có lỗi
  }

  async NhanThuongEventPhaoXu() {
    const TopXuboss = await this.user.getService().getUserRankXuBoss(this.user);

    if (await this.checkXemNhanThuongPhaoXu(this.user)) {
      console.log('Người chơi ' + this.user.getUsername() + ' đã nhận thưởng phao xu.');
      return; // Nếu đã nhận thưởng, kết thúc hàm
    }

    if (TopXuboss > 5) {
      return;
    }

    const TOP5XUBOSS = [];
    TOP5XUBOSS.push(new Item(3477, -1, 1));
    TOP5XUBOSS.push(new Item(2740, Date.now() + (86400000 * 3), 1));//the vip

    const TOP3SET = [];
    TOP3SET.push(new Item(3478, -1, 1));
    TOP3SET.push(new Item(3479, -1, 1));
    TOP3SET.push(new Item(3480, -1, 1));
    TOP3SET.push(new Item(3481, -1, 1));

    if (TopXuboss === 1) {
      // Trao thưởng top 1
      if (this.user.chests.length >= this.user.getChestSlot() - 5) {
        this.user.getAvatarService().SendTabmsg('Bạn phải có ít nhất 6 ô trống trong rương đồ để nhận thưởng top 1');
        return;
      }

      const phanThuongTop1boss = [];
      phanThuongTop1boss.push(new Item(3476, -1, 1));//tóc superblue6
      phanThuongTop1boss.push(new Item(2740, Date.now() + (86400000 * 3), 1));//the vip

      Utils.writeLogSystem(this.user, 'Nhận Thưởng TOP 1 XU BOSS : ');
      for (const item of TOP3SET) {
        this.user.addItemToChests(item);
        Utils.writeLog(this.user, item.getPart().getName());
      }
      for (const item of phanThuongTop1boss) {
        this.user.addItemToChests(item);
        Utils.writeLog(this.user, item.getPart().getName());
      }

      Utils.writeLogSystem(this.user, 'Username: ' + this.user.getUsername() + ', rank' + TopXuboss);
    } else if (TopXuboss === 2 || TopXuboss === 3) {
      if (this.user.chests.length >= this.user.getChestSlot() - 4) {
        this.user.getAvatarService().SendTabmsg('Bạn phải có ít nhất 5 ô trống trong rương đồ để nhận thưởng top lượng ' + TopXuboss);
        return;
      }
      Utils.writeLogSystem(this.user, 'Nhận Thưởng TOP 2or3 xu boss :');
      const theCaoCao = new Item(2740, Date.now() + (86400000 * 7), 1);
      this.user.addItemToChests(theCaoCao);
      Utils.writeLogSystem(this.user, theCaoCao.getPart().getName());

      Utils.writeLogSystem(this.user, this.user.getUsername() + ', rank3' + TopXuboss);
      for (const item of TOP5XUBOSS) {
        this.user.addItemToChests(item);
        Utils.writeLog(this.user, item.getPart().getName());
      }
      for (const item of TOP3SET) {
        this.user.addItemToChests(item);
        Utils.writeLog(this.user, item.getPart().getName());
      }
    } else if (TopXuboss === 4 || TopXuboss === 5) {
      if (this.user.chests.length >= this.user.getChestSlot() - 1) {
        this.user.getAvatarService().SendTabmsg('Bạn phải có ít nhất 2 ô trống trong rương đồ để nhận thưởng Pháo xu ' + TopXuboss);
        return;
      }
      Utils.writeLogSystem(this.user, 'Nhận Thưởng TOP 4or5 Pháo xu :');
      for (const item of TOP5XUBOSS) {
        this.user.addItemToChests(item);
        Utils.writeLogSystem(this.user, item.getPart().getName());
      }
    }
    await this.UpdateDaNhanThuongEventPhaoXu(this.user);
    this.user.getAvatarService().SendTabmsg('Bạn đã Nhận thưởng top Pháo xu ' + TopXuboss);
  }

  //eventThaPhaoLuong
  async NhanThuongEventluong() {
    const rankPhaoLuong = await this.user.getService().getUserRankPhaoLuong(this.user);

    if (await this.checkXemNhanThuongTopLuong(this.user)) {
      console.log('Người chơi ' + this.user.getUsername() + ' đã nhận thưởng phao luong.');
      return; // Nếu đã nhận thưởng, kết thúc hàm
    }

    if (rankPhaoLuong > 5) {
      return;
    }

    const slotChest = 2;

    // (phần thưởng top 1..3 đã bị comment trong bản Java)
    if (rankPhaoLuong === 4 || rankPhaoLuong === 5) {
      Utils.writeLogSystem(this.user, 'Nhận Thưởng TOP 4or5 Pháo Lượng :');
      const theCaoCao = new Item(2740, -1, 2);
      this.user.addItemToChests(theCaoCao);
      Utils.writeLog(this.user, theCaoCao.getPart().getName());
    }
    await this.UpdateDaNhanThuongEventluong(this.user);
    this.user.getAvatarService().SendTabmsg('Bạn đã Nhận thưởng top ' + rankPhaoLuong);
  }

  async UpdateDaNhanThuongEventluong(us) {
    const sql = 'UPDATE players SET thuongPhaoLuong = TRUE WHERE user_id = ?';
    try {
      await dbManager.executeUpdate(sql, [us.getId()]);
    } catch (e) {
      console.error(e);
    }
  }

  async checkXemNhanThuongTopLuong(us) {
    const sql = 'SELECT thuongPhaoLuong FROM players WHERE user_id = ?';
    try {
      const rs = await dbManager.queryOne(sql, [us.getId()]);
      if (rs != null) {
        return !!rs.thuongPhaoLuong;
      }
    } catch (e) {
      console.error(e);
    }
    return false; // Mặc định trả về false nếu có lỗi
  }

  /* ------------------------- thưởng nạp ------------------------- */

  async checkThuongNapLanDau() {
    const checkNap = 'SELECT tongnap,ThuongNapLanDau FROM users WHERE id = ? LIMIT 1;';
    try {
      const rows = await dbManager.query(checkNap, [this.user.getId()]);
      for (const rs of rows) {
        const tongnap = Number(rs.tongnap) | 0;
        const napLanDau = !!rs.ThuongNapLanDau;
        if (!napLanDau && tongnap >= 20000) {
          await this.nhanThuongLanDau();
        }
      }
    } catch (ex) {
      console.error(ex);
    }
  }

  async nhanThuongLanDau() {
    this.user.getAvatarService().SendTabmsg('Bạn vừa donate lần đầu trên 20k nhận được 5.000.000 xu và 10.000 lượng và 200 thẻ quay số miễn phí');

    await dbManager.executeUpdate('UPDATE `users` SET `ThuongNapLanDau` = ? WHERE `id` = ? LIMIT 1;',
      [1, this.user.getId()]);
    this.user.updateLuong(+10000);
    this.user.getAvatarService().updateMoney(0);
    this.user.updateXu(+5000000);
    this.user.getAvatarService().updateMoney(0);
    const item = new Item(593, -1, 200);
    this.user.addItemToChests(item);
  }

  async checkThuongNapSet() {
    const checkNap = 'SELECT tongnap, ThuongNapSet, ThuongNapBoSung FROM users WHERE id = ? LIMIT 1;';
    try {
      const rs = await dbManager.queryOne(checkNap, [this.user.getId()]);
      if (rs != null) {
        const tongnap = Number(rs.tongnap) | 0;
        const thuongNapSet = !!rs.ThuongNapSet;
        const thuongNapBoSung = !!rs.ThuongNapBoSung;

        // Kiểm tra nếu chưa nhận thưởng và tổng nạp >= 100k
        if (!thuongNapSet && tongnap >= 100000) {
          await this.nhanThuongNapSet();
        }

        // Kiểm tra nếu đã nhận thưởng lần đầu và chưa nhận phần thưởng bổ sung và tổng nạp >= 200k
        if (thuongNapSet && !thuongNapBoSung && tongnap >= 200000) {
          // nhanThuongNapBoSung();
        }
      }
    } catch (ex) {
      console.error(ex);
    }
  }

  async nhanThuongNapSet() {
    this.user.getAvatarService().SendTabmsg('Nhận phần thưởng set tích lũy 100k : Akatsuki');

    // Kiểm tra số ô trống trong rương
    if (this.user.chests.length >= this.user.getChestSlot() - 5) {
      this.user.getAvatarService().serverDialog('Bạn phải có ít nhất 6 ô trống trong rương đồ');
      return;
    }

    // Cập nhật đã nhận thưởng vào database
    await dbManager.executeUpdate('UPDATE `users` SET `ThuongNapSet` = ? WHERE `id` = ? LIMIT 1;',
      [1, this.user.getId()]);

    // Danh sách ID item phần thưởng set đầu tiên
    const itemIds = [5358, 5359, 5361, 5362, 5363];
    for (const itemId of itemIds) {
      const item = new Item(itemId);
      item.setExpired(-1);
      this.user.addItemToChests(item);
    }

    if (this.user.getGender() === 1) {
      const item = new Item(5357);
      item.setExpired(-1);
      this.user.addItemToChests(item);
    } else {
      const item1 = new Item(5360);
      item1.setExpired(-1);
      this.user.addItemToChests(item1);
    }
  }

  async nhanThuongNapBoSung() {
    this.user.getAvatarService().SendTabmsg('Nhận thêm phần thưởng cho tổng nạp 200k');
    if (this.user.chests.length >= this.user.getChestSlot() - 4) {
      this.user.getAvatarService().serverDialog('Bạn phải có ít nhất 5 ô trống trong rương đồ');
      return;
    }
    // Cập nhật đã nhận phần thưởng bổ sung vào database
    await dbManager.executeUpdate('UPDATE `users` SET `ThuongNapBoSung` = ? WHERE `id` = ? LIMIT 1;',
      [1, this.user.getId()]);
    // Danh sách ID item phần thưởng bổ sung khi đạt 200k (đã comment trong bản Java)
  }

  isNewVersion() {
    return true;
  }

  regMessage(ms) {
    const username = ms.reader().readUTF().trim();
    const password = ms.reader().readUTF().trim();
  }

  /* -------------------------- tạo nhân vật -------------------------- */

  createCharacter(ms) {
    const gender = ms.reader().readByte();//1 nam 2 nu
    const numItem = ms.reader().readByte();
    const items = [];
    const boyItems = [89, 88, 0, 4, 14];
    const girlItems = [89, 88, 0, 4, 49];
    const selectedItems = (gender === 1) ? boyItems : girlItems;

    for (const itemID of selectedItems) {
      items.push(new Item(itemID, -1, 1));
    }
    let isError = false;
    if (gender !== 1 && gender !== 2) {
      isError = true;
    }
    // NOTE: giữ nguyên hành vi bản Java (gán lại isError nên bỏ qua kiểm tra gender ở trên)
    isError = !CreateChar.getInstance().check(gender, items);
    if (isError) {
      ms = new Message(-35);
      const ds = ms.writer();
      ds.writeBoolean(false);
      ds.flush();
      this.sendMessage(ms);
      return;
    }
    this.user.setGender(gender);
    this.user.setWearing(items);
    ms = new Message(-35);
    const ds = ms.writer();
    ds.writeBoolean(true);
    ds.flush();
    this.sendMessage(ms);
  }

  /* ----------------------------- khu vực ----------------------------- */

  doiKhuVuc(ms) {
    if (this.messageHandler instanceof FarmMsgHandler) {
      return;
    }
    const numKhuVuc = 10;
    const mapid = ms.reader().readByte();
    const m = mapManager.find(mapid);
    ms = new Message(60);
    const ds = ms.writer();
    ds.writeByte(numKhuVuc);
    for (const zone of m.getZones()) {
      if (zone.getPlayers().length >= 9) {
        ds.writeByte(0);
      } else if (zone.getPlayers().length >= 4) {
        ds.writeByte(1);
      } else {
        ds.writeByte(2);
      }
    }
    ds.flush();
    this.sendMessage(ms);
    // Java gọi ds.close(); Node không cần đóng buffer trong bộ nhớ.
  }

  doJoinHouse4(ms) {
    console.log('-104:  ' + ms.reader().readInt());
  }

  /* ----------------------------- shop avatar ----------------------------- */

  buyItemShop(ms) {
    try {
      if (this.user.checkFullSlotChest()) {
        return;
      }

      const partID = ms.reader().readShort();
      const type = ms.reader().readByte();
      if (type < 1 || type > 2) {
        this.user.getService().serverDialog('Có lỗi xảy ra, vui lòng liên hệ admin. Mã lỗi: buyItemShopWrongType');
        return;
      }
      const part = partManager.findPartByID(partID);

      const itembyacc = this.user.findItemInChests(partID);
      if (itembyacc != null && (itembyacc.getPart().getZOrder() === 30 || itembyacc.getPart().getZOrder() === 40)) {
        // mắt mặt ko mua trùng
        this.user.getAvatarService().serverDialog('bạn đã có vật phẩm này ở rương đồ! đến npc saitama ở công viên để quản lý');
        return;
      }
      // NOTE: giữ nguyên hành vi bản Java (dùng part trước khi kiểm tra part != null)
      if (((part.getGender() === 2 || part.getGender() === 1) && (this.user.getGender() !== part.getGender()))) {
        this.user.getAvatarService().serverDialog('gioi tinh khong phu hop');
        return;
      }
      if (part.getName() == null) {
        this.user.getAvatarService().serverDialog('ITEM lỗi mua item khác tạm đi bro');
        return;
      }
      if (part != null) {
        const priceXu = part.getCoin();
        const priceLuong = part.getGold();
        let price = 0;
        if ((priceXu === -1 && priceLuong === -1) || (type === 1 && priceXu === -1)
          || (type === 2 && priceLuong === -1)) {
          return;
        }
        if (priceXu > 0) {
          price = priceXu;
          if (this.user.getXu() < price) {
            this.user.getService().serverMessage('Bạn không đủ xu!');
            return;
          }
          this.user.updateXu(-price);
          this.getAvatarService().updateMoney(0);
        } else {
          price = priceLuong;
          if (this.user.getLuong() < price) {
            this.user.getService().serverMessage('Bạn không đủ lượng!');
            return;
          }
          this.user.updateLuong(-price);
          this.getAvatarService().updateMoney(0);
        }
        let expired = Date.now() + (part.getExpiredDay() * 86400000);
        if (part.getExpiredDay() === 0) {
          expired = -1;
        }
        const item = Item.builder()
          .id(part.getId())
          .expired(expired)
          .build();
        console.log('expired: ' + expired);

        const zOrder = part.getZOrder();
        const w = this.user.findItemWearingByZOrder(zOrder);
        if (w != null) {
          this.user.removeItemFromWearing(w);
          this.user.addItemToChests(w);
        }

        this.user.addItemToWearing(item);
        this.user.removeItemFromChests(item);
        this.user.sortWearing();
        // NOTE: giữ nguyên hành vi bản Java (truyền `id` của session, không phải user.getId())
        this.user.getMapService().usingPart(this.id, toShort(item.getId()));

        ms = new Message(-24);
        const ds = ms.writer();
        ds.writeShort(partID);
        if (partID !== -1) {
          ds.writeInt(price);
          ds.writeByte(1);
        }
        ds.writeUTF('Bạn đã mua vật phẩm thành công.');
        ds.writeInt(this.user.getXu());
        ds.writeInt(this.user.getLuong());
        ds.writeInt(this.user.getLuongKhoa());
        ds.flush();
        this.sendMessage(ms);
      } else {
        this.getAvatarService().serverMessage('Vật phẩm không tồn tại !!!');
      }
    } catch (e) {
      console.log('[ERROR-DB]' + e.message);
    }
  }

  /* --------------------------- map offline --------------------------- */

  doJoinOfflineMap(ms) {
    const map = ms.reader().readByte();
    const mapOffline = mapOfflineManager.find(map);
    let npcs = [];
    if (mapOffline != null) {
      npcs = mapOffline.getNpcs();
    } else {
      console.log('Map offline join: ' + map);
    }
    ms = new Message(Cmd.JOIN_OFFLINE_MAP);
    const ds = ms.writer();
    ds.writeByte(map);
    ds.writeByte(npcs.length);
    for (const npc of npcs) {
      ds.writeInt(npc.getId());
      ds.writeUTF(npc.getUsername());
      const wearing = npc.getWearing();
      ds.writeByte(wearing.length);
      for (const item of wearing) {
        ds.writeShort(item.getId());
      }
      ds.writeShort(npc.getX());
      ds.writeShort(npc.getY());
      ds.writeByte(npc.getStar());
      ds.writeByte(0);
      ds.writeShort(npc.getIdImg());
      const chats = npc.getTextChats();
      ds.writeByte(chats.length);
      for (const text of chats) {
        ds.writeUTF(text);
      }
    }
    ds.writeShort(0);
    ds.flush();
    this.sendMessage(ms);
  }

  doRequestCityMap(ms) {
    if (ms.reader().available() > 0) {
      const idMini = ms.reader().readByte();
      console.log('RequestCityMap: ' + idMini);
    }
    ms = new Message(-63);
    const ds = ms.writer();
    ds.writeByte(-1);
    ds.flush();
    this.sendMessage(ms);
    this.user.getAvatarService().openMenuOption(5, 0, 'Đảo Hawaii', 'Ai Cập', 'Vương Quốc Bóng Đêm', 'Biển citylo');
  }

  /* --------------------------- doCommunicate --------------------------- */

  doCommunicate(ms) {
    const userId = ms.reader().readInt();
    if (userId >= 2000000000) {
      NpcHandler.handlerCommunicate(userId, this.user);
      return;
    } else {
      console.log('userId = ' + userId);
      if (userId === 0) {
        // hiện thị menu chức năng
        const menus = [
          Menu.builder().name('Auto Câu Cá').menus([
            Menu.builder().name('Kích hoạt Auto Câu Cá').action(async () => {
              const checkNap = 'SELECT ThuongNapLanDau FROM users WHERE id = ? LIMIT 1;';
              try {
                const rows = await dbManager.query(checkNap, [this.user.getId()]);
                for (const rs of rows) {
                  const napLanDau = !!rs.ThuongNapLanDau;
                  if (napLanDau) {
                    this.user.getAvatarService().serverDialog('Bạn Đã Kích Hoạt Auto Câu Cá Thành Công');
                    this.user.setAutoFish(true);
                  } else {
                    this.user.getAvatarService().serverDialog('Treo câu thì donate lần đầu nha : v');
                    this.user.setAutoFish(false);
                  }
                }
              } catch (ex) {
                console.error(ex);
              }
            }).build(),
            Menu.builder().name('Tắt Auto Câu Cá').action(() => {
              this.user.getAvatarService().serverDialog('Bạn Đã Tắt Auto Câu Cá');
              this.user.setAutoFish(false);
            }).build(),
          ]).build(),
          Menu.builder().name('Mã quà tặng(gift code)').action(() => {
            this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 20, 'Item code', 1);
          }).build(),
          Menu.builder().name('Mã Giới Thiệu').build(),
          Menu.builder().name('Diễn Đàn').build(),
        ];
        if (this.user.getId() === 7) {
          menus.unshift(Menu.builder().name('Admin')
            .menus([
              Menu.builder().name('Thêm item').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 7, 'Item code', 1);
              }).build(),
              Menu.builder().name('Fix Lỗi Rương').action(() => {
                try {
                  console.log('fix Item id : ' + this.user.getUsername());
                  const items = this.user.getChests();
                  const itemIndex = items.length - 1;
                  console.log('index: ' + itemIndex);
                  const item = items[itemIndex];
                  console.log('fix Item id : ' + this.user.getUsername());
                  this.user.removeItem(item.getId(), 1);
                  this.user.getAvatarService().serverDialog('ok');
                } catch (e) {
                  // NOTE: giữ nguyên hành vi bản Java (chỉ bắt NumberFormatException)
                  this.user.getAvatarService().serverDialog('error');
                }
              }).build(),
              Menu.builder().name('Chat tổng').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 8, 'thong bao', 1);
              }).build(),
              Menu.builder().name('Thời Tiết').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 9, 'thoi tiet', 1);
              }).build(),
              Menu.builder().name('bao tri').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 10, 'bao tri', 1);
              }).build(),
              Menu.builder().name('infor').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 11, 'infor', 1);
              }).build(),
              Menu.builder().name('thread?').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 12, 'thread', 1);
              }).build(),
              Menu.builder().name('pem').action(() => {
                if (this.user.getId() === 7 || this.user.getId() === 97) {
                  this.user.getZone().getPlayers().forEach((u) => {
                    EffectService.createEffect()
                      .session(u.session)
                      .id(toByte(23))
                      .style(toByte(0))
                      .loopLimit(toByte(5))
                      .loop(toShort(1))
                      .loopType(toByte(1))
                      .radius(toShort(5))
                      .idPlayer(this.user.getId())
                      .send();
                  });
                } else {
                  this.user.getAvatarService().serverDialog('ad mới bật được b ơi');
                }
              }).build(),
              Menu.builder().name('Tim Rơi').action(() => {
                if (this.user.getId() === 7) {
                  this.user.getZone().getPlayers().forEach((u) => {
                    EffectService.createEffect()
                      .session(u.session)
                      .id(toByte(56))
                      .style(toByte(0))
                      .loopLimit(toByte(5))
                      .loop(toShort(100))
                      .loopType(toByte(1))
                      .radius(toShort(250))
                      .idPlayer(this.user.getId())
                      .send();
                  });
                } else {
                  this.user.getAvatarService().serverDialog('ad mới bật được b ơi');
                }
              }).build(),
              Menu.builder().name('Menu sentb bao tri').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 98, 'bao tri sau 2p', 1);
              }).build(),
              Menu.builder().name('EFFECT').action(() => {
                if (this.user.getId() === 7) {
                  this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 99, 'ideffect', 1);
                } else {
                  this.user.getAvatarService().serverDialog('ad mới bật được b ơi');
                }
              }).build(),
              Menu.builder().name('Khoá nick').build(),
              Menu.builder().name('dau gia').action(() => {
                this.user.getAvatarService().sendTextBoxPopup(this.user.getId(), 81, 'Mo Dau gia type ? item id ?', 1);
              }).build(),
              Menu.builder().name('Tặng item').build(),
            ])
            .build());
        }
        this.user.setMenus(menus);
        this.user.getAvatarService().openUIMenu(1, 0, menus, null, null);
      }
      //            handleSelectFunction(this.user);
    }
  }

  /* ------------------------- Nâng cấp các shop ------------------------- */

  async handleBossShop(ms) {
    const idBoss = ms.reader().readInt();
    const type = ms.reader().readByte();
    const indexItem = ms.reader().readShort();
    if (idBoss === Npc.ID_ADD + NpcName.THO_KIM_HOAN && this.user.getBossShopItems() != null) {
      console.log(messageFormat('do upgrade item boss shop {0}, {1}, {2},', idBoss, type, indexItem));
      // NOTE: giữ nguyên hành vi bản Java (List.get(index); JS trả undefined thay vì throw)
      const upgradeItem = this.user.getBossShopItems()[indexItem];
      if (upgradeItem != null) {
        const item = this.user.findItemInChests(upgradeItem.getItemNeed());
        if (item == null) {
          const part = partManager.findPartById(upgradeItem.getItemNeed());
          this.getService().serverDialog(messageFormat('Bạn cần có {0} để nâng cấp món đồ này', part.getName()));
          return;
        }
        if (type === BossShopHandler.SELECT_XU) {
          if (upgradeItem.isOnlyLuong()) {
            this.getService().serverDialog('Vật phẩm này chỉ có thể nâng cấp bằng lượng');
            return;
          }
          if (this.user.getXu() < upgradeItem.getXu()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} xu để nâng cấp món đồ này', upgradeItem.getXu()));
            return;
          }
          this.user.updateXu(-upgradeItem.getXu());
          this.user.getAvatarService().updateMoney(0);
          Utils.writeLog(this.user, 'xu Nâng Cấp Item ' + upgradeItem.getItem().getPart().getName() + ' ' + this.user.getXu());
          this.doFinalUpgrade(upgradeItem, item);
          return;
        } else if (type === BossShopHandler.SELECT_LUONG) {
          if (this.user.getLuong() < upgradeItem.getLuong()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} lượng để nâng cấp món đồ này', upgradeItem.getLuong()));
            return;
          }
          this.user.updateLuong(-upgradeItem.getLuong());
          this.user.getAvatarService().updateMoney(0);
          Utils.writeLog(this.user, 'Luong Nâng Cấp Item ' + upgradeItem.getItem().getPart().getName() + ' ' + this.user.getLuong());
          this.doFinalUpgrade(upgradeItem, item);
          return;
        } else if (type === BossShopHandler.SELECT_DNS) {
          const item1 = this.user.findItemInChests(3672);
          if (item1 == null || item1.getQuantity() < upgradeItem.getScores()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} Đá ngũ sắc để nâng cấp món đồ này', upgradeItem.getScores()));
            return;
          }

          if (this.user.getLuong() < upgradeItem.getLuong()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} lượng để nâng cấp món đồ này', upgradeItem.getLuong()));
            return;
          }

          if (this.user.getXu() < upgradeItem.getXu()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} xu để nâng cấp món đồ này', upgradeItem.getXu()));
            return;
          }
          this.user.removeItem(3672, upgradeItem.getScores());
          this.user.updateLuong(-upgradeItem.getLuong());
          this.user.getAvatarService().updateMoney(0);
          this.user.updateXu(-upgradeItem.getXu());
          this.user.getAvatarService().updateMoney(0);
          Utils.writeLog(this.user, 'Xu Luong Nâng Cấp Item ' + upgradeItem.getItem().getPart().getName() + this.user.getXu() + ' luong ' + this.user.getLuong());
          this.doFinalUpgrade(upgradeItem, item);
          return;
        } else if (type === BossShopHandler.SELECT_HoaNS) {
          const item1 = this.user.findItemInChests(5389);
          let Quanty = upgradeItem.getScores();
          if (Quanty === 12) {
            Quanty = 20;
          }
          if (item1 == null || item1.getQuantity() < Quanty) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} Sen Ngũ Sắc để nâng cấp món đồ này', Quanty));
            return;
          }

          if (this.user.getLuong() < upgradeItem.getLuong()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} lượng để nâng cấp món đồ này', upgradeItem.getLuong()));
            return;
          }

          if (this.user.getXu() < upgradeItem.getXu()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} xu để nâng cấp món đồ này', upgradeItem.getXu()));
            return;
          }
          this.user.removeItem(5389, Quanty);
          this.user.updateLuong(-upgradeItem.getLuong());
          this.user.getAvatarService().updateMoney(0);
          this.user.updateXu(-upgradeItem.getXu());
          this.user.getAvatarService().updateMoney(0);
          Utils.writeLog(this.user, 'Xu Luong Nâng Cấp Item ' + upgradeItem.getItem().getPart().getName() + this.user.getXu() + ' luong ' + this.user.getLuong());
          this.doFinalUpgrade(upgradeItem, item);
          return;
        } else if (type === BossShopHandler.SELECT_ManhGhep) {
          const ManhGhep = this.user.findItemInChests(upgradeItem.getItemNeed());
          if (ManhGhep == null || ManhGhep.getQuantity() < upgradeItem.getScores()) {
            this.getService().serverDialog(messageFormat('Bạn cần có {0} Mảnh ghép để đổi {1} ',
              upgradeItem.getScores(), upgradeItem.getItem().getPart().getName()));
            return;
          }
          this.user.removeItem(upgradeItem.getItemNeed(), upgradeItem.getScores());
          upgradeItem.getItem().setExpired(-1);
          this.user.addItemToChests(upgradeItem.getItem());
          this.getService().serverDialog(messageFormat('Chúc mừng bạn đã đổi thành công {0}', upgradeItem.getItem().getPart().getName()));
          return;
        }
      }
    }

    if (idBoss === Npc.ID_ADD + NpcName.Chay_To_Win && this.user.getBossShopItems() != null) {
      console.log(messageFormat('do Event item boss shop Chay_To_Win {0}, {1}, {2},', idBoss, type, indexItem));
      const EventItem = this.user.getBossShopItems()[indexItem];
      if (EventItem != null) {
        await this.doFinalEventShop(EventItem, NpcName.Chay_To_Win);
        return;
      }
      // item ned(huy hieu thi cho vo pay to win
    }

    if (idBoss === Npc.ID_ADD + NpcName.Pay_To_Win && this.user.getBossShopItems() != null) {
      console.log(messageFormat('do Event item boss shop Pay_To_Win {0}, {1}, {2},', idBoss, type, indexItem));
      const EventItem = this.user.getBossShopItems()[indexItem];
      if (EventItem != null) {
        await this.doFinalEventShop(EventItem, NpcName.Pay_To_Win);
        return;
      }
    }
    if (idBoss === Npc.ID_ADD + NpcName.bunma && this.user.getBossShopItems() != null) {
      console.log(messageFormat('do Event item boss shop {0}, {1}, {2},', idBoss, type, indexItem));
      const EventItem = this.user.getBossShopItems()[indexItem];
      if (EventItem != null) {
        await this.doFinalEventShop(EventItem, NpcName.bunma);
        return;
      }
    }
    if (idBoss === Npc.ID_ADD + NpcName.Vegeta && this.user.getBossShopItems() != null) {
      console.log(messageFormat('do Event item boss shop vegenta {0}, {1}, {2},', idBoss, type, indexItem));
      const EventItem = this.user.getBossShopItems()[indexItem];
      if (EventItem != null) {
        await this.doFinalEventShop(EventItem, NpcName.Vegeta);
        return;
      }
    }
    if (idBoss === Npc.ID_ADD + NpcName.Shop_Buy_Luong && this.user.getBossShopItems() != null) {
      console.log(messageFormat('do Event item boss shop ShopDacBiet {0}, {1}, {2},', idBoss, type, indexItem));
      const EventItem = this.user.getBossShopItems()[indexItem];
      if (EventItem != null) {
        await this.doFinalEventShop(EventItem, NpcName.Shop_Buy_Luong);
        return;
      }
    }
  }

  doFinalEventShopThuong(item, npcId) {
    // pt thành dng item ko cần build qua updare
    const z = this.user.getZone();
    if (z != null) {
      const u = z.find(npcId + Npc.ID_ADD);
      if (u == null) {
        return;
      }
    } else {
      return;
    }
    switch (npcId) {
      case NpcName.Shop_Buy_Luong:
        if (this.user.getLuong() > item.getPart().getGold()) {
          if (this.user.getChestSlot() <= this.user.chests.length) {
            this.getAvatarService().serverDialog('Rương đồ đã đầy');
            return;
          }
          item.setExpired(-1);
          this.user.addItemToChests(item);
          this.user.updateLuong(-item.getPart().getGold());
          this.getAvatarService().requestYourInfo(this.user);
          this.getAvatarService().updateMoney(0);
          this.getService().serverDialog('Chúc mừng bạn đã đổi thành công');
        } else {
          this.getService().serverDialog('Bạn chưa đủ điều kiện để đổi');
        }
        break;
    }
  }

  async doFinalEventShop(Eventitem, npcId) {
    const z = this.user.getZone();
    if (z != null) {
      const u = z.find(npcId + Npc.ID_ADD);
      if (u == null) {
        return;
      }
    } else {
      return;
    }
    switch (npcId) {
      case NpcName.Chay_To_Win:
        if (this.user.getXu() > Eventitem.getXu()) {
          if (!this.isGenderCompatible(Eventitem.getItem(), this.user)) {
            this.getAvatarService().serverDialog('Giới tính không phù hợp !');
            return;
          }
          if (this.user.getChestSlot() <= this.user.chests.length) {
            this.getAvatarService().serverDialog('Rương đồ đã đầy');
            return;
          }

          Eventitem.getItem().setExpired(-1);
          this.user.updateXu(-Eventitem.getXu());
          this.getAvatarService().updateMoney(0);
          if (Eventitem.getItem().getPart().getType() === -2) {
            if (this.user.findItemInChests(Eventitem.getItem().getId()) != null) {
              const quantity = this.user.findItemInChests(Eventitem.getItem().getId()).getQuantity();
              this.user.findItemInChests(Eventitem.getItem().getId()).setQuantity(quantity + 1);
            } else {
              this.user.addItemToChests(Eventitem.getItem());
            }
          } else {
            this.user.addItemToChests(Eventitem.getItem());
          }
          this.getService().serverDialog('Chúc mừng bạn đã đổi thành công');
        } else {
          this.getService().serverDialog('Bạn chưa đủ Xu để đổi');
        }
        break;
      case NpcName.Pay_To_Win: {//shop đổi đá
        const huyhieu = this.user.findItemInChests(Eventitem.getItemNeed());
        if (huyhieu != null && huyhieu.getQuantity() >= Eventitem.getScores()) {
          Eventitem.getItem().setExpired(-1);
          if (this.user.getChestSlot() <= this.user.chests.length) {
            this.getAvatarService().serverDialog('Rương đồ đã đầy');
            return;
          }
          if (Eventitem.getItem().getId() === 4345) {
            this.user.removeItem(huyhieu.getId(), Eventitem.getScores());
            const quanpika = new Item(4346);
            quanpika.setExpired(-1);

            this.user.addItemToChests(quanpika);
            const aopika = new Item(4347);
            aopika.setExpired(-1);

            this.user.addItemToChests(aopika);
            this.getAvatarService().requestYourInfo(this.user);

            this.getService().serverDialog(`Chúc mừng bạn đã đổi thành công ${Eventitem.getItem().getPart().getName()}`);
            //user.addItemToChests(Eventitem.getItem());
            //return;
          }
          if (Eventitem.getItem().getId() === 6556) {
            const itemIdsPhapsudolong = [6557, 6558, 6559];

            for (const itemId of itemIdsPhapsudolong) {
              const item = new Item(itemId);
              item.setExpired(-1);
              this.user.addItemToChests(item);
            }
            this.getAvatarService().requestYourInfo(this.user);
            this.getService().serverDialog(`Chúc mừng bạn đã đổi thành công ${Eventitem.getItem().getPart().getName()}`);
          }
          if (Eventitem.getItem().getId() === 6560) {
            const itemIdsPhapsudolong = [6561, 6562, 6563];

            for (const itemId of itemIdsPhapsudolong) {
              const item = new Item(itemId);
              item.setExpired(-1);
              this.user.addItemToChests(item);
            }
            this.getAvatarService().requestYourInfo(this.user);
            this.getService().serverDialog(`Chúc mừng bạn đã đổi thành công ${Eventitem.getItem().getPart().getName()}`);
          }

          // NOTE: giữ nguyên hành vi bản Java (với id 4345 thì removeItem bị gọi 2 lần)
          this.user.removeItem(huyhieu.getId(), Eventitem.getScores());
          this.getAvatarService().requestYourInfo(this.user);
          this.getService().serverDialog(`Chúc mừng bạn đã đổi thành công ${Eventitem.getItem().getPart().getName()}`);
          if (Eventitem.getItem().getId() === 5408 || Eventitem.getItem().getId() === 5324 || Eventitem.getItem().getId() === 5880) {
            const hopqua = new Item(Eventitem.getItem().getId(), -1, 1);
            //hopqua.setExpired(System.currentTimeMillis() + (86400000L * time));
            if (this.user.findItemInChests(Eventitem.getItem().getId()) != null) {
              const quantity = this.user.findItemInChests(Eventitem.getItem().getId()).getQuantity();
              this.user.findItemInChests(Eventitem.getItem().getId()).setQuantity(quantity + 1);
            } else {
              this.user.addItemToChests(hopqua);
            }
            return;
          }
          this.user.addItemToChests(Eventitem.getItem());
        } else {
          const itemneed = new Item(Eventitem.getItemNeed(), -1, 1);
          const partName = itemneed.getPart() != null ? itemneed.getPart().getName() : 'unknown';
          this.getService().serverDialog(`Bạn không đủ ${partName} để đổi`);
        }
        break;
      }
      case NpcName.bunma:
        if (Eventitem.getItemRequest() === 3861) {
          // Kiểm tra nếu người dùng đã đổi vật phẩm này
          if (await this.isItemExchanged(this.user.getId(), 3861)) {
            this.getService().serverDialog('Bạn đã đổi vật phẩm này trước đó, không thể đổi lại.');
            return;
          } else {
            Eventitem.getItem().setExpired(Date.now() + (86400000 * 7));
            Eventitem.getItem().setQuantity(1);
            if (this.user.getChestSlot() <= this.user.chests.length) {
              this.getAvatarService().serverDialog('Rương đồ đã đầy');
              return;
            }
            this.user.addItemToChests(Eventitem.getItem());
            this.user.setStylish(toByte(this.user.getStylish() - 1));
            this.user.updateScores(-Eventitem.getScores());
            this.getAvatarService().requestYourInfo(this.user);
            this.getService().serverDialog('Chúc mừng bạn đã đổi thành công');
            await this.saveItemExchange(this.user.getId(), 3861);
          }
        } else {
          // Kiểm tra điểm cho các vật phẩm khác (nếu cần)
          if (this.user.getScores() >= Eventitem.getScores()) {
            // Thực hiện đổi các vật phẩm khác
            Eventitem.getItem().setExpired(-1);
            if (this.user.getChestSlot() <= this.user.chests.length) {
              this.getAvatarService().serverDialog('Rương đồ đã đầy');
              return;
            }
            this.user.addItemToChests(Eventitem.getItem());
            this.user.setStylish(toByte(this.user.getStylish() - 1));
            this.user.updateScores(-Eventitem.getScores());
            this.getAvatarService().requestYourInfo(this.user);
            this.getService().serverDialog('Chúc mừng bạn đã đổi thành công');
          } else {
            // Thông báo khi không đủ điểm cho vật phẩm khác
            this.getService().serverDialog('Bạn chưa đủ điểm để đổi');
          }
        }
        break;
      case NpcName.Vegeta: {
        const TheVip = this.user.findItemInChests(Eventitem.getItemNeed());
        if (TheVip != null) {
          Eventitem.getItem().setExpired(-1);
          if (this.user.getChestSlot() <= this.user.chests.length) {
            this.getAvatarService().serverDialog('Rương đồ đã đầy');
            return;
          }
          this.user.addItemToChests(Eventitem.getItem());
          this.user.setStylish(toByte(this.user.getStylish() - 1));
          this.user.removeItem(Eventitem.getItemNeed(), 1);
          this.getAvatarService().requestYourInfo(this.user);
          this.getService().serverDialog(`Chúc mừng bạn đã đổi thành công ${Eventitem.getItem().getPart().getName()}`);
        } else {
          // NOTE: giữ nguyên hành vi bản Java (in ra ID item chứ không phải tên)
          this.getService().serverDialog(`Bạn không có ${Eventitem.getItemNeed()} để đổi`);
        }
        break;
      }
      case NpcName.Shop_Buy_Luong:
        if (this.user.getLuong() > Eventitem.getItem().getPart().getGold()) {
          if (this.user.getChestSlot() <= this.user.chests.length) {
            this.getAvatarService().serverDialog('Rương đồ đã đầy');
            return;
          }
          if (Eventitem.getItem().getPart().getType() === -2) {
            if (this.user.findItemInChests(Eventitem.getItem().getId()) != null) {
              Eventitem.getItem().setExpired(-1);
              const quantity = this.user.findItemInChests(Eventitem.getItem().getId()).getQuantity();
              this.user.findItemInChests(Eventitem.getItem().getId()).setQuantity(quantity + 1);
            } else {
              Eventitem.getItem().setExpired(-1);
              this.user.addItemToChests(Eventitem.getItem());
            }
            this.getService().serverDialog('Chúc mừng bạn đã đổi thành công');
            this.user.updateLuong(-Eventitem.getItem().getPart().getGold());
            this.getAvatarService().updateMoney(0);
          } else {
            Eventitem.getItem().setExpired(-1);
            this.user.addItemToChests(Eventitem.getItem());
            this.user.updateLuong(-Eventitem.getItem().getPart().getGold());
            this.getAvatarService().updateMoney(0);
            this.getService().serverDialog('Chúc mừng bạn đã đổi thành công');
          }
        } else {
          this.getService().serverDialog('Bạn chưa đủ điều kiện để đổi');
        }
        break;
    }
  }

  async isItemExchanged(userId, itemId) {
    const query = 'SELECT COUNT(*) FROM itemLimited WHERE user_id = ? AND item_id = ?';
    try {
      const rs = await dbManager.queryOne(query, [userId, itemId]);
      if (rs != null && Number(Object.values(rs)[0]) > 0) {
        return true; // Đã tồn tại bản ghi, nghĩa là đã đổi
      }
    } catch (e) {
      console.error(e);
    }
    return false; // Chưa đổi
  }

  async saveItemExchange(userId, itemId) {
    const query = 'INSERT INTO itemLimited (user_id, item_id) VALUES (?, ?)';
    try {
      const rowsAffected = await dbManager.executeUpdate(query, [userId, itemId]);
      if (rowsAffected > 0) {
        console.log('Đã lưu vật phẩm vào bảng itemLimited.');
      } else {
        console.log('Không có dòng nào được thêm vào. Kiểm tra lại điều kiện.');
      }
    } catch (e) {
      console.error(e);
    }
  }

  isGenderCompatible(item, user) {
    const itemGender = item.getPart().getGender(); // Giới tính của item (0 = cả hai giới, 1 = nam, 2 = nữ)
    const userGender = user.getGender(); // Giới tính của user (1 = nam, 2 = nữ)

    // Nếu itemGender là 0, thì cả hai giới đều dùng được
    if (itemGender === 0) {
      return true;
    }

    // Nếu không, kiểm tra xem giới tính của item có khớp với giới tính của user không
    return itemGender === userGender;
  }

  doFinalUpgrade(item, itemOld) {
    const currentTime = Date.now();
    const lastActionTime = lastActionTimes.has(this.user.getId()) ? lastActionTimes.get(this.user.getId()) : 0;
    if (currentTime - lastActionTime < ACTION_COOLDOWN_MS) {
      this.user.getAvatarService().serverDialog('Từ từ thôi bạn!');
      return;
    }
    lastActionTimes.set(this.user.getId(), currentTime);
    if (itemOld.getExpired() !== -1) {
      this.user.getAvatarService().serverDialog('Bạn cần có vật phẩm ' + itemOld.getPart().getName() + ' vĩnh viễn');
      return;
    }
    let ratio = item.getRatio();
    let isUpgradeSuccess = false;
    if (ratio > 0) {
      isUpgradeSuccess = Utils.nextInt(0, 100) < ratio;
    } else {
      ratio = Math.abs(ratio);
      const correctNumber = Utils.nextInt(0, ratio);
      isUpgradeSuccess = correctNumber === Utils.nextInt(0, ratio);
    }
    if (isUpgradeSuccess || item.getRatio() === 100) {
      this.user.removeItemFromChests(itemOld);
      item.getItem().setExpired(-1);
      this.user.addItemToChests(item.getItem());
      this.getAvatarService().updateMoney(0);
      this.user.setStylish(toByte(this.user.getStylish() - 1));
      this.getAvatarService().requestYourInfo(this.user);
      const players = this.user.getZone().getPlayers();
      for (const player of players) {
        EffectService.createEffect()
          .session(player.session)
          .id(toByte(16))
          .style(toByte(0))
          .loopLimit(toByte(5))
          .loop(toShort(1))
          .loopType(toByte(1))
          .radius(toShort(1))
          .idPlayer(NpcName.THO_KIM_HOAN + Npc.ID_ADD)
          .send();
      }
      this.getService().serverDialog('Chúc mừng bạn đã ghép đồ thành công');

      const z = this.user.getZone();
      if (z != null) {
        let npc = npcManager.find(z.getMap().getId(), z.getId(), NpcName.THO_KIM_HOAN + Npc.ID_ADD);
        if (npc == null) {
          npc = npcManager.find(z.getMap().getId(), z.getId(), NpcName.Chay_To_Win + Npc.ID_ADD);
          for (const player of players) {
            EffectService.createEffect()
              .session(player.session)
              .id(toByte(16))
              .style(toByte(0))
              .loopLimit(toByte(5))
              .loop(toShort(1))
              .loopType(toByte(1))
              .radius(toShort(1))
              .idPlayer(NpcName.THO_KIM_HOAN + Npc.ID_ADD)
              .send();
          }
        }
        // NOTE: giữ nguyên hành vi bản Java (nếu vẫn không tìm được npc thì NPE ở đây)
        npc.setTextChats([messageFormat('Chúc mừng bạn {0} đã nâng cấp vật phẩm {1} thành công',
          this.user.getUsername(), item.getItem().getPart().getName())]);
      } else {
        return;
      }
    } else {
      this.getAvatarService().updateMoney(0);
      this.getService().serverDialog('Ghép đồ thất bại. Chúc bạn may mắn lần sau');
    }
  }

  /* ----------------------------- quay số ----------------------------- */

  doDialLucky(ms) {
    const partId = ms.reader().readShort();
    const degree = ms.reader().readShort();
    const dl = this.user.getDialLucky();
    if (dl != null) {
      if (dl.getType() === DialLuckyManager.MIEN_PHI) {
        const itm = this.user.findItemInChests(593);
        if (itm == null || itm.getQuantity() <= 0) {
          return;
        }
        this.user.removeItem(593, 1);
      }
      if (dl.getType() === DialLuckyManager.XU) {
        if (this.user.getXu() < 15000) {
          return;
        }
        // NOTE: giữ nguyên hành vi bản Java (kiểm tra 15000 nhưng trừ 25000)
        this.user.updateXu(-25000);
      }
      if (dl.getType() === DialLuckyManager.LUONG) {
        if (this.user.getLuong() < 5) {
          return;
        }
        this.user.updateLuong(-5);
      }
      this.getAvatarService().updateMoney(0);
      dl.doDial(this.user, partId, degree);
    }
  }

  /* ------------------------------ công viên ------------------------------ */

  doParkBuyItem(ms) {
    const id = ms.reader().readShort();
    const food = foodManager.findFoodByFoodID(id);
    if (food != null) {
      const shop = food.getShop();
      const price = food.getPrice();
      if (price > this.user.xu) {
        this.user.getService().serverDialog('Bạn không đủ xu!');
        return;
      }
      const name = food.getName();
      this.user.updateXu(-price);
      if (shop === 4) {
        let health = 100 - this.user.getHunger() + food.getPercentHelth();
        health = ((health > 100) ? 100 : health);
        this.user.updateHunger(100 - health);
        this.user.getAvatarService()
          .serverDialog(`Bạn đã ăn một ${name} sức khoẻ bạn hiện tại là ${health}`);
      } else if (shop === 5) {
        this.user.getService().serverDialog('Bạn đã cho thú nuôi ăn thành công');
      }
    }
  }

  requestFriendList(ms) {
    this.user.getAvatarService().chatTo('admin', 'ok', -1);
    this.user.getAvatarService().serverDialog('comingsion');
    return;
    //        ms = new Message(Cmd.CUSTOM_LIST);
    //        DataOutputStream ds = ms.writer();
    //        ds.flush();
  }

  /* -------------------------------- nhà -------------------------------- */

  async joinHouse(ms) {
    const userId = ms.reader().readInt();
    const hItems = [];

    try {
      const GET_HOUSE_DATA = 'SELECT * FROM `house_buy` WHERE `user_id` = ? LIMIT 1';
      const res = await dbManager.queryOne(GET_HOUSE_DATA, [userId]);

      if (res != null) {
        const ja_map = JSONParse(res.map_data) || [];
        const map_data = new Array(ja_map.length);
        for (let i = 0; i < ja_map.length; ++i) {
          map_data[i] = toByte(Number(ja_map[i]));
        }

        const GET_ITEMS_IN_CHEST = 'SELECT * FROM `house_player_item` WHERE `user_id` = ?';
        const rows = await dbManager.query(GET_ITEMS_IN_CHEST, [userId]);

        if (rows != null) {
          for (const r of rows) {
            const hItem = new HouseItem();
            hItem.itemId = toShort(Number(r.house_item_id));
            hItem.x = toByte(Number(r.x));
            hItem.y = toByte(Number(r.y));
            hItem.rotate = toByte(Number(r.rotate));
            hItems.push(hItem);
          }
        }

        this.user.getZone().leave(this.user);
        ms = new Message(-65);
        const ds = ms.writer();
        ds.writeByte(3);
        ds.writeInt(this.user.getId());
        ds.writeShort(map_data.length);
        for (let j = 0; j < map_data.length; ++j) {
          // Java: ds.write(int) -> ghi 1 byte thấp
          ds.writeByte(map_data[j]);
        }
        ds.writeByte(28);
        ds.writeShort(hItems.length);
        for (const hItem2 of hItems) {
          ds.writeShort(hItem2.itemId);
          ds.writeByte(hItem2.x);
          ds.writeByte(hItem2.y);
          ds.writeByte(hItem2.rotate);
        }
        ds.flush();
        this.sendMessage(ms);
      }
    } catch (e) {
      console.error(e);
    }
  }

  async BuyHouse() {
    const userId = this.user.session.user.getId();
    try {
      const GET_HOUSE_DATA = 'SELECT * FROM `house_buy` WHERE `user_id` = ? LIMIT 1';
      const res = await dbManager.queryOne(GET_HOUSE_DATA, [userId]);

      if (res != null) {
        this.user.session.getAvatarService().serverDialog('Bạn đã mua nhà rồi !');
        return;
      }
      if (this.user.session.user.getXu() < 1000000) {
        this.user.session.getAvatarService().serverDialog('Bạn không đủ tiền để mua nhà !');
        return;
      }

      this.user.updateXu(-1000000);
      this.user.getAvatarService().updateMoney(0);
      // Nếu chưa mua, tiến hành chèn dữ liệu nhà mới
      const INSERT_HOUSE_DATA = 'INSERT INTO `house_buy` (user_id, type, map_data, date_expired) VALUES (?, ?, ?, ?)';
      const rowsInserted = await dbManager.executeUpdate(INSERT_HOUSE_DATA, [
        userId,
        3, // `type`: thay đổi theo logic của bạn
        '[39,39,39,39,34,35,35,35,35,35,35,34,35,35,35,34,35,35,35,35,35,35,34,39,39,39,39,39,39,39,39,39,38,25,25,25,25,25,25,36,27,27,27,36,25,25,25,25,25,25,37,39,39,39,39,39,39,39,39,39,38,26,26,26,26,26,26,36,28,28,28,36,26,26,26,26,26,26,37,39,39,39,39,39,39,39,39,39,38,14,14,14,14,14,14,36,3,3,3,36,14,14,14,14,14,14,37,39,39,39,39,39,39,39,39,39,38,14,14,14,14,14,14,25,3,3,3,25,14,14,14,14,14,14,37,39,39,39,39,39,39,39,39,39,38,14,14,14,14,14,14,26,3,3,3,26,14,14,14,14,14,14,37,39,39,39,39,39,39,39,39,39,38,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,37,39,39,39,39,39,39,39,39,39,25,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,37,39,39,39,39,39,34,35,35,35,25,35,35,35,35,35,35,38,14,14,14,14,14,14,14,14,14,14,25,39,39,39,39,39,38,27,27,27,36,23,23,23,23,23,23,36,0,0,35,35,35,35,35,35,35,35,25,35,35,35,35,34,38,28,28,28,36,24,24,24,24,24,24,36,0,0,23,23,23,23,23,23,23,23,36,23,23,23,23,36,38,17,17,17,25,9,9,18,9,9,9,25,0,0,24,24,24,24,24,24,24,24,36,24,24,24,24,36,38,17,17,17,26,9,9,9,9,9,9,26,0,0,9,9,9,9,9,9,9,9,36,9,9,9,9,36,38,17,17,17,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,25,9,9,9,9,36,38,17,17,17,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,26,9,9,9,9,36,35,35,35,35,35,35,35,35,35,35,35,38,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,36,23,23,23,23,23,23,23,23,23,23,23,38,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,36,24,24,24,24,24,24,24,24,24,24,24,35,35,35,35,9,9,9,9,35,35,35,35,35,35,35,35,35,39,39,39,39,39,39,39,39,39,39,39,24,24,24,24,40,40,40,40,24,24,24,24,24,24,24,24,24]', // `map_data`
        '2000-01-01', // `date_expired`
      ]);
      if (rowsInserted > 0) {
        this.user.session.getAvatarService().serverDialog('Bạn đã mua nhà thành công!');
      }
    } catch (e) {
      console.error(e);
    }
  }

  /* ---------------------------- đổi mật khẩu ---------------------------- */

  async changePassword(ms) {
    const passOld = ms.reader().readUTF();
    const passNew = ms.reader().readUTF();
    try {
      const ACCOUNT_LOGIN = 'SELECT * FROM `users` WHERE `id` = ? AND `password` = ? LIMIT 1';
      const red = await dbManager.queryOne(ACCOUNT_LOGIN, [this.user.getId(), Utils.md5(passOld)]);
      if (red != null) {
        const ACCOUNT_UPDATE_PASSWORD = 'UPDATE `users` SET `password` = ? WHERE `id` = ?';
        const result = await dbManager.executeUpdate(ACCOUNT_UPDATE_PASSWORD,
          [Utils.md5(passNew), this.user.getId()]);
        if (result > 0) {
          ms = new Message(-62);
          const ds = ms.writer();
          ds.writeUTF(passNew);
          ds.flush();
          this.sendMessage(ms);
          this.user.getService().serverDialog('Đổi mật khẩu thành công.');
        } else {
          this.user.getAvatarService()
            .serverDialog('Có lỗi xảy ra, vui lòng thử lại sau.');
        }
      } else {
        this.user.getService().serverDialog('Mật khẩu cũ không đúng.');
      }
    } catch (e) {
      console.error(e);
      console.log(e.message);
    }
  }
}

export default GameSession;
