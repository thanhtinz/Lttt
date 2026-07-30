/** Port của avatar/play/offline/botPlayer.java */
import net from 'net';

import { Cmd } from '../../constants/Cmd.js';
import { dbManager } from '../../db/DbManager.js';
import { Item } from '../../item/Item.js';
import { MessageHandler } from '../../message/MessageHandler.js';
import { ParkMsgHandler } from '../../message/ParkMsgHandler.js';
import { User } from '../../model/User.js';
import { Message } from '../../net/Message.js';
import { Session } from '../../net/Session.js';
import { DataOutputStream } from '../../net/JavaIO.js';
import { MapManager } from '../MapManager.js';
import { CharacterInfo } from './CharacterInfo.js';

export class botPlayer extends User {

  constructor() {
    super();
    this.textChats = null;
    this.initializeCoordinates();
  }

  static getInstance() {
    if (botPlayer.instance == null) {
      botPlayer.instance = new botPlayer();
    }
    return botPlayer.instance;
  }
  // Khởi tạo danh sách tên và gender

  initializeCoordinates() {
    const map11 = [
      [120, 47],
      [34, 36],
      [146, 60],
      [107, 79],
      [164, 106],
      [58, 69],
      [78, 89],
      [162, 57],
      [226, 161],
      [274, 54],
      [470, 151],
      [500, 151],
      [524, 151],
      [189, 60],
      [62, 109],
      [150, 54]
    ];
    botPlayer.zoneCoordinates.set(11, map11);

    const map7 = [
      [130, 48]
    ];
    botPlayer.zoneCoordinates.set(7, map7);

    const map1 = [
      [165, 100]
    ];
    botPlayer.zoneCoordinates.set(1, map1);

    const map2 = [
      [204, 85]
    ];
    botPlayer.zoneCoordinates.set(2, map2);

    const map3 = [
      [288, 97]
    ];
    botPlayer.zoneCoordinates.set(3, map3);

    const map5 = [
      [188, 89]
    ];
    botPlayer.zoneCoordinates.set(5, map5);

    const map8 = [
      [264, 69]
    ];
    botPlayer.zoneCoordinates.set(8, map8);

    const map0 = [
      [92, 50],
      [92, 81],
      [276, 157]
    ];
    botPlayer.zoneCoordinates.set(0, map0);

    const map9 = [
      [258, 156],
      [494, 44],
      [534, 48],
      [543, 76],
      [450, 88]
    ];
    botPlayer.zoneCoordinates.set(9, map9);

    const map23 = [
      [646, 24],
      [742, 48],
      [702, 60],
      [746, 72],
      [654, 96],
      [750, 115],
      [830, 120]
    ];
    botPlayer.zoneCoordinates.set(23, map23);

    const map27 = [
      [314, 80],
      [350, 104],
      [460, 60],
      [446, 88],
      [518, 40],
      [598, 28],
      [622, 48]
    ];
    botPlayer.zoneCoordinates.set(27, map27);
  }

  async addBotToZone(boss, Map, zone) {
    if (botPlayer.bossCount >= botPlayer.TOTAL_BOSSES) {
      return; // Dừng nếu đã tạo đủ số lượng Boss
    }

    boss.getWearing().length = 0;
    boss.setId(botPlayer.currentBossId++);
    await this.assignRandomItemToBoss(boss);
    boss.bossMapId = Map;
    botPlayer.bossCount++;
    await this.sendAndHandleMessages(boss);
    const mapId = zone.getMap().getId();

    if (mapId === 11) {
      await this.moveBot(boss, 0);
    } else {
      const randomInt = Math.floor(Math.random() * 2); // Kết quả sẽ là 0 hoặc 1
      await this.moveBot(boss, randomInt);
    }

    const coordinates = botPlayer.zoneCoordinates.get(mapId);
    if (coordinates != null && coordinates.length !== 0) {
      // Lấy tọa độ theo chỉ mục hiện tại
      const coordinate = coordinates[botPlayer.currentCoordinateIndex];
      await this.moveBossXY(boss, coordinate[0], coordinate[1]);

      // Cập nhật chỉ mục tiếp theo (quay lại 0 nếu đã tới cuối danh sách)
      botPlayer.currentCoordinateIndex = (botPlayer.currentCoordinateIndex + 1) % coordinates.length;
    } else {
      console.error('Không có tọa độ cho bản đồ ID ' + mapId);
    }
  }

  async MoveArea(boss, khu) {
    const dos2 = new DataOutputStream();
    dos2.writeByte(boss.bossMapId);
    console.error('bot join ' + boss.bossMapId);
    dos2.writeByte(khu);
    dos2.writeShort(boss.getX()); // x
    dos2.writeShort(boss.getY()); // y
    dos2.flush();
    const dataJoinPak = dos2.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.AVATAR_JOIN_PARK, dataJoinPak));
    console.log('add boss khu :' + boss.getZone().getId());
  }

  async assignRandomItemToBoss(boss) {
    const nameAndGender = CharacterInfo.getRandomAndRemove();
    boss.setUsername(nameAndGender.getKey());
    boss.setGender(nameAndGender.getValue());

    const GET_PLAYER_DATA = 'SELECT * FROM `players` WHERE `user_id` = ? AND `gender` = ? LIMIT 1;';
    let found = false;

    // NOTE: giữ nguyên hành vi bản Java (lặp tới khi tìm được player khớp gender)
    while (!found) {
      try {
        const randomUserId = 151 + Math.floor(Math.random() * (910 - 151 + 1));
        const res = await dbManager.queryOne(GET_PLAYER_DATA, [randomUserId, boss.getGender()]);
        if (res != null) {
          found = true; // Đặt cờ để thoát khỏi vòng lặp

          // Lấy danh sách wearings từ kết quả
          const wearings = [];
          const wearing = JSON.parse(res.wearing);

          for (const obj of wearing) {
            const id = obj.id | 0;
            const expired = Number(obj.expired);
            const quantity = Object.prototype.hasOwnProperty.call(obj, 'quantity') ? (obj.quantity | 0) : 1;

            const item = Item.builder().id(id)
              .quantity(quantity)
              .expired(expired)
              .build();

            if (item.reliability() > 0) {
              wearings.push(item);
            }
          }

          // Thêm items vào wearing của boss
          for (const item of wearings) {
            boss.addItemToWearing(item);
          }
        }
      } catch (ex) {
        console.error(ex);
        this.getService().serverMessage(ex.message);
      }
    }
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
    await this.MoveArea(boss, Math.floor(Math.random() * 2));
  }

  async moveBot(boss, khu) {
    const dos1 = new DataOutputStream();
    dos1.writeShort(boss.getX()); // x
    dos1.writeShort(boss.getY()); // y
    dos1.writeByte(khu);
    dos1.flush();
    const data1 = dos1.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.MOVE_PARK, data1));
  }

  async moveBossXY(boss, x, y) {
    console.log('Di chuyển boss tới tọa độ: (' + x + ', ' + y + ')');
    const dos1 = new DataOutputStream();
    dos1.writeShort(x); // x
    dos1.writeShort(y); // y
    dos1.writeByte(Math.random() < 0.5 ? 0 : 2);
    dos1.flush();
    const data1 = dos1.toBuffer();
    const parkMsgHandler1 = new ParkMsgHandler(boss.session);
    await parkMsgHandler1.onMessage(new Message(Cmd.MOVE_PARK, data1));
  }

  static createSession(boss) {
    //Cmd.SET_PROVIDER
    try {
      // Tạo một Socket (thay thế bằng thông tin kết nối thực tế)
      // NOTE: Node connect bất đồng bộ, Session được trả về ngay như bản Java
      const socket = new net.Socket();
      socket.connect(19128, 'localhost'); // Thay thế bằng địa chỉ IP và cổng thực tế
      socket.on('error', (e) => console.error(e));
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

  static async spawnBotesForMap(mapId) {
    const m = MapManager.getInstance().find(mapId);
    const zones = m.getZones();

    // Lấy số lượng vị trí tương ứng với mapId
    const coordinates = botPlayer.zoneCoordinates.get(mapId);
    const numBosses = (coordinates != null) ? coordinates.length : 0; // Số lượng boss bằng số lượng vị trí

    for (let i = 0; i < numBosses; i++) {
      const bot = new botPlayer(); // Tạo boss mới
      bot.session = botPlayer.createSession(bot);

      // Chọn zone ngẫu nhiên từ danh sách các zone trong bản đồ
      const randomZone = zones[i % zones.length]; // Sử dụng số chỉ để tránh lỗi vượt quá kích thước
      await bot.addBotToZone(bot, mapId, randomZone);
      console.log('Bot ' + i + ' khu ' + randomZone.getId() + ' map ' + mapId);
    }
  }
}

// ==== static field của Java ====
botPlayer.instance = null;
botPlayer.zoneCoordinates = new globalThis.Map(); // tọa độ boss di chuyển trong map
botPlayer.currentCoordinateIndex = 0;
botPlayer.TOTAL_BOSSES = 400000000; // Tổng số Boss muốn tạo
botPlayer.currentBossId = 8100; // ID bắt đầu cho Boss
botPlayer.bossCount = 0; // Đếm số lượng Boss đã được tạo

export default botPlayer;
