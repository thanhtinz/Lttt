package avt;

import main.Canvas;

final class IActionTriBenh2 implements IAction {
   private FarmScr a;
   private final Animal b;

   IActionTriBenh2(FarmScr var1, Animal var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      boolean var1 = false;

      for(int var2 = 0; var2 < FarmScr.listItemFarm.size(); ++var2) {
         Item var3;
         if ((var3 = (Item)FarmScr.listItemFarm.elementAt(var2)).ID == 121) {
            FarmItem var4 = FarmScr.getFarmItem(var3.ID);
            FarmScr.setActionAnimalAccess(this.a, var4, var3.ID, this.b);
            var1 = true;
            break;
         }
      }

      if (!var1) {
         Canvas.startOKDlg(T.hasRob);
         FarmScr.gI().commandTab(8, -1);
      }

   }
}
