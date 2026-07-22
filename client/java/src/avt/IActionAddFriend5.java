package avt;

final class IActionAddFriend5 implements IAction {
   private final Avatar a;

   IActionAddFriend5(MapScr var1, Avatar var2) {
      this.a = var2;
   }

   public final void perform() {
      UNK var1 = MessageScr.gI().getTab(MessageScr.gI().currentTab);
      ParkService.gI().doAddFriend(this.a.IDDB, false);
      MessageScr.gI().removeTab(var1);
   }
}
