/** Port của avatar/model/UpgradeItem.java (@Getter @Setter @SuperBuilder, extends BossShopItem). */
import { BossShopItem } from '../common/BossShopItem.js';
import { NpcName } from '../constants/NpcName.js';
import { partManager } from '../item/PartManager.js';
import { Npc } from './Npc.js';

// NOTE: avatar.handler.BossShopHandler chưa được port; giữ nguyên giá trị hằng của bản Java.
const SELECT_XU = 0;
const SELECT_LUONG = 1;
const SELECT_DNS = 2;
const SELECT_HoaNS = 3;
const SELECT_ManhGhep = 4;

/** Mô phỏng java.text.MessageFormat.format: {n} + số có phân cách nghìn. */
function messageFormat(pattern, ...args) {
  return String(pattern).replace(/\{(\d+)\}/g, (m, i) => {
    const v = args[Number(i)];
    if (v === undefined) return m;
    if (typeof v === 'number') {
      // NumberFormat mặc định: nhóm 3 chữ số
      return v.toLocaleString('en-US', { maximumFractionDigits: 3 });
    }
    return String(v);
  });
}

export class UpgradeItem extends BossShopItem {
  constructor(id = 0, itemRequest = 0, item = null,
              isOnlyLuong = false, ratio = 0, itemNeed = 0, xu = 0, luong = 0, scores = 0) {
    super(id, itemRequest, item);
    // NOTE: JS không cho vừa field `isOnlyLuong` vừa method `isOnlyLuong()`
    // ⇒ lưu vào `_isOnlyLuong`, truy cập qua isOnlyLuong()/setOnlyLuong().
    this._isOnlyLuong = !!isOnlyLuong;
    this.ratio = ratio | 0;      // tỉ lệ nâng cấp
    this.itemNeed = itemNeed | 0; // item cần có để nâng
    this.itemRequest = itemRequest | 0; // name_item
    this.xu = xu | 0;
    this.luong = luong | 0;
    this.scores = scores | 0;    // điểm đổi sự kiện
  }

  isOnlyLuong() { return this._isOnlyLuong; }
  setOnlyLuong(v) { this._isOnlyLuong = !!v; }
  getRatio() { return this.ratio; }
  setRatio(v) { this.ratio = v | 0; }
  getItemNeed() { return this.itemNeed; }
  setItemNeed(v) { this.itemNeed = v | 0; }
  getItemRequest() { return this.itemRequest; }
  setItemRequest(v) { this.itemRequest = v | 0; }
  getXu() { return this.xu; }
  setXu(v) { this.xu = v | 0; }
  getLuong() { return this.luong; }
  setLuong(v) { this.luong = v | 0; }
  getScores() { return this.scores; }
  setScores(v) { this.scores = v | 0; }

  static builder() {
    const f = {};
    const b = {
      build: () => new UpgradeItem(f.id, f.itemRequest, f.item,
        f.isOnlyLuong, f.ratio, f.itemNeed, f.xu, f.luong, f.scores),
    };
    for (const k of ['id', 'itemRequest', 'item', 'isOnlyLuong', 'ratio', 'itemNeed',
      'xu', 'luong', 'scores']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }

  initDialog(bossShop) {
    const ratio = this.ratio;
    const xu = this.xu;
    const luong = this.luong;
    const itemNeed = this.itemNeed;

    if (this._isOnlyLuong && bossShop.getIdShop() === SELECT_XU) {
      return 'Bạn chỉ có thể nâng cấp vật phẩm này bằng lượng';
    } else if (bossShop.getIdBoss() === NpcName.bunma + Npc.ID_ADD) {
      if (this.getItem().getId() === 3861) {
        return messageFormat(
          'Bạn có muốn đổi {0} bằng {1} điểm sự kiện không?(tối đa 1)',
          this.getItem().getPart().getName(),
          this.scores,
        );
      }
      return messageFormat(
        'Bạn có muốn đổi {0} bằng {1} điểm sự kiện không?',
        this.getItem().getPart().getName(),
        this.scores,
      );
    } else if (bossShop.getIdShop() === SELECT_DNS) {
      let resources = '';
      if (luong > 0) {
        resources += luong + ' Lượng';
      }
      if (xu > 0) {
        if (resources.length > 0) {
          resources += ' + '; // Thêm dấu "+" nếu đã có Lượng
        }
        resources += xu + ' Xu';
      }
      let ratioDisplay = '';
      if (ratio > 0 && ratio < 100) {
        ratioDisplay = ratio + '%';
      } else if (ratio === 0) {
        ratioDisplay = 'Không xác định';
      }

      // Điều kiện định dạng dựa vào tỷ lệ xác suất
      const formatString = ratioDisplay.length === 0
        ? 'Bạn có muốn đổi {0} từ {1} + {2} + {3}'
        : 'Bạn có muốn nâng cấp {0} từ {1} + {2} + {3} (xác suất {4})';

      return messageFormat(
        formatString,
        this.getItem().getPart().getName(),
        partManager.findPartById(itemNeed).getName(),
        this.scores + ' Đá ngũ sắc',
        resources,
        ratioDisplay,
      );
    } else if (bossShop.getIdShop() === SELECT_HoaNS) {
      let resources = '';
      if (luong > 0) {
        resources += luong + ' Lượng';
      }
      if (xu > 0) {
        if (resources.length > 0) {
          resources += ' + ';
        }
        resources += xu + ' Xu';
      }
      return messageFormat(
        'Bạn có muốn nâng cấp {0} từ {1} + {2} + {3} (xác suất {4})',
        this.getItem().getPart().getName(),
        partManager.findPartById(itemNeed).getName(),
        this.scores === 12 ? ' 20 Sen Ngũ Sắc' : this.scores + ' Sen Ngũ Sắc',
        resources, // Sử dụng chuỗi tài nguyên đã xây dựng
        ratio > 0 ? (ratio + '%') : 'Không xác định',
      );
    } else if (bossShop.getIdShop() === SELECT_ManhGhep) {
      return messageFormat(
        'Bạn có muốn đổi {0} bằng {1} {2} không ?',
        this.getItem().getPart().getName(),
        this.scores,
        partManager.findPartById(itemNeed).getName(),
      );
    } else if (bossShop.getIdBoss() === NpcName.Vegeta + Npc.ID_ADD) {
      return messageFormat(
        'Bạn có muốn đổi {0} bằng {1} không?',
        this.getItem().getPart().getName(),
        partManager.findPartById(itemNeed).getName(),
      );
    } else if (bossShop.getIdBoss() === NpcName.Shop_Buy_Luong + Npc.ID_ADD) {
      return messageFormat(
        'Bạn có muốn đổi {0} bằng 1 jack à nhầm bằng {1} Lượng không?',
        this.getItem().getPart().getName(),
        this.getItem().getPart().getGold(),
      );
    } else if (bossShop.getIdBoss() === NpcName.Chay_To_Win + Npc.ID_ADD) {
      return messageFormat(
        'Bạn có muốn đổi {0} bằng {1} xu không?',
        this.getItem().getPart().getName(),
        this.getXu(),
      );
    } else if (bossShop.getIdBoss() === NpcName.Pay_To_Win + Npc.ID_ADD) {
      return messageFormat(
        'Bạn có muốn đổi {0} bằng {1} {2} không?',
        this.getItem().getPart().getName(),
        this.getScores(),
        partManager.findPartById(itemNeed).getName(),
      );
    }
    return messageFormat(
      'Bạn có muốn ghép 1 {0}+{1} để lấy 1 {2}(xác suất {3})',
      partManager.findPartById(itemNeed).getName(),
      bossShop.getIdShop() === SELECT_XU ? (xu + ' xu') : (luong + ' lượng'),
      this.getItem().getPart().getName(),
      ratio > 0 ? (ratio + '%') : 'Không xác định',
    );
  }
}

export { SELECT_XU, SELECT_LUONG, SELECT_DNS, SELECT_HoaNS, SELECT_ManhGhep };
export default UpgradeItem;
