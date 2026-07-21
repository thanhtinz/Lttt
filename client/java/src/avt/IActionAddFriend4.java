package avt;

import main.Canvas;

final class IActionAddFriend4 implements IAction {
   private final Avatar a;

   IActionAddFriend4(MapScr var1, Avatar var2) {
      this.a = var2;
   }

   public final void perform() {
      UNK var1 = MessageScr.gI().b(MessageScr.gI().b);
      if (ListScr.friendL != null) {
         ListScr.gI();
         ListScr.removeList();
      }

      ParkService.gI().doAddFriend(this.a.IDDB, true);
      MessageScr.gI().a(var1);
      Canvas.startOKDlg(T.addFriend + T.with + this.a.name + ".");
   }
}
