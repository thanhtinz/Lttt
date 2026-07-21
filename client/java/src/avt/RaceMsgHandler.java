// Decompiled with: Procyon 0.6.0
// Class Version: 8
package avt;

import main.Canvas;

public final class RaceMsgHandler implements IMiniGameMsgHandler
{
   public static RaceMsgHandler instance;

   @Override
   public final void onMessage(final Message var1) {
      try {
         switch (var1.command) {
            case 1: {
               if (var1.reader().readByte() == 0) {
                  final PetRace[] var2 = new PetRace[6];
                  for (int var3 = 0; var3 < 6; ++var3) {
                     var2[var3] = new PetRace(this);
                     var2[var3].money = 0;
                     var2[var3].IDDB = var1.reader().readByte();
                     var2[var3].rate = var1.reader().readByte();
                     var2[var3].idImg = var1.reader().readShort();
                     var2[var3].idIcon = var1.reader().readShort();
                  }
                  final short var4 = var1.reader().readShort();
                  RaceScr.gI().doOpenRace(var2, var4, false, true);
                  return;
               }
               if (!var1.reader().readBoolean()) {
                  final PetRace[] var5 = new PetRace[6];
                  for (int var6 = 0; var6 < 6; ++var6) {
                     var5[var6] = new PetRace(this);
                     var5[var6].money = 0;
                     var5[var6].IDDB = var1.reader().readByte();
                     var5[var6].idImg = var1.reader().readShort();
                     final byte var7 = var1.reader().readByte();
                     var5[var6].numTick = new short[var7];
                     var5[var6].vTick = new short[var7];
                     for (int var8 = 0; var8 < var7; ++var8) {
                        var5[var6].numTick[var8] = var1.reader().readShort();
                        var5[var6].vTick[var8] = var1.reader().readShort();
                     }
                  }
                  final short var9 = var1.reader().readShort();
                  RaceScr.gI().timeStart = var1.reader().readShort();
                  RaceScr.gI().curTimeStart = System.currentTimeMillis();
                  RaceScr.gI().doOpenRace(var5, var9, false, false);
                  return;
               }
               for (int var3 = 0; var3 < 6; ++var3) {
                  final int var6 = var1.reader().readByte();
                  RaceScr.gI().listPet[var3].numTick = new short[var6];
                  RaceScr.gI().listPet[var3].vTick = new short[var6];
                  for (int var10 = 0; var10 < var6; ++var10) {
                     RaceScr.gI().listPet[var3].numTick[var10] = var1.reader().readShort();
                     RaceScr.gI().listPet[var3].vTick[var10] = var1.reader().readShort();
                     RaceScr.gI();
                  }
               }
               final short var4 = var1.reader().readShort();
               RaceScr.gI().timeStart = var1.reader().readShort();
               RaceScr.gI().curTimeStart = System.currentTimeMillis();
               RaceScr.gI().doOpenRace(null, var4, true, false);
               return;
            }
            case 2: {
               final short var11 = var1.reader().readShort();
               final String var12 = var1.reader().readUTF();
               final int var8 = var1.reader().readShort();
               final short var4 = var1.reader().readByte();
               final int var6 = var1.reader().readByte();
               final byte var13 = var1.reader().readByte();
               RaceScr.gI().onPetInfo(var11, var12, (short)var8, (byte)var4, (byte)var6, var13);
               return;
            }
            case 5: {
               final int var10 = var1.reader().readByte();
               final int var14 = var1.reader().readInt();
               for (int var3 = 0; var3 < RaceScr.gI().listPet.length; ++var3) {
                  if (var10 == RaceScr.gI().listPet[var3].IDDB) {
                     RaceScr.gI().listPet[var3].money = var14;
                     RaceScr.gI().indexFocus = (byte)var3;
                     break;
                  }
               }
               Canvas.endDlg();
               return;
            }
            case 8: {
               final int var10;
               final short[] var15 = new short[var10 = var1.reader().readByte()];
               final String[] var16 = new String[var10];
               for (int var17 = 0; var17 < var10; ++var17) {
                  var15[var17] = var1.reader().readShort();
                  var16[var17] = var1.reader().readUTF();
               }
               if (var10 > 0) {
                  Canvas.currentDialog = new HistoryPopup(this, var15, var16);
                  return;
               }
               Canvas.endDlg();
               return;
            }
            case 9: {
               final String var18 = var1.reader().readUTF();
               RaceScr.gI().onChat(var18);
               return;
            }
            case 10: {
               RaceScr.gI().diaWin = new dialogWin();
               RaceScr.gI().diaWin.b = var1.reader().readByte();
               RaceScr.gI().diaWin.name = var1.reader().readUTF();
               RaceScr.gI();
               var1.reader().readByte();
               RaceScr.gI().diaWin.tienCuoc = var1.reader().readInt();
               RaceScr.gI().diaWin.tienAn = var1.reader().readInt();
               RaceScr.gI().diaWin.tienThue = var1.reader().readInt();
               RaceScr.gI().diaWin.tienNhanDuoc = var1.reader().readInt();
               break;
            }
         }
      }
      catch (final Exception var19) {
         var19.printStackTrace();
      }
   }
}
