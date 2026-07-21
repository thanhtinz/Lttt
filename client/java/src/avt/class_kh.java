package avt;

final class class_kh implements Runnable {
   private NetworkInit a;

   class_kh(NetworkInit var1) {
      this.a = var1;
   }

   public final void run() {
      try {
         Thread.sleep(20000L);
      } catch (InterruptedException var3) {
         System.out.println("ERROR 1111111111");
      }

      if (this.a.session.connecting) {
         try {
            Session_ME.setSc(this.a.session).close();
            Sender.a(Session_ME.getSender(this.a.session)).removeAllElements();
         } catch (Exception var2) {
         }

         Session_ME.l = true;
         this.a.session.connecting = false;
         this.a.session.connected = false;
         this.a.session.messageHandler.onConnectionFail();
      }

   }
}
