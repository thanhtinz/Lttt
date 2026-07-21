package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandItem extends Command {
   private final MapItemType f;
   private final int g;
   private final String h;
   private final String i;

   CommandItem(HouseScr var1, String var2, IAction var3, MapItemType var4, int var5, String var6, String var7) {
      super(var2, var3);
      this.f = var4;
      this.g = 90;
      this.h = var6;
      this.i = var7;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.f.imgID, var2, var3 + this.g / 2 - AvMain.hBlack - AvMain.hNormal - 5, 33);
      Canvas.fontChatB.drawString(var1, this.h, var2, var3 + this.g / 2 - AvMain.hBlack, 2);
      Canvas.normalFont.drawString(var1, this.i, var2, var3 + this.g / 2 - AvMain.hBlack - AvMain.hNormal, 2);
   }
}
