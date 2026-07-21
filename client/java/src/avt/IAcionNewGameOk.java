package avt;

final class IAcionNewGameOk implements IAction {
   IAcionNewGameOk(LoginScr var1) {
   }

   public final void perform() {
      ServerListScr.gI().switchToMe();
   }
}
