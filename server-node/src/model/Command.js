/** Port của avatar/model/Command.java (@Getter @Setter). */
export class Command {
  /**
   * Java có 2 constructor:
   *  Command(String name, int icon)
   *  Command(short anthor, String name, int icon, byte type)
   * Node không nạp chồng được ⇒ phân biệt theo số lượng/kiểu tham số.
   */
  constructor(a, b, c, d) {
    this.icon = 0;
    this.name = null;
    this.anthor = 0;
    this.type = 0;
    if (arguments.length >= 4 || typeof a === 'number') {
      // Command(anthor, name, icon, type)
      this.anthor = (a << 16) >> 16; // short
      this.name = b;
      this.icon = c | 0;
      this.type = (d << 24) >> 24;   // byte
    } else {
      // Command(name, icon)
      this.name = a;
      this.icon = b | 0;
    }
  }

  getIcon() { return this.icon; }
  setIcon(v) { this.icon = v | 0; }
  getName() { return this.name; }
  setName(v) { this.name = v; }
  getAnthor() { return this.anthor; }
  setAnthor(v) { this.anthor = (v << 16) >> 16; }
  getType() { return this.type; }
  setType(v) { this.type = (v << 24) >> 24; }
}

export default Command;
