package avt;

final class IActionSet11 implements IAction {
   private FarmScr a;
   private final CellFarm b;

   IActionSet11(FarmScr var1, CellFarm var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      FarmScr.focusCell.x = this.b.x / LoadMap.w;
      FarmScr.focusCell.y = this.b.y / LoadMap.w;
      FarmScr.setAction(this.a, (byte)1, FarmScr.idItemUsing);
   }
}
