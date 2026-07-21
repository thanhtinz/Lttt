package avt;

import main.Canvas;

final class class_ed implements IAction {
   class_ed(IActionDellPart1 var1) {
   }

   public final void perform() {
      Canvas.startOKDlg(T.getData, new class_ec(this));
   }
}
