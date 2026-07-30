package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandShopOffline extends Command {
   private MapScr mapScr;
   private final Part part;
   private final byte focusIndex;

   CommandShopOffline(MapScr var1, String var2, IAction var3, Part var4, byte var5) {
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
         MapScr.setAvatarShop(this.part);
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.part.IDPart);
         PopupShop.addStr(AvatarData.getName(this.part));
         PopupShop.addStr(T.priceStr + Canvas.getPriceMoney(this.part.price[0], this.part.price[1], false));
         PopupShop.addStr(T.youAreBittenByDog + AvatarData.getLevel(this.part));
         PopupShop.addStr(T.roomName[0] + MapScr.avatarShop.lvMain);
      }

   }
}
