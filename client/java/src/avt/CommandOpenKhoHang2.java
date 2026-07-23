package avt;

import javax.microedition.lcdui.Graphics;

final class CommandOpenKhoHang2 extends Command {
   private Item item;
   private final int focusIndex;

   CommandOpenKhoHang2(FarmScr var1, String var2, int var3, int var4, int var5) {
      super(var2, 13, var4);
      this.focusIndex = var5;
      this.item = (Item)FarmScr.listItemFarm.elementAt(var5);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmScr.getFarmItem(this.item.ID).paint(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
   }

   public final void update() {
      if (PopupShop.isTransFocus && this.focusIndex == PopupShop.focus - FarmScr.getItemSeed().size()) {
         PopupShop.resetIsTrans();
         FarmItem var1 = FarmScr.getFarmItem(this.item.ID);
         PopupShop.addStr("Id: " + this.item.ID);
         PopupShop.addStr(var1.des);
         int var2 = this.item.number;
         if (var1.type == 4) {
            var2 -= FarmScr.listFood[1].size();
         } else if (var1.type == 1) {
            var2 -= FarmScr.listFood[0].size();
         }

         PopupShop.addStr(T.number + var2);
      }

   }
}
