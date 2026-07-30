/** Port của avatar/handler/UpgradeItemHandler.java */
import { NpcName } from '../constants/NpcName.js';
import { partManager } from '../item/PartManager.js';
import { BossShop } from '../model/BossShop.js';
import { Npc } from '../model/Npc.js';

export class UpgradeItemHandler {
  static SELECT_XU = 0;
  static SELECT_LUONG = 1;

  /** Java: doShowUpgradeItems(User us, byte type, int from, int to) */
  static doShowUpgradeItems(us, type, from, to) {
    const service = us.getAvatarService();
    const upgradeItems = partManager.getUpgradeItems()
      .filter((upgradeItem) => upgradeItem.getItem().getId() >= from
        && upgradeItem.getItem().getId() <= to);
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
}

export default UpgradeItemHandler;
