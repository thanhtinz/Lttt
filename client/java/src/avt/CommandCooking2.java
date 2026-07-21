package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

final class CommandCooking2 extends Command {
   CommandCooking2(FarmScr var1, String var2, int var3, AvMain var4) {
      super(var2, 2, var4);
   }

   public final void paint(Graphics var1, int var2, int var3) {
      Food var7;
      FarmItem var9 = FarmScr.getFarmItem((var7 = FarmData.getFoodByID(FarmScr.foodID)).productID);
      FarmData.paintImg(var1, var9.IDImg, Canvas.cameraList.disX / 2, PopupShop.sai / 2 - 30, 3);
      Canvas.fontChatB.drawString(var1, var7.text, Canvas.cameraList.disX / 2, PopupShop.sai / 2 - 30 + 5 + FarmData.getImgIcon(var9.IDImg).h / 2 + AvMain.hSmall + 2, 2);
      String var8 = "";
      int var4 = FarmScr.remainTime / 3600;
      FontX var5 = Canvas.M;
      if (var4 > 0) {
         var8 = var4 + ":";
      }

      int var6;
      if ((var6 = (FarmScr.remainTime - var4 * 3600) / 60) > 0 || var4 > 0) {
         var8 = var8 + var6 + ":";
      }

      var4 = FarmScr.remainTime - var4 * 3600 - var6 * 60;
      var8 = var8 + var4;
      if (FarmScr.remainTime == 0) {
         var8 = T.done;
         var5 = Canvas.fontChatB;
      }

      var5.drawString(var1, var8, Canvas.cameraList.disX / 2, PopupShop.sai / 2 - 30 + 5 + FarmData.getImgIcon(var9.IDImg).h / 2, 2);
   }
}
