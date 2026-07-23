package avt;

import javax.microedition.lcdui.Graphics;

final class CommandUsingPart extends Command {
   private final SeriPart seriPart;
   private final int focusIndex;
   private final int type;

   CommandUsingPart(MapScr var1, String var2, IAction var3, SeriPart var4, int var5, int var6) {
      super(var2, var3);
      this.seriPart = var4;
      this.focusIndex = var5;
      this.type = var6;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.getPart(this.seriPart.idPart).paintIcon(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
      PaintPopup.fill(var2 + 3, var3 + PopupShop.h - 3 * AvMain.hd, PopupShop.h - 5, 2 * AvMain.hd, 1, var1);
      PaintPopup.fill(var2 + 3, var3 + PopupShop.h - 3 * AvMain.hd, PopupShop.h - 5 - this.seriPart.time * (PopupShop.h - 5) / 100, 2 * AvMain.hd, 11907085, var1);
   }

   public final void update() {
      if (PopupShop.isTransFocus && this.focusIndex == PopupShop.focus) {
         Part var1 = AvatarData.getPart(this.seriPart.idPart);
         PopupShop.resetIsTrans();
         PopupShop.addStr(T.doBen + (100 - this.seriPart.time) + "%");
         PopupShop.addStr("Id: " + var1.IDPart);
         String var2 = "";
         if (var1.zOrder == 20) {
            var2 = T.setMaxMoney;
         } else if (var1.zOrder == 10) {
            var2 = T.pant;
         }

         PopupShop.addStr(var2 + AvatarData.getName(var1));
         if (this.seriPart.expireString != null && !this.seriPart.expireString.equals("")) {
            PopupShop.addStr(this.seriPart.expireString);
         }

         if (this.type == 0) {
            PopupShop.addStr(T.roomName[2] + ": " + AvatarData.getLevel(var1));
            return;
         }

         if (var1.follow != -2) {
            byte var3;
            if (var1.follow >= 0) {
               var3 = ((APartInfo)AvatarData.getPart(var1.follow)).level;
            } else {
               var3 = ((APartInfo)var1).level;
            }

            PopupShop.addStr(T.roomName[2] + ": " + var3);
         }
      }

   }
}
