package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandBuyItemCuaHang extends Command {
   private final int treeIndex;

   CommandBuyItemCuaHang(FarmScr var1, String var2, int var3, int var4, int var5) {
      super(var2, 7, var4);
      this.treeIndex = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.treeInfo[this.treeIndex].paint(var1, 7, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }

   public final void update() {
      if (this.treeIndex == PopupShop.focus && PopupShop.isTransFocus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.treeIndex);
         PopupShop.addStr(FarmData.treeInfo[this.treeIndex].name1 + "(" + FarmData.treeInfo[this.treeIndex].harvestTime + T.h + ")");
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(FarmData.treeInfo[this.treeIndex].priceSeed[0], FarmData.treeInfo[this.treeIndex].priceSeed[1], false));
         PopupShop.addStr(T.roomName[2] + ": " + FarmData.treeInfo[this.treeIndex].lv);
         if (FarmData.treeInfo[this.treeIndex].isDynamic) {
            FarmItem var1 = FarmScr.getFarmItem(FarmData.treeInfo[this.treeIndex].productID);
            PopupShop.addStr(T.doo + ": " + var1.des);
         }

         PopupShop.addStr(T.detail + ": " + Canvas.getMoneys(FarmData.treeInfo[this.treeIndex].numProduct));
         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
