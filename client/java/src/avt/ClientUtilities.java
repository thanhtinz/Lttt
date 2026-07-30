package avt;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextField;
import main.Canvas;
import main.GameMidlet;

public final class ClientUtilities {
   private static final String RMS_UTIL_SPEED = "utilSpeed258";
   private static final String RMS_AUTO_CLICK = "aclick258";
   private static final String RMS_AUTO_GIFT = "agift258";
   private static final String RMS_AUTO_TROLL = "atroll258";
   private static final String RMS_AUTO_STONE = "astone258";
   private static final String RMS_EVENT = "event258";
   private static final String RMS_NPC = "npc258";
   private static final String RMS_FISHING = "fish258";
   private static final int STONE_SHOP_NPC_ID = 2000000011;
   private static final byte STONE_SHOP_MENU_BYTE = -4;
   private static final String TOOL_PICKAXE_XU = "cuốc thần kỳ xu";
   private static final String TOOL_PICKAXE_GOLD = "cuốc thần kỳ lượng";
   private static final String TOOL_SHOVEL_XU = "cuốc thường xu";
   private static final String TOOL_SHOVEL_GOLD = "cuốc thường lượng";

   public static boolean autoChat;
   public static boolean autoKiss;
   public static boolean autoHit;
   public static boolean autoGift;
   public static boolean autoTroll;
   public static boolean autoClickEnabled;
   public static int autoClickKeyCode = -5;
   public static int autoClickStartKey = 35;
   public static int autoClickIntervalMs = 100;
   public static boolean autoClickShowToggleNotice = true;
   public static boolean vietnameseTyping = true;
   public static int utilSpeed1to100 = 50;
   public static int autoChatIntervalSeconds = 3;
   public static String autoChatMessage = ":)";
   public static int autoGiftGiftId = 102;
   public static int autoGiftLimit = 0;
   public static int autoGiftCount = 0;
   public static int autoGiftIntervalMs = 100;
   public static int autoGiftPayBy = 1;
   public static int autoTrollCode = -1;
   public static int autoTrollLimit = 0;
   public static int autoTrollCount = 0;
   public static int autoTrollIntervalMs = 1000;
   public static boolean autoStoneEnabled;
   public static int autoStoneIntervalMs = 1000;
   public static int autoStoneStartKey = 42;
   public static boolean autoStoneRecoverHealth = true;
   public static boolean autoStonePickaxeXu = true;
   public static boolean autoStonePickaxeGold;
   public static boolean autoStoneShovelXu;
   public static boolean autoStoneShovelGold;
   public static boolean[] autoStoneSellStoneSelected = new boolean[]{false, false, false, false, false, false, false, false, false};
   public static int autoStonePickaxeXuBuyLimit = 0;
   public static int autoStonePickaxeGoldBuyLimit = 0;
   public static int autoStoneShovelXuBuyLimit = 0;
   public static int autoStoneShovelGoldBuyLimit = 0;
   public static int autoStonePickaxeXuBought;
   public static int autoStonePickaxeGoldBought;
   public static int autoStoneShovelXuBought;
   public static int autoStoneShovelGoldBought;
   private static int autoTickCounter;
   private static int autoChatPartIndex;
   private static long autoChatNextSendAtMs;
   private static long autoStoneNextAtMs;
   private static boolean autoClickInjectAwaitRelease;
   private static long autoClickNextAtMs;
   private static long autoTrollNextAtMs;

   static boolean fishingAutoLogin;
   static boolean fishingAutoBuyBait;
   static boolean fishingAutoBuyTicket;
   static boolean fishingAutoBuyRod;
   static boolean fishingAutoSellStone;
   static int fishingSelectedTicket; // 0 = vé thường (448), 1 = vé VIP (449)
   static int fishingSelectedRod; // 0 = cần thường, 1 = cần VIP
   static boolean fishingRedFieldEnabled;
   static int fishingCounter;
   static int fishingSoldCounter;
   static boolean[] fishingSellFishSelected = new boolean[]{true, true, true, true, true, true, true, true, true, true};
   static Hashtable fishingCaughtById = new Hashtable();
   static Hashtable fishingSoldById = new Hashtable();
   static Vector fishingFishIdOrder = new Vector();
   private static Thread fishingAutoLoginThread;
   private static byte fishingReloginStage; // 0 none, 1 waiting login success, 2 waiting join map 9, 3 waiting return map, 4 waiting return move
   private static byte fishingReloginSavedMap = -1;
   private static byte fishingReloginSavedBoard = -1;
   private static int fishingReloginSavedX = -1;
   private static int fishingReloginSavedY = -1;
   private static int fishingReloginSavedDir;
   private static int fishingReloginSavedDir2;
   private static long fishingReloginNextAtMs;
   private static boolean fishingReloginLoginIssued;
   private static long fishingAutoBaitNextAtMs;
   private static boolean fishingAutoBaitPendingBuy;
   private static int fishingAutoBaitBuyStep;
   private static int fishingAutoBaitMoveDownRemain;
   private static int fishingAutoBaitEnterRemain;
   private static byte fishingAutoBaitReturnMap = -1;
   private static boolean fishingAutoBaitRequested;
   private static int fishingAutoBaitReturnX = -1;
   private static int fishingAutoBaitReturnY = -1;
   private static boolean fishingAutoBaitNeedReturnMove;
   private static boolean fishingAutoBaitNeedResumeSit;
   private static int fishingAutoBaitSitX = -1;
   private static int fishingAutoBaitSitY = -1;
   private static int fishingAutoBaitMoveTargetX = -1;
   private static int fishingAutoBaitMoveTargetY = -1;
   private static int fishingAutoBaitForceExitTries;
   private static int fishingAutoBaitDirectBuyRemain;
   private static boolean fishingAutoBuyTicketRequested;
   private static boolean fishingAutoBuyRodRequested;
   private static int fishingAutoBuyTicketStep;
   private static int fishingAutoBuyRodStep;
   private static boolean fishingAutoBuyPending;
   private static boolean fishingAutoBuyBaitPending;


   private static String titleCaseWords(String s) {
      if (s == null) return "";
      s = s.trim();
      if (s.length() == 0) return "";
      char[] a = s.toLowerCase().toCharArray();
      boolean up = true;
      for (int i = 0; i < a.length; i++) {
         char c = a[i];
         if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
            up = true;
            continue;
         }
         if (up) {
            a[i] = Character.toUpperCase(c);
            up = false;
         }
      }
      return new String(a);
   }

   private static String toolDisplayName(String toolKey) {
      if (toolKey == null) {
         return "";
      }
      if (TOOL_PICKAXE_XU.equals(toolKey)) {
         return titleCaseWords(T.utilToolPickaxeXu);
      }
      if (TOOL_PICKAXE_GOLD.equals(toolKey)) {
         return titleCaseWords(T.utilToolPickaxeGold);
      }
      if (TOOL_SHOVEL_XU.equals(toolKey)) {
         return titleCaseWords(T.utilToolShovelXu);
      }
      if (TOOL_SHOVEL_GOLD.equals(toolKey)) {
         return titleCaseWords(T.utilToolShovelGold);
      }
      return titleCaseWords(toolKey);
   }

   private static String autoStoneToolStatusLine() {
      String tool = null;
      int bought = 0;
      int lim = 0;

      if (autoStonePickaxeXu && (autoStonePickaxeXuBuyLimit <= 0 || autoStonePickaxeXuBought < autoStonePickaxeXuBuyLimit)) {
         tool = TOOL_PICKAXE_XU;
         bought = autoStonePickaxeXuBought;
         lim = autoStonePickaxeXuBuyLimit;
      } else if (autoStonePickaxeGold && (autoStonePickaxeGoldBuyLimit <= 0 || autoStonePickaxeGoldBought < autoStonePickaxeGoldBuyLimit)) {
         tool = TOOL_PICKAXE_GOLD;
         bought = autoStonePickaxeGoldBought;
         lim = autoStonePickaxeGoldBuyLimit;
      } else if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) {
         tool = TOOL_SHOVEL_XU;
         bought = autoStoneShovelXuBought;
         lim = autoStoneShovelXuBuyLimit;
      } else if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) {
         tool = TOOL_SHOVEL_GOLD;
         bought = autoStoneShovelGoldBought;
         lim = autoStoneShovelGoldBuyLimit;
      }

      if (tool == null) {
         return T.utilPickaxeNone;
      }
      String limText = lim <= 0 ? T.utilUnlimited : String.valueOf(lim);
      return T.utilPickaxePrefix + toolDisplayName(tool) + T.utilBoughtPrefix + bought + T.utilLimitSuffix + limText;
   }

   private static String autoStoneToolNameForOverlay() {
      if (autoStonePickaxeXu && (autoStonePickaxeXuBuyLimit <= 0 || autoStonePickaxeXuBought < autoStonePickaxeXuBuyLimit)) return TOOL_PICKAXE_XU;
      if (autoStonePickaxeGold && (autoStonePickaxeGoldBuyLimit <= 0 || autoStonePickaxeGoldBought < autoStonePickaxeGoldBuyLimit)) return TOOL_PICKAXE_GOLD;
      if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) return TOOL_SHOVEL_XU;
      if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) return TOOL_SHOVEL_GOLD;
      return null;
   }

   private static int autoStoneBoughtForOverlay() {
      if (autoStonePickaxeXu && (autoStonePickaxeXuBuyLimit <= 0 || autoStonePickaxeXuBought < autoStonePickaxeXuBuyLimit)) return autoStonePickaxeXuBought;
      if (autoStonePickaxeGold && (autoStonePickaxeGoldBuyLimit <= 0 || autoStonePickaxeGoldBought < autoStonePickaxeGoldBuyLimit)) return autoStonePickaxeGoldBought;
      if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) return autoStoneShovelXuBought;
      if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) return autoStoneShovelGoldBought;
      return 0;
   }

   private static int autoStoneLimitForOverlay() {
      if (autoStonePickaxeXu && (autoStonePickaxeXuBuyLimit <= 0 || autoStonePickaxeXuBought < autoStonePickaxeXuBuyLimit)) return autoStonePickaxeXuBuyLimit;
      if (autoStonePickaxeGold && (autoStonePickaxeGoldBuyLimit <= 0 || autoStonePickaxeGoldBought < autoStonePickaxeGoldBuyLimit)) return autoStonePickaxeGoldBuyLimit;
      if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) return autoStoneShovelXuBuyLimit;
      if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) return autoStoneShovelGoldBuyLimit;
      return 0;
   }

   private static String autoStoneNextToolNameForOverlay() {
      String cur = autoStoneToolNameForOverlay();
      if (cur == null) return null;
      int curLim = autoStoneLimitForOverlay();
      if (curLim <= 0) return "";

      boolean after = false;
      if (TOOL_PICKAXE_XU.equals(cur)) {
         after = true;
      }
      if (after) {
         if (autoStonePickaxeGold && (autoStonePickaxeGoldBuyLimit <= 0 || autoStonePickaxeGoldBought < autoStonePickaxeGoldBuyLimit)) return TOOL_PICKAXE_GOLD;
         if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) return TOOL_SHOVEL_XU;
         if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) return TOOL_SHOVEL_GOLD;
         return null;
      }
      if (TOOL_PICKAXE_GOLD.equals(cur)) {
         if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) return TOOL_SHOVEL_XU;
         if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) return TOOL_SHOVEL_GOLD;
         return null;
      }
      if (TOOL_SHOVEL_XU.equals(cur)) {
         if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) return TOOL_SHOVEL_GOLD;
         return null;
      }
      return null;
   }

   public static String getAutoStoneOverlayLine1() {
      if (!autoStoneEnabled) return null;
      String tool = autoStoneToolNameForOverlay();
      if (tool == null) return T.utilMiningNone;
      return T.utilMiningPrefix + toolDisplayName(tool);
   }

   public static String getAutoStoneOverlayLine2() {
      if (!autoStoneEnabled) return null;
      String tool = autoStoneToolNameForOverlay();
      if (tool == null) return T.utilLimitPrefix + "0/0";
      int bought = autoStoneBoughtForOverlay();
      int lim = autoStoneLimitForOverlay();
      String limText = lim <= 0 ? T.utilUnlimited : String.valueOf(lim);
      return T.utilLimitPrefix + bought + "/" + limText;
   }

   public static String getAutoStoneOverlayLine3() {
      if (!autoStoneEnabled) return null;
      String tool = autoStoneToolNameForOverlay();
      if (tool == null) return T.utilNextBuyNone;
      String next = autoStoneNextToolNameForOverlay();
      if (next != null && next.length() == 0) {
         return T.utilNextBuyNoSwitch;
      }
      if (next == null) {
         return T.utilNextBuyNone;
      }
      return T.utilNextBuyPrefix + toolDisplayName(next);
   }

   
   static boolean eventCheckinPending;
   static byte eventCheckinTargetMap = 0;
   static String eventCheckinChat = "avatar";
   static int eventChangeZoneIntervalMs = 2700;
   static boolean eventScopeAllMaps = true;

   
   static boolean autoNpcEnabled;
   static boolean autoNpcAttackEnabled;
   static String npcSearchKeyword = "";
   static boolean npcSearchEnabled = true;
   static int npcAttackDelayMs = 200;
   static boolean npcAttackOnlyIfFindOn = true;
   static byte npcAttackType = 0;
   static Vector rememberedNpcList = new Vector(); 
   private static boolean rememberedNpcListFromMapMenu;
   private static long autoNpcNextHopAtMs;
   private static int autoNpcMapRotateIdx;
   private static final byte[] AUTO_NPC_MAP_ROTATION = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 11};
   private static int autoNpcLastFoundId = -1;
   private static boolean autoNpcFoundNotified;
   private static long autoNpcNextMoveToFoundAtMs;
   private static long autoNpcNextAttackAtMs;
   private static int autoNpcLastAttackNpcId = -1;
   private static boolean map13GuideShown;
   private static int lastObservedMapType = -1;
   private static long autoTrollDebugNextLogAtMs;

   private static byte autoStonePendingBuyType = -1;
   private static int autoStonePendingMoneyXu = -1;
   private static int autoStonePendingMoneyGold = -1;
   private static long autoStonePendingExpiresAtMs;

   public static String boolCap(boolean on) {
      return on ? T.utilStateOn : T.utilStateOff;
   }

   public static Vector buildUtilityCommandList() {
      Vector v = new Vector();
      v.addElement(new Command(T.utilChangeZone, new IActionUtilityCmd((byte)0)));
      v.addElement(new Command(T.utilChangeMap, new IActionUtilityCmd((byte)1)));
      v.addElement(new Command(T.utilAutoChat, new IActionUtilityCmd((byte)2)));
      v.addElement(new Command(T.utilAutoKiss, new IActionUtilityCmd((byte)3)));
      v.addElement(new Command(T.utilAutoHit + ": " + boolCap(autoHit), new IActionUtilityCmd((byte)4)));
      v.addElement(new Command(T.utilAutoGift, new IActionUtilityCmd((byte)5)));
      v.addElement(new Command(T.utilAutoTroll + ": " + boolCap(autoTroll), new IActionUtilityCmd((byte)6)));
      v.addElement(new Command(T.utilAutoClick, new IActionUtilityCmd((byte)7)));
      v.addElement(new Command(T.utilSpeed, new IActionUtilityCmd((byte)8)));
      v.addElement(new Command(T.utilGameRoom, new IActionUtilityCmd((byte)9)));
      v.addElement(new Command(T.loginSwitchCharacter, new IActionUtilityCmd((byte)13)));
      v.addElement(new Command(T.utilVietnameseIME + ": " + boolCap(vietnameseTyping), new IActionUtilityCmd((byte)10)));
      v.addElement(new Command(T.menuAutoMining, new IActionUtilityCmd((byte)11)));
     
      return v;
   }

   public static void stopAutoDialLuckyFromUtility() {
      DialLuckyScr.gI().stopAutoDialFromUtility();
      Canvas.startOKDlg(T.utilAutoDialStopped);
   }

   public static void openUtilitySubmenu() {
      Menu.gI().startAt(buildUtilityCommandList(), -1);
      Menu.h = null;
   }

   public static void openEventSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.eventNpcSettings, new IActionEventCmd((byte)0)));
      v.addElement(new Command(T.option, new IActionEventCmd((byte)1)));
      v.addElement(new Command(T.eventGiftCounter, new IActionEventCmd((byte)2)));
      v.addElement(new Command(T.eventGoFarm, new IActionEventCmd((byte)3)));
      v.addElement(new Command(T.eventCheckin, new IActionEventCmd((byte)4)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void onAfterJoinPark(byte roomId) {
      if (eventCheckinPending && roomId == eventCheckinTargetMap) {
         eventCheckinPending = false;
         if (eventCheckinChat != null && !eventCheckinChat.trim().equals("")) {
            ParkService.gI().chatToBoard(eventCheckinChat);
         }
      }

     
      try {
         long now = System.currentTimeMillis();
         if (fishingReloginStage == 2 && roomId == 9) {
            fishingReloginStage = 3;
            fishingReloginNextAtMs = now + 3000L;
            final byte backMap = fishingReloginSavedMap;
            final byte backBoard = fishingReloginSavedBoard;
            new Thread(new Runnable() {
               public void run() {
                  try { Thread.sleep(3000L); } catch (Throwable t) {}
                  try {
                     if (backMap >= 0) {
                        ParkService.gI().doJoinPark(backMap, backBoard);
                     }
                  } catch (Throwable t) {}
               }
            }).start();
            return;
         }
         if (fishingReloginStage == 3 && roomId == fishingReloginSavedMap) {
            fishingReloginStage = 4;
            final int x = fishingReloginSavedX;
            final int y = fishingReloginSavedY;
            final int savedDir = fishingReloginSavedDir;
            final int savedDir2 = fishingReloginSavedDir2;
            new Thread(new Runnable() {
               public void run() {
                  try { Thread.sleep(3000L); } catch (Throwable t) {}
                  int retry = 0;
                  while (retry < 6) {
                     try {
                        if (GameMidlet.avatar != null && x >= 0 && y >= 0 && LoadMap.TYPEMAP == fishingReloginSavedMap) {
                           GameMidlet.avatar.direct = (byte)savedDir;
                           GameMidlet.avatar.direct_ = (short)savedDir2;
                           int dir = savedDir;
                           int dir2 = savedDir2;
                           System.out.println("[FISH_AUTO_LOGIN] return move try=" + retry + " to x=" + x + " y=" + y + " map=" + LoadMap.TYPEMAP);
                           // Dùng cùng pattern như auto-bait: set focus local rồi gửi move packet.
                           GameMidlet.avatar.posFocus = new AvPosition(x, y);
                           GameMidlet.avatar.moveToFocus();
                           GameMidlet.avatar.xCur = x;
                           GameMidlet.avatar.yCur = y;
                           ParkService.gI().doMove(x, y, dir, dir2);
                           // Gửi thêm 1 lần qua MapScr để đồng bộ đường đi local + packet dự phòng.
                           MapScr.doMove(x, y, dir, dir2);
                        }
                     } catch (Throwable t) {
                        System.out.println("[FISH_AUTO_LOGIN] return move error: " + t.toString());
                     }
                     retry++;
                     try { Thread.sleep(700L); } catch (Throwable t) {}
                  }
                  fishingReloginStage = 0;
               }
            }).start();
         }
      } catch (Throwable t) {
      }
   }

   static void openEventNpcSettingsForm() {
      openNpcSettingsSubmenu();
   }

   static void openNpcSettingsSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.npcAutoPrefix + boolCap(autoNpcEnabled), new IActionNpcCmd((byte)0)));
      v.addElement(new Command(T.npcAttackPrefix + boolCap(autoNpcAttackEnabled), new IActionNpcCmd((byte)1)));
      v.addElement(new Command(T.npcSearchSettings, new IActionNpcCmd((byte)3)));
      v.addElement(new Command(T.npcAttackSettings, new IActionNpcCmd((byte)4)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   static boolean hasRememberedNpc() {
      return rememberedNpcList != null && rememberedNpcList.size() > 0;
   }

   static void openRememberedNpcListMenu() {
      rememberedNpcListFromMapMenu = false;
      openRememberedNpcListMenuInternal();
   }

   static void openRememberedNpcListMenuFromMap() {
      rememberedNpcListFromMapMenu = true;
      openRememberedNpcListMenuInternal();
   }

   static void reopenRememberedNpcListMenu() {
      openRememberedNpcListMenuInternal();
   }

   private static void openRememberedNpcListMenuInternal() {
      Vector v = new Vector();
      if (rememberedNpcList == null || rememberedNpcList.size() == 0) {
         Canvas.startOKDlg(T.npcListEmpty);
         if (!rememberedNpcListFromMapMenu) {
            openNpcSettingsSubmenu();
         }
         return;
      }

      for (int i = 0; i < rememberedNpcList.size(); i++) {
         String entry = (String) rememberedNpcList.elementAt(i);
         int p = entry.indexOf('|');
         String id = p < 0 ? entry : entry.substring(0, p);
         String name = p < 0 ? "" : safe(entry.substring(p + 1));
         String caption = safe(name).trim().length() > 0 ? safe(name) : T.npcDefaultName;
         v.addElement(new Command(caption, new IActionRememberedNpcCommunicate(id)));
      }

      v.addElement(new Command(T.npcClearList, new IActionNpcCmd((byte)11)));
      if (rememberedNpcListFromMapMenu) {
         v.addElement(new Command(T.loginBack, new IActionNoop()));
      } else {
         v.addElement(new Command(T.loginBack, new IActionNpcCmd((byte)7)));
      }
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   static void clearRememberedNpcList() {
      if (rememberedNpcList != null) {
         rememberedNpcList.removeAllElements();
      }

      persistNpcSettings();
      Canvas.startOKDlg(T.npcListCleared);
      if (!rememberedNpcListFromMapMenu) {
         openNpcSettingsSubmenu();
      }
   }

   static void rememberFocusedNpcFromMap() {
      rememberFocusedNpc();
   }

   static void openNpcSearchSettingsForm() {
      Vector v = new Vector();
      String key = safe(npcSearchKeyword).trim();
      if (key.length() == 0) {
         key = T.npcNameKey;
      }
      v.addElement(new Command(key + ": " + (npcSearchEnabled ? "ON" : "OFF"), new IActionNpcCmd((byte)6)));
      v.addElement(new Command(T.loginBack, new IActionNpcCmd((byte)7)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   static void openNpcAttackSettingsForm() {
      Vector v = new Vector();
      v.addElement(new Command(T.npcAttackPrefix + (autoNpcAttackEnabled ? "ON" : "OFF"), new IActionNpcCmd((byte)8)));
      v.addElement(new Command(T.loginBack, new IActionNpcCmd((byte)9)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   static void rememberFocusedNpc() {
      if (LoadMap.focusObj == null || LoadMap.focusObj.catagory != 0) {
         Canvas.startOKDlg(T.npcNotPointed);
         return;
      }

      Avatar a = (Avatar) LoadMap.focusObj;
      if (a == null) {
         Canvas.startOKDlg(T.npcNotPointed);
         return;
      }

      if (!isEncodedNpcId(a.IDDB)) {
         Canvas.startOKDlg(T.npcNotPointed);
         return;
      }

      String entry = String.valueOf(a.IDDB) + "|" + safe(a.name);
      for (int i = 0; i < rememberedNpcList.size(); i++) {
         if (entry.equals(rememberedNpcList.elementAt(i))) {
            Canvas.startOKDlg(T.npcAlreadySaved);
            return;
         }
      }

      rememberedNpcList.addElement(entry);
      persistNpcSettings();
      Canvas.startOKDlg(T.npcSavedPrefix + safe(a.name));
   }

   private static boolean matchesConfiguredNpcName(String name) {
      if (!npcSearchEnabled) {
         return false;
      }

      String key = safe(npcSearchKeyword).trim();
      if (key.length() == 0) {
         return false;
      }

      String n = safe(name).toLowerCase();
      String nn = normalizeForNpcMatch(n);
      String[] keys = splitKeywordList(key);

      for (int i = 0; i < keys.length; i++) {
         String k = keys[i];
         if (k.length() == 0) continue;
         String kl = k.toLowerCase();
         if (n.indexOf(kl) >= 0) {
            return true;
         }

         String kk = normalizeForNpcMatch(kl);
         if (kk.length() > 0 && nn.indexOf(kk) >= 0) {
            return true;
         }
      }

      return false;
   }

   private static String[] splitKeywordList(String s) {
      Vector v = new Vector();
      String cur = "";

      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if (c == ',' || c == ';' || c == '|' || c == '\n' || c == '\r') {
            String t = cur.trim();
            if (t.length() > 0) {
               v.addElement(t);
            }
            cur = "";
         } else {
            cur += c;
         }
      }

      String tail = cur.trim();
      if (tail.length() > 0) {
         v.addElement(tail);
      }

      if (v.size() == 0) {
         return new String[]{s.trim()};
      }

      String[] out = new String[v.size()];
      for (int i = 0; i < out.length; i++) {
         out[i] = (String)v.elementAt(i);
      }
      return out;
   }

   private static String normalizeForNpcMatch(String s) {
      if (s == null || s.length() == 0) {
         return "";
      }

      StringBuffer out = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
         char c = foldVietnameseChar(Character.toLowerCase(s.charAt(i)));
         if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
            out.append(c);
         }
      }
      return out.toString();
   }

   private static char foldVietnameseChar(char c) {
      switch (c) {
         case 'à': case 'á': case 'ạ': case 'ả': case 'ã':
         case 'â': case 'ầ': case 'ấ': case 'ậ': case 'ẩ': case 'ẫ':
         case 'ă': case 'ằ': case 'ắ': case 'ặ': case 'ẳ': case 'ẵ':
            return 'a';
         case 'è': case 'é': case 'ẹ': case 'ẻ': case 'ẽ':
         case 'ê': case 'ề': case 'ế': case 'ệ': case 'ể': case 'ễ':
            return 'e';
         case 'ì': case 'í': case 'ị': case 'ỉ': case 'ĩ':
            return 'i';
         case 'ò': case 'ó': case 'ọ': case 'ỏ': case 'õ':
         case 'ô': case 'ồ': case 'ố': case 'ộ': case 'ổ': case 'ỗ':
         case 'ơ': case 'ờ': case 'ớ': case 'ợ': case 'ở': case 'ỡ':
            return 'o';
         case 'ù': case 'ú': case 'ụ': case 'ủ': case 'ũ':
         case 'ư': case 'ừ': case 'ứ': case 'ự': case 'ử': case 'ữ':
            return 'u';
         case 'ỳ': case 'ý': case 'ỵ': case 'ỷ': case 'ỹ':
            return 'y';
         case 'đ':
            return 'd';
         default:
            return c;
      }
   }

   private static boolean matchesConfiguredNpc(Avatar a) {
      if (a == null) {
         return false;
      }
     
      return matchesConfiguredNpcName(a.name);
   }

   private static boolean isEncodedNpcId(int id) {
      return id >= 2000000000;
   }

   static void persistNpcSettings() {
      try {
         StringBuilder sb = new StringBuilder();
         sb.append(autoNpcEnabled ? "1" : "0").append(';');
         sb.append(autoNpcAttackEnabled ? "1" : "0").append(';');
         sb.append(safe(npcSearchKeyword)).append(';');
         sb.append(npcAttackDelayMs).append(';');
         sb.append(npcAttackOnlyIfFindOn ? "1" : "0").append(';');
         sb.append(npcAttackType).append(';');
         sb.append(npcSearchEnabled ? "1" : "0");
         for (int i = 0; i < rememberedNpcList.size(); i++) {
            sb.append('\n').append((String) rememberedNpcList.elementAt(i));
         }
         CRes.saveString(RMS_NPC, sb.toString());
      } catch (Exception e) {
      }
   }

   static void loadPersistedNpcSettings() {
      try {
         String s = CRes.loadString(RMS_NPC);
         if (s == null || s.length() == 0) {
            return;
         }

         int nl = s.indexOf('\n');
         String header = nl < 0 ? s : s.substring(0, nl);
         String body = nl < 0 ? "" : s.substring(nl + 1);

         int p1 = header.indexOf(';');
         int p2 = p1 < 0 ? -1 : header.indexOf(';', p1 + 1);
         int p3 = p2 < 0 ? -1 : header.indexOf(';', p2 + 1);
         int p4 = p3 < 0 ? -1 : header.indexOf(';', p3 + 1);
         int p5 = p4 < 0 ? -1 : header.indexOf(';', p4 + 1);
         int p6 = p5 < 0 ? -1 : header.indexOf(';', p5 + 1);
         if (p1 >= 0 && p2 > p1 && p3 > p2 && p4 > p3 && p5 > p4) {
            autoNpcEnabled = "1".equals(header.substring(0, p1));
            autoNpcAttackEnabled = "1".equals(header.substring(p1 + 1, p2));
            npcSearchKeyword = header.substring(p2 + 1, p3);
            npcAttackDelayMs = parseIntSafe(header.substring(p3 + 1, p4).trim(), npcAttackDelayMs);
            npcAttackOnlyIfFindOn = "1".equals(header.substring(p4 + 1, p5));
            if (p6 > p5) {
               npcAttackType = (byte)parseIntSafe(header.substring(p5 + 1, p6).trim(), npcAttackType);
               npcSearchEnabled = "1".equals(header.substring(p6 + 1).trim());
            } else {
               npcAttackType = (byte)parseIntSafe(header.substring(p5 + 1).trim(), npcAttackType);
               npcSearchEnabled = true;
            }
         }

         rememberedNpcList.removeAllElements();
         if (body.length() > 0) {
            String[] lines = splitLines(body);
            if (lines != null) {
               for (int i = 0; i < lines.length; i++) {
                  if (lines[i] != null && lines[i].indexOf('|') > 0) {
                     rememberedNpcList.addElement(lines[i]);
                  }
               }
            }
         }
      } catch (Exception e) {
      }
   }

   static void openEventSettingsForm() {
      final Form form = new Form(T.option);

      final TextField tfNpcList = new TextField(T.utilNpcKeyword, safe(npcSearchKeyword), 200, TextField.ANY);
      final TextField tfCheckin = new TextField(T.utilCheckinContent, eventCheckinChat == null ? "" : eventCheckinChat, 200, TextField.ANY);
      final TextField tfZoneMs = new TextField(T.utilZoneInterval, String.valueOf(eventChangeZoneIntervalMs), 8, TextField.NUMERIC);

      final ChoiceGroup cgScope = new ChoiceGroup(T.utilScope, ChoiceGroup.EXCLUSIVE);
      cgScope.append(T.utilScopeAllMaps, null);
      cgScope.append(T.utilScopeCurrentMap, null);
      cgScope.setSelectedIndex(eventScopeAllMaps ? 0 : 1, true);

      final ChoiceGroup cgAttack = new ChoiceGroup(T.utilAttack, ChoiceGroup.EXCLUSIVE);
      cgAttack.append(T.utilAttackNone, null);
      cgAttack.append(T.utilAttackYes, null);
      cgAttack.setSelectedIndex(autoNpcAttackEnabled ? 1 : 0, true);

      final ChoiceGroup cgAttackType = new ChoiceGroup(T.utilAttackType, ChoiceGroup.EXCLUSIVE);
      cgAttackType.append(T.utilAttackContinuous, null);
      cgAttackType.append(T.utilAttackOnce, null);
      cgAttackType.setSelectedIndex(npcAttackType == 1 ? 1 : 0, true);

      final TextField tfAttackMs = new TextField(T.utilAttackInterval, String.valueOf(npcAttackDelayMs), 8, TextField.NUMERIC);

      form.append(tfNpcList);
      form.append(tfCheckin);
      form.append(tfZoneMs);
      form.append(cgScope);
      form.append(cgAttack);
      form.append(cgAttackType);
      form.append(tfAttackMs);
      form.append(T.utilAttackNote1);

      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      final javax.microedition.lcdui.Command cmdAttackOpt = new javax.microedition.lcdui.Command(T.npcAttackSettings, 4, 2);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.addCommand(cmdAttackOpt);

      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdAttackOpt) {
               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
               ClientUtilities.openNpcAttackAdvancedSettingsForm();
               return;
            }

            if (c == cmdSave) {
               npcSearchKeyword = tfNpcList.getString();
               eventCheckinChat = tfCheckin.getString();
               eventScopeAllMaps = cgScope.getSelectedIndex() == 0;
               autoNpcAttackEnabled = cgAttack.getSelectedIndex() == 1;
               npcAttackType = (byte)(cgAttackType.getSelectedIndex() == 1 ? 1 : 0);

               int ms;
               try {
                  ms = Integer.parseInt(tfZoneMs.getString().trim());
               } catch (Exception e) {
                  Canvas.startOKDlg(T.utilZoneIntervalInvalid);
                  return;
               }
               if (ms < 1) ms = 1;
               if (ms > 600000) ms = 600000;
               eventChangeZoneIntervalMs = ms;

               int atkMs;
               try {
                  atkMs = Integer.parseInt(tfAttackMs.getString().trim());
               } catch (Exception e) {
                  Canvas.startOKDlg(T.utilAttackIntervalInvalid);
                  return;
               }
               if (atkMs < 1) atkMs = 1;
               if (atkMs > 600000) atkMs = 600000;
               npcAttackDelayMs = atkMs;

               persistEventSettings();
               persistNpcSettings();
               Canvas.addServerInfo(T.utilSaveSettingsOk);
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openEventSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   static void openGiftCounterMenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.eventGiftCountPrefix + autoGiftCount, new IActionEventCmd((byte)10)));
      v.addElement(new Command(T.eventResetCounter, new IActionEventCmd((byte)11)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   private static void persistEventSettings() {
      try {
         // format: checkinChat|zoneMs|scopeAllMaps(1/0)
         String s = safe(eventCheckinChat) + "|" + eventChangeZoneIntervalMs + "|" + (eventScopeAllMaps ? 1 : 0);
         CRes.saveString(RMS_EVENT, s);
      } catch (Exception e) {
      }
   }

   public static void loadPersistedEventSettings() {
      try {
         String s = CRes.loadString(RMS_EVENT);
         if (s == null) return;
         int p1 = s.indexOf('|');
         int p2 = p1 < 0 ? -1 : s.indexOf('|', p1 + 1);
         if (p1 < 0 || p2 < 0) {
            return;
         }

         eventCheckinChat = s.substring(0, p1);
         eventChangeZoneIntervalMs = parseIntSafe(s.substring(p1 + 1, p2).trim(), eventChangeZoneIntervalMs);
         eventScopeAllMaps = "1".equals(s.substring(p2 + 1).trim());
      } catch (Exception e) {
      }
   }

   private static int parseIntSafe(String s, int fallback) {
      try {
         return Integer.parseInt(s);
      } catch (Exception e) {
         return fallback;
      }
   }

   private static String safe(String s) {
      return s == null ? "" : s;
   }

   private static String buildRememberedNpcIdCsv() {
      if (rememberedNpcList == null || rememberedNpcList.size() == 0) {
         return "";
      }

      String s = "";
      for (int i = 0; i < rememberedNpcList.size(); i++) {
         String entry = (String) rememberedNpcList.elementAt(i);
         int p = entry.indexOf('|');
         String id = p < 0 ? entry : entry.substring(0, p);
         if (id.length() == 0) continue;
         if (s.length() > 0) s += ",";
         s += id;
      }
      return s;
   }

   private static void applyRememberedNpcIdCsv(String csv) {
      if (csv == null) csv = "";
      csv = csv.trim();
      rememberedNpcList.removeAllElements();
      if (csv.length() == 0) return;

      int start = 0;
      while (start < csv.length()) {
         int p = csv.indexOf(',', start);
         if (p < 0) p = csv.length();
         String id = csv.substring(start, p).trim();
         if (id.length() > 0) {
            rememberedNpcList.addElement(id + "|");
         }
         start = p + 1;
      }
   }

   static void openNpcAttackAdvancedSettingsForm() {
      final Form form = new Form(T.option);
      form.append(String.valueOf(eventChangeZoneIntervalMs));

      final ChoiceGroup cgAttack = new ChoiceGroup(T.utilAttack, ChoiceGroup.EXCLUSIVE);
      cgAttack.append(T.utilAttackNone, null);
      cgAttack.append(T.utilAttackYes, null);
      cgAttack.setSelectedIndex(autoNpcAttackEnabled ? 1 : 0, true);
      form.append(cgAttack);

      form.append(T.utilAttackNote1);
      form.append(T.utilAttackNote2);
      form.append(npcAttackOnlyIfFindOn ? "ON" : "OFF");

      final ChoiceGroup cgType = new ChoiceGroup(T.utilAttackType, ChoiceGroup.EXCLUSIVE);
      cgType.append(T.utilAttackContinuous, null);
      cgType.append(T.utilAttackOnce, null);
      cgType.setSelectedIndex(npcAttackType == 1 ? 1 : 0, true);
      form.append(cgType);

      final TextField tfAttackMs = new TextField(T.utilAttackInterval, String.valueOf(npcAttackDelayMs), 8, TextField.NUMERIC);
      form.append(tfAttackMs);

      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);

      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               autoNpcAttackEnabled = cgAttack.getSelectedIndex() == 1;
               npcAttackType = (byte)(cgType.getSelectedIndex() == 1 ? 1 : 0);
               int ms;
               try {
                  ms = Integer.parseInt(tfAttackMs.getString().trim());
               } catch (Exception e) {
                  Canvas.startOKDlg(T.utilAttackIntervalInvalid);
                  return;
               }
               if (ms < 1) ms = 1;
               if (ms > 600000) ms = 600000;
               npcAttackDelayMs = ms;
               persistNpcSettings();
               Canvas.addServerInfo(T.utilSaveSettingsOk);
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openEventSubmenu();
         }
      });

      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void openAutoClickSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.utilStatus + ": " + boolCap(autoClickEnabled), new IActionAutoClickSub((byte)0)));
      v.addElement(new Command(T.option, new IActionAutoClickSub((byte)1)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void openAutoTrollSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.utilStatus + ": " + boolCap(autoTroll), new IActionAutoTrollSub((byte)0)));
      v.addElement(new Command(T.option, new IActionAutoTrollSub((byte)1)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void openAutoStoneSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.utilAutoStoneToggle + boolCap(autoStoneEnabled), new IActionAutoStoneSub((byte)0)));
      v.addElement(new Command(T.utilAutoStoneSettings, new IActionAutoStoneSub((byte)1)));
      v.addElement(new Command(T.utilAutoStoneSellSettings, new IActionAutoStoneSub((byte)2)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void openAutoStoneSellSettingsForm() {
      final Form form = new Form(T.utilAutoStoneSellForm);
      final ChoiceGroup cgStone = new ChoiceGroup(T.utilAutoSell, ChoiceGroup.MULTIPLE);
      cgStone.append(T.stonePebble, null);
      cgStone.append(T.stoneWhite, null);
      cgStone.append(T.stoneBlue, null);
      cgStone.append(T.stoneGreen, null);
      cgStone.append(T.stonePink, null);
      cgStone.append(T.stonePurple, null);
      cgStone.append(T.stoneGold, null);
      cgStone.append(T.stoneOrange, null);
      cgStone.append(T.stoneVoid, null);

      for (int i = 0; i < autoStoneSellStoneSelected.length && i < 9; i++) {
         cgStone.setSelectedIndex(i, autoStoneSellStoneSelected[i]);
      }

      form.append(cgStone);
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               for (int i = 0; i < autoStoneSellStoneSelected.length && i < 9; i++) {
                  autoStoneSellStoneSelected[i] = cgStone.isSelected(i);
               }

               persistAutoStoneSettings();
               Canvas.startOKDlg(T.utilAutoStoneSellSaved);
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openAutoStoneSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void openAutoStoneSettingsForm() {
      final Form form = new Form(T.utilAutoStoneMineForm);
      form.append(autoStoneToolStatusLine());
      final TextField tfInterval = new TextField(T.utilMineTime, String.valueOf(autoStoneIntervalMs), 10, TextField.NUMERIC);
      final TextField tfStartKey = new TextField(T.utilMineToggleKey, String.valueOf(autoStoneStartKey), 12, TextField.ANY);
      final ChoiceGroup cgOptions = new ChoiceGroup(T.utilMineOptions, ChoiceGroup.MULTIPLE);
      cgOptions.append(T.utilMineRecoverHealth, null);
      cgOptions.append(T.utilMinePickaxeXuOpt, null);
      cgOptions.append(T.utilMinePickaxeGoldOpt, null);
      cgOptions.append(T.utilMineShovelXuOpt, null);
      cgOptions.append(T.utilMineShovelGoldOpt, null);
      final TextField tfLimitPickaxeXu = new TextField(T.utilMineLimitPickaxeXu, String.valueOf(autoStonePickaxeXuBuyLimit), 6, TextField.NUMERIC);
      final TextField tfLimitPickaxeGold = new TextField(T.utilMineLimitPickaxeGold, String.valueOf(autoStonePickaxeGoldBuyLimit), 6, TextField.NUMERIC);
      final TextField tfLimitShovelXu = new TextField(T.utilMineLimitShovelXu, String.valueOf(autoStoneShovelXuBuyLimit), 6, TextField.NUMERIC);
      final TextField tfLimitShovelGold = new TextField(T.utilMineLimitShovelGold, String.valueOf(autoStoneShovelGoldBuyLimit), 6, TextField.NUMERIC);
      cgOptions.setSelectedIndex(0, autoStoneRecoverHealth);
      cgOptions.setSelectedIndex(1, autoStonePickaxeXu);
      cgOptions.setSelectedIndex(2, autoStonePickaxeGold);
      cgOptions.setSelectedIndex(3, autoStoneShovelXu);
      cgOptions.setSelectedIndex(4, autoStoneShovelGold);
      form.append(tfInterval);
      form.append(tfStartKey);
      form.append(cgOptions);
      form.append(tfLimitPickaxeXu);
      form.append(tfLimitPickaxeGold);
      form.append(tfLimitShovelXu);
      form.append(tfLimitShovelGold);
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               int ms;
               try {
                  ms = Integer.parseInt(tfInterval.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidMineTime);
                  return;
               }

               if (ms < 1) {
                  ms = 1;
               } else if (ms > 600000) {
                  ms = 600000;
               }

               int startKey;
               try {
                  startKey = Integer.parseInt(tfStartKey.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidToggleKey);
                  return;
               }

               int limPickaxeXu;
               int limPickaxeGold;
               int limShovelXu;
               int limShovelGold;
               try {
                  limPickaxeXu = Integer.parseInt(tfLimitPickaxeXu.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidPickaxeXuLimit);
                  return;
               }
               try {
                  limPickaxeGold = Integer.parseInt(tfLimitPickaxeGold.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidPickaxeGoldLimit);
                  return;
               }
               try {
                  limShovelXu = Integer.parseInt(tfLimitShovelXu.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidShovelXuLimit);
                  return;
               }
               try {
                  limShovelGold = Integer.parseInt(tfLimitShovelGold.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidShovelGoldLimit);
                  return;
               }
               if (limPickaxeXu < 0) limPickaxeXu = 0;
               if (limPickaxeGold < 0) limPickaxeGold = 0;
               if (limShovelXu < 0) limShovelXu = 0;
               if (limShovelGold < 0) limShovelGold = 0;

               ClientUtilities.autoStoneIntervalMs = ms;
               ClientUtilities.autoStoneStartKey = startKey;
               ClientUtilities.autoStoneRecoverHealth = cgOptions.isSelected(0);
               ClientUtilities.autoStonePickaxeXu = cgOptions.isSelected(1);
               ClientUtilities.autoStonePickaxeGold = cgOptions.isSelected(2);
               ClientUtilities.autoStoneShovelXu = cgOptions.isSelected(3);
               ClientUtilities.autoStoneShovelGold = cgOptions.isSelected(4);
               ClientUtilities.autoStonePickaxeXuBuyLimit = limPickaxeXu;
               ClientUtilities.autoStonePickaxeGoldBuyLimit = limPickaxeGold;
               ClientUtilities.autoStoneShovelXuBuyLimit = limShovelXu;
               ClientUtilities.autoStoneShovelGoldBuyLimit = limShovelGold;
               ClientUtilities.persistAutoStoneSettings();
               Canvas.startOKDlg(T.utilAutoStoneSaved);
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openAutoStoneSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void openAutoTrollSettingsForm() {
      final Form form = new Form(T.option);
      final TextField tfCode = new TextField(T.utilAutoTrollCode, String.valueOf(autoTrollCode), 8, TextField.ANY);
      final TextField tfLimit = new TextField(T.utilAutoTrollLimit, String.valueOf(autoTrollLimit), 12, TextField.NUMERIC);
      final TextField tfInterval = new TextField(T.utilAutoTrollIntervalMs, String.valueOf(autoTrollIntervalMs), 8, TextField.NUMERIC);
      form.append(tfCode);
      form.append(tfLimit);
      form.append(tfInterval);
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.utilAutoChatSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               int code;
               int limit;
               int ms;

               try {
                  code = Integer.parseInt(tfCode.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidCode);
                  return;
               }

               try {
                  limit = Integer.parseInt(tfLimit.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidLimit);
                  return;
               }

               try {
                  ms = Integer.parseInt(tfInterval.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidInterval);
                  return;
               }

               if (limit < 0) {
                  limit = 0;
               }

               if (ms < 1) {
                  ms = 1;
               } else if (ms > 600000) {
                  ms = 600000;
               }

               ClientUtilities.autoTrollCode = code;
               ClientUtilities.autoTrollLimit = limit;
               ClientUtilities.autoTrollIntervalMs = ms;
               ClientUtilities.resetAutoTrollSchedule();
               ClientUtilities.persistAutoTrollSettings();

               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
               Canvas.startOK(T.utilAutoTrollSaveSuccess, new IActionAutoTrollAfterSaveOk());
               return;
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openAutoTrollSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void openAutoGiftSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.utilStatus + ": " + boolCap(autoGift), new IActionAutoGiftSub((byte)0)));
      v.addElement(new Command(T.option, new IActionAutoGiftSub((byte)1)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void openAutoGiftSettingsForm() {
      final Form form = new Form(T.option);
      final TextField tfGift = new TextField(T.utilAutoGiftGiftId, String.valueOf(autoGiftGiftId), 12, TextField.NUMERIC);
      final TextField tfLimit = new TextField(T.utilAutoGiftLimit, String.valueOf(autoGiftLimit), 12, TextField.NUMERIC);
      final TextField tfInterval = new TextField(T.utilAutoGiftIntervalMs, String.valueOf(autoGiftIntervalMs), 8, TextField.NUMERIC);
      final TextField tfPayBy = new TextField(T.utilAutoGiftPayBy, String.valueOf(autoGiftPayBy), 2, TextField.NUMERIC);
      form.append(tfGift);
      form.append(tfLimit);
      form.append(tfInterval);
      form.append(tfPayBy);
      form.append(T.utilAutoGiftPayByHint);
      form.append(T.utilAutoGiftLimitHint);
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.utilAutoChatSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               int gid;
               int limit;
               int ms;
               int pay;

               try {
                  gid = Integer.parseInt(tfGift.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidGift);
                  return;
               }

               try {
                  ms = Integer.parseInt(tfInterval.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidInterval);
                  return;
               }

               try {
                  limit = Integer.parseInt(tfLimit.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidLimit);
                  return;
               }

               try {
                  pay = Integer.parseInt(tfPayBy.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidPayBy);
                  return;
               }

               if (ms < 1) {
                  ms = 1;
               } else if (ms > 600000) {
                  ms = 600000;
               }

               if (pay != 1 && pay != 2) {
                  pay = 1;
               }

               ClientUtilities.autoGiftGiftId = gid;
               if (limit < 0) {
                  limit = 0;
               }

               ClientUtilities.autoGiftLimit = limit;
               ClientUtilities.autoGiftIntervalMs = ms;
               ClientUtilities.autoGiftPayBy = pay;
               ClientUtilities.autoGiftCount = 0;
               ClientUtilities.persistAutoGiftSettings();

               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
               Canvas.startOK(T.utilAutoGiftSaveSuccess, new IActionAutoGiftAfterSaveOk());
               return;
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openAutoGiftSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void openAutoClickSettingsForm() {
      final Form form = new Form(T.option);
      final TextField tfKey = new TextField(T.utilAutoClickKeyCode, String.valueOf(autoClickKeyCode), 12, TextField.ANY);
      final TextField tfStart = new TextField(T.utilAutoClickStartKey, String.valueOf(autoClickStartKey), 12, TextField.ANY);
      final TextField tfInterval = new TextField(T.utilAutoClickIntervalMs, String.valueOf(autoClickIntervalMs), 8, TextField.NUMERIC);
      final ChoiceGroup cgNotify = new ChoiceGroup(T.utilAutoClickNotifyToggle, 1);
      cgNotify.append(T.no, null);
      cgNotify.append(T.yes, null);
      cgNotify.setSelectedIndex(autoClickShowToggleNotice ? 1 : 0, true);
      form.append(tfKey);
      form.append(tfStart);
      form.append(tfInterval);
      form.append(cgNotify);
      form.append(T.utilAutoClickSettingsNote);
      form.append(T.utilAutoClickKeyList);
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.utilAutoChatSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               int k;
               int s;
               int ms;

               try {
                  k = Integer.parseInt(tfKey.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidKeyCode);
                  return;
               }

               try {
                  s = Integer.parseInt(tfStart.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidStartKey);
                  return;
               }

               try {
                  ms = Integer.parseInt(tfInterval.getString().trim());
               } catch (Exception var2) {
                  Canvas.startOKDlg(T.utilInvalidInterval);
                  return;
               }

               if (ms < 1) {
                  ms = 1;
               } else if (ms > 600000) {
                  ms = 600000;
               }

               ClientUtilities.autoClickKeyCode = k;
               ClientUtilities.autoClickStartKey = s;
               ClientUtilities.autoClickIntervalMs = ms;
               ClientUtilities.autoClickShowToggleNotice = cgNotify.getSelectedIndex() == 1;
               ClientUtilities.persistAutoClickSettings();
               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
               Canvas.startOK(T.utilAutoClickSaveSuccess, new IActionAutoClickAfterSaveOk());
               return;
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openAutoClickSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void persistAutoClickSettings() {
      try {
         CRes.saveString(RMS_AUTO_CLICK, autoClickKeyCode + ";" + autoClickStartKey + ";" + autoClickIntervalMs + ";" + (autoClickShowToggleNotice ? 1 : 0));
      } catch (Throwable var1) {
      }
   }

   public static void persistAutoGiftSettings() {
      try {
         CRes.saveString(RMS_AUTO_GIFT, autoGiftGiftId + "\n" + autoGiftLimit + "\n" + autoGiftIntervalMs + "\n" + autoGiftPayBy);
      } catch (Throwable var1) {
      }
   }

   public static void persistAutoTrollSettings() {
      try {
         CRes.saveString(RMS_AUTO_TROLL, autoTrollCode + "\n" + autoTrollLimit + "\n" + autoTrollIntervalMs);
      } catch (Throwable var1) {
      }
   }

   public static void persistAutoStoneSettings() {
      try {
         String sell = "";
         for (int i = 0; i < autoStoneSellStoneSelected.length; i++) {
            sell += (autoStoneSellStoneSelected[i] ? "1" : "0");
            if (i + 1 < autoStoneSellStoneSelected.length) sell += ",";
         }

         CRes.saveString(RMS_AUTO_STONE,
                 autoStoneIntervalMs + "\n"
                         + autoStoneStartKey + "\n"
                         + (autoStoneRecoverHealth ? 1 : 0) + "\n"
                         + (autoStonePickaxeXu ? 1 : 0) + "\n"
                         + (autoStonePickaxeGold ? 1 : 0) + "\n"
                         + (autoStoneShovelXu ? 1 : 0) + "\n"
                         + (autoStoneShovelGold ? 1 : 0) + "\n"
                         + autoStonePickaxeXuBuyLimit + "\n"
                         + autoStonePickaxeGoldBuyLimit + "\n"
                         + autoStoneShovelXuBuyLimit + "\n"
                         + autoStoneShovelGoldBuyLimit + "\n"
                         + sell);
      } catch (Throwable var1) {
      }
   }

   public static void resetAutoStoneSchedule() {
      autoStoneNextAtMs = 0L;
      autoStonePickaxeXuBought = 0;
      autoStonePickaxeGoldBought = 0;
      autoStoneShovelXuBought = 0;
      autoStoneShovelGoldBought = 0;
   }

   private static void autoStoneSellByMenuOption() {
      for(int idx = 0; idx < autoStoneSellStoneSelected.length; ++idx) {
         if (autoStoneSellStoneSelected[idx]) {
            ParkService.gI().doBossShop(STONE_SHOP_NPC_ID, STONE_SHOP_MENU_BYTE, idx);
         }
      }
   }

   private static boolean hasPartInContainer(short partId) {
      try {
         Vector v = GameMidlet.listContainer;
         if (v == null) return false;
         for (int i = 0; i < v.size(); i++) {
            Object o = v.elementAt(i);
            if (!(o instanceof SeriPart)) continue;
            SeriPart sp = (SeriPart)o;
            if (sp != null && sp.idPart == partId) {
               return true;
            }
         }
      } catch (Throwable t) {
      }
      return false;
   }

   private static boolean hasAnyPartInContainer(short a, short b) {
      return hasPartInContainer(a) || hasPartInContainer(b);
   }

   private static void loadPersistedAutoClickSettings() {
      String s = CRes.loadString(RMS_AUTO_CLICK);
      if (s == null) {
         return;
      }

      s = s.trim();
      if (s.length() == 0) {
         return;
      }

      int p1 = s.indexOf(59);
      int p2 = s.indexOf(59, p1 + 1);
      if (p1 < 0 || p2 < 0) {
         return;
      }
      int p3 = s.indexOf(59, p2 + 1);

      try {
         int k = Integer.parseInt(s.substring(0, p1).trim());
         int st = Integer.parseInt(s.substring(p1 + 1, p2).trim());
         int ms;
         if (p3 > 0) {
            ms = Integer.parseInt(s.substring(p2 + 1, p3).trim());
            int notify = Integer.parseInt(s.substring(p3 + 1).trim());
            autoClickShowToggleNotice = notify != 0;
         } else {
            ms = Integer.parseInt(s.substring(p2 + 1).trim());
         }

         autoClickKeyCode = k;
         autoClickStartKey = st;
         if (ms >= 1) {
            if (ms > 600000) {
               ms = 600000;
            }

            autoClickIntervalMs = ms;
         }
      } catch (Exception var5) {
      }
   }

   public static void openAutoChatSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(boolCap(autoChat), new IActionAutoChatSub((byte)0)));
      v.addElement(new Command(T.utilAutoChatBreak + ": " + autoChatIntervalSeconds + "s", new IActionAutoChatSub((byte)1)));
      v.addElement(new Command(T.utilAutoChatContent, new IActionAutoChatSub((byte)2)));
      v.addElement(new Command(T.utilAutoChatGuide, new IActionAutoChatSub((byte)3)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void openAutoChatBreakDialog() {
      Canvas.inputDlg.setImg(T.utilAutoChatBreakPrompt, new IActionAutoChatBreakOk(), 1, String.valueOf(autoChatIntervalSeconds));
   }

   public static void openAutoChatContentForm() {
      String cur = autoChatMessage != null ? autoChatMessage : ":)";
      if (cur.length() > 200) {
         cur = cur.substring(0, 200);
      }

      final Form form = new Form(T.utilAutoChatContent);
      final TextField tf = new TextField(T.utilAutoChatEnterContent, cur, 200, TextField.ANY);
      form.append(tf);
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.utilAutoChatSave, 4, 1);
      final javax.microedition.lcdui.Command cmdBack = new javax.microedition.lcdui.Command(T.utilAutoChatBack, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdBack);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               String s = tf.getString();
               if (s == null) {
                  s = "";
               } else {
                  s = s.trim();
               }

               if (s.length() == 0) {
                  ClientUtilities.autoChatMessage = ":)";
               } else if (s.length() > 200) {
                  ClientUtilities.autoChatMessage = s.substring(0, 200);
               } else {
                  ClientUtilities.autoChatMessage = s;
               }

               ClientUtilities.resetAutoChatSchedule();
               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
               Canvas.startOK(T.utilAutoChatSaveSuccess, new IActionAutoChatAfterSaveOk());
               return;
            }

            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openAutoChatSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   public static void openChangeMapSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.utilMapSubSun, new IActionJoinUtilityMap(0)));
      v.addElement(new Command(T.utilMapSubGreenPark, new IActionJoinUtilityMap(11)));
      v.addElement(new Command(T.utilMapSubShop, new IActionJoinUtilityMap(23)));
      v.addElement(new Command(T.utilMapSubFun, new IActionJoinUtilityMap(9)));
      v.addElement(new Command(T.utilMapSubEco, new IActionJoinUtilityMap(13)));
      v.addElement(new Command(T.utilMapSubFarm, new IActionJoinUtilityMap(25)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void requestChangeZone() {
      Canvas.startWaitDlg();
      MapScr.gI().joinCitymap();
   }

   public static void changeZone() {
      Canvas.startWaitDlg();
      ParkMsgHandler.onHandler();
      ParkService.gI().doRequestBoardList(MapScr.roomID);
   }

   public static void loadPersistedUtilitySettings() {
      byte[] r = CRes.loadRMS(RMS_UTIL_SPEED);
      if (r != null && r.length >= 1) {
         int v = r[0] & 255;
         if (v >= 1 && v <= 100) {
            utilSpeed1to100 = v;
         }
      }

      loadPersistedAutoClickSettings();
      loadPersistedAutoGiftSettings();
      loadPersistedAutoTrollSettings();
      loadPersistedAutoStoneSettings();
      loadPersistedEventSettings();
      loadPersistedNpcSettings();
      loadPersistedFishingSettings();
     
      map13GuideShown = false;
      lastObservedMapType = -1;
   }

   private static void maybeShowMap13GuideTab() {
      int map = LoadMap.TYPEMAP;
      if (map == lastObservedMapType) {
         return;
      }
      lastObservedMapType = map;
      if (map != 13 || map13GuideShown) {
         return;
      }

      map13GuideShown = true;

      Hashtable icons = new Hashtable();
      String title = "Bản cập nhật này có gì mới ?";
      String content =
              "1 .Cập nhật lại auto quay số , auto câu cá , cập nhật hiển thị rương đồ.\n"
              + "2. Hướng dẫn sử dụng\n"
                      + "- Bản cập nhật này Bằng đz đã thêm menu thêm npc.\n"
                      + "- Anh em đến NPC quay số/Quay số vip ấn menu thêm npc.\n"
                      + "- Cài đặt cấu hình đào đá và auto Bán đá.\n"
                      + "- Vào map ae muốn câu - Nhớ mua loại cần và vé tương ứng , sẽ cập nhật tự mua ở bản sau -\n"
                      + "- Vào map câu cá ngồi vào chỗ câu , ấn Menu cài đặt cấu hình bán cá , cập nhật danh sách cá cần bán\n"
                      + "- Ngồi câu ae có thể nhấn phím * (phím sao) ở bàn phím để bật auto đào đá, ấn menu chọn danh sách npc , chọn npcquay số và cài đặt cấu hình quay số , rồi auto quay số là được ! -\n"
                      + "- Chúc anh em có một trải nghiệm phiên bản vui vẻ -\n"
                      + "- Mọi thắc mắc vui lòng liên hệ admin : Nguyễn Văn Bằng \n"
                     +   "--------- Zalo : 0787054816  ---------\n";
      try {
         CustomTab.me = null;
      } catch (Throwable t) {
      }
      CustomTab.gI().setInfo(icons, title, content, (byte)-1);
      CustomTab.gI().show();
   }

   private static void persistFishingSettings() {
      try {
         String sell = "";
         for (int i = 0; i < fishingSellFishSelected.length; i++) {
            sell += (fishingSellFishSelected[i] ? "1" : "0");
            if (i + 1 < fishingSellFishSelected.length) sell += ",";
         }
         CRes.saveString(RMS_FISHING, (fishingAutoLogin ? "1" : "0") + "|" + (fishingAutoBuyBait ? "1" : "0") + "|" + (fishingRedFieldEnabled ? "1" : "0") + "|" + fishingCounter + "|" + fishingSoldCounter + "|" + sell + "|" + (fishingAutoBuyTicket ? "1" : "0") + "|" + fishingSelectedTicket + "|" + (fishingAutoBuyRod ? "1" : "0") + "|" + fishingSelectedRod + "|" + (autoStoneEnabled ? "1" : "0") + "|" + (fishingAutoSellStone ? "1" : "0"));
      } catch (Exception e) {
      }
   }

   private static void loadPersistedFishingSettings() {
      try {
         String s = CRes.loadString(RMS_FISHING);
         if (s == null) return;
         String[] parts = splitLines3(s);
         if (parts == null || parts.length == 0) {
            return;
         }
         fishingAutoLogin = "1".equals(parts[0].trim());
         fishingAutoBuyBait = parts.length > 1 && "1".equals(parts[1].trim());
         fishingRedFieldEnabled = parts.length > 2 && "1".equals(parts[2].trim());
         if (parts.length > 3) {
            try {
               fishingCounter = Integer.parseInt(parts[3].trim());
            } catch (Exception ex) {
               fishingCounter = 0;
            }
         }
         if (parts.length > 4) {
            try {
               fishingSoldCounter = Integer.parseInt(parts[4].trim());
            } catch (Exception ex) {
               fishingSoldCounter = 0;
               String[] sellsLegacy = parts[4].split(",");
               for (int i = 0; i < fishingSellFishSelected.length && i < sellsLegacy.length; i++) {
                  fishingSellFishSelected[i] = "1".equals(sellsLegacy[i].trim());
               }
            }
         }
         if (parts.length > 5) {
            String[] sells = parts[5].split(",");
            for (int i = 0; i < fishingSellFishSelected.length && i < sells.length; i++) {
               fishingSellFishSelected[i] = "1".equals(sells[i].trim());
            }
         }
         if (parts.length > 6) {
            fishingAutoBuyTicket = "1".equals(parts[6].trim());
         }
         if (parts.length > 7) {
            try {
               fishingSelectedTicket = Integer.parseInt(parts[7].trim());
               if (fishingSelectedTicket < 0) fishingSelectedTicket = 0;
               if (fishingSelectedTicket > 1) fishingSelectedTicket = 1;
            } catch (Exception ex) {
               fishingSelectedTicket = 0;
            }
         }
         if (parts.length > 8) {
            fishingAutoBuyRod = "1".equals(parts[8].trim());
         }
         if (parts.length > 9) {
            try {
               fishingSelectedRod = Integer.parseInt(parts[9].trim());
               if (fishingSelectedRod < 0) fishingSelectedRod = 0;
               if (fishingSelectedRod > 1) fishingSelectedRod = 1;
            } catch (Exception ex) {
               fishingSelectedRod = 0;
            }
         }
         if (parts.length > 10) {
            autoStoneEnabled = "1".equals(parts[10].trim());
         }
         if (parts.length > 11) {
            fishingAutoSellStone = "1".equals(parts[11].trim());
         }
         if (fishingAutoLogin) {
            ensureFishingAutoLoginThread();
         }
      } catch (Exception e) {
      }
   }

   static void openFishingSettingsSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.menuAutoFishingSettings, new IActionFishingSettings((byte)0)));
      v.addElement(new Command(T.container, new IActionFishingSettings((byte)1)));
      v.addElement(new Command(T.fishResetCounter, new IActionFishingSettings((byte)2)));
      v.addElement(new Command(T.fishAutoLoginPrefix + boolCap(fishingAutoLogin), new IActionFishingSettings((byte)3)));
      if (Canvas.currentMyScreen == FishingScr.gI() && (LoadMap.TYPEMAP == 14 || LoadMap.TYPEMAP == 15 || LoadMap.TYPEMAP == 16)) {
         v.addElement(new Command(T.mapNpcList, new IActionMapNpcCmd((byte)1)));
      }
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   static void openPlayerContainerSubmenu() {
      Avatar me = GameMidlet.avatar;
      if (Canvas.currentMyScreen != PopupShop.gI()) {
         PopupShop.gI().isFull = true;
         PopupShop.gI().addElement(new String[]{T.container, T.wearing}, new Vector[]{MapScr.gI().getListCmdDoUsing(GameMidlet.listContainer, me.IDDB, 1), MapScr.gI().getListYourPart(me, 0)}, (Vector)null);
         PopupShop.gI().setCmdLeft(MapScr.gI().cmdDellPart(GameMidlet.listContainer, 1, 0, true), 0);
         PopupShop.gI().setCmdLeft(MapScr.gI().cmdDellPart(me.seriPart, 0, 0, false), 1);
         if (Canvas.currentMyScreen != PopupShop.gI()) {
            PopupShop.gI().switchToMe();
         }
      }
   }

   static void requestContainerAndOpen() {
      Avatar me = GameMidlet.avatar;
      if (me != null) {
         GlobalService.gI().doRequestContainer(me.IDDB);
      }
      if (GameMidlet.listContainer != null) {
         openPlayerContainerSubmenu();
      } else {
         Canvas.addServerInfo(T.fishLoadingChest);
      }
   }

   static void toggleFishingAutoLogin() {
      fishingAutoLogin = !fishingAutoLogin;
      persistFishingSettings();
      System.out.println("[FISH_AUTO_LOGIN] toggleFishingAutoLogin -> " + fishingAutoLogin);
      if (fishingAutoLogin) {
         ensureFishingAutoLoginThread();
         Canvas.startOKDlg(T.fishAutoLoginOn);
      } else {
         Canvas.startOKDlg(T.fishAutoLoginOff);
      }
   }

   static void onFishingAutoDisconnected() {
      System.out.println("[FISH_AUTO_LOGIN] onFishingAutoDisconnected called. enabled=" + fishingAutoLogin + " stage=" + fishingReloginStage);
      if (!fishingAutoLogin) {
         System.out.println("[FISH_AUTO_LOGIN] skip disconnected flow because enabled=false");
         return;
      }
      if (fishingReloginStage != 0) {
         System.out.println("[FISH_AUTO_LOGIN] skip disconnected flow because stage=" + fishingReloginStage);
         return;
      }
      try {
         if (GameMidlet.avatar != null) {
            fishingReloginSavedX = GameMidlet.avatar.x;
            fishingReloginSavedY = GameMidlet.avatar.y;
            fishingReloginSavedDir = GameMidlet.avatar.direct;
            fishingReloginSavedDir2 = GameMidlet.avatar.direct_;
         }
         fishingReloginSavedMap = MapScr.roomID;
         fishingReloginSavedBoard = MapScr.boardID;
      } catch (Throwable t) {
      }
      fishingReloginStage = 1;
      fishingReloginNextAtMs = System.currentTimeMillis() + 3000L;
      fishingReloginLoginIssued = false;
      System.out.println("[FISH_AUTO_LOGIN] relogin armed. map=" + fishingReloginSavedMap + " board=" + fishingReloginSavedBoard + " x=" + fishingReloginSavedX + " y=" + fishingReloginSavedY + " nextAt=" + fishingReloginNextAtMs);
      try {
         // Yêu cầu: nhận dialog mất kết nối là về Login ngay, rồi đợi 3s tự login
         LoginScr.gI().switchToMe();
      } catch (Throwable t) {
      }
   }

   static boolean isFishingReloginWaiting() {
      return fishingReloginStage == 1;
   }

   static long getFishingReloginNextAtMs() {
      return fishingReloginNextAtMs;
   }

   static boolean isFishingReloginLoginIssued() {
      return fishingReloginLoginIssued;
   }

   static void markFishingReloginLoginIssued() {
      fishingReloginLoginIssued = true;
   }

   static boolean isFishingReloginActive() {
      return fishingAutoLogin && fishingReloginStage >= 1 && fishingReloginStage <= 4;
   }

   static void forceFishingReloginJoinMap9FromHandler9() {
      if (!fishingAutoLogin) return;
      if (fishingReloginStage != 1 && fishingReloginStage != 2) return;
      fishingReloginStage = 2;
      try {
         System.out.println("[FISH_AUTO_LOGIN] force doJoinPark(9, -1) from doGetHandler(9), stage=" + fishingReloginStage);
         ParkService.gI().doJoinPark(9, -1);
      } catch (Throwable t) {
         System.out.println("[FISH_AUTO_LOGIN] force doJoinPark(9, -1) error: " + t.toString());
      }
   }

   public static boolean onFishingAutoLoginSuccess() {
      System.out.println("[FISH_AUTO_LOGIN] onFishingAutoLoginSuccess called. enabled=" + fishingAutoLogin + " stage=" + fishingReloginStage + " loginIssued=" + fishingReloginLoginIssued);
      if (!fishingAutoLogin) return false;
      if (fishingReloginStage != 1) return false;
      fishingReloginStage = 2;
      try {
         System.out.println("[FISH_AUTO_LOGIN] doJoinPark(9, -1)");
         ParkService.gI().doJoinPark(9, -1);
      } catch (Throwable t) {
         System.out.println("[FISH_AUTO_LOGIN] doJoinPark(9, -1) error: " + t.toString());
      }
      return true;
   }

   static void toggleFishingAutoBuyBait() {
      fishingAutoBuyBait = !fishingAutoBuyBait;
      fishingAutoBaitPendingBuy = false;
      fishingAutoBaitBuyStep = 0;
      fishingAutoBaitMoveDownRemain = 0;
      fishingAutoBaitEnterRemain = 0;
      fishingAutoBaitReturnMap = -1;
      fishingAutoBaitNextAtMs = 0L;
      fishingAutoBaitRequested = false;
      fishingAutoBaitReturnX = -1;
      fishingAutoBaitReturnY = -1;
      fishingAutoBaitNeedReturnMove = false;
      fishingAutoBaitNeedResumeSit = false;
      fishingAutoBaitSitX = -1;
      fishingAutoBaitSitY = -1;
      fishingAutoBaitMoveTargetX = -1;
      fishingAutoBaitMoveTargetY = -1;
      fishingAutoBaitForceExitTries = 0;
      fishingAutoBaitDirectBuyRemain = 0;
      persistFishingSettings();
      Canvas.addServerInfo(T.fishAutoBaitToggle + boolCap(fishingAutoBuyBait));
   }

   public static void onDialogShown(String msg) {
      if (msg == null) {
         return;
      }

      String s = safe(msg).toLowerCase();
      String sn = normalizeForNpcMatch(s);

      
      try {
         if (fishingAutoLogin && (sn.indexOf("matketnoi") >= 0 || sn.indexOf("khongtheketnoi") >= 0)) {
            onFishingAutoDisconnected();
            try { Canvas.endDlg(); } catch (Throwable tt) {}
            return;
         }
      } catch (Throwable t) {
      }
      if (autoStoneEnabled) {
        
         try {
            if (autoStoneRecoverHealth && sn.indexOf("suckhoekhongdu") >= 0) {
               ParkService.gI().doBuyItem((short)7);
               Canvas.addServerInfo("Auto đào đá: tự hồi phục sức khỏe");
               try {
                  Canvas.endDlg();
               } catch (Throwable tt) {
               }
               return;
            } else if (sn.indexOf("alodao.dabangniem.tinha") >= 0
                    || sn.indexOf("alo?tinhdaodabangniemtinha") >= 0
                    || sn.indexOf("tinhdaodatbangniemtinha") >= 0
                    || sn.indexOf("daodatbangniemtinha") >= 0
                    || (sn.indexOf("alodao") >= 0 && sn.indexOf("niem.tinha") >= 0)) {
               
               boolean bought = false;
               if (autoStonePickaxeXu && (autoStonePickaxeXuBuyLimit <= 0 || autoStonePickaxeXuBought < autoStonePickaxeXuBuyLimit)) {
                  markAutoStonePendingPurchase((byte)0);
                  ParkService.gI().doBossShop(2000000010, -3, 3);
                  bought = true;
               } else if (autoStonePickaxeGold && (autoStonePickaxeGoldBuyLimit <= 0 || autoStonePickaxeGoldBought < autoStonePickaxeGoldBuyLimit)) {
                  markAutoStonePendingPurchase((byte)1);
                  ParkService.gI().doBossShop(2000000010, -3, 2);
                  bought = true;
               } else if (autoStoneShovelXu && (autoStoneShovelXuBuyLimit <= 0 || autoStoneShovelXuBought < autoStoneShovelXuBuyLimit)) {
                  markAutoStonePendingPurchase((byte)2);
                  ParkService.gI().doBossShop(2000000010, -3, 1);
                  bought = true;
               } else if (autoStoneShovelGold && (autoStoneShovelGoldBuyLimit <= 0 || autoStoneShovelGoldBought < autoStoneShovelGoldBuyLimit)) {
                  markAutoStonePendingPurchase((byte)3);
                  ParkService.gI().doBossShop(2000000010, -3, 0);
                  bought = true;
               }
               if (!bought) {
                  Canvas.addServerInfo("Auto đào đá: đã đạt giới hạn mua dụng cụ");
               }
               try {
                  Canvas.endDlg();
               } catch (Throwable tt) {
               }
               return;
            }
         } catch (Throwable t) {
         }

        
         try {
            Canvas.endDlg();
         } catch (Throwable tt) {
         }
         return;
      }

      int map = LoadMap.TYPEMAP;
      if (map != 13 && map != 14 && map != 15 && map != 16) {
         return;
      }
      if (sn.indexOf("bancansudungmoicau") >= 0 ||
         sn.indexOf("caukhatruongnaydungmoicau") >= 0 ||
         (sn.indexOf("moicau") >= 0 && sn.indexOf("caucatrongkhuvucnay") >= 0) ||
         sn.indexOf("trungkien") >= 0 ||
         (sn.indexOf("heti") >= 0 && sn.indexOf("moi") >= 0 && sn.indexOf("cau") >= 0 && sn.indexOf("vuilongmuathem") >= 0) ||
         (sn.indexOf("moi") >= 0 && sn.indexOf("cau") >= 0 && sn.indexOf("vuimua") >= 0)
      ) {
         fishingAutoBaitRequested = true;
         fishingAutoBaitForceExitTries = 0;
         fishingAutoBaitNextAtMs = 0L;
         forceExitFishingState();
         // Đóng dialog trước khi đi
         try { Canvas.endDlg(); } catch (Throwable ttt) {}
         // Đi đến map 13 mua mồi
         fishingAutoBaitReturnMap = (byte)LoadMap.TYPEMAP;
         fishingAutoBaitSitX = 924;
         fishingAutoBaitSitY = 119;
         fishingAutoBuyBaitPending = true;
         GlobalService.gI().getHandler(9);
         ParkService.gI().doJoinPark(13, -1);
         Canvas.addServerInfo("Phat hien het moi, di mua...");
         return;
      }

      // Xử lý dialog hết vé câu cá hoặc thiếu cần câu
      boolean isNeedTicketOrRod = (
         (sn.indexOf("vecaucamoi") >= 0 && (sn.indexOf("khuvucnay") >= 0 || sn.indexOf("khuvuc") >= 0)) ||
         (sn.indexOf("vecauca") >= 0 && sn.indexOf("map") >= 0) ||
         (sn.indexOf("banccan") >= 0 && sn.indexOf("cau") >= 0 && sn.indexOf("cauca") >= 0) ||
         (sn.indexOf("trangbi") >= 0 && sn.indexOf("cau") >= 0 && sn.indexOf("cauca") >= 0) ||
         (sn.indexOf("can") >= 0 && sn.indexOf("cau") >= 0 && sn.indexOf("cauca") >= 0 && sn.indexOf("vatpham") >= 0) ||
         (sn.indexOf("cancau") >= 0 && sn.indexOf("cuca") >= 0)
      );

      if (isNeedTicketOrRod && (fishingAutoBuyRod || fishingAutoBuyTicket)) {
         // Canvas.addServerInfo("Phat hien can mua - bat dau mua ngay!");
         try { Canvas.endDlg(); } catch (Throwable tt) {}
        
         int ticketId = getFishingTicketId();
         int rodId = getFishingRodId();
         if (fishingAutoBuyTicket) {
            // Canvas.addServerInfo("Mua ve: " + ticketId);
            AvatarService.gI().doBuyItem(ticketId, 1);
         }
         if (fishingAutoBuyRod) {
            // Canvas.addServerInfo("Mua can: " + rodId);
            AvatarService.gI().doBuyItem(rodId, 1);
         }
         return;
      }
   }

   private static void markAutoStonePendingPurchase(byte type) {
      try {
         if (GameMidlet.avatar == null) {
            return;
         }
         autoStonePendingBuyType = type;
         autoStonePendingMoneyXu = GameMidlet.avatar.money[0];
         autoStonePendingMoneyGold = GameMidlet.avatar.money[2];
         autoStonePendingExpiresAtMs = System.currentTimeMillis() + 5000L;
      } catch (Throwable t) {
      }
   }

   private static void checkAutoStonePendingPurchase() {
      if (autoStonePendingBuyType < 0) {
         return;
      }
      if (GameMidlet.avatar == null) {
         return;
      }

      long now = System.currentTimeMillis();
      if (now > autoStonePendingExpiresAtMs) {
         autoStonePendingBuyType = -1;
         return;
      }

      int xuNow = GameMidlet.avatar.money[0];
      int goldNow = GameMidlet.avatar.money[2];
      boolean xuSpent = autoStonePendingMoneyXu >= 0 && xuNow >= 0 && xuNow < autoStonePendingMoneyXu;
      boolean goldSpent = autoStonePendingMoneyGold >= 0 && goldNow >= 0 && goldNow < autoStonePendingMoneyGold;
      if (!xuSpent && !goldSpent) {
         return;
      }

      switch (autoStonePendingBuyType) {
         case 0:
            autoStonePickaxeXuBought++;
            break;
         case 1:
            autoStonePickaxeGoldBought++;
            break;
         case 2:
            autoStoneShovelXuBought++;
            break;
         case 3:
            autoStoneShovelGoldBought++;
            break;
      }
      autoStonePendingBuyType = -1;
   }

   private static void forceExitFishingState() {
      try {
         // Ask server to end current fishing state.
         ParkService.gI().doCauCaXong();
      } catch (Throwable t) {
      }

      try {
         // If fishing screen is still open, close it locally as well.
         if (Canvas.currentMyScreen == FishingScr.gI()) {
            FishingScr.gI().commandTab(2, -1);
         }
      } catch (Throwable t) {
      }
   }

   private static void ensureFishingAutoLoginThread() {
      if (!fishingAutoLogin) return;
      if (fishingAutoLoginThread != null && fishingAutoLoginThread.isAlive()) return;
      fishingAutoLoginThread = new Thread(new SmartLogin());
      System.out.println("[FISH_AUTO_LOGIN] start SmartLogin thread");
      fishingAutoLoginThread.start();
   }

   private static void loadPersistedAutoTrollSettings() {
      String s = CRes.loadString(RMS_AUTO_TROLL);
      if (s == null) {
         return;
      }

      String[] lines = splitLines3(s);
      if (lines == null) {
         return;
      }

      try {
         autoTrollCode = Integer.parseInt(lines[0].trim());
      } catch (Exception var2) {
      }

      try {
         int limit = Integer.parseInt(lines[1].trim());
         if (limit < 0) {
            limit = 0;
         }

         autoTrollLimit = limit;
      } catch (Exception var2) {
      }

      try {
         int ms = Integer.parseInt(lines[2].trim());
         if (ms < 1) {
            ms = 1;
         } else if (ms > 600000) {
            ms = 600000;
         }

         autoTrollIntervalMs = ms;
      } catch (Exception var2) {
      }
   }

   private static void loadPersistedAutoStoneSettings() {
      String s = CRes.loadString(RMS_AUTO_STONE);
      if (s == null) {
         return;
      }

      String[] lines = splitLines(s);
      if (lines == null || lines.length < 6) {
         return;
      }

      int base = 0;
      try {
         int ms = Integer.parseInt(lines[0].trim());
         if (ms < 1) {
            ms = 1;
         } else if (ms > 600000) {
            ms = 600000;
         }

         autoStoneIntervalMs = ms;
      } catch (Exception var2) {
      }

      try {
         // New format: [0]=ms,[1]=startKey,[2..]=flags...
         if (lines.length >= 7) {
            autoStoneStartKey = Integer.parseInt(lines[1].trim());
            base = 1;
         }
      } catch (Exception var2) {
      }

      try {
         autoStoneRecoverHealth = Integer.parseInt(lines[1 + base].trim()) != 0;
      } catch (Exception var2) {
      }

      try {
         autoStonePickaxeXu = Integer.parseInt(lines[2 + base].trim()) != 0;
      } catch (Exception var2) {
      }

      try {
         autoStonePickaxeGold = Integer.parseInt(lines[3 + base].trim()) != 0;
      } catch (Exception var2) {
      }

      try {
         autoStoneShovelXu = Integer.parseInt(lines[4 + base].trim()) != 0;
      } catch (Exception var2) {
      }

      try {
         autoStoneShovelGold = Integer.parseInt(lines[5 + base].trim()) != 0;
      } catch (Exception var2) {
      }

      if (lines.length >= (10 + base)) {
         try {
            autoStonePickaxeXuBuyLimit = Integer.parseInt(lines[6 + base].trim());
            if (autoStonePickaxeXuBuyLimit < 0) autoStonePickaxeXuBuyLimit = 0;
         } catch (Exception var2) {
         }
         try {
            autoStonePickaxeGoldBuyLimit = Integer.parseInt(lines[7 + base].trim());
            if (autoStonePickaxeGoldBuyLimit < 0) autoStonePickaxeGoldBuyLimit = 0;
         } catch (Exception var2) {
         }
         try {
            autoStoneShovelXuBuyLimit = Integer.parseInt(lines[8 + base].trim());
            if (autoStoneShovelXuBuyLimit < 0) autoStoneShovelXuBuyLimit = 0;
         } catch (Exception var2) {
         }
         try {
            autoStoneShovelGoldBuyLimit = Integer.parseInt(lines[9 + base].trim());
            if (autoStoneShovelGoldBuyLimit < 0) autoStoneShovelGoldBuyLimit = 0;
         } catch (Exception var2) {
         }
      }

      if (lines.length >= (11 + base)) {
         try {
            String[] sells = lines[10 + base].split(",");
            for (int i = 0; i < autoStoneSellStoneSelected.length && i < sells.length; i++) {
               autoStoneSellStoneSelected[i] = "1".equals(sells[i].trim());
            }
         } catch (Exception var2) {
         }
      }
   }

   public static void toggleAutoStoneEnabled() {
      autoStoneEnabled = !autoStoneEnabled;
      resetAutoStoneSchedule();
      Canvas.addServerInfo(T.fishAutoStoneToggle + boolCap(autoStoneEnabled));
   }

   public static boolean handleAutoStoneStartHotKey(int keyCode) {
      if (keyCode != autoStoneStartKey) return false;
      toggleAutoStoneEnabled();
      return true;
   }

   private static String[] splitLines3(String s) {
      if (s == null) {
         return null;
      }

      int p1 = s.indexOf(10);
      if (p1 < 0) return null;
      int p2 = s.indexOf(10, p1 + 1);
      if (p2 < 0) return null;
      return new String[]{s.substring(0, p1), s.substring(p1 + 1, p2), s.substring(p2 + 1)};
   }

   static void openFishingSettingsForm(boolean showAutoStone) {
      final Form form = new Form(T.mapFishingSettings);
      final ChoiceGroup cgRed = new ChoiceGroup(T.utilMineOptions, ChoiceGroup.EXCLUSIVE);
      cgRed.append(T.fishAutoDropKcx, null);
      cgRed.append(T.fishKeepKcx, null);
      cgRed.setSelectedIndex(fishingRedFieldEnabled ? 0 : 1, true);
      final ChoiceGroup cgFish = new ChoiceGroup(T.fishSellList, ChoiceGroup.MULTIPLE);
      for (int i = 0; i < T.fishNames.length; i++) {
         cgFish.append(T.fishNames[i], null);
      }
      for (int i = 0; i < fishingSellFishSelected.length && i < 10; i++) {
         cgFish.setSelectedIndex(i, fishingSellFishSelected[i]);
      }
      final ChoiceGroup cgTicket = new ChoiceGroup(T.fishBuyTicket, ChoiceGroup.EXCLUSIVE);
      cgTicket.append(T.fishAutoBuyTicketOpt, null);
      cgTicket.append(T.fishNoBuyTicket, null);
      cgTicket.setSelectedIndex(fishingAutoBuyTicket ? 0 : 1, true);
      final ChoiceGroup cgTicketType = new ChoiceGroup(T.fishTicketType, ChoiceGroup.EXCLUSIVE);
      for (int i = 0; i < T.fishTicketTypes.length; i++) {
         cgTicketType.append(T.fishTicketTypes[i], null);
      }
      if (fishingSelectedTicket < 0 || fishingSelectedTicket > 2) fishingSelectedTicket = 0;
      cgTicketType.setSelectedIndex(fishingSelectedTicket, true);
      final ChoiceGroup cgRod = new ChoiceGroup(T.fishBuyRod, ChoiceGroup.EXCLUSIVE);
      cgRod.append(T.fishAutoBuyRodOpt, null);
      cgRod.append(T.fishNoBuyRod, null);
      cgRod.setSelectedIndex(fishingAutoBuyRod ? 0 : 1, true);
      final ChoiceGroup cgRodType = new ChoiceGroup(T.fishRodType, ChoiceGroup.EXCLUSIVE);
      for (int i = 0; i < T.fishRodTypes.length; i++) {
         cgRodType.append(T.fishRodTypes[i], null);
      }
      if (fishingSelectedRod < 0 || fishingSelectedRod > 2) fishingSelectedRod = 0;
      cgRodType.setSelectedIndex(fishingSelectedRod, true);
      form.append(cgRed);
      form.append(cgFish);
      form.append(cgTicket);
      form.append(cgTicketType);
      form.append(cgRod);
      form.append(cgRodType);
      final ChoiceGroup cgAutoStone;
      final ChoiceGroup cgSellStone;
      if (showAutoStone) {
         cgAutoStone = new ChoiceGroup(T.fishAutoStoneGroup, ChoiceGroup.EXCLUSIVE);
         cgAutoStone.append(T.fishEnableAutoStone, null);
         cgAutoStone.append(T.fishDisableAutoStone, null);
         cgAutoStone.setSelectedIndex(autoStoneEnabled ? 0 : 1, true);
         cgSellStone = new ChoiceGroup(T.fishSellStoneGroup, ChoiceGroup.EXCLUSIVE);
         cgSellStone.append(T.fishEnableSellStone, null);
         cgSellStone.append(T.fishDisableSellStone, null);
         cgSellStone.setSelectedIndex(fishingAutoSellStone ? 0 : 1, true);
         form.append(cgAutoStone);
         form.append(cgSellStone);
      } else {
         cgAutoStone = null;
         cgSellStone = null;
      }
      final javax.microedition.lcdui.Command cmdSave = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
      final javax.microedition.lcdui.Command cmdCancel = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      form.addCommand(cmdSave);
      form.addCommand(cmdCancel);
      form.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
            if (c == cmdSave) {
               fishingRedFieldEnabled = cgRed.getSelectedIndex() == 0;
               for (int i = 0; i < fishingSellFishSelected.length && i < 10; i++) {
                  fishingSellFishSelected[i] = cgFish.isSelected(i);
               }
               fishingAutoBuyTicket = cgTicket.getSelectedIndex() == 0;
               fishingSelectedTicket = cgTicketType.getSelectedIndex();
               fishingAutoBuyRod = cgRod.getSelectedIndex() == 0;
               fishingSelectedRod = cgRodType.getSelectedIndex();
               if (showAutoStone) {
                  autoStoneEnabled = cgAutoStone.getSelectedIndex() == 0;
                  fishingAutoSellStone = cgSellStone.getSelectedIndex() == 0;
               }
               persistFishingSettings();
               Canvas.addServerInfo(T.fishSettingsSaved);
            }
            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            ClientUtilities.openFishingSettingsSubmenu();
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(form);
   }

   static void toggleFishingRedField() {
      fishingRedFieldEnabled = !fishingRedFieldEnabled;
      persistFishingSettings();
      Canvas.addServerInfo(T.fishRedFieldToggle + boolCap(fishingRedFieldEnabled));
   }

   static void resetFishingCounter() {
      fishingCounter = 0;
      fishingSoldCounter = 0;
      try {
         fishingCaughtById.clear();
         fishingSoldById.clear();
         fishingFishIdOrder.removeAllElements();
      } catch (Throwable t) {
      }
      persistFishingSettings();
      Canvas.addServerInfo(T.fishCounterResetMsg);
   }

   private static void incFishCount(Hashtable table, Vector order, short idFish) {
      try {
         Integer key = new Integer(idFish);
         Integer cur = (Integer)table.get(key);
         int next = (cur == null ? 0 : cur.intValue()) + 1;
         table.put(key, new Integer(next));
         boolean seen = false;
         for (int i = 0; i < order.size(); i++) {
            Integer v = (Integer)order.elementAt(i);
            if (v != null && v.intValue() == idFish) {
               seen = true;
               break;
            }
         }
         if (!seen) {
            order.addElement(key);
         }
      } catch (Throwable t) {
      }
   }

   private static int getFishCount(Hashtable table, short idFish) {
      try {
         Integer v = (Integer)table.get(new Integer(idFish));
         return v == null ? 0 : v.intValue();
      } catch (Throwable t) {
         return 0;
      }
   }

   private static String fishDisplayName(short idFish) {
      try {
         Part p = AvatarData.getPart(idFish);
         if (p != null && p.IDPart != -1) {
            String n = AvatarData.getName(p);
            if (n != null && n.trim().length() > 0) {
               return n.trim();
            }
         }
      } catch (Throwable t) {
      }
      // fallback cho các id cá phổ biến
      int idx = fishSellIndexFromId(idFish);
      if (idx >= 0) {
         if (idx < T.fishNames.length) return T.fishNames[idx];
      }
      return "ID " + idFish;
   }

   private static int getFishingTicketId() {
      switch (fishingSelectedTicket) {
         case 0: return 458; // Vé câu cá rô
         case 1: return 459; // Vé câu cá lóc
         case 2: return 460; // Vé câu cá mập
         default: return 458;
      }
   }

   private static int getFishingRodId() {
      switch (fishingSelectedRod) {
         case 0: return 442; // Cần câu tre
         case 1: return 445; // Cần câu sắt
         case 2: return 446; 
         default: return 442;
      }
   }

   static void onFishingCaught(short idFish) {
      ++fishingCounter;
      incFishCount(fishingCaughtById, fishingFishIdOrder, idFish);
      persistFishingSettings();
      if (LoadMap.TYPEMAP == 14 || LoadMap.TYPEMAP == 15 || LoadMap.TYPEMAP == 16) {
         onFishingSold(idFish);
         sellFishWithFallback();
      }
   }

   static void onFishingSold(short idFish) {
      ++fishingSoldCounter;
      incFishCount(fishingSoldById, fishingFishIdOrder, idFish);
      persistFishingSettings();
   }

   private static int fishSellIndexFromId(short idFish) {
      
      switch (idFish) {
         
         case 444: return 1; // cá rô
         case 449: return 2; // cá lòng tong
         case 450: return 0; // cá chép vàng
         case 451: return 3; // cá lóc
         case 452: return 4; // cá nóc
         case 453: return 5; // cua
         case 454: return 9; // cá chim
         case 455: return 7; // cá đuối
         case 456: return 8; // cá ngựa
         case 457: return 6; // cá mập

         case 775: return 0; // cá chép vàng
         case 774: return 1; // cá rô
         case 773: return 2; // cá lòng tong
         case 776: return 3; // cá lóc
         case 777: return 4; // cá nóc
         case 778: return 5; // cua
         case 782: return 6; // cá mập
         case 780: return 7; // cá đuối
         case 781: return 8; // cá ngựa
         case 779: return 9; // cá chim
         default: return -1;
      }
   }

   static boolean shouldAutoSellFish(short idFish) {
      int idx = fishSellIndexFromId(idFish);
      if (idx < 0) return false; // không nhận diện được thì an toàn: không bán
      if (fishingSellFishSelected == null) return true;
      if (idx >= fishingSellFishSelected.length) return true;
      return fishingSellFishSelected[idx];
   }

   static short fishVisualPartId(short idFish) {
      // Một số server trả về itemId (444..457). Cần map sang partId (773..782) để lấy đúng icon.
      switch (idFish) {
         case 444: return 774; // cá rô
         case 449: return 773; // cá lòng tong
         case 450: return 775; // cá chép vàng
         case 451: return 776; // cá lóc
         case 452: return 777; // cá nóc
         case 453: return 778; // cua
         case 454: return 779; // cá chim
         case 455: return 780; // cá đuối
         case 456: return 781; // cá ngựa
         case 457: return 782; // cá mập
         default: return idFish;
      }
   }

   static void sellFishWithFallback() {
      try {
         if (LoadMap.TYPEMAP == 14 || LoadMap.TYPEMAP == 15 || LoadMap.TYPEMAP == 16) {
            Avatar npc = findFishingNpcOnCurrentMap();
            if (npc != null) {
               GlobalService.gI().doMenuOption(npc.IDDB, (byte)0, 1);
               return;
            }
            
            GlobalService.gI().doMenuOption(2000000002, (byte)0, 1);
            return;
         }

         Avatar npc = findFishingNpcOnCurrentMap();
         if (npc != null) {
            GlobalService.gI().doMenuOption(npc.IDDB, (byte)0, 1);
         }
      } catch (Throwable t) {
      }
   }

  
   static void rememberLastNpcMenuAction(int menuInt, byte menuByte, int menuIdx) {
   }

   public static void paintFishingCounterOverlay(Graphics g) {
      if (LoadMap.TYPEMAP != 14 && LoadMap.TYPEMAP != 15 && LoadMap.TYPEMAP != 16) {
         return;
      }

      Canvas.resetTrans(g);
      int y = 30;
      int dy = AvMain.hBorder + 2;
    
      Canvas.borderFont.drawString(g, T.fishCaughtCountPrefix + fishingCounter, Canvas.hw, y, 2);
      Canvas.borderFont.drawString(g, T.fishSoldCountPrefix + fishingSoldCounter, Canvas.hw, y + dy, 2);

      int x = Canvas.w - 2; 
      int line = 0;
      for (int i = 0; i < fishingFishIdOrder.size(); i++) {
         Integer v = (Integer)fishingFishIdOrder.elementAt(i);
         if (v == null) continue;
         short id = (short)v.intValue();
         int c = getFishCount(fishingCaughtById, id);
         int s = getFishCount(fishingSoldById, id);
         if (c == 0 && s == 0) continue;
         String text = fishDisplayName(id) + ": " + c;
         if (s > 0 && s != c) {
            text += " (bán " + s + ")";
         }
         Canvas.borderFont.drawString(g, text, x, y + dy * line, 1);
         line++;
      }
   }

   private static void loadPersistedAutoGiftSettings() {
      String s = CRes.loadString(RMS_AUTO_GIFT);
      if (s == null) {
         return;
      }

      String[] lines = splitLines4(s);
      if (lines == null) {
         return;
      }

      try {
         autoGiftGiftId = Integer.parseInt(lines[0].trim());
      } catch (Exception var2) {
      }

      try {
         int limit = Integer.parseInt(lines[1].trim());
         if (limit < 0) {
            limit = 0;
         }

         autoGiftLimit = limit;
      } catch (Exception var2) {
      }

      try {
         int ms = Integer.parseInt(lines[2].trim());
         if (ms < 1) {
            ms = 1;
         } else if (ms > 600000) {
            ms = 600000;
         }

         autoGiftIntervalMs = ms;
      } catch (Exception var2) {
      }

      try {
         int pay = Integer.parseInt(lines[3].trim());
         if (pay == 1 || pay == 2) {
            autoGiftPayBy = pay;
         }
      } catch (Exception var2) {
      }
   }

   private static String[] splitLines4(String s) {
      if (s == null) {
         return null;
      }

      int p1 = s.indexOf(10);
      if (p1 < 0) return null;
      int p2 = s.indexOf(10, p1 + 1);
      if (p2 < 0) return null;
      int p3 = s.indexOf(10, p2 + 1);
      if (p3 < 0) return null;
      return new String[]{s.substring(0, p1), s.substring(p1 + 1, p2), s.substring(p2 + 1, p3), s.substring(p3 + 1)};
   }

   private static String[] splitLines(String s) {
      if (s == null) {
         return null;
      }

      Vector v = new Vector();
      int start = 0;

      while (true) {
         int p = s.indexOf(10, start);
         if (p < 0) {
            String tail = s.substring(start);
            if (tail.length() > 0) {
               v.addElement(tail);
            }
            break;
         }

         String line = s.substring(start, p);
         if (line.length() > 0) {
            v.addElement(line);
         }
         start = p + 1;
      }

      String[] out = new String[v.size()];
      for (int i = 0; i < out.length; i++) {
         out[i] = (String) v.elementAt(i);
      }
      return out;
   }

   public static void persistUtilSpeed() {
      try {
         CRes.saveRMS(RMS_UTIL_SPEED, new byte[]{(byte)utilSpeed1to100});
      } catch (Throwable var1) {
      }
   }

   public static void openSpeedInputDialog() {
      Canvas.inputDlg.setImg(T.utilSpeedInputPrompt + "\n" + T.utilSpeedCurrentPrefix + ClientUtilities.utilSpeed1to100, new IActionUtilitySpeedOk(), 1, String.valueOf(ClientUtilities.utilSpeed1to100));
   }

   public static void openSpeedSubmenu() {
      Vector v = new Vector();
      v.addElement(new Command(T.option, new IActionUtilitySpeedSub((byte)0)));
      v.addElement(new Command(T.utilSpeedResetBtn, new IActionUtilitySpeedSub((byte)1)));
      Menu.gI().startAt(v, -1);
      Menu.h = null;
   }

   public static void resetSpeedToDefault() {
      utilSpeed1to100 = 50;
      persistUtilSpeed();
      resetAutoTickCounter();
      Canvas.startOKDlg(T.utilSpeedResetMsg);
      openUtilitySubmenu();
   }

   public static int computeAutoPeriodTicks() {
      int s = utilSpeed1to100;
      if (s < 1) {
         s = 1;
      } else if (s > 100) {
         s = 100;
      }

      return 2 + (s - 1) * 298 / 99;
   }

   public static int getFrameTargetMs() {
      int s = utilSpeed1to100;
      if (s < 1) {
         s = 1;
      } else if (s > 100) {
         s = 100;
      }

      return 3 + (s - 1) * 97 / 99;
   }

   public static void resetAutoChatSchedule() {
      autoChatPartIndex = 0;
      autoChatNextSendAtMs = 0L;
   }

   public static void resetAutoTickCounter() {
      autoTickCounter = 0;
      resetAutoChatSchedule();
   }

   public static void resetAutoKissHitTickOnly() {
      autoTickCounter = 0;
   }

   public static void openAutoKissConfirmDialog() {
      Vector v = new Vector();
      if (autoKiss) {
         v.addElement(new Command(T.utilStateOff, new IActionAutoKissTurnOffYes()));
         v.addElement(new Command(T.no, new IActionAutoKissConfirmNo()));
         Canvas.setInfoC(T.utilAutoKissAskOff, v);
      } else {
         v.addElement(new Command(T.utilStateOn, new IActionAutoKissTurnOnYes()));
         v.addElement(new Command(T.no, new IActionAutoKissConfirmNo()));
         Canvas.setInfoC(T.utilAutoKissAskOn, v);
      }
   }

   private static Vector autoChatSegmentsFromRaw(String raw) {
      Vector out = new Vector();
      if (raw == null) {
         return out;
      }

      raw = raw.trim();
      if (raw.length() == 0) {
         return out;
      }

      int start = 0;

      for (int i = 0; i <= raw.length(); ++i) {
         if (i == raw.length() || raw.charAt(i) == '/') {
            if (i > start) {
               String seg = raw.substring(start, i).trim();
               if (seg.length() > 0) {
                  out.addElement(seg);
               }
            }

            start = i + 1;
         }
      }

      return out;
   }

   private static String expandAutoChatToken(String s) {
      if (s == null) {
         return "";
      }

      String t = s.trim();
      if (t.equalsIgnoreCase("iuem")) {
         return "iu em";
      }

      if (t.equalsIgnoreCase("nvl")) {
         return "vl";
      }

      return t;
   }

   private static int clampAutoChatSeconds(int sec) {
      if (sec < 1) {
         return 1;
      }

      return sec > 120 ? 120 : sec;
   }

   public static void resetAutoClickSequence() {
      autoClickInjectAwaitRelease = false;
      autoClickNextAtMs = 0L;
   }

   public static void resetAutoTrollSchedule() {
      autoTrollCount = 0;
      autoTrollNextAtMs = 0L;
   }

   public static void toggleAutoClickEnabled() {
      autoClickEnabled = !autoClickEnabled;
      if (autoClickEnabled) {
         resetAutoClickSequence();
      } else {
         stopAutoClickInject();
      }

      if (autoClickShowToggleNotice) {
         String msg = autoClickEnabled ? T.utilAutoClickEnabledNotice : T.utilAutoClickDisabledNotice;
         Canvas.addServerInfo(msg);
      }
   }

   public static boolean handleAutoClickStartHotKey(int keyCode) {
      if (keyCode != autoClickStartKey) {
         return false;
      }

      toggleAutoClickEnabled();
      return true;
   }

   public static void stopAutoClickInject() {
      if (autoClickInjectAwaitRelease && Canvas.instance != null) {
         try {
            Canvas.instance.keyReleased(autoClickKeyCode);
         } catch (Throwable var1) {
         }
      }

      autoClickInjectAwaitRelease = false;
      autoClickNextAtMs = 0L;
   }

   public static void onAutoClickMapTick() {
      if (!autoClickEnabled) {
         if (autoClickInjectAwaitRelease) {
            stopAutoClickInject();
         }

         return;
      }

      if (Canvas.instance == null) {
         return;
      }

      if (Canvas.load != -1) {
       
         if (autoClickInjectAwaitRelease) {
            try {
               Canvas.instance.keyReleased(autoClickKeyCode);
            } catch (Throwable t) {
            }
            autoClickInjectAwaitRelease = false;
         }
         return;
      }

      if (ChatTextField.isShow) {
         if (autoClickInjectAwaitRelease) {
            try {
               Canvas.instance.keyReleased(autoClickKeyCode);
            } catch (Throwable var4) {
            }

            autoClickInjectAwaitRelease = false;
         }

         return;
      }

      if (!Session_ME.gI().isConnected()) {
         return;
      }

      if (GameMidlet.avatar != null) {
         if (GameMidlet.avatar.task != 0 && GameMidlet.avatar.task != -5) {
            if (autoClickInjectAwaitRelease) {
               try {
                  Canvas.instance.keyReleased(autoClickKeyCode);
               } catch (Throwable var5) {
               }

               autoClickInjectAwaitRelease = false;
            }

            return;
         }
      }

      if (Bus.isRun) {
         if (autoClickInjectAwaitRelease) {
            try {
               Canvas.instance.keyReleased(autoClickKeyCode);
            } catch (Throwable var6) {
            }

            autoClickInjectAwaitRelease = false;
         }

         return;
      }

      int k = autoClickKeyCode;
      int gap = autoClickIntervalMs;
      if (gap < 1) {
         gap = 1;
      } else if (gap > 600000) {
         gap = 600000;
      }

      if (autoClickInjectAwaitRelease) {
         try {
            Canvas.instance.keyReleased(k);
         } catch (Throwable var2) {
         }

         autoClickInjectAwaitRelease = false;
         autoClickNextAtMs = System.currentTimeMillis() + (long)gap;
         return;
      }

      long now = System.currentTimeMillis();
      if (now < autoClickNextAtMs) {
         return;
      }

      try {
         Canvas.instance.keyPressed(k);
      } catch (Throwable var3) {
      }

      autoClickInjectAwaitRelease = true;
   }

   public static void onMapUpdateTick() {
      if (!Session_ME.gI().isConnected()) {
         return;
      }
      try {
         DialLuckyScr.gI().tickAutoDialBackground();
      } catch (Throwable t) {
      }
      checkAutoStonePendingPurchase();
      maybeShowMap13GuideTab();
      closeAutoTrollUiIfNeeded();
      if (Canvas.menuMain != null && !fishingAutoBaitPendingBuy) {
         closeAutoTrollUiIfNeeded();
         if (Canvas.menuMain != null) {
            return;
         }
      }
      boolean avatarTaskBlocksMapAutos = GameMidlet.avatar != null
            && GameMidlet.avatar.task != 0
            && GameMidlet.avatar.task != -5;
      if (avatarTaskBlocksMapAutos) {
         return;
      }
      if (Bus.isRun) {
         return;
      }
      if (autoChat) {
         long now = System.currentTimeMillis();
         if (now >= autoChatNextSendAtMs) {
            int sec = clampAutoChatSeconds(autoChatIntervalSeconds);
            autoChatNextSendAtMs = now + (long)sec * 1000L;
            Vector parts = autoChatSegmentsFromRaw(autoChatMessage);
            String msg;
            if (parts.size() == 0) {
               msg = ":)";
            } else {
               int idx = autoChatPartIndex % parts.size();
               msg = expandAutoChatToken((String)parts.elementAt(idx));
               if (msg.length() > 120) {
                  msg = msg.substring(0, 120);
               }

               ++autoChatPartIndex;
            }

            ParkService.gI().chatToBoard(msg);
         }
      }

      if (autoTroll) {
         if (autoTrollLimit > 0 && autoTrollCount >= autoTrollLimit) {
            autoTroll = false;
         } else {
            long now = System.currentTimeMillis();
            if (now >= autoTrollNextAtMs) {
               int gap = autoTrollIntervalMs;
               if (gap < 1) {
                  gap = 1;
               } else if (gap > 600000) {
                  gap = 600000;
               }

               autoTrollNextAtMs = now + (long)gap;
               byte a;
               if (autoTrollCode == -1) {
                  a = (byte)(1 + (Canvas.gameTick / 8 & 3));
               } else {
                  a = (byte)autoTrollCode;
               }
               closeAutoTrollUiIfNeeded();
               MapScr.doAction(a);
               ++autoTrollCount;
            }
         }
      }

      if (autoStoneEnabled && (autoStonePickaxeXu || autoStonePickaxeGold || autoStoneShovelXu || autoStoneShovelGold)) {
         long now = System.currentTimeMillis();
         if (now >= autoStoneNextAtMs) {
            int gap = autoStoneIntervalMs;
            if (gap < 1) {
               gap = 1;
            } else if (gap > 600000) {
               gap = 600000;
            }

            autoStoneNextAtMs = now + (long)gap;
            GlobalService.gI().doRequestCmdRotate(1000, -1);
            autoStoneSellByMenuOption();
         }
      }

      if (autoNpcEnabled) {
         autoNpcTick();
      }

      autoFishingBaitTick();

      if (!autoKiss && !autoHit && !autoGift && !autoTroll) {
         return;
      }

      int period = ClientUtilities.computeAutoPeriodTicks();
      if (period < 2) {
         period = 2;
      }

      ++autoTickCounter;
      if (autoTickCounter < period) {
         return;
      }

      autoTickCounter = 0;

      if (autoHit) {
         autoHitAllPlayersInZone();
      }

      if (MapScr.focusP == null) {
         return;
      }

      if (autoKiss) {
         MapScr.doKiss();
      }

      if (autoGift) {
         MapScr.doGivingDefferent(102);
      }

   }

   private static void autoHitAllPlayersInZone() {
      Vector list = LoadMap.playerLists;
      if (list == null || list.size() == 0) {
         return;
      }

      Avatar oldFocus = MapScr.focusP;

      for(int i = 0; i < list.size(); ++i) {
         MyObject obj = (MyObject)list.elementAt(i);
         if (obj != null && obj.catagory == 0) {
            Avatar target = (Avatar)obj;
            if (target != null && target != GameMidlet.avatar && target.IDDB != GameMidlet.avatar.IDDB && target.task == 0) {
               MapScr.focusP = target;
               MapScr.gI().doHit();
            }
         }
      }

      MapScr.focusP = oldFocus;
   }

   private static void autoFishingBaitTick() {
      long now = System.currentTimeMillis();
      if (now < fishingAutoBaitNextAtMs) {
         return;
      }
      if (!fishingAutoBaitRequested && !fishingAutoBaitPendingBuy && !fishingAutoBaitNeedReturnMove) {
         fishingAutoBaitNextAtMs = now + 1000L;
         return;
      }

      int map = LoadMap.TYPEMAP;
      if (map != 13 && map != 14 && map != 15 && map != 16) {
         fishingAutoBaitPendingBuy = false;
         fishingAutoBaitBuyStep = 0;
         fishingAutoBaitMoveDownRemain = 0;
         fishingAutoBaitEnterRemain = 0;
         fishingAutoBaitReturnMap = -1;
         fishingAutoBaitRequested = false;
         fishingAutoBaitReturnX = -1;
         fishingAutoBaitReturnY = -1;
         fishingAutoBaitNeedReturnMove = false;
         fishingAutoBaitNeedResumeSit = false;
         fishingAutoBaitSitX = -1;
         fishingAutoBaitSitY = -1;
         fishingAutoBaitMoveTargetX = -1;
         fishingAutoBaitMoveTargetY = -1;
         fishingAutoBaitForceExitTries = 0;
         fishingAutoBaitDirectBuyRemain = 0;
         fishingAutoBaitNextAtMs = now + 2000L;
         return;
      }

      if (map == 14 || map == 15 || map == 16) {
         if (fishingAutoBaitRequested && Canvas.currentMyScreen == FishingScr.gI()) {
            forceExitFishingState();
            if (fishingAutoBaitForceExitTries < 20) {
               ++fishingAutoBaitForceExitTries;
            }
            fishingAutoBaitNextAtMs = now + 250L;
            return;
         }
         if (fishingAutoBaitNeedReturnMove && fishingAutoBaitReturnX >= 0 && fishingAutoBaitReturnY >= 0) {
            GameMidlet.avatar.posFocus = new AvPosition(fishingAutoBaitReturnX, fishingAutoBaitReturnY);
            GameMidlet.avatar.moveToFocus();
            MapScr.doMove(fishingAutoBaitReturnX, fishingAutoBaitReturnY, GameMidlet.avatar.direct, GameMidlet.avatar.direct_);
            fishingAutoBaitNeedReturnMove = false;
            fishingAutoBaitNeedResumeSit = true;
            fishingAutoBaitNextAtMs = now + 1500L;
            return;
         }
         if (fishingAutoBaitNeedResumeSit) {
            if (GameMidlet.avatar.task == 0) {
               int sitX = fishingAutoBaitSitX >= 0 ? fishingAutoBaitSitX : GameMidlet.avatar.x;
               int sitY = fishingAutoBaitSitY >= 0 ? fishingAutoBaitSitY : GameMidlet.avatar.y;
               FishingScr.gI().doSat(sitX, sitY);
               fishingAutoBaitNeedResumeSit = false;
               fishingAutoBaitSitX = -1;
               fishingAutoBaitSitY = -1;
               fishingAutoBaitNextAtMs = now + 1200L;
            } else {
               fishingAutoBaitNextAtMs = now + 400L;
            }
            return;
         }
         if (GameMidlet.avatar.action == 2 || GameMidlet.avatar.action == 13) {
            forceExitFishingState();
            if (fishingAutoBaitForceExitTries < 20) {
               ++fishingAutoBaitForceExitTries;
            }
            fishingAutoBaitNextAtMs = now + 250L;
            return;
         }
         fishingAutoBaitForceExitTries = 0;
         fishingAutoBaitReturnMap = (byte)map;
         AvPosition sitPos = getDefaultFishSitPos(map);
         if (sitPos != null) {
            fishingAutoBaitSitX = sitPos.x;
            fishingAutoBaitSitY = sitPos.y;
            fishingAutoBaitReturnX = sitPos.x;
            fishingAutoBaitReturnY = sitPos.y;
         } else {
            fishingAutoBaitReturnX = GameMidlet.avatar.x;
            fishingAutoBaitReturnY = GameMidlet.avatar.y;
            fishingAutoBaitSitX = GameMidlet.avatar.x;
            fishingAutoBaitSitY = GameMidlet.avatar.y;
         }
         ParkService.gI().doJoinPark((byte)13, -1);
         fishingAutoBaitNextAtMs = now + 4000L;
         return;
      }

      // Xử lý mua mồi trực tiếp trên map 13
      if (map == 13 && fishingAutoBuyBaitPending) {
         if (GameMidlet.avatar.x != 286 || GameMidlet.avatar.y != 48) {
            GameMidlet.avatar.posFocus = new AvPosition(286, 48);
            GameMidlet.avatar.moveToFocus();
            MapScr.doMove(286, 48, 2, (short)0);
            fishingAutoBaitNextAtMs = now + 2000L;
            return;
         }
         // Đã đến vị trí, mua mồi
         AvatarService.gI().doBuyItem(448, 1);
         fishingAutoBuyBaitPending = false;
         fishingAutoBaitRequested = false;
         fishingAutoBaitNextAtMs = now + 1500L;
         // Quay về map 16
         fishingAutoBaitReturnX = 924;
         fishingAutoBaitReturnY = 119;
         fishingAutoBaitNeedReturnMove = true;
         ParkService.gI().doJoinPark(16, -1);
         return;
      }

      Avatar npc = findFishingNpcOnCurrentMap();
      if (npc == null) {
         fishingAutoBaitNextAtMs = now + 1200L;
         return;
      }

      // Kiểm tra nếu có request mua vé/cần từ dialog
      boolean hasBuyRequest = fishingAutoBuyTicketRequested || fishingAutoBuyRodRequested || fishingAutoBaitRequested;
      if (!fishingAutoBaitPendingBuy && hasBuyRequest) {
         fishingAutoBaitPendingBuy = true;
         fishingAutoBaitBuyStep = 0;
         fishingAutoBaitMoveDownRemain = 1;
         fishingAutoBaitEnterRemain = 30;
         fishingAutoBaitDirectBuyRemain = 30;
         fishingAutoBuyTicketStep = 0;
         fishingAutoBuyRodStep = 0;
         fishingAutoBuyPending = true;
         fishingAutoBaitNextAtMs = now + 900L;
         return;
      }

      if (!fishingAutoBaitPendingBuy) {
         fishingAutoBaitNextAtMs = now + 1000L;
         return;
      }

      // Xử lý mua vé, cần, mồi theo thứ tự
      if (fishingAutoBuyPending) {
         // Mua vé trước
         if (fishingAutoBuyTicket && fishingAutoBuyTicketStep == 0) {
            AvatarService.gI().doBuyItem(getFishingTicketId(), 1);
            Canvas.startWaitDlg();
            fishingAutoBuyTicketStep = 1;
            fishingAutoBaitNextAtMs = now + 300L;
            return;
         }
         if (fishingAutoBuyTicket && fishingAutoBuyTicketStep == 1) {
            GlobalService.gI().doMenuOption(npc.IDDB, (byte)0, 3);
            fishingAutoBuyTicketStep = 2;
            fishingAutoBaitNextAtMs = now + 300L;
            return;
         }

         // Mua cần
         if (fishingAutoBuyRod && fishingAutoBuyRodStep == 0) {
            AvatarService.gI().doBuyItem(getFishingRodId(), 1);
            Canvas.startWaitDlg();
            fishingAutoBuyRodStep = 1;
            fishingAutoBaitNextAtMs = now + 300L;
            return;
         }
         if (fishingAutoBuyRod && fishingAutoBuyRodStep == 1) {
            GlobalService.gI().doMenuOption(npc.IDDB, (byte)0, 3);
            fishingAutoBuyRodStep = 2;
            fishingAutoBaitNextAtMs = now + 300L;
            return;
         }

         // Mua mồi
         if (fishingAutoBuyBait && fishingAutoBaitDirectBuyRemain > 0) {
            AvatarService.gI().doBuyItem(448, 1);
            GlobalService.gI().doMenuOption(npc.IDDB, (byte)0, 3);
            Canvas.startWaitDlg();
            --fishingAutoBaitDirectBuyRemain;
            fishingAutoBaitNextAtMs = now + 170L;
            return;
         }

         // Hoàn thành mua
         fishingAutoBuyPending = false;
         fishingAutoBuyTicketRequested = false;
         fishingAutoBuyRodRequested = false;
      }

      fishingAutoBaitPendingBuy = false;
      fishingAutoBaitBuyStep = 0;
      fishingAutoBaitEnterRemain = 0;
      fishingAutoBaitRequested = false;
      fishingAutoBaitNextAtMs = now + 10000L;
      if (fishingAutoBaitReturnMap == 14 || fishingAutoBaitReturnMap == 15 || fishingAutoBaitReturnMap == 16) {
         ParkService.gI().doJoinPark(fishingAutoBaitReturnMap, -1);
         fishingAutoBaitNeedReturnMove = true;
         fishingAutoBaitReturnMap = -1;
         fishingAutoBaitNextAtMs = now + 4000L;
      }
   }

   private static AvPosition getDefaultFishSitPos(int mapId) {
      if (mapId == 16) {
         return new AvPosition(228, 167);
      }
      if (mapId == 15) {
         return new AvPosition(84, 119);
      }
      if (mapId == 14) {
         return new AvPosition(396, 167);
      }
      return null;
   }

   private static void pressGameKey(int keyCode) {
      try {
         Canvas.instance.keyPressed(keyCode);
      } catch (Throwable t) {
      }
      try {
         Canvas.instance.keyReleased(keyCode);
      } catch (Throwable t) {
      }
   }

   private static void closeAutoTrollUiIfNeeded() {
      if (!autoTroll) {
         return;
      }

      long now = System.currentTimeMillis();
      if (now >= autoTrollDebugNextLogAtMs) {
         autoTrollDebugNextLogAtMs = now + 800L;
      }

      try {
         if (Canvas.currentDialog != null) {
            Canvas.endDlg();
         }
      } catch (Throwable t) {
      }

      try {
         if (Canvas.menuMain != null) {
            Canvas.menuMain = null;
         }
      } catch (Throwable t) {
      }

      try {
         if (Display.getDisplay(GameMidlet.instance).getCurrent() != Canvas.instance) {
            Canvas.instance.setFullScreenMode(true);
            Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
         }
      } catch (Throwable t) {
      }

      try {
         if (Canvas.currentMyScreen != MapScr.gI()) {
            MapScr.gI().switchToMe();
         }
      } catch (Throwable t) {
      }
   }

   private static Avatar findFishingNpcOnCurrentMap() {
      try {
         Vector list = LoadMap.playerLists;
         for (int i = 0; i < list.size(); i++) {
            Object o = list.elementAt(i);
            if (!(o instanceof Avatar)) continue;
            Avatar a = (Avatar)o;
            if (a == null) continue;
            if (a.IDDB < 2000000000) continue;
            String n = safe(a.name).toLowerCase();
            String nn = normalizeForNpcMatch(n);
            if (n.indexOf("tho.cau") >= 0 || n.indexOf("tho cau") >= 0 || nn.indexOf("thocau") >= 0) {
               return a;
            }
         }
      } catch (Exception e) {
      }
      return null;
   }

   private static Avatar findAnyNpcOnCurrentMap() {
      try {
         Vector list = LoadMap.playerLists;
         for (int i = 0; i < list.size(); i++) {
            Object o = list.elementAt(i);
            if (!(o instanceof Avatar)) continue;
            Avatar a = (Avatar)o;
            if (a == null) continue;
            if (a.IDDB >= 2000000000) {
               return a;
            }
         }
      } catch (Exception e) {
      }
      return null;
   }

   private static void autoNpcTick() {
      long now = System.currentTimeMillis();
      if (autoNpcNextHopAtMs == 0L) {
         autoNpcNextHopAtMs = now + (long)clampMs(eventChangeZoneIntervalMs);
      }

      
      Avatar found = findNpcOnCurrentMapByName();
      if (found != null) {
         int id = found.IDDB;
         if (id != autoNpcLastFoundId) {
            autoNpcLastFoundId = id;
            autoNpcFoundNotified = false;
            autoNpcNextMoveToFoundAtMs = 0L;
            autoNpcNextAttackAtMs = 0L;
            autoNpcLastAttackNpcId = -1;
         }

         if (!autoNpcFoundNotified) {
            autoNpcFoundNotified = true;
            Canvas.addServerInfo(T.npcFoundPrefix + safe(found.name));
         }

         moveToFoundNpc(found, now);
         doAutoNpcAttack(found, now);
         return;
      } else {
         autoNpcLastFoundId = -1;
         autoNpcFoundNotified = false;
         autoNpcLastAttackNpcId = -1;
      }

      if (now < autoNpcNextHopAtMs) {
         return;
      }

      autoNpcNextHopAtMs = now + (long)clampMs(eventChangeZoneIntervalMs);

      if (eventScopeAllMaps) {
         byte nextMap = AUTO_NPC_MAP_ROTATION[autoNpcMapRotateIdx % AUTO_NPC_MAP_ROTATION.length];
         autoNpcMapRotateIdx = (autoNpcMapRotateIdx + 1) % AUTO_NPC_MAP_ROTATION.length;
         ParkService.gI().doJoinPark(nextMap, -1);
         return;
      }

      
      int max = MapScr.zoneMaxIndex;
      int nextBoard;
      if (max < 0) {
        
         ParkService.gI().doRequestBoardList(MapScr.roomID);
         return;
      }

      if (max == 0) {
         return;
      }

      int cur = MapScr.boardID & 255;
      nextBoard = (cur + 1) % (max + 1);
      ParkService.gI().doJoinPark(MapScr.roomID, nextBoard);
   }

   private static void moveToFoundNpc(Avatar npc, long now) {
      if (npc == null || GameMidlet.avatar == null) {
         return;
      }

      if (now < autoNpcNextMoveToFoundAtMs) {
         return;
      }
      autoNpcNextMoveToFoundAtMs = now + 700L;

      int dx = Math.abs(GameMidlet.avatar.x - npc.x);
      int dy = Math.abs(GameMidlet.avatar.y - npc.y);
      if (dx <= LoadMap.w && dy <= LoadMap.w) {
         return;
      }

      int targetX = npc.x;
      int targetY = npc.y;
      if (GameMidlet.avatar.x < npc.x) {
         targetX = npc.x - LoadMap.w;
      } else if (GameMidlet.avatar.x > npc.x) {
         targetX = npc.x + LoadMap.w;
      }
      
      
      GameMidlet.avatar.posFocus = new AvPosition(targetX, targetY);
      GameMidlet.avatar.moveToFocus();
      MapScr.doMove(targetX, targetY, GameMidlet.avatar.direct, GameMidlet.avatar.direct_);
   }

   private static void doAutoNpcAttack(Avatar npc, long now) {
      if (!autoNpcAttackEnabled || npc == null) {
         return;
      }

      // Don't inject actions while user is interacting with UI.
      if (Canvas.menuMain != null || Canvas.currentDialog != null || ChatTextField.isShow || Canvas.load != -1) {
         return;
      }

      if (npcAttackType == 1 && autoNpcLastAttackNpcId == npc.IDDB) {
         return;
      }

      if (now < autoNpcNextAttackAtMs) {
         return;
      }

      autoNpcNextAttackAtMs = now + (long)clampMs(npcAttackDelayMs);
      GlobalService.gI().doCommunicate(npc.IDDB);
      autoNpcLastAttackNpcId = npc.IDDB;
   }

   private static Avatar findNpcOnCurrentMapByName() {
      if (safe(npcSearchKeyword).trim().length() == 0) {
         return null;
      }

      try {
         Vector list = LoadMap.playerLists;
         for (int i = 0; i < list.size(); i++) {
            Object o = list.elementAt(i);
            if (!(o instanceof Avatar)) continue;
            Avatar a = (Avatar)o;
            if (isEncodedNpcId(a.IDDB) && matchesConfiguredNpc(a)) {
               return a;
            }
         }

         // Fallback: any matching Avatar on map
         for (int i = 0; i < list.size(); i++) {
            Object o = list.elementAt(i);
            if (!(o instanceof Avatar)) continue;
            Avatar a = (Avatar)o;
            if (matchesConfiguredNpc(a)) {
               return a;
            }
         }
      } catch (Throwable t) {
      }

      return null;
   }

   static void debugShowNpcsOnMap() {
      try {
         Vector list = LoadMap.playerLists;
         int totalNpc = 0;
         int totalAvatar = 0;
         String out = "";
         String keyRaw = safe(npcSearchKeyword).trim();
         String keyNorm = normalizeForNpcMatch(keyRaw.toLowerCase());

         out += "Keyword: " + (keyRaw.length() == 0 ? "(rỗng)" : keyRaw);
         out += "\nKeywordNorm: " + (keyNorm.length() == 0 ? "(rỗng)" : keyNorm);
         out += "\n----- NPC trong map -----";

         for (int i = 0; i < list.size(); i++) {
            Object o = list.elementAt(i);
            if (!(o instanceof Avatar)) continue;
            totalAvatar++;
            Avatar a = (Avatar)o;
            if (!isEncodedNpcId(a.IDDB)) continue;
            totalNpc++;

            if (totalNpc <= 20) {
               if (out.length() > 0) out += "\n";
               boolean matched = matchesConfiguredNpcName(a.name);
               out += a.IDDB + " - " + safe(a.name) + (matched ? " [MATCH]" : " [NO]");
            }
         }

         if (totalNpc == 0) {
            Canvas.startOKDlg(out + "\nKhông có NPC id >= 2000000000\nAvatar tổng: " + totalAvatar);
            return;
         }

         if (totalNpc > 20) {
            out += "\n... (" + totalNpc + " NPC)";
         } else {
            out += "\n(" + totalNpc + " NPC)";
         }
         out += "\nAvatar tổng: " + totalAvatar;

         Canvas.startOKDlg(out);
      } catch (Throwable t) {
         Canvas.startOKDlg(T.npcListReadError);
      }
   }

   private static int clampMs(int ms) {
      if (ms < 1) return 1;
      if (ms > 600000) return 600000;
      return ms;
   }
}

final class IActionUtilitySpeedOk implements IAction {
   public final void perform() {
      int var0;

      try {
         var0 = Integer.parseInt(Canvas.inputDlg.getText().trim());
      } catch (Exception var2) {
         Canvas.startOKDlg(T.utilSpeedInputRange);
         return;
      }

      if (var0 < 1) {
         var0 = 1;
      } else if (var0 > 100) {
         var0 = 100;
      }

      ClientUtilities.utilSpeed1to100 = var0;
      ClientUtilities.persistUtilSpeed();
      ClientUtilities.resetAutoTickCounter();
      Canvas.endDlg();
      ClientUtilities.openUtilitySubmenu();
   }
}

final class IActionOpenUtilitySubmenu implements IAction {
   public final void perform() {
      ClientUtilities.openUtilitySubmenu();
   }
}

final class IActionOpenEventSubmenu implements IAction {
   public final void perform() {
      ClientUtilities.openEventSubmenu();
   }
}

final class IActionOpenFishingSettingsMenu implements IAction {
   public final void perform() {
      ClientUtilities.openFishingSettingsSubmenu();
   }
}

final class IActionAutoKissTurnOnYes implements IAction {
   public final void perform() {
      if (MapScr.focusP == null) {
         Canvas.startOK(T.utilAutoKissNoFocus, new IActionOpenUtilitySubmenu());
         return;
      }

      ClientUtilities.autoKiss = true;
      ClientUtilities.resetAutoKissHitTickOnly();
      ClientUtilities.openUtilitySubmenu();
   }
}

final class IActionAutoKissTurnOffYes implements IAction {
   public final void perform() {
      ClientUtilities.autoKiss = false;
      ClientUtilities.openUtilitySubmenu();
   }
}

final class IActionAutoKissConfirmNo implements IAction {
   public final void perform() {
      ClientUtilities.openUtilitySubmenu();
   }
}

final class IActionEventCmd implements IAction {
   private final byte code;

   IActionEventCmd(byte code) {
      this.code = code;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.openEventNpcSettingsForm();
            return;
         case 1:
            ClientUtilities.openEventSettingsForm();
            return;
         case 2:
            ClientUtilities.openGiftCounterMenu();
            return;
         case 3:
            Canvas.startWaitDlg();
            ParkService.gI().doJoinPark((byte)25, -1);
            return;
         case 4:
            ClientUtilities.eventCheckinPending = true;
            ClientUtilities.eventCheckinTargetMap = 0;
            Canvas.startWaitDlg();
            ParkService.gI().doJoinPark((byte)0, -1);
            return;
         case 10:
            ClientUtilities.openGiftCounterMenu();
            return;
         case 11:
            ClientUtilities.autoGiftCount = 0;
            ClientUtilities.openGiftCounterMenu();
            return;
         default:
      }
   }
}

final class IActionNpcCmd implements IAction {
   private final byte code;

   IActionNpcCmd(byte code) {
      this.code = code;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.autoNpcEnabled = !ClientUtilities.autoNpcEnabled;
            ClientUtilities.persistNpcSettings();
            ClientUtilities.openNpcSettingsSubmenu();
            return;
         case 1:
            ClientUtilities.autoNpcAttackEnabled = !ClientUtilities.autoNpcAttackEnabled;
            ClientUtilities.persistNpcSettings();
            ClientUtilities.openNpcSettingsSubmenu();
            return;
         case 2:
            ClientUtilities.rememberFocusedNpc();
            ClientUtilities.openNpcSettingsSubmenu();
            return;
         case 3:
            ClientUtilities.openNpcSearchSettingsForm();
            return;
         case 4:
            ClientUtilities.openNpcAttackSettingsForm();
            return;
         case 5:
            ClientUtilities.debugShowNpcsOnMap();
            ClientUtilities.openNpcSettingsSubmenu();
            return;
         case 6:
            ClientUtilities.npcSearchEnabled = !ClientUtilities.npcSearchEnabled;
            ClientUtilities.persistNpcSettings();
            ClientUtilities.openNpcSearchSettingsForm();
            return;
         case 7:
            ClientUtilities.openNpcSettingsSubmenu();
            return;
         case 8:
            ClientUtilities.autoNpcAttackEnabled = !ClientUtilities.autoNpcAttackEnabled;
            ClientUtilities.persistNpcSettings();
            ClientUtilities.openNpcAttackSettingsForm();
            return;
         case 9:
            ClientUtilities.openNpcSettingsSubmenu();
            return;
         case 10:
            ClientUtilities.openRememberedNpcListMenu();
            return;
         case 11:
            ClientUtilities.clearRememberedNpcList();
            return;
         case 12:
            ClientUtilities.openRememberedNpcListMenu();
            return;
         default:
      }
   }
}

final class IActionRememberedNpcCommunicate implements IAction {
   private final String npcIdText;

   IActionRememberedNpcCommunicate(String npcIdText) {
      this.npcIdText = npcIdText;
   }

   public final void perform() {
      int npcId;
      try {
         npcId = Integer.parseInt(this.npcIdText.trim());
      } catch (Exception e) {
         Canvas.startOKDlg(T.npcIdInvalid);
         ClientUtilities.reopenRememberedNpcListMenu();
         return;
      }

      GlobalService.gI().doCommunicate(npcId);
   }
}

final class IActionMapNpcCmd implements IAction {
   private final byte code;

   IActionMapNpcCmd(byte code) {
      this.code = code;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.rememberFocusedNpcFromMap();
            return;
         case 1:
            ClientUtilities.openRememberedNpcListMenuFromMap();
            return;
         default:
      }
   }
}

final class IActionNoop implements IAction {
   public final void perform() {
   }
}

final class IActionUtilityCmd implements IAction {
   private final byte code;

   IActionUtilityCmd(byte var1) {
      this.code = var1;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.changeZone();
            return;
         case 1:
            ClientUtilities.openChangeMapSubmenu();
            return;
         case 2:
            ClientUtilities.openAutoChatSubmenu();
            return;
         case 3:
            ClientUtilities.openAutoKissConfirmDialog();
            return;
         case 4:
            ClientUtilities.autoHit = !ClientUtilities.autoHit;
            ClientUtilities.openUtilitySubmenu();
            return;
         case 5:
            ClientUtilities.openAutoGiftSubmenu();
            return;
         case 6:
            ClientUtilities.openAutoTrollSubmenu();
            return;
         case 7:
            ClientUtilities.openAutoClickSubmenu();
            return;
         case 8:
            ClientUtilities.openSpeedSubmenu();
            return;
         case 9:
            Canvas.startOKDlg(T.utilGameRoomHint);
            return;
         case 10:
            ClientUtilities.vietnameseTyping = !ClientUtilities.vietnameseTyping;
            TField.initKey(ClientUtilities.vietnameseTyping ? 0 : 1);
            TField.m = false;
            ClientUtilities.openUtilitySubmenu();
            return;
         case 11:
            ClientUtilities.openAutoStoneSubmenu();
            return;
         case 12:
            ClientUtilities.openFishingSettingsSubmenu();
            return;
         case 13:
            MapScr.exitGame();
            LoginScr.gI().openSwitchAccountSettingsForm();
            return;
         case 14:
            ClientUtilities.stopAutoDialLuckyFromUtility();
            return;
         default:
      }
   }
}

final class IActionUtilitySpeedSub implements IAction {
   private final byte code;

   IActionUtilitySpeedSub(byte code) {
      this.code = code;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.openSpeedInputDialog();
            return;
         case 1:
            ClientUtilities.resetSpeedToDefault();
            return;
         default:
      }
   }
}

final class IActionFishingSettings implements IAction {
   private final byte code;

   IActionFishingSettings(byte code) {
      this.code = code;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.openFishingSettingsForm(false);
            return;
         case 1:
            ClientUtilities.requestContainerAndOpen();
            return;
         case 2:
            ClientUtilities.resetFishingCounter();
            ClientUtilities.openFishingSettingsSubmenu();
            return;
         case 3:
            ClientUtilities.toggleFishingAutoLogin();
            ClientUtilities.openFishingSettingsSubmenu();
            return;
         default:
      }
   }
}

final class IActionAutoClickSub implements IAction {
   private final byte code;

   IActionAutoClickSub(byte var1) {
      this.code = var1;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.toggleAutoClickEnabled();
            ClientUtilities.openAutoClickSubmenu();
            return;
         case 1:
            ClientUtilities.openAutoClickSettingsForm();
            return;
         default:
      }
   }
}

final class IActionAutoClickAfterSaveOk implements IAction {
   public final void perform() {
      ClientUtilities.openAutoClickSubmenu();
   }
}

final class IActionAutoGiftSub implements IAction {
   private final byte code;

   IActionAutoGiftSub(byte var1) {
      this.code = var1;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.autoGift = !ClientUtilities.autoGift;
            ClientUtilities.openAutoGiftSubmenu();
            return;
         case 1:
            ClientUtilities.openAutoGiftSettingsForm();
            return;
         default:
      }
   }
}

final class IActionAutoGiftAfterSaveOk implements IAction {
   public final void perform() {
      ClientUtilities.openAutoGiftSubmenu();
   }
}

final class IActionAutoTrollSub implements IAction {
   private final byte code;

   IActionAutoTrollSub(byte var1) {
      this.code = var1;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.autoTroll = !ClientUtilities.autoTroll;
            if (ClientUtilities.autoTroll) {
               ClientUtilities.resetAutoTrollSchedule();
            }

            ClientUtilities.openAutoTrollSubmenu();
            return;
         case 1:
            ClientUtilities.openAutoTrollSettingsForm();
            return;
         default:
      }
   }
}

final class IActionAutoTrollAfterSaveOk implements IAction {
   public final void perform() {
      ClientUtilities.openAutoTrollSubmenu();
   }
}

final class IActionAutoStoneSub implements IAction {
   private final byte code;

   IActionAutoStoneSub(byte var1) {
      this.code = var1;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.autoStoneEnabled = !ClientUtilities.autoStoneEnabled;
            ClientUtilities.resetAutoStoneSchedule();
            ClientUtilities.openAutoStoneSubmenu();
            return;
         case 1:
            ClientUtilities.openAutoStoneSettingsForm();
            return;
         case 2:
            ClientUtilities.openAutoStoneSellSettingsForm();
            return;
         default:
      }
   }
}

final class IActionJoinUtilityMap implements IAction {
   private final int mapId;

   IActionJoinUtilityMap(int var1) {
      this.mapId = var1;
   }

   public final void perform() {
      Canvas.startWaitDlg();
      ParkService.gI().doJoinPark(this.mapId, -1);
   }
}

final class IActionAutoChatSub implements IAction {
   private final byte code;

   IActionAutoChatSub(byte var1) {
      this.code = var1;
   }

   public final void perform() {
      switch (this.code) {
         case 0:
            ClientUtilities.autoChat = !ClientUtilities.autoChat;
            if (ClientUtilities.autoChat) {
               ClientUtilities.resetAutoTickCounter();
            }

            ClientUtilities.openAutoChatSubmenu();
            return;
         case 1:
            ClientUtilities.openAutoChatBreakDialog();
            return;
         case 2:
            ClientUtilities.openAutoChatContentForm();
            return;
         case 3:
            Canvas.startOKDlg(T.utilAutoChatGuideBody);
            return;
         default:
      }
   }
}

final class IActionAutoChatAfterSaveOk implements IAction {
   public final void perform() {
      ClientUtilities.openAutoChatSubmenu();
   }
}

final class IActionAutoChatBreakOk implements IAction {
   public final void perform() {
      int var0;

      try {
         var0 = Integer.parseInt(Canvas.inputDlg.getText().trim());
      } catch (Exception var2) {
         Canvas.startOKDlg(T.utilChatSecondsRange);
         return;
      }

      if (var0 < 1) {
         var0 = 1;
      } else if (var0 > 120) {
         var0 = 120;
      }

      ClientUtilities.autoChatIntervalSeconds = var0;
      ClientUtilities.resetAutoChatSchedule();
      Canvas.endDlg();
      ClientUtilities.openAutoChatSubmenu();
   }
}

