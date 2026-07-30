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
      if ((var1 = SoundManager.instance.setSound("" + this.b)) == -1) {
         GlobalService.gI().doRequestSoundData(this.b);
      } else {
         SoundManager.instance.playSoundData((byte[])((byte[])SoundManager.getSound(this.a).elementAt(var1)));
      }

      this.a.isPlaying = 1;
   }
}
