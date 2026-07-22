# Avatar — Client Unity (Android + iOS)

Bản port **Unity C#** của client Avatar (được decode/export từ APK bằng AssetRipper).
Đây là client **native** cho cả **Android** và **iOS** — không phải bọc emulator J2ME,
nên **không dính lỗi lật sprite** như chạy jar qua emulator.

- **Engine gốc:** Unity `5.6.4f1` (xem `ProjectSettings/ProjectVersion.txt`)
- **Code:** `Assets/Scripts/Assembly-CSharp/` (198 file C#, có logic thật)
- **Tài nguyên:** `Assets/Resources/hd/`
- **Kết nối server:** `Session_ME.cs` dùng `TcpClient` tới port `19128` (khớp server trong repo)
- **Orientation:** đã khoá **màn hình ngang** (landscape, cả 2 chiều) trong `ProjectSettings/ProjectSettings.asset`

> ⚠️ Project export từ ripper nên khi mở lần đầu Unity sẽ re-import toàn bộ asset và
> có thể báo vài warning (GUID/shader). Script C# là code thật, build được.

---

## 1. Mở project

- Cài **Unity Hub**, thêm bản Unity phù hợp rồi **Open** thư mục `client/unity`.
- **Khuyến nghị nâng cấp:** bản gốc `5.6.4f1` (2017) quá cũ để build iOS với Xcode hiện tại.
  Nên mở bằng một **Unity LTS mới** (vd `2021.3 LTS` / `2022.3 LTS`) và để Unity tự nâng cấp
  project. Sau nâng cấp có thể phải sửa vài API cũ (`UnityEngine.Networking`, `WWW`, ...).

## 2. Trỏ về server của bạn

Sửa `Assets/Scripts/Assembly-CSharp/GameMidlet.cs` (constructor `static GameMidlet()`):

```csharp
IPEng   = "112.78.1.25";     // -> IP/domain server của bạn
PORTEng = 19128;             // giữ nguyên nếu server chạy 19128
IP[0][0] = new string[] { "avhm.teamobi.com", ... };   // -> IP/domain server của bạn
linkGetHost[0] = new string[] { "http://teamobi.com/srvips/avatarios.txt", ... };
                              // -> URL danh sách server của bạn, hoặc bỏ để dùng IP hardcode
```

Đổi các host `*.teamobi.com` và `linkGetHost` thành server của bạn. Cổng mặc định `19128`
đã khớp với `server/config.properties`.

## 3. Build Android (APK/AAB)

1. `File > Build Settings > Android`, `Switch Platform`.
2. `Player Settings`:
   - Orientation: đã set **Landscape** (khoá ngang).
   - Đặt package name, minSDK/targetSDK, icon.
   - Scripting Backend: `IL2CPP` + `ARM64` (bắt buộc để lên Google Play).
3. `Build` → ra file `.apk`/`.aab`.

## 4. Build iOS

1. `File > Build Settings > iOS`, `Switch Platform` → `Build` (ra **Xcode project**).
2. Mở project bằng **Xcode trên macOS**, cấu hình **Signing** (Apple Developer account).
3. Orientation: đã khoá **Landscape**.
4. `Product > Archive` → xuất **IPA** (cần chứng chỉ/provisioning profile).

> Build iOS **bắt buộc** có **macOS + Xcode**. Không thể build iOS trên Linux/Windows.

---

## Ghi chú
- Repo này chỉ chứa **source project**; file build (`.apk`/`.ipa`) phải tạo từ Unity Editor
  trên máy có cài Unity (và macOS cho iOS).
- Thư mục `Library/`, `Temp/`, `obj/`, `Build/` do Unity sinh ra khi mở — đã bị `.gitignore`.
