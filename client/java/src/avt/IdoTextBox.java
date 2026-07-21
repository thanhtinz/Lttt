package avt;

import main.Canvas;

final class IdoTextBox implements IAction {
   private final int a;
   private final byte b;

   IdoTextBox(GlobalMessageHandler var1, int var2, byte var3) {
      this.a = var2;
      this.b = var3;
   }

   public final void perform() {
      GlobalService.gI().doTextBox(this.a, this.b, Canvas.inputDlg.getText());
      Canvas.endDlg();
   }
}
