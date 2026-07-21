package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class RaceScr extends MyScreen implements IChatable {
   public static RaceScr me;
   private Command cmdChangeFocus;
   private Command r;
   public Command b;
   private Command s;
   private Command t;
   private Command u;
   public PetRace[] listPet;
   private short timeRemain;
   private boolean isRace;
   public boolean isStart;
   private boolean isEnd;
   private long curTime;
   public byte countStart = 0;
   public byte nWin = 1;
   public byte indexFocus;
   public static Image imgWater;
   public static Image imgFire;
   public static Image[] imgBui;
   public static Image[] imgTe;
   private ChatPopup myChat;
   public dialogWin diaWin;
   public static byte[][] FRAME;
   private int wPopup;
   private int hPopup;
   private int xPopup;
   private int yPopup;
   private int xInfo;
   private int yInfo;
   private int wInfo;
   private int Hinfo;
   private int xDC;
   private int yDC;
   private int wDC;
   private int hDC;
   private int xSelectDC;
   private int ySelectDC;
   private int wSelectDC;
   private int hSelectDC;
   private FrameImage imgInfo;
   private FrameImage imgBackpet;
   private FrameImage imgTime;
   private FrameImage imgBackMoney;
   private boolean isDC = false;
   private byte countCloseDC = 0;
   private byte W;
   public short timeStart = 0;
   public long curTimeStart;
   public Vector p = new Vector();
   private int idPet;
   private int countChangePetInfo;
   private int indexPet = -1;
   private int indexMoney = 0;
   private int indexDC = 0;
   private byte timeOpen;
   private boolean isTran = false;
   private long count;
   private long timeDelay;
   private int[] iMoney = new int[]{100, 500, 1000, 2000, 5000, 10000, 20000, 30000, 50000};
   private boolean isPetInfo = false;
   private short idImgPeInfo;
   private short numWin;
   private String namePetInfo;
   private byte ratePetInfo;
   private byte phongDoPetInfo;
   private byte sucKhoePetInfo;

   public static RaceScr gI() {
      return me == null ? (me = new RaceScr()) : me;
   }

   public RaceScr() {
      (FRAME = new byte[3][])[0] = new byte[]{0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1};
      FRAME[1] = new byte[]{2, 2, 2, 3, 3, 3, 2, 2, 2, 3, 3, 3};
      FRAME[2] = new byte[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
      this.cmdChangeFocus = new Command(T.next, 3, this);
      this.s = new Command(T.datCuoc, 1, this);
      this.r = new Command(T.menu, 7, this);
      this.t = new Command(T.OK, 6, this);
      this.u = new Command(T.close, 8, this);
      this.b = new Command(T.exit, 2, this);
      this.wPopup = 220 * AvMain.hd;
      this.hPopup = 240 * AvMain.hd;
      this.xInfo = 8 * AvMain.hd;
      this.yInfo = this.yDC = 23 * AvMain.hd;
      this.wInfo = 105 * AvMain.hd;
      this.Hinfo = this.hDC = 211 * AvMain.hd;
      this.wDC = 95 * AvMain.hd;
      this.xDC = this.wPopup - this.wDC - 8 * AvMain.hd;
      this.wSelectDC = 180 * AvMain.hd + 10 * AvMain.hd + 10 * AvMain.hd;
      this.hSelectDC = 110 * AvMain.hd;
      this.xSelectDC = (Canvas.w - this.wSelectDC) / 2;
      this.ySelectDC = (Canvas.h - this.hSelectDC) / 2;
   }

   public final void switchToMe() {
      super.switchToMe();
   }

   public final void doOpenRace(PetRace[] var1, short var2, boolean var3, boolean var4) {
      this.isEnd = false;
      this.nWin = 1;
      this.idPet = -1;
      Canvas.currentDialog = null;
      Canvas.currentFace = null;
      this.isDC = false;
      int var5;
      if (imgWater == null) {
         try {
            this.imgInfo = new FrameImage(Image.createImage(T.getPath() + "/race/popup/tile1.png"), 20 * AvMain.hd, 20 * AvMain.hd);
            this.imgBackpet = new FrameImage(Image.createImage(T.getPath() + "/race/popup/bt1.png"), 31 * AvMain.hd, 31 * AvMain.hd);
            this.imgBackMoney = new FrameImage(Image.createImage(T.getPath() + "/race/popup/bt0.png"), 60 * AvMain.hd, 24 * AvMain.hd);
            this.imgTime = new FrameImage(Image.createImage(T.getPath() + "/race/popup/time.png"), 14 * AvMain.hd, 14 * AvMain.hd);
            imgWater = Image.createImage(T.getPath() + "/race/28.png");
            imgFire = Image.createImage(T.getPath() + "/race/29.png");
            imgBui = new Image[5];

            for(var5 = 0; var5 < 5; ++var5) {
               imgBui[var5] = Image.createImage(T.getPath() + "/race/bui/d0" + var5 + ".png");
            }

            imgTe = new Image[3];

            for(var5 = 0; var5 < 3; ++var5) {
               imgTe[var5] = Image.createImage(T.getPath() + "/race/bui/w" + var5 + ".png");
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

      if (!var3) {
         if (var4) {
            for(var5 = 0; var5 < LoadMap.playerLists.size(); ++var5) {
               MyObject var6;
               if ((var6 = (MyObject)LoadMap.playerLists.elementAt(var5)).catagory == 10) {
                  LoadMap.removePlayer(var6);
               }
            }
         }

         if (me != Canvas.currentMyScreen) {
            LoadMap.orderVector(LoadMap.playerLists);
            gI().switchToMe();
            LoadMap.rememMap = -1;
            this.randomPlayer(1);
            this.randomPlayer(2);
            Canvas.loadMap.load(108);
            LoadMap.removePlayer(GameMidlet.avatar);
            RaceScr var10 = gI();
            AvCamera.gI().init(LoadMap.TYPEMAP);
            var10.xPopup = (Canvas.w - var10.wPopup) / 2;
            var10.yPopup = (Canvas.h - var10.hPopup) / 2;
            var10.W = (byte)(35 * AvMain.hd);
            if (Canvas.instance.getHeight() <= 240) {
               var10.W = 30;
               var10.hPopup = 215;
               var10.Hinfo = var10.hDC = 185;
            }

            AvCamera.isFollow = false;
         }

         this.listPet = null;
         this.listPet = var1;
         if (var1 != null) {
            for(var5 = 0; var5 < 6; ++var5) {
               this.listPet[var5].x = 20;
               this.listPet[var5].y = 80 + var5 * 12;
               LoadMap.playerLists.addElement(this.listPet[var5]);
            }

            AvCamera.gI().followPlayer = this.listPet[2];
            this.indexFocus = 3;
         }

         GameMidlet.avatar.x = GameMidlet.avatar.xCur = 0;
      }

      GameMidlet.avatar.y = GameMidlet.avatar.yCur = 96 * AvMain.hd;
      this.isStart = var3;
      this.isRace = var4;
      this.timeRemain = var2;
      this.curTime = System.currentTimeMillis();
      if (var3) {
         this.countStart = 48;
         super.center = null;
         super.right = this.cmdChangeFocus;
         super.left = this.r;
      } else {
         super.left = this.r;
         super.right = null;
         super.center = null;
         if (!var4) {
            super.right = this.cmdChangeFocus;

            for(var5 = 0; var5 < 6; ++var5) {
               int var9 = 0;

               for(int var8 = 0; var8 < this.listPet[var5].numTick.length; ++var8) {
                  var9 += this.listPet[var5].numTick[var8];
                  PetRace var10000 = this.listPet[var5];
                  var10000.x += this.listPet[var5].vTick[var8] * this.listPet[var5].numTick[var8];
                  ++this.listPet[var5].count;
                  if (var9 >= (var2 - 4) * 20) {
                     break;
                  }
               }
            }
         } else {
            GlobalService.gI().doPetInfo(this.listPet[0].IDDB);
            super.center = this.s;
         }
      }

      this.myChat = new ChatPopup();
   }

   private void randomPlayer(int var1) {
      Vector var2 = new Vector();
      Vector var3 = new Vector();
      Vector var4 = new Vector();
      Vector var5 = new Vector();
      Vector var6 = new Vector();

      int var7;
      for(var7 = 0; var7 < AvatarData.listPart.length; ++var7) {
         Part var8;
         APartInfo var9;
         if ((var8 = AvatarData.listPart[var7]).follow == -1 && var8.IDPart < 2000 && var8.sell > 0 && ((var9 = (APartInfo)var8).gender == var1 || var9.gender == 0)) {
            if (var9.zOrder == 10) {
               var2.addElement(var9);
            } else if (var8.zOrder == 20) {
               var3.addElement(var9);
            } else if (var8.zOrder == 30) {
               var4.addElement(var9);
            } else if (var8.zOrder == 40) {
               var5.addElement(var9);
            } else if (var8.zOrder == 50) {
               var6.addElement(var9);
            }
         }
      }

      for(var7 = 0; var7 < 10; ++var7) {
         Avatar var10;
         (var10 = new Avatar()).gender = (byte)var1;
         SeriPart var11;
         (var11 = new SeriPart()).idPart = ((Part)var2.elementAt(CRes.rnd(var2.size()))).IDPart;
         var10.addSeri(var11);
         (var11 = new SeriPart()).idPart = ((Part)var3.elementAt(CRes.rnd(var3.size()))).IDPart;
         var10.addSeri(var11);
         (var11 = new SeriPart()).idPart = ((Part)var4.elementAt(CRes.rnd(var4.size()))).IDPart;
         var10.addSeri(var11);
         (var11 = new SeriPart()).idPart = ((Part)var5.elementAt(CRes.rnd(var5.size()))).IDPart;
         var10.addSeri(var11);
         (var11 = new SeriPart()).idPart = ((Part)var6.elementAt(CRes.rnd(var6.size()))).IDPart;
         var10.addSeri(var11);
         var10.orderSeriesPath();
         this.p.addElement(var10);
      }

   }

   public final void commandActionPointer(int var1) {
      switch (var1) {
         case 0:
            GlobalService var4;
            (var4 = GlobalService.gI()).createMessage((byte)8);
            var4.sendMessage();
            Canvas.startWaitDlg();
            return;
         case 1:
            if (this.indexMoney >= 0) {
               this.isDC = true;
               super.center = this.t;
               super.left = null;
               super.right = this.u;
               return;
            }
            break;
         case 2:
            IACctionOut var3 = new IACctionOut(this);
            if (this.isStart) {
               Canvas.startOKDlg(T.notSendSmg, var3);
               return;
            }

            Canvas.startOKDlg(T.theft, var3);
            return;
         case 3:
            AvCamera var10000 = AvCamera.gI();
            PetRace[] var10001 = this.listPet;
            byte var10004 = this.indexFocus;
            this.indexFocus = (byte)(var10004 + 1);
            var10000.followPlayer = var10001[var10004];
            if (this.indexFocus >= 6) {
               this.indexFocus = 0;
               return;
            }
         case 4:
         default:
            break;
         case 5:
            if (this.isStart || !this.isRace) {
               super.left = this.cmdChangeFocus;
            }

            super.right = null;
            return;
         case 6:
            GlobalService.gI().doDatCuoc(this.listPet[this.indexMoney].IDDB, this.iMoney[this.indexDC]);
            this.commandActionPointer(8);
            return;
         case 7:
            Vector var2;
            (var2 = new Vector()).addElement(new Command(T.sendTo, 0, this));
            var2.addElement(new Command(T.exit, 2, this));
            Menu.gI().startAt(var2, 0);
            return;
         case 8:
            super.center = this.s;
            super.left = this.r;
            super.right = null;
            this.isDC = false;
      }

   }

   public final void update() {
      if (this.timeOpen >= 0) {
         --this.timeOpen;
         if (this.timeOpen == 0) {
            this.click();
         }
      }

      if ((this.isStart || !this.isRace) && System.currentTimeMillis() - this.curTimeStart >= 1000L) {
         this.curTimeStart = System.currentTimeMillis();
         --this.timeStart;
         if (this.timeStart < 0) {
            this.timeStart = 0;
         }
      }

      GameMidlet.avatar.setPos(AvCamera.gI().xCam + Canvas.hw, AvCamera.gI().yCam + Canvas.h - 40 * AvMain.hd);
      if (System.currentTimeMillis() - this.curTime >= 1000L) {
         this.curTime = System.currentTimeMillis();
         --this.timeRemain;
         if (this.timeRemain < 0) {
            this.timeRemain = 0;
         } else {
            ++this.countChangePetInfo;
            if (this.isRace && !this.isStart && this.countChangePetInfo > 0) {
               this.countChangePetInfo = 0;
               if (this.indexMoney >= 0 && this.listPet != null && this.indexMoney < 6 && this.listPet[this.indexMoney] != null && this.listPet[this.indexMoney].IDDB != this.idPet) {
                  this.idPet = this.listPet[this.indexMoney].IDDB;
                  GlobalService.gI().doPetInfo(this.idPet);
               }
            }
         }
      }

      int var1;
      if (this.listPet != null) {
         var1 = 0;

         int var2;
         for(var2 = 0; var2 < 6; ++var2) {
            if ((this.isStart || !this.isRace) && this.listPet[var2].count >= this.listPet[var2].vTick.length) {
               ++var1;
            }
         }

         if (!this.isEnd && var1 == 6) {
            this.isEnd = true;

            for(var2 = 0; var2 < 6; ++var2) {
               LoadMap.removePlayer((MyObject)this.listPet[var2]);
            }
         }

         if (this.isEnd && this.diaWin != null) {
            this.isEnd = false;
            Canvas.currentFace = this.diaWin;
            int[] var10000 = GameMidlet.avatar.money;
            var10000[0] += this.diaWin.tienNhanDuoc;
            Canvas.addFlyText(this.diaWin.tienNhanDuoc, Canvas.hw, Canvas.h - 30 * AvMain.hd, -1, -1);
            this.diaWin = null;
         }
      }

      Canvas.loadMap.update();
      if (this.isStart && this.countStart > 0) {
         --this.countStart;
      }

      if (this.myChat != null && this.myChat.setOut()) {
         this.myChat.chats = null;
      }

      if (this.isStart || !this.isRace) {
         for(var1 = 0; var1 < LoadMap.playerLists.size(); ++var1) {
            Base var4;
            if ((var4 = (Base)LoadMap.playerLists.elementAt(var1)).catagory == 9) {
               Avatar var5 = (Avatar)var4;
               if (System.currentTimeMillis() / 1000L - (long)var5.exp > (long)var5.an) {
                  var5.exp = (int)(System.currentTimeMillis() / 1000L);
                  var5.an = (short)(CRes.rnd(10) + 6);
                  int var3;
                  if ((var3 = CRes.rnd(6)) == 1) {
                     var5.setAction((byte)0);
                  } else if (var3 == 3) {
                     var5.setAction((byte)0);
                     var5.doJumps();
                  } else if (var3 == 2) {
                     var5.setAction((byte)7);
                  } else {
                     var5.setAction((byte)2);
                  }
               }
            }
         }
      }

   }

   public final void keyPress(int var1) {
      ChatTextField.gI().startChat(var1, this);
      super.keyPress(var1);
   }

   public final void updateKey() {
      super.updateKey();
      ++this.count;
      if (Canvas.welcome == null || !Welcome.isPaintArrow) {
         super.updateKey();
      }

      if (Canvas.a(2)) {
         if (this.isDC) {
            if (this.indexDC / 3 > 0) {
               this.indexDC -= 3;
            }
         } else {
            --this.indexMoney;
            if (this.indexMoney < 0) {
               this.indexMoney = 0;
            }
         }
      } else if (Canvas.a(8)) {
         if (this.isDC) {
            if (this.indexDC / 3 < 2) {
               this.indexDC += 3;
            }
         } else {
            ++this.indexMoney;
            if (this.indexMoney > 5) {
               this.indexMoney = 5;
            }
         }
      } else if (Canvas.a(4)) {
         if (this.isDC && this.indexDC % 3 > 0) {
            --this.indexDC;
         }
      } else if (Canvas.a(6) && this.isDC && this.indexDC % 3 < 2) {
         ++this.indexDC;
      }

      if (Canvas.isPointerClick && this.listPet != null && !this.isStart && this.isRace) {
         int var1;
         if (this.isDC) {
            if (Canvas.b(this.xSelectDC + this.wSelectDC - 30 * AvMain.hd, this.ySelectDC, 30 * AvMain.hd, 30 * AvMain.hd)) {
               Canvas.isPointerClick = false;
               this.countCloseDC = 5;
               this.isTran = true;
               this.timeDelay = this.count;
            } else {
               for(var1 = 0; var1 < 9; ++var1) {
                  if (Canvas.b(this.xSelectDC + 5 * AvMain.hd + var1 % 3 * (5 * AvMain.hd + this.imgBackMoney.frameWidth), this.ySelectDC + (this.hSelectDC - 29 * AvMain.hd * 3) + var1 / 3 * 29 * AvMain.hd - 1 * AvMain.hd, 60 * AvMain.hd, 26 * AvMain.hd)) {
                     this.indexDC = var1;
                     Canvas.isPointerClick = false;
                     this.isTran = true;
                     this.timeDelay = this.count;
                     break;
                  }
               }
            }
         } else {
            for(var1 = 0; var1 < 6; ++var1) {
               if (Canvas.b(this.xPopup + this.xDC + 32 * AvMain.hd / 2 - 15 * AvMain.hd, this.yPopup + this.yDC + 3 * AvMain.hd + 35 * AvMain.hd * var1 + 31 * AvMain.hd / 2 - 15 * AvMain.hd, 31 * AvMain.hd, 31 * AvMain.hd)) {
                  this.indexPet = var1;
                  this.isTran = true;
                  Canvas.isPointerClick = false;
                  this.timeDelay = this.count;
                  break;
               }

               if (Canvas.b(this.xPopup + this.xDC + this.wDC - 1 * AvMain.hd - this.imgBackMoney.frameWidth, this.yPopup + this.yDC + 3 * AvMain.hd + 35 * AvMain.hd * var1 + 31 * AvMain.hd / 2 - 15 * AvMain.hd, 60 * AvMain.hd, 31 * AvMain.hd)) {
                  this.indexMoney = var1;
                  this.isTran = true;
                  Canvas.isPointerClick = false;
                  this.timeDelay = this.count;
                  break;
               }
            }
         }
      }

      if (this.isTran) {
         if (Canvas.isPointerDown) {
            if (this.indexDC != -1) {
               if (!Canvas.b(this.xSelectDC + 5 * AvMain.hd + this.indexDC % 3 * (5 * AvMain.hd + this.imgBackMoney.frameWidth), this.ySelectDC + (this.hSelectDC - 29 * AvMain.hd * 3) + this.indexDC / 3 * 29 * AvMain.hd - 1 * AvMain.hd, 60 * AvMain.hd, 26 * AvMain.hd)) {
                  this.indexDC = -1;
               }
            } else if (this.countCloseDC != 0) {
               if (!Canvas.b(this.xSelectDC + this.wSelectDC - 30 * AvMain.hd, this.ySelectDC, 30 * AvMain.hd, 30 * AvMain.hd)) {
                  this.countCloseDC = 0;
               }
            } else if (this.indexPet != -1) {
               if (!Canvas.b(this.xPopup + this.xDC + 32 * AvMain.hd / 2 - 15 * AvMain.hd, this.yPopup + this.yDC + 3 * AvMain.hd + 35 * AvMain.hd * this.indexPet + 31 * AvMain.hd / 2 - 15 * AvMain.hd, 31 * AvMain.hd, 31 * AvMain.hd)) {
                  this.indexPet = -1;
               }
            } else if (this.indexMoney != -1 && !this.isDC && !Canvas.b(this.xPopup + this.xDC + this.wDC - 1 * AvMain.hd - this.imgBackMoney.frameWidth, this.yPopup + this.yDC + 3 * AvMain.hd + 35 * AvMain.hd * this.indexMoney + 31 * AvMain.hd / 2 - 15 * AvMain.hd, 60 * AvMain.hd, 31 * AvMain.hd)) {
               this.indexMoney = -1;
            }
         }

         if (Canvas.isPointerRelease) {
            if (this.count - this.timeDelay <= 4L) {
               this.timeOpen = 5;
            } else {
               this.click();
            }

            this.isTran = false;
            Canvas.isPointerRelease = false;
         }
      }

      if (this.isStart || !this.isRace) {
         Canvas.loadMap.updateKey();
      }

   }

   private void click() {
      if (this.indexDC != -1) {
         GlobalService.gI().doDatCuoc(this.listPet[this.indexMoney].IDDB, this.iMoney[this.indexDC]);
         this.indexDC = -1;
         this.indexMoney = -1;
         this.isDC = false;
         this.commandActionPointer(8);
      } else if (this.countCloseDC > 0) {
         this.countCloseDC = 0;
         this.isDC = false;
         this.indexMoney = -1;
      } else if (this.indexPet != -1) {
         GlobalService.gI().doPetInfo(this.listPet[this.indexPet].IDDB);
         this.indexPet = -1;
      } else if (this.indexMoney != -1) {
         this.isDC = true;
         super.center = this.t;
         super.left = null;
         super.right = this.u;
      }

   }

   private static void paint(Graphics var0, int var1, int var2, int var3, int var4, FrameImage var5, int var6) {
      var5.drawFrame(0, var1, var2, 0, var0);
      var5.drawFrame(2, var1 + var3 - var5.frameWidth, var2, 0, var0);
      var5.drawFrame(5, var1, var2 + var4 - var5.frameHeight, 0, var0);
      var5.drawFrame(7, var1 + var3 - var5.frameWidth, var2 + var4 - var5.frameHeight, 0, var0);

      int var7;
      for(var7 = 0; var7 < (var3 - (var5.frameWidth << 1)) / var5.frameWidth; ++var7) {
         var5.drawFrame(1, var1 + (var7 + 1) * var5.frameWidth, var2, 0, var0);
         var5.drawFrame(6, var1 + (var7 + 1) * var5.frameWidth, var2 + var4 - var5.frameHeight, 0, var0);
      }

      var5.drawFrame(1, var1 + var3 - (var5.frameWidth << 1), var2, 0, var0);
      var5.drawFrame(6, var1 + var3 - (var5.frameWidth << 1), var2 + var4 - var5.frameHeight, 0, var0);

      for(var7 = 0; var7 < (var4 - (var5.frameHeight << 1)) / var5.frameHeight; ++var7) {
         var5.drawFrame(3, var1, var2 + (var7 + 1) * var5.frameHeight, 0, var0);
         var5.drawFrame(4, var1 + var3 - var5.frameWidth, var2 + (var7 + 1) * var5.frameHeight, 0, var0);
      }

      var5.drawFrame(3, var1, var2 + var4 - (var5.frameHeight << 1), 0, var0);
      var5.drawFrame(4, var1 + var3 - var5.frameWidth, var2 + var4 - (var5.frameHeight << 1), 0, var0);
      if (var6 != -1) {
         var0.setColor(var6);
         var0.fillRect(var1 + var5.frameWidth, var2 + var5.frameHeight, var3 - (var5.frameWidth << 1), var4 - (var5.frameHeight << 1));
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      Canvas.resetTrans(var1);
      if (this.isRace) {
         Canvas.paint.drawRectangle(var1, this.xPopup, this.yPopup, this.wPopup, this.hPopup, PaintPopup.color[2], PaintPopup.color[3], 1);
         var1.translate(this.xPopup, this.yPopup);
         Canvas.normalFont.drawString(var1, T.datCuoc, this.wPopup / 2, 6 * AvMain.hd, 2);
         paint(var1, this.xInfo, this.yInfo, this.wInfo, this.Hinfo, this.imgInfo, -1);
         paint(var1, this.xDC, this.yDC, this.wDC, this.hDC, MenuNPC.imgDc, -12335933);

         int var4;
         for(var4 = 0; var4 < 6; ++var4) {
            this.imgBackpet.drawFrame(this.indexPet == var4 ? 1 : 0, this.xDC + 32 * AvMain.hd / 2, this.yDC + 3 * AvMain.hd + this.W * var4 + 31 * AvMain.hd / 2, 0, 3, var1);
            AvatarData.paintImg(var1, this.listPet[var4].idIcon, this.xDC + 32 * AvMain.hd / 2, this.yDC + 3 * AvMain.hd + this.W * var4 + 31 * AvMain.hd / 2, 3);
            Canvas.M.drawString(var1, "x" + this.listPet[var4].rate, this.xDC + 32 * AvMain.hd / 2 + this.imgBackpet.frameWidth / 2 - 5 * AvMain.hd, this.yDC + 3 * AvMain.hd + this.W * var4 + 31 * AvMain.hd / 2 + this.imgBackpet.frameHeight / 2 - AvMain.hBlack, 2);
            this.imgBackMoney.drawFrame(this.indexMoney == var4 ? 1 : 0, this.xDC + this.wDC - 1 * AvMain.hd - this.imgBackMoney.frameWidth, this.yDC + 7 * AvMain.hd + this.W * var4, 0, var1);
            if (this.listPet[var4].money > 0) {
               Canvas.normalFont.drawString(var1, "" + this.listPet[var4].money, this.xDC + this.wDC - 1 * AvMain.hd - this.imgBackMoney.frameWidth / 2, this.yDC + 7 * AvMain.hd + this.W * var4 + this.imgBackMoney.frameHeight / 2 - AvMain.hNormal / 2 - AvMain.hd - 1, 2);
            } else {
               Canvas.normalFont.drawString(var1, T.datCuoc, this.xDC + this.wDC - 1 * AvMain.hd - this.imgBackMoney.frameWidth / 2, this.yDC + 7 * AvMain.hd + this.W * var4 + this.imgBackMoney.frameHeight / 2 - AvMain.hNormal / 2 - AvMain.hd - 1, 2);
            }
         }

         if (this.isPetInfo && this.listPet != null) {
            Canvas.normalFont.drawString(var1, this.namePetInfo, this.xInfo + this.wInfo / 2, this.yInfo + 6 * AvMain.hd, 2);
            AvatarData.paintImg(var1, this.idImgPeInfo, this.xInfo + this.wInfo / 2, this.yInfo + 40 * AvMain.hd, 3);
            var4 = this.yInfo + 70 * AvMain.hd;
            Canvas.normalFont.drawString(var1, T.win, this.xInfo + 5 * AvMain.hd, var4, 0);
            Canvas.fontChatB.drawString(var1, this.numWin + "%", this.xInfo + this.wInfo - 8 * AvMain.hd, var4 + AvMain.hNormal / 2 - AvMain.hBlack / 2, 1);
            var4 += AvMain.hNormal;
            Canvas.normalFont.drawString(var1, T.cityIsOffLine, this.xInfo + 5 * AvMain.hd, var4, 0);
            Canvas.fontChatB.drawString(var1, "X" + this.ratePetInfo, this.xInfo + this.wInfo - 8 * AvMain.hd, var4 + AvMain.hNormal / 2 - AvMain.hBlack / 2, 1);
            var4 += AvMain.hNormal;
            Canvas.normalFont.drawString(var1, T.youAre, this.xInfo + 5 * AvMain.hd, var4, 0);
            Canvas.fontChatB.drawString(var1, T.cannotRegister[this.phongDoPetInfo], this.xInfo + this.wInfo - 8 * AvMain.hd, var4 + AvMain.hNormal / 2 - AvMain.hBlack / 2, 1);
            var4 += AvMain.hNormal;
            Canvas.normalFont.drawString(var1, T.email, this.xInfo + 5 * AvMain.hd, var4, 0);
            Canvas.fontChatB.drawString(var1, T.cannotRegister[this.sucKhoePetInfo], this.xInfo + this.wInfo - 8 * AvMain.hd, var4 + AvMain.hNormal / 2 - AvMain.hBlack / 2, 1);
            this.imgTime.drawFrame(0, this.xInfo + this.imgTime.frameWidth / 2 + 8 * AvMain.hd, this.yInfo + this.Hinfo - AvMain.hBorder - this.imgTime.frameHeight - 8 * AvMain.hd, 0, 3, var1);
            Canvas.normalFont.drawString(var1, String.valueOf(this.timeRemain), this.xInfo + 8 * AvMain.hd + this.imgTime.frameWidth + 2 * AvMain.hd, this.yInfo + this.Hinfo - AvMain.hBorder - this.imgTime.frameHeight - 8 * AvMain.hd - Canvas.normalFont.getHeight() / 2, 0);
            this.imgTime.drawFrame(1, this.xInfo + this.imgTime.frameWidth / 2 + 8 * AvMain.hd, this.yInfo + this.Hinfo - AvMain.hBorder - AvMain.hd, 0, 3, var1);
            Canvas.normalFont.drawString(var1, String.valueOf(GameMidlet.avatar.money[0]), this.xInfo + 8 * AvMain.hd + this.imgTime.frameWidth + 2 * AvMain.hd, this.yInfo + this.Hinfo - AvMain.hBorder - AvMain.hd - AvMain.hNormal / 2, 0);
         }

         if (this.isDC) {
            this.paintDC(var1);
         }
      } else {
         ImageIcon var5;
         if (this.isStart && this.countStart > 0 && (var5 = AvatarData.getImgIcon((short)1065)).count != -1) {
            int var3 = var5.h / 4;
            var1.drawRegion(var5.img, 0, (3 - this.countStart / 12) * var3, var5.w, var3, 0, Canvas.w / 2, Canvas.h / 2, 3);
         }
      }

      Canvas.resetTrans(var1);
      if (this.myChat != null && this.myChat.chats != null) {
         this.myChat.paintAnimal(var1);
      }

      if (Canvas.welcome == null || !Welcome.isPaintArrow) {
         super.paint(var1);
      }

      if ((this.isStart || !this.isRace) && Canvas.currentDialog == null && this.isEnd) {
         Canvas.borderFont.drawString(var1, String.valueOf(this.timeStart), Canvas.hw, 5, 2);
      }

      Canvas.paintPlus(var1);
   }

   private void paintDC(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.paint.drawRectangle(var1, this.xSelectDC, this.ySelectDC, this.wSelectDC, this.hSelectDC, PaintPopup.color[2], PaintPopup.color[3], 1);
      var1.translate(this.xSelectDC, this.ySelectDC);
      Canvas.normalFont.drawString(var1, T.upgradeChest, this.wSelectDC / 2, 10 * AvMain.hd, 2);

      for(int var2 = 0; var2 < 9; ++var2) {
         this.imgBackMoney.drawFrame(this.indexDC == var2 ? 1 : 0, 5 * AvMain.hd + var2 % 3 * (5 * AvMain.hd + this.imgBackMoney.frameWidth), this.hSelectDC - 29 * AvMain.hd * 3 + var2 / 3 * 29 * AvMain.hd, 0, var1);
         Canvas.smallFontYellow.drawString(var1, String.valueOf(this.iMoney[var2]), 5 * AvMain.hd + var2 % 3 * (5 * AvMain.hd + this.imgBackMoney.frameWidth) + this.imgBackMoney.frameWidth / 2, this.hSelectDC - 29 * AvMain.hd * 3 + var2 / 3 * 29 * AvMain.hd + this.imgBackMoney.frameHeight / 2 - AvMain.hSmall / 2, 2);
      }

   }

   public final void paintMain(Graphics var1) {
      Canvas.resetTrans(var1);
      Canvas.loadMap.paint(var1);

      for(int var2 = 0; var2 < 6; ++var2) {
         int var10001 = 4 * LoadMap.w;
         if (AvCamera.gI().xCam <= var10001 * AvMain.hd) {
            LoadMap.imgMap.drawFrameXY(0, var2 % 2 == 0 ? 2 : 3, 3 * LoadMap.w * AvMain.hd, (var2 + 6) * LoadMap.w * AvMain.hd, 0, var1);
         }

         if (AvCamera.gI().xCam + Canvas.w >= (LoadMap.wMap - 3) * LoadMap.w * AvMain.hd) {
            LoadMap.imgMap.drawFrameXY(0, var2 % 2 == 0 ? 2 : 3, (LoadMap.wMap - 3) * LoadMap.w * AvMain.hd, (var2 + 6) * LoadMap.w * AvMain.hd, 0, var1);
         }
      }

      Canvas.loadMap.paintBackGround(var1);
      Canvas.resetTrans(var1);
   }

   public final void onChatFromMe(String var1) {
      if (!var1.equals("")) {
         this.myChat = new ChatPopup(50, var1, (byte)0);
         this.myChat.xc = Canvas.hw;
         this.myChat.yc = Canvas.h - this.myChat.h - MyScreen.hTab - ChatTextField.gI().tfChat.height;
         GlobalService var10000 = GlobalService.gI();
         var10000.createMessage((byte)9);
         var10000.writeUTF(var1);
         var10000.sendMessage();
      }

   }

   public final void onPetInfo(short var1, String var2, short var3, byte var4, byte var5, byte var6) {
      this.isPetInfo = true;
      this.idImgPeInfo = var1;
      this.namePetInfo = var2;
      this.numWin = var3;
      this.ratePetInfo = var4;
      this.phongDoPetInfo = var5;
      this.sucKhoePetInfo = var6;
   }

   public final void onChat(String var1) {
      Vector var2 = new Vector();
      int var3 = AvCamera.gI().xTo;
      if (this.isStart || !this.isRace) {
         var3 += Canvas.w / 3;
      }

      int var4;
      for(var4 = 0; var4 < LoadMap.playerLists.size(); ++var4) {
         Base var5;
         if ((var5 = (Base)LoadMap.playerLists.elementAt(var4)).catagory == 9 && var5.x * AvMain.hd > var3 && var5.x * AvMain.hd < var3 + Canvas.w) {
            var2.addElement(var5);
         }
      }

      if (var2.size() > 0) {
         var4 = CRes.rnd(var2.size());
         ((Avatar)var2.elementAt(var4)).chat = new ChatPopup(50, var1, (byte)0);
      }

   }
}
