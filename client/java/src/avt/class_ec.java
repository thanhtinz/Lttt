package avt;

import main.Canvas;

final class class_ec implements IAction {
   class_ec(class_ed var1) {
   }

   public final void perform() {
      GlobalService.gI().doUpdateContainer(0);
      Canvas.startWaitDlg();
   }
}
