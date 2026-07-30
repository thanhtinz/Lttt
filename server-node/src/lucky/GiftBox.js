// Port của avatar/lucky/GiftBox.java
import { Item } from '../item/Item.js';
import { RandomCollection } from '../lib/RandomCollection.js';
import { Utils } from '../server/Utils.js';

// Định dạng số kiểu Java String.format("%,d", n)
function formatNumber(n) {
  return Math.trunc(n).toLocaleString('en-US');
}

export class GiftBox {
  static Items = 0;
  static XU = 1;
  static XP = 2;
  static LUONG = 3;

  static SieuNhan = [];
  static setHaiTac = [];
  static setVuTru = [];

  // NOTE: giữ nguyên hành vi bản Java (static initializer block). Java chỉ chạy block này
  // khi class được load lần đầu, nên ở đây khởi tạo lười để Item/Part đã sẵn sàng.
  static _staticInited = false;

  static _staticInit() {
    if (GiftBox._staticInited) return;
    GiftBox._staticInited = true;

    const setHaiTac = GiftBox.setHaiTac;
    const SieuNhan = GiftBox.SieuNhan;
    const setVuTru = GiftBox.setVuTru;

    const luffySet = [];
    for (let i = 5409; i <= 5413; i++) {
      luffySet.push(new Item(i));
    }
    setHaiTac.push(luffySet);//nam

    const namiSet = [];
    for (let i = 5414; i <= 5418; i++) {
      namiSet.push(new Item(i));
    }
    setHaiTac.push(namiSet);

    const mihawkSet = [];
    for (let i = 5419; i <= 5423; i++) {
      mihawkSet.push(new Item(i));
    }
    setHaiTac.push(mihawkSet);//nam

    const NicoRobin = [];
    for (let i = 5424; i <= 5427; i++) {
      NicoRobin.push(new Item(i));
    }
    setHaiTac.push(NicoRobin);//nam

    const Zoro = [];
    for (let i = 5428; i <= 5432; i++) {
      Zoro.push(new Item(i));
    }
    setHaiTac.push(Zoro);//nam
    ///hop sieu nhan

    const gaoDen = [];
    for (let i = 3937; i <= 3939; i++) {
      gaoDen.push(new Item(i));
    }
    SieuNhan.push(gaoDen);

    const gaoDO = [];
    for (let i = 3940; i <= 3942; i++) {
      gaoDO.push(new Item(i));
    }
    SieuNhan.push(gaoDO);//nam

    const gaoXanh = [];
    for (let i = 3943; i <= 3945; i++) {
      gaoXanh.push(new Item(i));
    }
    SieuNhan.push(gaoXanh);//nam
    //hop qua vu tru

    const iron = [];
    for (let i = 3174; i <= 3177; i++) {
      if (i === 3175) { continue; }
      iron.push(new Item(i));
    }
    setVuTru.push(iron);

    const Venom = [];
    for (let i = 4306; i <= 4308; i++) {
      Venom.push(new Item(i));
    }
    setVuTru.push(Venom);

    const Deadpool = [];
    for (let i = 4104; i <= 4107; i++) {
      Deadpool.push(new Item(i));
    }
    setVuTru.push(Deadpool);

    const DrStrange = [];
    for (let i = 4564; i <= 4567; i++) {
      DrStrange.push(new Item(i));
    }
    setVuTru.push(DrStrange);

    const tiDus = [];
    for (let i = 5343; i <= 5346; i++) {
      tiDus.push(new Item(i));
    }
    setVuTru.push(tiDus);

    const Yuna = [];
    for (let i = 5347; i <= 5350; i++) {
      Yuna.push(new Item(i));
    }
    setVuTru.push(Yuna);

    const Batman = [];
    for (let i = 5373; i <= 5375; i++) {
      Batman.push(new Item(i));
    }
    setVuTru.push(Batman);

    const NguoiMeo = [];
    for (let i = 5376; i <= 5378; i++) {
      NguoiMeo.push(new Item(i));
    }
    setVuTru.push(NguoiMeo);
  }

  constructor() {
    this.randomType = new RandomCollection();
    this.randomItemList1 = new RandomCollection();
    this.randomItemList2 = new RandomCollection();
    this.randomItemList3 = new RandomCollection();

    this.randomType1 = new RandomCollection();
    this.randomEvent1 = new RandomCollection();
    this.randomEvent2 = new RandomCollection();//xp hiế, hơn// item loại 2

    GiftBox._staticInit();

    this.randomType.add(45, GiftBox.Items);
    this.randomType.add(25, GiftBox.XU);   // Tỷ lệ
    this.randomType.add(15, GiftBox.XP);   // Tỷ lệ
    this.randomType.add(15, GiftBox.LUONG); // Tỷ lệ 10%

    this.randomItemList1.add(33, 3672);//dns

    this.randomItemList2.add(33, 593);//item

    this.randomItemList3.add(34, 0);//item

    this.randomType1.add(45, GiftBox.Items);
    this.randomType1.add(15, GiftBox.XP);   // Tỷ lệ
    this.randomType1.add(25, GiftBox.XU);   // Tỷ lệ
    this.randomType1.add(15, GiftBox.LUONG); // Tỷ lệ 10%

    this.randomEvent1.add(10, 5499);
    this.randomEvent1.add(10, 4882);
    this.randomEvent1.add(10, 4689);
    this.randomEvent1.add(10, 4692);

    this.randomEvent2.add(10, 6428);
    this.randomEvent2.add(10, 3495);
    this.randomEvent2.add(10, 5497);
    this.randomEvent2.add(10, 6030);
    this.randomEvent2.add(10, 6040);
    this.randomEvent2.add(10, 4686);
    this.randomEvent2.add(10, 4282);
  }

  open(us, item) {
    const type = this.randomType.next();
    switch (type) {
      case GiftBox.Items: {
        const chosenItemCollection = this.chooseItemCollection();
        const idItems = chosenItemCollection.next();
        if (idItems === 0) {
          us.getAvatarService().serverDialog('Bạn nhận được cái nịt : V' + ' Số lượng còn lại: ' + formatNumber(item.getQuantity()));
        }
        if (idItems === 3672 || idItems === 593) {
          const Nro = new Item(idItems, -1, 1);
          if (us.findItemInChests(idItems) != null) {
            const quantity = us.findItemInChests(idItems).getQuantity();
            us.findItemInChests(idItems).setQuantity(quantity + 1);
          } else {
            us.addItemToChests(Nro);
          }
          us.getAvatarService().serverDialog('Bạn nhận được ' + Nro.getPart().getName() + ' Số lượng còn lại: ' + formatNumber(item.getQuantity()));
        }
        break;
      }
      case GiftBox.XU: {
        const xu = Utils.nextInt(1, 5) * 1000;
        us.updateXu(xu);
        us.getAvatarService().serverDialog('Bạn nhận được ' + xu + ' Xu ' + 'Số lượng còn lại: ' + formatNumber(item.getQuantity()));
        us.getAvatarService().updateMoney(0);
        break;
      }
      case GiftBox.XP: {
        const xp = Utils.nextInt(1, 10) * 10;
        us.updateXP(xp);
        us.getAvatarService().serverDialog('Bạn nhận được ' + xp + ' XP ' + 'Số lượng còn lại : ' + formatNumber(item.getQuantity()));
        us.getAvatarService().updateMoney(0);
        break;
      }
      case GiftBox.LUONG: {
        const luong = Utils.nextInt(1, 2);
        us.updateLuong(luong);
        us.getAvatarService().serverDialog('Bạn nhận được ' + luong + ' Lượng ' + 'Số lượng còn lại : ' + formatNumber(item.getQuantity()));
        us.getAvatarService().updateMoney(0);
        break;
      }
    }
  }

  openHaiTac(us, item) {
    GiftBox._staticInit();
    let set;

    do {
      const setIndex = Utils.nextInt(GiftBox.setHaiTac.length);
      set = GiftBox.setHaiTac[setIndex];
    } while (!this.isGenderCompatible(set[0], us)); // Kiểm tra giới tính

    for (let i = 0; i < set.length; i++) {
      set[i].setExpired(-1);
      us.addItemToChests(set[i]);
    }

    us.getAvatarService().serverDialog('Bạn nhận được set ' + set[0].getPart().getName()
      + ' Số lượng còn lại : ' + formatNumber(item.getQuantity()));
  }

  openSieuNhan(us, item) {
    GiftBox._staticInit();
    const setIndex = Utils.nextInt(GiftBox.SieuNhan.length);

    const set = GiftBox.SieuNhan[setIndex];

    for (let i = 0; i < set.length; i++) {
      set[i].setExpired(-1);
      us.addItemToChests(set[i]);
    }
    us.getAvatarService().serverDialog('Bạn nhận được set ' + set[0].getPart().getName() + ' Số lượng còn lại : ' + formatNumber(item.getQuantity()));
  }

  openSetVuTru(us, item) {
    GiftBox._staticInit();
    let set;

    do {
      const setIndex = Utils.nextInt(GiftBox.setVuTru.length);
      set = GiftBox.setVuTru[setIndex];
    } while (!this.isGenderCompatible(set[0], us)); // Kiểm tra giới tính

    for (let i = 0; i < set.length; i++) {
      set[i].setExpired(-1);
      us.addItemToChests(set[i]);
    }

    us.getAvatarService().serverDialog('Bạn nhận được set ' + set[0].getPart().getName()
      + ' Số lượng còn lại : ' + formatNumber(item.getQuantity()));
  }

  openHopQuaMaQuai(us, item) {
    const type = this.randomType.next();
    switch (type) {
      case GiftBox.Items: {
        const chosenItemCollection = this.choseItemGiftEvent();
        const idItems = chosenItemCollection.next();
        const rewardItem = new Item(idItems);
        const ok = (Utils.nextInt(100) < 80) ? true : false;
        if (ok) {
          rewardItem.setExpired(-1);
          us.addItemToChests(rewardItem);
          us.getAvatarService().serverDialog('Bạn nhận được ' + rewardItem.getPart().getName() + ' Vĩnh viễn');
        } else {
          rewardItem.setExpired(Date.now() + (86400000 * 7));
          us.addItemToChests(rewardItem);
          us.getAvatarService().serverDialog('Bạn nhận được ' + rewardItem.getPart().getName() + ' 7 ngày');
        }
        break;
      }
      case GiftBox.XP: {
        const chosenItemCollection1 = this.choseItemGiftEvent();
        const idItems1 = chosenItemCollection1.next();
        const rewardItem1 = new Item(idItems1);
        const ok1 = (Utils.nextInt(100) < 80) ? true : false;
        if (ok1) {
          rewardItem1.setExpired(-1);
          us.addItemToChests(rewardItem1);
          us.getAvatarService().serverDialog('Bạn nhận được ' + rewardItem1.getPart().getName() + ' Vĩnh viễn');
        } else {
          rewardItem1.setExpired(Date.now() + (86400000 * 7));
          us.addItemToChests(rewardItem1);
          us.getAvatarService().serverDialog('Bạn nhận được ' + rewardItem1.getPart().getName() + ' 7 ngày');
        }
        break;
      }
      case GiftBox.XU: {
        const xu = Utils.nextInt(200, 500) * 1000;
        us.updateXu(xu);
        us.getAvatarService().serverDialog('Bạn nhận được ' + xu + ' Xu ');
        us.getAvatarService().updateMoney(0);
        break;
      }
      case GiftBox.LUONG: {
        const luong = Utils.nextInt(20, 60);
        us.updateLuong(luong);
        us.getAvatarService().serverDialog('Bạn nhận được ' + luong + ' Lượng ');
        us.getAvatarService().updateMoney(0);
        break;
      }
    }
  }

  isGenderCompatible(item, user) {
    const itemGender = item.getPart().getGender(); // Giới tính của item (0 = cả hai giới, 1 = nam, 2 = nữ)
    const userGender = user.getGender(); // Giới tính của user (1 = nam, 2 = nữ)

    // Nếu itemGender là 0, thì cả hai giới đều dùng được
    if (itemGender === 0) {
      return true;
    }

    // Nếu không, kiểm tra xem giới tính của item có khớp với giới tính của user không
    return itemGender === userGender;
  }

  chooseItemCollection() {
    const itemCollections = new RandomCollection();
    itemCollections.add(20, this.randomItemList1);
    itemCollections.add(35, this.randomItemList2);
    itemCollections.add(45, this.randomItemList3);
    return itemCollections.next();
  }

  choseItemGiftEvent() {
    const itemCollections = new RandomCollection();
    itemCollections.add(70, this.randomEvent1);
    itemCollections.add(30, this.randomEvent2);
    return itemCollections.next();
  }
}

export default GiftBox;
