package avt;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class TransMoneyDlg extends Dialog {
   private FrameImage imgButton;
   public static TransMoneyDlg me;
   private int x;
   private int y;
   private int w;
   private int h;
   private int hItem;
   private int wItem;
   private int focus;
   private int[] money;

   public static TransMoneyDlg gI() {
      return me == null ? (me = new TransMoneyDlg()) : me;
   }

   public final void init() {
      TransMoneyDlg var1 = this;
      if (this.imgButton == null) {
         try {
            var1.imgButton = new FrameImage(Image.createImage(T.getPath() + "/button.png"), AvMain.hd == 2 ? 112 : 52, 16 * AvMain.hd);
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         this.w = this.imgButton.frameWidth * 3 + 30 * AvMain.hd;
         this.h = this.imgButton.frameHeight * 3 + 60 * AvMain.hd;
         this.x = (Canvas.w - this.w) / 2;
         this.y = (Canvas.h - this.h) / 2;
         this.hItem = this.h / 3;
         this.wItem = this.w / 3;
         this.money = new int[]{100, 1000, 10000, 50000, 100000, 500000, 1000000, 5000000, 10000000};
         super.center = new Command(T.selectt, 0, this);
         super.right = new Command(T.close, 1, this);
      }

      Canvas.currentDialog = this;
   }

   public final void commandActionPointer(int var1) {
      switch (var1) {
         case 0:
            Canvas.startOKDlg("Bạn có chắc muốn chuyển tiền không ?", new IActionTransXeng(this));
            return;
         case 1:
            Canvas.currentDialog = null;
         default:
      }
   }

   public final void update() {
   }

   public final void updateKey() {
      super.updateKey();
      if (Canvas.isKeyPressed(2)) {
         if (this.focus / 3 > 0) {
            this.focus -= 3;
         }
      } else if (Canvas.isKeyPressed(4)) {
         if (this.focus % 3 > 0) {
            --this.focus;
         }
      } else if (Canvas.isKeyPressed(6)) {
         if (this.focus % 3 < 2) {
            ++this.focus;
         }
      } else if (Canvas.isKeyPressed(8) && this.focus / 3 < 2) {
         this.focus += 3;
      }

      if (Canvas.isPointerClick) {
         for(int var1 = 0; var1 < this.money.length; ++var1) {
            if (Canvas.isPointerInRect(this.x + var1 % 3 * this.wItem, this.y + var1 / 3 * this.hItem, this.wItem, this.hItem)) {
               Canvas.isPointerClick = false;
               this.focus = var1;
               return;
            }
         }
      }

   }

   public final void paint(Graphics var1) {
      Canvas.currentMyScreen.paintMain(var1);
      Canvas.resetTrans(var1);
      Canvas.paint.drawBox(var1, this.x, this.y, this.w, this.h);
      var1.translate(this.x, this.y);

      for(int var2 = 0; var2 < this.money.length; ++var2) {
         this.imgButton.drawFrame(this.focus == var2 ? 1 : 0, this.wItem / 2 + var2 % 3 * this.wItem, this.hItem / 2 + var2 / 3 * this.hItem, 0, 3, var1);
         Canvas.smallFontYellow.drawString(var1, String.valueOf(this.money[var2]), this.wItem / 2 + var2 % 3 * this.wItem, this.hItem / 2 + var2 / 3 * this.hItem - AvMain.hSmall / 2, 2);
      }

      Canvas.resetTrans(var1);
      OnScreen.paintTitle(var1, super.left, super.center, super.right);
   }

   static int[] getMoney(TransMoneyDlg var0) {
      return var0.money;
   }

   static int getFocus(TransMoneyDlg var0) {
      return var0.focus;
   }
}
