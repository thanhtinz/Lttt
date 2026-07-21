package avt;

public final class RoomInfo {
   public byte id;
   public byte roomFree;
   public byte lv;

   public RoomInfo(byte var1, byte var2, byte var3, byte var4) {
      this.id = var1;
      this.roomFree = 0;
      this.lv = var4;
   }

   public RoomInfo() {
   }
}
