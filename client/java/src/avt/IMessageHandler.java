package avt;

public interface IMessageHandler {
   void onMessage(Message var1);

   void onConnectionFail();

   void onDisconnected();
}
