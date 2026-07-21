package avt;

import javax.microedition.lcdui.Graphics;

final class CommandFlower extends Command {
   private final short idImg;

   CommandFlower(GlobalMessageHandler var1, String var2, IAction var3, short var4) {
      super(var2, var3);
      this.idImg = var4;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.idImg, var2, var3, 3);
   }
}
