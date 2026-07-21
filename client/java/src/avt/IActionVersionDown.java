package avt;

import main.GameMidlet;

final class IActionVersionDown implements IAction {
   private final String a;

   IActionVersionDown(GlobalLogicHandler var1, String var2) {
      this.a = var2;
   }

   public final void perform() {
      GameMidlet.flatForm(this.a);
   }
}
