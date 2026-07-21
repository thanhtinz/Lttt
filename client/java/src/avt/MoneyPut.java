package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class MoneyPut {
   private int x;
   private int y;
   int valuea;
   private int typePaint;

   public MoneyPut(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.valuea = var3;
      this.typePaint = var4;
   }

   public final void paint(Graphics var1) {
      ImageIcon var2;
      if ((var2 = AvatarData.getImgIcon((short)(Canvas.w > 200 ? 870 : 871))).count != -1) {
         var1.drawRegion(var2.img, 0, this.typePaint * BCBoardScr.c, BCBoardScr.b, BCBoardScr.c, 0, this.x, this.y, 3);
         FontX var3 = Canvas.O;
         if (Canvas.w <= 200) {
            var3 = Canvas.smallFontYellow;
         }

         if (Canvas.stypeInt > 0) {
            var3 = Canvas.normalFont;
         }

         var3.drawString(var1, String.valueOf(this.valuea), this.x, this.y - AvMain.hNormal / 2, 2);
      }

   }
}
