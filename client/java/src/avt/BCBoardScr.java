package avt;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class BCBoardScr extends BoardScr {
   public static BCBoardScr me_;
   private Vector moneyInput = new Vector();
   private Vector vtmoneySV = new Vector();
   private Vector mapSeat = new Vector();
   private Vector xn = new Vector();
   private Vector bc = new Vector();
   private Command cmdskipBC;
   private Command cmdNextBC;
   private int xbg;
   private int ybg;
   private byte idffr = -1;
   private byte idT = -1;
   private byte V;
   private byte addfr;
   private byte addt;
   private byte seat;
   private byte countEnter;
   public static int b;
   public static int c;
   private boolean[] isFinish = new boolean[6];
   public byte[][] moneySV = new byte[5][6];
   private byte[] result = new byte[3];
   public byte saveTime;
   private byte count = 0;
   private byte autoLuot;
   private boolean canTa;
   private boolean taOK;
   private boolean beginCharTa;
   private boolean isStopXn;
   public boolean canpointer;
   private int[] moneyP;
   private Image pointer;
   public static int rW;
   public static int hH;
   private static AvPosition[] posAvatar5;
   private Vector listFireWork = new Vector();
   private Command cmdSkip;

   public static BoardScr gI() {
      return me_ == null ? (me_ = new BCBoardScr()) : me_;
   }

   public final void switchToMe() {
      this.init();
      super.switchToMe();
   }

   private void resetdata() {
      int var1;
      for(var1 = 0; var1 < this.result.length; ++var1) {
         this.result[var1] = -1;
      }

      for(var1 = 0; var1 < this.isFinish.length; ++var1) {
         this.isFinish[var1] = false;
      }

      for(var1 = 0; var1 < this.moneySV.length; ++var1) {
         for(int var2 = 0; var2 < this.moneySV[var1].length; ++var2) {
            this.moneySV[var1][var2] = 0;
         }
      }

   }

   public BCBoardScr() {
      try {
         this.pointer = Image.createImage(T.getPath() + "/on/p.on");
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.resetdata();
      this.moneyP = null;
      this.cmdskipBC = new Command(T.skip, 7);
      this.cmdNextBC = new Command(T.continuee, 8);
      this.cmdSkip = new Command(T.skip, 9);
      if (Canvas.w > 200) {
         c = 23;
         b = 23;
         hH = 48;
         rW = 48;
         if (AvMain.hd == 2) {
            hH = 96;
            rW = 96;
         }
      } else {
         c = 12;
         b = 12;
         hH = 32;
         rW = 32;
      }

      this.loadIMG();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 7:
            this.onSkip();
            break;
         case 8:
            this.doContinue();
            this.listFireWork.removeAllElements();
            BoardScr.isGameEnd = false;
            BoardScr.isStartGame = false;
            BoardScr.disableReady = false;
            super.currentPlayer = -1;
            this.moneyP = null;
            this.moneyInput.removeAllElements();
            this.vtmoneySV.removeAllElements();
            this.idffr = -1;
            this.idT = -1;
            break;
         case 9:
            if (!this.canTa) {
               if (!this.isFinish[BoardScr.getIndexByID(GameMidlet.avatar.IDDB)]) {
                  this.autoLuot = 1;
                  this.putMoneyFN();
               }
            } else if (this.idffr != -1) {
               this.idffr = -1;
               super.center.caption = "Chọn";
               super.right = this.cmdskipBC;
            }
      }

      super.commandTab(var1, var2);
   }

   public final void init() {
      super.init();
      if (Canvas.w > 150) {
         posAvatar5 = new AvPosition[]{new AvPosition(20 * AvMain.hd, 50 + 30 * AvMain.hd, 6), new AvPosition(20 * AvMain.hd, Canvas.hh + 60, 6), new AvPosition(Canvas.hw, Canvas.hCan - Canvas.hTab - 10, 33), new AvPosition(Canvas.w - 14 * AvMain.hd, Canvas.hh + 60, 10), new AvPosition(Canvas.w - 14 * AvMain.hd, 50 + 30 * AvMain.hd, 10)};
      } else {
         posAvatar5 = new AvPosition[]{new AvPosition(20, 13, 6), new AvPosition(20, Canvas.hh - 5, 6), new AvPosition(Canvas.hw, Canvas.hCan - Canvas.hTab - 10, 33), new AvPosition(Canvas.w - 14, Canvas.hh - 5, 10), new AvPosition(Canvas.w - 14, 13, 10)};
      }

   }

   public final void showFlyText5Baucua(byte var1, byte var2, int var3) {
      if (var3 != 0) {
         Avatar var6 = (Avatar)BoardScr.avatarInfos.elementAt(var1);
         Avatar var8 = (Avatar)BoardScr.avatarInfos.elementAt(var2);
         Point var5;
         (var5 = new Point(var6.x, var6.y)).distant = (short)var3;
         var5.color = CRes.rnd(3);
         var2 = (byte)CRes.tan(var8.x - var6.x, -(var8.x - var6.y));
         var5.g = var2;
         var5.catagory = (byte)CRes.rnd(-1, 1);
         var5.h = CRes.fixangle(var5.g + var5.catagory * 90);
         var2 = (byte)(10 * CRes.cos(var5.h) >> 10);
         int var4 = -(10 * CRes.sin(var5.h)) >> 10;
         var5.xTo = (short)var8.x;
         var5.yTo = (short)var8.y;
         var5.x += var2;
         var5.y += var4;
         var5.color = 0;
         var5.dis = (byte)(CRes.rnd(4) + 2);
         var5.height = (short)(8 + CRes.rnd(5));
         this.listFireWork.addElement(var5);
      }

   }

   private void resetGame() {
      this.idffr = -1;
      this.idT = -1;
      this.addfr = 0;
      this.addt = 0;
      this.seat = 0;
      this.canTa = false;
      this.taOK = false;
      this.beginCharTa = false;
      this.count = 0;
      this.canpointer = false;
      this.moneyInput.removeAllElements();
      this.vtmoneySV.removeAllElements();
      this.xn.removeAllElements();
      this.countEnter = 0;
      this.isStopXn = false;
      BoardScr.isStartGame = false;
      super.currentPlayer = -1;
      this.autoLuot = 0;
      BoardScr.disableReady = false;
      this.resetdata();

      for(int var1 = 0; var1 < this.bc.size(); ++var1) {
         ((PimgBC)this.bc.elementAt(var1)).moneyPut = 0;
      }

   }

   private void loadIMG() {
      this.bc.removeAllElements();
      this.xbg = Canvas.w / 2 - rW - rW / 2 - 10;
      this.ybg = Canvas.h / 2 - hH - 12;

      for(int var1 = 0; var1 < 6; ++var1) {
         PimgBC var2;
         (var2 = new PimgBC()).type = var1;
         var2.x = this.xbg + var1 % 3 * (rW + 10);
         var2.y = this.ybg + var1 / 3 * (hH + 8);
         this.bc.addElement(var2);
      }

   }

   private void loadXingau() {
      if (this.xn.size() <= 0) {
         int var1;
         int var2;
         if (Canvas.w > 200) {
            var1 = Canvas.w / 2 - 64 * AvMain.hd;

            for(var2 = 0; var2 < 3; ++var2) {
               this.creatXn(var1 + (var2 << 6) * AvMain.hd, 10, var2, var2, false);
            }

            return;
         }

         var1 = Canvas.w / 2 - 49;

         for(var2 = 0; var2 < 3; ++var2) {
            this.creatXn(var1 + var2 * 49, 0, var2, var2, false);
         }
      }

   }

   private void ta() {
      if (!this.taOK) {
         BoardScr.setCmdWaiting();
         this.canpointer = true;
         CasinoService.gI().ta(this.idffr, this.idT);
         BoardScr.disableReady = true;
         super.currentPlayer = -1;
      }

   }

   private void onSkip() {
      BoardScr.setCmdWaiting();
      BoardScr.disableReady = true;
      CasinoService.gI().skip();
   }

   private void setMoney() {
      ++((PimgBC)this.bc.elementAt(this.V)).moneyPut;
      BCBoardScr var1 = this;

      for(int var2 = 0; var2 < 6; ++var2) {
         PimgBC var3 = (PimgBC)var1.bc.elementAt(var2);
         int var4 = getSeatATmapSeat(var1.mapSeat, BoardScr.getIndexByID(GameMidlet.avatar.IDDB));
         int var10001 = var3.x + rW / 2;
         int var10002 = var3.y + hH / 2;
         int var10003 = var3.moneyPut;
         int var7 = getIndex(var4);
         MoneyPut var8 = new MoneyPut(var10001, var10002, var10003, var7);
         var1.moneyInput.addElement(var8);
      }

   }

   private void paintSVmoney() {
      for(int var1 = 0; var1 < 6; ++var1) {
         PimgBC var2 = (PimgBC)this.bc.elementAt(var1);
         int var3 = getSeatATmapSeat(this.mapSeat, this.seat);
         this.creatSVMoneyPut(var2.x, var2.y, var2.x, var2.y, this.moneySV[this.seat][var1], getIndex(var3), var1, var1, false);
      }

   }

   private void paintXingau(Graphics var1) {
      if (this.xn.size() > 0) {
         for(int var2 = 0; var2 < this.xn.size(); ++var2) {
            ((Xingau)this.xn.elementAt(var2)).paint(var1);
         }
      }

   }

   private static int getIndex(int var0) {
      switch (var0) {
         case 0:
            return 3;
         case 1:
            return 0;
         case 2:
            return 1;
         case 3:
            return 2;
         default:
            return -1;
      }
   }

   private void creatXn(int var1, int var2, int var3, int var4, boolean var5) {
      Xingau var6 = new Xingau(var1, var2, var3, var4, false);
      this.xn.addElement(var6);
   }

   private void creatSVMoneyPut(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
      MoneySV var10 = new MoneySV(var1, var2, var3, var4, var5, var6, var7, var8, false);
      this.vtmoneySV.addElement(var10);
   }

   public final void onSetTurn(byte var1) {
      int var2 = BoardScr.getIndexByID(GameMidlet.avatar.IDDB);
      Avatar var3 = (Avatar)BoardScr.avatarInfos.elementAt(var1);
      if (var2 == var1) {
         this.isFinish[var2] = false;
         this.canTa = true;
         super.right = null;
         this.autoLuot = 2;
         this.canpointer = false;
      }

      super.currentPlayer = var3.IDDB;
      BoardScr.interval = this.saveTime;
      BoardScr.currentTime = (long)Canvas.getSecond();
      if (!this.beginCharTa) {
         this.beginCharTa = true;
      }

      if (GameMidlet.avatar.IDDB != BoardScr.ownerID && var2 == var1) {
         super.center = BoardScr.cmdFire;
         super.center.caption = "Chọn";
         super.right = this.cmdskipBC;
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      Canvas.resetTrans(var1);
      Graphics var3 = var1;
      BCBoardScr var2 = this;

      for(int var4 = 0; var4 < var2.listFireWork.size(); ++var4) {
         Point var5;
         if ((var5 = (Point)var2.listFireWork.elementAt(var4)).dis >= 0) {
            Canvas.O.drawString(var3, "+" + var5.distant, var5.x, var5.y, 2);
         }
      }

      super.paint(var1);
   }

   public final void paintMain(Graphics var1) {
      super.paintMain(var1);
      BCBoardScr var3;
      Graphics var4;
      int var5;
      int var10003;
      int var2;
      if (BoardScr.isStartGame || BoardScr.disableReady) {
         Canvas.resetTrans(var1);
         var4 = var1;
         var3 = this;
         if (this.bc.size() > 0) {
            if (this.idffr != -1) {
               var1.setColor(16777215);
               if (Canvas.gameTick % 20 > 10) {
                  var1.fillRect(this.xbg + this.idffr % 3 * (rW + 10), this.ybg + this.idffr / 3 * (hH + 8), rW, hH);
               }
            }

            if (this.idT != -1) {
               var1.setColor(1112500);
               if (Canvas.gameTick % 20 > 10) {
                  var1.fillRect(this.xbg + this.idT % 3 * (rW + 10), this.ybg + this.idT / 3 * (hH + 8), rW, hH);
               }
            }

            for(var5 = 0; var5 < var3.bc.size(); ++var5) {
               PimgBC var6 = (PimgBC)var3.bc.elementAt(var5);
               if (AvatarData.getImgIcon((short)(Canvas.w > 200 ? 872 : 873)).count != -1) {
                  var10003 = var6.type * hH;
                  var2 = var3.xbg + var5 % 3 * (rW + 10);
                  int var10008 = var3.ybg + var5 / 3 * (hH + 8);
                  var4.drawRegion(AvatarData.getImgIcon((short)(Canvas.w > 200 ? 872 : 873)).img, 0, var10003, rW, hH, 0, var2, var10008, 0);
               }
            }
         }
      }

      this.paintNamePlayers(var1);
      if (BoardScr.isStartGame || BoardScr.disableReady) {
         Canvas.resetTrans(var1);
         var4 = var1;
         var3 = this;

         int var7;
         for(var5 = 0; var5 < BoardScr.avatarInfos.size(); ++var5) {
            Avatar var10;
            if ((var10 = (Avatar)BoardScr.avatarInfos.elementAt(var5)).IDDB == BoardScr.ownerID || var10.IDDB != -1) {
               if (var3.currentPlayer != var10.IDDB || Canvas.gameTick % 10 >= 5) {
                  Canvas.smallFontYellow.drawString(var4, var10.getMoneyNew() + T.getMoney(), var10.x, var10.y + 5, 2);
               }

               if ((var7 = getSeatATmapSeat(var3.mapSeat, BoardScr.getIndexByID(var10.IDDB))) != -1 && AvatarData.getImgIcon((short)871).count != -1) {
                  var4.drawRegion(AvatarData.getImgIcon((short)871).img, 0, getIndex(var7) * 12, 12, 12, 0, var10.x, var10.y + 5 + AvMain.hSmall, 17);
               }
            }
         }

         if (BoardScr.isStartGame || BoardScr.disableReady) {
            if ((var2 = (int)((long)BoardScr.interval - BoardScr.dieTime)) > 0 && !BoardScr.isGameEnd && this.xn.size() <= 0) {
               Canvas.O.drawString(var1, String.valueOf(var2), Canvas.hw, 10, 2);
            }

            if (this.beginCharTa) {
               if (this.count < 100) {
                  ++this.count;
               } else {
                  this.count = 100;
               }

               if (this.count < 50) {
                  Canvas.borderFont.drawString(var1, "Bắt đầu tả", Canvas.hw, this.ybg - 40, 2);
               }
            }
         }

         if (this.moneyInput.size() > 0) {
            for(var2 = 0; var2 < this.moneyInput.size(); ++var2) {
               MoneyPut var8;
               if ((var8 = (MoneyPut)this.moneyInput.elementAt(var2)).valuea > 0) {
                  var8.paint(var1);
               }
            }
         }

         if (this.vtmoneySV.size() > 0) {
            for(var2 = 0; var2 < this.vtmoneySV.size(); ++var2) {
               MoneySV var9;
               if ((var9 = (MoneySV)this.vtmoneySV.elementAt(var2)).valuea > 0 && !var9.move) {
                  FontX var11 = Canvas.O;
                  if (Canvas.w <= 200) {
                     var11 = Canvas.smallFontYellow;
                  }

                  if (Canvas.stypeInt > 0) {
                     var11 = Canvas.normalFont;
                  }

                  int var12 = var9.x + rW / 4 + var9.typePaint % 2 * rW / 2;
                  var7 = var9.y + hH / 4 + var9.typePaint / 2 * hH / 2;
                  if (AvatarData.getImgIcon((short)(Canvas.w > 200 ? 870 : 871)).count != -1) {
                     var10003 = var9.typePaint * c;
                     var1.drawRegion(AvatarData.getImgIcon((short)(Canvas.w > 200 ? 870 : 871)).img, 0, var10003, b, c, 0, var12, var7, 3);
                  }

                  var11.drawString(var1, String.valueOf(var9.valuea), var12, var7 - var11.getHeight() / 2, 2);
               }
            }
         }

         if (GameMidlet.avatar.IDDB != BoardScr.ownerID && BoardScr.isStartGame && this.xn.size() == 0) {
            var1.drawImage(this.pointer, this.xbg + rW / 2 + this.V % 3 * (rW + 10), this.ybg + hH / 2 + this.V / 3 * (hH + 8) + Canvas.gameTick % 4 + 5, 3);
         }

         this.paintXingau(var1);
      }

   }

   private void putMoneyFN() {
      BoardScr.setCmdWaiting();
      this.canpointer = true;
      CasinoService.gI().PutMoneyOk(this.bc);
      this.moneyInput.removeAllElements();
   }

   public final void updateKey() {
      super.updateKey();
      if (!this.isFinish[BoardScr.getIndexByID(GameMidlet.avatar.IDDB)] && GameMidlet.avatar.IDDB != BoardScr.ownerID) {
         BCBoardScr var2 = this;
         if (!this.canpointer && BoardScr.isStartGame && !BoardScr.isGameEnd && this.bc.size() > 0 && Canvas.isPointerClick) {
            Canvas.isPointerClick = false;

            for(int var3 = 0; var3 < var2.bc.size(); ++var3) {
               PimgBC var4 = (PimgBC)var2.bc.elementAt(var3);
               if (Canvas.px >= var4.x && Canvas.px <= var4.x + rW && Canvas.py >= var4.y && Canvas.py <= var4.y + hH) {
                  var2.V = (byte)var3;
                  if (!var2.canTa) {
                     if (!var2.isFinish[BoardScr.getIndexByID(GameMidlet.avatar.IDDB)]) {
                        if (var2.countEnter < 6) {
                           var2.setMoney();
                        }

                        ++var2.countEnter;
                     }
                  } else if (var2.idT == -1) {
                     if (var2.idffr == -1) {
                        var2.idffr = var2.V;
                        var2.center.caption = "Tả";
                        var2.setBotCmdReChoose();
                     } else {
                        var2.idT = var2.V;
                        var2.ta();
                     }
                  }
                  break;
               }
            }
         }

         if (Canvas.isKeyPressed(6)) {
            ++this.V;
            if (this.V > 5) {
               this.V = 0;
            }

            return;
         }

         if (Canvas.isKeyPressed(4)) {
            --this.V;
            if (this.V < 0) {
               this.V = 5;
            }

            return;
         }

         if (Canvas.isKeyPressed(8)) {
            if (this.V / 3 <= 0) {
               this.V = (byte)(this.V + 3);
               return;
            }
         } else if (Canvas.isKeyPressed(2) && this.V > 2) {
            this.V = (byte)(this.V - 3);
         }
      }

   }

   protected final void doReady() {
      super.doReady();
      if (!BoardScr.isStartGame && !BoardScr.disableReady) {
         this.resetGame();
      }

   }

   public final void update() {
      super.update();
      if (!BoardScr.isStartGame && !BoardScr.disableReady) {
         this.updateReady();
      } else {
         BoardScr.dieTime = (long)((int)(System.currentTimeMillis() / 1000L - BoardScr.currentTime));
         if (BoardScr.isStartGame && !BoardScr.isGameEnd && !BoardScr.disableReady && (long)BoardScr.interval - BoardScr.dieTime < 0L) {
            this.canpointer = true;
            if (GameMidlet.avatar.IDDB != BoardScr.ownerID) {
               if (this.autoLuot == 0) {
                  this.autoLuot = 1;
                  this.putMoneyFN();
               }

               if (this.autoLuot == 2) {
                  this.autoLuot = 3;
                  this.onSkip();
               }
            }
         }

         BCBoardScr var1 = this;
         int var11;
         if (this.vtmoneySV.size() > 0 && this.bc.size() > 0) {
            MoneySV var4;
            for(int var2 = 0; var2 < var1.vtmoneySV.size(); ++var2) {
               MoneySV var3;
               if ((var4 = var3 = (MoneySV)var1.vtmoneySV.elementAt(var2)).x != var4.xTo) {
                  if (var4.xTo - var4.x >> 1 == 0) {
                     var4.x = var4.xTo;
                  } else {
                     var4.x += var4.xTo - var4.x >> 1;
                  }
               }

               if (var4.y != var4.yto) {
                  if (var4.yto - var4.y >> 1 == 0) {
                     var4.y = var4.yto;
                  } else {
                     var4.y += var4.yto - var4.y >> 1;
                  }
               }

               if (var4.isMoveOK && var4.x == var4.xTo && var4.y == var4.yto) {
                  var4.move = true;
               }

               if (var3.move) {
                  var1.vtmoneySV.removeElement(var3);
                  if (var1.taOK) {
                     PimgBC var10 = (PimgBC)var1.bc.elementAt(var1.addt);
                     int var5 = getSeatATmapSeat(var1.mapSeat, var1.seat);
                     var1.creatSVMoneyPut(var10.x, var10.y, var10.x, var10.y, var1.moneySV[var1.seat][var1.addt], getIndex(var5), var1.addt, var1.addt, false);
                     var1.taOK = false;
                  }
               }
            }

            PimgBC var7 = (PimgBC)var1.bc.elementAt(var1.addt);
            if (var1.taOK) {
               for(var11 = 0; var11 < var1.vtmoneySV.size(); ++var11) {
                  if ((var4 = (MoneySV)var1.vtmoneySV.elementAt(var11)).addFrom == var1.addfr) {
                     var4.xTo = var7.x;
                     var4.yto = var7.y;
                     var4.isMoveOK = true;
                  }
               }
            }
         }

         int var6;
         if (this.xn.size() > 0) {
            for(var6 = 0; var6 < this.xn.size(); ++var6) {
               Xingau var8;
               (var8 = (Xingau)this.xn.elementAt(var6)).update();
               if (this.isStopXn) {
                  var8.typeStop = this.result[var6];
                  var8.stopHere = true;
               }
            }
         }

         for(var6 = 0; var6 < this.listFireWork.size(); ++var6) {
            Point var9;
            if (CRes.abs((var11 = CRes.tan((var9 = (Point)this.listFireWork.elementAt(var6)).xTo - var9.x, -(var9.yTo - var9.y))) - var9.h) > 10) {
               var9.h -= var9.height * var9.catagory;
               var9.h = CRes.fixangle(var9.h);
            } else {
               var9.h = var11;
               var9.dis = (byte)(var9.dis + 2);
            }

            if (var9.color >= 4) {
               var9.color = 0;
            }

            ++var9.color;
            var11 = var9.dis * CRes.cos(var9.h) >> 10;
            int var12 = -(var9.dis * CRes.sin(var9.h)) >> 10;
            if (CRes.distance(var9.x, var9.y, var9.xTo, var9.yTo) >= var9.dis) {
               var9.x += var11;
               var9.y += var12;
            } else {
               this.listFireWork.removeElement(var9);
            }
         }
      }

   }

   public final void onFinish(int[] var1) {
      this.moneyP = var1;
      this.isStopXn = true;
      BoardScr.isGameEnd = true;
      super.right = null;
      this.beginCharTa = false;
      this.count = 0;
      super.center = this.cmdNextBC;
      BCBoardScr var4 = this;

      for(byte var2 = 0; var2 < 5; ++var2) {
         Avatar var3;
         if ((var3 = (Avatar)BoardScr.avatarInfos.elementAt(var2)).IDDB != -1) {
            BoardScr.showChat(var3.IDDB, String.valueOf(var4.moneyP[var2]));
            var3.setMoneyNew(var3.getMoneyNew() + var4.moneyP[var2]);
         }
      }

   }

   public final void onPlaying() {
      BoardScr.isStartGame = false;
      BoardScr.disableReady = true;
      this.mapSeat.removeAllElements();
      this.setPosPlaying();

      for(int var1 = 0; var1 < BoardScr.avatarInfos.size(); ++var1) {
         if (((Avatar)BoardScr.avatarInfos.elementAt(var1)).IDDB != BoardScr.ownerID) {
            this.mapSeat.addElement(String.valueOf(var1));
         }
      }

      this.paintSVmoney();
      super.center = BoardScr.cmdWaiting;
   }

   public final void doFire() {
      if (!this.canTa) {
         if (!this.isFinish[BoardScr.getIndexByID(GameMidlet.avatar.IDDB)]) {
            if (this.countEnter < 6) {
               this.setMoney();
            }

            ++this.countEnter;
            return;
         }
      } else if (this.idT == -1) {
         if (this.idffr == -1) {
            this.idffr = this.V;
            super.center.caption = "Tả";
            this.setBotCmdReChoose();
            return;
         }

         this.idT = this.V;
         this.ta();
      }

   }

   public final void setBotCmd() {
      super.center = BoardScr.cmdFire;
      super.right = this.cmdSkip;
      super.center.caption = "Đặt";
      super.right.caption = "Xong";
   }

   private void setBotCmdReChoose() {
      super.right = this.cmdSkip;
      super.right.caption = "Chọn lại";
   }

   private static int getSeatATmapSeat(Vector var0, int var1) {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         if (((String)var0.elementAt(var2)).equals(String.valueOf(var1))) {
            return var2;
         }
      }

      return -1;
   }

   public final void onStartGame() {
      super.onStartGame();
      this.mapSeat.removeAllElements();

      for(int var1 = 0; var1 < BoardScr.avatarInfos.size(); ++var1) {
         if (((Avatar)BoardScr.avatarInfos.elementAt(var1)).IDDB != BoardScr.ownerID) {
            this.mapSeat.addElement(String.valueOf(var1));
         }
      }

   }

   public final void onStartGame(byte roomID, byte boardID, byte interval) {
      super.start();
      Canvas.endDlg();
      this.resetGame();
      BoardScr.resetReady();
      this.mapSeat.removeAllElements();
      this.setPosPlaying();

      for(int var2 = 0; var2 < BoardScr.avatarInfos.size(); ++var2) {
         if (((Avatar)BoardScr.avatarInfos.elementAt(var2)).IDDB != BoardScr.ownerID) {
            this.mapSeat.addElement(String.valueOf(var2));
         }
      }

      if (GameMidlet.avatar.IDDB != BoardScr.ownerID) {
         this.setBotCmd();
      } else {
         super.center = null;
         super.right = null;
      }

      BoardScr.isGameEnd = false;
      BoardScr.isStartGame = true;
      BoardScr.interval = interval;
      BoardScr.currentTime = (long)Canvas.getSecond();
   }

   public final void onMove(byte var1) {
      this.seat = var1;
      this.isFinish[this.seat] = true;
      this.paintSVmoney();
   }

   public final void onHaphom(byte var1, byte var2, byte var3) {
      if (var2 != var3) {
         this.seat = var1;
         this.addfr = var2;
         this.addt = var3;
         this.taOK = true;
         this.autoLuot = 3;
      }

   }

   public final void onResult(byte[] var1) {
      this.result = var1;
      Vector var4 = new Vector();

      for(int var2 = 0; var2 < 6; ++var2) {
         PimgBC var3 = new PimgBC();
         if (var2 == this.result[0]) {
            var3.moneyPut = 6;
         }

         var4.addElement(var3);
      }

      CasinoService.gI().PutMoneyOk(var4);
      this.loadXingau();
   }

   public final void setPosPlaying() {
      for(int var1 = 0; var1 < BoardScr.numPlayer; ++var1) {
         Avatar var2;
         if ((var2 = (Avatar)BoardScr.avatarInfos.elementAt(var1)).IDDB != -1) {
            var2.ySat = 0;
            var2.setAction((byte)0);
            var2.setFrame(var2.action);
            var2.setPos(posAvatar5[BoardScr.indexPlayer[var1]].x, posAvatar5[BoardScr.indexPlayer[var1]].y);
            if (BoardScr.indexPlayer[var1] != 2 && BoardScr.indexPlayer[var1] != 3 && BoardScr.indexPlayer[var1] != 4) {
               var2.direct = var2.dirLast = 0;
            } else {
               var2.direct = var2.dirLast = Base.LEFT;
            }
         }
      }

   }
}
