package avt;

final class IActionGiving1 implements IAction {
   private final APartInfo a;

   IActionGiving1(MapScr var1, APartInfo var2) {
      this.a = var2;
   }

   public final void perform() {
      ParkService.gI().doGiftGiving(MapScr.focusP.IDDB, this.a.IDPart, 2);
   }
}
