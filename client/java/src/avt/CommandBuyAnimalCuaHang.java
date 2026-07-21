package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandBuyAnimalCuaHang extends Command {
   private final AnimalInfo f;
   private final int g;

   CommandBuyAnimalCuaHang(FarmScr var1, String var2, int var3, int var4, AnimalInfo var5, int var6) {
      super(var2, 8, var4);
      this.f = var5;
      this.g = var6;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.f.iconID, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }

   public final void update() {
      if (this.g == PopupShop.focus - FarmData.treeInfo.length && PopupShop.isTransFocus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.f.iconID);
         PopupShop.addStr(this.f.name + "(" + this.f.harvestTime + T.h + ")");
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(this.f.price[0], this.f.price[1], false));
         PopupShop.addStr(this.f.des);
         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
