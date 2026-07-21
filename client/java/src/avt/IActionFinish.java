package avt;

import main.Canvas;

final class IActionFinish implements IAction {
   private final TField[] a;

   IActionFinish(HouseScr var1, TField[] var2) {
      this.a = var2;
   }

   public final void perform() {
      MapScr.gI();
      if (MapScr.setEnterPass(this.a)) {
         GlobalService.gI().doChangeChestPass(this.a[0].getText(), this.a[1].getText());
         Canvas.startWaitDlg();
         InputFace.gI();
         Canvas.currentFace = null;
      }

   }
}
