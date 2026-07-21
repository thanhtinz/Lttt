package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class RegisterScr extends MyScreen {
   private static RegisterScr instance;
   private byte male = 1;
   public int index = 0;
   private int selected;
   public int countLeft;
   public int countRight;
   private Vector listHair;
   private Vector listClothing;
   private Vector listQ;
   private int time = 0;

   public static RegisterScr gI() {
      if (instance == null) {
         instance = new RegisterScr();
      }

      return instance;
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            Vector var3;
            (var3 = new Vector()).addElement(new Command(T.yes, 0, this));
            var3.addElement(new Command(T.no, 1, this));
            Canvas.setInfoC(T.milk, var3);
         default:
      }
   }

   public final void commandActionPointer(int var1) {
      switch (var1) {
         case 0:
            doFinish();
            return;
         case 1:
            doFinish();
         default:
      }
   }

   public final void switchToMe() {
      GameMidlet.avatar.direct = 0;
      GameMidlet.avatar.seriPart = new Vector();
      this.getAvatarPart();
      super.center = new Command(T.success, 0);
      SeriPart var1 = new SeriPart();
      int var2 = CRes.random.nextInt(this.listQ.size());
      var1.idPart = ((APartInfo)this.listQ.elementAt(var2)).IDPart;
      GameMidlet.avatar.addSeri(var1);
      var1 = new SeriPart();
      var2 = CRes.random.nextInt(this.listClothing.size());
      var1.idPart = ((APartInfo)this.listClothing.elementAt(var2)).IDPart;
      GameMidlet.avatar.addSeri(var1);
      (var1 = new SeriPart()).idPart = 4;
      GameMidlet.avatar.addSeri(var1);
      var1 = new SeriPart();
      var2 = CRes.random.nextInt(this.listHair.size());
      var1.idPart = ((APartInfo)this.listHair.elementAt(var2)).IDPart;
      GameMidlet.avatar.addSeri(var1);
      GameMidlet.avatar.addSeri(new SeriPart((short)0));
      GameMidlet.avatar.orderSeriesPath();
      PaintPopup.gI().a(T.createChar, 150 * AvMain.hd, 170 + (AvMain.hd == 2 ? 120 : 0), 1);
      super.switchToMe();
   }

   private void getAvatarPart() {
      GameMidlet.avatar.gender = this.male;
      if (this.listHair != null) {
         this.listHair.removeAllElements();
         this.listClothing.removeAllElements();
         this.listQ.removeAllElements();
      }

      this.listHair = new Vector();
      this.listClothing = new Vector();
      this.listQ = new Vector();

      for(int var1 = 0; var1 < AvatarData.listPart.length; ++var1) {
         APartInfo var2;
         if (AvatarData.listPart[var1] instanceof APartInfo && (var2 = (APartInfo)AvatarData.listPart[var1]) != null && (var2.gender == this.male || var2.gender == 0) && var2.level == 0) {
            if (var2.zOrder == 50) {
               this.listHair.addElement(var2);
            } else if (var2.zOrder == 20) {
               this.listClothing.addElement(var2);
            } else if (var2.zOrder == 10) {
               this.listQ.addElement(var2);
            }
         }
      }

      this.selected = 0;
      this.getId();
      if (GameMidlet.avatar.action != 10) {
         GameMidlet.avatar.setAction((byte)1);
      }

      GameMidlet.avatar.orderSeriesPath();
   }

   private static void doFinish() {
      Canvas.isInitChar = true;
      Canvas.startWaitDlg(T.createChar + "...");
      GlobalService.gI().doRequestCreCharacter();
   }

   public final void update() {
      if (this.countLeft > 0) {
         --this.countLeft;
      }

      if (this.countRight > 0) {
         --this.countRight;
      }

      ++this.time;
      if (this.time > 50) {
         this.time = 0;
         int var1 = CRes.random.nextInt(3);
         if (GameMidlet.avatar.action != 10) {
            if (var1 == 0) {
               GameMidlet.avatar.setAction((byte)1);
            } else {
               GameMidlet.avatar.setAction((byte)0);
            }
         }
      }

      GameMidlet.avatar.updateFrame();
   }

   public final void setKeyUpDown(int var1) {
      this.index = var1;
      if (this.index < 0) {
         this.index = 1;
      }

      if (this.index > 1) {
         this.index = 0;
      }

   }

   public final void setKeyLeftRight(int var1) {
      this.selected += var1;
      if (this.selected < 0) {
         this.selected = 1;
      }

      if (this.selected > 1) {
         this.selected = 0;
      }

      if (this.index == 0) {
         if (this.male == 1) {
            this.male = 2;
         } else {
            this.male = 1;
         }

         this.getAvatarPart();
      } else {
         this.getId();
      }

   }

   public final void updateKey() {
      Canvas.paint.initResourceFive();
      super.updateKey();
   }

   private void getId() {
      for(int var1 = 0; var1 < GameMidlet.avatar.seriPart.size(); ++var1) {
         SeriPart var2;
         APartInfo var3;
         if ((var3 = (APartInfo)AvatarData.getPart((var2 = (SeriPart)GameMidlet.avatar.seriPart.elementAt(var1)).idPart)).zOrder == 50 && this.listHair.size() != 0 && this.selected < this.listHair.size()) {
            var2.idPart = ((APartInfo)this.listHair.elementAt(this.selected)).IDPart;
         }

         if (var3.zOrder == 20 && this.listClothing.size() != 0 && this.selected < this.listClothing.size()) {
            var2.idPart = ((APartInfo)this.listClothing.elementAt(this.selected)).IDPart;
         }

         if (var3.zOrder == 10 && this.listQ.size() != 0 && this.selected < this.listQ.size()) {
            var2.idPart = ((APartInfo)this.listQ.elementAt(this.selected)).IDPart;
         }
      }

      GameMidlet.avatar.orderSeriesPath();
   }

   public final void paint(Graphics var1) {
      Canvas.loadMap.paint(var1);
      Canvas.loadMap.paintBackGround(var1);
      Canvas.resetTrans(var1);
      PaintPopup.gI().paint(var1);
      var1.translate(PaintPopup.gI().x, PaintPopup.gI().y);
      Canvas.paint.paintPlayer(var1, this.index, this.male, this.countLeft, this.countRight);
      super.paint(var1);
   }

   public static void onCreaCharacter(boolean var0) {
      Canvas.endDlg();
      if (var0) {
         ClientUtilities.requestChangeZone();
      } else {
         Canvas.startOKDlg(T.createCharFail);
      }

   }
}
