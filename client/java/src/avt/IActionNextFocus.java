package avt;

final class IActionNextFocus implements IAction {
   IActionNextFocus(LoadMap var1) {
   }

   public final void perform() {
      LoadMap.focusNextObject();
   }
}
