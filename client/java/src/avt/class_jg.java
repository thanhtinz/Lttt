package avt;

import javax.microedition.lcdui.Graphics;

final class class_jg extends Command {
   private final StringObj f;

   class_jg(MainMenu var1, String var2, int var3, int var4, StringObj var5) {
      super(var2, 17, var4);
      this.f = var5;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      AvatarData.paintImg(var1, this.f.dis, var2, var3, 3);
   }
}
