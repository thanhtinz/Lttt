package avt;

import java.util.Vector;
import main.Canvas;

public final class TienLenMsgHandler extends IService implements IMiniGameMsgHandler {
   private static TienLenMsgHandler instance = new TienLenMsgHandler();

   public static void onHandler() {
      BoardScr.numPlayer = 4;
      BoardListOnScr.type = BoardListOnScr.STYLE_4PLAYER;
      RoomListOnScr.setName(0, TLBoardScr.gI());
      CasinoMsgHandler.me.miniGameMessageHandler = instance;
   }

   public final void onMessage(Message var1) {
      try {
         int var2 = var1.reader().readByte();
         int var3 = var1.reader().readByte();
         if (!BoardScr.setR_B((byte)var2, (byte)var3)) {
            return;
         }

         System.out.println("tienlen: " + var1.command);
         int var5;
         byte var13;
         byte var14;
         int var15;
         switch (var1.command) {
            case 20:
               var14 = var1.reader().readByte();
               Vector var17 = new Vector();

               for(var15 = 0; var15 < 13; ++var15) {
                  var17.addElement(new Card(var1.reader().readByte()));
               }

               var15 = var1.reader().readInt();
               Canvas.endDlg();
               BoardScr.resetReady();
               TLBoardScr.gI().start(var15, var14, var17);
               CasinoService var11 = CasinoService.gI();

               try {
                  var11.createMessageWithBoard((byte)53);
               } catch (Exception e) {
               }

               var11.sendMessage();
               return;
            case 21:
               var5 = var1.reader().readInt();
               byte[] var16 = new byte[var14 = var1.reader().readByte()];

               for(var15 = 0; var15 < var14; ++var15) {
                  var16[var15] = var1.reader().readByte();
               }

               var15 = var1.reader().readInt();
               BoardScr.disableReady = true;
               TLBoardScr.gI().move(var5, var16, var15);
               TLBoardScr.gI().setPosPlaying();
               return;
            case 49:
               var2 = var1.reader().readInt();
               var3 = var1.reader().readInt();
               boolean var10 = var1.reader().readBoolean();
               TLBoardScr.gI().skip(var2, var3, var10);
               return;
            case 50:
               TLBoardScr.gI().isFirstMatch = false;
               TLBoardScr.gI();
               TLBoardScr.stopGame();
               if (var1.reader().available() > 0) {
                  var2 = var1.reader().readInt();
                  byte[] var18 = new byte[var13 = var1.reader().readByte()];

                  for(var5 = 0; var5 < var13; ++var5) {
                     var18[var5] = var1.reader().readByte();
                  }

                  TLBoardScr.gI().showCards(var2, var18);
                  return;
               }
               break;
            case 51:
               var2 = var1.reader().readInt();
               var13 = var1.reader().readByte();
               var15 = var1.reader().readInt();
               int var9 = var1.reader().readInt();
               TLBoardScr.gI();
               TLBoardScr.finish(var2, var13, var15, var9);
               return;
            case 53:
               var3 = var1.reader().readInt();
               byte[] var12 = new byte[13];

               try {
                  for(var15 = 0; var15 < 13; ++var15) {
                     var12[var15] = var1.reader().readByte();
                  }
               } catch (Exception e) {
                  var12 = null;
               }

               Canvas.endDlg();
               TLBoardScr.gI();
               TLBoardScr.stopGame();
               if (var12 != null) {
                  TLBoardScr.gI().showCards(var3, var12);
               }

               BoardScr.showChat(var3, T.forceFinish);
               return;
            case 54:
               String var4 = var1.reader().readUTF();
               TLBoardScr.gI().moveError(var4);
            default:
               return;
         }
      } catch (Exception var17) {
         var17.printStackTrace();
      }

   }
}
