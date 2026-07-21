package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMenuStarFruit2 extends Command {
   CommandMenuStarFruit2(FarmScr var1, String var2, int var3) {
      super(var2, 13);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.paintImg(var1, FarmScr.starFruil.timeFinish > 0 ? 64 : 63, var2, var3, 3);
   }
}
