package avt;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import main.Canvas;
import main.GameMidlet;

public final class LoginScr extends MyScreen {
   public static LoginScr me;
   private static final String RMS_SAVED_CHARS = "avChars258";
   private static final String RMS_LOGIN_KEY = "loginKey258";
   private static boolean isLoginKeyVerified = true ;
   private static boolean suppressAutoKeyPopup;
   private static String currentLoginKey = "";
   private static long nextKeyCheckAt;
   private static boolean isCheckingKeyOnline;
   private static String pendingKeyKickMessage;
   private static final String RMS_KEY_API_URL = "keyApiUrl258";
   private static String KEY_API_URL = "http://160.191.242.130/validate_key.php";
   private static boolean triedAutoVerifySavedKey;
   private static final byte CMD_MENU_OPEN_SWITCH_CHAR = 105;
   private static final byte CMD_MENU_SELECT_CHAR = 106;
   private static final byte CMD_MENU_ADD_CHAR = 107;
   private static final byte CMD_MENU_OPEN_LANG = 108;
   private static final byte CMD_MENU_LANG_VI = 109;
   private static final byte CMD_MENU_LANG_ID = 110;
   public TField tfUser = new TField();
   public TField tfPass = new TField();
   public TField tfReg = new TField();
   public TField tfEmail = new TField();
   private int focus;
   private int yL;
   private int defYL;
   private Command cmdRemem;
   Command cmdLogin;
   private Command D;
   Command g;
   private boolean isCheckBox = true;
   Command cmdMenu;
   public boolean isReg = false;
   private String numSupport = "0979486946";
   public int xLogin;
   public int yLogin;
   public int wLogin;
   public int hLogin;
   public int xCheck;
   public int yCheck;
   public int wC;
   public int xC;
   public long timeOut = 0L;
   public static int s = 0;
   public static String t;
   public static boolean isSelectedLanguage = false;
   public static boolean isNewGame;
   public static boolean isAccVir;
   private static boolean isShownLoginServerInfo;
   private String[] listStrNew = new String[0];
   public int hCellNew;
   public int yNew;
   private byte indexNewGame;
   private String nameVir = "";
   private String passVir = "";
   private boolean K;
   private String[] savedCharUsers = new String[0];
   private String[] savedCharPasses = new String[0];

   public static LoginScr gI() {
      if (me == null) {
         me = new LoginScr();
      }

      return me;
   }

   public final void close() {
      Canvas.startOKDlg(T.doYouWantExit2, 54);
   }

   private void refreshLoginTexts() {
      if (this.nameVir.equals("") && this.tfUser.getText().equals("")) {
         this.listStrNew = new String[]{T.loginPlayNew, T.loginChangeAccount};
      } else {
         this.listStrNew = new String[]{T.loginPlayContinue + (!this.tfUser.getText().equals("") ? ", " + this.tfUser.getText() : ""), T.loginPlayNew, T.loginChangeAccount};
      }
      this.tfEmail.q = T.loginEmailOptional;
      if (this.cmdMenu != null) {
         this.cmdMenu.caption = T.menu;
      }
      if (this.cmdLogin != null) {
         this.cmdLogin.caption = T.selectt;
      }
      if (this.g != null) {
         this.g.caption = T.selectt;
      }
      if (this.cmdRemem != null) {
         this.cmdRemem.caption = this.isCheckBox ? T.removee : T.remem;
      }
      if (this.D != null) {
         this.D.caption = T.register;
      }
   }

   public final void switchToMe() {
      this.initCmd();
      super.switchToMe();
      Canvas.endDlg();
      if (Canvas.isKeyBoard) {
         this.indexNewGame = -1;
      }

      isNewGame = true;
      super.center = this.g;
      this.refreshLoginTexts();

      if (!isShownLoginServerInfo) {
         isShownLoginServerInfo = true;
         Canvas.addServerInfo(T.loginServerInfo);
      }

      if (!isLoginKeyVerified) {
         if (!suppressAutoKeyPopup) {
            this.showLoginKeyPopup();
         }
      }
   }

   private boolean tryAutoVerifySavedLoginKey() {
      if (triedAutoVerifySavedKey) {
         return false;
      }
      triedAutoVerifySavedKey = true;
      if (isCheckingKeyOnline) {
         return true;
      }
      String saved = CRes.b(RMS_LOGIN_KEY);
      if (saved == null) {
         return false;
      }
      saved = saved.trim();
      if (saved.length() == 0) {
         return false;
      }

      final String key = saved;
      isCheckingKeyOnline = true;
      Canvas.startWaitDlg(T.loginCheckingKey);
      (new Thread(new Runnable() {
         public void run() {
            try {
               String url = LoginScr.KEY_API_URL + "?key=" + LoginScr.encodeUrlParam(key);
               String resp = GameMidlet.createhttpconnect(url);
               Canvas.endDlg();
               if (resp == null) {
                  Canvas.startOK(T.loginKeyConnectFail, new IAction() {
                     public void perform() {
                        LoginScr.this.showLoginKeyPopup();
                     }
                  });
                  return;
               }

               resp = resp.trim();
               if ("OK".equals(resp)) {
                  LoginScr.isLoginKeyVerified = true;
                  LoginScr.currentLoginKey = key;
                  LoginScr.nextKeyCheckAt = System.currentTimeMillis() + 10000L;
                  return;
               }

            
               CRes.a(LoginScr.RMS_LOGIN_KEY, "");
               LoginScr.isLoginKeyVerified = false;
               LoginScr.currentLoginKey = "";
               LoginScr.nextKeyCheckAt = 0L;
               Canvas.startOK(T.loginKeyInvalid, new IAction() {
                  public void perform() {
                     LoginScr.this.showLoginKeyPopup();
                  }
               });
            } finally {
               LoginScr.isCheckingKeyOnline = false;
            }
         }
      })).start();
      return true;
   }

   private void showLoginKeyPopup() {
      Canvas.inputDlg.setImg(T.loginEnterKey, new IAction() {
         public void perform() {
            String in = Canvas.inputDlg.getText();
            if (in == null) {
               in = "";
            }
            in = in.trim();

            if (in.length() == 0) {
               Canvas.startOKDlg(T.loginKeyRequired);
               return;
            }

            Canvas.currentDialog = null;
            Canvas.startWaitDlg(T.loginCheckingKey);

            final String key = in;
            (new Thread(new Runnable() {
               public void run() {
                 
                  String url = LoginScr.KEY_API_URL + "?key=" + LoginScr.encodeUrlParam(key) + "&claim=1";
                  String resp = GameMidlet.createhttpconnect(url);
                  if (resp == null) {
                     Canvas.endDlg();
                     Canvas.startOK(T.loginKeyConnectFail, new IAction() {
                        public void perform() {
                           LoginScr.this.showLoginKeyPopup();
                        }
                     });
                     return;
                  }

                  resp = resp.trim();
                  Canvas.endDlg();

                  if (resp.startsWith("DIS_OLD:")) {
                     CRes.a(LoginScr.RMS_LOGIN_KEY, "");
                     Canvas.startOK("Key sudah melebihi batas perangkat!\nIP pengganti: " + resp.substring(8), new IAction() {
                        public void perform() {
                           LoginScr.this.showLoginKeyPopup();
                        }
                     });
                     return;
                  }

                  if ("MANY_DEVICES".equals(resp)) {
                     CRes.a(LoginScr.RMS_LOGIN_KEY, "");
                     Canvas.startOK(T.loginKeyTooManyDevices, new IAction() {
                        public void perform() {
                           LoginScr.this.showLoginKeyPopup();
                        }
                     });
                     return;
                  }

                  if ("OK".equals(resp)) {
                     LoginScr.isLoginKeyVerified = true;
                     LoginScr.currentLoginKey = key;
                     LoginScr.nextKeyCheckAt = System.currentTimeMillis() + 10000L;
                     CRes.a(LoginScr.RMS_LOGIN_KEY, key);
                     return;
                  }

                  if ("USED".equals(resp)) {
                     CRes.a(LoginScr.RMS_LOGIN_KEY, "");
                     Canvas.startOK(T.loginKeyUsed, new IAction() {
                        public void perform() {
                           LoginScr.this.showLoginKeyPopup();
                        }
                     });
                     return;
                  }

                  if ("MANY_DEVICES".equals(resp)) {
                     CRes.a(LoginScr.RMS_LOGIN_KEY, "");
                     Canvas.startOK(T.loginKeyManyDevicesClan, new IAction() {
                        public void perform() {
                           LoginScr.this.showLoginKeyPopup();
                        }
                     });
                     return;
                  }

                  if ("EXPIRED".equals(resp)) {
                     CRes.a(LoginScr.RMS_LOGIN_KEY, "");
                     Canvas.startOK(T.loginKeyExpired, new IAction() {
                        public void perform() {
                           LoginScr.this.showLoginKeyPopup();
                        }
                     });
                     return;
                  }

                  CRes.a(LoginScr.RMS_LOGIN_KEY, "");
                  Canvas.startOK(T.loginKeyNotFound, new IAction() {
                     public void perform() {
                        GameMidlet.exit();
                     }
                  });
               }
            })).start();
         }
      }, 0);

      Canvas.inputDlg.left = new Command(T.exit, new IAction() {
         public void perform() {
            GameMidlet.exit();
         }
      });
   }

   public static void tickOnlineKeyGuard() {
      if (pendingKeyKickMessage != null) {
         String msg = pendingKeyKickMessage;
         pendingKeyKickMessage = null;
         handleKeyExpiredInGame(msg);
         return;
      }

      if (!isLoginKeyVerified) {
         return;
      }

      if (currentLoginKey == null || currentLoginKey.length() == 0) {
         return;
      }

      long now = System.currentTimeMillis();
      if (isCheckingKeyOnline || now < nextKeyCheckAt) {
         return;
      }

      isCheckingKeyOnline = true;
      nextKeyCheckAt = now + 15000L;
      final String key = currentLoginKey;
      (new Thread(new Runnable() {
         public void run() {
            try {
               // Guard check không claim: nếu IP đã bị đổi ở máy khác sẽ nhận USED và văng
               String url = LoginScr.KEY_API_URL + "?key=" + LoginScr.encodeUrlParam(key);
               String resp = GameMidlet.createhttpconnect(url);
               if (resp == null) {
                  return;
               }

               resp = resp.trim();
               if ("OK".equals(resp)) {
                  return;
               }

               if ("EXPIRED".equals(resp)) {
                  LoginScr.pendingKeyKickMessage = T.loginKeyExpiredKick;
                  return;
               }

               if ("MANY_DEVICES".equals(resp)) {
                  LoginScr.pendingKeyKickMessage = T.loginKeyManyDevicesClan;
                  return;
               }

               if (resp.startsWith("MANY_DEVICES:")) {
                  LoginScr.pendingKeyKickMessage = T.loginKeyManyDevicesClan;
                  return;
               }

               if ("USED".equals(resp)) {
                  LoginScr.pendingKeyKickMessage = T.loginKeyUsedOtherIp;
                  return;
               }

               LoginScr.pendingKeyKickMessage = T.loginKeyInvalid;
            } finally {
               LoginScr.isCheckingKeyOnline = false;
            }
         }
      })).start();
   }

   private static void handleKeyExpiredInGame(final String msg) {
      isLoginKeyVerified = false;
      currentLoginKey = "";
      nextKeyCheckAt = 0L;
      CRes.a(RMS_LOGIN_KEY, "");
      suppressAutoKeyPopup = true;
      Session_ME.gI().close();
      LoginScr.gI().switchToMe();
      Canvas.startOK(msg, new IAction() {
         public void perform() {
            LoginScr.suppressAutoKeyPopup = false;
            LoginScr.gI().showLoginKeyPopup();
         }
      });
   }

   private static String encodeUrlParam(String s) {
      if (s == null) {
         return "";
      }

      StringBuffer out = new StringBuffer();
      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '-' || c == '_' || c == '.' || c == '~') {
            out.append(c);
         } else {
            int v = c;
            if (v < 0) {
               v += 256;
            }
            out.append('%');
            int hi = v >> 4 & 15;
            int lo = v & 15;
            out.append((char)(hi < 10 ? 48 + hi : 55 + hi));
            out.append((char)(lo < 10 ? 48 + lo : 55 + lo));
         }
      }

      return out.toString();
   }

   public final void initImg() {
      this.timeOut = System.currentTimeMillis();
      FilePack.b(T.aw);

      try {
         if (GameMidlet.PROVIDER == 6) {
            MyScreen.imgLogo = Image.createImage("/lgyeah.png");
         } else {
            MyScreen.imgLogo = Image.createImage(T.getPath() + "/l.png");
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      FilePack.reset();
      this.yL = -50;
      Canvas.loadMap.load(26);
      GameMidlet.avatar.x = GameMidlet.avatar.xCur = LoadMap.wMap * 24 / 2 + 30;
      AvCamera.gI().xCam = AvCamera.gI().xTo = 200;
      this.tfUser.setFocus(true);
      this.tfPass.setFocus(false);
      this.focus = 0;
      this.tfUser.setFocus(true);
   }

   public final void initCmd() {
      this.cmdMenu = new Command(T.menu, 0);
      this.D = new Command(T.register, 3);
      this.cmdLogin = new Command(T.selectt, 1);
      this.cmdRemem = new Command(T.remem, 2);
      this.g = new Command(T.selectt, 104);
      super.left = this.cmdMenu;
   }

   public LoginScr() {
      this.init();
      this.tfUser.setFocus(true);
      this.tfUser.setIputType(0);
      this.tfPass.setIputType(2);
      this.tfReg.setIputType(2);
      this.tfEmail.setIputType(0);
      this.tfEmail.q = T.loginEmailOptional;
      this.focus = 0;
      if (CRes.b(CRes.b) == null) {
         AvatarData.b();
      }

      try {
         String u = CRes.b(RMS_KEY_API_URL);
         if (u != null) {
            u = u.trim();
            if (u.length() > 0) {
               KEY_API_URL = u;
            }
         }
      } catch (Throwable t) {
      }
   }

   public final void init() {
      if (Canvas.h > 200) {
         this.defYL = Canvas.hh - 80;
      } else {
         this.defYL = Canvas.hh - 65;
      }

      this.yL = -50;
      this.wC = Canvas.w - 30;
      if (this.wC < 70) {
         this.wC = 70;
      }

      if (this.wC > 99) {
         this.wC = 99;
      }

      this.xC = (Canvas.w - this.wC >> 1) + 29;
      if (Canvas.w <= 128) {
         this.wC = 80;
         this.xC = (Canvas.w - this.wC >> 1) + 20;
      }

      this.xC -= (AvMain.hd - 1) * 40;
      Canvas.paint.initPosLogin(this);
      this.defYL = this.yLogin / 2;
      this.yL = this.defYL;
      AvCamera.gI().followPlayer = GameMidlet.avatar;
      AvCamera.gI().update();
   }

   public final void commandActionPointer(int var1, int var2) {
      switch (var1) {
         case 0:
            this.isReg = true;
            Canvas.paint.initPosLogin(this);
            return;
         case 1:
            this.isReg = false;
            Canvas.paint.initPosLogin(this);
            return;
         case 2:
            Canvas.startOKDlg(T.doYouWantExit2, 54);
            return;
         case 3:
            Canvas.startOK(T.uNeedExitGame, 55, (AvMain)null);
            return;
         case 4:
            Canvas.inputDlg.setInfoIkb(T.nameAcc, 100, 3);
            return;
         case 5:
            OptionScr.gI().switchToMe();
            return;
         case 6:
            GameMidlet.flatForm("http://wap.teamobi.com/faqs.php?provider=" + GameMidlet.PROVIDER);
            return;
         case 7:
            GameMidlet.flatForm("http://wap.teamobi.com?info=checkupdate&game=8&version=2.5.8&provider=" + GameMidlet.PROVIDER + "&agent=" + GameMidlet.g);
            return;
         case 8:
            if (!this.numSupport.equals("")) {
               GameMidlet.flatForm("tel:" + this.numSupport);
               return;
            }

            if (!Session_ME.gI().connected) {
               Canvas.startWaitDlg(T.connecting);
               Canvas.connect();
            } else {
               Canvas.startWaitDlg();
            }

            GlobalService.gI().requestService((byte)5, (String)null);
            return;
         case 9:
            Canvas.startOKDlg(T.alreadyDelRMS + T.delRMS);
            AvatarData.delRMS();
            return;
         case CMD_MENU_OPEN_SWITCH_CHAR:
            this.openSwitchAccountSettingsForm();
            return;
         case CMD_MENU_SELECT_CHAR:
            this.selectSavedChar(var2);
            return;
         case CMD_MENU_ADD_CHAR:
            this.addCurrentChar();
            return;
         case CMD_MENU_OPEN_LANG:
            this.openLanguageMenu();
            return;
         case CMD_MENU_LANG_VI:
            this.changeLanguage(0);
            return;
         case CMD_MENU_LANG_ID:
            this.changeLanguage(1);
            return;
         case 50:
         default:
      }
   }

   private void doRememberPass() {
      if (!this.isCheckBox) {
         this.isCheckBox = true;
         this.cmdRemem.caption = T.removee;
      } else {
         this.isCheckBox = false;
         this.cmdRemem.caption = T.remem;
      }

   }

   public final void commandTab(int var1, int var2) {
      switch (var1) {
         case 0:
            Vector var5 = new Vector();
            Command var4 = new Command(T.exit, 2);
            var5.addElement(new Command(T.loginAddAccountShort, CMD_MENU_ADD_CHAR));
            var5.addElement(new Command(T.loginSwitchAccountShort, CMD_MENU_OPEN_SWITCH_CHAR));
            var5.addElement(new Command(T.loginChangeLanguageMenu, CMD_MENU_OPEN_LANG));
            var5.addElement(new Command(T.delRMS, 9));
            var5.addElement(var4);
            Menu.gI().startAt(var5, 0);
            return;
         case 1:
            isNewGame = true;
            super.left = this.cmdMenu;
            super.center = this.g;
            this.indexNewGame = 0;
            this.refreshLoginTexts();
            return;
         case 2:
            this.doRememberPass();
            return;
         case 3:
            if (this.tfUser.getText().equals("")) {
               Canvas.startOKDlg(T.nameReg[0]);
            } else if (this.tfPass.getText().equals("")) {
               Canvas.startOKDlg(T.nameReg[1]);
            } else if (this.tfReg.getText().equals("")) {
               Canvas.startOKDlg(T.nameReg[2]);
            } else if (!this.tfPass.getText().equals(this.tfReg.getText())) {
               Canvas.startOKDlg(T.nameReg[3]);
            } else {
               Canvas.endDlg();
               this.timeOut = System.currentTimeMillis();
               if (this.tfEmail.getText().equals("")) {
                  doSendRegisterInfo();
                  return;
               }

               Canvas.startOKDlg(T.loginRegisterConfirm, 102);
            }
            break;
         case 50:
            Canvas.startOKDlg(T.youUseNumRegGetpass);
            return;
         case 51:
            this.regRequest();
            return;
         case 52:
            return;
         case 53:
            GameMidlet.flatForm("http://teamobi.com/dieukhoan.htm");
            return;
         case 54:
            GameMidlet.exit();
            return;
         case 55:
            isSelectedLanguage = false;
            this.saveLogin();
            AvatarData.delErrorRms("avatarSV");
            GameMidlet.exit();
            return;
         case 100:
            String var3;
            if ((var3 = Canvas.inputDlg.getText()).equals("")) {
               return;
            }

            (new IActionForgetPass(this, var3)).perform();
            return;
         case 101:
            this.regRequest();
            return;
         case 102:
            doSendRegisterInfo();
            return;
         case 103:
            return;
         case 104:
            this.clickNewGame();
      }

   }

   private void openChangeAccountForm() {
      (new IActionChangeAcc(this)).perform();
      this.focus = 0;
      this.tfUser.setFocus(true);
      this.tfPass.setFocus(false);
   }

   public final void openSwitchAccountSettingsForm() {
      this.loadSavedChars();
      final LoginScr var1 = this;
      final List var2 = new List(T.loginSwitchCharacter, List.IMPLICIT);
      if (this.savedCharUsers.length == 0) {
         var2.append(T.loginEmpty, (Image)null);
      } else {
         for(int var4 = 0; var4 < this.savedCharUsers.length; ++var4) {
            String var5 = this.savedCharUsers[var4];
            if (var5 != null && var5.trim().length() != 0) {
               var2.append(var5, (Image)null);
            }
         }
      }

      final javax.microedition.lcdui.Command var19 = new javax.microedition.lcdui.Command(T.loginLoginBtn, 4, 1);
      final javax.microedition.lcdui.Command var20 = new javax.microedition.lcdui.Command(T.menu, 4, 1);
      var2.addCommand(var19);
      var2.addCommand(var20);
      var2.setSelectCommand(var19);
      var2.setCommandListener(new CommandListener() {
         public void commandAction(javax.microedition.lcdui.Command var1x, Displayable var2x) {
            if (var1x == var19) {
               var1.loadSavedChars();
               int var3x = ((List)var2x).getSelectedIndex();
               if (var1.savedCharUsers.length == 0 || var3x < 0 || var3x >= var1.savedCharUsers.length) {
                  Canvas.startOKDlg(T.loginEmptyList);
                  Canvas.instance.setFullScreenMode(true);
                  Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
                  return;
               }

               String var4x = var1.savedCharUsers[var3x];
               String var5x = var1.savedCharPasses[var3x];
               var1.openChangeAccountForm();
               var1.tfUser.setText(var4x);
               var1.tfPass.setText(var5x == null ? "" : var5x);
               if (var1.isCheckBox) {
                  var1.saveLogin();
               }

               var1.login();
               Canvas.instance.setFullScreenMode(true);
               Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
               return;
            }

            if (var1x == var20) {
               final List var6 = new List(T.menu, List.IMPLICIT);
               var6.append(T.loginBack, (Image)null);
               var6.append(T.loginEdit, (Image)null);
               var6.append(T.loginDelete, (Image)null);
               var6.append(T.loginAdd, (Image)null);
               final javax.microedition.lcdui.Command var8 = new javax.microedition.lcdui.Command(T.selectt, 4, 1);
               final javax.microedition.lcdui.Command var9 = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
               var6.addCommand(var8);
               var6.addCommand(var9);
               var6.setSelectCommand(var8);
               var6.setCommandListener(new CommandListener() {
                  public void commandAction(javax.microedition.lcdui.Command var1y, Displayable var2y) {
                     if (var1y == var9) {
                        Display.getDisplay(GameMidlet.instance).setCurrent(var2);
                        return;
                     }

                     int var3y = ((List)var2y).getSelectedIndex();
                     if (var3y == 0) {
                        Canvas.instance.setFullScreenMode(true);
                        Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
                        return;
                     }

                     if (var3y == 1 || var3y == 3) {
                        final boolean var4y = var3y == 1;
                        var1.loadSavedChars();
                        String var5y = "";
                        String var6y = "";
                        int var7y = var2.getSelectedIndex();
                        if (var4y) {
                           if (var1.savedCharUsers.length == 0 || var7y < 0 || var7y >= var1.savedCharUsers.length) {
                              Canvas.startOKDlg(T.loginEmptyList);
                              Canvas.instance.setFullScreenMode(true);
                              Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
                              return;
                           }

                           var5y = var1.savedCharUsers[var7y];
                           var6y = var1.savedCharPasses[var7y];
                        }

                        final Form var8y = new Form(var4y ? T.loginEditAccount : T.loginAddAccount);
                        final TextField var9y = new TextField(T.loginUsername, var5y, 40, TextField.ANY);
                        final TextField var10 = new TextField(T.loginPassword, var6y == null ? "" : var6y, 40, TextField.ANY);
                        var8y.append(var9y);
                        var8y.append(var10);
                        final javax.microedition.lcdui.Command var11 = new javax.microedition.lcdui.Command(T.loginSave, 4, 1);
                        final javax.microedition.lcdui.Command var12 = new javax.microedition.lcdui.Command(T.cancel, 2, 1);
                        var8y.addCommand(var11);
                        var8y.addCommand(var12);
                        var8y.setCommandListener(new CommandListener() {
                           public void commandAction(javax.microedition.lcdui.Command var1z, Displayable var2z) {
                              if (var1z == var12) {
                                 Display.getDisplay(GameMidlet.instance).setCurrent(var2);
                                 return;
                              }

                              String var3z = var9y.getString() == null ? "" : var9y.getString().trim();
                              String var4z = var10.getString() == null ? "" : var10.getString();
                              if (var3z.length() == 0) {
                                 Canvas.startOKDlg(T.loginNoUsername);
                                 Canvas.instance.setFullScreenMode(true);
                                 Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
                                 return;
                              }

                              var1.addOrUpdateSavedChar(var3z, var4z);
                              Canvas.instance.setFullScreenMode(true);
                              var1.openSwitchAccountSettingsForm();
                           }
                        });
                        Display.getDisplay(GameMidlet.instance).setCurrent(var8y);
                        return;
                     }

                     if (var3y == 2) {
                        var1.loadSavedChars();
                        int var13 = var2.getSelectedIndex();
                        if (var1.savedCharUsers.length == 0 || var13 < 0 || var13 >= var1.savedCharUsers.length) {
                           Canvas.startOKDlg(T.loginEmptyList);
                           Canvas.instance.setFullScreenMode(true);
                           Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
                           return;
                        }

                        String[] var14 = new String[var1.savedCharUsers.length - 1];
                        String[] var15 = new String[var1.savedCharPasses.length - 1];
                        int var16 = 0;

                        for(int var17 = 0; var17 < var1.savedCharUsers.length; ++var17) {
                           if (var17 != var13) {
                              var14[var16] = var1.savedCharUsers[var17];
                              var15[var16] = var1.savedCharPasses[var17];
                              ++var16;
                           }
                        }

                        var1.savedCharUsers = var14;
                        var1.savedCharPasses = var15;
                        var1.saveSavedChars();
                        Canvas.startOKDlg(T.loginDeleteAccountSuccess);
                        Canvas.instance.setFullScreenMode(true);
                        Display.getDisplay(GameMidlet.instance).setCurrent(Canvas.instance);
                     }
                  }
               });
               Display.getDisplay(GameMidlet.instance).setCurrent(var6);
            }
         }
      });
      Display.getDisplay(GameMidlet.instance).setCurrent(var2);
   }

   private void openLanguageMenu() {
      Vector var1 = new Vector();
      var1.addElement(new Command(T.langVi, CMD_MENU_LANG_VI));
      var1.addElement(new Command(T.langId, CMD_MENU_LANG_ID));
      Menu.gI().startAt(var1, 0);
   }

   private void changeLanguage(int var1) {
      if (OptionScr.gI().mapFocus[4] == var1) {
         return;
      }

      Canvas.startWaitDlg();
      OptionScr.gI().mapFocus[4] = var1;
      OptionScr.gI().save(0);
      T.applyLanguage(var1);
      this.initImg();
      this.initCmd();
      this.refreshLoginTexts();
      this.switchToMe();
   }

   private void openSwitchCharMenu() {
      this.loadSavedChars();
      if (this.savedCharUsers.length == 0) {
         Canvas.startOKDlg(T.loginNoAccountHint);
         return;
      }

      Vector var1 = new Vector();

      for(int var2 = 0; var2 < this.savedCharUsers.length; ++var2) {
         String var3 = this.savedCharUsers[var2];
         if (var3 != null && var3.trim().length() != 0) {
            var1.addElement(new Command(var3, CMD_MENU_SELECT_CHAR, var2));
         }
      }

      if (var1.size() == 0) {
         Canvas.startOKDlg(T.loginNoAccountHint);
         return;
      }

      Menu.gI().startAt(var1, 0);
   }

   private void selectSavedChar(int var1) {
      this.loadSavedChars();
      if (var1 < 0 || var1 >= this.savedCharUsers.length) {
         return;
      }

      String var2 = this.savedCharUsers[var1];
      String var3 = this.savedCharPasses[var1];
      if (var2 == null || var2.trim().length() == 0) {
         return;
      }

      this.openChangeAccountForm();
      this.tfUser.setText(var2);
      this.tfPass.setText(var3 == null ? "" : var3);
      if (this.isCheckBox) {
         this.saveLogin();
      }

   }

   private void loadSavedChars() {
      String var1 = CRes.b(RMS_SAVED_CHARS);
      if (var1 == null || var1.length() == 0) {
         this.savedCharUsers = new String[0];
         this.savedCharPasses = new String[0];
         return;
      }

      Vector var2 = new Vector();
      Vector var3 = new Vector();
      int var4 = 0;

      while(var4 <= var1.length()) {
         int var5 = var1.indexOf(10, var4);
         if (var5 == -1) {
            var5 = var1.length();
         }

         String var6 = var1.substring(var4, var5).trim();
         var4 = var5 + 1;
         if (var6.length() != 0) {
            int var7 = var6.indexOf(9);
            if (var7 > 0 && var7 < var6.length() - 1) {
               String var8 = var6.substring(0, var7);
               String var9 = var6.substring(var7 + 1);
               if (var8.length() != 0) {
                  var2.addElement(var8);
                  var3.addElement(var9);
               }
            }
         }
      }

      this.savedCharUsers = new String[var2.size()];
      this.savedCharPasses = new String[var3.size()];

      for(int var10 = 0; var10 < var2.size(); ++var10) {
         this.savedCharUsers[var10] = (String)var2.elementAt(var10);
         this.savedCharPasses[var10] = (String)var3.elementAt(var10);
      }

   }

   private void saveSavedChars() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.savedCharUsers.length; ++var2) {
         String var3 = this.savedCharUsers[var2] == null ? "" : this.savedCharUsers[var2].trim();
         String var4 = this.savedCharPasses[var2] == null ? "" : this.savedCharPasses[var2];
         if (var3.length() != 0) {
            var1.append(var3).append('\t').append(var4).append('\n');
         }
      }

      CRes.a(RMS_SAVED_CHARS, var1.toString());
   }

   private void addCurrentChar() {
      String var1 = this.tfUser.getText() == null ? "" : this.tfUser.getText().trim();
      String var2 = this.tfPass.getText() == null ? "" : this.tfPass.getText();
      if (var1.length() == 0) {
         Canvas.startOKDlg(T.loginNoUsername);
         return;
      }

      if (var2.length() == 0) {
         Canvas.startOKDlg(T.loginNoPassword);
         return;
      }

      this.addOrUpdateSavedChar(var1, var2);
   }

   private void addOrUpdateSavedChar(String var1, String var2) {
      this.loadSavedChars();
      int var3 = -1;

      for(int var4 = 0; var4 < this.savedCharUsers.length; ++var4) {
         if (var1.equals(this.savedCharUsers[var4])) {
            var3 = var4;
            break;
         }
      }

      if (var3 >= 0) {
         this.savedCharPasses[var3] = var2;
      } else {
         String[] var7 = new String[this.savedCharUsers.length + 1];
         String[] var5 = new String[this.savedCharPasses.length + 1];

         for(int var6 = 0; var6 < this.savedCharUsers.length; ++var6) {
            var7[var6] = this.savedCharUsers[var6];
            var5[var6] = this.savedCharPasses[var6];
         }

         var7[this.savedCharUsers.length] = var1;
         var5[this.savedCharPasses.length] = var2;
         this.savedCharUsers = var7;
         this.savedCharPasses = var5;
      }

      this.saveSavedChars();
      Canvas.startOKDlg(T.loginAddAccountSuccess);
   }


   private void regRequest() {
      Canvas.startWaitDlg();
      Canvas.connect();
      GlobalService.gI().doRegisterByEmail(this.tfUser.getText().toLowerCase(), this.tfPass.getText().toLowerCase(), this.tfEmail.getText());
      this.isReg = false;
      super.center = this.cmdLogin;
      Canvas.paint.initPosLogin(this);
   }

   private static void doSendRegisterInfo() {
      Vector var0;
      (var0 = new Vector()).addElement(new Command(T.agree, 51));
      var0.addElement(new Command(T.connectFail, 52));
      var0.addElement(new Command(T.cannotMove, 53));
      Canvas.setInfoC(T.areYouAgreeRule, var0);
   }

   public final void update() {
      if (!isNewGame && this == Canvas.currentMyScreen && Canvas.menuMain == null && !isNewGame) {
         this.tfUser.update();
         this.tfPass.update();
         if (this.isReg) {
            this.tfReg.update();
            this.tfEmail.update();
         }

         if (this.tfUser.isFocused()) {
            super.right = this.tfUser.a();
         } else if (this.tfPass.isFocused()) {
            super.right = this.tfPass.a();
         } else if (this.tfReg.isFocused()) {
            super.right = this.tfReg.a();
         }
      } else {
         super.right = null;
      }

      if (this.defYL != this.yL) {
         this.yL += this.defYL - this.yL >> 1;
      }

      if (this.isReg) {
         super.center = this.D;
      } else if (this.focus == 2) {
         super.right = this.cmdRemem;
      }

      Canvas.loadMap.update();
   }

   public final void keyPress(int var1) {
      if (this.tfUser.isFocused()) {
         this.tfUser.keyPressed(var1);
      } else if (this.tfPass.isFocused()) {
         this.tfPass.keyPressed(var1);
      } else if (this.tfReg.isFocused()) {
         this.tfReg.keyPressed(var1);
      } else if (this.tfEmail.isFocused()) {
         this.tfEmail.keyPressed(var1);
      }

      super.keyPress(var1);
   }

   public final void paint(Graphics var1) {
      this.paintMain(var1);
      super.paint(var1);
      Canvas.paintPlus(var1);
   }

   public final void paintMain(Graphics var1) {
      Canvas.loadMap.paint(var1);
      Canvas.loadMap.paintBackGround(var1);
      Canvas.resetTrans(var1);
      int var4;
      if (isNewGame) {
         Graphics var3 = var1;
         LoginScr var2 = this;
         Canvas.paint.paintPopupBack(var1, this.xLogin, this.yLogin, this.wLogin, this.hLogin, 0);
         var1.translate(this.xLogin, this.yLogin + this.yNew);
         if (this.indexNewGame != -1) {
            Canvas.paint.drawSelectedArea(var1, 5 * AvMain.hd, this.indexNewGame * this.hCellNew, this.wLogin - 10 * AvMain.hd, this.hCellNew);
         }

         for(var4 = 0; var4 < var2.listStrNew.length; ++var4) {
            Canvas.normalFont.drawString(var3, var2.listStrNew[var4], var2.wLogin / 2, var4 * var2.hCellNew + var2.hCellNew / 2 - Canvas.normalFont.getHeight() / 2, 2);
         }
      } else if (Canvas.currentDialog == null && this == Canvas.currentMyScreen) {
         Canvas.paint.paintPopupBack(var1, this.xLogin, this.yLogin, this.wLogin, this.hLogin, 0);
         var1.setClip(this.xLogin + 4, this.yLogin + 4, this.wLogin - 8, this.hLogin - 8);
         if (!this.numSupport.equals("") && OptionScr.gI().mapFocus[4] == 0) {
            Canvas.paint.drawString(var1, "Hotline: " + this.numSupport, this.xLogin + this.wLogin - 8, this.yLogin + this.hLogin - AvMain.hNormal - 4, 1);
         }

         this.tfUser.paint(var1);
         var1.setClip(this.xLogin + 4, this.yLogin + 4, this.wLogin - 8, this.hLogin - 8);
         if ((var4 = Canvas.normalFont.getWidth(T.chieuTuong + ":")) < this.tfUser.x - this.xLogin) {
            var4 = (this.tfUser.x - this.xLogin - var4) / 2 + AvMain.hDuBox;
         } else {
            var4 = this.tfUser.x - var4 - 5;
         }

         Canvas.paint.drawString(var1, T.chieuTuong, this.xLogin + var4, this.tfUser.y + this.tfUser.height / 2 - AvMain.hNormal / 2, 0);
         Canvas.paint.drawString(var1, T.pass + ":", this.xLogin + var4, this.tfPass.y + this.tfUser.height / 2 - AvMain.hNormal / 2, 0);
         if (!this.isReg) {
            Canvas.paint.paintCheckBox(var1, this.xCheck, this.yCheck, this.focus, this.isCheckBox);
         } else {
            Canvas.paint.drawString(var1, T.enterAgain, this.xLogin + var4, this.tfReg.y + this.tfUser.height / 2 - AvMain.hNormal, 0);
            Canvas.paint.drawString(var1, T.pass + ":", this.xLogin + var4, this.tfReg.y + this.tfUser.height / 2, 0);
            Canvas.paint.drawString(var1, T.loginMobile, this.xLogin + var4, this.tfEmail.y + this.tfUser.height / 2 - AvMain.hNormal, 0);
            Canvas.paint.drawString(var1, T.loginOrEmail, this.xLogin + var4, this.tfEmail.y + this.tfUser.height / 2, 0);
            this.tfReg.paint(var1);
            this.tfEmail.paint(var1);
         }

         this.tfPass.paint(var1);
      }

      Canvas.resetTrans(var1);
      var1.drawImage(MyScreen.imgLogo, Canvas.hw, this.yL, 3);
   }

   public final void updateKey() {
      if (isNewGame) {
         LoginScr loginScr = this;
         if (Canvas.a(2)) {
            --this.indexNewGame;
            if (this.indexNewGame < 0) {
               this.indexNewGame = (byte)(this.listStrNew.length - 1);
            }
         } else if (Canvas.a(8)) {
            ++this.indexNewGame;
            if (this.indexNewGame >= this.listStrNew.length) {
               this.indexNewGame = 0;
            }
         }

         if (Canvas.isPointerClick) {
            for(int n = 0; n < loginScr.listStrNew.length; ++n) {
               if (Canvas.b(loginScr.xLogin, loginScr.yLogin + loginScr.yNew + n * loginScr.hCellNew, loginScr.wLogin, loginScr.hCellNew)) {
                  loginScr.indexNewGame = (byte)n;
                  Canvas.isPointerClick = false;
                  loginScr.K = true;
                  break;
               }
            }
         }

         if (loginScr.K) {
            if (Canvas.isPointerDown && !Canvas.b(loginScr.xLogin, loginScr.yLogin + loginScr.yNew + loginScr.indexNewGame * loginScr.hCellNew, loginScr.wLogin, loginScr.hCellNew)) {
               loginScr.indexNewGame = -1;
            }

            if (Canvas.isPointerRelease) {
               Canvas.isPointerRelease = false;
               loginScr.K = false;
               if (loginScr.indexNewGame != -1) {
                  loginScr.clickNewGame();
               }
            }
         }

         super.updateKey();
      } else {
         if (Canvas.isPointerRelease && Canvas.isPointer(0, 0, Canvas.w, Canvas.h) && Canvas.isPointer(this.xCheck - 10, this.yCheck, 70, MyScreen.ITEM_HEIGHT * AvMain.hd + 10)) {
            this.doRememberPass();
         }

         if (Canvas.keyPressed[2]) {
            this.focus = this.focus > 0 ? --this.focus : (this.isReg ? 3 : 2);
         }

         if (Canvas.keyPressed[8]) {
            this.focus = this.focus < (this.isReg ? 3 : 2) ? ++this.focus : 0;
         }

         if (Canvas.keyPressed[2] || Canvas.keyPressed[8]) {
            Canvas.clearKeyPressed();
            if (this.focus == 0) {
               this.tfUser.setFocus(true);
               this.tfPass.setFocus(false);
               this.tfReg.setFocus(false);
               this.tfEmail.setFocus(false);
            } else if (this.focus == 1) {
               this.tfUser.setFocus(false);
               this.tfPass.setFocus(true);
               this.tfReg.setFocus(false);
               this.tfEmail.setFocus(false);
            } else if (this.focus == 2) {
               this.tfUser.setFocus(false);
               this.tfPass.setFocus(false);
               this.right = null;
               if (this.isReg) {
                  this.tfReg.setFocus(true);
                  this.tfEmail.setFocus(false);
               }
            } else {
               this.tfUser.setFocus(false);
               this.tfPass.setFocus(false);
               this.tfReg.setFocus(false);
               this.tfEmail.setFocus(true);
            }
         }

         super.updateKey();
      }
   }

   private void clickNewGame() {
      System.out.println("clickNewGame: " + isAccVir + "    " + this.indexNewGame);
      switch (this.indexNewGame) {
         case 0:
            if (this.listStrNew.length == 2) {
               (new IAcionNewGameOk(this)).perform();
               return;
            }

            if (isAccVir) {
               ServerListScr.gI().switchToMe();
               return;
            }

            String var2 = this.tfUser.getText().toLowerCase().trim();
            String var3 = this.tfPass.getText();
            if (!var2.equals("")) {
               if (var3.equals("")) {
                  this.focus = 1;
                  this.tfUser.setFocus(false);
                  this.tfPass.setFocus(true);
                  break;
               }

               ServerListScr.gI().switchToMe();
            }

            return;
         case 1:
            if (this.listStrNew.length != 2) {
               IAcionNewGameOk var1 = new IAcionNewGameOk(this);
               if (!this.nameVir.equals("") && this.tfUser.getText().equals("")) {
                  Canvas.startOKDlg(T.loginNewGameWarn, var1);
                  return;
               }

               var1.perform();
               return;
            }
         case 2:
            this.changeAcc();
      }

   }

   private void changeAcc() {
      this.openChangeAccountForm();
      this.tfUser.setText("");
      this.tfPass.setText("");
   }

   public final void saveLogin() {
      System.out.println("saveLogin");
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var2 = new DataOutputStream(var1);

      try {
         var2.writeUTF(GameMidlet.APP_VERSION);
         var2.writeByte(super.selected_);
         var2.writeUTF(this.numSupport);
         var2.writeUTF(this.nameVir);
         var2.writeUTF(this.passVir);
         if (this.isCheckBox) {
            var2.writeUTF(gI().tfUser.getText());
            var2.writeUTF(gI().tfPass.getText());
         }

         var2.writeInt(s);
         var2.writeBoolean(isSelectedLanguage);
         var2.writeBoolean(isAccVir);
         CRes.saveRMS("avlogin", var1.toByteArray());
         var2.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public final void loadLogin() {
      DataInputStream var1;
      if ((var1 = AvatarData.loadRMS("avlogin")) != null) {
         String var2 = "";

         try {
            var2 = var1.readUTF();
            super.selected_ = var1.readByte();
            this.numSupport = var1.readUTF();
            this.nameVir = var1.readUTF();
            this.passVir = var1.readUTF();
            if (this.isCheckBox) {
               this.tfUser.setText(var1.readUTF());
               this.tfPass.setText(var1.readUTF());
            }

            s = var1.readInt();
            isSelectedLanguage = var1.readBoolean();
            isAccVir = var1.readBoolean();
            var1.close();
         } catch (Exception var4) {
            AvatarData.delErrorRms("avlogin");
         }

         if (!isSelectedLanguage) {
            AvatarData.delErrorRms("avatarSV");
         }

         if (!GameMidlet.APP_VERSION.equals(var2)) {
            AvatarData.delRMS();
         }
      }

   }

   public final void onNumSupport(String var1) {
      this.numSupport = var1;
   }

   public final void login() {
      if (!isLoginKeyVerified) {
         Canvas.startOK(T.loginNeedKeyBeforeLogin, new IAction() {
            public void perform() {
               LoginScr.this.showLoginKeyPopup();
            }
         });
         return;
      }

      Canvas.connect();
      GlobalService.gI().doRequestNumSupport(gI().numSupport.hashCode());
      System.out.println("login: " + isNewGame + "    " + this.indexNewGame);
      if (!isNewGame || (this.indexNewGame != 0 || this.listStrNew.length != 2) && (this.indexNewGame != 1 || this.listStrNew.length != 3)) {
         if (this.tfUser.getText().equals("")) {
            GlobalService.gI().login(this.nameVir, this.passVir, GameMidlet.APP_VERSION);
            isAccVir = true;
         } else {
            isAccVir = false;
            this.nameVir = "";
            this.passVir = "";
            GlobalService.gI().login(this.tfUser.getText().toLowerCase(), this.tfPass.getText(), GameMidlet.APP_VERSION);
         }
      } else {
         GlobalService var1 = GlobalService.gI();
         System.out.println("doLoginNewGame");
         var1.createMessage((byte)-12);
         var1.sendMessage();
      }

   }

   public final void onLoginNewGame(String var1, String var2) {
      System.out.println("onLoginNewGame: " + var1 + "   " + var2);
      this.nameVir = var1;
      this.passVir = var2;
      this.tfUser.setText("");
      this.tfPass.setText("");
      isAccVir = true;
      isNewGame = false;
      this.login();
   }
}
