package avt;

import main.Canvas;

final class IActionOkReg implements IAction {
   private final TField[] a;

   IActionOkReg(IActionYesRef var1, TField[] var2) {
      this.a = var2;
   }

   public final void perform() {
      if (this.a[0].getText().equals("")) {
         Canvas.startOKDlg("Bạn chưa nhập tên");
      } else if (!this.a[1].getText().equals("") && !this.a[2].getText().equals("")) {
         if (!this.a[1].getText().equals(this.a[2].getText())) {
            Canvas.startOKDlg("Hai mật khẩu không giống nhau");
         } else {
            Canvas.currentFace = null;
            GlobalService.gI().doRegisterByEmail(this.a[0].getText().toLowerCase(), this.a[1].getText().toLowerCase(), this.a[3].getText());
         }
      } else {
         Canvas.startOKDlg("Bạn chưa nhập mật khẩu");
      }

   }
}
