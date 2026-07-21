package avt;

final class class_ig implements IAction {
   private MapScr a;

   class_ig(MapScr var1) {
      this.a = var1;
   }

   public final void perform() {
      MapScr.doEvent();
   }
}
