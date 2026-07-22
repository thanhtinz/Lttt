package avt;

import javax.microedition.lcdui.Graphics;

public final class APartInfo extends Part {
   public byte level;
   public byte gender;
   public short[] imgID;
   public byte[] dx;
   public byte[] dy;

   public final void paintAvatar(Graphics var1, int var2, int var3, int var4, int var5) {
      if (super.IDPart != -1) {
         if (super.IDPart >= 2000) {
            ImageIcon var6;
            if ((var6 = AvatarData.getImagePart(this.imgID[var2])).count != -1) {
               var1.drawRegion(var6.img, 0, 0, var6.w, var6.h, var5, var3 + this.dx[var2] * AvMain.hd - (var5 == Base.LEFT ? (this.dx[var2] * AvMain.hd << 1) + var6.w : 0), var4 + this.dy[var2] * AvMain.hd, 0);
               return;
            }
         } else {
            ImageInfo var7 = AvatarData.listImgInfo[this.imgID[var2]];
            AvatarData.drawImgRegion(var1, var7.bigID, var7.x0, var7.y0, var7.w, var7.h, var3 + this.dx[var2] * AvMain.hd - (var5 == Base.LEFT ? (this.dx[var2] * AvMain.hd << 1) + var7.w * AvMain.hd : 0), var4 + this.dy[var2] * AvMain.hd, var5, 0);
         }
      }

   }
}
