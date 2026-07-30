/** Port của avatar/common/BossShopItem.java (@Getter @Setter @AllArgsConstructor @SuperBuilder). */
export class BossShopItem {
  constructor(id = 0, itemRequest = 0, item = null) {
    this.id = id | 0;
    this.itemRequest = itemRequest | 0;
    this.item = item;
  }

  getId() { return this.id; }
  setId(v) { this.id = v | 0; }
  getItemRequest() { return this.itemRequest; }
  setItemRequest(v) { this.itemRequest = v | 0; }
  getItem() { return this.item; }
  setItem(v) { this.item = v; }

  /** abstract trong Java */
  initDialog(bossShop) {
    throw new Error('BossShopItem.initDialog is abstract');
  }
}

export default BossShopItem;
