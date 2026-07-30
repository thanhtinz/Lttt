package avt;

import java.io.ByteArrayInputStream;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import main.Canvas;

public final class SoundManager {
   private int volume;
   private Player player;
   public static SoundManager instance = new SoundManager();
   private Vector sound;
   private Vector name;
   int isPlaying = -1;

   public final void onRequestOpenSound(String var1, byte var2) {
      if (this.isPlaying != 0) {
         class_bl var4 = new class_bl(this, var2);
         if (this.isPlaying == 1) {
            var4.perform();
         } else {
            Vector var3 = new Vector();
            if (MapScr.isWedding) {
               OptionScr.gI().save(OptionScr.gI().volume / 10);
               var4.perform();
            } else {
               var3.addElement(new Command(T.lister[1], new class_bj(this, var4)));
               var3.addElement(new Command(T.lister[2], new class_bh(this, var4)));
               var3.addElement(new Command(T.no, new class_bw(this)));
               var3.addElement(new Command(T.lister[0], new class_bt(this, var4)));
               Canvas.setInfoC(var1, var3);
            }
         }
      }

   }

   public final int setSound(String var1) {
      if (this.name == null) {
         this.name = new Vector();
      }

      for(int var2 = 0; var2 < this.name.size(); ++var2) {
         if (((String)this.name.elementAt(var2)).equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public final void onSoundData(byte[] var1, byte var2) {
      if (this.sound == null) {
         this.sound = new Vector();
         this.name = new Vector();
      }

      try {
         this.name.addElement("" + var2);
         this.sound.addElement(var1);
         instance.playSoundData(var1);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public final void stop() {
      if (this.player != null) {
         if (this.player.getState() == 400) {
            try {
               this.player.stop();
            } catch (MediaException var2) {
               var2.printStackTrace();
            }
         }

         this.player.close();
      }

   }

   public final void playSoundData(byte[] var1) {
      this.stop();

      try {
         ByteArrayInputStream var3 = new ByteArrayInputStream(var1);
         this.player = Manager.createPlayer(var3, "audio/midi");
         this.player.setLoopCount(1);
         var3.close();
         if (this.volume > 0) {
            this.player.start();
            ((VolumeControl)this.player.getControl("VolumeControl")).setLevel(this.volume * 20);
            return;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public final void setVolume(int var1) {
      if (this.player != null && this.player.getState() != 0) {
         try {
            if (var1 > 0) {
               this.player.start();
               ((VolumeControl)this.player.getControl("VolumeControl")).setLevel(var1 * 20);
            } else {
               this.player.stop();
            }
         } catch (MediaException var3) {
            var3.printStackTrace();
         }
      }

      this.volume = var1;
   }

   static Vector getSound(SoundManager var0) {
      return var0.sound;
   }
}
