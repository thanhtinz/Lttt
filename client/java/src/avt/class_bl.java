package avt;

final class class_bl implements IAction {
   private SoundManager a;
   private final byte b;

   class_bl(SoundManager var1, byte var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      int var1;
      if ((var1 = SoundManager.a.a("" + this.b)) == -1) {
         GlobalService.gI().doRequestSoundData(this.b);
      } else {
         SoundManager.a.a((byte[])((byte[])SoundManager.a(this.a).elementAt(var1)));
      }

      this.a.b = 1;
   }
}
