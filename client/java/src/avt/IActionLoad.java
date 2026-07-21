package avt;

import main.Canvas;

final class IActionLoad implements IAction {
   private MoneyScr a;
   private final String b;
   private final TField[] c;

   IActionLoad(MoneyScr var1, String var2, TField[] var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public final void perform() {
      MoneyScr var10000 = this.a;
      String var10001 = this.b;
      String var10002 = this.c[0].getText();
      String var4 = this.c[1].getText();
      if (var10002.equals("")) {
         Canvas.startOKDlg(T.enterCard[0]);
      } else if (var4.equals("")) {
         Canvas.startOKDlg(T.enterCard[1]);
      } else {
         GlobalService.gI().b(var10001, var10002, var4);
         var10000.commandTab(var10000.left.indexMenu, var10000.left.subIndex);
         Canvas.startWaitDlg();
      }

   }
}
