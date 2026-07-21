package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class PopupName extends SubObject {
   private String name;
   private byte num;
   private byte c = 0;

   public PopupName(String var1, int var2, int var3) {
      super.catagory = 8;
      super.x = var2;
      super.y = var3;
      this.name = var1;
      this.num = (byte)CRes.rnd(8);
   }

   public final void update() {
      ++this.num;
      if (this.num >= 8) {
         this.num = 0;
      }

   }

   public final void paint(Graphics var1) {
      if (OptionScr.gI().mapFocus[1] != 1 && Canvas.welcome == null && super.x * MyObject.hd >= AvCamera.gI().xCam && super.x * MyObject.hd <= AvCamera.gI().xCam + Canvas.w && super.y * MyObject.hd >= AvCamera.gI().yCam && super.y * MyObject.hd <= AvCamera.gI().yCam + Canvas.h + 10 && Canvas.currentMyScreen != MainMenu.gI()) {
         var1.drawImage(LoadMap.imgShadow, super.x * MyObject.hd, super.y * MyObject.hd, 3);
         if (MiniMap.gI().imgArrow != null) {
            int var10002 = super.x * MyObject.hd;
            int var10003 = (super.y - 10 + this.num / 2) * MyObject.hd;
            MiniMap.gI().imgArrow.drawFrame(0, var10002, var10003, 0, 33, var1);
         }

         Canvas.smallFontYellow.drawString(var1, this.name, super.x * MyObject.hd, (super.y - 32 + this.num / 2) * MyObject.hd, 2);
      }

   }
}
