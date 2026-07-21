package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandGoVatPham extends Command {
   private final FarmItem item;
   private final int ii;

   CommandGoVatPham(FarmScr var1, String var2, int var3, int var4, FarmItem var5, int var6) {
      super(var2, 9, var4);
      this.item = var5;
      this.ii = var6;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.item.paint(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
   }

   public final void update() {
      if (this.ii == PopupShop.focus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.item.ID);
         PopupShop.addStr(this.item.des);
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(this.item.priceXu, this.item.priceLuong, false));
      }

   }
}
