package avt;

final class IActionMenuOption implements IAction {
   private int a;
   private byte b;
   private int c;

   public IActionMenuOption(GlobalLogicHandler var1, int var2, int var3, byte var4) {
      this.c = var2;
      this.a = var3;
      this.b = var4;
   }

   public final void perform() {
      System.out.println("DEBUG NPC_MENU_CLICK: doMenuOption args int=" + this.a + " byte=" + this.b + " idx=" + this.c);
      ClientUtilities.rememberLastNpcMenuAction(this.a, this.b, this.c);
      if (!ListScr.gI().setList(this.a + "-" + this.b + "-" + this.c)) {
         GlobalService.gI().doMenuOption(this.a, this.b, this.c);
      }

   }
}
