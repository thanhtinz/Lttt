package avt;

import main.Canvas;

final class IACctionOut implements IAction {
   IACctionOut(RaceScr var1) {
   }

   public final void perform() {
      Canvas.startWaitDlg();
      GlobalService.gI().getHandler(9);
   }
}
