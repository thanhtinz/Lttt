package avt;

final class class_ga implements IAction {
   private HouseScr a;
   private final MapItem b;

   class_ga(HouseScr var1, MapItem var2) {
      this.a = var1;
      this.b = var2;
   }

   public final void perform() {
      if (!HouseScr.a(this.a, AvatarData.getMapItemTypeByID(this.b.typeID))) {
         AvatarService.gI().doSortItem(HouseScr.getposSort(this.a).anchor, HouseScr.getposSort(this.a).x, HouseScr.getposSort(this.a).y, HouseScr.getX(this.a), HouseScr.getY(this.a), this.b.dir);
         HouseScr.isSelectObj = false;
         this.a.isSelectedItem = -1;
         HouseScr.setSelectedIndex(this.a, -1);
         HouseScr.isChange = false;
         this.a.setType(this.b);
         if (HouseScr.isSetTuong(this.a, this.b)) {
            ++this.b.y;
         }

         LoadMap.orderVector(LoadMap.treeLists);
         HouseScr.doOption(this.a);
      }

   }
}
