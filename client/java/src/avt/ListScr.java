package avt;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;

public final class ListScr extends MyScreen {
   public static ListScr instance;
   public MyScreen backMyScreen;
   private int focus = 0;
   public static Vector tempList = new Vector();
   private Command k;
   public static Vector friendL;
   private int wSmall;
   public static byte typeListFriend = 0;
   public static boolean isGetTypeHouse = false;
   public int selected;
   public static String idFriendList = "friendlist";
   public static Hashtable hList = new Hashtable();
   private boolean isAction = false;
   private String name;
   private boolean isHide = false;
   private int xCus = -20;
   private Command cmdSelected;

   public static ListScr gI() {
      if (instance == null) {
         instance = new ListScr();
      }

      return instance;
   }

   public final void switchToMe() {
      this.selected = 0;
      this.k = new Command(T.close, 1);
      super.right = this.k;
      this.backMyScreen = null;
      if (Canvas.currentMyScreen != MainMenu.gI() && Canvas.currentMyScreen != PopupShop.gI() && Canvas.currentMyScreen != gI()) {
         this.backMyScreen = Canvas.currentMyScreen;
      }

      this.setupPopupBox();
      super.switchToMe();
      if (Canvas.stypeInt > 0) {
         this.isHide = true;
      }

   }

   public final void setCam() {
      Canvas.cameraList.setInfo(20, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox, Canvas.w - 40, this.wSmall, Canvas.w - 40, tempList.size() * this.wSmall, Canvas.w - 40, PaintPopup.gI().h - 5 - (PaintPopup.hTab + 2 * AvMain.hDuBox), tempList.size());
      if (tempList.size() > 0) {
         Scroll.gI().init(tempList.size() * this.wSmall, PaintPopup.gI().h - 5 - (PaintPopup.hTab + 2 * AvMain.hDuBox));
      }

   }

   public final void setupPopupBox() {
      if (Canvas.stypeInt == 0) {
         PaintPopup.gI().setup(this.name, Canvas.w - 20, Canvas.hCan - Canvas.hTab - 20, 1);
      } else {
         PaintPopup.gI().setup(this.name, Canvas.w - 20 * AvMain.hd, Canvas.h - Canvas.ab - Canvas.hTab - 10 + (OnScreen.isOngame ? 7 * AvMain.hd : 0), 1);
      }

      PaintPopup.gI().y = 10 + Canvas.ab;
      if (tempList != null) {
         this.setCam();
      }

   }

   public ListScr() {
      this.wSmall = 40 * AvMain.hd;
   }

   public final void setSelected(int var1, boolean var2) {
      if (var2 && var1 == this.selected) {
         this.perform(this.cmdSelected);
      }

      this.xCus = -20;
      if (var1 >= 0 && var1 < tempList.size()) {
         this.selected = var1;
      }

   }

   public final void setHidePointer(boolean var1) {
      this.isHide = var1;
   }

   public final void paint(Graphics var1) {
      var1.setClip(0, 0, Canvas.w, Canvas.h);
      if (this.backMyScreen != null) {
         this.backMyScreen.paintMain(var1);
      } else {
         MapScr.gI().paintMain(var1);
      }

      PaintPopup.gI().paint(var1);
      var1.translate(0, PaintPopup.gI().y + PaintPopup.hTab + AvMain.hDuBox);
      var1.setClip(0, 0, Canvas.w, Canvas.h);
      int var2;
      if ((var2 = CameraList.cmtoY / this.wSmall) < 0) {
         var2 = 0;
      }

      int var3;
      if ((var3 = var2 + (Canvas.h - 40) / this.wSmall + 1) > tempList.size()) {
         var3 = tempList.size();
      }

      int var5;
      int var6;
      int var7;
      int var9;
      ListScr var11;
      Graphics var12;
      if (this.focus == 5) {
         var5 = var3;
         var12 = var1;
         var11 = this;
         var6 = 0 + this.wSmall * var2;

         for(var7 = var2; var7 < var5; ++var7) {
            var12.setClip(10 * AvMain.hd + 4 + AvMain.hd, 0, PaintPopup.gI().w - 8 - (AvMain.hd << 1), PaintPopup.gI().h - 5 - (PaintPopup.hTab + 2 * AvMain.hDuBox));
            var12.translate(0, -CameraList.cmtoY);
            StringObj var13 = (StringObj)tempList.elementAt(var7);
            int var8 = 0;
            if (!var11.isHide && var7 == var11.selected) {
               Canvas.paint.drawSelectedArea(var12, 10 * AvMain.hd + 3 + 2 * AvMain.hd, var6 + 2, Canvas.w - 20 * AvMain.hd - 6 - 4 * AvMain.hd, var11.wSmall - 4);
               if (var13.w2 > PaintPopup.gI().w - 40) {
                  var11.xCus += 2;
                  if (var11.xCus > var13.w2 - (PaintPopup.gI().w - 40)) {
                     var11.xCus = -20;
                  }
               }

               var8 = var11.xCus;
               if (var11.xCus < 0) {
                  var8 = 0;
               }
            }

            var9 = AvatarData.getImgIcon((short)var13.dis).h + 4;
            AvatarData.paintImg(var12, var13.dis, 10 * AvMain.hd + 10 + var9 / 2, var6 + var11.wSmall / 2 - 12 * AvMain.hd + AvMain.hNormal / 2, 3);
            Canvas.normalFont.drawString(var12, var13.str, 10 * AvMain.hd + 10 + var9, var6 + var11.wSmall / 2 - 12 * AvMain.hd, 0);
            Canvas.fontChatB.drawString(var12, var13.str2, 10 * AvMain.hd + 10 - var8, var6 + var11.wSmall / 2 + 3 * AvMain.hd, 0);
            var6 += var11.wSmall;
            var12.translate(0, CameraList.cmtoY);
         }
      } else if (this.focus == 6 || this.focus == 0) {
         var5 = var3;
         var12 = var1;
         var11 = this;
         var7 = 0;
         var6 = 0 + this.wSmall * var2;

         for(int var4 = var2; var4 < var5; ++var4) {
            var12.setClip(10 * AvMain.hd + 4, 0, PaintPopup.gI().w - 8, PaintPopup.gI().h - 5 - (PaintPopup.hTab + 2 * AvMain.hDuBox));
            var12.translate(0, -CameraList.cmtoY);
            Avatar var14 = (Avatar)tempList.elementAt(var4);
            var9 = 0;
            int var10;
            int var10001;
            int var10005;
            if (!var11.isHide && var4 == var11.selected) {
               Canvas.paint.drawSelectedArea(var12, 10 * AvMain.hd + 3 + 2 * AvMain.hd, var6 + 2, Canvas.w - 20 * AvMain.hd - 6 - 4 * AvMain.hd, var11.wSmall - 4);
               var10001 = var10 = Canvas.fontChatB.getWidth(var14.text2);
               var10005 = AvMain.hd - 1;
               if (var10001 > PaintPopup.gI().w - (57 + var10005 * 30)) {
                  var11.xCus += 2;
                  int var10004 = AvMain.hd - 1;
                  if (var11.xCus > var10 - (PaintPopup.gI().w - (57 + var10004 * 30))) {
                     var11.xCus = -20;
                  }
               }

               var9 = var11.xCus;
               if (var11.xCus < 0) {
                  var9 = 0;
               }
            }

            var14.paintIcon(var12, 10 * AvMain.hd + 25 + (AvMain.hd - 1) * 20, var6 + var11.wSmall - 5 * AvMain.hd, false);
            var10 = 0;
            if (var14.idImg != -1) {
               var10 = 6 * AvMain.hd;
               AvatarData.paintImg(var12, var14.idImg, 60 + (AvMain.hd - 1) * 30 + var10, var6 + var11.wSmall / 2 - 12 * AvMain.hd + AvMain.hNormal / 2, 3);
            }

            var10001 = 60 + (AvMain.hd - 1) * 30;
            var10005 = AvMain.hd - 1;
            var12.setClip(var10001, CameraList.cmtoY, PaintPopup.gI().w - (47 + var10005 * 30), PaintPopup.gI().h - 5 - (PaintPopup.hTab + 2 * AvMain.hDuBox));
            Canvas.normalFont.drawString(var12, var14.name, 60 + (var10 << 1) + (AvMain.hd - 1) * 30, var6 + var11.wSmall / 2 - 12 * AvMain.hd, 0);
            if (var14.idWedding != -1) {
               AvatarData.paintImg(var12, var14.idWedding, 60 + 6 * AvMain.hd + (var10 << 1) + (AvMain.hd - 1) * 30 + Canvas.normalFont.getWidth(var14.name), var6 + var11.wSmall / 2 - 12 * AvMain.hd + AvMain.hNormal / 2, 3);
            }

            if (var14.idStatus != -1) {
               var7 = 12 * AvMain.hd;
               AvatarData.paintImg(var12, var14.idStatus, 60 - var9 + (AvMain.hd - 1) * 30 + 6 * AvMain.hd, var6 + var11.wSmall / 2 + 3 * AvMain.hd + AvMain.hBlack / 2, 3);
            }

            Canvas.fontChatB.drawString(var12, var14.text2, 60 - var9 + (AvMain.hd - 1) * 30 + var7, var6 + var11.wSmall / 2 + 3 * AvMain.hd, 0);
            var6 += var11.wSmall;
            var12.translate(0, CameraList.cmtoY);
         }
      }

      Scroll.gI().paintScroll(var1, Canvas.w - 10 * AvMain.hd - 9 - AvMain.hd, 0);
      Canvas.resetTrans(var1);
      if (OnScreen.isOngame) {
         OnScreen.paintTitle(var1, super.left, super.center, super.right);
      } else {
         super.paint(var1);
      }

   }

   public final void updateKey() {
      if (OnScreen.isOngame && Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

   }

   public final void update() {
      if (this.backMyScreen != null) {
         this.backMyScreen.update();
      }

      Scroll.gI().updateScroll(CameraList.cmtoY, CameraList.cmy);
   }

   private void onList(int var1, Vector var2, MyScreen var3) {
      if (Canvas.currentMyScreen != gI()) {
         this.backMyScreen = var3;
      }

      switch (this.focus) {
         case 0:
            isGetTypeHouse = true;
            friendL = var2;
            if (typeListFriend == 1) {
               MapScr.gI();
               MapScr.doRequestAddFriend(MapScr.focusP);
            } else if (typeListFriend == 2) {
               isGetTypeHouse = false;
               Canvas.startWaitDlg();
               AvatarService.gI().getTypeHouse(1);
            } else if (Canvas.currentMyScreen != this) {
               this.switchToMe();
            }

            typeListFriend = 0;
         case 1:
         case 2:
         case 3:
         case 4:
      }

      tempList = null;
      tempList = var2;
      if (this.focus != 5) {
         for(var1 = 0; var1 < tempList.size(); ++var1) {
            Avatar var4;
            (var4 = (Avatar)tempList.elementAt(var1)).initPet();
            var4.orderSeriesPath();
         }
      }

      this.selected = 0;
      super.right = this.k;
      this.setCam();
   }

   public final void setFriendList(boolean var1) {
      this.focus = 0;
      if (friendL == null) {
         Canvas.startWaitDlg();
         CasinoService.gI().requestFriendList();
      } else {
         this.backMyScreen = Canvas.currentMyScreen;
         this.setList(idFriendList);
         if (Canvas.currentMyScreen != this) {
            this.switchToMe();
         }
      }

      if (var1) {
         this.isAction = true;
         this.cmdSelected = new Command(T.selectt, 4);
         if (Canvas.stypeInt == 0) {
            super.center = this.cmdSelected;
         }
      }

   }

   public static Avatar getAvatar(int var0) {
      int var1 = friendL.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         Avatar var3;
         if ((var3 = (Avatar)friendL.elementAt(var2)).IDDB == var0) {
            return var3;
         }
      }

      return null;
   }

   public final boolean setList(String var1) {
      byte[] var2 = (byte[])((byte[])hList.get(var1));
      Canvas.endDlg();
      if (var2 == null) {
         return false;
      } else {
         this.readList(var2, var1);
         return true;
      }
   }

   public final void readList(byte[] var1, String var2) {
      String[] var3 = null;
      byte[] var4 = null;
      ByteArrayInputStream var15 = new ByteArrayInputStream(var1);
      DataInputStream var16 = new DataInputStream(var15);

      try {
         String var5 = var16.readUTF();
         int var6 = var16.readInt();
         int var7 = var16.readByte();
         byte var8 = var16.readByte();
         short var9 = var16.readShort();
         Vector var10 = new Vector();
         int var19;
         if (var7 == 0) {
            this.focus = 5;

            for(var7 = 0; var7 < var9; ++var7) {
               StringObj var11;
               (var11 = new StringObj()).dis = var16.readShort();
               var11.str = var16.readUTF();
               var11.str2 = var16.readUTF();
               var11.w2 = Canvas.fontChatB.getWidth(var11.str2);
               var10.addElement(var11);
            }
         } else {
            this.focus = 6;

            for(var7 = 0; var7 < var9; ++var7) {
               Avatar var18;
               (var18 = new Avatar()).direct = 0;
               var19 = var16.readByte();
               var18.seriPart = new Vector();

               for(int var13 = 0; var13 < var19; ++var13) {
                  var18.addSeri(new SeriPart(var16.readShort()));
               }

               var18.IDDB = var16.readInt();
               var18.idImg = var16.readShort();
               if (var2.equals(idFriendList)) {
                  var18.idWedding = var16.readShort();
                  var18.idStatus = var16.readShort();
               }

               var18.name = var16.readUTF();
               var18.text2 = var16.readUTF();
               var10.addElement(var18);
            }
         }

         byte var17;
         if ((var17 = var16.readByte()) > 0) {
            var3 = new String[var17];
            var4 = new byte[var17];

            for(var19 = 0; var19 < var17; ++var19) {
               var4[var19] = var16.readByte();
               var3[var19] = var16.readUTF();
            }
         }

         if (var2.equals(idFriendList)) {
            this.focus = 0;
         }

         gI().onList(this.focus, var10, Canvas.currentMyScreen);
         this.name = var5;
         this.setupPopupBox();
         if (Canvas.currentMyScreen != this) {
            this.switchToMe();
         }

         super.left = null;
         if (var17 > 0) {
            super.left = new Command(T.menu, new IActionListMenu(this, var2, var3, var6, var8, var4));
         }

         if (!this.isAction) {
            if (var2.equals(idFriendList)) {
               this.cmdSelected = new Command(T.sendMessage, 0);
               if (Canvas.stypeInt == 0) {
                  super.center = this.cmdSelected;
               }
            } else if (!this.isAction) {
               this.cmdSelected = new Command(T.selectt, new IActionReadList(this, var6, var8));
               if (Canvas.stypeInt == 0) {
                  super.center = this.cmdSelected;
               }
            }
         }

         this.isAction = false;
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public final void commandTab(int var1, int var2) {
      Avatar var4;
      switch (var1) {
         case 0:
            if (this.selected >= 0 && this.selected < tempList.size()) {
               var4 = (Avatar)tempList.elementAt(this.selected);
               MessageScr.gI().doAction(var4.IDDB, var4.name);
               MessageScr.gI().switchToMe(this.backMyScreen);
               return;
            }
            break;
         case 1:
            super.center = null;
            super.right = null;
            super.left = null;
            tempList = null;
            Canvas.cameraList.isShow = false;
            if (this.backMyScreen != null) {
               this.backMyScreen.switchToMe();
               return;
            }

            MapScr.gI().switchToMe();
            break;
         case 2:
            return;
         case 3:
            var4 = (Avatar)tempList.elementAt(gI().selected);
            AvatarService.gI().doJoinHouse(var4.IDDB);
            Canvas.startWaitDlg();
            return;
         case 4:
            Canvas.startWaitDlg();
            FarmScr.gI().doJoinFarm(((Avatar)friendL.elementAt(this.selected)).IDDB, true);
      }

   }

   public final void initSelectCmd() {
      this.cmdSelected = new Command(T.selectt, 3);
      if (Canvas.stypeInt == 0) {
         super.center = this.cmdSelected;
      }

   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 50:
            CasinoService.gI().requestFriendList();
         default:
      }
   }

   public static void removeList() {
      hList.remove(idFriendList);
      friendL = null;
   }

   static boolean getisAction(ListScr var0) {
      return var0.isAction;
   }
}
