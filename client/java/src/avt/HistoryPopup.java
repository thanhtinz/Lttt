package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class HistoryPopup extends Dialog {
   private short[] idPet;
   private String[] time;
   private int w;
   private int h;
   private int hCell;
   private int cmtoY;
   private int cmy;
   private int cmdy;
   private int cmvy;
   private int cmyLim;
   private int pa = 0;
   private boolean trans = false;
   private int vY;
   private int count;
   private int timePoint;
   private int dyTran;

   public HistoryPopup(RaceMsgHandler var1, short[] var2, String[] var3) {
      this.idPet = var2;
      this.time = var3;
      super.center = new Command(T.OK, (IAction)null);
      this.h = 150 * AvMain.hd;
      this.w = 200 * AvMain.hd;
      this.w = 0;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         int var4;
         if ((var4 = Canvas.normalFont.getWidth(var3[var5]) + 40 * AvMain.hd) > this.w) {
            this.w = var4;
         }
      }

      this.hCell = AvMain.hBorder + 5 * AvMain.hd;
      this.cmyLim = var2.length * this.hCell - (this.h - 10 * AvMain.hd);
      if (this.cmyLim < 0) {
         this.cmyLim = 0;
      }

   }

   public final void updateKey() {
      ++this.count;
      boolean var1 = false;
      if (Canvas.isPointerClick && Canvas.isPointer((Canvas.w - this.w) / 2, (Canvas.h - this.h) / 2, this.w, this.h) && !this.trans) {
         this.pa = this.cmy;
         this.trans = true;
         this.vY = 0;
      }

      if (this.trans) {
         int var2 = Canvas.dy();
         if (Canvas.isPointerDown) {
            if (Canvas.gameTick % 3 == 0) {
               this.dyTran = Canvas.py;
               this.timePoint = this.count;
            }

            this.cmtoY = this.pa + var2;
            this.vY = 0;
            if (this.cmtoY < 0 || this.cmtoY > this.cmyLim) {
               this.cmtoY = this.pa + var2 / 2;
            }

            this.cmy = this.cmtoY;
         }

         if (Canvas.isPointerRelease) {
            this.trans = false;
            int var3 = this.count - this.timePoint;
            int var4;
            if (CRes.abs(var4 = this.dyTran - Canvas.py) > 40 && var3 < 10 && this.cmtoY > 0 && this.cmtoY < this.cmyLim) {
               this.vY = var4 / var3 * 10;
            }

            this.timePoint = -1;
            if (Math.abs(var2) < 10) {
               this.cmtoY = this.pa + var2;
            }
         }
      }

      if (Canvas.keyHold[2]) {
         this.cmtoY -= AvMain.hBorder;
         var1 = true;
      } else if (Canvas.keyHold[8]) {
         var1 = true;
         this.cmtoY += AvMain.hBorder;
      }

      if (var1) {
         if (this.cmtoY < 0) {
            this.cmtoY = 0;
         }

         if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
         }
      }

      if (this.vY != 0) {
         if (this.cmy < 0 || this.cmy > this.cmyLim) {
            this.vY -= this.vY / 4;
            this.cmy += this.vY / 20;
            if (this.vY / 10 <= 1) {
               this.vY = 0;
            }
         }

         if (this.cmy < 0) {
            if (this.cmy < -this.h / 2) {
               this.cmy = -this.h / 2;
               this.cmtoY = 0;
               this.vY = 0;
            }
         } else if (this.cmy > this.cmyLim) {
            if (this.cmy < this.cmyLim + this.h / 2) {
               this.cmy = this.cmyLim + this.h / 2;
               this.cmtoY = this.cmyLim;
               this.vY = 0;
            }
         } else {
            this.cmy += this.vY / 10;
         }

         this.cmtoY = this.cmy;
         this.vY -= this.vY / 10;
         if (this.vY / 10 == 0) {
            this.vY = 0;
         }
      } else if (this.cmy < 0) {
         this.cmtoY = 0;
      } else if (this.cmy > this.cmyLim) {
         this.cmtoY = this.cmyLim;
      }

      if (this.cmy != this.cmtoY) {
         this.cmvy = this.cmtoY - this.cmy << 2;
         this.cmdy += this.cmvy;
         this.cmy += this.cmdy >> 4;
         this.cmdy &= 15;
      }

      super.updateKey();
   }

   public final void paint(Graphics var1) {
      Canvas.paint.paintBoxTab(var1, (Canvas.w - this.w) / 2, (Canvas.hCan - this.h) / 2 - (PaintPopup.hTab + 3 * AvMain.hd), this.h + PaintPopup.hTab + 3 * AvMain.hd, this.w, 0, 0, PaintPopup.gI().wSub, PaintPopup.gI().wTab, PaintPopup.hTab, 1, 1, PaintPopup.gI().count, PaintPopup.gI().colorTab, "Lịch sử");
      Canvas.resetTrans(var1);
      var1.translate((Canvas.w - this.w) / 2, (Canvas.hCan - this.h) / 2);
      var1.setClip(0, 5 * AvMain.hd, this.w, this.h - 10 * AvMain.hd);
      var1.translate(0, -this.cmy);

      for(int var2 = 0; var2 < this.idPet.length; ++var2) {
         AvatarData.paintImg(var1, this.idPet[var2], 15 * AvMain.hd, 15 * AvMain.hd + var2 * this.hCell, 3);
         Canvas.normalFont.drawString(var1, this.time[var2], 35 * AvMain.hd, 15 * AvMain.hd + var2 * this.hCell - AvMain.hBorder / 2, 0);
      }

      super.paint(var1);
   }
}
