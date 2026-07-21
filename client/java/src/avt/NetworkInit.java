package avt;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

final class NetworkInit implements Runnable {
   private final String b;
   final Session_ME session;

   NetworkInit(Session_ME var1, String var2) {
      this.session = var1;
      this.b = var2;
   }

   public final void run() {
      Session_ME.l = false;
      (new Thread(new class_kh(this))).start();
      this.session.connecting = true;
      this.session.connected = true;

      try {
         String var2 = this.b;
         Session_ME.setSc(this.session, (SocketConnection)Connector.open(var2));
         Session_ME.setDos(this.session, Session_ME.setSc(this.session).openDataOutputStream());
         this.session.dis = Session_ME.setSc(this.session).openDataInputStream();
         (new Thread(Session_ME.getSender(this.session))).start();
         this.session.sendThread = new Thread(new MessageCollector(this.session));
         this.session.sendThread.start();
         this.session.j = System.currentTimeMillis();
         Session_ME.sendMes(this.session, new Message((byte)-27));
         this.session.connecting = false;
      } catch (Exception var4) {
         var4.printStackTrace();

         try {
            Thread.sleep(500L);
         } catch (InterruptedException var3) {
         }

         if (!Session_ME.l && this.session.messageHandler != null) {
            this.session.close();
            this.session.messageHandler.onConnectionFail();
         }
      }

   }
}
