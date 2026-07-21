package avt;

import javax.microedition.lcdui.Graphics;

public final class Point extends MyObject {
   public Layer layer;
   public int g;
   public int v;
   public int w;
   public int h;
   public int color = 0;
   public int limitY;
   public int countFr;
   public byte dis = 0;
   public short itemID;
   public boolean isFire;
   public boolean isRemove;
   public short yTo;
   public short xTo;
   public short distant;

   public Point() {
   }

   public Point(int var1, int var2) {
      super.x = var1;
      super.y = var2;
   }

   public Point(int var1, int var2, int var3) {
      super.x = var1;
      super.y = var2;
      this.xTo = (short)var1;
      this.yTo = (short)var2;
      this.itemID = (short)var3;
   }

   public final void update() {
      this.layer.update();
   }

   public final void paint(Graphics var1) {
      this.layer.paint(var1);
   }
}
