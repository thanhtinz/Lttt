package avt;

final class IActionVatPham12 implements IAction {
   private FarmScr a;
   private final AvPosition b;

   IActionVatPham12(FarmScr var1, AvPosition var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      FarmScr.a(this.a, FarmScr.indexItem, this.b.anchor);
      FarmScr.a(this.a);
   }
}
