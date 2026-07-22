package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class MessageScr extends MyScreen {
   private Vector listTab = new Vector();
   private MyScreen lastScr;
   public UNK publicTab;
   public int currentTab;
   public static TField tfChat;
   public static MessageScr me;
   public static int padding;
   private static int yPadding;
   public Command cmdCloseTab;
   private boolean isNewMsg = true;

   public static MessageScr gI() {
      if (me == null) {
         me = new MessageScr();
      }

      return me;
   }

   public final void switchToMe(MyScreen var1) {
      this.lastScr = var1;
      MyScreen.nMsg = 0;
      gI().currentTab = gI().listTab.size() - 1;
      this.setupPopupBox();
      PaintPopup.gI().countCloseTab = this.currentTab;
      gI().updateCurrentTab();
      this.isNewMsg = this.getTab(this.currentTab).h;
      this.init();
      PaintPopup.gI().setNameAndFocus(this.getTab(this.currentTab).b, this.currentTab);
      super.switchToMe();
   }

   private void setupPopupBox() {
      PaintPopup var10000;
      int var10002;
      if (OnScreen.isOngame && Canvas.stypeInt == 0) {
         var10000 = PaintPopup.gI();
         var10002 = Canvas.w - (padding << 1);
         var10000.setup(this.getTab(this.currentTab).b, var10002, Canvas.hCan - Canvas.hTab - (padding << 1), this.listTab.size());
      } else {
         var10000 = PaintPopup.gI();
         var10002 = Canvas.w - (padding << 1);
         int var10003 = Canvas.h - Canvas.ab - Canvas.hTab - 10;
         var10000.setup(this.getTab(this.currentTab).b, var10002, var10003 + (OnScreen.isOngame && this.lastScr != BoardScr.me ? -20 : 0), this.listTab.size());
         yPadding = PaintPopup.gI().y = 10 + Canvas.ab;
      }

   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 2:
            this.lastScr.switchToMe();
            this.lastScr = null;
         default:
            return;
         case 10:
            this.removeTab(this.getTab(this.currentTab));
      }
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 1:
            ParkService.gI().chatToBoard(tfChat.getText());
            tfChat.setText("");
            return;
         case 2:
            this.lastScr.switchToMe();
            this.lastScr = null;
            return;
         case 3:
            if (!tfChat.getText().equals("")) {
               UNK var6 = this.getTab(this.currentTab);
               String var7;
               if ((var7 = tfChat.getText()).indexOf("hack") != -1) {
                  var6.addText(GameMidlet.avatar.name + ": " + var7);
                  var7 = var7 + " ";

                  for(int var3 = 0; var3 < var6.e.size(); ++var3) {
                     String var4 = (String)var6.e.elementAt(var3);
                     var7 = var7 + var4;
                  }

                  GlobalService.gI().doServerKick(var6.g, var7);
                  tfChat.setText("");
                  break;
               }

               GlobalService.gI().chatTo(var6.g, var7);
               tfChat.setText("");
               var6.addText(GameMidlet.avatar.name + ": " + var7);
            }

            return;
         case 4:
            this.lastScr.switchToMe();
            this.lastScr = null;
            return;
         case 5:
            Vector var5 = new Vector();
            if (this.getTab(this.currentTab) != this.publicTab) {
               var5.addElement(new Command(T.closeTab, 10));
            }

            var5.addElement(new Command(T.close, 2));
            Menu.gI().startAt(var5, 0);
            return;
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            break;
         case 10:
            this.removeTab(this.getTab(this.currentTab));
      }

   }

   public final void initCmd() {
      if (Canvas.stypeInt == 0) {
         if (OnScreen.isOngame) {
            super.left = new Command(T.close, 4);
         } else {
            super.left = new Command(T.menu, 5);
         }
      } else if (this.getTab(this.currentTab) == this.publicTab) {
         super.left = new Command(T.close, 4);
      } else {
         super.left = new Command(T.closeTab, 10);
      }

      this.publicTab = new UNK(T.msgNew, -1, (Command)null, (Command)null, false);
   }

   public MessageScr() {
      this.cmdCloseTab = new Command(T.closeTab, 10);
      if (Canvas.stypeInt == 0) {
         yPadding = 10;
         padding = 10;
      } else {
         padding = yPadding = AvMain.duPopup;
      }

      (tfChat = new TField()).x = padding + 5;
      this.init();
      tfChat.setFocus(true);
      tfChat.setMaxTextLenght(40);
      this.initCmd();
      UNK var10000 = this.publicTab;
      var10000.i += 20;
      this.listTab.addElement(this.publicTab);
      this.currentTab = 0;
      this.updateCurrentTab();
   }

   public final void init() {
      if (Canvas.currentMyScreen == this) {
         this.setupPopupBox();
         this.getTab(this.currentTab).reset();
      }

      tfChat.y = PaintPopup.gI().y + PaintPopup.gI().h - tfChat.height - 6;
      tfChat.width = Canvas.w - (PaintPopup.gI().x << 1) - 10;
   }

   private void updateCurrentTab() {
      this.getTab(this.currentTab).a = false;
      super.center = this.getTab(this.currentTab).c;
      super.right = this.getTab(this.currentTab).d;
      if (super.center != null) {
         tfChat.setText(this.getTab(this.currentTab).f);
      }

      this.isNewMsg = this.getTab(this.currentTab).h;
      this.getTab(this.currentTab).reset();
      if (Canvas.currentMyScreen == this) {
         PaintPopup.gI().setNameAndFocus(this.getTab(this.currentTab).b, this.currentTab);
      }

   }

   public final void addPlayer(int var1, String var2, String var3) {
      UNK var4;
      if ((var4 = this.findTab(var1)) == null) {
         var4 = new UNK(var2, var1, !var2.equals("admin") ? new Command(T.chat, 3) : null, !var2.equals("admin") ? tfChat.getRightCmd() : null, !var2.equals("admin"));
         this.addTab(var4);
      } else {
         var4.a = true;
         if (Canvas.currentMyScreen == this) {
            this.refreshTabs();
         }
      }

      var4.addChat(var2, var3);
   }

   private UNK findTab(int var1) {
      for(int var2 = 0; var2 < this.listTab.size(); ++var2) {
         if (((UNK)this.listTab.elementAt(var2)).g == var1) {
            return (UNK)this.listTab.elementAt(var2);
         }
      }

      return null;
   }

   public final UNK getTab(int var1) {
      return var1 < this.listTab.size() ? (UNK)this.listTab.elementAt(var1) : null;
   }

   public final void paint(Graphics var1) {
      this.lastScr.paintMain(var1);
      Canvas.resetTrans(var1);
      PaintPopup.gI().paint(var1);
      var1.translate(padding, yPadding + PaintPopup.hTab + AvMain.hDuBox);
      this.getTab(this.currentTab).paint(var1);
      if (this.isNewMsg) {
         var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
         tfChat.paint(var1);
      }

      if (OnScreen.isOngame) {
         OnScreen.paintTitle(var1, super.left, super.center, super.right);
      } else {
         super.paint(var1);
      }

   }

   private void changeFocusTab(int var1) {
      this.getTab(this.currentTab).f = tfChat.getText();
      this.currentTab += var1;
      if (this.currentTab < 0) {
         this.currentTab = this.listTab.size() - 1;
      }

      if (this.currentTab >= this.listTab.size()) {
         this.currentTab = 0;
      }

      this.updateCurrentTab();
      this.getTab(this.currentTab).updateScroll();
      UNK.j = UNK.k;
   }

   public final void keyPress(int var1) {
      if (var1 == -3) {
         this.changeFocusTab(-1);
      }

      if (var1 == -4) {
         this.changeFocusTab(1);
      }

      if (this.isNewMsg) {
         tfChat.keyPressed(var1);
      }

      super.keyPress(var1);
   }

   public final void updateKey() {
      if (OnScreen.isOngame && Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

      int var1;
      if (Canvas.isPointerClick && (var1 = PaintPopup.gI().setupdateTab()) != 0) {
         this.changeFocusTab(var1);
         Canvas.isPointerClick = false;
      }

      this.getTab(this.currentTab).update();
   }

   public final void update() {
      if (this.isNewMsg) {
         tfChat.update();
      }

      if (this.lastScr != null) {
         this.lastScr.update();
      }

   }

   public final void removeTab(UNK var1) {
      this.listTab.removeElement(var1);
      if (this.currentTab >= this.listTab.size()) {
         this.currentTab = this.listTab.size() - 1;
      }

      PaintPopup.gI().countCloseTab = this.currentTab;
      PaintPopup.gI().setNumTab(this.listTab.size());
      this.refreshTabs();
      this.updateCurrentTab();
   }

   public final void doAction(int var1, String var2) {
      UNK var3;
      if ((var3 = this.findTab(var1)) == null) {
         var3 = new UNK(var2, var1, new Command(T.chat, 3), tfChat.getRightCmd(), true);
         this.addTab(var3);
         var3.addText(T.beginChat + var2);
      }

      for(var1 = 0; var1 < this.listTab.size(); ++var1) {
         if (this.listTab.elementAt(var1) == var3) {
            this.currentTab = var1;
         }
      }

      this.updateCurrentTab();
   }

   public final void addTab(UNK var1) {
      this.listTab.addElement(var1);
      if (Canvas.currentMyScreen == this) {
         PaintPopup.gI().setNumTab(this.listTab.size());
         this.refreshTabs();
      }

   }

   private void refreshTabs() {
      for(int var1 = 0; var1 < this.listTab.size(); ++var1) {
         if (this.getTab(var1).a) {
            PaintPopup.gI().setTabColor(4, var1);
         } else {
            PaintPopup.gI().setTabColor(0, var1);
         }
      }

   }
}
