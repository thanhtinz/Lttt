/**
 * Port của avatar/play/offline/ObjPet.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
import { Item } from '../../item/Item.js';
import { Npc } from '../../model/Npc.js';
import { AbsMapOffline } from './AbsMapOffline.js';

export class ObjPet extends AbsMapOffline {

  constructor(id) {
    super(id);
  }

  init() {
    const pet = Npc.builder().id(1213)
      .name('thu nuoi')
      .x(175)
      .y(168)
      .wearing([]).build();
    pet.addItemToWearing(new Item(3079));
    pet.addItemToWearing(new Item(3078));
    pet.addItemToWearing(new Item(0));
    pet.addItemToWearing(new Item(4));
    pet.addItemToWearing(new Item(3077));
    //pet.addChat("Chào mừng bạn đến với shop thú cưng");
    //pet.addChat("Các con thú thật dễ tương phải không nào");
    //pet.addChat("Mời bạn lại xem");
    this.addNpc(pet);
  }
}

export default ObjPet;
