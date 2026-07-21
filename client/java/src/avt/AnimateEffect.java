package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class AnimateEffect extends Effect {
   private static FrameImage img;
   private static FrameImage e;
   private byte type = 0;
   private int number = 0;
   public int timeStop;
   private int timeCur;
   private static int wind = 5;
   private static int countWind;
   private static int dirWind = CRes.rnd(1, -1);
   private Vector list = new Vector();

   public final void close() {
      super.close();
   }

   public AnimateEffect(int var1, int var2) {
      this.type = (byte)var1;
      this.number = var2 * 10;
      if (AvMain.hd == 1) {
         this.number = var2 * 5;
      }

      this.timeCur = (int)(System.currentTimeMillis() / 1000L);
      switch (var1) {
         case 0:
            this.number = Canvas.w * Canvas.h / 1000 + 50;
            break;
         case 1:
            this.number = 30;
            if (img == null) {
               FilePack.b(T.av);
               img = FrameImage.init("cobay", 16 * AvMain.hd, 10 * AvMain.hd);
               FilePack.reset();
            }
         case 2:
         default:
            break;
         case 3:
            this.number = Canvas.w * Canvas.h / 1000;
            FilePack.b(T.av);
            FrameImage.init("tuyet", 5 * AvMain.hd, 5 * AvMain.hd);
            FilePack.reset();
            e = img;
      }

      Point var3;
      for(var2 = 0; var2 < this.number; ++var2) {
         (var3 = new Point(0, (AvCamera.gI().yCam - (Canvas.h << 1) + CRes.rnd(Canvas.h << 1)) * 10)).x = (-Canvas.w / 2 + CRes.rnd(LoadMap.wMap * LoadMap.w + Canvas.w)) * 10;
         if (var1 != 3 && this.type != 2) {
            var3.h = CRes.rnd(4);
         } else {
            var3.h = CRes.rnd(3);
         }

         var3.limitY = 16 + (CRes.rnd(3) << 2);
         var3.v = CRes.rnd(-1, 1);
         var3.color = CRes.rnd(var3.limitY);
         var3.dis = (byte)CRes.rnd(20);
         this.list.addElement(var3);
      }

      if (var1 == 2) {
         for(var2 = 0; var2 < this.list.size() - 1; ++var2) {
            var3 = (Point)this.list.elementAt(var2);

            for(var1 = var2 + 1; var1 < this.list.size(); ++var1) {
               Point var4 = (Point)this.list.elementAt(var1);
               if (var3.h > var4.h) {
                  this.list.setElementAt(var3, var1);
                  this.list.setElementAt(var4, var2);
                  var3 = var4;
               }
            }
         }
      }

   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      var1.translate(-AvCamera.gI().xCam, -AvCamera.gI().yCam);
      int var3;
      Point var4;
      AnimateEffect var6;
      Graphics var7;
      switch (this.type) {
         case 0:
            var7 = var1;
            var6 = this;
            var1.setColor(14540253);

            for(var3 = 0; var3 < var6.number; ++var3) {
               var4 = (Point)var6.list.elementAt(var3);
               int var10001 = 2 - var4.h;
               int var5 = AvCamera.gI().xCam * var10001 * 20 / 120;
               var7.fillRect(var5 + var4.x / 10, var4.y / 10, 1, var4.h + 1);
            }

            return;
         case 1:
            var7 = var1;
            var6 = this;

            for(var3 = 0; var3 < var6.number; ++var3) {
               if ((var4 = (Point)var6.list.elementAt(var3)).x * AvMain.hd / 10 > AvCamera.gI().xCam && var4.x * AvMain.hd / 10 < AvCamera.gI().xCam + Canvas.w && var4.y * AvMain.hd / 10 > AvCamera.gI().yCam) {
                  img.drawFrame(var4.color / (var4.limitY / 4), var4.x * AvMain.hd / 10, var4.y * AvMain.hd / 10, 0, 3, var7);
               }
            }

            return;
         case 2:
            if (super.IDAction == -1) {
               return;
            } else {
               EffectData var2 = AvatarData.getEffect(super.IDAction);

               for(var3 = 0; var3 < this.number; ++var3) {
                  ++(var4 = (Point)this.list.elementAt(var3)).countFr;
                  if (var4.x * AvMain.hd / 10 > AvCamera.gI().xCam && var4.x * AvMain.hd / 10 < AvCamera.gI().xCam + Canvas.w && var4.y * AvMain.hd / 10 > AvCamera.gI().yCam && var4.y * AvMain.hd / 10 < AvCamera.gI().yCam + Canvas.hCan) {
                     if (var2 != null) {
                        if (var4.countFr >= var2.arrFrame.length) {
                           var4.countFr = 0;
                        }

                        var2.paint(var1, var4.x / 10, var4.y / 10, var4.countFr);
                     }

                     ++var4.dis;
                     if (var4.dis >= 20) {
                        var4.dis = 0;
                     }
                  }
               }

               return;
            }
         case 3:
            for(var3 = 0; var3 < this.number; ++var3) {
               if ((var4 = (Point)this.list.elementAt(var3)).x * AvMain.hd / 10 > AvCamera.gI().xCam && var4.x * AvMain.hd / 10 < AvCamera.gI().xCam + Canvas.w && var4.y * AvMain.hd / 10 > AvCamera.gI().yCam) {
                  e.drawFrame(2 - var4.h, var4.x * AvMain.hd / 10, var4.y * AvMain.hd / 10, 0, var1);
               }
            }
         default:
      }
   }

   public final void updateWind() {
      int var1 = 1;
      if (Canvas.gameTick % 6 == 3) {
         var1 = CRes.rnd(15);
      }

      if (var1 == 0 && wind == 5) {
         wind = 5 + CRes.rnd(20);
         countWind = 50 + CRes.rnd(100);
      }

      if (countWind > 0) {
         --countWind;
      }

      if (countWind == 0 && wind > 5 && Canvas.gameTick % 4 == 2) {
         --wind;
      }

      Point var10000;
      int var2;
      Point var3;
      AnimateEffect var5;
      switch (this.type) {
         case 0:
            var5 = this;

            for(var2 = 0; var2 < var5.number; ++var2) {
               var10000 = var3 = (Point)var5.list.elementAt(var2);
               var10000.y += (var3.h + 1) * 15 + (3 - var3.h) * 3;
               ++var3.g;
               var3.x += var3.h + 1 << 2;
               if (var3.y / 10 > AvCamera.gI().yCam + Canvas.h - (4 - var3.h) * 50) {
                  var5.rndPos(var3);
               }

               int var10001 = 2 - var3.h;
               int var4 = AvCamera.gI().xCam * var10001 * 20 / 120;
               if (var3.x / 10 + var4 < AvCamera.gI().xCam - 10) {
                  var3.x += (Canvas.w + 20) * 10;
               }

               if (var3.x / 10 + var4 > AvCamera.gI().xCam + Canvas.w + 10) {
                  var3.x -= (Canvas.w + 20) * 10;
               }
            }

            return;
         case 1:
            var5 = this;

            for(var2 = 0; var2 < var5.number; ++var2) {
               var10000 = var3 = (Point)var5.list.elementAt(var2);
               var10000.y += 10;
               var3.x += var3.v * 10 + wind * dirWind;
               ++var3.color;
               if (var3.color >= var3.limitY) {
                  var3.color = 0;
               }

               if (var3.y / 10 > LoadMap.Hmap * LoadMap.w - (4 - var3.h) * 20) {
                  var5.rndPos(var3);
               }
            }

            return;
         case 2:
            var5 = this;
            if (System.currentTimeMillis() / 1000L - (long)this.timeCur > (long)this.timeStop) {
               ++this.timeStop;

               int removeCount = var5.list.size();
               if (removeCount > 5) {
                  removeCount = 5;
               }

               for(var2 = 0; var2 < removeCount; ++var2) {
                  var5.list.removeElementAt(0);
               }

               var5.number = var5.list.size();
               if (var5.number == 0) {
                  var5.close();
                  return;
               }
            }

            for(var2 = 0; var2 < var5.number; ++var2) {
               var10000 = var3 = (Point)var5.list.elementAt(var2);
               var10000.y += (var3.h + 2) * 5;
               var3.x += (var3.h + 1 << 1) + wind * dirWind;
               if (var3.y / 10 > LoadMap.Hmap * LoadMap.w - (4 - var3.h) * 20) {
                  var5.rndPos(var3);
               }
            }

            return;
         case 3:
            var5 = this;

            for(var2 = 0; var2 < var5.number; ++var2) {
               var10000 = var3 = (Point)var5.list.elementAt(var2);
               var10000.y += (var3.h + 4) * 3;
               var3.x += (var3.h + 1 << 1) + wind * dirWind;
               if (var3.y / 10 > LoadMap.Hmap * LoadMap.w - (4 - var3.h) * 20) {
                  var5.rndPos(var3);
               }
            }
         default:
      }
   }

   private void rndPos(Point var1) {
      if (super.isStop) {
         this.list.removeElement(var1);
         this.number = this.list.size();
         if (this.list.size() == 0) {
            super.close();
            return;
         }
      } else {
         var1.y = (AvCamera.gI().yCam - Canvas.hh + CRes.rnd(Canvas.h << 1)) * 10;
         var1.x = (-Canvas.w / 2 + CRes.rnd(LoadMap.wMap * LoadMap.w + Canvas.w)) * 10;
      }

   }
}
