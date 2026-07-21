package avt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.SocketConnection;

public final class Session_ME implements ISession {
   private static Session_ME instance = new Session_ME();
   private DataOutputStream dos;
   public DataInputStream dis;
   public IMessageHandler messageHandler;
   private SocketConnection sc;
   public boolean connected;
   public boolean connecting;
   private final Sender sender = new Sender(this);
   private Thread initThread;
   public Thread sendThread;
   public int sendByteCount;
   public int g;
   boolean getKeyComplete;
   public byte[] key = null;
   private byte curR;
   private byte curW;
   long j;
   public String k = "";
   public static boolean l;

   public static Session_ME gI() {
      return instance;
   }

   public final boolean isConnected() {
      return this.connected;
   }

   public final void setHandler(IMessageHandler var1) {
      this.messageHandler = var1;
   }

   public final void connect(String var1) {
      if (!this.connected && !this.connecting) {
         this.getKeyComplete = false;
         this.sc = null;
         this.initThread = new Thread(new NetworkInit(this, var1));
         this.initThread.start();
      }

   }

   public final void sendMessage(Message var1) {
      this.sender.AddMessage(var1);
   }

   private synchronized void doSendMessage(Message var1) {
      byte[] var2 = var1.getData();

      try {
         int var5;
         if (this.getKeyComplete) {
            var5 = this.writeKey(var1.command);
            this.dos.writeByte(var5);
         } else {
            this.dos.writeByte(var1.command);
         }

         if (var2 != null) {
            var5 = var2.length;
            int var3;
            if (this.getKeyComplete) {
               var3 = this.writeKey((byte)(var5 >> 8));
               this.dos.writeByte(var3);
               byte var6 = this.writeKey((byte)var5);
               this.dos.writeByte(var6);
            } else {
               this.dos.writeShort(var5);
            }

            if (this.getKeyComplete) {
               for(var3 = 0; var3 < var2.length; ++var3) {
                  var2[var3] = this.writeKey(var2[var3]);
               }
            }

            this.dos.write(var2);
            this.sendByteCount += 5 + var2.length;
         } else {
            this.dos.writeShort(0);
            this.sendByteCount += 5;
         }

         this.dos.flush();
      } catch (IOException var6) {
      }

   }

   private byte writeKey(byte var1) {
      byte[] var10000 = this.key;
      byte var10003 = this.curW;
      this.curW = (byte)(var10003 + 1);
      var1 = (byte)(var10000[var10003] & 255 ^ var1 & 255);
      if (this.curW >= this.key.length) {
         this.curW = (byte)(this.curW % this.key.length);
      }

      return var1;
   }

   public final void close() {
      this.cleanNetwork();
   }

   private void cleanNetwork() {
      this.key = null;
      this.curR = 0;
      this.curW = 0;

      try {
         this.connected = false;
         this.connecting = false;
         if (this.sc != null) {
            this.sc.close();
            this.sc = null;
         }

         if (this.dos != null) {
            this.dos.close();
            this.dos = null;
         }

         if (this.dis != null) {
            this.dis.close();
            this.dis = null;
         }

         this.sendThread = null;
         System.gc();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   static SocketConnection setSc(Session_ME var0) {
      return var0.sc;
   }

   static Sender getSender(Session_ME var0) {
      return var0.sender;
   }

   static void setSc(Session_ME var0, SocketConnection var1) {
      var0.sc = var1;
   }

   static void setDos(Session_ME var0, DataOutputStream var1) {
      var0.dos = var1;
   }

   static void sendMes(Session_ME var0, Message var1) {
      var0.doSendMessage(var1);
   }

   static void cleanNW(Session_ME var0) {
      var0.cleanNetwork();
   }

   static byte readKey(Session_ME var0, byte var1) {
      byte[] var10000 = var0.key;
      byte var10003 = var0.curR;
      var0.curR = (byte)(var10003 + 1);
      var1 = (byte)(var10000[var10003] & 255 ^ var1 & 255);
      if (var0.curR >= var0.key.length) {
         var0.curR = (byte)(var0.curR % var0.key.length);
      }

      return var1;
   }
}
