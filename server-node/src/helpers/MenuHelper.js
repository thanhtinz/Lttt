/** Port của avatar/helpers/MenuHelper.java (@Getter @Setter). */
export class MenuHelper {
  constructor() {
    this.title = null;
    this.subMenus = null;
    this.action = 0;
  }

  getTitle() { return this.title; }
  setTitle(v) { this.title = v; }
  getAction() { return this.action; }
  setAction(v) { this.action = v | 0; }
  setSubMenus(v) { this.subMenus = v; }

  addAction(action) {
    this.action = action | 0;
  }

  addSubMenu(subMenu) {
    this.subMenus.push(subMenu);
  }

  getSubMenus() {
    return this.subMenus;
  }
}

export default MenuHelper;
