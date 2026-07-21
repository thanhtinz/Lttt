package avt;

import javax.microedition.lcdui.Graphics;

final class class_bp extends Command {
   class_bp(FarmScr var1, String var2, int var3) {
      super(var2, 11);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.paintImg(var1, 65, var2, var3, 3);
   }
}
