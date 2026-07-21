package avt;

import main.Canvas;
import main.GameMidlet;

final class CommandUsingPart1 implements IAction {
   private final SeriPart a;
   private final int b;
   private final int c;
   private final int d;

   CommandUsingPart1(MapScr var1, SeriPart var2, int var3, int var4, int var5) {
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
   }

   public final void perform() {
      Part var1 = AvatarData.getPart(this.a.idPart);
      if (this.b == GameMidlet.avatar.IDDB && (!AvatarData.isZOrderMain(var1.zOrder) || this.c != 0)) {
         Canvas.startOKDlg(T.emptyRoom[this.c], new CommandUsingPart2(this, this.c, this.d, this.a));
      }

   }
}
