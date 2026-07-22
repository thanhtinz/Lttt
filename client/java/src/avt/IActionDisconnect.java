package avt;

import main.Canvas;
import main.GameMidlet;

final class IActionDisconnect implements IAction {
   IActionDisconnect(GlobalLogicHandler var1) {
   }

   public final void perform() {
      GameMidlet.CLIENT_TYPE = 8;
      GlobalMessageHandler.gI().miniGameMessageHandler = null;
      Session_ME.gI().close();
      LoginScr.gI().switchToMe();
      Canvas.menuMain = null;
      SoundManager.instance.stop();
      FarmData.init();
      ClientUtilities.onFishingAutoDisconnected();
   }
}
