package avt;

import javax.microedition.lcdui.Graphics;

final class CommandVatPham22 extends Command {
   CommandVatPham22(FarmScr var1, String var2, int var3) {
      super(var2, 1);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmScr.p.drawFrame(0, var2, var3, 0, 3, var1);
   }
}
