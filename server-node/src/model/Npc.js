/**
 * Port của avatar/model/Npc.java (extends User).
 * NOTE: model/User.js chưa được port ở tầng này ⇒ nạp muộn, nếu chưa có thì
 * dùng lớp rỗng làm base để file vẫn import sạch; khi User.js có mặt Npc sẽ
 * kế thừa đúng như bản Java.
 */
import { User } from './User.js';

/** Thay cho Thread.sleep — timer nền nên unref. */
function sleep(ms) {
  return new Promise((resolve) => {
    const t = setTimeout(resolve, ms);
    if (t.unref) t.unref();
  });
}

export class Npc extends User {
  static ID_ADD = 2000000000;

  /** Java: @Builder Npc(int id, String name, short x, short y, ArrayList<Item> wearing) */
  constructor(id = 0, name = null, x = 0, y = 0, wearing = null) {
    super();
    this.textChats = null;
    this.setId(id > Npc.ID_ADD ? id : id + Npc.ID_ADD);
    this.setUsername(name);
    this.setRole(0);
    this.setX((x << 16) >> 16);
    this.setY((y << 16) >> 16);
    this.setWearing(wearing);
    this.textChats = [];
    if (id === 864) {
      this._autoChatBotSpeed();
    } else {
      this._autoChatBot();
    }
  }

  getTextChats() { return this.textChats; }
  setTextChats(v) { this.textChats = v; }

  /** Thread autoChatBot: chat mỗi 6s */
  async _autoChatBot() {
    for (;;) {
      try {
        for (const text of this.textChats) {
          this.getMapService().chat(this, text);
          await sleep(6000);
        }
        if (this.textChats == null || this.textChats.length === 0) {
          await sleep(10000);
        }
      } catch (ignored) {
        // InterruptedException ignored
      }
    }
  }

  /** Thread autoChatBotSpeed: chat mỗi 500ms */
  async _autoChatBotSpeed() {
    for (;;) {
      try {
        for (const text of this.textChats) {
          this.getMapService().chat(this, text);
          await sleep(500);
        }
        if (this.textChats == null || this.textChats.length === 0) {
          await sleep(1000);
        }
      } catch (ignored) {
        // InterruptedException ignored
      }
    }
  }

  addChat(chat) {
    this.textChats.push(chat);
  }

  sendMessage(ms) {
    // Java: rỗng
  }

  static builder() {
    const f = {};
    const b = { build: () => new Npc(f.id, f.name, f.x, f.y, f.wearing) };
    for (const k of ['id', 'name', 'x', 'y', 'wearing']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default Npc;
