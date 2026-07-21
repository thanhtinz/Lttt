package avt;

import java.util.Vector;
import main.Canvas;
import main.GameMidlet;

public final class BaLaMsgHandler extends IService implements IMiniGameMsgHandler {
   private static BaLaMsgHandler instance = new BaLaMsgHandler();

   public static void onHandler() {
      BoardScr.numPlayer = 4;
      BoardListOnScr.type = BoardListOnScr.STYLE_4PLAYER;
      RoomListOnScr.setName(4, BaLaBoardScr.gI());
      CasinoMsgHandler.me.miniGameMessageHandler = instance;
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
         Avatar var25;

         switch (var1.command) {
            case 6: {
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
            }
            case 7: {
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
            }
            case 8: {
               Canvas.load = 0;
               var15 = var1.reader().readByte();
               var16 = var1.reader().readByte();
               var13 = var1.reader().readInt();
               int var23 = var1.reader().readInt();

               Vector var6;
               Avatar var7;
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
                     byte var24 = var1.reader().readByte();

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

               CasinoMsgHandler.curScr.setPlayers(var15, var16, var13, var23, var6);
               BoardScr.disableReady = false;
               int var28 = var6.size();

               for(int var29 = 0; var29 < var28; ++var29) {
                  var25 = (Avatar)var6.elementAt(var29);
                  if (var25.IDDB == var13) {
                     var25.isReady = true;
                  }

                  if (var25.IDDB == GameMidlet.avatar.IDDB) {
                     GameMidlet.avatar.setMoneyNew(var25.getMoneyNew());
                  }
               }

               Canvas.endDlg();
               CasinoMsgHandler.curScr.switchToMe();
               return;
            }
            case 9: {
               var2 = var1.reader().readInt();
               String var4 = var1.reader().readUTF();
               int var17 = var1.reader().readInt();
               Avatar var3 = BoardScr.getAvatarByID(var2);
               if (var3 != null) {
                  var3.setMoneyNew(var17);
                  BoardScr.showChat(var2, var4);
                  BoardScr.showFlyText(var2, var17);
               }
               return;
            }
            case 10: {
               var25 = new Avatar();
               var9 = var1.reader().readByte();
               var25.IDDB = var1.reader().readInt();
               var25.setName(var1.reader().readUTF());
               var25.setMoneyNew(var1.reader().readInt());
               var15 = var1.reader().readByte();

               for(int var26 = 0; var26 < var15; ++var26) {
                  var25.addSeri(new SeriPart(var1.reader().readShort()));
               }

               var25.direct = 0;
               var25.setExp(var1.reader().readInt());
               var25.idImg = var1.reader().readShort();
               var25.isReady = false;
               CasinoMsgHandler.curScr.setAt(var9, var25);
               return;
            }
            case 12: {
               var25 = new Avatar();
               var9 = var1.reader().readByte();
               var25.IDDB = var1.reader().readInt();
               var25.setName(var1.reader().readUTF());
               var25.setMoneyNew(var1.reader().readInt());
               var15 = var1.reader().readByte();

               for(int var27 = 0; var27 < var15; ++var27) {
                  var25.addSeri(new SeriPart(var1.reader().readShort()));
               }

               var25.direct = 0;
               var25.setExp(var1.reader().readInt());
               var25.idImg = var1.reader().readShort();
               var25.isReady = false;
               CasinoMsgHandler.curScr.me.playerLeave(var9);
               CasinoMsgHandler.curScr.setAt(var9, var25);
               return;
            }
            case 14: {
               var2 = var1.reader().readInt();
               var13 = var1.reader().readInt();
               if (BoardScr.isStartGame && BoardScr.numPlayer == 2) {
                  CasinoMsgHandler.curScr.closeBoard(T.opponentQuit);
               }

               BoardScr.me.playerLeave(var2);
               BoardScr.setOwner(var13);
               return;
            }
            case 16: {
               var2 = var1.reader().readInt();
               boolean var14 = var1.reader().readBoolean();
               if (var2 == GameMidlet.avatar.IDDB) {
                  Canvas.endDlg();
               }

               BoardScr.setReady(var2, var14);
               return;
            }
            case 19: {
               var15 = var1.reader().readByte();
               var16 = var1.reader().readByte();
               var13 = var1.reader().readInt();
               if (BoardScr.setR_B(var15, var16)) {
                  CasinoMsgHandler.curScr.setMoney(var13);
                  return;
               }
               break;
            }
            case 20: {
               byte roomID = var1.reader().readByte();
               byte boardID = var1.reader().readByte();
               byte interval = var1.reader().readByte();

               Vector hand = new Vector();
               for (int i = 0; i < 3; ++i) {
                  hand.addElement(new Card(var1.reader().readByte()));
               }

               int whoFirst = var1.reader().readInt();

               BoardScr.roomID = roomID;
               BoardScr.boardID = boardID;
               Canvas.endDlg();
               BoardScr.resetReady();
               BaLaBoardScr.gI().start(interval, hand, whoFirst);
               return;
            }
            case 21: {
               byte roomID = var1.reader().readByte();
               byte boardID = var1.reader().readByte();
               int playerIndex = var1.reader().readByte();

               int numCards = 0;
               int[] cards = null;
               if (var1.reader().available() > 0) {
                  numCards = var1.reader().readByte();
                  if (numCards > 0) {
                     cards = new int[numCards];
                     for (int i = 0; i < numCards; ++i) {
                        cards[i] = var1.reader().readByte();
                     }
                  }
               }

               BaLaBoardScr.gI().onMove(playerIndex, cards);
               return;
            }
            case 51: {
               byte roomID = var1.reader().readByte();
               byte boardID = var1.reader().readByte();

               int[] moneyPlayers = new int[4];
               for (int i = 0; i < 4; ++i) {
                  moneyPlayers[i] = var1.reader().readInt();
               }

               int[][] hands = new int[4][4];
               int[] userIds = new int[4];
               for (int i = 0; i < 4; ++i) {
                  userIds[i] = var1.reader().readInt();
                  byte card;
                  int j = 0;
                  while (j < 3 && (card = var1.reader().readByte()) != -1) {
                     hands[i][j] = card;
                     j++;
                  }
                  for (; j < 3; ++j) {
                     hands[i][j] = -1;
                  }
               }

               int winnerId = var1.reader().readInt();
               byte rankOrd = var1.reader().readByte();

               BoardScr.roomID = roomID;
               BoardScr.boardID = boardID;
               Canvas.endDlg();
               BaLaBoardScr.gI().onFinish(moneyPlayers, hands, userIds, winnerId, rankOrd);
               return;
            }
            case 60: {
               byte roomID = var1.reader().readByte();
               byte boardID = var1.reader().readByte();
               byte fromIndex = var1.reader().readByte();
               byte toIndex = var1.reader().readByte();
               int money = var1.reader().readInt();
               if (BoardScr.setR_B(roomID, boardID)) {
                  BaLaBoardScr.gI().addMoneyFlyEffect(fromIndex, toIndex, money);
               }
               return;
            }
            default:
               return;
         }
      } catch (Exception var25) {
         var25.printStackTrace();
      }
   }
}
