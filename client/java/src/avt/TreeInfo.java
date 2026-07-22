package avt;

import javax.microedition.lcdui.Graphics;

public final class TreeInfo {
   public String name;
   public short ID;
   public short[] idImg;
   public byte[] Phase;
   public short harvestTime;
   public short dieTime = -1;
   public short[] priceSeed = new short[2];
   public short priceProduct;
   public short numProduct;
   public short productID;
   public String name1;
   public boolean isDynamic = false;
   public byte lv = 1;

   public final void paint(Graphics var1, int var2, int var3, int var4, int var5) {
      if (this.isDynamic) {
         FarmData.paintImg(var1, this.idImg[var2], var3, var4, var5);
      } else {
         if (var2 < 0 || var2 >= this.idImg.length || this.idImg[var2] < 0 || this.idImg[var2] >= FarmData.listImgInfo.length) {
            return;
         }
         ImageInfo var10000 = FarmData.listImgInfo[this.idImg[var2]];
         var1.drawRegion(FarmData.imgBig[var10000.bigID], var10000.x0 * AvMain.hd, var10000.y0 * AvMain.hd, var10000.w * AvMain.hd, var10000.h * AvMain.hd, 0, var3, var4, var5);
      }

   }
}
