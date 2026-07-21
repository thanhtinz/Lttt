package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMenuStarFruit3 extends Command {
   CommandMenuStarFruit3(FarmScr var1, String var2, int var3) {
      super(var2, 14);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.paintImg(var1, 61, var2, var3, 3);
   }
}
