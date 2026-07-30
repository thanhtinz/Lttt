/** Port của avatar/message/minigame/BauCuaMsgHandler.java */
import { Message } from '../../net/Message.js';
import { Service } from '../../service/Service.js';

export class BauCuaMsgHandler extends Service {
  constructor(cl) {
    super(cl);
  }

  /** Java: joinCasino(Message ms) — tham số bị ghi đè ngay lập tức. */
  async joinCasino(ms) {
    // NOTE: giữ nguyên hành vi bản Java (gán lại `ms`, bỏ qua gói tin nhận được)
    ms = new Message(61);
    const ds = ms.writer();
    ds.writeByte(22);
    ds.flush();
    await this.session.user.sendMessage(ms);
  }
}

export default BauCuaMsgHandler;
