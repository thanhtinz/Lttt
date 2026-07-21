package avt;

import java.util.Vector;
import main.Canvas;
import main.GameMidlet;

public final class CasinoMsgHandler extends IService implements IMiniGameMsgHandler {
   public static CasinoMsgHandler me = new CasinoMsgHandler();
   public IMiniGameMsgHandler miniGameMessageHandler;
   public static BoardScr curScr;

   public static void gI() {
      GlobalMessageHandler.gI().miniGameMessageHandler = me;
   }

   public final void onMessage(Message var1) {
      try {
         int var2;
         int var5;
         int var9;
         String var12;
         int var13;
         byte var15;
         byte var16;
         int var17;
         Avatar var25;
         byte var10;
         switch (var1.command) {
            case 6:
               Vector var21 = new Vector();

               while(var1.reader().available() > 0) {
                  RoomInfo var19;
                  (var19 = new RoomInfo()).id = var1.reader().readByte();
                  var19.roomFree = var1.reader().readByte();
                  var1.reader().readByte();
                  var19.lv = var1.reader().readByte();
                  var21.addElement(var19);
               }

               RoomListOnScr.gI().setRoomList(var21);
               RoomListOnScr.gI().switchToMe();
               Canvas.endDlg();
               return;
            case 7:
               Vector var18 = new Vector();
               var15 = var1.reader().readByte();

               while(var1.reader().available() > 0) {
                  BoardInfo var20;
                  (var20 = new BoardInfo()).boardID = var1.reader().readByte();
                  var5 = var1.reader().readUnsignedByte();
                  var20.nPlayer = (byte)(var5 % 16);
                  var20.maxPlayer = (byte)(var5 / 16);
                  int var22 = var1.reader().readUnsignedByte();
                  var20.isPass = (var22 & 1) != 0;
                  var20.isPlaying = (var22 & 2) != 0;
                  var20.money = var1.reader().readInt();
                  var20.strMoney = Canvas.getMoneys(var20.money) + T.getMoney();
                  var18.addElement(var20);
               }

               BoardListOnScr.gI().roomID = var15;
               BoardListOnScr.gI().a(var18);
               BoardListOnScr.gI().switchToMe();
               BoardListOnScr.gI().init();
               Canvas.endDlg();
               return;
            case 8:
               Canvas.load = 0;
               var15 = var1.reader().readByte();
               var16 = var1.reader().readByte();
               var17 = var1.reader().readInt();
               var5 = var1.reader().readInt();

               Vector var6;
               Avatar var7;
               int var24;
               for(var6 = new Vector(); var1.reader().available() > 0; var6.addElement(var7)) {
                  (var7 = new Avatar()).IDDB = var1.reader().readInt();
                  if (var7.IDDB == -1) {
                     var7.setName("");
                  } else {
                     if (var7.IDDB == GameMidlet.avatar.IDDB) {
                        var7 = GameMidlet.avatar;
                     }

                     var7.setName(var1.reader().readUTF());
                     var7.setMoneyNew(var1.reader().readInt());
                     var24 = var1.reader().readByte();

                     for(var9 = 0; var9 < var24; ++var9) {
                        SeriPart var26 = new SeriPart(var1.reader().readShort());
                        if (var7.IDDB != GameMidlet.avatar.IDDB) {
                           var7.addSeri(var26);
                        }
                     }

                     var9 = var1.reader().readInt();
                     var7.setExp(var9);
                     var7.isReady = var1.reader().readBoolean();
                     var7.setExp(var9);
                     var7.setMoneyNew(var7.getMoneyNew());
                     var7.idImg = var1.reader().readShort();
                  }
               }

               curScr.setPlayers(var15, var16, var17, var5, var6);
               TLBoardScr.gI().isFirstMatch = true;
               BoardScr.disableReady = false;
               int var23 = var6.size();

               for(var24 = 0; var24 < var23; ++var24) {
                  if ((var25 = (Avatar)var6.elementAt(var24)).IDDB == var17) {
                     var25.isReady = true;
                  }

                  if (var25.IDDB == GameMidlet.avatar.IDDB) {
                     GameMidlet.avatar.setMoneyNew(var25.getMoneyNew());
                  }
               }

               if (BoardListOnScr.type != 0) {
                  byte var10000 = BoardListOnScr.type;
                  var10 = BoardListOnScr.STYLE_4PLAYER;
               }

               curScr.loadMap();
               curScr.switchToMe();
               TLBoardScr.gI();
               TLBoardScr.setMode(false);
               Canvas.endDlg();
               Canvas.load = 1;
               return;
            case 9:
               var15 = var1.reader().readByte();
               var16 = var1.reader().readByte();
               var17 = var1.reader().readInt();
               var12 = var1.reader().readUTF();
               if (BoardScr.setR_B(var15, var16)) {
                  BoardScr.showChat(var17, var12);
                  return;
               }
               break;
            case 11:
               var15 = var1.reader().readByte();
               var16 = var1.reader().readByte();
               var13 = var1.reader().readInt();
               Canvas.currentDialog = null;
               if (BoardScr.setR_B(var15, var16)) {
                  if (var13 == GameMidlet.avatar.IDDB) {
                     Canvas.startOK(T.youAreKicked, new IActionKick(this));
                     return;
                  }

                  BoardScr.me.playerLeave(var13);
                  return;
               }
               break;
            case 12:
               var25 = new Avatar();
               var9 = var1.reader().readByte();
               var25.IDDB = var1.reader().readInt();
               var25.setName(var1.reader().readUTF());
               var25.setMoneyNew(var1.reader().readInt());
               var10 = var1.reader().readByte();

               for(var2 = 0; var2 < var10; ++var2) {
                  var25.addSeri(new SeriPart(var1.reader().readShort()));
               }

               var25.direct = 0;
               var25.setExp(var1.reader().readInt());
               var25.idImg = var1.reader().readShort();
               var25.isReady = false;
               TLBoardScr.gI().isFirstMatch = true;
               var25.isReady = false;
               curScr.setAt(var9, var25);
               return;
            case 14:
               var2 = var1.reader().readInt();
               var13 = var1.reader().readInt();
               if (BoardScr.isStartGame && BoardScr.numPlayer == 2) {
                  curScr.closeBoard(T.opponentQuit);
               }

               TLBoardScr.gI().isFirstMatch = true;
               BoardScr.me.playerLeave(var2);
               BoardScr.setOwner(var13);
               return;
            case 16:
               var2 = var1.reader().readInt();
               boolean var14 = var1.reader().readBoolean();
               if (var2 == GameMidlet.avatar.IDDB) {
                  Canvas.endDlg();
               }

               BoardScr.setReady(var2, var14);
               return;
            case 19:
               var15 = var1.reader().readByte();
               var16 = var1.reader().readByte();
               var13 = var1.reader().readInt();
               if (BoardScr.setR_B(var15, var16)) {
                  curScr.setMoney(var13);
                  return;
               }
               break;
            case 52:
               var1.reader().readByte();
               var1.reader().readByte();
               var2 = var1.reader().readInt();
               int var3 = var1.reader().readInt();
               var12 = var1.reader().readUTF();
               Avatar var4 = BoardScr.getAvatarByID(var2);
               if (var3 != 0 && var4 != null) {
                  var4.setMoneyNew(var4.getMoneyNew() + var3);
                  if (GameMidlet.avatar.IDDB == var2) {
                     GameMidlet.avatar.setMoneyNew(var4.getMoneyNew());
                  }

                  BoardScr.showChat(var2, var12);
                  BoardScr.showFlyText(var2, var3);
                  return;
               }

               return;
            case 61:
               switch (var1.reader().readByte()) {
                  case 3:
                     TienLenMsgHandler.onHandler();
                     break;
                  case 7:
                     PhomMsgHandler.onHandler();
                     break;
                  case 21:
                     DiamondMessageHandler.a();
                     break;
                  case 22:
                     BoardScr.numPlayer = 5;
                     BoardListOnScr.type = BoardListOnScr.STYLE_5PLAYER;
                     RoomListOnScr.setName(3, BCBoardScr.gI());
                     if (BaucuaMsgHandler.instance == null) {
                        BaucuaMsgHandler.instance = new BaucuaMsgHandler();
                     }

                     me.miniGameMessageHandler = BaucuaMsgHandler.instance;
                     break;
                  case 23:
                     BaLaMsgHandler.onHandler();
                     break;
                  default:
                     return;
               }

               Canvas.startWaitDlg(T.pleaseWait);
               CasinoService.gI().requestRoomList();
               return;
            default:
               this.miniGameMessageHandler.onMessage(var1);
               return;
         }
      } catch (Exception var21) {
         var21.printStackTrace();
      }

   }
}
