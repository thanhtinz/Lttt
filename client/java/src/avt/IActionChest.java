package avt;

import main.Canvas;

final class IActionChest implements IAction {
   private final byte a;

   IActionChest(GlobalLogicHandler var1, byte var2) {
      this.a = var2;
   }

   public final void perform() {
      if (this.a == 0) {
         GlobalService.gI().doUpdateContainer(1);
      } else {
         GlobalService.gI().doUpdateChest(1);
      }

      Canvas.startWaitDlg();
   }
}
