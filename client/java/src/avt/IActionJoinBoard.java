package avt;

import main.Canvas;

final class IActionJoinBoard implements IAction {
   private BoardListOnScr a;

   IActionJoinBoard(BoardListOnScr var1) {
      this.a = var1;
   }

   public final void perform() {
      BoardInfo var1 = (BoardInfo)this.a.boardList.elementAt(this.a.selected_);
      CasinoService.gI().joinBoard(this.a.roomID, var1.boardID, Canvas.inputDlg.getText());
      Canvas.endDlg();
   }
}
