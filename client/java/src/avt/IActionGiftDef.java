package avt;

final class IActionGiftDef implements IAction {
   private int a;
   private short b;
   private MapScr c;

   public IActionGiftDef(MapScr var1, int var2, short var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   public final void perform() {
      if (this.a != 0 || LoadMap.weather == -1) {
         MapScr.doGivingDefferent(this.b);
      }

      PopupShop.gI().close();
   }
}
