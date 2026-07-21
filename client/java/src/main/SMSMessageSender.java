package main;

import avt.T;
import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

final class SMSMessageSender implements Runnable {
   private final String a;
   private final String b;

   SMSMessageSender(String var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void run() {
      try {
         MessageConnection var1;
         TextMessage var2;
         (var2 = (TextMessage)(var1 = (MessageConnection)Connector.open(this.a)).newMessage("text")).setAddress(this.a);
         var2.setPayloadText(this.b);
         var1.send(var2);
         Canvas.startOKDlg(T.sentMsg);
      } catch (Exception var3) {
         Canvas.startOKDlg(T.canNotSendMsg);
      }

   }
}
