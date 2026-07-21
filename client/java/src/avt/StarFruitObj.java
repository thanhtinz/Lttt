package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class StarFruitObj extends SubObject {
   public short lv;
   public short productID;
   public short fruitID;
   public short numberFruit;
   public int timeFinish;
   private int w0 = 0;
   private int h0 = 0;
   public long time;
   public byte[] xFruit;
   public byte[] yFruit;

   public final void update() {
      if (System.currentTimeMillis() - this.time >= 1000L) {
         if (this.timeFinish > 0) {
            --this.timeFinish;
            if (this.timeFinish == 0) {
               FarmService var1;
               (var1 = FarmService.gI()).createMessage((byte)83);
               var1.sendMessage();
            }
         }

         this.time = System.currentTimeMillis();
         ImageIcon var4;
         if ((var4 = FarmData.getImgIcon(this.productID)).w > 0 && this.w0 == 0) {
            this.w0 = var4.w / 3 << 1;
            this.h0 = var4.h / 2;
            StarFruitObj var5 = this;
            if (this.numberFruit > 0) {
               int var2 = CRes.rnd(3) + 3;
               this.xFruit = new byte[var2];
               this.yFruit = new byte[var2];

               for(int var3 = 0; var3 < var2; ++var3) {
                  var5.xFruit[var3] = (byte)(CRes.rnd(var5.w0 - 10) - (var5.w0 - 10) / 2);
                  var5.yFruit[var3] = (byte)(CRes.rnd(var5.h0 - 10) - (var5.h0 - 10) / 2);
               }
            }
         }
      }

   }

   public final void paint(Graphics var1) {
      if (super.type >= 0 || super.x * MyObject.hd + this.w0 / 2 >= AvCamera.gI().xCam && super.x * MyObject.hd - this.w0 / 2 <= AvCamera.gI().xCam + Canvas.w) {
         FarmData.paintImg(var1, this.productID, super.x * MyObject.hd, super.y * MyObject.hd, 33);
         int var2;
         if (this.numberFruit > 0 && this.xFruit != null) {
            for(var2 = 0; var2 < this.xFruit.length; ++var2) {
               FarmData.paintImg(var1, this.fruitID, super.x * MyObject.hd + this.xFruit[var2], super.y * MyObject.hd - (FarmData.getImgIcon(this.productID).h / 2 + 5) + this.yFruit[var2], 3);
            }
         }

         var2 = FarmData.getImgIcon(this.productID).h + AvMain.hBorder;
         if (this.timeFinish > 0) {
            var2 += AvMain.hSmall;
         }

         FarmData.paintImg(var1, this.fruitID, (super.x - 8) * MyObject.hd, super.y * MyObject.hd - var2, 3);
         Canvas.borderFont.drawString(var1, "Lv" + this.lv, super.x * MyObject.hd, super.y * MyObject.hd - var2 - AvMain.hBorder / 2, 0);
         if (this.timeFinish > 0) {
            int var3 = this.timeFinish / 3600;
            int var4 = (this.timeFinish - var3 * 3600) / 60;
            int var5 = this.timeFinish - var3 * 3600 - var4 * 60;
            Canvas.smallFontYellow.drawString(var1, var3 + ":" + var4 + ":" + var5, (super.x + 3) * MyObject.hd, super.y * MyObject.hd - var2 + Canvas.borderFont.getHeight() / 2 + 2 * MyObject.hd, 2);
         }
      }

   }
}
