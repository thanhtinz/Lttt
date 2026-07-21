package avt;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class OptionScr extends MyScreen {
   public static OptionScr instance;
   private int point = 0;
   private int focus = 0;
   private int max = 5;
   public int[] mapFocus;
   public int volume = 0;
   private int xL;
   private int _hText_;
   private MyScreen lastScr;
   public static boolean isVirTualKey = false;
   public static boolean e = false;
   private boolean[] isPaint;

   public static OptionScr gI() {
      if (instance == null) {
         instance = new OptionScr();
      }

      return instance;
   }

   public final void switchToMe() {
      this.initSize();
      this.lastScr = Canvas.currentMyScreen;
      super.switchToMe();
      this.load();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            this.save(this.volume);
            this.lastScr.switchToMe();
         default:
      }
   }

   public OptionScr() {
      this.isPaint = new boolean[this.max];
   }

   public final void initSize() {
      super.left = new Command(T.complete, 0);
      this._hText_ = MyScreen.hText;
      this.xL = Canvas.h;
      int var1 = PaintPopup.hTab + (AvMain.hDuBox << 1);
      if (this.isPaint != null) {
         int var2;
         for(var2 = 0; var2 < this.isPaint.length; ++var2) {
            if (this.isPaint[var2]) {
               var1 += this._hText_;
            }
         }

         var2 = 176;
         if (Canvas.w < 176) {
            var2 = Canvas.w;
         }

         PaintPopup.gI().a(T.option, var2 * AvMain.hd, var1, 1);
         if (Canvas.currentMyScreen != this) {
            for(var1 = 0; var1 < 3; ++var1) {
               this.isPaint[var1] = true;
            }

            if (Canvas.E) {
               this.isPaint[3] = true;
            }

            this.mapFocus = new int[this.max];
         }
      }

   }

   public final void save(int var1) {
      this.volume = var1;
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      DataOutputStream var3 = new DataOutputStream(var2);

      try {
         var3.writeByte(var1);

         for(int var4 = 0; var4 < this.max; ++var4) {
            var3.writeByte(this.mapFocus[var4]);
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      try {
         CRes.saveRMS("avatarShowName", var2.toByteArray());
         var3.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      this.init();
      SoundManager.a.a(var1 / 10);
   }

   public final void load() {
      this.initSize();
      DataInputStream var1 = AvatarData.loadRMS("avatarShowName");
      isVirTualKey = false;
      if (var1 != null) {
         try {
            this.volume = var1.readByte();
            this.mapFocus = new int[this.max];

            for(int var2 = 0; var2 < this.max; ++var2) {
               this.mapFocus[var2] = var1.readByte();
               if (this.mapFocus[var2] > 1) {
                  this.mapFocus[var2] = 0;
               }
            }

            var1.close();
         } catch (Exception var3) {
            AvatarData.delErrorRms("avatarShowName");
         }

         this.init();
         SoundManager.a.a(this.volume / 10);
      }

   }

   private void init() {
      if (Canvas.E) {
         e = this.mapFocus[3] == 1;
      }

      Canvas.a();
   }

   public final void updateKey() {
      super.updateKey();
      if (Canvas.a(2)) {
         this.setMapFocus_(-1);
      } else if (Canvas.a(8)) {
         this.setMapFocus_(1);
      } else if (Canvas.a(4)) {
         this.setMapFocus(-1);
      } else if (Canvas.a(6)) {
         this.setMapFocus(1);
      }

      if (Canvas.isPointerClick && Canvas.isPointer(PaintPopup.gI().x, PaintPopup.gI().y, PaintPopup.gI().w, PaintPopup.gI().h)) {
         Canvas.isPointerClick = false;
         if (Canvas.isPointer(PaintPopup.gI().x, PaintPopup.gI().y, PaintPopup.gI().w, PaintPopup.gI().h)) {
            int var1;
            for(int var2 = var1 = (Canvas.py - (PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox)) / this._hText_; var2 >= 0; --var2) {
               if (!this.isPaint[var2]) {
                  ++var1;
               }
            }

            if (var1 == this.focus) {
               if (this.mapFocus[this.focus] == 1) {
                  this.setMapFocus(-1);
               } else {
                  this.setMapFocus(1);
               }
            }

            if (var1 >= this.max) {
               var1 = this.max - 1;
            }

            this.focus = var1;
         }
      }

   }

   private void setMapFocus_(int var1) {
      while(true) {
         this.focus += var1;
         if (this.focus < 0) {
            this.focus = this.max - 1;
         }

         if (this.focus >= this.max) {
            this.focus = 0;
         }

         if (this.isPaint[this.focus]) {
            return;
         }

         var1 /= CRes.abs(var1);
      }
   }

   private void setMapFocus(int var1) {
      if (this.focus == 2) {
         this.volume += var1 * 10;
         if (this.volume < 0) {
            this.volume = 100;
         }

         if (this.volume > 100) {
            this.volume = 0;
            return;
         }
      } else {
         if (this.mapFocus[this.focus] == 0) {
            this.mapFocus[this.focus] = 1;
            return;
         }

         this.mapFocus[this.focus] = 0;
      }

   }

   public final void update() {
      this.lastScr.update();
      if (this.xL != 0) {
         this.xL += -this.xL >> 1;
         if (this.xL < 0) {
            this.xL = 0;
         }
      }

   }

   public final void paint(Graphics var1) {
      this.lastScr.paintMain(var1);
      this.paintMain(var1);
      super.paint(var1);
   }

   public final void paintMain(Graphics var1) {
      var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
      var1.translate(0, this.xL);
      PaintPopup.gI().paint(var1);
      var1.translate(Canvas.hw - 65, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox);
      if (this.point >= 4) {
         this.point = 0;
      }

      int var3 = -AvMain.hNormal / 2 + this._hText_ / 2;
      int var4 = 0;

      for(int var5 = 0; var5 < this.max; ++var5) {
         if (this.isPaint[var5]) {
            Canvas.normalFont.drawString(var1, T.sms[var5][2], -50 * (AvMain.hd - 1), var4 + var3, 0);
            Canvas.normalFont.drawString(var1, T.sms[var5][this.mapFocus[var5]], 52 + 50 * AvMain.hd, var4 + var3 - 1, 2);
            byte var2 = 0;
            int var6;
            if ((var6 = Canvas.normalFont.getWidth(T.sms[var5][this.mapFocus[var5]]) + 10 + 15 * (Canvas.stypeInt + 1) + PaintPopup.b.frameWidth) < 25 * AvMain.hd) {
               var6 = 25 * AvMain.hd;
            }

            if (var5 == this.focus) {
               var2 = 1;
            }

            int var7 = var4 + var3 + AvMain.hNormal / 2 - PaintPopup.b.frameHeight / 2;
            PaintPopup.b.drawFrame(var2, 52 + 50 * AvMain.hd - var6 / 2, var7, 0, var1);
            PaintPopup.b.drawFrame(var2, 52 + 50 * AvMain.hd + var6 / 2 - PaintPopup.b.frameWidth, var7, 2, var1);
            var4 += this._hText_;
         }
      }

      Canvas.normalFont.drawString(var1, String.valueOf(this.volume), 52 + 50 * AvMain.hd, 2 * this._hText_ + var3, 2);
      ++this.point;
   }
}
