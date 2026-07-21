package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class Welcome extends AvMain {
   private int x;
   private int y;
   private int wPopup;
   private int next;
   private String[][] chats;
   public byte index = 0;
   public static MyScreen lastScr;
   public static int indexFish = 0;
   private String[][] textFish;
   private static int indexShop = 0;
   private String[][] textShop;
   public static boolean isPaintArrow = false;
   private static int indexMiniMap = 0;
   private String[][] textMiniMap;
   public static int indexMapScr = 0;
   private static short[] posArrayPopupX;
   private static short[] posArrayPopupY;
   private String[][] textMapScr;
   private String[][] textKhuMuaSam;
   private static int indexKhuMuaSam = 0;
   public static int indexFarmPath = 0;
   private static int indexTask = 0;
   private String[][] textFarmPath;
   private String[][] textTask;
   private static byte[] joinOrder;
   private static int indexFarm = 0;
   private String[][] textFarm;
   public static boolean isOut = false;
   private static byte[] indexWelcomeMiniMap = new byte[]{3, 7, 4, 1, 5};

   public Welcome() {
      isOut = false;
      isPaintArrow = true;
      this.x = 10;
      this.next = 0;
      super.center = new Command("", new IActionClick(this));
      super.left = new Command(T.notTooGreedy, new IActionLeft(this));
   }

   public final void update() {
   }

   public final void updateKey() {
      if (isPaintArrow) {
         super.updateKey();
      }

      if (isPaintArrow && lastScr == Canvas.currentMyScreen && Canvas.menuMain == null && Canvas.currentDialog == null) {
         if (this.chats != null) {
            Canvas.keyHold[2] = Canvas.keyHold[4] = Canvas.keyHold[6] = Canvas.keyHold[8] = false;
         }

         if (this.chats != null && this.next < this.chats.length - 1 && Canvas.currentMyScreen != PopupShop.gI()) {
            Canvas.isPointerRelease = false;
            Canvas.isPointerDown = false;
            Canvas.isPointerClick = false;
         }
      }

   }

   private void setNext() {
      this.wPopup = this.chats[this.next].length * AvMain.hBlack + (AvMain.hDuBox << 1);
      if (this.wPopup < (AvMain.hBlack << 1) + (AvMain.hDuBox << 1)) {
         this.wPopup = (AvMain.hBlack << 1) + (AvMain.hDuBox << 1);
      }

      this.y = 5;
   }

   public final void paint(Graphics var1) {
      if (lastScr == Canvas.currentMyScreen && Canvas.menuMain == null && Canvas.currentDialog == null) {
         Canvas.resetTrans(var1);
         var1.translate(0, Canvas.ab);
         if (isPaintArrow || Canvas.gameTick % 20 > 2) {
            ChatPopup.paintRoundRect(var1, this.x, this.y, Canvas.w - (this.x << 1), this.wPopup, 16777215, 1, (byte)0);
            if (this.chats != null && this.chats[this.next] != null) {
               byte var2 = 0;
               if (this.chats[this.next].length == 1) {
                  var2 = 2;
               }

               for(int var3 = 0; var3 < this.chats[this.next].length; ++var3) {
                  Canvas.fontChatB.drawString(var1, this.chats[this.next][var3], this.x + (Canvas.w - (this.x << 1)) / 2, this.y + this.wPopup / 2 - this.chats[this.next].length * AvMain.hBlack / 2 + var3 * AvMain.hBlack - var2, 2);
               }

               ++this.index;
               if (this.index >= 8) {
                  this.index = 0;
               }

               if (Canvas.currentMyScreen == MiniMap.me) {
                  var1.translate(-MiniMap.cmx + MiniMap.gI().x, -MiniMap.cmy + MiniMap.gI().y);
               } else {
                  var1.translate(-AvCamera.gI().xCam, -AvCamera.gI().yCam);
               }
            }
         }

         if (isPaintArrow) {
            super.paint(var1);
            if (Canvas.gameTick % 10 > 5 || Canvas.stypeInt > 0) {
               FontX var4 = Canvas.borderFont;
               if (Canvas.stypeInt > 0) {
                  var4 = Canvas.M;
               }

               var4.drawString(var1, T.continuee, Canvas.ae[1].x + MyScreen.wTab / 2, Canvas.ae[1].y + Canvas.hTab / 2 - AvMain.hBorder / 2, 2);
            }
         }
      }

   }

   public final void initMiniMap() {
      if (indexMiniMap == indexWelcomeMiniMap.length + 1) {
         Canvas.welcome = null;
         Canvas.isInitChar = false;
      } else {
         if (this.textMiniMap == null) {
            this.textMiniMap = T.getTextMiniMap();
         }

         lastScr = MiniMap.me;
         isPaintArrow = true;
         if (indexMiniMap < indexWelcomeMiniMap.length) {
            MiniMap.gI().selected = indexWelcomeMiniMap[indexMiniMap];
         }

         Canvas.welcome.setText(this.textMiniMap[indexMiniMap]);
         ++indexMiniMap;
      }

   }

   private void setText(String[] var1) {
      this.chats = new String[var1.length][];

      for(int var2 = 0; var2 < this.chats.length; ++var2) {
         this.chats[var2] = Canvas.fontChatB.splitFontBStrInLine(var1[var2], Canvas.w - (this.x << 1) - 35 * AvMain.hd);
      }

      this.setNext();
      isPaintArrow = true;
   }

   public final void initMapScr() {
      if (this.textMapScr == null) {
         this.textMapScr = T.getTextMapScr();
      }

      lastScr = MapScr.instance;
      (posArrayPopupX = new short[3])[0] = 180;
      posArrayPopupX[1] = 312;
      posArrayPopupX[2] = 720;
      joinOrder = new byte[]{108, 100, 107};
      if (indexMapScr != 0) {
         if (indexMapScr == posArrayPopupX.length) {
            this.close(288, 150);
            return;
         }

         AvCamera.gI().setToPos(posArrayPopupX[indexMapScr] * AvMain.hd, 20 * AvMain.hd);
         AvCamera.isFollow = true;
      }

      if (indexMapScr != 0) {
         SubObject var1 = new SubObject(-9, posArrayPopupX[indexMapScr], 50, 20);
         LoadMap.treeLists.addElement(var1);
         LoadMap.orderVector(LoadMap.treeLists);
      }

      Canvas.welcome.setText(this.textMapScr[indexMapScr]);
      ++indexMapScr;
   }

   public final void initKhuMuaSam() {
      if (this.textKhuMuaSam == null) {
         this.textKhuMuaSam = T.getTextMuaSam();
      }

      lastScr = MapScr.instance;
      (posArrayPopupX = new short[3])[0] = 865;
      posArrayPopupX[1] = 445;
      posArrayPopupX[2] = 95;
      joinOrder = new byte[]{57, 104, 58, 100, 107};
      if (indexKhuMuaSam != 0) {
         if (indexKhuMuaSam == posArrayPopupX.length) {
            this.close(640, 150);
            return;
         }

         AvCamera.gI().setToPos(posArrayPopupX[indexKhuMuaSam] * AvMain.hd, 20 * AvMain.hd);
         AvCamera.isFollow = true;
         SubObject var1 = new SubObject(-9, posArrayPopupX[indexKhuMuaSam], 50, 20);
         LoadMap.treeLists.addElement(var1);
         LoadMap.orderVector(LoadMap.treeLists);
      }

      Canvas.welcome.setText(this.textKhuMuaSam[indexKhuMuaSam]);
      ++indexKhuMuaSam;
   }

   public static boolean isJoinMapScr(int var0) {
      if (isOut) {
         return true;
      } else {
         switch (LoadMap.TYPEMAP) {
            case 9:
               if (indexMapScr - 1 < joinOrder.length && var0 == joinOrder[indexMapScr - 1]) {
                  return true;
               }
               break;
            case 23:
               if (indexKhuMuaSam - 1 < joinOrder.length && var0 == joinOrder[indexKhuMuaSam - 1]) {
                  return true;
               }
               break;
            case 25:
               if (indexFarmPath <= joinOrder.length && var0 == joinOrder[indexFarmPath - 1]) {
                  return true;
               }
               break;
            case 57:
               if (indexShop <= joinOrder.length && var0 == joinOrder[indexShop - 1]) {
                  return true;
               }
         }

         return false;
      }
   }

   public final void initFarmPath(MyScreen var1) {
      if (this.textFarmPath == null) {
         this.textFarmPath = T.getTextFarmPath();
      }

      lastScr = var1;
      if (indexFarmPath == 0) {
         posArrayPopupX = new short[]{372, -1, -1, 220};
         posArrayPopupY = new short[]{25, -1, -1, 25};
         joinOrder = new byte[]{52, -1, -1, 24};
      } else if (indexFarmPath == this.textFarmPath.length) {
         this.close(170, 150);
         return;
      }

      if (indexFarmPath == 1) {
         removeArrow();
      }

      SubObject var2 = new SubObject(-9, posArrayPopupX[indexFarmPath], posArrayPopupY[indexFarmPath], 20);
      LoadMap.treeLists.addElement(var2);
      LoadMap.orderVector(LoadMap.treeLists);
      AvCamera.gI().setToPos(posArrayPopupX[indexFarmPath] * AvMain.hd, 20 * AvMain.hd);
      AvCamera.isFollow = true;
      Canvas.welcome.setText(this.textFarmPath[indexFarmPath]);
      ++indexFarmPath;
   }

   public final void initTash() {
      if (this.textTask == null) {
         this.textTask = T.getTextToaThiChinh();
      }

      Canvas.welcome.setText(this.textTask[indexTask]);
      ++indexTask;
   }

   private void initFarm() {
      if (this.textFarm == null) {
         this.textFarm = T.getTextFarm();
      }

      lastScr = FarmScr.instance;
      if (indexFarm == 0) {
         posArrayPopupX = new short[]{(short)(FarmScr.gI().posTree[0].x * LoadMap.w + 12), (short)(FarmScr.posBarn.x + 12), (short)FarmScr.xPosCook, (short)FarmScr.starFruil.x, (short)(FarmScr.posPond.x + 12)};
         posArrayPopupY = new short[]{36, 36, (short)(FarmScr.yPosCook + 15), 36, 36};
      }

      int var1;
      if ((var1 = indexFarm) < 3) {
         var1 = 0;
      } else if (var1 == 3) {
         var1 = 1;
      } else if (var1 == 4) {
         var1 = 2;
      } else if (var1 == 5) {
         var1 = 3;
      } else if (var1 == 6) {
         var1 = 4;
      }

      if (indexFarm < 3 || indexFarm == 4 || indexFarm == 5) {
         SubObject var2 = new SubObject(-9, posArrayPopupX[var1], posArrayPopupY[var1], 20);
         LoadMap.treeLists.addElement(var2);
         LoadMap.orderVector(LoadMap.treeLists);
      }

      AvCamera.gI().setToPos(posArrayPopupX[var1] * AvMain.hd, 36 * AvMain.hd);
      AvCamera.isFollow = true;
      Canvas.welcome.setText(this.textFarm[indexFarm]);
      ++indexFarm;
      FarmScr.gI().left = null;
   }

   public final void initShop(MyScreen var1) {
      if (this.textShop == null) {
         this.textShop = T.getTextShop();
      }

      lastScr = var1;
      if (indexShop == 0) {
         posArrayPopupX = new short[]{192};
         joinOrder = new byte[]{56};
         SubObject var2 = new SubObject(-9, posArrayPopupX[indexShop] + 12, 135, 20);
         LoadMap.treeLists.addElement(var2);
         LoadMap.orderVector(LoadMap.treeLists);
         AvCamera.gI().setToPos(posArrayPopupX[indexShop] + 12, 130 * AvMain.hd);
      } else {
         if (indexShop == this.textShop.length) {
            this.close(180, 240);
            return;
         }

         AvCamera.isFollow = true;
      }

      Canvas.welcome.setText(this.textShop[indexShop]);
      ++indexShop;
   }

   public final void initFish() {
      if (this.textFish == null) {
         this.textFish = T.getTextFish();
      }

      lastScr = MapScr.instance;
      if (indexFish == 0) {
         joinOrder = new byte[]{56};
      } else {
         if (indexFish == this.textFish.length) {
            this.close(170, 170);
            return;
         }

         if (indexFish < 4) {
            posArrayPopupX = new short[]{12, 480, 230};
            posArrayPopupY = new short[]{110, 110, 12};
            AvCamera.gI().setToPos(posArrayPopupX[indexFish - 1] * AvMain.hd, posArrayPopupY[indexFish - 1] * AvMain.hd);
            AvCamera.isFollow = true;
            SubObject var1 = new SubObject(-9, posArrayPopupX[indexFish - 1], posArrayPopupY[indexFish - 1], 20);
            LoadMap.treeLists.addElement(var1);
            LoadMap.orderVector(LoadMap.treeLists);
         } else {
            AvCamera.isFollow = false;
         }
      }

      Canvas.welcome.setText(this.textFish[indexFish]);
      ++indexFish;
   }

   private static void removeArrow() {
      for(int var0 = 0; var0 < LoadMap.treeLists.size(); ++var0) {
         MyObject var1;
         if ((var1 = (MyObject)LoadMap.treeLists.elementAt(var0)).catagory == 1 && ((SubObject)var1).type == -9) {
            LoadMap.treeLists.removeElement(var1);
            --var0;
         }
      }

   }

   private void close(int var1, int var2) {
      this.next = 0;
      isOut = true;
      removeArrow();
      SubObject var3 = new SubObject(-9, var1, var2, 20);
      LoadMap.treeLists.addElement(var3);
      LoadMap.orderVector(LoadMap.treeLists);
      AvCamera.gI().setToPos(var1 * AvMain.hd, var2 * AvMain.hd);
      AvCamera.isFollow = true;
      String[] var4 = T.getTextOut();
      Canvas.welcome.setText(var4);
   }

   public static void goFarm() {
      int var0;
      if ((var0 = indexFarm) < 3) {
         var0 = 0;
      } else if (var0 == 3) {
         var0 = 1;
      } else if (var0 == 4) {
         var0 = 2;
      }

      if (var0 < posArrayPopupX.length) {
         (Canvas.welcome = new Welcome()).initFarm();
      }

   }

   public static void restart() {
      Canvas.isInitChar = true;
      indexFarm = 0;
      indexFarmPath = 0;
      indexFish = 0;
      indexMapScr = 0;
      indexMiniMap = 0;
      indexShop = 0;
      isOut = false;
      isPaintArrow = false;
   }

   static void click(Welcome var0) {
      if (var0.next < var0.chats.length - 1) {
         ++var0.next;
         isPaintArrow = true;
         var0.setNext();
         if (LoadMap.TYPEMAP == 23) {
            if (indexKhuMuaSam == 1 && var0.next == var0.chats.length - 1) {
               AvCamera.gI().setToPos(posArrayPopupX[0], 20);
               AvCamera.isFollow = true;
               SubObject var1 = new SubObject(-9, posArrayPopupX[indexKhuMuaSam - 1], 50, 20);
               LoadMap.treeLists.addElement(var1);
               LoadMap.orderVector(LoadMap.treeLists);
               return;
            }
         } else if (LoadMap.TYPEMAP == 9 && indexMapScr == 1 && var0.next == var0.chats.length - 1) {
            var0.initMapScr();
            return;
         }
      } else if (var0.next == var0.chats.length - 1) {
         AvCamera.isFollow = false;
         if (100 == LoadMap.TYPEMAP) {
            Canvas.welcome = null;
            return;
         }

         if (Canvas.currentMyScreen == MiniMap.me && var0.textMiniMap != null && indexMiniMap == var0.textMiniMap.length) {
            var0.initMiniMap();
            return;
         }

         if (LoadMap.TYPEMAP == 24) {
            if (indexFarm == 3 || indexFarm == 4 || indexFarm == 5 || indexFarm == 6) {
               removeArrow();
               (Canvas.welcome = new Welcome()).initFarm();
               isPaintArrow = true;
               return;
            }

            if (indexFarm == 7 && isPaintArrow && !isOut) {
               var0.close(470, 168);
               return;
            }
         } else if (LoadMap.TYPEMAP == 25) {
            if (indexFarmPath == var0.textFarmPath.length - 1) {
               Canvas.welcome = null;
            }
         } else if (LoadMap.TYPEMAP == 13) {
            var0.next = 0;
            if (!isOut) {
               var0.initFish();
               return;
            }
         }

         var0.y = 5;
         isPaintArrow = false;
      }

   }
}
