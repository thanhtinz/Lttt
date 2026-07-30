package avt;

import javax.microedition.lcdui.Graphics;

final class class_fk extends Command {
   private final IndexPlayer f;

   class_fk(MapScr var1, String var2, IAction var3, IndexPlayer var4) {
      super((String)null, (IAction)null);
      this.f = var4;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      var2 = PopupShop.w / 2 + 7;
      int var4 = (var3 = (PopupShop.sai - MyScreen.hTab - (AvMain.hDuBox << 1)) / 7) / 2 - MapScr.imgBar.getHeight() / 2;
      MapScr.drawStatBar(var1, T.login[0] + this.f.g, var2, var4, this.f.f);
      MapScr.drawStatBar(var1, T.login[1], var2, var4 += var3, this.f.a);
      MapScr.drawStatBar(var1, T.login[2], var2, var4 += var3, this.f.b);
      MapScr.drawStatBar(var1, T.login[3], var2, var4 += var3, this.f.e);
      MapScr.drawStatBar(var1, T.login[4], var2, var4 += var3, this.f.c);
      MapScr.drawStatBar(var1, T.login[5], var2, var4 + var3, this.f.d);
   }
}
