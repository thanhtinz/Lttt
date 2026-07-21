package avt;

import java.io.IOException;
import main.Canvas;
import main.GameMidlet;

public final class ParkMsgHandler extends IService implements IMiniGameMsgHandler {
   public static ParkMsgHandler instance;

   public static void onHandler() {
      if (instance == null) {
         instance = new ParkMsgHandler();
      }

      GlobalMessageHandler.gI().miniGameMessageHandler = instance;
   }

   public final void onMessage(Message var1) {
      try {
         int var2;
         int var3;
         int var4;
         int var5;
         int var7;
         int var8;
         int var10;
         short var11;
         byte var12;
         byte var14;
         short var16;
         String var17;
         int var18;
         switch (var1.command) {
            case -69:
               Canvas.startOK(T.outHouse, new IActionKickOutHome(this));
               return;
            case -68:
               var14 = var1.reader().readByte();
               var10 = var1.reader().readInt();
               MapScr.gI().onInviteToMyHome(var14, var10);
               return;
            case 51:
               MapScr.gI().onPlayerJoinPark(playerJoinBoard(var1));
               return;
            case 53:
               var10 = var1.reader().readInt();
               MapScr.gI();
               MapScr.onPlayerLeave(var10);
               return;
            case 54:
               GlobalMessageHandler.readMove(var1);
               return;
            case 55:
               GlobalMessageHandler.readChat(var1);
               return;
            case 57:
               var2 = var1.reader().readInt();
               var12 = var1.reader().readByte();
               MapScr.gI();
               MapScr.onFeel(var2, var12);
               return;
            case 58:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               var16 = var1.reader().readShort();
               var17 = "";
               if (var16 == -1) {
                  var17 = var1.reader().readUTF();
               }

               var18 = var1.reader().readInt();
               var1.reader().readByte();
               System.out.println("AVATAR_GIFT_GIVING: " + var1.reader().available());
               var7 = var1.reader().readInt();
               var8 = var1.reader().readInt();
               var10 = var1.reader().readInt();
               MapScr.gI().onGiftGiving(var2, var3, var16, var17, var18, var7, var8, var10);
               return;
            case 59:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               var16 = var1.reader().readShort();
               var17 = "";
               var18 = 0;
               if (var16 == -1) {
                  var17 = var1.reader().readUTF();
               } else {
                  var18 = var1.reader().readShort();
               }

               MapScr.gI().onGivingDefferent(var2, var3, var16, var17, var18);
               return;
            case 60:
               int[] var15 = new int[var14 = var1.reader().readByte()];

               for(var4 = 0; var4 < var14; ++var4) {
                  var15[var4] = var1.reader().readByte();
               }

               MapScr.gI().onParkList(var15);
               Canvas.endDlg();
               return;
            case 78:
               return;
            case 82:
               var4 = var1.reader().readInt();
               FishingScr.gI().onQuanCau(var4);
               return;
            case 84:
               var2 = var1.reader().readInt();
               var11 = var1.reader().readShort();
               FishingScr.gI().onFinish(var2, var11);
               return;
            case 85:
               var10 = var1.reader().readInt();
               FishingScr.gI().onCauCaXong(var10);
               return;
            case 86:
               boolean var19 = var1.reader().readBoolean();
               String var20 = "";
               if (!var19) {
                  var20 = var1.reader().readUTF();
               }

               FishingScr.gI().onStartFishing(var19, var20);
               return;
            case 87:
               var2 = var1.reader().readInt();
               var12 = var1.reader().readByte();
               FishingScr.gI();
               FishingScr.onStatus(var2, var12);
               return;
            case 88:
               var2 = var1.reader().readInt();
               byte var13 = var1.reader().readByte();
               var16 = (short)var1.reader().readByte();
               var5 = var1.reader().readInt();
               var11 = var1.reader().readShort();
               FishingScr.gI().onInfo(var2, var13, (byte)var16, var5, var11);
               return;
            case 91:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readShort();
               var4 = var1.reader().readShort();
               byte[][] var6 = new byte[var5 = var1.reader().readByte()][];

               for(var7 = 0; var7 < var5; ++var7) {
                  var8 = var1.reader().readShort();
                  var6[var7] = new byte[var8];
                  var1.reader().read(var6[var7]);
               }

               FishingScr.gI().onCaCanCau(var2, var3, (short)var4, var6);
               return;
            case 92:
               if (MapScr.s = var1.reader().readBoolean()) {
                  GameMidlet.avatar.timeTask = var1.reader().readShort();
                  return;
               }
               break;
            case 93:
               var2 = var1.reader().readInt();
               var10 = var1.reader().readInt();
               MapScr.gI().onWeddingStart(var2, var10);
               return;
            default:
               return;
         }
      } catch (Exception var20) {
         var20.printStackTrace();
      }

   }

   public static Avatar playerJoinBoard(Message var0) throws IOException {
      Avatar var1;
      (var1 = new Avatar()).IDDB = var0.reader().readInt();
      var1.setName(var0.reader().readUTF());
      byte var2 = var0.reader().readByte();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.addSeri(new SeriPart(var0.reader().readShort()));
      }

      var1.x = var1.xCur = var0.reader().readShort();
      var1.y = var1.yCur = var0.reader().readShort();
      var1.blogNews = var0.reader().readByte();
      var1.hungerPet = (short)((byte)(100 - var0.reader().readByte()));
      var1.idImg = var0.reader().readShort();
      var1.idWedding = var0.reader().readShort();
      return var1;
   }
}
