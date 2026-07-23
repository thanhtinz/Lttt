package avt;

import javax.microedition.lcdui.Graphics;

final class CommandItem55 extends Command {
   private final FarmItem farmItem;

   CommandItem55(FarmScr var1, String var2, IAction var3, FarmItem var4) {
      super(var2, var3);
      this.farmItem = var4;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.farmItem.paint(var1, var2, var3, 0, 3);
   }
}
