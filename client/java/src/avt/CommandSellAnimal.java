package avt;

import javax.microedition.lcdui.Graphics;

final class CommandSellAnimal extends Command {
   CommandSellAnimal(FarmScr var1, String var2, int var3) {
      super(var2, 2);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      var1.drawImage(FarmScr.imgFocusCel, var2, var3, 3);
   }
}
