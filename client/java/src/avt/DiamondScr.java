package avt;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class DiamondScr extends BoardScr {
   public static DiamondScr me_;
   private Point[][] array = new Point[8][8];
   private int x;
   private int y;
   private int wCell;
   private int isSelected;
   private int clearCount = -1;
   private int xPlayer1;
   private int xPlayer2;
   private byte countHit = -1;
   private Vector listFireWork = new Vector();
   private boolean isPath = false;
   private boolean isTrans = false;
   private Command cmdSelected;
   private Command cmdSkip;
   private FrameImage imgFireWork;
   private byte wImg;
   private int V = 0;
   public int idWin = -1;
   private boolean isEnd = false;
   public boolean c = false;
   private boolean isInit = false;
   private int[][] xCheck = new int[][]{{1, -2}, {1, -1}, {1, -1}, {1, 3}, {1, 2}, {1, 2}, new int[2], {0, -1}, {0, 1}, new int[2], {0, -1}, {0, 1}, {-1, 1}, {-1, 1}, {-1, -1}, {1, 1}};
   private int[][] yCheck = new int[][]{new int[2], {0, -1}, {0, 1}, new int[2], {0, -1}, {0, 1}, {1, 3}, {1, 2}, {1, 2}, {1, -2}, {1, -1}, {1, -1}, {-1, -1}, {1, 1}, {-1, 1}, {-1, 1}};
   private int[][] xSetSelected = new int[][]{{-1, -2}, new int[2], {1, 2}, new int[2], {-1, 1}, new int[2]};
   private int[][] ySetSelected = new int[][]{new int[2], {-1, -2}, new int[2], {1, 2}, new int[2], {-1, 1}};
   private boolean isTranCam = false;
   private int hhFill;
   private Vector aE = new Vector();
   private boolean isMove = false;
   private boolean ableMove = false;

   public static DiamondScr gI() {
      return me_ == null ? (me_ = new DiamondScr()) : me_;
   }

   public DiamondScr() {
      this.cmdSelected = new Command(T.selectt, 20);
      this.cmdSkip = new Command(T.skip, 21);
      FilePack.b(T.ax);
      this.imgFireWork = FrameImage.init("st", 11 * AvMain.hd, 11 * AvMain.hd);
      FilePack.reset();
   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 20:
            if (!this.isPath) {
               if (this.isSelected == -1 && super.center == this.cmdSelected && super.turn == GameMidlet.avatar.IDDB && !this.isTrans) {
                  this.isSelected = this.V;
               } else {
                  this.isSelected = -1;
               }
            }
            break;
         case 21:
            this.doSkip();
      }

      super.commandTab(var1, var2);
   }

   private void doSkip() {
      CasinoService var1 = CasinoService.gI();

      try {
         var1.createMessageWithBoard((byte)49);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      var1.sendMessage();
      super.turn = -1;
      super.center = BoardScr.cmdWaiting;
      super.right = null;
   }

   public final void init() {
      super.init();
      if (Canvas.hCan > 250) {
         this.wCell = 24 * AvMain.hd;
         this.wImg = (byte)(24 * AvMain.hd);
      } else {
         this.wCell = 16;
         this.wImg = 16;
      }

      this.hhFill = 40 * AvMain.hd;
      if (this.countHit == -1 || !BoardScr.isStartGame) {
         this.x = (Canvas.w - (this.wCell << 3)) / 2;
      }

      this.y = this.wCell / 2 + 2;
      if (Canvas.w < 160) {
         this.y = 0;
      }

   }

   public final void start(int var1, int var2, byte[][] var3) {
      MyScreen.repaint();
      super.start();
      this.isEnd = false;
      super.turn = var1;
      BoardScr.interval = var2;
      super.center = null;
      super.right = null;
      this.idWin = -1;
      BoardScr.dieTime = System.currentTimeMillis() + (long)(BoardScr.interval * 1000);
      if (GameMidlet.avatar.IDDB == super.turn) {
         this.isInit = true;
      }

      this.init();
      this.setPosPlaying();
      BoardScr.isStartGame = true;
      this.isSelected = -1;
      this.setArray(var3);
      Canvas.endDlg();
   }

   public final void setPosPlaying() {
      AvCamera.gI().setPos(0, 0);
      int var1 = this.x;
      int var2 = this.wCell << 3;
      if (Canvas.hCan < 250) {
         var2 = Canvas.w - 50;
         var1 = 25;
      }

      for(int var3 = 0; var3 < BoardScr.numPlayer; ++var3) {
         Avatar var4;
         if ((var4 = (Avatar)BoardScr.avatarInfos.elementAt(var3)).IDDB != -1) {
            if (var4.IDDB != GameMidlet.avatar.IDDB) {
               LoadMap.addPlayer(var4);
            }

            var4.yCur = var4.y = Canvas.hCan - Canvas.hTab - AvMain.hSmall / 2;
            if (var4.y < this.wCell << 3 && (var1 = this.x - this.hhFill - 15 * AvMain.hd) < 0) {
               var1 = 0;
            }

            if (Canvas.w < 160) {
               var4.yCur = var4.y = Canvas.hCan - 10;
            }

            if (Canvas.stypeInt == 0 && Canvas.w > 200) {
               var4.yCur = var4.y -= 10;
            }

            if (var4.IDDB == GameMidlet.avatar.IDDB) {
               this.xPlayer1 = var1 + 15 * AvMain.hd + this.hhFill;
               var4.xCur = var4.x = this.xPlayer1;
               var4.direct = var4.dirLast = 0;
            } else {
               this.xPlayer2 = var1 + var2 - 15 * AvMain.hd - this.hhFill;
               var4.xCur = var4.x = this.xPlayer2;
               var4.direct = var4.dirLast = Base.LEFT;
            }

            var4.ySat = 0;
            var4.setAction((byte)0);
            var4.setFrame(var4.action);
         }
      }

   }

   private void setArray(byte[][] var1) {
      boolean var2 = false;
      this.isTrans = true;

      for(int var3 = 7; var3 >= 0; --var3) {
         int var5 = 20;

         for(int var4 = 7; var4 >= 0; --var4) {
            this.array[var3][var4] = new Point(var4 * this.wCell, var3 * this.wCell, var1[var3][var4]);
            this.array[var3][var4].color = this.array[var3][var4].y;
            this.array[var3][var4].h = -var5;
            --var5;
            this.array[var3][var4].isFire = true;
            this.array[var3][var4].y = -(var4 * this.wCell + 24);
         }
      }

   }

   public final void update() {
      super.update();
      if (!BoardScr.isStartGame && !BoardScr.disableReady) {
         this.updateReady();
      } else {
         if (BoardScr.dieTime != 0L && (BoardScr.currentTime = System.currentTimeMillis()) > BoardScr.dieTime) {
            BoardScr.dieTime = 0L;
            if (super.turn == GameMidlet.avatar.IDDB && super.center == this.cmdSelected) {
               this.doSkip();
            }
         }

         boolean var1 = false;
         boolean var2 = false;

         int var3;
         int var6;
         for(var3 = 63; var3 >= 0; --var3) {
            if (this.array[var3 / 8][var3 % 8] != null && this.array[var3 / 8][var3 % 8].catagory == 1) {
               Point var4;
               if ((var4 = this.array[var3 / 8][var3 % 8]).x == var4.xTo && var4.y == var4.yTo) {
                  var6 = -1;
               } else if (Math.abs((var4.xTo - var4.x) / 2) <= 1 && Math.abs((var4.yTo - var4.y) / 2) <= 1) {
                  var4.x = var4.xTo;
                  var4.y = var4.yTo;
                  var6 = 0;
               } else {
                  if (var4.x != var4.xTo) {
                     var4.x += (var4.xTo - var4.x) / 2;
                  }

                  if (var4.y != var4.yTo) {
                     var4.y += (var4.yTo - var4.y) / 2;
                  }

                  var6 = CRes.distance(var4.x, var4.y, var4.xTo, var4.yTo) <= var4.distant / 5 ? 2 : 1;
               }

               if (var6 == -1) {
                  this.array[var3 / 8][var3 % 8].catagory = 0;
                  var2 = true;
               } else {
                  var1 = true;
               }
            }
         }

         if (var2 && this.isPath) {
            if (!this.setSelectedc(this.V) && !this.setSelectedc(this.isSelected)) {
               var3 = this.V;
               this.V = this.isSelected;
               this.isSelected = var3;
               this.change();
               super.center = this.cmdSelected;
               super.right = this.cmdSkip;
            } else if (super.turn == GameMidlet.avatar.IDDB) {
               CasinoService.gI().doMoveDiamond(this.isSelected, this.V);
            }

            this.isPath = false;
            this.isSelected = -1;
         }

         Point var7;
         int var5;
         if (!var1) {
            boolean var9 = false;
            var5 = 63;

            while(true) {
               if (var5 < 0) {
                  if (!var9 && this.isTrans) {
                     if (super.turn == GameMidlet.avatar.IDDB) {
                        if (!this.isInit) {
                           if (this.ableMove) {
                              this.setPath();
                           }
                        } else if (this.setOutPath()) {
                           super.center = this.cmdSelected;
                           super.right = this.cmdSkip;
                        } else {
                           CasinoService.gI().doOutPath();
                        }

                        this.isInit = false;
                     }

                     this.isTrans = false;
                  }
                  break;
               }

               if (this.array[var5 / 8][var5 % 8] != null && this.array[var5 / 8][var5 % 8].isFire) {
                  var7 = this.array[var5 / 8][var5 % 8];
                  var7.x += this.array[var5 / 8][var5 % 8].g;
                  if (this.array[var5 / 8][var5 % 8].g > 1 || this.array[var5 / 8][var5 % 8].g < -1) {
                     var7 = this.array[var5 / 8][var5 % 8];
                     var7.g -= this.array[var5 / 8][var5 % 8].g / CRes.abs(this.array[var5 / 8][var5 % 8].g);
                  }

                  var7 = this.array[var5 / 8][var5 % 8];
                  var7.y += this.array[var5 / 8][var5 % 8].h;
                  var7 = this.array[var5 / 8][var5 % 8];
                  var7.h += 2;
                  if (this.array[var5 / 8][var5 % 8].y >= this.array[var5 / 8][var5 % 8].color) {
                     this.array[var5 / 8][var5 % 8].y = this.array[var5 / 8][var5 % 8].color;
                     this.array[var5 / 8][var5 % 8].isFire = false;
                  } else {
                     var9 = true;
                  }
               }

               --var5;
            }
         }

         if (this.clearCount != -1) {
            if (this.clearCount % 10 == 0) {
               DiamondScr var10 = this;

               for(var5 = 4 - this.clearCount / 10; var5 < 4 + var10.clearCount / 10; ++var5) {
                  for(var6 = 4 - var10.clearCount / 10; var6 < 4 + var10.clearCount / 10; ++var6) {
                     var10.addFire(var10.array[var5][var6].x + 12, var10.array[var5][var6].y + 12, var10.array[var5][var6].itemID);
                     var10.array[var5][var6].itemID = -1;
                  }
               }
            }

            this.clearCount += 2;
            if (this.clearCount >= 50) {
               this.createPoint();
               this.clearCount = -1;
            }
         }

         for(var3 = 0; var3 < this.listFireWork.size(); ++var3) {
            if ((var7 = (Point)this.listFireWork.elementAt(var3)).limitY > 0) {
               ++var7.limitY;
               if (var7.limitY == 3) {
                  this.listFireWork.removeElement(var7);
                  continue;
               }
            }

            if (!var7.isFire) {
               if (CRes.abs((var6 = CRes.tan(var7.xTo - var7.x, -(var7.yTo - var7.y))) - var7.h) > 10) {
                  var7.h -= var7.height * var7.catagory;
                  var7.h = CRes.fixangle(var7.h);
               } else {
                  var7.h = var6;
                  var7.dis = (byte)(var7.dis + 2);
               }

               if (var7.color >= 4) {
                  var7.color = 0;
               }

               ++var7.color;
               int var11 = var7.dis * CRes.cos(var7.h) >> 10;
               var6 = -(var7.dis * CRes.sin(var7.h)) >> 10;
               if (CRes.distance(var7.x, var7.y, var7.xTo, var7.yTo) >= var7.dis) {
                  var7.x += var11;
                  var7.y += var6;
               } else {
                  this.listFireWork.removeElement(var7);
               }
            } else {
               var7.x += var7.g;
               if (var7.g > 1 || var7.g < -1) {
                  var7.g -= var7.g / CRes.abs(var7.g);
               }

               var7.y += var7.h;
               ++var7.h;
               if (var7.catagory == 1 && var7.color < 19) {
                  ++var7.color;
               }

               if (var7.y + this.y > Canvas.h) {
                  this.listFireWork.removeElement(var7);
               }
            }
         }

         for(var3 = 0; var3 < 2; ++var3) {
            Avatar var8;
            if ((var8 = (Avatar)BoardScr.avatarInfos.elementAt(var3)).task == -1 && CRes.abs(var8.xCur - var8.x) < 10) {
               if (this.countHit == -2) {
                  this.countHit = -1;
                  var8.task = 0;
                  if (var8.IDDB == this.idWin) {
                     var8.doAction((byte)10);
                     var8.setFeel(10);
                  } else {
                     var8.action = 0;
                     if (this.idWin != -1) {
                        var8.setFeel(9);
                     }
                  }

                  this.c = false;
                  if (var8.IDDB == GameMidlet.avatar.IDDB) {
                     var8.direct = 0;
                  }
               } else if (var8.task == -1) {
                  if (var8.isNo && Canvas.gameTick % 6 == 3) {
                     this.c(var8.x, var8.y - var8.height, 0);
                  }

                  if (this.countHit != -1) {
                     if (this.countHit >= 0) {
                        --this.countHit;
                        if (this.countHit == -1) {
                           this.countHit = -2;
                           if (var8.IDDB == GameMidlet.avatar.IDDB) {
                              var8.xCur = this.xPlayer1;
                           } else {
                              var8.xCur = this.xPlayer2;
                           }
                        }
                     }
                  } else {
                     for(var6 = 0; var6 < 2; ++var6) {
                        Avatar var12;
                        if ((var12 = (Avatar)BoardScr.avatarInfos.elementAt(var6)).IDDB != var8.IDDB) {
                           var12.setFeel(20);
                           var12.action = 4;
                           var12.ableShow = true;
                           var8.ableShow = true;
                        }
                     }

                     this.countHit = 20;
                     if (this.c) {
                        this.countHit = 30;
                     }
                  }
               }
            }

            if (var8.plusHP > 0) {
               var6 = var8.maxHP / 100 + 1;
               if (var8.plusHP - var6 < 0) {
                  var6 = var8.plusHP;
               }

               var8.plusHP = (short)(var8.plusHP - var6);
               var8.hp = (short)(var8.hp + var6);
            } else if (var8.plusHP < 0) {
               var6 = var8.maxHP / 100 + 1;
               if (var8.plusHP + var6 > 0) {
                  var6 = -var8.plusHP;
               }

               var8.hp = (short)(var8.hp - var6);
               var8.plusHP = (short)(var8.plusHP + var6);
            }

            if (var8.plusMP > 0) {
               var6 = var8.maxHP / 100 + 1;
               if (var8.plusMP - var6 < 0) {
                  var6 = var8.plusMP;
               }

               var8.plusMP = (short)(var8.plusMP - var6);
               var8.mp = (short)(var8.mp + var6);
            } else if (var8.plusMP < 0) {
               var6 = var8.maxHP / 100 + 1;
               if (var8.plusMP + var6 > 0) {
                  var6 = -var8.plusMP;
               }

               var8.mp = (short)(var8.mp - var6);
               var8.plusMP = (short)(var8.plusMP + var6);
            }
         }

         for(var3 = 0; var3 < this.aE.size(); ++var3) {
            --(var7 = (Point)this.aE.elementAt(var3)).limitY;
            if (var7.limitY <= 0) {
               this.aE.removeElement(var7);
            }
         }
      }

   }

   private boolean setOutPath() {
      for(int var1 = 0; var1 < 8; ++var1) {
         for(int var2 = 0; var2 < 8; ++var2) {
            for(int var3 = 0; var3 < this.xCheck.length; ++var3) {
               if (var1 + this.yCheck[var3][0] >= 0 && var1 + this.yCheck[var3][0] < 8 && var1 + this.yCheck[var3][1] >= 0 && var1 + this.yCheck[var3][1] < 8 && var2 + this.xCheck[var3][0] >= 0 && var2 + this.xCheck[var3][0] < 8 && var2 + this.xCheck[var3][1] >= 0 && var2 + this.xCheck[var3][1] < 8 && this.array[var1][var2].itemID == this.array[var1 + this.yCheck[var3][0]][var2 + this.xCheck[var3][0]].itemID && this.array[var1][var2].itemID == this.array[var1 + this.yCheck[var3][1]][var2 + this.xCheck[var3][1]].itemID) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void addFire(int var1, int var2, int var3) {
      Avatar var4;
      if (var3 != -1 && (var4 = BoardScr.getAvatarByID(super.turn)) != null) {
         int var5 = 0;
         int var6 = 0;
         switch (var3) {
            case 0:
               this.c(var1 + this.x, var2 + this.y, 0);
               return;
            case 1:
               var5 = var4.x;
               var6 = var4.y - var4.height / 2;
               if (var4.an > 0) {
                  if (var4.IDDB == GameMidlet.avatar.IDDB) {
                     var5 = this.xPlayer1 - 20 - 7;
                  } else {
                     var5 = this.xPlayer2 + 7 + 20;
                  }

                  var6 = var4.y - 22;
               }
               break;
            case 2:
               if (var4.IDDB == GameMidlet.avatar.IDDB) {
                  var5 = this.xPlayer1 - 20 - this.hhFill + var4.hp * this.hhFill / var4.maxHP;
               } else {
                  var5 = this.xPlayer2 + (this.hhFill - var4.hp * this.hhFill / var4.maxHP) + 20 - var4.hp * this.hhFill / var4.maxHP;
               }

               var6 = var4.y - 2 - 10 * AvMain.hd;
               break;
            case 3:
               if (var4.IDDB == GameMidlet.avatar.IDDB) {
                  var5 = this.xPlayer1 - 20 - this.hhFill + var4.mp * this.hhFill / var4.maxMP;
               } else {
                  var5 = this.xPlayer2 + (this.hhFill - var4.mp * this.hhFill / var4.maxMP) + 20 - var4.hp * this.hhFill / var4.maxHP;
               }

               var6 = var4.y - 5 * AvMain.hd;
               break;
            case 4:
               this.c(var1 + this.x, var2 + this.y, 4);
               return;
            case 5:
               return;
         }

         Point var10;
         (var10 = new Point(var1 + this.x, var2 + this.y)).limitY = 1;
         this.listFireWork.addElement(var10);

         for(int var11 = 0; var11 < (var3 != 1 ? 3 : 1); ++var11) {
            Point var7;
            (var7 = new Point(var1 + this.x, var2 + this.y)).distant = (short)var3;
            var7.color = CRes.rnd(3);
            int var8 = CRes.tan(var5 - var1, -(var6 - var2));
            var7.g = var8;
            var7.catagory = (byte)CRes.rnd(-1, 1);
            var7.h = CRes.fixangle(var7.g + var7.catagory * 90);
            var8 = 10 * CRes.cos(var7.h) >> 10;
            int var9 = -(10 * CRes.sin(var7.h)) >> 10;
            var7.xTo = (short)var5;
            var7.yTo = (short)var6;
            var7.x += var8;
            var7.y += var9;
            var7.color = 0;
            var7.dis = (byte)(CRes.rnd(4) + 4);
            var7.height = (short)(10 + CRes.rnd(5));
            this.listFireWork.addElement(var7);
         }
      }

   }

   private void c(int var1, int var2, int var3) {
      if (var3 != -1) {
         Point var4;
         (var4 = new Point(var1, var2)).limitY = 1;
         this.listFireWork.addElement(var4);

         for(int var7 = 0; var7 < 3; ++var7) {
            int var5 = CRes.rnd(-1, 1);
            Point var6;
            (var6 = new Point(var1, var2)).isFire = true;
            var6.color = CRes.rnd(3);
            var6.g = var5 * (CRes.rnd(100) / 10);
            var6.h = -CRes.rnd(100) / 10;
            var6.dis = (byte)var3;
            var6.catagory = 1;
            var6.limitY = 0;
            this.listFireWork.addElement(var6);
         }
      }

   }

   private boolean setSelectedc(int var1) {
      if (this.isSelected != -1 && !this.isTrans) {
         for(int var2 = 0; var2 < this.xSetSelected.length; ++var2) {
            if (var1 / 8 + this.ySetSelected[var2][0] >= 0 && var1 / 8 + this.ySetSelected[var2][0] < 8 && var1 / 8 + this.ySetSelected[var2][1] >= 0 && var1 / 8 + this.ySetSelected[var2][1] < 8 && var1 % 8 + this.xSetSelected[var2][0] >= 0 && var1 % 8 + this.xSetSelected[var2][0] < 8 && var1 % 8 + this.xSetSelected[var2][1] >= 0 && var1 % 8 + this.xSetSelected[var2][1] < 8 && this.array[var1 / 8][var1 % 8].itemID == this.array[var1 / 8 + this.ySetSelected[var2][0]][var1 % 8 + this.xSetSelected[var2][0]].itemID && this.array[var1 / 8][var1 % 8].itemID == this.array[var1 / 8 + this.ySetSelected[var2][1]][var1 % 8 + this.xSetSelected[var2][1]].itemID) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public final void updateKey() {
      super.updateKey();
      int var1;
      int var2;
      if (Canvas.isPointerClick && Canvas.isPointer(this.x, this.y, this.wCell << 3, this.wCell << 3) && this.isSelected == -1) {
         Canvas.isPointerClick = false;
         this.isTranCam = true;
         var1 = (Canvas.px - this.x) / this.wCell;
         var2 = (Canvas.py - this.y) / this.wCell;
         this.V = (var2 << 3) + var1;
      }

      if (!this.isPath && !this.isTrans && super.center == this.cmdSelected && super.center != BoardScr.cmdWaiting && this.isTranCam) {
         if (Canvas.isPointerDown) {
            var1 = Canvas.dx();
            var2 = Canvas.dy();
            if (var1 < -this.wCell / 2) {
               if (this.V % 8 < 7) {
                  this.isSelected = this.V++;
                  this.isTranCam = false;
                  this.change();
               }
            } else if (var1 > this.wCell / 2) {
               if (this.V % 8 > 0) {
                  this.isSelected = this.V--;
                  this.isTranCam = false;
                  this.change();
               }
            } else if (var2 < -this.wCell / 2) {
               if (this.V / 8 < 7) {
                  this.isSelected = this.V;
                  this.V += 8;
                  this.isTranCam = false;
                  this.change();
               }
            } else if (var2 > this.wCell / 2 && this.V >= 8) {
               this.isSelected = this.V;
               this.V -= 8;
               this.isTranCam = false;
               this.change();
            }
         }

         if (Canvas.isPointerRelease) {
            Canvas.isPointerRelease = false;
            this.isTranCam = false;
         }
      }

      if (BoardScr.isStartGame && super.center != BoardScr.cmdWaiting) {
         if (Canvas.a(2)) {
            if (!this.isPath && !this.isTrans) {
               if (this.V >= 8) {
                  this.V -= 8;
               }

               this.change();
               return;
            }
         } else if (Canvas.a(4)) {
            if (!this.isPath && !this.isTrans) {
               if (this.V % 8 > 0) {
                  --this.V;
               }

               this.change();
               return;
            }
         } else if (Canvas.a(6)) {
            if (!this.isPath && !this.isTrans) {
               if (this.V % 8 < 7) {
                  ++this.V;
               }

               this.change();
               return;
            }
         } else if (Canvas.a(8) && !this.isPath && !this.isTrans) {
            if (this.V / 8 < 7) {
               this.V += 8;
            }

            this.change();
         }
      }

   }

   private void change() {
      if (this.isSelected != -1 && !this.isTrans) {
         super.center = BoardScr.cmdWaiting;
         super.right = null;
         this.isPath = true;
         this.isTranCam = false;
         Point var1 = this.array[this.V / 8][this.V % 8];
         Point var2 = this.array[this.isSelected / 8][this.isSelected % 8];
         int var3 = var1.x;
         int var4 = var1.y;
         short var5 = var1.itemID;
         var1.x = var2.x;
         var1.y = var2.y;
         var1.itemID = var2.itemID;
         var2.x = var3;
         var2.y = var4;
         var2.itemID = var5;
         var2.catagory = 1;
         var1.catagory = 1;
      }

   }

   private void setPath() {
      boolean var1 = false;

      for(int var3 = 0; var3 < 64; ++var3) {
         if (this.array[var3 / 8][var3 % 8].itemID != -2) {
            int var2 = 0;

            int var4;
            for(var4 = var3 + 1; var4 % 8 < 8 && var4 < 64 && var4 / 8 == var3 / 8 && this.array[var3 / 8][var3 % 8].itemID == this.array[var4 / 8][var4 % 8].itemID; ++var4) {
               ++var2;
            }

            if (var2 > 1) {
               for(var4 = var3; var4 < var3 + var2 + 1; ++var4) {
                  this.array[var4 / 8][var4 % 8].isRemove = true;
                  var1 = true;
               }
            }

            var2 = 0;

            for(var4 = var3 + 8; var4 < 64 && var4 % 8 == var3 % 8 && this.array[var3 / 8][var3 % 8].itemID == this.array[var4 / 8][var4 % 8].itemID; var4 += 8) {
               ++var2;
            }

            if (var2 > 1) {
               for(var4 = var3; var4 < var3 + (var2 + 1 << 3); var4 += 8) {
                  this.array[var4 / 8][var4 % 8].isRemove = true;
                  var1 = true;
               }
            }
         }
      }

      if (var1) {
         CasinoService.gI().createCell(this.array);
      } else if (this.isMove) {
         this.isMove = false;
         this.doSkip();
      }

   }

   private void createPoint() {
      for(int var1 = 0; var1 < 8; ++var1) {
         for(int var2 = 7; var2 >= 0; --var2) {
            if (this.array[((var2 << 3) + var1) / 8][((var2 << 3) + var1) % 8].itemID == -1) {
               int var10001 = (var2 << 3) + var1;
               boolean var3 = true;
               int var5 = 4;
               DiamondScr var7 = this;
               this.isTrans = true;

               for(int var6 = var10001; var6 / 8 > 0; var6 -= 8) {
                  var7.array[var6 / 8][var6 % 8].itemID = var7.array[(var6 - 8) / 8][(var6 - 8) % 8].itemID;
                  var7.array[var6 / 8][var6 % 8].color = var6 / 8 * var7.wCell;
                  if (!var7.array[var6 / 8][var6 % 8].isFire) {
                     var7.array[var6 / 8][var6 % 8].h = -var5;
                     ++var5;
                     var7.array[var6 / 8][var6 % 8].isFire = true;
                  }

                  var7.array[var6 / 8][var6 % 8].y = var7.array[(var6 - 8) / 8][(var6 - 8) % 8].y;
               }

               var7.array[0][var10001 % 8].itemID = -2;
               var7.array[0][var10001 % 8].color = 0;
               if (!var7.array[0][var10001 % 8].isFire) {
                  var7.array[0][var10001 % 8].h = -var5;
                  ++var5;
                  var7.array[0][var10001 % 8].isFire = true;
                  var7.array[0][var10001 % 8].y = 0;
               }

               Point var10000 = var7.array[0][var10001 % 8];
               var10000.y -= 24;
               ++var2;
            }
         }
      }

   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      super.paint(var1);
   }

   public final void paintMain(Graphics var1) {
      super.paintMain(var1);
      if (!BoardScr.isStartGame) {
         this.paintNamePlayers(var1);
      } else {
         Canvas.resetTrans(var1);
         Graphics var3 = var1;
         DiamondScr var2 = this;
         var1.translate(this.x, this.y);
         int var5;
         int var10;
         if (AvatarData.getImgIcon((short)876).count != -1) {
            for(var5 = 0; var5 < var2.aE.size(); ++var5) {
               Point var6 = (Point)var2.aE.elementAt(var5);
               var10 = var5 * 17 - var2.wCell / 2 + 8;
               if (var6.color != GameMidlet.avatar.IDDB) {
                  var10 = (var2.wCell << 3) - var5 * 17 + var2.wCell / 2 - 8;
               }

               int var10003 = var6.itemID << 4;
               int var10008 = (var2.wCell << 3) + var2.wCell;
               var3.drawRegion(AvatarData.getImgIcon((short)876).img, 0, var10003, 16, 16, 0, var10, var10008, 3);
               Canvas.smallFontYellow.drawString(var3, String.valueOf(var6.dis), var10, (var2.wCell << 3) + var2.wCell - AvMain.hSmall / 2, 2);
            }
         }

         var3.setClip(-var2.wCell / 2, -var2.wCell / 2, (var2.wCell << 3) + var2.wCell, (var2.wCell << 3) + var2.wCell);
         ImageIcon var9;
         if (var2.V >= 0 && var2.array[var2.V / 8][var2.V % 8] != null && (var2.turn != GameMidlet.avatar.IDDB || Canvas.gameTick % 10 != 5) && (var9 = AvatarData.getImgIcon((short)(Canvas.hCan > 250 ? 878 : 879))).count != -1) {
            var3.drawRegion(var9.img, 0, (var2.isSelected != -1 && Canvas.gameTick % 6 < 3 ? 1 : 0) * var2.wCell, var2.wCell, var2.wCell, 0, var2.array[var2.V / 8][var2.V % 8].x, var2.array[var2.V / 8][var2.V % 8].y, 0);
         }

         if ((var9 = AvatarData.getImgIcon((short)(Canvas.hCan > 250 ? 875 : 876))).count != -1) {
            for(var5 = 0; var5 < 8; ++var5) {
               for(var10 = 0; var10 < 8; ++var10) {
                  if (var2.array[var5][var10] != null && var2.array[var5][var10].itemID >= 0) {
                     var3.drawRegion(var9.img, 0, var2.array[var5][var10].itemID * var2.wImg, var2.wImg, var2.wImg, 0, var2.array[var5][var10].x, var2.array[var5][var10].y, 0);
                  }
               }
            }
         }

         Canvas.resetTrans(var1);
         if (Canvas.w > 160) {
            this.paintNamePlayers(var1);
         }

         this.paintPlayer(var1);
         Canvas.resetTrans(var1);
         String var7 = "";
         if (BoardScr.dieTime != 0L) {
            long var8 = (BoardScr.currentTime - BoardScr.dieTime) / 1000L;
            var7 = var7 + -var8;
         }

         Canvas.O.drawString(var1, var7, this.x + (this.wCell << 3) / 2, this.y + (this.wCell << 3) + Canvas.O.getHeight() + 2, 2);
         this.f(var1);
      }

   }

   public final void paintCaro(Graphics var1) {
      var1.setClip(this.x - this.wCell / 2, this.y - this.wCell / 2, (this.wCell << 3) + this.wCell + 1, (this.wCell << 3) + this.wCell + 1);

      for(int var2 = 0; var2 < 10; ++var2) {
         for(int var3 = 0; var3 < 10; ++var3) {
            if (var3 % 2 == var2 % 2) {
               var1.setColor(5197647);
            } else {
               var1.setColor(2697513);
            }

            var1.fillRect(this.x - this.wCell + var2 * this.wCell, this.y + var3 * this.wCell - this.wCell, this.wCell, this.wCell);
         }
      }

      var1.setColor(0);
      var1.drawRect(this.x - this.wCell / 2, this.y - this.wCell / 2, (this.wCell << 3) + this.wCell, (this.wCell << 3) + this.wCell);
      var1.drawRect(this.x - this.wCell / 2 + 1, this.y - this.wCell / 2 + 1, (this.wCell << 3) + this.wCell - 2, (this.wCell << 3) + this.wCell - 2);
   }

   private void paintPlayer(Graphics var1) {
      int var3 = 0;
      int var4 = 0;

      for(int var11 = 0; var11 < 2; ++var11) {
         Avatar var12 = (Avatar)BoardScr.avatarInfos.elementAt(var11);
         ImageIcon var2;
         if (this.countHit != -1 && var12.task == -1 && var12.action == 0 && (var2 = AvatarData.getImgIcon((short)(this.c ? 882 : 881))).count != -1) {
            var1.drawRegion(var2.img, 0, 48 * AvMain.hd * (Canvas.gameTick % 6 < 3 ? 0 : 1), 48 * AvMain.hd, 48 * AvMain.hd, 0, var12.x, var12.y - var12.height / 2, 3);
         }

         byte var5;
         int var6;
         int var7;
         int var8;
         int var9;
         int var10;
         int var13;
         if (var12.IDDB == GameMidlet.avatar.IDDB) {
            var13 = this.xPlayer1 - (10 + 10 * AvMain.hd + this.hhFill);
            var3 = 0;
            var9 = 0;
            var10 = 0;
            var4 = 0;
            var6 = -2;
            var5 = 1;
            var7 = this.hhFill - 7;
            var8 = this.hhFill - 16 * AvMain.hd;
            if (Canvas.w > 160) {
               Canvas.smallFontYellow.drawString(var1, var12.getMoneyNew() + " " + T.getMoney(), var13 + this.hhFill, var12.y, 1);
            }
         } else {
            var13 = this.xPlayer2 + 10 + 10 * AvMain.hd;
            var3 += this.hhFill - var12.hp * this.hhFill / var12.maxHP;
            var4 += this.hhFill - var12.mp * this.hhFill / var12.maxMP;
            var10 = this.hhFill - (var12.hp + var12.plusHP) * this.hhFill / var12.maxHP;
            var9 = this.hhFill - (var12.mp + var12.plusMP) * this.hhFill / var12.maxMP;
            var6 = this.hhFill + 2;
            var7 = 8;
            var5 = 0;
            var8 = 16 * AvMain.hd;
            if (Canvas.w > 160) {
               Canvas.smallFontYellow.drawString(var1, var12.getMoneyNew() + " " + T.getMoney(), var13, var12.y, 0);
            }
         }

         Canvas.smallFontYellow.drawString(var1, String.valueOf(var12.hp), var13 + var6, var12.y - (AvMain.hSmall << 1) + 3 * AvMain.hd - AvMain.hSmall / 2, var5);
         Canvas.smallFontYellow.drawString(var1, String.valueOf(var12.mp), var13 + var6, var12.y - AvMain.hSmall + 3 * AvMain.hd - AvMain.hSmall / 2, var5);
         if (var12.an > 0 && var12.countDefent <= 0 || var12.countDefent > 0 && Canvas.gameTick % 6 < 3) {
            AvatarData.paintImg(var1, 880, var13 + var7, var12.y - AvMain.hSmall * 3, 3);
            Canvas.smallFontYellow.drawString(var1, String.valueOf(var12.an), var13 + var8, var12.y - AvMain.hSmall * 3 - AvMain.hSmall / 2, var5);
            if (var12.countDefent > 0) {
               --var12.countDefent;
            }
         }

         if (var12.plusHP != 0 && Canvas.gameTick % 6 >= 3) {
            var1.setColor(1908254);
         } else {
            var1.setColor(0);
         }

         var1.fillRect(var13, var12.y - (AvMain.hSmall << 1), this.hhFill, 6 * AvMain.hd);
         var1.fillRect(var13, var12.y - AvMain.hSmall, this.hhFill, 6 * AvMain.hd);
         if (var12.plusHP > 0) {
            var1.setColor(16583178);
            var1.fillRect(var13 + var10, var12.y - 4 - 10 * AvMain.hd, (var12.hp + var12.plusHP) * this.hhFill / var12.maxHP, 6 * AvMain.hd);
         }

         if (var12.plusHP != 0 && Canvas.gameTick % 6 >= 3) {
            var1.setColor(16734553);
         } else {
            var1.setColor(16711680);
         }

         var1.fillRect(var13 + var3, var12.y - (AvMain.hSmall << 1), var12.hp * this.hhFill / var12.maxHP, 6 * AvMain.hd);
         var1.setColor(14137273);
         var1.drawRect(var13, var12.y - (AvMain.hSmall << 1), this.hhFill, 6 * AvMain.hd);
         var1.drawRect(var13, var12.y - AvMain.hSmall, this.hhFill, 6 * AvMain.hd);
         if (var12.plusMP > 0) {
            var1.setColor(3771903);
            var1.fillRect(var13 + var9, var12.y - AvMain.hSmall + 1, (var12.mp + var12.plusMP) * this.hhFill / var12.maxMP, 6 * AvMain.hd - 1);
         }

         if ((var12.plusMP != 0 || var12.isNo) && Canvas.gameTick % 6 >= 3) {
            var1.setColor(6799871);
         } else {
            var1.setColor(299247);
         }

         var1.fillRect(var13 + var4, var12.y - AvMain.hSmall + 1, var12.mp * this.hhFill / var12.maxMP, 6 * AvMain.hd - 1);
      }

   }

   private void f(Graphics var1) {
      for(int var2 = 0; var2 < this.listFireWork.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.listFireWork.elementAt(var2)).limitY > 0) {
            AvatarData.paintImg(var1, 877, var3.x, var3.y, 3);
         } else if (var3.isFire) {
            this.imgFireWork.drawFrame(var3.color / 5, var3.x, var3.y, 0, 3, var1);
         } else if (var3.dis >= 0) {
            this.imgFireWork.drawFrame(var3.color / 2 + 1, var3.x, var3.y, 0, 3, var1);
         }
      }

   }

   public final void paintGame(byte[] var1, AvPosition[] var2, byte var3, Vector var4) {
      int var6;
      int var7;
      for(var6 = 0; var6 < var1.length; ++var6) {
         this.array[var1[var6] / 8][var1[var6] % 8].isRemove = true;
         if (Canvas.h > 300) {
            boolean var5 = false;

            Point var11;
            for(var7 = 0; var7 < this.aE.size(); ++var7) {
               if ((var11 = (Point)this.aE.elementAt(var7)).itemID == this.array[var1[var6] / 8][var1[var6] % 8].itemID) {
                  var11.limitY += 20;
                  var5 = true;
                  ++var11.dis;
                  break;
               }
            }

            if (!var5) {
               (var11 = new Point()).itemID = this.array[var1[var6] / 8][var1[var6] % 8].itemID;
               var11.limitY = 40;
               var11.dis = 1;
               var11.color = super.turn;
               this.aE.addElement(var11);
            }
         }
      }

      DiamondScr var9 = this;

      for(int var10 = 0; var10 < 8; ++var10) {
         for(var6 = 0; var6 < 8; ++var6) {
            if (var9.array[var10][var6].isRemove) {
               var9.array[var10][var6].isRemove = false;
               var9.addFire(var9.array[var10][var6].x + 12, var9.array[var10][var6].y + 12, var9.array[var10][var6].itemID);
               var9.array[var10][var6].itemID = -1;
            }
         }
      }

      var9.createPoint();

      for(var6 = 0; var6 < var2.length; ++var6) {
         var7 = var2[var6].anchor;
         this.array[var7 / 8][var7 % 8].itemID = var2[var6].depth;
      }

      if (var3 > 1) {
         Canvas.addFlyTextSmall("Combo x" + var3, Canvas.hw, Canvas.hh, -1, 1, 20);
      }

      if (var4.size() > 0) {
         for(var6 = 0; var6 < var4.size(); ++var6) {
            Canvas.addFlyTextSmall((String)var4.elementAt(var6), Canvas.hw, Canvas.hh + 40, -1, 1, var6 * 30);
         }
      }

      for(var6 = 0; var6 < 2; ++var6) {
         Avatar var12;
         (var12 = (Avatar)BoardScr.avatarInfos.elementAt(var6)).setFeel(4);
         if (var12.IDDB != super.turn && var12.fight > 0) {
            Avatar var13;
            if ((var13 = BoardScr.getAvatarByID(super.turn)).task != -1) {
               var13.doAction(var12.x, var12.y);
            }

            var13.task = -1;
            if (var12.an > 0) {
               var12.countDefent = 20;
            }
         }
      }

      Canvas.endDlg();
   }

   public final void move(int var1, int var2, int var3) {
      if (!this.isEnd) {
         Avatar var4;
         if ((var4 = BoardScr.getAvatarByID(var1)) != null && var4.action == 4) {
            var4.action = 0;
         }

         if (var1 == GameMidlet.avatar.IDDB) {
            this.isMove = true;
            this.setPath();
            this.ableMove = true;
         } else {
            super.center = BoardScr.cmdWaiting;
            super.right = null;
            this.isSelected = var2;
            this.V = var3;
            this.change();
            if (var1 == -1) {
               this.isPath = false;
               this.isSelected = -1;
            }
         }
      }

   }

   public final void onSkip(int var1) {
      if (!this.isEnd) {
         this.isSelected = -1;
         BoardScr.dieTime = System.currentTimeMillis() + (long)(BoardScr.interval * 1000);
         super.turn = var1;
         this.ableMove = false;
         if (var1 == GameMidlet.avatar.IDDB) {
            if (this.setOutPath()) {
               super.right = this.cmdSkip;
               super.center = this.cmdSelected;
            } else {
               CasinoService.gI().doOutPath();
            }
         } else {
            this.isMove = false;
            super.center = null;
            super.right = null;
         }
      }

   }

   public final void onOutPath(int var1, byte[][] var2) {
      super.turn = var1;
      if (var1 == GameMidlet.avatar.IDDB) {
         this.isInit = true;
      }

      this.setArray(var2);
   }

   public final void doContinue() {
      super.doContinue();
      BoardScr.isStartGame = false;
      this.isEnd = false;
      ReportDlg.gI().show();
      this.idWin = -1;

      for(int var1 = 0; var1 < BoardScr.avatarInfos.size(); ++var1) {
         Avatar var2;
         (var2 = (Avatar)BoardScr.avatarInfos.elementAt(var1)).resetAction();
         var2.setFeel(4);
      }

   }

   public final void onFinish(Vector var1) {
      ReportDlg var10000 = ReportDlg.gI();
      String var3 = "";
      ReportDlg var4 = var10000;
      var10000.g = var3;
      var10000.f = Canvas.normalFont.getWidth(var10000.g) + 20 * AvMain.hd;
      if (var10000.f < 50 + 20 * AvMain.hd) {
         var10000.f = 50 + 20 * AvMain.hd;
      }

      var10000.list = var1;
      var10000.h = var10000.list.size() * AvMain.hBlack + (AvMain.hDuBox << 1) + 10 + PaintPopup.hTab;
      var10000.w = 0;

      for(int var5 = 0; var5 < var4.list.size(); ++var5) {
         var3 = (String)var4.list.elementAt(var5);
         if (Canvas.normalFont.getWidth(var3) + 20 > var4.w) {
            var4.w = Canvas.normalFont.getWidth(var3) + 20;
         }
      }

      if (var4.w < 176) {
         var4.w = 176;
      }

      if (Canvas.w >= 240 && var4.w < 240 * AvMain.hd) {
         var4.w = 240 * AvMain.hd;
      }

      var4.x = (Canvas.w - var4.w) / 2;
      var4.y = Canvas.h - Canvas.hTab - var4.h - 10;
      ReportDlg.gI().center = new Command(T.OK, -1, this);
      super.center = BoardScr.cmdBack;
      super.right = null;
      super.turn = -1;
      BoardScr.resetReady();
      super.left = null;
      this.isEnd = true;
   }

   public final void onData(byte[][] var1) {
      for(int var2 = 7; var2 >= 0; --var2) {
         for(int var3 = 7; var3 >= 0; --var3) {
            this.array[var2][var3].itemID = (short)var1[var2][var3];
         }
      }

   }
}
