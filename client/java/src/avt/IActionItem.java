package avt;

import main.GameMidlet;

final class IActionItem implements IAction {
   final HouseScr me;
   private final int ii;
   private final String na;

   IActionItem(HouseScr var1, int var2, String var3) {
      this.me = var1;
      this.ii = var2;
      this.na = var3;
   }

   public final void perform() {
      HouseScr.setStatusBuyItem(this.me);
      if (HouseScr.getxTemp(this.me) != -1) {
         HouseScr.setX(this.me, HouseScr.getxTemp(this.me));
         HouseScr.setyTemp(this.me, HouseScr.getYtemp(this.me));
         GameMidlet.avatar.x = HouseScr.getxTemp(this.me) * 24;
         GameMidlet.avatar.y = HouseScr.getYtemp(this.me) * 24;
         AvCamera.gI().setToPos(GameMidlet.avatar.x * AvMain.hd, GameMidlet.avatar.y * AvMain.hd);
      }

      HouseScr.isSelectObj = true;
      HouseScr.isChange = true;
      HouseScr.setSelectedIndex(this.me, this.ii);
      this.me.center = new Command(T.sett, new IActionItem1(this, this.ii, this.na));
      this.me.left = null;
      this.me.right = null;
   }
}
