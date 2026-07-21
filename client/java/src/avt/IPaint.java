package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public interface IPaint {
   void paintTextBox(Graphics var1, int var2, int var3, int var4, int var5, TField var6, boolean var7);

   void drawRectangle(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

   void paintBoxTab(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int[] var13, int[] var14, String var15);

   void paintCmd(Graphics var1, Command var2, Command var3, Command var4);

   void drawArea(Graphics var1, int var2, int var3, int var4, int var5);

   void init();

   void initImgCard();

   void paintHalf(Graphics var1, Card var2);

   void paintHalfBackFull(Graphics var1, Card var2);

   void paintFull(Graphics var1, Card var2);

   void paintSmall(Graphics var1, Card var2, boolean var3);

   void paintMSG(Graphics var1);

   void initPos();

   int getDisplayValue();

   void initPosLogin(LoginScr var1);

   void paintCheckBox(Graphics var1, int var2, int var3, int var4, boolean var5);

   void paintPopupBack(Graphics var1, int var2, int var3, int var4, int var5, int var6);

   void drawBorder(Graphics var1, int var2, int var3, int var4, int var5);

   void drawHighlightedArea(Graphics var1, int var2, int var3, int var4, int var5, int var6);

   void drawString(Graphics var1, String var2, int var3, int var4, int var5);

   void drawSelectedArea(Graphics var1, int var2, int var3, int var4, int var5);

   void setVirtualKeyFish(int var1);

   void initPosPhom();

   void drawFormattedText(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, String var10, int var11, int var12);

   void drawStateElement(Graphics var1, int var2, int var3, int var4, boolean var5, int var6, int[] var7);

   void paintDefaultBg(Graphics var1);

   void drawElement(Graphics var1, int var2, int var3);

   void drawTextElements(Graphics var1, String var2, String var3, String var4);

   void setValue(int var1);

   void initResourceOne();

   void initResourceTwo();

   void initResourceThree();

   void initResourceFour();

   void paintPlayer(Graphics var1, int var2, int var3, int var4, int var5);

   void initResourceFive();

   void updateKeyOn(Command var1, Command var2, Command var3);

   void paintBackground(Graphics var1);

   void paintCommandAlt(Graphics var1, Command var2, Command var3, Command var4);

   void drawContainer(Graphics var1, int var2, int var3, int var4, int var5);

   void drawPanel(Graphics var1, int var2, int var3, int var4, int var5);

   void drawFrame(Graphics var1, int var2, int var3, int var4, int var5);

   void drawVectorElements(Graphics var1, Vector var2, int var3, int var4);

   void drawBox(Graphics var1, int var2, int var3, int var4, int var5);
}
