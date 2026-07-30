package avt;

import java.io.IOException;
import java.util.Vector;

public final class CasinoService extends IService {
   private static CasinoService instance;

   public static CasinoService gI() {
      if (instance == null) {
         instance = new CasinoService();
      }

      return instance;
   }

   public final void requestRoomList() {
      this.createMessage((byte)6);
      this.sendMessage();
   }

   public final void requestBoardList(byte var1) {
      this.createMessage((byte)7);
      this.writeByte(var1);
      this.sendMessage();
   }

   public final void setMaxPlayer(int var1) {
      try {
         this.createMessageWithBoard((byte)56);
         super.m.writer().writeByte(var1);
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void joinBoard(byte var1, byte var2, String var3) {
      this.createMessage((byte)8);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeUTF(var3);
      } catch (IOException var5) {
      }

      this.sendMessage();
   }

   public final void joinAnyBoard() {
      this.createMessage((byte)28);
      this.sendMessage();
   }

   public final void requestFriendList() {
      this.createMessage((byte)-18);
      this.sendMessage();
   }

   public final void move(byte[] var1) {
      try {
         this.createMessageWithBoard((byte)21);
         super.m.writer().writeByte(var1.length);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            super.m.writer().write(var1[var2]);
         }
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void skipTurn() {
      try {
         this.createMessageWithBoard((byte)49);
      } catch (Exception var2) {
      }

      this.sendMessage();
   }

   public final void haBai() {
      try {
         this.createMessageWithBoard((byte)21);
      } catch (Exception var2) {
      }

      this.sendMessage();
   }

   public final void createMessageWithBoard(byte var1) throws IOException {
      super.createMessage(var1);
      super.m.writer().writeByte(BoardScr.roomID);
      super.m.writer().writeByte(BoardScr.boardID);
   }

   public final void moveCo(byte var1) {
      try {
         this.createMessageWithBoard((byte)21);
         super.m.writer().writeByte(var1);
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void GetCardPhom() {
      try {
         this.createMessageWithBoard((byte)63);
      } catch (Exception var2) {
      }

      this.sendMessage();
   }

   public final void eatCardPhom(int[] var1, byte var2) {
      try {
         this.createMessageWithBoard((byte)64);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            super.m.writer().writeByte(var1[var3]);
         }

         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void HaPhomPhom(Card[] var1) {
      try {
         this.createMessageWithBoard((byte)65);
         byte var2 = -1;

         for(int var3 = 0; var3 < 10; ++var3) {
            if (var1[var3].phom != 0) {
               if (var1[var3].phom != var2 && var2 != -1) {
                  super.m.writer().writeByte(-1);
               }

               var2 = var1[var3].phom;
               super.m.writer().writeByte(var1[var3].cardID);
            } else if (var2 != -1) {
               super.m.writer().writeByte(-1);
               var2 = -1;
            }
         }
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void doAddCardPhom(int[] var1) {
      try {
         this.createMessageWithBoard((byte)67);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] != -1) {
               super.m.writer().writeByte(var1[var2]);
            }
         }
      } catch (Exception var3) {
      }

      this.sendMessage();
   }

   public final void doResetPhomEatPhom(int[] var1, int var2) {
      try {
         this.createMessageWithBoard((byte)68);
         super.m.writer().writeByte(var2);

         for(var2 = 0; var2 < 5 && var1[var2] != -1; ++var2) {
            super.m.writer().writeByte(var1[var2]);
         }
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void chatToBoard(String var1) {
      try {
         this.createMessageWithBoard((byte)9);
         super.m.writer().writeUTF(var1);
      } catch (IOException var3) {
      }

      this.sendMessage();
   }

   public final void leaveBoard() {
      try {
         this.createMessageWithBoard((byte)15);
      } catch (IOException var2) {
      }

      this.sendMessage();
   }

   public final void ready(boolean var1) {
      try {
         this.createMessageWithBoard((byte)16);
         super.m.writer().writeBoolean(var1);
      } catch (IOException var3) {
      }

      this.sendMessage();
   }

   public final void setMoney(int var1) {
      try {
         this.createMessageWithBoard((byte)19);
         super.m.writer().writeInt(var1);
      } catch (IOException var3) {
      }

      this.sendMessage();
   }

   public final void setPassword(String var1) {
      try {
         this.createMessageWithBoard((byte)18);
         super.m.writer().writeUTF(var1);
      } catch (IOException var3) {
      }

      this.sendMessage();
   }

   public final void kick(int var1) {
      try {
         this.createMessageWithBoard((byte)11);
         super.m.writer().writeInt(var1);
      } catch (IOException var3) {
      }

      this.sendMessage();
   }

   public final void startGame() {
      try {
         this.createMessageWithBoard((byte)20);
      } catch (IOException var2) {
      }

      this.sendMessage();
   }

   public final void createCell(Point[][] var1) {
      try {
         this.createMessageWithBoard((byte)64);

         for(int var2 = 0; var2 < 8; ++var2) {
            for(int var3 = 0; var3 < 8; ++var3) {
               if (var1[var2][var3].isRemove) {
                  var1[var2][var3].isRemove = false;
                  super.m.writer().writeByte((var2 << 3) + var3);
               }
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      this.sendMessage();
   }

   public final void doMoveDiamond(int var1, int var2) {
      try {
         this.createMessageWithBoard((byte)21);
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.sendMessage();
   }

   public final void doOutPath() {
      try {
         this.createMessageWithBoard((byte)24);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.sendMessage();
   }

   public final void PutMoneyOk(Vector var1) {
      try {
         this.createMessageWithBoard((byte)21);
         if (var1.size() > 0) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
               PimgBC var3 = (PimgBC)var1.elementAt(var2);
               super.m.writer().writeByte(var3.moneyPut);
               var3.moneyPut = 0;
            }
         }
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void ta(byte var1, byte var2) {
      try {
         this.createMessageWithBoard((byte)65);
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var4) {
      }

      this.sendMessage();
   }

   public final void skip() {
      try {
         this.createMessageWithBoard((byte)49);
      } catch (Exception var2) {
      }

      this.sendMessage();
   }
}
