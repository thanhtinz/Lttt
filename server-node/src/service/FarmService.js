/** Port của avatar/service/FarmService.java */
import fs from 'fs';

import { Cmd } from '../constants/Cmd.js';
import { Animal } from '../farm/Animal.js';
import { HatGiong } from '../farm/HatGiong.js';
import { LandItem } from '../farm/LandItem.js';
import { partManager } from '../item/PartManager.js';
import { KeyValue } from '../lib/KeyValue.js';
import { gameData } from '../model/GameData.js';
import { Message } from '../net/Message.js';
import { Service } from './Service.js';

/** Tương đương Avatar.getFile(): đọc cả file, lỗi -> null (không làm sập server). */
function getFile(url) {
  try {
    return fs.readFileSync(url);
  } catch (e) {
    return null;
  }
}

export class FarmService extends Service {

  constructor(cl) {
    super(cl);
  }

  sellFarmitm(user, ms) {
    const idFarmItm = ms.reader().readShort();
    console.log(idFarmItm);
    user.getAvatarService().serverDialog('id');
  }

  //trồng cây
  // Phương thức trồng cây
  plandSeed(ms) {
    const idUser = ms.reader().readInt();
    const indexCell = ms.reader().readByte();
    const idSeed = ms.reader().readByte();

    const hd = this.session.user.findhatgiong(idSeed);
    // Kiểm tra ô đất có tồn tại trong danh sách không
    if (indexCell >= 0 && indexCell < this.session.user.landItems.length && hd.getSoluong() > 0) {
      // Lấy ô đất tại vị trí indexCell và cập nhật thông tin
      const landItem = this.session.user.landItems[indexCell];
      landItem.setType(idSeed); // Đặt mã cây mới
      hd.setSoluong(hd.getSoluong() - 1);
      landItem.setSucKhoe(100);
      landItem.setGrowthTime(0); // Reset thời gian tăng trưởng, vì cây mới vừa trồng
      landItem.setResourceCount(0); // Đặt số lượng tài nguyên về 0, vì cây mới chưa có tài nguyên
      landItem.setWatered(false); // Đặt trạng thái tưới nước ban đầu
      landItem.setFertilized(false); // Đặt trạng thái bón phân ban đầu
      landItem.setHarvestable(false); // Đặt trạng thái chưa thể thu hoạch
    } else {
      // Nếu ô đất không tồn tại, có thể xử lý ngoại lệ hoặc thông báo lỗi
      console.log('Ô đất không tồn tại hoặc indexCell không hợp lệ.');
    }

    // Tạo Message và gửi thông tin trồng cây
    ms = new Message(Cmd.PLANT_SEED);
    const ds = ms.writer();
    ds.writeInt(idUser);
    ds.writeByte(indexCell);
    ds.writeByte(idSeed);
    ds.flush();
    this.session.sendMessage(ms);
  }

  //thu hoạch
  treeHarvest(ms) {
    const idfarm = ms.reader().readInt();//idUser
    const indexcell = ms.reader().readByte();
    ms = new Message(Cmd.TREE_HARVEST);
    const ds = ms.writer();
    ds.writeByte(indexcell);
    if (indexcell >= 0 && indexcell < this.session.user.landItems.length) {
      // Lấy ô đất tại vị trí indexCell và cập nhật thông tin
      const landItem = this.session.user.landItems[indexcell];
      const itemf = partManager.findFarmitemByID(landItem.getType());
      landItem.setType(-1);
      // Java: (itemf.getQuantity()%100) * landItem.getSucKhoe()/100 -> chia nguyên
      const sanluong = Math.trunc(((itemf.getQuantity() % 100) * landItem.getSucKhoe()) / 100);
      ds.writeShort(sanluong);//so luong thu hoach duoc
    } else {
      // Nếu ô đất không tồn tại, có thể xử lý ngoại lệ hoặc thông báo lỗi
      console.log('Ô đất không tồn tại hoặc indexCell không hợp lệ.');
    }
    //ds.writeShort(sanluong);//so luong thu hoach duoc
    ds.flush();
    this.session.sendMessage(ms);
  }

  // mở ô đất
  doRequestslot(ms) {
    const id = ms.reader().readInt();//id user
    ms = new Message(Cmd.REQUEST_SLOT);
    const ds = ms.writer();
    ds.writeUTF('Bạn có muốn mở ô đất @ với giá @ xu hoặc @ lượng không ?');
    ds.flush();
    this.session.sendMessage(ms);
  }

  //mở ô đất
  openLand(ms) {
    const id = ms.reader().readInt(); // ID của nông trại
    const typeBuy = ms.reader().readByte(); // Loại giao dịch hoặc mã người dùng

    this.session.user.landItems.push(new LandItem(0, -1, 0, 0, false, false, false, new Date())); // Ô đất mặc định

    ms = new Message(Cmd.OPEN_LAND);
    const ds = ms.writer();
    ds.writeInt(id);
    ds.writeInt(1);
    ds.writeByte(typeBuy);     // Thông tin giao dịch hoặc mã người dùng
    ds.writeUTF('Đã mở ô đất thành công'); // Thông báo mở đất thành công

    // Ghi thêm thông tin mô tả cho ô đất vừa được mở
    ds.writeInt(this.session.user.getXu() | 0);
    ds.writeInt(this.session.user.getLuong());
    ds.writeInt(0);
    ds.flush();

    this.session.sendMessage(ms);
  }

  setBigFarm(ms) {
    ms = new Message(51);
    const ds = ms.writer();
    const images = [99, 206];
    ds.writeByte(images.length);
    for (let i = 0; i < images.length; ++i) {
      ds.writeShort(i);
      ds.writeShort(images[i]);
    }
    ds.writeInt(15378);
    ds.writeInt(62724);
    ds.flush();
    this.session.sendMessage(ms);

    //apk goc
    // ms = new Message(51);
    // DataOutputStream ds = ms.writer();
    // short b19 = 2;
    // ds.writeByte(b19);
    // ...
  }

  Buy_item_farm(ms) {
    const id = ms.reader().readShort();
    const n = ms.reader().readByte();
    const type = ms.reader().readByte();
    console.log('item farm id ' + id + ' sl ' + n + ' type sell' + type);

    const itemf = partManager.findFarmitemByID(id);

    if (type === 1) {
      if (this.session.user.getXu() < itemf.getSell() * n) {
        this.session.user.getAvatarService().serverDialog('bạn không đủ xu');
        return;
      }
    } else {
      if (this.session.user.getLuong() < itemf.getSell() * n) {
        this.session.user.getAvatarService().serverDialog('bạn không đủ lượng');
        return;
      }
    }

    const hdid = this.session.user.findhatgiong(id);
    if (hdid != null) {
      if ((hdid.getSoluong() + n) > 99) {
        this.session.user.getAvatarService().serverDialog('số lượng nhiều rồi');
        return;
      }
    }

    ms = new Message(62);
    const ds = ms.writer();
    ds.writeShort(id);
    ds.writeByte(n);
    ds.writeInt(this.session.user.getXu() | 0);//.newMoney
    this.session.user.updateXu(-itemf.getSell() * n);
    ds.writeByte(type);
    ds.writeInt(this.session.user.getXu() | 0);
    ds.writeInt(this.session.user.getLuong());//luong
    ds.writeInt(0);//luongK
    ds.flush();
    this.session.sendMessage(ms);

    this.session.user.hatgiong.push(new HatGiong(id, n));
  }

  Buy_ANIMAL(ms) {
    const n = ms.reader().readByte();//loai
    const type = ms.reader().readByte();//typemua

    ms = new Message(Cmd.BUY_ANIMAL);
    const ds = ms.writer();
    ds.writeByte(n);
    ds.writeInt(0);//newMoney2
    ds.writeByte(type);
    ds.writeInt(0);
    ds.writeInt(0);//luong
    ds.writeInt(0);//luongK
    ds.flush();
    this.session.sendMessage(ms);

    const newAnimal = new Animal(n, 2000, 100, 0, 20, true, false, true); // Các giá trị mặc định
    this.session.user.Animal.push(newAnimal);
  }

  getBigFarm(ms) {
    const imageID = ms.reader().readShort();
    const folder = this.session.getResourcesPath() + 'bigFarm/';
    const dat = getFile(folder + imageID + '.png');
    if (dat == null) {
      return;
    }
    ms = new Message(54);
    const ds = ms.writer();
    ds.writeShort(imageID);
    ds.writeShort(dat.length);
    ds.writeShort(dat.length);
    for (let i = 0; i < dat.length; ++i) {
      ds.writeByte(dat[i]);
    }
    ds.flush();
    this.session.sendMessage(ms);
  }

  getImageData() {
    try {
      const imageInfos = gameData.getFarmImageDatas();
      const ms = new Message(Cmd.GET_IMAGE_FARM);
      const ds = ms.writer();
      ds.writeShort(imageInfos.length);
      for (const imageInfo of imageInfos) {
        ds.writeShort(imageInfo.getId());
        ds.writeShort(imageInfo.getBigImageID());
        ds.writeByte(imageInfo.getX());
        ds.writeByte(imageInfo.getY());
        ds.writeByte(imageInfo.getW());
        ds.writeByte(imageInfo.getH());
      }
      ds.flush();
      this.sendMessage(ms);
    } catch (e) {
      console.debug('getImageData: ' + e.message);
    }
  }

  getTreeInfo(ms) {
    const dat = getFile('res/data/farm_info.dat');
    if (dat == null) {
      return;
    }
    ms = new Message(Cmd.GET_TREE_INFO);
    const ds = ms.writer();
    ds.write(dat);
    ds.flush();
    this.session.sendMessage(ms);
  }

  getInventory(ms) {
    const us = this.session.user;
    const nongsandacbiet = [];
    nongsandacbiet.push(new KeyValue(255, 20));//thit ca
    nongsandacbiet.push(new KeyValue(215, 680));//khe
    nongsandacbiet.push(new KeyValue(214, 4));//tinh dau huong duong
    ms = new Message(60);
    const ds = ms.writer();
    ds.writeByte(this.session.user.hatgiong.length);
    for (const hatgiong of this.session.user.hatgiong) {
      ds.writeByte(hatgiong.getId());
      ds.writeShort(hatgiong.getSoluong());
    }

    ds.writeByte(this.session.user.NongSan.length);
    for (const ns of this.session.user.NongSan) {
      ds.writeByte(ns.getId());
      ds.writeShort(ns.getSoluong());
    }

    ds.writeInt(this.session.user.getXu() | 0);
    ds.writeByte(us.getLeverFarm());
    ds.writeByte(us.getLeverPercen());

    ds.writeByte(this.session.user.PhanBon.length);
    for (const pb of this.session.user.PhanBon) {
      ds.writeByte(pb.getId());
      ds.writeShort(pb.getSoluong());
    }
    /// ///////
    ds.writeByte(nongsandacbiet.length);
    for (const i of nongsandacbiet) {
      ds.writeShort(i.getKey());
      ds.writeShort(i.getValue());
    }
    ds.writeByte(1);
    ds.writeInt(64000);
    ds.writeBoolean(true);
    ds.writeShort(us.getLeverFarm());
    ds.writeByte(us.getLeverPercen());
    ds.writeByte(this.session.user.NongSan.length);
    for (const ns of this.session.user.NongSan) {
      ds.writeByte(ns.getId());
      ds.writeShort(ns.getSoluong());
    }

    /// //////
    ds.writeByte(nongsandacbiet.length);
    for (const i of nongsandacbiet) {
      ds.writeShort(i.getKey());
      ds.writeInt(i.getValue());
    }
    ds.flush();
    this.session.sendMessage(ms);
  }

  writeInfoCell(ds, land) {
    ds.writeShort(land.getMinutesSincePlanted() | 0);
    ds.writeByte(land.getSucKhoe());
    ds.writeByte(1); //100 la héo
    ds.writeBoolean(land.isWatered());
    ds.writeBoolean(land.isFertilized());
    ds.writeBoolean(land.isHarvestable());
  }

  writeInfoAnimal(ds, animal) {
    ds.writeInt(animal.getHealth());
    ds.writeByte(animal.getLevel());
    ds.writeByte(animal.getResourceCount());
    ds.writeByte(animal.getNextProductionTime());
    ds.writeBoolean(animal.isAlive());
    ds.writeBoolean(animal.isReadyForBreeding());
    ds.writeBoolean(animal.isHarvestable());
  }

  joinFarm(ms) {
    const userId = ms.reader().readInt();
    ms = new Message(Cmd.JOIN);
    const ds = ms.writer();
    ds.writeInt(userId);

    const landSize = this.session.user.landItems.length;
    ds.writeByte(landSize);  // Ghi số lượng ô đất

    // Ghi thông tin các ô đất (cây)
    for (let i = 0; i < landSize; ++i) {
      const landItem = this.session.user.landItems[i];
      if (landItem.getType() >= 0) {
        ds.writeByte(landItem.getType());  //id
        this.writeInfoCell(ds, landItem);
      } else {
        ds.writeByte(-1);  // Không có cây trong ô đất này
      }
    }
    ds.writeByte(this.session.user.Animal.length);  // Ghi số lượng động vật
    for (const animal of this.session.user.Animal) {
      ds.writeByte(animal.getId());  // Ghi ID động vật
      this.writeInfoAnimal(ds, animal);  // Ghi thông tin về động vật
    }

    // Ghi thông tin khác
    ds.writeByte(10);// chỉnh kích cỡ chuồng bò
    ds.writeByte(10);//chỉnh kích cỡ hồ cá
    ds.writeShort(10);  //cấp độ cây khế
    ds.writeShort(43);//43//img cay khe
    ds.writeShort(46);//46//img qua khe
    ds.writeShort(180);  // Số khế có thể thu hoạch
    ds.writeShort(0);//
    ds.writeShort(0);//0
    ds.writeShort(0);//0

    // Ghi trạng thái các ô đất
    for (let i = 0; i < landSize; ++i) {
      ds.writeByte(1);  // Trạng thái ô đất
    }

    ds.writeShort(3);//nấu ăn
    ds.writeShort(5);
    ds.flush();

    this.session.sendMessage(ms);
  }

  getImgFarm(ms) {
    const imageID = ms.reader().readShort();
    const folder = this.session.getResourcesPath() + 'farm/';
    const dat = getFile(folder + imageID + '.png');
    if (dat == null) {
      return;
    }
    ms = new Message(Cmd.GET_IMG_FARM);
    const ds = ms.writer();
    ds.writeShort(imageID);
    ds.writeShort(dat.length);
    ds.write(dat);
    ds.flush();
    this.session.sendMessage(ms);
  }
}

export default FarmService;
