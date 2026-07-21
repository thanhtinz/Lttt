package avt;

import javax.microedition.lcdui.Graphics;

final class CommandVatPham222 extends Command {
   CommandVatPham222(FarmScr var1, String var2, IAction var3) {
      super(var2, var3);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmScr.p.drawFrame(1, var2, var3, 0, 3, var1);
   }
}
