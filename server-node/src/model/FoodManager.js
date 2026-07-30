/** Port của avatar/model/FoodManager.java — nạp danh sách món ăn từ DB. */
import { dbManager } from '../db/DbManager.js';
import { Food } from './Food.js';

class FoodManager {
  constructor() {
    this.foods = [];
  }

  static getInstance() {
    return foodManager;
  }

  getFoods() { return this.foods; }

  async load() {
    this.foods.length = 0;
    try {
      const rows = await dbManager.query('SELECT * FROM foods;');
      for (const rs of rows) {
        const id = rs.id | 0;
        const name = rs.name;
        const description = rs.description;
        const img = rs.img | 0;
        const shop = rs.shop | 0;
        const percentHealth = rs.percent_health | 0;
        const price = rs.price | 0;
        const food = Food.builder()
          .id(id)
          .name(name)
          .description(description)
          .shop(shop)
          .icon(img)
          .percentHelth(percentHealth)
          .price(price)
          .build();
        this.foods.push(food);
      }
    } catch (ex) {
      console.error('load foods err ', ex);
    }
  }

  findFoodByFoodID(id) {
    for (const food of this.foods) {
      if (food.getId() === id) {
        return food;
      }
    }
    return null;
  }
}

export const foodManager = new FoodManager();
export { FoodManager };
export default foodManager;
