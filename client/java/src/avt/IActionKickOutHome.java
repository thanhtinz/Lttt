package avt;

final class IActionKickOutHome implements IAction {
   IActionKickOutHome(ParkMsgHandler var1) {
   }

   public final void perform() {
      ParkService.gI().doJoinPark(21, 0);
   }
}
