package avt;

import javax.microedition.lcdui.Graphics;

public final class FarmItem {
   public short ID;
   public short IDImg;
   public boolean isItem;
   public byte type;
   public byte action;
   public String des;
   public int priceXu;
   public int priceLuong;

   public final void paint(Graphics var1, int var2, int var3, int var4, int var5) {
      ImageIcon var6;
      if ((var6 = FarmData.getImgIcon(this.IDImg)).count != -1) {
         var1.drawRegion(var6.img, 0, 0, var6.w, var6.h, 0, var2, var3, 3);
      }

   }
}
