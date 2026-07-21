package avt;

import main.Canvas;

final class IActionSellItem implements IAction {
   private final MapItem a;

   IActionSellItem(HouseScr var1, MapItem var2) {
      this.a = var2;
   }

   public final void perform() {
      HomeMsgHandler.onHandler();
      AvatarService.gI().dodelItem(this.a);
      Canvas.startWaitDlg();
   }
}
