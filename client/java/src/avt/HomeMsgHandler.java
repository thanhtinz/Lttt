package avt;

import java.util.Vector;
import main.Canvas;

public final class HomeMsgHandler extends IService implements IMiniGameMsgHandler {
   public static HomeMsgHandler instance = new HomeMsgHandler();

   public static void onHandler() {
      GlobalMessageHandler.gI().miniGameMessageHandler = instance;
   }

   public final void onMessage(Message var1) {
      try {
         int var2;
         int var7;
         short var17;
         Vector var18;
         short var19;
         switch (var1.command) {
            case -75:
               int var12 = var1.reader().readInt();
               Canvas.inputDlg.setImg(T.setPass, new IActionSetPass(this, var12), 0);
            default:
               return;
            case -73:
               var19 = var1.reader().readShort();
               byte[] var22 = new byte[var1.reader().readInt()];
               var1.reader().read(var22);
               HouseScr.gI().saveTileMap((byte[])var22, var19);
               return;
            case -67:
               byte var14 = var1.reader().readByte();
               byte var16 = -1;
               var17 = 0;
               var18 = null;
               if (var14 == 0) {
                  var17 = var1.reader().readShort();
                  var16 = var1.reader().readByte();
               } else {
                  var18 = new Vector();
                  var19 = var1.reader().readShort();

                  for(var7 = 0; var7 < var19; ++var7) {
                     Avatar var21;
                     (var21 = new Avatar()).IDDB = var1.reader().readInt();
                     var21.typeHome = var1.reader().readByte();
                     var18.addElement(var21);
                  }
               }

               HouseScr.gI().onGetTypeHouse(var14, var16, var17, var18);
               return;
            case -66:
               MapItem var13;
               (var13 = new MapItem()).typeID = var1.reader().readShort();
               var13.x = var1.reader().readByte();
               var13.y = var1.reader().readByte();
               HouseScr.gI().onRemoveItem(var13);
               return;
            case -65:
               var17 = (short)var1.reader().readByte();
               var2 = var1.reader().readInt();
               short var5;
               short[] var15 = new short[var5 = var1.reader().readShort()];

               int var8;
               for(var8 = 0; var8 < var5; ++var8) {
                  var15[var8] = (short)var1.reader().readByte();
               }

               var19 = (short)var1.reader().readByte();
               var18 = new Vector();
               var7 = var1.reader().readShort();

               for(var8 = 0; var8 < var7; ++var8) {
                  MapItem var23;
                  (var23 = new MapItem()).typeID = var1.reader().readShort();
                  var23.x = var1.reader().readByte() * 24;
                  var23.y = var1.reader().readByte() * 24;
                  var23.dir = var1.reader().readByte();
                  var18.addElement(var23);
               }

               Vector var20 = GlobalMessageHandler.readListPlayer(var1);
               ParkMsgHandler.onHandler();
               HouseScr.gI().onJoin((byte)var17, var2, var15, (byte)var19, var18, var20);
               return;
            case -46:
               short var9 = var1.reader().readShort();
               String var11 = var1.reader().readUTF();
               HouseScr.gI().onCreateHome(var9, var11);
               return;
            case -43:
               Tile[] var3 = new Tile[var2 = var1.reader().readShort()];

               for(int var4 = 0; var4 < var2; ++var4) {
                  var3[var4] = new Tile();
                  var3[var4].name = var1.reader().readUTF();
                  var3[var4].priceXu = var1.reader().readInt();
                  var3[var4].priceLuong = var1.reader().readInt();
               }

               HouseScr.gI().onGetTileInfo(var3);
               return;
            case 51:
               MapScr.gI().onPlayerJoinPark(ParkMsgHandler.playerJoinBoard(var1));
               return;
            case 76:
               GlobalMessageHandler.readMove(var1);
               return;
            case 77:
               GlobalMessageHandler.readChat(var1);
         }
      } catch (Exception var19) {
         var19.printStackTrace();
      }

   }
}
