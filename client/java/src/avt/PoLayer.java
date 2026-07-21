package avt;

import javax.microedition.lcdui.Graphics;

final class PoLayer extends Layer {
   private final Point po;

   PoLayer(FarmScr var1, Point var2) {
      this.po = var2;
   }

   public final void paint(Graphics var1) {
      PaintPopup.fill(this.po.x * AvMain.hd, this.po.y * AvMain.hd, this.po.w, this.po.h, 5921542, var1);
   }

   public final void update() {
      if (this.po.y < this.po.limitY) {
         Point var10000 = this.po;
         var10000.x += this.po.v;
         var10000 = this.po;
         var10000.y += this.po.g;
         ++this.po.g;
      } else {
         this.po.v = 0;
         this.po.g = 0;
      }

   }
}
