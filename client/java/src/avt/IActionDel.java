package avt;

import java.util.Vector;

final class IActionDel implements IAction {
   private IActionDellPart a;
   private final Part b;
   private final int c;
   private final Vector d;
   private final Vector e;
   private final SeriPart f;
   private final int g;

   IActionDel(IActionDellPart var1, Part var2, int var3, Vector var4, Vector var5, SeriPart var6, int var7) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
      this.g = var7;
   }

   public final void perform() {
      GlobalService.gI().doRemoveItem(this.b.IDPart, this.c);
      this.d.removeElementAt(PopupShop.focus);
      this.e.removeElement(this.f);
      if (this.g == 0) {
         if (MainMenu.gI().isWearing) {
            MainMenu.gI();
            MainMenu.doWearing();
         } else {
            this.a.a.doStore();
         }
      } else {
         HouseScr.gI().restartPopup();
      }

   }
}
