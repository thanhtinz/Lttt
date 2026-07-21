package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandShopOffline extends Command {
   private MapScr f;
   private final Part g;
   private final byte h;

   CommandShopOffline(MapScr var1, String var2, IAction var3, Part var4, byte var5) {
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
         MapScr.setAvatarShop(this.g);
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.g.IDPart);
         PopupShop.addStr(AvatarData.getName(this.g));
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(this.g.price[0], this.g.price[1], false));
         PopupShop.addStr(T.youAreBittenByDog + AvatarData.getLevel(this.g));
         PopupShop.addStr(T.roomName[0] + MapScr.avatarShop.lvMain);
      }

   }
}
