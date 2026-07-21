package avt;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Image;

public final class FilePack {
   static FilePack instance;
   private String[] fname;
   private int[] fpos;
   private int[] flen;
   private byte[] fullData;
   private int nFile;
   private int hSize;
   private String name;
   private byte[] code = new byte[]{78, 103, 117, 121, 101, 110, 86, 97, 110, 77, 105, 110, 104};
   private int codeLen;
   private DataInputStream file;

   public FilePack() {
      this.codeLen = this.code.length;
   }

   public static void reset() {
      if (instance != null) {
         instance.close();
      }
      instance = null;
      System.gc();
   }

   public FilePack(String var1) {
      this.codeLen = this.code.length;
      int var3 = 0;
      int var4 = 0;
      this.name = var1;
      this.hSize = 0;
      this.file = new DataInputStream(this.getClass().getResourceAsStream(this.name));

      try {
         this.nFile = this.file.readUnsignedByte();
         ++this.hSize;
         this.fname = new String[this.nFile];
         this.fpos = new int[this.nFile];
         this.flen = new int[this.nFile];

         for(int var5 = 0; var5 < this.nFile; ++var5) {
            byte var7;
            byte[] var2 = new byte[var7 = this.file.readByte()];
            this.file.read(var2);
            this.encode(var2);
            this.fname[var5] = new String(var2);
            this.fpos[var5] = var3;
            this.flen[var5] = this.file.readUnsignedShort();
            var3 += this.flen[var5];
            var4 += this.flen[var5];
            this.hSize += var7 + 3;
         }

         this.fullData = new byte[var4];
         this.file.readFully(this.fullData);
         this.encode(this.fullData);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      this.close();
   }

   public static Image getImage(String var0) {
      Image img = null;
      try {
         if (instance != null) {
            img = instance.loadImage(var0 + ".png");
         }
      } catch (Throwable t) {
      }

      if (img == null) {
         try {
            img = Image.createImage(T.getPath() + "/" + var0 + ".png");
         } catch (Throwable t) {
         }
      }

      if (img == null) {
         try {
            img = Image.createImage(1, 1);
         } catch (Throwable t) {
         }
      }

      return img;
   }

   public static void b(String var0) {
      try {
         instance = new FilePack(T.getPath() + var0);
      } catch (Throwable t) {
         instance = new FilePack();
      }
   }

   private void encode(byte[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3] ^= this.code[var3 % this.codeLen];
      }

   }

   private void close() {
      try {
         if (this.file != null) {
            this.file.close();
            return;
         }
      } catch (IOException var2) {
      }

   }

   private Image loadImage(String var1) {
      for(int var2 = 0; var2 < this.nFile; ++var2) {
         if (this.fname[var2].compareTo(var1) == 0) {
            return Image.createImage(this.fullData, this.fpos[var2], this.flen[var2]);
         }
      }

      return null;
   }

   public final byte[] loadData(String var1) {
      for(int var2 = 0; var2 < this.nFile; ++var2) {
         if (this.fname[var2].compareTo(var1) == 0) {
            byte[] var3 = new byte[this.flen[var2]];
            System.arraycopy(this.fullData, this.fpos[var2], var3, 0, this.flen[var2]);
            return var3;
         }
      }

      return null;
   }
}
