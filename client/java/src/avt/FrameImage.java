package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class FrameImage {
   public int frameWidth;
   public int frameHeight;
   public int nFrame;
   public Image imgFrame;
   private Image imgFrameTinted;
   private int tintColor = -1;

   public FrameImage(Image var1, int var2, int var3) {
      this.imgFrame = var1;
      this.frameWidth = var2;
      this.frameHeight = var3;
      this.nFrame = var1.getHeight() / var3;
   }

   public static FrameImage init(String var0, int var1, int var2) {
      return new FrameImage(FilePack.getImage(var0), var1, var2);
   }

   public final FrameImage createTintedCopy(int var0) {
      Image tinted = this.createTintedImage(var0);
      return new FrameImage(tinted, this.frameWidth, this.frameHeight);
   }

   private final Image createTintedImage(int var0) {
      int var1 = this.frameWidth;
      int var2 = this.imgFrame.getHeight();
      int[] var3 = new int[var1 * var2];
      this.imgFrame.getRGB(var3, 0, var1, 0, 0, var1, var2);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if ((var3[var4] & 0x00FFFFFF) != 0) {
            var3[var4] = var0;
         }
      }

      return Image.createRGBImage(var3, var1, var2, true);
   }

   public final void setTintColor(int var0) {
      if (this.tintColor == var0) {
         return;
      }
      this.tintColor = var0;
      this.imgFrameTinted = this.createTintedImage(var0);
   }

   public final void clearTint() {
      this.tintColor = -1;
      this.imgFrameTinted = null;
   }

   public final void drawFrame(int var1, int var2, int var3, int var4, int var5, Graphics var6) {
      if (var1 >= 0 && var1 < this.nFrame) {
         Image var7 = this.imgFrameTinted != null ? this.imgFrameTinted : this.imgFrame;
         var6.drawRegion(var7, 0, var1 * this.frameHeight, this.frameWidth, this.frameHeight, var4, var2, var3, var5);
      }

   }

   public final void drawFrame(int var1, int var2, int var3, int var4, Graphics var5) {
      Image var6 = this.imgFrameTinted != null ? this.imgFrameTinted : this.imgFrame;
      var5.drawRegion(var6, 0, var1 * this.frameHeight, this.frameWidth, this.frameHeight, var4, var2, var3, 0);
   }

   public final void drawFrameXY(int var1, int var2, int var3, int var4, Graphics var5) {
      if (var1 >= 0 && var1 < this.nFrame && var2 >= 0 && var2 * this.frameHeight < this.imgFrame.getHeight() && var1 * this.frameWidth + this.frameWidth <= this.imgFrame.getWidth()) {
         Image var6 = this.imgFrameTinted != null ? this.imgFrameTinted : this.imgFrame;
         var5.drawRegion(var6, var1 * this.frameWidth, var2 * this.frameHeight, this.frameWidth, this.frameHeight, 0, var3, var4, 0);
      }

   }

   public final void drawFrameXY(int var1, int var2, int var3, int var4, int var5, Graphics var6) {
      if (var1 >= 0 && var1 < this.nFrame) {
         Image var7 = this.imgFrameTinted != null ? this.imgFrameTinted : this.imgFrame;
         var6.drawRegion(var7, var1 * this.frameWidth, var2 * this.frameHeight, this.frameWidth, this.frameHeight, 0, var3, var4, var5);
      }

   }
}
