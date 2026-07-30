// Port của avatar/minigame/TaiXiu.java
import { dbManager } from '../db/DbManager.js';
import { userManager } from '../server/UserManager.js';
import { Utils } from '../server/Utils.js';

// MessageFormat mặc định format số có dấu phân cách nhóm ⇒ giữ nguyên định dạng đó
function fmtNum(n) {
  return Number(n).toLocaleString('en-US');
}

// Java in double dạng "50.0" ⇒ mô phỏng khi nối chuỗi
function fmtDouble(d) {
  return Number.isInteger(d) ? d.toFixed(1) : String(d);
}

// Lớp đại diện cho cược của người chơi
class Bet {
  constructor(id, user, betType, currency, amount) {
    this.id = id;
    this.user = user;
    this.betType = betType;
    this.currency = currency;
    this.amount = amount;
  }

  getId() {
    return this.id;
  }

  getUser() {
    return this.user;
  }

  getBetType() {
    return this.betType;
  }

  getCurrency() {
    return this.currency;
  }

  getAmount() {
    return this.amount;
  }
}

export class TaiXiu {
  static instance = null; // Singleton instance

  static GAME_DURATION_SECONDS = 40;
  static RESULT_DISPLAY_DURATION_SECONDS = 5;

  static getInstance() {
    if (TaiXiu.instance == null) {
      TaiXiu.instance = new TaiXiu();
    }
    return TaiXiu.instance;
  }

  constructor() {
    this.TaiXiu = [];
    this.gameId = 0;
    this.countdown = 40;
    this._countdownTimer = null;
    // Java: this.gameId = getLastGameId(); (đồng bộ) — ở Node phải chờ DB
    this.initPromise = this.getLastGameId()
      .then((id) => { this.gameId = id; })
      .catch((e) => { console.error(e); });
  }

  // Thêm một NPC vào danh sách
  setNpcTaiXiu(npc) {
    if (npc != null && this.TaiXiu.length === 0) {  // Đảm bảo chỉ có 1 NPC
      this.TaiXiu.push(npc);
      console.log('NPC đã được thêm vào TaiXiu, kích thước hiện tại: ' + this.TaiXiu.length);
      // Bắt đầu luồng autoChat khi đã thêm NPC (chờ lấy xong gameId cuối)
      this.initPromise.then(() => this.startCountdown());
    }
  }

  getNpcTaiXiu() {
    return [...this.TaiXiu];
  }

  // Java: new Thread(...) + Thread.sleep(1000) ⇒ setInterval 1s (rule 9)
  startCountdown() {
    const startTime = Date.now();
    const tick = () => {
      try {
        if (this.countdown > 0) {
          const elapsedSeconds = Math.trunc((Date.now() - startTime) / 1000);
          this.countdown = 40 - elapsedSeconds;
          this.updateNpcChat(this.countdown);
          return;
        }
        // Hết vòng lặp while(countdown > 0) của bản Java
        if (this._countdownTimer != null) {
          clearInterval(this._countdownTimer);
          this._countdownTimer = null;
        }
        this.handleEndGame() // Kết thúc game, trả kết quả
          .then(() => this.startNewGame()) // Bắt đầu phiên mới
          .catch((e) => { console.error(e); });
      } catch (e) {
        console.error(e);
      }
    };
    tick(); // lần cập nhật đầu tiên (Java cập nhật trước sleep đầu tiên)
    this._countdownTimer = setInterval(tick, 1000);
    if (this._countdownTimer.unref) this._countdownTimer.unref();
  }

  // Cập nhật chat của NPC với thời gian còn lại
  updateNpcChat(countdown) {
    for (const npc of this.TaiXiu) {
      npc.setTextChats(['Phiên id : ' + fmtNum(this.gameId) + '. Thời gian còn lại: ' + fmtNum(countdown) + ' giây']);
    }
  }

  // Bắt đầu phiên cược mới
  async startNewGame() {
    this.countdown = 40;
    this.gameId++;

    await this.saveGameRoundResult(this.gameId, 'Pending');
    console.log('Bắt đầu phiên mới với ID: ' + this.gameId);
    this.startCountdown();
  }

  // Xử lý khi hết thời gian, tính toán và trả kết quả cho người chơi
  async handleEndGame() {
    const result = await this.calculateResult();
    const bets = await this.getAllBetsForGame(this.gameId);
    await this.endGame(result);
    const preStart = Date.now();
    let preCountdown = TaiXiu.RESULT_DISPLAY_DURATION_SECONDS;
    if (bets == null || bets.length === 0) {
      console.log('Không có cược nào cho phiên ' + this.gameId);
    } else {
      for (const bet of bets) {
        const user = bet.getUser();
        if (user != null) { // Kiểm tra user không null
          const username = user.getUsername();
          const betType = bet.getBetType();
          const currency = bet.getCurrency();
          const betAmount = bet.getAmount();

          // In thông tin chi tiết của từng người chơi
          console.log('Người chơi: ' + username);
          console.log('Loại cược: ' + betType);
          console.log('Tiền cược: ' + betAmount + ' ' + currency);

          if (result.includes(bet.getBetType())) {
            // Trả thưởng nếu thắng
            const reward = betAmount * 2 * 0.95;
            this.updateBalance(user, currency, Math.trunc(reward)); // Thêm tiền vào tài khoản người chơi
            user.getAvatarService().serverDialog('Chúc mừng ' + username + '! Bạn đã thắng cược và nhận được ' + Math.trunc(reward) + ' ' + currency + '.');
            await this.updateBetStatus(bet.getId(), 'Win');
            Utils.writeLog(user, 'Chúc mừng ' + username + '! Bạn đã thắng cược và nhận được ' + Math.trunc(reward) + ' ' + currency + '.' + result);
            Utils.writeLog(user, 'tiền XU tx:' + user.getXu() + 'Lượng tx' + user.getLuong());
            try {
              await user.getAvatarService().SendTabmsg(' bạn đã thắng ' + betType + ' ' + Math.trunc(reward));
            } catch (e) { }
          } else {
            user.getAvatarService().serverDialog('Rất tiếc, ' + username + ', bạn đã thua cược.');
            await this.updateBetStatus(bet.getId(), 'Lose');
            Utils.writeLog(user, 'Rất tiếc, ' + username + ', bạn đã thua cược.' + result);
            Utils.writeLog(user, 'tiền XU tx:' + user.getXu() + 'Lượng tx' + user.getLuong());
            try {
              await user.getAvatarService().SendTabmsg(' bạn đã thua ' + betType + ' ' + currency);
            } catch (e) { }
          }
        } else {
          console.log('Lỗi: Không thể tìm thấy người chơi cho cược ID: ' + bet.getId());
        }
      }
    }

    // Cập nhật kết quả và đếm ngược hiển thị (Java: while + Thread.sleep(1000))
    await new Promise((resolve) => {
      const showChat = () => {
        if (this.getNpcTaiXiu().length !== 0) {
          const npc = this.TaiXiu[0];
          npc.setTextChats([
            'Kết quả: ' + result + ', Ván mới sẽ bắt đầu sau: ' + fmtNum(preCountdown) + ' giây'
          ]);
        }
      };
      showChat();
      const t = setInterval(() => {
        preCountdown = TaiXiu.RESULT_DISPLAY_DURATION_SECONDS - Math.trunc((Date.now() - preStart) / 1000);
        if (preCountdown > 0) {
          showChat();
        } else {
          clearInterval(t);
          resolve();
        }
      }, 1000);
      if (t.unref) t.unref();
    });

    //clearBetsForGame(gameId); // Xóa cược cho ván hiện tại
    await this.updateGameRewardStatus(this.gameId); // Cập nhật trạng thái trả thưởng

    console.log('Kết quả phiên ' + this.gameId + ': ' + result);
  }

  // Tính kết quả ngẫu nhiên cho phiên cược
  async calculateResult() {
    const dice1 = Utils.nextInt(6) + 1;
    const dice2 = Utils.nextInt(6) + 1;
    const dice3 = Utils.nextInt(6) + 1;
    const total = dice1 + dice2 + dice3;
    await this.endGame('khóa');
    console.log('khóa cuoc');
    const result = (total >= 11 && total <= 18) ? 'Tài' : 'Xỉu';
    return ' ' + dice1 + ', ' + dice2 + ', ' + dice3 + ' ' + total + ' ' + result;
  }

  async handleBetWithInput(us, menuId, userId, text) {
    // Java: Integer.parseInt(text) ném NumberFormatException nếu không phải số
    if (!/^[+-]?\d+$/.test(String(text).trim())) {
      us.getAvatarService().serverDialog('Vui lòng nhập một số hợp lệ.');
      return;
    }
    const betAmount = parseInt(String(text).trim(), 10) | 0; // Chuyển text thành số nguyên để lấy số cược

    // Kiểm tra số tiền cược hợp lệ
    if (betAmount <= 0) {
      us.getAvatarService().serverDialog('Vui lòng nhập số tiền cược hợp lệ.');
      return;
    }

    let betType = ''; // Loại cược: "Tài" hoặc "Xỉu"
    let currency = ''; // Loại tiền tệ: "Xu" hoặc "Lượng"

    // Xác định loại cược và loại tiền tệ dựa trên menuId
    switch (menuId) {
      case 0: // Cược Tài (Xu)
        betType = 'Tài';
        currency = 'Xu';
        break;
      case 1: // Cược Xỉu (Xu)
        betType = 'Xỉu';
        currency = 'Xu';
        break;
      case 2: // Cược Tài (Lượng)
        betType = 'Tài';
        currency = 'Lượng';
        break;
      case 3: // Cược Xỉu (Lượng)
        betType = 'Xỉu';
        currency = 'Lượng';
        break;
    }

    // Kiểm tra giới hạn cược cho từng loại tiền tệ và hiển thị thông báo phù hợp
    if (currency === 'Xu' && betAmount > 50000000) {
      us.getAvatarService().serverDialog('Giới hạn cược cho Xu là 50 triệu Xu.');
      return;
    } else if (currency === 'Lượng' && betAmount > 5000) {
      us.getAvatarService().serverDialog('Giới hạn cược cho Lượng là 5 nghìn Lượng.');
      return;
    }

    // Kiểm tra số dư và thực hiện đặt cược tương ứng
    if (this.hasSufficientBalance(us, currency, betAmount)) {
      await this.handleBet(us, betType, currency, betAmount);
    } else {
      us.getAvatarService().serverDialog('Bạn không đủ ' + currency + ' để đặt cược.');
    }
  }

  // Kiểm tra số dư của người chơi cho loại tiền tệ cụ thể
  hasSufficientBalance(user, currency, betAmount) {
    if (currency === 'Xu') {
      return user.getXu() >= betAmount;
    } else if (currency === 'Lượng') {
      return user.getLuong() >= betAmount;
    }
    return false;
  }

  // Xử lý cược và trừ số tiền cược từ số dư của người chơi
  async handleBet(user, choice, currency, betAmount) {
    // Trừ số tiền cược từ tài khoản của người chơi
    if (!this.hasSufficientBalance(user, currency, betAmount)) {
      user.getAvatarService().serverDialog('Bạn không đủ tiền để đặt cược!');
      return; // Kết thúc hàm nếu không đủ tiền
    }
    if (!(await this.isGameOpen())) {
      user.getAvatarService().serverDialog('Không thể đặt cược. Phiên chơi đã kết thúc.');
      return;
    }
    // Kiểm tra xem người chơi đã cược trong phiên này chưa
    if (await this.hasBetInCurrentRound(user.getId(), this.gameId)) {
      user.getAvatarService().serverDialog('Bạn đã cược trong phiên này rồi. Không thể đặt cược thêm!');
      return; // Kết thúc hàm nếu đã cược
    }

    // Cập nhật số dư
    this.updateBalance(user, currency, -betAmount);

    // Ghi lại cược vào hệ thống (database)
    await this.saveBetToDatabase(user.getId(), this.gameId, choice, currency, betAmount);

    // Thông báo cho người chơi
    user.getAvatarService().serverDialog('Bạn đã đặt cược ' + betAmount + ' ' + currency + ' vào ' + choice + '.');
    Utils.writeLog(user, user.getUsername() + ' cuoc ' + betAmount + ' ' + currency + ' vào ' + choice + '.');
  }

  updateBalance(user, currency, amount) {
    if (currency === 'Xu') {
      user.updateXu(amount); // Cập nhật số dư Xu
      user.getAvatarService().updateMoney(0);
    } else if (currency === 'Lượng') {
      user.updateLuong(amount); // Cập nhật số dư Xu
      user.getAvatarService().updateMoney(0);
    }
  }

  // Lưu thông tin đặt cược vào cơ sở dữ liệu
  async saveBetToDatabase(userId, gameId, betType, currency, betAmount) {
    const insertQuery = "INSERT INTO betgame (user_id, game_id, bet_type, currency, bet_amount, status) VALUES (?, ?, ?, ?, ?, 'Pending')";
    try {
      await dbManager.executeUpdate(insertQuery, [userId, gameId, betType, currency, betAmount]);
    } catch (e) {
      console.error(e);
    }
  }

  // Lấy tất cả các cược trong phiên hiện tại từ database
  async getAllBetsForGame(gameId) {
    const bets = [];
    try {
      const rows = await dbManager.query('SELECT * FROM betgame WHERE game_id = ?', [gameId]);
      for (const rs of rows) {
        const userId = rs.user_id | 0;
        const user = userManager.find(userId);
        const betType = rs.bet_type;
        const currency = rs.currency;
        const amount = rs.bet_amount | 0;
        const betId = rs.bet_id | 0;
        bets.push(new Bet(betId, user, betType, currency, amount));
      }
    } catch (e) {
      console.error(e);
    }
    return bets;
  }

  // Xóa tất cả các cược trong phiên sau khi xử lý
  async clearBetsForGame(gameId) {
    try {
      await dbManager.executeUpdate('DELETE FROM betgame WHERE game_id = ?', [gameId]);
    } catch (e) {
      console.error(e);
    }
  }

  // Cập nhật trạng thái cược
  async updateBetStatus(betId, status) {
    try {
      await dbManager.executeUpdate('UPDATE betgame SET status = ? WHERE bet_id = ?', [status, betId]);
    } catch (e) {
      console.error(e);
    }
  }

  // Lưu kết quả phiên chơi
  async saveGameRoundResult(gameId, result) {
    const insertQuery = "INSERT INTO game_rounds (game_id, result, created_at, game_status, reward_status) VALUES (?, ?, NOW(), 'Open', 'chưa')"; // Đã sửa 'ceate' thành 'chưa
    try {
      await dbManager.executeUpdate(insertQuery, [gameId, result]);
    } catch (e) {
      console.error(e);
    }
  }

  // Cập nhật trạng thái trả thưởng
  async updateGameRewardStatus(gameId) {
    const updateQuery = "UPDATE game_rounds SET reward_status = 'đã trả' WHERE game_id = ?";
    try {
      await dbManager.executeUpdate(updateQuery, [gameId]);
    } catch (e) {
      console.error(e);
    }
    console.log('đã trả thưởng');
  }

  // Kiểm tra trạng thái phiên
  async isGameOpen() {
    const query = 'SELECT game_status FROM game_rounds WHERE game_id = ?';
    try {
      const rs = await dbManager.queryOne(query, [this.gameId]);
      if (rs != null) {
        const gameStatus = rs.game_status;
        return 'Open' === gameStatus;
      }
    } catch (e) {
      console.error(e);
    }
    return false;
  }

  async endGame(result) {
    // Cập nhật trạng thái trò chơi và thời gian kết thúc
    await this.updateGameEndStatus(this.gameId, result);
    console.log('Kết thúc phiên với ID: ' + this.gameId + ' với kết quả: ' + result);
  }

  // Cập nhật trạng thái khi kết thúc trò chơi
  async updateGameEndStatus(gameId, result) {
    const updateQuery = "UPDATE game_rounds SET game_status = 'Closed', end_time = CURRENT_TIMESTAMP, result = ? WHERE game_id = ?";
    try {
      await dbManager.executeUpdate(updateQuery, [result, gameId]);
    } catch (e) {
      console.error(e);
    }
    console.log('endGame không cho cược game ID ' + gameId);
  }

  async getLastGameId() {
    let lastGameId = 0; // Mặc định là 0 nếu không tìm thấy
    try {
      const rs = await dbManager.queryOne('SELECT MAX(game_id) FROM game_rounds');
      if (rs != null) {
        const v = Object.values(rs)[0];
        lastGameId = (v == null ? 0 : Number(v)) | 0;
      }
    } catch (e) {
      console.error(e);
    }
    return lastGameId;
  }

  async hasBetInCurrentRound(userId, gameId) {
    const query = 'SELECT COUNT(*) FROM betgame WHERE user_id = ? AND game_id = ?';
    try {
      const rs = await dbManager.queryOne(query, [userId, gameId]);
      if (rs != null) {
        console.log('co cuoc');
        return (Number(Object.values(rs)[0]) | 0) > 0; // Trả về true nếu có cược
      }
    } catch (e) {
      console.error(e);
    }
    return false; // Không có cược
  }

  async viewBetHistory(us) {
    const query = 'SELECT bg.bet_amount, bg.currency, gr.result, gr.game_status, bg.bet_type '
      + 'FROM betgame bg '
      + 'JOIN game_rounds gr ON bg.game_id = gr.game_id '
      + 'WHERE bg.user_id = ?';

    try {
      const rows = await dbManager.query(query, [us.getId()]);

      let totalBets = 0;
      let totalWins = 0;
      let totalLosses = 0;
      let totalAmountBetXu = 0;
      let totalAmountBetLuong = 0;
      let totalWinXu = 0;
      let totalLossXu = 0;
      let totalWinLuong = 0;
      let totalLossLuong = 0;

      for (const rs of rows) {
        const betAmount = Number(rs.bet_amount);
        const currency = rs.currency;
        const result = rs.result;
        const gameStatus = rs.game_status;
        const betType = rs.bet_type;

        if (currency == null || result == null || betType == null) continue;

        totalBets++;

        // Separate total bet amounts by currency
        if (currency === 'Xu') {
          totalAmountBetXu += betAmount;
        } else if (currency === 'Lượng') {
          totalAmountBetLuong += betAmount;
        }

        // Determine win/loss based on result and game status
        if (gameStatus === 'Closed') {
          if (result.includes(betType)) { // Player wins
            totalWins++;
            if (currency === 'Xu') {
              totalWinXu += betAmount * 0.95;
            } else if (currency === 'Lượng') {
              totalWinLuong += betAmount * 0.95;
            }
          } else { // Player loses
            totalLosses++;
            if (currency === 'Xu') {
              totalLossXu += betAmount;
            } else if (currency === 'Lượng') {
              totalLossLuong += betAmount;
            }
          }
        }
      }
      // Calculate win rate
      let winRate = totalBets > 0 ? totalWins / totalBets * 100 : 0;
      winRate = Math.round(winRate * 100.0) / 100.0;

      // Calculate net result for Xu and Luong
      const netXu = totalWinXu - totalLossXu;
      const netLuong = totalWinLuong - totalLossLuong;

      // Build the result message
      let sb = '';
      sb += 'Lịch sử đặt cược của: ' + us.getUsername() + '\n';
      sb += 'Tổng số cược: ' + totalBets + '\n';
      sb += ' Thắng: ' + totalWins;
      sb += ' Thua: ' + totalLosses;
      sb += ' Tỷ lệ thắng: ' + fmtDouble(winRate) + '%\n';
      sb += 'Tổng cược: ' + Math.trunc(totalAmountBetXu) + ' Xu\n';
      sb += 'Tổng cược: ' + Math.trunc(totalAmountBetLuong) + 'Lượng\n';

      if (netXu > 0) {
        sb += 'tổng: Thắng ' + Math.trunc(netXu) + ' xu\n';
      } else {
        sb += 'tổng: Thua ' + Math.trunc(Math.abs(netXu)) + ' xu\n';
      }

      if (netLuong > 0) {
        sb += 'tổng: Thắng ' + Math.trunc(netLuong) + ' lượng\n';
      } else {
        sb += 'tổng: Thua ' + Math.trunc(Math.abs(netLuong)) + ' lượng\n';
      }

      // Display the result
      us.getAvatarService().serverDialog(sb);

    } catch (e) {
      console.error(e);
    }
  }

  async viewGameRoundHistory(us) {
    const query = 'SELECT game_id, result '
      + 'FROM game_rounds '
      + 'ORDER BY created_at DESC ' // Sắp xếp theo thời gian tạo
      + 'LIMIT 10'; // Giới hạn 10 kết quả

    try {
      const rows = await dbManager.query(query);

      let sb = '';
      sb += 'Lịch sử 10 phiên gần nhất:\n';

      for (const rs of rows) {
        const gameId = rs.game_id | 0;
        const result = rs.result;

        sb += 'Phiên ' + gameId + ' : ' + ' ' + result + '\n';
      }

      us.getAvatarService().serverDialog(sb);
      console.log(sb);

    } catch (e) {
      console.error(e);
    }
  }

  async getTopWinerXu(user) {
    const query = 'SELECT bg.user_id, us.username, bg.currency, '
      + "SUM(CASE WHEN gr.result LIKE CONCAT('%', bg.bet_type, '%') THEN bg.bet_amount * 0.95 ELSE 0 END) - "
      + "SUM(CASE WHEN gr.result NOT LIKE CONCAT('%', bg.bet_type, '%') THEN bg.bet_amount ELSE 0 END) AS net_win "
      + 'FROM betgame bg '
      + 'JOIN game_rounds gr ON bg.game_id = gr.game_id '
      + 'JOIN users us ON bg.user_id = us.id '
      + "WHERE gr.game_status = 'Closed' AND bg.currency = 'Xu' "
      + 'GROUP BY bg.user_id, us.username, bg.currency '
      + 'HAVING net_win > 0 '
      + 'ORDER BY net_win DESC '
      + 'LIMIT 5';

    try {
      const rows = await dbManager.query(query);

      let sb = '';
      sb += 'Top 5 người WIN Xu:\n';

      for (const rs of rows) {
        const username = rs.username;
        const currency = rs.currency;
        const netWin = Number(rs.net_win);

        sb += '' + username + ' - Tổng thắng : ' + Math.trunc(netWin) + ' ' + currency + '\n';
      }
      sb += ' ------------------------------------------------------ ' + '\n';
      await user.getAvatarService().SendTabmsg(sb);

    } catch (e) {
      console.error(e);
    }
  }

  async getTopLossXu(user) {
    const query = 'SELECT bg.user_id, us.username, bg.currency, '
      + "SUM(CASE WHEN gr.result LIKE CONCAT('%', bg.bet_type, '%') THEN bg.bet_amount * 2 "
      + 'ELSE -bg.bet_amount END) AS net_loss '
      + 'FROM betgame bg '
      + 'JOIN game_rounds gr ON bg.game_id = gr.game_id '
      + 'JOIN users us ON bg.user_id = us.id '
      + "WHERE gr.game_status = 'Closed' AND bg.currency = 'Xu' "
      + 'GROUP BY bg.user_id, us.username, bg.currency '
      + 'HAVING net_loss < 0 '  // chỉ lấy người thua
      + 'ORDER BY net_loss ASC '
      + 'LIMIT 5';

    try {
      const rows = await dbManager.query(query);

      let sb = '';
      sb += 'Top 5 người LOSS Xu :\n';

      for (const rs of rows) {
        const username = rs.username;
        const currency = rs.currency;
        const netLoss = Number(rs.net_loss);

        sb += 'Người chơi: ' + username + ' - Tổng thua: ' + Math.trunc(Math.abs(netLoss)) + ' ' + currency + '\n';
      }
      sb += ' ------------------------------------------------------ ' + '\n';

      // Display the result
      await user.getAvatarService().SendTabmsg(sb);

    } catch (e) {
      console.error(e);
    }
  }

  async getTopLossLuong(user) {
    const query = 'SELECT bg.user_id, us.username, bg.currency, '
      + "SUM(CASE WHEN gr.result LIKE CONCAT('%', bg.bet_type, '%') THEN bg.bet_amount * 2 "
      + 'ELSE -bg.bet_amount END) AS net_loss '
      + 'FROM betgame bg '
      + 'JOIN game_rounds gr ON bg.game_id = gr.game_id '
      + 'JOIN users us ON bg.user_id = us.id '
      + "WHERE gr.game_status = 'Closed' AND bg.currency = 'Lượng' "
      + 'GROUP BY bg.user_id, us.username, bg.currency '
      + 'HAVING net_loss < 0 '  // chỉ lấy người thua
      + 'ORDER BY net_loss ASC '
      + 'LIMIT 5';

    try {
      const rows = await dbManager.query(query);

      let sb = '';
      sb += 'Top 5 người chơi thua Lượng:\n';

      for (const rs of rows) {
        const username = rs.username;
        const currency = rs.currency;
        const netLoss = Number(rs.net_loss);

        sb += 'Người chơi: ' + username + ' - Tổng thua: ' + Math.trunc(Math.abs(netLoss)) + ' ' + currency + '\n';
      }
      sb += ' Chúc bạn may mắn. ' + '\n';
      // Display the result
      await user.getAvatarService().SendTabmsg(sb);

    } catch (e) {
      console.error(e);
    }
  }

  async getTopWinLuong(user) {
    const query = 'SELECT bg.user_id, us.username, bg.currency, '
      + "SUM(CASE WHEN gr.result LIKE CONCAT('%', bg.bet_type, '%') THEN bg.bet_amount * 0.95 "
      + 'ELSE -bg.bet_amount END) AS net_win '
      + 'FROM betgame bg '
      + 'JOIN game_rounds gr ON bg.game_id = gr.game_id '
      + 'JOIN users us ON bg.user_id = us.id '
      + "WHERE gr.game_status = 'Closed' AND bg.currency = 'Lượng' "
      + 'GROUP BY bg.user_id, us.username, bg.currency '
      + 'HAVING net_win > 0 '  // chỉ lấy người thắng
      + 'ORDER BY net_win DESC '
      + 'LIMIT 5';

    try {
      const rows = await dbManager.query(query);

      let sb = '';
      sb += 'Top 5 người chơi thắng Lượng:\n';

      for (const rs of rows) {
        const username = rs.username;
        const currency = rs.currency;
        const netWin = Number(rs.net_win);

        sb += 'Người chơi: ' + username + ' - Tổng thắng: ' + Math.trunc(netWin) + ' ' + currency + '\n';
      }
      sb += ' ------------------------------------------------------ ' + '\n';
      // Display the result
      await user.getAvatarService().SendTabmsg(sb);

    } catch (e) {
      console.error(e);
    }
  }
}

export default TaiXiu;
