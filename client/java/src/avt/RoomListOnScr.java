package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class RoomListOnScr extends MyScreen {
   public static RoomListOnScr me;
   public static FrameImage imgRoomStat;
   private Vector roomList;
   public static String title;
   private int _selected;
   private int hSmall_;
   private Command cmdMenu;
   private Command cmdClose;
   private int i = 0;

   public static RoomListOnScr gI() {
      if (me == null) {
         me = new RoomListOnScr();
      }

      return me;
   }

   public final void switchToMe() {
      Canvas.paint.initResourceThree();
      super.switchToMe();
      super.right = this.cmdClose;
      if (Canvas.stypeInt == 0) {
         super.center = new Command(T.selectt, 3);
      } else {
         super.center = new Command(T.strongest, 1);
      }

      super.isHide_ = true;
      this.init();
      OnScreen.addCmd();
      this._selected = this.i;
      Canvas.cameraList.setSelect(this._selected);
   }

   public RoomListOnScr() {
      this.init();
      this.initCmd();
   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 1:
            Canvas.startWaitDlg();
            CasinoService.gI().joinAnyBoard();
            return;
         case 2:
            Canvas.startWaitDlg();
            CasinoService.gI().requestRoomList();
            return;
         case 3:
            Canvas.startWaitDlg();
            GlobalService.gI().requestInfoOf(GameMidlet.avatar.IDDB);
         default:
      }
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            Vector var3;
            (var3 = new Vector()).addElement(new Command(T.strongest, 1));
            var3.addElement(new Command(T.updateList, 2));
            if (Canvas.stypeInt == 0) {
               var3.addElement(MapScr.gI().f);
            }

            var3.addElement(new Command(T.viewMyInfo, 3));
            Menu.gI().startAt(var3, 0);
            return;
         case 1:
            this.doSelectRoom();
            return;
         case 2:
            GlobalService.gI().getHandler(9);
            Canvas.startWaitDlg();
            return;
         case 3:
            this.doSelectRoom();
         default:
      }
   }

   public final void initCmd() {
      this.cmdMenu = new Command(T.menu, 0);
      new Command(T.selectt, 1);
      this.cmdClose = new Command(T.close, 2);
      super.left = this.cmdMenu;
      super.right = this.cmdClose;
   }

   public static void setName(int var0, BoardScr var1) {
      if (!OnScreen.isOngame) {
         title = T.constructing[var0];
      } else {
         title = T.selectLanguage[var0];
      }

      CasinoMsgHandler.curScr = var1;
   }

   public final void init() {
      if (Canvas.stypeInt == 0) {
         this.hSmall_ = 50;
         this.i = 1;
      } else {
         if (Canvas.stypeInt == 1) {
            this.hSmall_ = 80;
         } else if (Canvas.stypeInt == 2) {
            this.hSmall_ = 150;
         }

         this.i = Canvas.w / this.hSmall_;
      }

      if (this.roomList != null && this.hSmall_ != 0) {
         if (Canvas.stypeInt == 0) {
            Canvas.cameraList.setInfo(0, Canvas.w < 200 ? this.hSmall_ / 2 : 50, Canvas.w, this.hSmall_, Canvas.w, this.roomList.size() * this.hSmall_, Canvas.w, Canvas.h - (Canvas.w < 200 ? this.hSmall_ / 2 : 50) - 4, this.roomList.size());
         } else {
            Canvas.cameraList.setInfo((Canvas.w - this.hSmall_ * this.i) / 2, 50 * AvMain.hd, this.hSmall_, this.hSmall_, Canvas.w, (this.roomList.size() / this.i + 2) * this.hSmall_, Canvas.w, Canvas.h - 50 * AvMain.hd - 4, this.roomList.size());
         }

         Canvas.cameraList.setSelect(this._selected);
      }

   }

   private void doSelectRoom() {
      byte var1;
      if ((var1 = ((RoomInfo)this.roomList.elementAt(this._selected)).id) != -1) {
         CasinoService.gI().requestBoardList(var1);
         Canvas.startWaitDlg();
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      OnScreen.paintTitle(var1, super.left, super.center, super.right);
      Canvas.paintPlus2(var1);
   }

   public final void paintMain(Graphics var1) {
      Canvas.paint.paintDefaultBg(var1);
      paintRoomList(var1, "Phòng " + title);
      Canvas.paint.drawVectorElements(var1, this.roomList, this.hSmall_, this._selected);
   }

   public static void paintRoomList(Graphics var0, String var1) {
      Canvas.paint.paintDefaultBg(var0);
      if (Canvas.w > 200) {
         Canvas.paint.drawContainer(var0, Canvas.hw - 100 * AvMain.hd, 5 * AvMain.hd - CameraList.cmtoY, 200 * AvMain.hd, 44 * AvMain.hd);
         FontX var2 = Canvas.O;
         if (Canvas.stypeInt == 0) {
            var2 = Canvas.borderFont;
         }

         var2.drawString(var0, var1, Canvas.hw, 5 * AvMain.hd - CameraList.cmtoY + 22 * AvMain.hd - var2.getHeight() / 2, 2);
      }

   }

   public final void setRoomList(Vector var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         RoomInfo var3 = (RoomInfo)var1.elementAt(var2);

         for(int var4 = var2; var4 < var1.size(); ++var4) {
            RoomInfo var5;
            if ((var5 = (RoomInfo)var1.elementAt(var4)).lv < var3.lv) {
               var1.setElementAt(var3, var4);
               var1.setElementAt(var5, var2);
               var3 = var5;
            }
         }
      }

      this.roomList = new Vector();
      byte var6 = -1;

      for(int var7 = 0; var7 < var1.size(); ++var7) {
         RoomInfo var8 = (RoomInfo)var1.elementAt(var7);
         if (var6 == -1 || var8.lv != var6) {
            this.roomList.addElement(new RoomInfo((byte)-1, (byte)0, (byte)0, var8.lv));
         }

         this.roomList.addElement(var8);
         var6 = var8.lv;
      }

      if (Canvas.stypeInt != 0) {
         this.fillEmptyRooms();
      }

      this._selected = 1;
      this.init();
   }

   private boolean fillEmptyRooms() {
      for(int var1 = 0; var1 < this.roomList.size(); ++var1) {
         RoomInfo var2;
         if ((var2 = (RoomInfo)this.roomList.elementAt(var1)).id == -1) {
            int var3;
            int var4;
            if ((var3 = this.i - var1 % this.i) != this.i) {
               for(var4 = 0; var4 < var3; ++var4) {
                  this.roomList.insertElementAt(new RoomInfo((byte)-2, (byte)0, (byte)0, var2.lv), var1);
               }

               var1 += var3;
            }

            for(var4 = 0; var4 < this.i - 1; ++var4) {
               this.roomList.insertElementAt(new RoomInfo((byte)-2, (byte)0, (byte)0, var2.lv), var1 + 1);
            }

            var1 += this.i;
         }
      }

      return false;
   }

   public final void setSelected(int var1, boolean var2) {
      if (var2 && this._selected == var1) {
         this.doSelectRoom();
      }

      if (Canvas.stypeInt == 0) {
         if (this._selected > 0 && this._selected < this.roomList.size()) {
            RoomInfo var3;
            if ((var3 = (RoomInfo)this.roomList.elementAt(var1)).id != -1 && var3.id != -1) {
               if (var1 >= 0 && var1 < this.roomList.size()) {
                  this._selected = var1;
               }
            } else if (var1 > this._selected) {
               this._selected = var1 + this.i;
            } else {
               this._selected = var1 - this.i;
            }

            Canvas.cameraList.setSelect(this._selected);
            if (this._selected <= 0) {
               this._selected = this.roomList.size() - 1;
               Canvas.cameraList.setSelect(this._selected);
               return;
            }
         }
      } else {
         this._selected = var1;
      }

   }

   public final void updateKey() {
      if (Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

   }

   public final void update() {
   }
}
