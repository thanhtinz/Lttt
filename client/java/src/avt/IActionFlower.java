package avt;

final class IActionFlower implements IAction {
   private final int a;

   IActionFlower(GlobalMessageHandler var1, int var2) {
      this.a = var2;
   }

   public final void perform() {
      GlobalService.gI().doFlowerLoveSelected(this.a);
   }
}
