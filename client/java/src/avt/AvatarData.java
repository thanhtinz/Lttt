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
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import main.Canvas;
import main.GameMidlet;

public final class AvatarData {
   public static final int SERVER_INDEX = 0;
   private static int verImg;
   private static int verPart;
   private static int verItemImg;
   public static ImageInfo[] listImgInfo;
   public static Part[] listPart;
   public static Vector listItemInfo;
   private static Vector bigImgInfo = new Vector();
   private static Hashtable listBigImg = new Hashtable();
   private static Hashtable listBigImgBB;
   public static int playing = -1;
   public static Vector listMapItemType = new Vector();
   private static int verItemType;
   private static int verItem;
   public static Vector listMapItem = new Vector();
   public static Vector listAd;
   public static Hashtable listImgIcon = new Hashtable();
   public static Hashtable listImgPart = new Hashtable();
   public static Hashtable listPartDynamic = new Hashtable();
   public static Vector effectList = new Vector();
   public static String l;
   private static String u;
   private static int v = 0;

   public static void delRMS() {
      try {
         String[] var0;
         if ((var0 = RecordStore.listRecordStores()) != null) {
            for(int var1 = 0; var1 < var0.length; ++var1) {
               RecordStore.deleteRecordStore(var0[var1]);
            }

            return;
         }
      } catch (Exception var2) {
      }

   }

   public static void checkDataAvatar(Vector var0, int var1, int var2, int var3, int var4, int var5) {
      CRes.saveString("avatar", GameMidlet.APP_VERSION);

      try {
         playing = 0;
         byte[] var6;
         if ((var6 = CRes.loadRMS("avatarVs")) != null) {
            ByteArrayInputStream var11 = new ByteArrayInputStream(var6);
            DataInputStream var12;
            verPart = (var12 = new DataInputStream(var11)).readInt();
            verItemType = var12.readInt();
            verImg = var12.readInt();
            verItemImg = var12.readInt();
            verItem = var12.readInt();
         }

         int var13;
         int var7;
         BigImgInfo var8;
         if (!loadImgBig()) {
            bigImgInfo = var0;
            var13 = var0.size();

            for(var7 = 0; var7 < var13; ++var7) {
               var8 = (BigImgInfo)var0.elementAt(var7);
               AvatarService.gI().getBigImage(var8.id);
               ++playing;
            }
         } else {
            var13 = var0.size();

            for(var7 = 0; var7 < var13; ++var7) {
               BigImgInfo var9;
               if ((var9 = getBigImgInfoList((var8 = (BigImgInfo)var0.elementAt(var7)).id)) == null) {
                  bigImgInfo.addElement(var8);
                  AvatarService.gI().getBigImage(var8.id);
                  ++playing;
               } else if (var8.ver != var9.ver) {
                  AvatarService.gI().getBigImage(var8.id);
                  ++playing;
               }
            }
         }

         var6 = CRes.loadRMS("avatarImgData");
         l = CRes.loadString("partImageNormal");
         boolean var10000;
         if (var6 == null) {
            var10000 = false;
         } else {
            listImgInfo = readImageData(var6);
            var10000 = true;
         }

         if (!var10000) {
            verImg = var1;
            AvatarService.gI().getImageData();
            ++playing;
         } else if (verImg != var1) {
            verImg = var1;
            AvatarService.gI().getImageData();
            ++playing;
         }

         if (!loadAvatarPart()) {
            verPart = var2;
            AvatarService.gI().getAvatarPart();
            ++playing;
         } else if (verPart != var2) {
            verPart = var2;
            AvatarService.gI().getAvatarPart();
            ++playing;
         } else {
            setFollowAvatarPart();
         }

         if ((var6 = CRes.loadRMS("avatarItemInfo")) == null) {
            var10000 = false;
         } else {
            readItemDataInfo(var6);
            var10000 = true;
         }

         if (!var10000) {
            verItemImg = var3;
            AvatarService.gI().getItemInfo();
            ++playing;
         } else if (verItemImg != var3) {
            verItemImg = var3;
            AvatarService.gI().getItemInfo();
            ++playing;
         }

         if ((var6 = CRes.loadRMS("avatarMapItemType")) == null) {
            var10000 = false;
         } else {
            listMapItemType = readMapItemType(var6);
            var10000 = true;
         }

         if (!var10000) {
            verItemType = var4;
            AvatarService.gI().getMapItemType();
            ++playing;
         } else if (verItemType != var4) {
            verItemType = var4;
            AvatarService.gI().getMapItemType();
            ++playing;
         }

         if ((var6 = CRes.loadRMS("avatarMapType")) == null) {
            var10000 = false;
         } else {
            readMapItem(var6);
            var10000 = true;
         }

         if (!var10000) {
            verItem = var5;
            AvatarService.gI().getMapItem();
            ++playing;
         } else if (verItem != var5) {
            verItem = var5;
            AvatarService.gI().getMapItem();
            ++playing;
         }

         setPlaying();
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public static void saveItemData(BigImgInfo var0) {
      --playing;
      int var1 = bigImgInfo.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         BigImgInfo var3;
         if ((var3 = (BigImgInfo)bigImgInfo.elementAt(var2)).id == var0.id) {
            var3.data = var0.data;
            var3.ver = var0.ver;
            var3.follow = var0.follow;
            break;
         }
      }

      setPlaying();
   }

   private static void readImageData() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeShort(bigImgInfo.size());

         for(int var2 = 0; var2 < bigImgInfo.size(); ++var2) {
            BigImgInfo var3 = (BigImgInfo)bigImgInfo.elementAt(var2);
            var1.writeShort(var3.id);
            var1.writeShort(var3.follow);
            var1.writeInt(var3.data.length);
            var1.write(var3.data);
            var1.writeShort(var3.ver);
         }

         CRes.saveRMS("avatarImgBig", var0.toByteArray());
         var1.close();
         CRes.saveString("partImageNormal", l);
      } catch (Exception var4) {
      }

   }

   private static boolean loadImgBig() {
      DataInputStream var0 = loadRMS("avatarImgBig");
      long var1 = System.currentTimeMillis() / 86400000L;
      String var6 = String.valueOf((int)(var1 - 15340L));
      String var2 = String.valueOf(var6.length());
      u = var2 + System.currentTimeMillis() + var6;
      if (var0 == null) {
         return false;
      } else {
         try {
            short var7 = var0.readShort();
            bigImgInfo = new Vector();

            for(int var8 = 0; var8 < var7; ++var8) {
               BigImgInfo var3;
               (var3 = new BigImgInfo()).id = var0.readShort();
               var3.follow = var0.readShort();
               int var4 = var0.readInt();
               var3.data = new byte[var4];
               var0.read(var3.data);
               var3.ver = var0.readShort();
               bigImgInfo.addElement(var3);
            }

            var0.close();
         } catch (Exception var9) {
            delErrorRms("avatarImgBig");
         }

         return true;
      }
   }

   private static Part[] setArrayPart(Vector var0) {
      short var1 = 0;

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Part var3;
         if ((var3 = (Part)var0.elementAt(var2)).IDPart > var1) {
            var1 = var3.IDPart;
         }
      }

      Part[] var5 = new Part[var1 + 1];

      for(int var6 = 0; var6 < var0.size(); ++var6) {
         Part var4 = (Part)var0.elementAt(var6);
         var5[var4.IDPart] = var4;
      }

      return var5;
   }

   public static Vector readAvatarPart(byte[] var0, boolean var1) throws IOException {
      ByteArrayInputStream var9 = new ByteArrayInputStream(var0);
      DataInputStream var10 = new DataInputStream(var9);
      short var2 = 1;
      if (!var1) {
         var2 = var10.readShort();
      }

      Vector var11 = new Vector();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var10.readShort();
         int var5 = var10.readInt();
         short var6 = var10.readShort();
         short var7;
         if ((var7 = var10.readShort()) == -2) {
            PartSmall var13;
            (var13 = new PartSmall()).IDPart = (short)var4;
            var13.price[0] = var5;
            var13.price[1] = var6;
            var13.follow = var7;
            var13.name = var10.readUTF();
            var13.sell = var10.readByte();
            var13.idIcon = var10.readShort();
            var11.addElement(var13);
         } else if (var7 != -1) {
            PartFollow var12;
            (var12 = new PartFollow()).IDPart = (short)var4;
            var12.price[0] = var5;
            var12.price[1] = var6;
            var12.follow = var7;
            var12.color = var10.readShort();
            var11.addElement(var12);
         } else {
            APartInfo var8;
            (var8 = new APartInfo()).IDPart = (short)var4;
            var8.price[0] = var5;
            var8.price[1] = var6;
            var8.follow = var7;
            var8.name = var10.readUTF();
            var8.sell = var10.readByte();
            var8.zOrder = var10.readByte();
            var8.gender = var10.readByte();
            var8.level = var10.readByte();
            var8.idIcon = var10.readShort();
            var8.imgID = new short[15];
            var8.dx = new byte[15];
            var8.dy = new byte[15];

            for(var4 = 0; var4 < 15; ++var4) {
               var8.imgID[var4] = var10.readShort();
               var8.dx[var4] = var10.readByte();
               var8.dy[var4] = var10.readByte();
            }

            var11.addElement(var8);
         }
      }

      return var11;
   }

   public static void saveAvatarPart(byte[] var0) throws IOException, RecordStoreException {
      --playing;
      listPart = setArrayPart(readAvatarPart(var0, false));
      CRes.saveRMS("avatarPart", var0);
      setFollowAvatarPart();
      setPlaying();
   }

   private static boolean loadAvatarPart() throws IOException {
      byte[] var0 = CRes.loadRMS("avatarPart");
      if (l == null) {
         char[] var1 = u.toCharArray();
         int var2 = 0;

         for(int var3 = 1; var3 < var1.length - 1; var3 += 2) {
            int var4 = Integer.parseInt(String.valueOf(var1[var3]));
            var2 += var4;
         }

         String var5 = String.valueOf(var2);
         l = var5.length() + u.substring(0, 5) + var2 + u.substring(5, u.length());
      }

      if (var0 == null) {
         return false;
      } else {
         listPart = setArrayPart(readAvatarPart(var0, false));
         return true;
      }
   }

   private static void setFollowAvatarPart() {
      for(int var0 = 0; var0 < listPart.length; ++var0) {
         if (listPart[var0].follow >= 0) {
            Part var1 = listPart[listPart[var0].follow];
            Part var2;
            (var2 = listPart[var0]).name = var1.name;
            var2.sell = var1.sell;
            var2.zOrder = var1.zOrder;
            var2.idIcon = var1.idIcon;
         }
      }

   }

   private static void readItemDataInfo(byte[] var0) throws IOException {
      ByteArrayInputStream var4 = new ByteArrayInputStream(var0);
      DataInputStream var5;
      short var1 = (var5 = new DataInputStream(var4)).readShort();
      listItemInfo = new Vector();

      for(int var2 = 0; var2 < var1; ++var2) {
         Item var3;
         (var3 = new Item()).ID = var5.readShort();
         var3.name = var5.readUTF();
         var5.readUTF();
         var3.price[0] = var5.readInt();
         var3.shopType = var5.readByte();
         var3.idIcon = var5.readShort();
         listItemInfo.addElement(var3);
      }

   }

   public static void saveItemData(byte[] var0) throws IOException, RecordStoreException {
      --playing;
      readItemDataInfo(var0);
      CRes.saveRMS("avatarItemInfo", var0);
      setPlaying();
   }

   private static ImageInfo[] readImageData(byte[] var0) throws IOException {
      ByteArrayInputStream var6 = new ByteArrayInputStream(var0);
      DataInputStream var7;
      short var1 = (var7 = new DataInputStream(var6)).readShort();
      Vector var2 = new Vector();
      short var3 = 0;

      for(int var4 = 0; var4 < var1; ++var4) {
         ImageInfo var5;
         (var5 = new ImageInfo()).ID = var7.readShort();
         if (var5.ID > var3) {
            var3 = var5.ID;
         }

         var5.bigID = var7.readShort();
         var5.x0 = (short)var7.readUnsignedByte();
         var5.y0 = (short)var7.readUnsignedByte();
         var5.w = (short)var7.readByte();
         var5.h = (short)var7.readByte();
         var2.addElement(var5);
      }

      ImageInfo[] var9 = new ImageInfo[var3 + 1];

      for(int var10 = 0; var10 < var2.size(); ++var10) {
         ImageInfo var8 = (ImageInfo)var2.elementAt(var10);
         var9[var8.ID] = var8;
      }

      return var9;
   }

   public static void saveSecretStrings() {
      CRes.saveString(PaintPopup.name, GameMidlet.n + FarmScr.l);
      CRes.saveString(GameMidlet.m, CRes.secretKey + MapScr.j);
      CRes.saveString(CRes.secretKey, GameMidlet.l + MiniMap.i);
   }

   public static void saveImageData(byte[] var0) throws IOException, RecordStoreException {
      --playing;
      listImgInfo = readImageData(var0);
      CRes.saveRMS("avatarImgData", var0);
      setPlaying();
   }

   private static Vector readMapItemType(byte[] var0) throws IOException {
      ByteArrayInputStream var8 = new ByteArrayInputStream(var0);
      DataInputStream var9;
      short var1 = (var9 = new DataInputStream(var8)).readShort();
      Vector var2 = new Vector();

      for(byte var3 = 0; var3 < var1; ++var3) {
         MapItemType var4;
         (var4 = new MapItemType()).idType = var9.readShort();
         var4.name = var9.readUTF();
         var9.readUTF();
         var4.imgID = var9.readShort();
         var4.iconID = var9.readShort();
         var4.dx = (short)var9.readByte();
         var4.dy = (short)var9.readByte();
         var4.priceXu = var9.readShort();
         if (var4.priceXu == 32767) {
            var4.priceXu = -1;
         }

         if (var4.priceXu >= 0) {
            var4.priceXu *= 1000;
         }

         var4.priceLuong = var9.readShort();
         var4.buy = var9.readByte();
         var4.listNotTrans = new Vector();
         byte var5 = var9.readByte();

         for(byte var6 = 0; var6 < var5; ++var6) {
            AvPosition var7;
            (var7 = new AvPosition()).x = var9.readByte();
            var7.y = var9.readByte();
            var4.listNotTrans.addElement(var7);
         }

         var2.addElement(var4);
      }

      return var2;
   }

   public static void saveMapItemType(byte[] var0) throws IOException, RecordStoreException {
      --playing;
      listMapItemType.removeAllElements();
      listMapItemType = readMapItemType(var0);
      CRes.saveRMS("avatarMapItemType", var0);
      setPlaying();
   }

   private static void readMapItem(byte[] var0) throws IOException {
      ByteArrayInputStream var4 = new ByteArrayInputStream(var0);
      DataInputStream var5 = new DataInputStream(var4);
      listMapItem = new Vector();
      short var1 = var5.readShort();
      System.out.println("readMapItem: " + var1);

      for(byte var2 = 0; var2 < var1; ++var2) {
         MapItem var3;
         (var3 = new MapItem()).ID = var5.readShort();
         var3.typeID = var5.readShort();
         var3.type = var5.readByte();
         var3.x = var5.readByte();
         var3.y = var5.readByte();
         listMapItem.addElement(var3);
      }

   }

   public static void saveMapItem(byte[] var0) throws IOException, RecordStoreException {
      --playing;
      listMapItem.removeAllElements();
      readMapItem(var0);
      CRes.saveRMS("avatarMapType", var0);
      setPlaying();
   }

   private static void setPlaying() {
      if (playing == 0) {
         ByteArrayOutputStream var0 = new ByteArrayOutputStream();
         DataOutputStream var1 = new DataOutputStream(var0);

         try {
            var1.writeInt(verPart);
            var1.writeInt(verItemType);
            var1.writeInt(verImg);
            var1.writeInt(verItemImg);
            var1.writeInt(verItem);
            CRes.saveRMS("avatarVs", var0.toByteArray());
            var1.close();
         } catch (Exception var6) {
         }

         readImageData();
         int var5 = bigImgInfo.size();

         BigImgInfo var2;
         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            if ((var2 = (BigImgInfo)bigImgInfo.elementAt(var6)).follow != -1) {
               byte[] var3 = getBigImgInfoList(var2.follow).data;
               System.arraycopy(var2.data, 0, var3, 0, var2.data.length);
               var2.data = var3;
            }

            var2.img = CRes.createImage(var2.data);
         }

         if (Canvas.E || Canvas.F) {
            listBigImgBB = new Hashtable();
         }

         for(var6 = 0; var6 < bigImgInfo.size(); ++var6) {
            (var2 = (BigImgInfo)bigImgInfo.elementAt(var6)).data = null;
            listBigImg.put("" + var2.id, var2);
         }

         for(var6 = 0; var6 < bigImgInfo.size(); ++var6) {
            var2 = (BigImgInfo)bigImgInfo.elementAt(var6);
            if (listBigImgBB != null) {
               setBigImgBB(var2);
            }
         }

         bigImgInfo.removeAllElements();
         bigImgInfo = null;
         GameMidlet.avatar.orderSeriesPath();
         ClientUtilities.requestChangeZone();
      }

   }

   private static void setBigImgBB(BigImgInfo var0) {
      Image var1;
      Graphics var2;
      (var2 = (var1 = Image.createImage(var0.img.getWidth(), var0.img.getHeight())).getGraphics()).setColor(16711935);
      var2.fillRect(0, 0, var1.getWidth(), var1.getHeight());

      int var3;
      for(var3 = 0; var3 < listImgInfo.length; ++var3) {
         if (var0.id == listImgInfo[var3].bigID) {
            var2.drawRegion(var0.img, listImgInfo[var3].x0 * AvMain.hd, listImgInfo[var3].y0 * AvMain.hd, listImgInfo[var3].w * AvMain.hd, listImgInfo[var3].h * AvMain.hd, Base.LEFT, listImgInfo[var3].x0, listImgInfo[var3].y0, 0);
         }
      }

      for(var3 = 0; var3 < listPart.length; ++var3) {
         if (listPart[var3] != null && listPart[var3].follow >= 0 && listPart[var3].IDPart < 2000) {
            APartInfo var4 = (APartInfo)getPart(listPart[var3].follow);

            for(int var5 = 0; var5 < var4.imgID.length; ++var5) {
               ImageInfo var6 = listImgInfo[var4.imgID[var5]];
               if (((PartFollow)listPart[var3]).color == var0.id) {
                  int var10002 = var6.x0 * AvMain.hd;
                  int var10003 = var6.y0 * AvMain.hd;
                  int var10004 = var6.w * AvMain.hd;
                  int var10005 = var6.h * AvMain.hd;
                  var2.drawRegion(getBigImgInfo(var0.id).img, var10002, var10003, var10004, var10005, Base.LEFT, var6.x0, var6.y0, 0);
               }
            }
         }
      }

      var1 = CRes.createRGBImage(var1, -65281);
      BigImgInfo var7;
      (var7 = new BigImgInfo()).follow = var0.follow;
      var7.id = var0.id;
      var7.img = var1;
      var7.ver = var0.ver;
      listBigImgBB.put("" + var7.id, var7);
   }

   public static void drawImgRegion(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10002;
      if (var8 == 0 || !Canvas.E && !Canvas.F) {
         var10002 = var2 * AvMain.hd;
         int var10003 = var3 * AvMain.hd;
         int var10004 = var4 * AvMain.hd;
         int var10005 = var5 * AvMain.hd;
         var0.drawRegion(getBigImgInfo(var1).img, var10002, var10003, var10004, var10005, var8, var6, var7, 0);
      } else {
         var10002 = var2 * AvMain.hd;
         int var10003 = var3 * AvMain.hd;
         var0.drawRegion(((BigImgInfo)listBigImgBB.get("" + var1)).img, var10002, var10003, var4 * AvMain.hd, var5 * AvMain.hd, 0, var6, var7, 0);
      }

   }

   public static DataInputStream loadRMS(String var0) {
      byte[] var1;
      if ((var1 = CRes.loadRMS(var0)) == null) {
         return null;
      } else {
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
         return new DataInputStream(var2);
      }
   }

   public static void saveProvider() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte(GameMidlet.PROVIDER);
         var1.writeUTF(GameMidlet.g);
         CRes.saveRMS("avatarSV", var0.toByteArray());
         var1.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void loadProvider() {
      DataInputStream var0;
      if ((var0 = loadRMS("avatarSV")) != null) {
         try {
            GameMidlet.PROVIDER = var0.readByte();
            GameMidlet.g = var0.readUTF();
            var0.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   public static void saveServerList() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte(SERVER_INDEX);
         var1.writeByte(GameMidlet.ipSV[SERVER_INDEX].length);

         for(int var2 = 0; var2 < GameMidlet.ipSV[SERVER_INDEX].length; ++var2) {
            var1.writeByte(GameMidlet.ipSV[SERVER_INDEX][var2].length);
            var1.writeUTF(GameMidlet.nameSV[SERVER_INDEX][var2][0]);

            for(int var3 = 0; var3 < GameMidlet.ipSV[SERVER_INDEX][var2].length; ++var3) {
               var1.writeUTF(GameMidlet.nameSV[SERVER_INDEX][var2][var3 + 1]);
               var1.writeUTF(GameMidlet.ipSV[SERVER_INDEX][var2][var3]);
               var1.writeInt(GameMidlet.PORT[SERVER_INDEX][var2][var3]);
            }
         }

         CRes.saveRMS("avatarSV", var0.toByteArray());
         var1.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void loadServerList() {
      DataInputStream var0;
      if ((var0 = loadRMS("avatarSV")) != null) {
         try {
            var0.readByte();
            byte var1;
            if ((var1 = var0.readByte()) == 0) {
               delErrorRms("avatarSV");
            } else {
               ensureServerArraySize();
               GameMidlet.nameSV[SERVER_INDEX] = new String[var1][];
               GameMidlet.ipSV[SERVER_INDEX] = new String[var1][];
               GameMidlet.PORT[SERVER_INDEX] = new int[var1][];

               for(int var2 = 0; var2 < var1; ++var2) {
                  byte var3 = var0.readByte();
                  GameMidlet.nameSV[SERVER_INDEX][var2] = new String[var3 + 1];
                  GameMidlet.nameSV[SERVER_INDEX][var2][0] = var0.readUTF();
                  GameMidlet.ipSV[SERVER_INDEX][var2] = new String[var3];
                  GameMidlet.PORT[SERVER_INDEX][var2] = new int[var3];

                  for(int var4 = 0; var4 < var3; ++var4) {
                     GameMidlet.nameSV[SERVER_INDEX][var2][var4 + 1] = var0.readUTF();
                     GameMidlet.ipSV[SERVER_INDEX][var2][var4] = var0.readUTF();
                     GameMidlet.PORT[SERVER_INDEX][var2][var4] = var0.readInt();
                  }
               }

               var0.close();
            }
         } catch (IOException var5) {
            var5.printStackTrace();
            delErrorRms("avatarSV");
         }
      }

   }

   public static void ensureServerArraySize() {
      if (GameMidlet.nameSV == null || GameMidlet.nameSV.length < 2) {
         String[][][] var0 = GameMidlet.nameSV;
         String[][][] var1 = GameMidlet.ipSV;
         int[][][] var2 = GameMidlet.PORT;
         GameMidlet.nameSV = new String[2][][];
         GameMidlet.ipSV = new String[2][][];
         GameMidlet.PORT = new int[2][][];

         for(int var3 = 0; var0 != null && var3 < var0.length && var3 < 2; ++var3) {
            GameMidlet.nameSV[var3] = var0[var3];
            GameMidlet.ipSV[var3] = var1[var3];
            GameMidlet.PORT[var3] = var2[var3];
         }
      }

   }

   public static boolean hasServerList(int var0) {
      return GameMidlet.nameSV != null && GameMidlet.nameSV.length > var0 && GameMidlet.nameSV[var0] != null && GameMidlet.nameSV[var0].length > 0;
   }

   public static void applyServerListText(String var0, int var1) {
      String[] var6 = Canvas.normalFont.split(var0, "*");
      GameMidlet.PORT[var1] = new int[var6.length - 1][];
      GameMidlet.ipSV[var1] = new String[var6.length - 1][];
      GameMidlet.nameSV[var1] = new String[var6.length - 1][];

      for(int var2 = 1; var2 < var6.length; ++var2) {
         String[] var3 = Canvas.normalFont.split(var6[var2], "\n");
         GameMidlet.nameSV[var1][var2 - 1] = new String[var3.length - 1];
         GameMidlet.ipSV[var1][var2 - 1] = new String[var3.length - 2];
         GameMidlet.PORT[var1][var2 - 1] = new int[var3.length - 2];
         GameMidlet.nameSV[var1][var2 - 1][0] = var3[0];

         for(int var4 = 1; var4 < var3.length - 1; ++var4) {
            String[] var5 = Canvas.normalFont.split(var3[var4], ":");
            GameMidlet.nameSV[var1][var2 - 1][var4] = var5[0];
            GameMidlet.ipSV[var1][var2 - 1][var4 - 1] = var5[1];
            var5[2] = var5[2].substring(0, var5[2].length() - 1);
            GameMidlet.PORT[var1][var2 - 1][var4 - 1] = Integer.parseInt(var5[2]);
         }
      }

   }

   public static boolean refreshServerListFromHost() {
      ensureServerArraySize();

      for(int var1 = 0; var1 < GameMidlet.linkGetHost[SERVER_INDEX].length; ++var1) {
         String var2;
         if ((var2 = GameMidlet.createhttpconnect(GameMidlet.linkGetHost[SERVER_INDEX][var1])) != null) {
            applyServerListText(var2, SERVER_INDEX);
            saveServerList();
            return true;
         }
      }

      return false;
   }

   private static BigImgInfo getBigImgInfoList(int var0) {
      int var1 = bigImgInfo.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         BigImgInfo var3;
         if ((var3 = (BigImgInfo)bigImgInfo.elementAt(var2)).id == var0) {
            return var3;
         }
      }

      return null;
   }

   public static BigImgInfo getBigImgInfo(int var0) {
      return (BigImgInfo)listBigImg.get("" + var0);
   }

   public static MapItemType getMapItemTypeByID(int var0) {
      int var1 = listMapItemType.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (((MapItemType)listMapItemType.elementAt(var2)).idType == var0) {
            return (MapItemType)listMapItemType.elementAt(var2);
         }
      }

      return null;
   }

   public static void onMapAd(Vector var0) {
      listAd = var0;
   }

   public static boolean isZOrderMain(int var0) {
      return var0 == 10 || var0 == 20 || var0 == 30 || var0 == 40 || var0 == 50;
   }

   public static APartInfo getPartByZ(Vector var0, int var1) {
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.size(); ++var2) {
            SeriPart var3;
            Part var4 = getPart((var3 = (SeriPart)var0.elementAt(var2)).idPart);
            if (var3 != null && var4 instanceof APartInfo && ((APartInfo)var4).zOrder == var1) {
               return (APartInfo)var4;
            }
         }
      }

      return null;
   }

   public static SeriPart getSeriByIdPart(Vector var0, int var1) {
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         SeriPart var4;
         if ((var4 = (SeriPart)var0.elementAt(var3)).idPart == var1) {
            return var4;
         }
      }

      return null;
   }

   public static SeriPart getSeriByZ(int var0, Vector var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         SeriPart var4;
         if (getPart((var4 = (SeriPart)var1.elementAt(var3)).idPart).zOrder == var0) {
            return var4;
         }
      }

      return null;
   }

   public static Part getPart(short var0) {
      if (var0 >= 2000) {
         Object var1;
         if ((var1 = (Part)listPartDynamic.get("" + var0)) == null) {
            ((Part)((Part)(var1 = new APartInfo()))).IDPart = -1;
            listPartDynamic.put("" + var0, var1);
            GlobalService.gI().requestPartDynaMic(var0);
         }

         return (Part)var1;
      } else {
         return listPart[var0];
      }
   }

   public static String getName(Part var0) {
      return var0.follow >= 0 ? getPart(var0.follow).name : var0.name;
   }

   public static void paintImg(Graphics var0, int var1, int var2, int var3, int var4) {
      if (getImgIcon((short)var1).count != -1) {
         var0.drawImage(getImgIcon((short)var1).img, var2, var3, var4);
      }

   }

   public static ImageIcon getImagePart(short var0) {
      ImageIcon var1;
      if ((var1 = (ImageIcon)listImgPart.get("" + var0)) == null) {
         var1 = new ImageIcon();
         listImgPart.put("" + var0, var1);
         GlobalService.gI().requestImagePart(var0);
      } else if (var1.count >= 0) {
         var1.count = (int)(System.currentTimeMillis() / 1000L);
      }

      return var1;
   }

   public static ImageIcon getImgIcon(short var0) {
      ImageIcon var1;
      if ((var1 = (ImageIcon)listImgIcon.get("" + var0)) == null) {
         var1 = new ImageIcon();
         listImgIcon.put("" + var0, var1);
         AvatarService.gI().doGetImgIcon(var0);
      } else if (var1.count >= 0) {
         var1.count = (int)(System.currentTimeMillis() / 1000L);
      }

      return var1;
   }

   public static void setLimitImage() {
      Enumeration var0;
      String var1;
      ImageIcon var2;
      if (listImgIcon.size() > 50) {
         var0 = listImgIcon.keys();

         while(var0.hasMoreElements()) {
            var1 = (String)var0.nextElement();
            if ((var2 = (ImageIcon)listImgIcon.get(var1)).count != -1 && System.currentTimeMillis() / 1000L - (long)var2.count > (long)Canvas.V) {
               listImgIcon.remove(var1);
            }
         }
      }

      if (listImgPart.size() > 50) {
         var0 = listImgPart.keys();

         while(var0.hasMoreElements()) {
            var1 = (String)var0.nextElement();
            if ((var2 = (ImageIcon)listImgPart.get(var1)).count != -1 && System.currentTimeMillis() / 1000L - (long)var2.count > (long)Canvas.V) {
               listImgPart.remove(var1);
            }
         }
      }

   }

   public static int getLevel(Part var0) {
      byte var1;
      if (var0.follow >= 0) {
         var1 = ((APartInfo)getPart(var0.follow)).level;
      } else {
         var1 = ((APartInfo)var0).level;
      }

      return var1;
   }

   public static EffectData getEffect(short var0) {
      for(int var1 = 0; var1 < effectList.size(); ++var1) {
         EffectData var2;
         if ((var2 = (EffectData)effectList.elementAt(var1)).ID == var0) {
            return var2;
         }
      }

      return null;
   }

   public static void delErrorRms(String var0) {
      try {
         RecordStore.deleteRecordStore(GameMidlet.APP_VERSION + var0);
      } catch (Exception var2) {
      }

   }
}
