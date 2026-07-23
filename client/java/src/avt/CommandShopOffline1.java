package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandShopOffline1 extends Command {
   private MapScr mapScr;
   private final Part part;
   private final byte focusIndex;

   CommandShopOffline1(MapScr var1, String var2, IAction var3, Part var4, byte var5) {
      super(var2, var3);
      this.mapScr = var1;
      this.part = var4;
      this.focusIndex = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.part.paintIcon(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
   }

   public final void update() {
      if (this.focusIndex == PopupShop.focus) {
         PopupShop.resetIsTrans();
         MapScr.setAvatarShop(this.part);
         String var1 = "";
         if (this.part.zOrder == 20) {
            var1 = T.setMaxMoney;
         } else if (this.part.zOrder == 10) {
            var1 = T.pant;
         } else if (this.part.zOrder == 40) {
            var1 = T.eye;
         } else if (this.part.zOrder == 50) {
            var1 = T.hair;
         }

         PopupShop.addStr("Id: " + this.part.IDPart);
         PopupShop.addStr(var1 + AvatarData.getName(this.part));
         PopupShop.addStr(Canvas.getPriceMoney(this.part.price[0], this.part.price[1], true));
         PopupShop.addStr(T.youAreBittenByDog + AvatarData.getLevel(this.part));
         PopupShop.addStr(T.roomName[0] + MapScr.avatarShop.lvMain);
      }

   }
}
