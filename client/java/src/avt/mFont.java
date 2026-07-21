package avt;

import java.io.DataInputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class mFont implements FontX {
   private Image a;
   private String b;
   private byte[] c;
   private int d;
   private static String[] e = new String[]{"normal", "border", "arial", "black", "number", "smallRed", "smallYellow", "big"};
   private char f;
   private char g;

   public mFont(int var1) {
      try {
         DataInputStream var2 = new DataInputStream(CRes.getResourceAsStream(T.getPath() + "/font/" + e[var1]));
         this.b = var2.readUTF();
         this.c = new byte[this.b.length()];

         for(int var3 = 0; var3 < this.c.length; ++var3) {
            this.c[var3] = var2.readByte();
         }

         this.d = var2.readByte();
         FilePack.b(T.ar);
         this.a = FilePack.getImage(String.valueOf(var1));
         FilePack.reset();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public final void drawString(Graphics var1, String var2, int var3, int var4, int var5) {
      int var6 = var2.length();
      if (var5 == 0) {
         var5 = var3;
      } else if (var5 == 1) {
         var5 = var3 - this.getWidth(var2);
      } else {
         var5 = var3 - (this.getWidth(var2) >> 1);
      }

      for(int var7 = 0; var7 < var6; ++var7) {
         this.f = var2.charAt(var7);
         if (this.f == ' ') {
            var5 += this.c[0] >> 1;
         } else {
            if ((var3 = this.b.indexOf(this.f)) == -1) {
               var3 = 0;
            }

            if (var3 >= 0) {
               var1.drawRegion(this.a, 0, var3 * this.d, this.a.getWidth(), this.d, 0, var5, var4, 20);
            }

            var5 += this.c[var3];
         }
      }

   }

   public final int getWidth(String var1) {
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         this.g = var1.charAt(var4);
         if (this.g == ' ') {
            var3 += this.c[0] >> 1;
         } else {
            int var2;
            if ((var2 = this.b.indexOf(this.g)) == -1) {
               var2 = 0;
            }

            var3 += this.c[var2];
         }
      }

      return var3;
   }

   public final String[] splitFontBStrInLine(String var1, int var2) {
      Vector var5;
      String[] var3 = new String[var2 = (var5 = this.splitFontBStrInLineV(var1, var2)).size()];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = (String)var5.elementAt(var4);
      }

      return var3;
   }

   public final Vector splitFontBStrInLineV(String var1, int var2) {
      Vector var3 = new Vector();
      int var4;
      if ((var4 = var1.length()) <= 1) {
         Vector var9;
         (var9 = new Vector()).addElement(var1);
         return var9;
      } else {
         String var5 = "";
         int var6 = 0;
         int var7 = 0;

         while(true) {
            while(this.getWidth(var5) < var2) {
               var5 = var5 + var1.charAt(var7);
               ++var7;
               if (var1.charAt(var7) == '\n') {
                  break;
               }

               if (var7 >= var4 - 1) {
                  var7 = var4 - 1;
                  break;
               }
            }

            if (var7 != var4 - 1 && var1.charAt(var7 + 1) != ' ') {
               int var8;
               for(var8 = var7; var1.charAt(var7 + 1) != '\n' && (var1.charAt(var7 + 1) != ' ' || var1.charAt(var7) == ' ') && var7 != var6; --var7) {
               }

               if (var7 == var6) {
                  var7 = var8;
               }
            }

            var3.addElement(var1.substring(var6, var7 + 1));
            if (var7 == var4 - 1) {
               break;
            }

            for(var6 = var7 + 1; var6 != var4 - 1 && var1.charAt(var6) == ' '; ++var6) {
            }

            if (var6 == var4 - 1) {
               break;
            }

            var7 = var6;
            var5 = "";
         }

         return var3;
      }
   }

   public final String replace(String var1, String var2, String var3) {
      StringBuffer var4 = new StringBuffer();
      int var5 = var1.indexOf(var2);
      int var6 = 0;

      for(int var7 = var2.length(); var5 != -1; var5 = var1.indexOf(var2, var6)) {
         var4.append(var1.substring(var6, var5)).append(var3);
         var6 = var5 + var7;
      }

      var4.append(var1.substring(var6, var1.length()));
      return var4.toString();
   }

   public final String[] split(String var1, String var2) {
      int var3 = 0;
      int var5 = var2.length();

      int var4;
      for(var4 = var1.indexOf(var2, 0); var4 != -1; ++var3) {
         var4 += var5;
         var4 = var1.indexOf(var2, var4);
      }

      String[] var8 = new String[var3 + 1];
      var4 = var1.indexOf(var2);
      int var6 = 0;

      int var7;
      for(var7 = 0; var4 != -1; ++var7) {
         var8[var7] = var1.substring(var6, var4);
         var6 = var4 + var5;
         var4 = var1.indexOf(var2, var6);
      }

      var8[var7] = var1.substring(var6, var1.length());
      return var8;
   }

   public final int getHeight() {
      return this.d;
   }
}
