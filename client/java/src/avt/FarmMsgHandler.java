package avt;

import java.io.IOException;
import java.util.Vector;
import main.Canvas;
import main.GameMidlet;

public final class FarmMsgHandler extends IService implements IMiniGameMsgHandler {
   public static FarmMsgHandler instance;

   public final void onMessage(Message var1) {
      try {
         int var2;
         int var3;
         int var4;
         int var6;
         Item var10;
         int[] var10000;
         int var70;
         int var71;
         short var72;
         int var76;
         int var79;
         int var80;
         int var83;
         short var85;
         short var86;
         Item var89;
         int var94;
         int var112;
         switch (var1.command) {
            case 51:
               byte var107;
               short[] var96 = new short[var107 = var1.reader().readByte()];
               short[] var97 = new short[var107];

               for(var94 = 0; var94 < var107; ++var94) {
                  var96[var94] = var1.reader().readShort();
                  var97[var94] = var1.reader().readShort();
               }

               var94 = var1.reader().readInt();
               var83 = var1.reader().readInt();
               FarmData.checkDataFarm(var107, var97, var94, var83);
               return;
            case 52:
            case 53:
            case 57:
            case 58:
            case 59:
            case 68:
            case 87:
            case 88:
            case 89:
            default:
               return;
            case 54:
               var86 = var1.reader().readShort();
               short var95 = var1.reader().readShort();
               var4 = var1.reader().readUnsignedShort();
               int avail = var1.reader().available();
               if (var4 > avail) {
                  var4 = avail;
               }
               byte[] var104 = new byte[var4];

               for(var6 = 0; var6 < var4; ++var6) {
                  var104[var6] = var1.reader().readByte();
               }

               FarmData.saveImgBig(var86, var95, var104);
               return;
            case 55:
               byte[] var101 = new byte[var1.reader().available()];
               var1.reader().read(var101);
               FarmData.saveImageData(var101);
               return;
            case 56:
               byte[] var106 = new byte[var1.reader().available()];
               var1.reader().read(var106);
               FarmData.saveTreeInfo(var106);
               return;
            case 60:
               var86 = (short)var1.reader().readByte();
               Vector var93 = new Vector();
               Vector var91 = new Vector();

               for(var94 = 0; var94 < var86; ++var94) {
                  Item var99;
                  (var99 = new Item()).ID = (short)var1.reader().readByte();
                  var99.number = var1.reader().readShort();
                  if (var99.ID > 100) {
                     var91.addElement(var99);
                  } else {
                     var93.addElement(var99);
                  }
               }

               byte var98 = var1.reader().readByte();
               Vector var100 = new Vector();

               Item var102;
               for(var2 = 0; var2 < var98; ++var2) {
                  (var102 = new Item()).ID = (short)var1.reader().readByte();
                  var102.number = var1.reader().readShort();
                  var100.addElement(var102);
               }

               GameMidlet.avatar.money[0] = var1.reader().readInt();
               GameMidlet.avatar.lvFarm = (short)var1.reader().readByte();
               GameMidlet.avatar.dirFirst = var1.reader().readByte();
               var86 = (short)var1.reader().readByte();
               var91 = new Vector();

               for(var94 = 0; var94 < var86; ++var94) {
                  (var102 = new Item()).ID = var1.reader().readShort();
                  var102.number = var1.reader().readShort();
                  var91.addElement(var102);
               }

               var98 = var1.reader().readByte();
               Vector var103 = new Vector();

               for(var2 = 0; var2 < var98; ++var2) {
                  Item var8;
                  (var8 = new Item()).ID = var1.reader().readShort();
                  var8.number = var1.reader().readShort();
                  var103.addElement(var8);
               }

               var86 = (short)var1.reader().readByte();
               int var105 = var1.reader().readInt();
               boolean var9 = var1.reader().readBoolean();
               GameMidlet.avatar.lvFarm = var1.reader().readShort();
               GameMidlet.avatar.dirFirst = var1.reader().readByte();
               var98 = var1.reader().readByte();
               var100.removeAllElements();

               Item var11;
               int var108;
               for(var108 = 0; var108 < var98; ++var108) {
                  (var11 = new Item()).ID = var1.reader().readShort();
                  var11.number = var1.reader().readInt();
                  var100.addElement(var11);
               }

               var103.removeAllElements();
               var98 = var1.reader().readByte();

               for(var108 = 0; var108 < var98; ++var108) {
                  (var11 = new Item()).ID = var1.reader().readShort();
                  System.out.println("pppppppp: " + var11.ID + "    " + var11.number);
                  var11.number = var1.reader().readInt();
                  var103.addElement(var11);
               }

               FarmScr.gI();
               FarmScr.setShopItems(var93, var100, var91, var103, (byte)var86, var105, var9);
               if (FarmData.playing == 0 && isFarmPlayable() && LoadMap.TYPEMAP != 25) {
                  FarmData.saveVersion();
                  ParkService.gI().doJoinPark(25, 0);
                  FarmScr.initImg();
                  FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, false);
                  return;
               }
               break;
            case 61:
               readFarmData(var1);
               return;
            case 62:
               (var10 = new Item()).ID = var1.reader().readShort();
               var10.number = var1.reader().readByte();
               var1.reader().readInt();
               var1.reader().readByte();
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               var83 = var1.reader().readInt();
               FarmScr.gI();
               FarmScr.onBuyItem(var10, var2, var3, var83);
               return;
            case 63:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               var85 = var1.reader().readShort();
               FarmScr.gI().onSell(var2, var3, var85);
               return;
            case 64:
               var1.reader().readInt();
               var86 = (short)var1.reader().readByte();
               byte var88 = var1.reader().readByte();
               FarmScr.gI().onPlantSeed(var86, var88);
               return;
            case 65:
               if (var1.reader().readByte() == 0) {
                  Canvas.startOKDlg(var1.reader().readUTF());
                  return;
               }

               if (FarmScr.getFarmItem(var85 = var1.reader().readShort()) != null && (var89 = Item.getItemByList(FarmScr.listItemFarm, var85)) != null) {
                  --var89.number;
                  if (var89.number <= 0) {
                     FarmScr.listItemFarm.removeElement(var89);
                     return;
                  }
               }
               break;
            case 66:
               var86 = (short)var1.reader().readByte();
               var85 = var1.reader().readShort();
               FarmScr.gI();
               FarmScr.onHarvestTree(var86, var85);
               return;
            case 67:
               FarmScr.gI();
               var1.reader().readInt();
               FarmScr.onKick();
               return;
            case 69:
               FarmScr.gI();
               FarmScr.showMoneyMenu(var1.reader().readUTF());
               return;
            case 70:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               byte var90 = var1.reader().readByte();
               String var5 = var1.reader().readUTF();
               var6 = var1.reader().readInt();
               int var7 = var1.reader().readInt();
               var83 = var1.reader().readInt();
               FarmScr.gI();
               FarmScr.onOpenLand(var2, var3, var90, var5, var6, var7, var83);
               return;
            case 71:
               (var89 = new Item()).ID = (short)var1.reader().readByte();
               var1.reader().readInt();
               var1.reader().readByte();
               var3 = var1.reader().readInt();
               var4 = var1.reader().readInt();
               var83 = var1.reader().readInt();
               FarmScr.gI();
               FarmScr.onBuyItem(var89, var3, var4, var83);
               return;
            case 72:
               var86 = (short)var1.reader().readByte();
               String var84 = var1.reader().readUTF();
               FarmScr.gI().onPriceAnimal((byte)var86, var84);
               return;
            case 73:
               var1.reader().readInt();
               var86 = (short)var1.reader().readByte();
               var83 = var1.reader().readInt();
               FarmScr.gI();
               FarmScr.onSellAnimal(var86, var83);
               return;
            case 74:
               var86 = (short)var1.reader().readByte();
               var83 = var1.reader().readShort();
               FarmScr.gI();
               FarmScr.onHarvestAnimal(var86, var83);
               return;
            case 75:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               GameMidlet.avatar.setMoney(var2);
               GameMidlet.avatar.money[1] = var3;
               Canvas.startOKDlg(var1.reader().readUTF());
               return;
            case 76:
               GlobalMessageHandler.readMove(var1);
               return;
            case 77:
               GlobalMessageHandler.readChat(var1);
               return;
            case 78:
               if (isFarmPlayable()) {
                  return;
               }

               var2 = var1.reader().readByte();
               CellFarm var87;
               (var87 = (CellFarm)FarmScr.cell.elementAt(var2)).idTree = var1.reader().readByte();
               readInfoCell(var87, var1);
               FarmScr.gI().setInfoCell(var2);
               return;
            case 79:
               if (isFarmPlayable()) {
                  return;
               }

               var2 = var1.reader().readByte();
               if ((var3 = var1.reader().readByte()) == -1) {
                  return;
               }

               Animal var64;
               (var64 = FarmScr.getAnimalByIndex(var2)).species = (byte)var3;
               readInfoAnimal(var64, var1);
               FarmScr.gI();
               FarmScr.setAnimal();
               return;
            case 80:
               if (var1.reader().readByte() == 0) {
                  String var66 = var1.reader().readUTF();
                  Vector var110;
                  (var110 = new Vector()).addElement(new Command(T.xu, 3, FarmScr.instance));
                  var110.addElement(new Command(T.gold, 4, FarmScr.instance));
                  var110.addElement(Canvas.ad);
                  Canvas.setInfoC(var66, var110);
                  return;
               }

               var1.reader().readByte();
               var1.reader().readInt();
               int var111 = var1.reader().readInt();
               var112 = var1.reader().readInt();
               var70 = var1.reader().readInt();
               GameMidlet.avatar.updateMoney(var111, var112, var70);
               Canvas.load = 1;
               FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, true);
               FarmScr.isReSize = true;
               return;
            case 81:
               if (var1.reader().readByte() == 0) {
                  String var109 = var1.reader().readUTF();
                  Vector var68;
                  (var68 = new Vector()).addElement(new Command(T.xu, 5, FarmScr.instance));
                  var68.addElement(new Command(T.gold, 6, FarmScr.instance));
                  var68.addElement(Canvas.ad);
                  Canvas.setInfoC(var109, var68);
                  return;
               }

               var1.reader().readByte();
               var1.reader().readInt();
               Canvas.load = 1;
               System.out.println("UPDATE_FARM_FISH: " + var1.reader().available());
               var112 = var1.reader().readInt();
               var70 = var1.reader().readInt();
               var71 = var1.reader().readInt();
               System.out.println("");
               GameMidlet.avatar.updateMoney(var112, var70, var71);
               FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, true);
               FarmScr.isReSize = true;
               return;
            case 82:
               short var67 = var1.reader().readShort();
               byte[] var69 = new byte[var1.reader().readShort()];
               var1.reader().read(var69);
               FarmData.listImgIcon.put("" + var67, new ImageIcon(CRes.createImage(var69)));
               return;
            case 83:
               if (var1.reader().readBoolean()) {
                  FarmScr.starFruil.productID = var1.reader().readShort();
                  ++FarmScr.starFruil.lv;
                  return;
               }
               break;
            case 84:
               if (var1.reader().readByte() == 0) {
                  Canvas.startOKDlg(var1.reader().readUTF(), 7, FarmScr.instance);
                  return;
               }

               var71 = var1.reader().readInt();
               var72 = var1.reader().readShort();
               var10000 = GameMidlet.avatar.money;
               var10000[0] -= var71;
               FarmScr.starFruil.timeFinish = var72 * 60;
               FarmScr.starFruil.time = System.currentTimeMillis();
               Canvas.addFlyText(-var71, GameMidlet.avatar.x, GameMidlet.avatar.y, -1, -1);
               return;
            case 85:
               var71 = var1.reader().readShort();
               var72 = var1.reader().readShort();
               FarmScr.gI();
               FarmScr.onHarvestStarFruit((short)var71, var72);
               return;
            case 86:
               if (var1.reader().readByte() == 0) {
                  Canvas.startOKDlg(var1.reader().readUTF(), 8, FarmScr.instance);
                  return;
               }

               var1.reader().readInt();
               FarmScr.starFruil.productID = var1.reader().readShort();
               FarmScr.starFruil.timeFinish = 0;
               ++FarmScr.starFruil.lv;
               int var113 = var1.reader().readInt();
               var76 = var1.reader().readInt();
               GameMidlet.avatar.updateMoney(GameMidlet.avatar.money[0], var113, var76);
               return;
            case 90:
               if (var1.reader().readByte() == 0) {
                  String var75 = var1.reader().readUTF();
                  Vector var115;
                  (var115 = new Vector()).addElement(new Command(T.xu, 9, FarmScr.instance));
                  var115.addElement(new Command(T.gold, 10, FarmScr.instance));
                  var115.addElement(Canvas.ad);
                  Canvas.setInfoC(var75, var115);
                  return;
               }

               var1.reader().readByte();
               var1.reader().readInt();
               byte var118 = var1.reader().readByte();
               Canvas.startOKDlg(var1.reader().readUTF());
               ++((CellFarm)FarmScr.cell.elementAt(var118)).level;
               FarmScr.gI().onJoin(FarmScr.idFarm, FarmScr.cell, FarmScr.animalLists, FarmScr.numBarn, FarmScr.numPond, FarmScr.foodID, FarmScr.remainTime);
               var79 = var1.reader().readInt();
               var80 = var1.reader().readInt();
               GameMidlet.avatar.updateMoney(GameMidlet.avatar.money[0], var79, var80);
               return;
            case 91:
               if ((var76 = var1.reader().readShort()) == -1) {
                  FarmScr.foodID = 0;
               } else {
                  short var117 = var1.reader().readShort();
                  FarmScr.foodID = (short)var76;
                  FarmScr.remainTime = var117 * 60;
               }

               Canvas.endDlg();
               return;
            case 92:
               Food var119;
               FarmItem var120 = FarmScr.getFarmItem((var119 = FarmData.getFoodByID(FarmScr.foodID)).productID);
               Item var121;
               if ((var121 = FarmScr.getItemProductByID(var119.productID)) != null) {
                  ++var121.number;
               } else {
                  (var10 = new Item()).ID = var119.productID;
                  var10.number = 1;
                  FarmScr.listFarmProduct.addElement(var10);
               }

               Canvas.addFlyText(0, FarmScr.xPosCook, FarmScr.yPosCook, -1, 0, var120.IDImg, -1);
               FarmScr.foodID = 0;
               return;
            case 93:
               if (var1.reader().readByte() == 0) {
                  Canvas.startOKDlg(var1.reader().readUTF(), 11, FarmScr.instance);
                  return;
               }

               int var78 = var1.reader().readInt();
               var10000 = GameMidlet.avatar.money;
               var10000[2] -= var78;
               FarmScr.remainTime = 0;
               var79 = var1.reader().readInt();
               var80 = var1.reader().readInt();
               GameMidlet.avatar.updateMoney(GameMidlet.avatar.money[0], var79, var80);
               return;
            case 94:
               if (var1.reader().readByte() == 0) {
                  String var114 = var1.reader().readUTF();
                  Vector var116;
                  (var116 = new Vector()).addElement(new Command(T.xu, 13, FarmScr.instance));
                  var116.addElement(new Command(T.gold, 14, FarmScr.instance));
                  var116.addElement(Canvas.ad);
                  Canvas.setInfoC(var114, var116);
                  return;
               }

               var76 = var1.reader().readByte();
               int var77 = var1.reader().readInt();
               if (var76 == 1) {
                  var10000 = GameMidlet.avatar.money;
                  var10000[0] -= var77;
               } else {
                  var10000 = GameMidlet.avatar.money;
                  var10000[2] -= var77;
               }

               var1.reader().readInt();
               ++FarmScr.levelStore;
               Canvas.startOKDlg(var1.reader().readUTF());
               FarmScr.gI().onJoin(FarmScr.idFarm, FarmScr.cell, FarmScr.animalLists, FarmScr.numBarn, FarmScr.numPond, FarmScr.foodID, FarmScr.remainTime);
               return;
         }
      } catch (Exception var54) {
         var54.printStackTrace();
      }

   }

   private static boolean isFarmPlayable() {
      return LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53;
   }

   private static void readInfoCell(CellFarm var0, Message var1) throws IOException {
      short var2 = var1.reader().readShort();
      FarmScr.startTextSmall(var0.time, var2, var0, (Animal)null);
      var0.time = var2;
      var0.tempTime = (long)(var0.time * 60);
      byte var5 = var1.reader().readByte();
      FarmScr.startTextSmall(var0.vitalityPer, var5, var0, (Animal)null);
      var0.vitalityPer = var5;
      var0.hervestPer = var1.reader().readByte();
      var0.isArid = var1.reader().readBoolean();
      boolean var6 = var1.reader().readBoolean();
      int var3 = -1;
      if (!var6 && var0.isWorm) {
         var3 = 0;
      }

      var0.isWorm = var6;
      boolean var4;
      if (!(var4 = var1.reader().readBoolean()) && var0.isGrass) {
         var3 = 9 * AvMain.hd;
      }

      var0.isGrass = var4;
      if (var3 >= 0) {
         Canvas.addFlyText(0, var0.xCell * LoadMap.w + 11, var0.yCell * LoadMap.w, -1, CRes.createRGBImage(0, var3, 13 * AvMain.hd, 9 * AvMain.hd, FarmScr.q.imgFrame), -1);
      }

   }

   private static void readInfoAnimal(Animal var0, Message var1) throws IOException {
      var0.bornTime = var1.reader().readInt();
      byte var2 = var1.reader().readByte();
      FarmScr.startTextSmall(var0.health, var2, (CellFarm)null, var0);
      var0.health = var2;
      var1.reader().readByte();
      var0.numEggOne = var1.reader().readByte();
      var0.hunger = var1.reader().readBoolean();
      boolean var5 = var1.reader().readBoolean();
      boolean var4 = var1.reader().readBoolean();
      int var3 = -1;
      if (!var5 && var0.disease[0]) {
         var3 = 0;
      }

      if (!var4 && var0.disease[1]) {
         var3 = 13 * AvMain.hd;
      }

      if (var3 >= 0) {
         Canvas.addFlyText(0, var0.x, var0.y - var0.height, -1, CRes.createRGBImage(0, var3, 9 * AvMain.hd, 13 * AvMain.hd, FarmScr.u.imgFrame), -1);
      }

      var0.disease[0] = var5;
      var0.disease[1] = var4;
   }

   private static void readFarmData(Message var0) throws IOException {
      int var1 = var0.reader().readInt();
      Vector var2 = new Vector();
      Vector var3 = new Vector();
      byte var4 = 0;
      int var8;
      byte var10;
      if (var1 != -1) {
         var4 = var0.reader().readByte();

         int var11;
         for(var11 = 0; var11 < var4; ++var11) {
            CellFarm var6;
            (var6 = new CellFarm()).idTree = var0.reader().readByte();
            if (var6.idTree == -1) {
               var2.addElement(var6);
            } else {
               readInfoCell(var6, var0);
               var2.addElement(var6);
            }
         }

         var10 = var0.reader().readByte();
         if (LoadMap.TYPEMAP != 24 || GameMidlet.avatar.IDDB != var1) {
            Cattle.numPig = 0;
            Dog.numBer = 0;
            Chicken.numChicken = 0;
            FarmScr.animalLists.removeAllElements();
         }

         for(var11 = 0; var11 < var10; ++var11) {
            Object var7 = null;
            var8 = var0.reader().readByte();
            int var9 = FarmScr.animalLists.size();
            if (LoadMap.TYPEMAP == 24 && var9 != 0 && var9 == var10) {
               FarmScr.getAnimalByIndex(var11);
               var7 = (Animal)FarmScr.animalLists.elementAt(var11);
            } else {
               AnimalInfo var14 = FarmData.getAnimalByID(var8);
               if (var8 != -1) {
                  switch (var14.area) {
                     case 1:
                        var7 = new Chicken(var11, (byte)var8, (byte)0);
                        break;
                     case 2:
                        var7 = new Cattle(var11, (byte)var8);
                        break;
                     case 3:
                        var7 = new Dog(var11, (byte)var8);
                        break;
                     case 4:
                        var7 = new FishFarm(var11, (byte)var8, (byte)0);
                  }
               }
            }

            if (var8 != -1 && var7 != null) {
               ((Animal)var7).species = (byte)var8;
               readInfoAnimal((Animal)var7, var0);
               var3.addElement(var7);
            }
         }
      }

      var10 = var0.reader().readByte();
      byte var12 = var0.reader().readByte();
      (FarmScr.starFruil = new StarFruitObj()).lv = var0.reader().readShort();
      FarmScr.starFruil.productID = var0.reader().readShort();
      FarmScr.starFruil.fruitID = var0.reader().readShort();
      FarmScr.starFruil.numberFruit = var0.reader().readShort();
      var0.reader().readShort();
      var0.reader().readShort();
      FarmScr.starFruil.timeFinish = var0.reader().readShort() * 60;
      FarmScr.starFruil.time = System.currentTimeMillis();

      for(int var13 = 0; var13 < var4; ++var13) {
         ((CellFarm)var2.elementAt(var13)).level = var0.reader().readByte();
      }

      short var15 = 0;
      var8 = 0;
      if (var0.reader().available() > 0) {
         var15 = var0.reader().readShort();
         var8 = var0.reader().readShort() * 60;
      }

      FarmScr.gI().onJoin(var1, var2, var3, var10, var12, var15, var8);
   }
}
