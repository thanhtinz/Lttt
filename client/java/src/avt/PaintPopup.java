package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class PaintPopup {
   public static FrameImage imgArrowUp;
   public static FrameImage b;
   public static PaintPopup me;
   public static int[] color;
   public int h;
   public int w;
   public int x;
   public int y;
   private int numTab;
   public int wTab;
   public int wSub = 10;
   public static String name;
   public int countCloseTab;
   private int q = 0;
   private int r;
   public int[] colorTab;
   public int[] count;
   private String s;
   public static byte hTab;

   public static PaintPopup gI() {
      if (me == null) {
         me = new PaintPopup();
      }

      return me;
   }

   public PaintPopup() {
      hTab = (byte)(AvMain.hNormal << 1);
      if (Canvas.instance != null && Canvas.isKeyBoard) {
         this.wSub = 17;
      } else {
         this.wSub = 10;
      }

      if (Canvas.stypeInt != 0) {
         this.wSub *= Canvas.stypeInt + 1;
      }

   }

   public final void setup(String var1, int var2, int var3, int var4) {
      this.w = var2;
      this.h = var3;
      this.numTab = var4;
      if (var1 != null) {
         this.s = var1;
         if (Canvas.normalFont.getWidth(this.s) > this.w / 2 && this.s.length() > 10) {
            this.s = this.s.substring(0, 10);
         }

         this.wTab = Canvas.normalFont.getWidth(this.s) + 10 + (Canvas.stypeInt != 0 ? 35 * Canvas.stypeInt : 0);
      }

      if (this.wTab < 40) {
         this.wTab = 40;
      }

      this.init();
      this.countCloseTab = 0;
      this.colorTab = new int[this.numTab];
      this.count = new int[this.numTab];
      this.r = (this.w - this.wTab) / this.wSub;
      this.q = 0;
   }

   public final void init() {
      this.x = Canvas.hw - this.w / 2;
      this.y = (Canvas.hCan - Canvas.hTab) / 2 - this.h / 2;
   }

   public final void setTabColor(int var1, int var2) {
      if (var2 != this.countCloseTab) {
         this.colorTab[var2] = var1;
         this.count[var2] = CRes.rnd(20);
      }

   }

   public final void setNameAndFocus(String var1, int var2) {
      if (this.colorTab != null && var2 < this.colorTab.length) {
         this.colorTab[var2] = 0;
      }

      this.s = var1;
      int var3;
      if ((var3 = Canvas.normalFont.getWidth(this.s) + 10) > this.wTab) {
         this.wTab = var3;
         this.r = (this.w - this.wTab) / this.wSub;
      }

      this.countCloseTab = var2;
      if (this.countCloseTab >= this.r && this.r > 0) {
         this.q = this.countCloseTab - (this.r - 1);
      }

      if (this.countCloseTab < this.q) {
         this.q = this.countCloseTab;
      }

   }

   public static void initNames() {
      GameMidlet.n = "ig_,";
      name = "plg";
      MiniMap.i = Canvas.shiftString(GameMidlet.n, 5);
   }

   public final void setNumTab(int var1) {
      this.colorTab = new int[var1];
      this.count = new int[var1];
      this.numTab = var1;
   }

   public final int setupdateTab() {
      if (Canvas.isPointerClick) {
         int var1;
         int var2;
         for(var2 = this.countCloseTab - 1; var2 >= this.q; --var2) {
            var1 = var2 - this.q;
            if (Canvas.isPointer(this.x + 3 + var1 * this.wSub, this.y + 3, this.wSub, hTab)) {
               return var2 - this.countCloseTab;
            }
         }

         if ((var2 = this.numTab) >= this.r) {
            var2 = this.r + this.q;
         }

         for(int var3 = this.countCloseTab + 1; var3 < var2; ++var3) {
            var1 = var3 - this.q;
            if (Canvas.isPointer(this.x + 3 + var1 * this.wSub + (this.wTab - this.wSub), this.y + 3, this.wSub, hTab)) {
               return var3 - this.countCloseTab;
            }
         }
      }

      return 0;
   }

   public final void paint(Graphics var1) {
      Canvas.paint.paintBoxTab(var1, this.x, this.y, this.h, this.w, this.countCloseTab, this.q, this.wSub, this.wTab, hTab, this.numTab, this.r, this.count, this.colorTab, this.s);
      Canvas.resetTrans(var1);
   }

   public static void paintCell(Graphics var0, int var1, int var2, int var3, int var4) {
      fill(var1, var2, var3, var4, color[0], var0);
      var0.setColor(color[2]);
      var0.drawRect(var1, var2, var3, var4);
      var0.setColor(12450472);
      var0.drawRect(var1 + 1, var2 + 1, var3 - 2, var4 - 2);
      var0.setColor(5738823);
      var0.drawRect(var1 + 2, var2 + 2, var3 - 4, var4 - 4);
   }

   public static void fill(int var0, int var1, int var2, int var3, int var4, Graphics var5) {
      var5.setColor(var4);
      var5.fillRect(var0, var1, var2, var3);
   }
}
