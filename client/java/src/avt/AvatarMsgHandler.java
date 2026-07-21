package avt;

import java.util.Vector;

public final class AvatarMsgHandler extends IService implements IMiniGameMsgHandler {
   private static AvatarMsgHandler instance = new AvatarMsgHandler();

   public static void onHandler() {
      GlobalMessageHandler.gI().miniGameMessageHandler = instance;
   }

   public final void onMessage(Message var1) {
      try {
         int var3;
         BigImgInfo var10;
         byte[] var13;
         switch (var1.command) {
            case -41:
               var13 = new byte[var1.reader().available()];
               var1.reader().read(var13);
               AvatarData.saveMapItem(var13);
               return;
            case -40:
               var13 = new byte[var1.reader().available()];
               var1.reader().read(var13);
               AvatarData.saveMapItemType(var13);
               return;
            case -37:
               var13 = new byte[var1.reader().available()];
               var1.reader().read(var13);
               AvatarData.saveItemData(var13);
               return;
            case -16:
               var13 = new byte[var1.reader().available()];
               var1.reader().read(var13);
               AvatarData.saveAvatarPart(var13);
               return;
            case -15:
               byte[] var14 = new byte[var1.reader().available()];
               var1.reader().read(var14);
               AvatarData.saveImageData(var14);
               return;
            case -14:
               (var10 = new BigImgInfo()).id = var1.reader().readShort();
               var10.ver = var1.reader().readShort();
               int var12 = var1.reader().readUnsignedShort();
               var10.data = new byte[var12];

               for(var3 = 0; var3 < var12; ++var3) {
                  var10.data[var3] = var1.reader().readByte();
               }

               var10.follow = -1;
               if (var1.reader().available() >= 2) {
                  var10.follow = var1.reader().readShort();
               }

               AvatarData.saveItemData(var10);
               return;
            case -11:
               Vector var2 = new Vector();
               var3 = var1.reader().readByte();

               for(int var4 = 0; var4 < var3; ++var4) {
                  BigImgInfo var5;
                  (var5 = new BigImgInfo()).id = var1.reader().readShort();
                  var5.ver = var1.reader().readShort();
                  var2.addElement(var5);
               }

               short var15 = var1.reader().readShort();
               short var16 = var1.reader().readShort();
               var3 = var1.reader().readShort();
               short var6 = var1.reader().readShort();
               short var7 = var1.reader().readShort();
               byte var8 = var1.reader().readByte();

               for(int var9 = 0; var9 < var8; ++var9) {
                  (var10 = new BigImgInfo()).id = var1.reader().readShort();
                  var10.ver = var1.reader().readShort();
                  var2.addElement(var10);
               }

               var1.reader().readInt();
               AvatarData.checkDataAvatar(var2, var15, var16, var3, var6, var7);
               return;
         }
      } catch (Exception var14) {
         var14.printStackTrace();
      }

   }
}
