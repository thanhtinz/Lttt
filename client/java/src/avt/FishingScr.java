package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

import java.util.Vector;

public final class FishingScr extends MyScreen {
   private static FishingScr me;
   private Command cmdQuanCau;
   private Command cmdClose;
   private Command cmdXong;
   private Command cmdSetting;
   public Image imgPhao;
   public FrameImage imgCa;
   private Fish fish = new Fish();
   private int h;
   private int i;
   private int j;
   private Image[] imgArrow;
   private int index = 0;
   private byte[] arrIndex;
   private long cTime;
   private short timeDelay;
   private int iCancau;
   private int xKeyArr;
   private int yKeyArr;
   private boolean isAutoTossAfterEnd;
   private long lastAutoArrowPressTime;
   private static final boolean AUTO_PRESS_ARROWS = true;
   private byte[] arrowDirCache;
   private int lastAutoPressedArrowIndex = -1;

   public static FishingScr gI() {
      return me == null ? (me = new FishingScr()) : me;
   }

   public final void commandTab(int n, int n2) {
      switch (n) {
         case 0:
            if (GameMidlet.avatar.action != 2 && GameMidlet.avatar.action != 13) {
               MapScr.gI().switchToMe();
            }

            ParkService a;
            (a = ParkService.gI()).createMessage((byte)82);
            a.sendMessage();
            Canvas.startWaitDlg();
            super.center = null;
            return;
         case 1:
            ParkService.gI().doCauCaXong();
            Canvas.startWaitDlg();
            return;
         case 2:
            this.doClose();
            ParkService.gI().doCauCaXong();
            return;
         case 3:
            this.showFishingSettingMenu();
            return;
         case 10:
         case 11:
         case 12:
            Canvas.addFlyTextSmall(T.fishDeveloping, GameMidlet.avatar.x, GameMidlet.avatar.y - 40, -1, 1, -1);
            return;
         default:
      }
   }

   public FishingScr() {
      this.cmdQuanCau = new Command(T.toss, 0);
      this.cmdXong = new Command(T.finish, 1);
      this.cmdClose = new Command(T.close, 2);
      this.cmdSetting = new Command(T.option, 3);
      super.center = this.cmdQuanCau;
      FilePack.init(T.av);
      this.imgPhao = FilePack.getImage("cucphao");
      this.imgCa = FrameImage.init("ca", 14 * AvMain.hd, 14 * AvMain.hd);
      FilePack.reset();
      this.j = 530;
   }

   private void doClose() {
      GameMidlet.avatar.resetTypeChair();
      Avatar k;
      if (GameMidlet.avatar.direct == 0) {
         k = GameMidlet.avatar;
         k.x -= 18;
      } else {
         k = GameMidlet.avatar;
         k.x += 18;
      }

      k = GameMidlet.avatar;
      k.y -= 10;
      AvCamera.setDistance(Canvas.w / 10);
      MapScr.listFish.removeElement(this.fish);
      MapScr.gI().switchToMe();
   }

   public final boolean doSat(int n, int n2) {
      this.yKeyArr = Canvas.h - Canvas.h / 4;
      if (this.yKeyArr > Canvas.h - 70 * AvMain.hd) {
         this.yKeyArr = Canvas.h - 70 * AvMain.hd;
      }

      this.xKeyArr = 60;
      if (this.xKeyArr < (Canvas.w - LoadMap.wMap * 24) / 2 + 50 * AvMain.hd) {
         this.xKeyArr = (Canvas.w - LoadMap.wMap * 24) / 2 + 50 * AvMain.hd;
      }

      this.index = 0;
      int d = LoadMap.getposMap(n, n2);
      if (LoadMap.map[d + 1] != 100 && LoadMap.map[d + 1] != 16 && LoadMap.map[d + 1] != 13) {
         GameMidlet.avatar.direct = Base.LEFT;
      } else {
         GameMidlet.avatar.direct = 0;
         this.xKeyArr = Canvas.w - this.xKeyArr;
      }

      GameMidlet.avatar.setLayPLayer(n, n2);
      ParkService a;
      (a = ParkService.gI()).createMessage((byte)86);
      a.sendMessage();
      Canvas.startWaitDlg();
      super.left = this.cmdSetting;
      super.right = this.cmdClose;
      Canvas.clearKeyHold();
      return true;
   }

   private void showFishingSettingMenu() {
      ClientUtilities.openFishingSettingsSubmenu();
   }

   public final void update() {
      MapScr.gI().update();
      if (this.fish.isCanCau && !this.fish.isSuccess) {
         if (this.index < this.arrIndex.length && System.currentTimeMillis() - this.cTime > (long)this.timeDelay) {
            this.setIndex(0);
         }

         this.autoPressArrowIfNeeded();

         if (GameMidlet.avatar.action == 2) {
            --this.iCancau;
            if (this.iCancau < 0) {
               this.iCancau = 0;
               this.fish.setPosDay(1);
            }
         }
      }

   }

   private static boolean isSimilarColor(int c1, int c2) {
      if (c1 == c2) {
         return true;
      }

      int r1 = (c1 & 16711680) >> 16;
      int g1 = (c1 & 65280) >> 8;
      int b1 = c1 & 255;

      int r2 = (c2 & 16711680) >> 16;
      int g2 = (c2 & 65280) >> 8;
      int b2 = c2 & 255;

      return Math.abs(r1 - r2) < 30 && Math.abs(g1 - g2) < 30 && Math.abs(b1 - b2) < 30;
   }

   private void autoPressArrowIfNeeded() {
      if (!AUTO_PRESS_ARROWS || this.timeDelay == -1 || this.imgArrow == null || this.index >= this.imgArrow.length) {
         return;
      }

      long now = System.currentTimeMillis();
      if (this.lastAutoPressedArrowIndex == this.index || now - this.lastAutoArrowPressTime < 50L) {
         return;
      }

      int dir = this.getCachedArrowDirection(this.index);
      if (dir == -1) {
         dir = this.detectArrowDirection(this.imgArrow[this.index]);
         this.cacheArrowDirection(this.index, dir);
      }

      if (dir >= 1 && dir <= 4) {
         this.lastAutoArrowPressTime = now;
         this.lastAutoPressedArrowIndex = this.index;
         this.setIndex(dir);
      }
   }

   private int getCachedArrowDirection(int arrowIndex) {
      if (this.arrowDirCache == null || arrowIndex < 0 || arrowIndex >= this.arrowDirCache.length) {
         return -1;
      }

      return this.arrowDirCache[arrowIndex];
   }

   private void cacheArrowDirection(int arrowIndex, int direction) {
      if (this.arrowDirCache != null && arrowIndex >= 0 && arrowIndex < this.arrowDirCache.length) {
         this.arrowDirCache[arrowIndex] = (byte)direction;
      }
   }

   private int detectArrowDirection(Image img) {
      if (img == null) {
         return 0;
      }

      int width = img.getWidth();
      int height = img.getHeight();
      if (width <= 0 || height <= 0) {
         return 0;
      }

      int[] pixels = new int[width * height];
      try {
         img.getRGB(pixels, 0, width, 0, 0, width, height);
      } catch (Throwable t) {
         return 0;
      }

      int[] bounds = new int[]{-1, -1, -1, -1};
      int[] vars = new int[7];

      int y = 0;
      while (y < 10 && y < height) {
         for (vars[6] = width / 2; vars[6] >= width / 2 - 10 && vars[6] >= 0; --vars[6]) {
            vars[5] = 0;
            vars[4] = 0;
            vars[3] = vars[6];
            vars[2] = y + 1;
            while (vars[2] < height) {
               vars[1] = -1;
               vars[0] = 0;
               while (vars[0] < width / 2) {
                  int c1 = pixels[vars[2] * width + vars[0]];
                  int c2 = pixels[y * width + vars[6]];
                  if (isSimilarColor(c1, c2) && (pixels[vars[2] * width + vars[0] + 1] & 16777215) != 0) {
                     vars[1] = vars[0];
                     break;
                  }
                  ++vars[0];
               }
               if (vars[1] == -1 || vars[1] > vars[3] + 3 || vars[1] < vars[3] - 3) {
                  break;
               }
               if (vars[1] < vars[3]) {
                  ++vars[5];
                  vars[4] = 0;
                  vars[3] = vars[1];
               }
               if (++vars[4] > 3) {
                  break;
               }
               ++vars[2];
            }
            if (vars[5] > 3) {
               bounds[0] = vars[6];
               break;
            }
         }
         ++y;
      }

      y = 0;
      while (y < 10 && y < height) {
         for (vars[6] = width / 2; vars[6] < width / 2 + 10 && vars[6] < width; ++vars[6]) {
            vars[5] = 0;
            vars[4] = 0;
            vars[3] = vars[6];
            vars[2] = y + 1;
            while (vars[2] < height) {
               vars[1] = -1;
               vars[0] = width - 1;
               while (vars[0] >= width / 2) {
                  int c1 = pixels[vars[2] * width + vars[0]];
                  int c2 = pixels[y * width + vars[6]];
                  if (isSimilarColor(c1, c2) && (pixels[vars[2] * width + vars[0] - 1] & 16777215) != 0) {
                     vars[1] = vars[0];
                     break;
                  }
                  --vars[0];
               }
               if (vars[1] == -1 || vars[1] < vars[3] - 3 || vars[1] > vars[3] + 3) {
                  break;
               }
               if (vars[1] > vars[3]) {
                  ++vars[5];
                  vars[4] = 0;
                  vars[3] = vars[1];
               }
               if (++vars[4] > 3) {
                  break;
               }
               ++vars[2];
            }
            if (vars[5] > 3) {
               bounds[1] = vars[6];
               break;
            }
         }
         ++y;
      }

      y = height - 1;
      while (y >= height - 10 && y >= 0) {
         for (vars[6] = width / 2; vars[6] >= width / 2 - 10 && vars[6] >= 0; --vars[6]) {
            vars[5] = 0;
            vars[4] = 0;
            vars[3] = vars[6];
            vars[2] = y - 1;
            while (vars[2] >= 0) {
               vars[1] = -1;
               vars[0] = 0;
               while (vars[0] < width / 2) {
                  int c1 = pixels[vars[2] * width + vars[0]];
                  int c2 = pixels[y * width + vars[6]];
                  if (isSimilarColor(c1, c2) && (pixels[vars[2] * width + vars[0] + 1] & 16777215) != 0) {
                     vars[1] = vars[0];
                     break;
                  }
                  ++vars[0];
               }
               if (vars[1] == -1 || vars[1] > vars[3] + 3 || vars[1] < vars[3] - 3) {
                  break;
               }
               if (vars[1] < vars[3]) {
                  ++vars[5];
                  vars[4] = 0;
                  vars[3] = vars[1];
               }
               if (++vars[4] > 3) {
                  break;
               }
               --vars[2];
            }
            if (vars[5] > 3) {
               bounds[2] = vars[6];
               break;
            }
         }
         --y;
      }

      y = height - 1;
      while (y >= height - 10 && y >= 0) {
         for (vars[6] = width / 2; vars[6] < width / 2 + 10 && vars[6] < width; ++vars[6]) {
            vars[5] = 0;
            vars[4] = 0;
            vars[3] = vars[6];
            vars[2] = y - 1;
            while (vars[2] >= 0) {
               vars[1] = -1;
               vars[0] = width - 1;
               while (vars[0] >= width / 2) {
                  int c1 = pixels[vars[2] * width + vars[0]];
                  int c2 = pixels[y * width + vars[6]];
                  if (isSimilarColor(c1, c2) && (pixels[vars[2] * width + vars[0] - 1] & 16777215) != 0) {
                     vars[1] = vars[0];
                     break;
                  }
                  --vars[0];
               }
               if (vars[1] == -1 || vars[1] < vars[3] - 3 || vars[1] > vars[3] + 3) {
                  break;
               }
               if (vars[1] > vars[3]) {
                  ++vars[5];
                  vars[4] = 0;
                  vars[3] = vars[1];
               }
               if (++vars[4] > 3) {
                  break;
               }
               --vars[2];
            }
            if (vars[5] > 3) {
               bounds[3] = vars[6];
               break;
            }
         }
         --y;
      }

      if (bounds[0] > 0 && bounds[1] > 0 && bounds[2] > 0 && bounds[3] > 0) {
         if (bounds[0] < bounds[1] && bounds[0] < bounds[2]) {
            bounds[0] = -1;
         } else if (bounds[1] < bounds[0] && bounds[1] < bounds[2]) {
            bounds[1] = -1;
         } else {
            bounds[2] = -1;
         }
      }

      if (bounds[0] > 0 && bounds[1] > 0 && bounds[3] > 0) {
         if (bounds[0] < bounds[1] && bounds[0] < bounds[3]) {
            bounds[0] = -1;
         } else if (bounds[1] < bounds[0] && bounds[1] < bounds[3]) {
            bounds[1] = -1;
         } else {
            bounds[3] = -1;
         }
      }

      if (bounds[1] > 0 && bounds[2] > 0 && bounds[3] > 0) {
         if (bounds[1] < bounds[2] && bounds[1] < bounds[3]) {
            bounds[1] = -1;
         } else if (bounds[2] < bounds[1] && bounds[2] < bounds[3]) {
            bounds[2] = -1;
         } else {
            bounds[3] = -1;
         }
      }

      if (bounds[0] > 0 && bounds[1] > 0) {
         return 2;
      } else if (bounds[2] > 0 && bounds[3] > 0) {
         return 4;
      } else if (bounds[0] > 0 && bounds[2] > 0) {
         return 1;
      } else if (bounds[1] > 0 && bounds[3] > 0) {
         return 3;
      } else {
         return 0;
      }
   }

   public final void keyPress(int n) {
      if (this.fish.isCanCau && !this.fish.isSuccess) {
         switch (n) {
            case 50:
            case 52:
            case 54:
            case 56:
               Canvas.keyPressed[n - 48] = true;
            case 51:
            case 53:
            case 55:
            default:
         }
      } else {
         MapScr.gI().keyPress(n);
      }
   }

   public final void updateKey() {
      if (this.fish.isCanCau && !this.fish.isSuccess) {
         if (Canvas.isKeyPressed(2)) {
            this.setIndex(2);
         } else if (Canvas.isKeyPressed(4)) {
            this.setIndex(1);
         } else if (Canvas.isKeyPressed(6)) {
            this.setIndex(3);
         } else if (Canvas.isKeyPressed(8)) {
            this.setIndex(4);
         }
      }

      super.updateKey();
   }

   private void setIndex(int n) {
      this.cTime = System.currentTimeMillis();
      if (this.index < this.arrIndex.length) {
         this.arrIndex[this.index] = (byte)n;
      }

      ++this.index;
      if (GameMidlet.avatar.action != 2) {
         this.fish.setPosDay(0);
         this.iCancau = 2;
      }

      if (this.index >= this.arrIndex.length) {
         this.fish.setPosDay(0);
         this.fish.isSuccess = true;
         ParkService.gI().doFinishFishing(true, this.arrIndex);
         Canvas.startWaitDlg();
      }

   }

   public final void paint(Graphics graphics) {
      MapScr.gI().paintMain(graphics);
      if (this.fish.isCanCau && !this.fish.isSuccess && this.timeDelay != -1) {
         Canvas.resetTrans(graphics);
         graphics.translate(-AvCamera.gI().xCam, -AvCamera.gI().yCam);
         graphics.setColor(8575990);
         if (this.imgArrow != null && this.index < this.imgArrow.length) {
            Image curArrow = this.imgArrow[this.index];
            if (curArrow == null) {
               super.paint(graphics);
               return;
            }
            if (System.currentTimeMillis() - this.cTime > 50L) {
               graphics.setColor(1423411);
            } else {
               graphics.setColor(15612731);
            }

            graphics.fillRoundRect(this.h - 1, this.i * AvMain.hd - 1, curArrow.getWidth() + 2, curArrow.getHeight() + 2, 5, 5);
            graphics.drawImage(curArrow, this.h, this.i * AvMain.hd, 0);
         }
      }

      super.paint(graphics);
   }

   public final void onQuanCau(int n) {
      Avatar g;
      if ((g = LoadMap.getAvatar(n)) != null) {
         Fish c;
         if ((c = getFish(g.IDDB)) != null) {
            MapScr.listFish.removeElement(c);
         }

         Fish fish = new Fish();
         if (g.IDDB == GameMidlet.avatar.IDDB) {
            Canvas.endDlg();
            this.fish = fish;
         } else {
            fish = new Fish();
         }

         MapScr.listFish.addElement(fish);
         if (g.action != 2) {
            if (g.IDDB != GameMidlet.avatar.IDDB) {
               fish.ava = g;
               fish.isWait = true;
            }

            return;
         }

         fish.doQuanCau(g);
      }

   }

   public final void onCaCanCau(int i, int h, short o, byte[][] array) {
      Fish c;
      if ((c = getFish(i)) != null && c.isQuan != 0) {
         if (c.ava.action != 13 && c.ava.action != 2 || c.isCanCau) {
            return;
         }

         c.isCanCau = true;
         c.setPosDay(0);
         c.ava.action = 2;
         c.idFish = h;
         if (o != -1) {
            Canvas.addFlyTextSmall(T.bite, c.ava.x, c.ava.y - 60, -1, 1, -1);
         }

         if (i == GameMidlet.avatar.IDDB) {
            this.cTime = System.currentTimeMillis();
            this.index = 0;
            this.iCancau = 2;
            this.lastAutoArrowPressTime = 0L;
            this.lastAutoPressedArrowIndex = -1;
            this.imgArrow = new Image[array.length];
            this.arrIndex = new byte[array.length];
            this.arrowDirCache = new byte[array.length];
            for (int k = 0; k < this.arrowDirCache.length; ++k) {
               this.arrowDirCache[k] = -1;
            }

            for(i = 0; i < this.imgArrow.length; ++i) {
               this.imgArrow[i] = CRes.createImage(array[i]);
            }

            this.timeDelay = o;
            this.h = this.fish.posTemp[this.fish.size - 2].x;
            this.i = this.fish.posTemp[this.fish.size - 2].y - 30;
            if (o == -1) {
               this.setIndex(0);
            }
         }
      }

   }

   public final void onFinish(int n, int h) {
      Fish c;
      if ((c = getFish(n)) != null) {
         if (c.ava.action != 2 && c.ava.action != 13) {
            MapScr.listFish.removeElement(c);
            return;
         }

         if (h < 0) {
            Canvas.addFlyTextSmall(T.miss, c.ava.x, c.ava.y - 60, -1, 1, -1);
         }

         c.idFish = h;
         c.isSuccess = true;
         c.setPosDay(0);
         if (c.ava.IDDB == GameMidlet.avatar.IDDB) {
            Canvas.endDlg();
            if (h >= 0) {
               short fishId = (short)h;
               // Hiệu ứng: ảnh cá bay lên (giống item bay của DialLuckyScr) - cắt đúng icon từ listImgInfo để khỏi sai ảnh
               try {
                  short partId = ClientUtilities.fishVisualPartId(fishId);
                  Part p = AvatarData.getPart(partId);
                  if (p != null && p.idIcon > 0 && AvatarData.listImgInfo != null && p.idIcon < AvatarData.listImgInfo.length) {
                     ImageInfo ii = AvatarData.listImgInfo[p.idIcon];
                     if (ii != null && AvatarData.getBigImgInfo(ii.bigID) != null && AvatarData.getBigImgInfo(ii.bigID).img != null) {
                        Image icon = CRes.createRGBImage(ii.x0 * AvMain.hd, ii.y0 * AvMain.hd, ii.w * AvMain.hd, ii.h * AvMain.hd, AvatarData.getBigImgInfo(ii.bigID).img);
                        if (icon != null) {
                           // Bay rõ ràng: dùng addFlyImage (dir=1) với icon đã crop đúng kiểu quay số
                           Canvas.addFlyImage(c.ava.x, c.ava.y - 60, icon, 0);
                        }
                     }
                  } else {
                     // fallback: thử vẽ theo idFish (trường hợp part dynamic/icon ngoài list)
                     Canvas.addFlyText(0, c.ava.x, c.ava.y - 60, -1, -1, -1, partId);
                  }
               } catch (Throwable t) {
               }
               ClientUtilities.onFishingCaught(fishId);
            }
            this.isAutoTossAfterEnd = true;
            ParkService.gI().doCauCaXong();
            Canvas.startWaitDlg();
         }
      }

   }

   public static Fish getFish(int n) {
      for(int i = 0; i < MapScr.listFish.size(); ++i) {
         Fish fish;
         if ((fish = (Fish)MapScr.listFish.elementAt(i)).ava.IDDB == n) {
            return fish;
         }
      }

      return null;
   }

   public final void onCauCaXong(int param1) {
      Canvas.endDlg();
      if (this.isAutoTossAfterEnd) {
         this.isAutoTossAfterEnd = false;
         // Buy bait after each fishing end, then toss again.
         try {
            AvatarService.gI().doBuyItem(448, 1);
         } catch (Throwable t) {
         }
         ParkService a;
         (a = ParkService.gI()).createMessage((byte)82);
         a.sendMessage();
         Canvas.startWaitDlg();
         super.center = null;
      }
   }

   public final void onStartFishing(boolean b, String s) {
      if (b) {
         this.fish.doSetDayCau();
         super.center = this.cmdQuanCau;
         this.switchToMe();
         AvCamera.setDistance(Canvas.w / 3);
         Canvas.endDlg();
         ParkService a;
         (a = ParkService.gI()).createMessage((byte)82);
         a.sendMessage();
         Canvas.startWaitDlg();
         super.center = null;
      } else {
         Canvas.startOK(s, 0, this);
      }
   }

   public final void commandActionPointer(int n) {
      switch (n) {
         case 0:
            this.doClose();
         default:
      }
   }

   public static void onStatus(int n, int n2) {
      Avatar g;
      if ((g = LoadMap.getAvatar(n)) != null && (g.action == 2 || g.action == 13)) {
         Fish obj = new Fish();
         MapScr.listFish.addElement(obj);
         obj.doQuanCau(g);
         obj.doQuanDay();
         obj.posDay[obj.size - 1].x = g.x + 70 + (AvMain.hd - 1) * 35 + CRes.rnd(25);
         obj.posDay[obj.size - 1].y = g.y;
         obj.isQuan = 1;
         obj.countQuan = -1;
         obj.setPosDay(1);
         if (n2 == 2) {
            obj.isCanCau = true;
            return;
         }

         if (n2 == 3) {
            obj.isCanCau = true;
            obj.isSuccess = true;
            obj.distant = 2;
         }
      }

   }

   public final void commandActionPointer(int n, int n2) {
   }

   public final void onInfo(int n, byte b, byte b2, int n2, short n3) {
      Avatar g;
      if ((g = LoadMap.getAvatar(n)) == null && ListScr.tempList != null) {
         for(int i = 0; i < ListScr.tempList.size(); ++i) {
            Avatar avatar;
            if ((avatar = (Avatar)ListScr.tempList.elementAt(i)).IDDB == n) {
               g = avatar;
            }
         }
      }

      if (g != null) {
         Vector vector;
         (vector = new Vector()).addElement(new CommandInfo(this, (String)null, 0, g, b, b2, n2, n3));
         PopupShop.gI().addElement(new String[]{T.viewInfo}, new Vector[1], vector);
         PopupShop.gI().switchToMe();
      }

      Canvas.endDlg();
   }
}
