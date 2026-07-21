package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class MapItem extends SubObject {
   public short ID;
   public short typeID;
   public byte dir;
   public boolean isGetImg = false;

   public MapItem() {
   }

   public MapItem(int var1, int var2, int var3, int var4, short var5) {
      super.type = (byte)var1;
      super.x = var2;
      super.y = var3;
      this.ID = (short)var4;
      this.typeID = var5;
   }

   public final void paint(Graphics var1) {
      MapItemType var2;
      if (this.isGetImg) {
         var2 = LoadMap.getMapItemTypeByID(this.typeID);
      } else {
         var2 = AvatarData.getMapItemTypeByID(this.typeID);
      }

      int var3;
      boolean var6;
      int var10003;
      int var10004;
      if (!this.isGetImg && LoadMap.TYPEMAP != 68 && LoadMap.TYPEMAP != 69 && LoadMap.TYPEMAP != 70 && LoadMap.TYPEMAP != 110) {
         ImageInfo var10 = AvatarData.listImgInfo[var2.imgID];
         if ((super.x + var2.dx + var10.w) * MyObject.hd >= AvCamera.gI().xCam && (super.x + var2.dx - var10.w) * MyObject.hd <= AvCamera.gI().xCam + Canvas.w && (super.y + var10.h) * MyObject.hd >= AvCamera.gI().yCam && (super.y + var2.dy - var10.h) * MyObject.hd <= AvCamera.gI().yCam + Canvas.h) {
            int var10002 = (super.x + var2.dx) * MyObject.hd;
            var10003 = (super.y + var2.dy) * MyObject.hd;
            var6 = false;
            byte var11 = this.dir;
            int var4 = var10003;
            var3 = var10002;
            var10002 = var10.x0 * AvMain.hd;
            var10003 = var10.y0 * AvMain.hd;
            var10004 = var10.w * AvMain.hd;
            int var10005 = var10.h * AvMain.hd;
            var1.drawRegion(AvatarData.getBigImgInfo(var10.bigID).img, var10002, var10003, var10004, var10005, var11, var3, var4, 0);
         }
      } else {
         var10003 = (super.x + var2.dx) * MyObject.hd;
         var10004 = (super.y + var2.dy) * MyObject.hd;
         var6 = false;
         var3 = var2.imgID;
         ImageIcon var9 = AvatarData.getImgIcon((short)var3);
         if (var10003 + var9.w >= AvCamera.gI().xCam && var10003 <= AvCamera.gI().xCam + Canvas.w && var10004 + var9.h >= AvCamera.gI().yCam && var10004 <= AvCamera.gI().yCam + Canvas.h && var9.count != -1) {
            var1.drawRegion(var9.img, 0, 0, var9.w, var9.h, this.dir, var10003, var10004, 0);
         }
      }

   }

   public final void update() {
   }
}
