package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandItem extends Command {
   private final MapItemType itemType;
   private final int height;
   private final String title;
   private final String desc;

   CommandItem(HouseScr var1, String var2, IAction var3, MapItemType var4, int var5, String var6, String var7) {
      super(var2, var3);
      this.itemType = var4;
      this.height = 90;
      this.title = var6;
      this.desc = var7;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.itemType.imgID, var2, var3 + this.height / 2 - AvMain.hBlack - AvMain.hNormal - 5, 33);
      Canvas.fontChatB.drawString(var1, this.title, var2, var3 + this.height / 2 - AvMain.hBlack, 2);
      Canvas.normalFont.drawString(var1, this.desc, var2, var3 + this.height / 2 - AvMain.hBlack - AvMain.hNormal, 2);
   }
}
