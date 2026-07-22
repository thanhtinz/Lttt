package avt;

import java.util.Vector;
import main.Canvas;

public final class DiamondMessageHandler extends IService implements IMiniGameMsgHandler {
   private static DiamondMessageHandler instance = new DiamondMessageHandler();

   public static void init() {
      BoardScr.numPlayer = 2;
      BoardListOnScr.type = 0;
      RoomListOnScr.setName(2, DiamondScr.gI());
      CasinoMsgHandler.me.miniGameMessageHandler = instance;
   }

   public final void onMessage(Message var1) {
      try {
         int var2 = var1.reader().readByte();
         int var3 = var1.reader().readByte();
         if (BoardScr.setR_B((byte)var2, (byte)var3)) {
            Vector var4;
            byte var11;
            byte var12;
            int var18;
            int var21;
            int var14;
            String var19;
            switch (var1.command) {
               case 20:
                  var12 = var1.reader().readByte();
                  var3 = var1.reader().readInt();
                  byte[][] var17 = new byte[8][8];

                  for(var18 = 0; var18 < 8; ++var18) {
                     for(var21 = 0; var21 < 8; ++var21) {
                        var17[var18][var21] = var1.reader().readByte();
                     }
                  }

                  for(var18 = 0; var18 < 2; ++var18) {
                     Avatar var20;
                     (var20 = BoardScr.getAvatarByID(var1.reader().readInt())).an = var1.reader().readShort();
                     var20.plusHP = var20.plusMP = 0;
                     var20.hp = var20.maxHP = var1.reader().readShort();
                     var20.mp = var1.reader().readShort();
                     var20.maxMP = var1.reader().readShort();
                     var20.v <<= 1;
                     var20.setFeel(4);
                  }

                  DiamondScr.gI().start(var3, var12, var17);
                  return;
               case 21:
                  var18 = var1.reader().readInt();
                  var21 = var1.reader().readByte();
                  var11 = var1.reader().readByte();
                  DiamondScr.gI().move(var18, var21, var11);
                  return;
               case 24:
                  var2 = var1.reader().readInt();
                  byte[][] var15 = new byte[8][8];

                  for(var14 = 0; var14 < 8; ++var14) {
                     for(var18 = 0; var18 < 8; ++var18) {
                        var15[var14][var18] = var1.reader().readByte();
                     }
                  }

                  DiamondScr.gI().onOutPath(var2, var15);
                  return;
               case 49:
                  var14 = var1.reader().readInt();
                  DiamondScr.gI().onSkip(var14);
                  return;
               case 51:
                  var4 = new Vector();

                  for(var18 = 0; var18 < 2; ++var18) {
                     var2 = var1.reader().readInt();
                     var3 = var1.reader().readInt();
                     Avatar var16;
                     Avatar var10000 = var16 = BoardScr.getAvatarByID(var2);
                     var10000.v /= 2;
                     var16.action = 0;
                     var16.setMoneyNew(var16.getMoneyNew() + var3);
                     if (var3 != 0) {
                        Canvas.addFlyText(var3, var16.x, var16.y, -1, 30);
                        var19 = var16.name + ": ";
                        if (var3 > 0) {
                           DiamondScr.gI().idWin = var16.IDDB;
                           var19 = var19 + T.win + "   +" + var3 + T.xu;
                        } else {
                           var19 = var19 + T.lose + "  " + var3 + T.xu;
                        }

                        var4.addElement("  ");
                        var4.addElement(var19);
                     }
                  }

                  DiamondScr.gI().onFinish(var4);
                  return;
               case 64:
                  byte[] var6 = new byte[var18 = var1.reader().readByte()];
                  AvPosition[] var7 = new AvPosition[var18];

                  for(var2 = 0; var2 < var18; ++var2) {
                     var7[var2] = new AvPosition();
                     var6[var2] = var1.reader().readByte();
                     var7[var2].anchor = var1.reader().readByte();
                     var7[var2].depth = (short)var1.reader().readByte();
                  }

                  var12 = var1.reader().readByte();
                  var11 = var1.reader().readByte();
                  var4 = new Vector();

                  for(var18 = 0; var18 < var11; ++var18) {
                     var19 = var1.reader().readUTF();
                     var4.addElement(var19);
                  }

                  for(var18 = 0; var18 < 2; ++var18) {
                     Avatar var13;
                     (var13 = BoardScr.getAvatarByID(var1.reader().readInt())).fight = var1.reader().readByte();
                     var13.an = var1.reader().readShort();
                     var13.plusHP = (short)(var1.reader().readShort() - var13.hp);
                     var13.plusMP = (short)(var1.reader().readShort() - var13.mp);
                     var13.isNo = var1.reader().readBoolean();
                     if (var13.isNo) {
                        DiamondScr.gI().c = true;
                     }
                  }

                  DiamondScr.gI().paintGame(var6, var7, var12, var4);
                  return;
               case 71:
                  byte[][] var5 = new byte[8][];

                  for(var2 = 0; var2 < 8; ++var2) {
                     var5[var2] = new byte[8];
                  }

                  for(var2 = 0; var2 < 8; ++var2) {
                     for(var3 = 0; var3 < 8; ++var3) {
                        var5[var2][var3] = var1.reader().readByte();
                     }
                  }

                  DiamondScr.gI().onData(var5);
                  return;
            }
         }
      } catch (Exception var15) {
         var15.printStackTrace();
      }

   }
}
