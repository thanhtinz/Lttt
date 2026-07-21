package avt;

import main.Canvas;

final class IActionPass implements IAction {
   private BoardListOnScr a;

   IActionPass(BoardListOnScr var1) {
      this.a = var1;
   }

   public final void perform() {
      CasinoService.gI().joinBoard(this.a.roomID, (byte)this.a.j, Canvas.inputDlg.getText());
      Canvas.endDlg();
   }
}
