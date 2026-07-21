package avt;

import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import main.GameMidlet;

public final class CRes {
   public static Random random = new Random();
   private static short[] sinn = new short[]{0, 18, 36, 54, 71, 89, 107, 125, 143, 160, 178, 195, 213, 230, 248, 265, 282, 299, 316, 333, 350, 367, 384, 400, 416, 433, 449, 465, 481, 496, 512, 527, 543, 558, 573, 587, 602, 616, 630, 644, 658, 672, 685, 698, 711, 724, 737, 749, 761, 773, 784, 796, 807, 818, 828, 839, 849, 859, 868, 878, 887, 896, 904, 912, 920, 928, 935, 943, 949, 956, 962, 968, 974, 979, 984, 989, 994, 998, 1002, 1005, 1008, 1011, 1014, 1016, 1018, 1020, 1022, 1023, 1023, 1024, 1024};
   private static short[] coss;
   private static int[] tann;
   public static String b;

   public static void init() {
      coss = new short[91];
      tann = new int[91];

      for(int var0 = 0; var0 <= 90; ++var0) {
         coss[var0] = sinn[90 - var0];
         if (coss[var0] == 0) {
            tann[var0] = Integer.MAX_VALUE;
         } else {
            tann[var0] = (sinn[var0] << 10) / coss[var0];
         }
      }

   }

   public static final int sin(int var0) {
      if ((var0 = fixangle(var0)) >= 0 && var0 < 90) {
         return sinn[var0];
      } else if (var0 >= 90 && var0 < 180) {
         return sinn[180 - var0];
      } else {
         return var0 >= 180 && var0 < 270 ? -sinn[var0 - 180] : -sinn[360 - var0];
      }
   }

   public static final int cos(int var0) {
      if ((var0 = fixangle(var0)) >= 0 && var0 < 90) {
         return coss[var0];
      } else if (var0 >= 90 && var0 < 180) {
         return -coss[180 - var0];
      } else {
         return var0 >= 180 && var0 < 270 ? -coss[var0 - 180] : coss[360 - var0];
      }
   }

   public static final int tan(int var0, int var1) {
      int var10000;
      if (var0 == 0) {
         var10000 = var1 > 0 ? 90 : 270;
      } else {
         int var2 = Math.abs((var1 << 10) / var0);
         int var3 = 0;

         while(true) {
            if (var3 <= 90) {
               if (tann[var3] < var2) {
                  ++var3;
                  continue;
               }

               var10000 = var3;
            } else {
               var10000 = 0;
            }

            var2 = var10000;
            if (var1 >= 0 && var0 < 0) {
               var2 = 180 - var10000;
            }

            if (var1 < 0 && var0 < 0) {
               var2 += 180;
            }

            if (var1 >= 0 || var0 < 0) {
               return var2;
            }

            var10000 = 360 - var2;
            break;
         }
      }

      return var10000;
   }

   public static final int fixangle(int var0) {
      if (var0 >= 360) {
         var0 -= 360;
      }

      if (var0 < 0) {
         var0 += 360;
      }

      return var0;
   }

   public static int random(int var0) {
      return random.nextInt() % 2;
   }

   public static int rnd(int var0) {
      return random.nextInt(var0);
   }

   public static int rnd(int var0, int var1) {
      return random.nextInt(2) == 0 ? var0 : var1;
   }

   public static int abs(int var0) {
      return var0 >= 0 ? var0 : -var0;
   }

   public static void b() {
      GameMidlet.n = b(PaintPopup.name);
      PopupShop.i = b(GameMidlet.m);
      MapScr.j = b(b);
   }

   public static int distance(int var0, int var1, int var2, int var3) {
      if ((var0 = (var0 - var2) * (var0 - var2) + (var1 - var3) * (var1 - var3)) <= 0) {
         return 0;
      } else {
         var1 = (var0 + 1) / 2;

         do {
            var2 = var1;
            var1 = var1 / 2 + var0 / (var1 * 2);
         } while(Math.abs(var2 - var1) > 1);

         return var1;
      }
   }

   private static byte[] b(byte[] var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var0[var1] = (byte)(~var0[var1]);
         }
      }

      return var0;
   }

   public static void saveRMS(String var0, byte[] var1) throws RecordStoreException {
      var1 = b(var1);
      RecordStore var2;
      if ((var2 = RecordStore.openRecordStore(GameMidlet.APP_VERSION + var0, true)).getNumRecords() > 0) {
         var2.setRecord(1, var1, 0, var1.length);
      } else {
         var2.addRecord(var1, 0, var1.length);
      }

      var2.closeRecordStore();
   }

   public static byte[] loadRMS(String var0) {
      byte[] var1;
      try {
         RecordStore var3;
         var1 = (var3 = RecordStore.openRecordStore(GameMidlet.APP_VERSION + var0, false)).getRecord(1);
         var3.closeRecordStore();
      } catch (Exception var3) {
         return null;
      }

      return b(var1);
   }

   public static void a(String var0, String var1) {
      try {
         saveRMS(var0, var1.getBytes("UTF-8"));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static String b(String var0) {
      byte[] var3;
      if ((var3 = loadRMS(var0)) == null) {
         return null;
      } else {
         try {
            return new String(var3, "UTF-8");
         } catch (Exception e) {
            return new String(var3);
         }
      }
   }

   public static Image createRGBImage(int var0, int var1, int var2, int var3, Image var4) {
      int[] var5 = new int[var2 * var3];
      var4.getRGB(var5, 0, var2, var0, var1, var2, var3);
      return Image.createRGBImage(var5, var2, var3, true);
   }

   public static Image createImage(byte[] var0) {
      return Image.createImage(var0, 0, var0.length);
   }

   public static void c() {
      LoginScr.s = 1;

      for(int var0 = 0; var0 < FarmScr.gI().K.size(); ++var0) {
         Command var1 = (Command)FarmScr.gI().K.elementAt(var0);
         LoginScr.s += var1.caption.hashCode();
      }

      LoginScr.gI().saveLogin();
   }

   public static Image createImage(byte[] var0, byte[] var1) {
      byte[] var2 = new byte[var0.length + var1.length];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      System.arraycopy(var1, 0, var2, var0.length, var1.length);
      return Image.createImage(var2, 0, var2.length);
   }

   public static InputStream getResourceAsStream(String var0) {
      return "".getClass().getResourceAsStream(var0);
   }

   public static Image createRGBImage(Image var0, int var1) {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      int[] var4 = new int[var2 * var3];
      var0.getRGB(var4, 0, var2, 0, 0, var2, var3);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5] == var1) {
            var4[var5] = 16777215;
         }
      }

      return Image.createRGBImage(var4, var2, var3, true);
   }
}
