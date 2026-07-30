// Port của avatar/model/Boss.java
import { randomInt } from 'node:crypto';
import net from 'node:net';

import { Cmd } from '../constants/Cmd.js';
import { Item } from '../item/Item.js';
import { MessageHandler } from '../message/MessageHandler.js';
import { ParkMsgHandler } from '../message/ParkMsgHandler.js';
import { DataOutputStream } from '../net/JavaIO.js';
import { Message } from '../net/Message.js';
import { Session } from '../net/Session.js';
import { mapManager } from '../play/MapManager.js';
import { serverManager } from '../server/ServerManager.js';
import { userManager } from '../server/UserManager.js';
import { Utils } from '../server/Utils.js';
import { EffectService } from '../service/EffectService.js';
import { Npc } from './Npc.js';
import { User } from './User.js';

// Ép về short/byte kiểu Java
const toShort = (v) => ((v | 0) << 16) >> 16;

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

export class Boss extends User {

  static TOTAL_BOSSES = 400000000; // Tổng số Boss muốn tạo
  static currentBossId = 1001 + Npc.ID_ADD; // ID bắt đầu cho Boss
  static bossCount = 0; // Đếm số lượng Boss đã được tạo
  static CHARACTERS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

  constructor() {
    super();
    this.textChats = null;
    this.zoneCoordinates = new Map();//tọa độ boss di chuyển trong map
    this.initializeCoordinates();

    //tọa độ boss dichuyeeren
    //        zoneCoordinates.put(11, [[182,121],[282,142],[282,88],[326,150]]);
    //        zoneCoordinates.put(7, [[212,90],[112,118],[292,118]]);
    //        zoneCoordinates.put(1, [[80,105],[244,109],[172,154]]);

    //autoChatBot.start();
  }

  getTextChats() {
    return this.textChats;
  }

  setTextChats(textChats) {
    this.textChats = textChats;
  }

  initializeCoordinates() {
    const map11 = [
      [270, 100]
    ];
    this.zoneCoordinates.set(11, map11);

    const map7 = [
      [216, 97]
    ];
    this.zoneCoordinates.set(7, map7);

    const map1 = [
      [165, 100]
    ];
    this.zoneCoordinates.set(1, map1);

    const map2 = [
      [204, 85]
    ];
    this.zoneCoordinates.set(2, map2);

    const map3 = [
      [288, 97]
    ];
    this.zoneCoordinates.set(3, map3);

    const map5 = [
      [188, 89]
    ];
    this.zoneCoordinates.set(5, map5);

    const map8 = [
      [264, 69]
    ];
    this.zoneCoordinates.set(8, map8);
  }

  // NOTE: giữ nguyên hành vi bản Java — thread autoChatBot được khai báo nhưng
  // KHÔNG được start() (dòng autoChatBot.start() bị comment trong constructor).
  async autoChatBot() {
    while (true) {
      try {
        if (this.textChats == null) {
          this.textChats = []; // Hoặc khởi tạo với một giá trị mặc định
        }
        for (const text of this.textChats) {
          this.getMapService().chat(this, text);
          await sleep(6000);
        }
        if (this.textChats == null || this.textChats.length === 0) {
          await sleep(10000);
        }
      } catch (ignored) {
        return; // Đảm bảo xử lý gián đoạn
      }
    }
  }

  async handleBossDefeat(boss, us) {
    //update lượt boss.
    const username = us.getUsername();

    if (us.getHunger() < 100) {
      const idItems = 2385;
      const keoAcMa = new Item(2385, -1, 1);
      if (us.findItemInChests(idItems) != null) {
        const quantity = us.findItemInChests(idItems).getQuantity();
        us.findItemInChests(idItems).setQuantity(quantity + 1);
      } else {
        us.addItemToChests(keoAcMa);
      }
      us.updateHunger(+1);//+1 cho slot 100 hop
      us.getAvatarService().SendTabmsg('Bạn vừa nhận được 1 ' + ' ' + keoAcMa.getPart().getName());
    }

    if (boss.getWearing()[1].getId() === 5112 && us.getHappy() < 100) {

      us.updateHappy(+1);//+1 cho slot 100 hop
      const hopqua = new Item(5532, Date.now() + (86400000 * 7), 1);
      us.addItemToChests(hopqua);
      userManager.users.forEach((user) => {
        user.getAvatarService().serverInfo('Chúc mừng bạn : ' + us.getUsername() + ' đã Kill được trùm ma bí và nhận 1 hộp quà Ma Quái (' + us.getHappy() + '/100) mọi người đều ngưỡng mộ.');
      });
    }

    const message = 'Khá lắm bạn ' + username + ' đã kill được '
      + boss.getUsername().substring(3, boss.getUsername().length - 6);
    const newMessages = [message, 'Ta sẽ quay lại sau!!!'];
    this.textChats = [...newMessages];

    // NOTE: giữ nguyên hành vi bản Java — xoá phần tử ngay trong for-each nên
    // vòng lặp chỉ chat được message đầu tiên rồi dừng (Iterator.hasNext() sai).
    let cursor = 0;
    while (cursor < this.textChats.length) {
      const chatMessage = this.textChats[cursor];
      cursor++;
      this.getMapService().chat(boss, chatMessage);
      const idx = this.textChats.indexOf(chatMessage);
      if (idx >= 0) this.textChats.splice(idx, 1);
    }

    // Java: scheduler.schedule(..., 5, TimeUnit.SECONDS)
    setTimeout(async () => {
      try {
        const now = new Date();
        const nowMin = now.getHours() * 60 + now.getMinutes();
        const tenAM = 10 * 60;
        const twoPM = 14 * 60;
        const sevenPM = 17 * 60;
        const elevenPM = 23 * 60;

        //tạo qu trong time
        if ((nowMin > tenAM && nowMin < twoPM) || (nowMin > sevenPM && nowMin < elevenPM)) {
          await this.createNearbyGiftBoxes(boss, boss.getZone(), boss.getX(), boss.getY(), Boss.currentBossId + 10000);
        }
        //boss.session.close();
        const m = mapManager.find(boss.getBossMapId());
        const zones = m.getZones();
        const randomZone = zones[Utils.nextInt(zones.length)];
        boss.getZone().leave(boss);
        await this.addBossToZone(boss, boss.bossMapId, randomZone, 0, 0, Utils.nextInt(70000, 120000));

      } catch (e) {
        console.error(e);
      }
    }, 5000); // 4 giây trễ trước khi thực hiện các hành động khác

    // Gửi hiệu ứng cho người chơi trong khu vực (bản Java comment lại)
  }

  async hanlderNhatHopQua(boss, us) {
    us.getAvatarService().serverDialog('bạn đã nhặt được hộp quà');
    //int time = Utils.getRandomInArray(new int[]{3, 7, 15, 30});
    const hopqua = new Item(683, -1, 1);
    //hopqua.setExpired(System.currentTimeMillis() + (86400000L * time));

    if (us.findItemInChests(683) != null) {
      const quantity = us.findItemInChests(683).getQuantity();
      us.findItemInChests(683).setQuantity(quantity + 1);
    } else {
      us.addItemToChests(hopqua);
    }
    serverManager.disconnect(boss.session);
    boss.session.close();
  }

  async addBossToZone(boss, Map, zone, x, y, hp) {
    if (Boss.bossCount >= Boss.TOTAL_BOSSES) {
      return; // Dừng nếu đã tạo đủ số lượng Boss
    }

    boss.getWearing().length = 0;
    boss.setId(Boss.currentBossId++);
    boss.setDefeated(false);
    this.assignRandomItemToBoss(boss);
    boss.setHP(hp);
    if (boss.getWearing()[1].getId() === 5112) {
      boss.setHP(hp + 90000);
      const chatMessages = ['gãi ngứa hả tên kia', 'Mau nộp kẹo cho taaaa'];
      boss.setTextChats(chatMessages);
    }
    boss.bossMapId = Map;
    Boss.bossCount++; // Tăng số lượng Boss đã tạo
    await this.sendAndHandleMessages(boss);
    await this.moveBoss(boss);
    const mapId = zone.getMap().getId();
    ///
    const coordinates = this.zoneCoordinates.get(mapId);
    if (coordinates != null && coordinates.length !== 0) {
      const randomCoordinate = coordinates[Utils.nextInt(coordinates.length)];
      await this.moveBossXY(boss, randomCoordinate[0], randomCoordinate[1]);
    } else {
      // Nếu không có tọa độ nào, có thể xử lý trường hợp này ở đây
      console.error('Không có tọa độ cho bản đồ ID ' + mapId);
    }
  }

  async MoveArea(boss) {
    const dos2 = new DataOutputStream();
    dos2.writeByte(boss.bossMapId);
    console.error('joinmaopboss ' + boss.bossMapId);
    dos2.writeByte(Utils.nextInt(9));
    dos2.writeShort(boss.getX());//x
    dos2.writeShort(boss.getY());//y
    dos2.flush();
    const dataJoinPak = dos2.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.AVATAR_JOIN_PARK, dataJoinPak));

    console.log('add boss khu :' + boss.getZone().getId());
  }

  createBoss(x, y, id) {
    const boss = new Boss();
    boss.setId(id);
    boss.setX(x);
    boss.setY(y);
    return boss;
  }

  async createGiftBox(zone, x, y, giftId) {
    const giftBox = this.createBoss(x, y, giftId);
    this.assignGiftItemToBoss(giftBox);// Gán item cho hộp quà
    giftBox.setUsername('');
    giftBox.session = Boss.createSession(giftBox);
    giftBox.setSpam(10);
    await this.sendAndHandleMessages(giftBox);
    await this.addGiftToZone(giftBox, zone);
    await this.moveGift(giftBox);
  }

  async createNearbyGiftBoxes(boss, zone, x, y, baseGiftId) {
    // Tạo hộp quà ở các vị trí gần Boss
    await this.createGiftBox(zone, toShort(boss.getX() + toShort(20)), toShort(boss.getY() + toShort(20)), baseGiftId);
    await this.createGiftBox(zone, toShort(boss.getX() - toShort(20)), toShort(boss.getY() - toShort(20)), baseGiftId + 1);
    await this.createGiftBox(zone, toShort(boss.getX() + toShort(20)), toShort(boss.getY() - toShort(20)), baseGiftId + 2);
    await this.createGiftBox(zone, toShort(boss.getX() - toShort(20)), toShort(boss.getY() + toShort(20)), baseGiftId + 3);
  }

  assignGiftItemToBoss(boss) {
    // Gán item cụ thể cho hộp quà phân thân, nếu khác với Boss chính
    const giftItems = [2215, 2215, 2215]; // Ví dụ các item cho hộp quà
    const randomItemId = giftItems[Utils.nextInt(giftItems.length)];
    boss.addItemToWearing(new Item(randomItemId));
  }

  //đồ của boss
  assignRandomItemToBoss(boss) {
    const itemIds = [0, 5112, 2469, 2470, 6428, 6431, 4304];//sen bo hung
    const itemIds1 = [0, 2468, 2469, 2470, 2282, 4304];//ma bu
    const itemIds2 = [0, 8, 2471, 2472, 2473, 3495, 4304];//ma bu map
    const itemIds3 = [10, 2049, 2050, 2051];
    const itemIds4 = [10, 2099, 2100, 2101];
    const itemIds5 = [10, 6036, 6037, 6038];

    const itemIds6 = [10, 2049, 2050, 2051];
    const itemIds7 = [10, 2099, 2100, 2101];

    const itemListToName = new Map();
    itemListToName.set(itemIds, 'TrumMaBi');
    itemListToName.set(itemIds1, 'MaBi');
    itemListToName.set(itemIds2, 'Frankeinstein');
    itemListToName.set(itemIds3, 'XuongKho');
    itemListToName.set(itemIds4, 'XacUop');
    itemListToName.set(itemIds5, 'TrumXacUop');

    itemListToName.set(itemIds6, 'XuongKho');
    itemListToName.set(itemIds7, 'XacUop');

    const allItemLists = [itemIds, itemIds1, itemIds2, itemIds3, itemIds4, itemIds5, itemIds6, itemIds7];
    const randomIndex = Utils.nextInt(allItemLists.length);
    const randomList = allItemLists[randomIndex];
    const bossName = itemListToName.get(randomList);

    for (const itemId of randomList) {
      const item = new Item(itemId);
      boss.addItemToWearing(item);
    }
    const bossUsername = Boss.generateRandomUsername(4).toLowerCase();
    const bossUsername1 = Boss.generateRandomUsername(3).toLowerCase();
    boss.setUsername(bossUsername + bossName + bossUsername1);
  }

  async sendAndHandleMessages(boss) {
    const dos = new DataOutputStream();
    dos.writeByte(0);
    dos.writeInt(1024);
    dos.writeUTF('MicroEmulator');
    dos.writeInt(512);
    dos.writeInt(1080);
    dos.writeInt(1920);
    dos.writeBoolean(true);
    dos.writeByte(0);
    dos.writeUTF('v1.0');
    dos.writeUTF('1');
    dos.writeUTF('2');
    dos.writeUTF('3');
    dos.flush();
    const data = dos.toBuffer();

    const handler = new MessageHandler(boss.session);
    await handler.onMessage(new Message(Cmd.SET_PROVIDER, data));

    const data2 = Buffer.from([9]);
    await boss.session.getHandler(new Message(Cmd.GET_HANDLER, data2));
    if (boss.getId() > 2000010000) {
      return;
    }
    await this.MoveArea(boss);
  }

  async moveBoss(boss) {
    const dos1 = new DataOutputStream();
    dos1.writeShort(boss.getX());//x
    dos1.writeShort(boss.getY());//y
    const ranArea = Utils.nextInt(9);
    dos1.writeByte((ranArea << 24) >> 24);
    dos1.flush();
    const data1 = dos1.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.MOVE_PARK, data1));
  }

  async addGiftToZone(gift, zone) {
    const dos2 = new DataOutputStream();
    dos2.writeByte(zone.getMap().getId());
    dos2.writeByte(zone.getId());
    dos2.writeShort(gift.getX());//x
    dos2.writeShort(gift.getY());//y
    dos2.flush();
    const dataJoinPak = dos2.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(gift.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.AVATAR_JOIN_PARK, dataJoinPak));
  }

  async moveGift(boss) {
    const dos1 = new DataOutputStream();
    dos1.writeShort(boss.getX());//x
    dos1.writeShort(boss.getY());//y
    dos1.writeByte(0);
    dos1.flush();
    const data1 = dos1.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.MOVE_PARK, data1));
    //getMapService().chat(this, "ta đến rồi đây");

    // Java: new Timer().schedule(..., 120000)
    setTimeout(() => {
      boss.session.close();
    }, 120000); // 2 phút = 120000 ms
  }

  async moveBossXY(boss, x, y) {
    const dos1 = new DataOutputStream();
    dos1.writeShort(x);//x
    dos1.writeShort(y);//y
    dos1.writeByte(2);
    dos1.flush();
    const data1 = dos1.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.MOVE_PARK, data1));
  }

  static createSession(boss) {
    //Cmd.SET_PROVIDER
    try {
      // Tạo một Socket (thay thế bằng thông tin kết nối thực tế)
      // NOTE: Java Socket.connect() chặn cho tới khi xong; Node kết nối bất đồng bộ
      // nhưng dữ liệu ghi ra được đệm nên hành vi tương đương.
      const socket = net.createConnection({ host: 'localhost', port: 19128 });
      socket.on('error', () => { /* bản Java bỏ qua lỗi kết nối */ });
      const sessionId = boss.getId(); // Ví dụ về ID, có thể là bất kỳ giá trị nào phù hợp
      const session = new Session(socket, sessionId);
      session.ip = '127.0.0.1';
      session.user = boss;
      session.connected = true;
      session.login = true;
      console.log('Session created with ID: ' + session.id);
      return session;
    } catch (e) {
      console.error(e);
    }
    return null;
  }

  addChat(chat) {
    this.textChats.push(chat);
  }

  sendMessage(ms) {

  }

  BossSkillRanDomUser(skill1, skill2) {
    const players = this.session.user.getZone().getPlayers();
    let randomPlayer = null;
    while (randomPlayer == null) {
      const rplayerIndex = Utils.nextInt(players.length);
      const playerss = players[rplayerIndex];

      if (playerss.getId() < Npc.ID_ADD) {
        randomPlayer = playerss;
      }
    }
    for (const player of players) {
      EffectService.createEffect()
        .session(player.session)
        .id(skill1)
        .style(0)
        .loopLimit(5)
        .loop(3)
        .loopType(1)
        .radius(1)
        .idPlayer(this.session.user.getId())
        .send();
      EffectService.createEffect()
        .session(player.session)
        .id(skill2)
        .style(0)
        .loopLimit(5)
        .loop(3)
        .loopType(1)
        .radius(1)
        .idPlayer(randomPlayer.getId())
        .send();
    }
  }

  static generateRandomUsername(length) {
    let sb = '';
    for (let i = 0; i < length; i++) {
      const randomIndex = randomInt(Boss.CHARACTERS.length); // Java: SecureRandom
      sb += Boss.CHARACTERS.charAt(randomIndex);
    }
    return sb;
  }

  static async spawnBossesForMap(mapId, numBosses) {
    const m = mapManager.find(mapId);
    const zones = m.getZones();
    for (let i = 0; i < numBosses; i++) {
      const boss = new Boss(); // Tạo boss mới
      const chatMessages = ['YAAAA', 'YOOOO'];
      boss.setTextChats(chatMessages);
      boss.session = Boss.createSession(boss);

      const randomZone = zones[Utils.nextInt(zones.length)];
      try {
        await boss.addBossToZone(boss, mapId, randomZone, 50, 50, 50000);
        console.log('Boss ' + i + ' khu ' + randomZone.getId() + ' map ' + mapId);
      } catch (e) {
        throw e; // NOTE: giữ nguyên hành vi bản Java (throw new RuntimeException(e))
      }
    }
  }
}

export default Boss;
