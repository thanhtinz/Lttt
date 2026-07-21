package avt;

import main.Canvas;

final class IActionSetPass implements IAction {
   private final int a;

   IActionSetPass(HomeMsgHandler var1, int var2) {
      this.a = var2;
   }

   public final void perform() {
      AvatarService.gI().doSetPassMyHouse(Canvas.inputDlg.getText(), this.a, 1);
      Canvas.endDlg();
   }
}
