package avt;

final class IActionTextDes implements IAction {
   private int a;
   private int b;
   private int c;

   public IActionTextDes(MapScr var1, int var2, int var3, int var4) {
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   public final void perform() {
      System.out.println("DEBUG SHOP_BUY_CONFIRM: doBossShop idBoss=" + this.a + " idShopByte=" + this.b + " optionIdx=" + this.c);
      ParkService.gI().doBossShop(this.a, this.b, this.c);
   }
}
