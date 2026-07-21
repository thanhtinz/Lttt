package avt;

final class IActionExitGame implements IAction {
   IActionExitGame(MapScr var1) {
   }

   public final void perform() {
      MapScr.exitGame();
   }
}
