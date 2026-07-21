package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class Fish {
   private static int dis = 10;
   private int direct = 1;
   public byte size = 9;
   public Avatar ava;
   AvPosition[] posDay;
   AvPosition[] posTemp;
   private AvPosition[] posGoc;
   private int indexQuan = 0;
   int distant;
   private int g;
   int isQuan;
   int countQuan;
   private int radius;
   int idFish;
   private int s;
   boolean isCanCau;
   boolean isSuccess;
   boolean isWait;
   private boolean isLac;
   private AvPosition[] u;
   private AvPosition posMoi;
   private AvPosition w;
   private AvPosition x;
   public static int[] l = new int[]{12577266, 10341591};
   private int y;
   private int z;
   private int count;

   public Fish() {
      this.distant = dis;
      this.g = -8;
      this.isQuan = 0;
      this.countQuan = -1;
      this.radius = 25;
      this.idFish = -1;
      this.isCanCau = false;
      this.isSuccess = false;
      this.isWait = false;
      this.isLac = false;
      this.y = 0;
      this.z = 0;
      this.count = 0;
      this.size = (byte)(7 + CRes.rnd(4));
      this.u = new AvPosition[2];

      int var1;
      for(var1 = 0; var1 < 2; ++var1) {
         this.u[var1] = new AvPosition(-10, 0, var1 * 15);
      }

      this.posGoc = new AvPosition[2];
      this.posGoc[0] = new AvPosition();
      this.posGoc[1] = new AvPosition();
      this.posDay = new AvPosition[this.size];
      this.posTemp = new AvPosition[this.size];

      for(var1 = 0; var1 < this.size; ++var1) {
         this.posDay[var1] = new AvPosition();
         this.posTemp[var1] = new AvPosition();
      }

      this.posMoi = new AvPosition(0, 0, -1);
      this.w = new AvPosition(0, 0, -1);
      this.x = new AvPosition();
   }

   public final void doSetDayCau() {
      this.indexQuan = 0;
      this.isQuan = 0;
      this.g = -(10 + CRes.rnd(4));
      this.countQuan = -1;
      this.isCanCau = false;
      this.isSuccess = false;
      this.isLac = false;
   }

   public final void doQuanCau(Avatar var1) {
      this.ava = var1;
      if (var1.direct == 0) {
         this.direct = 1;
      } else {
         this.direct = -1;
      }

      this.doSetDayCau();
      this.countQuan = 0;
      this.idFish = 0;
      Object var4;
      if (((Part)((Part)(var4 = AvatarData.getPartByZ(var1.seriPart, 70)))).follow >= 0) {
         var4 = AvatarData.getPart(((Part)var4).follow);
      }

      APartInfo var8 = (APartInfo)var4;
      int var7 = var1.x;
      int var3 = var1.y + var1.ySat;
      try {
         if (AvatarData.listImgInfo == null || var8.imgID == null || var8.dx == null || var8.dy == null) {
            throw new Exception();
         }
         if (var8.imgID.length <= 14 || var8.dx.length <= 14 || var8.dy.length <= 14) {
            throw new Exception();
         }
         int id3 = var8.imgID[3];
         int id14 = var8.imgID[14];
         if (id3 < 0 || id14 < 0 || id3 >= AvatarData.listImgInfo.length || id14 >= AvatarData.listImgInfo.length) {
            throw new Exception();
         }
         ImageInfo var5 = AvatarData.listImgInfo[id3];
         ImageInfo var6 = AvatarData.listImgInfo[id14];
         if (var5 == null || var6 == null) {
            throw new Exception();
         }
         this.posGoc[0].x = var7 + var8.dx[3] * AvMain.hd + var5.w * AvMain.hd;
         this.posGoc[0].y = var3 + var8.dy[3] * AvMain.hd - 5 * (AvMain.hd - 1);
         this.posGoc[1].x = var7 + var8.dx[14] * AvMain.hd + var6.w * AvMain.hd;
         this.posGoc[1].y = var3 + var8.dy[14] * AvMain.hd - 5 * (AvMain.hd - 1);
      } catch (Exception var9) {
         // Fallback tránh crash khi thiếu/khác version imgID
         this.posGoc[0].x = var7;
         this.posGoc[0].y = var3;
         this.posGoc[1].x = var7;
         this.posGoc[1].y = var3;
      }
      this.posMoi.anchor = -1;
      if (var1.IDDB == GameMidlet.avatar.IDDB) {
         MapScr.gI();
         MapScr.doAction((byte)13);
      }

   }

   public final void doQuanDay() {
      ++this.indexQuan;
      this.distant = dis;

      for(int var1 = 0; var1 < this.size; ++var1) {
         this.posDay[var1].x = this.posGoc[1].x;
         this.posDay[var1].y = this.posGoc[1].y;
      }

   }

   public final void setPosDay(int var1) {
      this.posDay[0].x = this.posGoc[var1].x;
      this.posDay[0].y = this.posGoc[var1].y;
      if (var1 == 1) {
         this.ava.action = 13;
      } else {
         this.ava.action = 2;
      }

   }

   public final void update() {
      if (this.ava != null) {
         ++this.count;
         if (this.count >= 6) {
            this.count = 0;
         }

         Fish var1 = this;
         int var2;
         int var3;
         int var5;
         AvPosition var10000;
         boolean var13;
         if (this.indexQuan != 0) {
            if (this.isQuan == 1) {
               for(var2 = 1; var2 < var1.size - 2; ++var2) {
                  var10000 = var1.posDay[var2];
                  var10000.y += 6;
               }

               label269: {
                  Fish var10 = var1;
                  if (var1.isLac && var1.idFish > 0) {
                     ++var1.y;
                     if (var1.y < 2) {
                        var3 = 1;

                        while(true) {
                           if (var3 >= var10.size) {
                              break label269;
                           }

                           var10000 = var10.posDay[var3];
                           var10000.x -= 6;
                           ++var3;
                        }
                     }

                     if (var1.y > 4 && var1.y < 8) {
                        var3 = 1;

                        while(true) {
                           if (var3 >= var10.size) {
                              break label269;
                           }

                           var10000 = var10.posDay[var3];
                           var10000.x += 6;
                           ++var3;
                        }
                     }

                     if (var1.y <= 14) {
                        break label269;
                     }

                     --var1.z;
                     if (var1.z >= 0) {
                        break label269;
                     }

                     var1.y = 0;
                  }

                  var1.z = CRes.rnd(20);
               }

               if (var1.distant == dis) {
                  var1.distant = 7;
               }
            }

            var13 = false;
            var3 = var1.size - 1;
            byte var4 = 1;
            if (var1.isSuccess) {
               var4 = 0;
            }

            int var6;
            int var7;
            int var8;
            for(var5 = 1; var5 < var1.size - var1.isQuan * var4; ++var5) {
               if ((var6 = CRes.distance(var1.posDay[var5].x, var1.posDay[var5].y, var1.posDay[var5 - 1].x, var1.posDay[var5 - 1].y)) > var1.distant + 1) {
                  var13 = true;
                  var7 = var6 - var1.distant;
                  var6 = CRes.tan(var1.posDay[var5 - 1].x - var1.posDay[var5].x, -(var1.posDay[var5 - 1].y - var1.posDay[var5].y));
                  var8 = var7 * CRes.cos(CRes.fixangle(var6)) >> 10;
                  var6 = -(var7 * CRes.sin(CRes.fixangle(var6))) >> 10;
                  var10000 = var1.posDay[var5];
                  var10000.x += var8;
                  var10000 = var1.posDay[var5];
                  var10000.y += var6;
               }
            }

            if (var1.posDay[var3].y < var1.ava.y + var1.ava.ySat + 5) {
               var10000 = var1.posDay[var3];
               var10000.x += 10;
               var10000 = var1.posDay[var3];
               var10000.y += var1.g;
               ++var1.g;
            }

            if (!var1.isSuccess) {
               for(var5 = var3 - 1; var5 > 0; --var5) {
                  if ((var6 = CRes.distance(var1.posDay[var5].x, var1.posDay[var5].y, var1.posDay[var5 + 1].x, var1.posDay[var5 + 1].y)) > var1.distant + 1) {
                     var13 = true;
                     var7 = CRes.tan(var1.posDay[var5 + 1].x - var1.posDay[var5].x, -(var1.posDay[var5 + 1].y - var1.posDay[var5].y));
                     var8 = (var6 -= var1.distant) * CRes.cos(CRes.fixangle(var7)) >> 10;
                     var6 = -(var6 * CRes.sin(CRes.fixangle(var7))) >> 10;
                     var10000 = var1.posDay[var5];
                     var10000.x += var8;
                     var10000 = var1.posDay[var5];
                     var10000.y += var6;
                  }
               }
            }

            if (!var13) {
               var1.isQuan = 1;
            }
         }

         if (this.countQuan != -1) {
            ++this.countQuan;
            if (Canvas.gameTick % 4 == 2) {
               if (this.ava.action == 2) {
                  this.ava.action = 13;
                  if (this.countQuan > 16) {
                     this.doQuanDay();
                     this.countQuan = -1;
                  }
               } else {
                  this.ava.action = 2;
               }
            }
         }

         var1 = this;
         if (this.isCanCau) {
            if (this.distant > 4 && Canvas.gameTick % 6 == 3) {
               --this.distant;
            }

            if (!this.isSuccess && Canvas.gameTick % 6 == 3 && this.ava != GameMidlet.avatar) {
               if (this.ava.action == 2) {
                  this.setPosDay(1);
               } else {
                  this.setPosDay(0);
               }
            }

            if (this.isSuccess && this.distant <= 4) {
               this.distant = 2;
               var2 = 0;
               if (!this.isLac) {
                  for(var3 = 0; var3 < var1.size - 1; ++var3) {
                     var10000 = var1.posDay[var3];
                     var13 = true;
                     var5 = var1.posDay[var3 + 1].x;
                     if (CRes.abs(var5 - var10000.x) > 1) {
                        ++var2;
                     }
                  }
               }

               if (var2 == 0 && !var1.isLac) {
                  var1.posMoi.anchor = -2;
                  var1.isLac = true;
               }
            }
         }

         if (this.isWait && (this.ava.action == 2 || this.ava.action == 13)) {
            this.doQuanCau(this.ava);
            this.isWait = false;
         }

         if (this.isQuan != 0) {
            var1 = this;

            for(var2 = 0; var2 < 2; ++var2) {
               if (var1.u[var2].anchor == 0 || var1.u[var2].x == -10) {
                  var1.u[var2].x = var1.posTemp[var1.size - 2].x;
                  var1.u[var2].y = var1.posTemp[var1.size - 2].y;
               }

               if (var1.isCanCau) {
                  var10000 = var1.u[var2];
                  var10000.anchor += 2;
               } else {
                  ++var1.u[var2].anchor;
               }

               if (var1.u[var2].anchor > var1.radius + (var1.isCanCau ? 10 : 0)) {
                  var1.u[var2].anchor = 0;
               }
            }
         }

         if (!this.isSuccess && this.isQuan == 1) {
            if (this.posMoi.anchor == -1) {
               this.posMoi.x = this.w.x = this.x.x = this.posDay[this.size - 1].x;
               this.posMoi.y = this.w.y = this.x.y = this.posDay[this.size - 1].y;
               this.posMoi.anchor = 0;
               this.s = -1;
            }

            var2 = this.x.x - this.w.x;
            var3 = this.x.y - this.w.y;
            if (this.s > 0) {
               --this.s;
            }

            if ((this.s <= 0 || this.isCanCau) && Canvas.gameTick % 2 == 1) {
               if (CRes.abs(var2) > 0) {
                  if (var2 > 0) {
                     --this.x.x;
                  } else {
                     ++this.x.x;
                  }

                  this.posDay[this.size - 1].x = this.x.x;
               }

               if (CRes.abs(var3) > 0) {
                  if (var3 > 0) {
                     --this.x.y;
                  } else {
                     ++this.x.y;
                  }

                  this.posDay[this.size - 1].y = this.x.y;
               }
            }

            if (CRes.abs(var2) <= 0 && CRes.abs(var3) <= 0) {
               this.s = 50 + CRes.rnd(100);
               this.w.x = this.posMoi.x + 10 - CRes.rnd(20);
               this.w.y = this.posMoi.y + CRes.rnd(6);
            }
         }

         if (this.ava.direct == 0) {
            this.direct = 1;
         } else {
            this.direct = -1;
         }

         for(int var9 = 0; var9 < this.size; ++var9) {
            var2 = this.posDay[var9].x - this.ava.x;
            if (var9 != this.size - 2 || CRes.abs(this.posTemp[var9].x - (this.ava.x + this.direct * var2)) > 1) {
               this.posTemp[var9].x = this.ava.x * AvMain.hd + this.direct * var2;
            }

            this.posTemp[var9].y = this.posDay[var9].y;
         }
      }

   }

   public final void paint(Graphics var1) {
      if (!this.isWait && this.countQuan == -1) {
         if (AvMain.hd > 1) {
            var1.translate(0, this.ava.y);
         }

         int var2;
         if (this.isQuan != 0 && !this.isSuccess && this.u[0].x > 0 && this.u[0].x > AvCamera.gI().xCam && this.u[0].x < AvCamera.gI().xCam + Canvas.w) {
            var1.setColor(l[LoadMap.status]);

            for(var2 = 0; var2 < 2; ++var2) {
               var1.drawRoundRect(this.u[var2].x - this.u[var2].anchor / 2, this.u[var2].y - this.u[var2].anchor / 4, this.u[var2].anchor, this.u[var2].anchor / 2, this.u[var2].anchor, this.u[var2].anchor);
            }
         }

         var1.setColor(8685448);
         if (this.posTemp[0].x > AvCamera.gI().xCam && this.posTemp[0].x < AvCamera.gI().xCam + Canvas.w || this.posTemp[this.size - 1].x > AvCamera.gI().xCam && this.posTemp[this.size - 1].x < AvCamera.gI().xCam + Canvas.w) {
            for(var2 = 0; var2 < this.size - 1 - this.isQuan; ++var2) {
               if (this.posTemp[var2 + 1].y < this.ava.y + this.ava.ySat + 20) {
                  var1.drawLine(this.posTemp[var2].x, this.posTemp[var2].y, this.posTemp[var2 + 1].x, this.posTemp[var2 + 1].y);
               }
            }

            if (this.isQuan == 0 && this.posTemp[this.size - 1].y < this.ava.y + this.ava.ySat + 10) {
               PaintPopup.fill(this.posTemp[this.size - 1].x, this.posTemp[this.size - 1].y, 2, 2, 0, var1);
            }

            int var10002 = this.posTemp[this.size - 2].x;
            int var10003 = this.posTemp[this.size - 2].y;
            var1.drawImage(FishingScr.gI().imgPhao, var10002, var10003, 3);
            if (this.isSuccess && this.idFish > 0) {
               int var10001 = 0 + this.count / 3;
               var10002 = this.posTemp[this.size - 2].x + 2;
               var10003 = this.posTemp[this.size - 2].y + 4;
               FishingScr.gI().imgCa.drawFrame(var10001, var10002, var10003, 0, 24, var1);
               Part var3;
               if (Canvas.gameTick % 10 > 5 && (var3 = AvatarData.getPart((short)this.idFish)) != null) {
                  var3.paint(var1, this.ava.x * AvMain.hd, this.ava.y - 55 * AvMain.hd, 3);
               }
            }
         }

         if (AvMain.hd > 1) {
            var1.translate(0, -this.ava.y);
         }
      }

   }
}
