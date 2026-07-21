package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class InputFace extends Face {
   public static InputFace me;
   private TField[] list;
   private String title;
   private int x;
   private int y;
   private int w;
   private int h;
   private int focus;
   private int wTab;
   private String[][] nameChangePass;

   public static InputFace gI() {
      return me == null ? (me = new InputFace()) : me;
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            Canvas.currentFace = null;
            return;
         default:
            Canvas.currentMyScreen.commandTab(var1, var2);
      }
   }

   public InputFace() {
      this.w = 200 + Canvas.stypeInt * 88;
      this.x = (Canvas.w - this.w) / 2;
   }

   public final void setIputType(TField[] var1, String var2, String[][] var3, Command var4) {
      super.left = new Command(T.close, 0);
      super.center = var4;
      this.title = var2;
      this.list = var1;
      this.nameChangePass = var3;
      this.h = MyScreen.hTab + AvMain.hDuBox + AvMain.hNormal + (var1[0].height << 1) * var1.length + Canvas.stypeInt * 12;
      this.y = (Canvas.h - Canvas.hTab - this.h) / 2;

      for(int var5 = 0; var5 < var1.length; ++var5) {
         var1[var5].width = this.w - 50 * (Canvas.stypeInt + 1) - Canvas.normalFont.getWidth(var3[0][0]);
         var1[var5].x = this.x + this.w - var1[var5].width - 10 * (Canvas.stypeInt + 1);
         var1[var5].y = this.y + PaintPopup.hTab + AvMain.hDuBox + AvMain.hNormal + (var1[0].height * var5 << 1);
      }

      this.wTab = Canvas.normalFont.getWidth(var2) + 20 * AvMain.hd;
      if (this.wTab < 50 + 20 * AvMain.hd) {
         this.wTab = 50 + 20 * AvMain.hd;
      }

      this.setFocus();
   }

   public final void updateKey() {
      for(int var1 = 0; var1 < this.list.length; ++var1) {
         this.list[var1].update();
      }

      boolean var2 = false;
      if (Canvas.a(2)) {
         --this.focus;
         if (this.focus < 0) {
            this.focus = this.list.length - 1;
         }

         var2 = true;
      } else if (Canvas.a(8)) {
         ++this.focus;
         if (this.focus > this.list.length - 1) {
            this.focus = 0;
         }

         var2 = true;
      }

      if (var2) {
         this.setFocus();
      }

      super.updateKey();
   }

   private void setFocus() {
      for(int var1 = 0; var1 < this.list.length; ++var1) {
         this.list[var1].setFocus(false);
      }

      this.list[this.focus].setFocus(true);
      super.right = this.list[this.focus].a();
   }

   public final void keyPress(int var1) {
      for(int var2 = 0; var2 < this.list.length; ++var2) {
         if (this.list[var2].isFocused()) {
            this.list[var2].keyPressed(var1);
         }
      }

      super.keyPress(var1);
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.paint.paintBoxTab(var1, this.x, this.y, this.h, this.w, 0, 0, PaintPopup.gI().wSub, this.wTab, PaintPopup.hTab, 1, 1, PaintPopup.gI().count, PaintPopup.gI().colorTab, this.title);

      for(int var2 = 0; var2 < this.list.length; ++var2) {
         var1.setClip(this.x + 4 * AvMain.hd, this.y, this.w - 8 * AvMain.hd, this.h);
         int var3;
         if ((var3 = this.list[var2].x - Canvas.normalFont.getWidth(this.nameChangePass[var2][0]) - 5) > this.x + 4 * AvMain.hd + 5) {
            var3 = this.x + 4 * AvMain.hd + 5;
         }

         byte var4 = 2;
         if (this.nameChangePass[var2][1].equals("")) {
            var4 = 1;
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            Canvas.normalFont.drawString(var1, this.nameChangePass[var2][var5], var3, this.list[var2].y + this.list[var2].height / 2 - AvMain.hNormal * var4 / 2 + AvMain.hNormal * var5, 0);
         }

         this.list[var2].paint(var1);
      }

      super.paint(var1);
   }
}
