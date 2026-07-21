package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandInfo extends Command {
   private final Avatar ava1;
   private final byte perLv;
   private final byte lv;
   private final int numFish;
   private final short idPart;

   CommandInfo(FishingScr var1, String var2, int var3, Avatar var4, byte var5, byte var6, int var7, short var8) {
      super((String)null, 0);
      this.ava1 = var4;
      this.perLv = var5;
      this.lv = var6;
      this.numFish = var7;
      this.idPart = var8;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      Canvas.resetTrans(var1);
      var2 = PaintPopup.hTab + (AvMain.hDuBox << 1) + 10 * AvMain.hd + 30 * (AvMain.hd - 1) + PopupShop.x;
      byte var5 = AvMain.hNormal;
      this.ava1.paintIcon(var1, Canvas.w / 2, var2, false);
      Canvas.normalFont.drawString(var1, T.nameStr + this.ava1.name, Canvas.w / 2, var2 + var5, 2);
      Canvas.normalFont.drawString(var1, T.roomName[3] + this.perLv + " (" + this.lv + "%)", Canvas.w / 2, var2 + (var5 << 1), 2);
      Canvas.normalFont.drawString(var1, T.numberFish + this.numFish, Canvas.w / 2, var2 + var5 * 3, 2);
      Canvas.normalFont.drawString(var1, T.achieve + ": ", Canvas.w / 2, var2 + (var5 << 2), 2);
      if (this.idPart != -1) {
         ((PartSmall)AvatarData.getPart(this.idPart)).paint(var1, Canvas.w / 2, var2 + var5 * 6, 3);
      }

   }
}
