package avt;

public final class Kiss {
   int xCur;
   int yCur;
   int[] x;
   int[] y;
   byte[] index;
   byte[] dir;
   byte[] dis;

   public Kiss(Avatar var1, int var2, int var3) {
      this.xCur = var2;
      this.yCur = var3;
      this.x = new int[3];
      this.y = new int[3];
      this.index = new byte[3];
      this.dir = new byte[3];
      this.dis = new byte[3];

      for(int var4 = 0; var4 < 3; ++var4) {
         this.index[var4] = (byte)CRes.rnd(8);
         this.y[var4] = -var4 * 20;
         this.dir[var4] = (byte)(CRes.rnd(2) == 0 ? 1 : -1);
         this.dis[var4] = 6;
      }

   }
}
