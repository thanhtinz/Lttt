package avt;

import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import main.Canvas;
import main.GameMidlet;

public final class MiniMap extends MyScreen {
   public static MiniMap me;
   private FrameImage imgMap;
   public FrameImage imgArrow;
   private byte[] map;
   private Vector listPos;
   private byte wMini;
   private byte hMini;
   private byte wSmall = 16;
   public int x;
   public int y;
   private static Image imgSmallIcon;
   private static Image imgBackIcon;
   public int selected;
   private static int cmtoX;
   public static int cmx;
   private static int cmdx;
   private static int cmvx;
   private static int cmxLim;
   private static int cmtoY;
   public static int cmy;
   private static int cmdy;
   private static int cmvy;
   private static int cmyLim;
   public IAction cmdUpdateKey;
   public static String i;
   private boolean trans;
   public static boolean isCityMap = false;
   public static Image[] imgClound = new Image[2];
   private static Vector listClound = new Vector();
   private static FrameImage imgPopup;
   public Command l;
   private Command cmdCenter;
   private int vY;
   private int vX;
   private int pa;
   private int pb;
   boolean ableTrans = false;
   private int dyTran;
   private int dxTran;
   private long timePointY;
   private long count;
   public static IAction actionReg;
   public static byte iRequestReg;

   public static MiniMap gI() {
      return me == null ? (me = new MiniMap()) : me;
   }

   public final void switchToMe() {
      super.switchToMe();
      if (!GlobalLogicHandler.isNewVersion) {
         Canvas.endDlg();
      }

      if (LoadMap.idTileImg != -1) {
         Canvas.endDlg();
      }

      super.left = this.l;
      if (Canvas.isInitChar) {
         (Canvas.welcome = new Welcome()).initMiniMap();
         super.left = null;
      } else if (MyScreen.nMsg > 0 && iRequestReg == 1) {
         MessageScr.gI().switchToMe(Canvas.currentMyScreen);
      }

      if (Canvas.load == 0) {
         Canvas.load = 1;
      }

      Canvas.currentEffect.removeAllElements();
      this.tran();
      MapScr.idMapOld = -1;
   }

   public MiniMap() {
      FilePack.b(T.aw);
      this.imgArrow = FrameImage.init("up", 13 * AvMain.hd, 11 * AvMain.hd);
      FilePack.reset();
      FilePack.b(T.av);
      imgSmallIcon = FilePack.getImage("sIc");
      imgBackIcon = FilePack.getImage("b_p");
      FilePack.reset();
      this.l = new Command(T.menu, 0);
      super.left = this.l;
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            if (Canvas.welcome == null || !Welcome.isPaintArrow) {
               Vector var3 = new Vector();
               if (actionReg != null) {
                  var3.addElement(new Command("Đăng ký", actionReg));
               }

               var3.addElement(new Command(T.menuAuto, 10));
               if (Canvas.stypeInt == 0) {
                  var3.addElement(MapScr.gI().f);
               }

               var3.addElement(new Command(T.option, 1));
               var3.addElement(new Command(T.viewInfo, 2));
               if (!LoginScr.isAccVir) {
                  var3.addElement(new Command(T.changePass, 3));
               }

               var3.addElement(new Command(T.giveGame, 4));
               var3.addElement(new Command(T.otherGame, 5));
               var3.addElement(new Command(T.exit, 6));
               Menu.gI().startAt(var3, 0);
            }

            return;
         case 1:
            MapScr.gI().switchToMe();
            imgPopup = null;
            break;
         case 10:
            Vector var4 = new Vector();
            var4.addElement(new Command(T.menuAutoFishing, 11));
            var4.addElement(new Command(T.menuAutoMining, 12));
            Menu.gI().startAt(var4, 0);
            return;
         default:
      }
   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 1:
            OptionScr.gI().switchToMe();
            return;
         case 2:
            GlobalService.gI().requestService((byte)6, "");
            return;
         case 3:
            MapScr.gI().doChangePass();
            return;
         case 4:
            Form var5 = new Form(T.sentToFriend);
            TextField var6 = new TextField(T.phoneNumber, "", 50, 3);
            var5.append(var6);
            var5.append(T.youCanSelectFromMenu);
            javax.microedition.lcdui.Command var3 = new javax.microedition.lcdui.Command(T.OK, 4, 1);
            var5.addCommand(var3);
            javax.microedition.lcdui.Command var4 = new javax.microedition.lcdui.Command(T.close, 2, 1);
            var5.addCommand(var4);
            var5.setCommandListener(new MiniMapCommandListener(this, var3, var6));
            Display.getDisplay(GameMidlet.instance).setCurrent(var5);
            return;
         case 5:
            GlobalService.gI().requestService((byte)3, (String)null);
            return;
         case 6:
            MapScr.gI().doExitGame();
            return;
         case 10:
            Vector var10 = new Vector();
            var10.addElement(new Command(T.menuAutoFishing, 11));
            var10.addElement(new Command(T.menuAutoMining, 12));
            Menu.gI().startAt(var10, 0);
            return;
         case 11:
            Vector var11 = new Vector();
            var11.addElement(new Command(T.menuAutoFishingOn, 13));
            var11.addElement(new Command(T.menuAutoFishingSettings, 14));
            Menu.gI().startAt(var11, 0);
            return;
         case 12:
            Vector var12 = new Vector();
            var12.addElement(new Command("Cài đặt đào", 16));
            var12.addElement(new Command("Cài đặt bán đá", 17));
            Menu.gI().startAt(var12, 0);
            return;
         case 16:
            ClientUtilities.openAutoStoneSettingsForm();
            return;
         case 17:
            ClientUtilities.openAutoStoneSellSettingsForm();
            return;
         case 13:
            doStartAutoFishing();
            return;
         case 14:
            ClientUtilities.openFishingSettingsForm(true);
            return;
         case 7:
            Welcome.restart();
            (Canvas.welcome = new Welcome()).initMiniMap();
            super.left = null;
         default:
      }
   }

   public final void close() {
      if (!isCityMap && Canvas.currentMyScreen != ServerListScr.me) {
         MapScr.gI().switchToMe();
         imgPopup = null;
      } else {
         MapScr.gI().doExitGame();
      }

   }

   public final void setInfo(FrameImage var1, byte[] var2, Vector var3, byte var4, int var5, Command var6) {
      AvatarData.getImgIcon((short)839);
      GameMidlet.avatar.ableShow = false;
      this.wSmall = (byte)var5;
      this.imgMap = var1;
      this.map = var2;
      this.listPos = var3;
      this.wMini = 34;
      this.cmdCenter = var6;
      if (Canvas.stypeInt == 0) {
         super.center = var6;
      }

      this.hMini = (byte)(var2.length / this.wMini);
      super.right = null;
      this.init();
      this.cmdUpdateKey = null;
      listClound.removeAllElements();

      for(int var7 = 0; var7 < 7; ++var7) {
         listClound.addElement(new AvPosition(var7 * this.wMini * this.wSmall / 10 + 50, CRes.rnd(10) * (this.hMini * this.wSmall / 10) + 20, CRes.rnd(2)));
      }

      cmtoY = cmy = cmx = cmtoX = this.selected = 0;
      this.tran();
      if (isCityMap) {
         FilePack.b(T.av);
         imgPopup = new FrameImage(FilePack.getImage("k"), 40 * AvMain.hd, 40 * AvMain.hd);
         FilePack.reset();
      }

   }

   public final void init() {
      this.x = (Canvas.w - this.wMini * this.wSmall) / 2;
      this.y = (Canvas.hCan - Canvas.hTab - this.hMini * this.wSmall) / 2;
      if (this.x < 0) {
         this.x = 0;
      }

      if (this.y < 0) {
         this.y = 0;
      }

      cmxLim = this.wMini * this.wSmall - Canvas.w;
      cmyLim = this.hMini * this.wSmall - Canvas.hCan;
      if (cmxLim < 0) {
         cmx = 0;
         cmxLim = 0;
      }

      if (cmyLim < 0) {
         cmy = 0;
         cmyLim = 0;
      }

   }

   public final void update() {
      if (this.vY != 0) {
         if (cmy < 0 || cmy > cmyLim) {
            this.vY -= this.vY / 4;
            cmy += this.vY / 20;
            if (this.vY / 10 <= 1) {
               this.vY = 0;
            }
         }

         cmtoY = cmy += this.vY / 10;
         this.vY -= this.vY / 10;
         if (this.vY / 10 == 0) {
            this.vY = 0;
         }
      }

      if (cmy < 0) {
         cmtoY = 0;
         this.vY = 0;
      } else if (cmy > cmyLim) {
         cmtoY = cmyLim;
         this.vY = 0;
      }

      if (this.vX != 0) {
         if (cmx < 0 || cmx > cmxLim) {
            this.vX -= this.vX / 4;
            cmx += this.vX / 20;
            if (this.vX / 10 <= 1) {
               this.vX = 0;
            }
         }

         cmx += this.vX / 10;
         this.vX -= this.vX / 10;
         cmtoX = cmx;
         if (this.vX / 10 == 0) {
            this.vX = 0;
         }
      }

      if (cmx < 0) {
         cmtoX = 0;
         this.vX = 0;
      } else if (cmx > cmxLim) {
         cmtoX = cmxLim;
         this.vX = 0;
      }

      if (cmy != cmtoY) {
         cmvy = cmtoY - cmy << 2;
         cmdy += cmvy;
         cmy += cmdy >> 4;
         cmdy &= 15;
      }

      if (cmx != cmtoX) {
         cmvx = cmtoX - cmx << 2;
         cmdx += cmvx;
         cmx += cmdx >> 4;
         cmdx &= 15;
      }

      if (cmtoY < 0 || cmy < 0) {
         cmy = 0;
         cmtoY = 0;
      }

      if (cmtoY > cmyLim || cmy > cmyLim) {
         cmtoY = cmy = cmyLim;
      }

      if (cmtoX < 0 || cmx < 0) {
         cmx = 0;
         cmtoX = 0;
      }

      if (cmtoX > cmxLim || cmx > cmxLim) {
         cmtoX = cmx = cmxLim;
      }

      for(int var1 = 0; var1 < listClound.size(); ++var1) {
         AvPosition var2;
         AvPosition var10000 = var2 = (AvPosition)listClound.elementAt(var1);
         var10000.x -= var2.anchor + (Canvas.gameTick % 5 == 1 ? 1 : 0);
         if (var2.x < -this.x - 50) {
            var2.x = this.x + CRes.rnd(4) * 50 + this.wMini * this.wSmall;
            var2.y = CRes.rnd(10) * (this.hMini * this.wSmall / 10) + 10;
            var2.anchor = CRes.rnd(2);
         }
      }

   }

   public static void f() {
      FarmScr.l = "e";
      PopupShop.i = "f";
      LoginScr.t = "a";
      MapScr.j = Canvas.a(i, -2);
   }

   public final void updateKey() {
      ++this.count;
      if (Canvas.welcome == null || !Welcome.isPaintArrow) {
         super.updateKey();
      }

      this.ableTrans = false;
      if (Canvas.isPointer(0, 0, Canvas.w, Canvas.h)) {
         int var1 = Canvas.dx();
         int var2 = Canvas.dy();
         int var3;
         PositionMap var4;
         if (Canvas.welcome == null && Canvas.isPointerClick) {
            Canvas.isPointerClick = false;

            for(var3 = 0; var3 < this.listPos.size(); ++var3) {
               var4 = (PositionMap)this.listPos.elementAt(var3);
               if (Canvas.isPointer(this.x + var4.x * this.wSmall + this.wSmall / 2 - 24 * AvMain.hd - cmx, this.y + var4.y * this.wSmall - 56 * AvMain.hd - cmy, 48 * AvMain.hd, 56 * AvMain.hd)) {
                  this.selected = var3;
                  return;
               }
            }
         }

         if (Canvas.isPointerDown) {
            if (Canvas.gameTick % 3 == 0) {
               this.dyTran = Canvas.py;
               this.dxTran = Canvas.px;
               this.timePointY = this.count;
            }

            this.vY = 0;
            this.vX = 0;
            if (!this.trans) {
               this.trans = true;
               this.pa = cmx;
               this.pb = cmy;
            }

            cmtoY = this.pb + var2;
            cmtoX = this.pa + var1;
            setLimit();
            cmy = cmtoY;
            cmx = cmtoX;
         }

         if (Canvas.isPointerRelease) {
            var3 = (int)(this.count - this.timePointY);
            int var5 = this.dyTran - Canvas.py;
            if (var3 < 10) {
               if (cmtoY >= 0 && cmtoY < cmyLim) {
                  this.vY = var5 / var3 * 10;
               }

               var5 = this.dxTran - Canvas.px;
               if (cmtoX >= 0 && cmtoX < cmxLim) {
                  this.vX = var5 / var3 * 10;
               }
            }

            this.timePointY = -1L;
            this.trans = false;
            if (CRes.abs(var1) < 10 && CRes.abs(var2) < 10) {
               var4 = (PositionMap)this.listPos.elementAt(this.selected);
               if (Canvas.isPointer(this.x + var4.x * this.wSmall + this.wSmall / 2 - 24 * AvMain.hd - cmx, this.y + var4.y * this.wSmall - 56 * AvMain.hd - cmy, 48 * AvMain.hd, 56 * AvMain.hd)) {
                  this.cmdCenter.perform();
                  return;
               }

               cmtoX = Canvas.px + cmx - Canvas.hw;
               cmtoY = Canvas.py + cmy - Canvas.hh;
               setLimit();
            }
         }
      }

      if (this.cmdUpdateKey == null) {
         if (!Canvas.a(2) && !Canvas.a(4)) {
            if (Canvas.a(8) || Canvas.a(6)) {
               ++this.selected;
               if (this.selected >= this.listPos.size()) {
                  this.selected = 0;
               }

               this.ableTrans = true;
            }
         } else {
            --this.selected;
            if (this.selected < 0) {
               this.selected = this.listPos.size() - 1;
            }

            this.ableTrans = true;
         }
      } else if (Canvas.welcome == null) {
         this.cmdUpdateKey.perform();
      }

      if (this.ableTrans) {
         this.tran();
      }

   }

   private void tran() {
      PositionMap var1;
      cmtoX = (var1 = (PositionMap)this.listPos.elementAt(this.selected)).x * this.wSmall - Canvas.w / 2;
      cmtoY = var1.y * this.wSmall - Canvas.hCan / 2;
      setLimit();
   }

   private static void setLimit() {
      if (cmtoY < 0) {
         cmtoY = 0;
      }

      if (cmtoY > cmyLim) {
         cmtoY = cmyLim;
      }

      if (cmtoX < 0) {
         cmtoX = 0;
      }

      if (cmtoX > cmxLim) {
         cmtoX = cmxLim;
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      if (Canvas.welcome == null || !Welcome.isPaintArrow) {
         super.paint(var1);
      }

      Canvas.paintPlus(var1);
   }

   public final void paintMain(Graphics var1) {
      Canvas.resetTrans(var1);
      var1.setColor(0);
      var1.fillRect(0, 0, Canvas.w, Canvas.hCan);
      var1.translate(this.x, this.y);
      var1.translate(-cmx, -cmy);

      int var2;
      int var4;
      for(var2 = 0; var2 < this.map.length; ++var2) {
         byte var3;
         var4 = (var3 = this.map[var2]) / this.imgMap.nFrame;
         this.imgMap.drawFrameXY(var4, var3 % this.imgMap.nFrame, var2 % this.wMini * this.wSmall, var2 / this.wMini * this.wSmall, var1);
      }

      for(var2 = 0; var2 < this.listPos.size(); ++var2) {
         PositionMap var9 = (PositionMap)this.listPos.elementAt(var2);
         if (var2 == this.selected) {
            var1.drawImage(imgBackIcon, var9.x * this.wSmall + this.wSmall / 2, var9.y * this.wSmall, 33);
            if (isCityMap) {
               imgPopup.drawFrame(var2, var9.x * this.wSmall + this.wSmall / 2, var9.y * this.wSmall - 12 * AvMain.hd, 0, 33, var1);
            } else {
               AvatarData.paintImg(var1, var9.d, var9.x * this.wSmall + this.wSmall / 2, var9.y * this.wSmall - 12 * AvMain.hd, 33);
            }
         } else {
            var1.drawImage(imgSmallIcon, var9.x * this.wSmall + this.wSmall / 2, var9.y * this.wSmall - var9.e / 3, 33);
            ++var9.e;
            if (var9.e >= 9) {
               var9.e = 0;
            }
         }
      }

      Graphics var10 = var1;
      MiniMap var8 = this;

      for(var4 = 0; var4 < var8.listPos.size(); ++var4) {
         PositionMap var5;
         int var6 = (var5 = (PositionMap)var8.listPos.elementAt(var4)).x * var8.wSmall;
         int var7;
         if ((var7 = var5.y * var8.wSmall) < cmy + 50) {
            var7 = cmy + 50;
         }

         if (var7 > cmy + Canvas.hCan - 20) {
            var7 = cmy + Canvas.hCan - 20;
         }

         if (var6 < cmx + 20) {
            var6 = cmx + 20;
         }

         if (var6 > cmx + Canvas.w - 47) {
            var6 = cmx + Canvas.w - 47;
         }

         Canvas.borderFont.drawString(var10, var5.c, var6 + 10, var7 - (var4 == var8.selected ? 70 * AvMain.hd : 35 * AvMain.hd) - var5.e / 3, 2);
      }

      Graphics var11 = var1;

      for(var4 = 0; var4 < listClound.size(); ++var4) {
         AvPosition var12;
         if ((var12 = (AvPosition)listClound.elementAt(var4)).x > cmx - 30 && var12.x < cmx + 30 + Canvas.w && var12.y > cmy - 20 && var12.y < cmy + 20 + Canvas.h) {
            var11.drawImage(imgClound[var12.anchor], var12.x, var12.y, 3);
         }
      }

      Canvas.resetTrans(var1);
   }

   public final void onRegisterByEmail(byte var1, String var2, String var3, String var4) {
      System.out.println("onRegisterByEmail: " + var3 + "   " + var4);
      if (var1 == 0) {
         actionReg = new IActionRequestReg(this, var2);
      } else if (var1 == 1) {
         actionReg = new IActionRequestOK(this, var2);
      } else if (var1 == 2) {
         LoginScr.gI().tfUser.setText(var3);
         LoginScr.gI().tfPass.setText(var4);
         LoginScr.gI().saveLogin();
         Canvas.startOKDlg("Đăng ký thành công.");
         actionReg = null;
      }

   }

   private void doStartAutoFishing() {
      Canvas.startWaitDlg();
      MapScr.roomID = 13;
      MapScr.typeJoin = 13;
      if (GameMidlet.avatar != null) {
         GameMidlet.avatar.direct = 2;
      }
      GlobalService.gI().getHandler(9);
      final int map13X = 282;
      final int map13Y = 52;
      final int targetX = 924;
      final int targetY = 119;
      final int targetDirect = 2;
      new Thread(new Runnable() {
         public void run() {
            try { Thread.sleep(3000L); } catch (Throwable t) {}
            if (GameMidlet.avatar != null) {
               if (MapScr.gI() != null) {
                  MapScr.gI().switchToMe();
                  CustomTab.gI().commandTab(0, 0);
                  try { Thread.sleep(500L); } catch (Throwable tt) {}
                  GameMidlet.avatar.x = map13X;
                  GameMidlet.avatar.y = map13Y;
                  GameMidlet.avatar.xCur = map13X;
                  GameMidlet.avatar.yCur = map13Y;
                  try { Thread.sleep(1000L); } catch (Throwable ttt) {}
                  AvatarService.gI().doBuyItem(448, 1);
                  try { Thread.sleep(1000L); } catch (Throwable tttt) {}
                  MapScr.roomID = 16;
                  MapScr.typeJoin = 16;
                  GlobalService.gI().getHandler(9);
                  try { Thread.sleep(3000L); } catch (Throwable ttttt) {}
                  if (MapScr.gI() != null) {
                     MapScr.gI().switchToMe();
                     try { Thread.sleep(500L); } catch (Throwable tttttt) {}
                     GameMidlet.avatar.x = targetX;
                     GameMidlet.avatar.y = targetY;
                     GameMidlet.avatar.xCur = targetX;
                     GameMidlet.avatar.yCur = targetY;
                     MapScr.doMove(targetX, targetY, targetDirect, (short)0);
                     try { Thread.sleep(1500L); } catch (Throwable t7) {}
                     FishingScr.gI().doSat(targetX, targetY);
                  }
               }
            }
            Canvas.endDlg();
            Canvas.addServerInfo("Đã bật auto câu cá!");
            if (ClientUtilities.autoStoneEnabled) {
               ClientUtilities.resetAutoStoneSchedule();
               Canvas.addServerInfo("Auto đào đá: Bật");
            }
         }
      }).start();
   }
}
