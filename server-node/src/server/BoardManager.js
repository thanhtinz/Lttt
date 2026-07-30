/** Port của avatar/server/BoardManager.java */
import { BoardInfo } from '../model/BoardInfo.js';

export class BoardManager {

  static getInstance() {
    return boardManager;
  }

  constructor() {
  }

  find(id) {
    for (const board of BoardManager.boardList) {
      if (board.boardID === id) {
        return board;
      }
    }
    return null; // Trả về null nếu không tìm thấy
  }

  increaseMaxPlayer(id, roomID, user) {
    for (const board of BoardManager.boardList) {
      if (board.boardID === id) {
        let userExists = false;
        for (const existingUser of board.lstUsers) {
          if (existingUser.getId() === user.getId()) {
            userExists = true;
            break;
          }
        }
        if (!userExists) {
          board.nPlayer += 1;
          board.lstUsers.push(user);
          user.setRoomID(roomID);
        }
      }
    }
  }

  remove(us) {
    const idx = BoardManager.users.indexOf(us);
    if (idx >= 0) {
      BoardManager.users.splice(idx, 1);
    }
  }

  initBoards() {
    for (let i = 0; i < 6; i++) {
      const board = new BoardInfo();
      board.boardID = (i << 24) >> 24;
      board.nPlayer = 80; // số ng chia 16
      board.maxPlayer = 5; // Đặt maxPlayer là 5
      board.isPass = false;
      board.isPlaying = false;
      board.money = 0;
      board.strMoney = '1000';
      BoardManager.boardList.push(board);
      console.log('create board : ' + board.boardID);
    }
  }

  findUserBoard(user) {
    for (const board of BoardManager.boardList) {
      if (board.lstUsers.includes(user)) {
        return board; // Trả về bàn nếu người dùng đang tham gia
      }
    }
    return null; // Trả về null nếu người dùng không tham gia bàn nào
  }

  // NOTE: giữ nguyên hành vi bản Java (equals() so sánh user.getId() với chính nó -> luôn true)
  equals(o) {
    if (this === o) return true;
    if (o == null || this.constructor !== o.constructor) return false;
    const user = o;
    return user.getId() === user.getId();
  }
}

BoardManager.boardList = [];
BoardManager.users = [];

export const boardManager = new BoardManager();
export default boardManager;
