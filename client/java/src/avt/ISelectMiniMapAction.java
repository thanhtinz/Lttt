package avt;

import main.Canvas;

final class ISelectMiniMapAction implements IAction {
   ISelectMiniMapAction(MapScr var1) {
   }

   public final void perform() {
      String var1 = T.beingOn;
      switch (MiniMap.gI().selected) {
         case 0:
            GlobalService.gI().getHandler(11);
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            GlobalService.gI().getHandler(9);
            break;
         case 7:
            GlobalService.gI().getHandler(10);
      }

      Canvas.startWaitDlg(var1 + T.nameRegion[MiniMap.gI().selected] + "...");
   }
}
