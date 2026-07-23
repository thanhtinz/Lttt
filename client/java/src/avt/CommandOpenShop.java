package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

final class CommandOpenShop extends Command {
   private Part part;
   private short idPart;
   private int focusIndex;
   private int index;
   private MapScr mapScr;

   public CommandOpenShop(MapScr var1, String var2, IActionOpenShop var3, Part var4, short var5, int var6, int var7, int var8) {
      super(var2, var3);
      this.mapScr = var1;
      this.part = var4;
      this.idPart = var5;
      this.focusIndex = var6;
      this.index = var7;
   }

   public final void update() {
      if (PopupShop.isTransFocus && this.focusIndex == PopupShop.focus) {
         Part var1 = this.part;
         if (this.part.IDPart == -1) {
            var1 = AvatarData.getPart(this.idPart);
         }

         if (var1.IDPart != -1) {
            MapScr.setAvatarShop(var1);
            PopupShop.resetIsTrans();
            PopupShop.addStr("Id: " + var1.IDPart);
            PopupShop.addStr(var1.name);
            if (this.index == -1) {
               PopupShop.addStr(Canvas.getPriceMoney(var1.price[0], var1.price[1], false));
            }

            if (var1.follow == -1) {
               PopupShop.addStr(T.roomName[0] + ((APartInfo)var1).level);
            }

            PopupShop.addStr(T.moneyStr + GameMidlet.avatar.strMoney);
            if (MapScr.isNewVersion) {
               PopupShop.addStr(T.tkNew + GameMidlet.avatar.money[3] + " " + T.getMoney());
            }
         }
      }

   }

   public final void paint(Graphics var1, int var2, int var3) {
      Part var4 = this.part;
      if (this.part.IDPart == -1) {
         var4 = AvatarData.getPart(this.idPart);
      }

      if (var4.IDPart != -1) {
         var4.paint(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
      }

   }
}
