package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class Xingau {
   private int x;
   private int index;
   private int idFrame;
   private int y;
   private int type;
   int typeStop;
   private static int[][] array = new int[][]{{6, 0, 7, 1, 6, 2, 7, 3}, {6, 5, 7, 4, 6, 3, 7, 2}, {7, 4, 6, 1, 7, 3, 6, 5}};
   boolean stopHere;
   private static byte wImg;
   private static byte hImg;

   public Xingau(int var1, int var2, int var3, int var4, boolean var5) {
      this.x = var1;
      this.y = var2;
      this.type = var3;
      this.typeStop = var4;
      this.stopHere = var5;
      hImg = 50;
      wImg = 54;
      if (AvMain.hd == 2) {
         hImg = 108;
         wImg = 108;
      }

   }

   public final void paint(Graphics var1) {
      if (AvatarData.getImgIcon((short)874).count != -1) {
         int var10003 = this.idFrame * hImg;
         var1.drawRegion(AvatarData.getImgIcon((short)874).img, 0, var10003, wImg, hImg, 0, this.x, this.y, 17);
      }

   }

   public final void update() {
      if (!this.stopHere) {
         if (Canvas.gameTick % 2 == 0) {
            ++this.index;
            if (this.index > array[this.type].length - 1) {
               this.index = 0;
            }
         }

         this.idFrame = array[this.type][this.index];
      } else {
         this.idFrame = this.typeStop;
      }

   }
}
