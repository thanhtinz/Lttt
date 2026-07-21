package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class ReportDlg extends Dialog {
   public Vector list;
   private static ReportDlg instance;
   public int y = 0;
   public int h;
   public int x;
   public int w;
   public int f;
   private boolean i = false;
   public String g = "";

   public static ReportDlg gI() {
      if (instance == null) {
         instance = new ReportDlg();
      }

      return instance;
   }

   public final void commandActionPointer(int var1) {
      switch (var1) {
         case 0:
            Canvas.endDlg();
            instance = null;
         default:
      }
   }

   public ReportDlg() {
      super.center = new Command(T.OK, 0);
   }

   public final void update() {
   }

   public final void show() {
      if (this.list != null && this.list.size() > 0) {
         Canvas.currentDialog = this;
      }

   }

   public final void paint(Graphics var1) {
      Canvas.paint.paintPopupBack(var1, this.x, this.y, this.w, this.h, 0);
      int var2 = this.y + PaintPopup.hTab + (5 + AvMain.hDuBox - AvMain.hNormal / 2);

      for(int var3 = 0; var3 < this.list.size(); ++var3) {
         String var4;
         if ((var4 = (String)this.list.elementAt(var3)).substring(0, 1).equals("0")) {
            Canvas.smallFontYellow.drawString(var1, var4.substring(1), this.x + this.w / 2, var2 + 3 + AvMain.hNormal / 2 - AvMain.hSmall / 2, 2);
         } else {
            Canvas.normalFont.drawString(var1, var4, this.x + 15, var2 + 3, 0);
         }

         var2 += AvMain.hNormal;
      }

      super.paint(var1);
   }
}
