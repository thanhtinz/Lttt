package avt;

public final class StringObj extends SubObject {
   public String str;
   public String str2;
   public int w2;
   private int dir = 1;
   public int dis = 0;
   public int anthor = 0;

   public StringObj() {
   }

   public StringObj(String var1, int var2) {
      this.str = var1;
      this.w2 = var2;
   }

   public StringObj(int var1, int var2, String var3) {
      super.x = var1;
      super.y = var2;
      this.str = var3;
   }

   public final void transTextLimit(int var1) {
      this.dis += this.dir;
      if (this.dis > this.w2 - var1) {
         this.dis = -20;
      }

   }
}
