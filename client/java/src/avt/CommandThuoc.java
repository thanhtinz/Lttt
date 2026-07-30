package avt;

import javax.microedition.lcdui.Graphics;

final class CommandThuoc extends Command {
   private FarmItem farmItem;

   public CommandThuoc(FarmScr var1, String var2, IAction var3, FarmItem var4) {
      super(var2, var3);
      this.farmItem = var4;
   }

   public CommandThuoc(FarmScr var1, String var2, int var3, int var4, FarmItem var5) {
      super(var2, 6, var4);
      this.farmItem = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.farmItem.paint(var1, var2, var3, 0, 3);
   }
}
