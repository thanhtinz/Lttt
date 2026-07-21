package avt;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class HouseScr extends MyScreen implements IChatable {
   public static HouseScr me;
   private int x;
   private int y;
   private int selected = -1;
   private Command cmdBrick;
   private Command cmdFinish;
   private Command cmdMenu;
   private static short numW = 0;
   public static boolean isSelectObj = false;
   private Vector listItem;
   public byte typeHome = -1;
   private int indexName = -1;
   public int isSelectedItem = -1;
   private int idHouse;
   public static boolean isChange = false;
   private Tile[] listTile;
   private AvPosition posSort;
   private AvPosition posJoin;
   private BigImgInfo imgTileMap;
   private Image imgBuyItem;
   private int xTemp = -1;
   private int yTemp = -1;
   private int[] color = new int[]{1688583, 14744065};
   public int f;
   public int g;
   private short IDHoa = 69;
   private short IDHo = 68;
   private short[] temp;
   private int C = 0;
   private Vector listP_Chest;
   private Vector listP_Con;
   private int moneyOnChest;
   private byte levelChest;

   public static HouseScr gI() {
      if (me == null) {
         me = new HouseScr();
      }

      return me;
   }

   public final void switchToMe() {
      super.switchToMe();
      this.i();
   }

   public HouseScr() {
      this.cmdBrick = new Command(T.sett, 0);
      this.cmdFinish = new Command(T.finish, 1);
      this.cmdMenu = new Command(T.menu, 2);
      FilePack.b(T.av);
      this.imgBuyItem = FilePack.getImage("hand");
      FilePack.reset();
   }

   private void i() {
      if (this.idHouse == GameMidlet.avatar.IDDB) {
         super.center = MapScr.gI().e;
         super.center.caption = T.selectt;
         super.left = this.cmdMenu;
      } else {
         super.left = this.cmdMenu;
         if (Canvas.stypeInt == 0) {
            super.center = MapScr.gI().e;
         }
      }

   }

   private void addPlayer() {
      LoadMap.addPlayer(GameMidlet.avatar);
      GameMidlet.avatar.x = this.posJoin.x;
      GameMidlet.avatar.y = this.posJoin.y;
      GameMidlet.avatar.action = 0;
      AvCamera.gI().setToPos(this.posJoin.x * AvMain.hd, this.posJoin.y * AvMain.hd);
   }

   public final void close() {
      MapScr.gI().doExit();
   }

   private void doOption() {
      super.center = new Command(T.selectt, 3);
      super.right = new Command(T.finish, 4);
      super.left = null;
      isChange = true;
      this.x = GameMidlet.avatar.x / 24;
      this.y = GameMidlet.avatar.y / 24;
      LoadMap.removePlayer(GameMidlet.avatar);
   }

   private void setStatusBuyItem() {
      HomeMsgHandler.onHandler();
      this.x = GameMidlet.avatar.x / 24;
      this.y = GameMidlet.avatar.y / 24;
      LoadMap.removePlayer(GameMidlet.avatar);
   }

   private static void doKick() {
      Vector var0 = new Vector();

      for(int var1 = 0; var1 < LoadMap.playerLists.size(); ++var1) {
         Base var2;
         if ((var2 = (Base)LoadMap.playerLists.elementAt(var1)).catagory == 0 && var2.IDDB != GameMidlet.avatar.IDDB) {
            var0.addElement(new Command(var2.name, 16, var1));
         }
      }

      Menu.gI().startAt(var0, 0);
   }

   private void doSelectMap() {
      this.setStatusBuyItem();
      if (this.temp == null) {
         this.temp = new short[LoadMap.map.length];

         for(int var1 = 0; var1 < LoadMap.map.length; ++var1) {
            this.temp[var1] = LoadMap.map[var1];
         }
      }

      isSelectObj = false;
      super.center = this.cmdBrick;
      super.right = this.cmdFinish;
      super.left = new Command(T.selectt, 5);
      Vector var4 = new Vector();

      for(int var2 = 0; var2 < this.listTile.length; ++var2) {
         if (this.listTile[var2].priceXu != -1 || this.listTile[var2].priceLuong != -1) {
            var4.addElement(new CommandMap(this, this.listTile[var2].name + "(" + Canvas.getPriceMoney(this.listTile[var2].priceXu, this.listTile[var2].priceLuong, true) + ")", 17, var2, var2));
         }
      }

      if (var4.size() > 0) {
         Menu.gI().startMenuFarm(var4, Canvas.hw, 27 * AvMain.hd, 27 * AvMain.hd);
      }

   }

   private void reset() {
      this.isSelectedItem = -1;
      this.selected = -1;
      isChange = false;
      isSelectObj = false;
      this.i();
      super.right = null;
      if (LoadMap.getAvatar(GameMidlet.avatar.IDDB) == null) {
         this.addPlayer();
      }

   }

   private void doSelectObject() {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < AvatarData.listMapItemType.size(); ++var2) {
         MapItemType var3;
         int var4;
         if ((var3 = (MapItemType)AvatarData.listMapItemType.elementAt(var2)).buy != 0 && (this.typeHome != 4 && (var3.buy == 1 || var3.buy == 2) || this.typeHome == 4) && (var4 = var3.name.indexOf(":")) != -1) {
            boolean var5 = false;
            String var6 = var3.name.substring(0, var4);

            for(var4 = 0; var4 < var1.size(); ++var4) {
               if (((Command)var1.elementAt(var4)).caption.equals(var6)) {
                  var5 = true;
               }
            }

            if (!var5 || var1.size() == 0) {
               var1.addElement(new Command(var6, 18, var2));
            }
         }
      }

      Menu.gI().startAt(var1, 2);
   }

   private void doSelectedItem(String var1) {
      this.reset();
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < AvatarData.listMapItemType.size(); ++var3) {
         MapItemType var4;
         int var5 = (var4 = (MapItemType)AvatarData.listMapItemType.elementAt(var3)).name.indexOf(var1);
         if (var4.buy != 0 && var5 != -1 && (this.typeHome != 4 && (var4.buy == 1 || var4.buy == 2) || this.typeHome == 4)) {
            String var8 = var4.name.substring(var4.name.indexOf(":") + 1);
            String var6 = Canvas.getPriceMoney(var4.priceXu, var4.priceLuong, true);
            var2.addElement(new CommandItem(this, "", new IActionItem(this, var3, var1), var4, 90, var6, var8));
         }
      }

      if (var2.size() > 0) {
         Menu.gI().startMenuFarm(var2, Canvas.hw, 90, 90);
         Menu.iNo = new IActionNo(this);
      }

   }

   private boolean isDisable(MapItemType var1) {
      if (var1.buy != 2 && var1.buy != 4) {
         if (LoadMap.type[this.y * LoadMap.wMap + this.x] != 80) {
            Canvas.startOKDlg(T.noPlaceItemHere);
            return true;
         }

         for(int var5 = 0; var5 < var1.listNotTrans.size(); ++var5) {
            AvPosition var6 = (AvPosition)var1.listNotTrans.elementAt(var5);
            if (LoadMap.type[(this.y + var6.y) * LoadMap.wMap + this.x + var6.x] != 80) {
               Canvas.startOKDlg(T.noPlaceItemHere);
               return true;
            }
         }
      } else {
         String var2 = "";

         int var3;
         for(var3 = 0; var3 < this.listItem.size(); ++var3) {
            MapItem var4 = (MapItem)this.listItem.elementAt(var3);
            if (var3 != this.isSelectedItem && var4.typeID == var1.idType && this.x == var4.x / 24 && this.y == var4.y / 24) {
               var2 = T.haveItem;
               break;
            }
         }

         if (!var2.equals("")) {
            Canvas.startOKDlg(var2);
            return true;
         }

         if (var1.buy == 2 || var1.buy == 4) {
            var3 = (this.y - 1) * LoadMap.wMap + this.x;
            if (LoadMap.map[var3] < numW || LoadMap.map[this.y * LoadMap.wMap + this.x] >= numW) {
               Canvas.startOKDlg(T.setTuong);
               return true;
            }
         }
      }

      return false;
   }

   public final void onBuyItemHouse(MapItem var1) {
      if (isSetTuong(var1)) {
         ++var1.y;
      }

      this.listItem.addElement(var1);
      LoadMap.treeLists.addElement(var1);
      this.setType(var1);
      LoadMap.orderVector(LoadMap.treeLists);
   }

   public final void updateKey() {
      super.updateKey();
      if (!isChange) {
         Canvas.loadMap.updateKey();
         GameMidlet.avatar.updateKey();
      } else {
         boolean var1 = false;
         if (Canvas.a(2)) {
            if (!setCollision(this.x, this.y - 1)) {
               --this.y;
            }

            if (this.y < 0) {
               this.y = 0;
            }

            var1 = true;
         } else if (Canvas.a(4)) {
            if (!setCollision(this.x - 1, this.y)) {
               --this.x;
            }

            if (this.x < 0) {
               this.x = 0;
            }

            var1 = true;
            GameMidlet.avatar.direct = Base.LEFT;
         } else if (Canvas.a(6)) {
            if (!setCollision(this.x + 1, this.y)) {
               ++this.x;
            }

            if (this.x >= LoadMap.wMap) {
               this.x = LoadMap.wMap - 1;
            }

            var1 = true;
            GameMidlet.avatar.direct = 0;
         } else if (Canvas.a(8)) {
            if (!setCollision(this.x, this.y + 1)) {
               ++this.y;
            }

            if (this.y >= LoadMap.Hmap) {
               this.y = LoadMap.Hmap - 1;
            }

            var1 = true;
         }

         if (Canvas.isPointerRelease) {
            int var3 = (AvCamera.gI().xCam + Canvas.px) / (LoadMap.w * AvMain.hd);
            int var2 = (AvCamera.gI().yCam + Canvas.py) / (LoadMap.w * AvMain.hd);
            if (var3 == this.x && var2 == this.y && super.center != null) {
               super.center.perform();
            }

            this.x = var3;
            this.y = var2;
            var1 = true;
            Canvas.isPointerRelease = false;
         }

         if (var1) {
            GameMidlet.avatar.x = this.x * 24 + 12;
            GameMidlet.avatar.y = this.y * 24 + 12;
            if (this.isSelectedItem != -1 && this.listItem.size() > 0) {
               MapItem var4;
               (var4 = (MapItem)this.listItem.elementAt(this.isSelectedItem)).x = this.x * 24;
               var4.y = this.y * 24;
               LoadMap.orderVector(LoadMap.treeLists);
            }
         }
      }

   }

   private static boolean setCollision(int var0, int var1) {
      return LoadMap.map[var1 * LoadMap.wMap + var0] == LoadMap.imgMap.nFrame - 2 || LoadMap.map[var1 * LoadMap.wMap + var0] == -1;
   }

   public final void update() {
      MapScr.gI().update();
      if (!isChange && !isSelectObj && super.right == null && MapScr.gI().right != null) {
         super.right = LoadMap.cmdNext;
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      super.paint(var1);
      Canvas.paintPlus(var1);
   }

   public final void paintMain(Graphics var1) {
      Canvas.loadMap.paint(var1);
      if (isChange && Canvas.menuMain == null) {
         Graphics var3 = var1;
         HouseScr var2 = this;
         int var6;
         if (!isSelectObj && this.isSelectedItem == -1) {
            if (this.selected != -1) {
               var6 = 0;

               while(true) {
                  if (var6 >= LoadMap.type.length) {
                     LoadMap.imgMap.drawFrame(var2.selected, var2.x * 24 * AvMain.hd, var2.y * 24 * AvMain.hd, 0, 0, var3);
                     break;
                  }

                  if (var2.indexName == 0 && LoadMap.map[var6] >= numW && LoadMap.map[var6] < var2.listTile.length && (var2.listTile[LoadMap.map[var6]].priceLuong != -1 || var2.listTile[LoadMap.map[var6]].priceXu != -1) || var2.indexName == 1 && LoadMap.map[var6] < numW) {
                     var2.paintIndex(var3, 2 + var6 % LoadMap.wMap * 24, 2 + var6 / LoadMap.wMap * 24, 0, 20);
                  }

                  ++var6;
               }
            }
         } else if (this.selected != -1) {
            MapItemType var4;
            if ((var4 = (MapItemType)AvatarData.listMapItemType.elementAt(this.selected)).buy != 2 && var4.buy != 4) {
               for(var6 = 0; var6 < LoadMap.type.length; ++var6) {
                  if (LoadMap.type[var6] == 80 && (var6 % LoadMap.wMap != var2.x || var6 / LoadMap.wMap != var2.y)) {
                     var2.paintIndex(var3, 2 + var6 % LoadMap.wMap * 24, 2 + var6 / LoadMap.wMap * 24, 0, 20);
                  }
               }
            } else {
               for(var6 = 0; var6 < LoadMap.map.length; ++var6) {
                  if (var6 > 0 && LoadMap.map[var6] < numW && LoadMap.map[var6 - LoadMap.wMap] >= numW) {
                     var2.paintIndex(var3, 2 + var6 % LoadMap.wMap * 24, 2 + var6 / LoadMap.wMap * 24, 0, 20);
                  }
               }
            }
         }

         var2.paintIndex(var3, var2.x * 24, var2.y * 24, 1, 24);
      }

      Canvas.loadMap.paintBackGround(var1);
      if (isChange) {
         if (isSelectObj && this.selected != -1) {
            MapItemType var5 = (MapItemType)AvatarData.listMapItemType.elementAt(this.selected);
            AvatarData.paintImg(var1, var5.imgID, (this.x * 24 + var5.dx) * AvMain.hd, (this.y * 24 + var5.dy) * AvMain.hd, 0);
         }

         if (Canvas.menuMain == null) {
            var1.drawImage(this.imgBuyItem, (this.x * 24 + 12) * AvMain.hd, (this.y * 24 + this.C) * AvMain.hd, 33);
         }

         if (this.indexName != -1) {
            Canvas.borderFont.drawString(var1, this.listTile[this.selected].name + "(" + Canvas.getPriceMoney(this.listTile[this.selected].priceXu, this.listTile[this.selected].priceLuong, true) + ")", (this.x * 24 + 12) * AvMain.hd, (this.y * 24 - 40) * AvMain.hd, 2);
         }

         ++this.C;
         if (this.C > 5) {
            this.C = 0;
         }
      }

      Canvas.resetTrans(var1);
      LoadMap.paintEffectCamera(var1);
   }

   private void paintIndex(Graphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(this.color[var4]);
      var1.drawRect(var2 * AvMain.hd, var3 * AvMain.hd, (var5 - 1) * AvMain.hd, (var5 - 1) * AvMain.hd);
   }

   public final void onJoin(byte var1, int var2, short[] var3, byte var4, Vector var5, Vector var6) {
      this.typeHome = var1;
      this.idHouse = var2;
      this.listItem = var5;
      LoadMap.wMap = (short)var4;
      LoadMap.Hmap = (short)(var3.length / var4);
      LoadMap.map = var3;
      if (this.typeHome == 4) {
         Canvas.loadMap.load(111);
      } else {
         Canvas.loadMap.load(68 + this.typeHome);
      }

      LoadMap.rememMap = -1;
      int doorStartX = -1;
      int doorWidth = 0;

      int var8;
      int var14;
      for(var8 = 0; var8 < var4; ++var8) {
         for(var14 = 0; var14 < LoadMap.Hmap; ++var14) {
            if (LoadMap.map[var14 * var4 + var8] < numW) {
               LoadMap.type[var14 * var4 + var8] = 80;
            } else {
               LoadMap.type[var14 * var4 + var8] = 88;
            }
         }

         if (LoadMap.map[(LoadMap.Hmap - 1) * var4 + var8] == this.imgTileMap.img.getHeight() / (24 * AvMain.hd) - 1) {
            LoadMap.map[(LoadMap.Hmap - 1) * var4 + var8] = LoadMap.map[(LoadMap.Hmap - 2) * var4 + var8];
            LoadMap.type[(LoadMap.Hmap - 1) * var4 + var8] = 21;
            ++doorWidth;
            if (doorStartX == -1) {
               doorStartX = var8 * 24;
            }
         }
      }

      int spawnX;
      int spawnY = LoadMap.Hmap * 24 - 30;
      if (doorStartX != -1 && doorWidth > 0) {
         spawnX = doorStartX + doorWidth * 24 / 2;
      } else {
         int fallback = -1;

         for(int i = 0; i < LoadMap.map.length; ++i) {
            if (LoadMap.type[i] == 80) {
               fallback = i;
               break;
            }
         }

         if (fallback != -1) {
            spawnX = fallback % LoadMap.wMap * 24 + 12;
            spawnY = fallback / LoadMap.wMap * 24 + 12;
         } else {
            spawnX = LoadMap.wMap * 12;
         }
      }

      int spawnTileX = spawnX / 24;
      int spawnTileY = spawnY / 24;
      int spawnIndex = spawnTileY * LoadMap.wMap + spawnTileX;
      int spawnType = spawnIndex >= 0 && spawnIndex < LoadMap.type.length ? LoadMap.type[spawnIndex] : -999;
      int spawnMapVal = spawnIndex >= 0 && spawnIndex < LoadMap.map.length ? LoadMap.map[spawnIndex] : -999;
      int doorTileStart = doorStartX == -1 ? -1 : doorStartX / 24;
      int bottomRow = LoadMap.Hmap - 1;
      int firstBottomIndex = bottomRow * var4;
      int firstBottomMapVal = firstBottomIndex >= 0 && firstBottomIndex < LoadMap.map.length ? LoadMap.map[firstBottomIndex] : -999;
      int firstBottomTypeVal = firstBottomIndex >= 0 && firstBottomIndex < LoadMap.type.length ? LoadMap.type[firstBottomIndex] : -999;
      System.out.println("[HouseJoin] typeHome=" + this.typeHome + ", idHouse=" + this.idHouse + ", wMap=" + LoadMap.wMap + ", hMap=" + LoadMap.Hmap + ", doorStartX=" + doorStartX + ", doorTileStart=" + doorTileStart + ", doorWidth=" + doorWidth + ", spawnX=" + spawnX + ", spawnY=" + spawnY + ", spawnTile=(" + spawnTileX + "," + spawnTileY + "), spawnIndex=" + spawnIndex + ", mapVal=" + spawnMapVal + ", typeVal=" + spawnType + ", firstBottomMapVal=" + firstBottomMapVal + ", firstBottomTypeVal=" + firstBottomTypeVal + ")");

      this.posJoin = new AvPosition(spawnX, spawnY);
      GameMidlet.avatar.x = this.posJoin.x;
      GameMidlet.avatar.y = this.posJoin.y;
      Pet var11;
      if ((var11 = LoadMap.getPet(GameMidlet.avatar.IDDB)) != null) {
         var11.setPos(GameMidlet.avatar.x, GameMidlet.avatar.y);
         var11.reset();
      }

      AvCamera.gI().init(70 + this.typeHome);
      LoadMap.imgMap = new FrameImage(this.imgTileMap.img, 24 * AvMain.hd, 24 * AvMain.hd);

      for(var14 = 0; var14 < var6.size(); ++var14) {
         Avatar var7;
         (var7 = (Avatar)var6.elementAt(var14)).xCur = var7.x;
         var7.yCur = var7.y;
         if (var7.IDDB != GameMidlet.avatar.IDDB) {
            LoadMap.addPlayer(var7);
         }
      }

      var14 = 0;
      var1 = 0;

      for(var2 = 0; var2 < this.listItem.size(); ++var2) {
         MapItem var12;
         if ((var12 = (MapItem)this.listItem.elementAt(var2)).x == 0 && var12.y == 0) {
            boolean var13 = false;

            for(int var16 = 0; var16 < LoadMap.map.length; ++var16) {
               if (LoadMap.type[var16] == 80) {
                  var12.x = var16 % LoadMap.wMap * 24;
                  var12.y = var16 / LoadMap.wMap * 24;
                  var14 = var12.x;
                  var1 = (byte)var12.y;
                  var13 = true;
                  this.setType(var12);
                  AvatarService.gI().doSortItem(var12.typeID, 0, 0, var12.x / 24, var12.y / 24, var12.dir);
                  break;
               }
            }

            if (!var13) {
               var12.x = var14;
               var12.y = var1;
               AvatarService.gI().doSortItem(var12.typeID, 0, 0, var12.x / 24, var12.y / 24, var12.dir);
            }
         }

         if (isSetTuong(var12)) {
            ++var12.y;
         }
      }

      MapScr.gI().move();
      Vector var10 = this.listItem;
      HouseScr var9 = this;

      for(var8 = 0; var8 < var10.size(); ++var8) {
         MapItem var15 = (MapItem)var10.elementAt(var8);
         LoadMap.treeLists.addElement(var15);
         var9.setType(var15);
      }

      LoadMap.orderVector(LoadMap.treeLists);
      this.switchToMe();
      Canvas.endDlg();
   }

   private static boolean isSetTuong(MapItem var0) {
      if (AvatarData.getMapItemTypeByID(var0.typeID).buy != 2 && AvatarData.getMapItemTypeByID(var0.typeID).buy != 4) {
         int var1 = (var0.y / 24 - 1) * LoadMap.wMap + var0.x / 24;
         if (LoadMap.map[var1] >= numW && LoadMap.map[var0.y / 24 * LoadMap.wMap + var0.x / 24] < numW) {
            return true;
         }
      }

      return false;
   }

   private BigImgInfo loadTileMap() {
      DataInputStream var1;
      if ((var1 = AvatarData.loadRMS("avatarTileMap")) == null) {
         return null;
      } else {
         this.imgTileMap = new BigImgInfo();

         try {
            this.imgTileMap.ver = var1.readShort();
            numW = var1.readShort();
            byte[] var2 = new byte[var1.available()];
            var1.read(var2);
            this.imgTileMap.img = CRes.createImage(var2);
            var1.close();
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         return this.imgTileMap;
      }
   }

   public final void saveTileMap(byte[] var1, int var2) {
      numW = (short)var2;
      this.imgTileMap.img = CRes.createImage(var1);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      DataOutputStream var4 = new DataOutputStream(var3);

      try {
         var4.writeShort(this.imgTileMap.ver);
         var4.writeShort(var2);
         var4.write(var1);
         CRes.saveRMS("avatarTileMap", var3.toByteArray());
         var4.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      if (MapScr.idHouse != -1) {
         AvatarService.gI().doJoinHouse(MapScr.idHouse);
         MapScr.idHouse = -1;
      } else {
         Canvas.endDlg();
      }

   }

   public final void commandTab(int var1, int var2) {
      Vector var7;
      switch (var1) {
         case 0:
            if (this.selected == -1) {
               return;
            }

            var2 = this.y * LoadMap.wMap + this.x;
            if (this.listTile[LoadMap.map[var2]].priceLuong == -1 && this.listTile[LoadMap.map[var2]].priceXu == -1) {
               Canvas.startOKDlg(T.noPlaceItemHere);
               break;
            }

            if ((this.selected >= numW || LoadMap.map[var2] < numW) && (this.selected < numW || LoadMap.map[var2] >= numW)) {
               this.xTemp = this.x;
               this.yTemp = this.y;
               LoadMap.map[this.y * LoadMap.wMap + this.x] = (short)this.selected;
               return;
            }

            Canvas.startOKDlg(T.noPlaceItemHere);
            break;
         case 1:
            this.selected = -1;
            this.indexName = -1;
            this.xTemp = -1;
            this.yTemp = -1;
            boolean var6 = false;

            for(var2 = 0; var2 < this.temp.length; ++var2) {
               if (this.temp[var2] != LoadMap.map[var2]) {
                  var6 = true;
                  break;
               }
            }

            if (var6) {
               AvatarService.gI().doCreateHome(LoadMap.map, 0);
               Canvas.startWaitDlg();
            }

            this.addPlayer();
            isChange = false;
            this.i();
            super.right = null;
            return;
         case 2:
            var7 = new Vector();
            if (this.idHouse == GameMidlet.avatar.IDDB) {
               var7.addElement(new Command(T.container, 1));
               var7.addElement(new Command(T.homeRepait, 2));
               var1 = 0;

               for(int var3 = 0; var3 < LoadMap.playerLists.size(); ++var3) {
                  if (((MyObject)LoadMap.playerLists.elementAt(var3)).catagory == 0) {
                     ++var1;
                  }
               }

               if (var1 > 1) {
                  var7.addElement(new Command(T.kick, 3));
               }

               var7.addElement(new Command(T.setPass, 4));
            }

            var7.addElement(new Command(T.exit, 5));
            Menu.gI().startAt(var7, 0);
            return;
         case 3:
            (var7 = new Vector()).addElement(new Command(T.move, 11));
            var7.addElement(new Command(T.rota, 12));
            var7.addElement(new Command(T.sell, 13));
            Menu.gI().startAt(var7, 2);
            Menu var10000 = Menu.gI();
            int var10001 = this.x * 24 * AvMain.hd - AvCamera.gI().xCam - Menu.gI().menuW / 2 + 12;
            int var4 = this.y * 24 * AvMain.hd - AvCamera.gI().yCam - Menu.gI().menuH - 12;
            var10000.menuX = var10001;
            var10000.menuY = var4;
            if (var10000.menuX < 0) {
               var10000.menuX = 0;
            }

            if (var10000.menuY < 0) {
               var10000.menuY = 0;
            }

            return;
         case 4:
            this.reset();
            return;
         case 5:
            this.doSelectMap();
            return;
         case 8:
            InputFace.gI();
            Canvas.currentFace = null;
            return;
         case 50:
            AvatarService.gI().doCreateHome(LoadMap.map, 1);
            Canvas.startWaitDlg();
            return;
         case 51:
            LoadMap.map = this.temp;
            this.temp = null;
            ParkMsgHandler.onHandler();
            return;
         case 53:
            GlobalService.gI().doUpdateChest(0);
            Canvas.startWaitDlg();
            return;
         case 100:
            AvatarService.gI().doSetPassMyHouse(Canvas.inputDlg.getText(), 0, 0);
            Canvas.endDlg();
            return;
         case 101:
            GlobalService.gI().doEnterPass(Canvas.inputDlg.getText(), (byte)0);
      }

   }

   public final void onCreateHome(short var1, String var2) {
      Canvas.endDlg();
      if (var1 == 0) {
         Vector var3;
         (var3 = new Vector()).addElement(new Command(T.yes, 50));
         var3.addElement(new Command(T.no, 51));
         Canvas.setInfoC(var2, var3);
      } else {
         Canvas.startOKDlg(var2);
         if (var1 == 2) {
            LoadMap.map = this.temp;
         }

         this.temp = null;
         ParkMsgHandler.onHandler();
         GameMidlet.avatar.x = this.posJoin.x;
         GameMidlet.avatar.y = this.posJoin.y;
         super.center = MapScr.gI().e;
         AvCamera.gI().init(70 + this.typeHome);
      }

   }

   public final void onGetTileInfo(Tile[] var1) {
      this.listTile = var1;
      this.doSelectMap();
      Canvas.endDlg();
   }

   private void removeTrans(MapItem var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < this.listItem.size(); ++var3) {
         MapItem var4;
         if ((var4 = (MapItem)this.listItem.elementAt(var3)).x / 24 == var1.x / 24 && var4.y / 24 == var1.y / 24) {
            ++var2;
         }
      }

      if (var2 == 1) {
         MapItemType var6 = AvatarData.getMapItemTypeByID(var1.typeID);

         for(int var7 = 0; var7 < var6.listNotTrans.size(); ++var7) {
            AvPosition var5 = (AvPosition)var6.listNotTrans.elementAt(var7);
            LoadMap.type[(var1.y / 24 + var5.y) * LoadMap.wMap + var1.x / 24 + var5.x] = 80;
         }
      }

   }

   public final void onRemoveItem(MapItem var1) {
      MapItem var2 = var1;
      HouseScr var5 = this;
      int var3 = 0;

      MapItem var10000;
      while(true) {
         if (var3 >= var5.listItem.size()) {
            var10000 = null;
            break;
         }

         MapItem var4;
         if ((var4 = (MapItem)var5.listItem.elementAt(var3)).x / 24 == var2.x && var4.y / 24 == var2.y && var4.typeID == var2.typeID) {
            var10000 = var4;
            break;
         }

         ++var3;
      }

      LoadMap.treeLists.removeElement(var10000);
      this.listItem.removeElement(var10000);
      this.removeTrans(var10000);
      ParkMsgHandler.onHandler();
      Canvas.endDlg();
   }

   public final void setType(MapItem var1) {
      MapItemType var2 = AvatarData.getMapItemTypeByID(var1.typeID);
      byte var3 = 88;
      if (var2.idType == this.IDHo) {
         var3 = 112;
      } else if (var2.idType == this.IDHoa) {
         var3 = 111;
      } else if (var2.iconID == 1) {
         var3 = 79;
      } else if (var2.iconID == 2) {
         var3 = 67;
      }

      for(int var4 = 0; var4 < var2.listNotTrans.size(); ++var4) {
         AvPosition var5 = (AvPosition)var2.listNotTrans.elementAt(var4);
         LoadMap.type[(var1.y / 24 + var5.y) * LoadMap.wMap + var1.x / 24 + var5.x] = (short)var3;
      }

   }

   public final void onGetTypeHouse(int var1, int var2, short var3, Vector var4) {
      if (var1 != 0) {
         for(var1 = 0; var1 < var4.size(); ++var1) {
            Avatar var6;
            Avatar var7 = ListScr.getAvatar((var6 = (Avatar)var4.elementAt(var1)).IDDB);
            if (var6 != null && var7 != null) {
               var7.typeHome = var6.typeHome;
            }
         }

         Canvas.endDlg();
         this.onRoadFriend();
      } else {
         GameMidlet.avatar.typeHome = (byte)var2;
         MapScr.gI().switchToMe();
         boolean var10000;
         if (this.imgTileMap == null) {
            this.loadTileMap();
            if (this.imgTileMap != null && var3 == this.imgTileMap.ver) {
               var10000 = true;
            } else {
               if (this.imgTileMap == null) {
                  this.imgTileMap = new BigImgInfo();
                  this.imgTileMap.ver = var3;
               }

               AvatarService var5;
               (var5 = AvatarService.gI()).createMessage((byte)-73);
               var5.sendMessage();
               var10000 = false;
            }
         } else {
            var10000 = true;
         }

         if (var10000) {
            if (MapScr.idHouse != -1) {
               AvatarService.gI().doJoinHouse(MapScr.idHouse);
               MapScr.idHouse = -1;
            } else {
               Canvas.load = 1;
               Canvas.endDlg();
            }
         } else {
            Canvas.load = 1;
         }
      }

   }

   public final void keyPress(int var1) {
      ChatTextField.gI().startChat(var1, this);
      super.keyPress(var1);
   }

   public final void onChatFromMe(String var1) {
      if (!var1.trim().equals("")) {
         ParkService.gI().chatToBoard(var1);
      }

   }

   public final void onRoadFriend() {
      if (ListScr.friendL == null) {
         Canvas.startWaitDlg();
         CasinoService.gI().requestFriendList();
         ListScr.typeListFriend = 2;
      } else if (ListScr.isGetTypeHouse) {
         ListScr.isGetTypeHouse = false;
         Canvas.startWaitDlg();
         AvatarService.gI().getTypeHouse(1);
      } else {
         Vector var1 = new Vector();

         for(int var2 = 0; var2 < ListScr.friendL.size(); ++var2) {
            Avatar var3;
            if ((var3 = (Avatar)ListScr.friendL.elementAt(var2)).typeHome == this.typeHome) {
               var1.addElement(var3);
            }
         }

         if (var1.size() == 0) {
            if (Canvas.currentMyScreen == ListScr.gI()) {
               ListScr.gI().backMyScreen.switchToMe();
            }

            Canvas.startOKDlg(T.noFriend);
         } else {
            ListScr.gI().switchToMe();
            ListScr.tempList = var1;
            ListScr.gI().setCam();
            ListScr.gI().g();
         }
      }

   }

   public final void commandActionPointer(int var1, int var2) {
      int var3 = -1;

      int var4;
      MapItem var5;
      for(var4 = 0; var4 < this.listItem.size(); ++var4) {
         if ((var5 = (MapItem)this.listItem.elementAt(var4)).x / 24 == this.x && var5.y / 24 == this.y) {
            var3 = var4;
            break;
         }
      }

      var5 = null;
      if (var3 != -1) {
         var5 = (MapItem)this.listItem.elementAt(var3);
      }

      var5 = var5;
      switch (var1) {
         case 1:
            GlobalService var11;
            (var11 = GlobalService.gI()).createMessage((byte)-87);
            var11.sendMessage();
            return;
         case 2:
            Vector var10;
            (var10 = new Vector()).addElement(new Command(T.buyItem, 6));
            var10.addElement(new Command(T.latGach, 7));
            if (this.listItem.size() > 0) {
               var10.addElement(new Command(T.sellItem, 8));
            }

            Menu.gI().startAt(var10, 2);
            return;
         case 3:
            doKick();
            return;
         case 4:
            Canvas.inputDlg.setInfoIkb(T.pass + ":", 100, 2);
            return;
         case 5:
            MapScr.gI().doExit();
            return;
         case 6:
            this.doSelectObject();
            return;
         case 7:
            isChange = true;
            if (this.listTile == null) {
               if (this.listTile == null) {
                  HomeMsgHandler.onHandler();
                  AvatarService var9;
                  (var9 = AvatarService.gI()).createMessage((byte)-43);
                  var9.sendMessage();
                  Canvas.startWaitDlg();
               }

               return;
            }

            this.doSelectMap();
            return;
         case 8:
            this.doOption();
            return;
         case 9:
         case 10:
         default:
            break;
         case 11:
            if (var3 == -1) {
               Canvas.startOKDlg(T.noItem);
               return;
            }

            this.isSelectedItem = var3;

            for(var1 = 0; var1 < AvatarData.listMapItemType.size(); ++var1) {
               if (((MapItemType)AvatarData.listMapItemType.elementAt(var1)).idType == var5.typeID) {
                  this.selected = var1;
                  break;
               }
            }

            super.left = null;
            super.right = null;
            this.removeTrans(var5);
            this.posSort = new AvPosition(this.x, this.y, var5.typeID);
            super.center = new Command(T.OK, new class_ga(this, var5));
            return;
         case 12:
            if (var3 == -1) {
               Canvas.startOKDlg(T.noItem);
               return;
            }

            if (var5.dir == 0) {
               var5.dir = 2;
            } else {
               var5.dir = 0;
            }

            AvatarService.gI().doSortItem(var5.typeID, this.x, this.y, this.x, this.y, var5.dir);
            return;
         case 13:
            if (var3 != -1 && var5.typeID != this.IDHoa) {
               Canvas.startOKDlg(T.doWantSellItem, new IActionSellItem(this, var5));
               return;
            }

            Canvas.startOKDlg(T.noItem);
            return;
         case 14:
            PopupShop.gI().close();
            Canvas.startOKDlg(T.getData, 53);
            return;
         case 15:
            TField[] var7 = new TField[3];

            for(var2 = 0; var2 < 3; ++var2) {
               var7[var2] = new TField();
               var7[var2].setIputType(2);
            }

            var7[0].setFocus(true);
            Command var8 = new Command(T.finish, new IActionFinish(this, var7));
            PopupShop.gI().close();
            InputFace.gI().setIputType(var7, T.changePass, T.gettingPrice, var8);
            Canvas.currentFace = InputFace.gI();
            InputFace.gI().left = new Command(T.close, 8);
            return;
         case 16:
            if (var2 < LoadMap.playerLists.size()) {
               Base var6 = (Base)LoadMap.playerLists.elementAt(var2);
               AvatarService.gI().doKickOutHome(var6.IDDB);
               return;
            }
            break;
         case 17:
            for(var1 = 0; var1 < this.listTile.length; ++var1) {
               if (var1 == var2) {
                  if (this.xTemp != -1) {
                     this.x = this.xTemp;
                     this.y = this.yTemp;
                     GameMidlet.avatar.x = this.xTemp * 24;
                     GameMidlet.avatar.y = this.yTemp * 24;
                     AvCamera.gI().setToPos(GameMidlet.avatar.x * AvMain.hd, GameMidlet.avatar.y * AvMain.hd);
                  }

                  this.selected = var1;
                  if (this.selected < numW) {
                     this.indexName = 1;
                  } else {
                     this.indexName = 0;
                  }
               }
            }

            return;
         case 18:
            for(var1 = 0; var1 < AvatarData.listMapItemType.size(); ++var1) {
               MapItemType var12;
               if (var1 == var2 && (var12 = (MapItemType)AvatarData.listMapItemType.elementAt(var1)).buy != 0 && (this.typeHome != 4 && (var12.buy == 1 || var12.buy == 2) || this.typeHome == 4) && (var4 = var12.name.indexOf(":")) != -1) {
                  String var13 = var12.name.substring(0, var4);
                  this.doSelectedItem(var13);
               }
            }
      }

   }

   public final void doOut() {
      this.listP_Chest = null;
      this.listP_Con = null;
      ParkService.gI().doJoinPark(21, 0);
      LoadMap.rememMap = -1;
   }

   public final void onCustomChest(Vector var1, Vector var2, int var3, byte var4) {
      this.listP_Con = var1;
      this.listP_Chest = var2;
      this.moneyOnChest = var3;
      this.levelChest = var4;
      Vector var7 = MapScr.gI().getListCmdDoUsing(var1, GameMidlet.avatar.IDDB, 3);
      var2 = MapScr.gI().getListCmdDoUsing(var2, GameMidlet.avatar.IDDB, 2);
      if (Canvas.currentMyScreen != MainMenu.me) {
         PopupShop.gI().isFull = true;
         PopupShop.gI().addElement(new String[]{T.mySeft, T.container}, new Vector[]{var7, var2}, (Vector)null);
         Command var5 = MapScr.gI().cmdDellPart(var1, 1, 1, false);
         Command var6 = new Command(T.menu, new IHouse1(this));
         PopupShop.gI().setCmdLeft(var5, 0);
         PopupShop.gI().setCmdLeft(var6, 1);
         if (Canvas.currentMyScreen != PopupShop.gI()) {
            PopupShop.gI().switchToMe();
         }
      }

   }

   public static void onEnterPass() {
      Canvas.inputDlg.setInfoIkb(T.pass, 101, 2);
   }

   public final void onTransChestPart(boolean var1, String var2) {
      if (!var1) {
         Canvas.startOKDlg(var2);
      } else {
         int var3 = PopupShop.focusTap;
         int var5 = PopupShop.focus;
         SeriPart var4;
         if (var3 == 0) {
            var4 = (SeriPart)this.listP_Con.elementAt(var5);
            this.listP_Chest.addElement(var4);
            this.listP_Con.removeElement(var4);
         } else {
            var4 = (SeriPart)this.listP_Chest.elementAt(var5);
            this.listP_Con.addElement(var4);
            this.listP_Chest.removeElement(var4);
         }

         this.restartPopup();
         Canvas.endDlg();
      }

   }

   public final void restartPopup() {
      int var1 = PopupShop.focusTap;
      int var2 = PopupShop.focus;
      PopupShop.gI().close();
      this.onCustomChest(this.listP_Con, this.listP_Chest, this.moneyOnChest, this.levelChest);
      PopupShop.focusTap = var1;
      PopupShop.gI().setCmyLim();
      if (var2 >= PopupShop.gI().listCell[var1].size()) {
         var2 = 0;
      }

      PopupShop.focus = var2;
      PopupShop.gI().setCaption();
      Canvas.cameraList.setSelect(PopupShop.focus);
   }

   public final void onOpenShop(byte var1, String var2, String[] var3, short[] var4, short[] var5, String[] var6, String[] var7, int[] var8, short[] var9) {
      MapScr.gI();
      MapScr.setAvatarShop(GameMidlet.avatar);
      Vector var10 = new Vector();

      for(int var11 = 0; var11 < var3.length; ++var11) {
         var10.addElement(new CommandShop1(this, T.selectt, new IActionShop1(this, var1, var5[var11], var7[var11]), var11, var3[var11], var4[var11], var5[var11], var6[var11], var8 == null ? -1 : var8[var11], var7[var11], var9[var11]));
      }

      if (var10.size() > 0) {
         PopupShop.gI().switchToMe();
         PopupShop.isHorizontal = true;
         PopupShop.gI().addElement(new String[]{var2}, new Vector[]{var10}, (Vector)null);
      }

   }

   static void setStatusBuyItem(HouseScr var0) {
      var0.setStatusBuyItem();
   }

   static int getxTemp(HouseScr var0) {
      return var0.xTemp;
   }

   static void setX(HouseScr var0, int var1) {
      var0.x = var1;
   }

   static int getYtemp(HouseScr var0) {
      return var0.yTemp;
   }

   static void setyTemp(HouseScr var0, int var1) {
      var0.y = var1;
   }

   static void setSelectedIndex(HouseScr var0, int var1) {
      var0.selected = var1;
   }

   static int getX(HouseScr var0) {
      return var0.x;
   }

   static void setxTemp(HouseScr var0, int var1) {
      var0.xTemp = var1;
   }

   static int getY(HouseScr var0) {
      return var0.y;
   }

   static void setYtemp(HouseScr var0, int var1) {
      var0.yTemp = var1;
   }

   static void a(HouseScr var0, int var1, String var2) {
      MapItemType var3 = (MapItemType)AvatarData.listMapItemType.elementAt(var1);
      if (!var0.isDisable(var3)) {
         Canvas.getTypeMoney(var3.priceXu, var3.priceLuong, new IActionBuyItem(var0, var3, var2), new IActionBuyItem1(var0, var3, var2), new IActionBuyItemClose1(var0));
      }

   }

   static void f(HouseScr var0) {
      var0.doSelectObject();
   }

   static boolean a(HouseScr var0, MapItemType var1) {
      return var0.isDisable(var1);
   }

   static AvPosition getposSort(HouseScr var0) {
      return var0.posSort;
   }

   static boolean isSetTuong(HouseScr var0, MapItem var1) {
      return isSetTuong(var1);
   }

   static void doOption(HouseScr var0) {
      var0.doOption();
   }

   static void doSelectedItem(HouseScr var0, String var1) {
      var0.doSelectedItem(var1);
   }

   static void reset(HouseScr var0) {
      var0.reset();
   }
}
