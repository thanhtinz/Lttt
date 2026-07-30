package avt;

import javax.microedition.lcdui.Graphics;

final class CommandMenuRotate extends Command {
   private final StringObj strObj;

   CommandMenuRotate(MapScr var1, String var2, IAction var3, StringObj var4) {
      super(var2, var3);
      this.strObj = var4;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.strObj.dis, var2, var3, 3);
   }
}
