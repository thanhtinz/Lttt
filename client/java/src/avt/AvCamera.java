package avt;

import main.Canvas;

public final class AvCamera {
   private static AvCamera instance;
   public int xCam;
   public int yCam;
   public int xTo;
   public int yTo;
   private int xLimit;
   private int yLimit;
   public long timeDelay;
   private int cmvx;
   private int cmdx;
   private int cmvy;
   private int cmdy;
   private static int distance;
   private static int w;
   public static boolean disable = false;
   public static boolean isFollow;
   public Base followPlayer;

   public static AvCamera gI() {
      if (instance == null) {
         instance = new AvCamera();
      }

      return instance;
   }

   public static void setDistance(int var0) {
      distance = var0;
   }

   public final void init(int var1) {
      if (this.followPlayer != null) {
         label54: {
            isFollow = false;
            w = LoadMap.w * AvMain.hd;
            distance = Canvas.w / 10;
            if (this.followPlayer.x * AvMain.hd > Canvas.hw) {
               if (this.followPlayer.x * AvMain.hd < LoadMap.wMap * w - Canvas.hw - w) {
                  this.xTo = this.followPlayer.x * AvMain.hd - Canvas.hw;
                  break label54;
               }

               this.xTo = LoadMap.wMap * w - Canvas.w;
               if (this.xTo >= 0) {
                  break label54;
               }
            }

            this.xTo = 0;
         }

         if (Canvas.w > LoadMap.wMap * w) {
            this.xTo = -(Canvas.w - LoadMap.wMap * w) / 2;
         }

         if (Canvas.h > LoadMap.Hmap * w && (var1 - 1 == 57 || var1 - 1 == 58 || var1 - 1 == 59 || var1 - 1 == 108)) {
            this.yTo = -(Canvas.h - LoadMap.Hmap * w) / 2;
         } else {
            this.yTo = LoadMap.Hmap * w - Canvas.h;
         }

         this.xLimit = LoadMap.wMap * w - Canvas.w;
         this.yLimit = LoadMap.Hmap * w - Canvas.h;
         this.xCam = this.xTo;
         if (this.xCam < 0) {
            this.xCam = 0;
         }

         if (this.xCam > this.xLimit) {
            this.xCam = this.xLimit;
         }

         if (this.yCam > this.yLimit) {
            this.yCam = this.yLimit;
         }

         if (this.yTo > this.yLimit) {
            this.yTo = this.yLimit;
         }
      }

   }

   public final void notTrans() {
      this.xCam = this.xTo;
      this.yCam = this.yTo;
   }

   public final void updateTo() {
      if (!disable) {
         if (this.xCam != this.xTo) {
            this.cmvx = this.xTo - this.xCam << 1;
            this.cmdx += this.cmvx;
            this.xCam += this.cmdx >> 4;
            this.cmdx &= 15;
            if (this.xCam < 0) {
               this.xCam = 0;
            }

            if (this.xCam > this.xLimit) {
               this.xCam = this.xLimit;
            }
         }
      } else {
         if (this.xCam < 0) {
            this.xCam = 0;
         }

         if (this.xCam > LoadMap.wMap * LoadMap.w * AvMain.hd - Canvas.w) {
            this.xCam = LoadMap.wMap * LoadMap.w * AvMain.hd - Canvas.w;
         }
      }

      if (this.yCam != this.yTo) {
         this.cmvy = this.yTo - this.yCam << 1;
         this.cmdy += this.cmvy;
         this.yCam += this.cmdy >> 4;
         this.cmdy &= 15;
         if (this.yCam > this.yLimit) {
            this.yCam = this.yLimit;
         }
      }

   }

   public final void setToPos(int var1, int var2) {
      this.timeDelay = 0L;
      this.xTo = var1 - Canvas.hw;
      this.yTo = var2 - Canvas.hh;
      if (this.xTo < 0) {
         this.xTo = 0;
      }

      if (this.xTo > LoadMap.wMap * w - Canvas.w) {
         this.xTo = LoadMap.wMap * w - Canvas.w;
      }

      if (this.yTo > LoadMap.Hmap * w - Canvas.h) {
         this.yTo = LoadMap.Hmap * w - Canvas.h;
      }

      this.setLimit();
   }

   public final void setPos(int var1, int var2) {
      this.xCam = this.xTo = 0;
      this.yCam = this.yTo = 0;
   }

   public final void update() {
      this.updateTo();
      if (System.currentTimeMillis() / 100L - this.timeDelay >= 20L && !isFollow) {
         int var1;
         if (this.followPlayer.direct == 0) {
            var1 = this.followPlayer.x * AvMain.hd + distance;
         } else {
            var1 = this.followPlayer.x * AvMain.hd - distance;
         }

         this.xTo = var1 - Canvas.hw;
         this.yTo = (this.followPlayer.y + this.followPlayer.direct_) * AvMain.hd - (Canvas.h - (Canvas.hh - w));
         if (this.followPlayer.direct == Base.LEFT) {
            if (this.followPlayer.x * AvMain.hd < Canvas.hw) {
               this.xTo = 0;
            }
         } else if (this.followPlayer.x * AvMain.hd > LoadMap.wMap * w - Canvas.hw) {
            this.xTo = LoadMap.wMap * w - Canvas.w;
         }

         this.setLimit();
      }

   }

   private void setLimit() {
      if (LoadMap.TYPEMAP >= 0 && LoadMap.TYPEMAP < LoadMap.bg.length && LoadMap.bg[LoadMap.TYPEMAP] == -1 && LoadMap.imgBG == null && Canvas.h > LoadMap.Hmap * w) {
         this.yCam = this.yTo = -(Canvas.h - LoadMap.Hmap * w) / 2;
      }

      if (Canvas.w > LoadMap.wMap * w) {
         this.xCam = this.xTo = -(Canvas.w - LoadMap.wMap * w) / 2;
      }

   }
}
