/**
 * Port của avatar/play/offline/ObjPremium.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
import { Item } from '../../item/Item.js';
import { Npc } from '../../model/Npc.js';
import { AbsMapOffline } from './AbsMapOffline.js';

export class ObjPremium extends AbsMapOffline {

  constructor(id) {
    super(id);
  }

  init() {
    const thuongNhan = Npc.builder().id(1221)
      .name('thuong nhan')
      .x(180)
      .y(168)
      .wearing([]).build();
    thuongNhan.addItemToWearing(new Item(3079));
    thuongNhan.addItemToWearing(new Item(3078));
    thuongNhan.addItemToWearing(new Item(0));
    thuongNhan.addItemToWearing(new Item(4));
    thuongNhan.addItemToWearing(new Item(3077));
    //thuongNhan.addChat("Chào mừng các bạn đến với shop premium");
    //thuongNhan.addChat("Mời các bạn xem hàng");
    this.addNpc(thuongNhan);
    const shopDong = Npc.builder().id(1212)
      .name('shop dong')
      .x(276)
      .y(168)
      .wearing([]).build();
    shopDong.addItemToWearing(new Item(3079));
    shopDong.addItemToWearing(new Item(3078));
    shopDong.addItemToWearing(new Item(0));
    shopDong.addItemToWearing(new Item(4));
    shopDong.addItemToWearing(new Item(3077));
    //shopDong.addChat("Shop xèng đê");
    //shopDong.addChat("Xèng đê, xèng đê");
    this.addNpc(shopDong);
  }
}

export default ObjPremium;
