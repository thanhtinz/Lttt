package avt;

import main.Canvas;

final class IActionForgetPass implements IAction {
   private final String a;

   IActionForgetPass(LoginScr var1, String var2) {
      this.a = var2;
   }

   public final void perform() {
      if (!Session_ME.gI().connected) {
         Canvas.startWaitDlg(T.connecting);
         Canvas.connect();
      } else {
         Canvas.startWaitDlg();
      }

      GlobalService.gI().requestService((byte)4, this.a);
   }
}
