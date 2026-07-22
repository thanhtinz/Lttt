package avt;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class OnScreen extends MyScreen {
   public static OnScreen instance;
   private Command d;
   private int e = 0;
   private int f;
   private int g;
   private int h;
   private int i;
   private int j;
   private int k;
   private static FrameImage l;
   private static int m;
   private static int n;
   private static int o;
   private static int p;
   private static int q;
   public static boolean isOngame = true;
   private static boolean r;
   private static Image s;
   public static int c = 0;
   private int t = 0;
   private int u;
   private int v;
   private int w;
   private int x;
   private int y;
   private int z;
   private boolean A = false;
   private int B;
   private int C = 2;
   private int D = 0;
   private int E = -40;
   private int F = 1;

   public static void addCmd() {
      l = null;
      s = null;
      OnSplashScr.imgBg = null;
   }

   public static OnScreen gI() {
      if (instance == null) {
         instance = new OnScreen();
      }

      return instance;
   }

   public final void switchToMe() {
      super.selected_ = 2;
      Canvas.menuMain = null;
      Canvas.endDlg();
      if (l == null) {
         FilePack.init(T.aw);
         FrameImage.init("iconGame", 13 * AvMain.hd, 11 * AvMain.hd);
         FilePack.reset();

         try {
            int var1 = 70 * AvMain.hd;
            if (Canvas.stypeInt == 0) {
               var1 = 40;
            }

            l = new FrameImage(Image.createImage(T.getPath() + "/on/iconGame0.on"), var1, var1);
            s = Image.createImage(T.getPath() + "/on/select.on");
            if (OnSplashScr.imgBg == null) {
               OnSplashScr.imgBg = Image.createImage(T.getPath() + "/on/logo.on");
            }
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      super.switchToMe();
      this.g = Canvas.h / 2 - AvMain.hNormal;
      this.j = 3;
      this.k = 70 * AvMain.hd;
      if (Canvas.stypeInt == 0) {
         this.k = 40;
      }

      this.h = Canvas.w / this.j;
      if (this.h > 100 * AvMain.hd) {
         this.h = 100 * AvMain.hd;
      }

      this.i = l.frameHeight + AvMain.hNormal + 5 * AvMain.hd;
      this.f = (Canvas.w - this.j * this.h) / 2 + this.h / 2;
      if ((q = this.j * this.h - Canvas.w) < 0) {
         q = 0;
      }

      updateLayout();
      if (Canvas.load == 0) {
         Canvas.load = 1;
      }

      r = true;
      isOngame = true;
   }

   private static void updateLayout() {
      Canvas.hTab = MyScreen.hText;
      if (Canvas.stypeInt == 0) {
         Canvas.hTab = AvMain.hBorder + 5;
      }

      Canvas.h = Canvas.instance.getHeight() - Canvas.hTab;

      for(int var0 = 0; var0 < 3; ++var0) {
         Canvas.ae[var0].y = Canvas.hCan - Canvas.hTab;
      }

   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            switch (this.e) {
               case 0:
               case 1:
               case 2:
               case 3:
                  var1 = this.e;
                  Canvas.startWaitDlg();
                  GlobalService.gI().getHandler(3);
                  MapScr.typeCasino = (byte)var1;
                  return;
               case 4:
                  Canvas.startWaitDlg();
                  GlobalService.gI().getHandler(3);
                  MapScr.typeCasino = 4;
                  return;
               case 5:
                  TransMoneyDlg.gI().init();
                  return;
               default:
                  return;
            }
         case 1:
            c = 1;
            Canvas.cameraList.isShow = false;
            GlobalService var3;
            (var3 = GlobalService.gI()).createMessage((byte)-96);
            var3.sendMessage();
            Canvas.startWaitDlg();
            return;
         case 2:
            GlobalService.gI().doCommunicate(1);
            Canvas.startWaitDlg();
            return;
         case 3:
            this.performDelayedAction();
         default:
      }
   }

   public OnScreen() {
      this.d = new Command(T.selectt, 0);
      super.right = new Command(T.exit, 1);
      super.left = new Command("Top", 2);
      if (Canvas.stypeInt == 0) {
         super.center = new Command(T.selectt, 3);
      }

   }

   public final void close() {
      this.commandTab(1, -1);
   }

   private void performDelayedAction() {
      r = true;
      this.d.perform();
   }

   public final void update() {
      if (this.w > 0) {
         --this.w;
         if (this.w == 0 && Canvas.currentMyScreen != PopupShop.me) {
            this.performDelayedAction();
         }
      }

      if (this.u != 0) {
         if (n < 0 || n > q) {
            if (this.u > 500) {
               this.u = 500;
            } else if (this.u < -500) {
               this.u = -500;
            }

            this.u -= this.u / 5;
            if (CRes.abs(this.u / 10) <= 10) {
               this.u = 0;
            }
         }

         m = n += this.u / 15;
         this.u -= this.u / 20;
      } else if (n < 0) {
         m = 0;
      } else if (n > q) {
         m = q;
      }

      if (n != m) {
         p = m - n << 2;
         o += p;
         n += o >> 4;
         o &= 15;
      }

      if (this.D >= 0) {
         this.E += this.F * this.D;
         this.D += this.F * this.C;
         if (this.D <= 0) {
            this.F = -this.F;
         }

         if (this.E > 0) {
            this.F = -this.F;
            this.D -= 2 * this.C;
         }
      }

   }

   public final void updateKey() {
      ++this.x;
      if (Canvas.isKeyPressed(4)) {
         if (this.e % this.j > 0) {
            --this.e;
         }
      } else if (Canvas.isKeyPressed(6)) {
         if (this.e < l.nFrame - 1 && this.e % this.j < this.j - 1) {
            ++this.e;
         }
      } else if (Canvas.isKeyPressed(2)) {
         if (this.e / this.j > 0) {
            this.e -= this.j;
         }
      } else if (Canvas.isKeyPressed(8) && this.e / this.j < l.nFrame / this.j && this.e + this.j < l.nFrame) {
         this.e += this.j;
      }

      int var1;
      if (Canvas.isPointerClick) {
         for(var1 = 0; var1 < T.selectLanguage.length; ++var1) {
            if (Canvas.isPointerInRect(this.f + var1 % this.j * this.h - this.k / 2, this.g + var1 / this.j * this.i - this.k / 2, this.k, this.k + AvMain.hNormal + 10)) {
               this.B = Canvas.pxLast;
               this.y = this.x;
               this.t = n;
               this.u = 0;
               Canvas.isPointerClick = false;
               this.A = true;
               break;
            }
         }
      }

      if (this.A) {
         var1 = this.x - this.y;
         int var2 = this.B - Canvas.px;
         this.B = Canvas.px;
         int var3;
         if (Canvas.isPointerDown) {
            if (this.x % 2 == 0) {
               this.v = Canvas.px;
               this.z = this.x;
            }

            this.u = 0;
            if (m > 0 && m < q) {
               m = this.t + var2;
               this.t = m;
            } else {
               m = this.t + Canvas.dx() / 2;
            }

            n = m;
            if (var1 < 20) {
               var2 = (m + Canvas.px - (this.f - this.h / 2)) / this.h;
               var3 = (Canvas.py - (this.g - this.h / 2)) / this.i;
               this.e = var3 * this.j + var2;
               if (this.e < 0) {
                  this.e = 0;
               }

               if (this.e >= T.selectLanguage.length) {
                  this.e = T.selectLanguage.length - 1;
               }
            }

            if (CRes.abs(Canvas.dy()) < 10 * AvMain.hd && CRes.abs(Canvas.dx()) < 10 * AvMain.hd) {
               if (var1 > 3 && var1 < 8) {
                  r = false;
               }
            } else {
               r = true;
            }
         }

         if (Canvas.isPointerRelease) {
            var2 = this.v - Canvas.px;
            var3 = this.x - this.z;
            if (CRes.abs(var2) > 40 && var3 < 20 && m > 0 && m < q) {
               this.u = var2 / var3 * 10;
            }

            this.z = -1;
            if (CRes.abs(Canvas.dy()) < 10 * AvMain.hd && CRes.abs(Canvas.dx()) < 10 * AvMain.hd) {
               if (var1 <= 4) {
                  this.w = 5;
                  r = false;
               } else if (!r) {
                  this.performDelayedAction();
               }
            }

            this.A = false;
            Canvas.isPointerRelease = false;
         }
      }

      if (Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

   }

   public final void paintMain(Graphics var1) {
      Canvas.paint.paintDefaultBg(var1);
      if (Canvas.W != 2) {
         Canvas.paint.drawElement(var1, Canvas.hw, (this.g - l.frameHeight / 2) / 2);
      }

      var1.translate(this.f, this.g);
      var1.translate(-n, 0);

      for(int var2 = 0; var2 < T.selectLanguage.length; ++var2) {
         int iconIndex = var2;
         if (var2 == 4) {
            iconIndex = 5;
         } else if (var2 == 5) {
            iconIndex = 4;
         }
         l.drawFrame(iconIndex, var2 % this.j * this.h, var2 / this.j * this.i, 0, 3, var1);
         Canvas.M.drawString(var1, T.selectLanguage[var2], var2 % this.j * this.h, var2 / this.j * this.i + l.frameHeight / 2 + 5, 2);
         if (this.e == var2 && (!Canvas.isKeyBoard || !r)) {
            var1.drawImage(s, var2 % this.j * this.h, var2 / this.j * this.i, 3);
         }
      }

   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      this.paintMain(var1);
      paintTitle(var1, super.left, super.center, super.right);
      Canvas.resetTrans(var1);
      Canvas.paintPlus2(var1);
   }

   public static void paintTitle(Graphics var0, Command var1, Command var2, Command var3) {
      Canvas.resetTrans(var0);
      Canvas.paint.paintBackground(var0);
      if (Canvas.menuMain == null && (Canvas.currentDialog == null || Canvas.currentDialog == TransMoneyDlg.me)) {
         Canvas.paint.paintCommandAlt(var0, var1, var2, var3);
      }

   }

   public static void disableVirtualKey() {
      if (isOngame && OptionScr.isVirTualKey) {
         OptionScr.isVirTualKey = false;
         OptionScr.gI().mapFocus[4] = 0;
         Canvas.instance.setSize();
         updateLayout();
      }

   }
}
