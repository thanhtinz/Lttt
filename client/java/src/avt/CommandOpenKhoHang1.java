package avt;

import javax.microedition.lcdui.Graphics;

final class CommandOpenKhoHang1 extends Command {
   private final Item item;
   private final int focusIndex;

   CommandOpenKhoHang1(FarmScr var1, String var2, int var3, int var4, Item var5, int var6) {
      super(var2, 12, var4);
      this.item = var5;
      this.focusIndex = var6;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmData.getTreeByID(this.item.ID).paint(var1, 7, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }

   public final void update() {
      if (this.focusIndex == PopupShop.focus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.item.ID);
         PopupShop.addStr(this.item.name);
         PopupShop.addStr(T.number + this.item.number);
      }

   }
}
