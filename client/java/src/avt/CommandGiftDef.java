package avt;

import javax.microedition.lcdui.Graphics;

final class CommandGiftDef extends Command {
   private int f;
   private int g;
   private ItemEffectInfo h;

   public CommandGiftDef(MapScr var1, String var2, IActionGiftDef var3, int var4, ItemEffectInfo var5, int var6) {
      super(var2, var3);
      this.f = var4;
      this.h = var5;
      this.g = var6;
   }

   public final void update() {
      if (PopupShop.isTransFocus && PopupShop.focus - this.g == this.f) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.h.IDIcon);
         PopupShop.addStr(T.nameStr + this.h.name);
         PopupShop.addStr(T.priceStr + this.h.money + (this.h.typeMoney == 0 ? T.xu : T.gold));
      }

   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.h.IDIcon, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }
}
