package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandShopOffline1 extends Command {
   private MapScr f;
   private final Part g;
   private final byte h;

   CommandShopOffline1(MapScr var1, String var2, IAction var3, Part var4, byte var5) {
      super(var2, var3);
      this.f = var1;
      this.g = var4;
      this.h = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.g.paintIcon(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
   }

   public final void update() {
      if (this.h == PopupShop.focus) {
         PopupShop.resetIsTrans();
         MapScr.setAvatarShop(this.g);
         String var1 = "";
         if (this.g.zOrder == 20) {
            var1 = T.setMaxMoney;
         } else if (this.g.zOrder == 10) {
            var1 = T.pant;
         } else if (this.g.zOrder == 40) {
            var1 = T.eye;
         } else if (this.g.zOrder == 50) {
            var1 = T.hair;
         }

         PopupShop.addStr("Id: " + this.g.IDPart);
         PopupShop.addStr(var1 + AvatarData.getName(this.g));
         PopupShop.addStr(Canvas.getPriceMoney(this.g.price[0], this.g.price[1], true));
         PopupShop.addStr(T.youAreBittenByDog + AvatarData.getLevel(this.g));
         PopupShop.addStr(T.roomName[0] + MapScr.avatarShop.lvMain);
      }

   }
}
