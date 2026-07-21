package avt;

final class IActionXuTree implements IAction {
   private final int a;
   private final int b;
   private final int c;

   IActionXuTree(FarmScr var1, int var2, int var3, int var4) {
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   public final void perform() {
      FarmService.gI().doBuyItem((short)this.a, (byte)this.b, 1);
      PopupShop.k = false;
   }
}
