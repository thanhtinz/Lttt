package avt;

final class IActionSelectedMoney1 implements IAction {
   private final Part a;

   IActionSelectedMoney1(Part var1) {
      this.a = var1;
   }

   public final void perform() {
      AvatarService.gI().doBuyItem(this.a.IDPart, 2);
   }
}
