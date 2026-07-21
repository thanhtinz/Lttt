package avt;

final class IActionNo implements IAction {
   private HouseScr a;

   IActionNo(HouseScr var1) {
      this.a = var1;
   }

   public final void perform() {
      HouseScr.f(this.a);
   }
}
