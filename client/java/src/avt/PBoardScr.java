package avt;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class PBoardScr extends BoardScr {
   public static PBoardScr instance;
   private int[] distant = new int[4];
   private Card[][] cardShow = new Card[4][4];
   private AvPosition g;
   private AvPosition h;
   public static AvPosition[] b;
   public static AvPosition[] c;
   public static AvPosition[] d;
   private AvPosition K;
   private int[] L = new int[10];
   private boolean M;
   private Card card;
   private Card[] myCard = new Card[10];
   private Card[] cardEat = new Card[3];
   private int[][] Q = new int[4][12];
   private int[][] R = new int[4][3];
   private int[] numCardEat = new int[4];
   private int[] numCardPhom = new int[4];
   private int[][] cardRac = new int[4][11];
   private byte[] numCardRac = new byte[4];
   private byte[] numberCard = new byte[4];
   private int numPhom;
   private int phomRandom;
   private int phomHa;
   private int firstPlayer;
   private int cardCurrent;
   private int firstHa;
   private Card hCard;
   private int assetChange;
   private boolean finish;
   private int winer;
   private boolean isU;
   private boolean isHaPhom;
   private int[] scorePlayer = new int[4];
   private int key;
   private boolean pause;
   private boolean aM;
   private boolean aN;
   private AvPosition getC;
   private Card cardE;
   private Card aQ;
   private int colorPhom_1 = 473848;
   private int colorPhom_2 = 517368;
   private int colorPhom_3 = 522270;
   private static int disCard_ = 12;
   private static int disShow = 12;
   private Command cmdEat;
   private Command cmdGet;
   private Command cmdHaPhom;
   private int xShow;
   private int remem = 0;
   private boolean trans = false;
   private int pos = -2;
   private int count;
   private Card ca = null;
   private int[] cardToEat = new int[5];
   private int bg = 0;

   public static PBoardScr gI() {
      if (instance == null) {
         instance = new PBoardScr();
      }

      return instance;
   }

   public final void resetCard() {
      this.reset();
      super.resetCard();
   }

   private void reset() {
      int var1;
      for(var1 = 0; var1 < 10; ++var1) {
         this.myCard[var1] = new Card((byte)-1, true);
         this.L[var1] = -1;
         if (var1 < 3) {
            this.cardEat[var1] = new Card((byte)-1, true);
         }
      }

      this.cardShow = new Card[4][4];
      super.selectedCard = 0;
      this.cardCurrent = -1;
      this.h = new AvPosition();
      this.h.x = this.g.x;
      this.h.y = this.g.y;
      this.M = false;

      for(var1 = 0; var1 < 4; ++var1) {
         int var2;
         for(var2 = 0; var2 < 4; ++var2) {
            if (var2 < 3) {
               this.R[var1][var2] = -1;
            }
         }

         for(var2 = 0; var2 < 12; ++var2) {
            this.Q[var1][var2] = -1;
            if (var2 < 11) {
               this.cardRac[var1][var2] = -1;
            }
         }

         this.scorePlayer[var1] = -1;
         this.distant[var1] = 0;
         this.numCardEat[var1] = 0;
         this.numCardPhom[var1] = 0;
         this.numCardRac[var1] = 0;
         this.numberCard[var1] = 0;
      }

      this.numPhom = 0;
      this.phomRandom = 3;
      this.phomHa = 0;
      this.hCard = new Card((byte)-1, true);
      this.assetChange = -1;
      this.key = 1;
      this.finish = false;
      this.winer = -1;
      this.isU = false;
      this.isHaPhom = false;
      this.pos = -2;
      this.pause = false;
      this.firstHa = -1;
      this.aM = false;
      this.aN = false;
      this.getC = new AvPosition(Canvas.hw, Canvas.hh, 3);
      this.cardE = new Card((byte)-1, true);
      this.aQ = new Card((byte)-1, true);
   }

   public final void init() {
      super.init();
      this.initP();
      if (BoardScr.isStartGame) {
         this.setPosCard(false);
      }

      this.getC = new AvPosition(Canvas.hw, Canvas.hh, 3);
      if (BoardScr.imgBan == null) {
         try {
            BoardScr.imgBan = Image.createImage(T.getPath() + "/on/star.on");
            return;
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

   }

   private void initP() {
      this.g = new AvPosition();
      this.K = new AvPosition();
      this.g.x = Canvas.hw - 27;
      this.g.y = Canvas.h - Canvas.hTab;
      if (Canvas.w < 200) {
         AvPosition var10000 = this.g;
         var10000.y += 10;
      }

      this.K.x = this.g.x - 24;
      this.K.y = this.g.y - BoardScr.hcard / 2 - 4;
      Canvas.paint.initPosPhom();
   }

   public final void commandTab(int var1, int var2) {
      PBoardScr var3;
      int var5;
      int var6;
      int[] var9;
      label116:
      switch (var1) {
         case 30:
            var3 = this;
            this.resetOrderCard();
            this.resetChangeCard();

            int var8;
            for(var8 = 0; var8 < var3.cardToEat.length; ++var8) {
               var3.cardToEat[var8] = -1;
            }

            var8 = 0;

            for(var5 = 0; var5 < 10; ++var5) {
               if (var3.L[var5] == 0) {
                  if (var8 == 5) {
                     Canvas.startOKDlg(T.youSelect);
                     break label116;
                  }

                  var3.cardToEat[var8] = var3.myCard[var5].cardID;
                  ++var8;
               }
            }

            if (var8 < 2) {
               Canvas.startOKDlg(T.upTwoCard);
            } else {
               var9 = new int[6];

               for(var6 = 0; var6 < 5; ++var6) {
                  if (var3.cardToEat[var6] != -1) {
                     var9[var6] = var3.cardToEat[var6];
                  } else {
                     var9[var6] = -1;
                  }
               }

               var9[5] = var3.cardCurrent;
               byte var10 = -1;
               if (var3.checkPhomSanh(var9)) {
                  var10 = 1;
               }

               if (var3.checkPhomBaLa(var9)) {
                  var10 = 0;
               }

               if (var10 == -1) {
                  Canvas.startOKDlg(T.notPhom);
               } else {
                  for(var8 = 0; var8 < var9.length; ++var8) {
                     if (var9[var8] != -1) {
                        for(int var7 = 0; var7 < 10; ++var7) {
                           if (var9[var8] == var3.myCard[var7].cardID) {
                              if (!var3.checkCardToEat(var7)) {
                                 Canvas.startOKDlg(T.notPhom);
                                 break label116;
                              }
                              break;
                           }
                        }
                     }
                  }

                  var3.resetCmd();
                  BoardScr.setCmdWaiting();
                  CasinoService.gI().eatCardPhom(var3.cardToEat, var10);
               }
            }
            break;
         case 31:
            this.doGet();
            break;
         case 32:
            var3 = this;
            this.resetOrderCard();
            if (GameMidlet.avatar.IDDB != super.currentPlayer) {
               Canvas.startOKDlg(T.waitToCurrent);
            } else {
               this.resetChangeCard();
               var9 = new int[12];
               var5 = -1;

               for(var6 = 0; var6 < 10; ++var6) {
                  if (var3.myCard[var6].phom != 0 && (var5 == -1 || var5 == var3.myCard[var6].phom)) {
                     var5 = var3.myCard[var6].phom;
                     var9[var6] = var3.myCard[var6].cardID;
                  } else {
                     var9[var6] = -1;
                  }
               }

               var3.resetCmd();
               BoardScr.setCmdWaiting();
               CasinoService.gI().HaPhomPhom(var3.myCard);
            }
      }

      super.commandTab(var1, var2);
   }

   public PBoardScr() {
      this.reset();
      this.cmdEat = new Command(T.eat, 30);
      this.cmdGet = new Command(T.gett, 31);
      this.cmdHaPhom = new Command(T.haPhom, 32);
      Canvas.paint.initPosPhom();
   }

   private void setCmdEatAndGet() {
      super.center = this.cmdEat;
      super.right = this.cmdGet;
   }

   private void setCmdFire() {
      super.center = BoardScr.cmdFire;
      super.right = null;
   }

   private void resetCmd() {
      super.center = null;
      super.right = null;
   }

   private void setcmdHaPhom() {
      super.center = this.cmdHaPhom;
      super.right = null;
   }

   private void setContinue() {
      super.center = BoardScr.cmdBack;
      super.right = null;
   }

   public final void doContinue() {
      this.finish = false;
      this.resetCard();
      super.doContinue();
   }

   private void resetChangeCard() {
      if (this.assetChange != -1 && this.hCard.cardID != -1) {
         if (Canvas.isKeyBoard) {
            this.cleanCard(super.selectedCard);
            this.cleanUp(this.assetChange);
            this.myCard[this.assetChange] = this.hCard;
            this.hCard = new Card((byte)-1, true);
         } else {
            this.cleanCard(super.selectedCard);
            this.cleanUp(this.assetChange);
            this.myCard[this.assetChange] = this.hCard;
            this.hCard = new Card((byte)-1, true);
         }
      }

   }

   private void resetCell(int var1) {
      this.myCard[var1] = new Card((byte)-1, true);
   }

   private void resetOrderCard() {
      if (this.trans && this.remem == 2) {
         this.remem = 0;
         this.trans = false;
         if (this.hCard.cardID != -1) {
            this.checkCardChange();
         }

         this.L[super.selectedCard] = -1;
         Canvas.isPointerDown = false;
      }

   }

   private void D() {
      for(int var1 = 0; var1 < 3; ++var1) {
         if (this.cardEat[var1].cardID == this.myCard[super.selectedCard].cardID && this.cardEat[var1].cardID != -1) {
            return;
         }
      }

      if (this.key < 2) {
         ++this.key;
      }

      if (this.key > 0 && this.hCard.cardID != -1) {
         this.checkCardChange();
      }

      if (this.key == 2) {
         this.L[super.selectedCard] = 0;
      }

   }

   public final void updateKey() {
      super.updateKey();
      if (!this.pause && !BoardScr.disableReady && BoardScr.isStartGame && super.center != BoardScr.cmdWaiting && !this.M) {
         int var1;
         if ((var1 = this.setAssetCard()) == -1) {
            var1 = 10;
         }

         if (Canvas.isPointerClick && this.hCard.cardID != -1) {
            Canvas.isPointerClick = false;
            this.trans = true;
         }

         int var2;
         if (Canvas.isPointerClick && Canvas.isPointer(this.xShow - BoardScr.wCard / 2, this.g.y - BoardScr.hcard / 2 - 20, var1 * disCard_, BoardScr.hcard)) {
            Canvas.isPointerClick = false;
            if (this.hCard.cardID != -1) {
               this.D();
               this.trans = false;
               return;
            }

            if ((var1 = this.setAssetCard()) == -1) {
               var1 = 9;
            } else {
               --var1;
            }

            var2 = (Canvas.px - (this.xShow - BoardScr.wCard / 2)) / disCard_;
            this.trans = true;
            if (var2 <= var1) {
               super.selectedCard = var2;
            }
         }

         if (this.trans) {
            var1 = Canvas.dx();
            var2 = Canvas.dy();
            if (Canvas.isPointerDown) {
               int var3 = (Canvas.px - (this.xShow - BoardScr.wCard / 2)) / disCard_;
               if (this.remem != 2 && var2 > 10) {
                  Canvas.keyPressed[2] = true;
                  if (this.L[super.selectedCard] != -1) {
                     this.remem = 1;
                  }
               } else if (this.remem != 2 && var2 < -10) {
                  if (this.remem == 1) {
                     this.trans = false;
                     this.remem = 0;
                     Canvas.keyPressed[8] = true;
                  }
               } else if (CRes.abs(var1) > 10) {
                  int var4;
                  if (this.remem != 2) {
                     for(var4 = 0; var4 < 3; ++var4) {
                        if (this.cardEat[var4].cardID == this.myCard[super.selectedCard].cardID) {
                           return;
                        }
                     }

                     this.hCard = this.myCard[super.selectedCard];
                     this.resetCell(super.selectedCard);
                     this.assetChange = super.selectedCard;
                  }

                  this.remem = 2;
                  if (super.selectedCard - var3 > 0) {
                     if (this.hCard.cardID != -1 && var3 >= 0) {
                        this.myCard[super.selectedCard] = this.myCard[var3];
                        this.resetCell(var3);
                        super.selectedCard = var3;
                     }
                  } else {
                     if ((var4 = this.setAssetCard()) == -1) {
                        var4 = 9;
                     } else {
                        --var4;
                     }

                     if (this.hCard.cardID != -1 && var3 <= var4) {
                        this.myCard[super.selectedCard] = this.myCard[var3];
                        this.resetCell(var3);
                        super.selectedCard = var3;
                     }
                  }
               }

               this.setPosCard(true);
            }

            if (Canvas.isPointerRelease) {
               if (this.hCard.cardID != -1) {
                  this.resetOrderCard();
               } else if (CRes.abs(var1) <= 10 && CRes.abs(var2) <= 10) {
                  if (this.key == 1) {
                     this.key = 2;
                     this.L[super.selectedCard] = -1;
                  } else if (this.key == 2) {
                     this.key = 1;
                     this.L[super.selectedCard] = 0;
                  }
               }

               this.setPosCard(false);
            }
         }

         if (Canvas.a(2)) {
            this.D();
         } else {
            if (Canvas.a(8)) {
               if (this.key > 0) {
                  --this.key;
               }

               if (this.key < 2) {
                  this.L[super.selectedCard] = -1;
               }

               if (this.key == 0 && this.hCard.cardID == -1) {
                  for(var1 = 0; var1 < 3; ++var1) {
                     if (this.cardEat[var1].cardID == this.myCard[super.selectedCard].cardID) {
                        return;
                     }
                  }

                  this.hCard = this.myCard[super.selectedCard];
                  this.resetCell(super.selectedCard);
                  this.assetChange = super.selectedCard;
               }

               this.setPosCard(false);
               return;
            }

            if (Canvas.a(4)) {
               if ((var1 = this.setAssetCard()) == -1) {
                  var1 = 9;
               } else {
                  --var1;
               }

               if (this.hCard.cardID != -1) {
                  if (super.selectedCard > 0) {
                     this.myCard[super.selectedCard] = this.myCard[super.selectedCard - 1];
                     this.resetCell(super.selectedCard - 1);
                  } else {
                     this.cleanCard(0);
                  }
               }

               if (super.selectedCard > 0) {
                  --super.selectedCard;
               } else {
                  super.selectedCard = var1;
               }

               this.setPosCard(false);
               return;
            }

            if (!Canvas.a(6)) {
               return;
            }

            if ((var1 = this.setAssetCard()) == -1) {
               var1 = 9;
            } else {
               --var1;
            }

            if (this.hCard.cardID != -1) {
               if (super.selectedCard < var1) {
                  this.myCard[super.selectedCard] = this.myCard[super.selectedCard + 1];
                  this.resetCell(super.selectedCard + 1);
               } else {
                  this.cleanUp(0);
               }
            }

            if (super.selectedCard < var1) {
               ++super.selectedCard;
            } else {
               super.selectedCard = 0;
            }
         }

         this.setPosCard(false);
      }

   }

   public final void update() {
      super.update();
      if (!BoardScr.isStartGame && !BoardScr.disableReady) {
         this.updateReady();
      } else {
         if (BoardScr.isStartGame && BoardScr.isStartGame && this.myCard != null) {
            for(int var1 = this.myCard.length - 1; var1 >= 0; --var1) {
               if (this.myCard[var1].translate() == -1) {
                  this.myCard[var1].isShow = false;
               }
            }
         }

         PBoardScr var8 = this;
         int var2;
         int var3;
         AvPosition var10000;
         if (this.M && (var2 = BoardScr.getIndexByID(this.firstPlayer)) != -1) {
            var2 = BoardScr.indexPlayer[var2];
            var3 = c[var2].x;
            var2 = c[var2].y;
            var10000 = this.h;
            var10000.x += (var3 - this.h.x) / 2;
            var10000 = this.h;
            var10000.y += (var2 - this.h.y) / 2;
            if (Math.abs(var3 - this.h.x) <= 1 && Math.abs(var2 - this.h.y) <= 1) {
               var3 = BoardScr.getIndexByID(this.firstPlayer);
               this.cardShow[var3][this.numberCard[var3]] = new Card((byte)this.cardCurrent, true);
               if (this.firstPlayer == GameMidlet.avatar.IDDB) {
                  for(int var4 = 0; var4 < 10; ++var4) {
                     if (var8.myCard[var4].cardID == var8.cardCurrent) {
                        var8.cleanCard(var4);
                        break;
                     }
                  }

                  if (var8.myCard[var8.selectedCard].cardID == -1) {
                     var8.selectedCard = var8.setAssetCard() - 1;
                  }
               }

               var8.M = false;
               var8.resetUpCard();
            }

            var8.setPosCard(false);
         }

         if (this.aM) {
            var2 = this.g.x + this.bg * disCard_;
            var3 = this.g.y;
            var10000 = this.getC;
            var10000.x += (var2 - this.getC.x) / 2;
            var10000 = this.getC;
            var10000.y += (var3 - this.getC.y) / 2;
            if (Math.abs((var2 - this.getC.x) / 2) <= 1 && Math.abs((var3 - this.getC.y) / 2) <= 1) {
               this.myCard[this.bg] = this.cardE;
               if (getAssetCard(this.cardShow[BoardScr.indexOfMe]) == 3) {
                  if (GameMidlet.avatar.IDDB == super.currentPlayer) {
                     this.setcmdHaPhom();
                  }
               } else if (GameMidlet.avatar.IDDB == super.currentPlayer) {
                  this.setCmdFire();
               }

               if (!this.isHaPhom) {
                  this.cleanPhomRandom();
                  this.findPhom();
               } else {
                  byte var10 = this.myCard[this.bg].cardID;
                  PBoardScr var9 = this;
                  int[] var11 = new int[6];

                  int var5;
                  for(var5 = 0; var5 < 5; ++var5) {
                     var11[var5] = -1;
                  }

                  var5 = 0;

                  for(int var6 = 0; var6 < 12; ++var6) {
                     if (var9.Q[BoardScr.indexOfMe][var6] != -1) {
                        var11[var5] = var9.Q[BoardScr.indexOfMe][var6];
                        ++var5;
                     } else {
                        var5 = 0;
                        var11[5] = var10;
                        orderArrayIncrease(var11);
                        if (var9.checkPhomSanh(var11) || var9.checkPhomBaLa(var11)) {
                           var9.resetOrderCard();
                           CasinoService.gI().doAddCardPhom(var11);
                           break;
                        }

                        for(int var7 = 0; var7 < 6; ++var7) {
                           var11[var7] = -1;
                        }
                     }
                  }
               }

               this.bg = 0;
               this.aM = false;
               this.getC = new AvPosition(Canvas.hw, Canvas.hh, 3);
               this.setPosCard(false);
            }
         }

         if (this.aN) {
            var2 = BoardScr.getIndexByID(super.currentPlayer);
            var3 = (BoardScr.posAvatar[BoardScr.indexPlayer[var2]].x - this.aQ.x) / 2;
            var2 = (BoardScr.posAvatar[BoardScr.indexPlayer[var2]].y - this.aQ.y) / 2;
            Card var12 = this.aQ;
            var12.x += var3;
            var12 = this.aQ;
            var12.y += var2;
            if (Math.abs(var3) <= 1 && Math.abs(var2) <= 1) {
               this.aN = false;
            }
         }

         var8 = this;
         BoardScr.dieTime = (long)((int)((long)Canvas.getSecond() - BoardScr.currentTime));
         if ((long)BoardScr.interval - BoardScr.dieTime >= 0L) {
            return;
         }

         if (super.center == this.cmdEat && super.right == this.cmdGet) {
            this.doGet();
            this.resetCmd();
         } else {
            if (super.center != BoardScr.cmdFire) {
               if (super.center == this.cmdHaPhom) {
                  this.resetChangeCard();
                  this.cmdHaPhom.perform();
               }

               return;
            }

            var2 = 0;

            for(var3 = 1; var3 < 10; ++var3) {
               if (var8.myCard[var3].phom == 0 && var8.myCard[var3].cardID > var8.myCard[var2].cardID) {
                  var2 = var3;
               }
            }

            var8.resetChangeCard();
            CasinoService.gI().moveCo(var8.myCard[var2].cardID);
         }
      }

   }

   private void setPosCard(boolean var1) {
      int var2;
      if (!var1) {
         if ((var2 = this.setAssetCard()) == -1) {
            var2 = 10;
         }

         if (Canvas.isKeyBoard && BoardScr.isStartGame && var2 != 0 && (disCard_ = (Canvas.w - BoardScr.wCard / 2) / var2) > BoardScr.wCard / 3 << 1) {
            disCard_ = BoardScr.wCard / 3 << 1;
         }

         if ((disShow = disCard_) > BoardScr.wCard / 4) {
            disShow = BoardScr.wCard / 4;
         }

         if (Canvas.w < 160) {
            disShow = 10;
         }

         this.xShow = (Canvas.w - (disCard_ * var2 + (BoardScr.wCard - disCard_)) >> 1) + BoardScr.wCard / 2;
         if (this.xShow < BoardScr.wCard / 2) {
            this.xShow = BoardScr.wCard / 2;
         }
      }

      for(var2 = 0; var2 < 10; ++var2) {
         int var3 = 0;
         if (this.L[var2] == 0) {
            var3 = 10 * (Canvas.stypeInt + 1);
         }

         this.myCard[var2].xTo = this.xShow + var2 * disCard_;
         this.myCard[var2].yTo = this.g.y - var3;
         if (var1) {
            this.myCard[var2].x = this.myCard[var2].xTo;
            this.myCard[var2].y = this.myCard[var2].yTo;
         }
      }

   }

   private void checkCardChange() {
      byte var1 = -1;

      int var2;
      for(var2 = 0; var2 < 3; ++var2) {
         if (this.hCard.phom == this.cardEat[var2].phom) {
            var1 = this.cardEat[var2].cardID;
         }
      }

      this.myCard[super.selectedCard] = this.hCard;
      if (var1 != -1) {
         if (!this.checkCardToEat(super.selectedCard)) {
            this.hCard = this.myCard[super.selectedCard];
            this.resetCell(super.selectedCard);
            this.resetChangeCard();
            return;
         }

         this.resetCell(super.selectedCard);
      }

      if (this.hCard.phom != 0 && (super.selectedCard > 0 && this.myCard[super.selectedCard - 1].phom == this.hCard.phom || super.selectedCard < 9 && this.myCard[super.selectedCard + 1].phom == this.hCard.phom)) {
         this.myCard[super.selectedCard] = this.hCard;
         this.hCard = new Card((byte)-1, true);
      } else if (var1 != -1 && this.hCard.phom != 0) {
         if (var1 != -1) {
            this.pos = -1;
            if (this.assetChange != super.selectedCard) {
               this.doResetPhomEat(this.hCard, var1);
            }
         }
      } else {
         if (super.selectedCard < 9) {
            for(var2 = 0; var2 < 3; ++var2) {
               if (this.cardEat[var2].phom != 0 && this.myCard[super.selectedCard + 1].phom == this.cardEat[var2].phom) {
                  int[] var5 = new int[10];
                  boolean var3 = false;

                  for(int var4 = 0; var4 < 10; ++var4) {
                     if (this.myCard[var4].phom == this.cardEat[var2].phom) {
                        var5[var4] = this.myCard[var4].cardID;
                     } else {
                        var5[var4] = -1;
                        if (!var3) {
                           var3 = true;
                           var5[var4] = this.hCard.cardID;
                        }
                     }
                  }

                  var5 = orderArrayIncrease(var5);
                  if (!this.checkPhomBaLa(var5) && !this.checkPhomSanh(var5)) {
                     this.resetChangeCard();
                     return;
                  }

                  this.pos = this.hCard.phom;
                  this.hCard.phom = this.cardEat[var2].phom;
                  this.myCard[super.selectedCard] = this.hCard;
                  if (this.assetChange != super.selectedCard) {
                     this.doResetPhomEat(this.cardEat[var2], this.cardEat[var2].cardID);
                  }

                  return;
               }
            }
         }

         PBoardScr var6 = this;
         var2 = 0;

         int var10000;
         while(true) {
            if (var2 >= 10) {
               var10000 = -1;
               break;
            }

            if (var6.myCard[var2].cardID == -1) {
               var10000 = var2;
               break;
            }

            if (var6.searchCardEat(var6.myCard[var2].phom)) {
               var10000 = var2;
               break;
            }

            ++var2;
         }

         if (super.selectedCard >= var10000 && var10000 != -1) {
            this.resetChangeCard();
         } else {
            this.myCard[super.selectedCard] = this.hCard;
            this.hCard = new Card((byte)-1, true);
            if (this.assetChange != super.selectedCard) {
               this.myCard[super.selectedCard].phom = 0;
               this.cleanPhomRandom();
               this.findPhom();
            }
         }
      }

   }

   private void doResetPhomEat(Card var1, int var2) {
      this.resetOrderCard();
      this.pause = true;
      int[] var3 = new int[5];

      for(int var4 = 0; var4 < 5; ++var4) {
         var3[var4] = -1;
      }

      int[] var9 = new int[6];

      int var5;
      for(var5 = 0; var5 < 6; ++var5) {
         var9[var5] = -1;
      }

      var5 = 0;

      int var6;
      for(var6 = 0; var6 < 10; ++var6) {
         if (this.myCard[var6].phom == var1.phom) {
            var9[var5] = this.myCard[var6].cardID;
            ++var5;
         }
      }

      if (var9[5] != -1) {
         orderArrayIncrease(var9);
         var6 = 0;

         for(var5 = 0; var5 < var9.length; ++var5) {
            if (var9[var5] == var2) {
               var6 = var5;
            }
         }

         var5 = 0;
         Card var7;
         int var8;
         if (var6 < 3) {
            for(var8 = 0; var8 < var9.length; ++var8) {
               if (var8 > 2) {
                  for(var6 = 0; var6 < 10; ++var6) {
                     if (var9[var8] == this.myCard[var6].cardID) {
                        this.myCard[var6].phom = 0;
                     }
                  }
               } else {
                  var3[var5] = var9[var8];
                  ++var5;

                  for(var6 = 0; var6 < 10; ++var6) {
                     if (var9[var8] == this.myCard[var6].cardID) {
                        var7 = this.myCard[var6];
                        this.cleanCard(var6);
                        this.myCard[this.setAssetCard()] = var7;
                     }
                  }
               }
            }
         } else {
            for(var8 = 0; var8 < var9.length; ++var8) {
               if (var8 < 3) {
                  for(var6 = 0; var6 < 10; ++var6) {
                     if (var9[var8] == this.myCard[var6].cardID) {
                        this.myCard[var6].phom = 0;
                     }
                  }
               } else {
                  var3[var5] = var9[var8];
                  ++var5;

                  for(var6 = 0; var6 < 10; ++var6) {
                     if (var9[var8] == this.myCard[var6].cardID) {
                        var7 = this.myCard[var6];
                        this.cleanCard(var6);
                        this.myCard[this.setAssetCard()] = var7;
                     }
                  }
               }
            }
         }
      } else {
         var6 = 0;

         for(var5 = 0; var5 < 10; ++var5) {
            if (this.myCard[var5].phom == var1.phom) {
               var3[var6] = this.myCard[var5].cardID;
               ++var6;
            }
         }
      }

      orderArrayIncrease(var3);
      CasinoService.gI().doResetPhomEatPhom(var3, var2);
   }

   public final void onResetPhomEat(byte var1) {
      this.resetOrderCard();
      this.pause = false;
      if (var1 == 0) {
         if (this.pos == -1) {
            this.myCard[super.selectedCard] = this.hCard;
            this.myCard[super.selectedCard].phom = 0;
            this.hCard = new Card((byte)-1, true);
            if (this.assetChange != super.selectedCard) {
               this.cleanPhomRandom();
               this.findPhom();
            }

            this.assetChange = -1;
         } else if (this.pos >= 0) {
            this.hCard = new Card((byte)-1, true);
            if (this.assetChange != super.selectedCard) {
               this.myCard[super.selectedCard].phom = 0;
               this.cleanPhomRandom();
               this.findPhom();
            }
         }
      } else if (this.pos == -1) {
         this.resetCell(super.selectedCard);
         this.resetChangeCard();
      } else if (this.pos >= 0) {
         this.resetCell(super.selectedCard);
         this.resetChangeCard();
         this.myCard[this.assetChange].phom = (byte)this.pos;
      }

      this.pos = -2;
      this.setPosCard(false);
   }

   private void cleanUp(int var1) {
      for(int var2 = 9; var2 > var1; --var2) {
         this.myCard[var2] = this.myCard[var2 - 1];
      }

      this.resetCell(var1);
   }

   private void cleanCard(int var1) {
      for(int var2 = var1; var2 < 9; ++var2) {
         this.myCard[var2] = this.myCard[var2 + 1];
      }

      this.resetCell(9);
      this.L[var1] = -1;
   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      super.paint(var1);
   }

   public final void paintNamePlayers(Graphics var1) {
      int var2 = AvMain.hd;
      if (BoardScr.isStartGame || BoardScr.disableReady) {
         var2 = 1;
      }

      for(int var3 = 0; var3 < BoardScr.numPlayer; ++var3) {
         Avatar var4;
         if ((var4 = (Avatar)BoardScr.avatarInfos.elementAt(var3)).IDDB != -1) {
            if (var4.IDDB != GameMidlet.avatar.IDDB || !BoardScr.isStartGame) {
               var4.paintIcon(var1, var4.x * var2, var4.y * var2, false);
            }

            var4.paintName(var1, var4.x * var2, var4.y * var2);
            BoardScr.paintReady(var1, var4.x * var2, (var4.y - 50) * var2 - 10 * (var2 - 1), 3, var4);
         }
      }

   }

   public final void paintMain(Graphics var1) {
      super.paintMain(var1);
      this.paintNamePlayers(var1);
      long var5;
      if (BoardScr.isStartGame && !BoardScr.isGameEnd && (var5 = (long)BoardScr.interval - BoardScr.dieTime) > 0L && b != null && b[0] != null) {
         Canvas.O.drawString(var1, String.valueOf(var5), Canvas.hw, b[0].y + AvMain.hSmall + 10, 2);
      }

      if (BoardScr.isStartGame) {
         this.paintMoneys(var1);
         this.paintMyCard(var1);
         if (this.aM) {
            this.cardE.x = this.getC.x;
            this.cardE.y = this.getC.y;
            if (Canvas.w > 176) {
               this.cardE.paintFull(var1);
            } else {
               this.cardE.paintSmall(var1, false);
            }
         }

         if (this.aN) {
            if (Canvas.w > 176) {
               this.aQ.paintFull(var1);
            } else {
               this.aQ.paintSmall(var1, false);
            }
         }

         if (this.M) {
            Card var7;
            (var7 = new Card((byte)this.cardCurrent, true)).x = this.h.x;
            var7.y = this.h.y;
            if (Canvas.w > 176) {
               var7.paintFull(var1);
            } else {
               var7.paintSmall(var1, false);
            }
         }

         if (Canvas.stypeInt == 0 && super.selectedCard != -1 && !BoardScr.disableReady && !this.finish) {
            AvPosition var10000;
            if (this.count == 4) {
               var10000 = this.K;
               var10000.y += 2;
            }

            if (this.count == 8) {
               var10000 = this.K;
               var10000.y -= 2;
               this.count = 0;
            }

            ++this.count;
            int var8 = 0;
            if (this.L[super.selectedCard] == 0) {
               var8 = 10 * (Canvas.stypeInt + 1);
            }

            if (this.hCard.cardID != -1) {
               var8 = -10;
            }

            if (this.myCard[super.selectedCard] != null) {
               MiniMap.gI().imgArrow.drawFrame(0, this.xShow - BoardScr.wCard / 2 + super.selectedCard * disCard_ + MiniMap.gI().imgArrow.frameWidth / 2, this.g.y - BoardScr.hcard / 2 - 4 - var8, 0, 33, var1);
            }
         }

         if (!this.finish) {
            int var2 = BoardScr.getIndexByID(this.firstHa);
            var1.drawImage(BoardScr.imgBan, b[BoardScr.indexPlayer[var2]].x, b[BoardScr.indexPlayer[var2]].y - 8, 3);
         }
      }

      BoardScr.paintChat(var1);
   }

   private void paintMoneys(Graphics var1) {
      for(int var2 = 0; var2 < 4; ++var2) {
         Avatar var3;
         if ((var3 = (Avatar)BoardScr.avatarInfos.elementAt(var2)).IDDB != -1 && (Canvas.w >= 160 || var3.IDDB == super.currentPlayer)) {
            int var4 = BoardScr.getIndexByID(super.currentPlayer);
            if (var2 != var4 || Canvas.gameTick % 20 > 5 && var2 == var4 || this.finish) {
               Canvas.smallFontYellow.drawString(var1, var3.getMoneyNew() + " " + T.getMoney(), b[BoardScr.indexPlayer[var2]].x, b[BoardScr.indexPlayer[var2]].y, b[BoardScr.indexPlayer[var2]].anchor);
            }
         }
      }

   }

   private void paintMyCard(Graphics var1) {
      this.card = new Card((byte)-1, true);
      int var2;
      if ((var2 = disShow) <= 12 && Canvas.w > 200) {
         var2 = 20;
      }

      byte var3 = 2;
      if (Canvas.stypeInt == 0 && Canvas.w > 200) {
         var3 = 1;
      }

      Graphics var5 = var1;
      PBoardScr var4 = this;
      int var6;
      if ((var6 = disShow) <= 12 && Canvas.w > 200) {
         var6 = 20;
      }

      int var10;
      Avatar var11;
      int var12;
      if (!this.finish) {
         for(var10 = 0; var10 < 4; ++var10) {
            if ((var11 = (Avatar)BoardScr.avatarInfos.elementAt(var10)) != null && var11.IDDB != -1) {
               var12 = 3;
               int var15;
               if (var4.cardShow[var10] != null) {
                  for(var15 = 0; var15 < 4; ++var15) {
                     if (var4.cardShow[var10][var15] == null) {
                        var12 = var15;
                        break;
                     }
                  }
               }

               if (BoardScr.indexPlayer[var10] != 0 && BoardScr.indexPlayer[var10] != 2) {
                  for(var15 = 0; var15 < 4 && var4.cardShow[var10][var15] != null; ++var15) {
                     var4.cardShow[var10][var15].x = c[BoardScr.indexPlayer[var10]].x;
                     var4.cardShow[var10][var15].y = c[BoardScr.indexPlayer[var10]].y + (var15 * var6 << 1) - (var12 * var6 << 1) / 2;
                     if (Canvas.w > 176) {
                        var4.cardShow[var10][var15].paintFull(var5);
                     } else {
                        var4.cardShow[var10][var15].paintSmall(var5, true);
                     }
                  }
               } else {
                  for(var15 = 0; var15 < 4 && var4.cardShow[var10] != null && var4.cardShow[var10][var15] != null; ++var15) {
                     var4.cardShow[var10][var15].x = c[BoardScr.indexPlayer[var10]].x + var15 * disShow - (var12 * disShow + (BoardScr.wCard - disShow)) / 2 + BoardScr.wCard / 2;
                     var4.cardShow[var10][var15].y = c[BoardScr.indexPlayer[var10]].y;
                     if (BoardScr.indexPlayer[var10] == 2) {
                        if (Canvas.w > 176) {
                           if (var15 < 3 && var4.cardShow[var10][var15 + 1] != null) {
                              if (disCard_ > 13) {
                                 var4.cardShow[var10][var15].paintFull(var5);
                              } else {
                                 var4.cardShow[var10][var15].paintFull(var5);
                              }
                           } else {
                              var4.cardShow[var10][var15].paintFull(var5);
                           }
                        } else {
                           var4.cardShow[var10][var15].paintSmall(var5, false);
                        }
                     } else if (Canvas.w > 176) {
                        var4.cardShow[var10][var15].x = c[BoardScr.indexPlayer[var10]].x - var15 * disShow + (var4.distant[var10] * disShow + (BoardScr.wCard - disShow)) / 2 - BoardScr.wCard / 2;
                        var4.cardShow[var10][var15].paintFull(var5);
                     } else {
                        var4.cardShow[var10][var15].paintSmall(var5, false);
                     }
                  }
               }
            }
         }
      }

      for(var10 = 0; var10 < 4; ++var10) {
         if ((var11 = (Avatar)BoardScr.avatarInfos.elementAt(var10)) != null && var11.IDDB != -1) {
            if (BoardScr.indexPlayer[var10] == 1 || BoardScr.indexPlayer[var10] == 3) {
               for(var12 = 0; var12 < 11 && this.cardRac[var10][var12] != -1; ++var12) {
                  this.card = new Card((byte)this.cardRac[var10][var12], true);
                  this.card.x = c[BoardScr.indexPlayer[var10]].x;
                  this.card.y = c[BoardScr.indexPlayer[var10]].y + var12 * (var2 << 1) - this.numCardRac[var10] * (var2 << 1) / 2;
                  if (Canvas.w > 176) {
                     this.card.paintFull(var1);
                  } else {
                     this.card.paintSmall(var1, true);
                  }
               }
            }

            if (BoardScr.indexPlayer[var10] == 0) {
               for(var12 = 0; var12 < 11 && this.cardRac[var10][var12] != -1; ++var12) {
                  this.card = new Card((byte)this.cardRac[var10][var12], true);
                  this.card.x = c[BoardScr.indexPlayer[var10]].x - var12 * disShow + (this.numCardRac[var10] * disShow + (BoardScr.wCard - disShow)) / 2 - BoardScr.wCard / 2;
                  this.card.y = c[BoardScr.indexPlayer[var10]].y;
                  if (Canvas.w > 176) {
                     this.card.paintFull(var1);
                  } else {
                     this.card.x = c[BoardScr.indexPlayer[var10]].x + var12 * disShow - (this.numCardRac[var10] * disShow + (BoardScr.wCard - disShow)) / 2 + BoardScr.wCard / 2;
                     this.card.paintSmall(var1, false);
                  }
               }
            }
         }
      }

      Card var13;
      for(var10 = 0; var10 < 4; ++var10) {
         if ((var11 = (Avatar)BoardScr.avatarInfos.elementAt(var10)) != null && var11.IDDB != -1) {
            if (BoardScr.indexPlayer[var10] == 1) {
               for(var12 = 0; var12 < 3 && this.R[var10][var12] != -1; ++var12) {
                  (var13 = new Card((byte)this.R[var10][var12], true)).x = d[BoardScr.indexPlayer[var10]].x;
                  var13.y = d[BoardScr.indexPlayer[var10]].y + var12 * (var2 << 1) - this.numCardEat[var10] * (var2 << 1) / 2;
                  var13.phom = 1;
                  if (Canvas.w > 176) {
                     var13.paintFull(var1);
                  } else {
                     var13.paintSmall(var1, true);
                  }

                  this.PaintLineColor(1, var13.x, var13.y, var1);
               }
            } else if (BoardScr.indexPlayer[var10] == 0) {
               for(var12 = 0; var12 < 3 && this.R[var10][var12] != -1; ++var12) {
                  (var13 = new Card((byte)this.R[var10][var12], true)).y = d[BoardScr.indexPlayer[var10]].y;
                  var13.x = d[BoardScr.indexPlayer[var10]].x + var12 * disShow - (this.numCardEat[var10] * disShow + (BoardScr.wCard - disShow)) / 2 + BoardScr.wCard / 2;
                  var13.phom = 1;
                  if (Canvas.w > 176) {
                     var13.paintFull(var1);
                  } else {
                     var13.paintSmall(var1, false);
                  }

                  this.PaintLineColor(1, var13.x, var13.y, var1);
               }
            }
         }
      }

      for(var10 = 0; var10 < 4; ++var10) {
         if (BoardScr.indexPlayer[var10] == 1) {
            for(var12 = 0; var12 < 12 && this.Q[var10][0] != -1; ++var12) {
               if (this.Q[var10][var12] != -1) {
                  this.card = new Card((byte)this.Q[var10][var12], true);
                  this.card.x = d[BoardScr.indexPlayer[var10]].x;
                  if (Canvas.w > 176) {
                     this.card.y = d[BoardScr.indexPlayer[var10]].y + var12 * var2 * var3 - this.numCardPhom[var10] * var2 * var3 / 2;
                     this.card.paintFull(var1);
                  } else {
                     this.card.y = d[BoardScr.indexPlayer[var10]].y + var12 * var2 - this.numCardPhom[var10] * var2 / 2;
                     this.card.paintSmall(var1, true);
                  }
               }
            }
         } else if (BoardScr.indexPlayer[var10] == 0) {
            if (this.Q[var10][0] == -1) {
               break;
            }

            for(var12 = 0; var12 < 12; ++var12) {
               if (this.Q[var10][var12] != -1) {
                  this.card = new Card((byte)this.Q[var10][var12], true);
                  this.card.x = d[BoardScr.indexPlayer[var10]].x - var12 * disShow + (this.numCardPhom[var10] * disShow + (BoardScr.wCard - disShow)) / 2 - BoardScr.wCard / 2;
                  this.card.y = d[BoardScr.indexPlayer[var10]].y;
                  if (Canvas.w > 176) {
                     this.card.paintFull(var1);
                  }
               }
            }
         }
      }

      for(var10 = 0; var10 < 4; ++var10) {
         if ((var11 = (Avatar)BoardScr.avatarInfos.elementAt(var10)) != null && var11.IDDB != -1 && BoardScr.indexPlayer[var10] == 3) {
            for(var12 = 0; var12 < 3 && this.R[var10][var12] != -1; ++var12) {
               (var13 = new Card((byte)this.R[var10][var12], true)).phom = 1;
               var13.x = d[BoardScr.indexPlayer[var10]].x;
               var13.y = d[BoardScr.indexPlayer[var10]].y + var12 * (var2 << 1) - this.numCardEat[var10] * (var2 << 1) / 2;
               if (Canvas.w > 176) {
                  var13.paintFull(var1);
               } else {
                  var13.paintSmall(var1, true);
               }

               this.PaintLineColor(1, var13.x, var13.y, var1);
            }
         }
      }

      for(var10 = 0; var10 < 4; ++var10) {
         if ((var11 = (Avatar)BoardScr.avatarInfos.elementAt(var10)) != null && var11.IDDB != -1 && BoardScr.indexPlayer[var10] == 3) {
            for(var12 = 0; var12 < 12 && this.Q[var10][0] != -1; ++var12) {
               if (this.Q[var10][var12] != -1) {
                  this.card = new Card((byte)this.Q[var10][var12], true);
                  this.card.x = d[BoardScr.indexPlayer[var10]].x;
                  if (Canvas.w > 176) {
                     this.card.y = d[BoardScr.indexPlayer[var10]].y + var12 * var2 * var3 - this.numCardPhom[var10] * var2 * var3 / 2;
                     this.card.paintFull(var1);
                  } else {
                     this.card.y = d[BoardScr.indexPlayer[var10]].y + var12 * var2 - this.numCardPhom[var10] * var2 / 2;
                     this.card.paintSmall(var1, true);
                  }
               }
            }
         }
      }

      for(var10 = 0; var10 < 4; ++var10) {
         if (BoardScr.indexPlayer[var10] == 2) {
            for(var12 = 0; var12 < 12 && this.Q[var10][0] != -1; ++var12) {
               if (this.Q[var10][var12] != -1) {
                  this.card = new Card((byte)this.Q[var10][var12], true);
                  this.card.x = d[BoardScr.indexPlayer[var10]].x + var12 * disShow - (this.numCardPhom[var10] * disShow + (BoardScr.wCard - disShow)) / 2 + BoardScr.wCard / 2;
                  this.card.y = d[BoardScr.indexPlayer[var10]].y;
                  if (Canvas.w > 176) {
                     if (var12 < 11 && this.Q[var10][var12 + 1] != -1) {
                        if (disCard_ > 13) {
                           this.card.paintFull(var1);
                        } else {
                           this.card.paintFull(var1);
                        }
                     } else {
                        this.card.paintFull(var1);
                     }
                  } else {
                     this.card.paintSmall(var1, false);
                  }
               }
            }
         }
      }

      var5 = var1;
      var4 = this;

      for(var6 = 0; var6 < 10; ++var6) {
         byte var14 = 0;
         if (var4.myCard[var6] != null && var4.myCard[var6].cardID != -1) {
            if (var4.L[var6] == 0) {
               var14 = 5;
            }

            var4.ca = new Card((byte)-1, true);
            var4.ca.x = var4.myCard[var6].x;
            var4.ca.y = var4.myCard[var6].y;
            if (!var4.myCard[var6].isShow) {
               var4.ca = var4.myCard[var6];
            }

            if (Canvas.w <= 176) {
               var4.ca.paintSmall(var5, false);
               if (var4.myCard[var6].phom != 0) {
                  var4.PaintLineColor(var4.myCard[var6].phom, var4.myCard[var6].x, var4.myCard[var6].y, var5);
               }
            } else if (var14 == 0 && var6 < 9 && var4.myCard[var6 + 1].cardID != -1 && var6 != var4.selectedCard) {
               if (disCard_ <= 14 && var4.myCard[var6 + 1].x == var4.myCard[var6 + 1].xTo) {
                  var4.ca.paintHalf(var5);
               } else {
                  var4.ca.paintHalfBackFull(var5);
               }

               if (var4.myCard[var6].phom != 0) {
                  var4.PaintLineColor(var4.myCard[var6].phom, var4.myCard[var6].x, var4.myCard[var6].y, var5);
               }
            } else {
               var4.ca.paintFull(var5);
               if (var4.myCard[var6].phom != 0) {
                  var4.PaintLineColor(var4.myCard[var6].phom, var4.myCard[var6].x, var4.myCard[var6].y, var5);
               }
            }
         }

         if (var6 == var4.selectedCard && var4.hCard.cardID != -1) {
            var4.hCard.x = var4.xShow + var4.selectedCard * disCard_;
            var4.hCard.y = var4.g.y + (var4.trans ? -5 : 10);
            if (Canvas.w > 176) {
               if (var4.selectedCard < 9) {
                  if (var4.myCard[var4.selectedCard + 1].cardID != -1 && !Canvas.isKeyBoard) {
                     var4.hCard.paintHalf(var5);
                  } else {
                     var4.hCard.paintFull(var5);
                  }
               } else {
                  var4.hCard.paintFull(var5);
               }
            } else {
               var4.hCard.paintSmall(var5, false);
            }

            if (Canvas.gameTick % 10 > 6 && AvMain.hd == 1 && Canvas.stypeInt == 0) {
               PaintPopup.b.drawFrame(0, var4.hCard.x - 40, var4.hCard.y - 30, 0, var5);
               PaintPopup.b.drawFrame(0, var4.hCard.x - 10, var4.hCard.y - 30, 3, var5);
            }
         }
      }

   }

   private void PaintLineColor(int var1, int var2, int var3, Graphics var4) {
      int var5 = 0;
      switch (var1) {
         case 1:
         case 4:
            var5 = this.colorPhom_1;
            break;
         case 2:
         case 5:
            var5 = this.colorPhom_2;
            break;
         case 3:
         case 6:
            var5 = this.colorPhom_3;
      }

      var4.setColor(var5);
      var4.fillRect(var2 - BoardScr.wCard / 2 + 2, var3 - BoardScr.hcard / 2 + 22, 7, 2);
   }

   private void findPhom() {
      this.phomRandom = 3;
      PBoardScr var2 = this;

      int var4;
      int var3;
      label148:
      for(var4 = 0; var4 < 8 && !var2.searchCardEat(var2.myCard[var4].phom); ++var4) {
         int[] var5 = new int[6];

         for(var3 = 0; var3 < 6; ++var3) {
            var5[var3] = -1;
         }

         for(var3 = 0; var3 < 3; ++var3) {
            if (var2.myCard[var4 + var3].phom != 0) {
               break label148;
            }

            var5[var3] = var2.myCard[var4 + var3].cardID;
         }

         if (var2.checkPhomSanh(var5) || var2.checkPhomBaLa(var5)) {
            ++var2.phomRandom;

            for(var3 = 0; var3 < 3; ++var3) {
               var2.myCard[var4 + var3].phom = (byte)var2.phomRandom;
            }

            var4 += 2;
         }
      }

      for(int var1 = 0; var1 < 10; ++var1) {
         if (!this.searchCardEat(this.myCard[var1].phom) && this.myCard[var1].phom == 0 && this.myCard[var1].cardID != -1) {
            var3 = var1;
            var2 = this;
            if (this.myCard[var1].phom == 0 && this.myCard[var1].cardID != -1) {
               var4 = 0;
               int var8 = 0;
               int var7;
               int[] var9;
               if (var1 > 0 && this.myCard[var1 - 1].phom != 0 && this.myCard[var1 - 1].cardID != -1) {
                  var9 = new int[10];

                  for(var7 = 0; var7 < 10; ++var7) {
                     if (var2.myCard[var7].phom == var2.myCard[var3 - 1].phom) {
                        var9[var7] = var2.myCard[var7].cardID;
                        var4 += var9[var7] / 4 + 1;
                     } else {
                        var9[var7] = -1;
                     }
                  }

                  (var9 = orderArrayIncrease(var9))[9] = var2.myCard[var3].cardID;
                  var9 = orderArrayIncrease(var9);
                  if (!var2.checkPhomSanh(var9) && !var2.checkPhomBaLa(var9)) {
                     var4 = 0;
                  }
               }

               if (var3 < 9 && (var3 == 0 || var3 != 0 && var2.myCard[var3 + 1].phom != var2.myCard[var3 - 1].phom) && var2.myCard[var3 + 1].phom != 0 && var2.myCard[var3 + 1].cardID != -1) {
                  var9 = new int[10];

                  for(var7 = 0; var7 < 10; ++var7) {
                     if (var2.myCard[var7].phom == var2.myCard[var3 + 1].phom) {
                        var9[var7] = var2.myCard[var7].cardID;
                        var8 += var9[var7] / 4 + 1;
                     } else {
                        var9[var7] = -1;
                     }
                  }

                  (var9 = orderArrayIncrease(var9))[9] = var2.myCard[var3].cardID;
                  var9 = orderArrayIncrease(var9);
                  if (!var2.checkPhomSanh(var9) && !var2.checkPhomBaLa(var9)) {
                     var8 = 0;
                  }
               }

               if (var4 < var8) {
                  var2.myCard[var3].phom = var2.myCard[var3 + 1].phom;
               } else if (var4 > 0) {
                  var2.myCard[var3].phom = var2.myCard[var3 - 1].phom;
               }
            }
         }
      }

      if (this.numPhom + (this.phomRandom - 3) == 3) {
         super.center = this.cmdHaPhom;
      }

   }

   private void cleanPhomRandom() {
      byte var1 = 0;

      for(int var2 = 0; var2 < 10; ++var2) {
         if (this.searchCardEat(this.myCard[var2].phom)) {
            return;
         }

         if (this.myCard[var2].phom != 0 && var1 != this.myCard[var2].phom) {
            var1 = this.myCard[var2].phom;
            --this.phomRandom;
         }

         this.myCard[var2].phom = 0;
      }

   }

   private boolean searchCardEat(int var1) {
      for(int var2 = 0; var2 < 3; ++var2) {
         if (this.cardEat[var2].phom == 0) {
            return false;
         }

         if (this.cardEat[var2].phom == var1) {
            return true;
         }
      }

      return false;
   }

   public final void start(byte var1, Vector var2, int var3, int var4) {
      super.start();
      this.initP();
      this.reset();
      BoardScr.currentTime = (long)Canvas.getSecond();
      super.currentPlayer = var3;
      this.firstPlayer = super.currentPlayer;
      BoardScr.interval = var1;
      this.firstHa = var4;
      BoardScr.isStartGame = true;
      var1 = (byte)var2.size();

      for(var3 = 0; var3 < var1; ++var3) {
         Card var5 = (Card)var2.elementAt(var3);
         this.myCard[var3] = new Card(var5.cardID, true);
         this.myCard[var3].x = Canvas.hw;
         this.myCard[var3].y = Canvas.hh;
         this.myCard[var3].isShow = true;
      }

      orderCardIncrease(this.myCard);
      this.findPhom();
      if (super.currentPlayer != GameMidlet.avatar.IDDB) {
         this.resetCmd();
      }

      this.setPosPlaying();
      this.setPosCard(false);
   }

   public final void doFire() {
      this.resetOrderCard();
      super.doFire();
      this.resetChangeCard();
      int var1 = 0;
      int var2 = -1;

      for(int var3 = 0; var3 < 10; ++var3) {
         if (this.L[var3] == 0) {
            ++var1;
            var2 = var3;
         }
      }

      if (var1 > 1) {
         Canvas.startOKDlg(T.canYouOnceOnly);
      } else if (var2 == -1) {
         Canvas.startOKDlg(T.yetSellectCard);
      } else {
         boolean var10000;
         if (this.myCard[var2].phom != 0 && !this.checkCardToEat(var2)) {
            Canvas.startOKDlg(T.ifFireBreakPhom);
            var10000 = false;
         } else {
            var10000 = true;
         }

         if (var10000) {
            this.resetCmd();
            BoardScr.setCmdWaiting();
            CasinoService.gI().moveCo(this.myCard[var2].cardID);
         }
      }

   }

   public final void onFire(int var1, int var2, int var3, byte var4) {
      if (var3 == -1) {
         this.setCmdFire();
         Canvas.startOKDlg(T.cardToMiss);
      } else {
         if (var1 == GameMidlet.avatar.IDDB) {
            this.resetOrderCard();
         }

         this.resetCmd();
         BoardScr.currentTime = (long)Canvas.getSecond();
         int var5;
         if ((var5 = BoardScr.getIndexByID(var2)) != -1) {
            this.h.x = BoardScr.posAvatar[BoardScr.indexPlayer[var5]].x;
            if (var2 == GameMidlet.avatar.IDDB) {
               this.h.x = this.g.x + super.selectedCard * disCard_;
            }

            if (var1 == GameMidlet.avatar.IDDB) {
               if (getAssetCard(this.cardShow[BoardScr.indexOfMe]) != -1 && getAssetCard(this.cardShow[BoardScr.indexOfMe]) <= 3) {
                  this.setCmdEatAndGet();
               }

               this.cleanPhomRandom();
               this.findPhom();
            }

            this.h.y = BoardScr.posAvatar[BoardScr.indexPlayer[var5]].y;
            this.cardCurrent = var3;
            super.currentPlayer = var1;
            this.firstPlayer = var2;
            this.numberCard[var5] = var4;
            this.M = true;
            int var10003 = this.distant[var5]++;
         }
      }

   }

   public final void onSkipPlayer(int var1, int var2) {
      if (BoardScr.getIndexByID(var1) != -1) {
         this.firstHa = var2;
         if (var1 == GameMidlet.avatar.IDDB && super.currentPlayer != var1) {
            this.setCmdEatAndGet();
            this.setPosCard(false);
         }

         super.currentPlayer = var1;
         BoardScr.currentTime = (long)Canvas.getSecond();
      }

   }

   private void resetUpCard() {
      for(int var1 = 0; var1 < this.L.length; ++var1) {
         this.L[var1] = -1;
      }

   }

   public final void onHaPhom(boolean var1, int[] var2, boolean var3, int var4) {
      int var5 = BoardScr.getIndexByID(var4);
      if (!var1) {
         Canvas.startOKDlg(T.notSamePhom);
      } else {
         if (var4 == GameMidlet.avatar.IDDB) {
            this.resetOrderCard();
         }

         this.resetUpCard();
         this.isU = var3;
         int var6 = 1;

         int var7;
         for(var7 = 0; var7 < this.Q[var5].length; ++var7) {
            if (this.Q[var5][var7] == -1) {
               if (var6 == 1) {
                  var6 = var7;
                  break;
               }

               var6 = 1;
            } else {
               var6 = 0;
            }
         }

         for(var7 = var6; var7 < var2.length; ++var7) {
            if (var2[var7 - var6] != -1) {
               this.Q[var5][var7] = var2[var7 - var6];
            }
         }

         if (GameMidlet.avatar.IDDB == var4) {
            this.isHaPhom = true;
            var7 = 0;

            while(true) {
               if (var7 >= 10) {
                  this.setCmdFire();
                  this.myCard = orderCardIncrease(this.myCard);
                  if (this.myCard[super.selectedCard].cardID == -1) {
                     super.selectedCard = this.setAssetCard() - 1;
                  }

                  this.setPosCard(false);
                  break;
               }

               for(var6 = 0; var6 < var2.length; ++var6) {
                  if (this.myCard[var7].cardID == var2[var6]) {
                     this.resetCell(var7);
                     break;
                  }
               }

               ++var7;
            }
         }

         if (this.isU) {
            this.finish = true;
            this.setContinue();
         }

         this.numCardPhom[var5] = 0;

         for(var7 = 0; var7 < this.Q[var5].length; ++var7) {
            if (this.Q[var5][var7] != -1) {
               int var10003 = this.numCardPhom[var5]++;
            }
         }
      }

   }

   public final void onFinish(int[] var1, int[][] var2) {
      this.resetOrderCard();
      int var3 = 1000;

      int var4;
      for(var4 = 0; var4 < 4; ++var4) {
         if (var1[var4] >= 0 && var1[var4] < var3) {
            var3 = var1[var4];
            this.winer = var4;
         }

         this.scorePlayer[var4] = var1[var4];
      }

      this.cardRac = var2;

      for(var4 = 0; var4 < 4; ++var4) {
         this.numCardRac[var4] = (byte)getAssetArray(var2[var4]);
      }

      this.finish = true;
      this.setContinue();

      for(var4 = 0; var4 < 4; ++var4) {
         Avatar var5;
         if ((var5 = (Avatar)BoardScr.avatarInfos.elementAt(var4)) != null && var5.IDDB != -1 && this.scorePlayer[var4] != -1) {
            if (this.scorePlayer[var4] == -2) {
               BoardScr.showChat(var5.IDDB, T.chay);
            } else if (var4 == this.winer) {
               BoardScr.showChat(var5.IDDB, T.win);
            } else {
               BoardScr.showChat(var5.IDDB, T.lose);
            }

            var5.isReady = false;
         }
      }

      GameMidlet.avatar.isReady = false;
      this.setPosCard(false);
   }

   public final void onOnceWin() {
      this.finish = true;
      this.setContinue();
      if (!BoardScr.disableReady) {
         this.winer = BoardScr.indexOfMe;
         BoardScr.showChat(((Avatar)BoardScr.avatarInfos.elementAt(this.winer)).IDDB, T.win);
      }

      BoardScr.isStartGame = true;
   }

   public final void onDenBai(int var1) {
      this.finish = true;
      this.setContinue();
      this.winer = var1;
      this.isU = true;
      BoardScr.showChat(this.winer, T.u);
      BoardScr.showChat(this.firstPlayer, T.denU);
   }

   private boolean checkCardToEat(int var1) {
      int var2 = -1;

      int var4;
      for(var4 = 0; var4 < 3; ++var4) {
         if (this.cardEat[var4].phom != 0 && this.cardEat[var4].phom == this.myCard[var1].phom) {
            var2 = var4;
            break;
         }
      }

      int[] var6;
      if (var2 == -1) {
         if (this.myCard[var1].phom != 0) {
            var6 = new int[10];

            for(var4 = 0; var4 < 10; ++var4) {
               if (this.myCard[var4].phom == this.myCard[var1].phom && this.L[var4] == -1 && var4 != var1) {
                  var6[var4] = this.myCard[var4].cardID;
               } else {
                  var6[var4] = -1;
               }
            }

            var6 = orderArrayIncrease(var6);
            if (!this.checkPhomSanh(var6) && !this.checkPhomBaLa(var6)) {
               for(var4 = 0; var4 < 10; ++var4) {
                  if (var4 != var1 && this.myCard[var4].phom == this.myCard[var1].phom) {
                     this.myCard[var4].phom = 0;
                  }
               }

               this.myCard[var1].phom = 0;
            }
         }

         return true;
      } else {
         var6 = new int[10];

         for(var4 = 0; var4 < 10; ++var4) {
            if (this.myCard[var4].phom == this.cardEat[var2].phom && this.L[var4] == -1 && var4 != var1) {
               var6[var4] = this.myCard[var4].cardID;
            } else {
               var6[var4] = -1;
            }
         }

         var6 = orderArrayIncrease(var6);
         var4 = -1;
         var1 = 0;

         int var5;
         for(var5 = 0; var5 < 10; ++var5) {
            if (var6[var5] == this.cardEat[var2].cardID) {
               var4 = var5;
            }
         }

         for(var5 = 0; var5 < 9 && var6[var5 + 1] != -1 && (var6[var5 + 1] / 4 == var6[var5] / 4 || var6[var5 + 1] / 4 - var6[var5] / 4 == 1 && var6[var5] % 4 == var6[var5 + 1] % 4); ++var5) {
            var1 = var5 + 1;
         }

         if (var4 > var1 && var1 > 1) {
            return false;
         } else if (var1 > 1) {
            for(var5 = var1 + 1; var5 < 10; ++var5) {
               for(var2 = 0; var2 < 10; ++var2) {
                  if (var6[var5] == this.myCard[var2].cardID) {
                     this.myCard[var2].phom = 0;
                  }
               }
            }

            return true;
         } else {
            int[] var7 = new int[3];

            for(var2 = 0; var2 < 3; ++var2) {
               var7[var2] = -1;
            }

            for(var2 = 0; var2 <= var1; ++var2) {
               var7[var2] = var6[var2];
               var6[var2] = -1;
            }

            var6 = orderArrayIncrease(var6);
            if (!this.checkPhomSanh(var6) && !this.checkPhomBaLa(var6)) {
               return false;
            } else {
               for(var2 = 0; var2 < 3; ++var2) {
                  for(var1 = 0; var1 < 10; ++var1) {
                     if (var7[var2] == this.myCard[var1].cardID) {
                        this.myCard[var1].phom = 0;
                     }
                  }
               }

               return true;
            }
         }
      }
   }

   public final void onEatCard(boolean var1, int var2, int var3, byte var4) {
      int var5;
      if ((var5 = BoardScr.getIndexByID(super.currentPlayer)) != -1) {
         if (super.currentPlayer == GameMidlet.avatar.IDDB) {
            this.resetOrderCard();
         }

         if (var1) {
            this.aN = true;
            this.aQ = new Card((byte)this.cardCurrent, true);
            int var6 = BoardScr.getIndexByID(this.firstPlayer);
            this.aQ.x = BoardScr.posAvatar[BoardScr.indexPlayer[var6]].x;
            this.aQ.y = BoardScr.posAvatar[BoardScr.indexPlayer[var6]].y;
            int var7;
            if ((var7 = BoardScr.getIndexByID(this.firstPlayer)) != -1) {
               var6 = BoardScr.getIndexByID(this.firstHa);
               if (var7 != var6) {
                  this.cardShow[var7][this.numberCard[var7]] = this.cardShow[var6][this.numberCard[var6]];
               }

               this.cardShow[var6][this.numberCard[var6]] = null;
               this.firstHa = var3;
               this.numberCard[var6] = var4;
               this.distant[var5] = this.numberCard[var5];
               this.distant[var6] = this.numberCard[var6];
            }

            int var10003 = this.numCardEat[var5]++;
            if (super.currentPlayer == GameMidlet.avatar.IDDB) {
               ++this.numPhom;
               if (getAssetCard(this.cardShow[BoardScr.indexOfMe]) == 3) {
                  this.setcmdHaPhom();
               } else {
                  this.setCmdFire();
               }

               for(var6 = var2 - 1; var6 >= 0; --var6) {
                  for(var7 = 0; var7 < 10; ++var7) {
                     if (this.myCard[var7].cardID == this.cardToEat[var6]) {
                        this.myCard[var7].phom = (byte)this.numPhom;
                        this.myCard[this.setAssetCard()] = this.myCard[var7];
                        this.cleanCard(var7);
                     }
                  }
               }

               var6 = this.setAssetCard();
               this.myCard[var6] = new Card((byte)this.cardCurrent, true);
               this.myCard[var6].phom = (byte)this.numPhom;

               for(var7 = 0; var7 < 3; ++var7) {
                  if (this.cardEat[var7].cardID == -1) {
                     this.cardEat[var7] = this.myCard[var6];
                     break;
                  }
               }

               this.cleanPhomRandom();
               this.findPhom();
            }

            this.R[var5][getAssetArray(this.R[var5])] = this.cardCurrent;
            this.resetUpCard();
         } else if (super.currentPlayer == GameMidlet.avatar.IDDB) {
            Canvas.startOKDlg(T.notPhom);
            this.setCmdEatAndGet();
         }

         if (GameMidlet.avatar.IDDB == super.currentPlayer || GameMidlet.avatar.IDDB == this.firstPlayer) {
            this.setPosCard(false);
         }
      }

   }

   private void doGet() {
      this.resetOrderCard();
      this.resetChangeCard();
      this.resetCmd();
      BoardScr.setCmdWaiting();
      CasinoService.gI().GetCardPhom();
   }

   public final void onGetCard(int var1) {
      this.resetOrderCard();
      PBoardScr var2 = this;
      int var3 = 0;

      int var10000;
      while(true) {
         if (var3 >= 10) {
            var10000 = -1;
            break;
         }

         if (var2.myCard[var3].cardID == -1) {
            var10000 = var3;
            break;
         }

         if (var2.searchCardEat(var2.myCard[var3].phom)) {
            for(int var4 = 9; var4 > var3; --var4) {
               var2.myCard[var4] = var2.myCard[var4 - 1];
            }

            var10000 = var3;
            break;
         }

         ++var3;
      }

      this.bg = var10000;
      this.aM = true;
      this.cardE = new Card((byte)var1, true);
   }

   public final void onAddCardToPhom(boolean var1, byte var2) {
      int var3;
      if ((var3 = BoardScr.getIndexByID(super.currentPlayer)) != -1) {
         if (super.currentPlayer == GameMidlet.avatar.IDDB) {
            this.resetOrderCard();
         }

         if (var1) {
            int var7;
            if (GameMidlet.avatar.IDDB == super.currentPlayer) {
               for(var7 = 0; var7 < 10 && this.myCard[var7].cardID != -1; ++var7) {
                  if (var2 == this.myCard[var7].cardID) {
                     this.resetCell(var7);
                     break;
                  }
               }

               this.setPosCard(false);
            }

            var7 = 0;
            int[] var4 = new int[6];

            int var5;
            for(var5 = 0; var5 < 6; ++var5) {
               var4[var5] = -1;
            }

            for(var5 = 0; var5 < this.Q[var3].length; ++var5) {
               if (this.Q[var3][var5] != -1) {
                  var4[var7] = this.Q[var3][var5];
                  ++var7;
               } else {
                  var7 = 0;
                  var4[5] = var2;
                  orderArrayIncrease(var4);
                  int var6;
                  if (this.checkPhomSanh(var4) || this.checkPhomBaLa(var4)) {
                     for(var6 = 11; var6 > var5; --var6) {
                        if (var6 - 1 >= 0) {
                           this.Q[var3][var6] = this.Q[var3][var6 - 1];
                        }
                     }

                     this.Q[var3][var5] = var2;
                  }

                  for(var6 = 0; var6 < 6; ++var6) {
                     var4[var6] = -1;
                  }
               }
            }
         }
      }

   }

   private int setAssetCard() {
      for(int var1 = 0; var1 < 10; ++var1) {
         if ((this.hCard.cardID == -1 || var1 != super.selectedCard) && this.myCard[var1].cardID == -1) {
            return var1;
         }
      }

      return -1;
   }

   private static int getAssetCard(Card[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1] == null) {
            return var1;
         }
      }

      return -1;
   }

   private static int getAssetArray(int[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1] == -1) {
            return var1;
         }
      }

      return -1;
   }

   public final void setPlayers(byte var1, byte var2, int var3, int var4, Vector var5) {
      super.setPlayers(var1, var2, var3, var4, var5);
      GameMidlet.avatar.isReady = false;
      BoardScr.notReadyDelay = 0;
   }

   private static int[] orderArrayIncrease(int[] var0) {
      for(int var1 = 0; var1 < var0.length - 1; ++var1) {
         for(int var2 = var1 + 1; var2 < var0.length; ++var2) {
            int var3;
            if (var0[var2] != -1 && ((var3 = var0[var1]) > var0[var2] || var3 == -1)) {
               var0[var1] = var0[var2];
               var0[var2] = var3;
            }
         }
      }

      return var0;
   }

   private static Card[] orderCardIncrease(Card[] var0) {
      for(int var1 = 0; var1 < var0.length - 1; ++var1) {
         for(int var2 = var1 + 1; var2 < var0.length; ++var2) {
            Card var3;
            if (var0[var2].cardID != -1 && ((var3 = var0[var1]).cardID > var0[var2].cardID || var3.cardID == -1)) {
               var0[var1] = var0[var2];
               var0[var2] = var3;
            }
         }
      }

      return var0;
   }

   private boolean checkPhomSanh(int[] var1) {
      var1 = orderArrayIncrease(var1);
      int var2 = 0;
      int var3 = 0;

      while(true) {
         if (var3 < var1.length - 1 && var1[var3 + 1] != -1) {
            if (var1[var3] - var1[var3 + 1] != 0 && var1[var3 + 1] / 4 - var1[var3] / 4 == 1 && var1[var3] % 4 - var1[var3 + 1] % 4 == 0) {
               ++var2;
               ++var3;
               continue;
            }

            return false;
         }

         if (var2 > 1) {
            return true;
         }

         return false;
      }
   }

   private boolean checkPhomBaLa(int[] var1) {
      var1 = orderArrayIncrease(var1);
      int var2 = 0;
      int var3 = 0;

      while(true) {
         if (var3 < var1.length - 1 && var1[var3 + 1] != -1) {
            if (var1[var3] / 4 - var1[var3 + 1] / 4 == 0 && var1[var3] - var1[var3 + 1] != 0) {
               ++var2;
               ++var3;
               continue;
            }

            return false;
         }

         if (var2 > 1) {
            return true;
         }

         return false;
      }
   }

   public final void onPlaying(int var1, int var2, int var3, int[][] var4, int[][] var5, int var6) {
      this.reset();
      BoardScr.disableReady = true;
      BoardScr.currentTime = (long)Canvas.getSecond();

      int var7;
      for(var7 = 0; var7 < var4.length; ++var7) {
         for(int var8 = 0; var8 < var5[var7].length; ++var8) {
            if (var4[var7][var8] != -1) {
               this.cardShow[var7][var8] = new Card((byte)var4[var7][var8], true);
            }
         }
      }

      this.R = var5;
      BoardScr.interval = var1;
      super.currentPlayer = var2;
      this.firstPlayer = var3;
      this.firstHa = var6;
      BoardScr.isStartGame = true;

      for(var7 = 0; var7 < 4; ++var7) {
         this.cardShow[BoardScr.indexOfMe][var7] = null;
      }

      for(var7 = 0; var7 < 3; ++var7) {
         this.R[BoardScr.indexOfMe][var7] = -1;
      }

      for(var7 = 0; var7 < this.Q.length; ++var7) {
         this.Q[BoardScr.indexOfMe][var7] = -1;
      }

      for(var7 = 0; var7 < this.cardRac.length; ++var7) {
         this.cardRac[BoardScr.indexOfMe][var7] = -1;
      }

      this.setPosPlaying();
      MyScreen.repaint();
      if (Canvas.isKeyBoard && (disCard_ = (Canvas.w - BoardScr.wCard / 2) / 10) > BoardScr.wCard / 3 << 1) {
         disCard_ = BoardScr.wCard / 3 << 1;
      }

   }
}
