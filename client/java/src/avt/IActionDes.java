package avt;

final class IActionDes implements IAction {
   private IActionShop1 a;

   IActionDes(IActionShop1 var1) {
      this.a = var1;
   }

   public final void perform() {
      GlobalService.gI().doSendOpenShopHouse(this.a.typeShop, this.a.idItem);
   }
}
