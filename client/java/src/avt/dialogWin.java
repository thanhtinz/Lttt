package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class dialogWin extends Face {
   public String name;
   public byte b;
   private int wPopupWin = 200;
   private int hPopupWin;
   public int tienCuoc;
   public int tienAn;
   public int tienThue;
   public int tienNhanDuoc;

   public dialogWin() {
      this.hPopupWin = (short)(AvMain.hNormal * 11);
      super.left = RaceScr.gI().b;
   }

   public final void paint(Graphics var1) {
      Canvas.paint.drawRectangle(var1, (Canvas.w - this.wPopupWin) / 2, (Canvas.h - this.hPopupWin) / 2, this.wPopupWin, this.hPopupWin, PaintPopup.color[2], PaintPopup.color[3], 1);
      var1.translate((Canvas.w - this.wPopupWin) / 2, (Canvas.h - this.hPopupWin) / 2);
      int var2;
      Canvas.normalFont.drawString(var1, String.valueOf(RaceScr.gI().timeStart), this.wPopupWin / 2, (var2 = 0 + AvMain.hNormal) - AvMain.hNormal / 2 - 2 * AvMain.hd, 2);
      Canvas.normalFont.drawString(var1, "Thú đua chiến thắng", this.wPopupWin / 2, var2 += AvMain.hNormal / 2 + 2 * AvMain.hd, 2);
      Canvas.borderFont.drawString(var1, this.name, this.wPopupWin / 2, var2 += AvMain.hNormal + 6 * AvMain.hd, 2);
      var2 += AvMain.hNormal << 1;

      for(int var3 = 0; var3 < 6; ++var3) {
         ImageIcon var4;
         if (this.b == RaceScr.gI().listPet[var3].IDDB && (var4 = AvatarData.getImgIcon(RaceScr.gI().listPet[var3].idImg)).count != -1) {
            int var5 = var4.h / 5;
            var1.drawRegion(var4.img, 0, RaceScr.FRAME[0][0] * var5, var4.w, var5, 0, this.wPopupWin / 2, var2 + AvMain.hNormal / 2, 3);
         }
      }

      var2 += AvMain.hNormal / 2;
      Canvas.normalFont.drawString(var1, "Tiền cược: ", 10, var2 += AvMain.hNormal, 0);
      Canvas.smallFontYellow.drawString(var1, "" + this.tienCuoc, this.wPopupWin - 20, var2 + AvMain.hNormal / 2 - AvMain.hSmall / 2, 1);
      Canvas.normalFont.drawString(var1, "Tiền ăn: ", 10, var2 += AvMain.hNormal, 0);
      Canvas.smallFontYellow.drawString(var1, "" + this.tienAn, this.wPopupWin - 20, var2 + AvMain.hNormal / 2 - AvMain.hSmall / 2, 1);
      Canvas.normalFont.drawString(var1, "Tiền thuế: ", 10, var2 += AvMain.hNormal, 0);
      Canvas.smallFontYellow.drawString(var1, "" + this.tienThue, this.wPopupWin - 20, var2 + AvMain.hNormal / 2 - AvMain.hSmall / 2, 1);
      Canvas.normalFont.drawString(var1, "Tiền nhận được: ", 10, var2 += AvMain.hNormal, 0);
      Canvas.smallFontYellow.drawString(var1, "" + this.tienNhanDuoc, this.wPopupWin - 20, var2 + AvMain.hNormal / 2 - AvMain.hSmall / 2, 1);
      super.paint(var1);
   }
}
