package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMenu extends Command {
   private final int frame;

   CommandMenu(MainMenu var1, String var2, int var3, int var4) {
      super(var2, var3);
      this.frame = var4;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      Menu.imgCmd.drawFrameXY(this.frame / Menu.imgCmd.nFrame, this.frame % Menu.imgCmd.nFrame, var2, var3, 3, var1);
   }
}
