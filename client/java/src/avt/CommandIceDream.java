package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

final class CommandIceDream extends Command {
   private final Item item;
   private final int ii;

   CommandIceDream(MapScr var1, String var2, IAction var3, Item var4, int var5) {
      super(var2, var3);
      this.item = var4;
      this.ii = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.listImgInfo[this.item.idIcon].paintPart(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }

   public final void update() {
      if (this.ii == PopupShop.focus || PopupShop.k) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.item.idIcon);
         PopupShop.addStr(this.item.name);
         PopupShop.addStr(T.priceStr + this.item.price[0] + T.dola);
         PopupShop.addStr(T.have + Canvas.getMoneys(GameMidlet.avatar.money[0]) + T.dola);
      }

   }
}
