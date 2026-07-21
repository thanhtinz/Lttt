package avt;

final class IActionListMenu2 implements IAction {
   private IActionListMenu a;
   private final int b;
   private final byte c;
   private final byte[] d;
   private final int e;

   IActionListMenu2(IActionListMenu var1, int var2, byte var3, byte[] var4, int var5) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
   }

   public final void perform() {
      GlobalService.gI().doListCustom(this.b, this.c, this.a.tex.selected, this.d[this.e]);
   }
}
