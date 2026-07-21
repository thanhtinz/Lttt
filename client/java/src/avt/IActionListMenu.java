package avt;

import java.util.Vector;

final class IActionListMenu implements IAction {
   final ListScr tex;
   private final String idType;
   private final String[] idMe;
   private final int idList;
   private final byte page;
   private final byte[] isFr;

   IActionListMenu(ListScr var1, String var2, String[] var3, int var4, byte var5, byte[] var6) {
      this.tex = var1;
      this.idType = var2;
      this.idMe = var3;
      this.idList = var4;
      this.page = var5;
      this.isFr = var6;
   }

   public final void perform() {
      Vector var1 = new Vector();
      if (!ListScr.getisAction(this.tex) && this.idType.equals(ListScr.idFriendList)) {
         var1.addElement(new Command(T.updateList, 50));
      }

      for(int var2 = 0; var2 < this.idMe.length; ++var2) {
         var1.addElement(new Command(this.idMe[var2], new IActionListMenu2(this, this.idList, this.page, this.isFr, var2)));
      }

      Menu.gI().startAt(var1, 0);
   }
}
