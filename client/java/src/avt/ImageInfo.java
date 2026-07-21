package avt;

import javax.microedition.lcdui.Graphics;

public final class ImageInfo {
   public short ID;
   public short bigID;
   public short x0;
   public short y0;
   public short w;
   public short h;

   public final void paintPart(Graphics var1, int var2, int var3, int var4) {
      int var10002 = this.x0 * AvMain.hd;
      int var10003 = this.y0 * AvMain.hd;
      int var10004 = this.w * AvMain.hd;
      int var10005 = this.h * AvMain.hd;
      var1.drawRegion(AvatarData.getBigImgInfo(this.bigID).img, var10002, var10003, var10004, var10005, 0, var2, var3, var4);
   }
}
