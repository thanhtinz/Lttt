package avt;

import main.Canvas;

final class IActionYesRef implements IAction {
   IActionYesRef(MiniMap var1) {
   }

   public final void perform() {
      TField[] var1 = new TField[4];

      for(int var2 = 0; var2 < 4; ++var2) {
         var1[var2] = new TField();
      }

      var1[0].setIputType(0);
      var1[1].setIputType(2);
      var1[2].setIputType(2);
      var1[3].setIputType(0);
      String[][] var3 = new String[][]{{"Tên:", ""}, {"Mật khẩu:", ""}, {"Nhập lại", "mật khẩu:"}, {"Số di động", "hoặc email:"}};
      InputFace.gI().setIputType(var1, "Đăng Ký", var3, new Command(T.finish, new IActionOkReg(this, var1)));
      Canvas.currentFace = InputFace.gI();
   }
}
