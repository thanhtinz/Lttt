package avt;

import java.io.IOException;
import main.Canvas;
import main.GameMidlet;

public final class ParkService extends IService {
   private static ParkService instance;

   public static ParkService gI() {
      if (instance == null) {
         instance = new ParkService();
      }

      return instance;
   }

   public final void doJoinPark(int var1, int var2) {
      System.out.println("doJoinPark: " + var1 + "   " + var2);
      if (OnScreen.c == 0) {
         Canvas.startWaitDlg();
      }

      this.createMessage((byte)50);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(LoadMap.xDichChuyen_);
         super.m.writer().writeShort(LoadMap.C);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.sendMessage();
   }

   public final void doMove(int var1, int var2, int var3, int var4) {
      this.createMessage((byte)54);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeShort(var4);
      } catch (IOException var6) {
      }

      this.sendMessage();
   }

   public final void chatToBoard(String var1) {
      if (GameMidlet.CLIENT_TYPE == 10) {
         this.createMessage((byte)77);
      } else {
         this.createMessage((byte)55);
      }

      this.writeUTF(var1);
      this.sendMessage();
   }

   public final void doRequestAddFriend(int var1) {
      this.createMessage((byte)-21);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doAddFriend(int var1, boolean var2) {
      this.createMessage((byte)-19);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeBoolean(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doGiftGiving(int var1, int var2, int var3) {
      System.out.println("doGiftGiving: " + var1 + "    " + GameMidlet.avatar.IDDB);
      this.createMessage((byte)58);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void doGivingDeferrent(int var1, int var2) {
      this.createMessage((byte)59);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeShort(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doRequestYourInfo(int var1) {
      System.out.println("doRequestYourInfo: " + var1);
      this.createMessage((byte)-22);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doRequestBoardList(byte var1) {
      this.createMessage((byte)60);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doBuyItem(short var1) {
      System.out.println("DEBUG BUYITEM: ParkService.doBuyItem(id=" + var1 + ") packet -38");
      this.createMessage((byte)-38);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void doFinishFishing(boolean var1, byte[] var2) {
      this.createMessage((byte)84);

      try {
         super.m.writer().writeBoolean(true);
         super.m.writer().writeByte(var2.length);

         for(int var4 = 0; var4 < var2.length; ++var4) {
            super.m.writer().writeByte(var2[var4]);
         }
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doCauCaXong() {
      this.createMessage((byte)85);
      this.sendMessage();
   }

   public final void doInviteToMyHome(int var1, int var2) {
      this.createMessage((byte)-68);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeInt(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doCustomPopup(int var1, int var2, int var3) {
      this.createMessage((byte)-77);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var5) {
      }

      this.sendMessage();
   }

   public final void doBossShop(int var1, int var2, int var3) {
      System.out.println("DEBUG BOSS_SHOP(-78): idBoss=" + var1 + " shopByte=" + var2 + " optionIdx=" + var3);
      this.createMessage((byte)-78);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
      } catch (Exception var5) {
      }

      this.sendMessage();
   }

   public final void doGetDropPart(int var1) {
      this.createMessage((byte)89);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeInt(var1);
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void doRequestWedding(int var1, int var2) {
      System.out.println("doRequestWedding");
      this.createMessage((byte)93);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      this.sendMessage();
   }
}
