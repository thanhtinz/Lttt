package avt;

final class IActionVatPham2 implements IAction {
   private FarmScr a;
   private final CellFarm b;

   IActionVatPham2(FarmScr var1, CellFarm var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      FarmScr.a(this.a, this.b);
   }
}
