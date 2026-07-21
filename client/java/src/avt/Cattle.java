package avt;

public final class Cattle extends Animal {
   public static AvPosition posPigTr;
   public static AvPosition posBucket;
   public static byte numPig = 0;
   public static byte numTileW = 5;
   public static short itemID = -1;

   public Cattle() {
   }

   public Cattle(int var1, byte var2) {
      super(0, 0, var1, var2);
      ++numPig;
   }

   public final void setInit() {
      this.setPos(FarmScr.posBarn.x + 48 + (CRes.rnd((FarmScr.numTileBarn - 2) * 6) << 2), FarmScr.posBarn.y + 24 + (CRes.rnd(12) << 2));
   }

   public final void updatePos() {
      super.posNext = new AvPosition();
      AvPosition var2;
      if (!super.isEat) {
         var2 = new AvPosition(FarmScr.posBarn.x + 12 + (CRes.rnd(FarmScr.numTileBarn * 6) << 2), FarmScr.posBarn.y + 12 + (CRes.rnd(18) << 2));
         super.posNext = var2;
      } else {
         var2 = posPigTr;
         super.posNext = var2;
      }

   }

   public final void updateEat() {
      if (super.hunger && !super.isEat && itemID != -1) {
         super.isEat = true;
      }

   }

   public final void reset() {
      super.reset();
      if (super.isEat && CRes.abs(posPigTr.x - super.x) < 20 && CRes.abs(posPigTr.y - super.y) < 15) {
         super.isEat = false;
         super.hunger = false;
         FarmScr.gI();
         FarmScr.doEat(itemID, super.IDDB);
      }

      super.cycle = 100 + 50 * (super.species - 50);
   }
}
