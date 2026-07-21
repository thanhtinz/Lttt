package avt;

final class IActionShopOffline1 implements IAction {
   private MapScr a;
   private final Part b;

   IActionShopOffline1(MapScr var1, Part var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      MapScr.doBuyItem(this.b.IDPart);
   }
}
