package avt;

public final class AvPosition {
   public int x;
   public int y;
   public int anchor;
   public short depth = 0;
   public short index = -1;

   public AvPosition() {
      this.x = 0;
      this.y = 0;
   }

   public AvPosition(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public AvPosition(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.anchor = var3;
   }
}
