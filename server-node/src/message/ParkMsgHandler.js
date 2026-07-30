/** Port của avatar/message/ParkMsgHandler.java */
import { Cmd } from '../constants/Cmd.js';
import serverManager from '../server/ServerManager.js';
import { Utils } from '../server/Utils.js';
import { ParkService } from '../service/ParkService.js';
import { MessageHandler } from './MessageHandler.js';

/** Thread.sleep → chờ bằng Promise. */
function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export class ParkMsgHandler extends MessageHandler {
  constructor(client) {
    super(client);
    this.service = new ParkService(client);
  }

  async onMessage(mss) {
    if (mss == null) {
      return;
    }
    if (this.client.user == null) {
      return;
    }
    console.log('ParkMsgHandler: ' + mss.getCommand());
    try {
      switch (mss.getCommand()) {
        case Cmd.WEDDING_BIGIN: {
          await this.client.getParkService().WEDDING_BIGINHanlder(this.client.user, mss);
          break;
        }
        case Cmd.MENU_ROTATE: {
          await this.client.getAvatarService().HandlerMENU_ROTATE(this.client.user, mss);
          break;
        }
        case Cmd.AVATAR_REQUEST_ADD_FRIEND:
          await this.client.getParkService().handleAddFriendRequest(mss);
          break;
        case Cmd.CHAT_TO:
          await this.client.getAvatarService().chatToUser(mss);
          break;
        case Cmd.AVATAR_JOIN_PARK:
          await serverManager.joinAreaMessage(this.client.user, mss);
          break;
        case Cmd.MOVE_PARK:
          await this.client.user.move(mss);
          break;

        case Cmd.CHAT_PARK:
          await this.client.user.chat(mss);
          break;

        case Cmd.AVATAR_FEEL:
          await this.client.user.doAvatarFeel(mss);
          break;

        case Cmd.REQUEST_DYNAMIC_PART:
          await this.client.getAvatarService().requestPartDynaMic(mss);
          break;
        case Cmd.REQUEST_JOIN_ANY:
          await this.client.getAvatarService().requestJoinAny(mss);
          break;
        case Cmd.START_CAU_CA: // 86
          await this.client.getParkService().handleStartFishing(mss);
          break;
        case Cmd.QUANG_CAU: { // 82
          await this.client.getParkService().handleQuangCau(mss); // 82

          const startTime = Date.now();
          console.log('Waiting started at: ' + startTime);
          await sleep(Utils.nextInt(12000, 18000));

          const endTime = Date.now();
          console.log('Waiting ended at: ' + endTime);
          console.log('Elapsed time: ' + (endTime - startTime) + ' ms');
          await this.client.getParkService().onCanCau(); // 91
          break;
        }
        case Cmd.CAU_CA_XONG: // 85
          await this.client.getParkService().CauCaXong();
          break;
        case Cmd.CAU_THANH_CONG: // 84
          await this.client.getParkService().CauThanhCong(); // 84
          break;
        default:
          console.log('ParkMsgHandler: ' + mss.getCommand());
          await super.onMessage(mss);
          break;
      }
    } catch (e) {
      console.error(e);
    }
  }
}

export default ParkMsgHandler;
