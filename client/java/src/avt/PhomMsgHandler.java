package avt;

import java.util.Vector;
import main.Canvas;

public final class PhomMsgHandler extends IService implements IMiniGameMsgHandler {
   private static PhomMsgHandler instance = new PhomMsgHandler();

   public static void onHandler() {
      BoardScr.numPlayer = 4;
      BoardListOnScr.type = BoardListOnScr.STYLE_4PLAYER;
      RoomListOnScr.setName(1, PBoardScr.gI());
      CasinoMsgHandler.me.miniGameMessageHandler = instance;
   }

   public final void onMessage(Message var1) {
      try {
         int var2 = var1.reader().readByte();
         int var3 = var1.reader().readByte();
         if (BoardScr.setR_B((byte)var2, (byte)var3)) {
            int var4;
            int var14;
            byte var15;
            boolean var17;
            byte var19;
            int var20;
            byte var27;
            switch (var1.command) {
               case 20:
                  var15 = var1.reader().readByte();
                  Vector var23 = new Vector();

                  for(var4 = 0; var4 < 9; ++var4) {
                     var27 = var1.reader().readByte();
                     var23.addElement(new Card(var27));
                  }

                  var4 = var1.reader().readInt();
                  var20 = var1.reader().readInt();
                  Canvas.endDlg();
                  BoardScr.resetReady();
                  PBoardScr.gI().start(var15, var23, var4, var20);
                  return;
               case 21:
                  var2 = var1.reader().readInt();
                  var3 = var1.reader().readInt();
                  var19 = var1.reader().readByte();
                  var27 = 0;
                  if (var19 != -1) {
                     var27 = var1.reader().readByte();
                  }

                  Canvas.endDlg();
                  PBoardScr.gI().onFire(var2, var3, var19, var27);
                  return;
               case 49:
                  var2 = var1.reader().readInt();
                  var14 = var1.reader().readInt();
                  PBoardScr.gI().onSkipPlayer(var2, var14);
                  return;
               case 51:
                  int[] var29 = new int[4];

                  for(var2 = 0; var2 < 4; ++var2) {
                     var29[var2] = var1.reader().readInt();
                  }

                  int[] var21 = new int[4];

                  for(var3 = 0; var3 < 4; ++var3) {
                     var21[var3] = var1.reader().readInt();
                  }

                  int[][] var22 = new int[4][11];

                  for(var4 = 0; var4 < 4; ++var4) {
                     for(var20 = 0; var20 < 11; ++var20) {
                        var22[var4][var20] = -1;
                     }
                  }

                  var4 = 0;
                  var20 = 0;

                  while(var1.reader().available() > 0) {
                     byte var26;
                     if ((var26 = var1.reader().readByte()) == -1) {
                        if (var4 < 3) {
                           ++var4;
                        }

                        var20 = 0;
                     } else {
                        var22[var4][var20] = var26;
                        if (var20 < 10) {
                           ++var20;
                        }
                     }
                  }

                  Canvas.endDlg();
                  PBoardScr.gI().onFinish(var21, var22);
                  return;
               case 62:
                  var19 = var1.reader().readByte();
                  var2 = var1.reader().readInt();
                  var3 = var1.reader().readInt();
                  var20 = var1.reader().readInt();
                  int[][] var25 = new int[4][4];
                  int[][] var28 = new int[4][3];

                  int var8;
                  int var9;
                  for(var8 = 0; var8 < 4; ++var8) {
                     for(var9 = 0; var9 < 4; ++var9) {
                        var25[var8][var9] = -1;
                        if (var9 < 3) {
                           var28[var8][var9] = -1;
                        }
                     }
                  }

                  var8 = 0;
                  var9 = 0;

                  byte var12;
                  for(int var10 = 0; var10 < 4; ++var10) {
                     for(int var11 = 0; var11 < 3 && (var12 = var1.reader().readByte()) != -2 && var12 != -1; ++var11) {
                        var28[var10][var11] = var12;
                     }
                  }

                  while(true) {
                     while(var1.reader().available() > 0) {
                        byte var30 = var1.reader().readByte();
                        if (var8 < 3 && var30 == -1) {
                           ++var8;
                           var9 = 0;
                        } else {
                           var25[var8][var9] = var30;
                           if (var9 < 3) {
                              ++var9;
                           }
                        }
                     }

                     PBoardScr.gI().onPlaying(var19, var2, var3, var25, var28, var20);
                     return;
                  }
               case 63:
                  var14 = var1.reader().readByte();
                  Canvas.endDlg();
                  PBoardScr.gI().onGetCard(var14);
                  return;
               case 64:
                  var2 = var1.reader().readInt();
                  var17 = var1.reader().readBoolean();
                  var19 = 0;
                  if (var17) {
                     var19 = var1.reader().readByte();
                  }

                  var14 = var1.reader().readByte();
                  Canvas.endDlg();
                  PBoardScr.gI().onEatCard(var17, var19, var2, (byte)var14);
                  return;
               case 65:
                  var2 = var1.reader().readInt();
                  var17 = var1.reader().readBoolean();
                  boolean var18 = var1.reader().readBoolean();
                  int[] var5 = new int[4];
                  int[] var24 = new int[12];
                  if (var17) {
                     int var7;
                     for(var7 = 0; var7 < 4; ++var7) {
                        var5[var7] = var1.reader().readInt();
                     }

                     for(var7 = 0; var7 < 12; ++var7) {
                        var24[var7] = -1;
                     }

                     for(var7 = 0; var1.reader().available() > 0; ++var7) {
                        var24[var7] = var1.reader().readByte();
                     }
                  }

                  Canvas.endDlg();
                  PBoardScr.gI().onHaPhom(var17, var24, var18, var2);
                  return;
               case 67:
                  boolean var6 = var1.reader().readBoolean();
                  var15 = -1;
                  if (var6) {
                     var15 = var1.reader().readByte();
                  }

                  Canvas.endDlg();
                  PBoardScr.gI().onAddCardToPhom(var6, var15);
                  return;
               case 68:
                  var14 = var1.reader().readByte();
                  Canvas.endDlg();
                  PBoardScr.gI().onResetPhomEat((byte)var14);
                  return;
               case 69:
                  var2 = var1.reader().readInt();
                  int[] var16 = new int[4];

                  for(var4 = 0; var4 < 4; ++var4) {
                     var16[var4] = var1.reader().readInt();
                  }

                  PBoardScr.gI().onDenBai(var2);
                  return;
               case 70:
                  var1.reader().readInt();
                  PBoardScr.gI().onOnceWin();
            }
         }
      } catch (Exception var25) {
         var25.printStackTrace();
      }

   }
}
