package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandBuyAnimalCuaHang extends Command {
   private final AnimalInfo animalInfo;
   private final int focusIndex;

   CommandBuyAnimalCuaHang(FarmScr var1, String var2, int var3, int var4, AnimalInfo var5, int var6) {
      super(var2, 8, var4);
      this.animalInfo = var5;
      this.focusIndex = var6;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.animalInfo.iconID, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }

   public final void update() {
      if (this.focusIndex == PopupShop.focus - FarmData.treeInfo.length && PopupShop.isTransFocus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.animalInfo.iconID);
         PopupShop.addStr(this.animalInfo.name + "(" + this.animalInfo.harvestTime + T.h + ")");
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(this.animalInfo.price[0], this.animalInfo.price[1], false));
         PopupShop.addStr(this.animalInfo.des);
         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
