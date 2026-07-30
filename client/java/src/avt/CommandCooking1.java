package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandCooking1 extends Command {
   private final Food food;
   private final int focusIndex;

   CommandCooking1(FarmScr var1, String var2, IAction var3, Food var4, int var5) {
      super(var2, var3);
      this.food = var4;
      this.focusIndex = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      FarmItem var4 = FarmScr.getFarmItem(this.food.productID);
      FarmData.paintImg(var1, var4.IDImg, var2 + PopupShop.h / 2, var3 + PopupShop.h / 2, 3);
      var1.translate(0, CameraList.cmtoY);
      var1.setClip(0, 0, 5 * PopupShop.h, PopupShop.sai);
      if (this.focusIndex == PopupShop.focus) {
         for(var2 = 0; var2 < this.food.material.length; ++var2) {
            Item var5;
            if (this.food.material[var2] < 50) {
               var5 = FarmScr.getProductByID(this.food.material[var2]);
               FarmData.getTreeByID(this.food.material[var2]).paint(var1, 7, PopupShop.w / 2 - this.food.material.length * 30 * AvMain.hd / 2 + var2 * 30 * AvMain.hd + 15 * (AvMain.hd - 1), (PopupShop.h << 1) + 25 * AvMain.hd + (AvMain.hBlack << 2) + 10 * (AvMain.hd - 1), 3);
            } else if (this.food.material[var2] < 100) {
               var5 = FarmScr.getProductByID(this.food.material[var2]);
               AnimalInfo var6 = FarmData.getAnimalByID(this.food.material[var2]);
               AvatarData.paintImg(var1, var6.iconProduct, PopupShop.w / 2 - this.food.material.length * 30 * AvMain.hd / 2 + var2 * 30 * AvMain.hd + 15 * (AvMain.hd - 1), (PopupShop.h << 1) + 25 * AvMain.hd + (AvMain.hBlack << 2) + 10 * (AvMain.hd - 1), 3);
            } else {
               var5 = FarmScr.getItemProductByID(this.food.material[var2]);
               var4 = FarmScr.getFarmItem(this.food.material[var2]);
               FarmData.paintImg(var1, var4.IDImg, PopupShop.w / 2 - this.food.material.length * 30 * AvMain.hd / 2 + var2 * 30 * AvMain.hd + 15 * (AvMain.hd - 1), (PopupShop.h << 1) + 25 * AvMain.hd + (AvMain.hBlack << 2) + 10 * (AvMain.hd - 1), 3);
            }

            FontX var7 = Canvas.fontChatB;
            if (var5 == null || var5.number < this.food.numberMaterial[var2]) {
               var7 = Canvas.M;
            }

            var7.drawString(var1, String.valueOf(this.food.numberMaterial[var2]), PopupShop.w / 2 - this.food.material.length * 30 * AvMain.hd / 2 + var2 * 30 * AvMain.hd - 1 + 15 * (AvMain.hd - 1), (PopupShop.h << 1) + 25 * AvMain.hd + (AvMain.hBlack << 2) + 8 * AvMain.hd + 10 * (AvMain.hd - 1), 2);
            if (var2 != this.food.material.length - 1) {
               Canvas.fontChatB.drawString(var1, "+", PopupShop.w / 2 - this.food.material.length * 30 * AvMain.hd / 2 + var2 * 30 * AvMain.hd + 15 * AvMain.hd + 15 * (AvMain.hd - 1), (PopupShop.h << 1) + 25 * AvMain.hd + (AvMain.hBlack << 2) + 10 * (AvMain.hd - 1), 2);
            }
         }
      }

      var1.setClip(0, 0, 5 * PopupShop.h, PopupShop.numH * PopupShop.h - PopupShop.duCam);
      var1.translate(0, -CameraList.cmtoY);
   }

   public final void update() {
      if (this.focusIndex == PopupShop.focus) {
         PopupShop.resetIsTrans();
         FarmItem var1;
         PopupShop.addStr("Id: " + this.food.productID);
         PopupShop.addStr(this.food.text);
         PopupShop.addStr(T.time + this.food.cookTime + "p");
         if ((var1 = FarmScr.getFarmItem(this.food.productID)).priceXu > 0) {
            PopupShop.addStr(T.salePrice + Canvas.getMoneys(var1.priceXu) + T.dola);
         } else if (var1.priceLuong > 0) {
            PopupShop.addStr(T.salePrice + Canvas.getMoneys(var1.priceLuong) + T.dola);
         }

         PopupShop.addStr(T.material);
      }

   }
}
