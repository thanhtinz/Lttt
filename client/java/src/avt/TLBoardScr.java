package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class TLBoardScr extends BoardScr {
   public static TLBoardScr instance;
   private Vector currentCards;
   private byte[] currentCardsValue;
   private byte currentCardsType;
   private Vector cardShows;
   private byte[] selectedCards;
   private byte selectedCardsType;
   private Vector cards;
   private Command L;
   private Command M;
   private boolean N = false;
   private static int wCard_;
   private static int hcard_;
   private int selectedCard_ = -1;
   private int R;
   private int S;
   private boolean T = false;
   private boolean U = false;
   private int V = 0;
   private boolean forceMove3Bich = false;
   public boolean isFirstMatch = true;

   public static TLBoardScr gI() {
      return instance == null ? (instance = new TLBoardScr()) : instance;
   }

   public final void resetCard() {
      System.out.println("resetCard");
      this.currentCards = new Vector();
      this.currentCardsType = -1;
      this.currentCardsValue = new byte[0];
      super.selectedCard = -1;
      this.selectedCards = new byte[0];
      this.selectedCardsType = -1;
      super.currentPlayer = -1;
      this.cardShows = new Vector();
      super.resetCard();
   }

   private static void sort(Vector var0) {
      int var1 = var0.size();

      for(int var2 = 0; var2 < var1 - 1; ++var2) {
         for(int var3 = var2 + 1; var3 < var1; ++var3) {
            Card var4 = (Card)var0.elementAt(var2);
            Card var5 = (Card)var0.elementAt(var3);
            if (var4.cardID > var5.cardID) {
               Object var7 = var0.elementAt(var3);
               var0.setElementAt(var0.elementAt(var2), var3);
               var0.setElementAt(var7, var2);
            }
         }
      }

   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 20:
            this.selectedCard_ = -1;
            break;
         case 21:
            Canvas.startOKDlg(avt.T.doYouWantSkip, 70);
            break;
         case 70:
            super.currentPlayer = -1;
            this.forceMove3Bich = false;
            CasinoService.gI().skip();
            Canvas.endDlg();
      }

      super.commandTab(var1, var2);
   }

   public TLBoardScr() {
      this.M = new Command(avt.T.sapBaiXong, 20);
      this.L = new Command(avt.T.skip, 21);
      this.initYShow();
   }

   private void initYShow() {
      this.S = Canvas.h - Canvas.hTab;
      if (Canvas.w < 150) {
         wCard_ = 26;
         hcard_ = 32;
         this.S = Canvas.hCan - Canvas.hTab - 10;
      } else {
         wCard_ = 54;
         hcard_ = 72;
      }

      if (AvMain.hd == 2) {
         wCard_ = 144;
         hcard_ = 194;
      }

      String[] var10000 = new String[]{"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "Heo"};
   }

   public final void init() {
      super.init();
      this.initYShow();
      if (BoardScr.isStartGame) {
         this.setPosCard(false);
      }

      this.currentCards = null;
   }

   public final void doContinue() {
      this.resetCard();
      super.doContinue();
   }

   private void doSelect() {
      ((Card)this.cards.elementAt(super.selectedCard)).f = !((Card)this.cards.elementAt(super.selectedCard)).f;
      this.selectedCards = this.getSelectedCardsValue();
      this.selectedCardsType = CardUtils.getType(this.selectedCards);
      if (this.selectedCardsType != -1) {
         BoardScr.addInfo(avt.T.cardTypeName[this.selectedCardsType], 10, -1);
      }

      this.setPosCard(false);
   }

   protected final void doFire() {
      super.doFire();
      boolean var10000;
      if (this.forceMove3Bich) {
         var10000 = false;

         for(int var2 = 0; var2 < this.selectedCards.length; ++var2) {
            if (this.selectedCards[var2] == 0) {
               var10000 = true;
            }
         }

         if (!var10000) {
            BoardScr.showChat(GameMidlet.avatar.IDDB, avt.T.youMustFire3Bich);
            return;
         }

         this.forceMove3Bich = false;
      }

      if (this.currentCards != null && this.currentCards.size() != 0) {
         label133: {
            byte var4 = this.currentCardsType;
            byte[] var3 = this.currentCardsValue;
            byte var6 = this.selectedCardsType;
            byte[] var5 = this.selectedCards;
            switch (var4) {
               case -1:
                  if (var6 != -1) {
                     var10000 = true;
                     break label133;
                  }
               case 0:
                  if (var6 == 0 && var5[0] > var3[0]) {
                     var10000 = true;
                     break label133;
                  }

                  if (var3[0] / 4 != 12 || var6 != 4 && var6 != 5 && var6 != 6) {
                     break;
                  }

                  var10000 = true;
                  break label133;
               case 1:
                  if (var6 == 1 && var5.length == var3.length && var5[var5.length - 1] > var3[var3.length - 1]) {
                     var10000 = true;
                     break label133;
                  }
                  break;
               case 2:
                  if (var6 == 2 && var5[1] > var3[1]) {
                     var10000 = true;
                     break label133;
                  }

                  if (var3[0] / 4 == 12 && (var6 == 6 || var6 == 5)) {
                     var10000 = true;
                     break label133;
                  }
                  break;
               case 3:
                  if (var6 == 3 && var5[2] > var3[2]) {
                     var10000 = true;
                     break label133;
                  }
                  break;
               case 4:
                  if (var6 == 4 && var5[5] > var3[5] || var6 == 6 || var6 == 5) {
                     var10000 = true;
                     break label133;
                  }
                  break;
               case 5:
                  if (var6 == 5 && var5[7] > var3[7]) {
                     var10000 = true;
                     break label133;
                  }
                  break;
               case 6:
                  if (var6 == 6 && var5[3] > var3[3] || var6 == 5) {
                     var10000 = true;
                     break label133;
                  }
            }

            var10000 = false;
         }

         if (!var10000) {
            BoardScr.showChat(GameMidlet.avatar.IDDB, avt.T.notSameOrSmaller);
            return;
         }
      }

      CasinoService.gI().move(this.selectedCards);
      super.currentPlayer = -1;
      super.right = null;
   }

   private void setUpDown(boolean var1) {
      ((Card)this.cards.elementAt(super.selectedCard)).f = var1;
      this.selectedCards = this.getSelectedCardsValue();
      this.selectedCardsType = CardUtils.getType(this.selectedCards);
      this.setPosCard(false);
   }

   private void moveSelect(int var1) {
      if (this.selectedCard_ == -1) {
         super.selectedCard += var1;
         if (super.selectedCard >= this.cards.size()) {
            super.selectedCard = 0;
         }

         if (super.selectedCard < 0) {
            super.selectedCard = this.cards.size() - 1;
            return;
         }
      } else {
         if (this.selectedCard_ > 0 || this.selectedCard_ < this.cards.size() - 1) {
            Card var2 = (Card)this.cards.elementAt(this.selectedCard_ + var1);
            this.cards.setElementAt(this.cards.elementAt(this.selectedCard_), this.selectedCard_ + var1);
            this.cards.setElementAt(var2, this.selectedCard_);
            this.selectedCard_ += var1;
            super.selectedCard = this.selectedCard_;
         }

         this.setPosCard(true);
      }

   }

   public final void updateKey() {
      super.updateKey();
      if (BoardScr.isStartGame) {
         int var1 = this.cards.size();
         if (this.cards != null && var1 > 0) {
            if (Canvas.isPointerClick && Canvas.isPointer(this.R - wCard_ / 2, this.S - hcard_ / 2 - 30, super.disCard * (var1 - 1) + wCard_, hcard_ + 15)) {
               this.U = true;
               Canvas.isPointerClick = false;
               this.V = (Canvas.pxLast - (this.R - wCard_ / 2)) / super.disCard;
               this.T = true;
               super.selectedCard = this.V;
            }

            if (this.U) {
               var1 = Canvas.dx();
               int var2 = Canvas.dy();
               if (Canvas.isPointerDown) {
                  if (var2 > 10) {
                     this.setUpDown(true);
                  } else if (var2 < -10) {
                     this.setUpDown(false);
                  } else if (CRes.abs(var1) > 10) {
                     if (this.T) {
                        this.selectedCard_ = super.selectedCard;
                     }

                     this.T = false;
                     int var3 = (Canvas.px - (this.R - wCard_ / 2)) / super.disCard;
                     if (super.selectedCard != var3) {
                        if (this.selectedCard_ != -1) {
                           if (var3 < this.selectedCard_) {
                              this.moveSelect(-1);
                           } else if (var3 > this.selectedCard_) {
                              this.moveSelect(1);
                           }

                           super.selectedCard = this.selectedCard_;
                           this.T = true;
                           return;
                        }

                        this.T = false;
                     }

                     super.selectedCard = var3;
                     if (super.selectedCard < 0) {
                        super.selectedCard = 0;
                     }

                     if (super.selectedCard >= this.cards.size()) {
                        super.selectedCard = this.cards.size() - 1;
                     }

                     this.setPosCard(true);
                  }
               }

               if (Canvas.isPointerRelease) {
                  this.U = false;
                  this.selectedCard_ = -1;
                  if (CRes.abs(var1) <= 10 && CRes.abs(var2) <= 10) {
                     this.setUpDown(!((Card)this.cards.elementAt(super.selectedCard)).f);
                  }
               }
            }
         }

         if (Canvas.a(6)) {
            this.moveSelect(1);
         } else if (Canvas.a(4)) {
            this.moveSelect(-1);
         }

         if (Canvas.a(2)) {
            if (this.selectedCard_ != -1) {
               this.selectedCard_ = -1;
               this.setPosCard(true);
               return;
            }

            this.doSelect();
            this.setPosCard(true);
         }

         if (Canvas.a(8)) {
            if (((Card)this.cards.elementAt(super.selectedCard)).f) {
               this.doSelect();
               this.setPosCard(true);
               return;
            }

            this.selectedCard_ = super.selectedCard;
            this.setPosCard(true);
         }
      }

   }

   public final void update() {
      super.update();
      Card var2;
      int var3;
      if (BoardScr.isStartGame && this.cards != null && this.cards.size() > 0) {
         for(int var1 = this.cards.size() - 1; var1 >= 0 && (var3 = (var2 = (Card)this.cards.elementAt(var1)).translate()) != 1; --var1) {
            if (var3 == -1) {
               var2.isShow = false;
            }
         }
      }

      if (BoardScr.dieTime != 0L && (BoardScr.currentTime = System.currentTimeMillis()) > BoardScr.dieTime) {
         if (super.currentPlayer == GameMidlet.avatar.IDDB) {
            CasinoService.gI().skip();
            super.currentPlayer = -1;
         }

         BoardScr.dieTime = 0L;
      }

      if (!BoardScr.isStartGame && !BoardScr.disableReady) {
         this.updateReady();
         super.right = null;
      } else if (this.selectedCard_ != -1) {
         super.left = null;
         super.right = null;
         if (Canvas.stypeInt == 0) {
            super.center = this.M;
         }
      } else {
         if (BoardScr.isGameEnd) {
            super.left = null;
            super.center = BoardScr.cmdBack;
            super.right = null;
         } else if (super.currentPlayer == GameMidlet.avatar.IDDB) {
            super.right = this.L;
            if (this.getSelectedCardsValue().length > 0) {
               if (this.selectedCardsType != -1) {
                  super.center = BoardScr.cmdFire;
               } else {
                  super.center = null;
               }
            } else {
               super.center = null;
            }
         } else {
            super.right = null;
            super.center = null;
         }

         TLBoardScr var4 = this;
         if (this.currentCards != null && this.N) {
            for(int var5 = 0; var5 < var4.currentCards.size(); ++var5) {
               Card var6;
               if ((var6 = (Card)var4.currentCards.elementAt(var5)) != null) {
                  var3 = var6.translate();
                  if (var5 == var4.currentCards.size() - 1 && var3 == 0) {
                     var4.N = false;
                  }
               }
            }
         }
      }

   }

   private void setPosCard(boolean var1) {
      int var2;
      if (this.cards.size() > 0 && !var1) {
         var2 = 12;
         if (Canvas.isKeyBoard && (var2 = (Canvas.w - wCard_ / 2) / this.cards.size()) > wCard_ / 3 << 1) {
            var2 = wCard_ / 3 << 1;
         }

         super.disCard = (Canvas.w - 60) / this.cards.size() + 1;
         if (super.disCard > var2) {
            super.disCard = var2;
         }

         if (super.disCard < 9) {
            super.disCard = 9;
         }

         if (Canvas.isKeyBoard) {
            super.disCard = var2;
         }

         this.R = (Canvas.w - (super.disCard * this.cards.size() + (wCard_ - super.disCard)) >> 1) + wCard_ / 2;
         if (this.R < wCard_ / 2) {
            this.R = wCard_ / 2;
         }
      }

      var2 = this.cards.size();
      int var3 = this.R;

      for(int var4 = 0; var4 < var2; ++var4) {
         Card var5 = (Card)this.cards.elementAt(var4);
         int var6 = 0;
         if (var5.f) {
            var6 = -8 * (Canvas.stypeInt + 1);
         }

         int var8 = this.S + var6;
         var5.xTo = var3;
         var5.yTo = var8;
         var5.distant = CRes.distance(var5.x, var5.y, var5.xTo, var5.yTo);
         if (var4 == this.selectedCard_ && !Canvas.isKeyBoard) {
            var5.yTo += 8 * (Canvas.stypeInt + 1);
         }

         var3 += super.disCard;
         if (var1) {
            var5.x = var5.xTo;
            var5.y = var5.yTo;
         }
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      super.paint(var1);
   }

   public final void paintNamePlayers(Graphics var1) {
      for(int var2 = 0; var2 < BoardScr.numPlayer; ++var2) {
         Avatar var3;
         if ((var3 = (Avatar)BoardScr.avatarInfos.elementAt(var2)).IDDB != -1) {
            if (var3.IDDB != GameMidlet.avatar.IDDB || !BoardScr.isStartGame) {
               var3.paintIcon(var1, var3.x, var3.y, false);
            }

            var3.paintName(var1, var3.x, var3.y);
            BoardScr.paintReady(var1, var3.x, var3.y - 50, 3, var3);
         }
      }

   }

   public final void paintMain(Graphics var1) {
      super.paintMain(var1);
      this.paintNamePlayers(var1);
      Graphics var3;
      TLBoardScr var2;
      int var11;
      if ((BoardScr.isStartGame || BoardScr.disableReady) && this.currentCards != null && this.currentCards.size() != 0) {
         var3 = var1;
         var2 = this;
         var11 = this.currentCards.size();

         for(int var7 = 0; var7 < var11; ++var7) {
            Card var8 = (Card)var2.currentCards.elementAt(var7);
            if (Canvas.w < 150) {
               var8.paintSmall(var3, false);
            } else if (var7 == var11 - 1) {
               var8.paintFull(var3);
            } else {
               var8.paintHalf(var3);
            }
         }

         if (!this.N) {
            this.N = true;
         }
      }

      if (BoardScr.isStartGame || BoardScr.disableReady) {
         var3 = var1;
         var2 = this;

         for(var11 = 0; var11 < 4; ++var11) {
            Avatar var12;
            if ((var12 = (Avatar)BoardScr.avatarInfos.elementAt(var11)).IDDB != -1) {
               byte var13 = 0;
               byte var14 = 0;
               if (BoardScr.indexPlayer[var11] == 2) {
                  var13 = -80;
               }

               if (BoardScr.indexPlayer[var11] == 1) {
                  var14 = -10;
               } else if (BoardScr.indexPlayer[var11] == 3) {
                  var14 = 10;
               }

               if (Canvas.w > 160) {
                  Canvas.smallFontYellow.drawString(var3, var12.getMoneyNew() + " " + avt.T.getMoney(), BoardScr.posAvatar[BoardScr.indexPlayer[var11]].x + var14, BoardScr.posAvatar[BoardScr.indexPlayer[var11]].y + 5 + var13, BoardScr.posAvatar[BoardScr.indexPlayer[var11]].anchor);
               }

               if (var12.IDDB == var2.currentPlayer && var2.center != BoardScr.cmdBack) {
                  String var15 = "";
                  if (BoardScr.dieTime != 0L) {
                     long var9 = (BoardScr.currentTime - BoardScr.dieTime) / 1000L;
                     var15 = var15 + -var9;
                  }

                  int var16 = BoardScr.posAvatar[BoardScr.indexPlayer[var11]].x;
                  int var10 = BoardScr.posAvatar[BoardScr.indexPlayer[var11]].y + 13 * AvMain.hd;
                  if (BoardScr.indexPlayer[var11] == 2) {
                     var10 = var2.S - hcard_ / 2 - 20 * AvMain.hd;
                  }

                  PaintPopup.fill(var16 - 10 * AvMain.hd, var10, 20 * AvMain.hd, AvMain.hBlack, 16776365, var3);
                  var3.setColor(332544);
                  var3.drawRect(var16 - 10 * AvMain.hd, var10, 20 * AvMain.hd, AvMain.hBlack);
                  Canvas.fontChatB.drawString(var3, var15, var16, var10 + 1, 2);
               }
            }
         }
      }

      if (BoardScr.isStartGame) {
         this.paintShowCards(var1);
      }

      if (BoardScr.isStartGame || BoardScr.disableReady) {
         this.paintCCard(var1);
      }

      BoardScr.paintChat(var1);
      Canvas.resetTrans(var1);
   }

   private void paintShowCards(Graphics var1) {
      if (BoardScr.isStartGame && this.cards != null && this.cards.size() > 0) {
         int var2 = this.cards.size();
         int var3 = 0;
         int var4 = 0;

         for(int var5 = 0; var5 < var2; ++var5) {
            Card var6 = (Card)this.cards.elementAt(var5);
            Card var7;
            (var7 = new Card((byte)-1, false)).x = var6.x;
            var7.y = var6.y;
            if (!var6.isShow) {
               var7 = (Card)this.cards.elementAt(var5);
            }

            if (Canvas.w < 150) {
               var7.paintSmall(var1, false);
            } else if (var5 != var2 - 1 && var5 != this.selectedCard_ && !var6.f && var5 != this.selectedCard_ - 1 && (var7 == null || !var7.f)) {
               if (super.disCard <= 14 && var7.x == var7.xTo && var7.y == var7.yTo) {
                  var7.paintHalf(var1);
               } else {
                  var7.paintHalfBackFull(var1);
               }
            } else {
               var7.paintFull(var1);
            }

            if (var5 == super.selectedCard) {
               var4 = var7.y - hcard_ / 2 - 2 + (Canvas.gameTick % 10 > 4 ? 2 : 0);
               var3 = var7.x - wCard_ / 2 + 5 * AvMain.hd;
            }

            if (Canvas.stypeInt == 0 && var5 == this.selectedCard_ && Canvas.gameTick % 10 > 6 && AvMain.hd == 1) {
               PaintPopup.b.drawFrame(0, var6.x - 40, var6.y - 30, 0, var1);
               PaintPopup.b.drawFrame(0, var6.x - 10, var6.y - 30, 3, var1);
            }
         }

         if (Canvas.stypeInt == 0) {
            MiniMap.gI().imgArrow.drawFrame(0, var3, var4, 0, 33, var1);
         }
      }

   }

   private void paintCCard(Graphics var1) {
      if (this.cardShows != null && this.cardShows.size() != 0) {
         int var2 = this.cardShows.size();
         int var3;
         if ((var3 = (Canvas.w - 60) / var2 + 1) > 12) {
            var3 = 12;
         }

         int var4 = Canvas.hw - (var3 * var2 >> 1) + 6;
         int var5 = (Canvas.h + Canvas.hTab) / 2;

         for(int var6 = 0; var6 < var2; ++var6) {
            Card var7;
            (var7 = (Card)this.cardShows.elementAt(var6)).x = var4;
            var7.y = var5;
            var4 += var3 * AvMain.hd;
            if (Canvas.w < 150) {
               var7.paintSmall(var1, false);
            } else if (var6 == var2 - 1) {
               var7.paintFull(var1);
            } else {
               var7.paintHalf(var1);
            }
         }
      }

   }

   public final void start(int var1, byte var2, Vector var3) {
      MyScreen.repaint();
      this.initYShow();
      super.start();
      BoardScr.isStartGame = true;
      this.forceMove3Bich = false;
      int var4;
      if (this.isFirstMatch && var1 == GameMidlet.avatar.IDDB) {
         for(var4 = 0; var4 < var3.size(); ++var4) {
            if (((Card)var3.elementAt(var4)).cardID == 0) {
               this.forceMove3Bich = true;
               break;
            }
         }
      }

      this.cardShows = null;
      this.currentCards = new Vector();
      this.currentCardsType = -1;
      this.currentCardsValue = new byte[0];
      BoardScr.isGameEnd = false;
      this.cards = var3;
      sort(var3);

      for(var4 = 0; var4 < this.cards.size(); ++var4) {
         Card var5;
         (var5 = (Card)this.cards.elementAt(var4)).x = Canvas.hw;
         var5.y = (Canvas.h + Canvas.hTab) / 2;
         var5.isShow = true;
      }

      for(int var6 = 0; var6 < BoardScr.numPlayer; ++var6) {
         BoardScr.avatarInfos.elementAt(var6);
      }

      BoardScr.interval = var2;
      BoardScr.dieTime = System.currentTimeMillis() + (long)(var2 * 1000);
      if (var1 == GameMidlet.avatar.IDDB) {
         super.right = this.L;
      }

      Avatar var7 = BoardScr.getAvatarByID(var1);
      BoardScr.addInfo(var7.name + avt.T.firstFire, 20, var7.IDDB);
      this.currentCardsType = -1;
      this.currentCardsValue = new byte[0];
      super.selectedCard = 2;
      super.currentPlayer = var1;
      this.setPosPlaying();
      this.setPosCard(false);
   }

   public final void move(int var1, byte[] var2, int var3) {
      this.forceMove3Bich = false;
      if (var1 != -1) {
         int var4 = BoardScr.getIndexByID(var1);
         int var6 = BoardScr.indexPlayer[var4];
         byte[] var5 = var2;
         TLBoardScr var15 = this;
         int var7 = 0;
         int var8 = 0;
         int var10;
         switch (var6) {
            case 0:
               var7 = Canvas.hw;
               var8 = -27;
               break;
            case 1:
               var7 = -10;
               var8 = (Canvas.h + Canvas.hTab) / 2 - 20;
               break;
            case 2:
               var7 = Canvas.hw;
               var8 = Canvas.h + Canvas.hTab - 20;
               var6 = this.cards.size() - 1;

               for(; var6 >= 0; --var6) {
                  Card var9 = (Card)var15.cards.elementAt(var6);

                  for(var10 = 0; var10 < var5.length; ++var10) {
                     if (var9.cardID == var5[var10]) {
                        var7 = var9.x;
                        var8 = var9.y;
                        break;
                     }
                  }
               }
               break;
            case 3:
               var7 = Canvas.w + 10;
               var8 = (Canvas.h + Canvas.hTab) / 2 - 20;
         }

         var6 = Canvas.hw + CRes.random.nextInt(20);
         int var16 = Canvas.h / 2 - 20 * AvMain.hd + CRes.random.nextInt(25);
         var10 = var5.length;
         int var11;
         if ((var11 = (Canvas.w - 60) / var10 + 1) > 12) {
            var11 = 12;
         }

         int var12 = (var11 * var10 >> 1) + 6;
         var15.N = true;
         var15.currentCards = new Vector();
         var15.currentCardsValue = var5;

         for(int var13 = 0; var13 < var10; ++var13) {
            Card var14;
            (var14 = new Card(var5[var13])).x = var7 + var13 * var15.disCard;
            var14.y = var8;
            var14.xTo = var6 - var12;
            var14.yTo = var16;
            var12 -= var11 * AvMain.hd;
            var15.currentCards.addElement(var14);
         }

         var15.currentCardsType = CardUtils.getType(var15.currentCardsValue);
      }

      if (var1 == GameMidlet.avatar.IDDB) {
         this.removeCards(var2);
         super.selectedCard = 0;
         this.setPosCard(false);
      }

      super.currentPlayer = var3;
      if (super.currentPlayer == GameMidlet.avatar.IDDB) {
         if (this.getSelectedCardsValue().length == 0) {
            super.right = this.L;
         } else {
            super.right = BoardScr.cmdFire;
         }
      } else {
         super.right = null;
      }

      if (BoardScr.interval == 0) {
         BoardScr.interval = 30;
      }

      BoardScr.dieTime = System.currentTimeMillis() + (long)(BoardScr.interval * 1000);
   }

   public final void skip(int var1, int var2, boolean var3) {
      if (var3) {
         MyScreen.repaint();
      }

      String var4;
      Avatar var5;
      if ((var5 = BoardScr.getAvatarByID(var1)).name.equals("")) {
         var4 = avt.T.exitBoard;
      } else {
         var4 = avt.T.skip;
      }

      BoardScr.addInfo(var4, 60, var5.IDDB);
      super.currentPlayer = var2;
      if (var3) {
         this.currentCards = new Vector();
         this.currentCardsType = -1;
         this.currentCardsValue = new byte[0];
      }

      if (super.currentPlayer == GameMidlet.avatar.IDDB) {
         if (this.getSelectedCardsValue().length == 0) {
            super.right = this.L;
         } else {
            super.right = BoardScr.cmdFire;
         }
      } else {
         super.right = null;
      }

      BoardScr.dieTime = System.currentTimeMillis() + (long)(BoardScr.interval * 1000);
   }

   public final void showCards(int var1, byte[] var2) {
      Avatar var3 = BoardScr.getAvatarByID(var1);
      CardUtils.sort(var2);
      this.cardShows = new Vector();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         this.cardShows.addElement(new Card(var2[var4]));
      }

      if (var3 != null && var3.IDDB == var1 && this.cards != null) {
         this.cards.removeAllElements();
      }

   }

   public static void finish(int var0, byte var1, int var2, int var3) {
      Avatar var4;
      if ((var4 = BoardScr.getAvatarByID(var0)) != null) {
         var4.isReady = false;
         if ((var3 += var4.exp) < 0) {
            var3 = 0;
         }

         var4.setExp(var3);
         var4.setMoneyNew(var4.getMoneyNew() + var2);
         if (var4.IDDB == GameMidlet.avatar.IDDB) {
            GameMidlet.avatar.setMoneyNew(var4.getMoneyNew());
         }
      }

      BoardScr.showChat(var0, avt.T.goad + (var1 + 1));
   }

   public static void stopGame() {
      BoardScr.isGameEnd = true;
   }

   public final void moveError(String var1) {
      BoardScr.addInfo(var1, 100, GameMidlet.avatar.IDDB);
      super.currentPlayer = GameMidlet.avatar.IDDB;
   }

   public static void setMode(boolean var0) {
      MyScreen.repaint();
      BoardScr.isStartGame = false;
   }

   private void removeCards(byte[] var1) {
      for(int var2 = this.cards.size() - 1; var2 >= 0; --var2) {
         Card var3 = (Card)this.cards.elementAt(var2);

         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (var3.cardID == var1[var4]) {
               this.cards.removeElementAt(var2);
            }
         }
      }

   }

   private byte[] getSelectedCardsValue() {
      Vector var1 = new Vector();
      int var2 = this.cards.size();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         Card var4;
         if ((var4 = (Card)this.cards.elementAt(var3)).f) {
            var1.addElement(var4);
         }
      }

      byte[] var5 = new byte[var3 = var1.size()];

      for(var2 = 0; var2 < var3; ++var2) {
         var5[var2] = ((Card)var1.elementAt(var2)).cardID;
      }

      CardUtils.sort(var5);
      return var5;
   }

   static {
      CRes.random.setSeed(System.currentTimeMillis());
   }
}
