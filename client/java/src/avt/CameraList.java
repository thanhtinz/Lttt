package avt;

import main.Canvas;

public final class CameraList {
   public int cmdy;
   public int cmvy;
   public int cmyLim;
   private int h;
   private int disY;
   public int disX;
   private int sizeW;
   private int sizeH;
   private int wOne;
   private int hOne;
   private int x;
   public int y;
   private int size;
   public int cmdx;
   public int cmvx;
   public int cmxLim;
   public static int cmtoY;
   public static int cmy;
   public static int cmtoX;
   public static int cmx;
   private int selected;
   public boolean isShow = false;
   private boolean isQuaTrang = false;
   private long timeDelay;
   private long count = 0L;
   private boolean C;
   private int D = 0;
   private int E = 0;
   public int n;
   public int o;
   private int F;
   private int G;
   private int H;
   public boolean p = false;
   private boolean I = false;
   private long J = 0L;
   private long K = 0L;
   private int L;
   private int M;

   public final void setInfo(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      this.isQuaTrang = false;
      this.x = var1;
      this.y = var2;
      this.sizeH = var6 / var4;
      this.sizeW = var5 / var3;
      this.size = var9;
      this.wOne = var3;
      this.hOne = var4;
      this.h = var5;
      this.disY = var8;
      this.disX = var7;
      this.selected = 0;
      cmy = 0;
      cmtoY = 0;
      this.cmyLim = var6 - this.disY;
      if (this.cmyLim < 0) {
         this.cmyLim = 0;
      }

      cmx = 0;
      cmtoX = 0;
      this.cmxLim = var5 - this.disX;
      if (this.cmxLim < 0) {
         this.cmxLim = 0;
      }

      this.isShow = true;
      this.count = 0L;
   }

   public final void setSelect(int var1) {
      this.selected = var1;
      this.setCam();
   }

   public final void updateKey() {
      ++this.count;
      this.C = false;
      if (Canvas.keyPressed[8]) {
         this.selected += this.sizeW;
         if (this.selected >= this.size) {
            this.selected = 0;
         }
      } else if (Canvas.keyPressed[2]) {
         this.selected -= this.sizeW;
         if (this.selected < 0) {
            this.selected = this.size - 1;
         }
      } else if (Canvas.keyPressed[6]) {
         ++this.selected;
         if (this.selected >= this.size) {
            this.selected = 0;
         }
      } else if (Canvas.keyPressed[4]) {
         --this.selected;
         if (this.selected < 0) {
            this.selected = this.size - 1;
         }
      }

      if (Canvas.keyPressed[4] || Canvas.keyPressed[6] || Canvas.keyPressed[8] || Canvas.keyPressed[2]) {
         this.C = true;
         Canvas.currentMyScreen.setSelected(this.selected, false);
         Canvas.keyPressed[4] = false;
         Canvas.keyPressed[6] = false;
         Canvas.keyPressed[8] = false;
         Canvas.keyPressed[2] = false;
      }

      if (this.C) {
         this.setCam();
      }

      if (Canvas.menuMain == null && Canvas.currentDialog == null) {
         if (this.H > 0) {
            --this.H;
            if (this.H == 0 && Canvas.currentMyScreen != PopupShop.me) {
               Canvas.currentMyScreen.setSelected(this.selected, true);
            }
         } else {
            if (Canvas.isPointerClick && Canvas.isPointer(this.x, this.y, this.h, this.disY)) {
               this.M = Canvas.pyLast;
               this.L = Canvas.pxLast;
               Canvas.isPointerClick = false;
               this.timeDelay = this.count;
               this.D = cmtoY;
               this.E = cmtoX;
               this.p = true;
               this.n = 0;
               this.o = 0;
            }

            if (this.p) {
               long var1 = this.count - this.timeDelay;
               int var3 = this.M - Canvas.py;
               this.M = Canvas.py;
               int var4 = this.L - Canvas.px;
               this.L = Canvas.px;
               if (Canvas.isPointerDown) {
                  if (this.count % 2L == 0L) {
                     this.F = Canvas.py;
                     this.G = Canvas.px;
                     this.J = this.count;
                     this.K = this.count;
                  }

                  this.n = 0;
                  this.o = 0;
                  if (cmy > 0 && cmy < this.cmyLim) {
                     cmy = this.D + var3;
                     this.D = cmy;
                  } else {
                     cmy = this.D + Canvas.dy() / 2;
                  }

                  if (cmx > 0 && cmx < this.cmxLim) {
                     cmx = this.E + var4;
                     this.E = cmx;
                  } else {
                     cmx = this.E + Canvas.dx() / 2;
                  }

                  cmtoY = cmy;
                  cmtoX = cmx;
                  if (var1 < 20L) {
                     var3 = (cmy + Canvas.py - this.y) / this.hOne;
                     var4 = (cmx + Canvas.px - this.x) / this.wOne;
                     this.selected = var3 * this.sizeW + var4;
                     if (this.selected < 0) {
                        this.selected = 0;
                     }

                     if (this.selected >= this.sizeH * this.sizeW) {
                        this.selected = this.sizeH * this.sizeW - 1;
                     }

                     Canvas.currentMyScreen.setSelected(this.selected, false);
                  }

                  if (CRes.abs(Canvas.dy()) < 10 * AvMain.hd && CRes.abs(Canvas.dx()) < 10 * AvMain.hd) {
                     if (var1 > 3L && var1 < 8L) {
                        this.I = false;
                        Canvas.currentMyScreen.setHidePointer(false);
                     }
                  } else {
                     Canvas.currentMyScreen.setHidePointer(true);
                  }
               }

               if (Canvas.isPointerRelease) {
                  this.p = false;
                  var3 = (int)(this.count - this.J);
                  var4 = this.F - Canvas.py;
                  int var5 = this.G - Canvas.px;
                  if (CRes.abs(var4) > 40 && var3 < 20 && cmy > 0 && cmy < this.cmyLim) {
                     this.n = var4 / var3 * 10;
                  }

                  var3 = (int)(this.count - this.K);
                  if (CRes.abs(var5) > 40 && var3 < 20 && cmx > 0 && cmx < this.cmxLim) {
                     this.o = var5 / var3 * 10;
                  }

                  this.J = -1L;
                  this.K = -1L;
                  if (CRes.abs(Canvas.dy()) < 10 * AvMain.hd && CRes.abs(Canvas.dx()) < 10 * AvMain.hd) {
                     if (var1 <= 4L) {
                        this.H = 5;
                        Canvas.currentMyScreen.setHidePointer(false);
                     } else {
                        Canvas.currentMyScreen.setSelected(this.selected, true);
                        if (Canvas.currentMyScreen != PopupShop.me) {
                           Canvas.currentMyScreen.setHidePointer(true);
                        }
                     }

                     this.I = false;
                  }

                  Canvas.isPointerRelease = false;
               }
            }
         }
      }

   }

   private void setCam() {
      if (!Canvas.isPointerDown) {
         if ((cmy = this.selected / this.sizeW * this.hOne - this.disY / 2 + this.hOne / 2) < 0) {
            cmy = 0;
         }

         if (cmy > this.cmyLim) {
            cmy = this.cmyLim;
         }

         if (this.selected / this.sizeW > this.sizeH - 1 || this.selected / this.sizeW == 0) {
            cmtoY = cmy;
         }

         if ((cmx = this.selected % this.sizeW * this.wOne - this.disX / 2 + this.wOne / 2) < 0) {
            cmx = 0;
         }

         if (cmx > this.cmxLim) {
            cmx = this.cmxLim;
         }

         if (this.selected % this.sizeW > this.sizeW - 1 || this.selected % this.sizeW == 0) {
            cmtoX = cmx;
         }
      }

   }
}
