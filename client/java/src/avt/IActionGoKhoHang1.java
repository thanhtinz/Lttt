package avt;

final class IActionGoKhoHang1 implements IAction {
   private int a;

   public IActionGoKhoHang1(FarmScr var1, int var2) {
      this.a = var2;
   }

   public final void perform() {
      Item var1 = (Item)FarmScr.itemProduct.elementAt(this.a);
      FarmScr.gI().doSellProduct(var1.ID, var1.name);
   }
}
