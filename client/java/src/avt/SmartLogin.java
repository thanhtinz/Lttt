package avt;

import main.Canvas;
// (GameMidlet import not needed)

public final class SmartLogin implements Runnable {
   private static void delay(int delayMs) {
      long start = System.currentTimeMillis();
      while (System.currentTimeMillis() - start < (long)delayMs) {
         try {
            Thread.sleep(delayMs);
         } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            break;
         }
      }
   }

   public final void run() {
      while (ClientUtilities.fishingAutoLogin) {
         try {
            if (Canvas.currentMyScreen == LoginScr.gI() && ClientUtilities.isFishingReloginWaiting()) {
               if (!ClientUtilities.isFishingReloginLoginIssued() && System.currentTimeMillis() >= ClientUtilities.getFishingReloginNextAtMs()) {
                  ClientUtilities.markFishingReloginLoginIssued();
                  try {
                     LoginScr.gI().login();
                  } catch (Throwable t) {
                  }
               }
               delay(200);
            } else {
               delay(500);
            }
         } catch (Throwable t) {
            delay(500);
         }
      }
   }
}

