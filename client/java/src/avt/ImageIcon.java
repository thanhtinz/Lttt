package avt;

import javax.microedition.lcdui.Image;

public final class ImageIcon {
   public Image img;
   public short w;
   public short h;
   public int count = -1;

   public ImageIcon() {
   }

   public ImageIcon(Image var1) {
      this.img = var1;
      this.count = 0;
      this.w = (short)var1.getWidth();
      this.h = (short)var1.getHeight();
   }
}
