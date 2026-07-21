package avt;


public final class AutoFishingFarmCare {
   private static final long START_WAIT_MS = 3000L;
   private static final long BUSY_RETRY_MS = 500L;
   private static final long MIN_CARE_TIME_MS = 1500L;

   private static boolean active;
   private static boolean started;
   private static long nextAtMs;
   private static long quickCareMinUntilMs;

   private AutoFishingFarmCare() {
   }

   private static void log(String msg) {
      System.out.println("[SMART_FISH_FARM] " + msg);
   }

   public static void reset() {
      active = false;
      started = false;
      nextAtMs = 0L;
      quickCareMinUntilMs = 0L;
   }

   public static void begin() {
      active = true;
      started = false;
      quickCareMinUntilMs = 0L;
      nextAtMs = System.currentTimeMillis() + START_WAIT_MS;
      log("begin nextAtMs=" + nextAtMs + " typemap=" + LoadMap.TYPEMAP + " screen=" + main.Canvas.currentMyScreen);
   }

   public static boolean isActive() {
      return active;
   }

   public static boolean tick() {
      if (!active) {
         log("tick inactive");
         return false;
      }

      long now = System.currentTimeMillis();
      log("tick now=" + now + " nextAtMs=" + nextAtMs + " started=" + started
            + " typemap=" + LoadMap.TYPEMAP + " screen=" + main.Canvas.currentMyScreen
            + " dialog=" + (main.Canvas.currentDialog != null));
      if (now < nextAtMs) {
         log("wait nextAtMs remain=" + (nextAtMs - now));
         return true;
      }

      if (LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53) {
         log("wait farm map typemap=" + LoadMap.TYPEMAP + " screen=" + main.Canvas.currentMyScreen);
         nextAtMs = now + 500L;
         return true;
      }

      if (main.Canvas.currentMyScreen != FarmScr.gI()) {
         log("wait FarmScr screen=" + main.Canvas.currentMyScreen + " typemap=" + LoadMap.TYPEMAP);
         nextAtMs = now + 500L;
         return true;
      }

      if (!started) {
         log("start quick care via menu typemap=" + LoadMap.TYPEMAP + " screen=" + main.Canvas.currentMyScreen);
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
         started = true;
         quickCareMinUntilMs = now + MIN_CARE_TIME_MS;
         nextAtMs = now + 1200L;
         log("quick care started quickCareMinUntilMs=" + quickCareMinUntilMs + " nextAtMs=" + nextAtMs);
         return true;
      }

      if (now < quickCareMinUntilMs) {
         log("wait min care remain=" + (quickCareMinUntilMs - now));
         nextAtMs = now + 300L;
         return true;
      }

      if (main.Canvas.currentDialog == null && !FarmScr.gI().isQuickCareBusyForAuto()) {
         log("quick care done");
         active = false;
         started = false;
         nextAtMs = 0L;
         quickCareMinUntilMs = 0L;
         return false;
      }

      log("quick care busy dialog=" + (main.Canvas.currentDialog != null)
            + " busyForAuto=" + FarmScr.gI().isQuickCareBusyForAuto());
      nextAtMs = now + BUSY_RETRY_MS;
      return true;
   }
}
