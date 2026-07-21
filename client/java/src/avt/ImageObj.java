package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class ImageObj extends SubObject {
   private Image img;

   public ImageObj(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, 0);
      FilePack.b(T.at);
      this.img = FilePack.getImage("" + var1);
      if (this.img != null) {
         this.img.getWidth();
      }

      FilePack.reset();
   }

   public final void update() {
   }

   public final void paint(Graphics var1) {
      if (this.img == null) {
         super.g = AvatarData.getImgIcon((short)super.type).w;
         AvatarData.paintImg(var1, super.type, super.x * MyObject.hd, super.y * MyObject.hd, 33);
      } else {
         var1.drawImage(this.img, super.x * MyObject.hd, super.y * MyObject.hd, 33);
      }

      if (super.type == 846) {
         Canvas.fontChatB.drawString(var1, String.valueOf(MapScr.boardID), super.x * MyObject.hd, super.y * MyObject.hd - 30 * MyObject.hd, 2);
      } else if (super.type == 1029 && FarmScr.foodID != 0) {
         FarmItem var2 = FarmScr.getFarmItem(FarmData.getFoodByID(FarmScr.foodID).productID);
         String var3 = "";
         int var4;
         if ((var4 = FarmScr.remainTime / 3600) > 0) {
            var3 = var4 + ":";
         }

         int var5;
         if ((var5 = (FarmScr.remainTime - var4 * 3600) / 60) > 0 || var4 > 0) {
            var3 = var3 + var5 + ":";
         }

         var4 = FarmScr.remainTime - var4 * 3600 - var5 * 60;
         var3 = var3 + var4;
         if (FarmScr.remainTime == 0) {
            var3 = "hoan thanh";
         }

         FarmScr.xPosCook = super.x - Canvas.smallFontYellow.getWidth(var3) / 2 / MyObject.hd;
         FarmScr.yPosCook = super.y - AvatarData.getImgIcon((short)super.type).h / MyObject.hd - 10;
         FarmData.paintImg(var1, var2.IDImg, super.x * MyObject.hd - Canvas.smallFontYellow.getWidth(var3) / 2, super.y * MyObject.hd - AvatarData.getImgIcon((short)super.type).h - 10 * MyObject.hd, 3);
         Canvas.smallFontYellow.drawString(var1, var3, super.x * MyObject.hd - Canvas.smallFontYellow.getWidth(var3) / 2 + 10 * MyObject.hd, super.y * MyObject.hd - AvatarData.getImgIcon((short)super.type).h - 10 * MyObject.hd - AvMain.hSmall / 2 + 2, 0);
      }

   }
}
