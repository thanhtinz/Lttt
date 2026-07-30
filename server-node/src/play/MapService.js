/** Port của avatar/play/MapService.java */
import { Cmd } from '../constants/Cmd.js';
import { Message } from '../net/Message.js';
import { Service } from '../service/Service.js';
import { DialLucky } from '../lucky/DialLucky.js';

export class MapService extends Service {

  constructor(cl) {
    super(cl);
    this.zone = null;
  }

  /** lombok @Setter */
  setZone(zone) {
    this.zone = zone;
  }

  leavePark(userID) {
    try {
      const ms = new Message(Cmd.PLAYER_LEAVE_PARK);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('leavePark()', ex);
    }
  }

  move(us) {
    try {
      const ms = new Message(Cmd.MOVE_PARK);
      const ds = ms.writer();
      ds.writeInt(us.getId());
      ds.writeShort(us.getX());
      ds.writeShort(us.getY());
      ds.writeByte(us.getDirect());
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('move()', ex);
    }
  }

  chat(user, text) {
    try {
      const ms = new Message(this.zone.getMap().getId() === 22 ? Cmd.CHAT_FARM : Cmd.CHAT_PARK);
      const ds = ms.writer();
      ds.writeInt(user.getId());
      ds.writeUTF(text);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('chat()', ex);
    }
  }

  dialLucky(user, degree, gifts) {
    try {
      const ms = new Message(Cmd.DIAL_LUCKY);
      const ds = ms.writer();
      ds.writeInt(user.getId());
      ds.writeShort(degree);
      ds.writeByte(gifts.length);
      for (const gift of gifts) {
        ds.writeByte(gift.getType());
        switch (gift.getType()) {
          case DialLucky.ITEM:
            ds.writeShort(gift.getId());
            ds.writeByte(gift.getExpireDay());
            break;

          case DialLucky.XU:
            ds.writeInt(gift.getXu());
            break;

          case DialLucky.XP:
            ds.writeInt(gift.getXp());
            break;

          case DialLucky.LUONG:
            ds.writeInt(gift.getLuong());
            break;
        }
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('dialLucky', ex);
    }
  }

  doAction(userID, idTo, action) {
    try {
      const ms = new Message(59);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeInt(idTo);
      ds.writeShort(action);
      if (action === -1) {
        ds.writeUTF('Có thằng nào vừa làm cái gì đó, thông báo admin biết nhen !');
      } else {
        ds.writeShort(10);
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.error('doAction()', e);
    }
  }

  doAvatarFeel(userID, idFeel) {
    try {
      const ms = new Message(Cmd.AVATAR_FEEL);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeByte(idFeel);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('doAvatarFeel()', ex);
    }
  }

  addPlayer(us) {
    try {
      const ms = new Message(51);
      const ds = ms.writer();
      ds.writeInt(us.getId());
      ds.writeUTF(us.getUsername());
      ds.writeByte(us.getWearing().length);
      for (const item of us.getWearing()) {
        ds.writeShort(item.getId());
      }
      ds.writeShort(us.getX());
      ds.writeShort(us.getY());
      ds.writeByte(us.getRole());
      ds.writeByte(-1);
      ds.writeShort(-1);
      ds.writeShort(-1);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('addPlayer()', ex);
    }
  }

  usingPart(userID, itemID) {
    try {
      const ms = new Message(Cmd.USING_PART);
      const ds = ms.writer();
      ds.writeInt(userID);
      ds.writeShort(itemID);
      ds.flush();
      this.sendMessage(ms);
    } catch (ex) {
      console.error('usingPart()', ex);
    }
  }

  sendMessage(ms) {
    const players = this.zone.getPlayers();
    for (const us of players) {
      us.sendMessage(ms);
    }
  }
}

export default MapService;
