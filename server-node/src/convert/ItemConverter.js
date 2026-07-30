/** Port của avatar/convert/ItemConverter.java. */
import { Item } from '../item/Item.js';

class ItemConverter {
  static getInstance() {
    return itemConverter;
  }

  newItem(oldItem) {
    return Item.builder()
      .id(oldItem.getId())
      .quantity(oldItem.getQuantity())
      .expired(oldItem.getExpired())
      .build();
  }
}

export const itemConverter = new ItemConverter();
export { ItemConverter };
export default itemConverter;
