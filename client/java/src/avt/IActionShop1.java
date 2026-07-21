package avt;

import main.Canvas;

final class IActionShop1 implements IAction {
   short idItem;
   private String des;
   byte typeShop;

   public IActionShop1(HouseScr var1, byte var2, short var3, String var4) {
      this.typeShop = var2;
      this.idItem = var3;
      this.des = var4;
   }

   public final void perform() {
      Canvas.startOKDlg(this.des, new IActionDes(this));
   }
}
