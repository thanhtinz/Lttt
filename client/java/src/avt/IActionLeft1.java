package avt;

import main.Canvas;

final class IActionLeft1 implements IAction {
   IActionLeft1(IActionLeft var1) {
   }

   public final void perform() {
      System.out.println("aaaaaaaaaaaaaaaa");
      Canvas.isInitChar = false;
      Canvas.welcome = null;
      AvCamera.isFollow = false;
      MiniMap.gI().left = MiniMap.gI().l;
      MapScr.gI().initCmd();
      FarmScr.gI().initCmd();
   }
}
