/** Port của avatar/service/Service.java */
import { Cmd } from '../constants/Cmd.js';
import { Message } from '../net/Message.js';
import { dbManager } from '../db/DbManager.js';
import { User } from '../model/User.js';
import { userManager } from '../server/UserManager.js';
import { Utils } from '../server/Utils.js';

export class Service {

  constructor(cl) {
    this.session = cl;
  }

  removeItem(userID, itemID) {
    try {
      const ms = new Message(Cmd.REMOVE_ITEM);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeShort(itemID);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('removeItem() ', ex);
    }
  }

  serverDialog(message) {
    try {
      const ms = new Message(Cmd.SET_MONEY_ERROR);
      const ds = ms.writer();
      ds.writeUTF(message);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  sendTextBoxPopup(userId, menuId, message, type) {
    try {
      const ms = new Message(Cmd.TEXT_BOX);
      const ds = ms.writer();
      ds.writeInt(userId);
      ds.writeByte(menuId);
      ds.writeUTF(message);
      ds.writeByte(type);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error(e);
    }
  }

  serverMessage(message) {
    try {
      const ms = new Message(Cmd.SERVER_MESSAGE);
      const ds = ms.writer();
      ds.writeUTF(message);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('serverMessage ', e);
    }
  }

  serverInfo(message) {
    try {
      const ms = new Message(Cmd.SERVER_INFO);
      const ds = ms.writer();
      ds.writeUTF(message);
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('serverMessage ', e);
    }
  }

  weather(weather) {
    try {
      console.log('weather: ' + weather);
      const ms = new Message(Cmd.WEATHER);
      const ds = ms.writer();
      ds.writeByte(weather);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('weather() ', ex);
    }
  }

  async getTop10PlayersByXuFromBoss() {
    const topPlayers = [];
    const sql = 'SELECT u.username, p.xu_from_boss '
      + 'FROM players p '
      + 'JOIN users u ON p.user_id = u.id '
      + 'ORDER BY p.xu_from_boss DESC '
      + 'LIMIT 10';
    try {
      const rows = await dbManager.query(sql, []);
      for (const rs of rows) {
        const username = rs.username;
        const xuFromBoss = rs.xu_from_boss | 0;

        // In ra giá trị đọc từ ResultSet để kiểm tra
        console.log('Username: ' + username + ', Xu From Boss: ' + xuFromBoss);

        const player = new User(username, xuFromBoss);
        topPlayers.push(player);
      }
    } catch (e) {
      console.error(e); // Xử lý ngoại lệ khi truy vấn thất bại
    }
    return topPlayers;
  }

  async getAllPlayersByxu_fromboss() {
    const allPlayers = [];
    const sql = 'SELECT u.username, p.xu_from_boss '
      + 'FROM players p '
      + 'JOIN users u ON p.user_id = u.id '
      + 'WHERE p.xu_from_boss > 0 ' // Chỉ lấy những người có TopPhaoLuong > 0
      + 'ORDER BY p.xu_from_boss DESC';
    try {
      const rows = await dbManager.query(sql, []);
      for (const rs of rows) {
        const username = rs.username;
        const xu_from_boss = rs.xu_from_boss | 0;

        const player = new User(username, xu_from_boss);
        allPlayers.push(player);
      }
    } catch (e) {
      console.error(e); // Xử lý ngoại lệ khi truy vấn thất bại
    }
    return allPlayers;
  }

  async getUserRankXuBoss(currentUser) {
    const allPlayers = await this.getAllPlayersByxu_fromboss();

    // Đảm bảo currentUser có giá trị TopPhaoLuong hợp lệ
    if (currentUser.getXu_from_boss() <= 0) {
      return -1; // Chỉ ra rằng người dùng không có mặt trong danh sách
    }

    let rank = 1;
    for (const player of allPlayers) {
      if (currentUser.getXu_from_boss() >= player.getXu_from_boss()) {
        return rank; // Trả về vị trí nếu giá trị của người dùng lớn hơn hoặc bằng
      }
      rank++;
    }

    return -1; // Nếu người dùng không có mặt trong danh sách
  }

  async getTopPhaoLuong() {
    const topPlayers = [];
    const sql = 'SELECT u.username, p.TopPhaoLuong '
      + 'FROM players p '
      + 'JOIN users u ON p.user_id = u.id '
      + 'ORDER BY p.TopPhaoLuong DESC '
      + 'LIMIT 10';
    try {
      const rows = await dbManager.query(sql, []);
      for (const rs of rows) {
        const username = rs.username;
        const TopPhaoLuong = rs.TopPhaoLuong | 0;

        // In ra giá trị đọc từ ResultSet để kiểm tra
        console.log('Username: ' + username + ', phao luong ' + TopPhaoLuong);

        const player = new User(username, 0, TopPhaoLuong);
        topPlayers.push(player);
      }
    } catch (e) {
      console.error(e); // Xử lý ngoại lệ khi truy vấn thất bại
    }
    return topPlayers;
  }

  async getAllPlayersByPhaoLuong() {
    const allPlayers = [];
    const sql = 'SELECT u.username, p.TopPhaoLuong '
      + 'FROM players p '
      + 'JOIN users u ON p.user_id = u.id '
      + 'WHERE p.TopPhaoLuong > 0 ' // Chỉ lấy những người có TopPhaoLuong > 0
      + 'ORDER BY p.TopPhaoLuong DESC';
    try {
      const rows = await dbManager.query(sql, []);
      for (const rs of rows) {
        const username = rs.username;
        const topPhaoLuong = rs.TopPhaoLuong | 0;

        // Tạo đối tượng User từ dữ liệu truy vấn và thêm vào danh sách
        const player = new User(username, 0, topPhaoLuong);
        allPlayers.push(player);
      }
    } catch (e) {
      console.error(e); // Xử lý ngoại lệ khi truy vấn thất bại
    }
    return allPlayers;
  }

  async getUserRankPhaoLuong(currentUser) {
    const allPlayers = await this.getAllPlayersByPhaoLuong();

    // Đảm bảo currentUser có giá trị TopPhaoLuong hợp lệ
    if (currentUser.getTopPhaoLuong() <= 0) {
      return -1; // Chỉ ra rằng người dùng không có mặt trong danh sách
    }

    let rank = 1;
    for (const player of allPlayers) {
      if (currentUser.getTopPhaoLuong() >= player.getTopPhaoLuong()) {
        return rank; // Trả về vị trí nếu giá trị của người dùng lớn hơn hoặc bằng
      }
      rank++;
    }

    return -1; // Nếu người dùng không có mặt trong danh sách
  }

  async getTopPhaoXu() {
    const topPlayers = [];
    const sql = 'SELECT u.username,u.id, p.TopPhaoXu '
      + 'FROM players p '
      + 'JOIN users u ON p.user_id = u.id '
      + 'ORDER BY p.TopPhaoXu DESC '
      + 'LIMIT 10';
    try {
      const rows = await dbManager.query(sql, []);
      for (const rs of rows) {
        const username = rs.username;
        const TopPhaoXu = rs.TopPhaoXu | 0;
        const userid = rs.id | 0;
        // In ra giá trị đọc từ ResultSet để kiểm tra
        console.log('Username: ' + username + ', phao xu ' + TopPhaoXu);

        const player = new User(username, userid, 0, TopPhaoXu);
        topPlayers.push(player);
      }
    } catch (e) {
      console.error(e); // Xử lý ngoại lệ khi truy vấn thất bại
    }
    return topPlayers;
  }

  async getAllPlayersByPhaoXu() {
    const allPlayers = [];
    const sql = 'SELECT u.username,u.id, p.TopPhaoXu '
      + 'FROM players p '
      + 'JOIN users u ON p.user_id = u.id '
      + 'WHERE p.TopPhaoXu > 0 ' // Chỉ lấy những người có TopPhaoLuong > 0
      + 'ORDER BY p.TopPhaoXu DESC';
    try {
      const rows = await dbManager.query(sql, []);
      for (const rs of rows) {
        const username = rs.username;
        const userid = rs.id | 0;
        const topPhaoLuong = rs.TopPhaoXu | 0;

        // Tạo đối tượng User từ dữ liệu truy vấn và thêm vào danh sách
        const player = new User(username, userid, 0, topPhaoLuong);
        allPlayers.push(player);
      }
    } catch (e) {
      console.error(e); // Xử lý ngoại lệ khi truy vấn thất bại
    }
    return allPlayers;
  }

  async getUserRankPhaoXu(currentUser) {
    const allPlayers = await this.getAllPlayersByPhaoXu();

    // Đảm bảo currentUser có giá trị TopPhaoLuong hợp lệ
    if (currentUser.getTopPhaoXu() <= 0) {
      return -1; // Chỉ ra rằng người dùng không có mặt trong danh sách
    }

    let rank = 1;
    for (const player of allPlayers) {
      if (currentUser.getTopPhaoXu() >= player.getTopPhaoXu()) {
        return rank; // Trả về vị trí nếu giá trị của người dùng lớn hơn hoặc bằng
      }
      rank++;
    }

    return -1; // Nếu người dùng không có mặt trong danh sách
  }

  DuDoanNY(us) {
    const lstUs = userManager.users;
    let result = '';
    const gender = us.getGender();
    const randomIndex = Utils.nextInt(lstUs.length);
    const ulove = lstUs[randomIndex];
    const map = this.checkNameMap(ulove.getZone().getMap());
    if (gender === 1) {
      if (ulove.getGender() === gender) {
        result = ulove.getUsername() + ' (cú có gai) đang ở'
          + ' Map : ' + map + ' Khu :' + ulove.getZone().getId();
      } else {
        result = ulove.getUsername() + ' (girl) đang ở'
          + ' Map : ' + map + ' Khu :' + ulove.getZone().getId();
      }
    } else {
      if (ulove.getGender() === gender) {
        result = ulove.getUsername() + '(Gái đó : v) đang ở'
          + ' Map : ' + map + ' Khu :' + ulove.getZone().getId();
      } else {
        result = ulove.getUsername() + ' (boy nè) đang ở'
          + ' Map : ' + map + ' Khu :' + ulove.getZone().getId();
      }
    }
    return result;
  }

  checkNameMap(m) {
    const mapid = m.getId();
    let Map = '';
    switch (mapid) {
      case 0:
        Map = 'Khu Mặt trời';
        break;
      case 1:
        Map = 'Khu quay số cũ';
        break;
      case 2:
        Map = 'Khu đấu giá cũ';
        break;
      case 3:
        Map = 'Khu ăn xin trái';
        break;
      case 4:
        Map = 'Khu cưới , clan';
        break;
      case 5:
        Map = 'Khu ăn xin phải';
        break;
      case 6:
        Map = 'Khu cô giáo';
        break;
      case 7:
        Map = 'Khu dưới cô giáo';
        break;
      case 9:
        Map = 'Khu giải trí';
        break;
      case 10:
        Map = 'Khu lễ đường';
        break;
      case 11:
        Map = 'Bến xe công viên';
        break;
      case 13:
        Map = 'Khu sinh thái';
        break;
      case 14:
        Map = 'Khu câu cá rô';
        break;
      case 15:
        Map = 'Khu câu cá lóc';
        break;
      case 16:
        Map = 'Khu câu cá mập';
        break;
      case 17:
        Map = 'Khu ngoại ô';
        break;
      case 18:
        Map = 'Trong nhà tù';
        break;
      case 19:
        Map = 'Trong lễ đường';
        break;
      case 23:
        Map = 'Khu mua sắm';
        break;
    }
    return Map;
  }

  sendMessage(ms) {
    this.session.sendMessage(ms);
  }
}

export default Service;
