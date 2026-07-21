package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class ParkListSrc extends MyScreen {
   public static ParkListSrc instance;
   private int[] listBoard;
   private MyScreen lastScr;
   private int maxW = 5;
   private int w;
   private int maxH = 7;

   public static ParkListSrc gI() {
      if (instance == null) {
         instance = new ParkListSrc();
      }

      return instance;
   }

   public final void switchToMe(MyScreen var1) {
      super.switchToMe();
      this.lastScr = var1;
      super.selected_ = 0;
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            Canvas.cameraList.isShow = false;
            this.lastScr.switchToMe();
            return;
         case 1:
            Canvas.cameraList.isShow = false;
            this.lastScr.switchToMe();
            ParkService.gI().doJoinPark(MapScr.roomID, super.selected_);
         default:
      }
   }

   public ParkListSrc() {
      super.right = new Command(T.close, 0);
      super.center = new Command(T.selectt, 1);
      this.w = 20;
      if (Canvas.stypeInt > 0) {
         this.w = Canvas.stypeInt * 30;
      }

      if (Canvas.w < 176) {
         this.w = 15;
      }

      if (this.maxH * this.w > Canvas.h - Canvas.hTab) {
         this.maxH = (Canvas.h - Canvas.hTab) / this.w;
      }

   }

   public final void setSelected(int var1, boolean var2) {
      if (var2 && super.selected_ == var1 && super.center != null) {
         super.center.perform();
      }

      super.setSelected(var1, var2);
   }

   public final void setList(int[] var1) {
      this.listBoard = var1;
      Canvas.cameraList.setInfo(Canvas.hw - (this.w * this.maxW + 10) / 2 + 4, Canvas.hh - this.w * this.maxH / 2, this.w, this.w, this.maxW * this.w, this.listBoard.length / this.maxW * this.w, this.w * this.maxW, this.w * this.maxH - (Canvas.stypeInt == 0 ? 30 : 0), var1.length);
   }

   public final void updateKey() {
      super.updateKey();
   }

   public final void update() {
      this.lastScr.update();
   }

   public final void paint(Graphics var1) {
      var1.translate(0, 0);
      var1.setClip(0, 0, Canvas.w, Canvas.h);
      this.lastScr.paintMain(var1);
      Canvas.paint.drawArea(var1, Canvas.hw - (this.w * this.maxW + 10) / 2, Canvas.hh - this.w * this.maxH / 2, this.w * this.maxW + 10, this.w * this.maxH);
      Canvas.paint.drawStateElement(var1, this.w, this.maxW, this.maxH, super.isHide_, super.selected_, this.listBoard);
      super.paint(var1);
   }
}
