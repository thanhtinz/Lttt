package avt;

import main.Canvas;

final class IActionTransXeng implements IAction {
   private TransMoneyDlg a;

   IActionTransXeng(TransMoneyDlg var1) {
      this.a = var1;
   }

   public final void perform() {
      GlobalService.gI().transXeng(TransMoneyDlg.getMoney(this.a)[TransMoneyDlg.getFocus(this.a)]);
      Canvas.startWaitDlg();
   }
}
