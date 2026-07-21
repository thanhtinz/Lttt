package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class Scroll {
   private static Scroll instance;
   private int limit;
   private int temp;
   private int dis;
   private int yScroll;
   private int hScroll;
   private int h;
   private boolean Disvisible = false;

   public static Scroll gI() {
      return instance == null ? (instance = new Scroll()) : instance;
   }

   public final void init(int var1, int var2) {
      if (var1 < var2) {
         this.Disvisible = true;
      } else {
         this.Disvisible = false;
         this.temp = var1;
         this.limit = var2;
         this.dis = var2 * var2 / var1;
         if (this.dis <= 0) {
            this.dis = 1;
         }
      }

   }

   public final void updateScroll(int var1, int var2) {
      this.hScroll = var1;
      this.h = var2;
      if (!this.Disvisible && (CRes.abs(var2 - var1) > 5 || Canvas.cameraList.n != 0 || Canvas.cameraList.p)) {
         var2 = this.temp * 100 / 100;
         var1 = var1 * 100 / var2;
         var2 = this.limit * 100 / 100;
         this.yScroll = var1 * var2;
      }

   }

   public final void paintScroll(Graphics var1, int var2, int var3) {
      if (!this.Disvisible && (CRes.abs(this.h - this.hScroll) > 5 || Canvas.cameraList.n != 0 || Canvas.cameraList.p)) {
         var1.setColor(6201499);
         var1.setClip(var2 - 1, -1, 6, this.limit + 2);
         var1.fillRect(var2, 0 + this.yScroll / 100, 4, this.dis);
      }

   }
}
