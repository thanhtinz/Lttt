package avt;

final class IActionXeng implements IAction {
   IActionXeng(BoardListOnScr var1) {
   }

   public final void perform() {
      TransMoneyDlg.gI().init();
   }
}
