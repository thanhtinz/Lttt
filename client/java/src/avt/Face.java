package avt;

import main.Canvas;

public abstract class Face extends AvMain {
   public final void show() {
      Canvas.currentFace = this;
   }
}
