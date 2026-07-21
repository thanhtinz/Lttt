package avt;

import javax.microedition.lcdui.Graphics;

public final class PartFollow extends Part {
   public short color;

   public final void paintIcon(Graphics var1, int var2, int var3, int var4, int var5) {
      APartInfo var6 = (APartInfo)AvatarData.getPart(super.follow);
      if (super.idIcon == var6.imgID[0]) {
         ImageInfo var7 = AvatarData.listImgInfo[var6.imgID[0]];
         int var10002 = var7.x0 * AvMain.hd;
         int var10003 = var7.y0 * AvMain.hd;
         int var10004 = var7.w * AvMain.hd;
         int var10005 = var7.h * AvMain.hd;
         var1.drawRegion(AvatarData.getBigImgInfo(this.color).img, var10002, var10003, var10004, var10005, 0, var2, var3, var5);
      } else {
         var6.paint(var1, var2, var3, var5);
      }

   }

   public final void paintAvatar(Graphics var1, int var2, int var3, int var4, int var5) {
      APartInfo var6 = (APartInfo)AvatarData.getPart(super.follow);
      ImageInfo var7 = AvatarData.listImgInfo[var6.imgID[var2]];
      AvatarData.a(var1, this.color, var7.x0, var7.y0, var7.w, var7.h, var3 + var6.dx[var2] * AvMain.hd - (var5 == Base.LEFT ? (var6.dx[var2] * AvMain.hd << 1) + var7.w * AvMain.hd : 0), var4 + var6.dy[var2] * AvMain.hd, var5, 0);
   }
}
