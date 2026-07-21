package avt;

import javax.microedition.lcdui.Graphics;

final class class_bn extends Command {
   private final Item f;

   class_bn(FarmScr var1, String var2, int var3, int var4, Item var5) {
      super(var2, 5, var4);
      this.f = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.getTreeByID(this.f.ID).a(var1, 7, var2, var3, 3);
   }
}
