/** Port của avatar/server/UserManager.java */
class UserManager {
  constructor() {
    /** @type {Array<any>} danh sách User đang online */
    this.users = [];
  }

  add(us) {
    this.users.push(us);
  }

  remove(us) {
    const i = this.users.indexOf(us);
    if (i >= 0) this.users.splice(i, 1);
  }

  find(id) {
    return this.users.find((u) => u.getId?.() === id || u.id === id) || null;
  }

  findByName(name) {
    // Bản Java so sánh bằng == (lỗi), ở đây so sánh giá trị cho đúng ý định
    return this.users.find((u) => (u.getUsername?.() ?? u.username) === name) || null;
  }
}

export const userManager = new UserManager();
export default userManager;
