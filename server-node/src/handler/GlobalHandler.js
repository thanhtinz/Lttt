/** Port của avatar/handler/GlobalHandler.java */
import dbManager from '../db/DbManager.js';
import { Item } from '../item/Item.js';
import { TaiXiu } from '../minigame/TaiXiu.js';
import { GiftCodeService } from '../model/GiftCodeService.js';
import { Message } from '../net/Message.js';
import { Avatar } from '../server/Avatar.js';
import serverManager from '../server/ServerManager.js';
import userManager from '../server/UserManager.js';
import { EffectService } from '../service/EffectService.js';

export class GlobalHandler {
  constructor(user) {
    this.us = user;
    this.lst = userManager.users;
  }

  async handleOptionMenu(ms) {
    const userId = ms.reader().readInt();
    const menuId = ms.reader().readByte();
    const select = ms.reader().readByte();
    console.log('userId = ' + userId + ', menuId = ' + menuId + ', select = ' + select);
    await this.menuOptionHandle(userId, menuId, select);
    if (userId >= 2000000000 || userId === 1) {
      // NpcHandler.handlerAction(this.us, userId, menuId, select);
      return;
    } else {
      switch (userId) {
        case 5:
          switch (menuId) {
            case 0:
              switch (select) {
                case 0:
                  await this.sendCityMap();
                  break;
                case 1:
                  await this.sendCityMap();
                  break;
                case 2:
                  await this.sendCityMap();
                  break;
              }
              break;
          }
          break;
        case 1000:
          switch (menuId) {
            case 1: {
              if (select !== 2) {
                this.us.incrementIntSpanboss(); // Tăng spam lên 1
                if (this.us.getIntSpanboss() >= 500) { // Giả sử MAX_SPAM là 10
                  this.us.resetUser();
                  userManager.find(this.us.getId()).session.close();
                }
                return;
              }
              switch (select) {
                case 0:
                  break;
                case 1:
                  break;
                case 2: {
                  const number1 = 1 + Math.floor(Math.random() * 10);
                  const number2 = 1 + Math.floor(Math.random() * 10);
                  this.us.setcorrectAnswer(number1 + number2);
                  const equation = number1 + ' + ' + number2 + ' =  ?';
                  this.us.getAvatarService().sendTextBoxPopup(this.us.getId(), 102, equation, 1);
                  break;
                }
                case 3:

                  break;
              }
              break;
            }
            case 2: {
              if (select === 1) {

              }
            }
            // NOTE: giữ nguyên hành vi bản Java (case 2 không có break)
          }
          break;
      }
    }
  }

  async menuOptionHandle(npcId, menuId, select) {
    const menus = this.us.getMenus();
    if (menus != null && select < menus.length) {
      const menu = menus[select];
      if (menu.isMenu()) {
        this.us.setMenus(menu.getMenus());
        this.us.getAvatarService().openUIMenu(npcId, menuId + 1, menu.getMenus(), menu.getNpcName(), menu.getNpcChat());
      } else if (menu.getAction() != null) {
        await menu.perform();
      } else {
        switch (menu.getId()) {
          // Java để trống
        }
      }
      return;
    }
  }

  async acceptHenHo() {
    const insertQuery = 'INSERT INTO marry (idNam, idNu, level, perLevel) VALUES (?, ?, ?, ?)';
    let idNvNam;
    let idNu;
    if (this.us.getGender() === 1) {
      idNvNam = this.us.getId();
      idNu = this.us.getIdUsHenHo();
    } else {
      idNvNam = this.us.getIdUsHenHo();
      idNu = this.us.getId();
    }
    try {
      await dbManager.executeUpdate(insertQuery, [idNvNam, idNu, 0, 0]);
    } catch (e) {
      console.error(e);
    }
  }

  async handleTextBox(ms) {
    const userId = ms.reader().readInt();
    const menuId = ms.reader().readByte();
    const text = ms.reader().readUTF();

    switch (menuId) {
      case 100: {
        const nameU = text;
        if (nameU === this.us.getUsername()) {
          this.us.getAvatarService().serverDialog('không chơi tự sướng nha b');
          return;
        }
        let idNvNam;
        let idNu;
        if (this.us.getGender() === 1) {
          idNvNam = this.us.getId();
          idNu = this.us.getIdUsHenHo();
        } else {
          idNvNam = this.us.getIdUsHenHo();
          idNu = this.us.getId();
        }
        const checkExistQuery = 'SELECT COUNT(*) FROM marry WHERE idNam = ? OR idNu = ?';
        try {
          // Kiểm tra xem cặp idNam và idNu có tồn tại hay không
          const rows = await dbManager.query(checkExistQuery, [idNvNam, idNu]);
          const first = rows[0];
          const count = first == null ? null : Number(Object.values(first)[0]);
          if (first != null && count === 0) {
            for (const user of userManager.users) {
              if (user.getUsername() === nameU) {
                this.us.getAvatarService().serverDialog('ok gửi lời mới hẹn hò tới ' + nameU);
                this.us.setIdUsHenHo(user.getId());
                user.setIdUsHenHo(this.us.getId());
                user.getAvatarService().sendTextBoxPopup(user.getId(), 101, 'Bạn có muốn hẹn hò với ' + this.us.getUsername() + ' không ? nếu muốn thì trả lời ok hoặc yes', 1);
              }
            }
          } else {
            this.us.getAvatarService().serverDialog('đã hẹn hò hoặc kết hôn rồi');
          }
        } catch (e) {
          console.error(e);
        }

        break;
      }
      case 101:
        try {
          if (text === 'ok' || text === 'yes') {
            await this.acceptHenHo();
            for (const user of userManager.users) {
              if (user.getId() === this.us.getIdUsHenHo()) {
                user.getAvatarService().serverDialog('Bạn đã hẹn hò thành công với ' + this.us.getUsername());
                this.us.getAvatarService().serverDialog('Bạn đã hẹn hò thành công với ' + user.getUsername());

                user.setWearingMarry(this.us.getWearing());
                user.setNamehh(this.us.getUsername());
                this.us.setWearingMarry(user.getWearing());
                this.us.setNamehh(user.getUsername());
              }
            }
          } else {

          }
        } catch (e) {
          console.error(e);
        }
        break;
      case 0: // Cược Tài (Xu)
      case 1: // Cược Xỉu (Xu)
      case 2: // Cược Tài (Lượng)
      case 3: // Cược Xỉu (Lượng)
        await TaiXiu.getInstance().handleBetWithInput(this.us, menuId, userId, text);
        break;
      case 102: {
        const userAnswer = parseInt(text, 10);
        if (Number.isNaN(userAnswer)) {
          // Java: NumberFormatException
          this.us.getAvatarService().serverDialog('Vui lòng nhập một số hợp lệ.');
          break;
        }
        if (userAnswer === this.us.getcorrectAnswer()) {
          this.us.getAvatarService().serverDialog('Chúc mừng! Bạn đã trả lời đúng.');
          this.us.setspamclickBoss(false);
          this.us.resetUser();
        } else {
          this.us.getAvatarService().serverDialog('Sai rồi! Kết quả đúng là: ' + this.us.getcorrectAnswer());
        }
        break;
      }
      case 20: {
        const giftCodeService = new GiftCodeService();
        await giftCodeService.useGiftCode(this.us.getId(), text);
        break;
      }
      case 98:
        if (parseInt(text, 10) === 1) {
          for (const user of userManager.users) {
            user.getAvatarService().serverInfo('ad : bảo trì sau 2p vui lòng off để tránh mất item');
            user.getAvatarService().serverDialog('bảo trì sau 2p vui lòng off : v');
          }
        }
        break;
      case 99: {
        const parts = text.split(' ');
        const value1 = (parseInt(parts[0], 10) << 24) >> 24; // byte
        const value2 = (parseInt(parts[1], 10) << 16) >> 16;  // short
        for (const u of this.us.getZone().getPlayers()) {
          EffectService.createEffect()
            .session(u.session)
            .id((value1 << 24) >> 24)
            .style(0)
            .loopLimit(5)
            .loop((value2 << 16) >> 16)
            .loopType(1)
            .radius(20)
            .idPlayer(this.us.getId())
            .send();
        }
        break;
      }
      case 7:
        try {
          // Tách id và username
          const idAndName = text.split(' '); // Tách phần trước và sau dấu cách
          const idPart = idAndName[0];       // Phần chứa id
          const usernamePart = idAndName[1]; // Phần chứa username
          // Chuyển đổi id từ chuỗi sang số ngắn (short)
          const idItem = (parseInt(idPart, 10) << 16) >> 16;
          if (Number.isNaN(parseInt(idPart, 10))) {
            this.us.getAvatarService().serverDialog('invalid input, item code must be number');
            break;
          }
          // In kết quả
          console.log('ID: ' + idItem);
          console.log('Username: ' + usernamePart);

          if (idItem > 0 && idItem < 9999) {
            const item = new Item(idItem, -1, -0);
            for (let i = 0; i < this.lst.length; i++) {
              if (this.lst[i].getUsername() === usernamePart) {
                await this.lst[i].addItemToChests(item);
                const content = 'admin : Bạn được tặng item ' + item.getPart().getName();
                this.lst[i].getAvatarService().SendTabmsg(content);
                this.lst[i].getAvatarService().serverDialog('added ' + item.getPart().getName() + ' into chests');
                this.lst[i].getAvatarService().serverInfo(content);
                this.us.getAvatarService().serverDialog('added ' + item.getPart().getName() + ' into my chests');
              }
            }
          } else {
            this.us.getAvatarService().serverDialog('id lớn hơn 2000 và nhỏ hơn 6795');
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        break;
      case 8:
        try {
          if (this.us.getId() === 7) {
            const noidung = String(text);
            for (let i = 0; i < this.lst.length; i++) {
              this.lst[i].getAvatarService().serverInfo(noidung);
            }
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        break;
      case 9:
        try {
          if (this.us.getId() === 7) {
            const weather = (parseInt(text, 10) << 24) >> 24;
            if (Number.isNaN(parseInt(text, 10))) {
              this.us.getAvatarService().serverDialog('invalid input, item code must be number');
              break;
            }
            this.us.getAvatarService().weather(weather);
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        break;
      case 10:
        try {
          if (this.us.getId() === 7) {
            // save data account
            const ids = [];

            for (const us of this.lst) {
              ids.push(us.getId());
            }
            for (const id of ids) {
              try {
                userManager.find(id).session.close();
              } catch (e) {
                // Java bỏ qua
              }
            }
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        break;
      case 11:
        try {
          if (this.us.getId() === 7) {
            if (parseInt(text, 10) === 1) {
              for (const user of userManager.users) {
                user.getAvatarService().serverInfo(`ad : thành phố  ${serverManager.cityName}. có ${serverManager.clients.length}  đang online. chúc mọi người vui vẻ`);
              }
            }
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        break;
      case 12:
        try {
          if (this.us.getId() === 7) {
            if (parseInt(text, 10) === 1) {
              for (let i = 0; i < this.lst.length; i++) {
                this.lst[i].getAvatarService().serverInfo(`ad : thành phố  ${serverManager.cityName}. có ${serverManager.clients.length}  đang online. chúc mọi người vui vẻ`);
              }
            }
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        // NOTE: giữ nguyên hành vi bản Java (case 12 thiếu break ⇒ rơi xuống case 13)
      case 13:
        try {
          if (this.us.getId() === 7) {
            if (parseInt(text, 10) === 1) {
              // Node không có ThreadMXBean; giữ nguyên thông báo, đếm 1 luồng
              const threadCount = 1;
              console.log('Number of threads: ' + threadCount);
              this.us.getAvatarService().serverDialog('theard = ' + threadCount);
            }
          }
        } catch (e) {
          this.us.getAvatarService().serverDialog('invalid input, item code must be number');
        }
        break;
    }
  }

  async sendCityMap() {
    const folder = 'res/map/';
    const data = await Avatar.getFile(folder + 'cityMap.dat');
    const image = await Avatar.getFile(folder + 'cityMap.png');
    const map27 = await Avatar.getFile(folder + '27.dat');
    const map_bg = await Avatar.getFile(folder + 'bg/27.png');

    let ms = new Message(-92);
    let ds = ms.writer();
    ds.writeByte(1);
    ds.writeInt(image.length);
    ds.write(image);

    ds.writeInt(data.length);
    ds.writeByte(34);
    ds.write(data);

    const idImg = [821, 827, 850];
    const doorName = ['Sân Bay', 'Bãi Biển', 'Trung Tâm Giải Trí'];
    const x = [23, 9, 21];
    const y = [7, 13, 18];

    ds.writeByte(idImg.length);
    let i = 0;
    while (i < idImg.length) {
      ds.writeByte(i);
      ds.writeShort(idImg[i]);
      ds.writeUTF(doorName[i]);
      ds.writeByte(x[i]);
      ds.writeByte(y[i]);
      ++i;
    }
    ds.flush();
    await this.us.sendMessage(ms);

    ms = new Message(-93);
    ds = ms.writer();

    ds.writeByte(27);
    ds.writeByte(1);
    ds.writeShort(306);
    ds.writeByte(34);
    ds.writeShort(map27.length);
    ds.write(map27);

    const arr = [828, -1, 835, 853, 852, 836, 832, 832, 851, 833, 837, 842, 843, -1, -1];
    ds.writeByte(arr.length);
    for (let j = 0; j < arr.length; j++) {
      ds.writeShort(arr[j]);
    }

    ds.writeShort(map_bg.length);
    ds.write(map_bg);

    try {
      const mapId = 27;

      const GET_MAP_ITEM_TYPE = 'SELECT * FROM `map_item_typem` WHERE `map_id` = ?';
      const res = await dbManager.query(GET_MAP_ITEM_TYPE, [mapId]);
      // NOTE: giữ nguyên hành vi bản Java (ResultSet != null luôn đúng ⇒ luôn vào nhánh này)
      if (res != null) {
        ds.writeShort(204);
        const rows = res.length;
        ds.writeByte(rows);

        for (const r of res) {
          ds.writeByte(r.id_type);
          ds.writeShort(r.id_img);
          ds.writeByte(r.icon_id);
          ds.writeShort(r.dx);
          ds.writeShort(r.dy);

          const av_position = JSON.parse(r.av_position);
          ds.writeByte(av_position.length);
          for (let m = 0; m < av_position.length; m++) {
            const av_position_element = av_position[m];
            ds.writeByte(av_position_element.x);
            ds.writeByte(av_position_element.y);
          }
        }
        const mapItemA = [26, 27, 28, 29, 31, 64];
        const mapItemB = [0, 0, 0, 0, 0, 4];
        const mapItemC = [2, 7, 27, 20, 33, 15];
        const mapItemD = [1, 1, 1, 1, 1, 0];

        ds.writeByte(mapItemA.length);
        for (let k = 0; k < mapItemA.length; k++) {
          ds.writeByte(mapItemA[k]);
          ds.writeByte(mapItemB[k]);
          ds.writeByte(mapItemC[k]);
          ds.writeByte(mapItemD[k]);
        }
      } else {
        ds.writeShort(0);
      }
    } catch (e) {
      console.error(e);
    }

    ds.flush();
    await this.us.sendMessage(ms);
  }
}

export default GlobalHandler;
