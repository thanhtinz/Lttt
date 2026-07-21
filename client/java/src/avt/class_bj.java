package avt;

final class class_bj implements IAction {
   private final IAction a;

   class_bj(SoundManager var1, IAction var2) {
      this.a = var2;
   }

   public final void perform() {
      OptionScr.gI().save(50);
      this.a.perform();
   }
}
