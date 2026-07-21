package avt;

final class IActionBuyAnimalLuong implements IAction {
   private final AnimalInfo a;

   IActionBuyAnimalLuong(FarmScr var1, AnimalInfo var2) {
      this.a = var2;
   }

   public final void perform() {
      FarmService.gI().doBuyAnimal(this.a, 2);
   }
}
