/** Port của avatar/model/Menu.java (@Getter @Setter @Builder). */
export class Menu {
  constructor(id = 0, name = null, menus = null, action = null, npcName = null, npcChat = null) {
    this.id = id | 0;
    this.name = name;
    this.menus = menus;
    this.action = action; // Java Runnable → function
    this.npcName = npcName;
    this.npcChat = npcChat;
  }

  getId() { return this.id; }
  setId(v) { this.id = v | 0; }
  getName() { return this.name; }
  setName(v) { this.name = v; }
  getMenus() { return this.menus; }
  setMenus(v) { this.menus = v; }
  getAction() { return this.action; }
  setAction(v) { this.action = v; }
  getNpcName() { return this.npcName; }
  setNpcName(v) { this.npcName = v; }
  getNpcChat() { return this.npcChat; }
  setNpcChat(v) { this.npcChat = v; }

  addMenu(menu) {
    this.menus.push(menu);
  }

  isMenu() {
    return this.menus != null && this.menus.length > 0;
  }

  perform() {
    if (this.action != null) {
      this.action();
    }
  }

  static builder() {
    const f = {};
    const b = { build: () => new Menu(f.id, f.name, f.menus, f.action, f.npcName, f.npcChat) };
    for (const k of ['id', 'name', 'menus', 'action', 'npcName', 'npcChat']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default Menu;
