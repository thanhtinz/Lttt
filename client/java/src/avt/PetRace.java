package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class PetRace extends Base {
   public byte rate;
   public byte count;
   private byte win = 10;
   private byte countWater = -1;
   private byte countFire = -1;
   private byte indexBui;
   private byte indexTe = 6;
   public short idImg;
   public short idIcon;
   public short[] numTick;
   public short[] vTick;
   public int money;
   private int numF = 0;

   public PetRace(RaceMsgHandler var1) {
   }

   public final void update() {
      ++this.indexBui;
      if (this.indexBui >= 10) {
         this.indexBui = 0;
      }

      if (this.indexTe < 9) {
         ++this.indexTe;
      }

      ++this.numF;
      if (this.numF >= 6) {
         this.numF = 0;
      }

      ++super.frame;
      if (super.frame == 12) {
         super.frame = 0;
      }

      if (super.x < (LoadMap.wMap + 1) * LoadMap.w) {
         if (this.numTick != null && this.count < this.numTick.length && RaceScr.gI().countStart <= 0) {
            super.x += this.vTick[this.count];
            if (this.vTick[this.count] == 0) {
               super.action = 2;
            } else {
               super.action = 1;
            }

            --this.numTick[this.count];
            if (this.numTick[this.count] <= 0) {
               ++this.count;
               if (this.count < this.vTick.length) {
                  if (this.indexTe == 9 && this.vTick[this.count] == 0) {
                     this.indexTe = 0;
                  } else if (this.countWater == -1 && this.vTick[this.count] == 2) {
                     this.countWater = 20;
                  } else if (this.countFire == -1 && this.vTick[this.count] == 5) {
                     this.countFire = 20;
                  }
               }
            }
         } else {
            super.action = 0;
            if (this.vTick != null && RaceScr.gI().countStart <= 0) {
               super.x += this.vTick[this.vTick.length - 1];
            }

            if (this.win == 10 && this.numTick != null && this.count >= this.numTick.length) {
               RaceScr var10001 = RaceScr.gI();
               byte var10003 = var10001.nWin;
               var10001.nWin = (byte)(var10003 + 1);
               this.win = var10003;
            }
         }

         if (this.countWater >= 0) {
            --this.countWater;
         }

         if (this.countFire >= 0) {
            --this.countFire;
         }
      }

   }

   public final void paint(Graphics var1) {
      ImageIcon var2;
      if ((var2 = AvatarData.getImgIcon(this.idImg)).count != -1) {
         int var3 = var2.h / 5;
         var1.drawRegion(var2.img, 0, RaceScr.FRAME[super.action][super.frame] * var3, var2.w, var3, 0, super.x * MyObject.hd, super.y * MyObject.hd, 33);
         if (RaceScr.gI().isStart && this.money > 0) {
            Canvas.M.drawString(var1, "" + this.money, super.x * MyObject.hd - var2.w / 2 - 8 * MyObject.hd, super.y * MyObject.hd - AvMain.hBlack / 2 - 3 * MyObject.hd, 1);
         }

         if (this.countWater >= 0) {
            var1.drawImage(RaceScr.imgWater, super.x * MyObject.hd + var2.w / 2, super.y * MyObject.hd - var3, 33);
         }

         if (this.indexTe < 9) {
            var1.drawImage(RaceScr.imgTe[this.indexTe / 3], super.x * MyObject.hd, super.y * MyObject.hd, 3);
         }

         if (this.countFire >= 0) {
            var1.drawImage(RaceScr.imgFire, super.x * MyObject.hd + var2.w / 2, super.y * MyObject.hd - var3, 33);
            var1.drawImage(RaceScr.imgBui[this.indexBui / 2], super.x * MyObject.hd - var2.w / 2, super.y * MyObject.hd, 3);
         }

         if (super.IDDB == AvCamera.gI().followPlayer.IDDB) {
            var1.drawImage(MapScr.imgFocusP, super.x * MyObject.hd, super.y * MyObject.hd - var3 - this.numF / 2 - 10 * MyObject.hd, 3);
         }
      }

   }
}
