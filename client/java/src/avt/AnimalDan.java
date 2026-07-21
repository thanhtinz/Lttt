package avt;

public class AnimalDan extends Animal {
   public int captainID = 0;
   public byte indexFood;

   public AnimalDan(int var1, int var2, int var3, byte var4) {
      super(0, 0, var3, var4);
   }

   public void update() {
      super.update();
   }

   public final void setAngleAndDis() {
      super.setAngleAndDis();
      if (!super.isEat && super.IDDB == this.captainID && super.distant > 150) {
         super.distant = 150;
      }

   }

   public void setInit() {
   }

   public Point getPosEat() {
      return (Point)FarmScr.listFood[this.indexFood].elementAt(CRes.rnd(FarmScr.listFood[this.indexFood].size()));
   }

   public final void updatePos() {
      if (!super.isEat && this.captainID == super.IDDB) {
         this.setPos();
      } else {
         AvPosition var1 = new AvPosition();
         if (super.isEat && FarmScr.listFood[this.indexFood].size() > 0) {
            Point var5;
            if ((var5 = this.getPosEat()) != null) {
               var1.x = var5.x;
               var1.y = var5.y;
               super.v = 2;
               super.posNext = var1;
            } else {
               this.setPos();
            }
         } else {
            int var2 = LoadMap.playerLists.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               Base var4;
               if ((var4 = (Base)LoadMap.playerLists.elementAt(var3)) instanceof AnimalDan && var4.IDDB == this.captainID) {
                  var1 = new AvPosition(var4.x, var4.y);
                  break;
               }
            }

            if (this.indexFood != 1 && !LoadMap.isTrans(super.x, super.y)) {
               this.setPos();
            } else {
               this.setFollowPos(var1);
            }
         }
      }

   }

   public void setFollowPos(AvPosition var1) {
   }

   public final void reset() {
      int var1 = FarmScr.listFood[this.indexFood].size();
      if (super.hunger && super.isEat && var1 > 0) {
         for(int var2 = 0; var2 < var1; ++var2) {
            Point var3;
            if (CRes.abs((var3 = (Point)FarmScr.listFood[this.indexFood].elementAt(var2)).x - super.x) <= 2 && CRes.abs(var3.y - super.y) <= 2) {
               FarmScr.listFood[this.indexFood].removeElement(var3);
               LoadMap.dynamicLists.removeElement(var3);
               super.hunger = false;
               super.isEat = false;
               super.v = 1;
               FarmScr.gI();
               FarmScr.doEat(var3.itemID, super.IDDB);
               break;
            }
         }
      }

      super.reset();
      super.cycle = 100 - (this.captainID != super.IDDB ? this.indexFood * CRes.rnd(70) : 0);
   }

   public final void updateEat() {
      if (FarmScr.listFood[this.indexFood].size() == 0) {
         super.isEat = false;
      } else if (super.hunger && !super.isEat) {
         super.isEat = true;
      }

   }
}
