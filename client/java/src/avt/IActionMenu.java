package avt;

final class IActionMenu implements IAction {
   private final IAction a;

   IActionMenu(MainMenu var1, IAction var2) {
      this.a = var2;
   }

   public final void perform() {
      this.a.perform();
   }
}
