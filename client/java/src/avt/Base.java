package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public class Base extends MyObject {
   public int IDDB;
   public String name = "";
   public int frame;
   public byte g = 7;
   public byte vhy;
   public byte vh;
   public int xCur;
   public int yCur;
   public int vx = 0;
   public int vy = 0;
   public int v = 4;
   public byte action = 0;
   public static byte RIGHT = 0;
   public static byte LEFT = 2;
   public byte direct;
   public boolean ableShow;
   public boolean M;
   public short direct_;
   public ChatPopup chat;
   public Vector listChat;

   public Base() {
      this.direct = LEFT;
      this.ableShow = false;
      this.M = false;
      this.direct_ = 0;
      this.listChat = new Vector();
   }

   public void paint(Graphics var1) {
      if (this.chat != null && Canvas.currentMyScreen != MainMenu.gI()) {
         this.chat.paintAnimal(var1);
      }

   }

   public final void setPos(int var1, int var2) {
      super.x = this.xCur = var1;
      super.y = this.yCur = var2;
   }

   public void update() {
      if (this.chat != null) {
         this.chat.setPos(super.x, super.y - super.height - 12);
         if (this.chat.setOut()) {
            this.chat = null;
            this.getChat();
         }
      }

   }

   private void getChat() {
      if (this.chat == null && this.listChat.size() > 0) {
         this.chat = (ChatPopup)this.listChat.elementAt(0);
         this.listChat.removeElementAt(0);
      }

   }

   public boolean detectCollision(int var1, int var2) {
      if (this.action != -1 && this.action != 14) {
         if (this.action != 10 && this.action != 2 && this.action != 4) {
            this.action = 0;
         }

         if (this.action != 0 && this.action != 1) {
            this.vx = 0;
            this.vy = 0;
            return true;
         } else {
            this.action = 1;
            int var3 = super.x;
            int var4 = super.y;
            if (super.catagory == 2) {
               var3 = this.xCur;
               var4 = this.yCur;
            }

            if (LoadMap.isTrans(var3 + var1, var4 + var2)) {
               if (var1 != 0) {
                  if (var1 > 0) {
                     this.vx = this.v;
                  } else {
                     this.vx = -this.v;
                  }
               }

               if (var2 != 0) {
                  if (var2 > 0) {
                     this.vy = this.v;
                  } else {
                     this.vy = -this.v;
                  }
               }

               return false;
            } else {
               this.vx = 0;
               this.vy = 0;
               return true;
            }
         }
      } else {
         this.vx = 0;
         this.vy = 0;
         return true;
      }
   }

   public final boolean setWay(int var1, int var2) {
      if (this.action != 0 && this.action != 1) {
         return false;
      } else if (LoadMap.getTypeMap(super.x + var1, super.y + var2) == 90) {
         return false;
      } else {
         int var3 = super.x;
         if (super.catagory == 0) {
            var3 += var1 < 0 ? -7 : 7;
         }

         int var4;
         int var5;
         if (var1 != 0) {
            var4 = LoadMap.getTypeMap(var3 + var1, super.y - 24);
            var5 = LoadMap.getTypeMap(var3, super.y - 24);
            if (var4 == 80 && var5 == 80) {
               this.vy = -this.v;
               this.xCur = var3;
               MapScr.gI().move();
               return true;
            }

            var1 = LoadMap.getTypeMap(var3 + var1, super.y + 24);
            var2 = LoadMap.getTypeMap(var3, super.y + 24);
            if (var1 == 80 && var2 == 80) {
               this.vy = this.v;
               this.xCur = var3;
               MapScr.gI().move();
               return true;
            }
         } else if (var2 != 0) {
            var4 = LoadMap.getTypeMap(var3 - 24, super.y + var2);
            var5 = LoadMap.getTypeMap(var3 - 24, super.y);
            if (var4 == 80 && var5 == 80) {
               this.vx = -this.v;
               this.yCur = super.y;
               MapScr.gI().move();
               return true;
            }

            var1 = LoadMap.getTypeMap(var3 + 24, super.y + var2);
            var2 = LoadMap.getTypeMap(var3 + 24, super.y);
            if (var1 == 80 && var2 == 80) {
               this.vx = this.v;
               this.yCur = super.y;
               MapScr.gI().move();
               return true;
            }
         }

         return false;
      }
   }

   public void paintIcon(Graphics var1, int var2, int var3, boolean var4) {
   }

   public final void addChat(int var1, String var2, byte var3) {
      this.listChat.addElement(new ChatPopup(var1, var2, var3));
      this.getChat();
   }
}
