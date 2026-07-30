package avt;

public final class Chicken extends AnimalDan {
   public static int numChicken = 0;
   public static AvPosition s;

   public Chicken(int var1, byte var2, byte var3) {
      super(0, 0, var1, var2);
      super.captainID = 0;
      super.indexFood = 0;
      ++numChicken;
   }

   public final void setInit() {
      super.posNext = new AvPosition();
      if (super.captainID == super.IDDB) {
         super.x = super.xCur = super.posNext.x = (FarmScr.numTileBarn + 3) * 24 + randomX();
         super.y = super.yCur = super.posNext.y = 72 + (CRes.rnd(24) << 2);
      } else {
         this.updatePos();
         if (!LoadMap.isTrans(super.x, super.y)) {
            AvPosition var2 = new AvPosition((FarmScr.numTileBarn + 3) * 24 + randomX(), 72 + (CRes.rnd(24) << 2));
            super.posNext = var2;
         }

         super.x = super.xCur = super.posNext.x;
         super.y = super.yCur = super.posNext.y;
      }

   }

   private static int randomX() {
      return CRes.rnd((LoadMap.wMap - FarmScr.numTilePond - FarmScr.numTileBarn - 5) * 6) << 2;
   }

   public final void setFollowPos(AvPosition var1) {
      AvPosition var2 = new AvPosition(var1.x - 48 + randomX(), var1.y - 48 + (CRes.rnd(24) << 2));
      super.posNext = var2;
   }

   public final void setPos() {
      super.setPos();
   }
}
