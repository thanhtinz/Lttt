package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class GamePad {
   private int padX;
   private int padY;
   private int padW;
   private int padH;
   private int cellW;
   private int cellH;
   private int gridX;
   private int gridY;
   private int gridCellH;
   private int gridCellW;
   private int padCols;
   private int padRows;
   private int gridCols;
   private int gridRows;
   private String[] padLabels;
   private String[] keysLower;
   private String[] keysUpper;
   private String[] keysNum;
   private String[] currentKeys;
   private byte[] keyCodes;
   private int selectedIndex = -1;
   public boolean isPointerClick = false;
   private boolean isHolding = false;
   private long holdStartTime;
   private String[] dirNames = new String[]{"Top", "Down", "Left", "Right"};
   private byte[] dirFrames = new byte[]{4, 7, 0, 2};

   public GamePad() {
      int var1;
      if (Canvas.h >= Canvas.w) {
         Canvas.G = false;
         this.padH = Canvas.h / 6 << 1;
         Canvas.h -= this.padH;
         this.padX = 0;
         this.padY = Canvas.h + 4;
         this.padH -= 4;
         this.padW = Canvas.w;
         this.cellW = this.padW / 4;
         this.cellH = this.padH / 2;
         this.gridX = this.padX;
         this.gridY = this.padY;
         this.gridCellH = this.padH / 3;
         this.gridCellW = this.padW / 4;
         this.padCols = 4;
         this.padRows = 2;
         this.gridCols = 4;
         this.gridRows = 3;
         this.padLabels = new String[]{"-", "Top", "ABC", "-", "Left", "Down", "Right", "OK"};
         this.keysLower = new String[]{".,?!1", "abc2", "def3", T.del, "ghi4", "jkl5", "mno6", T.finish, "pqrs7", "tuv8", "wxyz9", "0"};
         this.keysUpper = new String[12];

         for(var1 = 0; var1 < 12; ++var1) {
            this.keysUpper[var1] = this.keysLower[var1].toUpperCase();
         }

         this.keysUpper[3] = this.keysLower[3];
         this.keysNum = new String[]{"1", "2", "3", T.del, "4", "5", "6", T.finish, "7", "8", "9", "0"};
         this.keyCodes = new byte[]{-6, -1, 0, -7, -3, -2, -4, -5};
      } else {
         Canvas.G = true;
         this.padW = Canvas.w / 6 << 1;
         Canvas.w -= this.padW + 1;
         this.padY = 1;
         this.padH = Canvas.instance.getHeight();
         this.padX = Canvas.w + 4;
         this.padW -= 4;
         this.cellW = this.padW / 2;
         this.cellH = this.padH / 4;
         this.gridX = this.padX;
         this.gridY = this.padY;
         this.gridCellH = this.padH / 4;
         this.gridCellW = this.padW / 3;
         this.padCols = 2;
         this.padRows = 4;
         this.gridCols = 3;
         this.gridRows = 4;
         this.padLabels = new String[]{"-", "OK", "ABC", "Top", "Left", "Right", "-", "Down"};
         this.keysLower = new String[]{".,?!1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", T.finish, "0", T.del};
         this.keysUpper = new String[12];

         for(var1 = 0; var1 < 11; ++var1) {
            this.keysUpper[var1] = this.keysLower[var1].toUpperCase();
         }

         this.keysUpper[11] = this.keysLower[11];
         this.keysNum = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", T.finish, "0", T.del};
         this.keyCodes = new byte[]{-7, -5, 0, -1, -3, -4, -6, -2};
      }

      this.holdStartTime = -1L;
      this.updateKeypad();
   }

   private static void doDeleteFinish() {
      if (Canvas.stypeInt > 0) {
         TField.t.perform();
      } else if (ChatTextField.isShow) {
         ChatTextField.gI().right.perform();
      } else if (Canvas.currentMyScreen.right != null && Canvas.currentMyScreen.right.caption.equals(T.del)) {
         Canvas.currentMyScreen.right.action.perform();
      }

   }

   public final void updateKey() {
      int var2;
      int var3;
      if (!this.isPointerClick) {
         if (this.isHolding && Canvas.isPointerRelease) {
            this.isHolding = false;
            if (System.currentTimeMillis() / 10L - this.holdStartTime > 40L) {
               TField.keyPressedAscii();
               this.updateKeypad();
            } else {
               this.selectedIndex = -1;
               this.isPointerClick = true;
            }
         }

         if (Canvas.isPointer(this.padX, this.padY, this.padW, this.padH)) {
            if (Canvas.isPointerDown) {
               var2 = (Canvas.px - this.padX) / this.cellW;
               var3 = (Canvas.py - this.padY) / this.cellH;
               this.selectedIndex = var3 * this.padCols + var2;
               var3 = this.selectedIndex;
               if (var3 == 2) {
                  this.holdStartTime = System.currentTimeMillis() / 10L;
                  this.isHolding = true;
               } else {
                  Canvas.instance.keyPressed(this.keyCodes[var3]);
               }

               Canvas.isPointerDown = false;
            }

            if (Canvas.isPointerRelease && this.selectedIndex != -1) {
               var3 = this.selectedIndex;
               if (var3 != 2 && var3 < this.keyCodes.length) {
                  Canvas.instance.keyReleased(this.keyCodes[var3]);
               }

               this.selectedIndex = -1;
               Canvas.isPointerRelease = false;
            }
         }
      } else if (Canvas.isPointer(this.gridX, this.gridY, this.padW, this.padH)) {
         if (Canvas.isPointerDown) {
            var2 = (Canvas.px - this.gridX) / this.gridCellW;
            var3 = (Canvas.py - this.gridY) / this.gridCellH;
            this.selectedIndex = var3 * this.gridCols + var2;
            var3 = this.selectedIndex;
            if (Canvas.G && var3 < 9) {
               Canvas.instance.keyPressed(var3 + 49);
            } else if (!Canvas.G && var3 % 4 != 3) {
               Canvas.instance.keyPressed(var3 + 49 - var3 / 4);
            } else {
               switch (var3) {
                  case 3:
                     doDeleteFinish();
                  case 4:
                  case 5:
                  case 6:
                  case 8:
                  default:
                     break;
                  case 7:
                  case 9:
                     this.isPointerClick = false;
                     break;
                  case 10:
                     Canvas.instance.keyPressed(48);
                     break;
                  case 11:
                     if (Canvas.G) {
                        doDeleteFinish();
                     } else {
                        Canvas.instance.keyPressed(48);
                     }
               }
            }

            Canvas.isPointerDown = false;
         }

         if (Canvas.isPointerRelease && this.selectedIndex != -1) {
            this.selectedIndex = -1;
            Canvas.isPointerRelease = false;
         }
      }

   }

   private void updateKeypad() {
      switch (TField.mode) {
         case 0:
         case 1:
            this.currentKeys = this.keysLower;
            return;
         case 2:
            this.currentKeys = this.keysUpper;
            return;
         case 3:
            this.currentKeys = this.keysNum;
         default:
      }
   }

   public final void paint(Graphics var1) {
      var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
      var1.setClip(this.padX - 4, this.padY - 4, this.padW + 4, this.padH + 4);
      GamePad var2;
      Graphics var3;
      int var4;
      int var5;
      if (this.isPointerClick) {
         var3 = var1;
         var2 = this;
         var1.setClip(this.padX, this.padY, this.padW, this.padH);
         PaintPopup.fill(this.gridX, this.gridY, this.padW, this.padH, 8705740, var1);
         var1.setColor(1);
         var1.drawRect(this.gridX, this.gridY, this.padW - 1, this.padH - 1);

         for(var4 = 1; var4 < var2.gridCols; ++var4) {
            var3.fillRect(var2.gridX + var4 * var2.gridCellW, var2.gridY, 1, var2.padH);
         }

         for(var4 = 1; var4 < var2.gridRows; ++var4) {
            var3.fillRect(var2.gridX, var2.gridY + var4 * var2.gridCellH, var2.padW, 1);
         }

         for(var4 = 0; var4 < var2.keysUpper.length; ++var4) {
            var5 = var2.gridY + var4 / var2.gridCols * var2.gridCellH;
            var3.setClip(var2.gridX + var4 % var2.gridCols * var2.gridCellW, var5 - 5, var2.gridCellW, var2.gridCellH + 5);
            if (var2.selectedIndex == var4) {
               var3.setColor(14279153);
               var3.fillRect(var2.gridX + var4 % var2.gridCols * var2.gridCellW + 1, var5 + 1, var2.gridCellW - 2, var2.gridCellH - 2);
            }

            Canvas.normalFont.drawString(var3, var2.currentKeys[var4], var2.gridX + var4 % var2.gridCols * var2.gridCellW + var2.gridCellW / 2, var5 - 5 + var2.gridCellH / 2, 2);
         }
      } else {
         var3 = var1;
         var2 = this;
         var1.setClip(this.padX - 4, this.padY - 4, this.padW + 4, this.padH + 4);
         PaintPopup.fill(this.padX, this.padY, this.padW, this.padH, 8705740, var1);
         var1.setColor(0);
         var1.drawRect(this.padX, this.padY, this.padW - 1, this.padH - 1);

         for(var4 = 1; var4 < var2.padCols + 1; ++var4) {
            var3.fillRect(var2.padX + var4 * var2.cellW, var2.padY, 1, var2.padH);
         }

         for(var4 = 1; var4 < var2.padRows; ++var4) {
            var3.fillRect(var2.padX, var2.padY + var4 * var2.cellH, var2.padW, 1);
         }

         for(var4 = 0; var4 < var2.padLabels.length; ++var4) {
            if (var2.selectedIndex == var4) {
               var3.setColor(14279153);
               var3.fillRect(var2.padX + var4 % var2.padCols * var2.cellW + 1, var2.padY + var4 / var2.padCols * var2.cellH + 1, var2.cellW - 2, var2.cellH - 2);
            }

            var5 = var2.padX + var4 % var2.padCols * var2.cellW + var2.cellW / 2;
            int var6 = var2.padY + var4 / var2.padCols * var2.cellH + var2.cellH / 2;
            if (var2.padLabels[var4].equals("ABC")) {
               Canvas.normalFont.drawString(var3, TField.p[TField.mode], var5, var6 - 5, 2);
            } else {
               for(int var7 = 0; var7 < 4; ++var7) {
                  if (var2.padLabels[var4].equals(var2.dirNames[var7])) {
                     PaintPopup.b.drawFrame(0, var5, var6, var2.dirFrames[var7], 3, var3);
                  }
               }
            }
         }
      }

      var1.setClip(this.padX - 4, this.padY - 4, this.padW + 4, this.padH + 4);
      var1.setColor(2378578);
      if (Canvas.G) {
         var1.drawRect(this.padX - 4, this.padY, 4, this.padH);
         var1.setColor(6201499);
         var1.fillRect(this.padX - 4 + 1, this.padY + 1, 3, this.padH - 2);
         var1.setColor(2716523);
         var1.fillRect(this.padX - 4 + 3, this.padY + 1, 1, this.padH - 1);
      } else {
         var1.drawRect(this.padX, this.padY - 4, this.padW, 4);
         var1.setColor(6201499);
         var1.fillRect(this.padX + 1, this.padY - 4 + 1, this.padW - 2, 3);
         var1.setColor(2716523);
         var1.fillRect(this.padX + 1, this.padY - 4 + 3, this.padW - 1, 1);
      }

   }
}
