package avt;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;

public final class SplashScr extends MyScreen {
   public static SplashScr me;
   private static int splashScrStat = 20;
   private static Image imgLogoo;

   public static SplashScr gI() {
      return me == null ? (me = new SplashScr()) : me;
   }

   public final void switchToMe() {
      OnScreen.isOngame = false;
      splashScrStat = 0;
      if (OnScreen.c != 0) {
         imgLogoo = MyScreen.imgLogo;
      }

      super.switchToMe();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 50:
            selectedLanguage(0);
            return;
         case 51:
            selectedLanguage(1);
         default:
      }
   }

   public final void update() {
      if (splashScrStat > 51) {
         if (OnScreen.c != 0) {
            if (OnScreen.c == 2) {
               MapScr.gI().switchToMe();
               imgLogoo = null;
               OnScreen.c = 0;
               Canvas.paint.initResourceTwo();
            }
         } else if (splashScrStat == 52) {
            LoginScr.gI().loadLogin();
            OptionScr.gI().load();
            T.applyLanguage(OptionScr.gI().mapFocus[4]);
            if (!LoginScr.isSelectedLanguage) {
               LoginScr.isSelectedLanguage = true;
               AvatarData.delErrorRms("avatarSV");
               AvatarData.loadServerList();
               selectedLanguage(0);
            } else {
               AvatarData.loadServerList();
               LoginScr.gI().initImg();
               if (ServerListScr.gI() != Canvas.currentMyScreen) {
                  LoginScr.gI().switchToMe();
               }

               imgLogoo = null;
            }
         }
      } else if (OnScreen.c != 0 && splashScrStat == 0) {
         MapScr.gI().switchToMe();
         imgLogoo = null;
         OnScreen.c = 0;
         Canvas.paint.initResourceTwo();
      }

      ++splashScrStat;
   }

   private static void selectedLanguage(int var0) {
      Canvas.startWaitDlg();
      OptionScr.gI().mapFocus[4] = var0;
      OptionScr.gI().save(0);
      T.applyLanguage(var0);
      LoginScr.gI().initImg();
      LoginScr.gI().switchToMe();
      imgLogoo = null;
   }

   public final void paint(Graphics var1) {
      PaintPopup.fill(0, 0, Canvas.w, Canvas.instance.getHeight(), 0, var1);
      if (splashScrStat > 1 && imgLogoo != null) {
         var1.drawImage(imgLogoo, Canvas.w >> 1, Canvas.h >> 1, 3);
      }

      Canvas.paintPlus(var1);
   }

   static {
      try {
         imgLogoo = Image.createImage(T.getPath() + "/lg.png");
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }
}
