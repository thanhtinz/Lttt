package avt;

import main.GameMidlet;

public final class TimeFarm implements Runnable {
   private static final long FARM_DELAY_MS = 1000L;

   private static boolean running;

   private TimeFarm() {
   }

   private static void log(String msg) {
      System.out.println("[TIME_FARM] " + msg);
   }

   public static void reset() {
      running = false;
      SmartFarmCare.reset();
   }

   public static void start() {
      if (running) {
         return;
      }
      running = true;
      new Thread(new TimeFarm()).start();
   }

   public final void run() {
      try {
         long startAt = System.currentTimeMillis();
         log("run start typemap=" + LoadMap.TYPEMAP + " screen=" + main.Canvas.currentMyScreen);
         while (LoadMap.TYPEMAP != 25 && LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53) {
            if (!running) {
               return;
            }
            try {
               FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, true);
            } catch (Throwable t) {
               log("doJoinFarm failed: " + t);
            }
            try {
               Thread.sleep(FARM_DELAY_MS);
            } catch (Throwable t) {
               Thread.currentThread().interrupt();
               return;
            }
         }
         if (!running) {
            return;
         }
         if (LoadMap.TYPEMAP == 25 || LoadMap.TYPEMAP == 24 || LoadMap.TYPEMAP == 53) {
            SmartFarmCare.begin(5L);
            while (running && SmartFarmCare.isActive()) {
               try {
                  if (!SmartFarmCare.tick()) {
                     break;
                  }
               } catch (Throwable t) {
                  log("tick failed: " + t);
                  break;
               }
               try {
                  Thread.sleep(1000L);
               } catch (Throwable t) {
                  Thread.currentThread().interrupt();
                  break;
               }
            }
         }
         log("run end elapsed=" + (System.currentTimeMillis() - startAt));
      } finally {
         running = false;
      }
   }
}
