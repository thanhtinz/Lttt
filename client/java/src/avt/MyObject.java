package avt;

import javax.microedition.lcdui.Graphics;

public abstract class MyObject {
   public int x;
   public int y;
   public static int hd;
   public byte catagory = 0;
   public short height = 0;

   public void update() {
   }

   public void paint(Graphics var1) {
   }

   static {
      hd = AvMain.hd;
   }
}
