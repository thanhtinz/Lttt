package avt;

import java.util.Vector;

public final class Item {
   public short ID;
   public short idIcon;
   public byte shopType;
   public int[] price = new int[2];
   public int number;
   public String name = "";

   public static Item getItemByList(Vector var0, int var1) {
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         Item var4;
         if ((var4 = (Item)var0.elementAt(var3)).ID == var1) {
            return var4;
         }
      }

      return null;
   }
}
