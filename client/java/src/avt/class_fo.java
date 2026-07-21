package avt;

final class class_fo implements IAction {
   private final short a;

   class_fo(MapScr var1, short var2) {
      this.a = var2;
   }

   public final void perform() {
      GlobalService.gI().doRequestCmdRotate(this.a, -1);
      PopupShop.gI().right.perform();
   }
}
