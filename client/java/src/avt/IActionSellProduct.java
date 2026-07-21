package avt;

import main.Canvas;

final class IActionSellProduct implements IAction {
   private final int a;

   IActionSellProduct(FarmScr var1, int var2) {
      this.a = var2;
   }

   public final void perform() {
      FarmService.gI().doSellItem((short)this.a);
      Canvas.startWaitDlg();
   }
}
