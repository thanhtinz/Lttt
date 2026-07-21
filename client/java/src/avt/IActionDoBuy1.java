package avt;

import main.GameMidlet;

final class IActionDoBuy1 implements IAction {
   private final String a;

   IActionDoBuy1(MoneyScr var1, String var2) {
      this.a = var2;
   }

   public final void perform() {
      GameMidlet.flatForm(this.a);
   }
}
