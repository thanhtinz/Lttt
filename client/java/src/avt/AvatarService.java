package avt;

import java.io.IOException;
import main.Canvas;
import main.GameMidlet;

public final class AvatarService extends IService {
   private static AvatarService instance;

   public static AvatarService gI() {
      if (instance == null) {
         instance = new AvatarService();
      }

      return instance;
   }

   public final void getBigData() {
      this.createMessage((byte)-11);
      this.writeInt(LoginScr.s);
      this.sendMessage();
   }

   public final void getBigImage(short var1) {
      this.createMessage((byte)-14);
      this.writeShort(var1);
      this.sendMessage();
      Canvas.startWaitDlg(T.enemyFirstFire);
   }

   public final void getImageData() {
      this.createMessage((byte)-15);
      this.sendMessage();
      Canvas.startWaitDlg(T.enemyFirstFire);
   }

   public final void getAvatarPart() {
      this.createMessage((byte)-16);
      this.sendMessage();
      Canvas.startWaitDlg(T.enemyFirstFire);
   }

   public final void getItemInfo() {
      this.createMessage((byte)-37);
      this.sendMessage();
      Canvas.startWaitDlg(T.enemyFirstFire);
   }

   public final void getMapItemType() {
      this.createMessage((byte)-40);
      this.sendMessage();
   }

   public final void getMapItem() {
      this.createMessage((byte)-41);
      this.sendMessage();
   }

   public final void doFeel(int var1) {
      if (GameMidlet.CLIENT_TYPE == 9 || GameMidlet.CLIENT_TYPE == 11) {
         this.createMessage((byte)57);
         this.writeByte(var1);
         this.sendMessage();
      }

   }

   public final void doBuyItem(int var1, int var2) {
      System.out.println("DEBUG BUYITEM: AvatarService.doBuyItem(id=" + var1 + ", moneyType=" + var2 + ") packet -24");
      this.createMessage((byte)-24);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doCreateHome(short[] var1, int var2) {
      this.createMessage((byte)-46);

      try {
         super.m.writer().writeShort(var2);
         super.m.writer().writeShort(var1.length);

         for(var2 = 0; var2 < var1.length; ++var2) {
            super.m.writer().writeByte(var1[var2]);
         }

         super.m.writer().writeShort(0);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doBuyItemHouse(MapItem var1) {
      System.out.println("doBuyItemHouse; " + var1.typeID);
      this.createMessage((byte)-74);

      try {
         super.m.writer().writeShort(var1.typeID);
         super.m.writer().writeByte(var1.x / 24);
         super.m.writer().writeByte(var1.y / 24);
         super.m.writer().writeByte(var1.type);
      } catch (IOException var3) {
      }

      this.sendMessage();
   }

   public final void doJoinHouse(int var1) {
      Canvas.startWaitDlg();
      this.createMessage((byte)-65);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void dodelItem(MapItem var1) {
      this.createMessage((byte)-66);

      try {
         super.m.writer().writeShort(var1.typeID);
         super.m.writer().writeByte(var1.x / 24);
         super.m.writer().writeByte(var1.y / 24);
         super.m.writer().writeByte(var1.dir);
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void getTypeHouse(int var1) {
      this.createMessage((byte)-67);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doKickOutHome(int var1) {
      this.createMessage((byte)-69);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doRequestExpicePet(int var1) {
      this.createMessage((byte)-70);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doSortItem(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.createMessage((byte)-71);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeByte(var5);
         super.m.writer().writeByte(var6);
      } catch (IOException var8) {
      }

      this.sendMessage();
   }

   public final void doSetPassMyHouse(String var1, int var2, int var3) {
      this.createMessage((byte)-75);

      try {
         super.m.writer().writeByte(var3);
         super.m.writer().writeUTF(var1);
         if (var3 == 1) {
            super.m.writer().writeInt(var2);
         }
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void doGetImgIcon(short var1) {
      this.createMessage((byte)-80);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void doRequestEffectData(short var1) {
      this.createMessage((byte)-84);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doJoinHouse4(int var1) {
      this.createMessage((byte)-104);
      this.writeInt(var1);
      this.sendMessage();
   }
}
