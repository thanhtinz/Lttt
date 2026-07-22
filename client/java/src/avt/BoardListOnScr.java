package avt;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class BoardListOnScr extends MyScreen {
   public static BoardListOnScr me;
   public static byte STYLE_2PLAYER = 0;
   public static byte STYLE_4PLAYER = 1;
   public static byte STYLE_5PLAYER = 2;
   public static byte type;
   public static FrameImage imgBoard;
   private static Image imgTitleBoard;
   private static Image imgNumPlayer;
   private static Image imgPlay;
   private static Image imgLock;
   private int nBoardPerLine;
   Vector boardList;
   private int p;
   private int q;
   public byte roomID;
   private short wSmall;
   private Command cmdSellect;
   public static Image imgSelectBoard;
   int j;

   public static BoardListOnScr gI() {
      return me == null ? (me = new BoardListOnScr()) : me;
   }

   public final void switchToMe() {
      OnScreen.disableVirtualKey();
      MyScreen.repaint();
      super.selected_ = 0;
      Canvas.paint.setValue(type);
      if (imgTitleBoard == null) {
         try {
            imgTitleBoard = Image.createImage(T.getPath() + "/on/imgkhungsoban.on");
            imgNumPlayer = Image.createImage(T.getPath() + "/on/imgNumPlayer.on");
            imgPlay = Image.createImage(T.getPath() + "/on/imgPlay.on");
            imgLock = Image.createImage(T.getPath() + "/on/imgLock.on");
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      Canvas.load = 1;
      super.isHide_ = true;
      GameMidlet.avatar.ableShow = false;
      super.switchToMe();
   }

   public BoardListOnScr() {
      this.cmdSellect = new Command(T.selectt, 1);
      super.right = new Command(T.close, 2);
      if (Canvas.stypeInt != 0) {
         super.center = new Command(T.strongest, 5);
      } else {
         super.center = this.cmdSellect;
      }

      super.left = new Command(T.menu, 6);
      this.wSmall = (short)(110 * AvMain.hd);
      if (Canvas.stypeInt == 1) {
         this.wSmall = 95;
      } else if (Canvas.stypeInt == 0) {
         this.wSmall = (short)(Canvas.w / 4);
         if (this.wSmall < 70) {
            this.wSmall = (short)(Canvas.w / 3);
         }

         if (Canvas.w < 180) {
            this.wSmall = (short)(Canvas.w / 2);
         }
      }

      this.nBoardPerLine = Canvas.w / this.wSmall + 1;
      if (this.nBoardPerLine * this.wSmall > Canvas.w - this.wSmall / 2) {
         --this.nBoardPerLine;
      }

      this.p = this.wSmall / 2;
      this.q = this.wSmall / 2;
      this.q += 10;
      if (Canvas.w > this.nBoardPerLine * this.wSmall) {
         this.p = (Canvas.w - this.nBoardPerLine * this.wSmall) / 2 + this.wSmall / 2;
      }

   }

   public final void close() {
      Canvas.startWaitDlg();
      doExitBoardList();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 1:
            BoardInfo var4 = (BoardInfo)this.boardList.elementAt(super.selected_);
            if (MapScr.isNewVersion && var4.money > GameMidlet.avatar.money[3]) {
               gI().setXeng();
            } else {
               if (!var4.isPass) {
                  CasinoService.gI().joinBoard(this.roomID, var4.boardID, "");
                  Canvas.startWaitDlg();
                  return;
               }

               Canvas.inputDlg.setImg(T.ifPassword, new IActionJoinBoard(this), 2);
            }
            break;
         case 2:
            doExitBoardList();
            return;
         case 3:
            this.commandActionPointer(1, -1);
            return;
         case 4:
            this.doAskForBoardToGo();
            return;
         case 5:
            Canvas.startWaitDlg();
            CasinoService.gI().joinAnyBoard();
            return;
         case 6:
            Vector var3;
            (var3 = new Vector()).addElement(new Command(T.strongest, 5));
            var3.addElement(new Command("Đến bàn", 6));
            var3.addElement(MapScr.gI().f);
            var3.addElement(new Command(T.viewMyInfo, 7));
            Menu.gI().startAt(var3, 0);
      }

   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 1:
            Canvas.startWaitDlg(T.pleaseWait);
            CasinoService.gI().requestBoardList(this.roomID);
            return;
         case 3:
            Canvas.startWaitDlg();
            GlobalService.gI().requestInfoOf(GameMidlet.avatar.IDDB);
            return;
         case 4:
            doExitBoardList();
            return;
         case 5:
            Canvas.startWaitDlg();
            CasinoService.gI().joinAnyBoard();
            return;
         case 6:
            this.doAskForBoardToGo();
            return;
         case 7:
            Canvas.startWaitDlg();
            GlobalService.gI().requestInfoOf(GameMidlet.avatar.IDDB);
         case 2:
         default:
      }
   }

   private void doAskForBoardToGo() {
      Canvas.inputDlg.setImg(T.goToBoard, new IActionToGo(this), 3);
   }

   protected final void doAskForPass() {
      Canvas.inputDlg.setImg(T.banChiCo, new IActionPass(this), 0);
   }

   public final void setXeng() {
      Canvas.startOKDlg("Hiện tại bạn không đủ Xèng để tham gia màn chơi, bạn có muốn nạp thêm Xèng không?", new IActionXeng(this));
   }

   private static void doExitBoardList() {
      Canvas.cameraList.isShow = false;
      CasinoService.gI().requestRoomList();
      Canvas.startWaitDlg();
   }

   public final void paint(Graphics var1) {
      Canvas.resetTrans(var1);
      RoomListOnScr.paintRoomList(var1, "Phòng " + RoomListOnScr.title + " " + this.roomID);
      this.paintBoardList(var1);
      OnScreen.paintTitle(var1, super.left, super.center, super.right);
      Canvas.paintPlus2(var1);
   }

   private void paintBoardList(Graphics var1) {
      var1.translate(this.p, this.q);
      var1.translate(0, -CameraList.cmtoY);
      int var2;
      if ((var2 = CameraList.cmtoY / this.wSmall * this.nBoardPerLine - this.nBoardPerLine) < 0) {
         var2 = 0;
      }

      int var3;
      if ((var3 = var2 + Canvas.h / this.wSmall * this.nBoardPerLine + (this.nBoardPerLine << 1) + this.nBoardPerLine) > this.boardList.size()) {
         var3 = this.boardList.size();
      }

      for(var2 = var2; var2 < var3; ++var2) {
         int var4 = var2 % this.nBoardPerLine * this.wSmall;
         int var5 = var2 / this.nBoardPerLine * this.wSmall;
         BoardInfo var6 = (BoardInfo)this.boardList.elementAt(var2);
         if ((!Canvas.isKeyBoard || !super.isHide_) && var2 == super.selected_) {
            var1.drawImage(imgSelectBoard, var4, var5, 3);
         }

         imgBoard.drawFrame(var6.nPlayer, var4, var5, 0, 3, var1);
         var1.drawImage(imgTitleBoard, var4 - this.wSmall / 4, var5 - 30 * AvMain.hd, 3);
         Canvas.smallFontYellow.drawString(var1, "" + var6.boardID, var4 - this.wSmall / 4, var5 - 30 * AvMain.hd - AvMain.hSmall / 2, 2);
         if (var6.money > 0) {
            Canvas.smallFontYellow.drawString(var1, var6.strMoney, var4, var5 - 30 * AvMain.hd - AvMain.hSmall / 2, 2);
         }

         if (type == STYLE_4PLAYER && var6.maxPlayer < 4) {
            var1.drawImage(imgNumPlayer, var4 + this.wSmall / 4, var5 - 30 * AvMain.hd, 3);
            Canvas.smallFontRed.drawString(var1, "" + var6.maxPlayer, var4 + this.wSmall / 4, var5 - 30 * AvMain.hd - AvMain.hSmall / 2, 2);
         }

         if (var6.isPlaying) {
            var1.drawImage(imgNumPlayer, var4 - this.wSmall / 4, var5 + this.wSmall / 3, 3);
            var1.drawImage(imgPlay, var4 - this.wSmall / 4, var5 + this.wSmall / 3, 3);
         }

         if (var6.isPass) {
            var1.drawImage(imgNumPlayer, var4 + this.wSmall / 4, var5 + this.wSmall / 3, 3);
            var1.drawImage(imgLock, var4 + this.wSmall / 4, var5 + this.wSmall / 3, 3);
         }
      }

   }

   public final void updateKey() {
      if (OnScreen.isOngame && Canvas.stypeInt != 0) {
         Canvas.paint.updateKeyOn(super.left, super.center, super.right);
      } else {
         super.updateKey();
      }

   }

   public final void setBoardList(Vector var1) {
      this.boardList = var1;
   }

   public final void init() {
      int var1 = this.boardList.size() / this.nBoardPerLine;
      if (this.boardList.size() % this.nBoardPerLine != 0) {
         ++var1;
      }

      this.q = 100 * AvMain.hd;
      if (Canvas.w < 200) {
         this.q = 50;
      }

      Canvas.cameraList.setInfo(this.p - this.wSmall / 2, this.q - this.wSmall / 2, this.wSmall, this.wSmall, this.nBoardPerLine * this.wSmall, var1 * this.wSmall + 10, this.nBoardPerLine * this.wSmall, Canvas.h - (this.q - this.wSmall / 2) - 4, this.boardList.size());
   }

   public final void setSelected(int var1, boolean var2) {
      if (var2 && super.selected_ == var1 && this.cmdSellect != null) {
         this.cmdSellect.perform();
      }

      if (var1 >= 0 && var1 < this.boardList.size()) {
         super.setSelected(var1, var2);
      }

   }

   public final void update() {
   }

   static {
      type = STYLE_4PLAYER;
   }
}
