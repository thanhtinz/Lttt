package avt;

import java.io.IOException;
import main.Canvas;

public final class FarmService extends IService {
   private static FarmService instance;

   public static FarmService gI() {
      if (instance == null) {
         instance = new FarmService();
      }

      return instance;
   }

   public final void getBigImage(short var1) {
      this.createMessage((byte)54);
      this.writeShort(var1);
      this.sendMessage();
      Canvas.startWaitDlg(T.getFarmData);
   }

   public final void getImageData() {
      this.createMessage((byte)55);
      this.sendMessage();
      Canvas.startWaitDlg(T.getFarmData);
   }

   public final void getTreeInfo() {
      this.createMessage((byte)56);
      this.sendMessage();
      Canvas.startWaitDlg(T.getFarmData);
   }

   public final void getInventory() {
      this.createMessage((byte)60);
      this.sendMessage();
   }

   public final void doJoinFarm(int var1) {
      this.createMessage((byte)61);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doBuyItem(short var1, byte var2, int var3) {
      this.createMessage((byte)62);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
      Canvas.endDlg();
   }

   public final void doSellItem(short var1) {
      this.createMessage((byte)63);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void doPlantSeed(int var1, int var2, int var3) {
      this.createMessage((byte)64);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void doUsingItem(int var1, int var2, int var3) {
      System.out.println("doUsingItem: " + var2 + "    " + var3);
      this.createMessage((byte)65);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void doHervest(int var1, int var2) {
      System.out.println("doHervest: " + var1 + "   " + var2);
      this.createMessage((byte)66);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
      } catch (IOException var4) {
      }

      this.sendMessage();
   }

   public final void doOpenLand(int var1, int var2) {
      this.createMessage((byte)70);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doRequestPricePlant(int var1) {
      this.createMessage((byte)69);
      this.writeInt(var1);
      this.sendMessage();
   }

   public final void doHarvestAnimal(int var1, int var2) {
      this.createMessage((byte)74);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doSellAnimal(int var1, byte var2) {
      this.createMessage((byte)73);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doBuyAnimal(AnimalInfo var1, int var2) {
      Canvas.endDlg();
      this.createMessage((byte)71);

      try {
         super.m.writer().writeByte(var1.species);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doRequestPriceAnimal(int var1, int var2) {
      this.createMessage((byte)72);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doTransMoney(int var1, int var2) {
      this.createMessage((byte)75);

      try {
         super.m.writer().writeInt(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doUpdateFarm(int var1, int var2) {
      this.createMessage((byte)80);
      this.writeByte(var1);
      if (var1 == 1) {
         this.writeByte(var2);
      }

      this.sendMessage();
   }

   public final void doUpdateFish(int var1, int var2) {
      this.createMessage((byte)81);
      this.writeByte(var1);
      if (var1 == 1) {
         this.writeByte(var2);
      }

      this.sendMessage();
   }

   public final void doGetImgIcon(short var1) {
      this.createMessage((byte)82);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void doUpdateStarFruil(int var1) {
      this.createMessage((byte)84);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doUpdateStarFruitByMoney(int var1) {
      this.createMessage((byte)86);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doUpdateLand(int var1, int var2) {
      this.createMessage((byte)90);
      this.writeByte(var1);
      if (var1 == 1) {
         this.writeByte(var2);
      }

      this.sendMessage();
   }

   public final void doUpdateStore(int var1, int var2) {
      this.createMessage((byte)94);
      this.writeByte(var1);
      if (var1 == 1) {
         this.writeByte(var2);
      }

      this.sendMessage();
   }

   public final void doCooking(short var1) {
      Canvas.startWaitDlg();
      this.createMessage((byte)91);
      this.writeShort(var1);
      this.sendMessage();
   }

   public final void nauNhanh(int var1) {
      this.createMessage((byte)93);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void doSteal(int var1) {
      this.createMessage((byte)96);
      this.writeByte(0);
      this.sendMessage();
   }
}
