package avt;

import java.util.Vector;
import main.Canvas;

final class IActionOpenShop implements IAction {
   private Part a;
   private short b;
   private int idBoss;
   private String textDes;
   private int idShop;
   private int ii;
   private MapScr g;

   public IActionOpenShop(MapScr var1, Part var2, short var3, int var4, String var5, int var6, int var7) {
      this.g = var1;
      this.a = var2;
      this.b = var3;
      this.idShop = var4;
      this.textDes = var5;
      this.idBoss = var6;
      this.ii = var7;
   }

   public final void perform() {
      if (this.idShop == 100) {
         final short var6 = this.b;
         final MapScr var7 = this.g;
         Vector var8 = new Vector();
         var8.addElement(new Command(T.dial, new IActionDial(var7, var6, true)));
         var8.addElement(new Command("Cài đặt", new IAction() {
            public void perform() {
               PopupShop.gI().close();
               DialLuckyScr.gI().openSettingsFromShop(Canvas.currentMyScreen, var6);
            }
         }));
         Menu.gI().startAt(var8, 0);
      } else if (this.idShop == 26) {
         Canvas.endDlg();
         MapScr.gI().doGiving(this.b);
         PopupShop.gI().close();
      } else {
         Part var1 = this.a;
         if (this.a.IDPart == -1) {
            var1 = AvatarData.getPart(this.b);
         }

         if (this.idBoss != -1 && this.idShop != 17 && this.idShop != 18) {
            Canvas.startOKDlg(this.textDes, new IActionTextDes(this.g, this.idBoss, this.idShop, this.ii));
         } else {
            MapScr.doSelectMoneyBuyItem(var1);
         }
      }

   }
}
