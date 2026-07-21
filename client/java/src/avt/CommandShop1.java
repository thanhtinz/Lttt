package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

final class CommandShop1 extends Command {
   private int i = 0;
   private String nameItem;
   private String timeLimit;
   private short idPart;
   private short idPartGirl;
   private int price;

   public CommandShop1(HouseScr var1, String var2, IAction var3, int var4, String var5, short var6, short var7, String var8, int var9, String var10, short var11) {
      super(var2, var3);
      this.i = var4;
      this.nameItem = var5;
      this.idPart = var6;
      this.timeLimit = var8;
      this.price = var9;
      this.idPartGirl = var11;
   }

   public final void update() {
      if (PopupShop.isTransFocus && this.i == PopupShop.focus) {
         PopupShop.resetIsTrans();
         Part var1;
         if (GameMidlet.avatar.gender == 1) {
            var1 = AvatarData.getPart(this.idPart);
         } else {
            var1 = AvatarData.getPart(this.idPartGirl);
         }

         if (var1.IDPart != -1) {
            if (GameMidlet.avatar.gender == 1) {
               MapScr.gI();
               MapScr.setAvatarShop(var1);
            } else {
               MapScr.gI();
               MapScr.setAvatarShop(var1);
            }
         }

         PopupShop.addStr("Id: " + var1.IDPart);
         PopupShop.addStr(this.nameItem);
         if (this.timeLimit != null) {
            PopupShop.addStr(this.timeLimit);
         }

         if (this.price >= 0) {
            PopupShop.addStr(T.priceStr + Canvas.getMoneys(this.price) + " Tim");
         }
      }

   }

   public final void paint(Graphics var1, int var2, int var3) {
      Part var4;
      if (GameMidlet.avatar.gender == 1) {
         var4 = AvatarData.getPart(this.idPart);
      } else {
         var4 = AvatarData.getPart(this.idPartGirl);
      }

      var4.paintIcon(var1, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 0, 3);
   }
}
