package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

final class class_fj extends Command {
   private final Avatar f;
   private final Pet g;
   private final StringObj h;

   class_fj(String var1, IAction var2, Avatar var3, Pet var4, StringObj var5) {
      super((String)null, (IAction)null);
      this.f = var3;
      this.g = var4;
      this.h = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      this.f.paintIcon(var1, PopupShop.w / 2, 37 * AvMain.hd, true);
      if (this.g != null) {
         this.g.paintIcon(var1, PopupShop.w / 2 + 15 * AvMain.hd, 37 * AvMain.hd, this.f.hungerPet);
      }

      label54: {
         var2 = 40 * AvMain.hd;
         var3 = 15 * AvMain.hd;
         Canvas.fontChatB.drawString(var1, T.nameStr + this.f.name, 0, var2, 0);
         FontX var10000;
         Graphics var10001;
         StringBuffer var10002;
         String var10003;
         if (this.f.IDDB == GameMidlet.avatar.IDDB) {
            if (GameMidlet.myIndexP.g <= 0 && GameMidlet.myIndexP.f <= 0) {
               break label54;
            }

            var10000 = Canvas.fontChatB;
            var10001 = var1;
            var10002 = (new StringBuffer(String.valueOf(T.roomName[0]))).append(GameMidlet.myIndexP.g).append(" + ").append(GameMidlet.myIndexP.f);
            var10003 = "%";
         } else {
            if (this.f.lvMain <= 0 && this.f.perLvFarm <= 0) {
               break label54;
            }

            var10000 = Canvas.fontChatB;
            var10001 = var1;
            var10002 = (new StringBuffer(String.valueOf(T.roomName[0]))).append(this.f.lvMain).append(" + ");
            var10003 = this.f.perLvFarm > 0 ? this.f.perLvFarm + "%" : "";
         }

         var10000.drawString(var10001, var10002.append(var10003).toString(), 0, var2 += var3, 0);
      }

      if (this.f.lvFarm > 0 || this.f.dirFirst > 0) {
         Canvas.fontChatB.drawString(var1, T.roomName[1] + this.f.lvFarm + " + " + (this.f.dirFirst > 0 ? this.f.dirFirst + "%" : ""), 0, var2 += var3, 0);
      }

      int var4 = 0;
      if (this.h.w2 > 125 * AvMain.hd) {
         this.h.transTextLimit(100 * AvMain.hd);
         if (this.h.dis >= 0) {
            var4 = this.h.dis;
         }
      }

      Canvas.fontChatB.drawString(var1, this.h.str, 0 - var4, var2 += var3, 0);
      if (MapScr.isNewVersion) {
         Canvas.fontChatB.drawString(var1, T.tkNew + this.f.money[3] + T.getMoneyH(), 0, var2 + var3, 0);
      }

   }
}
