package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandBuyItemCuaHang extends Command {
   private final int f;

   CommandBuyItemCuaHang(FarmScr var1, String var2, int var3, int var4, int var5) {
      super(var2, 7, var4);
      this.f = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.treeInfo[this.f].paint(var1, 7, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }

   public final void update() {
      if (this.f == PopupShop.focus && PopupShop.isTransFocus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.f);
         PopupShop.addStr(FarmData.treeInfo[this.f].name1 + "(" + FarmData.treeInfo[this.f].harvestTime + T.h + ")");
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(FarmData.treeInfo[this.f].priceSeed[0], FarmData.treeInfo[this.f].priceSeed[1], false));
         PopupShop.addStr(T.roomName[2] + ": " + FarmData.treeInfo[this.f].lv);
         if (FarmData.treeInfo[this.f].isDynamic) {
            FarmItem var1 = FarmScr.getFarmItem(FarmData.treeInfo[this.f].productID);
            PopupShop.addStr(T.doo + ": " + var1.des);
         }

         PopupShop.addStr(T.detail + ": " + Canvas.getMoneys(FarmData.treeInfo[this.f].numProduct));
         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
