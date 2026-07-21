package avt;

import java.util.Vector;
import main.Canvas;

final class IActionDellPart implements IAction {
   final MapScr a;
   private final Vector b;
   private final int c;
   private final int d;

   IActionDellPart(MapScr var1, Vector var2, int var3, int var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   public final void perform() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.b.size(); ++var2) {
         SeriPart var3;
         Part var4;
         if ((var4 = AvatarData.getPart((var3 = (SeriPart)this.b.elementAt(var2)).idPart)) != null && var4.zOrder != 30 && var4.zOrder != 40) {
            var1.addElement(var3);
         }
      }

      SeriPart var5;
      Part var6;
      if (PopupShop.focus < var1.size() && (!AvatarData.isZOrderMain((var6 = AvatarData.getPart((var5 = (SeriPart)var1.elementAt(PopupShop.focus)).idPart)).zOrder) || this.c == 1)) {
         Canvas.startOKDlg(T.doYouWantDel, new IActionDel(this, var6, this.c, var1, this.b, var5, this.d));
      }

   }
}
