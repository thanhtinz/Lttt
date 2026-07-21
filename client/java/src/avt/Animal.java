package avt;

import javax.microedition.lcdui.Graphics;
import main.Canvas;

public class Animal extends Base {
   public int disTrans;
   public int angle;
   public int distant;
   public int period = 0;
   public int cycle;
   public boolean isEat = false;
   public int bornTime;
   public int health;
   public boolean hunger;
   public boolean[] disease = new boolean[2];
   public byte species;
   public byte l;
   public byte indexFr;
   public AvPosition posNext;
   public int numEggOne = 0;
   public boolean isStand = false;
   public int timeStand = 0;

   public Animal() {
      super.catagory = 2;
   }

   public void setInit() {
   }

   public Animal(int var1, int var2, int var3, byte var4) {
      AnimalInfo var5 = FarmData.getAnimalByID(var4);
      super.name = var5.name;
      super.catagory = 2;
      this.setPos(var1, var2);
      super.direct = 0;
      super.action = 0;
      super.IDDB = var3;
      this.period = 0;
      super.g = 4;
      super.vy = super.g;
      super.v = 1;
      this.species = var4;
      super.frame = CRes.rnd(12);
   }

   public void paint(Graphics var1) {
      AnimalInfo var2;
      ImageIcon var3;
      if (super.x * MyObject.hd + 30 >= AvCamera.gI().xCam && super.x * MyObject.hd - 30 <= AvCamera.gI().xCam + Canvas.w && Canvas.currentMyScreen != MainMenu.gI() && (var3 = AvatarData.getImgIcon((var2 = FarmData.getAnimalByID(this.species)).idImg[this.period])).count != -1) {
         if (super.height == 0) {
            super.height = (short)(var3.h / var2.frame);
         }

         if (super.catagory != 7) {
            this.indexFr = var2.arrFrame[super.action][super.frame];
         }

         var1.drawRegion(var3.img, 0, this.indexFr * super.height, var3.w, super.height, super.direct, super.x * MyObject.hd, super.y * MyObject.hd + this.l - (var2.area == 4 ? super.height / 3 << 1 : 0), var2.area != 4 ? 33 : 17);
         super.paint(var1);
         this.paintFocus(var1, super.height + 2, super.x * MyObject.hd, super.y * MyObject.hd, LoadMap.focusObj);
      }

   }

   public final void paintIcon(Graphics var1, int var2, int var3, boolean var4) {
      ImageIcon var5;
      AnimalInfo var6;
      if ((var5 = AvatarData.getImgIcon((var6 = FarmData.getAnimalByID(this.species)).idImg[this.period])).count != -1) {
         if (super.height == 0) {
            super.height = (short)(var5.h / var6.frame);
         }

         if (super.catagory != 7) {
            this.indexFr = var6.arrFrame[super.action][super.frame];
         }

         var1.drawRegion(var5.img, 0, this.indexFr * super.height, var5.w, super.height, super.direct, var2, var3 + this.l, var6.area != 4 ? 33 : 17);
         this.paintFocus(var1, super.height + 2, var2, var3, this);
      }

   }

   private void paintFocus(Graphics var1, int var2, int var3, int var4, MyObject var5) {
      if (true) {
         int var7 = FarmData.getAnimalByID(this.species).harvestTime * 60 - this.bornTime;
         int var6 = this.period * 5;
         if (this.bornTime > FarmData.getAnimalByID(this.species).harvestTime * 60) {
            PaintPopup.fill(var3 - (var6 + 22) * MyObject.hd / 2, var4 - (18 + this.l) * MyObject.hd - var2, (var6 + 22) * MyObject.hd, 4 * MyObject.hd, 1, var1);
            PaintPopup.fill(var3 - (var6 + 20) * MyObject.hd / 2, var4 - (17 + this.l) * MyObject.hd - var2, this.health * (var6 + 20) / 100 * MyObject.hd, 2 * MyObject.hd, 65280, var1);
         } else {
            PaintPopup.fill(var3 - (var6 + 22) * MyObject.hd / 2, var4 - (18 + this.l) * MyObject.hd - var2, (var6 + 22) * MyObject.hd, 4 * MyObject.hd, 1, var1);
            PaintPopup.fill(var3 - (var6 + 20) * MyObject.hd / 2, var4 - (17 + this.l) * MyObject.hd - var2, this.health * (var6 + 20) / 100 * MyObject.hd, 2 * MyObject.hd, 65280, var1);
            Canvas.smallFontYellow.drawString(var1, var7 / 60 + ":" + (var7 - var7 / 60 * 60), var3, var4 - (13 + this.l) * MyObject.hd - var2, 2);
         }

         if (super.catagory == 7) {
            var2 = 10;
         }

         if (this.disease[0]) {
            FarmScr.u.drawFrame(0, var3 - 10 * MyObject.hd, var4 - (22 + this.l) * MyObject.hd - var2, 0, 3, var1);
         }

         if (this.disease[1]) {
            FarmScr.u.drawFrame(1, var3 + 10 * MyObject.hd, var4 - (22 + this.l) * MyObject.hd - var2, 0, 3, var1);
         }
      }

   }

   public void update() {
      if (this.isStand) {
         if (Canvas.getSecond() - this.timeStand > 10) {
            this.isStand = false;
         }
      } else {
         ++super.frame;
         if (super.frame >= 12) {
            super.frame = 0;
         }

         this.updateEat();
         if (super.action != 1) {
            if (super.frame == 0) {
               super.action = (byte)CRes.rnd(5 + (this.species - 50) * 5);
               if (super.action != 2) {
                  super.action = 0;
               } else {
                  super.direct = (byte)CRes.rnd(0, Base.LEFT);
               }
            }

            if (this.cycle > 0) {
               --this.cycle;
               return;
            }

            this.updatePos();
            if (this.posNext.x > super.x) {
               super.direct = 0;
            } else {
               super.direct = Base.LEFT;
            }

            this.setAngleAndDis();
            super.action = 1;
         } else {
            this.move();
         }

         super.update();
      }

   }

   public void updatePos() {
   }

   public void updateEat() {
   }

   public void move() {
      int var1 = super.v * (this.disTrans * CRes.cos(CRes.fixangle(this.angle)) >> 10);
      int var2 = -super.v * this.disTrans * CRes.sin(CRes.fixangle(this.angle)) >> 10;
      if (this.detectCollision(var1, var2)) {
         if (this.setWay(var1, var2)) {
            super.x += super.vx;
            super.y += super.vy;
         }

         this.reset();
      } else {
         super.x = super.xCur + var1;
         super.y = super.yCur + var2;
         var1 = CRes.distance(super.xCur, super.yCur, super.x, super.y);
         ++this.disTrans;
         if (var1 > this.distant) {
            this.reset();
         }
      }

   }

   public void setAngleAndDis() {
      this.distant = CRes.distance(super.x, super.y, this.posNext.x, this.posNext.y);
      this.angle = CRes.tan(this.posNext.x - super.x, -(this.posNext.y - super.y));
   }

   public void setPos() {
      AvPosition var2 = new AvPosition(CRes.rnd(LoadMap.wMap * 6) << 2, CRes.rnd(LoadMap.Hmap * 6) << 2);
      this.posNext = var2;
   }

   public void reset() {
      super.action = 0;
      super.xCur = super.x;
      super.yCur = super.y;
      super.vx = 0;
      super.vy = 0;
      this.disTrans = 0;
   }
}
