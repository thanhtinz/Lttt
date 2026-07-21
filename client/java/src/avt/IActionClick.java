package avt;

final class IActionClick implements IAction {
   private Welcome a;

   IActionClick(Welcome var1) {
      this.a = var1;
   }

   public final void perform() {
      Welcome.click(this.a);
   }
}
