package avt;

final class class_bt implements IAction {
   private final IAction a;

   class_bt(SoundManager var1, IAction var2) {
      this.a = var2;
   }

   public final void perform() {
      OptionScr.gI().save(20);
      this.a.perform();
   }
}
