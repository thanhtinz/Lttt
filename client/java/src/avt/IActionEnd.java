package avt;

final class IActionEnd implements IAction {
   private final int a;
   private final int b;
   private final int c;

   IActionEnd(FarmScr var1, int var2, int var3, int var4) {
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   public final void perform() {
      FarmService.gI().doBuyItem((short)this.a, (byte)this.b, 2);
      PopupShop.k = false;
   }
}
