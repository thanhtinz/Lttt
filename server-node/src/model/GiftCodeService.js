// Port của avatar/model/GiftCodeService.java
import { dbManager } from '../db/DbManager.js';
import { Item } from '../item/Item.js';
import { userManager } from '../server/UserManager.js';
import { Utils } from '../server/Utils.js';

class GiftCode {
  constructor(id, code, message, data, startTime, endTime, num, createBy, createTime) {
    this.id = id;
    this.code = code;
    this.message = message;
    this.data = data;
    this.startTime = startTime;
    this.endTime = endTime;
    this.num = num;
    this.createBy = createBy;
    this.createTime = createTime;
  }

  // Getters and setters
}

export class GiftCodeService {

  constructor() {
    this.giftCodes = new Map();
    // NOTE: giữ nguyên hành vi bản Java (constructor gọi loadGiftCodes());
    // ở Node là async nên giữ promise để nơi khác có thể await.
    this.loadPromise = this.loadGiftCodes().catch((e) => { console.error(e); });
  }

  async loadGiftCodes() {
    const sql = 'SELECT * FROM giftcode';

    try {
      const rows = await dbManager.query(sql);
      for (const rs of rows) {
        const code = rs.code;
        const giftCode = new GiftCode(
          rs.id | 0,
          code,
          rs.message,
          rs.data,
          rs.start_time,
          rs.end_time,
          rs.num | 0,
          rs.create_by | 0,
          rs.create_time
        );
        this.giftCodes.set(code, giftCode);
      }
    } catch (e) {
      console.error(e);
    }
  }

  // Phương thức kiểm tra tính hợp lệ của mã quà tặng
  isValidGiftCode(code) {
    const giftCode = this.giftCodes.get(code);
    if (giftCode == null) {
      return false; // Mã không tồn tại
    }
    const now = Date.now();
    return giftCode.startTime.getTime() <= now && giftCode.endTime.getTime() >= now && giftCode.num > 0;
  }

  // Cập nhật số lượng mã quà tặng trong cơ sở dữ liệu
  async updateGiftCodeInDatabase(giftCode) {
    const sql = 'UPDATE giftcode SET num = ? WHERE code = ?';
    await dbManager.executeUpdate(sql, [giftCode.num, giftCode.code]);
  }

  // Ghi nhận việc sử dụng mã quà tặng vào bảng giftcode_use
  async recordGiftCodeUsage(userId, giftCodeId) {
    // Kiểm tra xem người dùng đã sử dụng mã quà tặng này chưa
    const checkSql = 'SELECT COUNT(*) FROM giftcode_use WHERE user = ? AND giftcode_id = ?';
    const rs = await dbManager.queryOne(checkSql, [userId, giftCodeId]);
    const count = rs == null ? 0 : (Number(Object.values(rs)[0]) | 0);

    if (count > 0) {
      // Nếu đã dùng, không thực hiện chèn và có thể thông báo lỗi
      // NOTE: giữ nguyên hành vi bản Java (chỉ báo lỗi rồi vẫn chèn bản ghi mới)
      userManager.find(userId).getAvatarService().serverDialog('Bạn đã dùng mã quà tặng đã được sử dụng trước đó');
    }

    // Nếu chưa tồn tại, chèn bản ghi mới
    const insertSql = 'INSERT INTO giftcode_use (user, giftcode_id) VALUES (?, ?)';
    await dbManager.executeUpdate(insertSql, [userId, giftCodeId]);
  }

  // Sử dụng mã quà tặng
  async useGiftCode(userId, code) {
    const giftCode = this.giftCodes.get(code);
    if (giftCode == null) {
      return false; // Mã không tồn tại
    }

    const now = Date.now();
    if (giftCode.startTime.getTime() <= now && giftCode.endTime.getTime() >= now && giftCode.num > 0) {
      // Giảm số lượng mã quà tặng trong bộ nhớ
      giftCode.num -= 1;

      try {
        // Cập nhật cơ sở dữ liệu
        await this.updateGiftCodeInDatabase(giftCode);

        // Ghi nhận việc sử dụng mã quà tặng
        await this.recordGiftCodeUsage(userId, giftCode.id);

        // Phân phối quà tương ứng
        this.distributeGift(userId, giftCode);

      } catch (e) {
        console.error(e);
        return false; // Xử lý lỗi nếu không thể cập nhật cơ sở dữ liệu
      }

      return true;
    }
    return false; // Mã không hợp lệ hoặc hết số lượng
  }

  distributeGift(userId, giftCode) {

    const MaCode = giftCode.code;
    const us = userManager.find(userId);

    switch (MaCode) {

      case '14tieng': {
        if (us.chests.length >= us.getChestSlot() - 6) {
          us.getAvatarService().serverDialog('chào bạn ' + us.getUsername() + ' bạn phải trống trên 7 ô rương');
          return;
        }
        const itemIds = [];
        itemIds.push(4732);
        itemIds.push(4733);
        itemIds.push(5724);
        itemIds.push(6112);
        itemIds.push(6112);
        itemIds.push(6670);
        // Bước 2: Tạo một đối tượng Random
        // Bước 3: Lấy một ID ngẫu nhiên từ danh sách
        const randomIndex = Utils.nextInt(itemIds.length); // Lấy chỉ số ngẫu nhiên
        const randomItemId = itemIds[randomIndex]; // Lấy ID ngẫu nhiên
        const daisen = new Item(randomItemId);
        daisen.setExpired(Date.now() + (86400000 * 3));
        us.getAvatarService().serverDialog('bạn vừa nhận được ' + daisen.getPart().getName() + ' 3 ngay');
        us.addItemToChests(daisen);
        const traiTim = new Item(6793);
        traiTim.setExpired(Date.now() + (86400000 * 3));
        const traiTim1 = new Item(6794);
        traiTim1.setExpired(Date.now() + (86400000 * 3));
        us.addItemToChests(traiTim);
        us.addItemToChests(traiTim1);

        const hopqua = new Item(593, -1, 140);
        //hopqua.setExpired(System.currentTimeMillis() + (86400000L * time));
        if (us.findItemInChests(593) != null) {
          const quantity = us.findItemInChests(593).getQuantity();
          us.findItemInChests(593).setQuantity(quantity + 140);
        } else {
          us.addItemToChests(hopqua);
        }

        break;
      }

      case '20thang10': {

        if (us.chests.length >= us.getChestSlot() - 6) {
          us.getAvatarService().serverDialog('chào bạn ' + us.getUsername() + ' bạn phải trống trên 7 ô rương');
          return;
        }

        const hopquask1 = new Item(683, -1, 200);
        if (us.findItemInChests(683) != null) {
          const quantity = us.findItemInChests(683).getQuantity();
          us.findItemInChests(683).setQuantity(quantity + 200);
        } else {
          us.addItemToChests(hopquask1);
        }
        const qs = new Item(593, -1, 100);
        us.addItemToChests(qs);

        const canh = new Item(6723);
        canh.setExpired(Date.now() + (86400000 * 3));
        us.addItemToChests(canh);

        const daixen = new Item(6670);

        if (us.getGender() === 2) {
          daixen.setExpired(Date.now() + (86400000 * 3));
          const hoahong = new Item(5485);
          hoahong.setExpired(Date.now() + (86400000 * 3));
          us.addItemToChests(hoahong);
        } else {
          daixen.setExpired(Date.now() + (86400000 * 1));
        }
        us.addItemToChests(daixen);
        const traiTim11 = new Item(6793);
        traiTim11.setExpired(Date.now() + (86400000 * 3));
        const traiTim1111 = new Item(6794);
        traiTim1111.setExpired(Date.now() + (86400000 * 3));
        us.addItemToChests(traiTim11);
        us.addItemToChests(traiTim1111);
        break;
      }

      case 'tanthu': {

        us.updateXu(+500000);

        const canCau = new Item(446);
        canCau.setExpired(Date.now() + (86400000 * 30));
        us.addItemToChests(canCau);

        const NcRuong = new Item(3861, Date.now() + (86400000 * 30), 1);
        us.addItemToChests(NcRuong);
        us.getAvatarService().serverDialog('tanthu bạn nhận được 1 thẻ nâng cấp rương 30 ngày, và 1 cần câu vip 30 ngày');

        const vecau = new Item(460, Date.now() + (86400000 * 30), 1);
        us.addItemToChests(vecau);
        break;
      }

      case 'denbu': {

        const hopqua12 = new Item(683, -1, 100);
        //hopqua.setExpired(System.currentTimeMillis() + (86400000L * time));
        if (us.findItemInChests(683) != null) {
          const quantity = us.findItemInChests(683).getQuantity();
          us.findItemInChests(683).setQuantity(quantity + 100);
        } else {
          us.addItemToChests(hopqua12);
        }
        const itemqs = new Item(593, -1, 200);
        us.addItemToChests(itemqs);
        us.getAvatarService().serverDialog('denbu bạn nhận được 100 hộp quà, và 100 thẻ quay số');
        //                String[] data = giftCode.data.split(":");// Ví dụ data = "itemId:quantity"
        //                int itemId = Integer.parseInt(data[0]);
        //                int quantity = Integer.parseInt(data[1]);
        //                Item useGift = new Item(itemId,-1,100);
        //                UserManager.getInstance().find(userId).addItemToChests(useGift);
        break;
      }
      case 'trungthu': {

        const hopquatt = new Item(683, -1, 200);
        //hopqua.setExpired(System.currentTimeMillis() + (86400000L * time));
        if (us.findItemInChests(683) != null) {
          // NOTE: giữ nguyên hành vi bản Java (đặt cứng 200, không cộng thêm)
          us.findItemInChests(683).setQuantity(200);
        } else {
          us.addItemToChests(hopquatt);
        }
        const itemqs1 = new Item(593, -1, 400);
        us.addItemToChests(itemqs1);
        us.getAvatarService().serverDialog('denbu bạn nhận được 100 hộp quà, và 400 thẻ quay số');
        break;
      }
      case '100tv': {

        const hopquask = new Item(683, -1, 200);
        //hopqua.setExpired(System.currentTimeMillis() + (86400000L * time));
        if (us.findItemInChests(683) != null) {
          const quantity = us.findItemInChests(683).getQuantity();
          us.findItemInChests(683).setQuantity(quantity + 200);
        } else {
          us.addItemToChests(hopquask);
        }
        const qs1 = new Item(593, -1, 200);
        us.addItemToChests(qs1);
        us.getAvatarService().serverDialog('100tv bạn nhận được 100 hộp quà và');
        if (us.getGender() === 2) {
          const nuhoangparty1 = new Item(3271);
          nuhoangparty1.setExpired(Date.now() + (86400000 * 10));
          const nuhoangparty2 = new Item(3270);
          nuhoangparty2.setExpired(Date.now() + (86400000 * 10));

          const nuhoangparty3 = new Item(2288);
          nuhoangparty3.setExpired(Date.now() + (86400000 * 30));

          us.addItemToChests(nuhoangparty1);
          us.addItemToChests(nuhoangparty2);
          us.addItemToChests(nuhoangparty3);

        } else {
          const onghoangparty1 = new Item(3276);
          onghoangparty1.setExpired(Date.now() + (86400000 * 7));
          const onghoangparty2 = new Item(3277);
          onghoangparty2.setExpired(Date.now() + (86400000 * 7));
          const onghoangparty3 = new Item(2288);
          onghoangparty3.setExpired(Date.now() + (86400000 * 30));

          us.addItemToChests(onghoangparty1);
          us.addItemToChests(onghoangparty2);
          us.addItemToChests(onghoangparty3);
        }

        break;
      }
      default:
        us.getAvatarService().serverDialog('mã quà tặng không hợp lệ');
        // Có thể thêm thông báo cho người chơi hoặc ghi log lỗi
    }
  }
}

export default GiftCodeService;
