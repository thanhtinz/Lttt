/** Port của avatar/server/GameString.java — chỉ là các chuỗi tĩnh. */
export class GameString {

  static leave1() {
    return 'Đã bỏ chạy -%dxp.';
  }

  static leave2() {
    return 'Đã bỏ chạy.';
  }

  static inviteError1() {
    return 'Bạn đó đã offline rồi!.';
  }

  static inviteError2() {
    return 'Bạn đó đã vào bàn khác chơi rồi!.';
  }

  static inviteMessage() {
    return '%s mời bạn chơi?';
  }

  static missionError1() {
    return 'Không tồn tại nhiệm vụ này!';
  }

  static missionError2() {
    return 'Nhiệm vụ chưa hoàn thành!';
  }

  static missionError3() {
    return 'Nhiệm vụ đã hoàn thành!';
  }

  static missionComplete() {
    return 'Chúc mừng bạn nhận được phần thưởng %s!';
  }

  static phucHoiSuccess() {
    return 'Phục hồi điểm nâng cấp thành công!.';
  }

  static x2XPSuccess() {
    return 'Sử dụng thành công. Bạn có 1 ngày xp x2!.';
  }

  static x2XPRequest() {
    return 'Bạn có muốn sử dụng item này không? Hiệu lực 1 ngày!.';
  }

  static phucHoiDiemString() {
    return 'Bạn có muốn sử dụng item này không? Sẽ xóa hết điểm đã nâng!.';
  }

  static mssTGString() {
    return 'Tin nhắn từ %s: %s.';
  }

  static thongBaoString() {
    return 'Thông Báo: %s.';
  }

  static dailyReward() {
    return 'Hôm nay bạn được tặng %dx item %s. Chúc chơi game vui vẻ.';
  }

  static newsNoti() {
    return 'Ủng hộ cho game soạn tin nhắn: KN UH %s gửi 8798. Thay lời cám ơn, chúng tôi sẽ tặng bạn biểu tượng trên nhân vật !';
  }

  static loginErr1() {
    return 'Bạn đang đăng nhập ở máy khác. Hãy thử đăng nhập lại';
  }

  static userLoginLock(minutes) {
    return 'Tài khoản của bạn đang bị khóa trong ' + ((minutes | 0) + 1) + ' phút, vui lòng liên hệ admin để biết thêm chi tiết !';
  }

  static userLoginLockForever() {
    return 'Tài khoản của bạn đã bị khóa trong vĩnh viễn, vui lòng liên hệ admin để biết thêm chi tiết !';
  }

  static userChatLock(minutes) {
    return 'Tài khoản của bạn đang bị khóa chat thế giới trong ' + ((minutes | 0) + 1) + ' phút !';
  }

  static userChatLockForever() {
    return 'Tài khoản của bạn đã bị khóa chat thế giới trong vĩnh viễn !';
  }

  static userLoginActive() {
    return 'Tài khoản chưa kích hoạt! Vui lòng kích hoạt để trải nghiệm.';
  }

  static userLoginMany() {
    return 'Có người khác đăng nhập vào tài khoản của bạn!.';
  }

  static loginPassFail() {
    return 'Thông tin tài khoản hoặc mật khẩu không chính xác';
  }

  static hopNgocFail() {
    return 'Chế ngọc thất bại, hao phí %d %s!.';
  }

  static hopNgocSucess() {
    return 'Chế ngọc thành công hao phí %d %s thu được %d %s!.';
  }

  static cheDoSuccess() {
    return 'Chế đồ thành công';
  }

  static cheDoFail() {
    return 'Chế đồ thất bại';
  }

  static hopNgocError() {
    return 'Lỗi kết hợp';
  }

  static hopNgocCantDo() {
    return 'Không thể kết hợp';
  }

  static hopNgocRequest() {
    return 'Bạn có muốn gắn ngọc vào trang bị?';
  }

  static hopNgocNC() {
    return 'Bạn có muốn hợp thành ngọc cấp cao hơn? Tỉ lệ thành công là %s.';
  }

  static hopNgocSell() {
    return 'Bạn có muốn bán %d vật phẩm với giá %d xu?';
  }

  static hopNgocNoSlot() {
    return 'Trang bị đã chọn không còn đủ chỗ';
  }

  static itemNotAvailable() {
    return 'Vật phẩm này không bán, vui lòng quay lại sau !.';
  }

  static xuNotEnought() {
    return 'Bạn không có đủ tiền!.';
  }

  static levelNotEnought() {
    return 'Biệt đội của bạn không đủ cấp độ để mua vật phẩm này ! Hãy liên hệ đội trưởng để nâng cấp !';
  }

  static buySuccess() {
    return 'Giao dịch thành công. Xin cảm ơn!.';
  }

  static changPassError1() {
    return 'Mật khẩu cũ không chính xác!.';
  }

  static ruongNoSlot() {
    return 'Không đủ chỗ trong rương!.';
  }

  static thaoNgocRequest() {
    return 'Bạn có muốn tháo ngọc khỏi trang bị? Bạn cần %d xu?';
  }

  static sellTBRequest() {
    return 'Bạn có muốn bán trang bị này với giá %d xu?';
  }

  static sellTBError1() {
    return 'Không thể bán trang bị đang sử dụng!.';
  }

  static thaoNgocError1() {
    return 'Không thể tháo ngọc trang bị đang sử dụng!.';
  }

  static thaoNgocError2() {
    return 'Bạn không có đủ tiền. Bạn cần %d xu để thực hiện thao tác này!.';
  }

  static thaoNgocSuccess() {
    return 'Thao tác thành công!.';
  }

  static giaHanRequest() {
    return 'Bạn có muốn gia hạn trang bị này với giá %d xu?';
  }

  static giaHanSucess() {
    return 'Gia hạn thành công!.';
  }

  static findKVError1() {
    return 'Không tìm được khu vực!.';
  }

  static joinKVError1() {
    return 'Mật khẩu không chính xác!.';
  }

  static joinKVError2() {
    return 'Không đủ tiền cược!.';
  }

  static joinKVError3() {
    return 'Khu vực đã đầy!.';
  }

  static kickString() {
    return 'Bạn bị kick bởi chủ phòng!.';
  }

  static datCuocError1() {
    return 'Chỉ có thể đặt cược từ %d xu đến %d xu!.';
  }

  static startGameError1() {
    return 'Mọi người chưa sẵn sàng!.';
  }

  static startGameError2() {
    return 'Còn %s chưa sẵn sàng!.';
  }

  static startGameError3() {
    return '%s lỗi item slot %d xin chọn lại!.';
  }

  static startGameError4() {
    return 'Số lượng 2 bên chưa ngang nhau';
  }

  static selectMapError1_1() {
    return 'Chỉ có thể chọn map %s!.';
  }

  static selectMapError1_2() {
    return 'Chỉ có thể chọn map %s hoặc map %s!.';
  }

  static selectMapError1_3() {
    return 'Lỗi chọn map!.';
  }
}

export default GameString;
