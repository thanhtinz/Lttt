package avt;

import main.Canvas;

final class IActionTriBenh3 implements IAction {
   private FarmScr instance;
   private final Animal pet;

   IActionTriBenh3(FarmScr var1, Animal var2) {
      this.instance = var1;
      this.pet = var2;
   }

   public final void perform() {
      boolean var1 = false;
      AnimalInfo var2 = FarmData.getAnimalByID(this.pet.species);

      for(int var3 = 0; var3 < FarmScr.listItemFarm.size(); ++var3) {
         Item var4;
         FarmItem var5;
         if ((var5 = FarmScr.getFarmItem((var4 = (Item)FarmScr.listItemFarm.elementAt(var3)).ID)).type == var2.area && var5.action == 5 && var4.number > 0) {
            var1 = true;
            this.pet.hunger = false;
            FarmScr.gI();
            FarmScr.doEat(var5.ID, this.pet.IDDB);
            this.instance.commandActionPointer(10, -1);
         }
      }

      if (!var1) {
         Canvas.startOKDlg(T.info);
         this.instance.commandTab(8, -1);
      }

   }
}
