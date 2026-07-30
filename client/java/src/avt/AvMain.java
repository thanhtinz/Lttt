package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public class AvMain {
   public static int hd = 1;
   public static int hDuBox;
   public static int duPopup;
   public static int hFillTab;
   public Command left;
   public Command center;
   public Command right;
   public static byte hBlack;
   public static byte hBorder;
   public static byte hNormal;
   public static byte hSmall;
   public boolean isHide_;
   public static boolean isQwerty = false;
   private static byte a = 0;
   private static byte b = 0;
   private static byte c = 0;

   public void initCmd() {
   }

   public void keyPress(int var1) {
   }

   public void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.paint.paintCmd(var1, this.left, this.center, this.right);
   }

   public void commandActionPointer(int var1, int var2) {
   }

   public void commandActionPointer(int var1) {
   }

   public void commandTab(int var1, int var2) {
   }

   private void click(Command var1) {
      if (var1 != null) {
         Canvas.isPointerClick = false;
         Canvas.isPointerRelease = false;
         Canvas.endDlg();
         this.perform(var1);
      }

   }

   public void update() {
   }

   public void updateKey() {
      if (Canvas.isPointerRelease) {
         if (Canvas.isPointerInRect(0, Canvas.ae[0].y, Canvas.w - 1, Canvas.hTab)) {
            switch (Canvas.paint.getDisplayValue()) {
               case 0:
                  if (Canvas.stypeInt == 0) {
                     this.click(this.left);
                  }
                  break;
               case 1:
                  if (Canvas.stypeInt == 0) {
                     this.click(this.center);
                  }
                  break;
               case 2:
                  if (Canvas.stypeInt == 0) {
                     this.click(this.right);
                  }
            }
         }

         a = 0;
         b = 0;
         c = 0;
         if (Canvas.isPaintIconVir()) {
            if (Canvas.isPointer(0, 0, 50 * hd, 50 * hd)) {
               if (!OptionScr.isVirTualKey) {
                  if (TField.m) {
                     isQwerty = true;
                     TField.m = false;
                  }
               } else if (isQwerty) {
                  TField.m = true;
               }

               OptionScr.isVirTualKey = !OptionScr.isVirTualKey;
               Canvas.instance.sizeChanged(0, 0);
               Canvas.isPointerRelease = false;
            }

            if (GameMidlet.CLIENT_TYPE == 9 && Canvas.isPointer(50, 0, 50 * hd, 50 * hd)) {
               if (!OptionScr.isVirTualKey) {
                  OptionScr.isVirTualKey = true;
                  OptionScr.gI().mapFocus[4] = 1;
                  Canvas.instance.setSize();
                  Canvas.z.isPointerClick = true;
                  if (Canvas.currentMyScreen == MapScr.gI()) {
                     ChatTextField.gI().parentMyScreen = MapScr.gI();
                     ChatTextField.isShow = true;
                  }
               } else {
                  OptionScr.isVirTualKey = false;
                  OptionScr.gI().mapFocus[4] = 0;
                  Canvas.instance.setSize();
                  if (Canvas.currentMyScreen == MapScr.gI()) {
                     ChatTextField.isShow = false;
                  }
               }

               Canvas.isPointerRelease = false;
            }
         }
      }

      if (Canvas.isKeyPressed(5)) {
         if (this.center != null) {
            Canvas.endDlg();
            this.perform(this.center);
            return;
         }

         if (Canvas.menuMain == this) {
            this.perform(this.left);
            return;
         }
      } else {
         if (Canvas.isKeyPressed(12)) {
            this.perform(this.left);
            return;
         }

         if (Canvas.E) {
            if (Canvas.isKeyPressed(13) || Canvas.keyReleased[13]) {
               Canvas.keyReleased[13] = false;
               this.perform(this.right);
               return;
            }
         } else if (Canvas.isKeyPressed(13)) {
            this.perform(this.right);
         }
      }

   }

   public final void perform(Command var1) {
      if (var1 != null) {
         if (var1.action != null) {
            var1.action.perform();
            return;
         }

         if (var1.pointer != null) {
            var1.pointer.commandActionPointer(var1.indexMenu);
            return;
         }

         if (ChatTextField.isShow) {
            ChatTextField.gI().commandTab(var1.indexMenu, var1.subIndex);
            return;
         }

         this.commandTab(var1.indexMenu, var1.subIndex);
      }

   }
}
