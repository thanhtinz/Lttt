package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class GamePad {
   private int b;
   private int c;
   private int d;
   private int e;
   private int f;
   private int g;
   private int h;
   private int i;
   private int j;
   private int k;
   private int l;
   private int m;
   private int n;
   private int o;
   private String[] p;
   private String[] q;
   private String[] r;
   private String[] s;
   private String[] t;
   private byte[] u;
   private int v = -1;
   public boolean isPointerClick = false;
   private boolean w = false;
   private long x;
   private String[] y = new String[]{"Top", "Down", "Left", "Right"};
   private byte[] z = new byte[]{4, 7, 0, 2};

   public GamePad() {
      int var1;
      if (Canvas.h >= Canvas.w) {
         Canvas.G = false;
         this.e = Canvas.h / 6 << 1;
         Canvas.h -= this.e;
         this.b = 0;
         this.c = Canvas.h + 4;
         this.e -= 4;
         this.d = Canvas.w;
         this.f = this.d / 4;
         this.g = this.e / 2;
         this.h = this.b;
         this.i = this.c;
         this.j = this.e / 3;
         this.k = this.d / 4;
         this.l = 4;
         this.m = 2;
         this.n = 4;
         this.o = 3;
         this.p = new String[]{"-", "Top", "ABC", "-", "Left", "Down", "Right", "OK"};
         this.q = new String[]{".,?!1", "abc2", "def3", T.del, "ghi4", "jkl5", "mno6", T.finish, "pqrs7", "tuv8", "wxyz9", "0"};
         this.r = new String[12];

         for(var1 = 0; var1 < 12; ++var1) {
            this.r[var1] = this.q[var1].toUpperCase();
         }

         this.r[3] = this.q[3];
         this.s = new String[]{"1", "2", "3", T.del, "4", "5", "6", T.finish, "7", "8", "9", "0"};
         this.u = new byte[]{-6, -1, 0, -7, -3, -2, -4, -5};
      } else {
         Canvas.G = true;
         this.d = Canvas.w / 6 << 1;
         Canvas.w -= this.d + 1;
         this.c = 1;
         this.e = Canvas.instance.getHeight();
         this.b = Canvas.w + 4;
         this.d -= 4;
         this.f = this.d / 2;
         this.g = this.e / 4;
         this.h = this.b;
         this.i = this.c;
         this.j = this.e / 4;
         this.k = this.d / 3;
         this.l = 2;
         this.m = 4;
         this.n = 3;
         this.o = 4;
         this.p = new String[]{"-", "OK", "ABC", "Top", "Left", "Right", "-", "Down"};
         this.q = new String[]{".,?!1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", T.finish, "0", T.del};
         this.r = new String[12];

         for(var1 = 0; var1 < 11; ++var1) {
            this.r[var1] = this.q[var1].toUpperCase();
         }

         this.r[11] = this.q[11];
         this.s = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", T.finish, "0", T.del};
         this.u = new byte[]{-7, -5, 0, -1, -3, -4, -6, -2};
      }

      this.x = -1L;
      this.c();
   }

   private static void b() {
      if (Canvas.stypeInt > 0) {
         TField.t.perform();
      } else if (ChatTextField.isShow) {
         ChatTextField.gI().right.perform();
      } else if (Canvas.currentMyScreen.right != null && Canvas.currentMyScreen.right.caption.equals(T.del)) {
         Canvas.currentMyScreen.right.action.perform();
      }

   }

   public final void a() {
      int var2;
      int var3;
      if (!this.isPointerClick) {
         if (this.w && Canvas.isPointerRelease) {
            this.w = false;
            if (System.currentTimeMillis() / 10L - this.x > 40L) {
               TField.keyPressedAscii();
               this.c();
            } else {
               this.v = -1;
               this.isPointerClick = true;
            }
         }

         if (Canvas.isPointer(this.b, this.c, this.d, this.e)) {
            if (Canvas.isPointerDown) {
               var2 = (Canvas.px - this.b) / this.f;
               var3 = (Canvas.py - this.c) / this.g;
               this.v = var3 * this.l + var2;
               var3 = this.v;
               if (var3 == 2) {
                  this.x = System.currentTimeMillis() / 10L;
                  this.w = true;
               } else {
                  Canvas.instance.keyPressed(this.u[var3]);
               }

               Canvas.isPointerDown = false;
            }

            if (Canvas.isPointerRelease && this.v != -1) {
               var3 = this.v;
               if (var3 != 2 && var3 < this.u.length) {
                  Canvas.instance.keyReleased(this.u[var3]);
               }

               this.v = -1;
               Canvas.isPointerRelease = false;
            }
         }
      } else if (Canvas.isPointer(this.h, this.i, this.d, this.e)) {
         if (Canvas.isPointerDown) {
            var2 = (Canvas.px - this.h) / this.k;
            var3 = (Canvas.py - this.i) / this.j;
            this.v = var3 * this.n + var2;
            var3 = this.v;
            if (Canvas.G && var3 < 9) {
               Canvas.instance.keyPressed(var3 + 49);
            } else if (!Canvas.G && var3 % 4 != 3) {
               Canvas.instance.keyPressed(var3 + 49 - var3 / 4);
            } else {
               switch (var3) {
                  case 3:
                     b();
                  case 4:
                  case 5:
                  case 6:
                  case 8:
                  default:
                     break;
                  case 7:
                  case 9:
                     this.isPointerClick = false;
                     break;
                  case 10:
                     Canvas.instance.keyPressed(48);
                     break;
                  case 11:
                     if (Canvas.G) {
                        b();
                     } else {
                        Canvas.instance.keyPressed(48);
                     }
               }
            }

            Canvas.isPointerDown = false;
         }

         if (Canvas.isPointerRelease && this.v != -1) {
            this.v = -1;
            Canvas.isPointerRelease = false;
         }
      }

   }

   private void c() {
      switch (TField.mode) {
         case 0:
         case 1:
            this.t = this.q;
            return;
         case 2:
            this.t = this.r;
            return;
         case 3:
            this.t = this.s;
         default:
      }
   }

   public final void a(Graphics var1) {
      var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
      var1.setClip(this.b - 4, this.c - 4, this.d + 4, this.e + 4);
      GamePad var2;
      Graphics var3;
      int var4;
      int var5;
      if (this.isPointerClick) {
         var3 = var1;
         var2 = this;
         var1.setClip(this.b, this.c, this.d, this.e);
         PaintPopup.fill(this.h, this.i, this.d, this.e, 8705740, var1);
         var1.setColor(1);
         var1.drawRect(this.h, this.i, this.d - 1, this.e - 1);

         for(var4 = 1; var4 < var2.n; ++var4) {
            var3.fillRect(var2.h + var4 * var2.k, var2.i, 1, var2.e);
         }

         for(var4 = 1; var4 < var2.o; ++var4) {
            var3.fillRect(var2.h, var2.i + var4 * var2.j, var2.d, 1);
         }

         for(var4 = 0; var4 < var2.r.length; ++var4) {
            var5 = var2.i + var4 / var2.n * var2.j;
            var3.setClip(var2.h + var4 % var2.n * var2.k, var5 - 5, var2.k, var2.j + 5);
            if (var2.v == var4) {
               var3.setColor(14279153);
               var3.fillRect(var2.h + var4 % var2.n * var2.k + 1, var5 + 1, var2.k - 2, var2.j - 2);
            }

            Canvas.normalFont.drawString(var3, var2.t[var4], var2.h + var4 % var2.n * var2.k + var2.k / 2, var5 - 5 + var2.j / 2, 2);
         }
      } else {
         var3 = var1;
         var2 = this;
         var1.setClip(this.b - 4, this.c - 4, this.d + 4, this.e + 4);
         PaintPopup.fill(this.b, this.c, this.d, this.e, 8705740, var1);
         var1.setColor(0);
         var1.drawRect(this.b, this.c, this.d - 1, this.e - 1);

         for(var4 = 1; var4 < var2.l + 1; ++var4) {
            var3.fillRect(var2.b + var4 * var2.f, var2.c, 1, var2.e);
         }

         for(var4 = 1; var4 < var2.m; ++var4) {
            var3.fillRect(var2.b, var2.c + var4 * var2.g, var2.d, 1);
         }

         for(var4 = 0; var4 < var2.p.length; ++var4) {
            if (var2.v == var4) {
               var3.setColor(14279153);
               var3.fillRect(var2.b + var4 % var2.l * var2.f + 1, var2.c + var4 / var2.l * var2.g + 1, var2.f - 2, var2.g - 2);
            }

            var5 = var2.b + var4 % var2.l * var2.f + var2.f / 2;
            int var6 = var2.c + var4 / var2.l * var2.g + var2.g / 2;
            if (var2.p[var4].equals("ABC")) {
               Canvas.normalFont.drawString(var3, TField.p[TField.mode], var5, var6 - 5, 2);
            } else {
               for(int var7 = 0; var7 < 4; ++var7) {
                  if (var2.p[var4].equals(var2.y[var7])) {
                     PaintPopup.b.drawFrame(0, var5, var6, var2.z[var7], 3, var3);
                  }
               }
            }
         }
      }

      var1.setClip(this.b - 4, this.c - 4, this.d + 4, this.e + 4);
      var1.setColor(2378578);
      if (Canvas.G) {
         var1.drawRect(this.b - 4, this.c, 4, this.e);
         var1.setColor(6201499);
         var1.fillRect(this.b - 4 + 1, this.c + 1, 3, this.e - 2);
         var1.setColor(2716523);
         var1.fillRect(this.b - 4 + 3, this.c + 1, 1, this.e - 1);
      } else {
         var1.drawRect(this.b, this.c - 4, this.d, 4);
         var1.setColor(6201499);
         var1.fillRect(this.b + 1, this.c - 4 + 1, this.d - 2, 3);
         var1.setColor(2716523);
         var1.fillRect(this.b + 1, this.c - 4 + 3, this.d - 1, 1);
      }

   }
}
