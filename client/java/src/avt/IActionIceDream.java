package avt;

final class IActionIceDream implements IAction {
   private MapScr a;
   private final Item b;

   IActionIceDream(MapScr var1, Item var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      this.a.doBuyIceDream(this.b);
   }
}
