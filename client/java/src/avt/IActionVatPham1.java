package avt;

final class IActionVatPham1 implements IAction {
   private FarmScr a;

   IActionVatPham1(FarmScr var1) {
      this.a = var1;
   }

   public final void perform() {
      FarmScr.setAction(this.a, (byte)1, FarmScr.idItemUsing);
   }
}
