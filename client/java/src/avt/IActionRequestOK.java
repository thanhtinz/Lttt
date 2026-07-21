package avt;

import main.Canvas;

final class IActionRequestOK implements IAction {
   private String a;
   private MiniMap b;

   public IActionRequestOK(MiniMap var1, String var2) {
      this.b = var1;
      this.a = var2;
   }

   public final void perform() {
      Canvas.startOK(this.a, new IActionYesRef(this.b));
   }
}
