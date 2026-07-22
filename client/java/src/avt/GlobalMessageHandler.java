package avt;

import java.io.EOFException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import main.Canvas;
import main.GameMidlet;

public final class GlobalMessageHandler extends IService implements IMessageHandler {
   private GlobalLogicHandler c = new GlobalLogicHandler();
   private static GlobalMessageHandler instance;
   public IMiniGameMsgHandler miniGameMessageHandler;

   public static GlobalMessageHandler gI() {
      if (instance == null) {
         instance = new GlobalMessageHandler();
      }

      return instance;
   }

   @Override
   public final void onConnectionFail() {
      System.out.println("onConnectionFail");
      Canvas.startOKDlg(T.gameDraw);
   }

   @Override
   public final void onDisconnected() {
      System.out.println("onDisconnected");
      System.out.println("[FISH_AUTO_LOGIN] onDisconnected entry enabled=" + ClientUtilities.fishingAutoLogin + " stageActive=" + ClientUtilities.isFishingReloginActive());
      Canvas.endDlg();
      GameMidlet.CLIENT_TYPE = 8;
      if (ClientUtilities.fishingAutoLogin) {
         System.out.println("[FISH_AUTO_LOGIN] onDisconnected -> trigger auto relogin flow");
         ClientUtilities.onFishingAutoDisconnected();
         Canvas.menuMain = null;
         HouseScr.me = null;
         MessageScr.me = null;
         SoundManager.instance.stop();
         if (ChatTextField.gI().left.action != null) {
            ChatTextField.gI().left.action.perform();
         }
         FarmData.init();
         return;
      }
      if (Canvas.currentMyScreen != LoginScr.me) {
         Canvas.startOK(T.disConnect, new IExitGame());
      }
      else {
         Canvas.startOKDlg(T.disConnect);
      }
      Canvas.menuMain = null;
      HouseScr.me = null;
      MessageScr.me = null;
      SoundManager.instance.stop();
      if (ChatTextField.gI().left.action != null) {
         ChatTextField.gI().left.action.perform();
      }
      FarmData.init();
   }

   public final void onMessage(Message var1) {
      int var2;
      int var3;
      int var4;
      String var202;
      byte var206;
      SeriPart var207;
      try {
         int var5;
         Vector var6;
         int var7;
         int var8;
         int var64;
         int var72;
         int var82;
         short var84;
         int var94;
         int var107;
         short var109;
         int var110;
         int var114;
         String var188;
         String var190;
         boolean var191;
         String var194;
         short var199;
         short var200;
         short var201;
         String var204;
         int var209;
         byte var234;
         byte var262;
         label823:
         switch (var1.command) {
            case -107:
               byte var265 = var1.reader().readByte();
               String var266 = null;
               String[] var267 = null;
               String[] var174 = null;
               String[] var268 = null;
               short[] var176 = null;
               short[] var177 = null;
               short[] var178 = null;
               int[] var179 = null;
               int var181;
               short var269;
               if (var265 == 0) {
                  var266 = var1.reader().readUTF();
                  var267 = new String[var269 = var1.reader().readShort()];
                  var176 = new short[var269];
                  var174 = new String[var269];
                  var268 = new String[var269];
                  var177 = new short[var269];
                  var178 = new short[var269];

                  for(var181 = 0; var181 < var269; ++var181) {
                     var177[var181] = var1.reader().readShort();
                     var176[var181] = var1.reader().readShort();
                     var178[var181] = var1.reader().readShort();
                     var267[var181] = var1.reader().readUTF();
                     var174[var181] = var1.reader().readUTF();
                     var268[var181] = var1.reader().readUTF();
                  }
               } else if (var265 == 1) {
                  var266 = var1.reader().readUTF();
                  var177 = new short[var269 = var1.reader().readShort()];
                  var267 = new String[var269];
                  var176 = new short[var269];
                  var179 = new int[var269];
                  var268 = new String[var269];
                  var178 = new short[var269];
                  var174 = new String[var269];

                  for(var181 = 0; var181 < var269; ++var181) {
                     var177[var181] = var1.reader().readShort();
                     var267[var181] = var1.reader().readUTF();
                     var174[var181] = var1.reader().readUTF();
                     var176[var181] = var1.reader().readShort();
                     var178[var181] = var1.reader().readShort();
                     var179[var181] = var1.reader().readInt();
                     var268[var181] = var1.reader().readUTF();
                  }
               }

               HouseScr.gI().onOpenShop(var265, var266, var267, var176, var177, var174, var268, var179, var178);
               return;
            case -105:
               byte var169 = var1.reader().readByte();
               Vector var170 = new Vector();

               for(int var171 = 0; var171 < var169; ++var171) {
                  short var172 = var1.reader().readShort();
                  String var173 = var1.reader().readUTF();
                  CommandFlower var175 = new CommandFlower(this, var173, new IActionFlower(this, var171), var172);
                  var170.addElement(var175);
               }

               Canvas.endDlg();
               FarmScr.gI();
               FarmScr.startMenuFarm(var170);
               break;
            case -103:
               Avatar var164 = LoadMap.getAvatar(var1.reader().readInt());
               if (var1.reader().readByte() == 0) {
                  var164.idImg = var1.reader().readShort();
               } else {
                  var164.idWedding = var1.reader().readShort();
               }
               break;
            case -102:
               int var166 = var1.reader().readInt();
               int var167 = var1.reader().readInt();
               Avatar var168;
               if (OnScreen.isOngame) {
                  var168 = BoardScr.getAvatarByID(var166);
               } else {
                  var168 = LoadMap.getAvatar(var166);
               }

               if (var168 != null) {
                  var168.money[3] = var167;
               }

               return;
            case -101:
               var262 = var1.reader().readByte();
               short var263 = var1.reader().readShort();
               System.out.println("DEBUG NPC_MENU_LIST(-101): mode=" + var262 + " anthor=" + var263);
               if (var262 == 1) {
                  StringObj var163;
                  (var163 = new StringObj()).anthor = var263;
                  var163.str = var1.reader().readUTF();
                  var163.dis = var1.reader().readShort();
                  var163.type = var1.reader().readByte();
                  System.out.println("DEBUG NPC_MENU_ITEM(-101): anthor=" + var163.anthor + " text=" + var163.str + " dis=" + var163.dis + " type=" + var163.type);
                  MapScr.listCmdRotate.addElement(var163);
                  if (Canvas.currentMyScreen == PopupShop.gI()) {
                     PopupShop.gI().close();
                  }

                  if (LoadMap.focusObj != null) {
                     MainMenu.gI().doExchange();
                  } else {
                     MainMenu.gI().perform();
                  }
                  break;
               } else {
                  int var264 = 0;

                  while(true) {
                     if (var264 >= MapScr.listCmdRotate.size()) {
                        break label823;
                     }

                     if (((StringObj)MapScr.listCmdRotate.elementAt(var264)).anthor == var263) {
                        System.out.println("DEBUG NPC_MENU_REMOVE(-101): anthor=" + var263);
                        MapScr.listCmdRotate.removeElementAt(var264);
                        break label823;
                     }

                     ++var264;
                  }
               }
            case -99:
               byte var155 = var1.reader().readByte();
               byte var156 = var1.reader().readByte();
               Vector var157 = new Vector();

               for(int var158 = 0; var158 < var156; ++var158) {
                  Avatar var159;
                  (var159 = new Avatar()).IDDB = var1.reader().readInt();
                  var159.setName(var1.reader().readUTF());
                  byte var160 = var1.reader().readByte();

                  for(int var161 = 0; var161 < var160; ++var161) {
                     var159.addSeri(new SeriPart(var1.reader().readShort()));
                  }

                  var159.x = var1.reader().readShort();
                  var159.y = var1.reader().readShort();
                  var159.blogNews = var1.reader().readByte();
                  var159.hungerPet = (byte)(100 - var1.reader().readByte());
                  var159.idImg = var1.reader().readShort();
                  var262 = var1.reader().readByte();
                  var159.textChat = new String[var262];

                  for(int var162 = 0; var162 < var262; ++var162) {
                     var159.textChat[var162] = var1.reader().readUTF();
                  }

                  var157.addElement(var159);
               }

               short var259 = var1.reader().readShort();
               Vector var260 = null;
               Vector var261 = null;
               if (var259 > 0) {
                  var260 = readMapItemType(var1);
                  var261 = readMapItem(var1);
               }

               MapScr.gI().onJoinOfflineMap(var155, var157, var260, var261);
               break;
            case -98:
               short var257 = var1.reader().readShort();
               byte[] var258 = new byte[var1.reader().readShort()];
               var1.reader().read(var258);
               AvatarData.listImgPart.put("" + var257, new ImageIcon(CRes.createImage(var258)));
               return;
            case -97:
               byte[] var144 = new byte[var1.reader().available()];
               var1.reader().read(var144);
               Part var146 = (Part)AvatarData.readAvatarPart(var144, true).elementAt(0);
               AvatarData.listPartDynamic.put("" + var146.IDPart, var146);

               for(int var147 = 0; var147 < LoadMap.playerLists.size(); ++var147) {
                  MyObject var148;
                  if ((var148 = (MyObject)LoadMap.playerLists.elementAt(var147)).catagory == 0) {
                     Avatar var149;
                     (var149 = (Avatar)var148).orderSeriesPath();
                  }
               }

               return;
            case -96:
               Canvas.endDlg();
               MapScr.gI().move();
               OnSplashScr.gI().switchToMe();
               OnSplashScr.gI().splashScrStat = 0;
               return;
            case -94:
               byte var142 = var1.reader().readByte();
               byte[] var143 = new byte[var1.reader().available()];
               var1.reader().read(var143);
               Canvas.loadMap.onTileImg(var142, var143);
               return;
            case -93:
               byte var253 = var1.reader().readByte();
               byte var254 = var1.reader().readByte();
               var1.reader().readShort();
               byte var132 = var1.reader().readByte();
               byte[] var134 = new byte[var1.reader().readShort()];
               var1.reader().read(var134);
               short[] var135 = null;
               byte var136;
               if ((var136 = var1.reader().readByte()) > 0) {
                  var135 = new short[var136];

                  for(int var137 = 0; var137 < var136; ++var137) {
                     var135[var137] = var1.reader().readShort();
                  }
               }

               short var255 = var1.reader().readShort();
               Image var138 = null;
               if (var255 > 0) {
                  byte[] var139 = new byte[var255];
                  var1.reader().read(var139);
                  var138 = CRes.createImage(var139);
               }

               short var256 = var1.reader().readShort();
               Vector var140 = null;
               Vector var141 = null;
               if (var256 > 0) {
                  var140 = readMapItemType(var1);
                  var141 = readMapItem(var1);
               }

               MapScr.gI();
               MapScr.onSelectedMiniMap(var134, var253, var254, var132, var138, var135, var140, var141);
               return;
            case -92:
               var1.reader().readByte();
               byte[] var125 = new byte[var1.reader().readInt()];
               var1.reader().read(var125);
               int var126 = var1.reader().readInt();
               var1.reader().readByte();
               byte[] var127 = new byte[var126];

               for(int var128 = 0; var128 < var126; ++var128) {
                  var127[var128] = var1.reader().readByte();
               }

               byte var252 = var1.reader().readByte();
               Vector var129 = new Vector();

               for(int var130 = 0; var130 < var252; ++var130) {
                  PositionMap var131 = new PositionMap();
                  var1.reader().readByte();
                  var131.d = var1.reader().readShort();
                  var131.c = var1.reader().readUTF();
                  var131.x = var1.reader().readByte();
                  var131.y = var1.reader().readByte();
                  var129.addElement(var131);
               }

               int tilePx = 16 * AvMain.hd;
               FrameImage fi;
               if (var125.length > 0) {
                  Image cityImg = null;

                  try {
                     cityImg = CRes.createImage(var125);
                  } catch (Exception var125img) {
                  }

                  if (cityImg != null) {
                     fi = new FrameImage(cityImg, tilePx, tilePx);
                  } else {
                     FilePack.init(T.aw);
                     fi = FrameImage.init("ct", tilePx, tilePx);
                     FilePack.reset();
                  }
               } else {
                  FilePack.init(T.aw);
                  fi = FrameImage.init("ct", tilePx, tilePx);
                  FilePack.reset();
               }

               byte mapCols = 34;
               if (var126 > 0 && var126 % mapCols != 0) {
                  mapCols = 1;
               }

               MiniMap.isCityMap = false;
               LoadMap.idTileImg = -1;
               MiniMap.gI().setInfo(fi, var127, var129, mapCols, tilePx, new Command(T.selectt, new ISelectMiniMapAction(MapScr.gI())));
               MiniMap.gI().cmdUpdateKey = new IActionMiniMapKey(MapScr.gI());
               Canvas.endDlg();
               MiniMap.gI().switchToMe();
               LoadMap.TYPEMAP = -1;
               LoadMap.typeAny = -108;
               LoadMap.typeTemp = -1;
               return;
            case -90:
            case -53:
               var206 = var1.reader().readByte();
               var190 = var1.reader().readUTF();
               this.c.onUpdateCHest((byte)(var1.command == -53 ? 0 : 1), var206, var190);
               return;
            case -89:
               HouseScr.gI().onTransChestPart(var1.reader().readBoolean(), var1.reader().readUTF());
               return;
            case -88:
               HouseScr.gI();
               HouseScr.onEnterPass();
               return;
            case -87:
               short var117 = var1.reader().readShort();
               Vector var118 = new Vector();

               int var119;
               for(var119 = 0; var119 < var117; ++var119) {
                  SeriPart var120;
                  (var120 = new SeriPart()).idPart = var1.reader().readShort();
                  var120.time = var1.reader().readByte();
                  var120.expireString = var1.reader().readUTF();
                  var118.addElement(var120);
               }

               var119 = var1.reader().readInt();
               byte var251 = var1.reader().readByte();
               short var121 = var1.reader().readShort();
               Vector var122 = new Vector();

               for(int var123 = 0; var123 < var121; ++var123) {
                  SeriPart var124;
                  (var124 = new SeriPart()).idPart = var1.reader().readShort();
                  var124.time = var1.reader().readByte();
                  var124.expireString = var1.reader().readUTF();
                  var122.addElement(var124);
               }

               HouseScr.gI().onCustomChest(var118, var122, var119, var251);
               return;
            case -85:
               var107 = var1.reader().readInt();
               byte var241 = var1.reader().readByte();
               Vector var244 = new Vector();

               for(var110 = 0; var110 < var241; ++var110) {
                  Emotion var249 = new Emotion(this);
                  var1.reader().readByte();
                  var249.id = var1.reader().readShort();
                  var249.time = var1.reader().readShort();
                  var244.addElement(var249);
               }

               MapScr.gI();
               MapScr.onEmotionList(var107, var244);
               return;
            case -84:
               var234 = var1.reader().readByte();
               byte var238;
               if ((var238 = var1.reader().readByte()) != 5 && var238 != 2) {
                  int var111;
                  byte var245;
                  if (var234 == 0) {
                     if (AvatarData.getEffect((short)var238) == null) {
                        AvatarService.gI().doRequestEffectData((short)var238);
                     }

                     EffectManager var240;
                     (var240 = new EffectManager()).ID = var238;
                     var240.style = var1.reader().readByte();
                     var240.loopLimit = var240.count = var1.reader().readByte();
                     if (var240.style != 4) {
                        var240.loop = var1.reader().readShort();
                        var240.loopType = var1.reader().readByte();
                        if (var240.loopType == 1) {
                           var240.radius = var1.reader().readShort();
                        } else if (var240.loopType == 2) {
                           byte var243 = var1.reader().readByte();
                           var240.xLoop = new short[var243];
                           var240.yLoop = new short[var243];

                           for(var110 = 0; var110 < var243; ++var110) {
                              var240.xLoop[var110] = var1.reader().readShort();
                              var240.yLoop[var110] = var1.reader().readShort();
                           }
                        }

                        if (var240.style == 0) {
                           var240.idPlayer = var1.reader().readInt();
                        } else {
                           var240.x = var1.reader().readShort();
                           var240.y = var1.reader().readShort();
                        }

                        MapScr.gI();
                        MapScr.onEffect(var240);
                        return;
                     }

                     var109 = var1.reader().readShort();
                     var245 = var1.reader().readByte();
                     if (Canvas.currentEffect.size() > 0) {
                        for(var111 = 0; var111 < Canvas.currentEffect.size(); ++var111) {
                           if (((Effect) Canvas.currentEffect.elementAt(var111)).IDAction == var238) {
                              return;
                           }
                        }
                     }

                     AnimateEffect var247;
                     (var247 = new AnimateEffect(2, var109)).timeStop = var245;
                     var247.IDAction = var238;
                     var247.show();
                     return;
                  }

                  EffectData var239;
                  (var239 = new EffectData()).ID = var238;
                  byte[] var242 = new byte[var1.reader().readShort()];
                  var1.reader().read(var242);
                  var239.img = CRes.createImage(var242);
                  var245 = var1.reader().readByte();
                  var239.imgImfo = new ImageInfo[var245];

                  for(var111 = 0; var111 < var245; ++var111) {
                     var239.imgImfo[var111] = new ImageInfo();
                     var239.imgImfo[var111].ID = var1.reader().readByte();
                     var239.imgImfo[var111].x0 = var1.reader().readByte();
                     var239.imgImfo[var111].y0 = var1.reader().readByte();
                     var239.imgImfo[var111].w = var1.reader().readByte();
                     var239.imgImfo[var111].h = var1.reader().readByte();
                  }

                  byte var246 = var1.reader().readByte();
                  var239.frame = new Frame[var246];

                  int var250;
                  for(int var112 = 0; var112 < var246; ++var112) {
                     var239.frame[var112] = new Frame(this);
                     var250 = var1.reader().readByte();
                     var239.frame[var112].dx = new short[var250];
                     var239.frame[var112].dy = new short[var250];
                     var239.frame[var112].idImg = new byte[var250];

                     for(var114 = 0; var114 < var250; ++var114) {
                        var239.frame[var112].dx[var114] = var1.reader().readByte();
                        var239.frame[var112].dy[var114] = var1.reader().readByte();
                        var239.frame[var112].idImg[var114] = var1.reader().readByte();
                     }
                  }

                  byte var248 = var1.reader().readByte();
                  var239.arrFrame = new byte[var248];

                  for(var250 = 0; var250 < var248; ++var250) {
                     var239.arrFrame[var250] = var1.reader().readByte();
                  }

                  AvatarData.effectList.addElement(var239);
                  return;
               }

               return;
            case -83:
               byte var92 = var1.reader().readByte();
               Vector var93 = new Vector();

               for(var94 = 0; var94 < var92; ++var94) {
                  StringObj var231;
                  (var231 = new StringObj()).anthor = var1.reader().readShort();
                  var231.str = var1.reader().readUTF();
                  var231.dis = var1.reader().readShort();
                  var93.addElement(var231);
               }

               MapScr.gI().onMenuRotate(var93);
               return;
            case -82:
               int var90 = var1.reader().readInt();
               short var91 = var1.reader().readShort();
               MapScr.gI();
               MapScr.onChangeClan(var90, var91);
               return;
            case -81:
               String var87 = var1.reader().readUTF();
               int var88 = 0;

               for(int var89 = 0; var89 < var87.length(); ++var89) {
                  if (var87.charAt(var89) == '-') {
                     ++var88;
                  }
               }

               byte[] var230 = new byte[var1.reader().available()];
               var1.reader().read(var230);
               if (var88 != 2 && !var87.equals(ListScr.idFriendList)) {
                  ListScr.gI().readList(var230, var87);
                  Canvas.endDlg();
                  return;
               }

               ListScr.hList.put(var87, var230);
               ListScr.gI().setList(var87);
               return;
            case -80:
               var84 = var1.reader().readShort();
               byte[] var86 = new byte[var1.reader().readShort()];
               var1.reader().read(var86);
               AvatarData.listImgIcon.put("" + var84, new ImageIcon(CRes.createImage(var86)));
               return;
            case -78:
               byte var232 = var1.reader().readByte();
               int var100 = var1.reader().readInt();
               byte var101 = var1.reader().readByte();
               String var102 = var1.reader().readUTF();
               short var103;
               if ((var103 = var1.reader().readShort()) > 0) {
                  short[] var233 = new short[var103];
                  String[] var235 = new String[var103];
                  String[] var237 = null;
                  if (var232 == 1) {
                     var237 = new String[var103];
                  }

                  for(var107 = 0; var107 < var103; ++var107) {
                     var233[var107] = var1.reader().readShort();
                     var235[var107] = var1.reader().readUTF();
                     if (var232 == 1) {
                        var237[var107] = var1.reader().readUTF();
                     }
                  }

                  MapScr.gI().onOpenShop(var232, var101, var102, var233, var100, var235);
               }

               return;
            case -77:
               var94 = var1.reader().readInt();
               byte var95 = var1.reader().readByte();
               String var96 = var1.reader().readUTF();
               byte var97;
               String[] var98 = new String[var97 = var1.reader().readByte()];

               for(int var99 = 0; var99 < var97; ++var99) {
                  var98[var99] = var1.reader().readUTF();
               }

               if (PopupShop.me != Canvas.currentMyScreen) {
                  MapScr.gI().showActionMenu(var94, var95, var96, var98);
               }

               return;
            case -74:
               MapItem var180;
               (var180 = new MapItem()).typeID = var1.reader().readShort();
               var180.x = 24 * var1.reader().readByte();
               var180.y = 24 * var1.reader().readByte();
               HouseScr.gI().onBuyItemHouse(var180);
               return;
            case -70:
               var82 = var1.reader().readInt();
               byte var229 = (byte)(100 - var1.reader().readByte());
               MapScr.gI();
               MapScr.onRequestExpicePet(var82, var229);
               return;
            case -64:
               int var78 = var1.reader().readInt();
               short var79 = var1.reader().readShort();
               byte var80 = var1.reader().readByte();
               Vector var81 = new Vector();

               for(var82 = 0; var82 < var80; ++var82) {
                  Gift var83;
                  (var83 = new Gift()).type = var1.reader().readByte();
                  switch (var83.type) {
                     case 1:
                        var83.idPart = var1.reader().readShort();
                        if ((var84 = var1.reader().readByte()) == -1) {
                           var83.expire = "(" + T.forever + ")";
                        } else {
                           var83.expire = "(" + var84 + " " + T.day + ")";
                        }
                        break;
                     case 2:
                        var83.xu = var1.reader().readInt();
                        break;
                     case 3:
                        var83.xp = var1.reader().readInt();
                        break;
                     case 4:
                        var83.luong = var1.reader().readInt();
                  }

                  var81.addElement(var83);
               }

               DialLuckyScr.gI().onStart(var78, var79, var81);
               return;
            case -63:
               LoadMap.onWeather(var1.reader().readByte());
               return;
            case -62:
               System.out.println("CHANGE_PASS");
               LoginScr.gI().tfPass.setText(var1.reader().readUTF());
               LoginScr.gI().saveLogin();
               break;
            case -60:
               var72 = var1.reader().readInt();
               byte var73 = var1.reader().readByte();
               String var74 = var1.reader().readUTF();
               byte var75 = var1.reader().readByte();
               byte[] var76 = null;
               if (var1.reader().available() > 0) {
                  var76 = new byte[var1.reader().readShort()];
                  var1.reader().read(var76);
               }

               Canvas.inputDlg.setImg(var74, new IdoTextBox(this, var72, var73), var75);
               if (var76 != null) {
                  Canvas.inputDlg.setImg(Image.createImage(var76, 0, var76.length));
               }

               return;
            case -59:
               if (Canvas.currentDialog == Canvas.msgdlg) {
                  Canvas.currentDialog = null;
               }

               if (Canvas.currentDialog != null) {
                  return;
               }

               var64 = var1.reader().readInt();
               byte var225 = var1.reader().readByte();
               byte var226;
               String[] var227 = new String[var226 = var1.reader().readByte()];
               short[] var68 = new short[var226];

               int var69;
               for(var69 = 0; var69 < var226; ++var69) {
                  var227[var69] = var1.reader().readUTF();
               }

               if (var1.reader().available() > 0) {
                  for(var69 = 0; var69 < var226; ++var69) {
                     var68[var69] = var1.reader().readShort();
                  }
               }

               String var228 = null;
               String var70 = null;
               boolean[] var71 = null;
               if (var1.reader().available() > 0) {
                  var228 = var1.reader().readUTF();
                  var70 = var1.reader().readUTF();
                  var71 = new boolean[var226];

                  for(var72 = 0; var72 < var226; ++var72) {
                     var71[var72] = var1.reader().readBoolean();
                  }
               }

               this.c.onMenuOption(var64, var225, var227, var228, var70, var71);
               return;
            case -58:
               var206 = var1.reader().readByte();
               Hashtable var219 = new Hashtable();

               for(var4 = 0; var4 < var206; ++var4) {
                  var200 = var1.reader().readShort();
                  byte[] var223 = new byte[var1.reader().readShort()];
                  var1.reader().read(var223);
                  Image var224 = CRes.createImage(var223);
                  var219.put("" + var200, var224);
               }

               var194 = var1.reader().readUTF();
               var202 = var1.reader().readUTF();
               System.err.println("CUSTOM_TAB: " + var194);
               System.err.println("CUSTOM_TAB111: " + var202);
               byte var217 = -1;
               if (var1.reader().available() > 0) {
                  var217 = var1.reader().readByte();
               }

               CustomTab.me = null;
               CustomTab.gI().setInfo(var219, var194, var202, var217);
               CustomTab.gI().show();
               return;
            case -54:
               var188 = var1.reader().readUTF();
               var190 = var1.reader().readUTF();
               var194 = var1.reader().readUTF();
               Canvas.endDlg();
               Canvas.startOKDlg(var188, (IAction)(new IActionOnSMS(this, var190, var194)));
               break;
            case -52:
               var188 = var1.reader().readUTF();
               var1.reader().readInt();
               LoginScr.gI().onNumSupport(var188);
               return;
            case -51:
               var206 = var1.reader().readByte();
               byte[] var218 = new byte[var1.reader().available()];
               var1.reader().read(var218);
               SoundManager.instance.onSoundData(var218, var206);
               return;
            case -50:
               var204 = var1.reader().readUTF();
               var206 = var1.reader().readByte();
               SoundManager.instance.onRequestOpenSound(var204, var206);
               return;
            case -49:
               var206 = var1.reader().readByte();
               System.out.println("OPEN_SHOP: " + var206);
               var190 = var1.reader().readUTF();
               short[] var210 = null;
               if ((var200 = var1.reader().readShort()) > 0) {
                  var210 = new short[var200];

                  for(var209 = 0; var209 < var200; ++var209) {
                     var210[var209] = var1.reader().readShort();
                  }
               }

               MapScr.gI().onOpenShop((byte)0, var206, var190, var210, -1, (String[])null);
               return;
            case -48:
               var3 = var1.reader().readInt();
               var199 = var1.reader().readShort();
               MapScr.gI();
               MapScr.onUsingPart(var3, var199);
               return;
            case -47:
               Vector var215 = new Vector();
               var201 = var1.reader().readShort();

               for(var3 = 0; var3 < var201; ++var3) {
                  (var207 = new SeriPart()).idPart = var1.reader().readShort();
                  var207.time = var1.reader().readByte();
                  var207.expireString = var1.reader().readUTF();
                  var215.addElement(var207);
               }

               MapScr.gI().onContainer(var215);
               return;
            case -42:
               var6 = new Vector();
               var201 = var1.reader().readByte();

               for(var3 = 0; var3 < var201; ++var3) {
                  ObjAd var205 = new ObjAd(this);
                  var1.reader().readShort();
                  var205.title = var1.reader().readUTF();
                  var205.text = var1.reader().readUTF();
                  var205.url = var1.reader().readUTF();
                  var205.sms = var1.reader().readUTF();
                  var205.to = var1.reader().readUTF();
                  var205.listPoint = new Vector();
                  var200 = var1.reader().readByte();

                  for(var7 = 0; var7 < var200; ++var7) {
                     AvPosition var213;
                     (var213 = new AvPosition()).anchor = var1.reader().readByte();
                     var213.x = var1.reader().readByte();
                     var213.y = var1.reader().readByte();
                     var205.listPoint.addElement(var213);
                  }

                  var6.addElement(var205);
               }

               for(var3 = 0; var3 < var201; ++var3) {
                  ((ObjAd)var6.elementAt(var3)).id = var1.reader().readByte();
               }

               AvatarData.onMapAd(var6);
               return;
            case -38:
               short var150 = var1.reader().readShort();
               int var151 = 0;
               if (var150 != -1) {
                  var151 = var1.reader().readInt();
               }

               int var152 = var1.reader().readInt();
               int var153 = var1.reader().readInt();
               int var154 = var1.reader().readInt();
               GameMidlet.avatar.updateMoney(var152, var153, var154);
               MapScr.gI();
               MapScr.onBuyIceDream(var150, var151);
               return;
            case -36:
               var2 = var1.reader().readInt();
               short var208 = var1.reader().readShort();
               MapScr.gI();
               MapScr.onRemoveItem(var2, (int)var208);
               return;
            case -35:
               var191 = var1.reader().readBoolean();
               RegisterScr.gI();
               RegisterScr.onCreaCharacter(var191);
               return;
            case -33:
               var3 = var1.reader().readInt();
               var199 = var1.reader().readByte();
               if (var3 != 0 && var199 != 1 && var199 == 2 && var199 == 5) {
                  GameMidlet.avatar.setMoneyNew(GameMidlet.avatar.money[3] + var3);
                  Canvas.addFlyTextSmall(var3 + "xeng", GameMidlet.avatar.x, GameMidlet.avatar.y, -1, 0, -1);
               }

               if (var1.reader().available() < 12) {
                  System.out.println("[WARN] cmd -33 payload too short: " + var1.reader().available() + " bytes");
                  return;
               }

               try {
                  var5 = var1.reader().readInt();
                  var7 = var1.reader().readInt();
                  var8 = var1.reader().readInt();
               } catch (EOFException var214) {
                  System.out.println("[WARN] cmd -33 parse money failed (EOF)");
                  return;
               }

               GameMidlet.avatar.updateMoney(var5, var7, var8);
               return;
            case -25:
               var199 = var1.reader().readByte();
               var188 = null;
               var190 = null;
               var202 = null;
               if (var199 == 2) {
                  var190 = var1.reader().readUTF();
                  var202 = var1.reader().readUTF();
               } else {
                  var188 = var1.reader().readUTF();
               }

               MiniMap.gI().onRegisterByEmail((byte)var199, var188, var190, var202);
               break;
            case -24:
               if ((var110 = var1.reader().readShort()) != -1) {
                  var1.reader().readInt();
                  var1.reader().readByte();
               }

               String var113 = var1.reader().readUTF();
               var114 = var1.reader().readInt();
               int var115 = var1.reader().readInt();
               int var116 = var1.reader().readInt();
               MapScr.gI();
               MapScr.onBuyItem((short)var110, var113, var114, var115, var116);
               return;
            case -23:
               Vector var193 = new Vector();

               while(var1.reader().available() > 0) {
                  MoneyInfo var198;
                  (var198 = new MoneyInfo()).info = var1.reader().readUTF();
                  var198.smsTo = var1.reader().readUTF();
                  var1.reader().readUTF();
                  var198.smsContent = var1.reader().readUTF();
                  var193.addElement(var198);
               }

               MoneyScr.gI().setAvatarList(var193);
               MoneyScr.gI().showWithBack(Canvas.currentMyScreen);
               Canvas.endDlg();
               return;
            case -22:
               var2 = var1.reader().readInt();
               IndexPlayer var197;
               (var197 = new IndexPlayer()).g = var1.reader().readByte();
               var197.f = var1.reader().readByte();
               var197.a = var1.reader().readByte();
               var197.b = var1.reader().readByte();
               var197.e = var1.reader().readByte();
               var197.c = var1.reader().readByte();
               var197.d = var1.reader().readByte();
               Avatar var195 = null;
               var5 = var1.reader().readInt();
               var204 = "";
               String var203 = "";
               short var211 = 0;
               byte var212 = 0;
               byte var10 = 0;
               short var11 = -1;
               String var12 = "";
               if (var5 != -1) {
                  (var195 = new Avatar()).IDDB = var5;
                  var195.setName(var1.reader().readUTF());
                  var200 = var1.reader().readByte();

                  for(var209 = 0; var209 < var200; ++var209) {
                     var195.addSeri(new SeriPart(var1.reader().readShort()));
                  }

                  var204 = var1.reader().readUTF();
                  var211 = var1.reader().readShort();
                  var212 = var1.reader().readByte();
                  var10 = var1.reader().readByte();
                  var203 = var1.reader().readUTF();
                  if ((var11 = var1.reader().readShort()) != -1) {
                     var12 = var1.reader().readUTF();
                  }
               }

               if (var1.reader().available() > 0) {
                  GameMidlet.avatar.lvMain = GameMidlet.myIndexP.g = var197.g = var1.reader().readShort();
               }

               MapScr.gI().updatePlayerInfo(var2, var197, var195, var204, var211, var212, var10, var203, var11, var12);
               return;
            case -21:
               Avatar var196;
               (var196 = new Avatar()).IDDB = var1.reader().readInt();
               var196.name = var1.reader().readUTF();
               var188 = var1.reader().readUTF();
               MapScr.gI().onRequestAddFriend(var196, var188);
               return;
            case -19:
               Avatar var189;
               (var189 = new Avatar()).IDDB = var1.reader().readInt();
               var189.name = var1.reader().readUTF();
               var191 = var1.reader().readBoolean();
               var190 = var1.reader().readUTF();
               MapScr.gI();
               MapScr.onAddFriend(var191, var190);
               return;
            case -17:
               GameMidlet.PROVIDER = var1.reader().readByte();
               GameMidlet.g = var1.reader().readUTF();
               AvatarData.saveProvider();
               break;
            case -12:
               var188 = var1.reader().readUTF();
               var190 = var1.reader().readUTF();
               LoginScr.gI().onLoginNewGame(var188, var190);
               break;
            case -10:
               var188 = var1.reader().readUTF();
               boolean var192 = false;
               if (var1.reader().available() > 0) {
                  var192 = var1.reader().readBoolean();
               }

               this.c.onSetMoneyError(var188, var192);
               return;
            case -9:
               Canvas.startOKDlg(var1.reader().readUTF());
               return;
            case -8:
               Canvas.showScrollInfo(var1.reader().readUTF());
               return;
            case -7:
               this.c.onVersion(var1.reader().readUTF(), var1.reader().readUTF());
               return;
            case -6:
               var2 = var1.reader().readInt();
               var190 = var1.reader().readUTF();
               var194 = var1.reader().readUTF();
               if (Canvas.currentMyScreen != MessageScr.gI()) {
                  ++MyScreen.nMsg;
               }

               MessageScr.gI().addPlayer(var2, var190, var194);
               return;
            case -1:
               GlobalLogicHandler.doGetHandler(var1.reader().readByte());
               return;
            case 34:
               if (var1.reader().readInt() != -1) {
                  var204 = var1.reader().readUTF();
                  var2 = var1.reader().readInt();
                  var1.reader().readShort();
                  var3 = var1.reader().readInt();
                  var4 = var1.reader().readInt();
                  var5 = var1.reader().readInt();
                  var7 = var1.reader().readInt();
                  var8 = var1.reader().readInt();
                  Avatar var9;
                  (var9 = new Avatar()).setExp(var3);
                  Canvas.startOKDlg(T.nameStr + var204 + ". " + T.moneyStr + var2 + "$. Level: " + var9.lvMain + "+" + var9.perLvFarm + "%. " + T.win + ": " + var4 + ". " + T.lose  + ": " + var5 + ". " + T.draw  + ": " + var7 + ". " + T.give + ": " + var8);
               }

               return;
            case 50:
               if (this.miniGameMessageHandler == FarmMsgHandler.instance || this.miniGameMessageHandler == ParkMsgHandler.instance || this.miniGameMessageHandler == HomeMsgHandler.instance) {
                  var2 = var1.reader().readByte();
                  var3 = var1.reader().readByte();
                  var4 = 0;
                  var5 = 0;
                  var6 = new Vector();
                  if (var3 != -1 && var3 != -2) {
                     var4 = var1.reader().readShort();
                     var5 = var1.reader().readShort();
                     var6 = readListPlayer(var1);
                  }

                  var64 = var1.reader().readShort();
                  Vector var65 = null;
                  Vector var66 = null;
                  if (var64 > 0) {
                     var65 = readMapItemType(var1);
                     var66 = readMapItem(var1);
                  }

                  if (GameMidlet.CLIENT_TYPE == 9) {
                     for(int var67 = 0; var67 < var6.size(); ++var67) {
                        ((Avatar)var6.elementAt(var67)).idWedding = var1.reader().readShort();
                     }
                  }

                  if (var2 < 0 && this.miniGameMessageHandler == HomeMsgHandler.instance) {
                     var2 = 21;
                  }

                  MapScr.gI().onJoinPark((byte)var2, (byte)var3, (short)var4, (short)var5, var6, var65, var66);
                  if (LoadMap.TYPEMAP == 21) {
                     Canvas.load = 0;
                     HomeMsgHandler.onHandler();
                     AvatarService.gI().getTypeHouse((int)0);
                     Canvas.startWaitDlg();
                  }
               }
               break;
            case 89:
               byte var104 = var1.reader().readByte();
               System.out.println("DROP_PART: " + var104 + "    " + var1.reader().available());
               if (var104 == 0) {
                  var234 = var1.reader().readByte();
                  short var236 = var1.reader().readShort();
                  var107 = var1.reader().readInt();
                  int var108 = var1.reader().readInt();
                  System.out.println("aaaaaa: " + var234 + "   " + var236 + "   " + var107 + "   " + var108);
                  var109 = var1.reader().readShort();
                  var110 = var1.reader().readShort();
                  MapScr.gI();
                  MapScr.onDropPark(var234, var108, var236, var107, var109, (short)var110);
                  return;
               }

               int var105 = var1.reader().readInt();
               int var106 = var1.reader().readInt();
               MapScr.gI();
               MapScr.onGetPart(var105, var106);
               return;
            case 122:
               var1.reader().readByte();
               byte var182 = var1.reader().readByte();
               byte var183 = var1.reader().readByte();
               short var184 = var1.reader().readShort();
               short var185 = var1.reader().readShort();
               LoadMap.onDichChuyen(var182, var183, var184, var185);
               return;
         }
      } catch (Exception var187) {
         var187.printStackTrace();
      }

      if (this.miniGameMessageHandler != null) {
         if (var1.command == 55 && this.miniGameMessageHandler == FarmMsgHandler.instance) {
            try {
               int len = var1.reader().available();
               byte[] data = new byte[len];
               var1.reader().read(data);
               int count = 0;
               if (data.length >= 2) {
                  count = (data[0] & 255) << 8 | data[1] & 255;
               }

               if (count > 0 && data.length >= 2 + count * 8) {
                  FarmData.saveImageData(data);
               } else {
                  readChat(new Message((byte)55, data));
               }
            } catch (Exception var300) {
               var300.printStackTrace();
            }

            return;
         }

         this.miniGameMessageHandler.onMessage(var1);
      } else {
         try {
            System.out.println("cmd: " + var1.command);
            switch (var1.command) {
               case -5:
                  GlobalLogicHandler.onServerMessage(var1.reader().readUTF());
                  return;
               case -4:
                  LoginScr.gI().saveLogin();
                  (GameMidlet.avatar = new Avatar()).IDDB = var1.reader().readInt();
                  var206 = var1.reader().readByte();
                  GameMidlet.avatar.seriPart = new Vector();

                  for(var3 = 0; var3 < var206; ++var3) {
                     (var207 = new SeriPart()).idPart = var1.reader().readShort();
                     GameMidlet.avatar.addSeri(var207);
                  }

                  GameMidlet.avatar.gender = var1.reader().readByte();
                  GameMidlet.myIndexP.g = var1.reader().readByte();
                  GameMidlet.myIndexP.f = var1.reader().readByte();
                  GameMidlet.avatar.setMoney(var1.reader().readInt());
                  GameMidlet.myIndexP.a = var1.reader().readByte();
                  GameMidlet.myIndexP.b = var1.reader().readByte();
                  GameMidlet.myIndexP.e = var1.reader().readByte();
                  GameMidlet.myIndexP.c = var1.reader().readByte();
                  GameMidlet.myIndexP.d = var1.reader().readByte();
                  GameMidlet.avatar.money[2] = var1.reader().readInt();
                  GameMidlet.avatar.blogNews = var1.reader().readByte();

                  for(var3 = 0; var3 < GameMidlet.avatar.seriPart.size(); ++var3) {
                     (var207 = (SeriPart)GameMidlet.avatar.seriPart.elementAt(var3)).time = var1.reader().readByte();
                     var207.expireString = var1.reader().readUTF();
                  }

                  GameMidlet.avatar.idImg = var1.reader().readShort();
                  MapScr.listCmd = new Vector();
                  byte var221 = var1.reader().readByte();

                  for(var4 = 0; var4 < var221; ++var4) {
                     StringObj var216;
                     (var216 = new StringObj()).str = var1.reader().readUTF();
                     var216.dis = var1.reader().readShort();
                     MapScr.listCmd.addElement(var216);
                  }

                  MapScr.listCmdRotate = new Vector();
                  byte var220 = var1.reader().readByte();

                  for(var2 = 0; var2 < var220; ++var2) {
                     StringObj var222;
                     (var222 = new StringObj()).anthor = var1.reader().readShort();
                     var222.str = var1.reader().readUTF();
                     var222.dis = var1.reader().readShort();
                     MapScr.listCmdRotate.addElement(var222);
                  }

                  MapScr.gI().isTour = var1.reader().readBoolean();
                  if (var1.reader().available() > 0) {
                     for(var2 = 0; var2 < var220; ++var2) {
                        ((StringObj)MapScr.listCmdRotate.elementAt(var2)).type = var1.reader().readByte();
                     }
                  }

                  if (var1.reader().available() > 0) {
                     Canvas.W = var1.reader().readByte();
                  }

                  GameMidlet.avatar.lvMain = GameMidlet.myIndexP.g = var1.reader().readShort();
                  if (Canvas.W == 1 || Canvas.W == 2) {
                     T.constructing = T.roomList;
                  }

                  GameMidlet.avatar.idWedding = var1.reader().readShort();
                  if (var1.reader().available() > 0) {
                     MapScr.isNewVersion = var1.reader().readBoolean();
                  }

                  if (MapScr.isNewVersion) {
                     GameMidlet.avatar.money[3] = var1.reader().readInt();
                  }

                  MapScr.listItemEffect = new Vector();
                  var206 = var1.reader().readByte();

                  for(var3 = 0; var3 < var206; ++var3) {
                     ItemEffectInfo var214;
                     (var214 = new ItemEffectInfo()).IDAction = var1.reader().readShort();
                     var214.name = var1.reader().readUTF();
                     var214.IDIcon = var1.reader().readShort();
                     var214.money = var1.reader().readInt();
                     var214.typeMoney = var1.reader().readByte();
                     MapScr.listItemEffect.addElement(var214);
                  }

                  GameMidlet.avatar.setGold(var1.reader().readInt());
                  GameMidlet.avatar.luongKhoa = var1.reader().readInt();
                  var1.reader().readByte();
                  var202 = var1.reader().readUTF();
                  GameMidlet.avatar.setName(var202);
                  System.out.println("money: " + GameMidlet.avatar.money[2] + "    " + var202);
                  GlobalLogicHandler.onLoginSuccess();
                  System.out.println("2222222222222222222");
               default:
            }
         } catch (Exception var186) {
            var186.printStackTrace();
         }
      }
   }

   public static Vector readListPlayer(Message var0) {
      Vector var1 = new Vector();

      try {
         byte var2 = var0.reader().readByte();

         int var3;
         int var5;
         for(var3 = 0; var3 < var2; ++var3) {
            Avatar var4;
            (var4 = new Avatar()).IDDB = var0.reader().readInt();
            var4.setName(var0.reader().readUTF());
            var5 = var0.reader().readByte();

            for(int var6 = 0; var6 < var5; ++var6) {
               short var7 = var0.reader().readShort();
               var4.addSeri(new SeriPart(var7));
            }

            var4.x = var0.reader().readShort();
            var4.y = var0.reader().readShort();
            var4.blogNews = var0.reader().readByte();
            var1.addElement(var4);
         }

         for(var3 = 0; var3 < var2; ++var3) {
            ((Avatar)var1.elementAt(var3)).direct = var0.reader().readByte();
         }

         for(var3 = 0; var3 < var2; ++var3) {
            ((Avatar)var1.elementAt(var3)).hungerPet = (byte)(100 - var0.reader().readByte());
         }

         for(var3 = 0; var3 < var2; ++var3) {
            ((Avatar)var1.elementAt(var3)).idImg = var0.reader().readShort();
         }

         byte var12 = var0.reader().readByte();

         for(int var9 = 0; var9 < var12; ++var9) {
            Drop_Part var11;
            (var11 = new Drop_Part()).type = var0.reader().readByte();
            var11.idDrop = var0.reader().readShort();
            var11.ID = var0.reader().readInt();
            var11.x = var0.reader().readShort();
            var11.y = var0.reader().readShort();
            var1.addElement(var11);
         }

         LoadMap.listImgAD = null;
         byte var10 = 0;
         if (var0.reader().available() > 0) {
            var10 = var0.reader().readByte();
         }

         if (var10 > 0) {
            LoadMap.listImgAD = new Vector();

            for(var5 = 0; var5 < var10; ++var5) {
               AvPosition var13;
               (var13 = new AvPosition()).anchor = var0.reader().readShort();
               var13.x = var0.reader().readShort();
               var13.y = var0.reader().readShort();
               var13.depth = var0.reader().readByte();
               LoadMap.listImgAD.addElement(var13);
            }
         }
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      return var1;
   }

   private static Vector readMapItem(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         System.out.println("readMapItemaaaa: " + var1);
         Vector var2 = new Vector();

         for(int var3 = 0; var3 < var1; ++var3) {
            MapItem var4;
            (var4 = new MapItem()).type = var0.reader().readByte();
            var4.typeID = var0.reader().readByte();
            var4.x = var0.reader().readByte();
            var4.y = var0.reader().readByte();
            var4.isGetImg = true;
            var2.addElement(var4);
         }

         return var2;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private static Vector readMapItemType(Message var0) {
      try {
         byte var1 = var0.reader().readByte();
         Vector var2 = new Vector();
         System.out.println("size item: " + var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            MapItemType var4;
            (var4 = new MapItemType()).idType = var0.reader().readByte();
            var4.imgID = var0.reader().readShort();
            var4.iconID = var0.reader().readByte();
            var4.dx = var0.reader().readShort();
            var4.dy = var0.reader().readShort();
            byte var5 = var0.reader().readByte();
            var4.listNotTrans = new Vector();

            for(int var6 = 0; var6 < var5; ++var6) {
               AvPosition var7;
               (var7 = new AvPosition()).x = var0.reader().readByte();
               var7.y = var0.reader().readByte();
               var4.listNotTrans.addElement(var7);
            }

            var2.addElement(var4);
         }

         return var2;
      } catch (IOException var8) {
         var8.printStackTrace();
         return null;
      }
   }

   public static void readMove(Message var0) throws IOException {
      int var1 = var0.reader().readInt();
      short var2 = var0.reader().readShort();
      short var3 = var0.reader().readShort();
      byte var4 = var0.reader().readByte();
      short var5 = 0;
      if (var0.reader().available() > 0) {
         var5 = var0.reader().readShort();
      }

      MapScr.gI();
      MapScr.onMovePark(var1, var2, var3, var4, (short)var5);
   }

   public static void readChat(Message var0) throws IOException {
      int var1 = var0.reader().readInt();
      String var2 = var0.reader().readUTF();
      MapScr.gI();
      MapScr.onChatFrom(var1, var2);
   }
}