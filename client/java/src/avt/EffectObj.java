package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class EffectObj extends Base {
   public short ID;
   public short dx;
   public short dy;
   public int idPlayer;
   public byte style;
   private byte index;

   public EffectObj() {
      this.dx = this.dy = 0;
      super.catagory = 6;
      this.index = 0;
   }

   public final void update() {
      EffectData var1;
      if ((var1 = AvatarData.getEffect(this.ID)) != null) {
         ++this.index;
         if (this.index < var1.arrFrame.length) {
            return;
         }
      }

      this.removee();
   }

   public final void paint(Graphics var1) {
      EffectData var2;
      if ((Canvas.stypeInt <= 0 || Canvas.currentMyScreen != MainMenu.gI()) && (var2 = AvatarData.getEffect(this.ID)) != null) {
         if (this.style == 0) {
            Avatar var3;
            if ((var3 = LoadMap.getAvatar(this.idPlayer)) == null) {
               this.removee();
               return;
            }

            super.x = var3.x + this.dx;
            super.y = var3.y + this.dy;
         }

         var2.paint(var1, super.x, super.y, this.index);
      }

   }

   private void removee() {
      switch (this.style) {
         case 0:
            LoadMap.playerLists.removeElement(this);
            return;
         case 1:
            LoadMap.treeLists.removeElement(this);
            return;
         case 2:
            LoadMap.effBgList.removeElement(this);
            return;
         case 3:
            LoadMap.effCameraList.removeElement(this);
         default:
      }
   }
}
