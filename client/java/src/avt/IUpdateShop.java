package avt;

final class IUpdateShop implements IAction {
   IUpdateShop(FarmScr var1) {
   }

   public final void perform() {
      FarmService.gI().doUpdateStore(0, -1);
   }
}
