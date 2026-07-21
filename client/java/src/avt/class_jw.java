package avt;

final class class_jw implements IAction {
   private TField a;

   class_jw(TField var1) {
      this.a = var1;
   }

   public final void perform() {
      if (TField.getFocus(this.a)) {
         this.a.clear();
      }

   }
}
