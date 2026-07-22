package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class InputDlg extends Dialog {
   private String[] info;
   private TField tfInput = new TField();
   private IAction okAction;
   private Image img;
   private int w;
   private int h;

   public InputDlg() {
      this.tfInput.e = false;
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 120:
            Canvas.currentDialog = null;
            return;
         default:
            Canvas.currentMyScreen.commandTab(var1, var2);
      }
   }

   public final void setImg(Image var1) {
      this.img = var1;
      this.h += var1.getHeight();
      this.init();
   }

   public final String getText() {
      return this.tfInput.getText();
   }

   public final void init() {
      this.tfInput.x = Canvas.hw - this.tfInput.width / 2;
      this.tfInput.y = Canvas.h - (Canvas.h - Canvas.ae[0].y + 5) - this.tfInput.height - 8;
   }

   public final void setInfoIkb(String var1, int var2, int var3) {
      this.initInfo(var1, var3);
      super.center = new Command(T.OK, var2);
      Canvas.currentDialog = this;
      this.tfInput.setFocus(true);
   }

   private void initInfo(String var1, int var2) {
      this.img = null;
      this.w = Canvas.w - 40;
      this.h = 70 * AvMain.hd;
      if (Canvas.normalFont.getWidth(var1) + 20 < this.w) {
         this.w = Canvas.normalFont.getWidth(var1) + 20;
      }

      if (this.w < Canvas.w / 2) {
         this.w = Canvas.w / 2;
      }

      this.info = Canvas.normalFont.splitFontBStrInLine(var1, this.w - 20);
      this.tfInput = new TField();
      this.tfInput.e = false;
      this.tfInput.width = this.w - 10;
      this.init();
      this.tfInput.setText("");
      this.tfInput.setIputType(var2);
      super.left = new Command(T.close, 120);
      Canvas.currentDialog = this;
   }

   public final void setImg(String var1, IAction var2, int var3) {
      this.initInfo(var1, var3);
      this.okAction = var2;
      super.center = new Command(T.OK, this.okAction);
      Canvas.currentDialog = this;
   }

   public final void setImg(String var1, IAction var2, int var3, String var4) {
      this.initInfo(var1, var3);
      if (var4 != null) {
         this.tfInput.setText(var4);
      }

      this.okAction = var2;
      super.center = new Command(T.OK, this.okAction);
      Canvas.currentDialog = this;
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.paint.paintPopupBack(var1, Canvas.hw - this.w / 2, Canvas.h - this.h - (Canvas.h - Canvas.ae[0].y + 5), this.w, this.h, 0);
      int var2 = Canvas.h - this.h - (Canvas.h - Canvas.ae[0].y + 5) + (this.h - this.tfInput.height - 8) / 2 - (this.info.length >> 1) * AvMain.hNormal - AvMain.hNormal / 2;
      if (this.img != null) {
         var1.drawImage(this.img, Canvas.hw, this.tfInput.y - this.img.getHeight() / 2 - 5 * AvMain.hd, 3);
         var2 -= this.img.getHeight() / 2;
      }

      int var3 = 0;

      for(var2 = var2; var3 < this.info.length; var2 += AvMain.hNormal) {
         Canvas.normalFont.drawString(var1, this.info[var3], Canvas.hw, var2, 2);
         ++var3;
      }

      this.tfInput.paint(var1);
      if (OnScreen.isOngame) {
         Canvas.resetTrans(var1);
         Canvas.paint.paintBackground(var1);
         Canvas.paint.paintCommandAlt(var1, super.left, super.center, super.right);
      } else {
         super.paint(var1);
      }

   }

   public final void keyPress(int var1) {
      this.tfInput.keyPressed(var1);
   }

   public final void updateKey() {
      this.tfInput.update();
      if (this.tfInput.isFocused()) {
         super.right = this.tfInput.getRightCmd();
      }

      if (OnScreen.isOngame && Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

   }
}
