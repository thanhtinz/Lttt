package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public interface FontX {
   void drawString(Graphics var1, String var2, int var3, int var4, int var5);

   int getWidth(String var1);

   String[] splitFontBStrInLine(String var1, int var2);

   Vector splitFontBStrInLineV(String var1, int var2);

   String replace(String var1, String var2, String var3);

   String[] split(String var1, String var2);

   int getHeight();
}
