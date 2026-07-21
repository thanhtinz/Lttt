package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class Bus {
   private int x;
   private int y;
   private int v;
   private int g;
   private int count;
   private static byte damToc;
   private static byte direct;
   public static AvPosition posBusStop;
   public static boolean isRun = false;
   private static boolean isExit = false;
   private Image imgBus;

   public final void setBus(byte var1) {
      if (!isRun && GameMidlet.avatar.action != -1) {
         FilePack.b(T.at);
         this.imgBus = FilePack.getImage("839");
         FilePack.reset();
         direct = var1;
         if (var1 == 1) {
            AvCamera.gI().xCam = AvCamera.gI().xTo = posBusStop.x * AvMain.hd - Canvas.hw - 300;
         }

         this.y = LoadMap.Hmap * LoadMap.w + (Canvas.stypeInt != 0 ? Canvas.hTab : 0) / AvMain.hd + 20 * AvMain.hd;
         this.x = posBusStop.x + 300;
         this.v = this.g = 15;
         this.count = 0;
         damToc = 1;
         isRun = true;
         GameMidlet.avatar.setAction((byte)-1);
         AvCamera.disable = true;
         isExit = false;
         if (direct == 1) {
            GameMidlet.avatar.ableShow = true;
         }
      }

   }

   public final void update() {
      if ((damToc == 1 && direct == 1 || damToc == -1 && direct == -1) && direct == -1 && !isExit) {
         GlobalService.gI().getHandler(8);
         GameMidlet.avatar.ableShow = true;
         isExit = true;
      }

      this.x -= this.v;
      this.count += CRes.abs(this.g - this.v / 2);
      if (this.count >= 20) {
         this.count = 0;
         this.v -= damToc;
         if (this.v == 0) {
            damToc = -1;
            this.g = 8;
            GameMidlet.avatar.setPos(this.x, posBusStop.y);
            GameMidlet.avatar.setAction((byte)0);
            AvCamera.disable = false;
            GameMidlet.avatar.ableShow = false;
            if (Canvas.isInitChar && Session_ME.gI().isConnected()) {
               if (LoadMap.TYPEMAP == 9) {
                  (Canvas.welcome = new Welcome()).initMapScr();
               } else if (direct == 1 && LoadMap.TYPEMAP == 25) {
                  (Canvas.welcome = new Welcome()).initFarmPath(MapScr.instance);
               } else if (LoadMap.TYPEMAP == 13 && Welcome.indexFish < 8) {
                  (Canvas.welcome = new Welcome()).initFish();
               } else if (direct == 1 && LoadMap.TYPEMAP == 23) {
                  (Canvas.welcome = new Welcome()).initKhuMuaSam();
               }
            }
         }
      }

      if ((this.x + 58) * AvMain.hd < AvCamera.gI().xCam) {
         isRun = false;
         if (direct == -1) {
            Canvas.startWaitDlg();
         }
      }

   }

   public final void paint(Graphics var1) {
      int var2 = 0;
      if (this.v > 1) {
         var2 = Canvas.gameTick % 6 < 3 ? 1 : 0;
      }

      var1.drawImage(this.imgBus, this.x * AvMain.hd, (this.y + var2) * AvMain.hd - this.imgBus.getHeight(), 17);
   }
}
