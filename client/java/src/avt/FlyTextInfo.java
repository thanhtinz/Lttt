package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class FlyTextInfo {
   private String text = "";
   private int x;
   private int y;
   private int state;
   private int delay;
   private boolean isSmall = false;
   private Image img;
   private byte dir;
   private byte normal = -1;
   private short imgID = -1;
   private short imgID_2 = -1;

   public FlyTextInfo(int var1, int var2, int var3, int var4, Image var5, int var6, int var7, int var8) {
      this.delay = var6;
      this.dir = (byte)var4;
      this.x = var1;
      this.y = var2;
      if (var3 > 0) {
         this.text = "+";
      }

      this.text = this.text + var3;
      if (var3 == 0) {
         this.text = "";
      }

      this.img = var5;
      this.isSmall = false;
      this.normal = -1;
      this.imgID = (short)var7;
      this.imgID_2 = (short)var8;
   }

   public FlyTextInfo(int var1, int var2, String var3, int var4, int var5, int var6) {
      this.delay = var6;
      this.dir = (byte)var4;
      this.x = var1;
      this.y = var2;
      this.text = var3;
      this.state = 0;
      this.isSmall = true;
      this.normal = (byte)var5;
      this.imgID = -1;
      this.imgID_2 = -1;
   }

   public final void update() {
      if (this.delay > 0) {
         --this.delay;
      } else {
         ++this.state;
         if (this.state > 40) {
            this.img = null;
            Canvas.flyTexts.removeElement(this);
         }

         if (this.state < 3) {
            this.y += -2 * this.dir;
         } else {
            this.y += this.dir;
         }
      }

   }

   public final void paint(Graphics var1) {
      if (Canvas.currentMyScreen == RaceScr.me) {
         Canvas.resetTrans(var1);
      }

      if (this.delay <= 0) {
         int var2 = AvMain.hd;
         if (Canvas.currentMyScreen == BoardScr.me && (BoardScr.isStartGame || BoardScr.disableReady) || Canvas.currentMyScreen == RaceScr.me) {
            var2 = 1;
         }

         FontX var3 = Canvas.O;
         if (this.isSmall) {
            if (this.normal == 0) {
               var3 = Canvas.smallFontRed;
            } else if (this.normal == 2) {
               var3 = Canvas.smallFontYellow;
            } else if (this.normal == 3) {
               var3 = Canvas.R;
            } else {
               var3 = Canvas.borderFont;
            }
         }

         var3.drawString(var1, this.text, this.x * var2, this.y * var2, 2);
         if (this.img == null) {
            if (this.imgID != -1) {
               FarmData.paintImg(var1, this.imgID, this.x * var2, (this.y - 5) * var2, 33);
               return;
            }

            if (this.imgID_2 != -1) {
               AvatarData.paintImg(var1, this.imgID_2, this.x * var2, (this.y - 5) * var2, 33);
               return;
            }
         } else if (!this.isSmall) {
            var1.drawImage(this.img, this.x * var2, this.y * var2, 33);
         }
      }

   }
}
