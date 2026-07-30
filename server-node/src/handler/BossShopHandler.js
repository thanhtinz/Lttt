/** Port của avatar/handler/BossShopHandler.java */
import { NpcName } from '../constants/NpcName.js';
import { partManager } from '../item/PartManager.js';
import { BossShop } from '../model/BossShop.js';
import { Npc } from '../model/Npc.js';

export class BossShopHandler {
  static SELECT_XU = 0;
  static SELECT_LUONG = 1;
  static SELECT_DNS = 2;
  static SELECT_HoaNS = 3;
  static SELECT_ManhGhep = 4;

  /**
   * Java: displayUI(User us, byte type, int... itemIds)
   * @param {any} us
   * @param {number} type
   * @param {...number} itemIds
   */
  static displayUI(us, type, ...itemIds) {
    const service = us.getAvatarService();
    const itemIdList = itemIds.map((v) => v | 0);
    // Giữ đúng thứ tự itemIds: sắp theo indexOf, không thấy thì đẩy về cuối
    const upgradeItems = partManager.getUpgradeItems()
      .filter((upgradeItem) => itemIdList.includes(upgradeItem.getItem().getId()))
      .sort((a, b) => {
        const ia = itemIdList.indexOf(a.getItem().getId());
        const ib = itemIdList.indexOf(b.getItem().getId());
        const ka = ia === -1 ? 2147483647 : ia;
        const kb = ib === -1 ? 2147483647 : ib;
        return ka - kb;
      });
    us.setBossShopItems(upgradeItems);
    service.openUIBossShop(
      BossShop.builder()
        .idBoss(NpcName.THO_KIM_HOAN + Npc.ID_ADD)
        .idShop((type << 24) >> 24)
        .typeShop(0)
        .name('Nâng cấp')
        .build(),
      upgradeItems,
    );
  }

  static handle() {
  }
}

export default BossShopHandler;
