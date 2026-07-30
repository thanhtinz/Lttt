/** Port của avatar/handler/ShopEventHandler.java */
import { partManager } from '../item/PartManager.js';
import { BossShop } from '../model/BossShop.js';
import { Npc } from '../model/Npc.js';

export class ShopEventHandler {
  /**
   * Java: displayUI(User us, int npcID, int... itemIds)
   * @param {any} us
   * @param {number} npcID
   * @param {...number} itemIds
   */
  static displayUI(us, npcID, ...itemIds) {
    const service = us.getAvatarService();
    const itemIdList = itemIds.map((v) => v | 0);
    const EventShop = partManager.getUpgradeItems()
      .filter((upgradeItem) => itemIdList.includes(upgradeItem.getItem().getId()))
      .sort((a, b) => {
        const ia = itemIdList.indexOf(a.getItem().getId());
        const ib = itemIdList.indexOf(b.getItem().getId());
        const ka = ia === -1 ? 2147483647 : ia;
        const kb = ib === -1 ? 2147483647 : ib;
        return ka - kb;
      });
    us.setBossShopItems(EventShop);
    service.openUIShopEvent(
      BossShop.builder()
        .idBoss(npcID + Npc.ID_ADD)
        .idShop(0)
        .typeShop(0)
        .name('Event')
        .build(),
      EventShop,
    );
  }

  static handle() {
  }
}

export default ShopEventHandler;
