package avt;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class OnSplashScr extends MyScreen {
   public static OnSplashScr me;
   public int splashScrStat = 0;
   public static Image imgBg;
   public static boolean isOpen = false;

   public static OnSplashScr gI() {
      return me == null ? (me = new OnSplashScr()) : me;
   }

   public final void switchToMe() {
      Canvas.listInfoSV.removeAllElements();
      Canvas.transTab = 0;
      Canvas.instance.setSize();
      OnScreen.isOngame = true;

      try {
         imgBg = Image.createImage(T.getPath() + "/on/logo.on");
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      super.switchToMe();
   }

   public final void update() {
      if (this.splashScrStat > 21) {
         LoadMap.xDichChuyen_ = GameMidlet.avatar.x;
         LoadMap.C = GameMidlet.avatar.y;
         OnScreen.gI().switchToMe();
      } else if (this.splashScrStat == 0) {
         Canvas.paint.initResourceOne();
      }

      ++this.splashScrStat;
   }

   public final void paint(Graphics var1) {
      Canvas.paint.paintDefaultBg(var1);
      if (this.splashScrStat > 1) {
         var1.drawImage(imgBg, Canvas.hw, Canvas.hCan / 2, 3);
      }

      Canvas.paintPlus(var1);
   }
}
