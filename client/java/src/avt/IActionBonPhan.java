package avt;

final class IActionBonPhan implements IAction {
   private FarmScr a;
   private final FarmItem b;
   private final int c;

   IActionBonPhan(FarmScr var1, FarmItem var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public final void perform() {
      FarmScr.setAction(this.a, (byte)3, this.b.ID);
      FarmService.gI().doUsingItem(FarmScr.idFarm, this.c, this.b.ID);
   }
}
