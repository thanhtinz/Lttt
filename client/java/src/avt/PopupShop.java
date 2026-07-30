package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class PopupShop extends MyScreen {
   public static PopupShop me;
   private static String[] name;
   private static int numTap;
   public static int x;
   public static int w;
   public static int sai;
   public static int h;
   private static int num = 5;
   private static int hAllCell;
   public static int numH = 5;
   private static int t;
   private static int hDuHori;
   public static int focusTap;
   private int v;
   public Vector[] listCell;
   private Vector listCmd;
   private Command[] listCell_;
   private boolean[] yy;
   private String[] z;
   public static String i;
   public static int focus = 0;
   public static boolean k = false;
   private static int numberPrice = 0;
   public static int duCam = 0;
   private static Vector strDes = new Vector();
   private static MyScreen lastScr;
   private int xL;
   private int fliped = 0;
   public boolean isFull = false;
   public static boolean isTransFocus = false;
   public static boolean isHorizontal = false;
   private static boolean isSelectedTab = false;
   private static int wPrice = 0;
   private static int H;
   private static String I = "";
   private int J;
   private int K;
   private boolean L = false;
   private int M = 0;
   private int N;

   public static PopupShop gI() {
      if (me == null) {
         me = new PopupShop();
      }

      return me;
   }

   public final void switchToMe() {
      lastScr = Canvas.currentMyScreen;
      this.xL = Canvas.h + 50;
      this.fliped = Canvas.getSecond();
      isTransFocus = true;
      wPrice = 86;
      if (Canvas.stypeInt != 0) {
         wPrice = 86 + 40 * Canvas.stypeInt;
      }

      isHorizontal = false;
      super.switchToMe();
   }

   public final void commandTab(int var1, int var2) {
      FarmItem var4;
      switch (var1) {
         case 0:
            this.close();
            return;
         case 1:
            if (focus < this.listCell[focusTap].size()) {
               ((Command)this.listCell[focusTap].elementAt(focus)).action.perform();
               this.setCaption();
            }

            return;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 14:
         default:
            break;
         case 7:
            FarmScr.gI().openBuyPopup(var2, 0);
            return;
         case 8:
            if (var2 < FarmData.listAnimalInfo.size()) {
               AnimalInfo var6 = (AnimalInfo)FarmData.listAnimalInfo.elementAt(var2);
               FarmScr.gI().doBuyAnimal(var6);
               return;
            }
            break;
         case 9:
            if (var2 < FarmData.listItemFarm.size()) {
               var4 = (FarmItem)FarmData.listItemFarm.elementAt(var2);
               FarmScr.gI().openBuyPopup(var4.ID, 4);
               return;
            }
            break;
         case 10:
            if (var2 < FarmScr.itemProduct.size()) {
               Item var5 = (Item)FarmScr.itemProduct.elementAt(var2);
               FarmScr.gI().doSellProduct(var5.ID, var5.name);
               return;
            }
            break;
         case 11:
            if (var2 < FarmScr.listFarmProduct.size()) {
               var4 = FarmScr.getFarmItem(((Item)FarmScr.listFarmProduct.elementAt(var2)).ID);
               FarmScr.gI().doSellProduct(var4.ID, var4.des);
               return;
            }
            break;
         case 12:
         case 13:
            return;
         case 15:
            byte[] var3 = new byte[]{0, 102};
            if (var2 != 0 || LoadMap.weather == -1) {
               MapScr.gI();
               MapScr.doGivingDefferent(var3[var2]);
            }

            gI().close();
      }

   }

   public final void commandActionPointer(int var1, int var2) {
      lastScr.commandActionPointer(var1, var2);
   }

   public final void close() {
      if (k) {
         k = false;
      } else {
         Canvas.cameraList.isShow = false;
         this.isFull = false;
         lastScr.switchToMe();
         if (Canvas.isInitChar) {
            if (LoadMap.TYPEMAP == 25 && Welcome.indexFarmPath != 0) {
               Canvas.welcome = new Welcome();
               if (Welcome.indexFarmPath == 2) {
                  Welcome.indexFarmPath = 3;
               }

               Canvas.welcome.initFarmPath(MapScr.instance);
               GameMidlet.avatar.direct = Base.LEFT;
               return;
            }

            if (LoadMap.TYPEMAP == 57) {
               (Canvas.welcome = new Welcome()).initShop(MapScr.instance);
            }
         }
      }

   }

   public final void initCmd() {
      super.right = new Command(T.close, 0);
      super.center = new Command(T.selectt, 1);
   }

   public PopupShop() {
      this.N = 80 * AvMain.hd;
      t = AvMain.hBlack;
      h = 30 * AvMain.hd;
      if (Canvas.w < 150) {
         h = 24;
      }

      if (Canvas.stypeInt == 1) {
         h = 35;
      }

      init();
      this.initCmd();
      H = 25 * (2 - AvMain.hd) + 40 * (Canvas.stypeInt + 1) + 10 * (AvMain.hd - 1);
   }

   public static void init() {
      w = h * 5 + 11 + AvMain.hDuBox + 2;
      sai = h * 6 + 10 + AvMain.hDuBox;
      numTap = Canvas.hw - h * 5 / 2;
      x = (Canvas.h - MyScreen.hTab) / 2 - sai / 2;
   }

   public static int getNumberPrice() {
      return numberPrice;
   }

   public static void startBuy() {
      k = true;
      updatePrice();
   }

   public static boolean isBuying() {
      return k;
   }

   public static void addStr(String var0) {
      if (var0 != null) {
         strDes.addElement(new StringObj(var0, Canvas.normalFont.getWidth(var0)));
      }

   }

   public final void addElement(String[] var1, Vector[] var2, Vector var3) {
      focusTap = 0;
      this.listCell = var2;
      this.listCell_ = new Command[var2.length];
      this.yy = new boolean[var2.length];
      this.z = new String[var2.length];
      this.listCmd = var3;
      name = var1;
      System.out.println("addElement: " + this.listCell.length);
      this.v = this.listCell.length;
      k = false;
      PaintPopup.gI().setup(name[focusTap], w, sai, this.v);
      this.setCmyLim();
   }

   public final void setCmyLim() {
      focus = 0;
      hDuHori = 0;
      if (isHorizontal || this.yy[focusTap]) {
         hDuHori = AvMain.hBlack;
      }

      if (this.listCell[focusTap] != null) {
         hAllCell = this.listCell[focusTap].size() / 5;
         boolean isContainerTab = name != null && focusTap >= 0 && focusTap < name.length && name[focusTap] != null && name[focusTap].equals(T.container);
         if (isContainerTab) {
            numH = 5;
         } else if (hAllCell >= 3 && !this.isFull && !isHorizontal && !this.yy[focusTap]) {
            numH = 5;
         } else {
            numH = 2;
         }

         if (this.listCell[focusTap].size() % 5 != 0) {
            ++hAllCell;
         }

         if (hAllCell < numH) {
            hAllCell = numH;
         }
      }

      int var1 = 1;
      if (this.listCell[focusTap] == null) {
         num = 1;
      } else {
         var1 = this.listCell[focusTap].size();
         num = 5;
      }

      duCam = -h / 2;
      if (numH > 2 || isHorizontal || this.yy[focusTap]) {
         duCam = 0;
      }

      Canvas.cameraList.setInfo(numTap, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox + (!isHorizontal && !this.yy[focusTap] ? 0 : hDuHori), h, h, h * num, h * hAllCell, h * 5, numH * h - duCam, var1);
      this.setCaption();
      PaintPopup.gI().setNameAndFocus(name[focusTap], focusTap);
   }

   public final void update() {
      lastScr.update();
      if (this.xL != 0) {
         this.xL += -this.xL >> 1;
      }

      if (this.xL == -1) {
         this.xL = 0;
      }

      if (this.listCell[focusTap] != null) {
         int var1 = this.listCell[focusTap].size();

         for(int var2 = 0; var2 < var1; ++var2) {
            if (isTransFocus) {
               ((Command)this.listCell[focusTap].elementAt(var2)).update();
            }
         }
      }

      if (this.listCell_[focusTap] != null) {
         super.left = this.listCell_[focusTap];
      } else {
         super.left = null;
      }

   }

   public static void initSecretKeys() {
      GameMidlet.m = "frp1qr";
      CRes.secretKey = "frp2qr";
      PaintPopup.name = Canvas.shiftString(GameMidlet.m, -2);
   }

   public final void setCmdLeft(Command var1, int var2) {
      this.listCell_[var2] = var1;
   }

   public final void updateKey() {
      int var2;
      if (k) {
         if (Canvas.isKeyPressed(4)) {
            this.setNumberPrice(-1);
            this.J = 5;
         } else if (Canvas.isKeyPressed(6)) {
            this.setNumberPrice(1);
            this.K = 5;
         }

         if (Canvas.isPointer(0, 0, Canvas.w, Canvas.h - Canvas.hTab)) {
            Canvas.isPointerClick = false;
         }

         if (Canvas.isPointerRelease) {
            var2 = focus % num * h;
            int var3 = (focus / num + 1) * h;
            if (var2 + h / 2 - wPrice / 2 + numTap + 5 < 0) {
               var2 = -h / 2 + wPrice / 2 - numTap - 5;
            } else if (var2 + h / 2 - wPrice / 2 + wPrice > Canvas.w) {
               var2 = Canvas.w - wPrice - h / 2 + wPrice / 2;
            }

            var2 += numTap;
            var3 += x + PaintPopup.hTab + AvMain.hDuBox;
            int var4 = (H - (AvMain.hDuBox << 1)) / 4;
            var3 += AvMain.hDuBox + 8;
            var2 = var2 + h / 2 - 35 * (Canvas.stypeInt + 1) / 2 - 2 - 10 - 10 * AvMain.hd;
            var3 = var3 + var4 / 2 + var4 + AvMain.hNormal / 2;
            if (Canvas.isPointer(var2, var3 - 15 * AvMain.hd - 5, 20 + 20 * AvMain.hd, 30 * AvMain.hd)) {
               this.setNumberPrice(-1);
               this.J = 5;
            } else if (Canvas.isPointer(var2 + 35 * (Canvas.stypeInt + 1), var3 - 15 * AvMain.hd - 5, 20 + 20 * AvMain.hd, 30 * AvMain.hd)) {
               this.setNumberPrice(1);
               this.K = 5;
            } else if (Canvas.isPointer(var2 + 20 + 20 * AvMain.hd, var3 - 15 * AvMain.hd - 5, var2 + 35 * (Canvas.stypeInt + 1) - (var2 + 20 + 20 * AvMain.hd), 30 * AvMain.hd)) {
               super.center.perform();
            }
         }

         if (this.J > 0) {
            --this.J;
         }

         if (this.K > 0) {
            --this.K;
         }
      } else {
         PopupShop var1 = this;
         if (Canvas.isKeyPressed(6)) {
            if (focus % num != num - 1 && num != 1 && !isSelectedTab) {
               ++focus;
            } else {
               this.setTap(1);
            }

            Canvas.cameraList.setSelect(focus);
            this.setCaption();
         } else if (Canvas.isKeyPressed(4)) {
            if (focus % num != 0 && num != 1 && !isSelectedTab) {
               --focus;
            } else {
               this.setTap(-1);
            }

            Canvas.cameraList.setSelect(focus);
            this.setCaption();
         } else if (Canvas.isKeyPressed(2)) {
            if (this.listCell[focusTap] != null && this.listCell[focusTap].size() > 0 && !isSelectedTab) {
               if (focus / num > 0) {
                  focus -= num;
               } else {
                  for(var2 = 0; var2 < var1.listCell.length; ++var2) {
                     if (var2 != focusTap) {
                        PaintPopup.gI().setTabColor(4, var2);
                     }
                  }

                  isSelectedTab = true;
               }
            }

            Canvas.cameraList.setSelect(focus);
            var1.setCaption();
         } else if (Canvas.isKeyPressed(8)) {
            if (isSelectedTab) {
               isSelectedTab = false;

               for(var2 = 0; var2 < var1.listCell.length; ++var2) {
                  if (var2 != focusTap) {
                     PaintPopup.gI().setTabColor(0, var2);
                  }
               }
            } else if (num > 1 && focus / num + 1 < hAllCell) {
               focus += num;
            }

            Canvas.cameraList.setSelect(focus);
            var1.setCaption();
         }

         if (Canvas.isPointerClick && (var2 = PaintPopup.gI().setupdateTab()) != 0) {
            var1.setTap(var2);
            Canvas.isPointerClick = false;
         }
      }

      super.updateKey();
   }

   private void setNumberPrice(int var1) {
      if ((numberPrice += var1) < 0) {
         numberPrice = 99;
      }

      if (numberPrice > 99) {
         numberPrice = 0;
      }

      this.setCaption();
      updatePrice();
   }

   public final void setSelected(int var1, boolean var2) {
      if (!k) {
         if (focus == var1 && super.center != null && !var2) {
            super.center.perform();
         }

         focus = var1;
         this.setCaption();
      }

   }

   private static void updatePrice() {
      if (focusTap == 1) {
         FarmItem var0;
         I = Canvas.getPriceMoney((var0 = (FarmItem)FarmData.listItemFarm.elementAt(focus)).priceXu * numberPrice, var0.priceLuong * numberPrice, true);
      } else {
         I = Canvas.getPriceMoney(FarmData.treeInfo[focus].priceSeed[0] * numberPrice, FarmData.treeInfo[focus].priceSeed[1] * numberPrice, true);
      }

      if ((wPrice = Canvas.normalFont.getWidth(I) + 16 + 30 * Canvas.stypeInt) < 86 * AvMain.hd) {
         wPrice = 86 * AvMain.hd;
      }

      if (Canvas.stypeInt != 0) {
         wPrice = 86 + 40 * Canvas.stypeInt;
      }

   }

   public final void setTap(int var1) {
      if ((focusTap += var1) == this.v) {
         focusTap = 0;
      }

      if (focusTap < 0) {
         focusTap = this.v - 1;
      }

      this.setCmyLim();
   }

   public final void setCaption() {
      if (this.listCell[focusTap] != null && focus < this.listCell[focusTap].size()) {
         super.center = (Command)this.listCell[focusTap].elementAt(focus);
      } else if (this.listCmd != null && focusTap < this.listCmd.size()) {
         Command var1;
         if ((var1 = (Command)this.listCmd.elementAt(focusTap)) != null) {
            super.center = var1;
         }
      } else {
         super.center = null;
      }

      isTransFocus = true;
      this.fliped = Canvas.getSecond();
   }

   public final void setHidePointer(boolean var1) {
      this.L = var1;
   }

   public final void paint(Graphics var1) {
      lastScr.paintMain(var1);
      Canvas.resetTrans(var1);
      PaintPopup.gI().paint(var1);
      var1.setColor(0);
      var1.translate(numTap, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox);
      int var3;
      int var4;
      if (isHorizontal) {
         String var2 = Canvas.getPriceMoney(GameMidlet.avatar.money[0], GameMidlet.avatar.money[2], GameMidlet.avatar.luongKhoa, true);
         var3 = Canvas.fontChatB.getWidth(var2);
         if (CRes.abs(var4 = this.M) > var3 + 20 - (w - 20)) {
            var4 = 0;
         }

         var1.setClip(0, 0, w - 20, 20);
         Canvas.fontChatB.drawString(var1, var2, var4, 0, 0);
         if (var3 > w - 20) {
            if (CRes.abs(this.M) > var3 + 50 - (w - 20)) {
               this.M = 0;
            }

            --this.M;
         }

         var1.translate(0, hDuHori);
      }

      if (this.listCell[focusTap] != null) {
         if (this.yy[focusTap]) {
            Canvas.fontChatB.drawString(var1, this.z[focusTap], 0, 0, 0);
            var1.translate(0, hDuHori);
         }

         var1.setClip(0, 0, 5 * h, numH * h - duCam);
         var1.translate(0, -CameraList.cmtoY);

         int var9;
         for(var9 = 0; var9 < hAllCell * num; ++var9) {
            PaintPopup.paintCell(var1, h * (var9 % num), h * (var9 / num), h, h);
         }

         if (!this.L && !isSelectedTab) {
            PaintPopup.fill(3 + focus % num * h, focus / num * h + 3, h - 5, h - 5, 2293623, var1);
         }

         var9 = this.listCell[focusTap].size();
         if ((var3 = CameraList.cmtoY / h * num) < 0) {
            var3 = 0;
         }

         if ((var4 = CameraList.cmtoY / h * num + (numH + 1) * num) > this.listCell[focusTap].size()) {
            var4 = this.listCell[focusTap].size();
         }

         for(var3 = var3; var3 < var4 && var3 < var9; ++var3) {
            ((Command)this.listCell[focusTap].elementAt(var3)).paint(var1, h * (var3 % num), h * (var3 / num));
         }

         var1.translate(0, CameraList.cmtoY - duCam);
         var1.setClip(0, 0, w - 9, sai);
         int var5;
         int var7;
         Graphics var11;
         StringObj var12;
         if (numH == 2) {
            if (isHorizontal && MapScr.avatarShop != null) {
               var1.translate(0, hDuHori);
               var1.setColor(10674392);
               var1.fillTriangle(30 * AvMain.hd, numH * h - (AvMain.hd == 2 ? 10 : 0), 8 * AvMain.hd, numH * h + 40 * AvMain.hd, 30 * AvMain.hd + 22 * AvMain.hd, numH * h + 40 * AvMain.hd);
               var1.setColor(13364969);
               var1.fillArc(8 * AvMain.hd, numH * h + 40 * AvMain.hd - 10 * AvMain.hd, 44 * AvMain.hd, 20 * AvMain.hd, 0, 360);
               MapScr.avatarShop.paintIcon(var1, 30 * AvMain.hd, numH * h + 45 * AvMain.hd, false);
               var1.translate(60 * AvMain.hd, 0);
            }

            var11 = var1;
            if (strDes != null && focus < this.listCell[focusTap].size()) {
               var4 = isHorizontal ? 80 : 0;
               var1.setClip(0, numH * h, w - var4 + 5, sai);

               for(var5 = 0; var5 < strDes.size(); ++var5) {
                  var12 = (StringObj)strDes.elementAt(var5);
                  var7 = 0;
                  if (var12.w2 > w + 5 - var4) {
                     var12.transTextLimit(w + 5 - var4);
                     if (var12.dis >= 0) {
                        var7 = var12.dis;
                     }
                  }

                  Canvas.fontChatB.drawString(var11, var12.str, 2 - var7, numH * h + var5 * t, 0);
               }
            }
         } else {
            var11 = var1;
            PopupShop var10 = this;
            if (Canvas.getSecond() - this.fliped > 0 && !k && strDes != null && focus < this.listCell[focusTap].size()) {
               var4 = focus % num * h - this.N / 2 + h / 2;
               var5 = (focus / num + 1) * h - CameraList.cmtoY + 5;
               int var6 = strDes.size() * AvMain.hBlack + (AvMain.hDuBox << 1) + 8;
               if (var5 + var6 + x + 12 > Canvas.h) {
                  var5 -= var6 + h + 10;
               }

               if (var5 + x < 0) {
                  var5 = -x;
               }

               if (var4 + numTap + 5 + this.N > Canvas.w) {
                  var4 = Canvas.w - (numTap + 5 + this.N);
               } else if (var4 + numTap < 0) {
                  var4 = -numTap;
               }

               var1.setClip(var4, var5, this.N, var6 * AvMain.hd);
               Canvas.paint.drawRectangle(var1, var4, var5, this.N, var6, PaintPopup.color[2], PaintPopup.color[3], 1);
               var4 += AvMain.hDuBox;
               var5 += AvMain.hDuBox - AvMain.hBlack / 2;

               for(var7 = 0; var7 < strDes.size(); ++var7) {
                  var12 = (StringObj)strDes.elementAt(var7);
                  int var8 = 0;
                  if (var12.w2 > var10.N + 5) {
                     var12.transTextLimit(var10.N);
                     if (var12.dis >= 0) {
                        var8 = var12.dis;
                     }
                  }

                  Canvas.fontChatB.drawString(var11, var12.str, var4 - var8, var5 + 5 + var7 * t, 0);
               }
            }
         }

         if (k) {
            Canvas.resetTrans(var1);
            var1.translate(numTap, Canvas.cameraList.y);
            Canvas.paint.drawFormattedText(var1, focus, num, h, wPrice, numTap, H, focusTap, numberPrice, I, this.J, this.K);
         }
      } else {
         var1.setClip(-5, 0, w - 10, sai);
         ((Command)this.listCmd.elementAt(focusTap)).paint(var1, 0, 0);
      }

      if (Canvas.welcome == null || Welcome.isOut || !Welcome.isPaintArrow) {
         super.paint(var1);
      }

      Canvas.paintPlus(var1);
   }

   public static void resetIsTrans() {
      isTransFocus = false;
      strDes.removeAllElements();
      if (k) {
         updatePrice();
      }

   }
}
