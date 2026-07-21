package avt;

import java.util.Vector;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import main.Canvas;
import main.GameMidlet;

public final class DialLuckyScr extends MyScreen {
   private static final String RMS_AUTO_DIAL = "adial258";
   private static DialLuckyScr me;
   private Image b;
   private Image c;
   private Image d;
   private Image e;
   private FrameImage imgFireWork;
   private int radius;
   private int degree;
   private int part;
   private int g;
   private int degreeKim;
   private int num;
   private int selectedNumber;
   private AvPosition posCenter;
   private boolean isTurn;
   private boolean isPaint;
   private boolean isable = false;
   private MyScreen lastScr;
   private short idPart;
   private Command cmdDial;
   private Command cmdWait;
   private Command cmdClose;
   private Command cmdMenu;
   private Vector listGift = new Vector();
   private long timePaint = 0L;
   private boolean[] isFireWork;
   private Vector listFireWork;
   private boolean isAutoDial;
   private int autoDialLimit;
   private int autoDialCount;
   private int autoDialDelay = 1400;
   private int tempAutoDialLimit;
   private int tempAutoDialDelay;
   private long nextAutoDialAt;
   private boolean isAutoRunning;
   private static final int AUTO_SPIN_SPEED = 18;
   private static final int CMD_MENU = 3;
   private static final int CMD_MAIN_OPEN_SETTINGS = 21;

   public static DialLuckyScr gI() {
      return me == null ? (me = new DialLuckyScr()) : me;
   }

   public final boolean isSpinning() {
      return this.g > 0 || super.center == this.cmdWait || this.isTurn;
   }

   public final String getSelectedDialItemText() {
      if (this.idPart <= 0) {
         return null;
      }

      Part p = AvatarData.getPart(this.idPart);
      if (p == null) {
         return null;
      }

      String name = AvatarData.getName(p);
      if (name == null || name.length() == 0) {
         return null;
      }

      return T.dialSpinningPrefix + name;
   }

   public final String getDialCountText() {
      if (this.autoDialCount <= 0 && !this.isAutoDial) {
         return T.dialCountPrefix + "0";
      }

      if (this.isAutoDial) {
         if (this.autoDialLimit <= 0) {
            return T.dialCountPrefix + this.autoDialCount;
         }

         return T.dialCountPrefix + this.autoDialCount + "/" + this.autoDialLimit;
      }

      return T.dialCountPrefix + this.autoDialCount;
   }

   public final String getSpinningGiftText() {
      if (!this.isSpinning()) {
         return null;
      }

      if (this.listGift == null || this.listGift.size() <= 0) {
         return null;
      }

      Gift gift = (Gift)this.listGift.elementAt(0);
      switch (gift.type) {
         case 1: {
            Part p = AvatarData.getPart(gift.idPart);
            if (p == null) return T.dialSpinningPrefix + T.dialSpinningItem;
            return T.dialSpinningPrefix + AvatarData.getName(p);
         }
         default:
            return null;
      }
   }

   public final boolean isAutoDialEnabled() {
      return this.isAutoDial;
   }

   public final void stopAutoDialFromUtility() {
      if (!this.isAutoDial && !this.isAutoRunning) {
         return;
      }

      this.isAutoDial = false;
      this.isAutoRunning = false;
      this.nextAutoDialAt = 0L;
      this.g = 0;
      this.refreshCommands();
      if (Canvas.currentMyScreen == this && this.lastScr != null) {
         this.finishCurrentRound(true);
      }
   }

   public final void switchToMe(MyScreen var1, short var2) {
      this.switchToMe(var1, var2, false);
   }

   public final void switchToMe(MyScreen var1, short var2, boolean var3) {
      this.lastScr = var1;
      this.idPart = var2;
      Canvas.keyHold[5] = false;
      this.autoDialCount = 0;
      this.nextAutoDialAt = 0L;
      if (var3) {
         this.isAutoDial = true;
         this.isAutoRunning = true;
         this.isable = true;
         this.g = AUTO_SPIN_SPEED;
      } else if (this.isAutoDial) {
        
         this.isAutoRunning = true;
      }
      this.refreshCommands();
      if (!var3) {
         super.switchToMe();
      }
      if (var3 || this.isAutoDial) {
         this.tryStartAutoDial();
      }
   }

   public final void openSettingsFromShop(MyScreen var1, short var2) {
      this.lastScr = var1;
      this.idPart = var2;
      this.tempAutoDialLimit = this.autoDialLimit;
      this.tempAutoDialDelay = this.autoDialDelay;
      this.openSettingsForm();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.isable = true;
            return;
         case 1:
            return;
         case 2:
            this.finishCurrentRound(true);
            return;
         case 3:
            this.openMainMenu();
            return;
         case CMD_MAIN_OPEN_SETTINGS:
            this.openSettingsForm();
            return;
         default:
      }
   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case CMD_MAIN_OPEN_SETTINGS:
            this.openSettingsForm();
            return;
         default:
      }
   }

   public final void commandActionPointer(int var1) {
      this.commandActionPointer(var1, -1);
   }

   public DialLuckyScr() {
      FilePack.b(T.ax);
      this.b = FilePack.getImage("c");
      this.d = FilePack.getImage("sq");
      this.e = FilePack.getImage("q");
      this.imgFireWork = FrameImage.init("st", 11 * AvMain.hd, 11 * AvMain.hd);
      this.c = FilePack.getImage("cb");
      FilePack.reset();
      if (Canvas.w < 200) {
         this.radius = 80;
      } else {
         this.radius = 90;
      }

      this.posCenter = new AvPosition(Canvas.w, Canvas.hh);
      this.part = 30;
      this.num = 360 / this.part;
      this.cmdDial = new Command(T.Dial, 0);
      this.cmdWait = new Command(T.pleaseWait, 1);
      this.cmdClose = new Command(T.close, 2);
      this.cmdMenu = new Command(T.menu, 3);
      super.center = this.cmdDial;
      super.left = this.cmdMenu;
      super.right = this.cmdClose;
      this.degreeKim = 90;
      this.isFireWork = new boolean[3];
      this.listFireWork = new Vector();
      this.loadPersistedAutoDialSettings();
   }

   private void persistAutoDialSettings() {
      try {
         CRes.a(RMS_AUTO_DIAL, this.autoDialLimit + "\n" + this.autoDialDelay);
      } catch (Throwable t) {
      }
   }

   private void loadPersistedAutoDialSettings() {
      try {
         String s = CRes.b(RMS_AUTO_DIAL);
         if (s == null) {
            return;
         }

         int p = s.indexOf('\n');
         if (p < 0) {
            return;
         }

         int limit = Integer.parseInt(s.substring(0, p).trim());
         int delay = Integer.parseInt(s.substring(p + 1).trim());
         if (limit < 0 || delay < 1) {
            return;
         }

         this.autoDialLimit = limit;
         this.autoDialDelay = delay;
      } catch (Throwable t) {
      }
   }

   private void refreshCommands() {
      super.left = this.cmdMenu;
      super.right = this.cmdClose;
   }

   private void openMainMenu() {
      Vector var1 = new Vector();
      var1.addElement(new Command(T.dialLimitPrefix + this.getLimitText(), CMD_MAIN_OPEN_SETTINGS, this));
      var1.addElement(new Command(T.option, CMD_MAIN_OPEN_SETTINGS, this));
      Menu.gI().startAt(var1, -1);
   }

   

   private void openSettingsForm() {
      final Form var1 = new Form(T.option);
      final ChoiceGroup var2 = new ChoiceGroup(T.utilMineOptions, 2);
      var2.append(T.dialDeleteJunk, null);
      var2.setSelectedIndex(0, true);
      final TextField var3 = new TextField(T.dialLimitField, String.valueOf(this.tempAutoDialLimit), 10, 2);
      final TextField var4 = new TextField(T.dialDelayField, String.valueOf(this.tempAutoDialDelay), 10, 2);
      var1.append(var2);
      var1.append(var3);
      var1.append(var4);
      var1.append(T.dialDelayNote);
      final javax.microedition.lcdui.Command var5 = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
      final javax.microedition.lcdui.Command var6 = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
      var1.addCommand(var5);
      var1.addCommand(var6);
      var1.setCommandListener(new CommandListener() {
            public void commandAction(javax.microedition.lcdui.Command var1x, Displayable var2x) {
               if (var1x == var5) {
                  try {
                     int var3x = Integer.parseInt(var3.getString());
                     int var4x = Integer.parseInt(var4.getString());
                     if (var3x < 0 || var4x < 0) {
                        Canvas.startOKDlg(T.dialInvalidValue);
                        return;
                     }

                     DialLuckyScr.this.autoDialLimit = var3x;
                     DialLuckyScr.this.autoDialDelay = var4x;
                     DialLuckyScr.this.tempAutoDialLimit = var3x;
                     DialLuckyScr.this.tempAutoDialDelay = var4x;
                     DialLuckyScr.this.autoDialCount = 0;
                     DialLuckyScr.this.nextAutoDialAt = 0L;
                     DialLuckyScr.this.persistAutoDialSettings();
                     Canvas.startOKDlg(DialLuckyScr.this.getAutoStatusText());
                  } catch (Exception var5x) {
                     Canvas.startOKDlg(T.dialInvalidValue);
                     return;
                  }
               }

               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
            }
         });
      Display.getDisplay(GameMidlet.instance).setCurrent(var1);
   }

   private String getLimitText() {
      return this.autoDialLimit == 0 ? T.dialLimitOff : String.valueOf(this.autoDialLimit);
   }

   private String getAutoStatusText() {
      if (this.autoDialLimit == 0) {
         return T.dialAutoLimitOff;
      } else {
         return T.dialAutoLimitOnPrefix + this.autoDialLimit + T.dialAutoLimitOnSuffix;
      }
   }

   private void finishCurrentRound(boolean var1) {
      this.isPaint = false;
      super.center = this.cmdDial;

      for(int var2 = 0; var2 < 3; ++var2) {
         this.isFireWork[var2] = false;
      }

      this.listFireWork.removeAllElements();
      setItemBay(this.listGift, GameMidlet.avatar, 0);
      if (var1) {
         this.lastScr.switchToMe();
      }
   }

   private boolean canStartAutoDial() {
      if (!this.isAutoDial || !this.isAutoRunning) {
         return false;
      } else if (this.autoDialLimit != 0 && this.autoDialCount >= this.autoDialLimit) {
         this.isAutoDial = false;
         this.isAutoRunning = false;
         Canvas.startOKDlg(T.dialLimitReached);
         this.refreshCommands();
         return false;
      } else if (System.currentTimeMillis() < this.nextAutoDialAt) {
         return false;
      } else {
         return true;
      }
   }

   private boolean hasForeverGift(Vector var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Gift var3 = (Gift)var1.elementAt(var2);
         if (var3.type == 1 && var3.expire != null && var3.expire.indexOf(T.forever) >= 0) {
            return true;
         }
      }

      return false;
   }

   private void tryStartAutoDial() {
      if (this.canStartAutoDial() && !this.isTurn && !this.isPaint && super.center == this.cmdDial) {
         if (this.selectedNumber <= 90) {
            this.selectedNumber = 180;
         }
         GlobalService.gI().doDialLucky(this.idPart, this.selectedNumber - 90);
         super.center = this.cmdWait;
      }
   }

   public final void tickAutoDialBackground() {
      if (!this.isAutoDial || !this.isAutoRunning) {
         return;
      }
      if (this.idPart <= 0) {
         return;
      }

      if (this.isTurn) {
         this.isTurn = false;
         this.isable = false;
         this.isPaint = false;
         super.center = this.cmdDial;

         for(int i = 0; i < 3; ++i) {
            this.isFireWork[i] = false;
         }

         this.listFireWork.removeAllElements();
         setItemBay(this.listGift, GameMidlet.avatar, 0);
         this.nextAutoDialAt = System.currentTimeMillis() + (long)this.autoDialDelay;
      }

      this.tryStartAutoDial();
   }

   private static void setItemBay(Vector var0, Avatar var1, int var2) {
      var2 = var2;

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         Gift var4 = (Gift)var0.elementAt(var3);
         String var5 = "";
         switch (var4.type) {
            case 1:
               Part var6 = AvatarData.getPart(var4.idPart);
               ImageInfo var7 = AvatarData.listImgInfo[var6.idIcon];
               Canvas.addFlyText(0, var1.x, var1.y - 50, -1, CRes.createRGBImage(var7.x0 * AvMain.hd, var7.y0 * AvMain.hd, var7.w * AvMain.hd, var7.h * AvMain.hd, AvatarData.getBigImgInfo(var7.bigID).img), var2);
               break;
            case 2:
               var5 = "+" + var4.xu + T.xu;
               var1.setMoney(var1.money[0] + var4.xu);
               var2 += 20;
               break;
            case 3:
               var5 = "+" + var4.xp + " xp";
               var1.setExp(var1.exp + var4.xp);
               var2 += 20;
               break;
            case 4:
               var5 = "+" + var4.luong + T.gold;
               int[] var10000 = var1.money;
               var10000[2] += var4.luong;
               var2 += 20;
         }

         if (!var5.equals("")) {
            Canvas.addFlyTextSmall(var5, var1.x, var1.y - 50, -1, 1, var2);
         }
      }

   }

   public final void update() {
      this.lastScr.update();
      if (this.isAutoDial && this.isAutoRunning) {
         this.g = AUTO_SPIN_SPEED;
         this.tryStartAutoDial();
      }

      int var1;
      int var2;
      if (this.g > 0) {
         this.degree -= this.g;
         if (this.degree < 0) {
            this.degree += 7200;
         }

         if (!this.isAutoDial || !this.isAutoRunning) {
            if (this.g < 10) {
               if (this.degree / 20 % 30 == 0) {
                  this.g = 0;
               }
            } else {
               --this.g;
            }
         }

         if (Canvas.gameTick % 8 == 4) {
            var1 = CRes.rnd(this.num);
            if ((var2 = this.degree / 20 + var1 * this.part) > 360) {
               var2 -= 360;
            }

            var2 = CRes.fixangle(var2);
            var1 = this.radius * CRes.cos(var2) >> 10;
            var2 = -(this.radius * CRes.sin(var2)) >> 10;
            this.addFire(this.posCenter.x + var1, this.posCenter.y + var2);
         }
      }

      if (this.isTurn) {
         DialLuckyScr var6 = this;
         this.isTurn = false;
         this.isable = false;
         if (this.isAutoDial) {
            this.isPaint = false;
            super.center = this.cmdDial;

            for(var2 = 0; var2 < 3; ++var2) {
               this.isFireWork[var2] = false;
            }

            this.listFireWork.removeAllElements();
            setItemBay(this.listGift, GameMidlet.avatar, 0);
            this.nextAutoDialAt = System.currentTimeMillis() + (long)this.autoDialDelay;
            this.tryStartAutoDial();
         } else {
            this.isPaint = true;
            this.timePaint = System.currentTimeMillis() / 100L;

            for(var2 = 0; var2 < var6.listGift.size(); ++var2) {
               Gift var3 = (Gift)var6.listGift.elementAt(var2);
               int var4;
               if (var2 == 0) {
                  var4 = 150;
               } else if (var2 == 1) {
                  var4 = 180;
               } else {
                  var4 = 210;
               }

               var4 = CRes.fixangle(var4);
               int var5 = var6.radius * CRes.cos(var4) >> 10;
               var4 = -(var6.radius * CRes.sin(var4)) >> 10;
               var3.x = var6.posCenter.x + var5;
               var3.y = var6.posCenter.y + var4;
            }
         }
      }

      if (super.center == this.cmdWait) {
         var1 = 0;

         for(var2 = 0; var2 < this.isFireWork.length; ++var2) {
            if (this.isFireWork[var2]) {
               ++var1;
            }
         }

         if (var1 == 3) {
            super.center = this.cmdClose;
         }
      }

      if (super.center == this.cmdClose && this.isAutoDial && this.isAutoRunning && !this.isTurn) {
         if (this.nextAutoDialAt == 0L) {
            this.nextAutoDialAt = System.currentTimeMillis() + (long)this.autoDialDelay;
         }

         if (System.currentTimeMillis() >= this.nextAutoDialAt) {
            this.nextAutoDialAt = 0L;
            this.finishCurrentRound(false);
            this.tryStartAutoDial();
         }
      }

      for(var1 = 0; var1 < this.listFireWork.size(); ++var1) {
         Point var7;
         Point var10000 = var7 = (Point)this.listFireWork.elementAt(var1);
         var10000.x += var7.g;
         if (var7.g > 1 || var7.g < -1) {
            var7.g -= var7.g / CRes.abs(var7.g);
         }

         var7.y += var7.h;
         ++var7.h;
         ++var7.color;
         if (var7.color > 20) {
            this.listFireWork.removeElement(var7);
         }
      }

      if (this.isPaint) {
         for(var1 = 0; var1 < this.listGift.size(); ++var1) {
            if (!this.isFireWork[var1] && System.currentTimeMillis() / 100L - this.timePaint > (long)((var1 + 1) * 5)) {
               this.isFireWork[var1] = true;
               Gift var8 = (Gift)this.listGift.elementAt(var1);
               this.addFire(var8.x, var8.y);
            }
         }
      }

   }

   private void addFire(int var1, int var2) {
      for(int var3 = 0; var3 < 10; ++var3) {
         byte var4 = 1;
         if (var3 % 2 == 0) {
            var4 = -1;
         }

         Point var5;
         (var5 = new Point(var1, var2)).color = 0;
         var5.g = var4 * (CRes.rnd(80) / 10);
         var5.h = -CRes.rnd(70) / 10;
         this.listFireWork.addElement(var5);
      }

   }

   public final void updateKey() {
      if (this.isAutoDial && this.isAutoRunning && this.lastScr != null) {
         this.lastScr.updateKey();
         super.updateKey();
         return;
      }

      if (!this.isPaint) {
         if (Canvas.paint.getDisplayValue() == 1) {
            if (Canvas.isPointerDown) {
               Canvas.keyHold[5] = true;
            }

            if (Canvas.isPointerRelease) {
               Canvas.keyReleased[5] = true;
            }
         }

         if (Canvas.keyHold[5] && !this.isTurn && this.isable) {
            if (this.degreeKim < 270) {
               this.degreeKim += 3;
            }
         } else if (this.degreeKim > 90) {
            this.degreeKim -= 3;
         }

         if (Canvas.keyReleased[5]) {
            if (!this.isAutoRunning && this.degreeKim > 90 && !this.isTurn && this.isable) {
               this.selectedNumber = this.degreeKim;
               GlobalService.gI().doDialLucky(this.idPart, this.selectedNumber - 90);
               Canvas.startWaitDlg();
            }

            Canvas.keyReleased[5] = false;
         }
      }

      super.updateKey();
   }

   public final void onStart(int var1, int var2, Vector var3) {
      if (var1 != GameMidlet.avatar.IDDB) {
         Avatar var4;
         if ((var4 = LoadMap.getAvatar(var1)) != null) {
            setItemBay(var3, var4, var2 + 100 + 20);
            return;
         }
      } else {
         this.listGift = var3;
         super.center = this.cmdWait;
         this.g = 100 + (this.selectedNumber - 90);
         this.isTurn = true;
         ++this.autoDialCount;
         if (this.isAutoDial && this.isAutoRunning) {
            if (this.hasForeverGift(var3)) {
               this.isAutoDial = false;
               this.isAutoRunning = false;
               this.nextAutoDialAt = 0L;
               this.g = 0;
               this.refreshCommands();
               Canvas.startOKDlg(T.dialForeverItemStop);
            }
         }
         Canvas.endDlg();
      }

   }

   public final void paint(Graphics var1) {
      this.lastScr.paintMain(var1);
      if (this.isAutoDial && this.isAutoRunning) {
         super.paint(var1);
         return;
      }

      Canvas.resetTrans(var1);
      int var2 = this.degree / 20;

      int var3;
      int var4;
      int var5;
      int var6;
      int var7;
      for(var3 = 0; var3 < this.num; ++var3) {
         if ((var4 = var2 + var3 * this.part) > 360) {
            var4 -= 360;
         }

         if (var4 >= 82 && var4 <= 278) {
            var5 = CRes.fixangle(var4);
            var6 = this.radius * CRes.cos(var5) >> 10;
            var7 = -(this.radius * CRes.sin(var5)) >> 10;
            var1.drawImage(this.c, this.posCenter.x + var6, this.posCenter.y + var7, 3);
         }
      }

      if (this.isPaint) {
         Graphics var12 = var1;
         DialLuckyScr var11 = this;

         for(var5 = 0; var5 < var11.listGift.size(); ++var5) {
            if (System.currentTimeMillis() / 100L - var11.timePaint > (long)((var5 + 1) * 5)) {
               Gift var13;
               switch ((var13 = (Gift)var11.listGift.elementAt(var5)).type) {
                  case 1:
                     AvatarData.getPart(var13.idPart).paint(var12, var13.x, var13.y, 3);
                     Canvas.borderFont.drawString(var12, var13.expire, var13.x - 17, var13.y - 7, 1);
                     break;
                  case 2:
                     Canvas.borderFont.drawString(var12, T.xu, var13.x, var13.y - AvMain.hBorder / 2, 2);
                     Canvas.borderFont.drawString(var12, String.valueOf(var13.xu), var13.x - 17, var13.y - 8, 1);
                     break;
                  case 3:
                     Canvas.borderFont.drawString(var12, "xp", var13.x, var13.y - AvMain.hBorder / 2, 2);
                     Canvas.borderFont.drawString(var12, String.valueOf(var13.xp), var13.x - 17, var13.y - 8, 1);
                     break;
                  case 4:
                     Canvas.borderFont.drawString(var12, T.gold, var13.x, var13.y - AvMain.hBorder / 2, 2);
                     Canvas.borderFont.drawString(var12, String.valueOf(var13.luong), var13.x - 17, var13.y - 8, 1);
               }
            }
         }
      }

      var3 = 0;

      for(var4 = 0; var4 < this.num; ++var4) {
         if ((var5 = var2 + var4 * this.part) > 360) {
            var5 -= 360;
         }

         if (var5 >= 82 && var5 <= 278) {
            var6 = CRes.fixangle(var5);
            var7 = this.radius * CRes.cos(var6) >> 10;
            var6 = -(this.radius * CRes.sin(var6)) >> 10;
            long var9 = System.currentTimeMillis() / 100L - this.timePaint;
            if (!this.isPaint || var5 < 150 || var5 > 210 || var9 <= (long)((var3 + 1) * 5) && var9 > (long)((var3 + 1) * 5 - 5)) {
               var1.drawImage(this.e, this.posCenter.x + var7, this.posCenter.y + var6, 3);
            } else {
               ++var3;
            }

            var1.drawImage(this.b, this.posCenter.x + var7, this.posCenter.y + var6, 3);
         }
      }

      var1.drawRegion(this.d, 0, 0, 64, 62, 0, this.posCenter.x, this.posCenter.y, 40);
      var1.drawRegion(this.d, 0, 0, 64, 62, 1, this.posCenter.x, this.posCenter.y, 24);
      var5 = CRes.fixangle(this.degreeKim);
      var6 = (this.radius / 3 + 2) * CRes.cos(var5) >> 10;
      var7 = -((this.radius / 3 + 2) * CRes.sin(var5)) >> 10;
      if ((var2 = this.degreeKim + 90) > 360) {
         var2 -= 360;
      }

      var2 = CRes.fixangle(var2);
      var5 = 6 * CRes.cos(var2) >> 10;
      var2 = -(6 * CRes.sin(var2)) >> 10;
      int var8;
      if ((var8 = this.degreeKim - 90) < 0) {
         var8 += 360;
      }

      var8 = CRes.fixangle(var8);
      int var14 = 6 * CRes.cos(var8) >> 10;
      var8 = -(6 * CRes.sin(var8)) >> 10;
      var1.setColor(14483456);
      var1.fillTriangle(this.posCenter.x + var6, this.posCenter.y + var7, this.posCenter.x + var5, this.posCenter.y + var2, this.posCenter.x + var14, this.posCenter.y + var8);
      var1.fillRoundRect(this.posCenter.x - 6, this.posCenter.y - 6, 12, 12, 12, 12);
      if (this.isPaint || this.g > 0) {
         this.paintFireWork(var1);
      }

      super.paint(var1);
   }

   private void paintFireWork(Graphics var1) {
      for(int var2 = 0; var2 < this.listFireWork.size(); ++var2) {
         Point var3 = (Point)this.listFireWork.elementAt(var2);
         this.imgFireWork.drawFrame(var3.color / 5, var3.x, var3.y, 0, 3, var1);
      }

   }
}
