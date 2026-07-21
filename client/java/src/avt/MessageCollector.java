package avt;

import java.io.IOException;

final class MessageCollector implements Runnable {
   private Session_ME instance;

   MessageCollector(Session_ME var1) {
      this.instance = var1;
   }

   public final void run() {
      while(true) {
         while(true) {
            try {
               if (this.instance.isConnected()) {
                  MessageCollector var1 = this;
                  byte var2 = this.instance.dis.readByte();
                  if (this.instance.getKeyComplete) {
                     var2 = Session_ME.readKey(this.instance, var2);
                  }

                  int var3;
                  int var5;
                  if (this.instance.getKeyComplete) {
                     byte var4 = this.instance.dis.readByte();
                     var5 = this.instance.dis.readByte();
                     var3 = (Session_ME.readKey(this.instance, var4) & 255) << 8 | Session_ME.readKey(this.instance, (byte)var5) & 255;
                  } else {
                     var3 = this.instance.dis.readUnsignedShort();
                  }

                  byte[] var11 = new byte[var3];
                  var5 = 0;
                  int var6 = 0;

                  int var7;
                  while(var5 != -1 && var6 < var3) {
                     if ((var5 = var1.instance.dis.read(var11, var6, var3 - var6)) > 0) {
                        var6 += var5;
                        Session_ME var10000 = var1.instance;
                        var10000.g += var6 + 5;
                        var7 = Session_ME.gI().g + Session_ME.gI().sendByteCount;
                        var1.instance.k = var7 / 1024 + "." + var7 % 1024 / 102 + "Kb";
                     }
                  }

                  if (var1.instance.getKeyComplete) {
                     for(var7 = 0; var7 < var11.length; ++var7) {
                        var11[var7] = Session_ME.readKey(var1.instance, var11[var7]);
                     }
                  }

                  Message var10;
                  if ((var10 = new Message(var2, var11)) != null) {
                     try {
                        if (var10.command == -27) {
                           this.getKey(var10);
                           continue;
                        }

                        this.instance.messageHandler.onMessage(var10);
                     } catch (Exception e) {
                        e.printStackTrace();
                     }
                     continue;
                  }
               }
            } catch (Exception var11) {
            }

            if (this.instance.connected) {
               if (this.instance.messageHandler != null) {
                  if (System.currentTimeMillis() - this.instance.j > 500L) {
                     this.instance.messageHandler.onDisconnected();
                  } else {
                     this.instance.messageHandler.onConnectionFail();
                  }
               }

               if (Session_ME.setSc(this.instance) != null) {
                  Session_ME.cleanNW(this.instance);
               }
            }

            return;
         }
      }
   }

   private void getKey(Message var1) throws IOException {
      byte var2 = var1.reader().readByte();
      this.instance.key = new byte[var2];

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         this.instance.key[var3] = var1.reader().readByte();
      }

      for(var3 = 0; var3 < this.instance.key.length - 1; ++var3) {
         byte[] var10000 = this.instance.key;
         var10000[var3 + 1] ^= this.instance.key[var3];
      }

      this.instance.getKeyComplete = true;
   }
}
