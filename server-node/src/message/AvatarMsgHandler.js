/** Port của avatar/message/AvatarMsgHandler.java */
import { MessageHandler } from './MessageHandler.js';

export class AvatarMsgHandler extends MessageHandler {
  constructor(client) {
    super(client);
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
        default:
          console.log('AvatarMsgHandler: ' + mss.getCommand());
          await super.onMessage(mss);
          break;
      }
    } catch (e) {
      console.error(e);
    }
  }
}

export default AvatarMsgHandler;
