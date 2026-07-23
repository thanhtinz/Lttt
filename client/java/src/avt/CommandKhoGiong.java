package avt;

import javax.microedition.lcdui.Graphics;

final class CommandKhoGiong extends Command {
   private final Item item;

   CommandKhoGiong(FarmScr var1, String var2, int var3, int var4, Item var5) {
      super(var2, 7, var4);
      this.item = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.getTreeByID(this.item.ID).paint(var1, 7, var2, var3, 3);
   }
}
