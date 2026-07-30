package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class MsgDlg extends Dialog {
   private Vector info;
   private String str = "";
   private Vector list = new Vector();
   private int index = 0;
   private int w;
   private int h;
   private int x;
   private int y;
   public boolean isWaiting = false;
   private int num = 0;
   private int colorIndex = 0;
   private static final int[] RAINBOW_COLORS = new int[]{0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF00FF00, 0xFF0000FF, 0xFF4B0082, 0xFF9400D3, 0xFFFF0000, 0xFFFF7F00};
   public static FrameImage[] imgLoadRainbow = new FrameImage[9];
   private int size = 0;
   private int hText;
   private int hDu;
   private int indexLeft = 0;
   private int indexRight = 0;
   private int hCell;
   public static FrameImage imgLoad;
   private long timeDelay = -1L;
   private long limitTime;
   private long timeEnd;
   private int u = 0;
   private int spinAngle = 0;
   private static final int[] sinTable = new int[360];
   private static final int[] cosTable = new int[360];
   static {
      for (int i = 0; i < 360; i++) {
         sinTable[i] = (int)(Math.sin(Math.toRadians(i)) * 100);
         cosTable[i] = (int)(Math.cos(Math.toRadians(i)) * 100);
      }
   }

   public MsgDlg() {
      this.hText = AvMain.hBlack;
   }

   public final void setInfoC(String var1, Command var2, Vector var3) {
      if (ChatTextField.isShow) {
         ChatTextField.gI().closeChat();
      }

      this.hCell = MyScreen.hText;
      this.isWaiting = false;
      this.str = var1;
      super.center = var2;
      this.index = 0;
      this.list = var3;
      if (var3 != null) {
         Command var4 = (Command)var3.elementAt(this.index);
         super.center = var4;
         if (var4 != null) {
            super.center.action = var4.action;
            super.center.indexMenu = var4.indexMenu;
            super.center.pointer = var4.pointer;
         }

         this.u = 0;

         for(int var5 = 0; var5 < var3.size(); ++var5) {
            var2 = (Command)var3.elementAt(var5);
            if (Canvas.normalFont.getWidth(var2.caption) > this.u) {
               this.u = Canvas.normalFont.getWidth(var2.caption) + (Canvas.isKeyBoard ? this.w / 3 : 0);
            }
         }
      } else {
         this.timeEnd = System.currentTimeMillis() / 100L;
      }

      this.size = 0;
      if (this.list != null) {
         this.size = this.list.size();
      }

      this.num = 0;
      this.timeDelay = -1L;
      this.init();
      Canvas.currentDialog = Canvas.msgdlg;
   }

   public final void init() {
      this.w = Canvas.w - 80;
      if (Canvas.w < 200) {
         this.w = Canvas.w - 40;
         if (Canvas.w <= 128) {
            this.w = Canvas.w - 10;
         }
      }

      if (this.str.equals(T.pleaseWait)) {
         this.w = Canvas.hw;
      }

      this.info = Canvas.M.splitFontBStrInLineV(this.str, this.w - 16);
      this.h = this.info.size() * this.hText + 20;
      this.hDu = 0;
      if (super.center != null) {
         this.h += this.hCell + 15 * AvMain.hd;
         this.hDu += this.hCell + 15 * AvMain.hd;
      }

      if (this.h < this.hCell * 3 + (AvMain.hd - 1) * 15) {
         this.h = this.hCell * 3 + (AvMain.hd - 1) * 15;
      }

      this.x = Canvas.hw - this.w / 2;
      this.y = Canvas.hCan - Canvas.hTab - this.h - 10;
   }

   public final void setIsWaiting(boolean var1) {
      this.isWaiting = var1;
      this.h = this.info.size() * this.hText + 20;
      if (this.isWaiting) {
         this.h += 25 * AvMain.hd + 4;
         this.hDu += 25 * AvMain.hd + 4;
         this.colorIndex = 0;
      }

      int var2 = this.hCell * 3 + (AvMain.hd - 1) * 15;
      if (this.h < var2) {
         this.h = var2;
      }

      this.y = Canvas.hCan - Canvas.hTab - this.h - 10;
      this.limitTime = (long)Canvas.getSecond();
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      if (System.currentTimeMillis() / 100L - this.timeEnd >= 5L) {
         Canvas.paint.drawRectangle(var1, this.x, this.y, this.w, this.h, PaintPopup.color[0], PaintPopup.color[1], 0);
         if (super.center != null) {
            PaintPopup.fill(this.x + 1, this.y + this.h - (this.hCell + 15 * AvMain.hd - 4), this.w - 2, this.hCell, 15530985, var1);
         }

         if (this.isWaiting) {
            int cx = this.x + this.w / 2;
            int cy = this.y + 4 + (this.h - this.hDu) / 2 + this.info.size() * AvMain.hBlack / 2 + (this.h - (4 + (this.h - this.hDu) / 2 + this.info.size() * AvMain.hBlack / 2)) / 2;
            int radius = 7 * AvMain.hd;
            int dotRadius = 2 * AvMain.hd;
            int numDots = 8;
            int[] colors = new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000, 0xFFFFFF00, 0xFF00FF00};
            for (int i = 0; i < numDots; i++) {
               int angle = this.spinAngle + (360 / numDots) * i;
               int dx = cx + (radius * cosTable[angle % 360]) / 100;
               int dy = cy - (radius * sinTable[angle % 360]) / 100;
               int color = colors[i % colors.length];
               var1.setColor(color);
               var1.fillArc(dx - dotRadius, dy - dotRadius, dotRadius * 2, dotRadius * 2, 0, 360);
            }
         }

         if (this.size > 0) {
            Command var2 = (Command)this.list.elementAt(this.index);
            Canvas.normalFont.drawString(var1, var2.caption, Canvas.hw, this.y + this.h - (this.hCell + 15 * AvMain.hd - 4) + this.hCell / 2 - AvMain.hNormal / 2, 2);
            if (this.size > 1) {
               Canvas.paint.drawHighlightedArea(var1, Canvas.hw - this.u / 2 - 11, (Canvas.stypeInt != 2 ? AvMain.hNormal / 2 : 0) + this.y + this.h - (this.hCell + 15 * AvMain.hd - 4) + MyScreen.hText / 2 + 1 + (Canvas.stypeInt == 1 ? -7 : 0) + (Canvas.stypeInt == 0 ? -3 : 0), 17 + this.u, this.indexLeft / 3, this.indexRight / 3);
            }
         } else if (super.center != null) {
            Canvas.normalFont.drawString(var1, super.center.caption, Canvas.hw, this.y + this.h - (this.hCell + 15 * AvMain.hd - 4) + this.hCell / 2 - AvMain.hNormal / 2, 2);
         }

         for(int var3 = 0; var3 < this.info.size(); ++var3) {
            Canvas.M.drawString(var1, (String)this.info.elementAt(var3), Canvas.hw, this.y + 4 + (this.h - this.hDu) / 2 - this.info.size() * AvMain.hBlack / 2 + var3 * AvMain.hBlack, 2);
         }
      }

   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case -2:
            MapScr.gI().doExitGame();
            return;
         case -1:
            this.isWaiting = false;
            Canvas.currentDialog = null;
            return;
         default:
            Canvas.currentMyScreen.commandTab(var1, var2);
      }
   }

   private void setIndex(int var1) {
      if (this.size > 0) {
         this.index += var1;
         if (this.index < 0) {
            this.index = this.size - 1;
         }

         if (this.index >= this.size) {
            this.index = 0;
         }

         Command var2 = (Command)this.list.elementAt(this.index);
         super.center = var2;
      }

   }

   public final void updateKey() {
      int var2;
      if (this.isWaiting) {
         ++this.num;
         if (this.num >= 8) {
            this.num = 0;
         }

         ++this.colorIndex;
         if (this.colorIndex >= 8) {
            this.colorIndex = 0;
         }

         this.spinAngle = (this.spinAngle + 15) % 360;

         if ((long)Canvas.getSecond() - this.limitTime > 30L) {
            String var1 = "";

            for(var2 = 0; var2 < this.info.size(); ++var2) {
               var1 = var1 + (String)this.info.elementAt(var2) + " ";
            }

            Canvas.startOK(var1, -2, (AvMain)null);
         }
      }

      if (this.timeDelay != -1L && System.currentTimeMillis() / 100L - this.timeDelay > 0L) {
         Canvas.keyPressed[5] = true;
      }

      if (this.indexLeft > 0) {
         --this.indexLeft;
      }

      if (this.indexRight > 0) {
         --this.indexRight;
      }

      if (Canvas.isKeyPressed(4)) {
         this.setIndex(-1);
         this.indexLeft = 5;
      } else if (Canvas.isKeyPressed(6)) {
         this.setIndex(1);
         this.indexRight = 5;
      }

      if (Canvas.isPointerRelease) {
         label85: {
            int var3 = 0;
            if (this.list != null && this.list.size() > 0) {
               Command var4 = (Command)this.list.elementAt(this.index);
               var3 = Canvas.normalFont.getWidth(var4.caption) + 20;
            } else if (super.center != null) {
               var3 = Canvas.normalFont.getWidth(super.center.caption) + 20;
            }

            var3 *= AvMain.hd;
            if (super.center != null && Canvas.isPointer(Canvas.hw - var3 / 2, this.y + this.h - (this.hCell + 18 * AvMain.hd - 4), var3, this.hCell)) {
               Canvas.endDlg();
               this.perform(super.center);
            } else {
               if (!Canvas.isPointer(this.x + 1, this.y + this.h - (this.hCell + 18 * AvMain.hd - 4), this.w - 2, this.hCell)) {
                  break label85;
               }

               if ((var2 = Canvas.hw - Canvas.px) > var3 / 2) {
                  this.setIndex(-1);
                  this.indexLeft = 5;
               } else if (var2 < -var3 / 2) {
                  this.setIndex(1);
                  this.indexRight = 5;
               }
            }

            Canvas.isPointerRelease = false;
         }
      }

      super.updateKey();
   }
}
