package avt;

public final class AnimalInfo {
   public byte species;
   public byte frame;
   public byte area;
   public int harvestTime;
   public int[] price = new int[2];
   public short priceProduct;
   public short iconID;
   public short iconProduct = -1;
   public short iconO = -1;
   public short[] idImg = new short[3];
   public byte[][] arrFrame = new byte[3][12];
   public String name;
   public String des;
}
