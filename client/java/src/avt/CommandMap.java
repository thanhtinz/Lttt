package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMap extends Command {
   private final int f;

   CommandMap(HouseScr var1, String var2, int var3, int var4, int var5) {
      super(var2, 17, var4);
      this.f = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      LoadMap.imgMap.drawFrame(this.f, var2 + 1, var3 + 1, 0, 3, var1);
   }
}
