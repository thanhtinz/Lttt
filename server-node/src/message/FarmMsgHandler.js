/** Port của avatar/message/FarmMsgHandler.java */
import { Cmd } from '../constants/Cmd.js';
import { NpcName } from '../constants/NpcName.js';
import { NpcHandler } from '../handler/NpcHandler.js';
import { Npc } from '../model/Npc.js';
import { FarmService } from '../service/FarmService.js';
import { MessageHandler } from './MessageHandler.js';

export class FarmMsgHandler extends MessageHandler {
  constructor(client) {
    super(client);
    this.service = new FarmService(client);
  }

  async onMessage(mss) {
    if (mss == null) {
      return;
    }
    if (this.client.user == null) {
      return;
    }
    try {
      console.log('FarmMsgHandler: ' + mss.getCommand());
      switch (mss.getCommand()) {
        case Cmd.SET_BIG_FARM: {
          await this.service.setBigFarm(mss);
          break;
        }
        case Cmd.BUY_ITEM: {
          await this.service.Buy_item_farm(mss);
          break;
        }
        case Cmd.BUY_ANIMAL: {
          await this.service.Buy_ANIMAL(mss);
          break;
        }
        case Cmd.GET_BIG_FARM: {
          await this.service.getBigFarm(mss);
          break;
        }
        case Cmd.GET_IMAGE_FARM: {
          await this.service.getImageData();
          break;
        }
        case Cmd.GET_TREE_INFO: {
          await this.service.getTreeInfo(mss);
          break;
        }
        case Cmd.INVENTORY: {
          await this.service.getInventory(mss);
          break;
        }
        case Cmd.JOIN: {
          await this.service.joinFarm(mss);
          break;
        }
        case Cmd.GET_IMG_FARM: {
          await this.service.getImgFarm(mss);
          break;
        }
        case Cmd.REQUEST_SLOT: {
          await this.service.doRequestslot(mss);
          break;
        }
        case Cmd.TREE_HARVEST: {
          await this.service.treeHarvest(mss);
          break;
        }

        case Cmd.OPEN_LAND: {
          await this.service.openLand(mss);
          break;
        }

        case Cmd.PLANT_SEED: {
          await this.service.plandSeed(mss);
          break;
        }

        case Cmd.REQUEST_FRIENDLIST: {
          await this.service.serverDialog('Ăn trộm đang xây dựng');
          break;
        }
        case Cmd.REQUEST_CHARGE_MONEY_INFO: {
          await this.service.serverDialog('Pay To Win hả');
          break;
        }
        case Cmd.GET_CARD: {
          await this.service.sellFarmitm(this.client.user, mss);
          break;
        }
        case Cmd.UPDATE_FARM_CATTLE: {
          await this.service.serverDialog('Mở rộng nông trại đang xây dựng vui lòng quay lại sau');
          break;
        }
        case Cmd.UPDATE_FARM_FISH: {
          await this.service.serverDialog('Mở rộng nuôi cá đang xây dựng');
          break;
        }
        case Cmd.COOKING: {
          // this.service.huy("Hủy Nấu Ăn Nhanh tutu");
          break;
        }
        case Cmd.COMMUNICATE: {
          // this.service.serverDialog("lãi buôn");
          await NpcHandler.handlerCommunicate(Npc.ID_ADD + NpcName.LAI_BUON, this.client.user);
          break;
        }
        default:
          await super.onMessage(mss);
          break;
      }
    } catch (e) {
      // Java: bắt IOException, SQLException thì bọc RuntimeException
      console.error(e);
    }
  }
}

export default FarmMsgHandler;
