package avt;

import main.Canvas;

final class IActionSetAnimal implements IAction {
   private FarmScr a;
   private final FarmItem b;
   private final short c;
   private final Animal d;

   IActionSetAnimal(FarmScr var1, FarmItem var2, short var3, Animal var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   public final void perform() {
      if (this.b.action == 4) {
         FarmScr.setAction(this.a, (byte)4, this.c);
         LoadMap.focusObj = this.d;
         this.a.aniDoing = (Animal)LoadMap.focusObj;
         this.a.aniDoing.isStand = true;
         this.a.aniDoing.timeStand = Canvas.getSecond();
      }

      FarmService.gI().doUsingItem(FarmScr.idFarm, this.d.IDDB, this.c);
   }
}
