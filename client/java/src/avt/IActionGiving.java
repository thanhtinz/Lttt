package avt;

final class IActionGiving implements IAction {
   private final APartInfo a;

   IActionGiving(MapScr var1, APartInfo var2) {
      this.a = var2;
   }

   public final void perform() {
      ParkService.gI().doGiftGiving(MapScr.focusP.IDDB, this.a.IDPart, 1);
   }
}
