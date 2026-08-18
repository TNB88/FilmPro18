# Ghi chú kỹ thuật — sửa 3 nguồn ngày 2026-08-18

## Phạm vi

- `Sextop1Provider`: version 15 → 16
- `VlxxProvider`: version 6 → 7
- `JavHDProvider`: version 6 → 7
- Không thay đổi `Krx18Provider`.

## Nguyên nhân và thay đổi

### Sextop1Provider

- Domain cũ `https://sextop1.cm` không còn là domain đang hoạt động.
- Chuyển toàn bộ base URL, Referer và poster header sang `https://sextop1.menu`.
- Giữ nguyên parser hiện có cho `article.dp-item`, trang chi tiết, API `/wp-json/sextop1/player/`, HLS và phụ đề ngoài.

### VlxxProvider

- URL lấy domain động `https://raw.githubusercontent.com/Datj0000/domain/refs/heads/main/vlxx.txt` trả về HTTP 404, làm provider không xác định được server.
- Bỏ phụ thuộc runtime vào file domain ngoài và dùng trực tiếp `https://vlxx.ms`.
- Giữ nguyên danh sách `div#video-list > div.video-item`, API `/ajax.php`, iframe `play.vlstream.net` và resolver HLS.

### JavHDProvider

- URL lấy domain động `https://raw.githubusercontent.com/Datj0000/domain/refs/heads/main/javhd.txt` trả về HTTP 404, làm provider không xác định được server.
- Bỏ phụ thuộc runtime vào file domain ngoài và dùng trực tiếp `https://javhdz.city`.
- Giữ nguyên danh sách `ul#movie-last-movie > li`, API `/ajax`, giải mã `window.atob(...)` và HLS.

### Metadata repo

- Đồng bộ version nhúng trong `manifest.json` với `plugins.json`.
- Cập nhật favicon domain và `fileSize` của ba gói.

## Kiểm thử

- Cả ba trang chủ trả về HTTP 200 tại domain mới.
- Selector danh sách và trang chi tiết của cả ba nguồn còn hợp lệ.
- Sextop1: API player trả HLS và phụ đề `.srt`.
- VLXX: `/ajax.php` trả iframe; player trả nguồn có Content-Type `application/x-mpegurl` và hỗ trợ HTTP Range.
- JavHD: `/ajax` trả payload Base64; giải mã được URL HLS `.m3u8`.
- Ba gói được apktool smali lại thành công, giải mã ngược kiểm tra được domain mới và version mới.
- Mỗi gói chỉ chứa đúng `classes.dex` và `manifest.json`.

## SHA-256

- `JavHDProvider.cs3`: `83D30F87EB5597EFFA87B1E742520A8CC1C7EC5BA450C6E02287F5ECA22A542A`
- `Sextop1Provider.cs3`: `3E99DEB457041112B28753E83287ABF634F4FC1B3884EBC9DBF3D503AD32A83C`
- `VlxxProvider.cs3`: `B1ED5C119265A29E015E2D0E8BC7E21C89D6FBF8794288CFDAC7890B998AC4B8`
