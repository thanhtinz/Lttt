package avt;

import main.Canvas;

final class CommandUsingPart2 implements IAction {
   private final int actionType;
   private final int chestId;
   private final SeriPart seriPart;

   CommandUsingPart2(CommandUsingPart1 var1, int var2, int var3, SeriPart var4) {
      this.actionType = var2;
      this.chestId = var3;
      this.seriPart = var4;
   }

   public final void perform() {
      if (this.actionType == 2) {
         GlobalService.gI().doTransChestPart(1, this.chestId, this.seriPart.idPart);
      } else if (this.actionType == 3) {
         GlobalService.gI().doTransChestPart(0, this.chestId, this.seriPart.idPart);
      } else {
         GlobalService.gI().doUsingItem(this.seriPart.idPart, (byte)this.actionType);
      }

      Canvas.startWaitDlg();
   }
}
