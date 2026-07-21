package avt;

final class class_ac implements IAction {
   private final int a;
   private final int b;
   private final int c;

   class_ac(MapScr var1, int var2, int var3, int var4) {
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   public final void perform() {
      ParkService.gI().doCustomPopup(this.a, this.b, this.c);
   }
}
