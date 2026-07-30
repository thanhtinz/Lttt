package avt;

import java.io.IOException;
import javax.microedition.rms.RecordStore;
import main.Canvas;
import main.GameMidlet;

public final class GlobalService extends IService {
   private static GlobalService instance;

   public static GlobalService gI() {
      if (instance == null) {
         instance = new GlobalService();
      }

      return instance;
   }

   public final void requestService(byte var1, String var2) {
      if (var2 == null) {
         var2 = "";
      }

      this.createMessage((byte)-55);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      this.sendMessage();
   }

   public final void setProviderAndClientType() {
      this.createMessage((byte)-1);
      this.writeByte(GameMidlet.CLIENT_TYPE);
      this.sendMessage();
      this.createMessage((byte)-17);

      try {
         super.m.writer().writeByte(GameMidlet.PROVIDER);
         Runtime var1 = Runtime.getRuntime();
         super.m.writer().writeInt((int)(var1.totalMemory() / 1024L));
         String var4;
         if ((var4 = System.getProperty("microedition.platform")) == null) {
            var4 = "null";
         }

         super.m.writer().writeUTF(var4);
         super.m.writer().writeInt(getRmsSize());
         super.m.writer().writeInt(Canvas.w);
         super.m.writer().writeInt(Canvas.h);
         super.m.writer().writeBoolean(Canvas.isKeyBoard);
         super.m.writer().writeByte(AvMain.hd - 1);
         super.m.writer().writeUTF(GameMidlet.APP_VERSION);
         System.out.println("setProviderAndClientType: " + PopupShop.i + "    " + MiniMap.i + "    " + MapScr.j);
         super.m.writer().writeUTF(PopupShop.i);
         super.m.writer().writeUTF(MiniMap.i);
         super.m.writer().writeUTF(MapScr.j);
      } catch (IOException var4) {
      }

      this.sendMessage();
      this.createMessage((byte)-79);

      try {
         super.m.writer().writeUTF(GameMidlet.g);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      this.sendMessage();
   }

   private static int getRmsSize() {
      long var0 = 0L;
      RecordStore var2 = null;

      try {
         var0 = (long)((var2 = RecordStore.openRecordStore("textrms", true)).getSizeAvailable() + var2.getSize());
      } catch (Exception var12) {
      } finally {
         try {
            if (var2 != null) {
               var2.closeRecordStore();
            }

            RecordStore.deleteRecordStore("textrms");
         } catch (Exception var11) {
         }

      }

      if (var0 > 0L) {
         var0 /= 1024L;
      }

      return (int)var0;
   }

   public final void requestInfoOf(int var1) {
      this.createMessage((byte)34);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void login(String var1, String var2, String var3) {
      this.createMessage((byte)-2);

      try {
         System.out.println("loginaaaaaaaaaaaaaa: " + var1 + "    " + var2);
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
         super.m.writer().writeUTF(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void setGameType(int var1) {
      this.createMessage((byte)61);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void chatTo(int var1, String var2) {
      this.createMessage((byte)-6);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeUTF(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doRequestCreCharacter() {
      this.createMessage((byte)-35);

      try {
         super.m.writer().writeByte(GameMidlet.avatar.gender);
         int var1 = GameMidlet.avatar.seriPart.size();
         super.m.writer().writeByte(var1);

         for(int var2 = 0; var2 < var1; ++var2) {
            SeriPart var3 = (SeriPart)GameMidlet.avatar.seriPart.elementAt(var2);
            super.m.writer().writeShort(var3.idPart);
         }
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doRemoveItem(int var1, int var2) {
      this.createMessage((byte)-36);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doRequestContainer(int var1) {
      this.createMessage((byte)-47);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doUsingItem(short var1, byte var2) {
      System.out.println("DEBUG USEITEM: GlobalService.doUsingItem(id=" + var1 + ", type=" + var2 + ") packet -48");
      this.createMessage((byte)-48);
      super.m = new Message((byte)-48);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void getHandler(int var1) {
      this.createMessage((byte)-1);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doRequestSoundData(byte var1) {
      this.createMessage((byte)-51);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void requestShop(int var1) {
      this.createMessage((byte)-49);
      System.out.println("requestShop: " + var1);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doRequestNumSupport(int var1) {
      this.createMessage((byte)-52);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doUpdateContainer(int var1) {
      this.createMessage((byte)-53);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doMenuOption(int var1, byte var2, int var3) {
      if (var1 >= 2000000000) {
         System.out.println("DEBUG NPC_OPTION(-59): npcId=" + var1 + " menuByte=" + var2 + " optionIdx=" + var3);
      } else {
         System.out.println("DEBUG MENUOPTION(-59): int=" + var1 + " byte=" + var2 + " idx=" + var3);
      }
      this.createMessage((byte)-59);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void menuOption(int var1, byte var2, byte var3) {
      this.doMenuOption(var1, var2, var3);
   }

   public final void doTextBox(int var1, byte var2, String var3) {
      this.createMessage((byte)-60);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeUTF(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void doCommunicate(int var1) {
      System.out.println("doCommunicate: " + var1);
      this.createMessage((byte)-61);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doLoadCard(String var1, String var2, String var3) {
      this.createMessage((byte)-56);

      try {
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
         super.m.writer().writeUTF(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void doChangePass(String var1, String var2) {
      this.createMessage((byte)-62);

      try {
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doDialLucky(short var1, int var2) {
      this.createMessage((byte)-64);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doServerKick(int var1, String var2) {
      this.createMessage((byte)-72);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doListCustom(int var1, byte var2, int var3, byte var4) {
      this.createMessage((byte)-81);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
      } catch (Exception var6) {
      }

      this.sendMessage();
   }

   public final void doRequestCmdRotate(int var1, int var2) {
      System.out.println("doRequestCmdRotate: " + var1);
      this.createMessage((byte)-83);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeInt(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doCustomTab(byte var1) {
      this.createMessage((byte)-58);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doEnterPass(String var1, byte var2) {
      this.createMessage((byte)-88);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeUTF(var1);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doChangeChestPass(String var1, String var2) {
      this.createMessage((byte)-88);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doUpdateChest(int var1) {
      this.createMessage((byte)-90);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doTransChestPart(int var1, int var2, short var3) {
      this.createMessage((byte)-89);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeShort(var3);
      } catch (Exception var5) {
      }

      this.sendMessage();
   }

   public final void requestCityMap(byte var1) {
      this.createMessage((byte)-92);
      if (var1 != -1) {
         this.writeByte(var1);
      }

      this.sendMessage();
   }

   public final void requestTileMap(byte var1) {
      this.createMessage((byte)-94);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void requestJoinAny(short var1) {
      this.createMessage((byte)-95);

      try {
         super.m.writer().writeByte(MapScr.A);
         super.m.writer().writeByte(0);
         super.m.writer().writeShort(var1);
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void requestPartDynaMic(short var1) {
      this.createMessage((byte)-97);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void requestImagePart(short var1) {
      this.createMessage((byte)-98);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void doJoinOfflineMap(int var1) {
      Canvas.startWaitDlg();
      this.createMessage((byte)-99);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doDatCuoc(int var1, int var2) {
      Canvas.startWaitDlg();
      this.createMessage((byte)5);
      this.writeByte(var1);
      this.writeInt(var2);
      this.sendMessage();
   }

   public final void doPetInfo(int var1) {
      this.createMessage((byte)2);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void transXeng(int var1) {
      this.createMessage((byte)-102);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doFlowerLoveSelected(int var1) {
      this.createMessage((byte)-106);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doSendOpenShopHouse(byte var1, short var2) {
      this.createMessage((byte)-107);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         this.sendMessage();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public final void doRegisterByEmail(String var1, String var2, String var3) {
      System.out.println("doRegisterByEmail: " + var1 + "   " + var2 + "   " + var3);
      this.createMessage((byte)-25);

      try {
         this.writeUTF(var1);
         this.writeUTF(var2);
         this.writeUTF(var3);
         this.writeByte(0);
         this.sendMessage();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
