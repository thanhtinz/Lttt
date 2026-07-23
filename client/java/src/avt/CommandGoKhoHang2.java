package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandGoKhoHang2 extends Command {
   private final FarmItem fItem;
   private final int focusIndex;
   private final Item item;

   CommandGoKhoHang2(FarmScr var1, String var2, int var3, int var4, FarmItem var5, int var6, Item var7) {
      super(var2, 11, var4);
      this.fItem = var5;
      this.focusIndex = var6;
      this.item = var7;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.fItem.paint(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
   }

   public final void update() {
      if (PopupShop.isTransFocus && this.focusIndex == PopupShop.focus - FarmScr.itemProduct.size()) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.fItem.ID);
         PopupShop.addStr(this.fItem.des);
         PopupShop.addStr(T.number + this.item.number);
         if (this.fItem.priceLuong > 0) {
            PopupShop.addStr(T.inCome + Canvas.getMoneys(this.item.number * this.fItem.priceLuong) + T.dola);
         } else if (this.fItem.priceXu > 0) {
            PopupShop.addStr(T.inCome + Canvas.getMoneys(this.item.number * this.fItem.priceXu) + T.dola);
         }

         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
