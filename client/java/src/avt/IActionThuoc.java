package avt;

final class IActionThuoc implements IAction {
   private FarmScr a;
   private final Item b;

   IActionThuoc(FarmScr var1, Item var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      this.a.doUsingVatPhamAnimal(this.b, 1);
   }
}
