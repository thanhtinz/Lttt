package avt;

import java.util.Hashtable;
import java.util.Vector;
import main.Canvas;
import main.GameMidlet;

public final class GlobalLogicHandler {
   public static boolean isNewVersion;

   public static void onServerMessage(String var0) {
      Canvas.startOKDlg(var0);
   }

   public static void onLoginSuccess() {
      AvatarMsgHandler.onHandler();
      boolean fishAutoHandled = ClientUtilities.onFishingAutoLoginSuccess();
      System.out.println("[FISH_AUTO_LOGIN] onLoginSuccess fishAutoHandled=" + fishAutoHandled + " playing=" + AvatarData.playing);
      if (fishAutoHandled) {
         AvatarService.gI().doRequestExpicePet(GameMidlet.avatar.IDDB);
         AvatarData.listImgIcon = new Hashtable();
         AvatarData.listImgPart = new Hashtable();
         return;
      }
      if (AvatarData.playing == -1) {
         AvatarService.gI().getBigData();
      } else {
         ClientUtilities.requestChangeZone();
      }

      AvatarService.gI().doRequestExpicePet(GameMidlet.avatar.IDDB);
      AvatarData.listImgIcon = new Hashtable();
      AvatarData.listImgPart = new Hashtable();
   }

   public final void onVersion(String var1, String var2) {
      IActionVersionDown var4 = new IActionVersionDown(this, var2);
      Vector var3;
      (var3 = new Vector()).addElement(new Command(T.OK, var4));
      var3.addElement(new Command(T.close, new IActionVersionDown2(this)));
      Canvas.msgdlg.setIsWaiting(false);
      Canvas.setInfoC(var1, var3);
      isNewVersion = true;
   }

   public final void onSetMoneyError(String var1, boolean var2) {
      if (var2) {
         Canvas.startOK(var1, new IActionDisconnect(this));
      } else {
         Canvas.startOKDlg(var1);
      }

   }

   public static void doGetHandler(byte var0) {
      if (GameMidlet.CLIENT_TYPE == 9) {
         isNewVersion = false;
      }

      System.out.println("doGetHandler: " + var0 + "    " + MapScr.typeJoin);
      if (GlobalMessageHandler.gI().miniGameMessageHandler != null) {
         switch (var0) {
            case 3:
               CasinoMsgHandler.gI();
               MapScr.gI();
               MapScr.onJoinCasino();
            case 4:
            case 5:
            case 6:
            case 7:
            default:
               break;
            case 8:
               if (ClientUtilities.isFishingReloginActive()) {
                  System.out.println("[FISH_AUTO_LOGIN] doGetHandler(8) detected relogin flow -> force join map 9");
                  ClientUtilities.forceFishingReloginJoinMap9FromHandler9();
                  return;
               }
               MapScr.gI().isTour = true;
               AvatarMsgHandler.onHandler();
               if (MapScr.idMapOffline != -1) {
                  GlobalService.gI().doJoinOfflineMap(MapScr.idMapOffline);
                  MapScr.idMapOffline = -1;
               } else if (MapScr.typeJoin != -1) {
                  Canvas.loadMap.load(57 + MapScr.typeJoin);
                  if (Canvas.isInitChar && LoadMap.TYPEMAP == 57) {
                     (Canvas.welcome = new Welcome()).initShop(MapScr.instance);
                  }

                  GameMidlet.avatar.setFeel(4);
                  Canvas.endDlg();
               } else {
                  ClientUtilities.requestChangeZone();
                  Canvas.endDlg();
               }
               break;
            case 9:
               ParkMsgHandler.onHandler();
               if (ClientUtilities.isFishingReloginActive()) {
                  System.out.println("[FISH_AUTO_LOGIN] doGetHandler(9) detected relogin flow -> force join map 9");
                  ClientUtilities.forceFishingReloginJoinMap9FromHandler9();
                  return;
               }
               if (LoadMap.xDichChuyen == -1) {
                  if (!OnScreen.isOngame) {
                     if (GameMidlet.CLIENT_TYPE == 12) {
                        LoadMap.w = 24;
                        LoadMap.rememMap = -1;
                        ParkService.gI().doJoinPark(MapScr.indexMap, -1);
                     } else if (GameMidlet.CLIENT_TYPE == 3) {
                        Canvas.paint.initResourceFour();
                        ParkService.gI().doJoinPark(MapScr.indexMap, -1);
                     } else if (MapScr.typeJoin != -1) {
                        MapScr.gI();
                        MapScr.doSetHandlerSuccess();
                     } else if (MapScr.idMapOld != -1) {
                        Canvas.startWaitDlg();
                        ParkService.gI().doJoinPark(MapScr.idMapOld, -1);
                        MapScr.idMapOld = -1;
                     } else {
                        MapScr.gI().doJoin();
                     }
                  } else {
                     Canvas.paint.initResourceFour();
                     OnScreen.gI().switchToMe();
                     Canvas.endDlg();
                  }
               } else {
                  LoadMap.idTileImg = -1;
               }
               break;
            case 10:
               if (FarmMsgHandler.instance == null) {
                  FarmMsgHandler.instance = new FarmMsgHandler();
               }

               GlobalMessageHandler.gI().miniGameMessageHandler = FarmMsgHandler.instance;
               if (FarmData.playing == -1) {
                  FarmService var2;
                  (var2 = FarmService.gI()).createMessage((byte)51);
                  var2.writeUTF(AvatarData.l);
                  var2.sendMessage();
               } else if (FarmScr.itemProduct == null) {
                  FarmService.gI().getInventory();
               } else {
                  ParkService.gI().doJoinPark(25, 0);
                  FarmScr.initImg();
                  FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, false);
               }
               break;
            case 11:
               HomeMsgHandler.onHandler();
               LoadMap.TYPEMAP = -1;
               ParkService.gI().doJoinPark(21, 0);
               if (MapScr.idHouse != -1) {
                  Canvas.startWaitDlg();
                  AvatarService.gI().getTypeHouse(0);
               }
               break;
            case 12:
               if (RaceMsgHandler.instance == null) {
                  RaceMsgHandler.instance = new RaceMsgHandler();
               }

               GlobalMessageHandler.gI().miniGameMessageHandler = RaceMsgHandler.instance;
               GlobalService var1 = GlobalService.gI();
               Canvas.startWaitDlg();
               var1.createMessage((byte)1);
               var1.sendMessage();
         }
      }

      GameMidlet.CLIENT_TYPE = var0;
   }

   public final void onMenuOption(int var1, byte var2, String[] var3, String var4, String var5, boolean[] var6) {
      if (Canvas.menuMain != null) {
         Canvas.menuMain = null;
      }

      Canvas.endDlg();
      Vector var7 = new Vector();

      for(int var8 = 0; var8 < var3.length; ++var8) {
         var7.addElement(new Command(var3[var8], new IActionMenuOption(this, var8, var1, var2)));
      }

      if (var4 != null) {
         MenuNPC.gI().setInfo(var7, var1, var4, var5, var6);
      } else {
         Menu.gI().startAt(var7, 0);
      }

   }

   public final void onUpdateCHest(byte var1, byte var2, String var3) {
      if (var2 == 0) {
         Canvas.startOKDlg(var3, new IActionChest(this, var1));
      } else {
         Canvas.startOKDlg(var3);
      }

   }
}
