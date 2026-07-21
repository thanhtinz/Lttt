package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

final class CommandOpenShop extends Command {
   private Part f;
   private short g;
   private int h;
   private int i;
   private MapScr j;

   public CommandOpenShop(MapScr var1, String var2, IActionOpenShop var3, Part var4, short var5, int var6, int var7, int var8) {
      super(var2, var3);
      this.j = var1;
      this.f = var4;
      this.g = var5;
      this.h = var6;
      this.i = var7;
   }

   public final void update() {
      if (PopupShop.isTransFocus && this.h == PopupShop.focus) {
         Part var1 = this.f;
         if (this.f.IDPart == -1) {
            var1 = AvatarData.getPart(this.g);
         }

         if (var1.IDPart != -1) {
            MapScr.setAvatarShop(var1);
            PopupShop.resetIsTrans();
            PopupShop.addStr("Id: " + var1.IDPart);
            PopupShop.addStr(var1.name);
            if (this.i == -1) {
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
      Part var4 = this.f;
      if (this.f.IDPart == -1) {
         var4 = AvatarData.getPart(this.g);
      }

      if (var4.IDPart != -1) {
         var4.paint(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
      }

   }
}
