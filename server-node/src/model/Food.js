/** Port của avatar/model/Food.java (@AllArgsConstructor @Builder @Getter). */
export class Food {
  constructor(id = 0, name = null, description = null, shop = 0, icon = 0, price = 0, percentHelth = 0) {
    this.id = id | 0;
    this.name = name;
    this.description = description;
    this.shop = shop | 0;
    this.icon = icon | 0;
    this.price = price | 0;
    this.percentHelth = percentHelth | 0;
  }

  getId() { return this.id; }
  getName() { return this.name; }
  getDescription() { return this.description; }
  getShop() { return this.shop; }
  getIcon() { return this.icon; }
  getPrice() { return this.price; }
  getPercentHelth() { return this.percentHelth; }

  static builder() {
    const f = {};
    const b = {
      build: () => new Food(f.id, f.name, f.description, f.shop, f.icon, f.price, f.percentHelth),
    };
    for (const k of ['id', 'name', 'description', 'shop', 'icon', 'price', 'percentHelth']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default Food;
