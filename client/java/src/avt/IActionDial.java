package avt;

import main.Canvas;

final class IActionDial implements IAction {
   private short a;
   private boolean b;

   public IActionDial(MapScr var1, short var2) {
      this(var1, var2, false);
   }

   public IActionDial(MapScr var1, short var2, boolean var3) {
      this.a = var2;
      this.b = var3;
   }

   public final void perform() {
      PopupShop.gI().close();
      DialLuckyScr.gI().switchToMe(Canvas.currentMyScreen, this.a, this.b);
   }
}
