package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public class Command {
   public String caption;
   public IAction action;
   public byte indexMenu;
   public AvMain pointer;
   public short subIndex = -1;

   public Command(String var1, IAction var2) {
      this.caption = var1;
      this.action = var2;
   }

   public Command(String var1, int var2) {
      this.caption = var1;
      this.indexMenu = (byte)var2;
   }

   public Command(String var1, int var2, AvMain var3) {
      this.caption = var1;
      this.indexMenu = (byte)var2;
      this.pointer = var3;
   }

   public Command(String var1, int var2, int var3) {
      this.caption = var1;
      this.indexMenu = (byte)var2;
      this.subIndex = (short)((byte)var3);
   }

   public final void perform() {
      if (this.action != null) {
         this.action.perform();
      } else if (this.pointer != null) {
         this.pointer.commandActionPointer(this.indexMenu);
      } else if (ChatTextField.isShow) {
         ChatTextField.gI().commandTab(this.indexMenu, this.subIndex);
      } else {
         Canvas.currentMyScreen.commandTab(this.indexMenu, this.subIndex);
      }

   }

   public void update() {
   }

   public void paint(Graphics var1, int var2, int var3) {
      Canvas.borderFont.drawString(var1, this.caption, var2, var3, 2);
   }
}
