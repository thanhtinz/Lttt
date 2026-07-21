package avt;

import main.Canvas;

final class IActionBuyDream implements IAction {
   private final Item a;

   IActionBuyDream(MapScr var1, Item var2) {
      this.a = var2;
   }

   public final void perform() {
      ParkService.gI().doBuyItem(this.a.ID);
      Canvas.startWaitDlg();
   }
}
