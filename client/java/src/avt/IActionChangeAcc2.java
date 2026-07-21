package avt;

final class IActionChangeAcc2 implements IAction {
   private LoginScr a;

   IActionChangeAcc2(LoginScr var1) {
      this.a = var1;
   }

   public final void perform() {
      LoginScr.isNewGame = true;
      this.a.left = this.a.cmdMenu;
      this.a.center = this.a.g;
   }
}
