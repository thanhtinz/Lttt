package avt;

import javax.microedition.lcdui.Graphics;

final class CommandFeel extends Command {
   private final byte[] partIds;
   private final int index;

   CommandFeel(MainMenu var1, String var2, int var3, int var4, byte[] var5, int var6) {
      super(var2, 19, var4);
      this.partIds = var5;
      this.index = var6;
   }

   public final void paint(Graphics var1, int var2, int var3) {
      APartInfo var4;
      (var4 = (APartInfo)AvatarData.getPart((short)0)).paint(var1, var2 + 2 + var4.dx[0] * AvMain.hd, var3 + 21 + 20 * (AvMain.hd - 1) + var4.dy[0] * AvMain.hd, 0);
      (var4 = (APartInfo)AvatarData.getPart((short)this.partIds[this.index])).paint(var1, var2 + 2 + var4.dx[0] * AvMain.hd, var3 + 21 + 20 * (AvMain.hd - 1) + var4.dy[0] * AvMain.hd, 0);
   }
}
