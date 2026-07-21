package avt;

import main.GameMidlet;

final class IActionSelectAction implements IAction {
   private MapScr a;
   private final int b;

   IActionSelectAction(MapScr var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      if (GameMidlet.avatar.action != 2) {
         MapScr.doAction(MapScr.ac[this.b]);
      }

   }
}
