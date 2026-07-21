package avt;

final class IActionBuyAnimalXu implements IAction {
   private final AnimalInfo a;

   IActionBuyAnimalXu(FarmScr var1, AnimalInfo var2) {
      this.a = var2;
   }

   public final void perform() {
      FarmService.gI().doBuyAnimal(this.a, 1);
   }
}
