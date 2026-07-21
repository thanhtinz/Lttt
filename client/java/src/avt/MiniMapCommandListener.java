package avt;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import main.Canvas;
import main.GameMidlet;

final class MiniMapCommandListener implements CommandListener {
   private final javax.microedition.lcdui.Command a;
   private final TextField b;

   MiniMapCommandListener(MiniMap var1, javax.microedition.lcdui.Command var2, TextField var3) {
      this.a = var2;
      this.b = var3;
   }

   public final void commandAction(javax.microedition.lcdui.Command var1, Displayable var2) {
      if (var1 == this.a) {
         if (this.b.getString().equals("")) {
            return;
         }

         GlobalService.gI().requestService((byte)2, this.b.getString());
      }

      Canvas.instance.setFullScreenMode(true);
      Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
   }
}
