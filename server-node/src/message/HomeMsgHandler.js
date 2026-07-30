/** Port của avatar/message/HomeMsgHandler.java */
import { Cmd } from '../constants/Cmd.js';
import { HomeService } from '../service/HomeService.js';
import { MessageHandler } from './MessageHandler.js';

export class HomeMsgHandler extends MessageHandler {
  constructor(client) {
    super(client);
    this.service = new HomeService(client);
  }

  async onMessage(mss) {
    if (mss == null) {
      return;
    }
    if (this.client.user == null) {
      return;
    }
    try {
      switch (mss.getCommand()) {
        case Cmd.BUY_ITEM_HOUSE: {
          await this.service.buyItemHouse(mss);
          break;
        }
        case Cmd.SORT_ITEM_HOUSE: {
          await this.service.sortItemHouse(mss);
          break;
        }
        case Cmd.GET_TYPE_HOUSE: {
          await this.service.getTypeHouse(mss);
          break;
        }
        case Cmd.DEL_ITEM_HOUSE: {
          await this.service.delItemHouse(mss);
          break;
        }
        case Cmd.CREATE_HOME: {
          await this.service.createHome(mss);
          break;
        }
        case Cmd.GET_IMG_OBJ_INFO: {
          await this.service.getImgObjInfo(mss);
          break;
        }
        case Cmd.CUSTOM_CHEST: {
          await this.service.onCustomChest(mss);
          break;
        }
        case Cmd.TRANS_PART_CHEST: {
          await this.service.transPartChest(mss);
          break;
        }
        case Cmd.UPGRADE_CHEST: {
          await this.service.upgradeChestHome(mss);
          break;
        }
        default:
          console.log('HomeMsgHandler: ' + mss.getCommand());
          await super.onMessage(mss);
          break;
      }
    } catch (e) {
      // Java chỉ bắt IOException
      console.error(e);
    }
  }
}

export default HomeMsgHandler;
