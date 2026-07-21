package avt;

import main.Canvas;

final class IActionCooking1 implements IAction {
   private final Food a;

   IActionCooking1(FarmScr var1, Food var2) {
      this.a = var2;
   }

   public final void perform() {
      for(int var1 = 0; var1 < this.a.material.length; ++var1) {
         String var3 = "";
         Item var2;
         if (this.a.material[var1] < 100) {
            var2 = FarmScr.getProductByID(this.a.material[var1]);
            if (this.a.material[var1] < 50) {
               var3 = FarmData.getTreeByID(this.a.material[var1]).name;
            } else if (FarmData.getAnimalByID(this.a.material[var1]).area == 1) {
               var3 = T.block2NoWin + " " + FarmData.getAnimalByID(this.a.material[var1]).name;
            } else if (FarmData.getAnimalByID(this.a.material[var1]).area == 2) {
               var3 = T.sixPointNoWin + " " + FarmData.getAnimalByID(this.a.material[var1]).name;
            }
         } else {
            var2 = FarmScr.getItemProductByID(this.a.material[var1]);
            var3 = FarmScr.getFarmItem(this.a.material[var1]).des;
         }

         if (var2 == null || var2.number < this.a.numberMaterial[var1]) {
            Canvas.startOKDlg(T.notEnough + var3);
            return;
         }
      }

      FarmService.gI().doCooking(this.a.ID);
      PopupShop.gI().close();
   }
}
