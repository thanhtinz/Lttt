package avt;

final class IActionSelectedMoney implements IAction {
   private final Part a;

   IActionSelectedMoney(Part var1) {
      this.a = var1;
   }

   public final void perform() {
      AvatarService.gI().doBuyItem(this.a.IDPart, 1);
   }
}
