package avt;

import javax.microedition.lcdui.Graphics;

public abstract class Part {
   public short follow;
   public short IDPart;
   public short idIcon;
   public int[] price = new int[2];
   public byte zOrder;
   public byte sell;
   public String name;

   public final void paint(Graphics var1, int var2, int var3, int var4) {
      if (this.IDPart != -1) {
         if (this.IDPart >= 2000) {
            boolean var7 = false;
            short var9 = this.idIcon;
            ImageIcon var10;
            if ((var10 = AvatarData.getImagePart(var9)).count != -1 || this.IDPart == -1) {
               var1.drawRegion(var10.img, 0, 0, var10.w, var10.h, 0, var2, var3, var4);
            }

            return;
         }

         AvatarData.listImgInfo[this.idIcon].paintPart(var1, var2, var3, var4);
      }

   }

   public void paintIcon(Graphics var1, int var2, int var3, int var4, int var5) {
      this.paint(var1, var2, var3, var5);
   }

   public void paintAvatar(Graphics var1, int var2, int var3, int var4, int var5) {
   }
}
