/**
 * Port của avatar/server/ServerManager.java: nạp config, nạp settings từ DB,
 * mở TCP socket, quản lý clients, và SettingsWatcher (live-control cho admin panel).
 */
import net from 'net';
import path from 'path';
import dbManager, { loadProperties } from '../db/DbManager.js';
import { Session } from '../net/Session.js';
import userManager from './UserManager.js';

class ServerManager {
  constructor() {
    this.cityName = '';
    this.hashSettings = null;
    this.active = true;         // false = đang bảo trì
    this.port = 19128;
    this.notify = '';
    this.expRate = 1.0;
    this.bigImgVersion = 0;
    this.partVersion = 0;
    this.bigItemImgVersion = 0;
    this.itemTypeVersion = 0;
    this.itemVersion = 0;
    this.objectVersion = 0;
    this.resHDPath = 'res/hd/';
    this.resMediumPath = 'res/medium/';
    this.numClients = 0;
    /** @type {Session[]} */
    this.clients = [];
    this.start = false;
    this.id = 0;
    this.debug = false;
    this.startTime = Date.now();
    this.baseDir = process.cwd();
  }

  loadConfigFile(baseDir = this.baseDir) {
    const p = loadProperties(path.join(baseDir, 'config.properties'));
    if (p['server.port']) this.port = Number(p['server.port']);
    this.cityName = p['game.city.name'] ?? this.cityName;
    if (p['server.active'] != null) this.active = String(p['server.active']) === 'true';
    if (p['server.debug'] != null) this.debug = String(p['server.debug']) === 'true';
    if (p['game.notify'] != null) this.notify = p['game.notify'];
    this.bigImgVersion = Number(p['game.big.image.version'] ?? 0);
    this.partVersion = Number(p['game.part.version'] ?? 0);
    this.bigItemImgVersion = Number(p['game.big.item.image.version'] ?? 0);
    this.itemTypeVersion = Number(p['game.itemtype.version'] ?? 0);
    this.itemVersion = Number(p['game.item.version'] ?? 0);
    this.objectVersion = Number(p['game.object.version'] ?? 0);
    this.resHDPath = p['game.resources.hd.path'] ?? this.resHDPath;
    this.resMediumPath = p['game.resources.medium.path'] ?? this.resMediumPath;
  }

  /** Đọc toàn bộ bảng settings thành Map. */
  async readSettings() {
    try {
      const rows = await dbManager.query('SELECT * FROM `settings`');
      const m = new Map();
      for (const r of rows) m.set(r.name, r.value);
      return m;
    } catch (e) {
      console.error('[settings] lỗi đọc:', e.message);
      return null;
    }
  }

  applySettings(s) {
    if (s.has('hash_settings')) this.hashSettings = s.get('hash_settings');
    if (s.has('bao_tri')) this.active = String(s.get('bao_tri')) === 'true';
    if (s.has('thong_bao')) this.notify = s.get('thong_bao');
    if (s.has('heso_exp')) {
      const v = Number(s.get('heso_exp'));
      if (Number.isFinite(v) && v > 0) this.expRate = v;
    }
  }

  async loadSettings() {
    console.log('Load settings in database');
    const s = await this.readSettings();
    if (!s) {
      process.exit(0);
    }
    this.applySettings(s);
  }

  /** Ghi (upsert) 1 key settings. */
  async putSetting(name, value) {
    try {
      const n = await dbManager.executeUpdate('UPDATE `settings` SET `value` = ? WHERE `name` = ?', [value, name]);
      if (n === 0) {
        await dbManager.executeUpdate('INSERT INTO `settings` (`name`, `value`) VALUES (?, ?)', [name, value]);
      }
    } catch (e) {
      console.error('[settings] lỗi ghi', name, e.message);
    }
  }

  /**
   * Theo dõi bảng settings mỗi 5s: nếu hash_settings đổi thì nạp lại cấu hình
   * và thực thi lệnh một-lần trong key `cmd` (admin panel "chỉnh là chạy").
   */
  startSettingsWatcher() {
    this.putSetting('server_start', String(Date.now()));
    this._watcher = setInterval(async () => {
      if (!this.start) return;
      await this.putSetting('heartbeat', String(Date.now()));
      const s = await this.readSettings();
      if (!s) return;
      const hash = s.get('hash_settings') ?? '';
      if (hash === this.hashSettings) return;
      console.log('[Settings] Phát hiện thay đổi, nạp lại cấu hình...');
      this.applySettings(s);
      const cmd = (s.get('cmd') ?? '').trim();
      if (cmd) {
        await this.executeAdminCommand(cmd);
        await this.putSetting('cmd', '');
      }
    }, 5000);
    this._watcher.unref?.();
  }

  async executeAdminCommand(cmd) {
    console.log('[Admin] Lệnh:', cmd);
    if (cmd.startsWith('broadcast:')) {
      this.broadcastAll(cmd.slice('broadcast:'.length));
    } else if (cmd === 'reset_boss') {
      this.emit?.('reset_boss');
    } else if (cmd === 'restart') {
      this.broadcastAll('May chu se khoi dong lai ngay bay gio!');
      setTimeout(() => process.exit(0), 1500);
    }
  }

  /** Gửi thông báo tới toàn bộ người chơi online. */
  broadcastAll(msg) {
    if (!msg) return;
    let n = 0;
    for (const us of [...userManager.users]) {
      try {
        us.getAvatarService?.().serverDialog?.(msg);
        n++;
      } catch { /* bỏ qua user lỗi */ }
    }
    console.log(`[Broadcast] ${msg} -> ${n} users`);
  }

  disconnect(session) {
    const i = this.clients.indexOf(session);
    if (i >= 0) this.clients.splice(i, 1);
    this.numClients = this.clients.length;
  }

  /**
   * Port của ServerManager.init(): nạp DB, config, settings, dữ liệu game,
   * khởi tạo map, NPC, reset trạng thái online và bàn minigame.
   */
  async init() {
    this.start = false;
    await dbManager.start(this.baseDir);
    this.loadConfigFile();
    await this.loadSettings();

    // Nạp động để tránh phụ thuộc vòng khi các module chưa sẵn sàng
    const { gameData } = await import('../model/GameData.js');
    const { partManager } = await import('../item/PartManager.js');
    const { foodManager } = await import('../model/FoodManager.js');
    const { mapManager } = await import('../play/MapManager.js');
    const { Map: GameMap } = await import('../play/Map.js');
    const { boardManager } = await import('./BoardManager.js');

    await gameData.load();
    await partManager.load();
    await foodManager.load();

    const numMap = 60;
    for (let i = 0; i < numMap; i++) {
      mapManager.add(new GameMap(i, 0, 10));
    }

    console.log('Load NPC data start ...');
    await this.loadNpcData();
    console.log('Reset player online ...');
    await dbManager.executeUpdate('UPDATE `players` SET `is_online` = 0, `client_id` = -1');
    console.log('Reset player online successfully');
    await boardManager.initBoards();
  }

  /** Port của ServerManager.loadNpcData(). */
  async loadNpcData() {
    const { Npc } = await import('../model/Npc.js');
    const { Item } = await import('../item/Item.js');
    const { npcManager } = await import('../play/NpcManager.js');

    let numNPC = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM `npc`');
      for (const res of rows) {
        const botID = Number(res.id);
        const botName = res.name;
        const map = Number(res.map) | 0;
        const X = Number(res.x) | 0;
        const Y = Number(res.y) | 0;

        const items = [];
        try {
          for (const o of JSON.parse(res.items || '[]')) items.push(new Item(Number(o)));
        } catch { /* items rỗng/không hợp lệ -> bỏ qua như bản Java */ }

        const chat = [];
        try {
          for (const c of JSON.parse(res.chat || '[]')) chat.push(String(c));
        } catch { /* chat rỗng */ }

        npcManager.add(new Npc(botID, botName, map, X, Y, Number(res.star) | 0, items, chat));
        numNPC++;
      }
    } catch (e) {
      console.error('[npc] lỗi nạp:', e.message);
    }
    console.log(`Load NPC data successfully: ${numNPC}`);
  }

  /**
   * Mở TCP server. `onSession` được gọi cho mỗi kết nối mới để gắn handler
   * (tách ra để phần net không phụ thuộc vào handler).
   */
  listen(onSession, SessionClass = Session) {
    this.start = true;
    const srv = net.createServer((socket) => {
      const s = new SessionClass(socket, this.id++);
      this.clients.push(s);
      this.numClients = this.clients.length;
      socket.on('close', () => this.disconnect(s));
      try {
        onSession(s);
      } catch (e) {
        console.error('[server] lỗi khởi tạo session:', e);
        s.close();
      }
    });
    srv.listen(this.port, () => {
      console.log(`Start socket port = ${this.port}`);
      console.log('Start server Success !');
      this.startSettingsWatcher();
    });
    this.server = srv;
    return srv;
  }

  stop() {
    this.start = false;
    clearInterval(this._watcher);
    this.server?.close();
  }
}

export const serverManager = new ServerManager();
export default serverManager;
