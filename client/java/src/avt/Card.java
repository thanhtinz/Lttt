package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class Card {
   public byte phom;
   public byte cardID;
   public int x;
   public int y;
   public int distant;
   public boolean f;
   public boolean isShow;
   public int[] cardMapping;
   public int cardType;
   public int cardValue;
   public int cardColor;
   public int yTo;
   public int xTo;

   public Card(byte var1, boolean var2) {
      this(var1);
      if (var2) {
         this.cardMapping = new int[]{11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
      }

   }

   public Card(byte var1) {
      this.cardID = var1;
      this.phom = 0;
      this.cardType = this.cardID % 4;
      this.cardValue = this.cardID / 4;
      this.cardColor = this.cardType < 2 ? 0 : 1;
      this.cardMapping = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
   }

   public final void paintHalf(Graphics var1) {
      Canvas.paint.paintHalf(var1, this);
   }

   public final void paintHalfBackFull(Graphics var1) {
      Canvas.paint.paintHalfBackFull(var1, this);
   }

   public final void paintFull(Graphics var1) {
      Canvas.paint.paintFull(var1, this);
   }

   public final void paintSmall(Graphics var1, boolean var2) {
      Canvas.paint.paintSmall(var1, this, var2);
   }

   public final int translate() {
      if (this.x == this.xTo && this.y == this.yTo) {
         return -1;
      } else if (Math.abs((this.xTo - this.x) / 2) <= 1 && Math.abs((this.yTo - this.y) / 2) <= 1) {
         this.x = this.xTo;
         this.y = this.yTo;
         return 0;
      } else {
         if (this.x != this.xTo) {
            this.x += (this.xTo - this.x) / 2;
         }

         if (this.y != this.yTo) {
            this.y += (this.yTo - this.y) / 2;
         }

         return CRes.distance(this.x, this.y, this.xTo, this.yTo) <= this.distant / 5 ? 2 : 1;
      }
   }
}
