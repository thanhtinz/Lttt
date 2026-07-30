/** Port của avatar/message/CasinoMsgHandler.java */
import { Cmd } from '../constants/Cmd.js';
import { Message } from '../net/Message.js';
import boardManager from '../server/BoardManager.js';
import { Utils } from '../server/Utils.js';
import { BauCuaMsgHandler } from './minigame/BauCuaMsgHandler.js';
import { MessageHandler } from './MessageHandler.js';

/** Thread.sleep → chờ bằng Promise. */
function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export class CasinoMsgHandler extends MessageHandler {
  constructor(client) {
    super(client);
    /** Java: private BauCuaMsgHandler service (bộ xử lý minigame con). */
    this.service = new BauCuaMsgHandler(client);
    // Giữ thêm tên `miniGameMessageHandler` cho cùng một đối tượng
    this.miniGameMessageHandler = this.service;
  }

  async onMessage(mss) {
    try {
      console.log('casino mess: ' + mss.getCommand());
      switch (mss.getCommand()) {
        case 61:
          await this.service.joinCasino(mss);
          break;
        case Cmd.REQUEST_ROOMLIST:
          await this.requestRoomList();
          break;
        case Cmd.GET_IMG_ICON: {
          if (this.client.user != null) {
            await this.client.doGetImgIcon(mss);
            break;
          }
        }
        // NOTE: giữ nguyên hành vi bản Java (thiếu `break` khi user == null ⇒
        // rơi xuống case REQUEST_BOARDLIST)
        case Cmd.REQUEST_BOARDLIST:
          await this.BoardList(mss);
          break;
        case Cmd.JOIN_BOARD:
          await this.joinBoard(mss, this.client.user);
          break;
        case Cmd.CHAT_TO_BOARD:
          await this.chatToBoard(mss);
          break;
        case Cmd.LEAVE_BOARD:
          await this.leaveBoard(mss, this.client.user);
          break;
        case Cmd.START:
          await this.Start(mss);
          break;
        case Cmd.READY:
          await this.Ready(mss, this.client.user);
          break;
        case Cmd.TO_XONG:
          await this.toXong(mss, this.client.user);
          break;
        case 65:
          await this.haPhom(mss, this.client.user);
          break;
        case 49:
          await this.Skip(mss, this.client.user);
          break;
        case Cmd.SET_MONEY:
          await this.setMoney(mss, this.client.user);
          break;
        default:
          console.log('casino mess: ' + mss.getCommand());
          break;
      }
    } catch (e) {
      console.error(e);
    }
  }

  async requestRoomList() { // ms 6
    const ms = new Message(Cmd.REQUEST_ROOMLIST);
    const ds = ms.writer();

    for (let i = 0; i < 3; i++) {
      ds.writeByte(43 + i); // id
      ds.writeByte(1);      // roomfree//vàng 0 đỏ 2 xanh
      ds.writeByte(0 + i);  // roomWait
      ds.writeByte(0 + i);  // lv
    }
    ds.flush();
    await this.client.user.sendMessage(ms);
  }

  async BoardList(ms) { // ms 7
    const id = ms.reader().readByte();
    ms = new Message(Cmd.REQUEST_BOARDLIST);
    const ds = ms.writer();
    ds.writeByte(id);

    const boardInfos = boardManager.boardList;

    for (const a of boardInfos) {
      ds.writeByte(a.boardID);
      ds.writeByte(a.nPlayer);
      if (a.isPass) { ds.writeByte(1); } else { ds.writeByte(0); }
      ds.writeInt(a.getMoney());
    }
    ds.flush();

    await this.client.user.sendMessage(ms);
  }

  async chatToBoard(ms) { // ms 7
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const text = ms.reader().readUTF();
    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();

    ms = new Message(Cmd.CHAT_TO_BOARD);
    const ds = ms.writer();
    ds.writeByte(roomID);
    ds.writeByte(boardID);
    ds.writeByte(this.client.user.getId()); // lv
    ds.writeUTF(text);
    ds.flush();

    for (const u of BoardUs) {
      await u.sendMessage(ms);
    }
    // this.client.user.sendMessage(ms);
  }

  async joinBoard(ms, us) { // ms 8
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const pass = ms.reader().readUTF();
    if (this.client.isResourceHD()) {
      this.client.user.getAvatarService().serverDialog('error 0011');
      return;
    }
    const board = boardManager.find(boardID);

    boardManager.increaseMaxPlayer(boardID, roomID, us);
    const BoardUs = board.getLstUsers();

    if (BoardUs.length > 1) {
      for (let i = 0; i < BoardUs.length; i++) {
        const ms1 = new Message(Cmd.SOMEONE_JOINBOARD);
        const ds1 = ms1.writer();
        ds1.writeByte(BoardUs.indexOf(us)); // seat // vi tri
        ds1.writeInt(us.getId());           // seat // vi tri
        ds1.writeUTF(us.getUsername());     // seat // vi tri
        ds1.writeInt(0);                    // tien

        ds1.writeByte(us.getWearing().length); // Số phần mặc
        for (const item of us.getWearing()) {
          ds1.writeShort(item.getId()); // ID item
        }
        ds1.writeInt(0); // tien
        ds1.writeInt(1); // tien
        ds1.flush();
        await BoardUs[i].session.sendMessage(ms1);

        for (let j = 1; j < BoardUs.length; j++) {
          ms = new Message(Cmd.READY); // 16
          const ds = ms.writer();

          ds.writeInt(BoardUs[i].getId());
          ds.writeBoolean(false);
          ds.flush();

          for (const u of BoardUs) {
            await u.session.sendMessage(ms);
          }
        }
      }
    }

    ms = new Message(Cmd.JOIN_BOARD);
    const ds = ms.writer();

    ds.writeByte(roomID);
    ds.writeByte(boardID);
    ds.writeInt(BoardUs[0].getId()); // ID user
    ds.writeInt(board.getMoney());   // số tiền cược ở phòng

    for (const user of BoardUs) {
      ds.writeInt(user.getId());      // IDDB
      ds.writeUTF(user.getUsername()); // Username
      // NOTE: giữ nguyên hành vi bản Java (ghi xeng của `us`, không phải `user`)
      ds.writeInt(us.getXeng());      // Số tiền của user
      ds.writeByte(user.getWearing().length); // Số phần mặc
      for (const item of user.getWearing()) {
        ds.writeShort(item.getId()); // ID item
      }

      ds.writeInt(10);                 // Kinh nghiệm
      ds.writeBoolean(false);          // Trạng thái sẵn sàng
      ds.writeShort(user.getIdImg());  // ID hình ảnh
    }

    for (let i = BoardUs.length; i < 5; i++) {
      ds.writeInt(-1); // IDDB placeholder for empty slots
    }

    ds.flush();
    await this.client.user.sendMessage(ms);

    if (board.isPlaying) {
      const ms2 = new Message(Cmd.PLAYING);
      const ds2 = ms2.writer();
      ds2.writeByte(roomID);
      ds2.writeByte(boardID);
      ds2.writeByte(10);
      ds2.flush();
      await us.getSession().sendMessage(ms2);
      us.setToXong(true);
      us.setHaPhom(true);
    }
  }

  async leaveBoard(ms, us) {
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const board = boardManager.find(boardID);

    let BoardUs = board.getLstUsers();
    if (board.isPlaying && BoardUs.length > 1) {
      us.setToXong(true);
      us.setHaPhom(true);
      for (const user of BoardUs) {
        if (!user.isHaPhom()) {
          console.log('Gửi Lượt Hạ Phỏm ' + user.getUsername());
          await this.setTurn(BoardUs, user, roomID, boardID, BoardUs.indexOf(user));
          board.nPlayer--;
          let updatedBoardUs = [...BoardUs];
          const idx = updatedBoardUs.indexOf(us);
          if (idx >= 0) updatedBoardUs.splice(idx, 1);
          board.setLstUsers(updatedBoardUs);
          BoardUs = updatedBoardUs;
          ms = new Message(Cmd.SOMEONE_LEAVEBOARD); // 14
          const ds = ms.writer();
          ds.writeInt(us.getId());
          ds.writeInt(BoardUs[0].getId());
          for (const user1 of board.getLstUsers()) {
            await user1.getSession().sendMessage(ms);
          }

          return;
        }
      }
      await this.gameResult(roomID, boardID);
    }

    board.nPlayer--;
    const updatedBoardUs = [...BoardUs];
    const idx = updatedBoardUs.indexOf(us);
    if (idx >= 0) updatedBoardUs.splice(idx, 1);
    board.setLstUsers(updatedBoardUs);
    BoardUs = updatedBoardUs;

    if (BoardUs.length === 0) {
      return;
    }

    ms = new Message(Cmd.SOMEONE_LEAVEBOARD); // 14
    const ds = ms.writer();
    ds.writeInt(us.getId());
    ds.writeInt(BoardUs[0].getId());
    for (const user of board.getLstUsers()) {
      await user.getSession().sendMessage(ms);
    }

    if (board.isPlaying && BoardUs.length === 1) {
      const ms3 = new Message(Cmd.FINISH);
      const ds3 = ms3.writer();
      ds3.writeByte(roomID);
      ds3.writeByte(boardID);
      for (let i = 0; i < 5; i++) {
        ds3.writeInt(0);
      }
      ds3.flush();
      await BoardUs[0].session.sendMessage(ms3);
    }
  }

  async Ready(ms, us) { // ms 20
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const isReady = ms.reader().readBoolean();

    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();

    ms = new Message(Cmd.READY); // 16
    const ds = ms.writer();

    ds.writeInt(us.getId());
    ds.writeBoolean(isReady);
    ds.flush();

    for (const u of BoardUs) {
      await u.session.sendMessage(ms);
    }
  }

  async Start(ms) { // 20
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();
    for (const u of BoardUs) {
      const moneyPutList = u.getMoneyPutList();
      moneyPutList.length = 0;
      u.setHaPhom(false);
      u.setToXong(false);
      u.getMoneyPutList().length = 0;
    }
    BoardUs[0].setToXong(true);
    BoardUs[0].setHaPhom(true);
    board.setPlaying(true);

    ms = new Message(Cmd.START); // 20
    const ds = ms.writer();

    ds.writeByte(roomID);
    ds.writeByte(boardID);
    ds.writeByte(10); // ID user hoặc ID bàn
    ds.flush();

    for (const user of BoardUs) {
      await user.session.sendMessage(ms);
    }
  }

  async toXong(ms, us) { // ms 21
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();

    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();
    const moneyPutList = us.getMoneyPutList();

    // Nếu chưa có putlist thì thêm putlist mới
    if (us.getMoneyPutList().length <= 0 && BoardUs.indexOf(us) !== 0) {
      while (ms.reader().available() > 0) {
        const moneyPut = ms.reader().readByte();
        moneyPutList.push(moneyPut);
      }
      us.setMoneyPutList(moneyPutList);
      ms = new Message(Cmd.TO_XONG);
      const ds = ms.writer();
      ds.writeByte(roomID);
      ds.writeByte(boardID);
      ds.writeByte(BoardUs.indexOf(us));
      for (const moneyPut of moneyPutList) {
        ds.writeByte(moneyPut);
      }
      ds.flush();
      await us.getSession().sendMessage(ms);
      us.setToXong(true);
      console.log(us.getUsername() + ' đã đặt xong ');

      for (const user of BoardUs) {
        await user.session.sendMessage(ms);
      }
    }

    // Kiểm tra xem tất cả người chơi đã "to xong" chưa
    let allToXong = true;
    for (const user of BoardUs) {
      if (!user.isToXong()) {
        console.log(user.getUsername() + ' chưa to xong ');
        allToXong = false;
        break; // Thoát khỏi vòng lặp nếu có một người chưa to xong
      }
    }

    if (allToXong) {
      for (const user of BoardUs) {
        if (!user.isHaPhom()) {
          console.log('luot ta => ' + user.getUsername());
          await this.setTurn(BoardUs, user, roomID, boardID, BoardUs.indexOf(user));
          return;
        }
      }
    }
  }

  async haPhom(ms, us) { // ms 65
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const indexFrom = ms.reader().readByte();
    const indexTo = ms.reader().readByte();

    console.log('indexFrom: ' + indexFrom);
    console.log('indexTo: ' + indexTo);

    const board1 = boardManager.find(boardID);
    const boardUsers = board1.getLstUsers();

    // Lấy danh sách tiền đã đặt của người chơi hiện tại (us)
    const userMoneyPutList = us.getMoneyPutList();
    console.log('Initial Money Put List for ' + us.getUsername() + ': ' + userMoneyPutList);

    // Tổng số tiền cược từ indexFrom đến indexTo
    let totalSum = 0;

    // Cộng dồn tổng tiền từ indexFrom đến indexTo
    if (indexFrom <= indexTo) {
      for (let i = indexFrom; i <= indexTo; i++) {
        totalSum += userMoneyPutList[i];
      }
    } else {
      for (let i = indexFrom; i >= indexTo; i--) {
        totalSum += userMoneyPutList[i];
      }
    }

    console.log('Total Sum from indexFrom (' + indexFrom + ') to indexTo (' + indexTo + '): ' + totalSum);

    // Cập nhật danh sách putMoneyList của người chơi us tại indexTo (ép về byte như Java)
    userMoneyPutList[indexTo] = (((userMoneyPutList[indexTo] + totalSum) | 0) << 24) >> 24;

    // Đặt lại tiền ở indexFrom về 0, giữ nguyên tiền ở các vị trí khác
    userMoneyPutList[indexFrom] = 0;

    console.log('Updated Money Put List for ' + us.getUsername() + ': ' + userMoneyPutList);

    ms = new Message(Cmd.HA_PHOM); // 65
    const ds = ms.writer();
    ds.writeByte(roomID);
    ds.writeByte(boardID);

    ds.writeByte(boardUsers.indexOf(us)); // Ghi lại chỉ số của người chơi hiện tại
    ds.writeByte(indexFrom);              // Ghi giá trị indexFrom
    ds.writeByte(indexTo);
    ds.writeByte(userMoneyPutList[indexTo]);
    ds.flush();

    console.log(us.getUsername() + 'đã Hạ Phỏm thành công ');
    for (const user of boardUsers) {
      await user.getSession().sendMessage(ms);
    }

    for (const user of boardUsers) {
      if (!user.isHaPhom()) {
        console.log('Gửi Lượt Hạ Phỏm ' + user.getUsername());
        await this.setTurn(boardUsers, user, roomID, boardID, boardUsers.indexOf(user));
        return;
      }
    }
    await this.gameResult(roomID, boardID);
  }

  async Skip(ms, us) { // ms 6
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    // us.getService().serverDialog("skip dang xay dung");
    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();
    us.setHaPhom(true);
    // NOTE: giữ nguyên hành vi bản Java (tạo message HA_PHOM rồi không dùng)
    ms = new Message(Cmd.HA_PHOM); // 65
    const ds = ms.writer();
    ds.writeByte(roomID);
    ds.writeByte(boardID);

    for (const user of BoardUs) {
      if (!user.isHaPhom()) {
        console.log('Gửi Lượt Hạ Phỏm ' + user.getUsername());
        await this.setTurn(BoardUs, user, roomID, boardID, BoardUs.indexOf(user));
        return;
      }
    }
    await this.gameResult(roomID, boardID);
  }

  async setTurn(lstus, us, roomID, boardID, index) {
    const ms1 = new Message(Cmd.SET_TURN);
    const ds1 = ms1.writer();
    ds1.writeByte(roomID);
    ds1.writeByte(boardID);
    ds1.writeByte(index);
    ds1.flush();
    await us.getSession().sendMessage(ms1);
    us.setHaPhom(true);
    console.log('Người chơi ' + us.getUsername() + ' đang hạ phỏm');
    for (const user1 of lstus) {
      await user1.session.sendMessage(ms1);
    }
  }

  async gameResult(roomID, boardID) {
    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();
    console.log('game resutl');
    const ms1 = new Message(Cmd.GAME_RESULT);

    const ds1 = ms1.writer();
    ds1.writeByte(roomID);
    ds1.writeByte(boardID);

    for (let i = 0; i < 3; i++) {
      const xn = Utils.nextInt(5);
      ds1.writeByte(xn);
    }

    ds1.flush();
    for (const user of BoardUs) {
      await user.getSession().sendMessage(ms1);
    }

    await sleep(2500);
    const ms2 = new Message(Cmd.WIN);
    const ds2 = ms2.writer();
    ds2.writeByte(roomID);
    ds2.writeByte(boardID);
    // NOTE: giữ nguyên hành vi bản Java (ghi dồn vào cùng 1 message trong vòng lặp)
    for (const user of BoardUs) {
      ds2.writeByte(BoardUs.indexOf(user));
      ds2.writeByte(1);
      ds2.writeByte(999); // money
      ds2.flush();
      await user.getSession().sendMessage(ms2);
    }

    const ms3 = new Message(Cmd.FINISH);
    const ds3 = ms3.writer();
    ds3.writeByte(roomID);
    ds3.writeByte(boardID);
    for (let i = 0; i < 5; i++) {
      ds3.writeInt(999);
    }
    ds3.flush();
    for (const user of BoardUs) {
      await user.getSession().sendMessage(ms3);
    }

    board.setPlaying(false);
  }

  async setMoney(ms, us) {
    const roomID = ms.reader().readByte();
    const boardID = ms.reader().readByte();
    const board = boardManager.find(boardID);
    const BoardUs = board.getLstUsers();

    const Money = ms.reader().readInt();
    const msOut = new Message(Cmd.SET_MONEY);

    if (us.getXeng() < Money * 48) {
      // NOTE: giữ nguyên hành vi bản Java (xengValue/result tính rồi không dùng)
      const xengValue = us.getXeng();
      const result = Math.trunc(xengValue / 50);
      us.getAvatarService().serverDialog('Để đặt được ' + Money + ' thì bạn cần có số tiền là ' + (Money * 48));
      return;
    }
    if (Money > 100000) {
      us.getAvatarService().serverDialog('Vui lòng đặt nhỏ hơn 100.000');
      return;
    }
    // NOTE: giữ nguyên hành vi bản Java (lấy writer trong vòng lặp ⇒ dữ liệu dồn lại)
    for (const user of BoardUs) {
      board.setMoney(Money);
      const ds = msOut.writer();
      ds.writeByte(roomID);
      ds.writeByte(boardID);
      ds.writeInt(Money);
      await user.getSession().sendMessage(msOut);
    }
  }
}

export default CasinoMsgHandler;
