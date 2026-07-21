package avt;

final class IActionExchange implements IAction {
   private final StringObj a;

   IActionExchange(MapScr var1, StringObj var2) {
      this.a = var2;
   }

   public final void perform() {
      if (MapScr.focusP != null) {
         GlobalService.gI().doRequestCmdRotate(this.a.anthor, MapScr.focusP.IDDB);
      } else {
         GlobalService.gI().doRequestCmdRotate(this.a.anthor, -1);
      }

   }
}
