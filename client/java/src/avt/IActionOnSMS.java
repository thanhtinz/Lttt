package avt;

import main.GameMidlet;

final class IActionOnSMS implements IAction {
   private final String a;
   private final String b;

   IActionOnSMS(GlobalMessageHandler var1, String var2, String var3) {
      this.a = var2;
      this.b = var3;
   }

   public final void perform() {
      GameMidlet.doSendSMS(this.a, this.b);
   }
}
