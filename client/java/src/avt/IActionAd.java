package avt;

import main.Canvas;
import main.GameMidlet;

final class IActionAd implements IAction {
   private final ObjAd a;

   IActionAd(LoadMap var1, ObjAd var2) {
      this.a = var2;
   }

   public final void perform() {
      if (this.a.id != -1) {
         GlobalService.gI().requestShop(this.a.id);
         Canvas.startWaitDlg();
      } else if (this.a.url != null && !this.a.url.equals("")) {
         GameMidlet.flatForm(this.a.url);
      } else {
         GameMidlet.doSendSMS(this.a.sms, this.a.to);
      }

   }
}
