package avt;

import javax.microedition.lcdui.Graphics;

final class CommandGiftDef extends Command {
   private int index;
   private int offset;
   private ItemEffectInfo giftInfo;

   public CommandGiftDef(MapScr var1, String var2, IActionGiftDef var3, int var4, ItemEffectInfo var5, int var6) {
      super(var2, var3);
      this.index = var4;
      this.giftInfo = var5;
      this.offset = var6;
   }

   public final void update() {
      if (PopupShop.isTransFocus && PopupShop.focus - this.offset == this.index) {
         PopupShop.resetIsTrans();
         PopupShop.addStr("Id: " + this.giftInfo.IDIcon);
         PopupShop.addStr(T.nameStr + this.giftInfo.name);
         PopupShop.addStr(T.priceStr + this.giftInfo.money + (this.giftInfo.typeMoney == 0 ? T.xu : T.gold));
      }

   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.giftInfo.IDIcon, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
   }
}
