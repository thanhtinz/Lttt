package avt;

final class IActionBuyItem1 implements IAction {
   private HouseScr a;
   private final MapItemType b;
   private final String c;

   IActionBuyItem1(HouseScr var1, MapItemType var2, String var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public final void perform() {
      MapItem var1 = new MapItem(2, HouseScr.getX(this.a) * 24, HouseScr.getY(this.a) * 24, 1, this.b.idType);
      AvatarService.gI().doBuyItemHouse(var1);
      HouseScr.doSelectedItem(this.a, this.c);
   }
}
