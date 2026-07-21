package avt;

final class class_jc implements IAction {
   class_jc(MainMenu var1) {
   }

   public final void perform() {
      ParkService.gI().doRequestWedding(MapScr.roomID, MapScr.boardID);
   }
}
