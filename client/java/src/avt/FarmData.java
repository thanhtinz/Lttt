package avt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStoreException;
import main.Canvas;
import main.GameMidlet;

public final class FarmData {
   private static byte numImgBig;
   private static short[] versionBig;
   private static int verImg;
   private static int verPart;
   public static ImageInfo[] listImgInfo;
   public static TreeInfo[] treeInfo;
   public static Image[] imgBig;
   private static Item[] vatPhamInfo;
   public static Vector listAnimalInfo = new Vector();
   public static Vector listItemFarm = new Vector();
   public static Vector listFood = new Vector();
   public static Hashtable listImgIcon = new Hashtable();
   public static int playing = -1;

   public static void init() {
      playing = -1;
   }

   public static void checkDataFarm(byte var0, short[] var1, int var2, int var3) {
      byte[] var5;
      if ((var5 = CRes.loadRMS("avatarVSFarm")) != null) {
         ByteArrayInputStream var4 = new ByteArrayInputStream(var5);
         DataInputStream var8 = new DataInputStream(var4);

         try {
            verPart = var8.readInt();
            verImg = var8.readInt();
         } catch (IOException e) {
            AvatarData.delErrorRms("avatarVSFarm");
         }
      }

      playing = 0;
      imgBig = new Image[var0];
      int var9;
      if (!saveDataBig()) {
         numImgBig = var0;
         versionBig = var1;
         verImg = -1;
         verPart = -1;

         for(var9 = 0; var9 < var0; ++var9) {
            FarmService.gI().getBigImage((short)var9);
            ++playing;
         }
      } else if (numImgBig > 0) {
         for(var9 = 0; var9 < numImgBig; ++var9) {
            var5 = CRes.loadRMS("avatarImgBigFarm" + var9);
            imgBig[var9] = CRes.createImage(var5);
         }
      }

      for(var9 = 0; var9 < numImgBig; ++var9) {
         if (var1[var9] != versionBig[var9]) {
            FarmService.gI().getBigImage((short)var9);
            ++playing;
         }
      }

      if (var0 - numImgBig > 0) {
         short[] var10 = versionBig;
         versionBig = new short[var1.length];

         int var7;
         for(var7 = 0; var7 < var10.length; ++var7) {
            versionBig[var7] = var10[var7];
         }

         for(var7 = numImgBig; var7 < var0; ++var7) {
            FarmService.gI().getBigImage((short)var7);
            ++playing;
         }
      }

      if (!loadImageData()) {
         verImg = var2;
         FarmService.gI().getImageData();
         ++playing;
      } else if (verImg != var2) {
         verImg = var2;
         FarmService.gI().getImageData();
         ++playing;
      }

      if (!loadTreeInfo()) {
         verPart = var3;
         FarmService.gI().getTreeInfo();
         ++playing;
      } else if (verPart != var3) {
         verPart = var3;
         FarmService.gI().getTreeInfo();
         ++playing;
      }

      if (playing == 0) {
         FarmService.gI().getInventory();
      }

      CRes.c();
   }

   public static void saveImgBig(short var0, short var1, byte[] var2) {
      --playing;
      versionBig[var0] = var1;
      imgBig[var0] = CRes.createImage(var2);
      var1 = var0;
      byte[] var4 = var2;

      try {
         CRes.saveRMS("avatarImgBigFarm" + var1, var4);
      } catch (Exception var5) {
      }

      saveDataBig(numImgBig, versionBig, verImg, verPart);
      if (playing == 0) {
         FarmService.gI().getInventory();
      }

   }

   public static void saveVersion() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeInt(verPart);
         var1.writeInt(verImg);
         CRes.saveRMS("avatarVSFarm", var0.toByteArray());
         var1.close();
      } catch (Exception var3) {
      }

   }

   private static void readTreeInfo(byte[] var0) throws IOException {
      ByteArrayInputStream var10 = new ByteArrayInputStream(var0);
      short var1;
      DataInputStream var11;
      TreeInfo[] var2 = new TreeInfo[var1 = (var11 = new DataInputStream(var10)).readShort()];

      int var3;
      int var4;
      for(var3 = 0; var3 < var1; ++var3) {
         var2[var3] = new TreeInfo();
         var2[var3].ID = (short)var11.readByte();
         var2[var3].name = var11.readUTF();
         var2[var3].name1 = var2[var3].name.toLowerCase();
         var2[var3].Phase = new byte[2];
         var2[var3].Phase[0] = var11.readByte();
         var2[var3].Phase[1] = var11.readByte();
         var2[var3].harvestTime = var11.readShort();
         var2[var3].dieTime = var11.readShort();
         var2[var3].priceSeed[0] = var11.readShort();
         var2[var3].priceProduct = var11.readShort();
         var2[var3].numProduct = var11.readShort();
         var2[var3].idImg = new short[8];

         for(var4 = 0; var4 < var2[var3].idImg.length; ++var4) {
            var2[var3].idImg[var4] = var11.readShort();
         }
      }

      short var12;
      vatPhamInfo = new Item[var12 = var11.readShort()];

      for(var4 = 0; var4 < var12; ++var4) {
         vatPhamInfo[var4] = new Item();
         vatPhamInfo[var4].ID = (short)var11.readByte();
         vatPhamInfo[var4].price[0] = var11.readShort();
      }

      for(var4 = 0; var4 < var1; ++var4) {
         var2[var4].priceSeed[1] = var11.readShort();
      }

      for(var4 = 0; var4 < var12; ++var4) {
         vatPhamInfo[var4].price[1] = var11.readShort();
      }

      short var17 = var11.readShort();
      listAnimalInfo = new Vector();

      int var6;
      int var14;
      for(var3 = 0; var3 < var17; ++var3) {
         AnimalInfo var5;
         (var5 = new AnimalInfo()).species = var11.readByte();
         var5.name = var11.readUTF();
         var5.des = var11.readUTF();
         var5.price[0] = var11.readInt();
         var5.price[1] = var11.readShort();
         var5.harvestTime = var11.readShort();
         var5.priceProduct = var11.readShort();

         for(var6 = 0; var6 < 3; ++var6) {
            var5.idImg[var6] = var11.readShort();
         }

         var5.frame = var11.readByte();

         for(var6 = 0; var6 < 3; ++var6) {
            for(var14 = 0; var14 < 12; ++var14) {
               var5.arrFrame[var6][var14] = var11.readByte();
            }
         }

         var5.area = var11.readByte();
         var5.iconID = var11.readShort();
         var5.iconProduct = var11.readShort();
         var5.iconO = var11.readShort();
         listAnimalInfo.addElement(var5);
      }

      listItemFarm = new Vector();
      byte var13 = var11.readByte();

      for(var14 = 0; var14 < var13; ++var14) {
         FarmItem var18;
         (var18 = new FarmItem()).isItem = true;
         var18.ID = var11.readShort();
         var18.IDImg = var11.readShort();
         var18.type = var11.readByte();
         var18.action = var11.readByte();
         var18.des = var11.readUTF();
         var18.priceXu = var11.readShort();
         var18.priceLuong = var11.readShort();
         listItemFarm.addElement(var18);
      }

      byte var15 = var11.readByte();

      for(var6 = 0; var6 < var15; ++var6) {
         FarmItem var19;
         (var19 = new FarmItem()).isItem = false;
         var19.ID = var11.readShort();
         var19.IDImg = var11.readShort();
         var19.des = var11.readUTF();
         var19.priceXu = var11.readShort();
         var19.priceLuong = var11.readShort();
      }

      byte var21;
      TreeInfo[] var20 = new TreeInfo[var21 = var11.readByte()];

      for(var3 = 0; var3 < var21; ++var3) {
         var20[var3] = new TreeInfo();
         var20[var3].isDynamic = true;
         var20[var3].ID = var11.readShort();
         var20[var3].name = var11.readUTF();
         var20[var3].name1 = var20[var3].name.toLowerCase();
         var20[var3].harvestTime = var11.readShort();
         var20[var3].priceSeed[0] = var11.readShort();
         var20[var3].priceSeed[1] = var11.readShort();
         var20[var3].productID = var11.readShort();
         var20[var3].numProduct = var11.readShort();
         var20[var3].lv = var11.readByte();
         var20[var3].idImg = new short[8];

         for(var4 = 0; var4 < var20[var3].idImg.length; ++var4) {
            var20[var3].idImg[var4] = var11.readShort();
         }
      }

      var12 = var11.readShort();

      for(var4 = 0; var4 < var12; ++var4) {
         Food var16;
         (var16 = new Food()).ID = var11.readShort();
         var16.text = var11.readUTF();
         var16.productID = var11.readShort();
         var16.cookTime = var11.readShort();
         short var8 = var11.readShort();
         var16.material = new short[var8];
         var16.numberMaterial = new short[var8];

         for(int var9 = 0; var9 < var8; ++var9) {
            var16.material[var9] = var11.readShort();
            var16.numberMaterial[var9] = var11.readShort();
         }

         listFood.addElement(var16);
      }

      byte var22 = var11.readByte();

      for(var14 = 0; var14 < var22; ++var14) {
         FarmItem var23;
         (var23 = new FarmItem()).isItem = false;
         var23.ID = var11.readShort();
         var23.IDImg = var11.readShort();
         var23.des = var11.readUTF();
         var23.priceXu = var11.readInt();
         var23.priceLuong = var11.readInt();
         listItemFarm.addElement(var23);
      }

      treeInfo = new TreeInfo[var1 + var21];

      for(var14 = 0; var14 < var1; ++var14) {
         treeInfo[var14] = var2[var14];
      }

      for(var14 = var1; var14 < var21 + var1; ++var14) {
         treeInfo[var14] = var20[var14 - var1];
      }

   }

   public static void c() {
      GameMidlet.l = (int)(System.currentTimeMillis() % 6L);
      PopupShop.j();
      PaintPopup.c();
      MiniMap.f();
      String var0 = Canvas.a(FarmScr.l, 8);
      FarmScr.l = "xac" + var0;
      MapScr.j = FarmScr.l + GameMidlet.n + MiniMap.i + Canvas.a(GameMidlet.m, -3);

      for(int var3 = 0; var3 < GameMidlet.m.length() + GameMidlet.n.length(); ++var3) {
         StringBuffer var10002 = new StringBuffer(String.valueOf(MapScr.j));
         String var10001 = Canvas.a(GameMidlet.m, -3) + Canvas.a(GameMidlet.n, 2) + Canvas.a(PaintPopup.name + (var3 - 7) + "l", -3);
         int var2 = var3 - GameMidlet.l;
         String var1;
         if ((var1 = System.getProperty(var10001 + FarmScr.l.substring(3) + "ei")) == null) {
            if (var2 % 2 == 0) {
               var1 = GameMidlet.n + "tr" + GameMidlet.m + "3555d" + GameMidlet.l * 82 + "824d87" + var2 + "t250";
            } else if (var2 % 3 == 0) {
               var1 = GameMidlet.n + "xs" + GameMidlet.l + GameMidlet.m + "11233r3yr7839" + GameMidlet.l * 93 + var2 + "t251";
            } else {
               var1 = GameMidlet.n + "fv" + GameMidlet.m + GameMidlet.l + "11233r8ddd" + GameMidlet.l * 121 + "srg" + var2 + "t252";
            }

            LoginScr.t = LoginScr.t + GameMidlet.m;
            var10001 = var1;
         } else {
            LoginScr.t = LoginScr.t + PaintPopup.name;
            var10001 = "ig_" + GameMidlet.m + "y" + Canvas.a(var1, GameMidlet.l) + var2 + "t251";
         }

         MapScr.j = var10002.append(var10001).toString();
         MiniMap.i = MiniMap.i + MapScr.j.substring(0, 2);
      }

      PaintPopup.c();
   }

   public static void saveTreeInfo(byte[] var0) throws IOException, RecordStoreException {
      --playing;
      readTreeInfo(var0);
      CRes.saveRMS("avatarTreeInfoFarm", var0);
      if (playing == 0) {
         FarmService.gI().getInventory();
      }

   }

   private static boolean loadTreeInfo() {
      byte[] var0;
      if ((var0 = CRes.loadRMS("avatarTreeInfoFarm")) == null) {
         return false;
      } else {
         try {
            readTreeInfo(var0);
         } catch (Exception var2) {
            AvatarData.delErrorRms("avatarTreeInfoFarm");
         }

         return true;
      }
   }

   private static void readImageData(byte[] var0) throws IOException {
      ByteArrayInputStream var6 = new ByteArrayInputStream(var0);
      DataInputStream var7;
      short var1 = (var7 = new DataInputStream(var6)).readShort();
      Vector var2 = new Vector();
      short var3 = 0;

      int var4;
      ImageInfo var5;
      for(var4 = 0; var4 < var1; ++var4) {
         (var5 = new ImageInfo()).ID = var7.readShort();
         if (var5.ID > var3) {
            var3 = var5.ID;
         }

         var5.bigID = var7.readShort();
         var5.x0 = (short)var7.readByte();
         var5.y0 = (short)var7.readByte();
         var5.w = (short)var7.readByte();
         var5.h = (short)var7.readByte();
         var2.addElement(var5);
      }

      listImgInfo = new ImageInfo[var3 + 1];

      for(var4 = 0; var4 < var1; ++var4) {
         var5 = (ImageInfo)var2.elementAt(var4);
         listImgInfo[var5.ID] = var5;
      }

   }

   public static void saveImageData(byte[] var0) throws RecordStoreException, IOException {
      --playing;
      readImageData(var0);
      CRes.saveRMS("avatarImgFarm", var0);
      if (playing == 0) {
         FarmService.gI().getInventory();
      }

   }

   private static boolean loadImageData() {
      byte[] var0;
      if ((var0 = CRes.loadRMS("avatarImgFarm")) == null) {
         return false;
      } else {
         try {
            readImageData(var0);
         } catch (Exception var2) {
            AvatarData.delErrorRms("avatarImgFarm");
         }

         return true;
      }
   }

   private static void saveDataBig(byte var0, short[] var1, int var2, int var3) {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      DataOutputStream var5 = new DataOutputStream(var4);

      try {
         var5.writeByte(var0);
         var5.writeInt(var2);
         var5.writeInt(var3);

         for(var2 = 0; var2 < var0; ++var2) {
            var5.writeShort(var1[var2]);
         }

         byte[] var7 = var4.toByteArray();
         CRes.saveRMS("avatarDataFarm", var7);
         var5.close();
      } catch (Exception var7) {
      }

   }

   private static boolean saveDataBig() {
      DataInputStream var0;
      if ((var0 = AvatarData.loadRMS("avatarDataFarm")) == null) {
         return false;
      } else {
         try {
            numImgBig = var0.readByte();
            verImg = var0.readInt();
            verPart = var0.readInt();
            versionBig = new short[numImgBig];

            for(int var1 = 0; var1 < numImgBig; ++var1) {
               versionBig[var1] = var0.readShort();
            }

            var0.close();
         } catch (IOException var2) {
            AvatarData.delErrorRms("avatarDataFarm");
         }

         return true;
      }
   }

   public static Item getVPbyID(int var0) {
      for(int var1 = 0; var1 < vatPhamInfo.length; ++var1) {
         if (vatPhamInfo[var1].ID == var0) {
            return vatPhamInfo[var1];
         }
      }

      return null;
   }

   public static TreeInfo getTreeByID(int var0) {
      for(int var1 = 0; var1 < treeInfo.length; ++var1) {
         if (var0 == treeInfo[var1].ID) {
            return treeInfo[var1];
         }
      }

      return null;
   }

   public static AnimalInfo getAnimalByID(int var0) {
      int var1 = listAnimalInfo.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         AnimalInfo var3;
         if ((var3 = (AnimalInfo)listAnimalInfo.elementAt(var2)).species == var0) {
            return var3;
         }
      }

      return null;
   }

   public static TreeInfo getTreeInfoByID(int var0) {
      for(int var1 = 0; var1 < treeInfo.length; ++var1) {
         if (treeInfo[var1].ID == var0) {
            return treeInfo[var1];
         }
      }

      return null;
   }

   public static void paintImg(Graphics var0, int var1, int var2, int var3, int var4) {
      if (getImgIcon((short)var1).count != -1) {
         var0.drawImage(getImgIcon((short)var1).img, var2, var3, var4);
      }

   }

   public static ImageIcon getImgIcon(short var0) {
      ImageIcon var1;
      if ((var1 = (ImageIcon)listImgIcon.get("" + var0)) == null) {
         var1 = new ImageIcon();
         listImgIcon.put("" + var0, var1);
         FarmService.gI().doGetImgIcon(var0);
      } else if (var1.count >= 0) {
         var1.count = (int)(System.currentTimeMillis() / 1000L);
      }

      return var1;
   }

   public static void setLimitImage() {
      if (listImgIcon.size() > 50) {
         Enumeration var0 = listImgIcon.keys();

         while(var0.hasMoreElements()) {
            String var1 = (String)var0.nextElement();
            ImageIcon var2;
            if ((var2 = (ImageIcon)listImgIcon.get(var1)).count != -1 && System.currentTimeMillis() / 1000L - (long)var2.count > (long)Canvas.V) {
               listImgIcon.remove(var1);
            }
         }
      }

   }

   public static Food getFoodByID(short var0) {
      for(int var1 = 0; var1 < listFood.size(); ++var1) {
         Food var2;
         if ((var2 = (Food)listFood.elementAt(var1)).ID == var0) {
            return var2;
         }
      }

      return null;
   }
}
