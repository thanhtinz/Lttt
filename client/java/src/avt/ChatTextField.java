package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class ChatTextField extends AvMain {
   public static ChatTextField instance;
   public TField tfChat;
   public static boolean isShow = false;
   public IChatable parentMyScreen;
   private long lastTimeChat;

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.tfChat.setText("");
            isShow = false;
            this.tfChat.setFocus(true);
            return;
         case 1:
            long var3;
            if ((var3 = System.currentTimeMillis()) - this.lastTimeChat < 2000L) {
               return;
            } else if (this.parentMyScreen != null) {
               this.parentMyScreen.onChatFromMe(this.tfChat.getText());
               this.tfChat.setText("");
               isShow = false;
               this.tfChat.setFocus(true);
               this.lastTimeChat = var3;
            }
         default:
      }
   }

   public final void closeChat() {
      this.tfChat.setText("");
      isShow = false;
      this.tfChat.setFocus(true);
      if (OnScreen.isOngame && OptionScr.isVirTualKey) {
         OptionScr.isVirTualKey = false;
         OptionScr.gI().mapFocus[4] = 0;
         Canvas.instance.setSize();
      }

   }

   protected ChatTextField() {
      super.left = new Command(T.close, 0);
      super.center = new Command(T.chat, 1);
      this.tfChat = new TField();
      this.tfChat.e = false;
      this.tfChat.setFocus(true);
      this.init();
      this.tfChat.x = (Canvas.w - this.tfChat.width) / 2;
      this.tfChat.setMaxTextLenght(40);
      super.right = this.tfChat.getRightCmd();
   }

   public final void init() {
      this.tfChat.y = Canvas.hCan - Canvas.hTab - this.tfChat.height - 5;
      if (OnScreen.isOngame) {
         TField var10000 = this.tfChat;
         var10000.y -= 2 * AvMain.hd;
      }

      this.tfChat.width = Canvas.w - 10;
   }

   public final void keyPress(int var1) {
      if (isShow) {
         this.tfChat.keyPressed(var1);
      }

   }

   public static ChatTextField gI() {
      return instance == null ? (instance = new ChatTextField()) : instance;
   }

   public final void startChat(int var1, IChatable var2) {
      if (Canvas.currentFace == null) {
         this.tfChat.keyPressed(var1);
         if (!this.tfChat.getText().equals("")) {
            this.parentMyScreen = var2;
            isShow = true;
         }

         this.init();
      }

   }

   public final void updateKey() {
      this.tfChat.update();
      if (OnScreen.isOngame && Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

   }

   public final void paint(Graphics var1) {
      if (OnScreen.isOngame) {
         OnScreen.paintTitle(var1, super.left, super.center, super.right);
      } else {
         super.paint(var1);
      }

      this.tfChat.paint(var1);
   }
}
