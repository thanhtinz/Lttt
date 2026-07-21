package avt;

import main.Canvas;

final class class_w implements IAction {
   private FarmScr a;
   private final Animal b;

   class_w(FarmScr var1, Animal var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      boolean var1 = false;

      for(int var2 = 0; var2 < FarmScr.listItemFarm.size(); ++var2) {
         Item var3;
         if (FarmScr.getFarmItem((var3 = (Item)FarmScr.listItemFarm.elementAt(var2)).ID).action == 6) {
            FarmService.gI().doUsingItem(FarmScr.idFarm, this.b.IDDB, var3.ID);
            var1 = true;
            this.a.commandActionPointer(10, -1);
            break;
         }
      }

      if (!var1) {
         FarmScr.gI().commandTab(8, -1);
         Canvas.startOKDlg(T.doYouWantToTrade);
      }

   }
}
