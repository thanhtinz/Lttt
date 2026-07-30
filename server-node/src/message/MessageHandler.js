/**
 * Port của avatar/message/MessageHandler.java — bộ phân phối lệnh chính.
 * Giữ nguyên thứ tự case và điều kiện `user != null` như bản Java.
 */
import { Cmd } from '../constants/Cmd.js';
import serverManager from '../server/ServerManager.js';

export class MessageHandler {
  /** @param {import('../net/Session.js').Session} client */
  constructor(client) {
    this.client = client;
  }

  async onMessage(mss) {
    if (!mss) return;
    const c = this.client;
    try {
      switch (mss.getCommand()) {
        case Cmd.JOIN_HOUSE_4: {
          if (c.user != null) {
            await c.doJoinHouse4(mss);
          }
          break;
        }
        case Cmd.JOIN_OFFLINE_MAP: {
          if (c.user != null) {
            await c.doJoinOfflineMap(mss);
          }
          break;
        }
        case Cmd.REQUEST_IMAGE_PART: {
          if (c.user != null) {
            await c.requestImagePart(mss);
          }
          break;
        }
        case Cmd.REQUEST_DYNAMIC_PART: {
          if (c.user != null) {
            await c.getAvatarService().requestPartDynaMic(mss);
          }
          break;
        }
        case Cmd.REQUEST_TILE_MAP: {
          if (c.user != null) {
            await c.requestTileMap(mss);
          }
          break;
        }
        case Cmd.REQUEST_CITY_MAP: {
          if (c.user != null) {
            await c.doRequestCityMap(mss);
          }
          break;
        }
        case Cmd.GET_IMG_ICON: {
          if (c.user != null) {
            await c.doGetImgIcon(mss);
          }
          break;
        }
        case Cmd.SET_AGENT: {
          if (c.user != null) {
            await c.agentInfo(mss);
          }
          break;
        }
        case Cmd.GET_TILE_MAP: {
          if (c.user != null) {
            await c.getAvatarService().getTileMap();
          }
          break;
        }
        case Cmd.REQUEST_EXPICE_PET: {
          if (c.user != null) {
            await c.doRequestExpicePet(mss);
          }
          break;
        }
        case Cmd.JOIN_HOUSE: {
          if (c.user != null) {
            await c.joinHouse(mss);
          }
          break;
        }
        case Cmd.DIAL_LUCKY: {
          if (c.user != null) {
            await c.doDialLucky(mss);
          }
          break;
        }
        case Cmd.CHANGE_PASS: {
          if (c.user != null) {
            await c.changePassword(mss);
          }
          break;
        }
        case Cmd.COMMUNICATE: {
          if (c.user != null) {
            await c.doCommunicate(mss);
          }
          break;
        }
        case Cmd.TEXT_BOX: {
          if (c.user != null) {
            await c.handler.handleTextBox(mss);
          }
          break;
        }
        case Cmd.BOSS_SHOP: {
          if (c.user != null) {
            await c.handleBossShop(mss);
          }
          break;
        }
        case Cmd.MENU_OPTION: {
          if (c.user != null) {
            await c.handler.handleOptionMenu(mss);
          }
          break;
        }
        case Cmd.REQUEST_SERVICE: {
          await c.doRequestService(mss);
          break;
        }
        case Cmd.UPDATE_CONTAINER: {
          if (c.user != null) {
            await c.doRequestService(mss);
          }
          break;
        }
        case Cmd.NUMBER_SUPPORT: {
          console.log("-52:  " + mss.reader().readInt());
          break;
        }
        case Cmd.USING_PART: {
          if (c.user != null) {
            await c.user.usingItem(mss);
          }
          break;
        }
        case Cmd.CONTAINER: {
          if (c.user != null) {
            await c.user.viewChest(mss);
          }
          break;
        }
        case Cmd.MAP_ITEM: {
          if (c.user != null) {
            await c.getAvatarService().getMapItems(mss);
          }
          break;
        }
        case Cmd.MAP_ITEM_TYPE: {
          if (c.user != null) {
            await c.getAvatarService().getMapItemTypes(mss);
          }
          break;
        }
        case Cmd.PARK_BUY_ITEM: {
          if (c.user != null) {
            await c.doParkBuyItem(mss);
          }
          break;
        }
        case Cmd.GET_ITEM_INFO: {
          if (c.user != null) {
            await c.getAvatarService().getFoodData();
          }
          break;
        }
        case Cmd.REMOVE_ITEM: {
          if (c.user != null) {
            await c.user.doRemoveItem(mss);
          }
          break;
        }
        case Cmd.CRE_CHARACTER: {
          if (c.user != null) {
            await c.createCharacter(mss);
          }
          break;
        }
        case Cmd.AVATAR_BUY_ITEM: {
          if (c.user != null) {
            await c.buyItemShop(mss);
          }
          break;
        }
        case Cmd.REQUEST_YOUR_INFO: {
          if (c.user != null) {
            await c.user.requestYourInfo(mss);
          }
          break;
        }
        case -27: {
          await c.handshakeMessage();
          break;
        }
        case Cmd.REQUEST_FRIENDLIST: {
          await c.requestFriendList(mss);
          break;
        }
        case Cmd.SET_PROVIDER: {
          await c.clientInfo(mss);
          break;
        }
        case Cmd.GET_AVATAR_PART: {
          await c.getAvatarService().getAvatarPart();
          break;
        }
        case Cmd.GET_IMAGE: {
          await c.getAvatarService().getImageData();
          break;
        }
        case Cmd.GET_BIG: {
          await c.getAvatarService().getBigImage(mss);
          break;
        }
        case Cmd.SET_BIG: {
          await c.getAvatarService().getBigData();
          break;
        }
        case Cmd.LOGIN: {
          await c.doLogin(mss);
          break;
        }
        case Cmd.GET_HANDLER: {
          if (c.user != null) {
            await c.getHandler(mss);
          }
          break;
        }
        case Cmd.AVATAR_JOIN_PARK: {
          if (c.user != null) {
            await serverManager.joinAreaMessage(c.user, mss);
          }
          break;
        }
        case Cmd.MOVE_PARK: {
          if (c.user != null) {
            await c.user.move(mss);
            console.log("55:  ");
          }
          break;
        }
        case Cmd.CHAT_PARK: {
          if (c.user != null) {
            await c.user.chat(mss);
            console.log("551:  ");
          }
          break;
        }
        case Cmd.AVATAR_FEEL: {
          if (c.user != null) {
            await c.user.doAvatarFeel(mss);
          }
          break;
        }
        case Cmd.ACTION: {
          if (c.user != null) {
            await c.user.doAction(mss);
          }
          break;
        }
        case Cmd.PARK_BOARD_LIST: {
          await c.doiKhuVuc(mss);
          break;
        }
        case Cmd.EFFECT_OBJ: {
          await c.getAvatarService().sendEffectData(mss);
          break;
        }
        case Cmd.MENU_ROTATE: {
          await c.getAvatarService().HandlerMENU_ROTATE(c.user, mss);
          break;
        }
        default:
          break;
      }
    } catch (e) {
      console.error('[MessageHandler] lỗi lệnh', mss.getCommand(), e);
    }
  }

  onConnectionFail() {}

  onDisconnected() {
    // Bản Java để trống; dọn dẹp do Session.close() đảm nhiệm
  }

  onConnectOK() {}
}

export default MessageHandler;
