package avt;

import java.util.Vector;

final class IHouse1 implements IAction {
   IHouse1(HouseScr var1) {
   }

   public final void perform() {
      Vector var1;
      (var1 = new Vector()).addElement(new Command(T.won, 14));
      var1.addElement(new Command(T.setPass, 15));
      Menu.gI().startAt(var1, 0);
   }
}
