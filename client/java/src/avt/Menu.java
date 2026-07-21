package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class Menu extends MenuMain {
   private static Menu me;
   private Vector list;
   private int selected;
   private int chan;
   public int menuX;
   public int menuY;
   public int menuW;
   public int menuH;
   private int m;
   public int e;
   public static FrameImage imgCmd;
   private boolean showMenuFarm = false;
   private int cmtoY;
   private int cmy;
   private int cmdy;
   private int cmvy;
   private int cmyLim;
   private int xL;
   private int size = 0;
   private static Command v;
   public static IAction iNo;
   public static short[] h;
   private int vY;
   private int x;
   private int y = 0;
   private int z;
   private boolean A = false;
   private long B;
   private long count;
   private long D;

   public static Menu gI() {
      return me == null ? (me = new Menu()) : me;
   }

   public Menu() {
      this.initCmd();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.click();
            return;
         case 1:
            this.showMenuFarm = false;
            Canvas.menuMain = null;
            if (iNo != null) {
               iNo.perform();
            }
         default:
      }
   }

   public final void initCmd() {
      if (Canvas.stypeInt == 0) {
         super.left = new Command(T.selectt, 0);
      }

      super.right = new Command(T.close, 1);
   }

   public final void startMenuFarm(Vector var1, int var2, int var3, int var4) {
      if (var1.size() != 0) {
         if (Canvas.stypeInt > 0) {
            super.isHide_ = true;
         }

         this.size = var1.size();
         this.xL = Canvas.h;
         this.showMenuFarm = true;
         this.menuW = this.size * var3 + (AvMain.hDuBox << 1) + 4;
         if (this.menuW > Canvas.w) {
            this.menuW = Canvas.w;
         }

         this.menuX = var2 - this.menuW / 2;
         this.menuH = var4 + (AvMain.hDuBox << 1) + 4;
         if (this.menuX < 0) {
            this.menuX = 0;
         }

         this.menuY = Canvas.hCan - Canvas.hTab - this.menuH - (AvMain.hDuBox << 1);
         this.m = this.menuY;
         this.e = var4;
         this.list = var1;
         this.setSelected();
         this.cmyLim = this.size * this.e - (this.menuW - (AvMain.hDuBox << 1) - 4);
         if (this.cmyLim < 0) {
            this.cmyLim = 0;
         }

         this.x = this.menuW;
         v = null;
         iNo = null;
         h = null;
         Canvas.menuMain = this;
      }

   }

   private void setSelected() {
      if (this.selected < 0) {
         this.selected = 0;
      }

      if (this.selected >= this.size) {
         this.selected = 0;
      }

   }

   public final void startAt(Vector var1, int var2) {
      if (var1.size() != 0) {
         if (Canvas.stypeInt > 0) {
            super.isHide_ = true;
         }

         this.e = MyScreen.hText;
         h = null;
         this.xL = Canvas.h;
         this.chan = 0;
         this.list = var1;
         this.size = this.list.size();
         this.menuW = this.menuH = 0;

         for(int var4 = 0; var4 < this.size; ++var4) {
            Command var3 = (Command)this.list.elementAt(var4);
            int var5;
            if ((var5 = Canvas.normalFont.getWidth(var3.caption) + 20) > this.menuW) {
               this.menuW = var5;
            }

            this.menuH += this.e;
         }

         if (this.menuW < Canvas.w / 3) {
            this.menuW = Canvas.w / 3;
         }

         if (this.menuW > Canvas.w - 4) {
            this.menuW = Canvas.w - 4;
         }

         this.menuH += 4;
         if (var2 == 0) {
            this.menuX = 2 * (Canvas.stypeInt != 0 ? 2 : 1);
         } else if (var2 == 1) {
            this.menuX = Canvas.w - this.menuW - 2;
         } else {
            this.menuX = (Canvas.w >> 1) - (this.menuW >> 1);
         }

         if (this.size > 5) {
            this.menuH = MyScreen.hText * 5 + 4;
         }

         this.menuY = Canvas.h - this.menuH - AvMain.hDuBox - Canvas.hTab;
         if (OnScreen.isOngame) {
            this.menuY = Canvas.hCan - Canvas.hTab - this.menuH - 5;
         }

         if (Canvas.h < 200) {
            this.menuY += 10;
         }

         this.m = Canvas.h - this.e;
         if (Canvas.stypeInt > 0) {
            this.menuY = Canvas.hCan - this.menuH - AvMain.hDuBox - 3;
            if (Canvas.stypeInt == 1) {
               this.menuY -= 7;
            }

            super.left = null;
         }

         this.showMenuFarm = false;
         this.selected = 0;
         this.cmyLim = (this.size - 5) * this.e;
         if (this.cmyLim < 0) {
            this.cmyLim = 0;
         }

         this.cmtoY = 0;
         this.cmy = 0;
         v = null;
         if (Canvas.E) {
            Canvas.clearKeyReleased();
         }

         iNo = null;
         this.x = this.menuH;
         Canvas.menuMain = this;
      }

   }

   private void click() {
      this.showMenuFarm = false;
      Canvas.menuMain = null;
      Command var1;
      if ((var1 = (Command)this.list.elementAt(this.selected)).pointer != null) {
         var1.pointer.commandActionPointer(var1.indexMenu);
      } else if (var1.action != null) {
         var1.action.perform();
      } else {
         Canvas.currentMyScreen.commandActionPointer(var1.indexMenu, var1.subIndex);
      }

   }

   public final void updateKey() {
      super.updateKey();
      ++this.count;
      boolean var2 = false;
      if (!Canvas.a(2) && !Canvas.a(4)) {
         if (Canvas.a(8) || Canvas.a(6)) {
            var2 = true;
            ++this.selected;
            if (this.selected > this.size - 1) {
               this.selected = 0;
            }

            super.isHide_ = false;
         }
      } else {
         var2 = true;
         --this.selected;
         if (this.selected < 0) {
            this.selected = this.size - 1;
         }

         super.isHide_ = false;
      }

      if (Canvas.isPointerClick && Canvas.b(this.menuX - 2, this.m - 7, this.menuW + 4, this.menuH + 15)) {
         Canvas.isPointerClick = false;
         this.y = this.cmy;
         this.B = System.currentTimeMillis() / 10L;
         this.A = true;
      }

      if (this.A) {
         int var3 = Canvas.dy();
         if (this.showMenuFarm) {
            var3 = Canvas.dx();
         }

         long var4 = System.currentTimeMillis() / 10L - this.B;
         int var6;
         int var7;
         if (Canvas.isPointerDown) {
            if (Canvas.gameTick % 3 == 0) {
               this.z = Canvas.py;
               this.D = this.count;
            }

            this.vY = 0;
            if (Math.abs(var3) < 20 * AvMain.hd) {
               var6 = this.m;
               var7 = (this.cmtoY + Canvas.py - var6) / this.e;
               if (this.showMenuFarm) {
                  var6 = this.menuX;
                  var7 = (this.cmtoY + Canvas.px - var6) / this.e;
               }

               this.selected = var7;
               this.setSelected();
            }

            if (CRes.abs(var3) >= 20 * AvMain.hd) {
               super.isHide_ = true;
            } else if (var4 > 10L && var4 < 20L) {
               super.isHide_ = false;
            }

            this.cmtoY = this.y + var3;
            if (this.cmtoY < 0 || this.cmtoY > this.cmyLim) {
               this.cmtoY = this.y + var3 / 3;
            }

            this.cmy = this.cmtoY;
         }

         if (Canvas.isPointerRelease && Canvas.b(this.menuX - 2, this.m - 7, this.menuW + 4, this.menuH + 15)) {
            var6 = (int)(this.count - this.D);
            if (CRes.abs(var7 = this.z - Canvas.py) > 40 && var6 < 10 && this.cmtoY > 0 && this.cmtoY < this.cmyLim) {
               this.vY = var7 / var6 * 10;
            }

            this.D = -1L;
            if (Math.abs(var3) < 20 * AvMain.hd) {
               if (var4 <= 10L) {
                  super.isHide_ = false;
               }

               if (!super.isHide_) {
                  var3 = this.m;
                  var3 = (this.cmtoY + Canvas.py - var3) / this.e;
                  if (this.showMenuFarm) {
                     var3 = this.menuX;
                     var3 = (this.cmtoY + Canvas.px - var3) / this.e;
                  }

                  this.selected = var3;
                  this.setSelected();
                  this.click();
               }
            }

            Canvas.isPointerRelease = false;
         }
      }

      if (Canvas.isPointerRelease) {
         if (!this.A) {
            this.showMenuFarm = false;
            Canvas.menuMain = null;
            if (iNo != null) {
               iNo.perform();
            }
         }

         this.A = false;
         Canvas.isPointerRelease = false;
      }

      if (var2) {
         this.cmtoY = this.selected * this.e - this.menuW / 2 + this.e / 2;
         if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
            return;
         }

         if (this.cmtoY < 0) {
            this.cmtoY = 0;
         }
      }

   }

   public final void paint(Graphics var1) {
      var1.translate(0, this.xL);
      int var6;
      int var8;
      Graphics var10;
      Menu var2;
      if (this.showMenuFarm) {
         var10 = var1;
         var2 = this;
         Canvas.resetTrans(var1);
         Canvas.paint.drawArea(var1, this.menuX, this.menuY, this.menuW, this.menuH);
         var1.translate(this.menuX + AvMain.hDuBox + 2, this.menuY + AvMain.hDuBox + 2);
         var1.setClip(0, 0, this.menuW - (AvMain.hDuBox << 1) - 4, this.e);
         var1.translate(-this.cmy, 0);
         int var4;
         if ((var4 = this.cmy / this.e) < 0) {
            var4 = 0;
         }

         int var5;
         if ((var5 = var4 + this.menuW / this.e + 2) > this.size) {
            var5 = this.size;
         }

         if (!super.isHide_) {
            PaintPopup.paintCell(var1, this.selected * this.e, 0, this.e, this.e);
         }

         for(var6 = var4; var6 < var5; ++var6) {
            ((Command)var2.list.elementAt(var6)).paint(var10, var6 * var2.e + var2.e / 2, var2.e / 2);
         }

         if (var2.selected >= 0 && var2.selected < var2.list.size()) {
            Command var11 = (Command)var2.list.elementAt(var2.selected);
            var10.setClip(var2.cmy - 50, -100, var2.cmy + Canvas.w + 100, var2.menuH + 200);
            int var7 = var2.selected * var2.e + var2.e / 2;
            if (var2.size * var2.e + (AvMain.hDuBox << 1) + 10 > Canvas.w) {
               var8 = Canvas.borderFont.getWidth(var11.caption) / 2;
               if (var7 - var8 < var2.cmy) {
                  var7 = var2.cmy + var8;
               } else if (var7 + var8 > Canvas.w + var2.cmy - 15) {
                  var7 = Canvas.w + var2.cmy - var8 - 15;
               }
            }

            Canvas.borderFont.drawString(var10, var11.caption, var7, -AvMain.hBorder - AvMain.hDuBox - 6 - (AvMain.hd == 2 ? 15 : 0), 2);
         }

         Canvas.resetTrans(var10);
      } else if (this.size != 0) {
         var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
         var10 = var1;
         var2 = this;
         Canvas.resetTrans(var1);
         if (OnScreen.isOngame) {
            Canvas.paint.drawContainer(var1, this.menuX - 2, this.m - 7, this.menuW + 4, this.menuH + 15);
         } else {
            Canvas.paint.drawArea(var1, this.menuX - 2, this.m - 7, this.menuW + 4, this.menuH + 15);
         }

         var1.setClip(this.menuX, this.m, this.menuW, this.menuH);
         var1.translate(this.menuX + 3, this.m + 1);
         var1.translate(0, -this.cmy);
         var6 = (this.e - AvMain.hNormal) / 2;

         for(var8 = 0; var8 < var2.size; ++var8) {
            var10.setColor(0);
            if (!var2.isHide_ && var8 == var2.selected) {
               if (OnScreen.isOngame) {
                  var10.setColor(35217);
                  var10.fillRect(0, var8 * var2.e, var2.menuW - 6, var2.e);
               } else {
                  Canvas.paint.drawBorder(var10, 0, var8 * var2.e, var2.menuW - 6, var2.e);
               }
            }

            short var12 = 0;
            if (h != null && var8 < h.length && h[var8] != -1 && AvatarData.getImgIcon(h[var8]) != null) {
               var12 = AvatarData.getImgIcon(h[var8]).w;
               AvatarData.paintImg(var10, h[var8], 3, var8 * var2.e + var6 + 1, 0);
            }

            if (OnScreen.isOngame) {
               Canvas.borderFont.drawString(var10, ((Command)var2.list.elementAt(var8)).caption, var12 + 5, var8 * var2.e + var6, 0);
            } else {
               Canvas.paint.drawString(var10, ((Command)var2.list.elementAt(var8)).caption, var12 + 5, var8 * var2.e + var6, 0);
            }

            if (var8 == 0 && h != null && h.length > 0 && h[0] != -1 && AvatarData.getImgIcon(h[0]) != null) {
               int var13 = var12 + 5 + Canvas.normalFont.getWidth(((Command)var2.list.elementAt(var8)).caption) + 1;
               AvatarData.paintImg(var10, h[0], var13, var8 * var2.e + var6 + 1, 0);
            }
         }
      }

      super.paint(var1);
   }

   public final void update() {
      if (this.xL != 0) {
         this.xL += -this.xL >> 1;
      }

      if (this.xL == -1) {
         this.xL = 0;
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
            if (this.cmy < -this.x / 2) {
               this.cmy = -this.x / 2;
               this.cmtoY = 0;
               this.vY = 0;
            }
         } else if (this.cmy > this.cmyLim) {
            if (this.cmy < this.cmyLim + this.x / 2) {
               this.cmy = this.cmyLim + this.x / 2;
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

      if (this.m > this.menuY) {
         int var2;
         if ((var2 = this.m - this.menuY >> 2) <= 0) {
            var2 = 1;
         }

         this.m -= var2;
      }

      this.m = this.menuY;
   }
}
