package avt;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class ServerListScr extends MyScreen {
   public static ServerListScr me;
   public int indexSV;
   private Image img;
   private boolean isSelected = false;
   private static int cmtoY;
   private static int cmy;
   private static int cmdy;
   private static int cmvy;
   private static int cmyLim;
   private static int hDis;
   private static int x;
   private static int y;
   private int indexUSer = 0;
   private long timeDelay;
   private int vY;
   private boolean transY;
   private int pa;
   private long count;
   private long timePoint;
   private int t;

   public static ServerListScr gI() {
      return me == null ? (me = new ServerListScr()) : me;
   }

   public final void switchToMe() {
      AvatarData.ensureServerArraySize();
      if (!AvatarData.hasServerList(AvatarData.SERVER_INDEX)) {
         AvatarData.refreshServerListFromHost();
      }
      super.switchToMe();
      this.setupPopupBox();
      this.indexUSer = 0;
      if (super.center == null) {
         this.initCmd();
      }

      this.chans();
   }

   public ServerListScr() {
      FilePack.init(T.av);
      this.img = FilePack.getImage("tp");
      FilePack.reset();
      this.initCmd();
      CRes.loadSavedData();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            if (this.isSelected && super.selected_ > 0) {
               Canvas.startWaitCancelDlg(T.logging);
               LoginScr.gI().timeOut = System.currentTimeMillis();
               LoginScr.gI().login();
               return;
            } else {
               if (!this.isSelected) {
                  this.isSelected = true;
                  this.setupPopupBox();
                  super.selected_ = 1 + CRes.rnd(GameMidlet.nameSV[AvatarData.SERVER_INDEX][this.indexSV].length - 1);
                  this.chans();
                  return;
               }

               this.isSelected = false;
               return;
            }
         case 1:
            this.onGetText();
            return;
         case 2:
            this.isSelected = false;
            this.indexSV = 0;
            super.selected_ = 0;
            LoginScr.gI().switchToMe();
         default:
      }
   }

   public final void initCmd() {
      if (T.selectt != null) {
         if (Canvas.stypeInt == 0) {
            super.center = new Command(T.selectt, 0);
         }

         if (GameMidlet.PROVIDER == 0) {
            super.left = new Command(T.updateList, 1);
         }

         super.right = new Command(T.back, 2);
      }

   }

   private void onGetText() {
      while(true) {
         Canvas.startWaitCancelDlg(T.pleaseWait);
         if (this.indexUSer >= GameMidlet.linkGetHost[AvatarData.SERVER_INDEX].length) {
            Canvas.startOKDlg(T.canNotConnect);
            this.indexUSer = 0;
            return;
         }

         String var1;
         if ((var1 = GameMidlet.createhttpconnect(GameMidlet.linkGetHost[AvatarData.SERVER_INDEX][this.indexUSer])) != null) {
            AvatarData.ensureServerArraySize();
            AvatarData.applyServerListText(var1, AvatarData.SERVER_INDEX);
            AvatarData.saveServerList();
            Canvas.endDlg();
            this.setupPopupBox();
            return;
         }

         ++this.indexUSer;
      }
   }

   public final void setupPopupBox() {
      if (Canvas.stypeInt > 0) {
         super.isHide_ = true;
      }

      int var1 = 176;
      if (176 > Canvas.w) {
         var1 = Canvas.w;
      }

      PaintPopup.gI().setup(T.cloth, var1 * AvMain.hd, MyScreen.hText * 6, 1);
      x = PaintPopup.gI().x + 4;
      y = PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox;
      hDis = PaintPopup.gI().h - (PaintPopup.hTab + (AvMain.hDuBox << 1));
      String[][] var2 = GameMidlet.nameSV[AvatarData.SERVER_INDEX];
      if (var2 == null) {
         var2 = new String[0][];
      }

      cmyLim = var2.length * MyScreen.hText + (this.isSelected && this.indexSV >= 0 && this.indexSV < var2.length ? var2[this.indexSV].length * MyScreen.hText : 0) - hDis;
      cmtoY = 0;
      cmy = 0;
      if (cmyLim < 0) {
         cmyLim = 0;
      }

   }

   public final void update() {
      if (this.vY != 0) {
         if (cmy < 0 || cmy > cmyLim) {
            this.vY -= this.vY / 4;
            cmy += this.vY / 20;
            if (this.vY / 10 <= 1) {
               this.vY = 0;
            }
         }

         if (cmy < 0) {
            if (cmy < -hDis / 2) {
               cmy = -hDis / 2;
               cmtoY = 0;
               this.vY = 0;
            }
         } else if (cmy > cmyLim) {
            if (cmy < cmyLim + hDis / 2) {
               cmy = cmyLim + hDis / 2;
               cmtoY = cmyLim;
               this.vY = 0;
            }
         } else {
            cmy += this.vY / 10;
         }

         cmtoY = cmy;
         this.vY -= this.vY / 10;
         if (this.vY / 10 == 0) {
            this.vY = 0;
         }
      } else if (cmy < 0) {
         cmtoY = 0;
      } else if (cmy > cmyLim) {
         cmtoY = cmyLim;
      }

      if (cmy != cmtoY) {
         cmvy = cmtoY - cmy << 2;
         cmdy += cmvy;
         cmy += cmdy >> 4;
         cmdy &= 15;
      }

      Canvas.loadMap.update();
   }

   private void setIndex(int var1) {
      this.indexSV = var1;
      if (this.indexSV >= GameMidlet.nameSV[AvatarData.SERVER_INDEX].length) {
         this.indexSV = 0;
      }

      if (this.indexSV < 0) {
         this.indexSV = GameMidlet.nameSV[AvatarData.SERVER_INDEX].length - 1;
      }

   }

   public final void setSelected(int var1, boolean var2) {
      super.selected_ = var1;
      if (super.selected_ >= GameMidlet.nameSV[AvatarData.SERVER_INDEX][this.indexSV].length || super.selected_ <= 0) {
         super.selected_ = 0;
         if (var2) {
            this.isSelected = false;
            this.setupPopupBox();
         }
      }

   }

   public final void updateKey() {
      ++this.count;
      boolean var1 = false;
      if (Canvas.isKeyPressed(8)) {
         var1 = true;
         if (!this.isSelected) {
            this.setIndex(this.indexSV + 1);
         } else {
            this.setSelected(super.selected_ + 1, true);
         }
      } else if (Canvas.isKeyPressed(2)) {
         var1 = true;
         if (!this.isSelected) {
            this.setIndex(this.indexSV - 1);
         } else {
            this.setSelected(super.selected_ - 1, true);
         }
      }

      if (Canvas.isPointerClick && Canvas.isPointerInRect(x, y, PaintPopup.gI().w, hDis)) {
         Canvas.isPointerClick = false;
         this.pa = cmy;
         this.transY = true;
         this.timeDelay = System.currentTimeMillis() / 10L;
      }

      if (this.transY) {
         long var2 = System.currentTimeMillis() / 10L - this.timeDelay;
         int var4 = Canvas.dy();
         int var5;
         if (Canvas.isPointerDown) {
            if (Canvas.gameTick % 3 == 0) {
               this.t = Canvas.py;
               this.timePoint = this.count;
            }

            this.vY = 0;
            var5 = (cmtoY + Canvas.py - y) / MyScreen.hText;
            if (this.isSelected) {
               super.selected_ = var5 - this.indexSV;
            } else if (var5 >= 0 && var5 < GameMidlet.nameSV[AvatarData.SERVER_INDEX].length) {
               this.indexSV = var5;
            }

            if (CRes.abs(var4) >= 20 * AvMain.hd) {
               super.isHide_ = true;
            } else if (var2 > 10L && var2 < 20L) {
               super.isHide_ = false;
            }

            if ((cmtoY = this.pa + var4) < 0 || cmtoY > cmyLim) {
               cmtoY = this.pa + var4 / 2;
            }

            cmy = cmtoY;
         }

         if (Canvas.isPointerRelease && Canvas.isPointerInRect(x, y, PaintPopup.gI().w, hDis)) {
            var5 = (int)(this.count - this.timePoint);
            int var6;
            if (CRes.abs(var6 = this.t - Canvas.py) > 40 && var5 < 10 && cmtoY > 0 && cmtoY < cmyLim) {
               this.vY = var6 / var5 * 10;
            }

            this.timePoint = -1L;
            if (Math.abs(var4) < 20 * AvMain.hd) {
               if (var2 <= 10L) {
                  super.isHide_ = false;
               }

               if (!super.isHide_) {
                  int var7 = (cmtoY + Canvas.py - y) / MyScreen.hText;
                  if (this.isSelected) {
                     if (var7 - this.indexSV > 0 && var7 - this.indexSV < GameMidlet.nameSV[AvatarData.SERVER_INDEX][this.indexSV].length) {
                        super.selected_ = var7 - this.indexSV;
                        this.commandTab(0, -1);
                     } else {
                        if (var7 - this.indexSV <= 0) {
                           this.isSelected = false;
                           super.selected_ = 0;
                           this.indexSV = var7;
                           var1 = true;
                        }

                        if (var7 >= GameMidlet.nameSV[AvatarData.SERVER_INDEX][this.indexSV].length - this.indexSV && var7 < GameMidlet.nameSV[AvatarData.SERVER_INDEX][this.indexSV].length - 1 + GameMidlet.nameSV[AvatarData.SERVER_INDEX].length) {
                           this.isSelected = false;
                           super.selected_ = 0;
                           this.indexSV = var7 - GameMidlet.nameSV[AvatarData.SERVER_INDEX][this.indexSV].length + 1;
                           var1 = true;
                        }
                     }
                  } else if (var7 >= 0 && var7 < GameMidlet.nameSV[AvatarData.SERVER_INDEX].length) {
                     this.indexSV = var7;
                     this.commandTab(0, -1);
                  }
               }
            }
         }
      }

      if (Canvas.isPointerRelease) {
         this.transY = false;
      }

      if (var1) {
         this.chans();
      }

      super.updateKey();
   }

   private void chans() {
      if ((cmtoY = (this.indexSV + (this.isSelected ? super.selected_ : 0)) * MyScreen.hText - hDis / 2 + MyScreen.hText / 2) < 0) {
         cmtoY = 0;
      }

      if (cmtoY > cmyLim) {
         cmtoY = cmyLim;
      }

   }

   public final void paint(Graphics var1) {
      Canvas.loadMap.paint(var1);
      Canvas.loadMap.paintBackGround(var1);
      Canvas.resetTrans(var1);
      PaintPopup.gI().paint(var1);
      if (GameMidlet.avatar != null && !GameMidlet.avatar.name.equals("")) {
         Canvas.borderFont.drawString(var1, T.StoreEmtpy + ", " + GameMidlet.avatar.name, PaintPopup.gI().x + PaintPopup.gI().w / 2, PaintPopup.gI().y - AvMain.hBorder, 2);
      }

      var1.translate(x, y);
      var1.setClip(0, 0, PaintPopup.gI().w - 9, PaintPopup.gI().h - (PaintPopup.hTab + (AvMain.hDuBox << 1)));
      var1.translate(0, -cmy);
      if (!super.isHide_) {
         Canvas.paint.drawSelectedArea(var1, 2 * AvMain.hd, this.indexSV * MyScreen.hText + (this.isSelected ? super.selected_ * MyScreen.hText : 0), PaintPopup.gI().w - 8 - 4 * AvMain.hd, MyScreen.hText);
      }

      int var2 = (MyScreen.hText - AvMain.hNormal) / 2;
      String[][] var6 = GameMidlet.nameSV[AvatarData.SERVER_INDEX];
      if (var6 == null) {
         var6 = new String[0][];
      }

      for(int var3 = 0; var3 < var6.length; ++var3) {
         Canvas.normalFont.drawString(var1, var6[var3][0], 24 * AvMain.hd, var2, 0);
         PaintPopup.imgArrowUp.drawFrame(0, 14 * AvMain.hd, var2 + AvMain.hNormal / 2, 5, 3, var1);
         var2 += MyScreen.hText;
         if (this.isSelected && this.indexSV == var3) {
            for(int var4 = 1; var4 < var6[var3].length; ++var4) {
               Canvas.normalFont.drawString(var1, var6[var3][var4], 36 * AvMain.hd, var2, 0);
               var1.drawImage(this.img, 24 * AvMain.hd, var2 + AvMain.hNormal / 2, 3);
               var2 += MyScreen.hText;
            }
         }
      }

      super.paint(var1);
      Canvas.paintPlus(var1);
   }
}
