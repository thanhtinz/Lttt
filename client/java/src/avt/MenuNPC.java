package avt;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class MenuNPC extends MenuMain {
   private static MenuNPC me;
   private int idUser;
   private int x;
   private int y;
   private int w;
   private int h;
   private int xList;
   private int yList;
   private int wList;
   private int hList;
   private int hItem;
   private int focus;
   public static FrameImage imgDc;
   private Vector list = new Vector();
   private String nameNPC;
   private String[] textChat;
   private boolean[] isMenu;
   private int pa = 0;
   private int dyTran;
   private int timeOpen;
   private int pyLast;
   private boolean trans = false;
   private boolean isG = false;
   private long timeDelay;
   private long count;
   private long timePoint;
   private int vY;
   private int cmtoY;
   private int cmy;
   private int cmdy;
   private int cmvy;
   private int cmyLim;

   public static MenuNPC gI() {
      return me == null ? (me = new MenuNPC()) : me;
   }

   public MenuNPC() {
      this.w = 200 * AvMain.hd;
      this.h = 190 * AvMain.hd;
      this.x = (Canvas.w - this.w) / 2;
      this.y = (Canvas.h - this.h) / 2;
      this.yList = 70 * AvMain.hd;
      this.wList = 120 * AvMain.hd;
      this.xList = this.w - this.wList - 12 * AvMain.hd;
      this.hItem = 30 * AvMain.hd;
      this.hList = this.hItem * 3 + 20 * AvMain.hd;
      super.center = new Command(T.selectt, 0, this);
      super.right = new Command(T.close, 1, this);
   }

   public final void commandActionPointer(int var1) {
      switch (var1) {
         case 0:
            this.click();
            return;
         case 1:
            Canvas.menuMain = null;
         default:
      }
   }

   public final void setInfo(Vector var1, int var2, String var3, String var4, boolean[] var5) {
      this.list = var1;
      this.isMenu = var5;
      this.idUser = var2;
      this.cmyLim = var1.size() * this.hItem - (this.hList - 20 * AvMain.hd);
      if (this.cmyLim < 0) {
         this.cmyLim = 0;
      }

      this.nameNPC = var3;
      this.textChat = Canvas.M.splitFontBStrInLine(var4, this.w - 50 * AvMain.hd);
      Canvas.menuMain = this;
   }

   public final void update() {
      if (this.timeOpen > 0) {
         --this.timeOpen;
         if (this.timeOpen == 0) {
            this.click();
         }
      }

      if (this.vY != 0) {
         if (this.cmy < 0 || this.cmy > this.cmyLim) {
            this.vY -= this.vY / 4;
            this.cmy += this.vY / 20;
            if (this.vY / 10 <= 1) {
               this.vY = 0;
            }
         }

         if (this.cmy < 0) {
            if (this.cmy < -this.hList / 2) {
               this.cmy = -this.hList / 2;
               this.cmtoY = 0;
               this.vY = 0;
            }
         } else if (this.cmy > this.cmyLim) {
            if (this.cmy < this.cmyLim + this.hList / 2) {
               this.cmy = this.cmyLim + this.hList / 2;
               this.cmtoY = this.cmyLim;
               this.vY = 0;
            }
         } else {
            this.cmy += this.vY / 10;
         }

         this.cmtoY = this.cmy;
         this.vY -= this.vY / 10;
         if (this.vY / 10 == 0) {
            this.vY = 0;
         }
      } else if (this.cmy < 0) {
         this.cmtoY = 0;
      } else if (this.cmy > this.cmyLim) {
         this.cmtoY = this.cmyLim;
      }

      if (this.cmy != this.cmtoY) {
         this.cmvy = this.cmtoY - this.cmy << 2;
         this.cmdy += this.cmvy;
         this.cmy += this.cmdy >> 4;
         this.cmdy &= 15;
      }

   }

   public final void updateKey() {
      super.updateKey();
      ++this.count;
      boolean var1 = false;
      if (Canvas.a(2)) {
         --this.focus;
         if (this.focus < 0) {
            this.focus = this.list.size() - 1;
         }

         var1 = true;
      } else if (Canvas.a(8)) {
         ++this.focus;
         if (this.focus >= this.list.size()) {
            this.focus = 0;
         }

         var1 = true;
      }

      if (Canvas.isPointerClick) {
         this.pyLast = Canvas.pyLast;
         this.isG = false;
         if (Canvas.b(this.x + this.xList, this.y + this.yList, this.wList, this.hList)) {
            if (this.vY != 0) {
               this.isG = true;
            }

            Canvas.isPointerClick = false;
            this.pa = this.cmtoY;
            this.timeDelay = this.count;
            this.trans = true;
         }
      }

      if (this.trans) {
         int var2 = this.pyLast - Canvas.py;
         this.pyLast = Canvas.py;
         long var3 = this.count - this.timeDelay;
         int var5;
         int var6;
         if (Canvas.isPointerDown) {
            if (this.count % 2L == 0L) {
               this.dyTran = Canvas.py;
               this.timePoint = this.count;
            }

            this.vY = 0;
            if (Math.abs(var2) < 10 * AvMain.hd) {
               var5 = this.y + this.yList + 10 * AvMain.hd;
               var6 = this.hItem;
               if ((var5 = (this.cmtoY + Canvas.py - var5) / var6) >= 0 && var5 < this.list.size()) {
                  this.focus = var5;
               }
            }

            if (CRes.abs(Canvas.dy()) >= 10 * AvMain.hd) {
               super.isHide_ = true;
            } else if (var3 > 3L && var3 < 8L) {
               var5 = this.y + this.yList + 10 * AvMain.hd;
               var6 = this.hItem;
               if ((var5 = (this.cmtoY + Canvas.py - var5) / var6) >= 0 && var5 < this.list.size() && !this.isG) {
                  super.isHide_ = false;
               }
            }

            if (this.cmtoY < 0 || this.cmtoY > this.cmyLim) {
               this.cmtoY = this.pa + var2 / 2;
               this.pa = this.cmtoY;
            }

            this.cmy = this.cmtoY;
         }

         if (Canvas.isPointerRelease && Canvas.b(this.x, this.y, this.w, this.h)) {
            this.isG = false;
            var5 = (int)(this.count - this.timePoint);
            if (CRes.abs(var6 = this.dyTran - Canvas.py) > 40 && var5 < 10 && this.cmtoY > 0 && this.cmtoY < this.cmyLim) {
               this.vY = var6 / var5 * 10;
            }

            this.timePoint = -1L;
            if (Math.abs(var2) < 10 * AvMain.hd) {
               if (var3 <= 4L) {
                  super.isHide_ = false;
                  this.timeOpen = 5;
               } else if (!super.isHide_) {
                  this.click();
               }
            }

            this.trans = false;
            Canvas.isPointerRelease = false;
         }
      } else if (Canvas.isPointerRelease && !Canvas.b(this.x, this.y, this.w, this.h)) {
         Canvas.isPointerRelease = false;
         Canvas.menuMain = null;
      }

      if (var1) {
         this.cmtoY = this.focus * this.hItem - this.hList / 2 + this.hItem / 2;
         if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
            return;
         }

         if (this.cmtoY < 0) {
            this.cmtoY = 0;
         }
      }

   }

   private void click() {
      if (!this.isMenu[this.focus]) {
         Canvas.menuMain = null;
      } else {
         Canvas.startWaitDlg();
      }

      ((Command)this.list.elementAt(this.focus)).perform();
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.paint.paintPopupBack(var1, this.x, this.y, this.w, this.h, 0);
      var1.translate(this.x, this.y);
      var1.setColor(695195);
      var1.fillRect(12 * AvMain.hd, 12 * AvMain.hd, this.w - 24 * AvMain.hd, 50 * AvMain.hd);
      var1.setColor(12648440);
      var1.fillRect(15 * AvMain.hd, 15 * AvMain.hd, this.w - 30 * AvMain.hd, 44 * AvMain.hd);

      int var2;
      for(var2 = 0; var2 < this.textChat.length; ++var2) {
         Canvas.fontChatB.drawString(var1, this.textChat[var2], 20 * AvMain.hd, 12 * AvMain.hd + 25 * AvMain.hd - this.textChat.length * AvMain.hBlack / 2 + var2 * AvMain.hBlack, 0);
      }

      Avatar var9 = LoadMap.getAvatar(this.idUser);
      String npcName = this.nameNPC == null ? "" : this.nameNPC;
      Canvas.normalFont.drawString(var1, npcName, this.xList / 2, this.yList + this.hList / 2 - AvMain.hNormal - 20 * AvMain.hd, 2);
      if (var9 != null) {
         var9.paintIcon(var1, this.xList / 2, this.yList + this.hList / 2 + var9.height, true);
      }
      var2 = 4441283;
      FrameImage var7 = imgDc;
      int var6 = this.hList;
      int var5 = this.wList;
      int var4 = this.yList;
      int var3 = this.xList;
      Graphics var10 = var1;
      var7.drawFrame(0, var3, var4, 0, var1);
      var7.drawFrame(2, var3 + var5 - var7.frameWidth, var4, 0, var1);
      var7.drawFrame(5, var3, var4 + var6 - var7.frameHeight, 0, var1);
      var7.drawFrame(7, var3 + var5 - var7.frameWidth, var4 + var6 - var7.frameHeight, 0, var1);

      int var8;
      for(var8 = 0; var8 < (var5 - (var7.frameWidth << 1)) / var7.frameWidth; ++var8) {
         var7.drawFrame(1, var3 + (var8 + 1) * var7.frameWidth, var4, 0, var10);
         var7.drawFrame(6, var3 + (var8 + 1) * var7.frameWidth, var4 + var6 - var7.frameHeight, 0, var10);
      }

      var7.drawFrame(1, var3 + var5 - (var7.frameWidth << 1), var4, 0, var10);
      var7.drawFrame(6, var3 + var5 - (var7.frameWidth << 1), var4 + var6 - var7.frameHeight, 0, var10);

      for(var8 = 0; var8 < (var6 - (var7.frameHeight << 1)) / var7.frameHeight; ++var8) {
         var7.drawFrame(3, var3, var4 + (var8 + 1) * var7.frameHeight, 0, var10);
         var7.drawFrame(4, var3 + var5 - var7.frameWidth, var4 + (var8 + 1) * var7.frameHeight, 0, var10);
      }

      var7.drawFrame(3, var3, var4 + var6 - (var7.frameHeight << 1), 0, var10);
      var7.drawFrame(4, var3 + var5 - var7.frameWidth, var4 + var6 - (var7.frameHeight << 1), 0, var10);
      var10.setColor(4441283);
      var10.fillRect(var3 + var7.frameWidth, var4 + var7.frameHeight, var5 - (var7.frameWidth << 1), var6 - (var7.frameHeight << 1));
      var1.translate(this.xList, this.yList);
      var1.setClip(0, 0, this.wList, this.hList);
      var1.translate(0, -this.cmy);

      for(var2 = 0; var2 < this.list.size(); ++var2) {
         Command var11 = (Command)this.list.elementAt(var2);
         if (var2 == this.focus && !super.isHide_) {
            var1.setColor(10543802);
            var1.fillRect(4 * AvMain.hd, 10 * AvMain.hd + var2 * this.hItem, this.wList - 8 * AvMain.hd, this.hItem);
         }

         Canvas.normalFont.drawString(var1, var11.caption, 10 * AvMain.hd, 10 * AvMain.hd + var2 * this.hItem + this.hItem / 2 - AvMain.hNormal / 2, 0);
      }

      super.paint(var1);
   }

   static {
      try {
         imgDc = new FrameImage(Image.createImage(T.getPath() + "/race/popup/tile0.png"), 20 * AvMain.hd, 20 * AvMain.hd);
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }
}
