/**
 * Port của avatar/model/User.java — model người chơi trung tâm.
 *
 * Quy ước:
 *  - Giữ nguyên tên phương thức của Java (kể cả tên tiếng Việt / viết sai chính tả).
 *  - Lombok @Getter/@Setter được port thành getX()/setX() thật, field vẫn public.
 *  - Mọi hàm chạm DB là async (dbManager). `synchronized` bỏ đi (Node đơn luồng).
 *  - Số học đúng kiểu Java: chia nguyên bằng Math.trunc, ép byte/short bằng dịch bit.
 */
import { Item } from '../item/Item.js';
import { GiftBox } from '../lucky/GiftBox.js';
import { Command } from './Command.js';
import { NoService } from '../service/NoService.js';
import { EffectService } from '../service/EffectService.js';
import { LandItem } from '../farm/LandItem.js';
import { Animal } from '../farm/Animal.js';
import { HatGiong } from '../farm/HatGiong.js';
import { NongSan } from '../farm/NongSan.js';
import { NongSanDacBiet } from '../farm/NongSanDacBiet.js';
import { PhanBon } from '../farm/PhanBon.js';
import { GameString } from '../server/GameString.js';
import { Utils } from '../server/Utils.js';
import serverManager from '../server/ServerManager.js';
import userManager from '../server/UserManager.js';
import dbManager from '../db/DbManager.js';

/* ===== helper ép kiểu như Java ===== */
const toByte = (v) => ((v | 0) << 24) >> 24;
const toShort = (v) => ((v | 0) << 16) >> 16;
const toInt = (v) => v | 0;
const idiv = (a, b) => Math.trunc(a / b);

/** Tương đương org.json.simple.JSONValue.parse: lỗi thì trả về null. */
function JSONParse(text) {
  if (text == null) return null;
  try {
    return JSON.parse(text);
  } catch {
    return null;
  }
}

/**
 * Tương đương JSONArray.toJSONString() của json-simple: không khoảng trắng.
 * NOTE: json-simple dùng HashMap nên thứ tự khoá không xác định; ở đây dùng thứ tự
 * chèn (id, expired, quantity). Server đọc lại theo khoá nên không ảnh hưởng.
 */
function toJSONString(arr) {
  return JSON.stringify(arr);
}

/** ISO_LOCAL_DATE_TIME của Java: yyyy-MM-ddTHH:mm:ss(.SSS) */
function formatLocalDateTime(d) {
  const p = (n, w = 2) => String(n).padStart(w, '0');
  let s = `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
  const ms = d.getMilliseconds();
  if (ms !== 0) s += `.${p(ms, 3)}`;
  return s;
}

function parseLocalDateTime(s) {
  // "not_planted" và các chuỗi sai định dạng -> Invalid Date (Java sẽ ném lỗi parse)
  const d = new Date(s);
  return d;
}

export class User {
  static chestLevel = 0;
  static UPGRADE_COST_COINS = [0, 0, 0, 20000, 50000, 100000, 200000, 200000, 500000, 600000, 70000, 0, 1000000, 1200000, 1500000, 1700000, 2000000, 2500000, 2700000, 3000000, 4000000, 5000000];
  static UPGRADE_COST_GOLD = [0, 0, 0, 0, 0, 0, 0, 200, 500, 600, 700, 1000, 1000, 1200, 1500, 1700, 2000, 2500, 2700, 3000, 4000, 5000];

  /**
   * Gộp 4 constructor của Java theo số tham số:
   *  User(), User(username, xuFromBoss),
   *  User(username, xeng, TopPhaoLuong), User(username, id, xeng, TopPhaoXu)
   */
  constructor(...args) {
    this.AutoFish = false;

    this.bossMapId = 0;
    this.TopPhaoLuong = 0;
    this.TopPhaoXu = 0;

    this.xu_from_boss = 0;
    this.spamclickBoss = false;

    this.intSpanboss = 0;
    this.spam = 0;

    this.HP = 0;

    this.isDefeatedFlag = false; // field Java: isDefeated
    this.isSpamFlag = false;     // field Java: isSpam

    // ===== hẹn hò =====
    this.idUsHenHo = 0;
    this.namehh = null;
    this.wearingMarry = [];
    this.levelMarry = 0;
    this.PerLevelMarry = 0;
    this.imginfo = 0;
    this.tenNhan = null;
    // ==================

    this.storedXuUpdate = 0;
    this.session = null;
    this.id = 0;
    this.username = null;
    this.password = null;
    this.idFish = 0;
    this.gender = 0;
    this.xu = 0;
    this.luong = 0;
    this.luongKhoa = 0;

    this.availableSkills = [];
    this.useSkill = 0;
    this.dame = 0;
    this.dameToXu = 0;
    this.randomTimeInMillis = 0;
    this.lastTimeSet = 0;
    this.correctAnswer = 0;

    this.xeng = 0;
    this.clanID = 0;
    this.role = -1;
    this.star = 0;
    this.leverMain = 0;
    this.expMain = 0;
    this.leverFarm = 0;
    this.leverPercen = 0;
    this.expFarm = 0;
    this.friendly = 0;
    this.crazy = 0;
    this.stylish = 0;
    this.happy = 0;
    this.hunger = 0;
    this.chestSlot = 0;
    this.chestHomeSlot = 0;
    this.scores = 0;
    this.wearing = [];
    this.chests = [];
    this.chestsHome = null;

    // ===== farm =====
    this.landItems = [];
    this.Animal = [];
    this.hatgiong = [];
    this.NongSan = [];
    this.PhanBon = [];
    this.NongSanDacBiet = [];
    // ================

    this.zone = null;
    this.x = 0;
    this.y = 0;
    this.direct = 0;
    this.menus = null;
    this.dialLucky = null;
    this.idImg = -1;
    this.listCmd = [];
    this.listCmdRotate = [];

    this.loadDataFinish = false;
    this.bossShopItems = null;
    this.ShopEvent = null;

    this.boardIDs = [];
    this.roomID = 0;
    this.moneyPutList = [];
    this.isToXongFlag = false; // field Java: isToXong
    this.isHaPhomFlag = false; // field Java: isHaPhom

    if (args.length === 2) {
      this.username = args[0];
      this.xu_from_boss = toInt(args[1]);
    } else if (args.length === 3) {
      this.username = args[0];
      this.xeng = toInt(args[1]);
      this.TopPhaoLuong = toInt(args[2]);
    } else if (args.length === 4) {
      this.username = args[0];
      this.id = toInt(args[1]);
      this.xeng = toInt(args[2]);
      this.TopPhaoXu = toInt(args[3]);
    }
  }

  /* ======================= boss spam ======================= */

  getIntSpanboss() {
    return this.intSpanboss;
  }

  incrementIntSpanboss() {
    this.intSpanboss++;
  }

  resetIntSpanboss() {
    this.intSpanboss = 0;
  }

  // Phương thức để reset tất cả thông tin của người chơi
  resetUser() {
    this.resetIntSpanboss();
    this.setspamclickBoss(false);
  }

  getspamclickBoss() {
    return this.spamclickBoss;
  }

  setspamclickBoss(spamclickBoss) {
    this.spamclickBoss = spamclickBoss;
  }

  /* ======================= phỏm ======================= */

  isHaPhom() {
    return this.isHaPhomFlag;
  }

  setHaPhom(isHaPhom) {
    this.isHaPhomFlag = isHaPhom;
  }

  isToXong() {
    return this.isToXongFlag;
  }

  setToXong(isToXong) {
    this.isToXongFlag = isToXong;
  }

  getMoneyPutList() {
    return this.moneyPutList;
  }

  updateMoneyPutList(newMoneyPutList) {
    if (this.moneyPutList == null) {
      this.moneyPutList = []; // Khởi tạo danh sách nếu chưa có
    }
    this.moneyPutList.length = 0; // Xóa danh sách cũ (nếu cần)
    for (const b of newMoneyPutList) this.moneyPutList.push(b);
  }

  updateMoneyPutListByIndex(indexFrom, indexTo) {
    if (this.moneyPutList != null && this.moneyPutList.length > 0) {
      if (indexFrom >= 0 && indexFrom < this.moneyPutList.length
        && indexTo >= 0 && indexTo < this.moneyPutList.length) {
        const valueToMove = this.moneyPutList[indexFrom];
        this.moneyPutList.splice(indexFrom, 1);
        this.moneyPutList.splice(indexTo, 0, valueToMove);
      }
    }
  }

  /* ======================= skill / dame ======================= */

  setUseSkill(skill) {
    this.useSkill = toInt(skill);
  }

  getListSkill() {
    return this.availableSkills;
  }

  calculateDameToXu() {
    let totalDamage = 30;
    if (this.getStar() === 2) {
      totalDamage = 80;
    }
    const Item1 = [3440, 3443, 3174, 3972, 4442, 6142, 4121]; // Set mũ
    const Item2 = [3441, 3445, 3176, 3974, 4443, 4122];       // Set áo
    const Item3 = [3442, 3446, 3177, 3975, 4444, 4123];       // Set quần
    let countItem1 = 0, countItem2 = 0, countItem3 = 0;
    let cung = false, maybay = false, haoquanhoalong = false, bang = false, hophong = false;
    // Kiểm tra toàn bộ items nhân vật đang mặc
    for (const item of this.wearing) {
      totalDamage += item.getPart().getLevel();

      cung = cung || item.getId() === 6400;
      maybay = maybay || item.getId() === 4715;
      haoquanhoalong = haoquanhoalong || item.getId() === 5455;
      bang = bang || item.getId() === 6485;
      hophong = hophong || item.getId() === 5828;
      if (Item1.includes(item.getId())) countItem1++;
      if (Item2.includes(item.getId())) countItem2++;
      if (Item3.includes(item.getId())) countItem3++;
    }
    // Xử lý các kỹ năng tương ứng
    this.handleSkillSet(countItem1, countItem2, countItem3);
    this.handleSkill(cung, 2);            // Kỹ năng cung
    this.handleSkill(maybay, 4);          // Kỹ năng máy bay
    this.handleSkill(haoquanhoalong, 5);
    this.handleSkill(bang, 6);
    this.handleSkill(hophong, 7);

    this.dameToXu = toInt(totalDamage);
  }

  handleSkill(hasItem, skillId) {
    if (hasItem) {
      this.addSkill(skillId);
      this.useSkill = skillId;
    } else {
      this.removeSkill(skillId);
    }
  }

  handleSkillSet(countItem1, countItem2, countItem3) {
    if (countItem1 > 0 && countItem2 > 0 && countItem3 > 0) {
      this.addSkill(1);
      this.useSkill = 1;
    } else {
      this.removeSkill(1);
    }
  }

  addSkill(skillId) {
    if (!this.availableSkills.includes(skillId)) {
      this.availableSkills.push(skillId);
    }
  }

  removeSkill(skillId) {
    const i = this.availableSkills.indexOf(skillId);
    if (i >= 0) this.availableSkills.splice(i, 1);
    this.useSkill = 0; // Reset skill được sử dụng nếu xóa
  }

  /* ======================= service ======================= */

  getAvatarService() {
    return this.session.getAvatarService();
  }

  getFarmService() {
    return this.session.getFarmService();
  }

  getHomeService() {
    return this.session.getHomeService();
  }

  getParkService() {
    return this.session.getParkService();
  }

  getMapService() {
    if (this.zone == null) {
      return NoService.getInstance();
    }
    return this.zone.getService();
  }

  getService() {
    return this.session.getService();
  }

  sortWearing() {
    this.wearing.sort((o1, o2) => o1.getPart().getZOrder() - o2.getPart().getZOrder());
  }

  /* ======================= update tiền / chỉ số ======================= */

  setGender(gender) {
    this.gender = toByte(gender);
  }

  updateXu(xuUp) {
    this.xu += Number(xuUp);
  }

  updateXuKillBoss(dame) {
    this.storedXuUpdate += toInt(dame); // Lưu xu vào biến tạm thời
  }

  applyStoredXuUpdate() {
    // this.updateXu(storedXuUpdate * 5); // Cộng dồn số xu ba lần
    this.Updatexu_from_boss(this.storedXuUpdate);
    Utils.writeLog(this, 'xu : ' + this.storedXuUpdate + ' X ' + this.getDame() + ' dame to xu = >' + this.xu);
    this.storedXuUpdate = 0; // Reset xu đã lưu trữ
  }

  updateCrazy(crazy) {
    this.crazy = toShort(this.crazy + toInt(crazy));
  } // 1k item câu cá

  updateHappy(Happy) {
    this.happy = toByte(this.happy + toInt(Happy));
  }

  updateHunger(hunger) {
    this.hunger = toByte(this.hunger + toByte(hunger));
  } // 100 vp kill bos

  updateXP(XP) {
    this.expMain = toInt(this.expMain + toInt(XP));
  }

  Updatexu_from_boss(xu_from_boss) {
    this.xu_from_boss = toInt(this.xu_from_boss + toInt(xu_from_boss));
  }

  updateLuong(luongUp) {
    this.luong = toInt(this.luong + toInt(luongUp));
    try {
      this.getAvatarService().SendTabmsg('Luong : ' + this.luong);
      Utils.writeLog(this, 'luong : ' + this.luong);
    } catch (e) {
      throw e; // Java: throw new RuntimeException(e)
    }
  }

  updateScores(ScoresUp) {
    this.scores = toInt(this.scores + toInt(ScoresUp));
  }

  updateLuongKhoa(luongUp) {
    // NOTE: giữ nguyên hành vi bản Java (cộng vào `luong`, không phải `luongKhoa`)
    this.luong = toInt(this.luong + toInt(luongUp));
  }

  updateXeng(xengUp) {
    this.xeng = toInt(this.xeng + toInt(xengUp));
  }

  updateChestSlot(chestslot) {
    this.chestSlot = toByte(this.chestSlot + toByte(chestslot));
  }

  updateChest_homeSlot(chestslot) {
    this.chestHomeSlot = toByte(this.chestHomeSlot + toByte(chestslot));
  }

  async updateHP(dame, boss, us) {
    this.HP += Number(dame);
    if (this.HP <= 0) {
      this.HP = 0;
      if (!this.isDefeatedFlag) {
        this.isDefeatedFlag = true;
        // Chỉ thực hiện xử lý khi boss chưa bị đánh bại
        await boss.handleBossDefeat(boss, us);
      }
    }
  }

  async updateSpam(spams, boss, us) {
    boss.spam += Number(spams);
    console.log('Spam ' + boss.getSpam());
    if (boss.getSpam() <= 0) {
      boss.spam = 0;
      this.isSpamFlag = false;
      // NOTE: giữ nguyên hành vi bản Java (gán false rồi kiểm tra !isSpam nên luôn đúng)
      if (!this.isSpamFlag) {
        this.isSpamFlag = true;
        await boss.hanlderNhatHopQua(boss, us);
      }
    }
  }

  isSpam() {
    return this.isSpamFlag;
  }

  isDefeated() {
    return this.isDefeatedFlag;
  }

  getRoomID() {
    return this.roomID;
  }

  setRoomID(RoomID) {
    this.roomID = toByte(RoomID);
  }

  getRandomTimeInMillis() {
    return this.randomTimeInMillis;
  }

  setRandomTimeInMillis(randomTimeInMillis) {
    this.randomTimeInMillis = randomTimeInMillis;
  }

  getcorrectAnswer() {
    return this.correctAnswer;
  }

  setcorrectAnswer(sum) {
    this.correctAnswer = toInt(sum);
  }

  set(randomTimeInMillis) {
    this.randomTimeInMillis = randomTimeInMillis;
  }

  getLastTimeSet() {
    return this.lastTimeSet;
  }

  getChestLevel() {
    const chestSlotHome = this.getChestHomeSlot(); // Lấy số ô của rương hiện tại

    if (chestSlotHome <= 10) return 1;
    else if (chestSlotHome <= 15) return 2;
    else if (chestSlotHome <= 20) return 3;
    else if (chestSlotHome <= 25) return 4;
    else if (chestSlotHome <= 30) return 5;
    else if (chestSlotHome <= 35) return 6;
    else if (chestSlotHome <= 40) return 7;
    else if (chestSlotHome <= 45) return 8;
    else if (chestSlotHome <= 50) return 9;
    else if (chestSlotHome <= 55) return 10;

    return -1; // Trường hợp không hợp lệ
  }

  async updateTopPhaoLuong(luongThaPhao) {
    this.luong = toInt(this.luong + toInt(luongThaPhao));
    this.TopPhaoLuong += 1;
    await dbManager.executeUpdate('UPDATE `players` SET `TopPhaoLuong` = ? WHERE `user_id` = ? LIMIT 1;',
      [this.TopPhaoLuong, this.id]);
  }

  async updateTopPhaoXu(xuThaPhao) {
    this.xu += Number(xuThaPhao);
    this.TopPhaoXu += 1;
    await dbManager.executeUpdate('UPDATE `players` SET `TopPhaoXu` = ? WHERE `user_id` = ? LIMIT 1;',
      [this.TopPhaoXu, this.id]);
  }

  sendMessage(ms) {
    this.session.sendMessage(ms);
  }

  /* ======================= lưu / nạp dữ liệu ======================= */

  async saveData() {
    await dbManager.executeUpdate('UPDATE `players` SET `gender` = ?, `friendly` = ?, `crazy` = ?, `stylish` = ?, `happy` = ?, `hunger` = ?, `chest_slot` = ? , `chest_home_slot` = ? WHERE `user_id` = ? LIMIT 1;',
      [this.gender, this.friendly, this.crazy, this.stylish, this.happy, this.hunger, this.chestSlot, this.chestHomeSlot, this.id]);
    await dbManager.executeUpdate('UPDATE `players` SET `xu` = ?, `luong` = ?, `luong_khoa` = ?, `xeng` = ?, `level_main` = ?, `exp_main` = ?,`scores` = ? , `xu_from_boss` = ? , `TopPhaoLuong` = ?, `TopPhaoXu` = ? WHERE `user_id` = ? LIMIT 1;',
      [this.xu, this.luong, this.luongKhoa, this.xeng, this.leverMain, this.expMain, this.scores, this.xu_from_boss, this.TopPhaoLuong, this.TopPhaoXu, this.id]);
    const jChests = [];
    for (const item of this.chests) {
      const obj = {};
      obj.id = item.getId();
      obj.expired = item.getExpired();
      obj.quantity = item.getQuantity();
      this.checkItemQuantityLog(item, 'saveData error' + item.getPart().getName());
      jChests.push(obj);
    }
    const jWearing = [];
    for (const item of this.wearing) {
      const obj = {};
      obj.id = item.getId();
      obj.expired = item.getExpired();
      obj.quantity = item.getQuantity();
      jWearing.push(obj);
    }

    const jChestsHome = [];
    for (const item of this.chestsHome) {
      const obj = {};
      obj.id = item.getId();
      obj.expired = item.getExpired();
      obj.quantity = item.getQuantity();
      jChestsHome.push(obj);
    }

    await dbManager.executeUpdate('UPDATE `players` SET `chests` = ?, `wearing` = ?, `chests_home` = ? WHERE `user_id` = ? LIMIT 1;',
      [toJSONString(jChests), toJSONString(jWearing), toJSONString(jChestsHome), this.id]);
    console.log('Save data user ' + this.getUsername());

    await this.saveFarmData(this.id);
  }

  async saveFarmData(userId) {
    // Chuẩn bị dữ liệu để lưu vào cơ sở dữ liệu
    const landData = [];

    for (const landItem of this.session.user.landItems) {
      const landObject = {};
      landObject.growthTime = landItem.getGrowthTime();
      landObject.type = landItem.getType();      // lao
      landObject.suckhoe = landItem.getSucKhoe(); // skhoe
      landObject.resourceCount = landItem.getResourceCount();
      landObject.isWatered = landItem.isWatered();
      landObject.isFertilized = landItem.isFertilized();
      landObject.isHarvestable = landItem.isHarvestable();

      const plantedTime = landItem.getPlantedTime();
      if (plantedTime != null) {
        landObject.plantedTime = formatLocalDateTime(plantedTime);
      } else {
        landObject.plantedTime = 'not_planted'; // Hoặc loại bỏ dòng này
      }

      landData.push(landObject);
    }

    const animalData = [];
    for (const animal of this.session.user.Animal) {
      const animalObject = {};
      animalObject.id = animal.getId();
      animalObject.health = animal.getHealth();
      animalObject.level = animal.getLevel();
      animalObject.resourceCount = animal.getResourceCount();
      animalObject.nextProductionTime = animal.getNextProductionTime();
      animalObject.isAlive = animal.isAlive();
      animalObject.isReadyForBreeding = animal.isReadyForBreeding();
      animalObject.isHarvestable = animal.isHarvestable();
      animalData.push(animalObject);
    }

    const hatgiongData = [];
    for (const hatGiong of this.session.user.hatgiong) {
      const hatGiongObject = {};
      hatGiongObject.id = hatGiong.getId();
      hatGiongObject.soluong = hatGiong.getSoluong();
      hatgiongData.push(hatGiongObject);
    }

    const phanbonData = [];
    for (const phanBon of this.session.user.PhanBon) {
      const phanBonObject = {};
      phanBonObject.id = phanBon.getId();
      phanBonObject.soluong = phanBon.getSoluong();
      // NOTE: giữ nguyên hành vi bản Java (bug: add chính mảng phanbonData vào nó,
      // không phải phanBonObject -> dữ liệu tự tham chiếu)
      phanbonData.push(phanbonData);
    }

    const nongsanData = [];
    for (const nongSan of this.session.user.NongSan) {
      const nongSanObject = {};
      nongSanObject.id = nongSan.getId();
      nongSanObject.soluong = nongSan.getSoluong();
      // NOTE: giữ nguyên hành vi bản Java (bug: add nongsanData vào chính nó)
      nongsanData.push(nongsanData);
    }

    const nongsandacbietData = [];
    for (const nongsandacbiet of this.session.user.NongSanDacBiet) {
      const nongsandacbietObject = {};
      nongsandacbietObject.id = nongsandacbiet.getId();
      nongsandacbietObject.soluong = nongsandacbiet.getSoluong();
      // NOTE: giữ nguyên hành vi bản Java (bug: add phanbonData vào phanbonData)
      phanbonData.push(phanbonData);
    }

    // Cập nhật cơ sở dữ liệu với dữ liệu đã tạo
    const query = 'INSERT INTO `farm_data` (user_id, land_data, animal_data,hatgiong,phanbon,nongsan,nongsandacbiet) VALUES (?, ?, ?, ?, ?, ?, ?) '
      + 'ON DUPLICATE KEY UPDATE land_data = ?, animal_data = ?, hatgiong = ?, phanbon = ?, nongsan = ?, nongsandacbiet = ?';

    // Chuyển đổi dữ liệu thành chuỗi JSON
    const landDataString = toJSONString(landData);
    const animalDataString = toJSONString(animalData);
    const hatgiongDataString = toJSONString(hatgiongData);
    const phanbonDataString = toJSONString(phanbonData);
    const nongsanDataString = toJSONString(nongsanData);
    const nongsandacbietDataString = toJSONString(nongsandacbietData);

    await dbManager.executeUpdate(query, [
      userId,
      landDataString, animalDataString, hatgiongDataString, phanbonDataString, nongsanDataString, nongsandacbietDataString,
      landDataString, animalDataString, hatgiongDataString, phanbonDataString, nongsanDataString, nongsandacbietDataString,
    ]);
  }

  async loadFarmData(userId) {
    const query = 'SELECT land_data, animal_data,hatgiong,phanbon,nongsan,nongsandacbiet FROM `farm_data` WHERE user_id = ?';

    const res = await dbManager.queryOne(query, [userId]);
    if (res) {
      const landDataString = res.land_data;
      const animalDataString = res.animal_data;
      const hatgiongDataString = res.hatgiong;
      const phanbonDataString = res.phanbon;
      const nongsanDataString = res.nongsan;
      const nongsandacbietDataString = res.nongsandacbiet;

      // Phân tích dữ liệu ô đất (land_data)
      const landData = JSONParse(landDataString);
      const landItems = [];

      for (const land of landData) {
        const obj = land;
        const growthTime = toInt(obj.growthTime);
        const type = toInt(obj.type);
        const suckhoe = toInt(obj.suckhoe);
        const resourceCount = toInt(obj.resourceCount);
        const isWatered = obj.isWatered;
        const isFertilized = obj.isFertilized;
        const isHarvestable = obj.isHarvestable;

        const plantedTimeStr = obj.plantedTime;
        const plantedTime = parseLocalDateTime(plantedTimeStr);

        const landItem = new LandItem(growthTime, type, suckhoe, resourceCount, isWatered, isFertilized, isHarvestable, plantedTime);
        landItems.push(landItem);
      }
      // Cập nhật danh sách ô đất cho người chơi
      this.session.user.landItems = landItems;

      // Phân tích dữ liệu vật nuôi (animal_data)
      const animalData = JSONParse(animalDataString);
      const animals = [];

      for (const animal of animalData) {
        const obj = animal;
        const id = toInt(obj.id);
        const health = toInt(obj.health);
        const level = toInt(obj.level);
        const resourceCount = toInt(obj.resourceCount);
        const nextProductionTime = toInt(obj.nextProductionTime);
        const isAlive = obj.isAlive;
        const isReadyForBreeding = obj.isReadyForBreeding;
        const isHarvestable = obj.isHarvestable;

        const animalObj = new Animal(id, health, level, resourceCount, nextProductionTime, isAlive, isReadyForBreeding, isHarvestable);
        animals.push(animalObj);
      }
      // Cập nhật danh sách vật nuôi cho người chơi
      this.session.user.Animal = animals;

      const hatgiongdata = JSONParse(hatgiongDataString);
      const hatgiongs = [];
      for (const hatgiong of hatgiongdata) {
        const obj = hatgiong;
        const id = toInt(obj.id);
        const soluong = toInt(obj.soluong);
        const animalObj = new HatGiong(id, soluong);
        hatgiongs.push(animalObj);
      }
      this.session.user.hatgiong = hatgiongs;

      const phanbondata = JSONParse(phanbonDataString);
      const phanBons = [];
      for (const phanbon of phanbondata) {
        const obj = phanbon;
        const id = toInt(obj.id);
        const soluong = toInt(obj.soluong);
        const pb = new PhanBon(id, soluong);
        phanBons.push(pb);
      }
      this.session.user.PhanBon = phanBons;

      const nongsandata = JSONParse(nongsanDataString);
      const nongSans = [];
      for (const nongsan of nongsandata) {
        const obj = nongsan;
        const id = toInt(obj.id);
        const soluong = toInt(obj.soluong);
        const ns = new NongSan(id, soluong);
        nongSans.push(ns);
      }
      this.session.user.NongSan = nongSans;

      const nongsandbdata = JSONParse(nongsandacbietDataString);
      const nongSandbs = [];
      for (const nongsandb of nongsandbdata) {
        const obj = nongsandb;
        const id = toInt(obj.id);
        const soluong = toInt(obj.soluong);
        const nsdb = new NongSanDacBiet(id, soluong);
        // NOTE: giữ nguyên hành vi bản Java (bug: add vào field NongSanDacBiet
        // thay vì list nongSandbs, sau đó list rỗng ghi đè lên field)
        this.NongSanDacBiet.push(nsdb);
      }
      this.session.user.NongSanDacBiet = nongSandbs;
    }

    // Nếu không có dữ liệu, tạo mặc định cho người chơi
    if (this.session.user.landItems.length === 0) {
      // Tạo mặc định cho 6 ô đất
      const defaultLandItems = [];
      for (let i = 0; i < 6; i++) {
        defaultLandItems.push(new LandItem(0, -1, -1, 0, false, false, false, new Date())); // Cây mặc định
      }
      this.session.user.landItems = defaultLandItems;
    }

    if (this.session.user.Animal.length === 0) {
      this.session.user.Animal = [];
    }
    if (this.session.user.hatgiong.length === 0) {
      this.session.user.hatgiong = [];
    }
    if (this.session.user.PhanBon.length === 0) {
      this.session.user.PhanBon = [];
    }
    if (this.session.user.NongSan.length === 0) {
      this.session.user.NongSan = [];
    }
    if (this.session.user.NongSanDacBiet.length === 0) {
      this.session.user.NongSanDacBiet = [];
    }
  }

  /* ======================= đăng nhập ======================= */

  async login() {
    if (!serverManager.active) {
      this.getService().serverMessage('Máy chủ đang bảo trì. Vui lòng quay lại sau : v');
      return false;
    }

    const ACCOUNT_LOGIN = 'SELECT * FROM `users` WHERE `username` = ? AND `password` = ? LIMIT 1 FOR UPDATE;';
    const SET_LOCK_ACCOUNT = 'UPDATE `users` SET `login_lock` = 1 WHERE `id` = ?;';
    let connection = null;
    let committed = false;
    try {
      connection = await dbManager.pool.getConnection();
      await connection.beginTransaction(); // Bắt đầu transaction

      const [rows] = await connection.query(ACCOUNT_LOGIN, [this.username, Utils.md5(this.password)]);
      const red = rows[0];
      if (red) {
        this.id = toInt(red.id);
        this.role = toByte(red.role);
        const active = !!Number(red.active);
        if (!active) {
          this.getService().serverMessage(GameString.userLoginActive());
          await connection.rollback();
          return false;
        }

        // Kiểm tra khóa đăng nhập
        if (toInt(red.login_lock) === 1) {
          this.getService().serverMessage(GameString.userLoginMany());
          await connection.rollback(); // Rollback nếu phát hiện người dùng đang đăng nhập
          const us = userManager.find(this.id);
          if (us != null) {
            // Ngắt kết nối người dùng cũ
            us.getService().serverMessage(GameString.userLoginMany());
            us.session.close();
            userManager.remove(us);
          }
          const UNLOCK_ACCOUNT_SQL = 'UPDATE users SET login_lock = 0 WHERE id = ?';
          try {
            await dbManager.executeUpdate(UNLOCK_ACCOUNT_SQL, [this.id]);
            console.log('Account unlocked successfully.');
          } catch (ex) {
            console.error(ex);
          }
          return false;
        }

        // Đặt khóa đăng nhập
        await connection.query(SET_LOCK_ACCOUNT, [this.id]);

        // Kiểm tra nếu tài khoản bị cấm
        const banData = (red.ban != null) ? (JSONParse(red.ban) ?? {}) : {};
        if (Object.keys(banData).length !== 0) {
          const banType = toInt(banData.type);
          if (banType === 2) {
            if (banData.forever != null) {
              this.getService().serverMessage(GameString.userLoginLockForever());
              await connection.rollback();
              return false;
            }
            let minutes = toInt(banData.minutes);
            const timeNowwww = new Date();
            const banStart = Utils.getDate(banData.start);
            const banEnd = new Date(banStart.getTime() + 60000 * minutes);
            if (banEnd.getTime() > timeNowwww.getTime()) {
              minutes = Math.trunc((banEnd.getTime() - timeNowwww.getTime()) / 60000);
              this.getService().serverMessage(GameString.userLoginLock(minutes));
              await connection.rollback();
              return false;
            }
          }
        }

        // Kiểm tra nếu người dùng đã đăng nhập từ thiết bị khác
        const us = userManager.find(this.id);
        if (us != null) {
          this.getService().serverMessage(GameString.userLoginMany());
          us.getService().serverMessage(GameString.userLoginMany());
          Utils.setTimeout(() => {
            us.session.close();
            userManager.remove(this);
          }, Utils.nextInt(1500));
          await connection.rollback();
          return false;
        }

        // Mọi thứ OK, commit và giữ khóa đăng nhập
        await connection.commit();
        committed = true;
        return true;
      } else {
        this.getService().serverMessage(GameString.loginPassFail());
      }
    } catch (ex) {
      this.getService().serverMessage(ex.message);
    } finally {
      if (connection) {
        // Java dùng try-with-resources: HikariCP tự rollback transaction chưa
        // commit khi trả connection về pool. mysql2 KHÔNG làm vậy, nên phải
        // rollback tay, nếu không `SELECT ... FOR UPDATE` sẽ giữ lock mãi.
        if (!committed) {
          try { await connection.rollback(); } catch { /* bỏ qua */ }
        }
        try { connection.release(); } catch { /* bỏ qua */ }
      }
    }
    return false;
  }

  async GetdataUserHenho() {
    const GET_PLAYER_DATA = 'SELECT wearing FROM `players` WHERE `user_id` = ? LIMIT 1;';
    try {
      const res = await dbManager.queryOne(GET_PLAYER_DATA, [this.idUsHenHo]);
      if (res) {
        this.wearingMarry = [];
        const wearing = JSONParse(res.wearing);
        for (const o of wearing) {
          const obj = o;
          const id = toInt(obj.id);
          const expired = Number(obj.expired);
          let quantity = 1;
          if (Object.prototype.hasOwnProperty.call(obj, 'quantity')) {
            quantity = toInt(obj.quantity);
          }
          const item = Item.builder().id(id)
            .quantity(quantity)
            .expired(expired)
            .build();
          if (item.reliability() > 0) {
            this.wearingMarry.push(item);
          }
        }
      }
    } catch (e) {
      console.error(e);
    }
  }

  async loadData() {
    const GET_PLAYER_DATA = 'SELECT * FROM `players` WHERE `user_id` = ? LIMIT 1;';
    try {
      const res = await dbManager.queryOne(GET_PLAYER_DATA, [this.id]);
      if (res) {
        if (toInt(res.user_id) === 7) {
          // this.id+=(Npc.ID_ADD+1000);
        }
        this.leverMain = toInt(res.level_main);
        this.expMain = toInt(res.exp_main);
        this.gender = toByte(res.gender);
        this.chestSlot = toByte(res.chest_slot);
        this.chestHomeSlot = toByte(res.chest_home_slot);
        this.xu = Number(res.xu);
        Utils.writeLog(this, 'xu load :' + this.xu);
        this.luong = toInt(res.luong);
        Utils.writeLog(this, 'luong load :' + this.luong);
        this.luongKhoa = toInt(res.luong_khoa);
        this.xeng = toInt(res.xeng);
        this.clanID = toShort(res.clan_id);
        this.friendly = toByte(res.friendly);
        this.crazy = toShort(res.crazy); // vp sk/
        this.stylish = toByte(res.stylish);
        this.happy = toByte(res.happy);
        this.hunger = toByte(res.hunger); // quà
        this.star = toByte(res.star);
        this.scores = toInt(res.scores);
        this.xu_from_boss = toInt(res.xu_from_boss);
        this.TopPhaoLuong = toInt(res.TopPhaoLuong);
        this.TopPhaoXu = toInt(res.TopPhaoXu);
        this.chests = [];
        const chests = JSONParse(res.chests);
        for (const chest of chests) {
          const obj = chest;
          const id = toInt(obj.id);
          const expired = Number(obj.expired);
          let quantity = 1;
          if (Object.prototype.hasOwnProperty.call(obj, 'quantity')) {
            quantity = toInt(obj.quantity);
            if (quantity > 100 || quantity < 0) {
              Utils.writeLog(this, 'loadData quantity ' + quantity);
            }
            if (quantity > 15000 || quantity < 0) {
              Utils.writeLog(this, 'loadData quantity và khoa acc' + quantity);
              Utils.writeLogKhoaAcc(this, 'loadData quantity và khoa acc' + quantity);
              // (phần khóa acc bị comment trong bản Java)
            }
            // có gì khóa acc
          }
          const item = Item.builder().id(id)
            .quantity(quantity)
            .expired(expired)
            .build();
          if (item.reliability() > 0) {
            this.chests.push(item);
          }
        }
        this.wearing = [];
        const wearing = JSONParse(res.wearing);
        for (const o of wearing) {
          const obj = o;
          const id = toInt(obj.id);
          const expired = Number(obj.expired);
          let quantity = 1;
          if (Object.prototype.hasOwnProperty.call(obj, 'quantity')) {
            quantity = toInt(obj.quantity);
          }
          const item = Item.builder().id(id)
            .quantity(quantity)
            .expired(expired)
            .build();
          if (item.reliability() > 0) {
            this.wearing.push(item);
          }
        }
        this.chestsHome = [];
        const chestshome = JSONParse(res.chests_home);
        for (const chest of chestshome) {
          const obj = chest;
          const id = toInt(obj.id);
          const expired = Number(obj.expired);
          let quantity = 1;
          if (Object.prototype.hasOwnProperty.call(obj, 'quantity')) {
            quantity = toInt(obj.quantity);
          }
          const item = Item.builder().id(id)
            .quantity(quantity)
            .expired(expired)
            .build();
          if (item.reliability() > 0) {
            this.chestsHome.push(item);
          }
        }
        await this.loadFarmData(this.id);
        this.calculateDameToXu();

        const checkExistQuery = 'SELECT * FROM marry WHERE idNam = ? OR idNu = ?';
        const userId = this.id; // ID của người dùng hiện tại

        try {
          const rs = await dbManager.queryOne(checkExistQuery, [userId, userId]);
          if (rs) {
            // Nếu có kết quả, chúng ta lấy idNam và idNu
            const idNam = toInt(rs.idNam);
            const idNu = toInt(rs.idNu);
            this.levelMarry = toInt(rs.level);
            this.PerLevelMarry = toInt(rs.perLevel);
            // Kiểm tra xem ID người dùng hiện tại là idNam hay idNu, và lấy ID còn lại
            const otherId = (idNam === userId) ? idNu : idNam;

            // Lấy thông tin của người còn lại
            const userInfoQuery = 'SELECT * FROM users WHERE id = ?';
            const rsUser = await dbManager.queryOne(userInfoQuery, [otherId]);
            if (rsUser) {
              const username = rsUser.username;
              const userID = toInt(rsUser.id);
              this.setIdUsHenHo(userID);
              this.setNamehh(username);
              await this.GetdataUserHenho();
              console.log('Người còn lại: ' + username + ' , id = ' + userID);
            }
          } else {
            console.log('Không tìm thấy người dùng hẹn hò hoặc kết hôn với bạn.');
          }
        } catch (e) {
          console.error(e);
        }

        this.setLoadDataFinish(true);
        return true;
      }
    } catch (ex) {
      console.error(ex);
      this.getService().serverMessage(ex.message);
    }
    return false;
  }

  initAvatar() {
    this.sortWearing();
    this.listCmd.push(new Command('Chức năng', 2));
    this.listCmdRotate.push(new Command(0, 'Hội nhóm', 41, 1));
    this.listCmdRotate.push(new Command(4, 'Oan Tu Xi', 44, 1));
    this.listCmdRotate.push(new Command(23, 'Đổi Skill', 355, 0));
    this.listCmdRotate.push(new Command(36, 'Hẹn hò', 1096, 1));
  }

  doAction(ms) {
    try {
      const idTo = ms.reader().readInt();
      const action = ms.reader().readShort();
      const us = userManager.find(idTo);

      if (this.getZone().getMap().getId() === 16) {
        this.getAvatarService().serverDialog('Bạn không thể hành động ở đây !');
        return;
      }
      switch (action) {
        case 101: {
          // getDayOfWeek(): 1 = Monday ... 7 = Sunday
          const jsDay = new Date().getDay();
          const dayIndex = jsDay === 0 ? 7 : jsDay;
          if (dayIndex === 5 || dayIndex === 6) {
            this.getMapService().doAction(this.id, idTo, action);
            break;
          }
          if (this.gender === us.gender) {
            this.getAvatarService().serverDialog('làm gì vậy bro, đồng giới thì thứ 6 thứ 7');
            break;
          }
          // NOTE: giữ nguyên hành vi bản Java (case 101 fall-through xuống default)
        }
        // eslint-disable-next-line no-fallthrough
        default:
          this.getMapService().doAction(this.id, idTo, action);
      }
    } catch (e) {
      console.error(e);
    }
  }

  getExpMax() {
    return idiv(this.leverMain * (this.leverMain + 1), 2) * 1000;
  }

  getLeverMainPercen() {
    return toByte(idiv(this.expMain * 100, this.getExpMax()));
  }

  viewChest(ms) {
    const type = ms.reader().readInt();
    if (type !== this.id) {
      const _chests = this.chests.filter((item) => item.getPart().getZOrder() === 30 || item.getPart().getZOrder() === 40);
      this.getAvatarService().viewChest(_chests);
      return;
    }
    const _chests = this.chests.filter((item) => item.getPart().getZOrder() !== 30 && item.getPart().getZOrder() !== 40);
    this.getAvatarService().viewChest(_chests);
  }

  // hỏi nâng cấp
  getUpgradeRequirements() {
    if (idiv(this.chestSlot, 5) >= User.UPGRADE_COST_COINS.length - 1) {
      return 'Rương đã đạt cấp tối đa';
    }

    const nextLevel = idiv(this.chestSlot, 5) + 1;
    const coinCost = User.UPGRADE_COST_COINS[nextLevel];
    const goldCost = User.UPGRADE_COST_GOLD[nextLevel];

    return `Để nâng cấp lên rương cấp ${nextLevel - 2} bạn cần ${coinCost} xu và ${goldCost} lượng hoặc thẻ nâng cấp rương.`;
  }

  // nâng cấp rương
  upgradeChest() {
    if (idiv(this.chestSlot, 5) >= User.UPGRADE_COST_COINS.length - 1) {
      return 'Rương đã đạt cấp tối đa';
    }

    const nextLevel = idiv(this.chestSlot, 5) + 1;
    const coinCost = User.UPGRADE_COST_COINS[nextLevel];
    const goldCost = User.UPGRADE_COST_GOLD[nextLevel];

    const theNangCap = this.findItemInChests(3861);
    if (theNangCap != null) {
      this.removeItem(3861, 1);
      this.updateChestSlot(+5);
      return `chúc mừng bạn đã nâng cấp thành công rương cấp ${nextLevel - 2} và có ${this.getChestSlot()} ô rương.`;
    }

    if (this.xu >= coinCost && this.luong >= goldCost) {
      this.updateXu(-coinCost);
      this.getAvatarService().updateMoney(0);
      this.updateLuong(-goldCost);
      this.updateChestSlot(+5);
      this.getAvatarService().updateMoney(0);
      return `chúc mừng bạn đã nâng cấp thành công rương cấp ${nextLevel - 2} và có ${this.getChestSlot()} ô rương.`;
    }

    return 'không đủ xu hoặc lượng';
  }

  requestYourInfo(ms) {
    try {
      const userId = ms.reader().readInt();
      const us = userManager.find(userId);
      if (us != null) {
        this.getAvatarService().requestYourInfo(us);
      }
    } catch (e) {
      console.error(e);
    }
  }

  doAvatarFeel(ms) {
    try {
      if (ms.reader().available() <= 0) {
        return;
      }
      const idFeel = ms.reader().readByte();
      console.log('doAvatarFeel msg 57 = ' + idFeel + ' ');
      this.getMapService().doAvatarFeel(this.id, idFeel);
    } catch (e) {
      console.error(e);
    }
  }

  async close() {
    if (this.zone != null) {
      this.zone.leave(this);
    }
    const timestamp = new Date();
    await dbManager.executeUpdate('UPDATE `players` SET `is_online` = ?, `client_id` = ?, `last_online` = ? WHERE `user_id` = ? LIMIT 1;',
      [0, this.session.id, timestamp, this.id]);
    if (this.isLoadDataFinish()) {
      await this.saveData();
    }
  }

  toString() {
    return 'User ' + this.username;
  }

  move(ms) {
    try {
      if (ms.reader().available() < 5) {
        return;
      }
      const x = ms.reader().readShort();
      const y = ms.reader().readShort();
      const direct = ms.reader().readByte();
      if (ms.reader().available() >= 2) {
        ms.reader().readShort();
      }
      this.x = x;
      this.y = y;
      this.direct = direct;
      this.getMapService().move(this);
      console.log('move ' + x + ', y = ' + y);
    } catch (e) {
      console.error(e);
    }
  }

  addExp(exp) {
    // ServerManager.expRate: hệ số EXP điều khiển trực tiếp từ admin panel
    exp = Math.round(exp * serverManager.expRate) | 0;
    this.expMain = toInt(this.expMain + exp);
    const expMax = this.getExpMax();
    if (this.expMain >= expMax) {
      this.leverMain++;
      this.expMain = toInt(this.expMain - expMax);
    }
  }

  checkFullSlotChest() {
    console.log('chestSlot: ' + this.chestSlot);
    console.log('chests.size(): ' + this.chests.length);
    if (this.getChestSlot() <= this.getChests().length) {
      this.getAvatarService().serverDialog('Rương đồ đã đầy');
      return true;
    }
    return false;
  }

  addItemToChests(item) {
    if (this.chestSlot <= this.chests.length) {
      this.getAvatarService().serverDialog('Rương đồ đã đầy');
      return;
    }
    this.checkItemQuantityLog(item, 'addItemToChests error');

    let itm = this.findItemInChests(item.getId());

    if (itm != null) {
      // Nếu item đã tồn tại và loại item cho phép (type == -2), tăng số lượng
      if (itm.getPart().getType() === -2) {
        itm.increase(this, item.getQuantity(), item.getId());
      } else {
        // Cập nhật độ tin cậy của item (reliability)
        this.setReliabilityForItem(itm, item);
        this.chests.push(item);
      }
    } else {
      // Nếu không tồn tại trong chests, tìm trong wearing
      // itm = findItemInWearing(item.getId());

      if (itm != null) {
        this.setReliabilityForItem(itm, item);
      } else {
        this.chests.push(item);
        Utils.writeLogAddChest(this, 'add item to chests ' + item.getPart().getName());
      }
    }
  }

  checkItemQuantityLog(item, message) {
    if (item == null) {
      Utils.writeLog(this, 'Lỗi: item là null trong ' + message);
      return; // Dừng nếu item là null
    }

    if (item.getQuantity() >= 2 || item.getQuantity() < -1) {
      Utils.writeLog(this, message + ' ' + item.getQuantity() + ' Item ' + (item.getPart() != null ? item.getPart().getName() : 'Unknown'));
    }
  }

  addItemToChestsHome(item) {
    if (this.chestHomeSlot <= this.chestsHome.length) {
      this.getAvatarService().serverDialog('Rương nhà đã đầy');
      return;
    }
    this.checkItemQuantityLog(item, 'addItemToChestsHome error');
    let itm = this.findItemInChests(item.getId());
    if (itm != null) {
      if (itm.getPart().getType() === -2) {
        itm.increase(this, item.getQuantity(), item.getId());
      } else {
        this.setReliabilityForItem(itm, item);
      }
      this.chestsHome.push(item);
      Utils.writeLogAddChest(this, 'add item to chests 1 ' + item.getPart().getName());
      return;
    } else {
      itm = this.findItemInChests(item.getId());
      if (itm != null) {
        this.setReliabilityForItem(itm, item);
        return;
      }
    }
    this.chestsHome.push(item);
    Utils.writeLogAddChest(this, 'add item to chests 2 ' + item.getPart().getName());
  }

  setReliabilityForItem(old, newI) {
    // item expired == -1;
    if (!old.isForever()) {
      if (newI.isForever() || newI.reliability() > old.reliability()) {
        old.setExpired(newI.getExpired());
      }
    }
  }

  removeItemFromChests(item) {
    const i = this.chests.indexOf(item);
    if (i >= 0) this.chests.splice(i, 1);
    this.checkItemQuantityLog(item, 'removeItemFromChest bug');
  }

  removeItemFromChestsHome(item) {
    const i = this.chestsHome.indexOf(item);
    if (i >= 0) this.chestsHome.splice(i, 1);
  }

  addItemToWearing(item) {
    this.checkItemQuantityLog(item, 'addItemToWearing error');
    const itm = this.findItemInWearing(item.getId());
    if (itm == null) {
      this.wearing.push(item);
    } else {
      itm.increase(this, item.getQuantity(), item.getId());
    }
    this.calculateDameToXu();
  }

  removeItemFromWearing(item) {
    this.checkItemQuantityLog(item, 'removeItemFromWearning bug');
    const i = this.wearing.indexOf(item);
    if (i >= 0) this.wearing.splice(i, 1);
    this.calculateDameToXu();
  }

  findItemInChests(id) {
    for (const item of this.chests) {
      if (item.getId() === id) {
        this.checkItemQuantityLog(item, 'FindItemInChests bug');
        return item;
      }
    }
    return null;
  }

  findItemInWearing(id) {
    for (const item of this.wearing) {
      if (item.getId() === id) {
        this.checkItemQuantityLog(item, 'findItemInWearning bug');
        return item;
      }
    }
    return null;
  }

  findItemInChestsHome(id) {
    for (const item of this.chestsHome) {
      if (item.getId() === id) {
        return item;
      }
    }
    return null;
  }

  findItemWearingByZOrder(zOrder) {
    for (const item of this.wearing) {
      if (item.getPart().getZOrder() === zOrder) {
        this.checkItemQuantityLog(item, 'findItemWearningByZorder bug');
        return item;
      }
    }
    return null;
  }

  removeItem(id, quantity) {
    const item = this.findItemInChests(id);
    this.checkItemQuantityLog(item, 'removeItem error');
    if (item != null) {
      const q = item.reduce(quantity);
      if (q <= 0) {
        this.removeItemFromChests(item);
      }
      return true;
    }
    return false;
  }

  findhatgiong(id) {
    for (const hd of this.hatgiong) {
      if (hd.getId() === id) {
        return hd;
      }
    }
    return null;
  }

  /**
   * Java có 2 overload: usingItem(short itemID, byte type) và usingItem(Message ms).
   * Gộp lại: 1 tham số kiểu Message -> đọc từ gói tin.
   */
  usingItem(a, b) {
    if (b === undefined) {
      return this.usingItemMs(a);
    }
    return this.usingItemImpl(toShort(a), toByte(b));
  }

  usingItemImpl(itemID, type) {
    try {
      // logger.debug
      if (type === 1) {
        const item = this.findItemInChests(itemID);
        if (item == null) {
          this.getService().serverDialog('Không tìm thấy vật phẩm');
          return;
        }
        const part = item.getPart();
        const gender = part.getGender();
        if ((gender === 1 || gender === 2) && (this.gender !== gender)) {
          this.getService().serverDialog('Giới tính không phù hợp');
          return;
        }
        const pType = part.getType();
        if (pType === -1) {
          // hp quà ma quái
          if (item.getId() === 5532) {
            if (this.chests.length >= this.getChestSlot()) {
              this.getService().serverMessage('Bạn phải có ít nhất 1 ô trống');
              return;
            }
            this.removeItem(item.getId(), 1);
            const giftBox = new GiftBox();
            giftBox.openHopQuaMaQuai(this, item);
            return;
          }
          const zOrder = part.getZOrder();
          const w = this.findItemWearingByZOrder(zOrder);
          if (this.chestSlot <= this.chests.length) {
            this.getAvatarService().serverDialog('Rương đồ đã đầy 001 ');
            return;
          }
          if (w != null) {
            this.removeItemFromWearing(w);
            this.addItemToChests(w);
          }
          this.addItemToWearing(item);
          this.removeItemFromChests(item);
          this.sortWearing();
          this.getMapService().usingPart(this.id, itemID);
        } else if (pType === -2) {
          if (item.getId() === 683) {
            if (this.chests.length >= this.getChestSlot()) {
              this.getService().serverMessage('Bạn phải có ít nhất 1 ô trống');
              return;
            }
            this.removeItem(item.getId(), 1);
            const giftBox = new GiftBox();
            giftBox.open(this, item);
            return;
          }
          if (item.getId() === 5408) {
            if (this.chests.length >= this.getChestSlot() - 4) {
              this.getService().serverMessage('Bạn phải có ít nhất 5 ô trống để mở hộp quà hải tặc');
              return;
            }
            this.removeItem(item.getId(), 1);
            const giftBox = new GiftBox();
            giftBox.openHaiTac(this, item);
          }
          if (item.getId() === 5324) {
            if (this.chests.length >= this.getChestSlot() - 2) {
              this.getService().serverMessage('Bạn phải có ít nhất 3 ô trống để mở hộp quà siêu nhân');
              return;
            }
            this.removeItem(item.getId(), 1);
            const giftBox = new GiftBox();
            giftBox.openSieuNhan(this, item);
          }
          if (item.getId() === 5880) {
            if (this.chests.length >= this.getChestSlot() - 3) {
              this.getService().serverMessage('Bạn phải có ít nhất 4 ô trống để mở hộp quà siêu anh hùng');
              return;
            }
            this.removeItem(item.getId(), 1);
            const giftBox = new GiftBox();
            giftBox.openSetVuTru(this, item);
          } else {
            // NOTE: giữ nguyên hành vi bản Java (else chỉ gắn với if id==5880)
            // String.format("Số lượng: %,d", ...) -> nhóm nghìn bằng dấu phẩy
            this.getService().serverMessage('Số lượng: ' + item.getQuantity().toLocaleString('en-US'));
          }
        } else {
          this.getService().serverDialog('error 0020'); // Vật phẩm shop Loi, sẽ sớm fix
        }
      } else {
        const item = this.findItemInWearing(itemID);
        if (item == null) {
          return;
        }
        const zOrder = item.getPart().getZOrder();
        if (zOrder === 10 || zOrder === 20 || zOrder === 50) {
          this.getService().serverDialog('Không thể cất vật phẩm này.');
          return;
        }
        if (this.chestSlot <= this.chests.length) {
          this.getAvatarService().serverDialog('Rương đồ đã đầy 002 ');
          return;
        }
        this.removeItemFromWearing(item);
        this.addItemToChests(item);
        this.getMapService().usingPart(this.id, itemID);
      }
    } catch (e) {
      console.error(e);
    }
  }

  usingItemMs(ms) {
    try {
      const itemID = ms.reader().readShort();
      const type = ms.reader().readByte();
      this.usingItemImpl(itemID, type);
    } catch (e) {
      console.error(e);
    }
  }

  chat(ms) {
    try {
      if (ms.reader().available() < 4) {
        return;
      }
      const message = ms.reader().readUTF();
      this.getMapService().chat(this, message);
    } catch (e) {
      console.error(e);
    }
  }

  doRemoveItem(ms) {
    try {
      const itemID = ms.reader().readShort();
      const type = ms.reader().readByte();
      if (type === 0) {
        const item = this.findItemInWearing(itemID);
        if (item != null) {
          const zOrder = item.getPart().getZOrder();
          if (zOrder === 10 || zOrder === 20 || zOrder === 50) {
            this.getAvatarService().serverDialog('error : 001');
            return;
          }
          this.removeItemFromWearing(item);
          this.getMapService().removeItem(this.id, itemID);
          if (this.getStylish() > 0) {
            this.setStylish(toByte(this.getStylish() - 1));
            this.getAvatarService().requestYourInfo(this);
          }
        }
      } else {
        const item = this.findItemInChests(itemID);
        if (item != null) {
          this.removeItemFromChests(item);
          this.getAvatarService().removeItem(this.id, itemID);
          if (this.getStylish() > 0) {
            this.setStylish(toByte(this.getStylish() - 1));
            this.getAvatarService().requestYourInfo(this);
          }
        }
      }
    } catch (e) {
      console.error(e);
    }
  }

  notifyNetWaitMessage() {
    // NOTE: giữ nguyên hành vi bản Java (Java notifyAll trên session.obj để đánh thức
    // luồng chờ; Node đơn luồng nên không cần — giữ hàm cho tương thích API)
    if (this.session && this.session.obj && typeof this.session.obj.notifyAll === 'function') {
      this.session.obj.notifyAll();
    }
  }

  skillUidToBoss(players, us, npcID, skill1, skill2) {
    for (const player of players) {
      EffectService.createEffect()
        .session(player.session)
        .id(skill1)
        .style(0)
        .loopLimit(5)
        .loop(1)
        .loopType(1)
        .radius(1)
        .idPlayer(us)
        .send();
      EffectService.createEffect()
        .session(player.session)
        .id(skill2)
        .style(0)
        .loopLimit(5)
        .loop(1)
        .loopType(1)
        .radius(1)
        .idPlayer(npcID)
        .send();
    }
  }

  /* =========================================================================
   * Lombok @Getter/@Setter — port thành phương thức thật (field vẫn dùng được)
   * ========================================================================= */

  isAutoFish() { return this.AutoFish; }
  getAutoFish() { return this.AutoFish; }
  setAutoFish(v) { this.AutoFish = v; }

  getBossMapId() { return this.bossMapId; }
  setBossMapId(v) { this.bossMapId = toInt(v); }

  getTopPhaoLuong() { return this.TopPhaoLuong; }
  setTopPhaoLuong(v) { this.TopPhaoLuong = toInt(v); }

  getTopPhaoXu() { return this.TopPhaoXu; }
  setTopPhaoXu(v) { this.TopPhaoXu = toInt(v); }

  getXu_from_boss() { return this.xu_from_boss; }
  setXu_from_boss(v) { this.xu_from_boss = toInt(v); }

  getSpam() { return this.spam; }
  setSpam(v) { this.spam = toInt(v); }

  getHP() { return this.HP; }
  setHP(v) { this.HP = toInt(v); }

  setDefeated(v) { this.isDefeatedFlag = v; }
  setSpamFlag(v) { this.isSpamFlag = v; }

  getIdUsHenHo() { return this.idUsHenHo; }
  setIdUsHenHo(v) { this.idUsHenHo = toInt(v); }

  getNamehh() { return this.namehh; }
  setNamehh(v) { this.namehh = v; }

  getWearingMarry() { return this.wearingMarry; }
  setWearingMarry(v) { this.wearingMarry = v; }

  getLevelMarry() { return this.levelMarry; }
  setLevelMarry(v) { this.levelMarry = toInt(v); }

  getPerLevelMarry() { return this.PerLevelMarry; }
  setPerLevelMarry(v) { this.PerLevelMarry = toInt(v); }

  getImginfo() { return this.imginfo; }
  setImginfo(v) { this.imginfo = toInt(v); }

  getTenNhan() { return this.tenNhan; }
  setTenNhan(v) { this.tenNhan = v; }

  getStoredXuUpdate() { return this.storedXuUpdate; }
  setStoredXuUpdate(v) { this.storedXuUpdate = toInt(v); }

  getSession() { return this.session; }
  setSession(v) { this.session = v; }

  getId() { return this.id; }
  setId(v) { this.id = toInt(v); }

  getUsername() { return this.username; }
  setUsername(v) { this.username = v; }

  getPassword() { return this.password; }
  setPassword(v) { this.password = v; }

  getIdFish() { return this.idFish; }
  setIdFish(v) { this.idFish = toShort(v); }

  getGender() { return this.gender; }

  getXu() { return this.xu; }
  setXu(v) { this.xu = Number(v); }

  getLuong() { return this.luong; }
  setLuong(v) { this.luong = toInt(v); }

  getLuongKhoa() { return this.luongKhoa; }
  setLuongKhoa(v) { this.luongKhoa = toInt(v); }

  getAvailableSkills() { return this.availableSkills; }
  setAvailableSkills(v) { this.availableSkills = v; }

  getUseSkill() { return this.useSkill; }

  getDame() { return this.dame; }
  setDame(v) { this.dame = toInt(v); }

  getDameToXu() { return this.dameToXu; }
  setDameToXu(v) { this.dameToXu = toInt(v); }

  setLastTimeSet(v) { this.lastTimeSet = v; }

  getCorrectAnswer() { return this.correctAnswer; }
  setCorrectAnswer(v) { this.correctAnswer = toInt(v); }

  getXeng() { return this.xeng; }
  setXeng(v) { this.xeng = toInt(v); }

  getClanID() { return this.clanID; }
  setClanID(v) { this.clanID = toShort(v); }

  getRole() { return this.role; }
  setRole(v) { this.role = toByte(v); }

  getStar() { return this.star; }
  setStar(v) { this.star = toByte(v); }

  getLeverMain() { return this.leverMain; }
  setLeverMain(v) { this.leverMain = toInt(v); }

  getExpMain() { return this.expMain; }
  setExpMain(v) { this.expMain = toInt(v); }

  getLeverFarm() { return this.leverFarm; }
  setLeverFarm(v) { this.leverFarm = toInt(v); }

  getLeverPercen() { return this.leverPercen; }
  setLeverPercen(v) { this.leverPercen = toByte(v); }

  getExpFarm() { return this.expFarm; }
  setExpFarm(v) { this.expFarm = toInt(v); }

  getFriendly() { return this.friendly; }
  setFriendly(v) { this.friendly = toByte(v); }

  getCrazy() { return this.crazy; }
  setCrazy(v) { this.crazy = toShort(v); }

  getStylish() { return this.stylish; }
  setStylish(v) { this.stylish = toByte(v); }

  getHappy() { return this.happy; }
  setHappy(v) { this.happy = toByte(v); }

  getHunger() { return this.hunger; }
  setHunger(v) { this.hunger = toByte(v); }

  getChestSlot() { return this.chestSlot; }
  setChestSlot(v) { this.chestSlot = toByte(v); }

  getChestHomeSlot() { return this.chestHomeSlot; }
  setChestHomeSlot(v) { this.chestHomeSlot = toByte(v); }

  getScores() { return this.scores; }
  setScores(v) { this.scores = toInt(v); }

  getWearing() { return this.wearing; }
  setWearing(v) { this.wearing = v; }

  getChests() { return this.chests; }
  setChests(v) { this.chests = v; }

  getChestsHome() { return this.chestsHome; }
  setChestsHome(v) { this.chestsHome = v; }

  getLandItems() { return this.landItems; }
  setLandItems(v) { this.landItems = v; }

  getAnimal() { return this.Animal; }
  setAnimal(v) { this.Animal = v; }

  getHatgiong() { return this.hatgiong; }
  setHatgiong(v) { this.hatgiong = v; }

  getNongSan() { return this.NongSan; }
  setNongSan(v) { this.NongSan = v; }

  getPhanBon() { return this.PhanBon; }
  setPhanBon(v) { this.PhanBon = v; }

  getNongSanDacBiet() { return this.NongSanDacBiet; }
  setNongSanDacBiet(v) { this.NongSanDacBiet = v; }

  getZone() { return this.zone; }
  setZone(v) { this.zone = v; }

  getX() { return this.x; }
  setX(v) { this.x = toShort(v); }

  getY() { return this.y; }
  setY(v) { this.y = toShort(v); }

  getDirect() { return this.direct; }
  setDirect(v) { this.direct = toByte(v); }

  getMenus() { return this.menus; }
  setMenus(v) { this.menus = v; }

  getDialLucky() { return this.dialLucky; }
  setDialLucky(v) { this.dialLucky = v; }

  getIdImg() { return this.idImg; }
  setIdImg(v) { this.idImg = toShort(v); }

  getListCmd() { return this.listCmd; }
  setListCmd(v) { this.listCmd = v; }

  getListCmdRotate() { return this.listCmdRotate; }
  setListCmdRotate(v) { this.listCmdRotate = v; }

  isLoadDataFinish() { return this.loadDataFinish; }
  setLoadDataFinish(v) { this.loadDataFinish = v; }

  getBossShopItems() { return this.bossShopItems; }
  setBossShopItems(v) { this.bossShopItems = v; }

  getShopEvent() { return this.ShopEvent; }
  setShopEvent(v) { this.ShopEvent = v; }

  getBoardIDs() { return this.boardIDs; }
  setBoardIDs(v) { this.boardIDs = v; }

  setMoneyPutList(v) { this.moneyPutList = v; }
}

export default User;
