package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public abstract class Effect {
   public boolean isStop = false;
   public short IDAction = -1;

   public abstract void updateWind();

   public abstract void paint(Graphics var1);

   public final void show() {
      Canvas.currentEffect.addElement(this);
   }

   public void close() {
      Canvas.currentEffect.removeElement(this);
   }
}
