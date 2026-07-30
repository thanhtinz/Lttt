package avt;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import main.Canvas;
import main.GameMidlet;

public final class Avatar extends Base {
   private byte cFrame;
   public int[] money = new int[]{0, -1, 0, 0};
   public int luongKhoa;
   public String strMoney = "";
   public byte gender;
   public Vector seriPart;
   public Vector emotionList;
   public Vector moveList = new Vector();
   public byte ySat;
   public int idFrom;
   public int idTo;
   public int idGift;
   public String showName;
   public String text2;
   public int exp;
   public boolean isReady;
   public boolean isLeave = false;
   public byte typeHome = -1;
   private byte maxJump;
   public byte perLvFarm;
   public byte dirFirst;
   public byte dirLast;
   public short lvFarm;
   public short lvMain;
   private boolean isLoad = false;
   private boolean isSetAction = false;
   public int task = 0;
   public int isJumps = -1;
   private int numSleep = 0;
   public short feel = 4;
   public short numFeel = 0;
   public short firFeel;
   private short wName;
   private short nFrame;
   public short idPet = -1;
   public short hungerPet;
   public short idImg = -1;
   public short Y;
   public short timeTask = 0;
   public short idWedding = -1;
   public short idStatus = -1;
   private int angle = 0;
   public byte blogNews = -1;
   public boolean isNo;
   public static FrameImage imgBlog;
   public byte fight = 0;
   public byte countDefent = -1;
   public int bossHp = -1;
   public int bossMaxHp = -1;
   public short hp = 1000;
   public short mp = 300;
   public short plusHP = 0;
   public short plusMP = 0;
   public short maxHP = 1000;
   public short maxMP = 1000;
   public short an;
   private short aJ;
   public Avatar focus;
   public static FrameImage imgHit;
   public static FrameImage imgKiss;
   private byte timeHit = 0;
   private Kiss kiss;
   public String[] textChat;
   private int countChat;
   private static byte[][] FRAME = new byte[][]{{0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 2, 2, 2, 2, 2}, {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, {4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, {5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, {6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, {7, 7, 7, 7, 7, 7, 7, 7, 7, 7}, {8, 8, 8, 8, 8, 8, 8, 8, 8, 8}, {9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, {10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, {11, 11, 11, 11, 11, 11, 11, 11, 11, 11}, {12, 12, 12, 12, 12, 12, 12, 12, 12, 12}, {13, 13, 13, 13, 13, 13, 13, 13, -2, 13}, {14, 14, 14, 14, 14, 14, 14, 14, 14, 14}, {0, 0, 0, 0, 0, 1, 1, 1, 1, 1}};
   private static byte[] duX = new byte[]{-3, 0, 1};
   public short timeEmotion = 0;
   public boolean[][] at;
   private short[] aP;
   private short[] aQ;
   private short[] aR;
   public int au = 0;
   public AvPosition posFocus;

   public Avatar() {
      super.catagory = 0;
      super.height = 42;
      this.cFrame = (byte)CRes.rnd(9);
      this.maxJump = (byte)(CRes.rnd(30) + 10);
   }

   public final int getMoney() {
      return this.money[0];
   }

   public final void setMoney(int var1) {
      this.money[0] = var1;
      this.strMoney = Canvas.getMoneys(this.money[0]) + T.dola;
   }

   public final int getMoneyNew() {
      return MapScr.isNewVersion ? this.money[3] : this.money[0];
   }

   public final void setMoneyNew(int var1) {
      if (MapScr.isNewVersion) {
         this.money[3] = var1;
      } else {
         this.money[0] = var1;
      }

   }

   public final void setGold(int var1) {
      this.money[2] = var1;
   }

   public final void paint(Graphics var1) {
      if ((super.x + 15) * MyObject.hd >= AvCamera.gI().xCam && (super.x - 15) * MyObject.hd <= AvCamera.gI().xCam + Canvas.w && !super.ableShow && (Canvas.stypeInt <= 0 || Canvas.currentMyScreen != MainMenu.gI())) {
         if (super.action != 14) {
            var1.drawImage(LoadMap.imgShadow, (super.x + (super.direct == Base.LEFT ? 2 : -2)) * MyObject.hd, (super.y - 1) * MyObject.hd, 3);
         }

         int var2 = this.seriPart.size();
         boolean var3 = false;

         int var4;
         for(var4 = 0; var4 < var2; ++var4) {
            Part var5;
            if ((var5 = AvatarData.getPart(((SeriPart)this.seriPart.elementAt(var4)).idPart)) != null && (super.action != 14 || var5.zOrder == 30 || var5.zOrder == 40 || var5.zOrder == 50)) {
               if (var5.zOrder == 40) {
                  if (this.feel != 4) {
                     var5 = AvatarData.getPart(this.feel);
                  } else if ((this.feel == 4 || this.feel == 6) && this.nFrame < 1 + this.gender) {
                     var3 = true;
                  }
               }

               if (LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53 || AvatarData.isZOrderMain(var5.zOrder) || var5.zOrder == 52) {
                  var5.paintAvatar(var1, super.frame, super.x * MyObject.hd, (super.y + super.vh + this.ySat + super.direct_) * MyObject.hd, super.direct);
                  if (var3) {
                     var3 = false;
                     var5 = AvatarData.getPart((short)606);
                     if (LoadMap.TYPEMAP != 24 && LoadMap.TYPEMAP != 53 || AvatarData.isZOrderMain(var5.zOrder) || var5.zOrder == 52) {
                        var5.paintAvatar(var1, super.frame, super.x * MyObject.hd, (super.y + super.vh + this.ySat + super.direct_) * MyObject.hd, super.direct);
                     }
                  }
               }
            }
         }

         if ((OptionScr.gI().mapFocus[0] == 0 || this == LoadMap.focusObj) && LoadMap.TYPEMAP != 24) {
            this.paintName(var1, super.x * MyObject.hd, (super.y + super.direct_) * MyObject.hd - AvMain.hSmall);
         }

         if (this.kiss != null) {
            Graphics var7 = var1;
            Kiss var6 = this.kiss;

            for(var4 = 0; var4 < 3; ++var4) {
               imgKiss.drawFrame(var6.index[var4] / 3, (var6.xCur + var6.x[var4]) * AvMain.hd, (var6.yCur + var6.y[var4]) * AvMain.hd, 0, 3, var7);
            }
         }

         if (this.timeHit > 0 && this.task == -2) {
            imgHit.drawFrame(Canvas.gameTick % 6 >= 3 ? 0 : 1, super.x * MyObject.hd, super.y * MyObject.hd - super.height / 2, 0, 3, var1);
         }

         super.paint(var1);
      }

   }

   public final void paintIcon(Graphics var1, int var2, int var3, boolean var4) {
      var1.drawImage(LoadMap.imgShadow, var2 + (super.direct == Base.LEFT ? 2 : -2), var3 - 1, 3);
      if (this.seriPart != null) {
         int var5 = this.seriPart.size();

         for(int var6 = 0; var6 < var5; ++var6) {
            Part var7;
            if ((var7 = AvatarData.getPart(((SeriPart)this.seriPart.elementAt(var6)).idPart)) != null) {
               if (var7.zOrder == 40 && this.feel != 4) {
                  var7 = AvatarData.getPart(this.feel);
               }

               var7.paintAvatar(var1, super.frame, var2, var3, super.direct);
            }
         }
      }

      if (var4) {
         this.paintName(var1, var2, var3 - AvMain.hSmall);
      }

      super.paint(var1);
   }

   public final void paintName(Graphics var1, int var2, int var3) {
      byte var4 = 0;
      var3 = var3 - super.height * MyObject.hd + (super.vh + this.ySat) * MyObject.hd;
      if (this.idImg != -1) {
         var4 = 7;
         AvatarData.paintImg(var1, this.idImg, var2 + duX[super.direct] * MyObject.hd - this.wName / 2, var3 + AvMain.hSmall / 2, 3);
      }

      var2 += (duX[super.direct] + var4) * MyObject.hd;
      if (super.IDDB >= 2000001000) {
         int var5 = 40 * MyObject.hd;
         int var6 = 4 * MyObject.hd;
         int var7 = var2 - var5 / 2;
         int var8 = var3 - 6 * MyObject.hd;
         int var9 = this.bossMaxHp > 0 ? this.bossMaxHp : this.maxHP;
         int var10 = this.bossHp >= 0 ? this.bossHp : this.hp;
         if (var10 < 0) {
            var10 = 0;
         } else if (var10 > var9) {
            var10 = var9;
         }

         int var11 = var9 > 0 ? var10 * var5 / var9 : 0;
         PaintPopup.fill(var7 - MyObject.hd, var8 - MyObject.hd, var5 + (MyObject.hd << 1), var6 + (MyObject.hd << 1), 1, var1);
         PaintPopup.fill(var7, var8, var11, var6, 65280, var1);
      }

      if (this.idWedding != -1) {
         AvatarData.paintImg(var1, this.idWedding, var2 + this.wName / 2 + 6 * MyObject.hd, var3 + AvMain.hSmall / 2, 3);
      }

      if (this.blogNews != -1) {
         imgBlog.drawFrame(this.blogNews, var2 + this.wName / 2 + 7 * MyObject.hd, var3 + 3, 0, 3, var1);
      }

      if (super.IDDB == GameMidlet.avatar.IDDB) {
         Canvas.smallFontRed.drawString(var1, super.name, var2, var3, 2);
      } else {
         Canvas.smallFontYellow.drawString(var1, super.name, var2, var3, 2);
      }

   }

   public final void setExp(int var1) {
      this.exp = var1;
      int var2 = 1;

      while(true) {
         int var4 = var2 * 100;
         int var3 = var1;
         if ((var1 -= var4) < 0) {
            this.lvMain = (short)((byte)var2);
            this.perLvFarm = (byte)(var3 * 100 / (var2 * 100));
            return;
         }

         ++var2;
         var1 = var1;
      }
   }

   public final void addSeri(SeriPart var1) {
      if (this.seriPart == null) {
         this.seriPart = new Vector();
      }

      this.seriPart.addElement(var1);
   }

   public final void setName(String var1) {
      super.name = var1;
      if (var1.length() > 7) {
         this.showName = var1.substring(0, 6) + "..";
      } else {
         this.showName = var1;
      }

      this.wName = (short)Canvas.smallFontRed.getWidth(var1);
   }

   public final void setFeel(int var1) {
      this.feel = (short)var1;
   }

   public final void update() {
      int var2;
      if (this.kiss != null) {
         Kiss var1 = this.kiss;

         for(var2 = 0; var2 < 3; ++var2) {
            int var10003 = var1.y[var2]--;
            if (var1.y[var2] < -60) {
               var1.y[var2] = 0;
               var1.dis[var2] = 6;
            }

            ++var1.index[var2];
            if (var1.index[var2] == 6) {
               var1.index[var2] = 0;
            }

            int[] var6 = var1.x;
            var6[var2] += var1.dir[var2] << 1;
            if (var1.dir[var2] == 1) {
               if (var1.x[var2] > 10 - CRes.abs(var1.y[var2] / 10)) {
                  var1.dir[var2] = -1;
                  if (var1.dis[var2] > 0) {
                     --var1.dis[var2];
                  }
               }
            } else {
               if (var1.x[var2] < -(10 - CRes.abs(var1.y[var2] / 10))) {
                  var1.dir[var2] = 1;
               }

               if (var1.dis[var2] > 0) {
                  --var1.dis[var2];
               }
            }
         }
      }

      if (this.isLoad && Canvas.gameTick % 20 == 10) {
         this.orderSeriesPath();
      }

      if (this.isLeave && (!MapScr.isWedding || super.IDDB != MapScr.idUserWedding_1 && super.IDDB != MapScr.idUserWedding_2) && this.moveList.size() == 0 && super.x == super.xCur && super.y == super.yCur) {
         LoadMap.removePlayer(this);
         if (MapScr.focusP != null && MapScr.focusP.IDDB == super.IDDB) {
            MapScr.focusP = null;
            LoadMap.focusObj = null;
         }
      }

      int var3;
      if (super.xCur == super.x && super.yCur == super.y && this.au >= 0) {
         --this.au;
         if (this.au < 0) {
            this.au = -1;
            this.isSetAction = false;
            this.task = 0;
            if (this == GameMidlet.avatar) {
               LoadMap.A = -1;
            }
         } else {
            var2 = (this.aR[this.au] & 255) * LoadMap.w + LoadMap.w / 2;
            var3 = (this.aR[this.au] >> 8) * LoadMap.w + LoadMap.w / 2;
            if (this.au == 1) {
               GameMidlet.avatar.isSetAction = true;
               var2 = this.posFocus.x / AvMain.hd;
               var3 = this.posFocus.y / AvMain.hd;
               this.au = 0;
            }

            if (this != GameMidlet.avatar) {
               super.action = 1;
               this.doAction(var2, var3);
            } else if (!this.doJoin(var2 - super.x, var3 - super.y) && !this.detectCollisionMap(var2 - super.x, var3 - super.y)) {
               if (!LoadMap.setTypeFindEnd(LoadMap.getTypeMap(var2, var3))) {
                  super.action = 1;
                  this.doAction(var2, var3);
               }
            } else {
               this.au = 0;
               this.resetMovement();
               this.isSetAction = false;
            }
         }
      }

      this.updateFrame();
      if (this.emotionList != null) {
         for(var3 = 0; var3 < this.emotionList.size(); ++var3) {
            Emotion var5;
            if ((var5 = (Emotion)this.emotionList.elementAt(var3)).time == this.timeEmotion) {
               this.timeEmotion = 0;
               this.feel = var5.id;
               this.emotionList.removeElement(var5);
               break;
            }
         }

         ++this.timeEmotion;
      }

   }

   public final void setFrame(int var1) {
      if (var1 < 0) {
         super.frame = FRAME[0][this.cFrame];
      } else {
         super.frame = FRAME[var1][this.cFrame];
      }

   }

   public final void updateFrame() {
      if (this.nFrame <= 0) {
         this.nFrame = (short)(10 + CRes.rnd(70) / (this.gender + 1));
      }

      --this.nFrame;
      ++this.cFrame;
      if (this.cFrame >= 10) {
         this.cFrame = 0;
      }

      if (super.action < 0) {
         super.frame = FRAME[0][this.cFrame];
      } else if (super.action < FRAME.length) {
         super.frame = FRAME[super.action][this.cFrame];
      }

      int var3;
      if (this.numFeel != 0 || this.feel == 11 || this.feel == 7 || this.feel == 9) {
         if (this.numFeel == 0) {
            this.firFeel = this.feel;
         }

         ++this.numFeel;
         if (this.numFeel % 10 > 5) {
            if (this.numFeel > 45) {
               this.numFeel = 0;
            }

            var3 = 4;
            this.feel = (short)var3;
         } else {
            var3 = this.firFeel;
            this.feel = (short)var3;
         }
      }

      if ((this != GameMidlet.avatar || this.task != 0 || Canvas.currentMyScreen == BoardScr.me) && super.action != 10) {
         if (CRes.distance(super.x, super.y + super.direct_, super.xCur, super.yCur) <= super.v) {
            if (super.M && this.aJ == 0) {
               this.aJ = 0;
               super.M = false;
               super.direct_ = 0;
            }

            if (this.focus != null && this.timeHit == 0) {
               if (this.task == -2) {
                  this.timeHit = 20;
               } else if (this.task == 11) {
                  this.timeHit = 30;
                  this.feel = 107;
                  this.focus.feel = 107;
                  this.kiss = new Kiss(this, super.x, super.y);
               }
            }

            if (this.task == -5) {
               this.dirLast = super.direct;
               super.x = super.xCur;
               super.y = super.yCur;
               if (this.au <= 0) {
                  this.task = 0;
               }

               if (Canvas.currentDialog == null) {
                  MapScr.gI();
                  MapScr.doMove(super.x, super.y, super.direct, super.direct_);
               }

               if (MapScr.isWedding && GameMidlet.avatar.IDDB == super.IDDB) {
                  super.direct = 0;
               }
            } else {
               if (super.IDDB != GameMidlet.avatar.IDDB) {
                  super.xCur = super.x;
                  super.yCur = super.y + super.direct_;
               }

               if (this.moveList.size() == 0) {
                  if (super.action == 1) {
                     super.action = 0;
                  }

                  super.direct = this.dirLast;
               } else {
                  AvPosition var2 = (AvPosition)this.moveList.elementAt(0);
                  this.doAction(var2.x, var2.y + var2.depth);
                  this.aJ = var2.depth;
                  if (var2.depth != 0) {
                     super.M = true;
                  }

                  if (super.xCur == -1 && super.yCur == -1) {
                     super.xCur = super.x;
                     super.yCur = super.y + super.direct_;
                     if (super.action == 14) {
                        LoadMap.type[super.y / LoadMap.w * LoadMap.wMap + super.x / LoadMap.w] = 112;
                     }

                     super.action = (byte)var2.anchor;
                     if (super.action == 14) {
                        super.y = super.yCur;
                        super.x = super.xCur;
                        LoadMap.type[super.y / LoadMap.w * LoadMap.wMap + super.x / LoadMap.w] = 90;
                     }

                     if ((super.action == 2 || super.action == 13 || super.action == 4) && (var3 = (super.y - LoadMap.w) / LoadMap.w * LoadMap.wMap + super.x / LoadMap.w) >= 0 && var3 < LoadMap.type.length) {
                        if (super.action == 4 && LoadMap.type[var3] == 67) {
                           this.ySat = -10;
                        }

                        int var4 = LoadMap.getposMap(super.x, super.y - 10);
                        super.yCur = super.y;
                        if (var4 != -1) {
                           short var6;
                           if ((var6 = LoadMap.type[var4]) == 92) {
                              this.ySat = -10;
                           }

                           if (var6 == 79 || var6 == 92 || var6 == 90 || var6 == 54) {
                              LoadMap.type[var4] = 90;
                           }
                        }
                     }
                  } else {
                     this.dirLast = (byte)var2.anchor;
                     super.direct = this.dirLast;
                  }

                  this.moveList.removeElementAt(0);
               }
            }

            if (this.task != 0 && this.task != -5) {
               if (super.IDDB == this.idTo && LoadMap.getAvatar(this.idFrom) == null) {
                  this.task = 0;
                  this.idFrom = -1;
               } else if (this.idFrom != -1 && this.idTo != -1) {
                  Avatar var7 = LoadMap.getAvatar(this.idFrom);
                  Avatar var8;
                  if ((var8 = LoadMap.getAvatar(this.idTo)) != null && var7 != null) {
                     if (var8.x > var7.x) {
                        var8.direct = var8.dirLast = Base.LEFT;
                        var7.direct = var7.dirLast = 0;
                     } else {
                        var8.direct = var8.dirLast = 0;
                        var7.direct = var7.dirLast = Base.LEFT;
                     }

                     if (super.IDDB == this.idFrom) {
                        if (this.Y > 0) {
                           --this.Y;
                        } else {
                           switch (this.task) {
                              case -3:
                                 if (LoadMap.weather == -1) {
                                    AnimateEffect var1 = new AnimateEffect(2, 0);
                                    Canvas.currentEffect.addElement(var1);
                                 }

                                 this.task = 0;
                                 var8.task = 0;
                                 break;
                              case 9:
                                 if (this == GameMidlet.avatar) {
                                    MapScr.gI();
                                    MapScr.doAction((byte)9);
                                 } else if (GameMidlet.avatar.task == 8 && super.IDDB == GameMidlet.avatar.idFrom) {
                                    MapScr.gI();
                                    MapScr.doAction((byte)8);
                                    GameMidlet.avatar.task = 0;
                                 }

                                 MapScr.gI();
                                 MapScr.setGifts(var8);
                                 this.task = 0;
                                 var8.task = 0;
                                 break;
                              case 12:
                                 boolean var9 = false;
                                 var8.task = 0;
                                 var9 = false;
                                 this.task = 0;
                           }

                           this.idGift = -1;
                           this.idFrom = -1;
                           this.idTo = -1;
                        }
                     }
                  } else {
                     if (var7 != null) {
                        var7.task = 0;
                        var7.ableShow = false;
                     }

                     if (var8 != null) {
                        var8.task = 0;
                        var8.ableShow = false;
                     }
                  }
               }
            }
         } else {
            this.angle = CRes.tan(super.xCur - super.x, -(super.yCur - (super.y + super.direct_)));
            int var5 = super.v * CRes.cos(this.angle) >> 10;
            var3 = -(super.v * CRes.sin(this.angle)) >> 10;
            if (this.isSetAction && this.task == -5 && GameMidlet.avatar.setLayPLayer(super.x + var5, super.y + super.direct_ + var3)) {
               this.resetMovement();
               super.vx = super.vy = 0;
            } else {
               super.vx = var5;
               super.vy = var3;
               super.vhy = 0;
               super.vh = 0;
               this.ySat = 0;
               this.setDir(super.x + var5);
               if (super.x != super.xCur) {
                  this.resetTypeChair();
               }

               if (super.y + super.direct_ != super.yCur) {
                  this.resetTypeChair();
               }

               super.action = 1;
            }
         }
      }

      if (super.M) {
         super.x += super.vx;
         super.direct_ = (short)(super.direct_ + super.vy);
         if (super.IDDB == GameMidlet.avatar.IDDB && super.direct_ < -this.timeTask) {
            super.direct_ = (short)(-this.timeTask);
         }
      } else {
         super.x += super.vx;
         super.y += super.vy;
      }

      super.vh += super.vhy;
      if (super.action == 10) {
         ++super.vhy;
      }

      if (Math.abs(super.vhy) >= super.g || Math.abs(super.vh) > 28) {
         super.action = 0;
         super.vhy = 0;
         super.vh = 0;
      }

      if (this.isJumps != -1 && super.action == 0) {
         ++this.isJumps;
         if (this.isJumps > this.maxJump) {
            this.isJumps = -1;
         } else if (this.isJumps % 6 == 0) {
            this.doJumps();
         }
      }

      if (super.action == 0) {
         this.ySat = 0;
      }

      if (super.action == 1 && super.vx == 0 && super.vy == 0) {
         super.action = 0;
      }

      super.vx = 0;
      super.vy = 0;
      if (this.timeHit > 0) {
         --this.timeHit;
         if (this.timeHit == 0) {
            if (this.task == -2) {
               this.focus.action = 4;
               this.focus.feel = 20;
               super.action = 4;
               this.feel = 20;
            } else if (this.task == 11) {
               this.feel = 12;
               this.focus.feel = 12;
            }

            this.task = 0;
            this.focus.task = 0;
            this.focus = null;
            this.kiss = null;
         }
      }

      if (this.textChat != null) {
         super.chat = new ChatPopup(100, this.textChat[this.countChat / 40], (byte)(this.idFrom >= 2000000000 ? 1 : 0));
         ++this.countChat;
         if (this.countChat >= this.textChat.length * 40) {
            if (super.IDDB >= 2000000000) {
               this.countChat = 0;
            } else {
               this.textChat = null;
            }
         }
      }

      super.update();
   }

   public final void updateKey() {
      if (super.action != -1 && !super.ableShow && this.task == 0 && !MapScr.isWedding) {
         super.vx = 0;
         super.vy = 0;
         if (Canvas.keyHold[2]) {
            AvCamera.gI().timeDelay = 0L;
            this.isJumps = -1;
            this.finishFeelAction();
            if (super.M) {
               super.vy = -super.v;
            } else if (this.detectCollisionMap(super.vx, -(super.v - 1))) {
               if (!this.doJoin(super.vx, -(super.v - 1))) {
                  this.setLayPLayer(super.x + super.vx, super.y - (super.v - 1));
               } else {
                  super.vx = 0;
                  super.vy = 0;
               }
            }
         } else if (Canvas.keyHold[8]) {
            AvCamera.gI().timeDelay = 0L;
            this.isJumps = -1;
            this.resetTypeChair();
            this.resetNam_nghi(0, 1);
            if (super.M) {
               super.vy = super.v;
               if (super.direct_ + super.v >= 0) {
                  super.direct_ = 0;
                  if (LoadMap.getTypeMap(super.x, super.y + LoadMap.w / 2) == 80) {
                     super.M = false;
                  } else {
                     super.vy = 0;
                  }
               }
            } else if (!this.doJoin(super.vx, super.v - 1)) {
               this.detectCollisionMap(super.vx, super.v - 1);
            } else {
               super.vx = 0;
               super.vy = 0;
            }
         }

         if (Canvas.keyHold[4]) {
            AvCamera.gI().timeDelay = 0L;
            this.isJumps = -1;
            if (this.numSleep == 0 && super.direct == Base.LEFT) {
               this.numSleep = 3;
            }

            super.direct = Base.LEFT;
            if (this.numSleep > 2) {
               if (super.M) {
                  super.vx = -super.v;
               } else if (!this.doJoin(-(super.v + 8), super.vy)) {
                  this.detectCollisionMap(-(super.v + 8), super.vy);
                  this.resetNam_nghi(-1, 0);
               } else {
                  super.vx = 0;
                  super.vy = 0;
               }
            }

            ++this.numSleep;
         } else if (Canvas.keyHold[6]) {
            AvCamera.gI().timeDelay = 0L;
            this.isJumps = -1;
            if (this.numSleep == 0 && super.direct == 0) {
               this.numSleep = 3;
            }

            super.direct = 0;
            if (this.numSleep > 2) {
               if (super.M) {
                  super.vx = super.v;
               } else if (!this.doJoin(super.v + 6, super.vy)) {
                  this.detectCollisionMap(super.v + 6, super.vy);
                  this.resetNam_nghi(1, 0);
               } else {
                  super.vx = 0;
                  super.vy = 0;
               }
            }

            ++this.numSleep;
         } else {
            this.numSleep = 0;
         }

         boolean var1 = false;
         if (Canvas.keyPressed[2]) {
            if (Canvas.keyHold[4] || Canvas.keyHold[6] || Canvas.keyHold[8]) {
               var1 = true;
            }
         } else if (Canvas.keyPressed[4]) {
            if (Canvas.keyHold[2] || Canvas.keyHold[6] || Canvas.keyHold[8]) {
               var1 = true;
            }
         } else if (Canvas.keyPressed[6]) {
            if (Canvas.keyHold[4] || Canvas.keyHold[2] || Canvas.keyHold[8]) {
               var1 = true;
            }
         } else if (Canvas.keyPressed[8] && (Canvas.keyHold[4] || Canvas.keyHold[6] || Canvas.keyHold[2])) {
            var1 = true;
         }

         if (Canvas.keyReleased[2] || Canvas.keyReleased[4] || Canvas.keyReleased[6] || Canvas.keyReleased[8]) {
            var1 = true;
            Canvas.clearKeyReleased();
         }

         if (var1 && super.action != 2 && super.action != 13 && super.action != 4 && !Canvas.keyReleased[2]) {
            MapScr.gI();
            MapScr.doMove(super.x, super.y, super.direct, super.direct_);
         }

         if (super.vx == 0 && super.vy == 0 && super.action == 1) {
            super.action = 0;
         }

         if (super.M && (super.vx != 0 || super.vy != 0)) {
            super.action = 1;
         }

         Canvas.keyPressed[2] = Canvas.keyPressed[4] = Canvas.keyPressed[6] = Canvas.keyPressed[8] = false;
      }

   }

   public final void doAction(byte var1) {
      if (super.action != 10) {
         if (var1 != 2 && var1 != 13 && var1 != 4) {
            if (super.action != 14) {
               super.action = 0;
            }
         } else {
            short var2;
            if ((var2 = LoadMap.type[(super.y - 15) / LoadMap.w * LoadMap.wMap + super.x / LoadMap.w]) != 79 && var2 != 81 && var2 != 54) {
               if (var2 == 92 || var2 == 67) {
                  this.ySat = -10;
               }
            } else {
               this.ySat = -6;
               if (var2 == 81) {
                  this.ySat = (byte)(-6 * MyObject.hd);
               }
            }

            super.action = var1;
         }

         if (var1 == 10) {
            this.doJumps();
         } else if (super.IDDB != GameMidlet.avatar.IDDB) {
            this.moveList.addElement(new AvPosition(-1, -1, var1));
         } else {
            super.action = var1;
         }
      }

   }

   public final void doAction(int var1, int var2) {
      super.xCur = var1;
      super.yCur = var2;
      this.setDir(var1);
   }

   private void setDir(int var1) {
      if (var1 > super.x) {
         super.direct = 0;
      } else if (var1 < super.x) {
         super.direct = Base.LEFT;
      }

   }

   public final void resetTypeChair() {
      if (super.action != 2 && super.action != 13) {
         this.finishFeelAction();
      } else {
         int var1;
         if ((var1 = LoadMap.getposMap(super.x, super.y - 18)) == -1) {
            return;
         }

         super.action = 0;
         this.ySat = 0;
         if (LoadMap.type[var1] == 90) {
            if (LoadMap.map[var1] == 80) {
               LoadMap.type[var1] = 92;
               return;
            }

            if (LoadMap.map[var1] == 97) {
               LoadMap.type[var1] = 54;
               return;
            }

            LoadMap.type[var1] = 79;
            return;
         }
      }

   }

   private void finishFeelAction() {
      if (super.action == 14) {
         super.action = 0;
         this.setPos(HouseScr.gI().f, HouseScr.gI().g);
         AvatarService.gI().doFeel(0);
         MapScr.gI();
         MapScr.doMove(GameMidlet.avatar.x, GameMidlet.avatar.y, GameMidlet.avatar.direct, GameMidlet.avatar.timeTask);
         Canvas.keyHold[8] = false;
      }

   }

   private void resetNam_nghi(int var1, int var2) {
      if (super.action == 4) {
         if (LoadMap.getposMap(super.x, super.y - 18) == -1) {
            return;
         }

         if (LoadMap.getTypeMap(super.x + var1 * 12, super.y + var2 * 12 - 10) == 80) {
            super.action = 0;
            this.ySat = 0;
         }
      }

   }

   private boolean doJoin(int var1, int var2) {
      if (this != GameMidlet.avatar) {
         return false;
      } else if (super.action != -1 && Canvas.currentDialog == null) {
         boolean var3;
         if ((var3 = Canvas.loadMap.doJoin(super.x + var1, super.y + var2)) && super.action == 1) {
            super.action = 0;
         }

         return var3;
      } else {
         return false;
      }
   }

   private boolean detectCollisionMap(int var1, int var2) {
      boolean var3;
      if (var3 = this.detectCollision(var1, var2)) {
         this.setWay(var1, var2);
      }

      return var3;
   }

   public final boolean setLayPLayer(int var1, int var2) {
      if (super.action != 0 && super.action != 1 || this.task != 0 && this.task != -5) {
         return false;
      } else {
         short var3;
         if ((var3 = LoadMap.type[var2 / LoadMap.w * LoadMap.wMap + var1 / LoadMap.w]) != 79 && var3 != 81 && var3 != 54) {
            if (var3 == 92) {
               super.action = 2;
               this.ySat = -10;
               super.x = var1 / LoadMap.w * LoadMap.w + LoadMap.w / 2;
               super.y = var2 / LoadMap.w * LoadMap.w + LoadMap.w - 1;
               MapScr.gI();
               MapScr.doMove(super.x, var2, super.direct, super.direct_);
               MapScr.gI();
               MapScr.doAction((byte)2);
               return true;
            } else if (var3 == 67) {
               super.action = 4;
               this.ySat = -10;
               super.x = var1 / LoadMap.w * LoadMap.w + LoadMap.w / 2;
               MapScr.gI();
               MapScr.doMove(super.x, var2, super.direct, super.direct_);
               MapScr.gI();
               MapScr.doAction((byte)4);
               return true;
            } else {
               return false;
            }
         } else {
            super.action = 2;
            this.ySat = -6;
            super.x = var1 / LoadMap.w * LoadMap.w + LoadMap.w / 2;
            super.y = var2 / LoadMap.w * LoadMap.w + LoadMap.w - 1;
            MapScr.gI();
            MapScr.doMove(super.x, super.y, super.direct, super.direct_);
            MapScr.gI();
            MapScr.doAction(super.action);
            return true;
         }
      }
   }

   public final void doJumps() {
      if (super.action == 0 || super.action == 1) {
         super.action = 10;
         if (this.isJumps == -1) {
            this.isJumps = 0;
         }

         super.vhy = (byte)(-super.g);
      }

   }

   public final void orderSeriesPath() {
      this.isLoad = false;

      try {
         for(int var1 = 0; var1 < this.seriPart.size() - 1; ++var1) {
            SeriPart var2;
            if (AvatarData.getPart((var2 = (SeriPart)this.seriPart.elementAt(var1)).idPart) != null) {
               for(int var3 = var1 + 1; var3 < this.seriPart.size(); ++var3) {
                  SeriPart var4;
                  if (AvatarData.getPart((var4 = (SeriPart)this.seriPart.elementAt(var3)).idPart).IDPart == -1) {
                     this.isLoad = true;
                  }

                  if (AvatarData.getPart(var4.idPart) != null && AvatarData.getPart(var2.idPart).zOrder > AvatarData.getPart(var4.idPart).zOrder) {
                     this.seriPart.setElementAt(var2, var3);
                     this.seriPart.setElementAt(var4, var1);
                     var2 = var4;
                  }
               }
            }
         }
      } catch (Exception var5) {
         this.isLoad = true;
      }

   }

   public final void addPart(int var1, int var2) {
      for(int var3 = 0; var3 < this.seriPart.size() - 1; ++var3) {
         SeriPart var4;
         Part var5 = AvatarData.getPart((var4 = (SeriPart)this.seriPart.elementAt(var3)).idPart);
         if (var2 == var5.zOrder) {
            this.seriPart.removeElement(var4);
            break;
         }
      }

      this.addSeri(new SeriPart((short)var1));
   }

   public final void setAction(byte var1) {
      super.action = var1;
   }

   public final void addSeriPart(SeriPart var1) {
      Part var2;
      if ((var2 = AvatarData.getPart(var1.idPart)) != null && var2.follow != -2) {
         SeriPart var3;
         if ((var3 = AvatarData.getSeriByZ(var2.zOrder, this.seriPart)) != null) {
            this.seriPart.removeElement(var3);
         }

         this.seriPart.addElement(var1);
      }

   }

   public final void initPet() {
      for(int var1 = 0; var1 < this.seriPart.size(); ++var1) {
         SeriPart var2;
         if (AvatarData.getPart((var2 = (SeriPart)this.seriPart.elementAt(var1)).idPart).zOrder == -1) {
            this.seriPart.removeElement(var2);
            this.idPet = var2.idPart;
         }
      }

   }

   public final void setPet() {
      this.initPet();
      LoadMap.setPet(this);
   }

   public final void changePet(short var1) {
      this.idPet = var1;
      Pet var2;
      if ((var2 = LoadMap.getPet(super.IDDB)) != null) {
         LoadMap.removePlayer((MyObject)var2);
         this.idPet = var1;
      }

      this.setPet();
   }

   public final void resetAction() {
      super.vhy = 0;
      super.vh = 0;
      super.action = 0;
   }

   private void resetMovement() {
      this.au = 0;
      this.isSetAction = false;
      this.task = 0;
      if (this == GameMidlet.avatar) {
         LoadMap.A = -1;
      } else {
         this.posFocus = null;
         this.aP = null;
         this.aQ = null;
         this.aR = null;
         this.at = (boolean[][])null;
      }

   }

   public final void moveToFocus() {
      if (this.at == null) {
         this.createAvatarArrays();
      }

      short var1 = 88;
      int var2 = LoadMap.w * AvMain.hd;
      if (this.posFocus.y / var2 * LoadMap.wMap + this.posFocus.x / var2 > 0 && this.posFocus.y / var2 * LoadMap.wMap + this.posFocus.x / var2 < LoadMap.type.length) {
         var1 = LoadMap.type[this.posFocus.y / var2 * LoadMap.wMap + this.posFocus.x / var2];
      }

      if (this != GameMidlet.avatar || var1 == 80 || LoadMap.TYPEMAP == 24 || LoadMap.setTypeJoint(var1) || LoadMap.setTypeFind(var1)) {
         this.posFocus.anchor = 0;
         if (this.task == 0 || this.task == -5) {
            this.task = -5;
            this.isJumps = -1;
            super.xCur = super.x;
            super.yCur = super.y;
            if (this == GameMidlet.avatar) {
               if (super.x > this.posFocus.x / AvMain.hd) {
                  LoadMap.A = Base.LEFT;
               } else {
                  LoadMap.A = 0;
               }
            }

            this.findPath();
         }
      }

   }

   public final void findPath() {
      int var1 = LoadMap.w * AvMain.hd;
      if (super.x / LoadMap.w == this.posFocus.x / var1 && super.y / LoadMap.w == this.posFocus.y / var1) {
         this.doAction(this.posFocus.x / var1 * LoadMap.w + this.posFocus.x % var1 / 2, this.posFocus.y / var1 * LoadMap.w + this.posFocus.y % var1 / 2);
      } else {
         int var10001 = super.x / LoadMap.w;
         int var10002 = super.y / LoadMap.w;
         int var10003 = this.posFocus.x / var1;
         int var5 = this.posFocus.y / var1;
         int var4 = var10003;
         Avatar var14 = this;
         int var6 = 1;
         int var7 = 0;
         int[] var10 = new int[]{0, -1, 1, 0};
         int[] var11 = new int[]{-1, 0, 0, 1};
         boolean var12 = false;

         int var8;
         for(var8 = 0; var8 < var14.at.length * var14.at[0].length; ++var8) {
            var14.aP[var8] = 0;
            var14.aQ[var8] = 0;
            var14.aR[var8] = 0;
            if (LoadMap.type[var8] != 80 && !LoadMap.setTypeJoint(LoadMap.type[var8])) {
               var14.at[var8 % LoadMap.wMap][var8 / LoadMap.wMap] = true;
            } else {
               var14.at[var8 % LoadMap.wMap][var8 / LoadMap.wMap] = false;
            }
         }

         if (LoadMap.setTypeFind(LoadMap.getTypeMap(var10003 * LoadMap.w, var5 * LoadMap.w))) {
            var14.at[var10003][var5] = false;
         }

         int var2;
         int var13;
         for(var14.aP[0] = (short)((var10002 << 8) + var10001); !var12 && var7 < var6; ++var7) {
            var2 = var14.aP[var7] & 255;
            int var3 = var14.aP[var7] >> 8;

            for(var13 = 0; var13 < 4 && !var12; ++var13) {
               var8 = var2 + var10[var13];
               int var9 = var3 + var11[var13];
               if (var8 >= 0 && var8 < var14.at.length && var9 >= 0 && var9 < var14.at[0].length && !var14.at[var8][var9]) {
                  var14.aQ[var6] = var14.aP[var7];
                  var14.aP[var6++] = (short)((var9 << 8) + var8);
                  var14.at[var8][var9] = true;
                  if (var4 == var8 && var5 == var9) {
                     var12 = true;
                  }
               }

               if (var6 >= var14.at.length * var14.at[0].length) {
                  var12 = true;
                  break;
               }
            }
         }

         var14.au = 0;
         if (var12) {
            GameMidlet.avatar.resetAction();
            var13 = var6 - 1;
            var14.aR[var14.au++] = var14.aP[var13];

            label72:
            while(true) {
               while(true) {
                  if (var13 <= 0) {
                     break label72;
                  }

                  for(var2 = 0; var2 < var6; ++var2) {
                     if (var14.aP[var2] == var14.aQ[var13]) {
                        var13 = var2;
                        var14.aR[var14.au++] = var14.aP[var2];
                        break;
                     }
                  }
               }
            }
         }

         --var14.au;
         if (!var12) {
            this.au = 0;
            if (this == GameMidlet.avatar) {
               LoadMap.A = -1;
            }
         }
      }

   }

   public final void createAvatarArrays() {
      short var1 = LoadMap.wMap;
      short var2 = LoadMap.Hmap;
      this.at = new boolean[var1][var2];
      this.aP = new short[var1 * var2];
      this.aQ = new short[var1 * var2];
      this.aR = new short[var1 * var2];
   }

   public final void updateMoney(int var1, int var2, int var3) {
      System.out.println("updateMoney: " + var1 + "    " + var2 + "   " + var3);
      if (this.money[0] != var1) {
         Canvas.addFlyTextSmall(var1 - this.money[0] + "xu", super.x, super.y - CRes.rnd(20), -1, 0, -1);
         this.money[0] = var1;
      }

      if (this.money[2] != var2) {
         Canvas.addFlyTextSmall(var2 - this.money[2] + "luong", super.x, super.y - CRes.rnd(20), -1, 0, -1);
         this.money[2] = var2;
      }

      if (this.luongKhoa != var3) {
         Canvas.addFlyTextSmall(var3 - this.luongKhoa + "luong", super.x, super.y - CRes.rnd(20), -1, 0, -1);
         this.luongKhoa = var3;
      }

   }
}
