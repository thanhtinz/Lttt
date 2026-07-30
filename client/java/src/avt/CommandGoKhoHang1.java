package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandGoKhoHang1 extends Command {
   private int focusIndex = 0;
   private Item item;

   public CommandGoKhoHang1(FarmScr var1, String var2, IAction var3, int var4, Item var5) {
      super(var2, var3);
      this.focusIndex = var4;
      this.item = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      if (this.item.ID < 50) {
         FarmData.getTreeByID(this.item.ID).paint(var1, 7, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
      } else {
         int var10002 = var2 + PopupShop.h / 2;
         int var10003 = var3 + PopupShop.h / 2;
         AvatarData.paintImg(var1, FarmData.getAnimalByID(this.item.ID).iconProduct, var10002, var10003, 3);
      }

   }

   public final void update() {
      if (this.focusIndex == PopupShop.focus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.item.ID);
         PopupShop.addStr(this.item.name);
         PopupShop.addStr(T.number + this.item.number);
         PopupShop.addStr(T.inCome + Canvas.getMoneys(this.item.price[0] * this.item.number) + T.dola);
         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
