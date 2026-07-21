package avt;

import main.Canvas;

final class IActionToGo implements IAction {
   private BoardListOnScr a;

   IActionToGo(BoardListOnScr var1) {
      this.a = var1;
   }

   public final void perform() {
      try {
         this.a.j = Integer.parseInt(Canvas.inputDlg.getText());
      } catch (Exception var2) {
         return;
      }

      this.a.doAskForPass();
   }
}
