package avt;

import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class FarmScr extends MyScreen {
   private static final byte CMD_FARM_QUICK_CARE = 101;
   private static final byte CMD_FARM_CROP = 102;
   private static final byte CMD_FARM_ANIMAL = 103;
   private static final byte CMD_FARM_AUTO_CARE = 104;
   private static final byte CMD_FARM_KITCHEN = 105;
   private static final byte CMD_FARM_WAREHOUSE = 106;
   private static final byte CMD_FARM_SHOP = 107;
   private static final byte CMD_FARM_LOBBY = 108;
   private static final byte CMD_FARM_SWITCH_ACC = 109;
   private static final byte CMD_FARM_LOGOUT = 110;
   private static final byte CMD_FARM_COM_CHAO = 120;
   private static final byte CMD_FARM_EXIT = 112;
   private static final byte CMD_CROP_HARVEST = 122;
   private static final byte CMD_CROP_SOW = 113;
   private static final byte CMD_CROP_TILL = 114;
   private static final byte CMD_CROP_FERTILIZE = 115;
   private static final byte CMD_CROP_PEST_WEED = 116;
   private static final byte CMD_CROP_WATER = 117;
   private static final byte CMD_ANIMAL_SELL = 118;
   private static final byte CMD_ANIMAL_COLLECT = 119;
   private static final byte CMD_ANIMAL_PUMP = 120;
   private static final byte CMD_ANIMAL_CURE = 121;
   public static FarmScr instance;
   public static int idFarm;
   private String nameFarm;
   public static Vector cell;
   private static Vector itemSeed = new Vector();
   public static Vector listItemFarm = new Vector();
   public static Vector listFarmProduct = new Vector();
   public static Vector itemProduct;
   public static Vector listNest;
   public static Vector listBucket;
   public static Vector animalLists = new Vector();
   public static Vector[] listFood = new Vector[2];
   public static Image[] imgWorm_G;
   public static String l;
   public static Image imgBuyLant;
   public static Image imgFocusCel;
   public static Image imgSell;
   public static FrameImage p;
   public static FrameImage q;
   public static FrameImage r;
   public static FrameImage s;
   public static FrameImage unk;
   public static FrameImage u;
   public AvPosition[] posTree;
   private Vector listHound;
   public static int numTileBarn;
   public static int numTilePond;
   private byte[] typeCell = new byte[]{33, 34, 35, 36, 37};
   private byte[] typeCell1 = new byte[]{33, 120, 121, 122, 123};
   private Vector listAction = new Vector();
   public static boolean isAutoVatNuoi = false;
   public static boolean isNew = false;
   public static AvPosition focusCell;
   public static AvPosition posName;
   public static AvPosition posBarn;
   public static AvPosition posPond;
   public static byte action = -1;
   public static byte frame;
   private AvPosition posDoing;
   private int t;
   private static int numO = 12;
   private static int numH = 4;
   public static int idItemUsing = -1;
   public static int idSelected = -1;
   private int timeLimit;
   private long curTime;
   private long curTimeCooking;
   private static int tempTime = 0;
   public static boolean isSteal = false;
   private static boolean isAbleSteal = false;
   private static final byte[][] FRAME = new byte[][]{{0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {2, 2, 2, 2, 2, 3, 3, 3, 3, 3}, {4, 4, 4, 4, 4, 5, 5, 5, 5, 5}, {6, 6, 6, 6, 6, 7, 7, 7, 7, 7}, {8, 8, 8, 8, 8, 9, 9, 9, 9, 9}};
   private static Command aO;
   private static Command aP;
   private static Command aQ;
   private static Command aR;
   private static Command aS;
   private static Command aT;
   public static StarFruitObj starFruil;
   private Command aU;
   private Command aV;
   public Vector K = new Vector();
   private boolean isSelectedCell = false;
   private boolean isChamSoc = false;
   public static int indexItem = -1;
   Animal aniDoing;
   private long timeDoing = -1L;
   public static boolean isSelected = false;
   private boolean isTrans;
   private Vector listSelectedCell = new Vector();
   private boolean isTran = false;
   private int n = 0;
   private boolean isRangeSowMode = false;
   private int rangeSowSeedIndex = -1;
   private int rangeSowFrom = -1;
   private boolean isBatchSellAnimal = false;
   private int batchSellRemain = 0;
   public boolean isQuickCareActive() {
      return quickCareThrottleActive || quickCareQueueRunning || quickCareQueueType.size() > 0;
   }

   private static final int QUICK_CARE_DELAY_MS = 50;
   private boolean quickCareThrottleActive = false;
   private Vector quickCareQueueType = new Vector();
   private Vector quickCareQueueA = new Vector();
   private Vector quickCareQueueB = new Vector();
   private long quickCareNextSendAtMs = 0L;
   private boolean quickCareQueueRunning = false;
   private int quickCarePendingHarvestTreeAcks = 0;
   private boolean quickCarePendingSowPromptAfterHarvest = false;
   private int pendingHarvestPromptCellIndex = -1;
   private boolean pendingHarvestPromptResume = false;
   private boolean pendingHarvestSowPrompt = false;
   private int pendingHarvestSowCellIndex = -1;
   private boolean pendingHarvestSowAskQueued = false;
   private int pendingHarvestSowAskCellIndex = -1;
   private boolean pendingHarvestSowAskResumeAfter = false;
   private short preferredSeedAutoBuyId = -1;
   private boolean quickCareAutoSellSessionActive = false;
   private boolean quickCareRebuyTriggered = false;
   private boolean quickCarePendingLobbyRebuy = false;
   private boolean quickCarePendingRebuyAfterSow = false;
   private boolean quickCarePendingRangeSowAskQueued = false;
   private boolean quickCareRangeSowAskShowing = false;
   private boolean quickCareSowInProgress = false;
   private int quickCarePendingPlantAcks = 0;
   private Vector quickCareRebuySpecies = new Vector();
   private Vector quickCareRebuyCounts = new Vector();
   private Vector quickCarePendingAnimalHarvestIds = new Vector();
   public static byte levelStore;
   public static byte numBarn;
   public static byte numPond;
   public static boolean isReSize = false;
   public static int xRemember = -1;
   public static int yRemember = -1;
   public static int remainTime;
   public static short foodID = 0;
   private static int bd = -1;
   private static String be = "";
   private boolean isJoin = true;
   private int indexAuto = 0;
   public static int xPosCook;
   public static int yPosCook;

   public final boolean isQuickCareBusyForAuto() {
      if (this.quickCareQueueRunning) return true;
      if (this.quickCareQueueType.size() > 0) return true;
      if (this.isBatchSellAnimal || this.batchSellRemain > 0) return true;
      if (this.quickCarePendingHarvestTreeAcks > 0) return true;
      if (this.quickCarePendingAnimalHarvestIds.size() > 0) return true;
      if (this.quickCarePendingSowPromptAfterHarvest) return true;
      if (this.pendingHarvestSowPrompt || this.pendingHarvestSowAskQueued) return true;
      if (this.isRangeSowMode) return true;
      if (this.quickCarePendingRangeSowAskQueued || this.quickCareRangeSowAskShowing) return true;
      if (this.quickCareSowInProgress) return true;
      if (this.quickCarePendingPlantAcks > 0) return true;
      if (this.quickCarePendingLobbyRebuy || this.quickCarePendingRebuyAfterSow) return true;
      return false;
   }

   public static FarmScr gI() {
      if (instance == null) {
         instance = new FarmScr();
      }

      return instance;
   }

   public final void switchToMe() {
      super.switchToMe();
   }

   public static void initImg() {
      isSteal = false;
      isAbleSteal = false;
      if (unk == null) {
         FilePack.b(T.au);
         imgBuyLant = FilePack.getImage("buyLand");
         unk = FrameImage.init("cut", 24 * AvMain.hd, 24 * AvMain.hd);
         p = FrameImage.init("vp", 16 * AvMain.hd, 16 * AvMain.hd);
         (imgWorm_G = new Image[2])[0] = FilePack.getImage("w");
         imgWorm_G[1] = FilePack.getImage("g");
         q = FrameImage.init("wg", 13 * AvMain.hd, 9 * AvMain.hd);
         r = FrameImage.init("m", 27 * AvMain.hd, 17 * AvMain.hd);
         s = FrameImage.init("tc", 13 * AvMain.hd, 13 * AvMain.hd);
         imgSell = FilePack.getImage("focus");
         FilePack.reset();
      }

   }

   public final void initCmd() {
      aO = new Command(T.selectt, 0);
      aP = new Command(T.menu, 1);
      aQ = new Command((String)null, 2);
      aR = new Command((String)null, 3);
      super.left = aP;
   }

   public FarmScr() {
      listFood[0] = new Vector();
      listFood[1] = new Vector();
      this.initCmd();
      FilePack.b(T.au);
      imgFocusCel = FilePack.getImage("coin");
      u = FrameImage.init("iB", 9 * AvMain.hd, 13 * AvMain.hd);
      FilePack.reset();
      this.r();
      initImg();
      aS = new Command(T.finish, 8);
      aT = new Command(T.next, 9);
      this.aU = new Command(T.next, 16, this);
      this.aV = new Command(T.close, 18, this);
   }

   private void doFeeding() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < listItemFarm.size(); ++var2) {
         Item var3;
         FarmItem var4;
         if ((var4 = getFarmItem((var3 = (Item)listItemFarm.elementAt(var2)).ID)).action == 5 && (var4.type == 4 || var4.type == 101)) {
            var1.addElement(new CommandThuoc(this, var4.des, new IActionThuoc(this, var3), var4));
         }
      }

      startMenuFarm(var1);
   }

   private void r() {
      this.K.removeAllElements();
      this.K.addElement(new Command(T.farmQuickCare, CMD_FARM_QUICK_CARE));
      this.K.addElement(new Command(T.farmCrop, CMD_FARM_CROP));
      this.K.addElement(new Command(T.farmAnimal, CMD_FARM_ANIMAL));
      this.K.addElement(new Command(T.farmAutoCare, CMD_FARM_AUTO_CARE));
      this.K.addElement(new Command(T.farmKitchen, CMD_FARM_KITCHEN));
      this.K.addElement(new Command(T.farmWarehouse, CMD_FARM_WAREHOUSE));
      this.K.addElement(new Command(T.farmShop, CMD_FARM_SHOP));
      this.K.addElement(new Command(T.farmLobby, CMD_FARM_LOBBY));
      this.K.addElement(new Command(T.farmSwitchAccount, CMD_FARM_SWITCH_ACC));
      this.K.addElement(new Command(T.farmLogout, CMD_FARM_LOGOUT));
//      this.K.addElement(new Command(T.farmComChao, CMD_FARM_COM_CHAO));
      this.K.addElement(new Command(T.farmExit, CMD_FARM_EXIT));
   }

   private void openCropMenu() {
      Vector var1 = new Vector();
      var1.addElement(new Command(T.farmHarvest, CMD_CROP_HARVEST));
      var1.addElement(new Command(T.farmSowSeed, CMD_CROP_SOW));
      var1.addElement(new Command(T.farmTillSoil, CMD_CROP_TILL));
      var1.addElement(new Command(T.farmFertilize, CMD_CROP_FERTILIZE));
      var1.addElement(new Command(T.farmPestWeed, CMD_CROP_PEST_WEED));
      var1.addElement(new Command(T.farmWatering, CMD_CROP_WATER));
      Menu.gI().startAt(var1, -1);
   }

   private void openAnimalMenu() {
      Vector var1 = new Vector();
      var1.addElement(new Command(T.farmSellAnimal, CMD_ANIMAL_SELL));
      var1.addElement(new Command(T.farmCollectProduce, CMD_ANIMAL_COLLECT));
      var1.addElement(new Command(T.farmPumpTonic, CMD_ANIMAL_PUMP));
      var1.addElement(new Command(T.farmCureDisease, CMD_ANIMAL_CURE));
      Menu.gI().startAt(var1, -1);
   }

   /** Cây đủ giờ thu hoạch (theo time từ server / đếm local, khớp thanh thời gian). */
   private static boolean isCellHarvestReady(CellFarm c) {
      if (c == null || c.idTree == -1) {
         return false;
      }
      TreeInfo ti = FarmData.getTreeInfoByID(c.idTree);
      return ti != null && c.time >= ti.harvestTime * 60;
   }

   private void tickCellTime(int cellIndex) {
      CellFarm c = (CellFarm)cell.elementAt(cellIndex);
      if (c.idTree != -1 && c.statusTree < 6) {
         ++c.time;
         c.tempTime = (long)c.time * 60L;
         this.setInfoCell(cellIndex);
      }
   }

   private boolean useItemForCell(int var1, int var2, int var3) {
      if (var1 >= 0 && var1 < cell.size()) {
         CellFarm c = (CellFarm)cell.elementAt(var1);
         if (isCellHarvestReady(c)) {
            return false;
         }
      }
      for(int var4 = 0; var4 < listItemFarm.size(); ++var4) {
         Item var5 = (Item)listItemFarm.elementAt(var4);
         FarmItem var6 = getFarmItem(var5.ID);
         if (var6 != null && var6.type == 0 && var6.action == var2 && (var3 == -1 || var5.ID == var3) && var5.number > 0) {
            if (this.quickCareThrottleActive) {
               this.quickCareEnqueue((byte)1, var1, var5.ID);
            } else {
               FarmService.gI().doUsingItem(idFarm, var1, var5.ID);
               --var5.number;
               if (var5.number <= 0) {
                  listItemFarm.removeElement(var5);
               }
            }
            return true;
         }
      }

      if (var2 == 1) {
         return false;
      }

      short autoBuyId = this.findAutoBuyItemIdForCellAction(var2, (short)var3);
      if (autoBuyId != -1 && this.tryAutoBuyItem(autoBuyId) && this.tryUseItemForTarget(var1, autoBuyId)) {
         return true;
      }

      return false;
   }

   private boolean useItemForAnimalAction(Animal var1, int var2) {
      AnimalInfo var3 = FarmData.getAnimalByID(var1.species);
      if (var3 == null) {
         return false;
      } else {
         for(int var4 = 0; var4 < listItemFarm.size(); ++var4) {
            Item var5 = (Item)listItemFarm.elementAt(var4);
            FarmItem var6 = getFarmItem(var5.ID);
            if (var6 != null && var6.action == var2 && var5.number > 0 && (var6.type == var3.area || var6.type == 101 || var6.type == 100 && var3.area != 4)) {
               if (this.quickCareThrottleActive) {
                  this.quickCareEnqueue((byte)1, var1.IDDB, var5.ID);
               } else {
                  FarmService.gI().doUsingItem(idFarm, var1.IDDB, var5.ID);
               }
               --var5.number;
               if (var5.number <= 0) {
                  listItemFarm.removeElement(var5);
               }
               return true;
            }
         }

         short autoBuyId = this.findAutoBuyItemIdForAnimalAction(var2, var3.area, (short)-1);
         if (autoBuyId != -1 && this.tryAutoBuyItem(autoBuyId) && this.tryUseItemForTarget(var1.IDDB, autoBuyId)) {
            return true;
         }

         return false;
      }
   }

   private boolean useSpecificAnimalItem(Animal var1, int var2, int var3) {
      AnimalInfo var4 = FarmData.getAnimalByID(var1.species);
      if (var4 == null) {
         return false;
      } else {
         for(int var5 = 0; var5 < listItemFarm.size(); ++var5) {
            Item var6 = (Item)listItemFarm.elementAt(var5);
            FarmItem var7 = getFarmItem(var6.ID);
            if (var6.ID == var3 && var7 != null && var7.action == var2 && var6.number > 0 && (var7.type == var4.area || var7.type == 101 || var7.type == 100 && var4.area != 4)) {
               if (this.quickCareThrottleActive) {
                  this.quickCareEnqueue((byte)1, var1.IDDB, var6.ID);
               } else {
                  FarmService.gI().doUsingItem(idFarm, var1.IDDB, var6.ID);
               }
               --var6.number;
               if (var6.number <= 0) {
                  listItemFarm.removeElement(var6);
               }

               return true;
            }
         }

         short autoBuyId = this.findAutoBuyItemIdForAnimalAction(var2, var4.area, (short)var3);
         if (autoBuyId != -1 && this.tryAutoBuyItem(autoBuyId) && this.tryUseItemForTarget(var1.IDDB, autoBuyId)) {
            return true;
         }

         return false;
      }
   }

   private short chooseMoneyTypeForBuy(FarmItem fi) {
      if (fi == null) {
         return 0;
      } else {
         boolean canXu = fi.priceXu > 0 && GameMidlet.avatar.money[0] >= fi.priceXu;
         boolean canLuong = fi.priceLuong > 0 && GameMidlet.avatar.money[1] >= fi.priceLuong;
         if (canXu) {
            return 1;
         } else if (canLuong) {
            return 2;
         } else if (fi.priceXu > 0) {
            return 1;
         } else if (fi.priceLuong > 0) {
            return 2;
         } else {
            return 0;
         }
      }
   }

   private short findAutoBuyItemIdForCellAction(int action, short preferredId) {
      if (preferredId > 0) {
         FarmItem preferred = getFarmItem(preferredId);
         if (preferred != null && preferred.isItem && preferred.type == 0 && preferred.action == action && (preferred.priceXu > 0 || preferred.priceLuong > 0)) {
            return preferredId;
         }
      }

      short selected = -1;
      int bestCost = Integer.MAX_VALUE;

      for(int i = 0; i < FarmData.listItemFarm.size(); ++i) {
         FarmItem fi = (FarmItem)FarmData.listItemFarm.elementAt(i);
         if (fi != null && fi.isItem && fi.type == 0 && fi.action == action && (fi.priceXu > 0 || fi.priceLuong > 0)) {
            int cost = fi.priceXu > 0 ? fi.priceXu : fi.priceLuong;
            if (cost < bestCost) {
               bestCost = cost;
               selected = fi.ID;
            }
         }
      }

      return selected;
   }

   private short findAutoBuyItemIdForAnimalAction(int action, int area, short preferredId) {
      if (preferredId > 0) {
         FarmItem preferred = getFarmItem(preferredId);
         if (preferred != null && preferred.isItem && preferred.action == action && (preferred.type == area || preferred.type == 101 || preferred.type == 100 && area != 4) && (preferred.priceXu > 0 || preferred.priceLuong > 0)) {
            return preferredId;
         }
      }

      short selected = -1;
      int bestCost = Integer.MAX_VALUE;

      for(int i = 0; i < FarmData.listItemFarm.size(); ++i) {
         FarmItem fi = (FarmItem)FarmData.listItemFarm.elementAt(i);
         if (fi != null && fi.isItem && fi.action == action && (fi.type == area || fi.type == 101 || fi.type == 100 && area != 4) && (fi.priceXu > 0 || fi.priceLuong > 0)) {
            int cost = fi.priceXu > 0 ? fi.priceXu : fi.priceLuong;
            if (cost < bestCost) {
               bestCost = cost;
               selected = fi.ID;
            }
         }
      }

      return selected;
   }

   private void addFarmItemLocal(short itemId, int amount) {
      if (amount <= 0) {
         return;
      }

      Item it = Item.getItemByList(listItemFarm, itemId);
      if (it == null) {
         it = new Item();
         it.ID = itemId;
         it.number = amount;
         FarmItem fi = getFarmItem(itemId);
         if (fi != null) {
            it.name = fi.des;
         }

         listItemFarm.addElement(it);
      } else {
         it.number += amount;
      }
   }

   private boolean tryAutoBuyItem(short itemId) {
      FarmItem fi = getFarmItem(itemId);
      short moneyType = this.chooseMoneyTypeForBuy(fi);
      if (fi == null || moneyType == 0) {
         return false;
      } else {
         if (this.quickCareThrottleActive) {
            this.quickCareEnqueue((byte)4, itemId, moneyType);
         } else {
            FarmService.gI().doBuyItem(itemId, (byte)1, moneyType);
         }

         this.addFarmItemLocal(itemId, 1);
         return true;
      }
   }

   private boolean tryUseItemForTarget(int targetId, short itemId) {
      Item it = Item.getItemByList(listItemFarm, itemId);
      if (it == null || it.number <= 0) {
         return false;
      } else {
         if (this.quickCareThrottleActive) {
            this.quickCareEnqueue((byte)1, targetId, itemId);
         } else {
            FarmService.gI().doUsingItem(idFarm, targetId, itemId);
         }

         --it.number;
         if (it.number <= 0) {
            listItemFarm.removeElement(it);
         }

         return true;
      }
   }

   private byte chooseMoneyTypeForSeed(short seedId) {
      TreeInfo t = FarmData.getTreeByID(seedId);
      if (t == null) {
         return 0;
      } else {
         boolean canXu = t.priceSeed[0] > 0 && GameMidlet.avatar.money[0] >= t.priceSeed[0];
         boolean canLuong = t.priceSeed[1] > 0 && GameMidlet.avatar.money[1] >= t.priceSeed[1];
         if (canXu) {
            return 1;
         } else if (canLuong) {
            return 2;
         } else if (t.priceSeed[0] > 0) {
            return 1;
         } else if (t.priceSeed[1] > 0) {
            return 2;
         } else {
            return 0;
         }
      }
   }

   private void addSeedLocal(short seedId, int amount) {
      if (amount <= 0) {
         return;
      }

      Item seed = Item.getItemByList(itemSeed, seedId);
      if (seed == null) {
         seed = new Item();
         seed.ID = seedId;
         seed.number = amount;
         TreeInfo t = FarmData.getTreeByID(seedId);
         if (t != null) {
            seed.name = t.name;
         }

         itemSeed.addElement(seed);
      } else {
         seed.number += amount;
      }
   }

   private boolean tryAutoBuySeed(short seedId, int amount) {
      if (amount <= 0) {
         return true;
      } else {
         byte moneyType = this.chooseMoneyTypeForSeed(seedId);
         if (moneyType == 0) {
            return false;
         } else {
            for(int i = 0; i < amount; ++i) {
               FarmService.gI().doBuyItem(seedId, (byte)1, moneyType);
            }

            this.addSeedLocal(seedId, amount);
            return true;
         }
      }
   }

   private int cureAnimalDiseases(Animal var1) {
      int var2 = 0;
      if (var1.disease[1] && this.useSpecificAnimalItem(var1, 4, 120)) {
         ++var2;
      }

      if (var1.disease[0] && this.useSpecificAnimalItem(var1, 4, 121)) {
         ++var2;
      }

      if (var2 == 0 && (var1.disease[0] || var1.disease[1]) && this.useItemForAnimalAction(var1, 4)) {
         ++var2;
      }

      return var2;
   }

   private int boostPlantUntilFull(int var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < 20; ++var3) {
         if (!this.useItemForCell(var1, 2, 111) && !this.useItemForCell(var1, 2, 112) && !this.useItemForCell(var1, 2, -1)) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   private int boostAnimalUntilFull(Animal var1) {
      if (var1 == null || var1.health >= 100) {
         return 0;
      } else {
         return this.useItemForAnimalAction(var1, 6) ? 1 : 0;
      }
   }

   private boolean isAnimalMature(Animal var1) {
      if (var1 == null) {
         return false;
      } else {
         AnimalInfo info = FarmData.getAnimalByID(var1.species);
         return info != null && info.harvestTime > 0 && var1.bornTime >= info.harvestTime * 60;
      }
   }

   private boolean shouldAutoSellMatureAnimal(Animal var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1.species == 54 || var1.species == 59 || var1.species == 52 || var1.species == 58 || var1.species == 61;
      }
   }

   private boolean isFishOrTurtleSpecies(byte species) {
      return species == 54 || species == 59;
   }

   private void addQuickCareRebuySpecies(byte species) {
      for(int i = 0; i < this.quickCareRebuySpecies.size(); ++i) {
         Byte sp = (Byte)this.quickCareRebuySpecies.elementAt(i);
         if (sp.byteValue() == species) {
            Integer old = (Integer)this.quickCareRebuyCounts.elementAt(i);
            this.quickCareRebuyCounts.setElementAt(new Integer(old.intValue() + 1), i);
            return;
         }
      }

      this.quickCareRebuySpecies.addElement(new Byte(species));
      this.quickCareRebuyCounts.addElement(new Integer(1));
   }

   private void addPendingAnimalHarvest(int animalId) {
      Integer id = new Integer(animalId);
      if (!this.quickCarePendingAnimalHarvestIds.contains(id)) {
         this.quickCarePendingAnimalHarvestIds.addElement(id);
      }
   }

   private void quickCareProcessPendingAnimalHarvests() {
      for(int i = 0; i < this.quickCarePendingAnimalHarvestIds.size(); ++i) {
         int animalId = ((Integer)this.quickCarePendingAnimalHarvestIds.elementAt(i)).intValue();
         Animal a = getAnimalByIndex(animalId);
         if (a == null) {
            this.quickCarePendingAnimalHarvestIds.removeElementAt(i);
            --i;
         } else if (a.health >= 100 && !a.disease[0] && !a.disease[1] && a.numEggOne > 0) {
            a.numEggOne = 0;
            this.quickCareEnqueue((byte)0, a.IDDB, 0);
            this.quickCarePendingAnimalHarvestIds.removeElementAt(i);
            --i;
         }
      }
   }

   private void tryShowQueuedHarvestSowPrompt() {
      if (!this.pendingHarvestSowAskQueued) {
         return;
      }

      if (this.quickCareQueueRunning || this.quickCareQueueType.size() > 0 || this.isBatchSellAnimal || this.batchSellRemain > 0) {
         return;
      }

      if (Canvas.currentDialog != null) {
         Canvas.endDlg();
      }

      final int cellIndex = this.pendingHarvestSowAskCellIndex;
      final boolean resumeAfter = this.pendingHarvestSowAskResumeAfter;
      this.pendingHarvestSowAskQueued = false;
      this.pendingHarvestSowAskCellIndex = -1;
      this.pendingHarvestSowAskResumeAfter = false;

      Canvas.startOKDlg("Đã thu hoạch xong, gieo hạt luôn ?", new IAction() {
         public void perform() {
            FarmScr.this.pendingHarvestSowPrompt = false;
            FarmScr.this.pendingHarvestSowCellIndex = -1;
            FarmScr.this.openSeedSelectMenuForCell(cellIndex, resumeAfter);
         }
      });
   }

   private void tryShowQueuedQuickCareRangeSowPrompt() {
      if (!this.quickCarePendingRangeSowAskQueued) {
         return;
      }

      if (this.quickCareQueueRunning || this.quickCareQueueType.size() > 0) {
         return;
      }

      if (this.isBatchSellAnimal || this.batchSellRemain > 0) {
         return;
      }

      if (Canvas.currentDialog != null) {
         Canvas.endDlg();
      }

      this.quickCarePendingRangeSowAskQueued = false;
      this.quickCareRangeSowAskShowing = true;
      Canvas.startOKDlg("Đã thu hoạch xong, gieo hạt luôn ?", new IAction() {
         public final void perform() {
            FarmScr.this.quickCareRangeSowAskShowing = false;
            FarmScr.this.quickCareSowInProgress = true;
            FarmScr.this.startRangeSowFlow();
         }
      });
   }

   private int chooseMoneyTypeForAnimal(AnimalInfo info) {
      if (info == null) {
         return 0;
      } else {
         boolean canXu = info.price[0] > 0 && GameMidlet.avatar.money[0] >= info.price[0];
         boolean canLuong = info.price[1] > 0 && GameMidlet.avatar.money[1] >= info.price[1];
         if (canXu) {
            return 1;
         } else if (canLuong) {
            return 2;
         } else if (info.price[0] > 0) {
            return 1;
         } else if (info.price[1] > 0) {
            return 2;
         } else {
            return 0;
         }
      }
   }

   private void tryFinalizeQuickCareRebuyFlow() {
      if (this.quickCareRebuyTriggered) {
         return;
      }

      if (this.quickCareRebuySpecies.size() == 0) {
         return;
      }

      if (this.isBatchSellAnimal || this.batchSellRemain > 0) {
         return;
      }

      if (this.pendingHarvestSowPrompt || this.pendingHarvestSowAskQueued || this.isRangeSowMode || this.quickCarePendingRangeSowAskQueued || this.quickCareRangeSowAskShowing || this.quickCareSowInProgress || this.quickCarePendingPlantAcks > 0) {
         this.quickCarePendingRebuyAfterSow = true;
         return;
      }

      this.quickCareRebuyTriggered = true;
      this.quickCareAutoSellSessionActive = false;
      this.quickCarePendingLobbyRebuy = false;
      this.quickCarePendingRebuyAfterSow = false;
      this.doGoFarmWay();

      boolean sentAnyBuy = false;
      for(int i = 0; i < this.quickCareRebuySpecies.size(); ++i) {
         byte species = ((Byte)this.quickCareRebuySpecies.elementAt(i)).byteValue();
         int count = ((Integer)this.quickCareRebuyCounts.elementAt(i)).intValue();
         AnimalInfo info = FarmData.getAnimalByID(species);
         int moneyType = this.chooseMoneyTypeForAnimal(info);
         if (info != null && moneyType != 0) {
            for(int j = 0; j < count; ++j) {
               FarmService.gI().doBuyAnimal(info, moneyType);
               sentAnyBuy = true;
            }
         }
      }

      if (sentAnyBuy) {
         ParkService.gI().chatToBoard("Đã chăm sóc xong");
      }

      this.quickCareRebuySpecies.removeAllElements();
      this.quickCareRebuyCounts.removeAllElements();
   }

   private void doQuickCareAll() {
      System.out.println("[QUICK_CARE_AUTO] doQuickCareAll START idFarm=" + idFarm + " myID=" + GameMidlet.avatar.IDDB + " isAutoVatNuoi=" + isAutoVatNuoi);
      if (idFarm != GameMidlet.avatar.IDDB) {
         System.out.println("[QUICK_CARE_AUTO] FAIL: not owner farm");
         Canvas.startOKDlg(T.notOnFarmOther);
         return;
      }

      if (isAutoVatNuoi) {
         System.out.println("[QUICK_CARE_AUTO] FAIL: auto vat nuoi already on");
         Canvas.startOKDlg("Chăm sóc tự động đang bật.");
         return;
      }

      // Reset pending flags to prevent stuck state in AutoFishingFarmCare
      this.quickCarePendingLobbyRebuy = false;
      this.quickCarePendingRebuyAfterSow = false;

      System.out.println("[QUICK_CARE_AUTO] doQuickCareAll proceeding with care");
      boolean var1 = false;
      boolean harvestedCrop = false;
      this.quickCareAutoSellSessionActive = false;
      this.quickCareRebuyTriggered = false;
      this.quickCareRebuySpecies.removeAllElements();
      this.quickCareRebuyCounts.removeAllElements();
      this.quickCarePendingAnimalHarvestIds.removeAllElements();
      this.quickCarePendingHarvestTreeAcks = 0;
      this.quickCarePendingSowPromptAfterHarvest = false;
      this.quickCareThrottleActive = true;
      int var2;
      CellFarm var3;
      for(var2 = 0; var2 < cell.size(); ++var2) {
         var3 = (CellFarm)cell.elementAt(var2);
         if (isCellHarvestReady(var3)) {
            this.quickCareEnqueue((byte)3, var2, 0);
            ++this.quickCarePendingHarvestTreeAcks;
            var1 = true;
            harvestedCrop = true;
            continue;
         }

         if (var3.idTree != -1 && var3.statusTree < 6 && !isCellHarvestReady(var3)) {
            if (var3.isWorm && this.useItemForCell(var2, 7, -1)) {
               var1 = true;
            }

            if (var3.isGrass && this.useItemForCell(var2, 3, -1)) {
               var1 = true;
            }

            if (var3.vitalityPer < 100 && this.boostPlantUntilFull(var2) > 0) {
               var1 = true;
            }
         }
      }

      for(var2 = 0; var2 < animalLists.size(); ++var2) {
         Animal var4 = (Animal)animalLists.elementAt(var2);
         if ((var4.disease[0] || var4.disease[1]) && this.cureAnimalDiseases(var4) > 0) {
            var1 = true;
         }

         int forcedPump = 0;
         for(int p = 0; p < 5 && var4.health < 100; ++p) {
            if (!this.useItemForAnimalAction(var4, 6)) {
               break;
            }

            ++forcedPump;
         }
         if (forcedPump > 0) {
            var1 = true;
         }

         if (var4.numEggOne > 0) {
            this.addPendingAnimalHarvest(var4.IDDB);
            var1 = true;
         }
      }

      for(var2 = 0; var2 < animalLists.size(); ++var2) {
         Animal var16 = (Animal)animalLists.elementAt(var2);
         if (this.isAnimalMature(var16) && this.shouldAutoSellMatureAnimal(var16)) {
            this.quickCareEnqueue((byte)5, var16.IDDB, 0);
            if (this.isFishOrTurtleSpecies(var16.species)) {
               this.addQuickCareRebuySpecies(var16.species);
            }
            var1 = true;
            this.quickCareAutoSellSessionActive = true;
            this.isBatchSellAnimal = true;
            ++this.batchSellRemain;
         }
      }

      boolean var7 = false;
      for(var2 = 0; var2 < listNest.size(); ++var2) {
         if (var2 < listNest.size()) {
            AvPosition var8 = (AvPosition)listNest.elementAt(var2);
            for(int var9 = 0; var9 < animalLists.size(); ++var9) {
               Animal var10 = (Animal)animalLists.elementAt(var9);
               AnimalInfo var11 = FarmData.getAnimalByID(var10.species);
               if (var10.numEggOne > 0 && var8.anchor == var10.species && var11.area == 1) {
                  var7 = true;
                  break;
               }
            }
         }
      }

      for(var2 = 0; var2 < listNest.size(); ++var2) {
         this.doHarvestAnimal(1, var2, listNest);
      }

      for(var2 = 0; var2 < listBucket.size(); ++var2) {
         if (var2 < listBucket.size()) {
            AvPosition var12 = (AvPosition)listBucket.elementAt(var2);
            for(int var13 = 0; var13 < animalLists.size(); ++var13) {
               Animal var14 = (Animal)animalLists.elementAt(var13);
               AnimalInfo var15 = FarmData.getAnimalByID(var14.species);
               if (var14.numEggOne > 0 && var12.anchor == var14.species && var15.area == 2) {
                  var7 = true;
                  break;
               }
            }
         }
      }

      for(var2 = 0; var2 < listBucket.size(); ++var2) {
         this.doHarvestAnimal(2, var2, listBucket);
      }

      if (var7) {
         var1 = true;
      }

      if (starFruil != null && starFruil.numberFruit > 0) {
         FarmService var5 = FarmService.gI();
         var5.createMessage((byte)85);
         var5.sendMessage();
         var1 = true;
      }

      if (foodID > 0 && remainTime == 0) {
         FarmService var6 = FarmService.gI();
         var6.createMessage((byte)92);
         var6.sendMessage();
         var1 = true;
      }

      this.quickCareThrottleActive = false;
      if (harvestedCrop) {
         this.quickCarePendingSowPromptAfterHarvest = true;
      }
      if (var1) {
         this.quickCareStart();
      }

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      }
   }

   private void doCropHarvestAll() {
      boolean var1 = false;

      for(int var2 = 0; var2 < cell.size(); ++var2) {
         CellFarm var3 = (CellFarm)cell.elementAt(var2);
         if (isCellHarvestReady(var3)) {
            FarmService.gI().doHervest(idFarm, var2);
            var1 = true;
         }
      }

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      }
   }

   private void doCropFertilizeAll() {
      this.openFertilizerSelectMenu();
   }

   private void openFertilizerSelectMenu() {
      if (idFarm != GameMidlet.avatar.IDDB) {
         Canvas.startOKDlg(T.notOnFarmOther);
         return;
      }

      Vector cmds = new Vector();
      cmds.addElement(this.buildFixedFertilizerCmd((short)111, "Phân bón trung cấp"));
      cmds.addElement(this.buildFixedFertilizerCmd((short)112, "Phân bón siêu cấp"));
      cmds.addElement(this.buildFixedFertilizerCmd((short)113, "Giảm 15 phút"));
      cmds.addElement(this.buildFixedFertilizerCmd((short)114, "Giảm 30 phút"));
      cmds.addElement(this.buildFixedFertilizerCmd((short)115, "Giảm 60 phút"));

      Menu.gI().startAt(cmds, -1);
   }

   private Command buildFixedFertilizerCmd(final short itemId, String label) {
      int count = this.getFarmItemCount(itemId);
      String caption = label + " (" + count + ")";
      return new Command(caption, new IAction() {
         public void perform() {
            FarmScr.this.doCropFertilizeAllByItem(itemId);
         }
      });
   }

   private int getFarmItemCount(short itemId) {
      for (int i = 0; i < listItemFarm.size(); i++) {
         Item it = (Item)listItemFarm.elementAt(i);
         if (it != null && it.ID == itemId) {
            return it.number;
         }
      }
      return 0;
   }

   private boolean useFarmItemOnCellById(int cellIndex, short itemId) {
      for (int i = 0; i < listItemFarm.size(); i++) {
         Item it = (Item)listItemFarm.elementAt(i);
         if (it == null || it.ID != itemId || it.number <= 0) continue;

         FarmItem fi = getFarmItem(it.ID);
         if (fi == null || fi.type != 0) {
            return false;
         }

         return this.tryUseItemForTarget(cellIndex, itemId);
      }

      if (this.tryAutoBuyItem(itemId)) {
         return this.tryUseItemForTarget(cellIndex, itemId);
      }

      return false;
   }

   private int boostPlantUntilFullWithItem(int cellIndex, short itemId) {
      return this.useItemForCell(cellIndex, 2, itemId) ? 1 : 0;
   }

   private void doCropFertilizeAllByItem(short itemId) {
      boolean did = false;
      this.quickCareThrottleActive = true;

      for (int i = 0; i < cell.size(); i++) {
         CellFarm c = (CellFarm)cell.elementAt(i);
         if (c.idTree == -1 || c.statusTree >= 6) continue;

         if (itemId == 111 || itemId == 112) {
            if (c.vitalityPer < 100) {
               if (this.boostPlantUntilFullWithItem(i, itemId) > 0) {
                  did = true;
               }
            }
         } else {
            if (this.useFarmItemOnCellById(i, itemId)) {
               did = true;
            }
         }
      }

      this.quickCareThrottleActive = false;
      if (!did) {
         Canvas.startOKDlg("Không có gì để làm.");
      } else {
         this.quickCareStart();
      }
   }

   private void doCropPestWeedAll() {
      boolean var1 = false;

      for(int var2 = 0; var2 < cell.size(); ++var2) {
         CellFarm var3 = (CellFarm)cell.elementAt(var2);
         if (var3.idTree != -1 && var3.statusTree < 6) {
            if (var3.isWorm && this.useItemForCell(var2, 7, -1)) {
               var1 = true;
            }

            if (var3.isGrass && this.useItemForCell(var2, 3, -1)) {
               var1 = true;
            }
         }
      }

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      }
   }

   private void doCropWaterAll() {
      boolean var1 = false;

      for(int var2 = 0; var2 < cell.size(); ++var2) {
         CellFarm var3 = (CellFarm)cell.elementAt(var2);
         if (var3.idTree != -1 && var3.statusTree < 6 && this.useItemForCell(var2, 1, -1)) {
            var1 = true;
         }
      }

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      }
   }

   private void doAnimalCollectAll() {
      boolean var1 = false;
      int var2;
      for(var2 = 0; var2 < animalLists.size(); ++var2) {
         Animal var3 = (Animal)animalLists.elementAt(var2);
         if (var3.numEggOne > 0) {
            var3.numEggOne = 0;
            AnimalInfo var4 = FarmData.getAnimalByID(var3.species);
            if (var4 != null) {
               if (var4.area == 1) {
                  removePopup(-50);
               } else if (var4.area == 2) {
                  removePopup(-51);
               }
            }
            this.quickCareEnqueue((byte)0, var3.IDDB, 0);
            var1 = true;
         }
      }

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      } else {
         this.quickCareStart();
      }
   }

   private void doAnimalPumpAll() {
      boolean var1 = false;
      this.quickCareThrottleActive = true;

      for(int var2 = 0; var2 < animalLists.size(); ++var2) {
         Animal var3 = (Animal)animalLists.elementAt(var2);
         if (var3.health < 100 && this.boostAnimalUntilFull(var3) > 0) {
            var1 = true;
         }
      }

      this.quickCareThrottleActive = false;

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      } else {
         this.quickCareStart();
      }
   }

   private void doAnimalCureAll() {
      boolean var1 = false;
      this.quickCareThrottleActive = true;

      for(int var2 = 0; var2 < animalLists.size(); ++var2) {
         Animal var3 = (Animal)animalLists.elementAt(var2);
         if ((var3.disease[0] || var3.disease[1]) && this.cureAnimalDiseases(var3) > 0) {
            var1 = true;
         }
      }

      this.quickCareThrottleActive = false;

      if (!var1) {
         Canvas.startOKDlg("Không có gì để làm.");
      } else {
         this.quickCareStart();
      }
   }

   private void quickCareEnqueue(byte type, int a, int b) {
      this.quickCareQueueType.addElement(new Byte(type));
      this.quickCareQueueA.addElement(new Integer(a));
      this.quickCareQueueB.addElement(new Integer(b));
   }

   private void quickCareStart() {
      if (this.quickCareQueueRunning) {
         return;
      }

      if (this.quickCareQueueType.size() == 0) {
         return;
      }

      this.quickCareQueueRunning = true;
      this.quickCareNextSendAtMs = 0L;
   }

   public void startAutoFishingQuickCare() {
      System.out.println("[SMART_FISH] FarmScr.startAutoFishingQuickCare screen=" + Canvas.currentMyScreen
              + " typemap=" + LoadMap.TYPEMAP + " dialog=" + (Canvas.currentDialog != null)
              + " queueTypeSize=" + this.quickCareQueueType.size()
              + " queueRunning=" + this.quickCareQueueRunning);
      this.doQuickCareAll();
   }



   private void quickCareTickQueue() {
      if (!this.quickCareQueueRunning) {
         return;
      }

      long now = System.currentTimeMillis();
      if (this.quickCareNextSendAtMs != 0L && now < this.quickCareNextSendAtMs) {
         return;
      }

      if (this.quickCareQueueType.size() == 0) {
         this.quickCareQueueRunning = false;
         return;
      }

      Byte t = (Byte)this.quickCareQueueType.elementAt(0);
      Integer a = (Integer)this.quickCareQueueA.elementAt(0);
      Integer b = (Integer)this.quickCareQueueB.elementAt(0);
      this.quickCareQueueType.removeElementAt(0);
      this.quickCareQueueA.removeElementAt(0);
      this.quickCareQueueB.removeElementAt(0);

      if (t.byteValue() == 0) {
         FarmService.gI().doHarvestAnimal(idFarm, a.intValue());
      } else if (t.byteValue() == 1) {
         FarmService.gI().doUsingItem(idFarm, a.intValue(), (short)b.intValue());
      } else if (t.byteValue() == 2) {
         FarmService.gI().doPlantSeed(idFarm, a.intValue(), (short)b.intValue());
      } else if (t.byteValue() == 3) {
         FarmService.gI().doHervest(idFarm, a.intValue());
      } else if (t.byteValue() == 4) {
         FarmService.gI().doBuyItem((short)a.intValue(), (byte)1, b.intValue());
      } else if (t.byteValue() == 5) {
         FarmService.gI().doRequestPriceAnimal(idFarm, a.intValue());
      }

      this.quickCareNextSendAtMs = now + (long)QUICK_CARE_DELAY_MS;
      this.tryFinalizeQuickCareRebuyFlow();
   }

   private int countAnimalBySpecies(byte var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < animalLists.size(); ++var3) {
         Animal var4 = (Animal)animalLists.elementAt(var3);
         if (var4.species == var1) {
            ++var2;
         }
      }

      return var2;
   }

   private void openAnimalSellMenu() {
      Vector var1 = new Vector();
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < animalLists.size(); ++var3) {
         Animal var4 = (Animal)animalLists.elementAt(var3);
         Byte var5 = new Byte(var4.species);
         if (!var2.contains(var5)) {
            var2.addElement(var5);
            AnimalInfo var6 = FarmData.getAnimalByID(var4.species);
            final byte var7 = var4.species;
            final String var8 = var6 != null ? var6.name : ("ID " + var7);
            int var9 = this.countAnimalBySpecies(var7);
            String var10 = var8 + " (" + var9 + ")";
            var1.addElement(new Command(var10, new IAction() {
               public void perform() {
                  Canvas.startOKDlg("Bạn có muốn bán " + var8 + " không ?", new IAction() {
                     public void perform() {
                        FarmScr.this.sellAllAnimalsBySpecies(var7);
                     }
                  });
               }
            }));
         }
      }

      if (var1.size() == 0) {
         Canvas.startOKDlg("Không có gì để làm.");
      } else {
         Menu.gI().startAt(var1, -1);
      }
   }

   private void sellAllAnimalsBySpecies(byte var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < animalLists.size(); ++var3) {
         Animal var4 = (Animal)animalLists.elementAt(var3);
         if (var4.species == var1) {
            var2.addElement(new Integer(var4.IDDB));
         }
      }

      if (var2.size() == 0) {
         Canvas.startOKDlg("Không có gì để làm.");
      } else {
         this.isBatchSellAnimal = true;
         this.batchSellRemain = var2.size();

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            Integer var6 = (Integer)var2.elementAt(var5);
            FarmService.gI().doRequestPriceAnimal(idFarm, var6.intValue());
         }
      }
   }

   private void startRangeSowFlow() {
      this.isRangeSowMode = true;
      this.rangeSowSeedIndex = -1;
      this.rangeSowFrom = -1;
      this.doKhoGiong();
   }

   private int parseCellInput(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         String var2 = var1.trim();
         if (var2.length() == 0) {
            return -1;
         } else {
            int var3;
            try {
               var3 = Integer.parseInt(var2);
            } catch (Exception var5) {
               return -1;
            }

            if (var3 < 1 || var3 > 48) {
               return -1;
            } else {
               return var3 - 1;
            }
         }
      }
   }

   private void promptRangeSowFrom() {
      Canvas.inputDlg.setImg("Bắt đầu gieo từ ô:", new IAction() {
         public final void perform() {
            int var1 = FarmScr.this.parseCellInput(Canvas.inputDlg.getText());
            if (var1 >= 0 && var1 < FarmScr.cell.size()) {
               FarmScr.this.rangeSowFrom = var1;
               Canvas.endDlg();
               FarmScr.this.promptRangeSowTo();
            } else {
               Canvas.startOKDlg("Ô bắt đầu không hợp lệ.");
            }
         }
      }, 1);
   }

   private void promptRangeSowTo() {
      Canvas.inputDlg.setImg("Đến ô:", new IAction() {
         public final void perform() {
            int var1 = FarmScr.this.parseCellInput(Canvas.inputDlg.getText());
            if (var1 >= FarmScr.this.rangeSowFrom && var1 < FarmScr.cell.size()) {
               Canvas.endDlg();
               FarmScr.this.performRangeSow(FarmScr.this.rangeSowFrom, var1, FarmScr.this.rangeSowSeedIndex);
            } else {
               Canvas.startOKDlg("Ô kết thúc không hợp lệ.");
            }
         }
      }, 1);
   }

   private void performRangeSow(int var1, int var2, int var3) {
      this.commandTab(5, -1);
      this.quickCarePendingPlantAcks = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      Item var7 = null;
      if (var3 >= 0 && var3 < itemSeed.size()) {
         var7 = (Item)itemSeed.elementAt(var3);
         if (var7 != null) {
            this.preferredSeedAutoBuyId = var7.ID;
         }
      }

      if (var7 == null || var7.number <= 0) {
         this.quickCareSowInProgress = false;
         this.isRangeSowMode = false;
         this.rangeSowSeedIndex = -1;
         this.rangeSowFrom = -1;
         Canvas.startOKDlg("Không có hạt giống để gieo.");
         return;
      }

      for(int var8 = 0; var8 < cell.size(); ++var8) {
         CellFarm var9 = (CellFarm)cell.elementAt(var8);
         int var10 = var9.time1;
         int var11 = var8;
         boolean var12 = var10 >= var1 && var10 <= var2;
         if (!var12) {
            var12 = var11 >= var1 && var11 <= var2;
         }
         if (!var12 && var1 > 0 && var2 > 0) {
            var12 = var10 >= var1 - 1 && var10 <= var2 - 1;
         }
         if (!var12 && var1 > 0 && var2 > 0) {
            var12 = var11 >= var1 - 1 && var11 <= var2 - 1;
         }

         if (var12 && (var9.idTree == -1 || var9.statusTree == 6)) {
            ++var6;
         }
      }

      if (var6 > var7.number) {
         int needBuy = var6 - var7.number;
         if (!this.tryAutoBuySeed(var7.ID, needBuy)) {
            this.quickCareSowInProgress = false;
            this.isRangeSowMode = false;
            this.rangeSowSeedIndex = -1;
            this.rangeSowFrom = -1;
            Canvas.startOKDlg("Thiếu hạt giống và không đủ tiền để tự mua.");
            return;
         }

         var7 = (Item)itemSeed.elementAt(var3);
      }

      if (var6 > var7.number) {
         this.quickCareSowInProgress = false;
         this.isRangeSowMode = false;
         this.rangeSowSeedIndex = -1;
         this.rangeSowFrom = -1;
         Canvas.startOKDlg("Không đủ hạt giống để gieo.");
         return;
      }

      for(int var13 = 0; var13 < cell.size() && var5 < var7.number; ++var13) {
         CellFarm var14 = (CellFarm)cell.elementAt(var13);
         int var15 = var14.time1;
         int var16 = var13;
         boolean var17 = var15 >= var1 && var15 <= var2;
         if (!var17) {
            var17 = var16 >= var1 && var16 <= var2;
         }
         if (!var17 && var1 > 0 && var2 > 0) {
            var17 = var15 >= var1 - 1 && var15 <= var2 - 1;
         }
         if (!var17 && var1 > 0 && var2 > 0) {
            var17 = var16 >= var1 - 1 && var16 <= var2 - 1;
         }

         if (var17 && (var14.idTree == -1 || var14.statusTree == 6)) {
            ++this.quickCarePendingPlantAcks;
            doPlantSeed(var3, var13);
            ++var5;
            ++var4;
         }
      }

      this.isRangeSowMode = false;
      this.rangeSowSeedIndex = -1;
      this.rangeSowFrom = -1;
      if (this.quickCarePendingPlantAcks == 0) {
         this.quickCareSowInProgress = false;
      }
      if (var4 == 0) {
         this.commandTab(5, -1);
         Canvas.startOKDlg("Không có ô hợp lệ để gieo.");
      }
   }

   private void tillAllLand() {
      if (idFarm != GameMidlet.avatar.IDDB) {
         Canvas.startOKDlg(T.notOnFarmOther);
         return;
      }

      boolean var1 = false;
      for(int var2 = 0; var2 < cell.size(); ++var2) {
         CellFarm var3 = (CellFarm)cell.elementAt(var2);
         if (var3.idTree != -1 && var3.statusTree < 5) {
            var1 = true;
            break;
         }
      }

      if (var1) {
         Canvas.startOKDlg("Bạn có muốn phá cây hiện tại ?", new IAction() {
            public final void perform() {
               FarmScr.this.tillAllLandInternal(true);
            }
         });
      } else {
         this.tillAllLandInternal(false);
      }
   }

   private void tillAllLandInternal(boolean var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < cell.size(); ++var3) {
         CellFarm var4 = (CellFarm)cell.elementAt(var3);
         boolean var5 = var4.idTree != -1 && var4.statusTree < 5;
         if (var5) {
            if (var1) {
               FarmService.gI().doPlantSeed(idFarm, var3, -1);
               this.applyTillLocal(var4);
               var2 = true;
            }
         } else if (var4.level == 1 && var4.status != this.typeCell[1] || var4.level == 2 && var4.status != this.typeCell1[1]) {
            FarmService.gI().doPlantSeed(idFarm, var3, -1);
            this.applyTillLocal(var4);
            var2 = true;
         }
      }

      if (!var2) {
         Canvas.startOKDlg("Không có ô nào cần làm đất.");
      } else {
         Canvas.startOKDlg("Đã làm đất xong, Gieo hạt luôn ?", new IAction() {
            public final void perform() {
               FarmScr.this.startRangeSowFlow();
            }
         });
      }
   }

   private void applyTillLocal(CellFarm var1) {
      this.setStatusCell(var1, 1);
      var1.statusTree = 0;
      var1.idTree = -1;
      var1.isGrass = false;
      var1.isWorm = false;
      var1.time = 0;
      var1.tempTime = 0L;
      var1.vitalityPer = 100;
      var1.hervestPer = 0;
      LoadMap.map[var1.yCell * LoadMap.wMap + var1.xCell] = (short)var1.status;
   }

   private void doKhoGiong() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < itemSeed.size(); ++var2) {
         Item var3;
         if (FarmData.getTreeByID((var3 = (Item)itemSeed.elementAt(var2)).ID) != null) {
            var1.addElement(new CommandKhoGiong(this, var3.name + "(" + var3.number + ")", 7, var2, var3));
         }
      }

      if (var1.size() == 0) {
         this.promptOpenSeedShop();
         return;
      }

      startMenuFarm(var1);
   }

   private void promptOpenSeedShop() {
      short seedId = this.preferredSeedAutoBuyId;
      if (FarmData.getTreeByID(seedId) == null) {
         seedId = -1;
      }

      if (seedId != -1 && this.tryAutoBuySeed(seedId, 1)) {
         Canvas.startOKDlg("Đã tự mua hạt giống.");
      } else if (seedId == -1) {
         Canvas.startOKDlg("Chưa chọn loại hạt để tự mua.");
      } else {
         Canvas.startOKDlg("Kho hết giống và không đủ tiền để tự mua.");
      }
   }

   public final void close() {
      MapScr.typeJoin = -1;
      MapScr.typeCasino = -1;
      Canvas.startWaitDlg();
      GlobalService.gI().getHandler(8);
   }

   private void doSellect() {
      int var1;
      if ((var1 = this.getPosTreeByFocus(focusCell.x, focusCell.y)) - cell.size() == 0) {
         Canvas.startWaitDlg();
         FarmService.gI().doRequestPricePlant(idFarm);
      } else if (var1 >= 0 && var1 < cell.size()) {
         CellFarm var8 = (CellFarm)cell.elementAt(var1);
         if (isCellHarvestReady(var8)) {
            this.doHarvest();
            return;
         }

         if (var8.idTree == -1 && (var8.level == 1 && var8.status == this.typeCell[1] || var8.level == 2 && var8.status == this.typeCell1[1])) {
            FarmScr var9 = this;
            if (itemSeed.size() != 0) {
               if (action == -1) {
                  Vector var2 = new Vector();
                  int var3 = this.getPosTreeByFocus(focusCell.x, focusCell.y);
                  CellFarm var4 = (CellFarm)cell.elementAt(var3);
                  CellFarm var5 = null;
                  if (var3 > 0) {
                     var5 = (CellFarm)cell.elementAt(var3 - 1);
                  }

                  for(int var6 = 0; var6 < itemSeed.size(); ++var6) {
                     Item var7;
                     if (FarmData.getTreeByID((var7 = (Item)itemSeed.elementAt(var6)).ID) != null) {
                        var2.addElement(new class_bn(var9, var7.name + "(" + var7.number + ")", 5, var6, var7));
                     }
                  }

                  if (idFarm == GameMidlet.avatar.IDDB && (var4.level == 1 && var3 == 0 || var3 > 0 && var4.level < var5.level)) {
                     var2.addElement(new class_bp(var9, T.update, 11));
                  }

                  startMenuFarm(var2);
               }

               return;
            }

            Canvas.startOKDlg(T.egg);
         } else {
            this.doVatPham(var8);
         }
      }

   }

   public static void a(String var0) {
      Vector var1;
      (var1 = new Vector()).addElement(new Command(T.xu, 51));
      var1.addElement(new Command(T.gold, 52));
      var1.addElement(Canvas.ad);
      Canvas.setInfoC(var0, var1);
   }

   private void setAction(IAction var1) {
      if (action != -1) {
         this.listAction.addElement(var1);
      } else {
         var1.perform();
      }

   }

   private boolean setBonPhan(int var1, int var2) {
      boolean var3 = false;

      for(int var4 = 0; var4 < listItemFarm.size(); ++var4) {
         FarmItem var5;
         if ((var5 = getFarmItem(((Item)listItemFarm.elementAt(var4)).ID)).type == 0 && var5.action == var2) {
            this.setAction(new IActionBonPhan(this, var5, var1));
            var3 = true;
            break;
         }
      }

      if (!var3) {
         Canvas.startOKDlg(T.youWantBuyPro);
      }

      return var3;
   }

   private void doVatPham(CellFarm var1) {
      int var2 = this.getPosTreeByFocus(focusCell.x, focusCell.y);
      CellFarm var3 = (CellFarm)cell.elementAt(var2);
      CellFarm var4 = null;
      if (var2 > 0) {
         var4 = (CellFarm)cell.elementAt(var2 - 1);
      }

      Command1 var5 = null;
      if (idFarm == GameMidlet.avatar.IDDB && (var3.level == 1 && var2 == 0 || var2 > 0 && var3.level < var4.level)) {
         var5 = new Command1(this, T.update, 11);
      }

      if (var1.idTree != -1 && var1.statusTree < 6 && var1.status == 36) {
         this.setAction(new IActionVatPham1(this));
      }

      if (var1.idTree == -1 || var1.statusTree >= 6) {
         IActionVatPham2 var7 = new IActionVatPham2(this, var1);
         if (var5 != null) {
            Vector var8;
            (var8 = new Vector()).addElement(new CommandVatPham2(this, T.land, var7));
            var8.addElement(var5);
            startMenuFarm(var8);
            return;
         }

         this.setAction(var7);
      }

      if (var1.idTree != -1 && var1.statusTree < 6 && var2 < cell.size() && listItemFarm.size() > 0) {
         if (var1.isWorm) {
            this.setBonPhan(var2, 7);
         } else if (var1.isGrass) {
            this.setBonPhan(var2, 3);
         } else if (var1.vitalityPer < 80) {
            this.setBonPhan(var2, 2);
         }
      }

      if (action == -1) {
         Vector var9 = new Vector();
         CommandVatPham22 var6 = new CommandVatPham22(this, T.watering, 1);
         var9.addElement(var6);
         if (idFarm == GameMidlet.avatar.IDDB) {
            var9.addElement(new CommandVatPham222(this, T.land, new IActionVatPham22(this, var1)));
         }

         if (var5 != null) {
            var9.addElement(var5);
         }

         for(var2 = 0; var2 < listItemFarm.size(); ++var2) {
            Item var10;
            FarmItem var12;
            if ((var12 = getFarmItem((var10 = (Item)listItemFarm.elementAt(var2)).ID)).type == 0 && (var12.action == 3 && var1.isGrass || var12.action == 7 && var1.isWorm || var12.action != 3 && var12.action != 7)) {
               String var11 = var12.des + "(" + var10.number + ")";
               var9.addElement(new CommandThuoc(this, var11, 6, var2, var12));
            }
         }

         startMenuFarm(var9);
      }

   }

   public static void startMenuFarm(Vector var0) {
      int var1 = LoadMap.w * AvMain.hd;
      if (Canvas.isKeyBoard) {
         var1 += var1 / 3;
      }

      Menu.gI().startMenuFarm(var0, Canvas.hw, var1, var1);
   }

   public final void commandActionPointer(int var1) {
      FarmService var2;
      switch (var1) {
         case 0:
            Canvas.startOKDlg(T.doUWantCancel, 1, this);
            return;
         case 1:
            FarmService.gI().doCooking((short)-1);
            PopupShop.gI().close();
            return;
         case 2:
            PopupShop.gI().close();
            if (remainTime == 0) {
               (var2 = FarmService.gI()).createMessage((byte)92);
               var2.sendMessage();
               return;
            }

            FarmService.gI().nauNhanh(0);
            return;
         case 3:
            FarmService.gI().doUpdateFarm(1, 0);
            return;
         case 4:
            FarmService.gI().doUpdateFarm(1, 1);
            return;
         case 5:
            FarmService.gI().doUpdateFish(1, 0);
            return;
         case 6:
            FarmService.gI().doUpdateFish(1, 1);
            return;
         case 7:
            FarmService.gI().doUpdateStarFruil(1);
            return;
         case 8:
            FarmService.gI().doUpdateStarFruitByMoney(1);
            return;
         case 9:
            FarmService.gI().doUpdateLand(1, 1);
            return;
         case 10:
            FarmService.gI().doUpdateLand(1, 2);
            return;
         case 11:
            FarmService.gI().nauNhanh(1);
            return;
         case 12:
            Canvas.startOKDlg(T.feedFor, new IUpdateShop(this));
            return;
         case 13:
            FarmService.gI().doUpdateStore(1, 1);
            return;
         case 14:
            FarmService.gI().doUpdateStore(1, 2);
            return;
         case 15:
            ListScr.gI().setFriendList(true);
            return;
         case 16:
            FarmService.gI().doSteal(0);
            return;
         case 17:
            (var2 = FarmService.gI()).createMessage((byte)95);
            var2.sendMessage();
            return;
         case 18:
            gI().doGoFarmWay();
            return;
         case 19:
            (var2 = FarmService.gI()).createMessage((byte)98);
            var2.sendMessage();
            return;
         case 20:
            isAbleSteal = true;
            super.left = null;
         default:
      }
   }

   public final void commandActionPointer(int var1, int var2) {
      FarmService var3;
      FarmItem var4;
      Item var8;
      int var5;
      int var6;
      switch (var1) {
         case CMD_FARM_QUICK_CARE:
            this.doQuickCareAll();
            return;
         case CMD_FARM_CROP:
            this.openCropMenu();
            return;
         case CMD_FARM_ANIMAL:
            this.openAnimalMenu();
            return;
         case CMD_FARM_AUTO_CARE:
            this.commandActionPointer(10, -1);
            return;
         case CMD_FARM_KITCHEN:
            this.doOpenCooking();
            return;
         case CMD_FARM_WAREHOUSE:
            this.doOpenKhoHang();
            return;
         case CMD_FARM_SHOP:
            this.doOpenCuaHang();
            return;
         case CMD_FARM_LOBBY:
            ParkService.gI().doJoinPark(25, -1);
            return;
         case CMD_FARM_SWITCH_ACC:
            MapScr.exitGame();
            LoginScr.gI().openSwitchAccountSettingsForm();
            return;
         case CMD_FARM_LOGOUT:
            MapScr.exitGame();
            return;
         case CMD_FARM_EXIT:
            this.close();
            return;

         case CMD_CROP_HARVEST:
            this.doCropHarvestAll();
            return;
         case CMD_CROP_SOW:
            this.startRangeSowFlow();
            return;
         case CMD_CROP_TILL:
            this.tillAllLand();
            return;
         case CMD_CROP_FERTILIZE:
            this.doCropFertilizeAll();
            return;
         case CMD_CROP_PEST_WEED:
            this.doCropPestWeedAll();
            return;
         case CMD_CROP_WATER:
            this.doCropWaterAll();
            return;
         case CMD_ANIMAL_SELL:
            this.openAnimalSellMenu();
            return;
         case CMD_ANIMAL_COLLECT:
            this.doAnimalCollectAll();
            return;
         case CMD_ANIMAL_PUMP:
            this.doAnimalPumpAll();
            return;
         case CMD_ANIMAL_CURE:
            this.doAnimalCureAll();
            return;
         case 1:
            this.setAction((byte)1, idItemUsing);
            return;
         case 2:
            if (LoadMap.focusObj != null) {
               Canvas.endDlg();
               FarmService.gI().doRequestPriceAnimal(idFarm, ((Base)LoadMap.focusObj).IDDB);
               return;
            }
            break;
         case 3:
            if (LoadMap.focusObj != null) {
               AnimalInfo var12 = FarmData.getAnimalByID(getAnimalByIndex(((Base)LoadMap.focusObj).IDDB).species);

               for(var6 = 0; var6 < listItemFarm.size(); ++var6) {
                  if (var2 == var6) {
                     Item var9 = (Item)listItemFarm.elementAt(var6);
                     this.doUsingVatPhamAnimal(var9, var12.area == 1 ? 0 : 1);
                  }
               }

               return;
            }
            break;
         case 4:
            if (LoadMap.focusObj != null) {
               for(var1 = 0; var1 < listItemFarm.size(); ++var1) {
                  if (var2 == var1) {
                     var4 = getFarmItem((var8 = (Item)listItemFarm.elementAt(var1)).ID);
                     this.setActionAnimal(var4, var8.ID, (Animal)LoadMap.focusObj);
                  }
               }

               return;
            }
            break;
         case 5:
            for(var1 = 0; var1 < itemSeed.size(); ++var1) {
               if (var1 == var2) {
                  if ((var8 = (Item)itemSeed.elementAt(var1)).number <= 0) {
                     if (!this.tryAutoBuySeed(var8.ID, 1)) {
                        Canvas.startOKDlg("Thiếu hạt giống và không đủ tiền để tự mua.");
                        return;
                     }

                     var8 = (Item)itemSeed.elementAt(var1);
                  }

                  if ((var5 = this.getPosTreeByFocus(focusCell.x, focusCell.y)) >= cell.size()) {
                     return;
                  }

                  this.preferredSeedAutoBuyId = var8.ID;
                  doPlantSeed(var1, var5);
               }
            }

            return;
         case 6:
            for(var1 = 0; var1 < listItemFarm.size(); ++var1) {
               if (var1 == var2) {
                  if ((var8 = (Item)listItemFarm.elementAt(var1)).number > 0) {
                     if ((var5 = this.getPosTreeByFocus(focusCell.x, focusCell.y)) < cell.size() && listItemFarm.size() != 0) {
                        if ((var6 = (var4 = getFarmItem(var8.ID)).action) != 7) {
                           if (var6 == 1) {
                              this.setAction((byte)2, var4.ID);
                           } else {
                              this.setAction((byte)var6, var4.ID);
                           }
                        }

                        FarmService.gI().doUsingItem(idFarm, var5, var4.ID);
                     }
                  } else {
                     Canvas.startOKDlg(T.empty + var8.name);
                  }
               }
            }

            return;
         case 7:
            if (this.isRangeSowMode) {
               this.rangeSowSeedIndex = var2;
               this.promptRangeSowFrom();
            } else {
               this.setAuto(var2);
            }
            return;
         case 8:
            this.commandTab(5, -1);
            this.doKhoGiong();
            return;
         case 9:
            this.isChamSoc = true;
            this.setAuto(0);
            return;
         case 10:
            isAutoVatNuoi = true;

            for(var1 = this.indexAuto; var1 < animalLists.size(); ++var1) {
               Animal var7 = (Animal)animalLists.elementAt(var1);
               boolean var10000;
               if (var7.disease[1]) {
                  LoadMap.focusObj = var7;
                  AvCamera.gI().setToPos(var7.x * AvMain.hd, var7.y * AvMain.hd);
                  AvCamera.isFollow = true;
                  super.center = new Command(T.salonBeauty, new IActionTriBenh1(this, var7));
                  super.left = aS;
                  super.right = aT;
                  var10000 = true;
               } else if (var7.disease[0]) {
                  LoadMap.focusObj = var7;
                  AvCamera.gI().setToPos(var7.x * AvMain.hd, var7.y * AvMain.hd);
                  AvCamera.isFollow = true;
                  super.center = new Command(T.salonBeauty, new IActionTriBenh2(this, var7));
                  super.left = aS;
                  super.right = aT;
                  var10000 = true;
               } else if (var7.hunger && !(var7 instanceof Dog) && !(var7 instanceof Cattle)) {
                  LoadMap.focusObj = var7;
                  AvCamera.gI().setToPos(var7.x * AvMain.hd, var7.y * AvMain.hd);
                  AvCamera.isFollow = true;
                  super.center = new Command(T.feeding, new IActionTriBenh3(this, var7));
                  super.left = aS;
                  super.right = aT;
                  var10000 = true;
               } else if (var7.health < 50) {
                  LoadMap.focusObj = var7;
                  AvCamera.gI().setToPos(var7.x * AvMain.hd, var7.y * AvMain.hd);
                  AvCamera.isFollow = true;
                  super.center = new Command(T.view, new class_w(this, var7));
                  super.left = aS;
                  super.right = aT;
                  var10000 = true;
               } else {
                  var10000 = false;
               }

               if (var10000) {
                  return;
               }

               ++this.indexAuto;
            }

            this.commandTab(8, -1);
            Canvas.startOKDlg(T.areYouUseNumReg);
            return;
         case 11:
            FarmService.gI().doUpdateLand(0, 0);
            return;
         case 12:
            (var3 = FarmService.gI()).createMessage((byte)85);
            var3.sendMessage();
            return;
         case 13:
            if (starFruil.timeFinish > 0) {
               FarmService.gI().doUpdateStarFruitByMoney(0);
               return;
            }

            FarmService.gI().doUpdateStarFruil(0);
            return;
         case 14:
            (var3 = FarmService.gI()).createMessage((byte)87);
            var3.sendMessage();
            return;
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         default:
            break;
         case 20:
            this.close();
      }

   }

   private void setAuto(int var1) {
      idSelected = 0;
      super.left = new Command(T.finish, 5);
      super.right = null;
      AvCamera.isFollow = true;
      super.center = null;
      this.isSelectedCell = true;
      indexItem = var1;
   }

   private void setActionAnimal(FarmItem var1, short var2, Animal var3) {
      this.setAction(new IActionSetAnimal(this, var1, var2, var3));
   }

   protected final void doUsingVatPhamAnimal(Item var1, int var2) {
      int var3 = GameMidlet.avatar.direct == 0 ? 1 : -1;
      int var4 = listFood[var2].size();
      if (var1.number - var4 <= 0) {
         Canvas.startOKDlg(T.foodForEmpty);
      } else {
         for(int var5 = 0; var5 < 3 && var5 < var1.number - var4; ++var5) {
            Point var6 = new Point(GameMidlet.avatar.x, GameMidlet.avatar.y - 40);
            FarmItem var7 = getFarmItem(var1.ID);
            var6.itemID = var1.ID;
            var6.w = var6.h = 2;
            var6.g = -(4 + CRes.rnd(3));
            var6.v = var3 * (2 + CRes.rnd(3));
            var6.limitY = GameMidlet.avatar.y - 20 + CRes.rnd(4) * 5;
            if (var7.type == 4) {
               int var8 = LoadMap.getposMap(GameMidlet.avatar.x, GameMidlet.avatar.y + 23);
               if (LoadMap.map[var8] == 14) {
                  var6.limitY = 50 + CRes.rnd(50);
                  var6.v = var3 * CRes.rnd(3);
               }
            }

            var6.layer = new PoLayer(this, var6);
            listFood[var2].addElement(var6);
            LoadMap.dynamicLists.addElement(var6);
         }
      }

   }

   public static void startTextSmall(int var0, int var1, CellFarm var2, Animal var3) {
      if (LoadMap.TYPEMAP != 25 && var0 != var1) {
         String var4 = "";
         if (var1 - var0 > 0) {
            var4 = var4 + "+";
         }

         int var5;
         int var6;
         if (var2 != null) {
            var5 = var2.xCell * LoadMap.w + LoadMap.w / 2;
            var6 = var2.yCell * LoadMap.w - LoadMap.w / 2;
         } else {
            var5 = var3.x;
            var6 = var3.y - 30;
         }

         Canvas.addFlyTextSmall(var4 + (var1 - var0), var5, var6, -1, 0, -1);
      }

   }

   private void doHarvest() {
      if (GameMidlet.avatar.IDDB == idFarm) {
         int var1 = this.getPosTreeByFocus(focusCell.x, focusCell.y);
         GameMidlet.avatar.getClass();
         FarmService.gI().doHervest(idFarm, var1);
      }

   }

   private static void doPlantSeed(int var0, int var1) {
      if (Canvas.isInitChar) {
         Welcome.goFarm();
      }

      Item var2 = (Item)itemSeed.elementAt(var0);
      if (var1 < 0 || var1 >= cell.size()) {
         return;
      }

      FarmScr scr = instance;
      if (scr == null) {
         FarmService.gI().doPlantSeed(idFarm, var1, var2.ID);
         return;
      }

      CellFarm c = (CellFarm)cell.elementAt(var1);
      boolean needsTill = false;
      if (c != null) {
         if (c.idTree != -1 && c.statusTree == 6) {
            needsTill = true;
         } else if (c.idTree == -1 && ((c.level == 1 && c.status != scr.typeCell[1]) || (c.level == 2 && c.status != scr.typeCell1[1]))) {
            needsTill = true;
         }
      }
      if (needsTill) {
         scr.quickCareEnqueue((byte)2, var1, -1);
         scr.applyTillLocal(c);
      }

      scr.quickCareEnqueue((byte)2, var1, var2.ID);
      scr.quickCareStart();
   }

   private int getPosTreeByFocus(int var1, int var2) {
      for(int var3 = 0; var3 < this.posTree.length; ++var3) {
         for(int var4 = 0; var4 < numO; ++var4) {
            int var5 = this.posTree[var3].x + var4 / numH;
            int var6 = this.posTree[var3].y + var4 % numH;
            if (var1 == var5 && var2 == var6) {
               return var3 * numO + var4;
            }
         }
      }

      return -1;
   }

   private void setAction(byte var1, int var2) {
      idItemUsing = var2;
      action = var1;
      GameMidlet.avatar.task = -1;
      GameMidlet.avatar.idFrom = -1;
      GameMidlet.avatar.idTo = -1;
      if (action == 4) {
         this.posDoing = new AvPosition(LoadMap.focusObj.x / LoadMap.w, LoadMap.focusObj.y / LoadMap.w);
      } else {
         this.posDoing = new AvPosition(focusCell.x, focusCell.y);
      }

      GameMidlet.avatar.yCur = this.posDoing.y * LoadMap.w + LoadMap.w / 2;
      GameMidlet.avatar.xCur = this.posDoing.x * LoadMap.w;
      if (GameMidlet.avatar.direct == Base.LEFT) {
         Avatar var10000 = GameMidlet.avatar;
         var10000.xCur += LoadMap.w;
      }

   }

   private void setCellAll() {
      for(int var1 = 0; var1 < this.posTree.length; ++var1) {
         for(int var2 = 0; var2 < numO; ++var2) {
            int var3 = this.posTree[var1].x + var2 / numH;
            int var4 = this.posTree[var1].y + var2 % numH;
            if (var1 * numO + var2 < cell.size()) {
               LoadMap.setType(var3, var4, (byte)51);
               CellFarm var5;
               (var5 = (CellFarm)cell.elementAt(var1 * numO + var2)).time1 = (short)(var1 * numO + var2);
               var5.xCell = var3;
               var5.yCell = var4;
               var5.x = var3 * LoadMap.w + LoadMap.w / 2;
               var5.y = var4 * LoadMap.w + 18;
               this.setInfoCell(var1 * numO + var2);
               LoadMap.treeLists.addElement(var5);
            } else {
               if (var1 * numO + var2 == cell.size()) {
                  LoadMap.treeLists.addElement(new SubObject(-3, var3 * LoadMap.w + 20, var4 * LoadMap.w + 20, imgBuyLant.getWidth()));
                  LoadMap.setType(var3, var4, (byte)51);
                  LoadMap.orderVector(LoadMap.treeLists);
               }

               if (LoadMap.map[var4 * LoadMap.wMap + var3] == this.typeCell[0]) {
                  LoadMap.orderVector(LoadMap.treeLists);
                  return;
               }

               if (var3 == this.posTree[var1].x && var4 == this.posTree[var1].y) {
                  LoadMap.map[var4 * LoadMap.wMap + var3] = 4;
               }
            }
         }
      }

      LoadMap.orderVector(LoadMap.treeLists);
   }

   public final void update() {
      if (this.quickCareRangeSowAskShowing && Canvas.currentDialog == null) {
         this.quickCareRangeSowAskShowing = false;
      }
      if (this.quickCarePendingSowPromptAfterHarvest && this.quickCarePendingHarvestTreeAcks == 0 && !this.quickCarePendingRangeSowAskQueued && !this.quickCareRangeSowAskShowing) {
         this.quickCarePendingSowPromptAfterHarvest = false;
         this.quickCarePendingRangeSowAskQueued = true;
         this.quickCarePendingRebuyAfterSow = true;
      }
      this.tryShowQueuedHarvestSowPrompt();
      this.tryShowQueuedQuickCareRangeSowPrompt();
      this.quickCareProcessPendingAnimalHarvests();
      if (this.quickCarePendingRebuyAfterSow && !this.pendingHarvestSowPrompt && !this.pendingHarvestSowAskQueued && !this.isRangeSowMode && !this.quickCarePendingRangeSowAskQueued && !this.quickCareRangeSowAskShowing && !this.quickCareSowInProgress && this.quickCarePendingPlantAcks == 0) {
         this.tryFinalizeQuickCareRebuyFlow();
      }
      if (this.quickCarePendingLobbyRebuy && LoadMap.TYPEMAP == 25) {
         this.quickCarePendingLobbyRebuy = false;
         boolean sentAnyBuy = false;

         for(int i = 0; i < this.quickCareRebuySpecies.size(); ++i) {
            byte species = ((Byte)this.quickCareRebuySpecies.elementAt(i)).byteValue();
            int count = ((Integer)this.quickCareRebuyCounts.elementAt(i)).intValue();
            AnimalInfo info = FarmData.getAnimalByID(species);
            int moneyType = this.chooseMoneyTypeForAnimal(info);
            if (info != null && moneyType != 0) {
               for(int j = 0; j < count; ++j) {
                  FarmService.gI().doBuyAnimal(info, moneyType);
                  sentAnyBuy = true;
               }
            }
         }

         if (sentAnyBuy) {
            ParkService.gI().chatToBoard("Đã chăm sóc xong");
         }

         this.quickCareRebuySpecies.removeAllElements();
         this.quickCareRebuyCounts.removeAllElements();
      }

      this.t += 2;
      if (this.t >= 10) {
         this.t = 0;
      }

      if (action != -1) {
         frame = FRAME[action][this.t];
         ++this.timeLimit;
         if (this.timeLimit > 10) {
            this.timeLimit = 0;
            this.resetAction();
         }
      }

      if ((LoadMap.TYPEMAP == 24 || LoadMap.TYPEMAP == 53) && (System.currentTimeMillis() - this.curTime) / 1000L > 300L) {
         this.curTime = System.currentTimeMillis();
         this.doJoinFarm(idFarm, true);
      }

      Canvas.loadMap.update();
      this.quickCareTickQueue();
      if (!isAutoVatNuoi && !isSelected && indexItem == -1) {
         this.setFocus();
      }

      int var2;
      label142: {
         if (action != -1 && this.timeDoing == -1L && GameMidlet.avatar.action == 0) {
            this.timeDoing = System.currentTimeMillis() / 100L;
            var2 = -1;
            if (this.posDoing != null) {
               var2 = this.getPosTreeByFocus(this.posDoing.x, this.posDoing.y);
            }

            if (action == 4) {
               var2 = 0;
            }

            if (this.posDoing.x * LoadMap.w < GameMidlet.avatar.x) {
               GameMidlet.avatar.direct = Base.LEFT;
            } else {
               GameMidlet.avatar.direct = 0;
            }

            GameMidlet.avatar.dirLast = GameMidlet.avatar.direct;
            if (this.aniDoing != null) {
               this.aniDoing.isStand = false;
               this.aniDoing = null;
            }

            if (var2 == -1) {
               this.resetAction();
               break label142;
            }

            SubObject var6 = new SubObject(-2, GameMidlet.avatar.x, GameMidlet.avatar.y - 5, unk.frameWidth);
            LoadMap.treeLists.addElement(var6);
            byte var3 = 0;
            if (action == 0) {
               var3 = 5;
               var6.y = GameMidlet.avatar.y - 8;
            }

            if (GameMidlet.avatar.direct == 0) {
               var6.x = GameMidlet.avatar.x + 10 + var3;
            } else {
               var6.x = GameMidlet.avatar.x - 10 - var3;
            }
         }

         if (this.timeDoing != -1L && (action == 1 || action == 0 || action == 2) && System.currentTimeMillis() / 100L - this.timeDoing > 2L) {
            this.timeDoing = System.currentTimeMillis() / 100L;
            if (GameMidlet.avatar.action == 6) {
               GameMidlet.avatar.setAction((byte)0);
            } else {
               GameMidlet.avatar.setAction((byte)6);
            }
         }
      }

      if ((LoadMap.TYPEMAP == 24 || LoadMap.TYPEMAP == 53) && animalLists.size() > 0 && ++tempTime > 250) {
         tempTime = 0;
         var2 = CRes.rnd(animalLists.size());
         Animal var7 = (Animal)animalLists.elementAt(var2);
         String var8 = "";
         if (var7.disease[0]) {
            var8 = var8 + T.diarrhea;
         }

         if (var7.disease[1]) {
            if (!var8.equals("")) {
               var8 = var8 + ", ";
            }

            var8 = var8 + T.flu;
         }

         if (var7.hunger) {
            if (!var8.equals("")) {
               var8 = var8 + ", ";
            }

            var8 = var8 + T.hunger;
         }

         if (var7.health < 20) {
            if (!var8.equals("")) {
               var8 = var8 + ", ";
            }

            var8 = var8 + T.tire;
         }

         if (!var8.equals("")) {
            var7.chat = new ChatPopup(25, var8, (byte)0);
            var7.chat.setPos(var7.x, var7.y - 45);
         }
      }

      if (System.currentTimeMillis() / 1000L - this.curTimeCooking / 1000L >= 1L) {
         if (remainTime > 0) {
            --remainTime;
         }

         this.curTimeCooking = System.currentTimeMillis();

         for(int var1 = 0; var1 < cell.size(); ++var1) {
            this.tickCellTime(var1);
         }
      }

   }

   private void resetAction() {
      int var1;
      for(var1 = 0; var1 < LoadMap.treeLists.size(); ++var1) {
         if (((SubObject)LoadMap.treeLists.elementAt(var1)).type == -2) {
            LoadMap.treeLists.removeElementAt(var1);
            if (var1 > 0) {
               --var1;
            }
         }
      }

      this.timeDoing = -1L;
      var1 = -1;
      if (this.posDoing != null) {
         int var2 = this.posDoing.y;
         var1 = this.posDoing.x;
         int var3 = cell.size();
         int var4 = 0;

         int var10000;
         while(true) {
            if (var4 >= var3) {
               var10000 = -1;
               break;
            }

            CellFarm var5;
            if ((var5 = (CellFarm)cell.elementAt(var4)).xCell == var1 && var5.yCell == var2) {
               var10000 = var4;
               break;
            }

            ++var4;
         }

         var1 = var10000;
      }

      if (var1 == -1) {
         action = -1;
         GameMidlet.avatar.action = 0;
         GameMidlet.avatar.task = 0;
         this.doAction();
      } else {
         if (idItemUsing == -1) {
            CellFarm var6 = (CellFarm)cell.elementAt(var1);
            switch (action) {
               case 0:
                  this.setStatusCell(var6, 1);
                  var6.statusTree = 0;
                  LoadMap.map[var6.yCell * LoadMap.wMap + var6.xCell] = (short)var6.status;
                  if (var6.idTree != -1) {
                     FarmService.gI().doPlantSeed(idFarm, var1, -1);
                  }

                  var6.idTree = -1;
                  if (Canvas.isInitChar) {
                     Welcome.goFarm();
                  }
                  break;
               case 1:
                  this.setStatusCell(var6, 4);
                  var6.isArid = false;
                  LoadMap.map[var6.yCell * LoadMap.wMap + var6.xCell] = (short)var6.status;
                  FarmService.gI().doUsingItem(idFarm, var1, 100);
               case 2:
            }
         }

         idItemUsing = -1;
         this.posDoing = null;
         action = -1;
         GameMidlet.avatar.task = 0;
         GameMidlet.avatar.action = 0;
         this.doAction();
      }

   }

   private void doAction() {
      if (isAutoVatNuoi) {
         this.commandActionPointer(10, -1);
      } else if (this.listAction.size() > 0) {
         IAction var1;
         (var1 = (IAction)this.listAction.elementAt(0)).perform();
         this.listAction.removeElement(var1);
      } else if (this.isChamSoc) {
         this.setGieoHat();
      }

   }

   private void setFocus() {
      if (LoadMap.TYPEMAP != 25) {
         int var1;
         if (GameMidlet.avatar.direct == Base.LEFT) {
            var1 = GameMidlet.avatar.x - 23;
         } else {
            var1 = GameMidlet.avatar.x + 23;
         }

         var1 /= LoadMap.w;
         int var2 = GameMidlet.avatar.y / LoadMap.w;
         int var3 = LoadMap.type[var2 * LoadMap.wMap + var1];
         int var4 = this.getPosTreeByFocus(var1, var2);
         if (var3 == 51 && var4 <= cell.size()) {
            focusCell.x = var1;
            focusCell.y = var2;
            if (action != 0 && action != 1) {
               super.center = aO;
            } else {
               super.center = null;
            }
         } else {
            if (super.center == aO || super.center == aR) {
               super.center = null;
            }

            focusCell.x = -1;
            focusCell.y = -1;
            if (LoadMap.focusObj == null) {
               var2 = LoadMap.getposMap(GameMidlet.avatar.x + 12, GameMidlet.avatar.y);
               var3 = LoadMap.getposMap(GameMidlet.avatar.x, GameMidlet.avatar.y + 12);
               boolean var10000;
               if ((LoadMap.map[var2] != 100 || GameMidlet.avatar.direct != 0) && LoadMap.map[var3] != 14) {
                  super.center = null;
                  var10000 = false;
               } else {
                  super.center = aR;
                  var10000 = true;
               }

               if (var10000) {
                  return;
               }
            }

            if (LoadMap.focusObj != null && super.center == null) {
               if (super.right == null) {
                  super.right = LoadMap.cmdNext;
               }

               super.center = aQ;
            }

            if (LoadMap.focusObj == null) {
               super.right = null;
            }

            if (LoadMap.focusObj == null && super.center == aQ) {
               super.center = null;
            }
         }
      }

   }

   public final void updateKey() {
      CellFarm var4;
      if (this.isTrans && GameMidlet.avatar.action == 0 && GameMidlet.avatar.task == 0 && GameMidlet.avatar.x == GameMidlet.avatar.xCur && GameMidlet.avatar.y == GameMidlet.avatar.yCur) {
         this.isTrans = false;
         GameMidlet.avatar.direct = 0;
         this.setFocus();
         if (action == -1) {
            if (indexItem != -1) {
               if (this.listSelectedCell.size() > 0 && indexItem != -1) {
                  label245: {
                     AvPosition var3 = (AvPosition)this.listSelectedCell.elementAt(0);
                     (var4 = (CellFarm)cell.elementAt(var3.anchor)).isSelected = false;
                     focusCell.x = var4.x / LoadMap.w;
                     focusCell.y = var4.y / LoadMap.w;
                     if (this.isChamSoc) {
                        if (isCellHarvestReady(var4)) {
                           this.pendingHarvestPromptCellIndex = var3.anchor;
                           this.pendingHarvestPromptResume = true;
                           FarmService.gI().doHervest(idFarm, var3.anchor);
                        } else {
                           boolean var5 = false;
                           if (var4.idTree != -1 && var4.statusTree < 6 && var4.status == 36) {
                              this.setAction(new IActionSet11(this, var4));
                              var5 = true;
                           }

                           if (var4.idTree != -1 && var4.statusTree < 6) {
                              if (var3.anchor >= cell.size()) {
                                 break label245;
                              }

                              if (var4.isWorm && this.setBonPhan(var3.anchor, 7)) {
                                 var5 = true;
                              }

                              if (var4.isGrass && this.setBonPhan(var3.anchor, 3)) {
                                 var5 = true;
                              }

                              if (var4.vitalityPer < 80) {
                                 this.doAutoFertilizeQuickCare(var3.anchor);
                              }
                           }

                           if (!var5) {
                              this.setGieoHat();
                           }
                        }
                     } else if (isCellHarvestReady(var4)) {
                        this.doHarvest();
                        this.setGieoHat();
                     } else {
                        this.setAction(new IActionSet1(this, var4));
                        this.setAction(new IActionVatPham12(this, var3));
                     }

                     this.listSelectedCell.removeElement(var3);
                  }
               }
            } else {
               indexItem = -1;
               this.doSellect();
            }
         }
      }

      int var2;
      if (idSelected != -1) {
         if (Canvas.a(2)) {
            Canvas.keyHold[2] = false;
            if ((var2 = idSelected) % 12 % 4 != 0) {
               --var2;
            }

            if (var2 >= 0) {
               idSelected = var2;
            }
         } else if (Canvas.a(4)) {
            Canvas.keyHold[4] = false;
            var2 = idSelected;
            var2 -= 4;
            if (var2 >= 0) {
               idSelected = var2;
            }
         } else if (Canvas.a(6)) {
            Canvas.keyHold[6] = false;
            var2 = idSelected;
            var2 += 4;
            if (var2 < cell.size()) {
               idSelected = var2;
            }
         } else if (Canvas.a(8)) {
            Canvas.keyHold[8] = false;
            if ((var2 = idSelected) % 12 % 4 != 3) {
               ++var2;
            }

            if (var2 < cell.size()) {
               idSelected = var2;
            }
         } else if (Canvas.a(5)) {
            label274: {
               var2 = LoadMap.w;
               if ((var4 = (CellFarm)cell.elementAt(idSelected)).idTree != -1 && var4.statusTree < 6) {
                  if (this.isChamSoc) {
                     if (!var4.isSelected) {
                        this.listSelectedCell.addElement(new AvPosition(var4.x / var2, var4.y / var2, idSelected));
                     }

                     var4.isSelected = true;
                     this.setGieoHat();
                     break label274;
                  }
               } else if (!this.isChamSoc) {
                  if (!var4.isSelected) {
                     this.listSelectedCell.addElement(new AvPosition(var4.x / var2, var4.y / var2, idSelected));
                  }

                  var4.isSelected = true;
                  this.setGieoHat();
                  break label274;
               }

               Canvas.startOKDlg(T.disagree);
            }
         }

         if (Canvas.stypeInt == 0) {
            var4 = (CellFarm)cell.elementAt(idSelected);
            AvCamera.gI().setToPos(var4.x, var4.y);
         }
      }

      int var9;
      int var11;
      CellFarm var13;
      if (Canvas.isPointerClick) {
         var2 = Canvas.px + AvCamera.gI().xCam;
         var9 = Canvas.py + AvCamera.gI().yCam;
         var11 = LoadMap.w * AvMain.hd;
         if (var9 / var11 * LoadMap.wMap + var2 / var11 >= 0 && var9 / var11 * LoadMap.wMap + var2 / var11 <= LoadMap.type.length && LoadMap.type[var9 / var11 * LoadMap.wMap + var2 / var11] == 51) {
            this.isTran = true;
            isSelected = true;
            var11 = this.getPosTreeByFocus(var2 / var11, var9 / var11);
            var13 = (CellFarm)cell.elementAt(var11);
            focusCell.x = var13.x / LoadMap.w;
            focusCell.y = var13.y / LoadMap.w;
         }
      }

      if (this.isTran && Canvas.isPointerRelease) {
         this.isTran = false;
         isSelected = false;
         var2 = Canvas.px + AvCamera.gI().xCam;
         var9 = Canvas.py + AvCamera.gI().yCam;
         var11 = LoadMap.w * AvMain.hd;
         if (!this.isSelectedCell && super.center != null && focusCell != null && var2 / var11 == focusCell.x && var9 / var11 == focusCell.y) {
            super.center.perform();
         } else if (var9 / var11 * LoadMap.wMap + var2 / var11 >= 0 && var9 / var11 * LoadMap.wMap + var2 / var11 <= LoadMap.type.length && LoadMap.type[var9 / var11 * LoadMap.wMap + var2 / var11] == 51) {
            var11 = this.getPosTreeByFocus(var2 / var11, var9 / var11);
            var13 = (CellFarm)cell.elementAt(var11);
            focusCell.x = var13.x / LoadMap.w;
            focusCell.y = var13.y / LoadMap.w;
            if (this.isSelectedCell && var11 >= 0 && var11 < cell.size()) {
               idSelected = var11;
               if (var13.idTree != -1 && !isCellHarvestReady(var13) && var13.statusTree < 6) {
                  Canvas.isPointerRelease = false;
                  if (this.isChamSoc) {
                     if (!var13.isSelected) {
                        this.listSelectedCell.addElement(new AvPosition(var2 / LoadMap.w, var9 / LoadMap.w, var11));
                     }

                     var13.isSelected = true;
                     this.setGieoHat();
                  } else if (!isCellHarvestReady(var13)) {
                     Canvas.startOKDlg(T.acc);
                  }
               } else {
                  Canvas.isPointerRelease = false;
                  if (this.isChamSoc && !isCellHarvestReady(var13)) {
                     Canvas.startOKDlg(T.viewRule);
                  } else {
                     if (!var13.isSelected) {
                        this.listSelectedCell.addElement(new AvPosition(var2 / LoadMap.w, var9 / LoadMap.w, var11));
                     }

                     var13.isSelected = true;
                     this.setGieoHat();
                  }
               }
            } else {
               Canvas.pxLast = Canvas.px -= LoadMap.w * AvMain.hd;
               this.isTrans = true;
            }
         }
      }

      if (Canvas.keyPressed[5] && (LoadMap.TYPEMAP == 24 || LoadMap.TYPEMAP == 53) && super.left != null && super.center == null) {
         super.left.perform();
      }

      super.updateKey();
      Canvas.loadMap.updateKey();
      if (action == -1) {
         GameMidlet.avatar.updateKey();
      }

   }

   private void setGieoHat() {
      if (this.listSelectedCell.size() > 0 && indexItem != -1) {
         this.isTrans = true;
         AvPosition var1 = (AvPosition)this.listSelectedCell.elementAt(0);
         if (GameMidlet.avatar.at == null) {
            LoadMap.posFocus = new AvPosition();
            GameMidlet.avatar.createAvatarArrays();
         }

         LoadMap.posFocus.x = var1.x * 24 - 24;
         LoadMap.posFocus.y = var1.y * 24 + 12;
         GameMidlet.avatar.task = -5;
         GameMidlet.avatar.isJumps = -1;
         GameMidlet.avatar.xCur = GameMidlet.avatar.x;
         GameMidlet.avatar.yCur = GameMidlet.avatar.y;
         GameMidlet.avatar.posFocus = LoadMap.posFocus;
         GameMidlet.avatar.findPath();
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
      Canvas.loadMap.paint(var1);
      Canvas.loadMap.paintBackGround(var1);
      if (idSelected >= 0) {
         if (this.n >= 8) {
            this.n = 0;
         }

         CellFarm var4 = (CellFarm)cell.elementAt(idSelected);
         var1.drawImage(MapScr.imgFocusP, var4.x * AvMain.hd, (var4.y - 24 + this.n / 2) * AvMain.hd, 3);
         ++this.n;
      } else if (Canvas.stypeInt == 0 && focusCell != null && focusCell.x != -1 && LoadMap.TYPEMAP != 25) {
         if (this.n >= 8) {
            this.n = 0;
         }

         var1.drawImage(MapScr.imgFocusP, (focusCell.x * LoadMap.w + LoadMap.w / 2) * AvMain.hd, (focusCell.y * LoadMap.w - 4 + this.n / 2) * AvMain.hd, 3);
         ++this.n;
      }

      if (LoadMap.TYPEMAP != 25) {
         Canvas.fontChatB.drawString(var1, this.nameFarm, (posName.x + 26) * AvMain.hd, (posName.y - 14) * AvMain.hd + (AvMain.hd - 1) * 7, 2);
      }

      Canvas.resetTrans(var1);
      LoadMap.paintEffectCamera(var1);
   }

   public static void a(Vector var0, Vector var1, Vector var2, Vector var3, byte var4, int var5, boolean var6) {
      itemSeed = var0;
      isNew = var6;
      levelStore = var4;
      int var7 = itemSeed.size();

      for(var4 = 0; var4 < var7; ++var4) {
         Item var8;
         TreeInfo var9;
         if ((var9 = FarmData.getTreeByID((var8 = (Item)itemSeed.elementAt(var4)).ID)) != null) {
            var8.name = var9.name;
         }
      }

      itemProduct = var1;

      for(var4 = 0; var4 < itemProduct.size(); ++var4) {
         setNameItem((Item)itemProduct.elementAt(var4));
      }

      listItemFarm = var2;
      listFarmProduct = var3;
   }

   private static void setNameItem(Item var0) {
      if (var0.ID < 50) {
         var0.price[0] = FarmData.getTreeByID(var0.ID).priceProduct;
         var0.name = FarmData.getTreeByID(var0.ID).name;
      } else if (var0.ID < 100) {
         var0.price[0] = FarmData.getAnimalByID(var0.ID).priceProduct;
         if (FarmData.getAnimalByID(var0.ID).area == 1) {
            var0.name = T.block2NoWin + " " + FarmData.getAnimalByID(var0.ID).name;
            return;
         }

         if (FarmData.getAnimalByID(var0.ID).area == 2) {
            if (var0.ID == 55) {
               var0.name = T.privateMsg + " " + FarmData.getAnimalByID(var0.ID).name;
               return;
            }

            var0.name = T.sixPointNoWin + " " + FarmData.getAnimalByID(var0.ID).name;
         }
      }

   }

   public static FarmItem getFarmItem(int var0) {
      for(int var1 = 0; var1 < FarmData.listItemFarm.size(); ++var1) {
         FarmItem var2;
         if ((var2 = (FarmItem)FarmData.listItemFarm.elementAt(var1)).ID == var0) {
            return var2;
         }
      }

      return null;
   }

   public static void onBuyItem(Item var0, int var1, int var2, int var3) {
      GameMidlet.avatar.updateMoney(var1, var2, var3);
      PopupShop.isTransFocus = true;
      if (var0.ID >= 50 && var0.ID <= 100) {
         cell = null;
      }

      if (var0.number > 0) {
         Item var4;
         if (var0.ID >= 111) {
            if ((var4 = Item.getItemByList(listItemFarm, var0.ID)) != null) {
               var4.number += var0.number;
            } else {
               FarmItem var5 = getFarmItem(var0.ID);
               var0.name = var5.des;
               listItemFarm.addElement(var0);
            }
         } else if (var0.ID <= 100 && var0.ID < 50) {
            if ((var4 = Item.getItemByList(itemSeed, var0.ID)) != null) {
               var4.number += var0.number;
            } else {
               itemSeed.addElement(var0);
               var0.name = FarmData.getTreeByID(var0.ID).name;
            }

            if (itemSeed.size() == 0) {
               itemSeed.addElement(var0);
            }
         }
      }

   }

   private void doAutoFertilizeQuickCare(int cellIndex) {
      if (cellIndex < 0 || cellIndex >= cell.size()) {
         return;
      }

      CellFarm c = (CellFarm)cell.elementAt(cellIndex);
      if (c == null || c.idTree == -1 || c.statusTree >= 6 || isCellHarvestReady(c)) {
         return;
      }

      if (c.vitalityPer >= 100) {
         return;
      }

      short superId = this.findAvailableFarmItemId((short)112);
      short midId = this.findAvailableFarmItemId((short)111);
      this.quickCareThrottleActive = true;

      boolean did = false;
      short chosenId = superId != -1 ? superId : midId;
      if (chosenId != -1) {
         did |= this.boostPlantUntilFullWithItem(cellIndex, chosenId) > 0;
      }
      if (!did && c.vitalityPer < 100) {
         did |= this.boostPlantUntilFull(cellIndex) > 0;
      }

      this.quickCareThrottleActive = false;
      if (did) {
         this.quickCareStart();
      } else {
         Canvas.startOKDlg("Không có phân bón để dùng.");
      }
   }

   private short findAvailableFarmItemId(short itemId) {
      for (int i = 0; i < listItemFarm.size(); i++) {
         Item it = (Item)listItemFarm.elementAt(i);
         if (it != null && it.ID == itemId && it.number > 0) {
            return itemId;
         }
      }
      return -1;
   }

   private void promptSowAfterHarvest(final int cellIndex, final boolean resumeAfter) {
      this.pendingHarvestSowPrompt = true;
      this.pendingHarvestSowCellIndex = cellIndex;
      this.pendingHarvestSowAskQueued = true;
      this.pendingHarvestSowAskCellIndex = cellIndex;
      this.pendingHarvestSowAskResumeAfter = resumeAfter;
      this.tryShowQueuedHarvestSowPrompt();
   }

   private void openSeedSelectMenuForCell(final int cellIndex, final boolean resumeAfter) {
      Vector cmds = new Vector();
      for (int i = 0; i < itemSeed.size(); i++) {
         Item it = (Item)itemSeed.elementAt(i);
         if (it == null || it.number <= 0) continue;
         final int seedIndex = i;
         cmds.addElement(new Command(it.name + " (" + it.number + ")", new IAction() {
            public void perform() {
               doPlantSeed(seedIndex, cellIndex);
               if (resumeAfter) {
                  FarmScr.this.setGieoHat();
               }
            }
         }));
      }

      if (cmds.size() == 0) {
         Canvas.startOKDlg("Không có hạt giống để gieo.");
         if (resumeAfter) {
            this.setGieoHat();
         }
         return;
      }

      Menu.gI().startAt(cmds, -1);
   }

   public final void onJoin(int var1, Vector var2, Vector var3, byte var4, byte var5, short var6, int var7) {
      numBarn = var4;
      numPond = var5;
      foodID = var6;
      remainTime = var7;
      idFarm = var1;
      if (var1 != GameMidlet.avatar.IDDB) {
         Avatar var10;
         if ((var10 = ListScr.getAvatar(var1)) == null) {
            Canvas.startOKDlg(T.notOnFarm);
            return;
         }

         if (var10.showName == null) {
            var10.setName(var10.name);
         }

         this.nameFarm = var10.showName;
         listFood[0].removeAllElements();
         listFood[1].removeAllElements();
      } else {
         this.nameFarm = GameMidlet.avatar.showName;
      }

      cell = var2;
      if (LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53 && animalLists.size() == 0) {
         animalLists = var3;
      }

      setAnimal();
      if (this.isJoin) {
         int var12;
         if (isReSize || LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53) {
            isReSize = false;
            focusCell = new AvPosition();
            action = -1;
            this.timeLimit = 0;
            Cattle.itemID = -1;
            Dog.itemID = -1;
            this.posTree = new AvPosition[4];
            Canvas.loadMap.load(25);
            Canvas.load = 0;
            var12 = var5 & 255;
            int var11 = var4 & 255;
            var12 = 0;
            var11 = 0;

            int var13;
            int barnX;
            try {
               numTilePond = FishFarm.WTile + var12;
               numTileBarn = Cattle.numTileW + var11;
               var13 = posPond.x / 24;
               barnX = posBarn.x / 24 + 2;
               InputStream var16;
               LoadMap.map = new short[(var16 = LoadMap.loadDataMap(25)).available()];

               for(int i = 0; i < LoadMap.map.length; ++i) {
                  LoadMap.map[i] = (short)var16.read();
               }

               short[] var17 = new short[LoadMap.map.length + LoadMap.Hmap * (var12 + var11)];
               int index = 0;

               for(int i = 0; i < LoadMap.map.length; ++i) {
                  var17[index] = LoadMap.map[i];
                  ++index;
                  int var8;
                  if (i % LoadMap.wMap == var13) {
                     for(var8 = 0; var8 < var12; ++var8) {
                        var17[index] = LoadMap.map[i];
                        ++index;
                     }
                  }

                  if (i % LoadMap.wMap == barnX) {
                     for(var8 = 0; var8 < var11; ++var8) {
                        var17[index] = LoadMap.map[i];
                        ++index;
                     }
                  }
               }

               LoadMap.wMap = (short)(LoadMap.wMap + var12 + var11);
               LoadMap.map = var17;
               LoadMap.treeLists.removeAllElements();
               Canvas.loadMap.setMap((InputStream)null, LoadMap.TYPEMAP + 1, true);
               Avatar var10000 = GameMidlet.avatar;
               var10000.x += var11 * 24;
               LoadMap.addObjTree(849, posPond.x + 12 + CRes.rnd(numTilePond - 2) * 24, posPond.y + 12 + CRes.rnd(3) * 24);
            } catch (Exception var17) {
               var17.printStackTrace();
            }

            listNest = new Vector();
            listBucket = new Vector();
            setChickNest(1, Chicken.s, (byte)87, -8, listNest);
            setChickNest(2, Cattle.posBucket, (byte)86, -7, listBucket);
            var13 = animalLists.size();

            for(barnX = 0; barnX < var13; ++barnX) {
               Animal var15;
               if ((var15 = (Animal)animalLists.elementAt(barnX)) instanceof FishFarm) {
                  ((FishFarm)var15).setInit();
               } else if (var15 instanceof Chicken) {
                  ((Chicken)var15).setInit();
               } else if (var15 instanceof Dog) {
                  ((Dog)var15).setInit();
               } else if (var15 instanceof Cattle) {
                  ((Cattle)var15).setInit();
               } else {
                  var15.setInit();
               }

               LoadMap.playerLists.addElement(var15);
            }

            Canvas.load = 1;
            Canvas.endDlg();
         }

         for(var12 = 0; var12 < LoadMap.treeLists.size(); ++var12) {
            SubObject var14;
            if ((var14 = (SubObject)LoadMap.treeLists.elementAt(var12)).type < 800 && var14.type >= 100 || var14.type == -3 || var14 instanceof CellFarm) {
               LoadMap.treeLists.removeElement(var14);
               --var12;
            }
         }

         this.setCellAll();
         this.curTime = System.currentTimeMillis();
         this.curTimeCooking = System.currentTimeMillis();
         if (Canvas.currentMyScreen != this) {
            this.switchToMe();
         }

         if (Canvas.isInitChar) {
            Welcome.goFarm();
         }

         GameMidlet.avatar.xCur = GameMidlet.avatar.x;
         GameMidlet.avatar.yCur = GameMidlet.avatar.y;
      }

      this.isJoin = true;
      if (xRemember != -1) {
         GameMidlet.avatar.x = GameMidlet.avatar.xCur = xRemember;
         GameMidlet.avatar.y = GameMidlet.avatar.yCur = yRemember;
         xRemember = -1;
         yRemember = -1;
      }

      super.left = aP;
      super.right = null;
      super.center = null;
   }

   private static void setChickNest(int var0, AvPosition var1, byte var2, int var3, Vector var4) {
      int var5 = 0;

      for(int var6 = 0; var6 < animalLists.size(); ++var6) {
         Animal var7;
         AnimalInfo var8;
         if ((var8 = FarmData.getAnimalByID((var7 = (Animal)animalLists.elementAt(var6)).species)).area == var0 && var8.iconProduct != -1) {
            boolean var11 = false;

            int var9;
            for(var9 = 0; var9 < var4.size(); ++var9) {
               if (((AvPosition)var4.elementAt(var9)).anchor == var7.species) {
                  var11 = true;
                  break;
               }
            }

            if (!var11) {
               var9 = var1.x + var5 * 24;
               var4.addElement(new AvPosition(var9, var1.y, var7.species));
               int var10 = LoadMap.getposMap(var9, var1.y);
               LoadMap.type[var10] = (short)var2;
               LoadMap.addObjTree(var3, var9, var1.y);
               ++var5;
            }
         }
      }

   }

   public static void setAnimal() {
      Vector var0 = new Vector();

      for(int var1 = 0; var1 < animalLists.size(); ++var1) {
         Animal var2;
         AnimalInfo var3 = FarmData.getAnimalByID((var2 = (Animal)animalLists.elementAt(var1)).species);
         if (var2 instanceof AnimalDan) {
            boolean var4 = false;

            for(int var5 = 0; var5 < var0.size(); ++var5) {
               AvPosition var6;
               if ((var6 = (AvPosition)var0.elementAt(var5)).anchor == var2.species) {
                  ((AnimalDan)var2).captainID = var6.x;
                  var4 = true;
                  break;
               }
            }

            if (!var4) {
               ((AnimalDan)var2).captainID = var2.IDDB;
               var0.addElement(new AvPosition(var2.IDDB, 0, var2.species));
            }
         }

         int var7;
         if ((var7 = var3.harvestTime * 60 / 3) > 0) {
            var2.period = var2.bornTime / var7;
         }

         if (var2.period > 2) {
            var2.period = 2;
         }

         if (var2.bornTime == -1 || var3.area == 3) {
            var2.period = 0;
         }
      }

   }

   public final void onPlantSeed(int var1, int var2) {
      Item var3;
      if ((LoadMap.TYPEMAP == 24 || LoadMap.TYPEMAP == 53) && (var3 = Item.getItemByList(itemSeed, var2)) != null) {
         CellFarm var4;
         (var4 = (CellFarm)cell.elementAt(var1)).idTree = var2;
         this.setStatusCell(var4, 4);
         LoadMap.map[var4.yCell * LoadMap.wMap + var4.xCell] = (short)var4.status;
         var4.statusTree = 0;
         var4.isGrass = false;
         var4.isWorm = false;
         var4.time = 0;
         var4.tempTime = 0L;
         var4.vitalityPer = 100;
         var4.hervestPer = 0;
         --var3.number;
         if (var3.number <= 0) {
            itemSeed.removeElement(var3);
         }
      }

      if (this.quickCarePendingPlantAcks > 0) {
         --this.quickCarePendingPlantAcks;
         if (this.quickCarePendingPlantAcks <= 0) {
            this.quickCarePendingPlantAcks = 0;
            this.quickCareSowInProgress = false;
         }
      }

   }

   public final void setInfoCell(int var1) {
      CellFarm var4;
      if ((var4 = (CellFarm)cell.elementAt(var1)).idTree == -1) {
         this.setStatusCell(var4, 2);
      } else {
         TreeInfo var2 = FarmData.getTreeInfoByID(var4.idTree);
         int full = var2.harvestTime * 60;
         int var3 = full / 5;
         if (var3 <= 0) {
            var3 = 1;
         }

         if (var4.time >= full || var4.hervestPer == 100) {
            var4.statusTree = 5;
         } else {
            var4.statusTree = var4.time / var3;
            if (var4.statusTree > 4) {
               var4.statusTree = 4;
            }
         }

         if (var4.time < 0 || var2.dieTime != -1 && var4.time - full > var2.dieTime * 60 || var4.statusTree < 0) {
            var4.statusTree = 6;
         }

         if (var4.isArid) {
            this.setStatusCell(var4, 3);
         } else {
            this.setStatusCell(var4, 4);
         }
      }

      LoadMap.map[var4.yCell * LoadMap.wMap + var4.xCell] = (short)var4.status;
   }

   private void setStatusCell(CellFarm var1, int var2) {
      if (var1.level == 2) {
         var1.status = this.typeCell1[var2];
      } else {
         var1.status = this.typeCell[var2];
      }

   }

   public static void onHarvestTree(int var0, int var1) {
      CellFarm var3 = (CellFarm)cell.elementAt(var0);
      TreeInfo var4;
      if (var1 > 0) {
         if ((var4 = FarmData.getTreeByID(var3.idTree)).isDynamic) {
            Canvas.addFlyText(var1, var3.xCell * LoadMap.w + 11, var3.yCell * LoadMap.w, -1, 0, var4.idImg[var3.statusTree], -1);
         } else {
            ImageInfo var5 = FarmData.listImgInfo[var4.idImg[var3.statusTree]];
            Canvas.addFlyText(var1, var3.xCell * LoadMap.w + 11, var3.yCell * LoadMap.w, -1, CRes.createRGBImage(var5.x0 * AvMain.hd, var5.y0 * AvMain.hd, var5.w * AvMain.hd, var5.h * AvMain.hd, FarmData.imgBig[var5.bigID]), -1);
         }
      }

      if (idFarm == GameMidlet.avatar.IDDB) {
         var3.statusTree = 6;
         var3.hervestPer = 100;
         var3.isGrass = false;
         var3.isWorm = false;
      }

      Item var6;
      if ((var4 = FarmData.getTreeByID(var3.idTree)).isDynamic) {
         if ((var6 = getItemProductByID(var4.productID)) != null) {
            var6.number += var1;
         } else {
            (var6 = new Item()).ID = var4.productID;
            var6.number = var1;
            var6.price[0] = var4.priceProduct;
            var6.name = var4.name;
            listFarmProduct.addElement(var6);
         }
      } else if ((var6 = Item.getItemByList(itemProduct, var4.ID)) != null) {
         var6.number += var1;
      } else {
         (var6 = new Item()).ID = var4.ID;
         var6.number = var1;
         var6.price[0] = FarmData.getTreeByID(var4.ID).priceProduct;
         var6.name = FarmData.getTreeByID(var4.ID).name;
         itemProduct.addElement(var6);
      }

      FarmScr scr = instance;
      if (scr != null && idFarm == GameMidlet.avatar.IDDB && scr.pendingHarvestPromptCellIndex == var0) {
         boolean resume = scr.pendingHarvestPromptResume;
         scr.pendingHarvestPromptCellIndex = -1;
         scr.pendingHarvestPromptResume = false;
         scr.promptSowAfterHarvest(var0, resume);
      } else if (scr != null && idFarm == GameMidlet.avatar.IDDB && scr.quickCarePendingHarvestTreeAcks > 0) {
         --scr.quickCarePendingHarvestTreeAcks;
         if (scr.quickCarePendingHarvestTreeAcks == 0 && scr.quickCarePendingSowPromptAfterHarvest) {
            scr.quickCarePendingSowPromptAfterHarvest = false;
            scr.quickCarePendingRangeSowAskQueued = true;
            scr.quickCarePendingRebuyAfterSow = true;
         }
      } else if (scr != null && idFarm == GameMidlet.avatar.IDDB && scr.pendingHarvestSowPrompt && scr.pendingHarvestSowCellIndex == var0) {
         scr.pendingHarvestSowPrompt = false;
         scr.pendingHarvestSowCellIndex = -1;
         scr.promptSowAfterHarvest(var0, false);
      }
   }

   public static void onHarvestAnimal(int var0, int var1) {
      Animal var6 = getAnimalByIndex(var0);
      if (var1 > 0 && var6 != null) {
         AnimalInfo var2;
         AnimalInfo var3 = var2 = FarmData.getAnimalByID(var6.species);
         Item var5;
         if ((var5 = Item.getItemByList(itemProduct, var3.species)) != null) {
            var5.number += var1;
         } else {
            (var5 = new Item()).ID = (short)var3.species;
            var5.number = var1;
            var5.name = var3.name;
            var5.price[0] = var3.priceProduct;
            setNameItem(var5);
            itemProduct.addElement(var5);
         }

         if (AvatarData.getImgIcon(var2.iconProduct) != null) {
            AvPosition var7 = null;
            if (var2.area == 1) {
               var7 = getPosO(listNest, var6.species);
            } else if (var2.area == 2) {
               var7 = getPosO(listBucket, var6.species);
            }

            if (var7 != null) {
               Canvas.addFlyText(var1, var7.x, var7.y - 25, -1, AvatarData.getImgIcon(var2.iconProduct).img, -1);
            }
         }
      }

   }

   private static AvPosition getPosO(Vector var0, int var1) {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         AvPosition var3;
         if ((var3 = (AvPosition)var0.elementAt(var2)).anchor == var1) {
            return var3;
         }
      }

      return null;
   }

   public static void onOpenLand(int var0, int var1, byte var2, String var3, int var4, int var5, int var6) {
      if (var0 == idFarm) {
         System.out.println("onOpenLand: " + var1 + "    " + var2);
         GameMidlet.avatar.updateMoney(var4, var5, var6);
         Canvas.startOKDlg(var3);
      }

   }

   public final void doJoinFarm(int var1, boolean var2) {
      this.isJoin = var2;
      FarmService.gI().doJoinFarm(var1);
   }

   public final void doSellProduct(int var1, String var2) {
      Canvas.startOKDlg(T.pw + " " + var2 + "?", new IActionSellProduct(this, var1));
   }

   public final void doOpenCuaHang() {
      Vector var1 = new Vector();

      int var2;
      for(var2 = 0; var2 < FarmData.treeInfo.length; ++var2) {
         CommandBuyItemCuaHang var4 = new CommandBuyItemCuaHang(this, T.selectt, 7, FarmData.treeInfo[var2].ID, var2);
         var1.addElement(var4);
      }

      var2 = FarmData.listAnimalInfo.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         AnimalInfo var6 = (AnimalInfo)FarmData.listAnimalInfo.elementAt(var3);
         CommandBuyAnimalCuaHang var7 = new CommandBuyAnimalCuaHang(this, T.selectt, 8, var3, var6, var3);
         var1.addElement(var7);
      }

      PopupShop.gI().switchToMe();
      PopupShop.gI().addElement(new String[]{T.seed, T.item, T.storePro}, new Vector[]{var1, this.goVatPham(), this.goKhoHang()}, (Vector)null);
      if (Canvas.isInitChar && !Welcome.isOut) {
         (Canvas.welcome = new Welcome()).initFarmPath(PopupShop.me);
      }

   }

   private Vector goVatPham() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < FarmData.listItemFarm.size(); ++var2) {
         FarmItem var3;
         if ((var3 = (FarmItem)FarmData.listItemFarm.elementAt(var2)).isItem && (var3.priceLuong > 0 || var3.priceXu > 0)) {
            var1.addElement(new CommandGoVatPham(this, T.selectt, 9, var2, var3, var2));
         }
      }

      return var1;
   }

   private Vector goKhoHang() {
      Vector var1 = new Vector();
      int var2 = itemProduct.size();

      int var3;
      Item var7;
      for(var3 = 0; var3 < var2; ++var3) {
         if (FarmData.getTreeByID((var7 = (Item)itemProduct.elementAt(var3)).ID) != null || var7.ID >= 50) {
            CommandGoKhoHang1 var5 = new CommandGoKhoHang1(this, T.sell, new IActionGoKhoHang1(this, var3), var3, var7);
            var1.addElement(var5);
         }
      }

      for(var3 = 0; var3 < listFarmProduct.size(); ++var3) {
         FarmItem var6 = getFarmItem((var7 = (Item)listFarmProduct.elementAt(var3)).ID);
         System.out.println("aaaaaaaaaa: " + var6 + "    " + var7.ID);
         var1.addElement(new CommandGoKhoHang2(this, "", 11, var3, var6, var3, var7));
      }

      return var1;
   }

   public final void doOpenKhoHang() {
      if (GameMidlet.avatar.IDDB != idFarm) {
         Canvas.startOKDlg(T.notOnFarmOther);
      } else {
         Vector var1 = new Vector();

         int var2;
         for(var2 = 0; var2 < itemSeed.size(); ++var2) {
            Item var4;
            if (FarmData.getTreeByID((var4 = (Item)itemSeed.elementAt(var2)).ID) != null) {
               CommandOpenKhoHang1 var3 = new CommandOpenKhoHang1(this, "", 12, var2, var4, var2);
               var1.addElement(var3);
            }
         }

         for(var2 = 0; var2 < listItemFarm.size(); ++var2) {
            CommandOpenKhoHang2 var6 = new CommandOpenKhoHang2(this, "", 13, var2, var2);
            var1.addElement(var6);
         }

         PopupShop.gI().switchToMe();
         PopupShop.gI().addElement(new String[]{T.storePro, T.StoreSeed}, new Vector[]{this.goKhoHang(), var1}, (Vector)null);

         for(int var5 = 0; var5 < itemProduct.size(); ++var5) {
            itemProduct.elementAt(var5);
         }
      }

   }

   public final void f(int var1, int var2) {
      if (var2 != 3 && !PopupShop.h()) {
         PopupShop.g();
         if (Canvas.isInitChar) {
            Canvas.welcome = new Welcome();
            if (Welcome.indexFarmPath > 2) {
               --Welcome.indexFarmPath;
            }

            Canvas.welcome.initFarmPath(PopupShop.me);
            return;
         }
      } else {
         int var3 = PopupShop.f();
         int var4 = 0;
         int var5 = 0;
         if (var2 == 0) {
            TreeInfo var7;
            var4 = (var7 = FarmData.getTreeInfoByID(var1)).priceSeed[0];
            var5 = var7.priceSeed[1];
         } else if (var2 == 2) {
            var4 = FarmData.getVPbyID(var1).price[0];
            var5 = FarmData.getVPbyID(var1).price[1];
         } else {
            FarmItem var8;
            if (var2 == 4 && (var8 = getFarmItem(var1)) != null) {
               var4 = var8.priceXu;
               var5 = var8.priceLuong;
            }
         }

         Canvas.getTypeMoney(var4 * var3, var5 * var3, new IActionXuTree(this, var1, var3, var4), new IActionEnd(this, var1, var3, var5), (IAction)null);
      }

   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.doSellect();
            return;
         case 1:
            if (Canvas.welcome == null || Welcome.isPaintArrow) {
               Menu.gI().startAt(this.K, -1);
               return;
            }
            break;
         case 2:
            FarmScr var10 = this;
            Vector var11 = new Vector();
            Animal var3;
            AnimalInfo var4 = FarmData.getAnimalByID((var3 = getAnimalByIndex(((Base)LoadMap.focusObj).IDDB)).species);

            int var5;
            Item var6;
            FarmItem var7;
            for(var5 = 0; var5 < listItemFarm.size(); ++var5) {
               if ((var7 = getFarmItem((var6 = (Item)listItemFarm.elementAt(var5)).ID)).type == var4.area && var7.action == 5 && (var4.area == 4 || var4.area == 1)) {
                  int var8 = var6.number;
                  if (var4.area == 4) {
                     var8 -= listFood[1].size();
                  } else if (var4.area == 1) {
                     var8 -= listFood[0].size();
                  }

                  var11.addElement(new CommandItem55(var10, var7.des + "(" + var8 + ")", new IActionItem55(var10, var6, var4), var7));
               }
            }

            for(var5 = 0; var5 < listItemFarm.size(); ++var5) {
               if ((var7 = getFarmItem((var6 = (Item)listItemFarm.elementAt(var5)).ID)).action != 5 && var7.type != 0 && (var7.type == var4.area || var7.type == 101 || var7.type == 100 && var4.area != 4) && (var7.action != 4 || var3.disease[0] || var3.disease[1]) && (var7.action != 6 || var3.health < 100)) {
                  var11.addElement(new CommandItem5(var10, var7.des + "(" + var6.number + ")", new IActionItem5(var10, var7, var6), var7));
               }
            }

            if (idFarm == GameMidlet.avatar.IDDB) {
               var11.addElement(new CommandSellAnimal(var10, T.sell, 2));
            }

            startMenuFarm(var11);
            return;
         case 3:
            this.doFeeding();
            return;
         case 4:
            this.r();
            return;
         case 5:
            super.left = aP;
            super.right = null;
            this.isSelectedCell = false;
            AvCamera.isFollow = false;
            this.isChamSoc = false;
            this.listSelectedCell.removeAllElements();

            for(var1 = 0; var1 < cell.size(); ++var1) {
               ((CellFarm)cell.elementAt(var1)).isSelected = false;
            }

            idSelected = -1;
            indexItem = -1;
            isSelected = false;
            return;
         case 6:
            this.doKhoGiong();
            return;
         case 7:
            this.isChamSoc = true;
            this.setAuto(0);
            return;
         case 8:
            isAutoVatNuoi = false;
            super.right = null;
            super.center = null;
            super.left = aP;
            this.indexAuto = 0;
            AvCamera.isFollow = false;
            return;
         case 9:
            ++this.indexAuto;
            this.commandActionPointer(10, -1);
            return;
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         default:
            break;
         case 51:
            FarmService.gI().doOpenLand(idFarm, 1);
            this.curTime = System.currentTimeMillis();
            this.doJoinFarm(idFarm, true);
            return;
         case 52:
            FarmService.gI().doOpenLand(idFarm, 2);
            this.curTime = System.currentTimeMillis();
            this.doJoinFarm(idFarm, true);
            return;
         case 53:
            this.setAction((byte)0, -1);
            Canvas.endDlg();
            return;
         case 54:
            this.doGoFarmWay();
            return;
         case CMD_FARM_QUICK_CARE:
            this.commandTab(7, -1);
            return;
         case CMD_FARM_CROP:
            this.openCropMenu();
            return;
         case CMD_FARM_ANIMAL:
            this.openAnimalMenu();
            return;
         case CMD_FARM_AUTO_CARE:
            this.commandActionPointer(10, -1);
            return;
         case CMD_FARM_KITCHEN:
            this.doOpenCooking();
            return;
         case CMD_FARM_SHOP:
            this.doOpenCuaHang();
            return;
         case CMD_FARM_WAREHOUSE:
            this.doOpenKhoHang();
            return;
         case CMD_FARM_LOBBY:
            ParkService.gI().doJoinPark(25, -1);
            return;
         case CMD_FARM_SWITCH_ACC:
            MapScr.exitGame();
            LoginScr.gI().openSwitchAccountSettingsForm();
            return;
         case CMD_FARM_LOGOUT:
            MapScr.exitGame();
            return;
         case CMD_FARM_EXIT:
            this.close();
            return;

         case CMD_CROP_HARVEST:
            this.doCropHarvestAll();
            return;
         case CMD_CROP_SOW:
            this.startRangeSowFlow();
            return;
         case CMD_CROP_TILL:
            this.tillAllLand();
            return;
         case CMD_CROP_FERTILIZE:
            this.doCropFertilizeAll();
            return;
         case CMD_CROP_PEST_WEED:
            this.doCropPestWeedAll();
            return;
         case CMD_CROP_WATER:
            this.doCropWaterAll();
            return;
         case CMD_ANIMAL_SELL:
            this.openAnimalSellMenu();
            return;
         case CMD_ANIMAL_COLLECT:
            this.doAnimalCollectAll();
            return;
         case CMD_ANIMAL_PUMP:
            this.doAnimalPumpAll();
            return;
         case CMD_ANIMAL_CURE:
            this.doAnimalCureAll();
            return;
      }

   }

   public final void doBuyAnimal(AnimalInfo var1) {
      Canvas.getTypeMoney(var1.price[0], var1.price[1], new IActionBuyAnimalXu(this, var1), new IActionBuyAnimalLuong(this, var1), (IAction)null);
   }

   public static void onKick() {
      if (LoadMap.TYPEMAP == 24 || LoadMap.TYPEMAP == 53) {
         Canvas.menuMain = null;
         Canvas.startOK(T.youAreBittenByDogByHound, 54, (AvMain)null);
      }

   }

   public static boolean doEat(short var0, int var1) {
      if (Item.getItemByList(listItemFarm, var0) == null) {
         return false;
      } else {
         FarmService.gI().doUsingItem(idFarm, var1, var0);
         return false;
      }
   }

   public final void doCattleFeeding(byte var1, byte var2) {
      Vector var6 = new Vector();

      for(int var3 = 0; var3 < listItemFarm.size(); ++var3) {
         Item var4;
         FarmItem var5;
         if (((var5 = getFarmItem((var4 = (Item)listItemFarm.elementAt(var3)).ID)).type == var1 || var5.type == 101) && var5.action == 5) {
            var6.addElement(new CommandThuoc(this, var5.des + "(" + var4.number + ")", new IActionCattleFeeding(this, var1, var4), var5));
         }
      }

      Menu.gI().startMenuFarm(var6, Canvas.hw, LoadMap.w * AvMain.hd, LoadMap.w * AvMain.hd);
   }

   private static void sendHarvestAnimal(Animal var0) {
      FarmService.gI().doHarvestAnimal(idFarm, var0.IDDB);
   }

   public final void doHarvestAnimal(int var1, int var2, Vector var3) {
      if (GameMidlet.avatar.IDDB == idFarm && var2 >= 0 && var2 < var3.size()) {
         AvPosition var6 = (AvPosition)var3.elementAt(var2);

         for(int var7 = 0; var7 < animalLists.size(); ++var7) {
            Animal var4;
            AnimalInfo var5 = FarmData.getAnimalByID((var4 = (Animal)animalLists.elementAt(var7)).species);
            if (var4.numEggOne > 0 && var6.anchor == var4.species) {
               var4.numEggOne = 0;
               if (var1 == 1 && var5.area == var1) {
                  if (this.quickCareThrottleActive) {
                     this.quickCareEnqueue((byte)0, var4.IDDB, 0);
                  } else {
                     sendHarvestAnimal(var4);
                  }
                  removePopup(-50);
               }

               if (var1 == 2 && var5.area == var1) {
                  if (this.quickCareThrottleActive) {
                     this.quickCareEnqueue((byte)0, var4.IDDB, 0);
                  } else {
                     sendHarvestAnimal(var4);
                  }
                  removePopup(-51);
               }
            }
         }
      }

   }

   public final void onSell(int var1, int var2, short var3) {
      GameMidlet.avatar.money[0] = var2;
      PopupShop.isTransFocus = true;
      Canvas.startOKDlg(T.moneySellPro + var1 + T.dola);
      Item var4;
      if ((var4 = Item.getItemByList(itemProduct, var3)) == null) {
         var4 = Item.getItemByList(listFarmProduct, var3);
         listFarmProduct.removeElement(var4);
      } else {
         itemProduct.removeElement(var4);
      }

      if (Canvas.currentMyScreen == PopupShop.gI()) {
         PopupShop.gI().close();
         if (LoadMap.TYPEMAP == 25) {
            this.doOpenCuaHang();
            PopupShop.gI().setTap(2);
         } else {
            this.doOpenKhoHang();
         }
      }

      Canvas.endDlg();
   }

   public static void onSellAnimal(int var0, int var1) {
      Animal var4;
      if ((var4 = getAnimalByIndex(var0)) != null) {
         int var2 = var1 - GameMidlet.avatar.money[0];
         LoadMap.focusObj = null;
         Image var3 = AvatarData.getImgIcon(FarmData.getAnimalByID(var4.species).idImg[var4.period]).img;
         Canvas.addFlyText(var2, var4.x, var4.y - 7, -1, CRes.createRGBImage(0, var4.indexFr * var4.height, var3.getWidth(), var4.height, var3), -1);
         animalLists.removeElement(var4);
         LoadMap.playerLists.removeElement(var4);
      }

      PopupShop.isTransFocus = true;
      GameMidlet.avatar.money[0] = var1;
      FarmScr scr2 = instance;
      if (scr2 != null) {
         scr2.tryFinalizeQuickCareRebuyFlow();
      }
   }

   public final void onPriceAnimal(byte var1, String var2) {
      if (this.isBatchSellAnimal) {
         FarmService.gI().doSellAnimal(idFarm, var1);
         --this.batchSellRemain;
         if (this.batchSellRemain <= 0) {
            this.isBatchSellAnimal = false;
            this.batchSellRemain = 0;
            this.tryFinalizeQuickCareRebuyFlow();
         }
      } else {
         Canvas.startOKDlg(var2, new IActionPriceAnimal(this, var1));
      }
   }

   public final void doGoFarmWay() {
      isSteal = false;
      isAbleSteal = false;
      Cattle.itemID = -1;
      Dog.itemID = -1;
      this.listHound = null;
      super.right = null;
      ParkService.gI().doJoinPark(25, -1);
   }

   public static Animal getAnimalByIndex(int var0) {
      for(int var1 = 0; var1 < animalLists.size(); ++var1) {
         Animal var2;
         if ((var2 = (Animal)animalLists.elementAt(var1)).IDDB == var0) {
            return var2;
         }
      }

      return null;
   }

   public final void doMenuStarFruit() {
      if (GameMidlet.avatar.IDDB == idFarm) {
         Vector var1 = new Vector();
         if (starFruil.numberFruit > 0) {
            var1.addElement(new CommandMenuStarFruit1(this, T.harvest + "(" + starFruil.numberFruit + ")", 12));
         }

         var1.addElement(new CommandMenuStarFruit2(this, starFruil.timeFinish > 0 ? T.QuickUpgrade : T.update, 13));
         var1.addElement(new CommandMenuStarFruit3(this, T.viewInfo, 14));
         startMenuFarm(var1);
      }

   }

   private static void removePopup(int var0) {
      for(int var1 = 0; var1 < LoadMap.treeLists.size(); ++var1) {
         SubObject var2;
         if ((var2 = (SubObject)LoadMap.treeLists.elementAt(var1)).catagory == 8 && var2.type == var0) {
            LoadMap.treeLists.removeElement(var2);
            return;
         }
      }

   }

   public final void doOpenCooking() {
      if (idFarm == GameMidlet.avatar.IDDB) {
         Vector var1 = new Vector();

         for(int var2 = 0; var2 < FarmData.listFood.size(); ++var2) {
            Food var3 = (Food)FarmData.listFood.elementAt(var2);
            var1.addElement(new CommandCooking1(this, T.cook, new IActionCooking1(this, var3), var3, var2));
         }

         Vector var5 = new Vector();
         if (foodID > 0) {
            var5.addElement((Object)null);
            CommandCooking2 var6 = new CommandCooking2(this, remainTime == 0 ? T.done : T.playNow, 2, this);
            var5.addElement(var6);
         }

         PopupShop.gI().switchToMe();
         PopupShop.gI().isFull = true;
         if (foodID > 0) {
            PopupShop.gI().addElement(new String[]{T.cook, T.cooking}, new Vector[]{var1, null}, var5);
            PopupShop.gI().setCmdLeft(new Command(T.cancel, 0, this), 1);
            PopupShop.focusTap = 1;
            PopupShop.gI().setCmyLim();
            PopupShop.gI().setCaption();
         } else {
            PopupShop.gI().addElement(new String[]{T.cook}, new Vector[]{var1}, (Vector)null);
         }
      }

   }

//   public final void doOpenComChao() {
//      Canvas.startOK("Menu Cơm cháo đang phát triển!");
//   }

   public static void onHarvestStarFruit(short var0, short var1) {
      for(int var2 = 0; var2 < starFruil.xFruit.length; ++var2) {
         Canvas.addFlyText(0, starFruil.x + starFruil.xFruit[var2], starFruil.y - 45 + starFruil.yFruit[var2], -1, 0, starFruil.fruitID, -1);
      }

      Canvas.addFlyText(var1, GameMidlet.avatar.x, GameMidlet.avatar.y - GameMidlet.avatar.height, -1, 10);
      starFruil.numberFruit = 0;
      Item var3;
      if ((var3 = getItemProductByID(var0)) != null) {
         var3.number += var1;
      } else {
         (var3 = new Item()).ID = var0;
         var3.number = var1;
         listFarmProduct.addElement(var3);
      }

      Canvas.endDlg();
   }

   public static Item getItemProductByID(int var0) {
      for(int var1 = 0; var1 < listFarmProduct.size(); ++var1) {
         Item var2;
         if ((var2 = (Item)listFarmProduct.elementAt(var1)).ID == var0) {
            return var2;
         }
      }

      return null;
   }

   public static Item getProductByID(int var0) {
      for(int var1 = 0; var1 < itemProduct.size(); ++var1) {
         Item var2;
         if ((var2 = (Item)itemProduct.elementAt(var1)).ID == var0) {
            return var2;
         }
      }

      return null;
   }

   public static void doMenuFarmFriend() {
      ListScr.gI().setFriendList(true);
   }

   static void setAction(FarmScr var0, byte var1, int var2) {
      var0.setAction(var1, var2);
   }

   static void a(FarmScr var0, CellFarm var1) {
      if (var1.idTree != -1 && var1.statusTree < 6) {
         Canvas.startOKDlg(T.youWantBreakTree, 53);
      } else {
         var0.setAction((byte)0, -1);
         Canvas.endDlg();
      }

   }

   static void a(FarmScr var0, int var1, int var2) {
      doPlantSeed(var1, var2);
   }

   static void a(FarmScr var0) {
      var0.setGieoHat();
   }

   static Vector getItemSeed() {
      return itemSeed;
   }

   static void a(FarmScr var0, FarmItem var1, short var2, Animal var3) {
      var0.setActionAnimal(var1, var2, var3);
   }
}
