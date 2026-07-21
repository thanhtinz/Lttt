package avt;

import java.util.Vector;

final class IActionDellPart1 implements IAction {
   private final Command a;

   IActionDellPart1(MapScr var1, Command var2) {
      this.a = var2;
   }

   public final void perform() {
      Vector var1;
      (var1 = new Vector()).addElement(this.a);
      var1.addElement(new Command(T.won, new class_ed(this)));
      Menu.gI().startAt(var1, 0);
   }
}
