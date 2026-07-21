package avt;

import main.Canvas;

final class IActionInviteHouse implements IAction {
   private final int a;

   IActionInviteHouse(MapScr var1, int var2) {
      this.a = var2;
   }

   public final void perform() {
      ParkService.gI().doInviteToMyHome(1, this.a);
      Canvas.startWaitDlg();
   }
}
