package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class MainMenu extends MyScreen {
   public static MainMenu me;
   private int selected;
   private int x;
   private int y;
   private int wSmall;
   private int disSmall;
   private int numW;
   private boolean isAble = false;
   private Vector list;
   private short[] l;
   private MyScreen lastScr;
   private Command n;
   public AvPosition b;
   private static PopupName popFocus;
   public boolean isWearing = false;
   private boolean isTran;
   private long q = 0L;
   private boolean isCircle = false;
   private int s;

   public static MainMenu gI() {
      return me == null ? (me = new MainMenu()) : me;
   }

   public final void switchToMe() {
      if (Canvas.currentMyScreen != this) {
         this.lastScr = Canvas.currentMyScreen;
      }

      this.initCmd();
      super.switchToMe();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.closeGo();
            return;
         case 1:
            this.closeGo();
            Command var3;
            if ((var3 = (Command)this.list.elementAt(this.selected)).action != null) {
               var3.action.perform();
               return;
            } else {
               this.commandActionPointer(var3.indexMenu, var3.subIndex);
            }
         default:
      }
   }

   public final void initCmd() {
      if (Canvas.stypeInt == 0) {
         super.right = new Command(T.close, 0);
      } else {
         this.isAble = true;
      }

      this.n = new Command(T.selectt, 1);
   }

   public MainMenu() {
      this.s = 50 * AvMain.hd;
   }

   private void closeGo() {
      this.lastScr.switchToMe();
      if (MapScr.focusP != null) {
         MapScr.focusP.ableShow = false;
      }

      this.isCircle = false;
      popFocus = null;
      super.center = null;
      this.isTran = false;
   }

   public final void commandActionPointer(int var1, int var2) {
      StringObj var8;
      switch (var1) {
         case 1:
            MapScr.gI().doSellectAction();
            return;
         case 2:
            MainMenu var10 = this;
            byte[] var11 = new byte[]{4, 5, 6, 7, 8, 9, 10, 11, 12};
            Vector var12 = new Vector();

            for(int var13 = 0; var13 < var11.length; ++var13) {
               var12.addElement(new CommandFeel(var10, "", 19, var13, var11, var13));
            }

            var10.b = null;
            var10.setInfo(var12);
            return;
         case 3:
            this.doExchange();
            return;
         case 4:
            MapScr.isOpenInfo = true;
            ParkService.gI().doRequestYourInfo(GameMidlet.avatar.IDDB);
            return;
         case 5:
            ListScr.gI().setFriendList(false);
            return;
         case 6:
            this.isWearing = false;
            GlobalService.gI().doRequestContainer(GameMidlet.avatar.IDDB);
            return;
         case 7:
            MapScr.gI();
            MapScr.doRequestAddFriend(MapScr.focusP);
            return;
         case 8:
            GlobalService.gI().requestShop(26);
            Canvas.startWaitDlg();
            return;
         case 9:
            MapScr.gI().doHit();
            return;
         case 10:
            MapScr.gI();
            MapScr.doKiss();
            return;
         case 11:
            MapScr.isOpenInfo = true;
            MapScr.gI();
            MapScr.doRequestYourInfo();
            return;
         case 12:
            MapScr.gI().doAction();
            return;
         case 13:
            MapScr.gI();
            MapScr.doInviteToMyHome();
            return;
         case 14:
         default:
            break;
         case 15:
            MapScr.gI().a(GameMidlet.myIndexP);
            return;
         case 16:
            if ((var8 = (StringObj)MapScr.listCmdRotate.elementAt(var2)).type == 1) {
               GlobalService.gI().doRequestCmdRotate(var8.anthor, MapScr.focusP != null ? MapScr.focusP.IDDB : -1);
               return;
            }
            break;
         case 17:
            GlobalService.gI().doCommunicate(var2);
            return;
         case 18:
            if ((var8 = (StringObj)MapScr.listCmdRotate.elementAt(var2)).type == 0) {
               GlobalService.gI().doRequestCmdRotate(var8.anthor, MapScr.focusP != null ? MapScr.focusP.IDDB : -1);
               return;
            }
            break;
         case 19:
            byte[] var7 = new byte[]{4, 5, 6, 7, 8, 9, 10, 11, 12};
            if (var2 == 0) {
               MapScr.gI();
               MapScr.doSellectFeel(4);
               return;
            }

            MapScr.gI();
            MapScr.doSellectFeel(var7[var2]);
            return;
         case 20:
            if (GameMidlet.avatar.task != 0 && GameMidlet.avatar.task != -5 || Bus.isRun) {
               return;
            }

            if (LoadMap.focusObj != null && LoadMap.focusObj.catagory == 5) {
               ParkService.gI().doGetDropPart(((Drop_Part)LoadMap.focusObj).ID);
               break;
            }

            Vector var9 = new Vector();
            Command var3 = this.setCommandMenu(T.viewInfo, 4, 17);
            Command var4 = this.setCommandMenu(T.basket, 6, 14);
            Command var5 = this.setCommandMenu(T.wearing, 21, 14);
            Command var6 = this.setCommandMenu(T.index, 15, 17);
            var9.addElement(var3);
            var9.addElement(var6);
            var9.addElement(var5);
            var9.addElement(var4);
            if (Canvas.currentMyScreen != PopupShop.gI()) {
               gI().setInfo(var9);
            }

            return;
         case 21:
            GlobalService.gI().doRequestContainer(GameMidlet.avatar.IDDB);
            this.isWearing = true;
      }

   }

   public final void doExchange() {
      if (MapScr.focusP != null) {
         this.isCircle = false;
         Vector var1;
         (var1 = new Vector()).addElement(this.setCommandMenu(T.hit, 9, 13));
         var1.addElement(this.setCommandMenu(T.tkFarm, 12, 2));
         var1.addElement(this.setCommandMenu(T.addFriend, 7, 11));
         var1.addElement(this.setCommandMenu(T.giveGift, 8, 12));
         var1.addElement(this.setCommandMenu(T.kiss, 10, 21));
         var1.addElement(this.setCommandMenu(T.tkChinh, 11, 19));
         var1.addElement(this.setCommandMenu(T.inviteMyHouse, 13, 22));
         if (MapScr.listCmdRotate.size() > 0) {
            for(int var2 = 0; var2 < MapScr.listCmdRotate.size(); ++var2) {
               StringObj var3;
               if ((var3 = (StringObj)MapScr.listCmdRotate.elementAt(var2)).type == 1) {
                  var1.addElement(new class_ji(this, var3.str, 16, var2, var3));
               }
            }
         }

         this.setInfo(var1);
      }

   }

   public final void perform() {
      if ((GameMidlet.avatar.task == 0 || GameMidlet.avatar.task == -5) && !Bus.isRun) {
         if (LoadMap.focusObj != null && LoadMap.focusObj.catagory == 5) {
            ParkService.gI().doGetDropPart(((Drop_Part)LoadMap.focusObj).ID);
         } else if (LoadMap.focusObj != null && LoadMap.focusObj.catagory == 0 && ((Avatar)LoadMap.focusObj).IDDB == -100) {
            Canvas.startOKDlg(T.doYouWantUpgradeCoffer, new class_jc(this));
         } else {
            Vector var1 = new Vector();
            Command var2 = this.setCommandMenu(T.action, 1, 1);
            Command var3 = this.setCommandMenu(T.feel, 2, 0);
            Command var4 = this.setCommandMenu(T.exchange, 3, 20);
            Command var5 = this.setCommandMenu(T.mySeft, 20, 17);
            Command var6 = this.setCommandMenu(T.friend, 5, 18);
            if (Canvas.stypeInt == 0) {
               var1.addElement(var4);
            }

            var1.addElement(var5);
            var1.addElement(var3);
            if (Canvas.stypeInt == 0) {
               var1.addElement(MapScr.gI().f);
            }

            if (GameMidlet.avatar.action != 14) {
               var1.addElement(var2);
            }

            var1.addElement(var6);
            int var7;
            StringObj var8;
            if (Canvas.stypeInt > 0 && MapScr.listCmd != null && MapScr.listCmd.size() > 0) {
               for(var7 = 0; var7 < MapScr.listCmd.size(); ++var7) {
                  var8 = (StringObj)MapScr.listCmd.elementAt(var7);
                  var1.addElement(new class_jg(this, var8.str, 17, var7, var8));
               }
            }

            if (MapScr.listCmdRotate.size() > 0) {
               for(var7 = 0; var7 < MapScr.listCmdRotate.size(); ++var7) {
                  if ((var8 = (StringObj)MapScr.listCmdRotate.elementAt(var7)).type == 0) {
                     var1.addElement(new class_ix(this, var8.str, 18, var7, var8));
                  }
               }
            }

            if (Canvas.currentMyScreen != PopupShop.gI()) {
               this.b = null;
               gI().setInfo(var1);
            }
         }
      }

   }

   public static void doWearing() {
      Avatar var0 = GameMidlet.avatar;
      if (Canvas.currentMyScreen != me) {
         PopupShop.gI().isFull = true;
         PopupShop.gI().addElement(new String[]{T.wearing, T.container}, new Vector[]{MapScr.gI().getListYourPart(var0, 0), MapScr.gI().getListCmdDoUsing(GameMidlet.listContainer, var0.IDDB, 1)}, (Vector)null);
         PopupShop.gI().setCmdLeft(MapScr.gI().cmdDellPart(var0.seriPart, 0, 0, false), 0);
         PopupShop.gI().setCmdLeft(MapScr.gI().cmdDellPart(GameMidlet.listContainer, 1, 0, true), 1);
         if (Canvas.currentMyScreen != PopupShop.gI()) {
            PopupShop.gI().switchToMe();
         }
      }

   }

   private Command setCommandMenu(String var1, int var2, int var3) {
      return new CommandMenu(this, var1, var2, var3);
   }

   public final Command setCommandMenu(String var1, IAction var2, int var3) {
      return new CommandMenu1(this, var1, new IActionMenu(this, var2), var3);
   }

   public final void setInfo(Vector var1) {
      this.list = var1;
      if (Canvas.isKeyBoard) {
         this.wSmall = 40 * AvMain.hd + (AvMain.hd - 1) * 20;
         if (Canvas.stypeInt == 1 && Canvas.w > 300) {
            this.wSmall += 20;
         }
      } else {
         this.wSmall = 30;
      }

      this.disSmall = this.wSmall + 2 * AvMain.hd;
      this.y = AvMain.hBorder << 1;
      this.x = 0;
      this.numW = Canvas.w / this.disSmall;
      if (var1.size() * this.disSmall < Canvas.w) {
         this.x = (Canvas.w - var1.size() * this.disSmall) / 2;
      } else {
         this.x = (Canvas.w - this.numW * this.disSmall) / 2;
      }

      this.l = new short[var1.size()];

      for(int var2 = 0; var2 < this.l.length; ++var2) {
         this.l[var2] = -40;
      }

      if (this.selected >= var1.size()) {
         this.selected = 0;
      }

      this.isCircle = false;
      if (MapScr.focusP != null && Canvas.stypeInt > 0) {
         MapScr.focusP.ableShow = true;
      }

      if (Canvas.stypeInt > 0) {
         this.y = Canvas.hh - (var1.size() / this.numW + 1) * this.wSmall / 2;
      }

      this.switchToMe();
      if (Canvas.stypeInt == 0) {
         super.center = this.n;
      }

   }

   public final void update() {
      this.lastScr.update();

      for(int var1 = 0; var1 < this.l.length; ++var1) {
         if (this.l[var1] != var1 % this.numW * this.disSmall) {
            short[] var10000 = this.l;
            var10000[var1] = (short)(var10000[var1] + (var1 % this.numW * this.disSmall - this.l[var1]) / 3);
         }
      }

   }

   public final void updateKey() {
      if (Canvas.isPointerClick) {
         boolean var1 = false;

         for(int var2 = this.list.size() - 1; var2 >= 0; --var2) {
            if (Canvas.isPointer(this.l[var2] + this.x, this.y + var2 / this.numW * this.disSmall, this.wSmall, this.wSmall)) {
               this.selected = var2;
               this.isTran = true;
               this.isAble = false;
               var1 = true;
               this.q = System.currentTimeMillis() / 100L;
               break;
            }
         }

         if (!var1) {
            this.closeGo();
         }
      }

      int var3;
      if (this.isTran) {
         if (System.currentTimeMillis() / 100L - this.q > 10L) {
            this.isAble = false;
         }

         if (Canvas.isPointerRelease) {
            this.isTran = false;
            this.isAble = true;

            for(var3 = this.list.size() - 1; var3 >= 0; --var3) {
               if (Canvas.isPointer(this.l[var3] + this.x, this.y + var3 / this.numW * this.disSmall, this.wSmall, this.wSmall)) {
                  if (var3 == this.selected) {
                     this.closeGo();
                     this.commandTab(1, -1);
                  }
                  break;
               }
            }

            Canvas.isPointerRelease = false;
         }
      }

      if (Canvas.a(4)) {
         --this.selected;
         if (this.selected < 0) {
            this.selected = this.list.size() - 1;
         }
      } else if (Canvas.a(6)) {
         ++this.selected;
         if (this.selected >= this.list.size()) {
            this.selected = 0;
         }
      } else if (Canvas.a(2)) {
         if ((var3 = this.selected - this.numW) < 0) {
            if ((var3 += this.list.size() / this.numW * this.numW + this.numW) < this.list.size()) {
               this.selected = var3;
            }
         } else {
            this.selected = var3;
         }
      } else if (Canvas.a(8)) {
         this.selected += this.numW;
         if (this.selected >= this.list.size()) {
            this.selected %= this.numW;
         }
      }

      super.updateKey();
   }

   public final void paint(Graphics var1) {
      this.lastScr.paintMain(var1);
      Canvas.resetTrans(var1);
      Graphics var3 = var1;
      MainMenu var2 = this;
      if (GameMidlet.avatar.action != 14) {
         GameMidlet.avatar.paintIcon(var1, GameMidlet.avatar.x * AvMain.hd - AvCamera.gI().xCam, GameMidlet.avatar.y * AvMain.hd - AvCamera.gI().yCam, false);
      }

      Command var4 = (Command)this.list.elementAt(this.selected);
      Canvas.borderFont.drawString(var1, var4.caption, Canvas.hw, this.y - 15, 2);
      var1.translate(this.x, this.y);

      for(int var5 = this.list.size() - 1; var5 >= 0; --var5) {
         byte var7 = 0;
         Command var6 = (Command)var2.list.elementAt(var5);
         if (var5 == var2.selected && !var2.isAble) {
            var7 = 4;
         }

         Canvas.paint.paintPopupBack(var3, var2.l[var5], var5 / var2.numW * var2.disSmall, var2.wSmall, var2.wSmall, var7);
         var6.paint(var3, var2.disSmall / 2 + var2.l[var5], var2.disSmall / 2 + var5 / var2.numW * var2.disSmall);
      }

      super.paint(var1);
   }
}
