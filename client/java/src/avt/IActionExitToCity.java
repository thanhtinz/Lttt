package avt;

import main.Canvas;

final class IActionExitToCity implements IAction {
   IActionExitToCity(LoadMap var1) {
   }

   public final void perform() {
      Canvas.startWaitDlg();
      if (LoadMap.TYPEMAP == 108) {
         ParkService.gI().doJoinPark(9, -1);
      } else {
         Canvas.startWaitDlg();
         GlobalService.gI().getHandler(9);
      }

   }
}
