package avt;

final class IActionCattleFeeding implements IAction {
   private final byte a;
   private final Item b;

   IActionCattleFeeding(FarmScr var1, byte var2, Item var3) {
      this.a = var2;
      this.b = var3;
   }

   public final void perform() {
      if (this.a == 2) {
         Cattle.itemID = this.b.ID;
      } else {
         Dog.itemID = this.b.ID;
      }

   }
}
