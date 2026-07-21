package avt;

import main.Canvas;

final class CommandUsingPart2 implements IAction {
   private final int a;
   private final int b;
   private final SeriPart c;

   CommandUsingPart2(CommandUsingPart1 var1, int var2, int var3, SeriPart var4) {
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   public final void perform() {
      if (this.a == 2) {
         GlobalService.gI().doTransChestPart(1, this.b, this.c.idPart);
      } else if (this.a == 3) {
         GlobalService.gI().doTransChestPart(0, this.b, this.c.idPart);
      } else {
         GlobalService.gI().doUsingItem(this.c.idPart, (byte)this.a);
      }

      Canvas.startWaitDlg();
   }
}
