package avt;

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class CustomTab extends Face {
   public static CustomTab me;
   private int x;
   private int xChange;
   private int y;
   private int w;
   private int h;
   private Vector listLabel = new Vector();
   private Vector listPic = new Vector();
   private Hashtable listImg;
   private String title = "";
   private String strTemp = "";
   private int x0 = 5;
   private int y0 = 5;
   private int wTab = 30;
   private byte idAction;
   private static int cmtoY;
   private static int cmy;
   private static int cmdy;
   private static int cmvy;
   private static int cmyLim;
   private static int yL;
   private static int wStr = 14;
   private long time;
   private int ww = 0;
   private int pa = 0;
   private boolean trans = false;
   private int vY;
   private int count;
   private int timePointY;
   private int dyTran;

   public static CustomTab gI() {
      return me == null ? (me = new CustomTab()) : me;
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.listLabel.removeAllElements();
            this.listPic.removeAllElements();
            this.listImg.clear();
            Canvas.currentFace = null;
            Canvas.endDlg();
            me = null;
            return;
         case 1:
            GlobalService.gI().doCustomTab(this.idAction);
         default:
      }
   }

   public CustomTab() {
      this.setSize();
      super.right = new Command(T.close, 0);
      wStr = AvMain.hBlack;
   }

   private void setSize() {
      this.w = Canvas.w - 20;
      this.h = Canvas.h - Canvas.hTab - 20;
   }

   public final void d() {
      this.setSize();
      this.setInfo(this.listImg, this.title, this.strTemp, this.idAction);
   }

   private void setFont(String var1) {
      int var2;
      if ((var2 = var1.indexOf("Ę")) != -1) {
         String var3 = var1.substring(0, var2);
         this.setCanhle(var3, "");
         if ((var2 = (var1 = var1.substring(var2 + 1)).indexOf("\n")) != -1) {
            var3 = var1.substring(0, var2);
            this.setCanhle(var3, "Ę");
            var1 = var1.substring(var2 + 1);
            this.setFont(var1);
         } else {
            this.setCanhle(var1, "Ę");
         }
      } else {
         this.setCanhle(var1, "");
      }

   }

   private void setCanhle(String var1, String var2) {
      if (!var1.equals("")) {
         int var3;
         if ((var3 = var1.indexOf("ę")) != -1) {
            String var4;
            if (!(var4 = var1.substring(0, var3)).equals("")) {
               this.addd(var4, var2, 0);
            }

            int var5 = Integer.parseInt(var1.substring(var3 + 1, var3 + 2));
            if ((var3 = (var1 = var1.substring(var3 + 2, var1.length())).indexOf("\n")) != -1) {
               this.addd(var1.substring(0, var3), var2, var5);
               this.setCanhle(var1.substring(var3 + 1), var2);
            } else {
               this.addd(var1, var2, var5);
            }
         } else {
            this.addd(var1, var2, 0);
         }
      }

   }

   private void addd(String var1, String var2, int var3) {
      boolean var4 = false;
      String[] var5;
      if (var1.indexOf("tem") != -1) {
         var4 = true;
         var5 = Canvas.fontChatB.splitFontBStrInLine(var1, this.w - 30 - 8 * AvMain.hd);
      } else {
         var5 = Canvas.normalFont.splitFontBStrInLine(var1, this.w - 30 - 8 * AvMain.hd);
      }

      for(int var6 = 0; var6 < var5.length; ++var6) {
         int var7;
         if (var4) {
            var7 = Canvas.fontChatB.getWidth(var5[var6]);
         } else {
            var7 = Canvas.normalFont.getWidth(var5[var6]);
         }

         if (var7 > this.ww) {
            this.ww = var7;
         }

         StringObj var8;
         (var8 = new StringObj(this.x0, this.y0 += wStr, var2 + var5[var6])).anthor = var3;
         this.listLabel.addElement(var8);
      }

   }

   public final void setInfo(Hashtable var1, String var2, String var3, byte var4) {
      this.count = 0;
      super.center = null;
      if (var4 != -1) {
         super.center = new Command(T.selectt, 1);
      }

      this.idAction = var4;
      this.ww = 0;
      this.wTab = Canvas.normalFont.getWidth(var2) + 20 * AvMain.hd;
      if (this.wTab < 50 + 20 * AvMain.hd) {
         this.wTab = 50 + 20 * AvMain.hd;
      }

      this.listImg = var1;
      this.title = var2;
      this.strTemp = var3;
      this.listLabel.removeAllElements();
      this.listPic.removeAllElements();
      boolean var9 = false;
      this.x0 = 0;
      this.y0 = -10;

      int var10;
      while((var10 = var3.indexOf("µ")) != -1) {
         String var13 = var3.substring(0, var10);
         var3 = var3.substring(var10 + 1, var3.length());
         String var5;
         if (var9) {
            var10 = var13.indexOf(",");
            var5 = var13.substring(0, var10);
            var10 = (var13 = var13.substring(var10 + 1, var13.length())).indexOf(",");
            int var6 = Integer.parseInt(var13.substring(0, var10));
            boolean var11 = Integer.parseInt(var13.substring(var10 + 1, var13.length())) != 0;
            Image var14 = (Image)this.listImg.get(var5);
            byte var7 = 0;
            if (var6 == 17) {
               var7 = 1;
            } else if (var6 == 24) {
               var7 = 2;
            }

            PictureObj var15 = new PictureObj(this, Integer.parseInt(var5), var7, this.y0 + wStr + 5, var6, var11);
            var14.getWidth();
            var15.e = var14.getHeight();
            if (var11) {
               PictureObj var12 = (PictureObj)this.listPic.elementAt(this.listPic.size() - 1);
               var6 = ((Image)this.listImg.get(String.valueOf(var12.d))).getHeight();
               if (var14.getHeight() > var6) {
                  var12.b += var14.getHeight() - var6;
               }

               var15.b = var12.b + var6 - var14.getHeight();
            }

            this.y0 = var15.b + var14.getHeight() - 10;
            this.listPic.addElement(var15);
            var13 = "";
         }

         var9 = !var9;

         while(true) {
            while((var10 = var13.indexOf("¶")) != -1) {
               var5 = var13.substring(0, var10);
               var13 = var13.substring(var10 + 1, var13.length());

               try {
                  Integer.parseInt(var5, 16);
                  this.setFont("¶" + var5);
                  this.y0 -= wStr / 2;
               } catch (Exception var15) {
                  this.setFont(var5);
               }
            }

            if (!var13.equals("")) {
               this.setFont(var13.substring(0, var13.length() - 1));
            }

            if (var3.indexOf("µ") != -1 || var3.indexOf("¶") == -1) {
               break;
            }

            var13 = var3;
            var3 = "";
         }
      }

      this.setFont(var3);
      this.xChange = 9 * AvMain.hd;
      if (this.ww < 140 * AvMain.hd) {
         this.ww = 140 * AvMain.hd;
      }

      if (this.ww >= 120 && this.ww < this.w - 30) {
         this.w = this.ww + 20 * AvMain.hd;
         this.xChange = 10 * AvMain.hd;
      }

      if (this.y0 + 10 + (wStr << 1) < this.h - 30) {
         this.h = this.y0 + 10 + (wStr << 1) + 20;
      }

      if (this.h < 80 * AvMain.hd + MyScreen.hTab) {
         this.h = 80 * AvMain.hd + MyScreen.hTab;
      }

      if ((cmyLim = this.y0 - (this.h - PaintPopup.hTab - 2 * AvMain.hDuBox - (wStr << 1))) < 0) {
         cmyLim = 0;
      }

      cmtoY = 0;
      cmy = 0;
      this.x = (Canvas.w - this.w) / 2;
      this.y = (Canvas.h - Canvas.hTab - this.h) / 2;
      this.time = System.currentTimeMillis();
   }

   public final void updateKey() {
      if (System.currentTimeMillis() - this.time >= 1000L) {
         this.time = System.currentTimeMillis();
      }

      ++this.count;
      boolean var1 = false;
      if (yL != 0) {
         yL += -yL >> 1;
      }

      if (yL == -1) {
         yL = 0;
      }

      if (Canvas.isPointerClick && Canvas.isPointer(this.x, this.y, this.w, this.h) && !this.trans) {
         this.pa = cmy;
         this.trans = true;
         this.vY = 0;
      }

      if (this.trans) {
         int var2 = Canvas.dy();
         if (Canvas.isPointerDown) {
            if (Canvas.gameTick % 3 == 0) {
               this.dyTran = Canvas.py;
               this.timePointY = this.count;
            }

            cmtoY = this.pa + var2;
            this.vY = 0;
            if (cmtoY < 0 || cmtoY > cmyLim) {
               cmtoY = this.pa + var2 / 2;
            }

            cmy = cmtoY;
         }

         if (Canvas.isPointerRelease) {
            this.trans = false;
            int var3 = this.count - this.timePointY;
            int var4;
            if (CRes.abs(var4 = this.dyTran - Canvas.py) > 40 && var3 < 10 && cmtoY > 0 && cmtoY < cmyLim) {
               this.vY = var4 / var3 * 10;
            }

            this.timePointY = -1;
            if (Math.abs(var2) < 10) {
               cmtoY = this.pa + var2;
            }
         }
      }

      if (Canvas.keyHold[2]) {
         cmtoY -= 14;
         var1 = true;
      } else if (Canvas.keyHold[8]) {
         var1 = true;
         cmtoY += 14;
      }

      if (var1) {
         if (cmtoY < 0) {
            cmtoY = 0;
         }

         if (cmtoY > cmyLim) {
            cmtoY = cmyLim;
         }
      }

      if (this.vY != 0) {
         if (cmy < 0 || cmy > cmyLim) {
            this.vY -= this.vY / 4;
            cmy += this.vY / 20;
            if (this.vY / 10 <= 1) {
               this.vY = 0;
            }
         }

         if (cmy < 0) {
            if (cmy < -this.h / 2) {
               cmy = -this.h / 2;
               cmtoY = 0;
               this.vY = 0;
            }
         } else if (cmy > cmyLim) {
            if (cmy < cmyLim + this.h / 2) {
               cmy = cmyLim + this.h / 2;
               cmtoY = cmyLim;
               this.vY = 0;
            }
         } else {
            cmy += this.vY / 10;
         }

         cmtoY = cmy;
         this.vY -= this.vY / 10;
         if (this.vY / 10 == 0) {
            this.vY = 0;
         }
      } else if (cmy < 0) {
         cmtoY = 0;
      } else if (cmy > cmyLim) {
         cmtoY = cmyLim;
      }

      if (cmy != cmtoY) {
         cmvy = cmtoY - cmy << 2;
         cmdy += cmvy;
         cmy += cmdy >> 4;
         cmdy &= 15;
      }

      super.updateKey();
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.paint.paintBoxTab(var1, this.x, this.y, this.h, this.w, 0, 0, PaintPopup.gI().wSub, this.wTab, PaintPopup.hTab, 1, 1, PaintPopup.gI().count, PaintPopup.gI().colorTab, this.title);
      var1.setClip(this.x + 4, this.y + PaintPopup.hTab + 4 * AvMain.hd, this.w - 8, this.h - PaintPopup.hTab - 8 * AvMain.hd);
      var1.translate(this.x + this.xChange, this.y + PaintPopup.hTab);
      var1.translate(0, -cmy);
      var1.setColor(0);

      int var2;
      for(var2 = 0; var2 < this.listLabel.size(); ++var2) {
         StringObj var3;
         if ((var3 = (StringObj)this.listLabel.elementAt(var2)).y > cmy - 10 && var3.y < cmy + this.h) {
            int var4;
            if (var3.str.length() > 2 && var3.str.substring(0, 1).equals("¶")) {
               var4 = Integer.parseInt(var3.str.substring(1, var3.str.length()), 16);
               PaintPopup.fill(var3.x, var3.y, Canvas.w - (var3.x << 1), 1, var4, var1);
            } else {
               var4 = var3.x;
               if (var3.anthor == 2) {
                  var4 += (this.w - 30) / 2 + 4;
               } else if (var3.anthor == 1) {
                  var4 += this.w - 30 + 10;
               }

               if (var3.str.length() > 2 && var3.str.substring(0, 1).equals("Ę")) {
                  Canvas.fontChatB.drawString(var1, var3.str.substring(1, var3.str.length()), var4, var3.y, var3.anthor);
               } else if (var3.str.length() > 1 && var3.str.substring(0, 1).equals("0")) {
                  var1.setColor(8654855);
                  int var5 = Canvas.M.getWidth(var3.str.substring(1) + "") + 20;
                  var1.fillRect(var4 - var5 / 2, var3.y + AvMain.hNormal / 2 - AvMain.hSmall / 2 - 1, var5, Canvas.M.getHeight());
                  Canvas.M.drawString(var1, var3.str.substring(1) + "", var4, var3.y + AvMain.hNormal / 2 - AvMain.hBlack / 2, var3.anthor);
               } else {
                  Canvas.normalFont.drawString(var1, var3.str, var4, var3.y, var3.anthor);
               }
            }
         }
      }

      for(var2 = 0; var2 < this.listPic.size(); ++var2) {
         PictureObj var6;
         if ((var6 = (PictureObj)this.listPic.elementAt(var2)).b + var6.e > cmy && var6.b < cmy + this.h) {
            var1.drawImage((Image)this.listImg.get(String.valueOf(var6.d)), var6.a * ((this.w - (this.xChange << 1)) / 2), var6.b, var6.c);
         }
      }

      super.paint(var1);
   }
}
