package avt;

import javax.microedition.lcdui.Graphics;

public final class Drop_Part extends Base {
   public short idDrop;
   private short deltaH;
   public int x0;
   public int y0;
   public int ID;
   private byte g_;
   private byte dir;
   private byte i;
   private byte j = 1;
   public byte type;
   private byte state;

   public Drop_Part() {
      super.catagory = 5;
   }

   public Drop_Part(byte var1, short var2, int var3) {
      this.ID = var3;
      super.catagory = 5;
      this.type = var1;
      this.idDrop = var2;
      this.dir = 0;
      Part var4;
      if ((var4 = AvatarData.getPart(this.idDrop)).idIcon < AvatarData.listImgInfo.length) {
         super.height = AvatarData.listImgInfo[var4.idIcon].h;
      }

      this.i = (byte)CRes.rnd(10);
   }

   public final void update() {
      switch (this.state) {
         case 0:
         case 1:
            super.x += (short)(this.x0 - super.x >> 2);
            super.y += (short)(this.y0 - super.y >> 2);
            if (this.g_ >= -6) {
               this.deltaH = (short)(this.deltaH + this.g_);
               --this.g_;
            }

            if ((CRes.abs(super.x - this.x0) < 4 || CRes.abs(super.y - this.y0) < 4) && this.deltaH <= 1) {
               super.x = this.x0;
               super.y = this.y0;
               this.deltaH = 0;
               this.g_ = 0;
               if (this.state == 1) {
                  LoadMap.removePlayer((MyObject)this);
               }

               this.state = 2;
               return;
            }
         case 2:
         default:
            break;
         case 3:
            this.deltaH = (short)(this.deltaH + 3);
            if (this.deltaH > 50) {
               LoadMap.removePlayer((MyObject)this);
               return;
            }
            break;
         case 4:
            if (this.deltaH > 0) {
               this.deltaH = (short)(this.deltaH - this.g_);
               ++this.g_;
               return;
            }

            this.deltaH = 0;
            this.state = 2;
      }

   }

   public final void paint(Graphics var1) {
      var1.drawImage(LoadMap.imgShadow, super.x, super.y + 1, 33);
      if (this.type == 0) {
         AvatarData.getPart(this.idDrop).paintIcon(var1, super.x, super.y + this.i / 10 - this.deltaH, 0, 33);
      } else {
         super.height = (short)(AvatarData.getImgIcon(this.idDrop).h + 10);
         AvatarData.paintImg(var1, this.idDrop, super.x, super.y + this.i / 10 - this.deltaH, 33);
      }

      this.i += this.j;
      if (CRes.abs(this.i) >= 10) {
         this.dir = (byte)(-this.dir);
      }

   }

   public final void startFlyTo(int var1) {
      Avatar var2;
      if ((var2 = LoadMap.getAvatar(var1)) != null) {
         this.x0 = var2.x;
         this.y0 = var2.y;
         this.state = 1;
         this.deltaH = 0;
      } else {
         this.deltaH = 0;
         this.state = 3;
      }

      this.g_ = 6;
   }

   public final void startDropFrom(int var1, short var2, short var3) {
      if (var1 == -2) {
         super.x = var2;
         super.y = var3;
         this.state = 2;
      } else {
         Avatar var4;
         if ((var4 = LoadMap.getAvatar(var1)) != null) {
            super.x = var4.x;
            super.y = var4.y;
            this.state = 0;
            this.g_ = 6;
            this.deltaH = 0;
         } else {
            this.state = 4;
            super.x = var2;
            super.y = var3;
            this.deltaH = 100;
            this.g_ = 0;
         }
      }

      this.x0 = var2;
      this.y0 = var3;
   }
}
