package avt;

final class IActionItem1 implements IAction {
   private IActionItem a;
   private final int b;
   private final String c;

   IActionItem1(IActionItem var1, int var2, String var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public final void perform() {
      HouseScr.setxTemp(this.a.me, HouseScr.getX(this.a.me));
      HouseScr.setYtemp(this.a.me, HouseScr.getY(this.a.me));
      HouseScr.buyMapItemAccess(this.a.me, this.b, this.c);
   }
}
