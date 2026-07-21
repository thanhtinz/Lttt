package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class BaLaBoardScr extends BoardScr {
   public static BaLaBoardScr instance;

   public static BaLaBoardScr gI() {
      return instance == null ? (instance = new BaLaBoardScr()) : instance;
   }

   private Vector myCards;
   private Vector[] otherCards;
   private Vector[] phomCards;
   private int[] moneyPlayers;
   private int winnerId;
   private byte rankOrd;
   private boolean isFinish;
   private Command cmdSkip;
   private Command cmdHaBai;
   private int wCard_;
   private int hcard_;
   private int xShow;
   private static int disCard_ = 14;
   private Vector listFireWork = new Vector();

   public BaLaBoardScr() {
      this.cmdSkip = new Command(T.skip, 20);
      this.cmdHaBai = new Command(T.haBai, 21);
      this.initYShow();
   }

   private void initYShow() {
      if (Canvas.w < 150) {
         wCard_ = 26;
         hcard_ = 32;
      } else {
         wCard_ = 54;
         hcard_ = 72;
      }

      if (AvMain.hd == 2) {
         wCard_ = 144;
         hcard_ = 194;
      }
   }

   public final void init() {
      super.init();
      this.initYShow();
      if (BoardScr.isStartGame) {
         this.setPosCard();
      }
   }

      public final void resetCard() {
         this.myCards = new Vector();
         this.otherCards = new Vector[4];
         for (int i = 0; i < 4; ++i) {
            this.otherCards[i] = new Vector();
         }
         this.phomCards = new Vector[4];
         for (int i = 0; i < 4; ++i) {
            this.phomCards[i] = new Vector();
         }
         this.moneyPlayers = new int[4];
         this.winnerId = -1;
         this.rankOrd = 0;
         this.isFinish = false;
         super.resetCard();
      }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 20:
            CasinoService.gI().e();
            break;
         case 21:
            if (this.myCards != null) {
               for (int i = 0; i < this.myCards.size(); ++i) {
                  Card c = (Card) this.myCards.elementAt(i);
                  c.isShow = true;
                  c.f = false;
               }
            }
            byte[] cardData = new byte[this.myCards != null ? this.myCards.size() : 0];
            for (int i = 0; i < cardData.length; ++i) {
               cardData[i] = ((Card) this.myCards.elementAt(i)).cardID;
            }
            CasinoService.gI().move(cardData);
            break;
      }
      super.commandTab(var1, var2);
   }

   public final void update() {
      super.update();
      if (!BoardScr.isStartGame && !BoardScr.disableReady) {
         this.updateReady();
      } else {
         BoardScr.dieTime = (long)((int)(System.currentTimeMillis() / 1000L - BoardScr.currentTime));
         if (this.myCards != null) {
            for (int i = this.myCards.size() - 1; i >= 0; --i) {
               Card card = (Card) this.myCards.elementAt(i);
               if (card.translate() == -1) {
                  card.isShow = false;
               }
            }
         }

         for (int i = 0; i < this.listFireWork.size(); ++i) {
            Point p = (Point) this.listFireWork.elementAt(i);
            int angle = CRes.tan(p.xTo - p.x, -(p.yTo - p.y));
            if (CRes.abs(angle - p.h) > 10) {
               p.h -= p.height * p.catagory;
               p.h = CRes.fixangle(p.h);
            } else {
               p.h = angle;
               p.dis = (byte)(p.dis + 2);
            }

            if (p.color >= 4) {
               p.color = 0;
            }
            ++p.color;

            int dx = p.dis * CRes.cos(p.h) >> 10;
            int dy = -(p.dis * CRes.sin(p.h)) >> 10;
            if (CRes.distance(p.x, p.y, p.xTo, p.yTo) >= p.dis) {
               p.x += dx;
               p.y += dy;
            } else {
               this.listFireWork.removeElement(p);
            }
         }
      }
   }

      public final void updateKey () {
         super.updateKey();
         if (!BoardScr.isStartGame) {
            return;
         }


         if (Canvas.isPointerClick) {
            Canvas.isPointerClick = false;
            int numCards = this.myCards != null ? this.myCards.size() : 0;
            if (numCards > 0 && Canvas.isPointer(0, Canvas.h - Canvas.hTab - hcard_ - 20, Canvas.w, hcard_ + 20)) {
               int cardIndex = (Canvas.px - (this.xShow - wCard_ / 2)) / disCard_;
               if (cardIndex >= 0 && cardIndex < numCards) {
                  super.selectedCard = cardIndex;
                  this.setPosCard();
               }
            }
         }

         if (Canvas.a(2)) {
            if (super.selectedCard >= 0 && super.selectedCard < this.myCards.size()) {
               Card card = (Card) this.myCards.elementAt(super.selectedCard);
               card.f = !card.f;
               this.setPosCard();
            }
         }

         if (Canvas.a(4)) {
            if (super.selectedCard > 0) {
               --super.selectedCard;
               this.setPosCard();
            }
         }

         if (Canvas.a(6)) {
            if (super.selectedCard < this.myCards.size() - 1) {
               ++super.selectedCard;
               this.setPosCard();
            }
         }
      }

      public final void paint (Graphics var1){
         this.paintMain(var1);
         Canvas.resetTrans(var1);
         Graphics g = var1;
         for (int i = 0; i < this.listFireWork.size(); ++i) {
            Point p = (Point) this.listFireWork.elementAt(i);
            if (p.dis >= 0) {
               Canvas.O.drawString(g, "+" + p.distant, p.x, p.y, 2);
            }
         }
         super.paint(var1);
      }

      public final void paintNamePlayers (Graphics var1){
         for (int i = 0; i < BoardScr.numPlayer; ++i) {
            Avatar av;
            if ((av = (Avatar) BoardScr.avatarInfos.elementAt(i)).IDDB != -1) {
               if (av.IDDB != GameMidlet.avatar.IDDB || !BoardScr.isStartGame) {
                  av.paintIcon(var1, av.x, av.y, false);
               }
               av.paintName(var1, av.x, av.y);
               BoardScr.paintReady(var1, av.x, av.y - 50, 3, av);
            }
         }
      }

      public final void paintMain (Graphics var1){
         super.paintMain(var1);
         this.paintNamePlayers(var1);

         if (BoardScr.isStartGame) {
            this.paintMyCards(var1);
            this.paintOtherCards(var1);
            this.paintPhomCards(var1);
            this.paintRankInfo(var1);

            int remain = (int) ((long) BoardScr.interval - BoardScr.dieTime);
            if (remain > 0 && !BoardScr.isGameEnd) {
               Canvas.O.drawString(var1, String.valueOf(remain), Canvas.hw, 10, 2);
            }
         }

         if (this.isFinish) {
            this.paintFinishInfo(var1);
         }

         BoardScr.paintChat(var1);
      }

      private void paintMyCards (Graphics var1){
         if (this.myCards == null || this.myCards.size() == 0) {
            return;
         }

         int numCards = this.myCards.size();
         int baseY = Canvas.h - Canvas.hTab;

         for (int i = 0; i < numCards; ++i) {
            Card card = (Card) this.myCards.elementAt(i);
            card.x = this.xShow + i * disCard_;
            card.y = baseY;

            Card showCard;
            if (!card.isShow) {
               showCard = card;
            } else {
               showCard = card;
            }

            int offsetY = 0;
            if (card.f) {
               offsetY = -12;
            }
            if (i == super.selectedCard) {
               offsetY -= 6;
            }

            showCard.y += offsetY;

            if (Canvas.w < 150) {
               showCard.paintSmall(var1, false);
            } else if (i == numCards - 1) {
               showCard.paintFull(var1);
            } else {
               showCard.paintHalf(var1);
            }
         }
      }

      private void paintOtherCards (Graphics var1){
         if (this.otherCards == null) {
            return;
         }

         for (int i = 0; i < BoardScr.numPlayer; ++i) {
            Avatar av;
            if ((av = (Avatar) BoardScr.avatarInfos.elementAt(i)).IDDB == -1) {
               continue;
            }
            if (av.IDDB == GameMidlet.avatar.IDDB) {
               continue;
            }

            Vector cards = this.otherCards[i];
            if (cards == null || cards.size() == 0) {
               continue;
            }

            int px = BoardScr.posAvatar[BoardScr.indexPlayer[i]].x;
            int py = BoardScr.posAvatar[BoardScr.indexPlayer[i]].y;

            int cardCount = cards.size();
            int totalWidth = cardCount * 8;
            int startX = px - totalWidth / 2;

            for (int j = 0; j < cardCount; ++j) {
               Card back = new Card((byte) -1);
               back.x = startX + j * 8;
               back.y = py;
               back.paintSmall(var1, false);
            }
         }
      }

      private void paintPhomCards (Graphics var1) {
         if (this.phomCards == null) {
            return;
         }

         int numPlayer = BoardScr.numPlayer;
         int indexMe = BoardScr.indexOfMe;
         int smallW = BoardScr.wCard;
         int smallH = BoardScr.hcard;
         int gapX = 8 * AvMain.hd;

         for (int i = 0; i < numPlayer; ++i) {
            Avatar av = (Avatar) BoardScr.avatarInfos.elementAt(i);
            if (av.IDDB == -1) {
               continue;
            }

            Vector phom = this.phomCards[i];
            if (phom == null || phom.size() == 0) {
               continue;
            }

            int posIdx = BoardScr.indexPlayer[i];
            int px = BoardScr.posAvatar[posIdx].x;
            int py = BoardScr.posAvatar[posIdx].y;

            int startX;
            int startY;

            int totalWidth = phom.size() * gapX;
            int centerX = px;

            if (posIdx == 0) {
               startX = centerX - totalWidth / 2;
               startY = py + smallH + 8;
            } else if (posIdx == 2) {
               startX = centerX - totalWidth / 2;
               startY = py - smallH - 8;
            } else if (posIdx == 1) {
               startX = px + smallW + 5;
               startY = py - smallH / 2;
            } else {
               startX = px - totalWidth - 5;
               startY = py - smallH / 2;
            }

            for (int j = 0; j < phom.size(); ++j) {
               Card card = (Card) phom.elementAt(j);
               card.x = startX + j * gapX;
               card.y = startY;
               card.paintSmall(var1, false);
            }
         }
      }

      private void paintRankInfo (Graphics var1){
         for (int i = 0; i < BoardScr.numPlayer; ++i) {
            Avatar av;
            if ((av = (Avatar) BoardScr.avatarInfos.elementAt(i)).IDDB == -1) {
               continue;
            }
            if (this.moneyPlayers[i] == 0) {
               continue;
            }

            int px = BoardScr.posAvatar[BoardScr.indexPlayer[i]].x;
            int py = BoardScr.posAvatar[BoardScr.indexPlayer[i]].y - hcard_ - 15;

            int color = this.moneyPlayers[i] > 0 ? 0x00FF00 : 0xFF0000;
            String sign = this.moneyPlayers[i] > 0 ? "+" : "";
            Canvas.smallFontYellow.drawString(var1, sign + this.moneyPlayers[i] + " " + T.getMoney(), px, py, 1);
         }
      }

      private void paintFinishInfo (Graphics var1){
         Avatar winner = BoardScr.getAvatarByID(this.winnerId);
         if (winner != null) {
            String winText = winner.name + " " + T.win;
            if (this.winnerId == -1) {
               winText = T.draw;
            }
            Canvas.normalFont.drawString(var1, winText, Canvas.hw, Canvas.hh - 50, 1);
         }
      }

   private void setPosCard () {
      if (this.myCards == null || this.myCards.size() == 0) {
         return;
      }

      int numCards = this.myCards.size();
      int totalWidth = numCards * disCard_ + (wCard_ - disCard_);
      this.xShow = (Canvas.w - totalWidth) / 2 + wCard_ / 2;
      if (this.xShow < wCard_ / 2) {
         this.xShow = wCard_ / 2;
      }

      int baseY = Canvas.h - Canvas.hTab;

      for (int i = 0; i < numCards; ++i) {
         Card card = (Card) this.myCards.elementAt(i);
         int offsetY = 0;
         if (card.f) {
            offsetY = -12;
         }
         if (i == super.selectedCard) {
            offsetY -= 6;
         }
         card.yTo = baseY + offsetY;
         card.xTo = this.xShow + i * disCard_;
      }
   }

   public final void start ( byte interval, Vector hand,int whoFirst){
      super.start();
      this.initYShow();
      this.resetCard();
      BoardScr.isStartGame = true;
      BoardScr.isGameEnd = false;
      BoardScr.interval = interval;
      BoardScr.currentTime = System.currentTimeMillis() / 1000L;
      BoardScr.dieTime = 0L;

      this.myCards = hand;
      for (int i = 0; i < this.myCards.size(); ++i) {
         Card card = (Card) this.myCards.elementAt(i);
         card.x = Canvas.hw;
         card.y = Canvas.hh;
         card.isShow = true;
         card.f = false;
      }

      for (int i = 0; i < 4; ++i) {
         this.otherCards[i] = new Vector();
      }

      this.sortMyCards();
      this.setPosCard();
      this.setPosPlaying();
      this.setButtons();
   }

      public final void onMove (int playerIndex, int[] cards){
         if (playerIndex >= 0 && playerIndex < 4 && cards != null && cards.length > 0) {
            for (int i = 0; i < cards.length; ++i) {
               Card card = new Card((byte) cards[i]);
               card.isShow = true;
               this.phomCards[playerIndex].addElement(card);
            }
         }
         BoardScr.showChat(playerIndex, T.haBai);
      }

      public final void onFinish ( int[] money, int[][] hands, int[] userIds, int winId, byte rank){
         this.isFinish = true;
      this.winnerId = winId;
      this.rankOrd = rank;

      for (int i = 0; i < 4; ++i) {
         this.moneyPlayers[i] = money[i];
         if (hands[i] != null && hands[i][0] >= 0 && userIds[i] >= 0) {
            int targetIndex = -1;
            for (int j = 0; j < BoardScr.numPlayer; ++j) {
               Avatar av = (Avatar) BoardScr.avatarInfos.elementAt(j);
               if (av.IDDB != -1 && av.IDDB == userIds[i]) {
                  targetIndex = j;
                  break;
               }
            }

            if (targetIndex == -1) continue;

            for (int j = 0; j < 3 && hands[i][j] >= 0; ++j) {
               Card card = new Card((byte) hands[i][j]);
               if (targetIndex == BoardScr.indexOfMe) {
                  card.isShow = true;
                  if (this.myCards != null) {
                     for (int k = 0; k < this.myCards.size(); ++k) {
                        Card myCard = (Card) this.myCards.elementAt(k);
                        if (myCard.cardID == hands[i][j]) {
                           myCard.isShow = true;
                        }
                     }
                  }
               } else {
                  card.isShow = true;
                  Vector phom = this.phomCards[targetIndex];
                  boolean alreadyInPhom = false;
                  if (phom != null && phom.size() > 0) {
                     for (int k = 0; k < phom.size(); ++k) {
                        if (((Card)phom.elementAt(k)).cardID == hands[i][j]) {
                           alreadyInPhom = true;
                           break;
                        }
                     }
                  }
                  if (!alreadyInPhom && this.otherCards[targetIndex] != null) {
                     this.otherCards[targetIndex].addElement(card);
                  }
               }
            }
         }
      }

      for (int i = 0; i < BoardScr.numPlayer; ++i) {
         Avatar av;
         if ((av = (Avatar) BoardScr.avatarInfos.elementAt(i)).IDDB != -1 && this.moneyPlayers[i] != 0) {
            av.setMoneyNew(av.getMoneyNew() + this.moneyPlayers[i]);
            if (av.IDDB == GameMidlet.avatar.IDDB) {
               GameMidlet.avatar.setMoneyNew(av.getMoneyNew());
            }
            if (this.moneyPlayers[i] > 0) {
               BoardScr.showChat(av.IDDB, T.win);
               // Add money fly animation from loser to winner
               for (int j = 0; j < BoardScr.numPlayer; ++j) {
                  if (this.moneyPlayers[j] < 0) {
                     this.addMoneyFlyEffect(j, i, this.moneyPlayers[i]);
                     break;
                  }
               }
            } else {
               BoardScr.showChat(av.IDDB, T.lose);
            }
         }
      }

         super.center = BoardScr.cmdBack;
         BoardScr.isGameEnd = true;
      }

      private void setButtons () {
         if (GameMidlet.avatar.IDDB != BoardScr.ownerID) {
            super.center = this.cmdHaBai;
            super.right = this.cmdSkip;
         } else {
            super.center = null;
            super.right = null;
         }
      }

      private void sortMyCards () {
         if (this.myCards == null || this.myCards.size() <= 1) {
            return;
         }
         for (int i = 0; i < this.myCards.size() - 1; ++i) {
            for (int j = i + 1; j < this.myCards.size(); ++j) {
               Card ci = (Card) this.myCards.elementAt(i);
               Card cj = (Card) this.myCards.elementAt(j);
               if (ci.cardID > cj.cardID) {
                  this.myCards.setElementAt(cj, i);
                  this.myCards.setElementAt(ci, j);
               }
            }
         }
      }

   public final void doContinue () {
      this.resetCard();
      this.listFireWork.removeAllElements();
      BoardScr.isStartGame = false;
      BoardScr.isGameEnd = false;
      BoardScr.disableReady = false;
      super.doContinue();
   }

   public void addMoneyFlyEffect(int fromIndex, int toIndex, int money) {
      if (money == 0) {
         return;
      }

      Avatar toAvatar = (Avatar) BoardScr.avatarInfos.elementAt(toIndex);
      if (toAvatar == null || toAvatar.IDDB == -1) {
         return;
      }

      Avatar fromAvatar = null;
      if (fromIndex >= 0 && fromIndex < BoardScr.avatarInfos.size()) {
         fromAvatar = (Avatar) BoardScr.avatarInfos.elementAt(fromIndex);
      }
      int fromX;
      int fromY;
      if (fromAvatar != null && fromAvatar.IDDB != -1) {
         fromX = fromAvatar.x;
         fromY = fromAvatar.y;
      } else {
         fromX = Canvas.hw;
         fromY = 50;
      }

      Point p = new Point(fromX, fromY);
      p.distant = (short) Math.abs(money);
      p.color = 0;
      int dx = toAvatar.x - fromX;
      int dy = toAvatar.y - fromY;
      p.g = (byte) CRes.tan(dx, -dy);
      p.catagory = (byte) CRes.rnd(-1, 1);
      p.h = CRes.fixangle(p.g + p.catagory * 90);
      p.xTo = (short) toAvatar.x;
      p.yTo = (short) toAvatar.y;
      p.x += (10 * CRes.cos(p.h)) >> 10;
      p.y += -(10 * CRes.sin(p.h)) >> 10;
      p.dis = (byte) (CRes.rnd(4) + 2);
      p.height = (short) (8 + CRes.rnd(5));
      this.listFireWork.addElement(p);
   }

      public final void doFire () {
         if (this.myCards == null || this.myCards.size() == 0) {
            return;
         }
         CasinoService.gI().move(null);
      }
   }

