package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandGoKhoHang1 extends Command {
   private int f = 0;
   private Item g;

   public CommandGoKhoHang1(FarmScr var1, String var2, IAction var3, int var4, Item var5) {
      super(var2, var3);
      this.f = var4;
      this.g = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      if (this.g.ID < 50) {
         FarmData.getTreeByID(this.g.ID).a(var1, 7, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
      } else {
         int var10002 = var2 + PopupShop.h / 2;
         int var10003 = var3 + PopupShop.h / 2;
         AvatarData.paintImg(var1, FarmData.getAnimalByID(this.g.ID).iconProduct, var10002, var10003, 3);
      }

   }

   public final void update() {
      if (this.f == PopupShop.focus) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.g.ID);
         PopupShop.addStr(this.g.name);
         PopupShop.addStr(T.number + this.g.number);
         PopupShop.addStr(T.inCome + Canvas.getMoneys(this.g.price[0] * this.g.number) + T.dola);
         PopupShop.addStr(MapScr.strTkFarm());
      }

   }
}
