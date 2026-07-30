package avt;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public final class TField {
   public int x;
   public int y;
   public int width;
   public int height;
   private boolean isFocus;
   private boolean w = false;
   public boolean e = true;
   private static int typeXpeed = 1;
   private static int[] MAX_TIME_TO_CONFIRM_KEY = new int[]{18, 14, 11, 9, 6, 4, 2};
   public static int f = 0;
   private static String[] print = new String[]{" 0", ".,@?!_1\"/$-():*+<=>;%&~#%^&*{}[];'/1", "abc2âă", "def3đê", "ghi4", "jkl5", "mno6ôơ", "pqrs7", "tuv8ư", "wxyz9", "*", "#"};
   private static String[] printA = new String[]{"0", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", "0", "0"};
   private static String[] printBB = new String[]{" 0", "er1", "ty2", "ui3", "df4", "gh5", "jk6", "cv7", "bn8", "m9", "0", "0", "qw!", "as?", "zx", "op.", "l,"};
   private String text = "";
   private String passwordText = "";
   public String paintedText = "";
   public int caretPos = 0;
   public int counter = 0;
   private int maxTextLenght = 500;
   public int offsetX = 0;
   private static int lastKey = -1984;
   public int keyInActiveState = 0;
   private int indexOfActiveChar = 0;
   public int showCaretCounter = 10;
   private int inputType = 0;
   public static boolean m;
   public static int mode = 0;
   public static int timeChangeMode;
   public static final String[] p = new String[]{"abc", "Abc", "ABC", "123"};
   private static int I = 11;
   private static int changeDau;
   private Command K;
   public String q = "";
   public static FrameImage tfframe;
   private static Canvas canvas;
   public static int s;
   public static IAction t;
   public static boolean u = false;
   private int indexDau = -1;
   private int indexTemplate = 0;
   private int indexCong = 0;
   private long timeDau = 0L;
   private static String printDau = "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêếềểễệiíìỉĩịoóòỏõọôốồổỗộơớờởỡợuúùủũụưứừửữựyýỳỷỹỵ";
   private boolean isTransTF = false;
   private static int[][] S = new int[][]{{32, 48}, {49, 69}, {50, 84}, {51, 85}, {52, 68}, {53, 71}, {54, 74}, {55, 67}, {56, 66}, {57, 77}, {42, 128}, {35, 137}, {33, 113}, {63, 97}, {64, 121, 122}, {46, 111}, {44, 108}};

   public static void initKey(int var0) {
      if (var0 == 1) {
         print[0] = "0";
         print[10] = " *";
         print[11] = "#";
         I = 35;
         changeDau = 42;
      } else if (var0 == 0) {
         print[0] = " 0";
         print[10] = "*";
         print[11] = "#";
         I = 35;
         changeDau = 42;
      } else if (var0 == 2) {
         print[0] = "0";
         print[10] = "*";
         print[11] = " #";
         I = 42;
         changeDau = 35;
      }

   }

   public final void setFocus(boolean var1) {
      if (this.isFocus != var1) {
         mode = 0;
      }

      lastKey = -1984;
      timeChangeMode = main.Canvas.getSecond();
      this.isFocus = var1;
   }

   public final Command getRightCmd() {
      t = this.K.action;
      return main.Canvas.stypeInt == 0 ? this.K : null;
   }

   public static void setNoGameAction(boolean var0) {
      m = var0;
      main.Canvas.M.getWidth("ABC");
   }

   public TField() {
      this.text = "";
      f = AvMain.hBlack + 1;
      this.K = new Command(T.del, new class_jw(this));
      if (canvas == null) {
         canvas = main.Canvas.instance;
      }

      this.setFocus(false);
      this.height = tfframe.frameHeight;
   }

   public final void clear() {
      if (this.caretPos > 0 && this.text.length() > 0) {
         this.text = this.text.substring(0, this.caretPos - 1) + this.text.substring(this.caretPos, this.text.length());
         --this.caretPos;
         this.setOffset(0);
         this.setPasswordTest();
      }

   }

   private void setOffset(int var1) {
      if (this.inputType == 2) {
         this.paintedText = this.passwordText;
      } else {
         this.paintedText = this.text;
      }

      int var2 = main.Canvas.M.getWidth(this.paintedText.substring(0, this.caretPos));
      if (var1 == -1) {
         if (var2 + this.offsetX < 15 && this.caretPos > 0 && this.caretPos < this.paintedText.length()) {
            this.offsetX += main.Canvas.M.getWidth(this.paintedText.substring(this.caretPos, this.caretPos + 1));
         }
      } else if (var1 == 1) {
         if (var2 + this.offsetX > this.width - 25 && this.caretPos < this.paintedText.length() && this.caretPos > 0) {
            this.offsetX -= main.Canvas.M.getWidth(this.paintedText.substring(this.caretPos - 1, this.caretPos));
         }
      } else {
         this.offsetX = -(var2 - (this.width - 12));
      }

      if (this.offsetX > 0) {
         this.offsetX = 0;
      } else if (this.offsetX < 0) {
         var1 = main.Canvas.M.getWidth(this.paintedText) - (this.width - 12);
         if (this.offsetX < -var1) {
            this.offsetX = -var1;
         }
      }

   }

   private void keyPressedAscii(int var1) {
      if ((this.inputType != 2 && this.inputType != 3 || var1 >= 48 && var1 <= 57 || var1 >= 65 && var1 <= 90 || var1 >= 97 && var1 <= 122) && this.text.length() < this.maxTextLenght) {
         String var2 = this.text.substring(0, this.caretPos) + (char)var1;
         if (this.caretPos < this.text.length()) {
            var2 = var2 + this.text.substring(this.caretPos, this.text.length());
         }

         this.text = var2;
         ++this.caretPos;
         this.setPasswordTest();
         this.setOffset(0);
      }

   }

   public static void keyPressedAscii() {
      if (++mode > 3) {
         mode = 0;
      }

      lastKey = I;
      timeChangeMode = main.Canvas.getSecond();
   }

   private void keyPressedAny(int var1) {
      if (this.inputType != 0 && this.inputType != 2 && this.inputType != 3) {
         if (this.inputType == 1) {
            this.keyPressedAscii(var1);
            this.keyInActiveState = 1;
         }
      } else {
         int var2 = var1;
         String[] var3;
         if (main.Canvas.E) {
            var3 = printBB;
         } else if (this.inputType != 2 && this.inputType != 3) {
            var3 = print;
         } else {
            var3 = printA;
         }

         if (main.Canvas.E) {
            var2 = var1;
            int var4 = 0;

            int var10000;
            label86:
            while(true) {
               if (var4 >= S.length) {
                  var10000 = -1;
                  break;
               }

               for(int var5 = 0; var5 < S[var4].length; ++var5) {
                  if (S[var4][var5] == var2) {
                     var10000 = var4 + 48;
                     break label86;
                  }
               }

               ++var4;
            }

            var2 = var10000;
            if (var10000 == -1) {
               return;
            }
         }

         String var6;
         char var7;
         if (var2 == lastKey) {
            this.indexOfActiveChar = (this.indexOfActiveChar + 1) % var3[var2 - 48].length();
            var7 = var3[var2 - 48].charAt(this.indexOfActiveChar);
            if (mode == 0) {
               var7 = Character.toLowerCase(var7);
            } else if (mode == 1) {
               var7 = Character.toUpperCase(var7);
            } else if (mode == 2) {
               var7 = Character.toUpperCase(var7);
            } else {
               var7 = var3[var2 - 48].charAt(var3[var2 - 48].length() - 1);
            }

            var6 = this.text.substring(0, this.caretPos - 1) + var7;
            if (this.caretPos < this.text.length()) {
               var6 = var6 + this.text.substring(this.caretPos, this.text.length());
            }

            this.text = var6;
            this.keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
            this.setPasswordTest();
         } else if (this.text.length() < this.maxTextLenght) {
            if (mode == 1 && lastKey != -1984) {
               mode = 0;
            }

            this.indexOfActiveChar = 0;
            var7 = var3[var2 - 48].charAt(this.indexOfActiveChar);
            if (mode == 0) {
               var7 = Character.toLowerCase(var7);
            } else if (mode == 1) {
               var7 = Character.toUpperCase(var7);
            } else if (mode == 2) {
               var7 = Character.toUpperCase(var7);
            } else {
               var7 = var3[var2 - 48].charAt(var3[var2 - 48].length() - 1);
            }

            var6 = this.text.substring(0, this.caretPos) + var7;
            if (this.caretPos < this.text.length()) {
               var6 = var6 + this.text.substring(this.caretPos, this.text.length());
            }

            this.text = var6;
            this.keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
            ++this.caretPos;
            this.setPasswordTest();
            this.setOffset(0);
         }

         lastKey = var2;
      }

   }

   public final boolean keyPressed(int var1) {
      if (main.Canvas.E) {
         if (var1 == 8 || var1 == 127) {
            this.clear();
         }
      } else if (var1 == 8 || var1 == -8 || var1 == 204) {
         this.clear();
         return true;
      }

      if (!main.Canvas.E) {
         if (ClientUtilities.vietnameseTyping) {
            if (var1 >= 32 && (var1 < 48 || var1 > 57)) {
               m = true;
            }
         } else if (var1 >= 65 && var1 <= 122) {
            m = true;
         }
      }

      if (m && !main.Canvas.E) {
         if (var1 == 45) {
            if (var1 == lastKey && this.keyInActiveState < MAX_TIME_TO_CONFIRM_KEY[typeXpeed]) {
               this.text = this.text.substring(0, this.caretPos - 1) + '_';
               this.paintedText = this.text;
               this.setPasswordTest();
               this.setOffset(0);
               lastKey = -1984;
               return false;
            }

            lastKey = 45;
         }

         if (var1 >= 32) {
            if (var1 >= 48 && var1 <= 57) {
               // fall through -> keyPressedAny (T9)
            } else {
               this.keyPressedAscii(var1);
               return false;
            }
         }
      }

      if (!m && var1 == I) {
         keyPressedAscii();
         this.keyInActiveState = 1;
         lastKey = var1;
         return false;
      } else if (var1 == changeDau && this.inputType == 0) {
         this.setDau();
         return false;
      } else {
         if (var1 == 42) {
            var1 = 58;
         }

         if (var1 == 35) {
            var1 = 59;
         }

         if (main.Canvas.E && var1 >= 48) {
            if (m) {
               this.keyPressedAscii(var1);
               this.keyInActiveState = 1;
            } else {
               this.keyPressedAny(var1);
            }
         } else if (var1 >= 48 && var1 <= 59) {
            this.keyPressedAny(var1);
         } else {
            this.indexOfActiveChar = 0;
            lastKey = -1984;
            if (var1 == 14) {
               if (this.caretPos > 0) {
                  --this.caretPos;
                  this.setOffset(0);
                  this.showCaretCounter = 10;
                  return false;
               }
            } else if (var1 == 15) {
               if (this.caretPos < this.text.length()) {
                  ++this.caretPos;
                  this.setOffset(0);
                  this.showCaretCounter = 10;
                  return false;
               }
            } else {
               if (var1 == 19) {
                  this.clear();
                  return false;
               }

               lastKey = var1;
            }
         }

         return true;
      }
   }

   private void setDau() {
      this.timeDau = System.currentTimeMillis() / 100L;
      if (this.indexDau != -1) {
         ++this.indexCong;
         if (this.indexCong >= 6) {
            this.indexCong = 0;
         }

         String var5 = this.text.substring(0, this.indexDau);
         String var6 = this.text.substring(this.indexDau + 1);
         String var7 = printDau.substring(this.indexTemplate + this.indexCong, this.indexTemplate + this.indexCong + 1);
         this.text = var5 + var7 + var6;
      } else {
         for(int var1 = this.caretPos; var1 > 0; --var1) {
            char var2 = this.text.charAt(var1 - 1);

            for(int var3 = 0; var3 < printDau.length(); ++var3) {
               char var4 = printDau.charAt(var3);
               if (var2 == var4) {
                  this.indexTemplate = var3;
                  this.indexCong = 0;
                  this.indexDau = var1 - 1;
                  return;
               }
            }
         }

         this.indexDau = -1;
      }

   }

   public final void paint(Graphics var1) {
      boolean var2 = this.isFocus;
      if (this.inputType == 2) {
         this.paintedText = this.passwordText;
      } else {
         this.paintedText = this.text;
      }

      var1.setClip(0, 0, main.Canvas.w + 20, main.Canvas.h);
      var1.setColor(7829367);
      main.Canvas.paint.paintTextBox(var1, this.x, this.y, this.width, this.height, this, var2);
   }

   public final boolean isFocused() {
      return this.isFocus;
   }

   private void setPasswordTest() {
      if (this.inputType == 2) {
         this.passwordText = "";

         for(int var1 = 0; var1 < this.text.length(); ++var1) {
            this.passwordText = this.passwordText + "*";
         }

         if (this.keyInActiveState > 0 && this.caretPos > 0) {
            this.passwordText = this.passwordText.substring(0, this.caretPos - 1) + this.text.charAt(this.caretPos - 1) + this.passwordText.substring(this.caretPos, this.passwordText.length());
         }
      }

   }

   public final void update() {
      ++this.counter;
      if (this.keyInActiveState > 0) {
         --this.keyInActiveState;
         if (this.keyInActiveState == 0 || mode > 2) {
            this.indexOfActiveChar = 0;
            if (this.isFocus && mode == 1 && lastKey != I) {
               mode = 0;
            }

            lastKey = -1984;
            this.setPasswordTest();
         }
      }

      if (this.showCaretCounter > 0) {
         --this.showCaretCounter;
      }

      if (main.Canvas.isPointerClick && main.Canvas.menuMain == null && main.Canvas.isPointerClick && main.Canvas.isPointer(0, 0, main.Canvas.w, main.Canvas.h - main.Canvas.hTab / 2)) {
         if (main.Canvas.isPointer(this.x, this.y - 6, this.width, this.height + 12)) {
            if (!this.isFocus) {
               this.isFocus = true;
            } else {
               if (!OptionScr.isVirTualKey) {
                  this.isTransTF = true;
                  OptionScr.isVirTualKey = true;
                  main.Canvas.instance.setSize();
               }

               main.Canvas.z.isPointerClick = true;
            }
         } else {
            if (this.isTransTF) {
               OptionScr.isVirTualKey = false;
               main.Canvas.instance.setSize();
               this.isTransTF = false;
            }

            if (this.e) {
               this.isFocus = false;
            }
         }
      }

      if (this.indexDau != -1 && System.currentTimeMillis() / 100L - this.timeDau > 5L) {
         this.indexDau = -1;
      }

      if (this.isFocus && main.Canvas.currentDialog == null) {
         if (main.Canvas.keyPressed[4]) {
            if (this.inputType != 2) {
               --this.caretPos;
               if (this.caretPos < 0) {
                  this.caretPos = 0;
               }

               this.setOffset(-1);
            }

            main.Canvas.keyPressed[4] = false;
            return;
         }

         if (main.Canvas.keyPressed[6]) {
            if (this.inputType != 2) {
               ++this.caretPos;
               if (this.caretPos > this.text.length()) {
                  this.caretPos = this.text.length();
               }

               this.setOffset(1);
            }

            main.Canvas.keyPressed[6] = false;
         }
      }

   }

   public final String getText() {
      return this.text;
   }

   public final void setText(String var1) {
      if (var1 != null) {
         lastKey = -1984;
         this.keyInActiveState = 0;
         this.indexOfActiveChar = 0;
         this.text = var1;
         this.paintedText = var1;
         this.setPasswordTest();
         this.caretPos = var1.length();
         this.setOffset(0);
      }

   }

   public final void setMaxTextLenght(int var1) {
      this.maxTextLenght = 40;
   }

   public final void setIputType(int var1) {
      this.inputType = var1;
   }

   static boolean getFocus(TField var0) {
      return var0.isFocus;
   }
}
