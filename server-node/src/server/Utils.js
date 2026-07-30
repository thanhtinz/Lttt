/**
 * Port của avatar/server/Utils.java
 * Giữ nguyên tên phương thức, thứ tự đọc/ghi và SQL.
 */
import fs from 'fs';
import crypto from 'crypto';

import { dbManager } from '../db/DbManager.js';
import { DataInputStream } from '../net/JavaIO.js';

/** Thư mục log — giống hằng LOG_DIRECTORY của Java. */
const LOG_DIRECTORY = 'logs/';

/**
 * Bản Java ghi ra đường dẫn Windows cố định (C:\Users\...). Ở Node cho phép
 * cấu hình qua 2 hằng dưới đây; nếu ghi lỗi thì chỉ log ra như Java (không crash).
 */
const FARM_IMG_DATA_FILE = process.env.FARM_IMG_DATA_FILE
  || 'C:\\Users\\Administrator\\IdeaProjects\\Avatar-Sv-master\\img_farm_data.txt';
const ITEMS_QUERY_FILE = process.env.ITEMS_QUERY_FILE
  || 'C:\\Users\\Administrator\\Desktop\\a\\itemsquery1.txt';

/** Tương đương Avatar.getFile(): đọc cả file thành byte[], lỗi -> null. */
function getFile(url) {
  try {
    return fs.readFileSync(url);
  } catch (e) {
    return null;
  }
}

/** Định dạng "yyyy-MM-dd HH:mm:ss" giống SimpleDateFormat (giờ địa phương). */
function formatDateTime(date) {
  const p2 = (n) => String(n).padStart(2, '0');
  return date.getFullYear() + '-' + p2(date.getMonth() + 1) + '-' + p2(date.getDate())
    + ' ' + p2(date.getHours()) + ':' + p2(date.getMinutes()) + ':' + p2(date.getSeconds());
}

export class Utils {

  static writeLogAddChest(user, message) {
    const username = user.getUsername();
    const logFilePath = LOG_DIRECTORY + username + '_addchest.txt'; // Tên file log theo username
    try {
      const line = formatDateTime(new Date()) + ' - ' + message + ' ' + username + '\n';
      fs.appendFileSync(logFilePath, line);
    } catch (e) {
      console.error(e);
    }
  }

  static writeLog(user, message) {
    const username = user.getUsername();
    const logFilePath = LOG_DIRECTORY + username + '_log.txt'; // Tên file log theo username
    try {
      const line = formatDateTime(new Date()) + ' - ' + message + '\n';
      fs.appendFileSync(logFilePath, line);
    } catch (e) {
      console.error(e);
    }
  }

  static writeLogSystem(user, message) {
    const username = user.getUsername();
    const logFilePath = LOG_DIRECTORY + 'System.txt'; // log file hệ thống
    try {
      const line = formatDateTime(new Date()) + ' - ' + message + ' ' + username + '\n';
      fs.appendFileSync(logFilePath, line);
    } catch (e) {
      console.error(e);
    }
  }

  static writeLogKhoaAcc(user, message) {
    const username = user.getUsername();
    const logFilePath = LOG_DIRECTORY + 'KhoaAcc.txt';
    try {
      const line = formatDateTime(new Date()) + ' - ' + message + ' ' + username + '\n';
      fs.appendFileSync(logFilePath, line);
    } catch (e) {
      console.error(e);
    }
  }

  static writeLogCaMap(user, message) {
    const username = user.getUsername();
    const logFilePath = LOG_DIRECTORY + 'caMap.txt';
    try {
      const line = formatDateTime(new Date()) + ' - ' + message + ' ' + username + '\n';
      fs.appendFileSync(logFilePath, line);
    } catch (e) {
      console.error(e);
    }
  }

  static distanceBetween(x1, y1, x2, y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  }

  static sin(arg) {
    if ((arg = Utils.toArg0_360(arg)) >= 0 && arg < 90) {
      return Utils.sinData[arg];
    }
    if (arg >= 90 && arg < 180) {
      return Utils.sinData[180 - arg];
    }
    if (arg >= 180 && arg < 270) {
      return -Utils.sinData[arg - 180];
    }
    return -Utils.sinData[360 - arg];
  }

  static cos(arg) {
    if ((arg = Utils.toArg0_360(arg)) >= 0 && arg < 90) {
      return Utils.cosData[arg];
    }
    if (arg >= 90 && arg < 180) {
      return -Utils.cosData[180 - arg];
    }
    if (arg >= 180 && arg < 270) {
      return -Utils.cosData[arg - 180];
    }
    return Utils.cosData[360 - arg];
  }

  static getArg(cos, sin) {
    if (cos === 0) {
      return (sin === 0) ? 0 : ((sin < 0) ? 270 : 90);
    }
    let arg = Math.abs(Math.trunc((sin << 10) / cos));
    for (;;) {
      for (let i = 0; i <= 90; ++i) {
        if (Utils.tanData[i] >= arg) {
          arg = i;
          if (sin >= 0 && cos < 0) {
            arg = 180 - arg;
          }
          if (sin < 0 && cos < 0) {
            arg += 180;
          }
          if (sin < 0 && cos >= 0) {
            arg = 360 - arg;
          }
          return arg;
        }
      }
      arg = 0;
    }
  }

  static toArg0_360(arg) {
    if (arg >= 360) {
      arg -= 360;
    }
    if (arg < 0) {
      arg += 360;
    }
    return arg;
  }

  static getSqrt(num) {
    if (num <= 0) {
      return 0;
    }
    let newS = Math.trunc((num + 1) / 2);
    let oddS;
    do {
      oddS = newS;
      newS = Math.trunc(newS / 2) + Math.trunc(num / (newS * 2));
    } while (Math.abs(oddS - newS) > 1);
    return newS;
  }

  /**
   * Gộp 3 overload của Java:
   *  nextInt(max), nextInt(from, to), nextInt(int[] percen)
   */
  static nextInt(a, b) {
    if (Array.isArray(a)) {
      const percen = a;
      let next = Utils.nextInt(1000);
      let i;
      for (i = 0; i < percen.length; ++i) {
        if (next < percen[i]) {
          return i;
        }
        next -= percen[i];
      }
      return i;
    }
    if (b === undefined) {
      return Math.floor(Math.random() * a);
    }
    return a + Math.floor(Math.random() * (b - a));
  }

  static tryParse(text) {
    const v = Number.parseInt(text, 10);
    // Java Integer.parseInt chỉ nhận số nguyên hoàn chỉnh
    if (Number.isNaN(v) || !/^[+-]?\d+$/.test(String(text).trim())) {
      return null;
    }
    return v | 0;
  }

  static getRandomInArray(array) {
    const rnd = Math.floor(Math.random() * array.length);
    return array[rnd];
  }

  /** Parse "yyyy-MM-dd HH:mm:ss" -> Date, lỗi -> null. */
  static getDate(dateString) {
    try {
      const m = /^(\d{4})-(\d{1,2})-(\d{1,2})[ T](\d{1,2}):(\d{1,2}):(\d{1,2})/.exec(String(dateString));
      if (m == null) {
        throw new Error('Unparseable date: ' + dateString);
      }
      return new Date(+m[1], +m[2] - 1, +m[3], +m[4], +m[5], +m[6]);
    } catch (e) {
      console.error(e);
      return null;
    }
  }

  static setTimeout(runnable, delay) {
    globalThis.setTimeout(() => {
      try {
        runnable();
      } catch (e) {
        console.error(e);
      }
    }, delay);
  }

  static toDateString(date) {
    return formatDateTime(date);
  }

  static addNumDay(dat, nDays) {
    dat.setTime(dat.getTime() + nDays * 86400000);
  }

  static getNumDay(from, to) {
    return Math.trunc(Math.trunc((to.getTime() - from.getTime()) / 1000) / 86400);
  }

  static isNewDay(from, to) {
    const dateFrom = from.getDate(); // ngày trong tháng (Java Date.getDate())
    const dateTo = to.getDate();
    return dateFrom !== dateTo;
  }

  static addNumHour(dat, nHours) {
    dat.setTime(dat.getTime() + nHours * 3600000);
  }

  static getNumHour(from, to) {
    return Math.trunc(Math.trunc((to.getTime() - from.getTime()) / 1000) / 3600);
  }

  static getStringNumber(num) {
    if (num >= 1000000000) {
      return Math.trunc(num / 1000000000) + 't\u1ef7';
    }
    if (num >= 1000000) {
      return Math.trunc(num / 1000000) + 'tr';
    }
    if (num >= 10000) {
      return Math.trunc(num / 1000) + 'k';
    }
    return String(num);
  }

  static getShort(ab, off) {
    return ((((ab[off] & 0xFF) << 8 | (ab[off + 1] & 0xFF)) << 16) >> 16);
  }

  static inRegion(x, y, x0, y0, w, h) {
    return x >= x0 && x < x0 + w && y >= y0 && y < y0 + h;
  }

  static intersecRegions(x1, y1, w1, h1, x2, y2, w2, h2) {
    return x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2;
  }

  static isNotAlpha(rgb) {
    return (rgb >> 24) !== 0;
  }

  static getTeamPoint(TongDD, nteam) {
    if (nteam === 1) {
      return 0;
    }
    return Math.trunc((TongDD - 100) / 100) + Math.trunc((TongDD - 100) * nteam / 1000);
  }

  static tinhRank(top, profile) {
    if (top > 0) {
      if (top <= 20) {
        return 'Th\u00e1ch \u0111\u1ea5u';
      }
      if (top <= 40) {
        return 'Cao Th\u1ee7';
      }
      if (top <= 60) {
        return 'Kim C\u01b0\u01a1ng' + Utils.tinhRankphu(top, 140);
      }
      if (top <= 80) {
        return 'B\u1ea1ch Kim' + Utils.tinhRankphu(top, 140);
      }
      if (top <= 100) {
        return 'V\u00e0ng' + Utils.tinhRankphu(top, 140);
      }
      if (top <= 120) {
        return 'B\u1ea1c' + Utils.tinhRankphu(top, 140);
      }
      if (top <= 140) {
        return '\u0110\u1ed3ng' + Utils.tinhRankphu(top, 140);
      }
    }
    if (profile) {
      return 'Ch\u01b0a c\u00f3 h\u1ea1ng';
    }
    return null;
  }

  static tinhRankIcon(top) {
    if (top > 0) {
      if (top <= 20) {
        return 1006;
      }
      if (top <= 40) {
        return 1005;
      }
      if (top <= 60) {
        return 1004;
      }
      if (top <= 80) {
        return 1003;
      }
      if (top <= 100) {
        return 1002;
      }
      if (top <= 120) {
        return 1001;
      }
      if (top <= 140) {
        return 1000;
      }
    }
    return 0;
  }

  // NOTE: giữ nguyên hành vi bản Java — trả về null nếu capRank ngoài 1..5
  // (chuỗi ghép sẽ ra "...null" đúng như Java).
  static tinhRankphu(top, i) {
    let capRankS = null;
    const capRank = Math.trunc((top - i) / 4);
    if (capRank === 1) {
      capRankS = 'I';
    } else if (capRank === 2) {
      capRankS = 'II';
    } else if (capRank === 3) {
      capRankS = 'III';
    } else if (capRank === 4) {
      capRankS = 'IV';
    } else if (capRank === 5) {
      capRankS = 'V';
    }
    return capRankS;
  }

  static md5(str) {
    let result = '';
    try {
      const hex = crypto.createHash('md5').update(Buffer.from(str, 'utf8')).digest('hex');
      // Java: String.format("%32s", bi.toString(16)).replace(' ', '0')
      result = hex.replace(/^0+/, '').padStart(32, '0');
    } catch (e) {
      console.error(e);
    }
    return result;
  }

  static validateUserName(userName) {
    return Utils.userNamePattern.test(String(userName));
  }

  /**
   * Gộp 2 overload removeAccent(char) / removeAccent(String):
   * bỏ dấu từng ký tự trong chuỗi (chuỗi 1 ký tự = overload char).
   */
  static removeAccent(str) {
    let out = '';
    const s = String(str);
    for (let i = 0; i < s.length; ++i) {
      const ch = s.charCodeAt(i);
      const index = binarySearch(Utils.SOURCE_CHARACTERS, ch);
      out += index >= 0 ? String.fromCharCode(Utils.DESTINATION_CHARACTERS[index]) : s.charAt(i);
    }
    return out;
  }

  static async main(args) {
    await dbManager.start();
    //decodeItemFile();
  }

  static readTreeInfo() {
    const dat = getFile('res/data/farm_info.dat');
    try {
      const dataInputStream = new DataInputStream(dat);
      const numTrees = dataInputStream.readShort();
      console.log('Number of Trees: ' + numTrees);
      for (let i = 0; i < numTrees; i++) {
        const treeID = dataInputStream.readByte();
        const treeName = dataInputStream.readUTF();
        const treeNameLower = treeName.toLowerCase();
        const phase0 = dataInputStream.readByte();
        const phase1 = dataInputStream.readByte();
        const harvestTime = dataInputStream.readShort();
        const dieTime = dataInputStream.readShort();
        const priceSeed0 = dataInputStream.readShort();
        const priceProduct = dataInputStream.readShort();
        const numProduct = dataInputStream.readShort();

        console.log('Tree ' + (i + 1) + ':');
        console.log('  ID: ' + treeID);
        console.log('  Name: ' + treeName);
        console.log('  Phases: ' + phase0 + ', ' + phase1);
        console.log('  Harvest Time: ' + harvestTime);
        console.log('  Die Time: ' + dieTime);
        console.log('  Price Seed[0]: ' + priceSeed0);
        console.log('  Price Product: ' + priceProduct);
        console.log('  Num Product: ' + numProduct);

        for (let j = 0; j < 8; j++) {
          const idImg = dataInputStream.readShort();
          console.log('  Image ID[' + j + ']: ' + idImg);
        }
      }

      const numItems = dataInputStream.readShort();
      console.log('Number of Items: ' + numItems);
      for (let k = 0; k < numItems; k++) {
        const itemID = dataInputStream.readByte();
        const price0 = dataInputStream.readShort();

        console.log('Item ' + (k + 1) + ':');
        console.log('  ID: ' + itemID);
        console.log('  Price[0]: ' + price0);
      }

      for (let l = 0; l < numTrees; l++) {
        const priceSeed1 = dataInputStream.readShort();
        console.log('Tree ' + (l + 1) + ' - Price Seed[1]: ' + priceSeed1);
      }

      for (let m = 0; m < numItems; m++) {
        const price1 = dataInputStream.readShort();
        console.log('Item ' + (m + 1) + ' - Price[1]: ' + price1);
      }

      const numAnimals = dataInputStream.readShort();
      console.log('Number of Animals: ' + numAnimals);
      for (let n = 0; n < numAnimals; n++) {
        const species = dataInputStream.readByte();
        const name = dataInputStream.readUTF();
        const description = dataInputStream.readUTF();
        const price0 = dataInputStream.readInt();
        const price1 = dataInputStream.readShort();
        const harvestTime = dataInputStream.readShort();
        const priceProduct = dataInputStream.readShort();

        console.log('Animal ' + (n + 1) + ':');
        console.log('  Species: ' + species);
        console.log('  Name: ' + name);
        console.log('  Description: ' + description);
        console.log('  Price[0]: ' + price0);
        console.log('  Price[1]: ' + price1);
        console.log('  Harvest Time: ' + harvestTime);
        console.log('  Price Product: ' + priceProduct);

        for (let num4 = 0; num4 < 3; num4++) {
          const idImg = dataInputStream.readShort();
          console.log('  Image ID[' + num4 + ']: ' + idImg);
        }
      }

      const farmItemCount = dataInputStream.readByte();
      console.log('Number of Farm Items: ' + farmItemCount);
      for (let num7 = 0; num7 < farmItemCount; num7++) {
        const farmItemID = dataInputStream.readShort();
        const farmItemImgID = dataInputStream.readShort();
        const type = dataInputStream.readByte();
        const action = dataInputStream.readByte();
        const description = dataInputStream.readUTF();
        const priceXu = dataInputStream.readShort();
        const priceLuong = dataInputStream.readShort();

        console.log('Farm Item ' + (num7 + 1) + ':');
        console.log('  ID: ' + farmItemID);
        console.log('  Image ID: ' + farmItemImgID);
        console.log('  Type: ' + type);
        console.log('  Action: ' + action);
        console.log('  Description: ' + description);
        console.log('  Price Xu: ' + priceXu);
        console.log('  Price Luong: ' + priceLuong);
      }
    } catch (e) {
      console.error('Error reading data: ' + e.message);
    }
  }

  static async decodeFoodFile() {
    try {
      const dat = getFile('res/data/food.dat');
      const dis = new DataInputStream(dat);
      const numItem = dis.readShort();
      console.log('Num item ' + numItem);
      for (let n = 0; n < numItem; ++n) {
        const itemID = dis.readShort();
        const name = dis.readUTF();
        const desc = dis.readUTF();
        const price = dis.readInt();
        const shop = dis.readByte();
        const imgId = dis.readShort();
        await dbManager.executeUpdate(
          'INSERT INTO `foods` (`id`, `name`, `description`, `price`, `shop`, `img`) VALUES (?, ?, ?, ?, ?, ?);',
          [itemID, name, desc, price, shop, imgId]);
      }
    } catch (ex) {
      // Java bỏ qua IOException
    }
  }

  static async decodeItemFile() {
    try {
      const dat = getFile('res/data/item.dat');
      const dis = new DataInputStream(dat);
      const numItem = dis.readShort();
      console.log('Num item ' + numItem);
      for (let n = 0; n < numItem; ++n) {
        const itemID = dis.readShort();
        const bigID = dis.readShort();
        const x0 = dis.readUnsignedByte();
        const y0 = dis.readUnsignedByte();
        const w = dis.readByte();
        const h = dis.readByte();
        await dbManager.executeUpdate(
          'INSERT INTO `item_image_data`(`id`, `image_id`, `x`, `y`, `w`, `h`) VALUES (?,?,?,?,?,?)',
          [itemID, bigID, x0, y0, w, h]);
      }
    } catch (ex) {
      // Java bỏ qua IOException
    }
  }

  static async decodeMapItemFile() {
    try {
      const dat = getFile('res/data/map_item.dat');
      const dis = new DataInputStream(dat);
      const numItem = dis.readShort();
      console.log('Num item ' + numItem);
      for (let n = 0; n < numItem; ++n) {
        const id = dis.readShort();
        const typeID = dis.readShort();
        const type = dis.readByte();
        const x = dis.readByte();
        const y = dis.readByte();
        await dbManager.executeUpdate(
          'INSERT INTO `map_item`(`id`, `type_id`, `type`, `x`, `y`) VALUES (?,?,?,?,?)',
          [id, typeID, type, x, y]);
      }
    } catch (ex) {
      // Java bỏ qua IOException
    }
  }

  static async decodeMapItemTypeFile() {
    try {
      const dat = getFile('res/data/map_item_type.dat');
      const dis = new DataInputStream(dat);
      const numItem = dis.readShort();
      console.log('Num item ' + numItem);
      for (let n = 0; n < numItem; ++n) {
        const id = dis.readShort();
        const name = dis.readUTF();
        const des = dis.readUTF();
        const imgID = dis.readShort();
        const iconID = dis.readShort();
        const dx = dis.readByte();
        const dy = dis.readByte();
        const price_coin = dis.readShort();
        const price_gold = dis.readShort();
        const buy = dis.readByte();
        const pn = dis.readByte();
        const position = [];
        for (let i = 0; i < pn; i++) {
          const obj = {};
          const x = dis.readByte();
          const y = dis.readByte();
          obj.x = x;
          obj.y = y;
          position.push(obj);
        }
        await dbManager.executeUpdate(
          'INSERT INTO `map_item_type`(`id`, `name`, `description`, `image`, `icon`, `price_coin`, `price_gold`, `buy`, `dx`, `dy`, `position`) VALUES (?,?,?,?,?,?,?,?,?,?,?)',
          [id, name, des, imgID, iconID, price_coin, price_gold, buy, dx, dy, JSON.stringify(position)]);
      }
    } catch (ex) {
      // Java bỏ qua IOException
    }
  }

  static async decodeItemFarmFile() { // farm IMG Data
    try {
      const dat = getFile('res/data/map_item.dat');
      const dis = new DataInputStream(dat);
      const numItem = dis.readShort();
      console.log('Num item farm ' + numItem);
      for (let n = 0; n < numItem; ++n) {
        const itemID = dis.readShort();
        const bigID = dis.readShort();
        const x0 = dis.readUnsignedByte();
        const y0 = dis.readUnsignedByte();
        const w = dis.readByte();
        const h = dis.readByte();
        // Convert JSON array to string and escape single quotes
        const query = 'INSERT INTO `farm_image_data`(`id`, `image_id`, `x`, `y`,`w`, `h`) VALUES ('
          + itemID + ', ' + bigID + ', ' + x0 + ', ' + y0 + ", '" + w + "', "
          + "'" + h + "'" + ');\n';
        try {
          // Write the query to the specified file path
          fs.appendFileSync(FARM_IMG_DATA_FILE, query);
          console.log('File written successfully.');
        } catch (e) {
          console.error('Error writing to file: ' + e.message);
        }
        await dbManager.executeUpdate(
          'INSERT INTO `farm_image_data`(`id`, `image_id`, `x`, `y`, `w`, `h`) VALUES (?,?,?,?,?,?)',
          [itemID, bigID, x0, y0, w, h]);
      }
    } catch (ex) {
      // Java bỏ qua IOException
    }
  }

  static async decodeItemDataFile(dat, isSimple) {
    try {
      const dis = new DataInputStream(dat);
      let numItem;
      if (!isSimple) {
        numItem = dis.readShort();
      } else {
        numItem = 1;
      }
      console.log('S\u1ed1 l\u01b0\u1ee3ng item: ' + numItem);
      for (let i = 0; i < numItem; ++i) {
        let itemName = '';
        const itemID = dis.readShort();
        const itemXu = dis.readInt();
        const itemLuong = dis.readShort();
        console.log('Item: ' + itemID);
        const itemType = dis.readShort();
        if (itemType === -2) {
          itemName = dis.readUTF();
          const sell = dis.readByte();
          const idIcon = dis.readShort();
          const INSERT_ITEM = 'INSERT INTO `items` (`id`, `coin`, `gold`, `type`, `icon`, `name`, `sell`) VALUES (?, ?, ?, ?, ?, ?, ?)';
          await dbManager.executeUpdate(INSERT_ITEM,
            [itemID, itemXu, itemLuong, itemType, idIcon, itemName, sell]);
        } else if (itemType === -1) {
          itemName = dis.readUTF();
          const sell = dis.readByte();
          const zOrder = dis.readByte();
          const gender = dis.readByte();
          const lvRequire = dis.readByte();
          const idIcon2 = dis.readShort();
          const animation = [];
          for (let j = 0; j < 15; ++j) {
            const animation_i = {};
            const imgID = dis.readShort();
            const dx = dis.readByte();
            const dy = dis.readByte();
            animation_i.img = imgID;
            animation_i.dx = dx;
            animation_i.dy = dy;
            animation.push(animation_i);
          }
          itemName = itemName.split("'").join("\\'");

          // Convert JSON array to string and escape single quotes
          const animations = JSON.stringify(animation).split("'").join("\\'");
          const query = 'INSERT INTO `items`(`id`, `coin`, `gold`, `type`,`icon`, `name`, `sell`, `expired_day`, `zorder`, `gender`, `level`, `animation`) VALUES ('
            + itemID + ', ' + itemXu + ', ' + itemLuong + ', ' + itemType + ", '" + idIcon2 + "', "
            + "'" + itemName + "'" + ', ' + sell + ", '" + 0 + "'," + zOrder + ', ' + gender + ', ' + lvRequire + ', '
            + "'" + animations + "'" + ');\n';
          try {
            // Write the query to the specified file path
            fs.appendFileSync(ITEMS_QUERY_FILE, query);
            console.log('File written successfully.');
          } catch (e) {
            console.error('Error writing to file: ' + e.message);
          }
          const INSERT_ITEM2 = 'INSERT INTO `items` (`id`, `coin`, `gold`, `type`, `icon`, `name`, `sell`, `zorder`, `gender`, `level`, `animation`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)';
          await dbManager.executeUpdate(INSERT_ITEM2,
            [itemID, itemXu, itemLuong, itemType, idIcon2, itemName, sell, zOrder, gender, lvRequire,
              JSON.stringify(animation)]);
        } else {
          const color = dis.readShort();
          const INSERT_ITEM3 = 'INSERT INTO `items` (`id`, `coin`, `gold`, `type`, `icon`) VALUES (?, ?, ?, ?, ?)';
          await dbManager.executeUpdate(INSERT_ITEM3,
            [itemID, itemXu, itemLuong, itemType, color]);
        }
      }
    } catch (ex) {
      // Java bỏ qua Exception (chỉ in thông tin lỗi SQL)
      if (ex && ex.code) {
        console.log('SQLException occured. getErrorCode=> ' + ex.errno);
        console.log('SQLException occured. getCause=> ' + ex.sqlState);
        console.log('SQLException occured. getMessage=> ' + ex.message);
      }
    }
  }
}

/** Arrays.binarySearch trên mảng char đã sắp xếp. */
function binarySearch(arr, key) {
  let low = 0;
  let high = arr.length - 1;
  while (low <= high) {
    const mid = (low + high) >>> 1;
    if (arr[mid] < key) low = mid + 1;
    else if (arr[mid] > key) high = mid - 1;
    else return mid;
  }
  return -(low + 1);
}

// ==== static block của Java ====
Utils.userNamePattern = /^[a-z0-9]{5,16}$/;
Utils.sinData = [0, 18, 36, 54, 71, 89, 107, 125, 143, 160, 178, 195, 213, 230, 248, 265, 282, 299, 316,
  333, 350, 367, 384, 400, 416, 433, 449, 465, 481, 496, 512, 527, 543, 558, 573, 587, 602, 616, 630, 644,
  658, 672, 685, 698, 711, 724, 737, 749, 761, 773, 784, 796, 807, 818, 828, 839, 849, 859, 868, 878, 887,
  896, 904, 912, 920, 928, 935, 943, 949, 956, 962, 968, 974, 979, 984, 989, 994, 998, 1002, 1005, 1008,
  1011, 1014, 1016, 1018, 1020, 1022, 1023, 1023, 1024, 1024];
Utils.cosData = new Array(91).fill(0);
Utils.tanData = new Array(91).fill(0);
for (let i = 0; i <= 90; ++i) {
  Utils.cosData[i] = Utils.sinData[90 - i];
  if (Utils.cosData[i] === 0) {
    Utils.tanData[i] = 2147483647; // Integer.MAX_VALUE
  } else {
    Utils.tanData[i] = Math.trunc((Utils.sinData[i] << 10) / Utils.cosData[i]);
  }
}
Utils.SOURCE_CHARACTERS = ['\u00c0', '\u00c1', '\u00c2', '\u00c3', '\u00c8', '\u00c9', '\u00ca', '\u00cc',
  '\u00cd', '\u00d2', '\u00d3', '\u00d4', '\u00d5', '\u00d9', '\u00da', '\u00dd', '\u00e0', '\u00e1',
  '\u00e2', '\u00e3', '\u00e8', '\u00e9', '\u00ea', '\u00ec', '\u00ed', '\u00f2', '\u00f3', '\u00f4',
  '\u00f5', '\u00f9', '\u00fa', '\u00fd', '\u0102', '\u0103', '\u0110', '\u0111', '\u0128', '\u0129',
  '\u0168', '\u0169', '\u01a0', '\u01a1', '\u01af', '\u01b0', '\u1ea0', '\u1ea1', '\u1ea2', '\u1ea3',
  '\u1ea4', '\u1ea5', '\u1ea6', '\u1ea7', '\u1ea8', '\u1ea9', '\u1eaa', '\u1eab', '\u1eac', '\u1ead',
  '\u1eae', '\u1eaf', '\u1eb0', '\u1eb1', '\u1eb2', '\u1eb3', '\u1eb4', '\u1eb5', '\u1eb6', '\u1eb7',
  '\u1eb8', '\u1eb9', '\u1eba', '\u1ebb', '\u1ebc', '\u1ebd', '\u1ebe', '\u1ebf', '\u1ec0', '\u1ec1',
  '\u1ec2', '\u1ec3', '\u1ec4', '\u1ec5', '\u1ec6', '\u1ec7', '\u1ec8', '\u1ec9', '\u1eca', '\u1ecb',
  '\u1ecc', '\u1ecd', '\u1ece', '\u1ecf', '\u1ed0', '\u1ed1', '\u1ed2', '\u1ed3', '\u1ed4', '\u1ed5',
  '\u1ed6', '\u1ed7', '\u1ed8', '\u1ed9', '\u1eda', '\u1edb', '\u1edc', '\u1edd', '\u1ede', '\u1edf',
  '\u1ee0', '\u1ee1', '\u1ee2', '\u1ee3', '\u1ee4', '\u1ee5', '\u1ee6', '\u1ee7', '\u1ee8', '\u1ee9',
  '\u1eea', '\u1eeb', '\u1eec', '\u1eed', '\u1eee', '\u1eef', '\u1ef0', '\u1ef1'].map((c) => c.charCodeAt(0));
Utils.DESTINATION_CHARACTERS = ['A', 'A', 'A', 'A', 'E', 'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U',
  'Y', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A', 'a', 'D', 'd',
  'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
  'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
  'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
  'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
  'U', 'u', 'U', 'u', 'U', 'u'].map((c) => c.charCodeAt(0));

export default Utils;
