# Ghi chú sửa KRX18 ngày 19 tháng 11 năm 2026

KRX18Provider nâng từ v8 lên v9, giữ lớp `com.anhdaden.KRX18Plugin`, tên hiển thị KRX18 và danh sách quốc gia. Không sửa các provider khác của FilmPro18, không thay quyền tài khoản hay cơ chế kích hoạt.

## Nguyên nhân

Trang chi tiết và API Dooplay vẫn hoạt động, nhưng player đã chuyển từ cơ chế CryptoJS AES CBC dạng Salted hex sang **AES GCM, Base64 và IV của player hiện tại**. Resolver v8 dùng giao thức cũ nên không trả được link.

## Luồng lấy nguồn mới

1. Đọc các tùy chọn `.dooplay_player_option` trong trang phim để lấy `data-post`, `data-type`, `data-nume`.
2. Gọi `/wp-json/dooplayer/v2/{post}/{type}/{nume}` và đọc `embed_url`, hỗ trợ cả URL và iframe HTML.
3. Đọc `idfile_enc`, `idUser_enc` và `DOMAIN_API` trên trang embed. Không cố định host API khi HTML đã cung cấp host mới.
4. Giải cấu hình theo giao thức AES GCM của JS công khai. Gửi `data` dạng form tới API được chỉ định, trong đó có mã media, origin trang nguồn và thời gian hiện tại.
5. Chấp nhận phản hồi `status=success`; nếu `type=url-m3u8-encv1`, giải URL theo giao thức phản hồi tương ứng. Lỗi máy chủ được giữ thành thông báo lỗi, không giả báo đã tìm được nguồn.
6. Chuyển HLS cho player native của Cloudstream, giữ User-Agent và Referer của embed trong cả bước lấy link lẫn phát. Thử các tùy chọn nguồn độc lập để một nguồn lỗi không làm mất nguồn khác.

Các hằng số mã hóa là thông số truyền tải được công khai trong JS của website, không phải mật khẩu, API key TorBox hoặc thông tin đăng nhập của người dùng. Không tạo quyền VIP, không gỡ DRM và không giả token tài khoản.

## Cấu hình và cập nhật online

`domainkrx18.txt` tiếp tục là địa chỉ đổi tên miền, mặc định `https://krx18.com`, lưu tạm 5 phút. Tên miền mới phải dùng giao thức tương thích; đổi nội dung file không tự sửa được thay đổi giao thức player.

`repo.json` trước đây trỏ cố định tới `plugins.json` của commit cũ. Chuyển sang manifest ở nhánh `main` để lần cập nhật sau được phát hiện. Riêng URL binary trong manifest trỏ commit bất biến chứa đúng file v9, tránh cache nhầm v8. Đồng bộ `version`, `fileSize` và SHA-256.

Trong Cloudstream, làm mới kho FilmPro18 và cập nhật KRX18 lên v9. Nếu kho đã thêm trước đây vẫn giữ manifest cố định, thêm lại URL `https://raw.githubusercontent.com/TNB88/FilmPro18/main/repo.json` để ứng dụng nhận địa chỉ manifest mới.

## Kiểm tra và source

Build thành công với Kotlin 2.3.0 và D8 của Android build-tools 37.0.0, min API 21. Kiểm tra trực tiếp đã đi qua Dooplay, embed, API mới, giải URL HLS và đọc được đoạn video đầu. Bộ kiểm tra chung 38 ca của đợt này có 37 đạt; một ca nguồn C thuộc kho FilmFree bị chặn bởi Cloudflare ở CDN, không phải ca KRX.

Chưa thử UI hoặc phát dài trong Cloudstream trên điện thoại/TV vì ADB không có thiết bị kết nối. Kết quả kiểm tra xác nhận giao thức và khả năng tải đoạn đầu, không bảo đảm mọi video của website đều còn hoạt động.

Source: workspace Codex `build/filmfree-repair-20261119/source/KRX18Provider`, phần giao thức dùng chung ở `shared/src/main/kotlin/com/tnb88/filmfree/KrxProtocol.kt`. Bản cũ và ghi chú trước đây được giữ lại.
