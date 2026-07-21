package avt;

import main.Canvas;

final class IActionMiniMapKey implements IAction {
   IActionMiniMapKey(MapScr var1) {
   }

   public final void perform() {
      int var1 = MiniMap.gI().selected;
      if (Canvas.a(8)) {
         if (var1 == 0) {
            var1 = 5;
         } else if (var1 == 1) {
            var1 = 4;
         } else if (var1 == 3) {
            var1 = 6;
         } else if (var1 == 2) {
            var1 = 7;
         } else if (var1 == 4) {
            var1 = 7;
         }

         MiniMap.gI().ableTrans = true;
      } else if (Canvas.a(2)) {
         if (var1 == 4) {
            var1 = 1;
         } else if (var1 == 5) {
            var1 = 0;
         } else if (var1 == 6) {
            var1 = 3;
         } else if (var1 == 7) {
            var1 = 4;
         } else if (var1 == 3) {
            var1 = 0;
         }

         MiniMap.gI().ableTrans = true;
      } else if (Canvas.a(6)) {
         if (var1 == 0) {
            var1 = 3;
         } else if (var1 == 1) {
            var1 = 2;
         } else if (var1 == 3) {
            var1 = 4;
         } else if (var1 == 4) {
            var1 = 2;
         } else if (var1 == 6) {
            var1 = 7;
         } else if (var1 == 5) {
            var1 = 3;
         }

         MiniMap.gI().ableTrans = true;
      } else if (Canvas.a(4)) {
         if (var1 == 1) {
            var1 = 3;
         } else if (var1 == 2) {
            var1 = 1;
         } else if (var1 == 3) {
            var1 = 0;
         } else if (var1 == 6) {
            var1 = 5;
         } else if (var1 == 7) {
            var1 = 6;
         } else if (var1 == 4) {
            var1 = 3;
         }

         MiniMap.gI().ableTrans = true;
      }

      if (MiniMap.gI().ableTrans) {
         MiniMap.gI().selected = var1;
      }

   }
}
