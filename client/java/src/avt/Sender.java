package avt;

import java.util.Vector;

final class Sender implements Runnable {
   private final Vector sendingMessage;
   private Session_ME instance;

   public Sender(Session_ME var1) {
      this.instance = var1;
      this.sendingMessage = new Vector();
   }

   public final void AddMessage(Message var1) {
      this.sendingMessage.addElement(var1);
   }

   public final void run() {
      while(this.instance.connected) {
         if (this.instance.getKeyComplete) {
            while(this.sendingMessage.size() > 0) {
               Message var1 = (Message)this.sendingMessage.elementAt(0);
               this.sendingMessage.removeElementAt(0);
               Session_ME.sendMes(this.instance, var1);
            }
         }

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var2) {
         }
      }

   }

   static Vector a(Sender var0) {
      return var0.sendingMessage;
   }
}
