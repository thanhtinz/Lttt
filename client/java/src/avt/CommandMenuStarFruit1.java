package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMenuStarFruit1 extends Command {
   CommandMenuStarFruit1(FarmScr var1, String var2, int var3) {
      super(var2, 12);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.paintImg(var1, 62, var2, var3, 3);
   }
}
