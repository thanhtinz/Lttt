package avt;

final class class_bw implements IAction {
   private SoundManager a;

   class_bw(SoundManager var1) {
      this.a = var1;
   }

   public final void perform() {
      this.a.isPlaying = 0;
   }
}
