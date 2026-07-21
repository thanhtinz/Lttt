package avt;

import main.GameMidlet;

public final class SmartFarmCare {
   private static boolean active;
   private static long nextAtMs;
   private static long farmEndAtMs;

   private SmartFarmCare() {
   }

   private static void log(String msg) {
   }

   public static void reset() {
      active = false;
      nextAtMs = 0L;
      farmEndAtMs = 0L;
   }

   public static boolean isActive() {
      return active;
   }

   public static void begin(long farmMinutes) {
      active = true;
      farmEndAtMs = System.currentTimeMillis() + Math.max(0L, farmMinutes) * 60_000L;
      nextAtMs = 0L;
      log("begin farmMinutes=" + farmMinutes + " endAt=" + farmEndAtMs + " typemap=" + LoadMap.TYPEMAP
            + " screen=" + main.Canvas.currentMyScreen);
   }

   public static boolean tick() {
      if (!active) {
         log("tick inactive returning false");
         return false;
      }

      long now = System.currentTimeMillis();
      log("tick now=" + now + " nextAtMs=" + nextAtMs + " endAt=" + farmEndAtMs
            + " typemap=" + LoadMap.TYPEMAP + " screen=" + main.Canvas.currentMyScreen
            + " dialog=" + (main.Canvas.currentDialog != null));

      if (farmEndAtMs > 0L && now >= farmEndAtMs) {
         log("farm complete");
         active = false;
         nextAtMs = 0L;
         farmEndAtMs = 0L;
         return false;
      }

      if (nextAtMs > 0L && now < nextAtMs) {
         log("wait nextAtMs remain=" + (nextAtMs - now));
         return true;
      }

      if (LoadMap.TYPEMAP == 25) {
         log("at farm lobby map 25 -> enter work map");
         try {
            FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, true);
         } catch (Throwable t) {
            log("doJoinFarm from 25 failed: " + t);
         }
         nextAtMs = now + 1200L;
         return true;
      }

      if (LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53) {
         log("not in farm map -> join farm");
         try {
            FarmScr.gI().doJoinFarm(GameMidlet.avatar.IDDB, true);
         } catch (Throwable t) {
            log("doJoinFarm failed: " + t);
         }
         nextAtMs = now + 1500L;
         return true;
      }

      if (main.Canvas.currentDialog != null) {
         log("closing server dialog");
         main.Canvas.endDlg();
         nextAtMs = now + 100L;
         return true;
      }

      log("calling commandTab(1,-1) to open farm menu");
      try {
         FarmScr.gI().commandTab(1, -1);
         log("commandTab(1,-1) opened menu");
         try {
            Thread.sleep(300L);
         } catch (InterruptedException ex) {}
         FarmScr.gI().commandTab(0, -1);
         log("commandTab(0,-1) clicked first item");
      } catch (Throwable t) {
         log("menu click failed: " + t);
      }

      nextAtMs = now + 1200L;
      return true;
   }
}
