package main;

import avt.Avatar;
import avt.AvatarData;
import avt.CRes;
import avt.GlobalMessageHandler;
import avt.IndexPlayer;
import avt.Session_ME;
import avt.SplashScr;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class GameMidlet extends MIDlet {
   public static final String APP_VERSION = "2.5.8";
   public static String[][][] nameSV = new String[][][]{{{"Xu So Avatar", "PGaming"}}};
   public static String[][][] ipSV = new String[][][]{{{"192.168.1.69"}}};
   public static int[][][] PORT = new int[][][]{{{19128}}};
   public static final String[][] linkGetHost = new String[][]{{"http://teamobi.com/srvips/avatar2.txt"}, {"http://teamobi.com/srvips/avatarinterd2.txt"}};
   public static int CLIENT_TYPE = 8;
   public static byte PROVIDER = -1;
   public static String g;
   private static Canvas canvas;
   public static GameMidlet instance;
   public static Avatar avatar;
   public static IndexPlayer myIndexP;
   public static Vector listContainer;
   private static Display p;
   public static int l;
   public static String m;
   public static String n;

   public GameMidlet() {
      instance = this;
      InputStream var1 = this.getClass().getResourceAsStream("/provider.txt");
      StringBuffer var2 = new StringBuffer();

      try {
         int var3;
         while((var3 = var1.read()) != -1) {
            var2.append((char)var3);
         }

         PROVIDER = Byte.parseByte(var2.toString());
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      if (PROVIDER == -1) {
         AvatarData.loadProvider();
      }

      InputStream var9 = CRes.getResourceAsStream("/agent.txt");
      StringBuffer var6 = new StringBuffer();

      try {
         int var7;
         while((var7 = var9.read()) != -1) {
            var6.append((char)var7);
         }

         g = var6.toString();
      } catch (Exception e) {
      }

      (canvas = new Canvas()).startThread();
      avatar = new Avatar();
      myIndexP = new IndexPlayer();
      SplashScr.gI().switchToMe();
      canvas.sizeChanged(0, 0);
      canvas.setSize();
      Display.getDisplay(this).setCurrent(canvas);
      Session_ME.gI().setHandler(GlobalMessageHandler.gI());
      String var8;
      if ((var8 = CRes.loadString("avatar")) == null || !var8.equals(APP_VERSION)) {
         AvatarData.delRMS();
      }

   }

   public void destroyApp(boolean var1) {
      instance.notifyDestroyed();
   }

   public static void exit() {
      instance.destroyApp(true);
   }

   protected void pauseApp() {
   }

   protected void startApp() {
      (p = Display.getDisplay(this)).setCurrent(canvas);
   }

   public static void doSendSMS(String var0, String var1) {
      (new Thread(new SMSMessageSender(var1, var0))).start();
   }

   public static String createhttpconnect(String var0) {
      try {
         HttpConnection var4;
         (var4 = (HttpConnection)Connector.open(var0)).setRequestMethod("GET");
         var4.setRequestProperty("Content-Type", "//textMiniMap plain");
         var4.setRequestProperty("Connection", "close");
         if (var4.getResponseCode() == 200) {
            String var1 = "";
            InputStream var2 = var4.openInputStream();
            int var5;
            if ((var5 = (int)var4.getLength()) != -1) {
               byte[] var6 = new byte[var5];
               var2.read(var6);
               var1 = new String(var6);
            }

            return var1;
         } else {
            return null;
         }
      } catch (IOException var6) {
         return null;
      }
   }

   public static void flatForm(String var0) {
      try {
         instance.platformRequest(var0);
         instance.notifyDestroyed();
      } catch (ConnectionNotFoundException var2) {
         var2.printStackTrace();
      }

   }
}
