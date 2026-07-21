package avt;

import main.Canvas;

final class IActionRequestReg implements IAction {
   private String a;
   private MiniMap b;

   public IActionRequestReg(MiniMap var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   public final void perform() {
      Canvas.startOKDlg(this.a, new IActionYesRef(this.b));
   }
}
