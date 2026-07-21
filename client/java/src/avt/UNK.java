package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class UNK {
   public boolean a;
   public String b;
   public Command c;
   public Command d;
   public Vector e = new Vector();
   public String f = "";
   public int g;
   public boolean h;
   public int i;
   private static int l;
   public static int j;
   private static int m;
   private static int n;
   private static int o;
   public static int k;
   private int p = 0;
   private int q;
   private int r;
   private boolean s = false;
   private long t;
   private long u;

   public UNK(String var1, int var2, Command var3, Command var4, boolean var5) {
      this.b = var1;
      this.g = var2;
      this.c = var3;
      this.h = var5;
      if (var4 == null && MessageScr.me != null) {
         this.d = MessageScr.me.f;
      } else {
         this.d = var4;
      }

      this.d();
      this.c();
      this.a = true;
   }

   private void d() {
      this.i = PaintPopup.gI().h - PaintPopup.hTab - (AvMain.hDuBox << 1) - 7 - (this.h ? MessageScr.tfChat.height : 0);
   }

   public final void a(String var1, String var2) {
      this.a = true;
      this.a(var1 + ": " + var2);
   }

   public final void a(String var1) {
      Vector var4;
      int var2 = (var4 = Canvas.fontChatB.splitFontBStrInLineV(var1, Canvas.w - ((MessageScr.e << 1) + 30 + 10 * (AvMain.hd - 1)))).size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.e.addElement(var4.elementAt(var3));
         if (this.e.size() > 100) {
            this.e.removeElementAt(0);
         }
      }

      if (MessageScr.gI().b(MessageScr.gI().b) == this) {
         this.a();
      }

   }

   public final void a() {
      int var1 = this.e.size();
      Scroll.gI().init(var1 * l, this.i);
      if ((k = var1 * l - this.i) < 0) {
         k = 0;
      }

      if (CRes.abs(j - k) <= l) {
         j = k;
      }

   }

   public final void a(Graphics var1) {
      Scroll.gI().paintScroll(var1, Canvas.w - 50, 0);
      var1.setClip(0, 0, Canvas.w - (MessageScr.e << 1), this.i + 4);
      var1.translate(0, -m);
      int var2;
      if ((var2 = m / l) < 0) {
         var2 = 0;
      }

      int var3;
      if ((var3 = var2 + this.i / l + 1) > this.e.size()) {
         var3 = this.e.size();
      }

      for(var2 = var2; var2 < var3; ++var2) {
         String var4 = (String)this.e.elementAt(var2);
         Canvas.fontChatB.drawString(var1, var4, 10 * AvMain.hd, var2 * l + 5, 0);
      }

   }

   public final void b() {
      ++this.t;
      boolean var1 = false;
      if (Canvas.keyHold[2]) {
         var1 = true;
         j -= l;
      } else if (Canvas.keyHold[8]) {
         j += l;
         var1 = true;
      }

      if (Canvas.isPointerClick) {
         Canvas.isPointerClick = false;
         this.p = m;
         this.s = true;
         this.q = 0;
      }

      if (this.s) {
         if (Canvas.isPointerDown) {
            if (Canvas.gameTick % 3 == 0) {
               this.r = Canvas.py;
               this.u = this.t;
            }

            this.q = 0;
            if ((j = this.p + Canvas.dy()) < 0 || j > k) {
               j = this.p + Canvas.dy() / 2;
            }

            m = j;
         }

         if (Canvas.isPointerRelease) {
            int var2 = (int)(this.t - this.u);
            int var3;
            if (CRes.abs(var3 = this.r - Canvas.py) > 40 && var2 < 10 && j > 0 && j < k) {
               this.q = var3 / var2 * 10;
            }

            this.u = -1L;
         }
      }

      if (var1) {
         if (j < 0) {
            j = 0;
         }

         if (j > k) {
            j = k;
         }
      }

      if (this.q != 0) {
         if (m < 0 || m > k) {
            this.q -= this.q / 4;
            m += this.q / 20;
            if (this.q / 10 <= 1) {
               this.q = 0;
            }
         }

         if (m < 0) {
            if (m < -this.i / 2) {
               m = -this.i / 2;
               j = 0;
               this.q = 0;
            }
         } else if (m > k) {
            if (m < k + this.i / 2) {
               m = k + this.i / 2;
               j = k;
               this.q = 0;
            }
         } else {
            m += this.q / 10;
         }

         j = m;
         this.q -= this.q / 10;
         if (this.q / 10 == 0) {
            this.q = 0;
         }
      } else if (m < 0) {
         j = 0;
      } else if (m > k) {
         j = k;
      }

      if (m != j) {
         o = j - m << 2;
         n += o;
         m += n >> 4;
         n &= 15;
      }

      Scroll.gI().updateScroll(m, j);
   }

   public final void c() {
      this.d();
      m = 0;
      j = 0;
      this.a();
      j = m;
   }

   static {
      l = Canvas.fontChatB.getHeight();
   }
}
