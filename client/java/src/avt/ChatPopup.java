package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class ChatPopup {
   private int timeOut;
   public int xc;
   public int yc;
   public int h;
   private int w;
   private static short hText;
   public String[] chats;
   private static FrameImage[] imgPopup = new FrameImage[2];
   private static Image[] imgArrow = new Image[2];
   private byte iNPC = 0;

   public ChatPopup() {
   }

   public ChatPopup(int var1, String var2, byte var3) {
      this.iNPC = var3;
      this.prepareData(var1, var2);
   }

   public final void setPos(int var1, int var2) {
      this.xc = var1;
      this.yc = var2;
   }

   public final boolean setOut() {
      if (this.timeOut > 0) {
         --this.timeOut;
      }

      if (this.timeOut == 0) {
         return true;
      } else {
         if (Canvas.currentMyScreen == BoardScr.me) {
            if (this.yc - this.h < 0) {
               this.yc = this.h + 10;
            }

            if (this.xc - 30 < 0) {
               this.xc = 32;
            }

            if (this.xc + 30 > Canvas.w) {
               this.xc = Canvas.w - 40;
            }
         }

         return false;
      }
   }

   public final void prepareData(int var1, String var2) {
      this.w = 80 * AvMain.hd;
      this.chats = Canvas.fontChatB.splitFontBStrInLine(var2, this.w - 25);
      this.h = AvMain.hBlack * this.chats.length + 4 + 4;
      if (this.h < hText << 1) {
         this.h = hText << 1;
      }

      if (this.chats.length == 1) {
         this.w = Canvas.fontChatB.getWidth(this.chats[0]) + 20;
      }

      if (this.w < 30 * AvMain.hd) {
         this.w = 30 * AvMain.hd;
      }

      this.timeOut = var1;
   }

   public final void paintAnimal(Graphics var1) {
      int var2 = AvMain.hd;
      if (Canvas.currentMyScreen == BoardScr.me) {
         var2 = 1;
      }

      paintRoundRect(var1, this.xc * var2 - this.w / 2, this.yc * var2 - this.h, this.w, this.h, this.iNPC == 1 ? 16773580 : 16777215, this.iNPC == 1 ? 14957056 : 1, this.iNPC);
      var1.drawImage(imgArrow[this.iNPC], this.xc * var2, this.yc * var2 - 1, 17);
      byte var3 = AvMain.hBlack;

      for(int var4 = 0; var4 < this.chats.length; ++var4) {
         Canvas.fontChatB.drawString(var1, this.chats[var4], this.xc * var2 - this.w / 2 + this.w / 2, this.yc * var2 - this.h / 2 + var4 * var3 - this.chats.length * var3 / 2, 2);
      }

   }

   public static void paintRoundRect(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, byte var7) {
      imgPopup[var7].drawFrame(0, var1, var2, 0, var0);
      imgPopup[var7].drawFrame(1, var1 + var3 - hText, var2, 0, var0);
      imgPopup[var7].drawFrame(3, var1, var2 + var4 - hText, 0, var0);
      imgPopup[var7].drawFrame(2, var1 + var3 - hText, var2 + var4 - hText, 0, var0);
      var0.setColor(var5);
      var0.fillRect(var1 + hText, var2, var3 - (hText << 1), hText);
      var0.fillRect(var1 + hText, var2 + var4 - hText, var3 - (hText << 1), hText - 1);
      var0.fillRect(var1, var2 + hText, var3, var4 - (hText << 1));
      var0.setColor(var6);
      var0.fillRect(var1 + hText, var2, var3 - (hText << 1), 1);
      var0.fillRect(var1 + hText, var2 + var4 - 1, var3 - (hText << 1), 1);
      var0.fillRect(var1, var2 + hText, 1, var4 - (hText << 1));
      var0.fillRect(var1 + var3 - 1, var2 + hText, 1, var4 - (hText << 1));
   }

   static {
      FilePack.init(T.aw);
      hText = 8;
      imgPopup[0] = FrameImage.init("c", hText, hText);
      imgPopup[1] = FrameImage.init("cB", hText, hText);
      imgArrow[0] = FilePack.getImage("ar");
      imgArrow[1] = FilePack.getImage("ara");
      FilePack.reset();
   }
}
