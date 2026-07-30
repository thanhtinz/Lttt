/** Port của avatar/handler/NpcHandler.java — máy trạng thái menu/dialog của NPC. */
import { NpcName } from '../constants/NpcName.js';
import { Item } from '../item/Item.js';
import { Part } from '../item/Part.js';
import { partManager } from '../item/PartManager.js';
import { DialLuckyManager } from '../lucky/DialLuckyManager.js';
import { TaiXiu } from '../minigame/TaiXiu.js';
import { Menu } from '../model/Menu.js';
import { Npc } from '../model/Npc.js';
import { Utils } from '../server/Utils.js';
import { BossShopHandler } from './BossShopHandler.js';
import { ShopEventHandler } from './ShopEventHandler.js';

// Static import của Java: dùng trực tiếp tên NPC
const { bunma, Vegeta, Shop_Buy_Luong, Pay_To_Win, Chay_To_Win } = NpcName;

export class NpcHandler {
  /** @type {Map<number, number>} */
  static lastActionTimes = new Map();
  static ACTION_COOLDOWN_MS = 90; // 2 giây cooldown

  static async handleDiaLucky(us, type) {
    const dl = DialLuckyManager.getInstance().find(type);
    if (dl != null) {
      if (dl.getType() === DialLuckyManager.MIEN_PHI) {
        if (us.chests.length >= us.getChestSlot() - 2) {
          us.getAvatarService().serverDialog('Bạn phải có ít nhất 3 ô trống trong rương đồ');
          return;
        }
        const itm = us.findItemInChests(593);
        // NOTE: giữ nguyên hành vi bản Java (dùng itm trước khi kiểm tra null ⇒ NPE nếu không có vé)
        if (itm.getQuantity() <= 0 || itm.getQuantity() > 1998) {
          return;
        }
        if (itm == null || itm.getQuantity() <= 0) {
          us.getAvatarService().serverDialog('Bạn không có Vé quay số miễn phí!');
          return;
        }
      }
      if (dl.getType() === DialLuckyManager.XU) {
        if (us.chests.length >= us.getChestSlot() - 2) {
          us.getAvatarService().serverDialog('Bạn phải có ít nhất 3 ô trống trong rương đồ');
          return;
        }
        if (us.getXu() < 25000) {
          us.getAvatarService().serverDialog('Bạn không đủ xu!');
          return;
        }
      }
      if (dl.getType() === DialLuckyManager.LUONG) {
        if (us.chests.length >= us.getChestSlot() - 2) {
          us.getAvatarService().serverDialog('Bạn phải có ít nhất 3 ô trống trong rương đồ');
          return;
        }
        if (us.getLuong() < 5) {
          us.getAvatarService().serverDialog('Bạn không đủ lượng!!');
          return;
        }
      }
    }
    us.setDialLucky(dl);
    // NOTE: giữ nguyên hành vi bản Java (gọi dl.show khi dl == null ⇒ NPE)
    await dl.show(us);
  }

  static async handlerCommunicate(npcId, us) {
    const z = us.getZone();
    if (z != null) {
      const u = z.find(npcId);
      if (u == null) {
        return;
      }
    } else {
      return;
    }

    const currentTime = Date.now();
    const lastActionTime = NpcHandler.lastActionTimes.has(us.getId())
      ? NpcHandler.lastActionTimes.get(us.getId()) : 0;

    if (currentTime - lastActionTime < NpcHandler.ACTION_COOLDOWN_MS) {
      us.getAvatarService().serverDialog('Từ từ thôi bạn!');
      return;
    }
    // Cập nhật thời gian thực hiện hành động
    NpcHandler.lastActionTimes.set(us.getId(), currentTime);
    const npcIdCase = npcId - Npc.ID_ADD;
    const boss = z.find(npcId);
    const maxDistance = 55.0;
    const playerX = us.getX();
    const playerY = us.getY();
    const bossX = boss.getX();
    const bossY = boss.getY();
    const distance = Utils.distanceBetween(playerX, playerY, bossX, bossY);

    if (npcIdCase > 1000 && npcIdCase <= 9999) {
      if (us.getRandomTimeInMillis() === 0) {
        const randomMinutes = 10 + Math.floor(Math.random() * 11); // 10-20 phút
        const randomTimeInMillis = randomMinutes * 60 * 1000;
        // Gán thời gian ngẫu nhiên cho user
        us.setRandomTimeInMillis(randomTimeInMillis);
        us.setLastTimeSet(Date.now()); // Lưu thời gian hiện tại khi gán
      }

      // Kiểm tra thời gian hiện tại và thời gian đã gán
      const currentTime1 = Date.now();
      console.log(currentTime1);
      if (currentTime1 - us.getLastTimeSet() >= us.getRandomTimeInMillis() || us.getspamclickBoss()) {
        // Nếu thời gian đã hết, hiện thông báo rô bốt
        us.getAvatarService().openMenuOption(1000, 1,
          'bạn có phải robot không? : Đúng rồi',
          'robot là bạn hả ? : Chắc chắn rồi',
          'robot hả : Không phải',
          'bạn là robot : Yes sir');
        us.setspamclickBoss(true);
        us.setRandomTimeInMillis(0); // Đặt lại để lần sau có thể gán thời gian mới
        return;
      }
      if (us.getSession().isResourceHD()) {
        const listmenuboss = [];
        const bossmenu = Menu.builder().name(' Đánh ').action(() => {
          us.getAvatarService().serverDialog('OK !');
        }).build();
        listmenuboss.push(bossmenu);
        us.setMenus(listmenuboss);
        us.getAvatarService().openMenuOption(npcId, 0, listmenuboss);
      }

      if (boss.isDefeated()) {
        us.getAvatarService().serverDialog('boss đã chết');
        return;
      }
      if (distance > maxDistance) {
        us.getAvatarService().serverDialog('Bạn đứng xa rồi : v');
        return;
      }
      // LocalTime.now() so với [07:00, 22:59] (so sánh chặt như Java)
      const d = new Date();
      const nowSec = d.getHours() * 3600 + d.getMinutes() * 60 + d.getSeconds();
      const startSec = 7 * 3600;
      const endSec = 22 * 3600 + 59 * 60;

      if (nowSec > startSec && nowSec < endSec) {
        us.updateXuKillBoss(+1);
      } else {
        // Xử lý nếu thời gian không nằm trong khoảng
        console.log('Hàm không được kích hoạt ngoài khoảng thời gian từ 6h sáng đến 11h đêm.');
      }
      us.updateXu(+us.getDameToXu());

      us.getAvatarService().updateMoney(0);

      const lstUs = us.getZone().getPlayers();

      const skill = us.getUseSkill();
      switch (skill) {
        case 0:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 23, 24);
          break;
        case 1:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 25, 26);
          break;
        case 2:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 12, 13);
          break;
        case 3:
          // Thực hiện hành động khi skill = 3
          break;
        case 4:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 38, 39);
          break;
        case 5:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 42, 43);
          break;
        case 6:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 36, 37);
          break;
        case 7:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 44, 48);
          break;
        default:
          us.skillUidToBoss(lstUs, us.getId(), npcId, 23, 24);
          break;
      }
      await boss.updateHP(-us.getDameToXu(), boss, us);
    } else if (npcIdCase >= 10000) {
      if (distance > maxDistance) {
        us.getAvatarService().serverDialog('Bạn đứng xa rồi : v');
        return;
      }
      if (boss.isSpam()) {
        us.getAvatarService().serverDialog('hộp này đã nhặt');
        return;
      }
      await us.updateSpam(-1, boss, us);
    } else {
      switch (npcIdCase) {
        case NpcName.Than_Tai_Xiu: {
          const menuList = [
            Menu.builder().name('Cược bằng Xu').menus(
              [
                Menu.builder().name('Cược Tài (Xu)').action(() => {
                  us.getAvatarService().sendTextBoxPopup(us.getId(), 0, 'Nhập số xu bạn muốn cược vào Tài:', 1);
                }).build(),
                Menu.builder().name('Cược Xỉu (Xu)').action(() => {
                  us.getAvatarService().sendTextBoxPopup(us.getId(), 1, 'Nhập số xu bạn muốn cược vào Xỉu:', 1);
                }).build(),
              ]).build(),

            Menu.builder().name('Cược bằng Lượng').menus(
              [
                Menu.builder().name('Cược Tài (Lượng)').action(() => {
                  us.getAvatarService().sendTextBoxPopup(us.getId(), 2, 'Nhập số lượng bạn muốn cược vào Tài:', 1);
                }).build(),
                Menu.builder().name('Cược Xỉu (Lượng)').action(() => {
                  us.getAvatarService().sendTextBoxPopup(us.getId(), 3, 'Nhập số lượng bạn muốn cược vào Xỉu:', 1);
                }).build(),
              ]).build(),

            Menu.builder().name('lịch sử 10 ván').action(async () => {
              await TaiXiu.getInstance().viewGameRoundHistory(us);
            }).build(),
            Menu.builder().name('top cao thủ thắng và thua nhiều nhất').action(async () => {
              await TaiXiu.getInstance().getTopWinerXu(us);
              await TaiXiu.getInstance().getTopLossXu(us);
              await TaiXiu.getInstance().getTopWinLuong(us);
              await TaiXiu.getInstance().getTopLossLuong(us);
              us.getAvatarService().serverDialog('check tin nhắn đi');
            }).build(),
            Menu.builder().name('Thành tích').action(async () => {
              await TaiXiu.getInstance().viewBetHistory(us);
            }).build(),
            Menu.builder().name('Thoát').action(() => {
            }).build(),
          ];
          us.setMenus(menuList);
          us.getAvatarService().openMenuOption(npcId, 0, menuList);
          break;
        }

        case NpcName.Tien_chi_mu_Lovanga: {
          const list = [];
          const tienchi = Menu.builder().name('Dự đoán vị trí người yêu').action(() => {
            us.getService().serverDialog(us.getService().DuDoanNY(us));
          }).build();
          list.push(tienchi);
          list.push(Menu.builder().name('Dự đoán kết quả sổ số').action(() => {
            us.getService().serverDialog('Dự đoán kết quả : Comingsion');
          }).build());
          list.push(Menu.builder().name('Dự đoán kết quả Tài Xỉu').action(() => {
            us.getService().serverDialog('Dự đoán kết quả : Comingsion');
          }).build());
          list.push(Menu.builder().name('Thoát').build());
          us.setMenus(list);
          us.getAvatarService().openMenuOption(npcId, 0, list);
          break;
        }
        case NpcName.SAITAMA: {
          const QuanLyItem = [];
          const QuanLyeye = Menu.builder().name('Quản Lý Mặt').action(() => {
            const _chests = us.chests.filter((item) => item.getPart().getZOrder() === 30);
            us.getAvatarService().viewChest(_chests);
          }).build();
          QuanLyItem.push(QuanLyeye);
          QuanLyItem.push(Menu.builder().name('Quản Lý Mắt').action(() => {
            const _chests = us.chests.filter((item) => item.getPart().getZOrder() === 40);
            us.getAvatarService().viewChest(_chests);
          }).build());

          QuanLyItem.push(Menu.builder().name('Xem Ô Rương Còn Trống').action(() => {
            const totalSlots = us.getChestSlot(); // Tổng số ô rương của người dùng
            const usedSlots = us.chests.length;   // Số ô hiện đang sử dụng trong rương
            const emptySlots = totalSlots - usedSlots; // Tính số ô trống còn lại

            // Kiểm tra nếu còn trống, không thì hiển thị rương đã đầy
            if (emptySlots > 0) {
              us.getAvatarService().serverDialog('Số ô trống còn lại trong rương: ' + emptySlots + '/' + totalSlots + ' Rương Lv ' + us.session.user.getChestLevel());
            } else {
              us.getAvatarService().serverDialog('Rương đã đầy!');
            }
          }).build());

          QuanLyItem.push(Menu.builder().name('Xóa Vật Phẩm Trùng (Mặt và Mắt)').action(() => {
            // Lọc ra các vật phẩm có ZOrder là 30 hoặc 40
            const filteredItems = us.chests.filter((item) => {
              const zOrder = item.getPart().getZOrder();
              return zOrder === 30 || zOrder === 40;
            });

            // Tạo một Map để giữ lại mỗi loại item duy nhất dựa trên ID
            const uniqueItemsMap = new Map();
            for (const item of filteredItems) {
              // Giữ lại item đầu tiên nếu trùng
              if (!uniqueItemsMap.has(item.getId())) {
                uniqueItemsMap.set(item.getId(), item);
              }
            }

            // Xóa các vật phẩm có ZOrder 30 hoặc 40 trong `chests`
            for (let i = us.chests.length - 1; i >= 0; i--) {
              const zOrder = us.chests[i].getPart().getZOrder();
              if (zOrder === 30 || zOrder === 40) {
                us.chests.splice(i, 1);
              }
            }

            // Thêm lại các vật phẩm duy nhất vào `chests`
            for (const item of uniqueItemsMap.values()) {
              us.chests.push(item);
            }

            // us.getAvatarService().viewChest(us.chests);
            us.getAvatarService().serverDialog('ok bạn ơi');
          }).build());

          QuanLyItem.push(Menu.builder().name('Thoát').build());
          us.setMenus(QuanLyItem);
          us.getAvatarService().openMenuOption(npcId, 0, QuanLyItem);
          break;
        }
        case NpcName.bunma: {
          const list1 = [];
          const Event = Menu.builder().name('Đổi Quà').action(() => {
            ShopEventHandler.displayUI(us, bunma, 3861, 2295, 2543, 4277, 3494, 4261, 5103, 3962, 5104, 3964, 5130, 5539, 4726, 4903, 3364, 3509, 3679, 2577, 4195, 3358, 5303, 5304, 5305, 5306, 5307, 5308, 6415, 5501, 5502, 6429, 5126, 4044, 2119);
          }).build();
          list1.push(Event);
          list1.push(Menu.builder().name('Góp Kẹo')
            .action(async () => {
              await NpcHandler.GopDiemSK(us);
            })
            .build());
          list1.push(Menu.builder().name('Nhận thưởng đua top')
            .action(async () => {
              await us.session.NhanThuongEventluong();
              await us.session.NhanThuongEventXuBoss();
            })
            .build());

          list1.push(Menu.builder().name('Thành tích bản thân')
            .action(async () => {
              let detailedMessage = 'Thành tích bản thân';
              detailedMessage += `\n Bạn đang có ${us.getScores()} điểm sự kiện`;
              const rankPhaoLuong = await us.getService().getUserRankPhaoLuong(us);
              detailedMessage += `\n Bạn đang ở top ${rankPhaoLuong} thả pháo lượng : ${us.getTopPhaoLuong()}`;

              // int rankPhaoXu = us.getService().getUserRankPhaoXu(us);
              // detailedMessage.append(...)

              const rankXuboss = await us.getService().getUserRankXuBoss(us);
              detailedMessage += `\n Bạn đang ở top ${rankXuboss} điểm đánh boss : ${us.getXu_from_boss()}`;
              us.getAvatarService().serverDialog(detailedMessage);
            })
            .build());
          list1.push(Menu.builder().name('Bảng xếp hạng thả pháo lượng')
            .action(async () => {
              const topPlayers = await us.getService().getTopPhaoLuong();
              let result = '';
              let rank = 1; // Biến đếm để theo dõi thứ hạng

              for (const player of topPlayers) {
                if (player.getTopPhaoLuong() > 0) {
                  result += player.getUsername()
                    + ' Top ' + rank + ' : '
                    + player.getTopPhaoLuong()
                    + '\n';
                  rank++; // Tăng thứ hạng sau mỗi lần thêm người chơi vào kết quả
                }
              }
              us.getAvatarService().customTab('Top 10 thả pháo lượng', result);
            })
            .build());
          list1.push(Menu.builder().name('Bảng xếp hạng điểm đánh boss')
            .action(async () => {
              const topPlayers = await us.getService().getTop10PlayersByXuFromBoss();
              let result = '';
              let rank = 1; // Biến đếm để theo dõi thứ hạng

              for (const player of topPlayers) {
                if (player.getXu_from_boss() > 0) {
                  result += player.getUsername()
                    + ' Top ' + rank + ' : '
                    + player.getXu_from_boss()
                    + ' Điểm\n';
                  rank++; // Tăng thứ hạng sau mỗi lần thêm người chơi vào kết quả
                }
              }
              us.getAvatarService().customTab('Top 10 Điểm đánh boss', result);
            })
            .build());
          list1.push(Menu.builder().name('Xem hướng dẫn')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'cứ đánh boss là 1 điểm chỉ tính trong thời gian từ 7h đến 23h hàng ngày');
            })
            .build());
          list1.push(Menu.builder().name('Thoát').id(npcId).build());
          us.setMenus(list1);
          us.getAvatarService().openMenuOption(npcId, 0, list1);
          break;
        }
        case NpcName.Vegeta: {
          const lstVegeta = [];

          const vegenta = Menu.builder().name('Quà Thẻ VIP PREMIUM').action(() => {
            ShopEventHandler.displayUI(us, Vegeta, 6450, 6314, 6555, 5822, 4560, 4698, 4699, 6430, 5651, 5455, 5341);
          }).build();
          lstVegeta.push(vegenta);

          lstVegeta.push(Menu.builder().name('Quà Thẻ VIP Cao Cấp').action(() => {
            ShopEventHandler.displayUI(us, Vegeta, 6113, 6553, 4561, 4562, 4563, 4304, 6449, 5761);
          }).build());

          lstVegeta.push(Menu.builder().name('Quà Thẻ VIP').action(() => {
            ShopEventHandler.displayUI(us, Vegeta, 3638, 3636, 620, 2090, 6541, 2052, 2053, 3636, 3638);
          }).build());

          lstVegeta.push(Menu.builder().name('Xem hướng dẫn')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'Top 5 đánh boss nhận 1 thẻ vip trong top 3 được tùy chọn 1 trong 2 set Xác ướp hoặc Tù trưởng xác ướp riêng top 1 nhận thêm 1 thẻ vip. Top 4-5 thả pháo lượng nhận 2 thẻ vip trong top 3 nhận thẻ vip cao cấp và được tùy chọn 1 trong 3 set xác ướp , tù trưởng xác ướp, doremon (doremon thì ko có dame)');
            })
            .build());
          lstVegeta.push(Menu.builder().name('Thoát').id(npcId).build());
          us.setMenus(lstVegeta);

          us.getAvatarService().openMenuOption(npcId, 0, lstVegeta);
          break;
        }
        case NpcName.CHU_DAU_TU: {
          const chudautu = [];
          const chuDautu = Menu.builder().name('Mua biệt thự').action(async () => {
            await us.session.BuyHouse();
          }).build();
          chudautu.push(chuDautu);
          us.setMenus(chudautu);
          us.getAvatarService().openMenuOption(npcId, 0, chudautu);
          break;
        }

        case NpcName.DAU_GIA: {
          us.getAvatarService().serverDialog('đấu giá đang bảo trì các bạn vui lòng quay lại sau');
          break;
        }
        case NpcName.QUAY_SO: {
          const qs = [];
          const quaySo1 = Menu.builder().name('Quay số').menus(
            [
              Menu.builder().name('5 lượng').action(async () => {
                console.log('Action for 5 lượng triggered');
                await NpcHandler.handleDiaLucky(us, DialLuckyManager.LUONG);
              }).build(),
              Menu.builder().name('25.000 xu').action(async () => {
                console.log('Action for 15.000 xu triggered');
                await NpcHandler.handleDiaLucky(us, DialLuckyManager.XU);
              }).build(),
              Menu.builder().name('Q.S miễn phí').action(async () => {
                console.log('Action for Q.S miễn phí triggered');
                await NpcHandler.handleDiaLucky(us, DialLuckyManager.MIEN_PHI);
              }).build(),
              Menu.builder().name('Thoát').action(() => {
                console.log('Exit menu triggered');
              }).build(),
            ])
            .id(npcId)
            .build();
          qs.push(quaySo1);
          qs.push(Menu.builder().name('Xem hướng dẫn').action(() => {
            console.log('Action for Xem hướng dẫn triggered');
            us.getAvatarService().customTab('Hướng dẫn', 'Để tham gia quay số bạn phải có ít nhất 5 lượng hoặc 25 ngàn xu trong tài khoản và 3 ô trống trong rương\n Bạn sẽ nhận được danh sách những món đồ đặc biệt mà bạn muốn quay. Những món đồ đặc biệt này bạn sẽ không thể tìm thấy trong bất cứ shop nào của thành phố.\n Sau khi chọn được món đồ muốn quay bạn sẽ bắt đầu chỉnh vòng quay để quay\n Khi quay bạn giữ phím 5 để chỉnh lực quay sau đó thả ra để bắt đầu quay\n Khi quay bạn sẽ có cơ hội trúng từ 1 đến 3 món quà\n Quà của bạn nhận được có thể là vật phẩm bất kì, xu, hoặc điểm kinh nghiệm\n Bạn có thể quay được những bộ đồ bán bằng lượng như đồ hiệp sĩ, pháp sư...\n Tuy nhiên vật phẩm bạn quay được sẽ có hạn sử dụng trong một số ngày nhất định.\n Nếu bạn quay được đúng món đồ mà bạn đã chọn thì bạn sẽ được sở hữu món đồ đó vĩnh viễn.\n Hãy thử vận may để sở hữa các món đồ cực khủng nào !!!');
          }).build());
          qs.push(Menu.builder().name('Thoát').build());
          us.setMenus(qs);
          us.getAvatarService().openMenuOption(npcId, 0, qs);
          break;
        }
        case NpcName.THO_KIM_HOAN: {
          const nangcap = [];
          const npcChat = 'Muốn nâng cấp đồ thì vào đây';
          const upgrade = Menu.builder().name('Nâng cấp').id(npcId).menus(
            [
              Menu.builder().name('Nâng cấp xu').id(npcId)
                .menus(NpcHandler.listItemUpgrade(npcId, us, BossShopHandler.SELECT_XU))
                .build(),
              Menu.builder().name('Nâng cấp lượng').id(npcId)
                .menus(NpcHandler.listItemUpgrade(npcId, us, BossShopHandler.SELECT_LUONG))
                .id(npcId)
                .build(),
              Menu.builder().name('Thoát').id(npcId).build(),
            ])
            .build();
          nangcap.push(upgrade);
          nangcap.push(Menu.builder().name('Xem hướng dẫn')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'Nâng thì nâng không nâng thì cút!');
            })
            .build());
          nangcap.push(Menu.builder().name('Thoát').id(npcId).build());
          us.setMenus(nangcap);
          us.getAvatarService().openMenuOption(npcId, 0, nangcap);
          break;
        }
        case NpcName.LAI_BUON: {
          const laibuon = [];
          const LAI_BUON = Menu.builder().name('Điểm Danh').action(() => {
            // Item item = new Item(593, -1, 1);
            // us.addItemToChests(item);
            // us.addExp(5);
            us.getService().serverMessage('đang xây dựng'); // Bạn nhận được 5 điểm exp + 1 thẻ quay số miễn phí
          }).build();
          laibuon.push(LAI_BUON);
          laibuon.push(Menu.builder().name('Xem hướng dẫn').action(() => {
            us.getAvatarService().customTab('Hướng dẫn', 'Đăng nhập mỗi ngày để nhận quà.\nDùng điểm chuyên cần để nhận đucợ những món quà có giá trị trong tương lai');
          }).build());
          laibuon.push(Menu.builder().name('Thoát').build());
          us.setMenus(laibuon);
          us.getAvatarService().openMenuOption(npcId, 0, laibuon);
          break;
        }
        case NpcName.THO_CAU: {
          const thocau = [];
          const thoCau = Menu.builder().name('Câu cá').action(() => {
            const Items1 = [];
            const item = new Item(446, 30, 0); // câu vip
            Items1.push(item);
            const item1 = new Item(460, 2, 0); // vé cau
            Items1.push(item1);
            const item2 = new Item(448, 30, 1); // mồi
            Items1.push(item2);
            us.getAvatarService().openUIShop(npcId, 'Trùm Câu Cá,', Items1);
            us.getAvatarService().updateMoney(0);
          }).build();
          thocau.push(thoCau);
          thocau.push(Menu.builder().name('Bán cá').action(async () => {
            await NpcHandler.sellFish(us);
          }).build());
          thocau.push(Menu.builder().name('Xem hướng dẫn').action(() => {
            us.getAvatarService().customTab('Hướng dẫn', 'Câu cá kiếm được nhiều xu bản auto lên thanhpholo.com');
          }).build());
          thocau.push(Menu.builder().name('Thoát').build());
          us.setMenus(thocau);
          us.getAvatarService().openMenuOption(npcId, 0, thocau);
          break;
        }
        case NpcName.CUA_HANG: {
          const listet = [];
          const Items = [];

          const quaySo = Menu.builder().name('shop noname').action(() => {
            const itemshop0 = Part.shopByPart(partManager.getShop0());

            if (itemshop0 == null) {
              console.log('Items list is null');
              return; // Handle the null case
            }
            us.getAvatarService().openUIShop(5, 'shop 0', itemshop0);
          }).build();
          listet.push(quaySo);
          listet.push(Menu.builder().name('Hướng dẫn').action(() => {
            us.getAvatarService().customTab('Hướng dẫn', 'chua co huong dan');
          }).build());
          listet.push(Menu.builder().name('Thoát').build());
          us.setMenus(listet);
          us.getAvatarService().openUIMenu(npcId, 0, listet, 'text 1', 'text2');
          break;
        }
        case NpcName.Shop_Buy_Luong: {
          const ListDacBiet = [];
          const ShopDacBiet = Menu.builder().name('Đổi Quà').action(() => {
            ShopEventHandler.displayUI(us, Shop_Buy_Luong, 3672, 5898, 4331, 3112, 4736);
          }).build();
          ListDacBiet.push(ShopDacBiet);
          ListDacBiet.push(Menu.builder().name('Xem hướng dẫn')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'co cai d.');
            })
            .build());
          ListDacBiet.push(Menu.builder().name('Thoát').id(npcId).build());
          us.setMenus(ListDacBiet);
          us.getAvatarService().openMenuOption(npcId, 0, ListDacBiet);
          break;
        }
        case NpcName.Pay_To_Win: {
          const ListDacBiet = [];
          const ShopDacBiet = Menu.builder().name('Pay To Win').action(() => {
            ShopEventHandler.displayUI(us, Pay_To_Win, 6803, 6824, 3076);
          }).build();
          ListDacBiet.push(ShopDacBiet);
          const ShopQuaSet = Menu.builder().name('Pay To Win cả Set').action(() => {
            ShopEventHandler.displayUI(us, Pay_To_Win, 5880, 5324, 5408, 4345, 6556, 6560);
          }).build();
          ListDacBiet.push(ShopQuaSet);
          ListDacBiet.push(Menu.builder().name('Shop Nâng Cấp Pay To Win').id(npcId)
            .menus(NpcHandler.listItemUpgradePay(npcId, us, BossShopHandler.SELECT_HoaNS))
            .build());
          ListDacBiet.push(Menu.builder().name('Đổi quà cả Set là gì ?')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'Đổi cả set là được cả set(ít nhất 3 món) , Hộp quà vũ trụ mở ra set 80 dame như : iron , Venmon , DeadPool,(nam,nữ) Dr Strange(nam),  TiDus(nam)/Yuna(nữ), Batman(nam), Người mèo(nữ) | hộp quà siêu nhân 50 dame : gao xanh, đỏ ,đen,| hộp quà hải tặc (80 dame) : Luffy: Nam,Nami: Nữ,Mihawk: Nam,Nico Robin: Nữ,Zoro: Nam');
            })
            .build());
          ListDacBiet.push(Menu.builder().name('Sen Ngũ Sắc, Đá Ngũ Sắc ở đâu?')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'Sen Ngũ Sắc khi nạp 20k(1400lg) bạn sẽ được kèm thêm 50 Sen Ngũ Sắc chỉ nạp mới có, còn Đá ngũ sắc mở từ hộp quà may mắn (nhặt khi đánh boss) , mua bằng lượng thì qua hawai');
            })
            .build());
          ListDacBiet.push(Menu.builder().name('Thoát').id(npcId).build());
          us.setMenus(ListDacBiet);
          us.getAvatarService().openMenuOption(npcId, 0, ListDacBiet);
          break;
        }
        case NpcName.Chay_To_Win: {
          const ListDacBiet = [];
          const ShopDacBiet = Menu.builder().name('Chay To Win (Xu)').action(() => {
            ShopEventHandler.displayUI(us, Chay_To_Win, 2049, 2050, 2051, 2054, 2041, 2056, 2354, 2355, 3440, 3441, 3442, 3443, 3445, 3446, 3627, 3628, 3629, 3630, 3631, 3632, 3633, 3634, 3360, 6822, 6731, 4736);
          }).build();
          ListDacBiet.push(ShopDacBiet);
          ListDacBiet.push(Menu.builder().name('Shop Nâng Cấp Chay To Win').id(npcId)
            .menus(NpcHandler.listItemUpgradeChay(npcId, us, BossShopHandler.SELECT_DNS))
            .build());
          ListDacBiet.push(Menu.builder().name('Shop Đổi Bằng Đá Ngũ Sắc').action(() => {
            ShopEventHandler.displayUI(us, Pay_To_Win, 3743, 3742);
          }).build());
          ListDacBiet.push(Menu.builder().name('Sen Ngũ Sắc, Đá Ngũ Sắc ở đâu ?')
            .action(() => {
              us.getAvatarService().customTab('Hướng dẫn', 'Sen Ngũ Sắc khi nạp trên 20k(1400lg) bạn sẽ được kèm thêm 50 Sen Ngũ Sắc chỉ nạp mới có, còn Đá ngũ sắc mở từ hộp quà may mắn (nhặt khi đánh boss) , mua bằng lượng thì qua hawai');
            })
            .build());
          ListDacBiet.push(Menu.builder().name('Thoát').id(npcId).build());
          us.setMenus(ListDacBiet);
          us.getAvatarService().openMenuOption(npcId, 0, ListDacBiet);
          break;
        }
      }
    }
  }

  static async sellFish(us) {
    const array = [2130, 2131, 2132, 454, 455, 456, 457];

    for (const idFish of array) {
      let item = us.findItemInChests(idFish); // Tìm item trong rương theo idFish

      // Nếu không tìm thấy item, tiếp tục với ID tiếp theo
      while (item != null && item.getQuantity() > 0) {
        const sellPrice = item.getPart().getCoin(); // Giá bán 1 món đồ
        const message = `Bạn vừa bán 1 ${item.getPart().getName()} với giá = ${sellPrice} xu.`;

        us.removeItemFromChests(item);
        us.updateXu(sellPrice); // Cập nhật xu

        us.getAvatarService().updateMoney(0); // Cập nhật tiền tệ
        us.getAvatarService().SendTabmsg(message); // Gửi thông báo bán hàng

        // Cập nhật lại item để kiểm tra số lượng
        item = us.findItemInChests(idFish);
      }
    }
  }

  static async GopDiemSK(us) {
    const itemsToProcess = new Map();
    itemsToProcess.set(2383, 1);
    itemsToProcess.set(2384, 2);
    itemsToProcess.set(2385, 3);
    let addscores = 0;
    // Lặp qua từng cặp ID và số lượng

    let detailedMessage = 'Bạn đã đổi thành công:';
    for (const [itemId, scores] of itemsToProcess) {
      const item = us.findItemInChests(itemId);
      if (item != null && item.getQuantity() > 0) {
        addscores += item.getQuantity() * scores;
        detailedMessage += `\n${item.getPart().getName()} :(Điểm ${scores}) Số lượng ${item.getQuantity()}  x  tong ${item.getQuantity() * scores} điểm`;
        // NOTE: giữ nguyên hành vi bản Java (cộng dồn addscores mỗi vòng lặp)
        us.updateScores(+addscores);
        await us.removeItem(itemId, item.getQuantity());
      }
    }
    if (addscores > 0) {
      detailedMessage += `\n Tổng tất cả ${addscores}` + ' điểm';
      us.getAvatarService().serverDialog(detailedMessage);
    } else {
      us.getAvatarService().serverDialog('Bạn không còn kẹo');
    }
  }

  static listItemUpgradeChay(npcId, us, type) {
    return [
      Menu.builder()
        .name('Nâng cấp item Chay To Win')
        .id(npcId)
        .action(() => {
          BossShopHandler.displayUI(us, BossShopHandler.SELECT_DNS, 3774, 3776, 3775, 4157, 5012, 5010, 5011, 5401, 6491, 3855);
        })
        .build(),
      Menu.builder()
        .name('Nâng cấp Item Quay Số/Nâng Cấp') // Nâng bằng đá ngũ sắc
        .id(npcId)
        .action(() => {
          BossShopHandler.displayUI(us, BossShopHandler.SELECT_DNS, 4907, 4119, 5516, 5099, 5337, 5342, 5470, 5667, 5846, 6575);
        })
        .build(),
      Menu.builder()
        .name('Đổi item từ mảnh ghép') // đổi mảnh ghép thành item
        .id(npcId)
        .action(() => {
          BossShopHandler.displayUI(us, BossShopHandler.SELECT_ManhGhep, 6837);
        })
        .build(),
    ];
  }

  static listItemUpgradePay(npcId, us, type) {
    return [
      Menu.builder()
        .name('Pay, Nâng cấp item')
        .id(npcId)
        .action(() => {
          BossShopHandler.displayUI(us, BossShopHandler.SELECT_HoaNS, 6671, 6672, 5012, 5010, 5011, 5401, 6491);
        })
        .build(),
      Menu.builder()
        .name('Pay, Nâng cấp Item Quay Số/Nâng Cấp')
        .id(npcId)
        .action(() => {
          BossShopHandler.displayUI(us, BossShopHandler.SELECT_HoaNS, 6671, 6672);
        })
        .build(),
    ];
  }

  static listItemUpgrade(npcId, us, type) {
    // String npcName = "Thợ KH";
    // String npcChat = "Muốn đồ đang mặc đẹp hơn không? Ta có thể giúp bạn đấy";
    return [
      Menu.builder().name('Quà cầm tay').id(npcId)
        .menus([
          Menu.builder().name('Bông hoa cổ tích').action(() => {
            BossShopHandler.displayUI(us, type, 6212, 6213, 6214);
          }).build(),
          Menu.builder().name('Hoa hồng phong thần').action(() => {
            BossShopHandler.displayUI(us, type, 5321, 5322, 5323);
          }).build(),
          Menu.builder().name('Hoa hồng xanh pha lê thần thoại').action(() => {
            BossShopHandler.displayUI(us, type, 5286, 5287, 5288);
          }).build(),
          Menu.builder().name('Mộc thảo hồ điệp').action(() => {
            BossShopHandler.displayUI(us, type, 4160, 4161, 4162, 4163, 5050);
          }).build(),
          Menu.builder().name('Cung thần tình yêu thần thoại').action(() => {
            BossShopHandler.displayUI(us, type, 4893, 4894, 4895);
          }).build(),
          Menu.builder().name('Cung xanh thần thoại').action(() => {
            BossShopHandler.displayUI(us, type, 4890, 4891, 4892);
          }).build(),
          Menu.builder().name('Gậy thả thính mê hoặc').action(() => {
            BossShopHandler.displayUI(us, type, 3507, 4218);
          }).build(),
          Menu.builder().name('Chong chóng thiên thần').action(() => {
            BossShopHandler.displayUI(us, type, 2238, 2239, 2274, 2275, 2404);
          }).build(),
          Menu.builder().name('Cục vàng huyền thoại').action(() => {
            BossShopHandler.displayUI(us, type, 2217, 2218, 2219, 2220, 2221, 2222, 2223);
          }).build(),
        ])
        .build(),
      Menu.builder().name('Nón').menus([
        Menu.builder().name('Nón phù thuỷ hoả ngục truyền thuyết').action(() => {
          BossShopHandler.displayUI(us, type, 2411, 2412, 2413, 2414, 5503, 5504);
        }).build(),
        Menu.builder().name('Vương miện hoàng thân').action(() => {
          BossShopHandler.displayUI(us, type, 5394);
        }).build(),
        Menu.builder().name('Vương miện hoàng thân').action(() => {
          BossShopHandler.displayUI(us, type, 5391);
        }).build(),
        Menu.builder().name('Tôi thấy hoa vàng trên cỏ xanh').action(() => {
          BossShopHandler.displayUI(us, type, 3266, 3267, 3268, 3269, 3954);
        }).build(),
        Menu.builder().name('Vương miện phép màu').action(() => {
          BossShopHandler.displayUI(us, type, 3422, 3423, 3639, 3640);
        }).build(),
        Menu.builder().name('Mũ ảo thuật tinh anh').action(() => {
          BossShopHandler.displayUI(us, type, 2899, 2900, 2901, 2902, 2903, 3037, 3038, 3039);
        }).build(),
        Menu.builder().name('Vương miện huyền vũ').action(() => {
          BossShopHandler.displayUI(us, type, 2997, 2998, 2999);
        }).build(),
      ])
        .build(),
      Menu.builder().name('Trang phục')
        .menus([
          Menu.builder().name('Danh gia vọng tộc').action(() => {
            BossShopHandler.displayUI(us, type, 5392, 5393);
          }).build(),
          Menu.builder().name('Nữ hoàng sương mai').action(() => {
            BossShopHandler.displayUI(us, type, 5054, 5055);
          }).build(),
          Menu.builder().name('Bá tước bóng đêm').action(() => {
            BossShopHandler.displayUI(us, type, 2876, 2877);
          }).build(),
          Menu.builder().name('Napoleon').action(() => {
            BossShopHandler.displayUI(us, type, 2231, 2232);
          }).build(),
          Menu.builder().name('Elizabeth').action(() => {
            BossShopHandler.displayUI(us, type, 2229, 2230);
          }).build(),
        ])
        .build(),
      Menu.builder().name('Cánh')
        .menus([
          Menu.builder().name('Cánh Thần Mặt Trời').action(() => {
            BossShopHandler.displayUI(us, type, 5312);
          }).build(),
          Menu.builder().name('Cánh chiến thần hắc hoá').action(() => {
            BossShopHandler.displayUI(us, type, 5971, 5972, 5973, 5974);
          }).build(),
          Menu.builder().name('Cánh quạ đen hoả ngục').action(() => {
            BossShopHandler.displayUI(us, type, 4332, 5313);
          }).build(),
          Menu.builder().name('Cánh tiểu thần phong linh').action(() => {
            BossShopHandler.displayUI(us, type, 2419, 2482, 2483, 2505, 2506, 5252, 5253);
          }).build(),
          Menu.builder().name('Cửu vỹ hồ ly thần thoại').action(() => {
            BossShopHandler.displayUI(us, type, 4333, 4910, 4911, 4912, 4913, 4914, 4915, 4916, 4334, 4889);
          }).build(),
          Menu.builder().name('Cánh vàng ròng đa sắc').action(() => {
            BossShopHandler.displayUI(us, type, 3376, 3377, 3404, 4897);
          }).build(),
          Menu.builder().name('Cánh thiên thần tiên bướm').action(() => {
            BossShopHandler.displayUI(us, type, 4056, 4796);
          }).build(),
          Menu.builder().name('Cánh thiên hồ tình yêu vĩnh cửu').action(() => {
            BossShopHandler.displayUI(us, type, 4196, 4435);
          }).build(),
          Menu.builder().name('Cánh băng hoả thần thoại').action(() => {
            BossShopHandler.displayUI(us, type, 3448, 4057, 4375);
          }).build(),
          Menu.builder().name('Cánh hoả thần').action(() => {
            BossShopHandler.displayUI(us, type, 4311, 4312, 4313);
          }).build(),
          Menu.builder().name('Cánh thiên sứ tình yêu').action(() => {
            BossShopHandler.displayUI(us, type, 2148, 2149, 2150, 2151, 2152, 3637);
          }).build(),
          Menu.builder().name('Cánh thiên sứ').action(() => {
            BossShopHandler.displayUI(us, type, 2142, 2143, 2144, 2145, 2146, 3635);
          }).build(),
          Menu.builder().name('Cánh địa ngục hắc ám').action(() => {
            BossShopHandler.displayUI(us, type, 3529, 3530, 3531, 3532);
          }).build(),
          Menu.builder().name('Cánh cổng địa ngục').action(() => {
            BossShopHandler.displayUI(us, type, 3522, 3523, 3524, 3525, 3526, 3527);
          }).build(),
          Menu.builder().name('Cánh bướm đêm huyền thoại').action(() => {
            BossShopHandler.displayUI(us, type, 3366, 3379);
          }).build(),
          Menu.builder().name('Cánh băng giá huyền thoại').action(() => {
            BossShopHandler.displayUI(us, type, 3365, 3378);
          }).build(),
          Menu.builder().name('Cánh phép màu ước mơ').action(() => {
            BossShopHandler.displayUI(us, type, 2793, 2794, 2795, 2796);
          }).build(),
          Menu.builder().name('Cánh blue vững vàng').action(() => {
            BossShopHandler.displayUI(us, type, 2788, 2789, 2790, 2791);
          }).build(),
        ])
        .build(),
      Menu.builder().name('Thú cưng')
        .menus([
          Menu.builder().name('Thiên thần hộ mệnh toàn năng').action(() => {
            BossShopHandler.displayUI(us, type, 5224, 5225, 5226);
          }).build(),
          Menu.builder().name('Cáo tuyết cửu vỹ').action(() => {
            BossShopHandler.displayUI(us, type, 4904, 4905);
          }).build(),
          Menu.builder().name('Cửu vỹ hồ ly').action(() => {
            BossShopHandler.displayUI(us, type, 4724, 4728, 4729);
          }).build(),
          Menu.builder().name('Bay nax 2.0').action(() => {
            BossShopHandler.displayUI(us, type, 4079, 4080);
          }).build(),
          Menu.builder().name('Phương hoàng lửa').action(() => {
            BossShopHandler.displayUI(us, type, 3668, 3771, 3772, 3773, 3854);
          }).build(),
          Menu.builder().name('King Kong').action(() => {
            BossShopHandler.displayUI(us, type, 3744);
          }).build(),
          Menu.builder().name('Kỳ lân truyền thuyết').action(() => {
            BossShopHandler.displayUI(us, type, 2726, 2727, 2728, 2729, 2730);
          }).build(),
        ])
        .build(),
      Menu.builder().name('Tóc')
        .menus([
          Menu.builder().name('Tóc Siêu Xaya').action(() => {
            BossShopHandler.displayUI(us, type, 2019);
          }).build(),
        ])
        .build(),
    ];
  }

  static async handlerAction(us, npcId, menuId, select) {
    const z = us.getZone();
    if (z != null) {
      const u = z.find(npcId);
      if (u == null) {
        return;
      }
    } else {
      return;
    }
    const menus = us.getMenus();
    if (menus != null && select < menus.length) {
      const menu = menus[select];
      if (menu.isMenu()) {
        us.setMenus(menu.getMenus());
        us.getAvatarService().openUIMenu(npcId, menuId + 1, menu.getMenus(), menu.getNpcName(), menu.getNpcChat());
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
}

export default NpcHandler;
