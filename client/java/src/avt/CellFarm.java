package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class CellFarm extends SubObject {
   public int xCell;
   public int yCell;
   public int idTree;
   public short time;
   public short time1 = -1;
   public long tempTime;
   public boolean isArid;
   public boolean isWorm;
   public boolean isGrass;
   public boolean isSelected = false;
   public byte hervestPer;
   public byte vitalityPer;
   public byte status;
   public byte level;
   public int statusTree;

   public final void paint(Graphics var1) {
      if (super.x * MyObject.hd >= AvCamera.gI().xCam - 10 && this.xCell * MyObject.hd <= AvCamera.gI().xCam + Canvas.w + 10) {
         if (this.isSelected) {
            var1.drawImage(FarmScr.imgSell, (super.x - 13) * MyObject.hd, (super.y - 18) * MyObject.hd, 0);
         }

         int var2 = FarmScr.focusCell.x;
         int var3 = FarmScr.focusCell.y;
         if (this.xCell == var2 && this.yCell == var3) {
            Canvas.smallFontYellow.drawString(var1, "lv" + this.level, super.x * MyObject.hd, (super.y - 44) * MyObject.hd, 2);
         }

         if (this.idTree != -1) {
            TreeInfo var4;
            (var4 = FarmData.getTreeByID(this.idTree)).paint(var1, this.statusTree, super.x * MyObject.hd, super.y * MyObject.hd, 33);
            int var5 = var4.harvestTime * 60 + var4.dieTime * 60;
            if ((this.time <= var5 || var4.dieTime == -1) && this.hervestPer != 100 && this.time >= 0) {
               if (this.isGrass) {
                  var1.drawImage(FarmScr.imgWorm_G[1], (super.x + 5) * MyObject.hd, (super.y - 12) * MyObject.hd, 3);
               }

               if (this.isWorm) {
                  var1.drawImage(FarmScr.imgWorm_G[0], (super.x - 7) * MyObject.hd, super.y * MyObject.hd, 3);
               }

               if (this.xCell == var2 && this.yCell == var3 || this.time1 != -1 && this.time1 == FarmScr.idSelected) {
                  var2 = var2 * 24 * MyObject.hd;
                  var3 = var3 * 24 * MyObject.hd;
                  if (this.time1 != -1 && this.time1 == FarmScr.idSelected) {
                     var2 = this.xCell * 24 * MyObject.hd;
                     var3 = this.yCell * 24 * MyObject.hd;
                  }

                  var4.paint(var1, 7, var2 - 3, var3 - 40 * MyObject.hd, 33);
                  var1.setColor(1);
                  var1.fillRect(var2 - 4 * MyObject.hd, var3 - 38 * MyObject.hd, 31 * MyObject.hd, 5 * MyObject.hd);
                  var1.setColor(65280);
                  var1.fillRect(var2 - 3 * MyObject.hd, var3 - 37 * MyObject.hd, this.vitalityPer * 30 / 100 * MyObject.hd, 3 * MyObject.hd);
                  var1.setColor(2512938);
                  var1.drawRect(var2 - 4 * MyObject.hd, var3 - 38 * MyObject.hd, 31 * MyObject.hd, 4 * MyObject.hd);
                  long var10 = (long)(var4.harvestTime * 60 - this.time);
                  long var8 = var10 * 60L;
                  String var20 = "";
                  if (var8 < 0L) {
                     var8 = 0L;
                  }

                  long var13 = var8 / 60L / 60L;
                  long var15 = var8 / 60L % 60L;
                  long var17 = var8 % 60L;
                  var20 = var20 + var13 + ":" + var15 + ":" + var17;
                  if (var10 <= 0L) {
                     var20 = T.canHarvest;
                  }

                  Canvas.smallFontYellow.drawString(var1, var20, var2 + 5 * MyObject.hd, var3 - 49 * MyObject.hd, 0);
                  if ((var5 = this.time * 100 / (var4.harvestTime * 60) * 30 / 100) == 0) {
                     var5 = 1;
                  }

                  if (var5 >= 30) {
                     var5 = 29;
                  }

                  if (var4.harvestTime * 60 - this.time < 0) {
                     var5 = 30;
                  }

                  var1.setColor(1);
                  var1.fillRect(var2 - 4 * MyObject.hd, var3 - 32 * MyObject.hd, 31 * MyObject.hd, 5 * MyObject.hd);
                  var1.setColor(255, 255, 0);
                  var1.fillRect(var2 - 3 * MyObject.hd, var3 - 31 * MyObject.hd, var5 * MyObject.hd, 3 * MyObject.hd);
                  var1.setColor(2512938);
                  var1.drawRect(var2 - 4 * MyObject.hd, var3 - 32 * MyObject.hd, 31 * MyObject.hd, 4 * MyObject.hd);
                  byte var19 = 0;
                  if (this.isGrass) {
                     var19 = 1;
                     FarmScr.q.drawFrame(1, var2 + (5 + (this.isWorm ? 6 : 0)) * MyObject.hd, var3 - 22 * MyObject.hd, 0, var1);
                  }

                  if (this.isWorm) {
                     FarmScr.q.drawFrame(0, var2 + (4 - var19 * 6) * MyObject.hd, var3 - 22 * MyObject.hd, 0, var1);
                  }
               }
            }
         }
      }

   }
}
