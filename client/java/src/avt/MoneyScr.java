package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class MoneyScr extends MyScreen {
   public static MoneyScr instance;
   private Vector avs;
   private int type = 0;
   private int focusTap = 0;
   private MyScreen backScr;
   private Command cmdTrans;
   private Command cmdLoad;
   private Command cmdClose;
   private Image imgSell;
   private int x;
   private int y;
   private int w;
   private int h;
   private int _hSmall__;
   private int xTrans;
   private int dir;

   public static MoneyScr gI() {
      if (instance == null) {
         instance = new MoneyScr();
      }

      return instance;
   }

   public final void a(MyScreen var1) {
      this.init();
      this.focusTap = 0;
      super.selected_ = 0;
      this.backScr = var1;
      this.initCmd();
      super.switchToMe();
   }

   public final void init() {
      if (this.imgSell == null) {
         FilePack.b(T.au);
         this.imgSell = FilePack.getImage("coin");
         FilePack.reset();
      }

      String var1;
      if (LoadMap.TYPEMAP == 25) {
         this.type = 1;
         var1 = T.strName[1];
         FarmService.gI().doTransMoney(0, 0);
         Canvas.startWaitDlg();
      } else {
         var1 = T.strName[0];
         this.type = 0;
      }

      this.initPos();
      PaintPopup.gI().a(var1, this.w, this.h, 2);
      if (OnScreen.isOngame) {
         PaintPopup.gI().y = 25 + MyScreen.ITEM_HEIGHT + 1;
      }

      this.y = PaintPopup.gI().y;
      this.setCamera();
   }

   public final void setSelected(int var1, boolean var2) {
      if (var2 && super.selected_ == var1) {
         if (super.center != null) {
            super.center.perform();
         } else if (super.left != null) {
            super.left.perform();
         }
      }

      super.setSelected(var1, var2);
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            Canvas.cameraList.isShow = false;
            this.backScr.switchToMe();
            this.imgSell = null;
            return;
         case 1:
         case 2:
            if (this.type != 0) {
               Canvas.inputDlg.setInfoIkb(T.number, 100, 1);
               return;
            }

            String var3;
            MoneyInfo var8;
            if ((var8 = (MoneyInfo)this.avs.elementAt(super.selected_)).smsTo.indexOf(T.aH) != -1) {
               var3 = Canvas.normalFont.replace(var8.smsTo, T.aI, GameMidlet.avatar.name);
               Canvas.startOKDlg(T.doYouWantExitIntoRegion, new IActionDoBuy1(this, var3));
            } else if (var8.smsTo.indexOf("napthe:") != -1) {
               var3 = var8.smsTo.substring(0, var8.smsTo.indexOf("napthe:") + "napthe:".length());
               var3 = Canvas.normalFont.replace(var8.smsTo, var3, "");
               String var10001 = var3;
               var3 = var8.info;
               TField[] var4;
               (var4 = new TField[2])[0] = new TField();
               var4[1] = new TField();
               var4[0].setIputType(0);
               var4[1].setIputType(1);
               InputFace.gI().setIputType(var4, var3, T.sendSmgFinish, new Command(T.finish, new IActionLoad(this, var10001, var4)));
               Canvas.currentFace = InputFace.gI();
            } else {
               if (var8.smsTo.indexOf("ServerNap:") == -1) {
                  Canvas.startWaitDlg();
                  GlobalService var10000 = GlobalService.gI();
                  String var9 = var8.smsContent;
                  var10000.createMessage((byte)-91);
                  var10000.writeUTF(var9);
                  var10000.sendMessage();
                  return;
               }

               var3 = var8.smsTo.substring(0, var8.smsTo.indexOf("ServerNap:") + "ServerNap:".length());
               var3 = Canvas.normalFont.replace(var8.smsTo, var3, "");
               AvatarService var6;
               (var6 = AvatarService.gI()).createMessage((byte)-76);
               var6.writeUTF(var3);
               var6.sendMessage();
               Canvas.startWaitDlg();
            }
            break;
         case 100:
            try {
               if (Canvas.inputDlg.getText().equals("")) {
                  return;
               }

               var1 = Integer.parseInt(Canvas.inputDlg.getText());
               FarmService.gI().doTransMoney(var1, super.selected_ == 0 ? 1 : 0);
               Canvas.startWaitDlg();
               return;
            } catch (Exception e) {
            }
      }

   }

   public final void initCmd() {
      this.cmdTrans = new Command(T.strName[0], 1);
      super.left = this.cmdTrans;
      this.cmdLoad = new Command(T.selectt, 2);
      super.center = this.cmdLoad;
      this.cmdClose = new Command(T.close, 0);
      super.right = this.cmdClose;
   }

   public MoneyScr() {
      new AvPosition(0, 1);
      this.xTrans = 0;
      this.dir = -1;
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      if (OnScreen.isOngame) {
         Canvas.paint.paintDefaultBg(var1);
         Canvas.paint.drawTextElements(var1, T.loadMoney.toUpperCase(), GameMidlet.avatar.money[0] + T.xu, GameMidlet.avatar.money[2] + T.gold);
      } else if (this.backScr != null) {
         this.backScr.paintMain(var1);
      }

      if (InputFace.me == null || Canvas.currentFace != InputFace.me) {
         if (!OnScreen.isOngame) {
            PaintPopup.gI().paint(var1);
            var1.translate(0, this.y + PaintPopup.hTab + AvMain.hDuBox);
            var1.setClip(this.x + 5, 0, this.w - 10, PaintPopup.gI().h - PaintPopup.hTab - 2 * AvMain.hDuBox);
         } else {
            var1.translate(0, this.y);
            var1.setClip(this.x + 5, 0, this.w - 10, this.h);
         }

         if (this.focusTap == 1) {
            int var2 = (this.h - PaintPopup.hTab + (AvMain.hDuBox << 1)) / 6;
            Canvas.fontChatB.drawString(var1, T.nameStr + GameMidlet.avatar.name, this.x + this.w / 2, var2 / 2, 2);
            if (!FarmScr.isNew) {
               Canvas.fontChatB.drawString(var1, T.youFirstFire + ": " + GameMidlet.avatar.strMoney, this.x + this.w / 2, var2 / 2 + var2, 2);
            }

            Canvas.fontChatB.drawString(var1, GameMidlet.avatar.money[2] + T.gold, this.x + this.w / 2, var2 / 2 + (var2 << 1), 2);
            if (FarmScr.isNew) {
               Canvas.fontChatB.drawString(var1, MapScr.strTkFarm(), this.x + this.w / 2, var2 / 2 + var2 * 3, 2);
            }
         } else {
            var1.translate(0, -CameraList.cmtoY);
            if (this.type == 0) {
               this.paintRichList(var1);
            } else {
               this.paintTransMoney(var1);
            }
         }

         if (Canvas.welcome == null || !Welcome.isPaintArrow) {
            super.paint(var1);
         }

         Canvas.paintPlus(var1);
      }

   }

   public final void setAvatarList(Vector var1) {
      this.initPos();
      this.avs = var1;
      this.setCamera();
      this.xTrans = 0;
   }

   private void setCamera() {
      if (this.avs != null) {
         this.avs.size();
         int var1 = this.avs.size() * this._hSmall__;
         int var2 = this.avs.size();
         if (LoadMap.TYPEMAP == 25) {
            var1 = this._hSmall__ << 1;
            var2 = 2;
         }

         Canvas.cameraList.setInfo(this.x, this.y + (!OnScreen.isOngame ? PaintPopup.hTab + AvMain.hDuBox : 0), this.w, this._hSmall__, this.w, var1, this.w, this.h - (PaintPopup.hTab + 2 * AvMain.hDuBox) - AvMain.hDuBox, var2);
      }

   }

   private void initPos() {
      if (OnScreen.isOngame) {
         this.w = Canvas.w + 8;
         this.h = Canvas.h - 25 - MyScreen.ITEM_HEIGHT + (AvMain.hDuBox << 1);
      } else {
         this.w = LoginScr.gI().wLogin;
         this.h = LoginScr.gI().hLogin;
      }

      this._hSmall__ = MyScreen.hText;
      this.x = Canvas.hw - this.w / 2;
   }

   private void paintTransMoney(Graphics var1) {
      for(int var2 = 0; var2 < 2; ++var2) {
         if (!super.isHide_ && var2 == super.selected_) {
            Canvas.paint.drawSelectedArea(var1, this.x + 3 * AvMain.hd, var2 * this._hSmall__ + 5, this.w - 6 * AvMain.hd, this._hSmall__);
         }

         Canvas.normalFont.drawString(var1, T.strTransMoney[var2], this.x + 10 + (super.selected_ == var2 ? this.xTrans : 0), var2 * this._hSmall__ + 5 + this._hSmall__ / 2 - AvMain.hNormal / 2, 0);
      }

   }

   private void paintRichList(Graphics var1) {
      int var2 = this.imgSell.getWidth() + 14;
      int var3 = this.avs.size();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         if (var4 == super.selected_ && !super.isHide_) {
            if (OnScreen.isOngame) {
               var1.setColor(14328855);
               var1.fillRect(this.x, var4 * this._hSmall__, this.w - 3 * AvMain.hd, this._hSmall__);
            } else {
               Canvas.paint.drawSelectedArea(var1, this.x + 6, var4 * this._hSmall__, this.w - 6 * AvMain.hd, this._hSmall__);
            }
         }

         var1.drawImage(this.imgSell, this.x + var2 / 2, var4 * this._hSmall__ + this._hSmall__ / 2, 3);
      }

      for(var4 = 0; var4 < var3; ++var4) {
         MoneyInfo var5 = (MoneyInfo)this.avs.elementAt(var4);
         var1.setClip(this.x + var2 - 3, CameraList.cmtoY, this.w - var2 - 2, this.h - (!OnScreen.isOngame ? PaintPopup.hTab + 2 * AvMain.hDuBox : 0));
         Canvas.normalFont.drawString(var1, var5.info, this.x + var2, var4 * this._hSmall__ + this._hSmall__ / 2 - AvMain.hNormal / 2, 0);
      }

   }

   public final void updateKey() {
      super.updateKey();
      if (!OnScreen.isOngame) {
         if (Canvas.keyPressed[4] || Canvas.keyPressed[6]) {
            this.setTab();
         }

         if (Canvas.isPointerClick && Canvas.isPointer(0, PaintPopup.gI().y, Canvas.w, PaintPopup.hTab)) {
            Canvas.isPointerClick = false;
            this.setTab();
         }
      }

   }

   private void setTab() {
      String var1;
      if (this.focusTap == 0) {
         this.focusTap = 1;
         super.left = null;
         var1 = T.strName[2];
      } else {
         if (this.type == 1) {
            var1 = T.strName[1];
         } else {
            var1 = T.strName[0];
         }

         this.focusTap = 0;
      }

      PaintPopup.gI().setNameAndFocus(var1, this.focusTap);
   }

   public final void keyPress(int var1) {
   }

   public final void update() {
      if (this.backScr != null) {
         this.backScr.update();
      }

      int var2;
      if (this.type == 0) {
         MoneyInfo var1 = (MoneyInfo)this.avs.elementAt(super.selected_);
         var2 = Canvas.normalFont.getWidth(var1.info);
      } else {
         var2 = Canvas.normalFont.getWidth(T.strTransMoney[super.selected_]);
      }

      if (var2 > this.w - 20) {
         this.xTrans += this.dir;
         if (this.xTrans <= -(var2 - (this.w - 30))) {
            this.dir = 1;
         }

         if (this.xTrans > 0) {
            this.dir = -1;
         }
      } else {
         this.xTrans = 0;
      }

      if (this.focusTap == 0) {
         if (LoadMap.TYPEMAP != 25) {
            super.left = this.cmdTrans;
            super.center = null;
         } else {
            super.left = null;
            super.center = this.cmdLoad;
         }
      } else {
         super.left = null;
         super.center = null;
      }

   }
}
