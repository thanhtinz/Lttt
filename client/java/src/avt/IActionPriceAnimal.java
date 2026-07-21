package avt;

final class IActionPriceAnimal implements IAction {
   private final byte a;

   IActionPriceAnimal(FarmScr var1, byte var2) {
      this.a = var2;
   }

   public final void perform() {
      FarmService.gI().doSellAnimal(FarmScr.idFarm, this.a);
   }
}
