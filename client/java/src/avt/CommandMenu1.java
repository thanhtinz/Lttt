package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMenu1 extends Command {
   private final int f;

   CommandMenu1(MainMenu var1, String var2, IAction var3, int var4) {
      super(var2, var3);
      this.f = var4;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      Menu.imgCmd.drawFrame(this.f, var2, var3, 0, 3, var1);
   }
}
