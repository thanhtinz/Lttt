package avt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class MapScr extends MyScreen implements IChatable {
   public static MapScr instance;
   public static byte roomID;
   public static byte boardID;
   public static int zoneMaxIndex = -1;
   public static Image imgFocusP;
   private Command cmdMenu;
   public Command e;
   public Command f;
   private Command I;
   private Command J;
   public static byte typeJoin = -1;
   public static Avatar focusP;
   public static byte typeCasino = -1;
   public static String j;
   public static Image imgBar;
   public static Vector listFish = new Vector();
   public static int indexMap = -1;
   public static Vector listCmd;
   public static Vector listCmdRotate;
   public static Vector listChair;
   public static Vector listItemEffect;
   public static boolean r = false;
   public static boolean s = false;
   public static boolean isWedding = false;
   public static boolean isNewVersion = false;
   public static int idHouse = -1;
   static byte[] ac = new byte[]{10, 4, 3, 5};
   private byte iGoChaSu = 0;
   private byte countWeeding = -1;
   public static boolean isOpenInfo = false;
   private Vector chatList = new Vector();
   private int chatDelay;
   private int MAX_CHAT_DELAY = 60;
   public boolean isTour = true;
   public static byte z;
   public static byte A;
   public static short[] idImg;
   public static Avatar avatarShop;
   public static int idMapOffline = -1;
   public static int idUserWedding_1;
   public static int idUserWedding_2;
   public static int idMapOld = -1;

   public final void switchToMe() {
      this.initCmd();
      super.switchToMe();
   }

   public static MapScr gI() {
      if (instance == null) {
         instance = new MapScr();
      }

      return instance;
   }

   public final void initCmd() {
      this.cmdMenu = new Command(T.menu, 0, this);
      super.left = this.cmdMenu;
      this.f = MainMenu.gI().setCommandMenu(T.eventt, new class_ig(this), 15);
      this.e = new Command(Canvas.isKeyBoard ? (Canvas.stypeInt == 0 ? T.selectt : T.menu) : "", 1, this);
      if (Canvas.stypeInt > 0 && Canvas.welcome == null) {
         super.left = this.e;
      }

      this.I = new Command(T.exit, 2, this);
      this.J = new Command(T.exchange, 2);
   }

   public final void commandActionPointer(int var1) {
      switch (var1) {
         case 0:
            this.onInviteToMyHome();
            return;
         case 1:
            if (!isWedding) {
               MainMenu.gI().perform();
               return;
            }
            break;
         case 2:
            this.doExit();
            return;
         case 3:
            exitGame();
      }

   }

   public final void close() {
      this.I.perform();
   }

   public MapScr() {
      this.initCmd();
   }

   public final void doExit() {
      Canvas.startWaitDlg();
      typeJoin = -1;
      typeCasino = -1;
      if (GameMidlet.CLIENT_TYPE == 8) {
         this.joinCitymap();
      } else {
         GlobalService.gI().getHandler(8);
      }

   }

   protected static void doEvent() {
      MessageScr.gI().switchToMe(Canvas.currentMyScreen);
   }

   protected final void doHit() {
      if (focusP != null && focusP.task == 0) {
         doGivingDefferent(100);
      }

   }

   protected static void doInviteToMyHome() {
      if (focusP != null) {
         ParkService.gI().doInviteToMyHome(0, focusP.IDDB);
      }

   }

   public final void onInviteToMyHome(byte var1, int var2) {
      Canvas.endDlg();
      Avatar var3;
      if ((var3 = LoadMap.getAvatar(var2)) != null) {
         if (var1 == 0) {
            Canvas.startOKDlg(T.youAreInvite + var3.name + ". " + T.doYouWant, new IActionInviteHouse(this, var2));
         } else if (var1 == 1) {
            idHouse = var2;
            GlobalService.gI().getHandler(11);
            Canvas.startWaitDlg();
         }
      }

   }

   protected final void doAction() {
      MessageScr.gI().doAction(focusP.IDDB, focusP.name);
      MessageScr.gI().switchToMe(this);
   }

   private void onInviteToMyHome() {
      Vector var1 = new Vector();
      short[] var2 = null;

      if (Canvas.stypeInt == 0) {
         if (DialLuckyScr.gI().isAutoDialEnabled()) {
            var1.addElement(new Command(T.utilStopAutoDial, new IActionUtilityCmd((byte)14)));
         }

         if (LoadMap.TYPEMAP == 16) {
            var1.addElement(new Command(T.mapFishingSettings, new IActionOpenFishingSettingsMenu()));
         }
         if (LoadMap.TYPEMAP == 16) {
            var1.addElement(new Command(T.mapAddNpc, new IActionMapNpcCmd((byte)0)));
            if (ClientUtilities.hasRememberedNpc()) {
               var1.addElement(new Command(T.mapNpcList, new IActionMapNpcCmd((byte)1)));
            }
            var1.addElement(new Command(T.eventMenu, new IActionOpenEventSubmenu()));
            var1.addElement(new Command(T.utilities, new IActionOpenUtilitySubmenu()));
         } else {
            var1.addElement(new Command(T.mapAddNpc, new IActionMapNpcCmd((byte)0)));
            if (ClientUtilities.hasRememberedNpc()) {
               var1.addElement(new Command(T.mapNpcList, new IActionMapNpcCmd((byte)1)));
            }
            var1.addElement(new Command(T.eventMenu, new IActionOpenEventSubmenu()));
            var1.addElement(new Command(T.utilities, new IActionOpenUtilitySubmenu()));
         }

         if (LoadMap.TYPEMAP == 14 || LoadMap.TYPEMAP == 15) {
            var1.addElement(new Command(T.mapFishingSettings, new IActionOpenFishingSettingsMenu()));
         }
      }

      int serverCmdStart = var1.size();
      if (LoadMap.TYPEMAP != 25 && listCmd != null && listCmd.size() > 0) {
         var2 = new short[serverCmdStart + listCmd.size()];

         int i;
         for(i = 0; i < serverCmdStart; ++i) {
            var2[i] = -1;
         }

         for(i = 0; i < listCmd.size(); ++i) {
            StringObj var3 = (StringObj)listCmd.elementAt(i);
            var2[serverCmdStart + i] = -1;
            var1.addElement(new Command(var3.str, 2, i));
         }

         if (var2.length > 0 && listCmd.size() > 0) {
            var2[0] = (short)((StringObj)listCmd.elementAt(0)).dis;
         }
      }

      var1.addElement(this.I);
      Menu var10000 = Menu.gI();
      boolean var5 = false;
      var10000.startAt(var1, -1);
      Menu.h = var2;
      if (var2 != null) {
         var10000.menuW += var10000.e;
      }

   }

   public static void doAction(byte var0) {
      GameMidlet.avatar.doAction(var0);
      AvatarService.gI().doFeel(var0);
   }

   public static void doSellectFeel(int var0) {
      GameMidlet.avatar.setFeel(var0);
      GameMidlet.avatar.firFeel = GameMidlet.avatar.feel;
      GameMidlet.avatar.numFeel = 0;
      AvatarService.gI().doFeel(var0 + 100);
   }

   public static void onFeel(int var0, byte var1) {
      Avatar var2;
      if ((var2 = LoadMap.getAvatar(var0)) != null) {
         if (var1 >= 100) {
            var2.setFeel(var1 - 100);
            var2.firFeel = var2.feel;
            var2.numFeel = 0;
            return;
         }

         var2.doAction(var1);
      }

   }

   protected final void doSellectAction() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < 4; ++var2) {
         Command var3 = MainMenu.gI().setCommandMenu(T.actionStr[var2], new IActionSelectAction(this, var2), var2 + 7);
         var1.addElement(var3);
      }

      MainMenu.gI().b = null;
      MainMenu.gI().setInfo(var1);
   }

   public final void update() {
      Canvas.loadMap.update();
      ClientUtilities.onMapUpdateTick();
      ClientUtilities.onAutoClickMapTick();
      if (Canvas.stypeInt == 0 && LoadMap.focusObj != null) {
         if (focusP != null && LoadMap.focusObj.catagory != 5 && focusP.IDDB > 2000000000) {
            super.center = this.J;
         } else {
            super.center = null;
         }

         super.right = LoadMap.cmdNext;
         if (LoadMap.focusObj.catagory == 0) {
            super.right.caption = ((Avatar)LoadMap.focusObj).name;
            if (super.right.caption.length() > 8) {
               super.right.caption = super.right.caption.substring(0, 8) + "..";
            }
         }
      }

      if (LoadMap.focusObj == null && super.right == LoadMap.cmdNext) {
         super.right = null;
         super.center = null;
      }

      Avatar var2;
      Avatar var3;
      if (isWedding) {
         Avatar var4;
         if (this.iGoChaSu == 1 && Canvas.load == -1) {
            System.out.println("updateWedding1111111111111: " + this.iGoChaSu);
            this.iGoChaSu = 2;
            var2 = LoadMap.getAvatar(-100);
            var3 = LoadMap.getAvatar(idUserWedding_1);
            var4 = LoadMap.getAvatar(idUserWedding_2);
            if (var3 != null && var4 != null) {
               AvCamera.gI().followPlayer = var2;
               System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
               var2.addChat(150, T.enterPassReferral[0] + var3.name + T.enterPassReferral[1] + var4.name + T.enterPassReferral[2], (byte)1);
            } else {
               this.resetWedding();
            }
         }

         if (this.iGoChaSu == 2 && Canvas.gameTick % 4 == 2 && LoadMap.getAvatar(-100).chat == null) {
            this.iGoChaSu = 3;
            var3 = LoadMap.getAvatar(idUserWedding_1);
            var4 = LoadMap.getAvatar(idUserWedding_2);
            if (var3 != null && var4 != null) {
               var4.xCur = 26 * LoadMap.w - LoadMap.w;
               var4.task = -5;
               var3.xCur = 26 * LoadMap.w - (LoadMap.w << 1);
               var3.task = -5;
               AvCamera.gI().followPlayer = var3;
            } else {
               this.resetWedding();
            }
         }

         if (this.iGoChaSu == 3) {
            var2 = LoadMap.getAvatar(idUserWedding_1);
            var3 = LoadMap.getAvatar(idUserWedding_2);
            if (var2 != null && var3 != null && var2.task == 0 && var3.task == 0) {
               this.iGoChaSu = 4;
               var4 = LoadMap.getAvatar(-100);
               AvCamera.gI().followPlayer = var4;
               var4.addChat(200, T.registerSuccess[0] + var2.name + T.enterPassReferral[1] + var3.name, (byte)1);
               var4.addChat(200, T.registerSuccess[1], (byte)1);
               var4.addChat(150, T.registerSuccess[2], (byte)1);
               var4.addChat(100, T.registerSuccess[3], (byte)1);
            }
         }

         if (this.iGoChaSu == 4) {
            var2 = LoadMap.getAvatar(idUserWedding_1);
            var3 = LoadMap.getAvatar(idUserWedding_2);
            var2.v = 4;
            var3.v = 4;
            if ((var4 = LoadMap.getAvatar(-100)).chat == null && var4.listChat.size() == 0) {
               if (idUserWedding_1 == GameMidlet.avatar.IDDB) {
                  ParkService.gI().doGivingDeferrent(idUserWedding_2, 101);
               }

               this.countWeeding = 0;
               this.iGoChaSu = 5;
            }
         }
      }

      if (this.iGoChaSu == 5 && this.countWeeding >= 0) {
         ++this.countWeeding;
         if (this.countWeeding > 20) {
            if (this.countWeeding == 21) {
               AnimateEffect var5 = new AnimateEffect(2, 0);
               Canvas.currentEffect.addElement(var5);
               AvCamera.gI().followPlayer = GameMidlet.avatar;
               GameMidlet.avatar.v = 4;
            }

            if (GameMidlet.avatar.IDDB != idUserWedding_1) {
               isWedding = false;
               this.countWeeding = -1;
            }

            if (GameMidlet.avatar.task == 0 && GameMidlet.avatar.IDDB == idUserWedding_1) {
               isWedding = false;
               var2 = LoadMap.getAvatar(idUserWedding_1);
               var3 = LoadMap.getAvatar(idUserWedding_2);
               if (var2 != null && var3 != null) {
                  var2.v = 4;
                  var3.v = 4;
               }

               this.iGoChaSu = 6;
               this.countWeeding = -1;
               ParkService.gI().doGivingDeferrent(idUserWedding_2, 102);
            }
         }
      }

      if (super.center == null && Canvas.stypeInt == 0 && Canvas.welcome == null) {
         super.center = this.e;
      } else if (Canvas.welcome != null) {
         super.center = null;
      }

      if (listFish.size() > 0) {
         for(int var1 = 0; var1 < listFish.size(); ++var1) {
            ((Fish)listFish.elementAt(var1)).update();
         }
      }

      if (this.chatDelay > 0) {
         --this.chatDelay;
         if (this.chatDelay == 0) {
            if (this.chatList.size() > 0) {
               this.chatList.removeElementAt(0);
            }

            if (this.chatList.size() > 0) {
               this.chatDelay = this.MAX_CHAT_DELAY;
            }
         }
      }

   }

   private void resetWedding() {
      isWedding = false;
      this.iGoChaSu = 0;

      for(int var1 = 0; var1 < LoadMap.playerLists.size(); ++var1) {
         MyObject var2;
         if ((var2 = (MyObject)LoadMap.playerLists.elementAt(var1)).catagory == 0) {
            Avatar var3;
            (var3 = (Avatar)var2).v = 4;
         }
      }

   }

   public final void updateKey() {
      if (Canvas.isPointerClick && Canvas.isPointer(0, 0, Canvas.w, 0)) {
         Canvas.isPointerClick = false;
         GlobalService.gI().doRequestContainer(GameMidlet.avatar.IDDB);
      }

      if (Canvas.welcome == null || !Welcome.isPaintArrow) {
         super.updateKey();
      }

      Canvas.loadMap.updateKey();
      GameMidlet.avatar.updateKey();
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
      Canvas.loadMap.paint(var1);
      int var2;
      if (listFish.size() > 0) {
         for(var2 = 0; var2 < listFish.size(); ++var2) {
            ((Fish)listFish.elementAt(var2)).paint(var1);
         }
      }

      Canvas.loadMap.paintBackGround(var1);
      Canvas.resetTrans(var1);
      if (this.chatList.size() != 0) {
         String var4 = (String)this.chatList.elementAt(0);
         if ((var2 = this.MAX_CHAT_DELAY - this.chatDelay) > 10) {
            var2 = 10;
         }

         int var5 = Canvas.w;

         for(int var6 = 0; var6 < var2; ++var6) {
            var5 >>= 1;
         }

         Canvas.borderFont.drawString(var1, var4, var5 + 3, 2, 0);
      }

      Canvas.resetTrans(var1);
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.onInviteToMyHome();
            return;
         case 2:
            GlobalService.gI().doCommunicate(focusP.IDDB);
            return;
         case 52:
            if (Canvas.currentMyScreen == MiniMap.me && LoadMap.TYPEMAP == -1) {
               Canvas.startWaitDlg();
               GlobalService.gI().getHandler(8);
            }
         default:
      }
   }

   public final void onJoinPark(byte var1, byte var2, short var3, short var4, Vector var5, Vector var6, Vector var7) {
      if (var2 == -1) {
         Canvas.startOK(T.areaIsFull, 52, (AvMain)null);
      } else {
         if (LoadMap.idTileImg == -1) {
            LoadMap.mapItemType = var6;
            LoadMap.mapItem = var7;
         }

         Canvas.paint.setVirtualKeyFish(var1);
         Canvas.clearKeyReleased();
         roomID = var1;
         boardID = var2;
         focusP = null;
         LoadMap.focusObj = null;
         GameMidlet.avatar.task = 0;
         if (Canvas.isInitChar || var1 != LoadMap.TYPEMAP || var1 == LoadMap.TYPEMAP && LoadMap.idTileImg == -1 || LoadMap.idTileImg == -1 && (LoadMap.TYPEMAP == 14 || LoadMap.TYPEMAP == 15 || LoadMap.TYPEMAP == 16)) {
            GameMidlet.avatar.ableShow = false;
            if (var1 != LoadMap.TYPEMAP) {
               GameMidlet.avatar.x = var3;
               GameMidlet.avatar.y = var4;
            }

            LoadMap.treeLists.removeAllElements();
            Canvas.loadMap.load(var1 + 1);
         } else {
            listFish.removeAllElements();
            LoadMap.playerLists.removeAllElements();
            LoadMap.dynamicLists.removeAllElements();
            Canvas.currentEffect.removeAllElements();
            LoadMap.addPlayer(GameMidlet.avatar);
         }

         if (var6 != null) {
            LoadMap.setMapItemType();
         }

         if (LoadMap.xDichChuyen_ != -1) {
            GameMidlet.avatar.setPos(LoadMap.xDichChuyen_, LoadMap.C);
            LoadMap.C = -1;
            LoadMap.xDichChuyen_ = -1;
         }

         if (LoadMap.xDichChuyen != -1) {
            GameMidlet.avatar.x = LoadMap.xDichChuyen;
            GameMidlet.avatar.y = LoadMap.yDichChuyen;
            LoadMap.yDichChuyen = -1;
            LoadMap.xDichChuyen = -1;
            doMove(GameMidlet.avatar.x, GameMidlet.avatar.y, GameMidlet.avatar.direct, 0);
         }

         Canvas.instance.setSize();
         if (Canvas.currentMyScreen != this) {
            if (OnScreen.c == 0) {
               gI().switchToMe();
            } else {
               OnScreen.c = 2;
               SplashScr.gI().switchToMe();
            }
         }

         for(var1 = 0; var1 < var5.size(); ++var1) {
            MyObject var8;
            if ((var8 = (MyObject)var5.elementAt(var1)).catagory == 0) {
               Avatar var9;
               (var9 = (Avatar)var8).xCur = var9.x;
               var9.yCur = var9.y;
               var9.dirLast = var9.direct;
               var9.orderSeriesPath();
               if (var9.IDDB != GameMidlet.avatar.IDDB) {
                  setGender(var9);
                  LoadMap.addPlayer(var9);
               }
            } else if (var8.catagory == 5) {
               Drop_Part var10;
               (var10 = (Drop_Part)var8).x0 = var10.x;
               var10.y0 = var10.y;
               LoadMap.playerLists.addElement(var10);
            }
         }

         if (Bus.isRun) {
            doMove(Bus.posBusStop.x, Bus.posBusStop.y, GameMidlet.avatar.direct, GameMidlet.avatar.direct_);
         } else {
            ++GameMidlet.avatar.y;
            this.move();
         }

         doSellectFeel(GameMidlet.avatar.feel);
         if (Canvas.stypeInt == 0 && Canvas.welcome == null) {
            super.left = this.cmdMenu;
         }

         focusP = null;
         if (LoadMap.TYPEMAP != 25) {
            Canvas.endDlg();
         }

         if (roomID != 21 && LoadMap.TYPEMAP != 21) {
            ClientUtilities.onAfterJoinPark(roomID);
         }
         Canvas.instance.sizeChanged(0, 0);
         if (Canvas.isInitChar) {
            if (LoadMap.TYPEMAP == 9 && Welcome.indexMapScr != 0) {
               (Canvas.welcome = new Welcome()).initMapScr();
            } else if (!Bus.isRun && LoadMap.TYPEMAP == 23) {
               (Canvas.welcome = new Welcome()).initKhuMuaSam();
            } else if (LoadMap.TYPEMAP == 25 && Welcome.indexFarmPath > 0) {
               (Canvas.welcome = new Welcome()).initFarmPath(instance);
            }

            super.left = null;
            super.center = null;
         }

         GameMidlet.avatar.M = false;
         GameMidlet.avatar.direct_ = 0;
         GameMidlet.avatar.v = 4;
         r = false;
         isWedding = false;
         Canvas.currentFace = null;
         if (LoadMap.TYPEMAP == 108) {
            AvCamera.gI().update();
            AvCamera.gI().notTrans();
         }

         if (Canvas.load == 0) {
            Canvas.load = 1;
         }
      }

   }

   public static void onJoinCasino() {
      byte var0 = 0;
      switch (typeCasino) {
         case 0:
            var0 = 3;
            break;
         case 1:
            var0 = 7;
            break;
         case 2:
            var0 = 21;
            break;
         case 3:
            var0 = 22;
            break;
         case 4:
            var0 = 23;
            break;
         case 5:
            var0 = 22;
      }

      GlobalService.gI().setGameType(var0);
   }

   public final void doJoinShop(byte var1) {
      if (typeJoin == -1) {
         this.move();
         System.out.println("doJoinShop: " + var1);
         Canvas.startWaitDlg();
         typeJoin = var1;
         GlobalService.gI().getHandler(8);
      }

   }

   public static void doMove(int var0, int var1, int var2, int var3) {
      if ((GameMidlet.CLIENT_TYPE == 9 || GameMidlet.CLIENT_TYPE == 11) && !isWedding) {
         GameMidlet.avatar.xCur = var0;
         GameMidlet.avatar.yCur = var1;
         ParkService.gI().doMove(var0, var1, var2, var3);
      }

   }

   public final void move() {
      doMove(GameMidlet.avatar.x, GameMidlet.avatar.y, GameMidlet.avatar.direct, GameMidlet.avatar.direct_);
   }

   public static void onMovePark(int var0, int var1, int var2, int var3, short var4) {
      Avatar var5 = LoadMap.getAvatar(var0);
      if (var0 != GameMidlet.avatar.IDDB && !isWedding && var5 != null) {
         if (var5.ableShow && var5.task == 0) {
            var5.ableShow = false;
            var5.setPos(var1, var2);
            var5.direct_ = var4;
         }

         if (var5.action == -3) {
            var5.action = 0;
         }

         var5.isJumps = -1;
         if (var5.task == 0) {
            AvPosition var6;
            (var6 = new AvPosition(var1, var2, var3)).depth = var4;
            var5.moveList.addElement(var6);
         }
      }

   }

   public final void onPlayerJoinPark(Avatar var1) {
      setGender(var1);
      var1.orderSeriesPath();
      var1.ableShow = true;
      Avatar var2;
      if ((var2 = LoadMap.getAvatar(var1.IDDB)) != null) {
         LoadMap.playerLists.removeElement(var2);
      }

      LoadMap.addPlayer(var1);
   }

   private static void setGender(Avatar var0) {
      APartInfo var1;
      if ((var1 = AvatarData.getPartByZ(var0.seriPart, 50)) != null) {
         var0.gender = var1.gender;
      }

   }

   public static void onPlayerLeave(int var0) {
      Avatar var1;
      if ((var1 = LoadMap.getAvatar(var0)) != null) {
         var1.resetTypeChair();
         var1.isLeave = true;
         Fish var2;
         if ((var2 = FishingScr.getFish(var0)) != null) {
            listFish.removeElement(var2);
         }
      }

   }

   public final void keyPress(int var1) {
      ChatTextField.gI().startChat(var1, this);
      super.keyPress(var1);
   }

   public final void onChatFromMe(String var1) {
      if (!var1.trim().equals("")) {
         String var2 = var1.trim();
         if (var2.length() >= 2 && (var2.charAt(0) == 'k' || var2.charAt(0) == 'K')) {
            int var3 = 1;

            int var4;
            for(var4 = 0; var3 < var2.length(); ++var3) {
               char var5 = var2.charAt(var3);
               if (var5 < '0' || var5 > '9') {
                  var4 = -1;
                  break;
               }

               var4 = var4 * 10 + (var5 - 48);
            }

            if (var4 >= 0) {
               ParkService.gI().doJoinPark(roomID, var4);
               return;
            }
         }

         if (var1.indexOf("dmw") != -1) {
            if (focusP != null) {
               GlobalService.gI().doServerKick(focusP.IDDB, var1);
            }
         } else if (var1.indexOf("ptw") == 0 && focusP != null && focusP.chat != null && focusP.chat.chats != null) {
            String var6 = var1 + " (";

            for(int var7 = 0; var7 < focusP.chat.chats.length; ++var7) {
               var6 = var6 + " " + focusP.chat.chats[var7];
            }

            var6 = var6 + ").";
            GlobalService.gI().doServerKick(focusP.IDDB, var6);
         } else {
            ParkService.gI().chatToBoard(var1);
         }
      }

   }

   public static void onChatFrom(int var0, String var1) {
      Avatar var2;
      if (LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53 && (var2 = LoadMap.getAvatar(var0)) != null) {
         if (var0 >= 2000001000 && var1 != null && var1.startsWith("HP:")) {
            String var3 = var1.substring(3).trim();

            try {
               long var4 = Long.parseLong(var3);
               if (var4 < 0L) {
                  var4 = 0L;
               }

               int var6 = var4 > 2147483647L ? Integer.MAX_VALUE : (int)var4;
               var2.bossHp = var6;
               if (var2.bossMaxHp < var6) {
                  var2.bossMaxHp = var6;
               }
            } catch (Exception var8) {
            }
         }

         var2.chat = null;
         var2.addChat(100, var1, (byte)(var0 >= 2000000000 ? 1 : 0));
         if (var0 < 2000000000) {
            MessageScr var10000 = MessageScr.gI();
            var1 = var2.name + ": " + var1;
            var10000.a.a(var1);
         }
      }

   }

   protected static void doKiss() {
      if (focusP != null && focusP.task == 0) {
         ParkService.gI().doGivingDeferrent(focusP.IDDB, 101);
      }

   }

   public final void doGiving(int var1) {
      if (focusP != null) {
         APartInfo var2;
         Canvas.getTypeMoney((var2 = (APartInfo)AvatarData.getPart((short)var1)).price[0], var2.price[1], new IActionGiving(this, var2), new IActionGiving1(this, var2), (IAction)null);
      }

   }

   public static void doGivingDefferent(int var0) {
      ParkService.gI().doGivingDeferrent(focusP.IDDB, var0);
   }

   public final void onGivingDefferent(int var1, int var2, int var3, String var4, int var5) {
      if (var3 == -1) {
         Canvas.startOKDlg(var4);
      } else {
         this.translates(1, var1, var2, var3, var5);
      }

   }

   public final void onGiftGiving(int var1, int var2, int var3, String var4, int var5, int var6, int var7, int var8) {
      if (var3 == -1) {
         Canvas.startOKDlg(var4);
      } else {
         if (var1 == GameMidlet.avatar.IDDB) {
            System.out.println("onGiftGiving: " + var5);
            GameMidlet.avatar.updateMoney(var6, var7, var8);
         }

         this.translates(0, var1, var2, var3, 0);
      }

   }

   private void translates(int var1, int var2, int var3, int var4, int var5) {
      Avatar var6 = LoadMap.getAvatar(var2);
      Avatar var7 = LoadMap.getAvatar(var3);
      if (var6 != null && var7 != null && var6.task == 0 && var7.task == 0) {
         var6.idTo = var7.IDDB;
         var6.idFrom = var6.IDDB;
         var7.idFrom = var6.IDDB;
         var7.idTo = var7.IDDB;
         if (var2 == GameMidlet.avatar.IDDB) {
            GameMidlet.avatar.yCur = var7.y;
            if (GameMidlet.avatar.x < var7.x) {
               var2 = var7.x - 15;
            } else {
               var2 = var7.x + 15;
            }

            GameMidlet.avatar.xCur = var2;
            doMove(var2, var7.y, GameMidlet.avatar.direct, GameMidlet.avatar.direct_);
         }

         if (var3 == GameMidlet.avatar.IDDB) {
            doMove(GameMidlet.avatar.x, GameMidlet.avatar.y, var6.direct == 0 ? Base.LEFT : 0, GameMidlet.avatar.direct_);
         }

         if (var1 == 1) {
            var7.isJumps = -1;
            switch (var4) {
               case 0:
                  var7.task = var6.task = -3;
                  this.showChat(var6.name + " " + T.giveGiftFlower + var7.name);
                  break;
               case 100:
                  if (var7.task == 0) {
                     var6.task = -2;
                     var7.task = -2;
                     var6.moveList.removeAllElements();
                     var7.moveList.removeAllElements();
                     var6.focus = var7;
                     var6.doAction(var7.x, var7.y + 5);
                  }
                  break;
               case 101:
                  if (var7.task == 0) {
                     var6.task = 11;
                     var7.task = 11;
                     var6.moveList.removeAllElements();
                     var7.moveList.removeAllElements();
                     var6.focus = var7;
                     if (var6.x < var7.x) {
                        var6.doAction(var7.x - 20, var7.y + 2);
                     } else {
                        var6.doAction(var7.x + 20, var7.y + 2);
                     }
                  }
                  break;
               case 102:
               case 103:
                  var7.task = var6.task = 12;
                  var7.Y = var6.Y = (short)var5;
                  this.showChat(var6.name + " " + T.giveGift + " " + var7.name);
                  break;
               default:
                  this.showChat(var6.name + " tặng quà " + var7.name);
            }
         } else {
            var6.task = 9;
            var7.task = 8;
            var7.isJumps = -1;
            var7.idGift = var4;
            Part var8 = AvatarData.getPart((short)var4);
            this.showChat(var6.name + " " + T.dunation + " " + var8.name + " " + T.cho + " " + var7.name);
         }

         var7.firFeel = var7.feel;
         var7.numFeel = 0;
         var6.firFeel = var6.feel;
         var6.numFeel = 0;
      }

   }

   public static void setGifts(Avatar var0) {
      SeriPart var1;
      if ((var1 = AvatarData.getSeriByZ(((APartInfo)AvatarData.getPart((short)var0.idGift)).zOrder, var0.seriPart)) == null) {
         var0.addSeri(new SeriPart((short)var0.idGift));
         var0.orderSeriesPath();
      } else {
         var1.idPart = (short)var0.idGift;
      }

   }

   public static void doRequestAddFriend(Avatar var0) {
      if (var0 != null) {
         ParkService.gI().doRequestAddFriend(var0.IDDB);
         Canvas.startOKDlg(T.pleaseWait + " " + var0.name + "  " + T.agree);
      }

   }

   public final void onRequestAddFriend(Avatar var1, String var2) {
      UNK var3;
      (var3 = new UNK(T.addFriend, -2, new Command(T.agree, new IActionAddFriend4(this, var1)), new Command(T.refused, new IActionAddFriend5(this, var1)), false)).a(var2);
      MessageScr var4 = MessageScr.gI();
      var3.a = true;
      var4.b(var3);
      if (Canvas.currentMyScreen != MessageScr.gI()) {
         ++MyScreen.nMsg;
      }

   }

   public static void onAddFriend(boolean var0, String var1) {
      if (var0) {
         ListScr.gI();
         ListScr.removeList();
      }

      Canvas.startOKDlg(var1);
   }

   protected static void doRequestYourInfo() {
      if (focusP != null) {
         Canvas.startWaitCancelDlg(T.pleaseWait);
         ParkService.gI().doRequestYourInfo(focusP.IDDB);
      }

   }

   public static void onRemoveItem(int var0, int var1) {
      Avatar var2;
      SeriPart var3;
      if (var0 != GameMidlet.avatar.IDDB && (var2 = LoadMap.getAvatar(var0)) != null && (var3 = AvatarData.getSeriByIdPart(var2.seriPart, var1)) != null) {
         var2.seriPart.removeElement(var3);
      }

   }

   public final void onParkList(int[] var1) {
      if (var1 != null) {
         zoneMaxIndex = var1.length - 1;
      }
      ParkListSrc.gI().setList(var1);
      ParkListSrc.gI().switchToMe(this);
   }

   public final void onContainer(Vector var1) {
      GameMidlet.listContainer = var1;
      if (MainMenu.gI().isWearing) {
         MainMenu.gI();
         MainMenu.doWearing();
      } else {
         this.doStore();
      }

   }

   public static void onUsingPart(int var0, short var1) {
      Avatar var2;
      if ((var2 = LoadMap.getAvatar(var0)) != null) {
         if (AvatarData.getPart(var1).zOrder == -1) {
            if (var2.idPet == var1) {
               Pet var3;
               if ((var3 = LoadMap.getPet(var2.IDDB)) != null) {
                  LoadMap.playerLists.removeElement(var3);
                  var2.idPet = -1;
               }
            } else {
               var2.changePet(var1);
               AvatarService.gI().doRequestExpicePet(var2.IDDB);
            }
         } else {
            SeriPart var4;
            if ((var4 = AvatarData.getSeriByIdPart(var2.seriPart, var1)) != null) {
               var2.seriPart.removeElement(var4);
            } else {
               var2.addSeriPart(new SeriPart(var1));
               var2.orderSeriesPath();
            }
         }

         if (var0 == GameMidlet.avatar.IDDB) {
            if (Canvas.currentMyScreen == PopupShop.gI()) {
               PopupShop.gI().close();
            }

            GameMidlet.listContainer = null;
            Canvas.endDlg();
         }

         r = false;
      }

   }

   public final Command cmdDellPart(Vector var1, int var2, int var3, boolean var4) {
      Command var5 = new Command(T.removee, new IActionDellPart(this, var1, var2, var3));
      return var4 ? new Command(T.menu, new IActionDellPart1(this, var5)) : var5;
   }

   protected final void doStore() {
      Avatar var1 = GameMidlet.avatar;
      if (Canvas.currentMyScreen != MainMenu.me) {
         PopupShop.gI().isFull = true;
         PopupShop.gI().addElement(new String[]{T.container, T.wearing}, new Vector[]{this.getListCmdDoUsing(GameMidlet.listContainer, var1.IDDB, 1), this.getListYourPart(var1, 0)}, (Vector)null);
         PopupShop.gI().setCmdLeft(this.cmdDellPart(var1.seriPart, 0, 0, false), 1);
         PopupShop.gI().setCmdLeft(this.cmdDellPart(GameMidlet.listContainer, 1, 0, true), 0);
         if (Canvas.currentMyScreen != PopupShop.gI()) {
            PopupShop.gI().switchToMe();
         }
      }

   }

   public final Vector getListYourPart(Avatar var1, int var2) {
      Avatar var6;
      (var6 = new Avatar()).name = var1.name;
      var6.setMoney(var1.getMoney());
      var6.IDDB = var1.IDDB;
      var6.idPet = var1.idPet;
      var6.hungerPet = var1.hungerPet;

      for(int var3 = 0; var3 < var1.seriPart.size(); ++var3) {
         SeriPart var4;
         Part var5;
         if ((var5 = AvatarData.getPart((var4 = (SeriPart)var1.seriPart.elementAt(var3)).idPart)) != null && var5.zOrder != 30 && var5.zOrder != 40) {
            var6.addSeri(var4);
         }
      }

      if (var6.idPet != -1) {
         SeriPart var7;
         (var7 = new SeriPart(var6.idPet)).time = (byte)(100 - var6.hungerPet);
         var6.seriPart.addElement(var7);
      }

      new Vector();
      return this.getListCmdDoUsing(var6.seriPart, var6.IDDB, 0);
   }

   public final Vector getListCmdDoUsing(Vector var1, int var2, int var3) {
      Vector var4 = new Vector();

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         SeriPart var7;
         Part var8 = AvatarData.getPart((var7 = (SeriPart)var1.elementAt(var5)).idPart);
         String var9 = null;
         if (var2 == GameMidlet.avatar.IDDB && (!AvatarData.isZOrderMain(var8.zOrder) || var3 != 0)) {
            if (var3 == 1) {
               var9 = T.use;
            } else {
               var9 = T.trans;
            }
         }

         CommandUsingPart var6 = new CommandUsingPart(this, var9, new CommandUsingPart1(this, var7, var2, var3, var5), var7, var5, var3);
         var4.addElement(var6);
      }

      return var4;
   }

   private Command b(IndexPlayer var1) {
      return new class_fk(this, (String)null, (IAction)null, var1);
   }

   public final void a(IndexPlayer var1) {
      Vector var2;
      (var2 = new Vector()).addElement(this.b(var1));
      PopupShop.gI().isFull = true;
      PopupShop.gI().addElement(new String[]{T.mySeft}, new Vector[1], var2);
      if (Canvas.currentMyScreen != PopupShop.gI()) {
         PopupShop.gI().switchToMe();
      }

   }

   public static void a(Graphics var0, String var1, int var2, int var3, int var4) {
      var0.drawImage(imgBar, var2, var3 + 2, 17);
      int var5 = imgBar.getWidth() - 4 * AvMain.hd;
      int var6;
      if ((var6 = var4 * var5 / 100) > var5) {
         var6 = var5;
      }

      if (var6 < 0) {
         var6 = 0;
      }

      Canvas.fontChatB.drawString(var0, var1, var2 - 32 * AvMain.hd, var3 + 4 * AvMain.hd - AvMain.hBlack / 2, 1);
      PaintPopup.fill(var5 = var2 - 27 * AvMain.hd, var3 + 4 * AvMain.hd - 1, var6, 4 * AvMain.hd, 47084, var0);
      PaintPopup.fill(var5, var3 + 5 * AvMain.hd - 1, var6, 1 * AvMain.hd, 8575990, var0);
      PaintPopup.fill(var5 + var6, var3 + 4 * AvMain.hd - 1, 1, 4 * AvMain.hd, 13379, var0);
      if (!var1.equals("")) {
         Canvas.fontChatB.drawString(var0, String.valueOf(var4), var2 + 29 * AvMain.hd + Canvas.fontChatB.getWidth("100"), var3 + 4 * AvMain.hd - AvMain.hBlack / 2, 1);
      }

   }

   public static String strTkFarm() {
      return T.youFirstFire + ": " + Canvas.getMoneys(GameMidlet.avatar.money[0]) + T.dola;
   }

   private static void f(int var0, int var1) {
      if (var0 != var1) {
         Canvas.addFlyTextSmall((var1 - var0 > 0 ? "+" : "") + (var1 - var0), GameMidlet.avatar.x, GameMidlet.avatar.y - 40, -1, 0, -1);
      }

   }

   public final void a(int var1, IndexPlayer var2, Avatar var3, String var4, short var5, byte var6, byte var7, String var8, short var9, String var10) {
      if (var1 == GameMidlet.avatar.IDDB) {
         f(GameMidlet.myIndexP.g, var2.g);
         f(GameMidlet.myIndexP.a, var2.a);
         f(GameMidlet.myIndexP.b, var2.b);
         f(GameMidlet.myIndexP.e, var2.e);
         f(GameMidlet.myIndexP.c, var2.c);
         f(GameMidlet.myIndexP.d, var2.d);
         GameMidlet.myIndexP = var2;
      }

      Canvas.endDlg();
      Avatar var19;
      if ((var19 = LoadMap.getAvatar(var1)) != null && isOpenInfo) {
         isOpenInfo = false;
         byte var24 = var7;
         var7 = var6;
         var6 = (byte)var5;
         Object var20 = var19;
         Vector var12 = new Vector();
         if (var19.IDDB != GameMidlet.avatar.IDDB) {
            var12 = this.getListYourPart(var19, 0);
         }

         Vector var13 = new Vector();
         String var16 = T.youFirstFire + ": " + Canvas.getPriceMoney(GameMidlet.avatar.money[0], GameMidlet.avatar.money[2], GameMidlet.avatar.luongKhoa, true);
         StringObj var17 = new StringObj(var16, Canvas.fontChatB.getWidth(var16));
         Pet var18 = LoadMap.getPet(var19.IDDB);
         class_fj var15 = new class_fj((String)null, (IAction)null, var19, var18, var17);
         var19.direct = 0;
         var13.addElement(var15);
         if (var3 != null) {
            var3.idWedding = var19.idWedding;
            var20 = new CMDUnkMapScr1(this, "", (IAction)null, var4, var19, var3, (short)var6, var7, var24, var8);
            var13.addElement(var20);
         }

         if (GameMidlet.avatar.IDDB != ((Base)var20).IDDB) {
            var13.addElement(this.b(var2));
         }

         if (Canvas.currentMyScreen != MainMenu.me) {
            PopupShop.gI().isFull = true;
            if (GameMidlet.avatar.IDDB == ((Base)var20).IDDB) {
               if (var3 != null) {
                  PopupShop.gI().addElement(new String[]{T.mySeft, T.wedding}, new Vector[2], var13);
                  if (var9 != -1) {
                     PopupShop.gI().setCmdLeft(new Command(var10, new class_fv(this, var9)), 1);
                  }
               } else {
                  PopupShop.gI().addElement(new String[]{T.mySeft}, new Vector[1], var13);
               }
            } else if (var3 != null) {
               PopupShop.gI().addElement(new String[]{T.mySeft, T.wedding, T.index, T.mySeft}, new Vector[]{null, null, null, var12}, var13);
               if (var9 != -1) {
                  PopupShop.gI().setCmdLeft(new Command(var10, new class_fo(this, var9)), 1);
               }
            } else {
               PopupShop.gI().addElement(new String[]{T.mySeft, T.index, T.mySeft}, new Vector[]{null, null, var12}, var13);
            }

            if (Canvas.currentMyScreen != PopupShop.gI()) {
               PopupShop.gI().switchToMe();
            }
         }
      }

   }

   public final void doOpenIceDream(String var1, int var2) {
      Vector var3 = new Vector();

      for(int var4 = 0; var4 < AvatarData.listItemInfo.size(); ++var4) {
         Item var5;
         if ((var5 = (Item)AvatarData.listItemInfo.elementAt(var4)).shopType == var2) {
            var3.addElement(var5);
         }
      }

      Vector var8 = new Vector();

      for(int var9 = 0; var9 < var3.size(); ++var9) {
         Item var6 = (Item)var3.elementAt(var9);
         CommandIceDream var7 = new CommandIceDream(this, T.buy, new IActionIceDream(this, var6), var6, var9);
         var8.addElement(var7);
      }

      PopupShop.gI().switchToMe();
      PopupShop.gI().addElement(new String[]{var1}, new Vector[]{var8}, (Vector)null);
   }

   protected final void doBuyIceDream(Item var1) {
      Canvas.startOKDlg(T.doYouWantBuy, new IActionBuyDream(this, var1));
   }

   public static void onBuyIceDream(short var0, int var1) {
      Canvas.endDlg();
      PopupShop.isTransFocus = true;
      Item var2;
      if ((var2 = Item.getItemByList(AvatarData.listItemInfo, var0)) != null) {
         if (var2.shopType == 5) {
            AvatarService.gI().doRequestExpicePet(GameMidlet.avatar.IDDB);
         }

         GameMidlet.avatar.setMoney(var1);
      }

   }

   public final void onOpenShop(byte var1, int var2, String var3, short[] var4, int var5, String[] var6) {
      if (Canvas.currentMyScreen != PopupShop.gI()) {
         setAvatarShop(GameMidlet.avatar);
         if (var2 == 26) {
            if (focusP == null) {
               return;
            }

            setAvatarShop(focusP);
         } else {
            setAvatarShop(GameMidlet.avatar);
         }

         Vector var7 = new Vector();
         if (var1 == 0) {
            int var27;
            if (var4 != null && var4.length != 0) {
               for(var27 = 0; var27 < var4.length; ++var27) {
                  var7.addElement(AvatarData.getPart(var4[var27]));
               }
            } else {
               for(var27 = 0; var27 < AvatarData.listPart.length; ++var27) {
                  Part var8;
                  if ((var8 = AvatarData.listPart[var27]) != null && (var8.price[0] > 0 || var8.price[1] > 0) && var2 == var8.sell) {
                     var7.addElement(var8);
                  }
               }
            }

            int var19;
            String var11;
            if (var2 == 26) {
               Vector[] var14 = new Vector[6];

               for(var19 = 0; var19 < 6; ++var19) {
                  var14[var19] = new Vector();
               }

               int[] var21 = new int[6];

               int var20;
               for(var20 = 0; var20 < var7.size(); ++var20) {
                  Part var23 = (Part)var7.elementAt(var20);
                  var11 = "";
                  if (var6 != null && var6.length > 0) {
                     var11 = var6[var20];
                  }

                  var3 = T.mapGive;
                  int var10000;
                  int var10003;
                  if (var23.zOrder == 20) {
                     var14[0].addElement(new CommandOpenShop(this, var3, new IActionOpenShop(this, var23, var4 != null ? var4[var20] : -1, var2, var11, var5, var21[0]), var23, var4 != null ? var4[var20] : -1, var21[0], var5, var2));
                     var10003 = var21[0];
                     var10000 = var21[0];
                     var21[0] = var10003 + 1;
                  } else if (var23.zOrder == 10) {
                     var14[1].addElement(new CommandOpenShop(this, var3, new IActionOpenShop(this, var23, var4 != null ? var4[var20] : -1, var2, var11, var5, var21[1]), var23, var4 != null ? var4[var20] : -1, var21[1], var5, var2));
                     var10003 = var21[1];
                     var10000 = var21[1];
                     var21[1] = var10003 + 1;
                  } else if (var23.zOrder != 52 && var23.zOrder != 53 && var23.zOrder != 5) {
                     if (var23.zOrder == 60) {
                        var14[3].addElement(new CommandOpenShop(this, var3, new IActionOpenShop(this, var23, var4 != null ? var4[var20] : -1, var2, var11, var5, var21[3]), var23, var4 != null ? var4[var20] : -1, var21[3], var5, var2));
                        var10003 = var21[3];
                        var10000 = var21[3];
                        var21[3] = var10003 + 1;
                     } else if (var23.zOrder == 70) {
                        var14[4].addElement(new CommandOpenShop(this, var3, new IActionOpenShop(this, var23, var4 != null ? var4[var20] : -1, var2, var11, var5, var21[4]), var23, var4 != null ? var4[var20] : -1, var21[4], var5, var2));
                        var10003 = var21[4];
                        var10000 = var21[4];
                        var21[4] = var10003 + 1;
                     } else {
                        var14[5].addElement(new CommandOpenShop(this, var3, new IActionOpenShop(this, var23, var4 != null ? var4[var20] : -1, var2, var11, var5, var21[5]), var23, var4 != null ? var4[var20] : -1, var21[5], var5, var2));
                        var10003 = var21[5];
                        var10000 = var21[5];
                        var21[5] = var10003 + 1;
                     }
                  } else {
                     var14[2].addElement(new CommandOpenShop(this, var3, new IActionOpenShop(this, var23, var4 != null ? var4[var20] : -1, var2, var11, var5, var21[2]), var23, var4 != null ? var4[var20] : -1, var21[2], var5, var2));
                     var10003 = var21[2];
                     var10000 = var21[2];
                     var21[2] = var10003 + 1;
                  }
               }

               var20 = 0;

               for(int var24 = 0; var24 < var14.length; ++var24) {
                  if (var14[var24].size() > 0) {
                     ++var20;
                  }
               }

               String[] var25 = T.shopCategories;
               byte[] var26 = new byte[]{0, 1, 2, 3, 4, 5};
               Vector[] var15 = new Vector[var20];
               byte[] var12 = new byte[var20];
               String[] var16 = new String[var20];
               var5 = 0;
               int var17 = 0;

               while(true) {
                  if (var17 >= var14.length) {
                     PopupShop.gI().switchToMe();
                     PopupShop.isHorizontal = true;
                     PopupShop.gI().addElement(var16, var15, (Vector)null);
                     break;
                  }

                  if (var14[var17].size() > 0 || var17 == 5) {
                     if (var17 == 5) {
                        int var18 = var14[5].size();

                        for(var19 = 0; var19 < listItemEffect.size(); ++var19) {
                           ItemEffectInfo var22 = (ItemEffectInfo)listItemEffect.elementAt(var19);
                           var14[5].addElement(new CommandGiftDef(this, T.giveGift, new IActionGiftDef(this, var19, var22.IDAction), var19, var22, var18));
                        }
                     }

                     var15[var5] = var14[var17];
                     var12[var5] = var26[var17];
                     var16[var5] = var25[var17];
                     ++var5;
                  }

                  ++var17;
               }
            } else {
               Vector var13 = new Vector();

               for(var19 = 0; var19 < var7.size(); ++var19) {
                  Part var9 = (Part)var7.elementAt(var19);
                  String var10 = "";
                  if (var6 != null && var6.length > 0) {
                     var10 = var6[var19];
                  }

                  if (var2 == 100) {
                     var11 = T.dial;
                  } else if (var2 == 26) {
                     var11 = T.mapGive;
                  } else {
                     var11 = T.buy;
                  }

                  var13.addElement(new CommandOpenShop(this, var11, new IActionOpenShop(this, var9, var4 != null ? var4[var19] : -1, var2, var10, var5, var19), var9, var4 != null ? var4[var19] : -1, var19, var5, var2));
               }

               if (var13.size() > 0) {
                  PopupShop.gI().switchToMe();
                  PopupShop.isHorizontal = true;
                  PopupShop.gI().addElement(new String[]{var3}, new Vector[]{var13}, (Vector)null);
               }
            }

            Canvas.endDlg();
         }
      }

   }

   public static void onRequestExpicePet(int var0, byte var1) {
      if (var0 == GameMidlet.avatar.IDDB) {
         GameMidlet.avatar.hungerPet = (short)var1;
      } else {
         Avatar var2;
         if ((var2 = LoadMap.getAvatar(var0)) != null) {
            var2.hungerPet = (short)var1;
         }
      }

   }

   public final void a(int var1, int var2, String var3, String[] var4) {
      Vector var5 = new Vector();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         var5.addElement(new Command(var4[var6], new class_ac(this, var1, var2, var6)));
      }

      Canvas.setInfoC(var3, var5);
   }

   public static void onChangeClan(int var0, short var1) {
      Avatar var2;
      if ((var2 = LoadMap.getAvatar(var0)) != null) {
         var2.idImg = var1;
      }

   }

   private void showChat(String var1) {
      this.chatList.addElement(var1);
      if (this.chatDelay == 0) {
         this.chatDelay = this.MAX_CHAT_DELAY;
      }

   }

   public final void onMenuRotate(Vector var1) {
      if (var1.size() != 0) {
         Vector var2 = new Vector();

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            StringObj var4 = (StringObj)var1.elementAt(var3);
            var2.addElement(new CommandMenuRotate(this, var4.str, new IActionExchange(this, var4), var4));
         }

         MainMenu.gI().setInfo(var2);
      }

   }

   public static void onDropPark(byte var0, int var1, short var2, int var3, short var4, short var5) {
      Drop_Part var6;
      (var6 = new Drop_Part(var0, var2, var3)).startDropFrom(var1, var4, var5);
      LoadMap.playerLists.addElement(var6);
      LoadMap.orderVector(LoadMap.treeLists);
   }

   public static void onGetPart(int var0, int var1) {
      var0 = var0;
      int var2 = 0;

      Drop_Part var10000;
      while(true) {
         if (var2 >= LoadMap.playerLists.size()) {
            var10000 = null;
            break;
         }

         MyObject var3;
         Drop_Part var5;
         if ((var3 = (MyObject)LoadMap.playerLists.elementAt(var2)).catagory == 5 && (var5 = (Drop_Part)var3).ID == var0) {
            var10000 = var5;
            break;
         }

         ++var2;
      }

      if (var10000 != null) {
         var10000.startFlyTo(var1);
      }

   }

   public static void onEffect(EffectManager var0) {
      if (LoadMap.effManager == null) {
         LoadMap.effManager = new Vector();
      }

      LoadMap.effManager.addElement(var0);
   }

   public static void onEmotionList(int var0, Vector var1) {
      Avatar var2;
      if ((var2 = LoadMap.getAvatar(var0)) != null) {
         var2.emotionList = var1;
         var2.timeEmotion = 0;
      }

   }

   public final void doJoin() {
      if (this.isTour) {
         this.isTour = true;
         Canvas.startWaitDlg();
         int selected = MiniMap.gI().selected;
         if (selected == 2) {
            GlobalService.gI().requestCityMap((byte)-1);
         } else {
            byte[] var1 = new byte[]{0, 13, 20, 9, 23, 11, 17};
            if (selected < 0 || selected >= var1.length) {
               // Invalid minimap selection; avoid crashing.
               this.isTour = false;
               Canvas.endDlg();
               return;
            }
            ParkService.gI().doJoinPark(var1[selected], -1);
         }
      }

   }

   public final void joinCitymap() {
      if (GameMidlet.avatar.gender == 0) {
         if (!GlobalLogicHandler.isNewVersion) {
            RegisterScr.gI().switchToMe();
            Canvas.endDlg();
         }
      } else {
         if (Canvas.currentMyScreen != MessageScr.me && Canvas.currentMyScreen != OptionScr.instance) {
            Canvas.load = 0;
         }

         if (!this.isTour) {
            GlobalService.gI().getHandler(9);
            GlobalService.gI().requestCityMap((byte)0);
         } else {
            int var1 = 16 * AvMain.hd;
            LoadMap.idTileImg = -1;
            FilePack.b(T.aw);
            FrameImage var10 = FrameImage.init("ct", var1, var1);
            FilePack.reset();
            Vector var2 = new Vector();
            byte[] var3 = new byte[884];
            int var4 = 0;
            InputStream var5 = CRes.getResourceAsStream(T.getPath() + "/citiMap");

            try {
               for(int var6 = 0; var6 < 26; ++var6) {
                  for(int var7 = 0; var7 < 34; ++var7) {
                     var3[var6 * 34 + var7] = (byte)var5.read();
                     if (var3[var6 * 34 + var7] == 69) {
                        PositionMap var8;
                        (var8 = new PositionMap()).x = (byte)var7;
                        var8.y = (byte)var6;
                        var8.d = (short)(var4 + 819);
                        var8.c = T.nameRegion[var4];
                        var2.addElement(var8);
                        ++var4;
                     }
                  }
               }

               var5.close();
            } catch (IOException e) {
               e.printStackTrace();
            }

            LoadMap.TYPEMAP = -1;
            MiniMap.isCityMap = true;
            MiniMap.gI().setInfo(var10, var3, var2, (byte)34, 16 * AvMain.hd, new Command(T.selectt, new ISelectMiniMapAction(this)));
            MiniMap.gI().cmdUpdateKey = new IActionMiniMapKey(this);
            MiniMap.gI().selected = 3;
            MiniMap.gI().switchToMe();
            Canvas.endDlg();
            if (MiniMap.actionReg != null && MiniMap.iRequestReg == 0 && !Canvas.isInitChar) {
               MiniMap.actionReg.perform();
               MiniMap.iRequestReg = 1;
            }
         }
      }

   }

   protected final void doChangePass() {
      TField[] var1 = new TField[3];

      for(int var2 = 0; var2 < 3; ++var2) {
         var1[var2] = new TField();
         var1[var2].setIputType(2);
      }

      var1[0].setFocus(true);
      Command var3 = new Command(T.finish, new IActionChangePass(this, var1));
      InputFace.gI().setIputType(var1, T.changePass, T.gettingPrice, var3);
      Canvas.currentFace = InputFace.gI();
   }

   public static boolean setEnterPass(TField[] var0) {
      int var1 = -1;

      for(int var2 = 0; var2 < 3; ++var2) {
         if (var0[var2].getText().equals("")) {
            var1 = var2;
         }
      }

      if (!var0[1].getText().equals(var0[2].getText())) {
         var1 = 3;
      }

      if (var0[0].getText().equals(var0[1].getText())) {
         var1 = 4;
      }

      if (var1 != -1) {
         Canvas.startOKDlg(T.enterPass[var1]);
         return false;
      } else {
         return true;
      }
   }

   public static void onSelectedMiniMap(byte[] var0, byte var1, byte var2, byte var3, Image var4, short[] var5, Vector var6, Vector var7) {
      idImg = var5;
      Canvas.load = 0;
      roomID = var1;
      LoadMap.mapItemType = var6;
      LoadMap.mapItem = var7;
      ByteArrayInputStream var11 = new ByteArrayInputStream(var0);
      LoadMap.map = new short[var0.length];
      LoadMap.wMap = (short)var3;
      LoadMap.Hmap = (short)(var0.length / var3);
      LoadMap.imgBG = var4;
      if (var4 != null) {
         int[] var9 = new int[4];
         var4.getRGB(var9, 0, 2, 0, 0, 2, 2);
         LoadMap.s = var9[0];
      }

      try {
         for(int var10 = 0; var10 < LoadMap.map.length; ++var10) {
            LoadMap.map[var10] = (short)var11.read();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      if (var2 != LoadMap.idTileImg) {
         GlobalService.gI().requestTileMap(var2);
      } else {
         Canvas.loadMap.setMapAny();
      }

   }

   public final void doExitGame() {
      Canvas.startOKDlg(T.doYouWantExit2, new IActionExitGame(this));
   }

   public static void exitGame() {
      if (GameMidlet.avatar.seriPart != null) {
         GameMidlet.avatar.seriPart.removeAllElements();
      }

      LoadMap.rememMap = -1;
      LoadMap.imgMap = null;
      LoadMap.w = 24;
      Session_ME.gI().close();
      LoginScr.gI().switchToMe();
      LoginScr.gI().initImg();
      OnScreen.isOngame = false;
      OnScreen.c = 0;
      ListScr.friendL = null;
      LoadMap.playerLists.removeAllElements();
      GameMidlet.avatar = new Avatar();
      GameMidlet.myIndexP = new IndexPlayer();
      Canvas.listInfoSV.removeAllElements();
      GlobalMessageHandler.gI().miniGameMessageHandler = null;
   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 0:
            AvatarService.gI().doJoinHouse(GameMidlet.avatar.IDDB);
            Canvas.startWaitDlg();
            return;
         case 1:
            HouseScr.gI().onRoadFriend();
            return;
         case 2:
            GlobalService.gI().doCommunicate(var2);
            return;
         case 3:
            gI().doOpenShopOffline(GameMidlet.avatar, 0);
            return;
         case 4:
            gI().doOpenShopOffline(GameMidlet.avatar, 1);
         default:
      }
   }

   public static void g(int var0) {
      HouseScr.gI().typeHome = (byte)var0;
      if (GameMidlet.avatar.typeHome != var0 && GameMidlet.avatar.typeHome != -1) {
         HouseScr.gI().onRoadFriend();
      } else {
         Vector var1;
         (var1 = new Vector()).addElement(new Command(T.goHome, 0));
         var1.addElement(new Command(T.joinFrHome, 1));
         Menu.gI().startAt(var1, 2);
      }

   }

   public static void setAvatarShop(Avatar var0) {
      (avatarShop = new Avatar()).seriPart = new Vector();
      avatarShop.direct = 0;
      avatarShop.gender = var0.gender;
      avatarShop.lvMain = var0.lvMain;

      for(int var1 = 0; var1 < var0.seriPart.size(); ++var1) {
         SeriPart var2;
         (var2 = new SeriPart()).idPart = ((SeriPart)var0.seriPart.elementAt(var1)).idPart;
         avatarShop.addSeri(var2);
      }

   }

   private void doOpenShopOffline(Avatar var1, int var2) {
      setAvatarShop(var1);
      byte[] var3 = null;
      byte[] var5 = new byte[2];
      if (typeJoin == 3) {
         var5[0] = 3;
         var5[1] = 8;
      }

      System.out.println("typeJoin: " + typeJoin);
      byte[] var4;
      Vector[] var6;
      String[] var7;
      switch (typeJoin) {
         case 1:
         case 6:
            var3 = new byte[]{10, 20};
            (var6 = new Vector[2])[0] = new Vector();
            var6[1] = new Vector();
            (var7 = new String[2])[0] = T.pant;
            var7[1] = T.setMaxMoney;
            var5[0] = 1;
            var5[1] = 6;
            var4 = new byte[2];
            break;
         case 2:
         case 7:
            var3 = new byte[]{40, 50};
            (var6 = new Vector[2])[0] = new Vector();
            var6[1] = new Vector();
            (var7 = new String[2])[0] = T.eye;
            var7[1] = T.hair;
            var4 = new byte[2];
            var5[0] = 2;
            var5[1] = 7;
            break;
         case 3:
         case 4:
         case 5:
         default:
            (var6 = new Vector[1])[0] = new Vector();
            (var7 = new String[1])[0] = T.gift;
            var4 = new byte[1];
      }

      for(int var8 = 0; var8 < AvatarData.listPart.length; ++var8) {
         if (AvatarData.listPart[var8].follow != -2) {
            Part var9;
            int var10;
            if ((var9 = AvatarData.listPart[var8]).follow >= 0) {
               var10 = ((APartInfo)AvatarData.listPart[var9.follow]).gender;
            } else {
               var10 = ((APartInfo)var9).gender;
            }

            if (var9 != null && (var9.price[0] > 0 || var9.price[1] > 0) && (var1.gender == var10 || var10 == 0) && (var5[0] == var9.sell || var5[1] == var9.sell) && var9.follow > -2) {
               byte var11;
               if (var3 == null) {
                  var11 = var4[0];
                  var6[0].addElement(new CommandShopOffline(this, T.selectt, new IActionShopOffline(this, var9), var9, var11));
                  ++var4[0];
               } else {
                  for(var10 = 0; var10 < var6.length; ++var10) {
                     if (var3[var10] == var9.zOrder) {
                        var11 = var4[var10];
                        var6[var10].addElement(new CommandShopOffline1(this, T.selectt, new IActionShopOffline1(this, var9), var9, var11));
                        ++var4[var10];
                     }
                  }
               }
            }
         }
      }

      PopupShop.gI().switchToMe();
      PopupShop.isHorizontal = true;
      PopupShop.gI().addElement(var7, var6, (Vector)null);
      PopupShop.focusTap = var2;
      PopupShop.gI().setCmyLim();
      Canvas.endDlg();
      if (LoadMap.TYPEMAP == 57 && Canvas.isInitChar) {
         (Canvas.welcome = new Welcome()).initShop(PopupShop.me);
      }

   }

   public static void setAvatarShop(Part var0) {
      (avatarShop = new Avatar()).direct = 0;
      avatarShop.seriPart = new Vector();
      boolean var1 = false;

      for(int var2 = 0; var2 < GameMidlet.avatar.seriPart.size(); ++var2) {
         SeriPart var3;
         (var3 = new SeriPart()).idPart = ((SeriPart)GameMidlet.avatar.seriPart.elementAt(var2)).idPart;
         if (AvatarData.getPart(var3.idPart).zOrder == var0.zOrder) {
            var3.idPart = var0.IDPart;
            var1 = true;
         }

         avatarShop.addSeri(var3);
      }

      if (!var1) {
         SeriPart var5;
         (var5 = new SeriPart()).idPart = var0.IDPart;
         avatarShop.addSeri(var5);
         avatarShop.orderSeriesPath();
      }

   }

   public static void doBuyItem(int var0) {
      doSelectMoneyBuyItem(AvatarData.getPart((short)var0));
   }

   public static void doSelectMoneyBuyItem(Part var0) {
      Canvas.getTypeMoney(var0.price[0], var0.price[1], new IActionSelectedMoney(var0), new IActionSelectedMoney1(var0), (IAction)null);
   }

   public static void onBuyItem(short var0, String var1, int var2, int var3, int var4) {
      Canvas.startOKDlg(var1);
      GameMidlet.avatar.setMoney(var2);
      GameMidlet.avatar.setGold(var3);
      GameMidlet.avatar.luongKhoa = var4;
      Part var5;
      if ((var5 = AvatarData.getPart(var0)).follow != -2) {
         SeriPart var6;
         if ((var6 = AvatarData.getSeriByZ(var5.zOrder, GameMidlet.avatar.seriPart)) != null) {
            var6.idPart = var0;
         } else if (var5.zOrder == -1 && GameMidlet.avatar.idPet != -1) {
            GameMidlet.avatar.changePet(var0);
            AvatarService.gI().doRequestExpicePet(GameMidlet.avatar.IDDB);
         } else {
            GameMidlet.avatar.addSeri(new SeriPart(var0));
            GameMidlet.avatar.orderSeriesPath();
         }

         GameMidlet.avatar.setFeel(11);
         if (var5.zOrder == -1 && GameMidlet.avatar.idPet == -1) {
            GameMidlet.avatar.setPet();
            AvatarService.gI().doRequestExpicePet(GameMidlet.avatar.IDDB);
         }
      }

      GameMidlet.listContainer = null;
   }

   public static void doSetHandlerSuccess() {
      ParkService.gI().doJoinPark(roomID, -1);
      typeJoin = -1;
   }

   public final void commandAction() {
      this.commandActionPointer(3, -1);
   }

   public final void onJoinOfflineMap(byte var1, Vector var2, Vector var3, Vector var4) {
      byte[] var5 = new byte[]{59, 60, 58, 104, 105, 101, 102};
      LoadMap.mapItemType = var3;
      LoadMap.mapItem = var4;
      Canvas.loadMap.load(var5[var1]);
      if (var3 != null) {
         LoadMap.setMapItemType();
      }

      for(int var6 = 0; var6 < var2.size(); ++var6) {
         MyObject var7;
         if ((var7 = (MyObject)var2.elementAt(var6)).catagory == 0) {
            Avatar var8;
            (var8 = (Avatar)var7).xCur = var8.x;
            var8.yCur = var8.y;
            var8.dirLast = var8.direct;
            var8.orderSeriesPath();
            if (var8.IDDB != GameMidlet.avatar.IDDB) {
               setGender(var8);
               LoadMap.addPlayer(var8);
            }
         } else if (var7.catagory == 5) {
            Drop_Part var9;
            (var9 = (Drop_Part)var7).x0 = var9.x;
            var9.y0 = var9.y;
            LoadMap.playerLists.addElement(var9);
         }
      }

      if (Bus.isRun) {
         doMove(Bus.posBusStop.x, Bus.posBusStop.y, GameMidlet.avatar.direct, GameMidlet.avatar.direct_);
      } else {
         ++GameMidlet.avatar.y;
         this.move();
      }

      doSellectFeel(GameMidlet.avatar.feel);
      if (Canvas.isInitChar && var5[var1] == 101) {
         (Canvas.welcome = new Welcome()).initTash();
      }

   }

   public static void doJoinMapOffline(int var0) {
      idMapOffline = var0;
      idMapOld = LoadMap.TYPEMAP;
      gI().move();
      GlobalService.gI().getHandler(8);
      Canvas.startWaitDlg();
   }

   public final void onWeddingStart(int var1, int var2) {
      if (Canvas.currentMyScreen == PopupShop.me) {
         PopupShop.gI().close();
      }

      System.out.println("onWeddingStart 1111111111111");
      Canvas.load = 1;
      idUserWedding_1 = var1;
      idUserWedding_2 = var2;
      isWedding = true;
      this.iGoChaSu = 0;

      int var3;
      int var5;
      for(var3 = 0; var3 < listChair.size() - 1; ++var3) {
         AvPosition var4 = (AvPosition)listChair.elementAt(var3);

         for(var5 = var3 + 1; var5 < listChair.size(); ++var5) {
            AvPosition var6 = (AvPosition)listChair.elementAt(var5);
            if (var4.index > var6.index) {
               listChair.setElementAt(var6, var3);
               listChair.setElementAt(var4, var5);
               var4 = var6;
            }
         }
      }

      MyObject var10;
      for(var3 = 0; var3 < LoadMap.playerLists.size() - 1; ++var3) {
         if ((var10 = (MyObject)LoadMap.playerLists.elementAt(var3)).catagory == 0) {
            for(var5 = var3 + 1; var5 < LoadMap.playerLists.size(); ++var5) {
               MyObject var14;
               if ((var14 = (MyObject)LoadMap.playerLists.elementAt(var5)).catagory == 0 && ((Avatar)var10).IDDB > ((Avatar)var14).IDDB) {
                  LoadMap.playerLists.setElementAt(var14, var3);
                  LoadMap.playerLists.setElementAt(var10, var5);
                  var10 = var14;
               }
            }
         }
      }

      Avatar var13;
      for(var3 = 0; var3 < LoadMap.playerLists.size(); ++var3) {
         if ((var10 = (MyObject)LoadMap.playerLists.elementAt(var3)).catagory == 0) {
            (var13 = (Avatar)var10).moveList.removeAllElements();
            if (var13.IDDB == var2) {
               var13.x = var13.xCur = 0;
               var13.y = var13.yCur = 8 * LoadMap.w + LoadMap.w / 2 - LoadMap.w / 2;
               var13.v = 2;
               this.iGoChaSu = 1;
               var13.addPart(2475, 20);
               var13.addPart(2476, 10);
               var13.addPart(300, 60);
               var13.addPart(302, 70);
               var13.orderSeriesPath();
            } else if (var13.IDDB == var1) {
               var13.x = var13.xCur = 0;
               var13.y = var13.yCur = 8 * LoadMap.w + LoadMap.w / 2 + LoadMap.w / 2;
               var13.v = 2;
               this.iGoChaSu = 1;
               var13.addPart(2477, 20);
               var13.addPart(2478, 10);
               var13.orderSeriesPath();
            }
         }
      }

      var13 = LoadMap.getAvatar(var1);
      Avatar var12 = LoadMap.getAvatar(var2);
      LoadMap.playerLists.removeElement(var13);
      LoadMap.playerLists.removeElement(var12);
      var5 = 0;

      for(int var15 = 0; var15 < LoadMap.playerLists.size(); ++var15) {
         MyObject var7;
         Avatar var8;
         if ((var7 = (MyObject)LoadMap.playerLists.elementAt(var15)).catagory == 0 && (var8 = (Avatar)var7).IDDB != -100) {
            AvPosition var9;
            Canvas.px = Canvas.pxLast = (var9 = (AvPosition)listChair.elementAt(var5 / 2)).x - AvCamera.gI().xCam + LoadMap.w / 2;
            Canvas.py = Canvas.pyLast = var9.y - AvCamera.gI().yCam + LoadMap.w / 2 + var15 % 2 * (LoadMap.w - 5);
            ++var5;
            var8.setPos(Canvas.px + AvCamera.gI().xCam, Canvas.py + AvCamera.gI().yCam);
         }
      }

      LoadMap.playerLists.addElement(var13);
      LoadMap.playerLists.addElement(var12);
      LoadMap.orderVector(LoadMap.playerLists);
      Canvas.endDlg();
      System.out.println("onWeddingStart 2222222222222222222: " + isWedding + "     " + this.iGoChaSu);
   }
}
