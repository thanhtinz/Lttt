package avt;

public final class BaucuaMsgHandler implements IMiniGameMsgHandler {
   public static BaucuaMsgHandler instance;

   public final void onMessage(Message var1) {
      try {
         int var2 = var1.reader().readByte();
         int var3 = var1.reader().readByte();
         if (!BoardScr.setR_B((byte)var2, (byte)var3)) {
            return;
         }

         int var6;
         byte var7;
         switch (var1.command) {
            case 20:
               var7 = var1.reader().readByte();
               BCBoardScr.me_.saveTime = var7;
               BCBoardScr.me_.onStartGame((byte)var2, (byte)var3, var7);
               return;
            case 21:
               if ((var7 = var1.reader().readByte()) == -1) {
                  BCBoardScr.me_.setBotCmd();
                  BCBoardScr.me_.canpointer = false;
                  return;
               }

               if (var7 != -1) {
                  for(var3 = 0; var3 < 6; ++var3) {
                     BCBoardScr.me_.moneySV[var7][var3] = var1.reader().readByte();
                  }

                  BCBoardScr.me_.onMove(var7);
                  return;
               }
               break;
            case 37:
               byte[] var10 = new byte[3];

               for(var2 = 0; var2 < 3; ++var2) {
                  var10[var2] = var1.reader().readByte();
               }

               BCBoardScr.me_.onResult(var10);
               BoardScr.setCmdWaiting();
               return;
            case 51:
               int[] var8 = new int[BoardScr.avatarInfos.size()];

               for(var3 = 0; var3 < var8.length; ++var3) {
                  var8[var3] = var1.reader().readInt();
               }

               BCBoardScr.me_.onFinish(var8);
               return;
            case 60:
               var7 = var1.reader().readByte();
               byte var9 = var1.reader().readByte();
               var6 = var1.reader().readInt();
               BCBoardScr.me_.showFlyText5Baucua(var7, var9, var6);
               return;
            case 62:
               var2 = var1.reader().readByte();
               BCBoardScr.me_.saveTime = (byte)var2;

               for(var3 = 0; var3 < BoardScr.avatarInfos.size(); ++var3) {
                  for(var2 = 0; var2 < 6; ++var2) {
                     BCBoardScr.me_.moneySV[var3][var2] = var1.reader().readByte();
                  }
               }

               BCBoardScr.me_.onPlaying();
            default:
               return;
            case 65:
               var2 = var1.reader().readByte();
               var3 = var1.reader().readByte();
               byte var4 = var1.reader().readByte();
               var6 = var1.reader().readByte();
               if (var3 != var4 && var6 > 0) {
                  BCBoardScr.me_.moneySV[var2][var4] = (byte)var6;
                  BCBoardScr.me_.onHaphom((byte)var2, (byte)var3, var4);
                  return;
               }
               break;
            case 100:
               var2 = var1.reader().readByte();
               BCBoardScr.me_.onSetTurn((byte)var2);
               return;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }
}
