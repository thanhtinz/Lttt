package avt;

import main.Canvas;
import main.GameMidlet;

final class CommandUsingPart1 implements IAction {
   private final SeriPart seriPart;
   private final int idOwner;
   private final int actionType;
   private final int chestId;

   CommandUsingPart1(MapScr var1, SeriPart var2, int var3, int var4, int var5) {
      this.seriPart = var2;
      this.idOwner = var3;
      this.actionType = var4;
      this.chestId = var5;
   }

   public final void perform() {
      Part var1 = AvatarData.getPart(this.seriPart.idPart);
      if (this.idOwner == GameMidlet.avatar.IDDB && (!AvatarData.isZOrderMain(var1.zOrder) || this.actionType != 0)) {
         Canvas.startOKDlg(T.emptyRoom[this.actionType], new CommandUsingPart2(this, this.actionType, this.chestId, this.seriPart));
      }

   }
}
