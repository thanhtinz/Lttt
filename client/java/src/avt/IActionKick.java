package avt;

import main.Canvas;

final class IActionKick implements IAction {
   IActionKick(CasinoMsgHandler var1) {
   }

   public final void perform() {
      Canvas.startWaitDlg();
      CasinoService.gI().requestBoardList(BoardScr.roomID);
   }
}
