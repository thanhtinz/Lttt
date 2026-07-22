package avt;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class MediumPaint implements IPaint {
   private static FrameImage a;
   private static Image b;
   private static Image c;
   private static Image d;
   private static Image e;
   private static Image[] f;
   private static FrameImage[] g;
   private static FrameImage h;
   private static FrameImage i;
   private static Image[] j;
   private static FrameImage k;
   private static FrameImage l;
   private static byte[][] cardIconInfo = new byte[][]{{4, 6, 17, 0, 27, 14, 0, 27, 36}, {4, 6, 17, 0, 17, 13, 0, 37, 13}, {4, 6, 17, 0, 17, 13, 0, 37, 13, 0, 27, 36}, {4, 6, 17, 0, 17, 13, 0, 37, 13, 0, 17, 36, 0, 37, 36}, {4, 6, 17, 0, 17, 13, 0, 37, 13, 0, 17, 36, 0, 37, 36, 0, 27, 30}, {4, 6, 17, 0, 17, 13, 0, 37, 13, 0, 17, 28, 0, 37, 28}, {4, 6, 17, 0, 17, 13, 0, 37, 13, 0, 17, 28, 0, 37, 28, 0, 27, 36}, {4, 6, 17, 0, 17, 13, 0, 37, 13, 0, 17, 28, 0, 37, 28, 0, 27, 20}, {4, 6, 17, 8, 27, 36}, {4, 6, 17, 9, 27, 36}, {4, 6, 17, 10, 27, 36}, {4, 6, 17, 0, 27, 36}, {4, 6, 17, 0, 27, 14}};
   private static int n;
   private byte o;
   private byte p;
   private byte q;
   private static int colorSelect;
   private static int colorBold;
   private static int colorNormal;
   private static int colorLight;

   public MediumPaint() {
      try {
         Canvas.imagePlug = Image.createImage(T.getPath() + "/12Plus.png");
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      FilePack.init(T.av);
      Avatar.imgHit = FrameImage.init("5", 50, 48);
      Avatar.imgKiss = FrameImage.init("2", 11, 10);
      Canvas.imgTabInfo = FilePack.getImage("transtab");
      Pet.imgShadow[0] = FilePack.getImage("s1");
      Pet.imgShadow[1] = FilePack.getImage("s2");
      PaintPopup.imgArrowUp = FrameImage.init("arrowup", 9, 6);
      MsgDlg.imgLoad = FrameImage.init("busy", 16, 16);
      Menu.imgCmd = FrameImage.init("cmd", 24, 24);
      MapScr.imgBar = FilePack.getImage("bar");
      MapScr.imgFocusP = FilePack.getImage("arF");
      FrameImage.init("icon", 17, 19);
      Avatar.imgBlog = new FrameImage(FilePack.getImage("dauhoathi"), 9, 9);
      h = FrameImage.init("check", 12, 12);
      TField.tfframe = FrameImage.init("tb", 4, 19);
      a = FrameImage.init("round", 8, 8);
      PaintPopup.b = FrameImage.init("ar2", 4, 6);
      i = new FrameImage(FilePack.getImage("arW"), 6, 11);

      for(int var1 = 0; var1 < 2; ++var1) {
         MiniMap.imgClound[var1] = FilePack.getImage("cl" + var1);
      }

      FilePack.reset();

      try {
         MyScreen.ao = Image.createImage(T.getPath() + "/on/msg0.on");
         RoomListOnScr.imgRoomStat = new FrameImage(Image.createImage(T.getPath() + "/on/stat.on"), 11, 11);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public final void paintTextBox(Graphics var1, int var2, int var3, int var4, int var5, TField var6, boolean var7) {
      if (var7) {
         TField.tfframe.drawFrame(2, var2 + 1, var3 + 1, 0, var1);
         TField.tfframe.drawFrame(3, var2 + var4 - 5, var3 + 1, 0, var1);
         PaintPopup.fill(var2 + 4, var3 + 1, var4 - 8, 2, 2716523, var1);
         var1.fillRect(var2 + 4, var3 + 18, var4 - 8, 2);
         PaintPopup.fill(var2 + 4, var3 + 3, var4 - 8, 1, 2704964, var1);
         PaintPopup.fill(var2 + 4, var3 + 4, var4 - 8, 1, 5014141, var1);
         PaintPopup.fill(var2 + 4, var3 + 5, var4 - 8, 13, 6201499, var1);
      } else {
         TField.tfframe.drawFrame(0, var2 + 1, var3 + 1, 0, var1);
         TField.tfframe.drawFrame(1, var2 + var4 - 5, var3 + 1, 0, var1);
         PaintPopup.fill(var2 + 4, var3 + 2, var4 - 9, 1, 11074288, var1);
         var1.fillRect(var2 + 4, var3 + 18, var4 - 9, 1);
         PaintPopup.fill(var2 + 4, var3 + 3, var4 - 9, 1, 2704964, var1);
         PaintPopup.fill(var2 + 4, var3 + 4, var4 - 9, 1, 5014141, var1);
         PaintPopup.fill(var2 + 4, var3 + 5, var4 - 9, 13, 6201499, var1);
      }

      var1.setClip(var2 + 3, var3 + 1, var4 - 8, var5 - 2);
      var1.setColor(0);
      if (var6.paintedText.equals("")) {
         Canvas.normalFont.drawString(var1, var6.q, 5 + var6.offsetX + var2, var3 + (var5 - AvMain.hBlack) / 2, 0);
      } else {
         Canvas.M.drawString(var1, var6.paintedText, 5 + var6.offsetX + var2, var3 + (var5 - AvMain.hBlack) / 2 + 1, 0);
      }

      if (var6.isFocused() && var6.keyInActiveState == 0 && (var6.showCaretCounter > 0 || var6.counter / 5 % 2 == 0)) {
         var1.setColor(16777215);
         var1.fillRect(5 + var6.offsetX + var2 + Canvas.M.getWidth(var6.paintedText.substring(0, var6.caretPos)) - 1 + 1, var3 + (var5 - TField.f) / 2 + 2, 1, var5 - 5 * AvMain.hd);
      }

      if (var7 && Canvas.getSecond() - TField.timeChangeMode < 2) {
         int var8 = Canvas.normalFont.getWidth(TField.p[TField.mode]);
         var1.setClip(0, 0, Canvas.w, Canvas.h);
         PaintPopup.fill(var2 + var4 - var8 - 4, var3 + 4, var8 + 1, var5 - 6, 8969676, var1);
         PaintPopup.fill(var2 + var4 - var8 - 4, var3 + 4, var8 + 1, 1, 5614233, var1);
         Canvas.normalFont.drawString(var1, TField.p[TField.mode], var2 + var4 - 3, var3 + 3, 1);
      }

   }

   public final void drawRectangle(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      a.drawFrame(0 + (var8 << 2), var2, var3, 0, var1);
      a.drawFrame(1 + (var8 << 2), var2 + var4 - 8, var3, 0, var1);
      a.drawFrame(2 + (var8 << 2), var2, var3 + var5 - 8, 0, var1);
      a.drawFrame(3 + (var8 << 2), var2 + var4 - 8, var3 + var5 - 8, 0, var1);
      PaintPopup.fill(var2 + 8, var3, var4 - 16, 8, var6, var1);
      var1.fillRect(var2 + 8, var3 + var5 - 8, var4 - 16, 7);
      var1.fillRect(var2, var3 + 8, var4, var5 - 16);
      PaintPopup.fill(var2 + 8, var3, var4 - 16, 1, var7, var1);
      var1.fillRect(var2 + 8, var3 + var5 - 1, var4 - 16, 1);
      var1.fillRect(var2, var3 + 8, 1, var5 - 16);
      var1.fillRect(var2 + var4 - 1, var3 + 8, 1, var5 - 16);
   }

   public final void paintBoxTab(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int[] var13, int[] var14, String var15) {
      Canvas.resetTrans(var1);
      this.drawRectangle(var1, var2, var3, var5, var4, PaintPopup.color[0], PaintPopup.color[1], 0);

      int var16;
      int var17;
      int var10003;
      int var18;
      for(var17 = var7; var17 < var6; ++var17) {
         var10003 = var13[var17]++;
         if (var13[var17] > 20) {
            var13[var17] = 0;
         }

         var18 = var14[var17];
         if (PaintPopup.gI().count[var17] > 5) {
            var18 = 0;
         }

         var16 = var17 - var7;
         a.drawFrame(var18 + 4, var2 + 3 + var16 * var8, var3 + 3, 0, var1);
         PaintPopup.fill(var2 + 11 + var16 * var8, var3 + 3, var9 - 16, var10 - 2, PaintPopup.color[2 + var18 / 2], var1);
         var1.fillRect(var2 + 3 + var16 * var8, var3 + 11, var9 - 1, var10);
         PaintPopup.fill(var2 + 11 + var16 * var8, var3 + 3, var9 - 16, 1, PaintPopup.color[3 + var18 / 2], var1);
         var1.fillRect(var2 + 3 + var16 * var8, var3 + 11, 1, var10 + 1);
         var1.fillRect(var2 + 2 + var9 + var16 * var8, var3 + 11, 1, var10 + 1);
      }

      var17 = var11;
      if (var11 >= var12) {
         var17 = var12 + var7;
      }

      for(var18 = var17 - 1; var18 >= var6; --var18) {
         var10003 = var13[var18]++;
         if (var13[var18] > 20) {
            var13[var18] = 0;
         }

         var11 = var14[var18];
         if (var13[var18] > 5) {
            var11 = 0;
         }

         var16 = var18 - var7;
         if (var18 == var6) {
            a.drawFrame(var11 + 4, var2 + 3 + var16 * var8, var3 + 3, 0, var1);
         }

         a.drawFrame(var11 + 5, var2 + 3 + var9 - 8 + var16 * var8, var3 + 3, 0, var1);
         PaintPopup.fill(var2 + 11 + var16 * var8, var3 + 3, var9 - 16, 8, PaintPopup.color[2 + var11 / 2], var1);
         var1.fillRect(var2 + 3 + var16 * var8, var3 + 11, var9 - 1, 15);
         PaintPopup.fill(var2 + 11 + var16 * var8, var3 + 3, var9 - 16, 1, PaintPopup.color[3 + var11 / 2], var1);
         var1.fillRect(var2 + 3 + var16 * var8, var3 + 11, 1, 20);
         var1.fillRect(var2 + 2 + var9 + var16 * var8, var3 + 11, 1, 15);
      }

      this.drawRectangle(var1, var2 + 3, var3 + var10, var5 - 6, var4 - var10 - 3, PaintPopup.color[2], PaintPopup.color[3], 1);
      PaintPopup.fill(var2 + 4 + (var6 - var7) * var8, var3 + var10 / 2, var9 - 2, var10, PaintPopup.color[2], var1);
      Canvas.normalFont.drawString(var1, var15, var2 + 3 + var9 / 2 + (var6 - var7) * var8, var3 + var10 / 2 - AvMain.hNormal / 2, 2);
   }

   public final void paintCmd(Graphics var1, Command var2, Command var3, Command var4) {
      if (var2 != null && var2.caption != null) {
         Canvas.borderFont.drawString(var1, var2.caption, Canvas.ae[0].x + 2, Canvas.ae[0].y + Canvas.hTab / 2 - AvMain.hBorder / 2, 0);
      }

      if (var3 != null && var3.caption != null) {
         Canvas.borderFont.drawString(var1, var3.caption, Canvas.ae[1].x + MyScreen.wTab / 2, Canvas.ae[1].y + Canvas.hTab / 2 - AvMain.hBorder / 2, 2);
      }

      if (var4 != null && var4.caption != null) {
         Canvas.borderFont.drawString(var1, var4.caption, Canvas.ae[2].x + MyScreen.wTab - 2, Canvas.ae[2].y + Canvas.hTab / 2 - AvMain.hBorder / 2, 1);
      }

   }

   public final void drawArea(Graphics var1, int var2, int var3, int var4, int var5) {
      this.paintPopupBack(var1, var2, var3, var4, var5, 0);
   }

   public final void initImgCard() {
      if (b == null) {
         try {
            f = new Image[14];
            g = new FrameImage[2];

            int var1;
            for(var1 = 0; var1 < 14; ++var1) {
               f[var1] = Image.createImage(T.getPath() + "/card/c" + var1 + ".png");
            }

            var1 = f[12].getWidth();
            int var2 = f[12].getHeight();
            Image var3;
            Graphics var4;
            (var4 = (var3 = Image.createImage(var1 << 1, var2 << 1)).getGraphics()).setColor(-523560);
            var4.fillRect(0, 0, var1 << 1, var2 << 1);
            var4.drawImage(f[12], 0, 0, 0);
            var4.drawRegion(f[12], 0, 0, var1, var2, 2, var1, 0, 0);
            var4.drawRegion(f[12], 0, 0, var1, var2, 1, 0, var2, 0);
            var4.drawRegion(f[12], 0, 0, var1, var2, 3, var1, var2, 0);
            var3 = CRes.createRGBImage(var3, -65315);
            f[12] = var3;
            g[0] = new FrameImage(Image.createImage(T.getPath() + "/card/f.png"), 8, 9);
            g[1] = new FrameImage(Image.createImage(T.getPath() + "/card/g.png"), 8, 9);
            b = Image.createImage(T.getPath() + "/card/cb.png");
            c = Image.createImage(T.getPath() + "/card/cb1.png");
            d = Image.createImage(T.getPath() + "/card/cb2.png");
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public final void paintHalf(Graphics var1, Card var2) {
      if (var2.cardID == -1) {
         var1.drawImage(f[12], var2.x - 27, var2.y - 36, 0);
      } else {
         var1.drawImage(c, var2.x - 27, var2.y - 36, 0);
         paintCard(var1, var2);
      }

   }

   public final void paintHalfBackFull(Graphics var1, Card var2) {
      if (var2.cardID == -1) {
         var1.drawImage(f[12], var2.x - 27, var2.y - 36, 0);
      } else {
         var1.drawImage(b, var2.x - 27, var2.y - 36, 0);
         paintCard(var1, var2);
      }

   }

   private static void paintCard(Graphics var0, Card var1) {
      int var2 = 0;

      while(var2 < 2) {
         int var3;
         if ((var3 = cardIconInfo[var1.cardMapping[var1.cardValue]][var2++]) == 0 || var3 == 4) {
            var3 += var1.cardType;
         }

         if (var1.cardType == 0 && var1.cardMapping[var1.cardValue] == 11 && var3 == 0) {
            var3 = 11;
         }

         byte var4 = cardIconInfo[var1.cardMapping[var1.cardValue]][var2++];
         byte var5 = cardIconInfo[var1.cardMapping[var1.cardValue]][var2++];
         var0.drawImage(f[var3], var1.x - 27 + var4, var1.y - 36 + var5, 3);
      }

      g[var1.cardColor].drawFrame(var1.cardMapping[var1.cardValue], var1.x - 27 + 5, var1.y - 36 + 7, 0, 3, var0);
   }

   public final void paintFull(Graphics var1, Card var2) {
      if (var2.cardID == -1) {
         var1.drawImage(f[12], var2.x - 27, var2.y - 36, 0);
      } else {
         var1.drawImage(b, var2.x - 27, var2.y - 36, 0);
         int var3 = 0;

         while(var3 < cardIconInfo[var2.cardMapping[var2.cardValue]].length) {
            int var4;
            if ((var4 = cardIconInfo[var2.cardMapping[var2.cardValue]][var3++]) == 0 || var4 == 4) {
               var4 += var2.cardType;
            }

            if (var2.cardType == 0 && var2.cardMapping[var2.cardValue] == 11 && var4 == 0) {
               var4 = 11;
            }

            byte var5 = cardIconInfo[var2.cardMapping[var2.cardValue]][var3++];
            byte var6 = cardIconInfo[var2.cardMapping[var2.cardValue]][var3++];
            var1.drawImage(f[var4], var2.x - 27 + var5, var2.y - 36 + var6, 3);
            if (var6 < 30) {
               var1.drawRegion(f[var4], 0, 0, f[var4].getWidth(), f[var4].getHeight(), 1, var2.x + 27 - var5, var2.y + 36 - var6, 3);
            }
         }

         g[var2.cardColor].drawFrame(var2.cardMapping[var2.cardValue], var2.x - 27 + 5, var2.y - 36 + 7, 0, 3, var1);
         g[var2.cardColor].drawFrame(var2.cardMapping[var2.cardValue], var2.x + 27 - 5, var2.y + 36 - 7, 3, 3, var1);
      }

   }

   public final void paintSmall(Graphics var1, Card var2, boolean var3) {
      if (var2.cardID == -1) {
         var1.drawImage(f[12], var2.x - 13, var2.y - 16, 0);
      } else {
         var1.drawImage(d, var2.x - 13, var2.y - 16, 0);
         g[var2.cardColor].drawFrame(var2.cardMapping[var2.cardValue], var2.x - 13 + 6, var2.y - 16 + 7, 0, 3, var1);
         if (var3) {
            var1.drawImage(f[var2.cardType + 4], var2.x - 13 + 6 + 7, var2.y - 16 + 7, 3);
         } else {
            var1.drawImage(f[var2.cardType + 4], var2.x - 13 + 6, var2.y - 16 + 17, 3);
         }

         var1.drawImage(f[var2.cardType], var2.x - 13 + 17, var2.y - 16 + 17, 3);
      }

   }

   public final void init() {
      AvMain.hDuBox = 5;
   }

   public final void paintMSG(Graphics var1) {
      byte var2 = 0;
      if (Canvas.currentMyScreen == LoginScr.me || Canvas.currentMyScreen == MiniMap.me) {
         var2 = 14;
      }

      if (MyScreen.nMsg > 0 && Canvas.currentDialog == null) {
         var1.drawImage(MyScreen.ao, Canvas.w - 8 * AvMain.hd - 2, var2 + 2, 17);
         Canvas.borderFont.drawString(var1, "" + MyScreen.nMsg, Canvas.w - 16 * AvMain.hd - 4, 1 + 6 * AvMain.hd - AvMain.hBorder / 2 + var2, 1);
      }

      if (MyScreen.ap != null && Canvas.isPaintIconVir()) {
         var1.drawImage(MyScreen.ap, 25, 25, 3);
         if (GameMidlet.CLIENT_TYPE == 9) {
            var1.drawImage(MyScreen.aq, 75, 25, 3);
         }
      }

   }

   public final void initPos() {
      MyScreen.hText = Canvas.h / 12;
      if ((MyScreen.hTab = Canvas.h / 18) < 18) {
         MyScreen.hTab = 18;
      }

      if (MyScreen.hTab > 45) {
         MyScreen.hTab = 45;
      }

      if (Canvas.isKeyBoard) {
         MyScreen.hTab = 35;
      }

      AvMain.hFillTab = 0;
      int var1 = Canvas.hTab = MyScreen.hTab;
      if (MyScreen.hText < 20 || Canvas.instance == null || !Canvas.isKeyBoard) {
         MyScreen.hText = 20;
      }

      if (MyScreen.hText > 50) {
         MyScreen.hText = 50;
      }

      MyScreen.wTab = Canvas.w / 4;
      Canvas.ae[0] = new AvPosition(2, Canvas.h - var1, 2);
      Canvas.ae[1] = new AvPosition(Canvas.hw - MyScreen.wTab / 2, Canvas.h - var1, 2);
      Canvas.ae[2] = new AvPosition(Canvas.w - MyScreen.wTab - 2, Canvas.h - var1, 2);
      Canvas.af = new AvPosition(Canvas.w - 2, 1, 1);
   }

   public final int getDisplayValue() {
      for(int var1 = 0; var1 < 3; ++var1) {
         if (Canvas.isPointer(Canvas.ae[var1].x, Canvas.ae[var1].y, MyScreen.wTab, Canvas.hTab)) {
            return var1;
         }
      }

      return -1;
   }

   public final void initPosLogin(LoginScr var1) {
      var1.wLogin = 176;
      if (var1.isReg) {
         var1.hLogin = 170;
      } else {
         var1.hLogin = 130;
      }

      if (var1.wLogin > Canvas.w) {
         var1.wLogin = Canvas.w;
         var1.hLogin = 100;
      }

      var1.hCellNew = (var1.hLogin - 20) / 3;
      var1.yNew = 10;
      var1.xLogin = Canvas.hw - var1.wLogin / 2;
      var1.yLogin = Canvas.hh - var1.hLogin / 2 + 5;
      int var2 = var1.yLogin + 15 + 4;
      var1.tfUser.y = var2;
      var1.tfUser.x = var1.tfPass.x = var1.tfReg.x = var1.tfEmail.x = var1.xC;
      var1.tfUser.width = var1.tfPass.width = var1.tfReg.width = var1.tfEmail.width = var1.wC;
      var2 += var1.tfUser.height + 15;
      var1.tfPass.y = var2;
      var2 += var1.tfUser.height + 15;
      var1.tfReg.y = var2;
      var1.yCheck = var2 - 10;
      var2 += var1.tfUser.height + 15;
      var1.tfEmail.y = var2;
      var1.xCheck = var1.tfPass.x - 40;
   }

   public final void paintPopupBack(Graphics var1, int var2, int var3, int var4, int var5, int var6) {
      this.drawRectangle(var1, var2, var3, var4, var5, PaintPopup.color[var6], PaintPopup.color[var6 + 1], 0);
      this.drawRectangle(var1, var2 + 3, var3 + 3, var4 - 6, var5 - 6, PaintPopup.color[2], PaintPopup.color[3], 1);
   }

   public final void paintCheckBox(Graphics var1, int var2, int var3, int var4, boolean var5) {
      byte var6 = 0;
      if (var4 == 2) {
         var6 = 1;
      }

      h.drawFrame(var6, var2, var3 + AvMain.hNormal / 2, 0, var1);
      if (var5) {
         h.drawFrame(2, var2, var3 + AvMain.hNormal / 2, 0, var1);
      }

      Canvas.normalFont.drawString(var1, T.rememPass, var2 + 15, var3 + h.frameHeight / 2, 0);
   }

   public final void drawBorder(Graphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(15530985);
      var1.fillRect(0, var3, var4, var5);
   }

   public final void drawHighlightedArea(Graphics var1, int var2, int var3, int var4, int var5, int var6) {
      PaintPopup.b.drawFrame(var5, var2 - n / 5, var3 - 3, 0, 3, var1);
      PaintPopup.b.drawFrame(var6, var2 + var4 + n / 5, var3 - 3, 3, 3, var1);
      if (++n >= 15) {
         n = 0;
      }

   }

   public final void drawString(Graphics var1, String var2, int var3, int var4, int var5) {
      Canvas.normalFont.drawString(var1, var2, var3, var4, var5);
   }

   public final void drawSelectedArea(Graphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(14279153);
      var1.fillRect(var2, var3, var4, var5);
   }

   public final void setVirtualKeyFish(int var1) {
   }

   public final void initPosPhom() {
      int var1 = Canvas.h;
      AvPosition[] var10000 = new AvPosition[]{new AvPosition(Canvas.hw + 5, 5, 0), new AvPosition(5, var1 / 2, 0), new AvPosition(Canvas.hw + 5, var1 - 50, 0), new AvPosition(Canvas.w - 5, var1 / 2, 1)};
      var10000 = new AvPosition[]{new AvPosition(Canvas.hw, 2, 3), new AvPosition(10, var1 / 2, 20), new AvPosition(Canvas.hw - 10, var1 - 75 - MyScreen.hTab, 3), new AvPosition(Canvas.w - 60, var1 / 2, 3)};
      int var2 = Canvas.h - 24;
      var1 = var1 - 15 - Canvas.hTab;
      if (Canvas.w < 200) {
         PBoardScr.c = new AvPosition[]{new AvPosition(Canvas.hw, BoardScr.hcard / 2, 0), new AvPosition(BoardScr.wCard / 2, var2 / 2, 0), new AvPosition(Canvas.hw, var1 - BoardScr.hcard + 20, 0), new AvPosition(Canvas.w - BoardScr.wCard / 2 - 3, var2 / 2, 0)};
         PBoardScr.d = new AvPosition[]{new AvPosition(Canvas.hw, BoardScr.hcard, 0), new AvPosition(BoardScr.wCard + 3, var2 / 2, 0), new AvPosition(Canvas.hw, var1 - BoardScr.hcard / 2 + 20, 0), new AvPosition(Canvas.w - 3, var2 / 2, 0)};
         PBoardScr.b = new AvPosition[]{new AvPosition(Canvas.hw, BoardScr.hcard + BoardScr.hcard / 2 + 2, 2), new AvPosition(BoardScr.wCard / 4 * 3 + BoardScr.wCard / 2 + 5, var2 / 2, 0), new AvPosition(Canvas.hw, var1 - BoardScr.hcard - AvMain.hSmall - 5, 2), new AvPosition(Canvas.w - BoardScr.wCard - 5, var2 / 2 - 5, 1)};
      } else {
         PBoardScr.c = new AvPosition[]{new AvPosition(Canvas.hw, BoardScr.hcard / 2, 0), new AvPosition(BoardScr.wCard / 2, var2 / 2, 0), new AvPosition(Canvas.hw, var1 - BoardScr.hcard / 2, 0), new AvPosition(Canvas.w - BoardScr.wCard / 2, var2 / 2, 0)};
         PBoardScr.d = new AvPosition[]{new AvPosition(Canvas.hw, 0, 0), new AvPosition(BoardScr.wCard / 4 * 3, var2 / 2, 0), new AvPosition(Canvas.hw, var1 - BoardScr.hcard / 2 + BoardScr.hcard / 4, 0), new AvPosition(Canvas.w - BoardScr.wCard / 4, var2 / 2, 0)};
         PBoardScr.b = new AvPosition[]{new AvPosition(Canvas.hw, BoardScr.hcard + 2, 2), new AvPosition(BoardScr.wCard / 4 * 3 + BoardScr.wCard / 2 + 5, var2 / 2 - 10, 0), new AvPosition(Canvas.hw, var1 - BoardScr.hcard - AvMain.hSmall - 1, 2), new AvPosition(Canvas.w - BoardScr.wCard - 5, var2 / 2 - 10, 1)};
      }

   }

   public final void drawFormattedText(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, String var10, int var11, int var12) {
      var6 = var2 % var3 * var4;
      var3 = (var2 / var3 + 1) * var4;
      var4 = var6 + var4 / 2;
      var1.setClip(var4 - var5 / 2, var3, var5, var7);
      var6 = (var7 - (AvMain.hDuBox << 1)) / 4;
      this.paintPopupBack(var1, var4 - var5 / 2, var3, var5, var7, 0);
      var3 += AvMain.hDuBox + 8;
      if (var8 == 1) {
         ((FarmItem)FarmData.listItemFarm.elementAt(var2)).paint(var1, var4, var3 + var6 / 2, 0, 3);
      } else {
         FarmData.treeInfo[var2].paint(var1, 7, var4, var3 + var6 / 2, 3);
      }

      Canvas.borderFont.drawString(var1, String.valueOf(var9), var4, var3 + var6 / 2 + var6 - 2, 2);
      Canvas.normalFont.drawString(var1, var10, var4, var3 + var6 / 2 + (var6 << 1), 2);
      var2 = var3 + var6 / 2 + var6 + AvMain.hNormal / 2;
      i.drawFrame(var11 / 3, var4 - 17, var2 + 1, 2, 3, var1);
      i.drawFrame(var12 / 3, var4 - 17 + 35, var2, 0, 3, var1);
   }

   public final void drawStateElement(Graphics var1, int var2, int var3, int var4, boolean var5, int var6, int[] var7) {
      Canvas.fontChatB.drawString(var1, T.area + var6, Canvas.hw, Canvas.hh + var2 * var4 / 2 - 20, 2);
      var1.translate(Canvas.hw - (var2 * var3 + 10) / 2 + 4, Canvas.hh - var2 * var4 / 2 + 4);
      var1.setClip(0, 3, var2 * var3 + 2, var2 * var4 - 32);
      var1.translate(1, -CameraList.cmtoY);
      if (!var5) {
         Canvas.paint.drawSelectedArea(var1, var6 % var3 * var2, var6 / var3 * var2, var2, var2);
      }

      int var8;
      if ((var8 = (var4 = CameraList.cmtoY / var2 * var3) + var2 * 7 / var2 * var3 + var3) > var7.length) {
         var8 = var7.length;
      }

      for(var4 = var4; var4 < var8; ++var4) {
         RoomListOnScr.imgRoomStat.drawFrame(var7[var4], var4 % var3 * var2 + var2 / 2, var4 / var3 * var2 + var2 / 2, 0, 3, var1);
      }

   }

   public final void paintDefaultBg(Graphics var1) {
      for(int var2 = 0; var2 < Canvas.w / 50 + 1; ++var2) {
         for(int var3 = 0; var3 < Canvas.hCan / 71 + 1; ++var3) {
            var1.drawImage(e, var2 * 50, var3 * 71, 0);
         }
      }

   }

   public final void drawElement(Graphics var1, int var2, int var3) {
      var1.drawImage(OnSplashScr.imgBg, var2, var3, 3);
   }

   public final void drawTextElements(Graphics var1, String var2, String var3, String var4) {
      var1.setClip(0, 0, Canvas.w, Canvas.h);
      Canvas.paint.paintDefaultBg(var1);
      Canvas.R.drawString(var1, var2, Canvas.hw, 2, 2);
      var1.setColor(6192786);
      var1.fillRect(0, 25, Canvas.w, MyScreen.ITEM_HEIGHT);
      Canvas.M.drawString(var1, var3, 10, 28, 0);
      Canvas.M.drawString(var1, var4, Canvas.w - 10, 28, 1);
   }

   public final void setValue(int var1) {
      try {
         if (var1 == 0) {
            BoardListOnScr.imgBoard = new FrameImage(Image.createImage(T.getPath() + "/on/imgBan2.on"), 60, 46);
         } else if (var1 == 1) {
            BoardListOnScr.imgBoard = new FrameImage(Image.createImage(T.getPath() + "/on/imgBan4.on"), 60, 46);
         } else {
            BoardListOnScr.imgBoard = new FrameImage(Image.createImage(T.getPath() + "/on/imgBan5.on"), 60, 46);
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public final void initResourceOne() {
      try {
         PaintPopup.color = new int[]{21080, 12313816, 8703190, 2713971, 5107863, 4559225};
         a = new FrameImage(Image.createImage(T.getPath() + "/on/round.on"), 8, 8);
         e = Image.createImage(T.getPath() + "/on/bg.on");
         j = new Image[8];

         for(int var1 = 0; var1 < 8; ++var1) {
            j[var1] = Image.createImage(T.getPath() + "/on/imgPopup" + var1 + ".on");
         }

         l = new FrameImage(Image.createImage(T.getPath() + "/barMoney.png"), 10, 10);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public final void initResourceTwo() {
      PaintPopup.color = new int[]{6201499, 2378578, 8705740, 2716523, 16701696, 7042560};
      FilePack.init(T.av);
      a = FrameImage.init("round", 8, 8);
      FilePack.reset();
      e = null;
      l = null;
      OnScreen.instance = null;
      TLBoardScr.instance = null;
      PBoardScr.instance = null;
      OnSplashScr.me = null;
      BoardScr.me = null;
      CasinoMsgHandler.curScr = null;
   }

   public final void initResourceThree() {
      try {
         k = new FrameImage(Image.createImage(T.getPath() + "/on/imgDoor.on"), 45, 44);
         new FrameImage(Image.createImage(T.getPath() + "/on/trangthai.on"), 11, 4);
         BoardListOnScr.imgSelectBoard = Image.createImage(T.getPath() + "/on/imgSelectban.on");
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public final void initResourceFour() {
      RoomListOnScr.me = null;
      BoardListOnScr.me = null;
      BoardScr.me = null;
      DiamondScr.me_ = null;
      BCBoardScr.me_ = null;
   }

   public final void paintPlayer(Graphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(12442838);
      byte var6 = 0;
      byte var7 = 30;
      if (var2 > 0) {
         var7 = 50;
         var6 = 40;
      }

      var1.fillRect(4, PaintPopup.hTab + 20 + AvMain.hBlack / 2 + var6 - var7 / 2, PaintPopup.gI().w - 8, var7);
      Canvas.normalFont.drawString(var1, var3 == 1 ? T.gender[0] : T.gender[1], PaintPopup.gI().w / 2, PaintPopup.hTab + 20, 2);
      PaintPopup.imgArrowUp.drawFrame(0, PaintPopup.gI().w / 2 - 35 - var4 / 2, PaintPopup.hTab + 20 + AvMain.hBlack / 2 + var6, 4, 3, var1);
      PaintPopup.imgArrowUp.drawFrame(0, PaintPopup.gI().w / 2 + 35 + var5 / 2, PaintPopup.hTab + 20 + AvMain.hBlack / 2 + var6, 7, 3, var1);
      GameMidlet.avatar.paintIcon(var1, PaintPopup.gI().w / 2 + 1, PaintPopup.hTab + 87, false);
      Canvas.normalFont.drawString(var1, T.nameStr + GameMidlet.avatar.name, PaintPopup.gI().w / 2, PaintPopup.hTab + 100, 2);
      Canvas.normalFont.drawString(var1, T.moneyStr + GameMidlet.avatar.strMoney, PaintPopup.gI().w / 2, PaintPopup.hTab + 115, 2);
   }

   public final void initResourceFive() {
      if (Canvas.isPointerClick) {
         if (Canvas.isPointerInRect(PaintPopup.gI().x + PaintPopup.gI().w / 2 - 20, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hBlack / 2, 40, 40)) {
            RegisterScr.gI().setKeyUpDown(0);
            Canvas.isPointerClick = false;
         } else if (Canvas.isPointerInRect(PaintPopup.gI().x + PaintPopup.gI().w / 2 - 20, PaintPopup.gI().y + PaintPopup.hTab + 95 - GameMidlet.avatar.height / 2 - 20, 40, 45)) {
            RegisterScr.gI().setKeyUpDown(1);
            Canvas.isPointerClick = false;
         } else if (Canvas.isPointerInRect(PaintPopup.gI().x + PaintPopup.gI().w / 2 - 20 - 40, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hBlack / 2 + 50 * RegisterScr.gI().index, 40, 40)) {
            RegisterScr.gI().setKeyLeftRight(-1);
            RegisterScr.gI().countLeft = 6;
            Canvas.isPointerClick = false;
         } else if (Canvas.isPointerInRect(PaintPopup.gI().x + PaintPopup.gI().w / 2 - 20 + 40, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hBlack / 2 + 50 * RegisterScr.gI().index, 40, 40)) {
            RegisterScr.gI().setKeyLeftRight(1);
            RegisterScr.gI().countRight = 6;
            Canvas.isPointerClick = false;
         }
      }

      if (Canvas.isKeyPressed(2)) {
         RegisterScr.gI().setKeyUpDown(RegisterScr.gI().index - 1);
      } else if (Canvas.isKeyPressed(4)) {
         RegisterScr.gI().setKeyLeftRight(-1);
         RegisterScr.gI().countLeft = 6;
      } else if (Canvas.isKeyPressed(6)) {
         RegisterScr.gI().setKeyLeftRight(1);
         RegisterScr.gI().countRight = 6;
      } else if (Canvas.isKeyPressed(8)) {
         RegisterScr.gI().setKeyUpDown(RegisterScr.gI().index + 1);
      }

   }

   public final void paintCommandAlt(Graphics var1, Command var2, Command var3, Command var4) {
      int var5 = Canvas.hCan - Canvas.hTab / 2 - AvMain.hBorder / 2;
      if (var2 != null && var2.caption != "") {
         Canvas.borderFont.drawString(var1, var2.caption, 4, var5, 0);
      }

      if (var3 != null && var3.caption != "") {
         Canvas.borderFont.drawString(var1, var3.caption, Canvas.hw, var5, 2);
      }

      if (var4 != null && var4.caption != "") {
         Canvas.borderFont.drawString(var1, var4.caption, Canvas.w - 4, var5, 1);
      }

   }

   public final void drawContainer(Graphics var1, int var2, int var3, int var4, int var5) {
      boolean var9 = true;
      var5 = var5;
      var4 = var4;
      var3 = var3;
      var2 = var2;
      var1 = var1;
      int var6 = j[0].getWidth();
      int var7 = j[0].getHeight();
      var1.drawImage(j[0], var2, var3, 0);

      int var8;
      for(var8 = 1; var8 < var4 / var6 - 1; ++var8) {
         var1.drawImage(j[1], var2 + var6 * var8, var3, 0);
      }

      var1.drawImage(j[1], var2 + var4 - (var6 << 1), var3, 0);
      var1.drawImage(j[2], var2 + var4 - var6, var3, 0);
      if (var5 / var7 > 2) {
         for(var8 = 1; var8 < var5 / var7; ++var8) {
            var1.drawImage(j[3], var2, var3 + var7 * var8, 0);
            var1.drawImage(j[4], var2 + var4 - var6, var3 + var7 * var8, 0);
         }

         var1.drawImage(j[3], var2, var3 + var5 - (var7 << 1), 0);
         var1.drawImage(j[4], var2 + var4 - var6, var3 + var5 - (var7 << 1), 0);
      }

      if (var5 > (var7 << 1) - 20 && var5 <= var7 * 3) {
         var1.drawImage(j[3], var2, var3 + var5 / 2 - var7 / 2, 0);
         var1.drawImage(j[4], var2 + var4 - var6, var3 + var5 / 2 - var7 / 2, 0);
      }

      var1.drawImage(j[5], var2, var3 + var5 - var7, 0);

      for(var8 = 1; var8 < var4 / var6 - 1; ++var8) {
         var1.drawImage(j[6], var2 + var6 * var8, var3 + var5 - var7, 0);
      }

      var1.drawImage(j[6], var2 + var4 - (var6 << 1), var3 + var5 - var7, 0);
      var1.drawImage(j[7], var2 + var4 - var6, var3 + var5 - var7, 0);
      var1.setColor(colorNormal);
      var1.fillRect(var2 + 10, var3 + 10, var4 - 20, var5 - 20);
   }

   public final void drawPanel(Graphics var1, int var2, int var3, int var4, int var5) {
      if (AvMain.hd == 1) {
         var1.setColor(colorBold);
         var1.fillRect(0, var3 + 1, var4, var5 - var3 + 1);
      } else {
         var1.setColor(colorBold);
         var1.fillRect(0, var3 + 1, var4, var5 - var3 + 2);
      }

   }

   public final void drawFrame(Graphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(colorSelect);
      var1.fillRect(2, var3, var4, var5);
   }

   public final void paintBackground(Graphics var1) {
      var1.setColor(colorBold);
      var1.fillRect(0, Canvas.hCan - Canvas.hTab + 1, Canvas.w, Canvas.hTab);
      var1.setColor(colorLight);
      var1.fillRect(0, Canvas.hCan - Canvas.hTab, Canvas.w, 1);
   }

   public final void updateKeyOn(Command var1, Command var2, Command var3) {
   }

   private static int getSoftKeyPointer(Command var0, Command var1, Command var2) {
      if (var0 != null && !var0.caption.equals("") && Canvas.isPointer(0, Canvas.hCan - Canvas.hTab, 95, Canvas.hTab)) {
         return 1;
      } else if (var1 != null && !var1.caption.equals("") && Canvas.isPointer(Canvas.w / 2 - 43 - 8, Canvas.hCan - Canvas.hTab, 95, Canvas.hTab)) {
         return 2;
      } else {
         return var2 != null && !var2.caption.equals("") && Canvas.isPointer(Canvas.w - 87 - 8, Canvas.hCan - Canvas.hTab, 95, Canvas.hTab) ? 3 : 0;
      }
   }

   public final void drawVectorElements(Graphics var1, Vector var2, int var3, int var4) {
      Canvas.resetTrans(var1);
      var1.translate(0, Canvas.cameraList.y);
      var1.translate(0, -CameraList.cmtoY);
      int var6 = (var3 - AvMain.hBorder) / 2;
      int var7;
      if ((var7 = CameraList.cmtoY / var3 - 2) < 0) {
         var7 = 0;
      }

      int var8;
      if ((var8 = var7 + (Canvas.h - 40) / var3 + 3) > var2.size()) {
         var8 = var2.size();
      }

      int var5 = 4 + var7 * var3;

      for(var7 = var7; var7 < var8; ++var7) {
         RoomInfo var9 = (RoomInfo)var2.elementAt(var7);
         if (var7 == var4 && var9.id != -1) {
            Canvas.paint.drawFrame(var1, 2, var5, Canvas.w - 4, var3);
         }

         if (var9.id == -1) {
            Canvas.borderFont.drawString(var1, T.roomLevelText[var9.lv], 15, var5 + 8 + (Canvas.stypeInt == 0 ? -4 : 0), 0);
            Canvas.paint.drawPanel(var1, 0, var5 + 25, Canvas.w, var5 + 25);
         } else {
            k.drawFrame(0, 22, var5 + var3 / 2 + 1, 0, 3, var1);
            Canvas.M.drawString(var1, T.room + var9.id, 50, var5 + var6, 0);
            if (var9.roomFree >= 0 && var9.roomFree <= 2) {
               RoomListOnScr.imgRoomStat.drawFrame(var9.roomFree, Canvas.w - 20, var5 + var3 / 2, 0, 3, var1);
            }
         }

         var5 += var3;
      }

   }

   public final void drawBox(Graphics var1, int var2, int var3, int var4, int var5) {
      l.drawFrame(0, var2, var3, 0, var1);
      l.drawFrame(1, var2 + var4 - 10, var3, 0, var1);
      l.drawFrame(2, var2, var3 + var5 - 10, 0, var1);
      l.drawFrame(3, var2 + var4 - 10, var3 + var5 - 10, 0, var1);
      var1.setColor(29555);
      var1.fillRect(var2 + 10, var3 + 1, var4 - 20, var5 - 2);
      var1.fillRect(var2 + 1, var3 + 10, 9, var5 - 20);
      var1.fillRect(var2 + var4 - 10, var3 + 10, 9, var5 - 20);
      var1.setColor(16777215);
      var1.fillRect(var2 + 10, var3, var4 - 20, 1);
      var1.fillRect(var2 + 10, var3 + var5 - 1, var4 - 20, 1);
      var1.fillRect(var2, var3 + 10, 1, var5 - 20);
      var1.fillRect(var2 + var4 - 1, var3 + 10, 1, var5 - 20);
   }

   static {
      TField.s = 0;
      PaintPopup.color = new int[]{6201499, 2378578, 8705740, 2716523, 16701696, 7042560};
      n = 0;
      colorSelect = 35217;
      colorBold = 32382;
      colorNormal = 23135;
      colorLight = 14414578;
   }
}
