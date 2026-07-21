package avt;

import java.io.ByteArrayInputStream;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import main.Canvas;

public final class SoundManager {
   private int c;
   private Player d;
   public static SoundManager a = new SoundManager();
   private Vector e;
   private Vector f;
   int b = -1;

   public final void a(String var1, byte var2) {
      if (this.b != 0) {
         class_bl var4 = new class_bl(this, var2);
         if (this.b == 1) {
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

   public final int a(String var1) {
      if (this.f == null) {
         this.f = new Vector();
      }

      for(int var2 = 0; var2 < this.f.size(); ++var2) {
         if (((String)this.f.elementAt(var2)).equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public final void a(byte[] var1, byte var2) {
      if (this.e == null) {
         this.e = new Vector();
         this.f = new Vector();
      }

      try {
         this.f.addElement("" + var2);
         this.e.addElement(var1);
         a.a(var1);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public final void a() {
      if (this.d != null) {
         if (this.d.getState() == 400) {
            try {
               this.d.stop();
            } catch (MediaException var2) {
               var2.printStackTrace();
            }
         }

         this.d.close();
      }

   }

   public final void a(byte[] var1) {
      this.a();

      try {
         ByteArrayInputStream var3 = new ByteArrayInputStream(var1);
         this.d = Manager.createPlayer(var3, "audio/midi");
         this.d.setLoopCount(1);
         var3.close();
         if (this.c > 0) {
            this.d.start();
            ((VolumeControl)this.d.getControl("VolumeControl")).setLevel(this.c * 20);
            return;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public final void a(int var1) {
      if (this.d != null && this.d.getState() != 0) {
         try {
            if (var1 > 0) {
               this.d.start();
               ((VolumeControl)this.d.getControl("VolumeControl")).setLevel(var1 * 20);
            } else {
               this.d.stop();
            }
         } catch (MediaException var3) {
            var3.printStackTrace();
         }
      }

      this.c = var1;
   }

   static Vector a(SoundManager var0) {
      return var0.e;
   }
}
