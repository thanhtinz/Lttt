/**
 * Port của avatar/model/BoardInfo.java.
 * Java để field public nên code khác ghi trực tiếp (`board.isPlaying = false`).
 * NOTE: JS không cho vừa có field `isPlaying` vừa có method `isPlaying()`;
 * giữ field (giống Java public field) ⇒ đọc bằng `board.isPlaying`, ghi bằng
 * `board.isPlaying = v` hoặc `setPlaying(v)`.
 */
export class BoardInfo {
  constructor() {
    this.boardID = 0;   // byte
    this.nPlayer = 0;   // byte
    this.maxPlayer = 0; // byte
    this.isPass = false;
    this.isPlaying = false;
    this.money = 0;
    this.strMoney = null;
    this.lstUsers = []; // Khởi tạo danh sách người dùng
  }

  getLstUsers() {
    return this.lstUsers;
  }

  setPlaying(isPlaying) {
    this.isPlaying = isPlaying;
  }

  setMoney(Money) {
    this.money = Money | 0;
  }

  getMoney() {
    return this.money;
  }

  // Phương thức để cập nhật danh sách người chơi
  setLstUsers(lstUsers) {
    this.lstUsers = lstUsers;
    this.nPlayer = (lstUsers.length << 24) >> 24; // Cập nhật số người chơi (byte)
  }
}

export default BoardInfo;
