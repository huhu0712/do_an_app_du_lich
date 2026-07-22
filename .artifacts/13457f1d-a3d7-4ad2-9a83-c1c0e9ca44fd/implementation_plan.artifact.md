# Cập nhật chọn lọc lên GitHub (Khách sạn, Máy bay, Nhà hàng)

Kế hoạch này nhằm cập nhật chỉ các thành phần liên quan đến Khách sạn, Vé máy bay và Nhà hàng lên GitHub, đồng thời khôi phục/giữ nguyên các thành phần khác (bao gồm cả tính năng Vé tàu) về trạng thái trước đó trên GitHub.

## Câu hỏi cần xác nhận
- [ ] Bạn có muốn khôi phục lại nút **"Vé tàu"** trên GitHub không? (Nếu giữ nguyên GitHub cũ thì nút này sẽ quay lại).
- [ ] Các phần "còn lại" bạn muốn giữ nguyên là bao gồm cả các file cấu hình như `build.gradle`, `.idea`, hay chỉ là giao diện màn hình chính?

## Các thay đổi đề xuất

### [Component: GitHub Sync]

#### [MODIFY] Selective Push
- Chỉ commit và push các file thuộc 3 module:
    - **Hotel:** `Hotel.java`, `HotelActivity.java`, `HotelAdapter.kt`, `HotelResponse.java`, `activity_hotel.xml`, `item_hotel.xml`, `HotelDetailActivity.java`, `SerpHotelModels.kt`, `activity_hotel_detail.xml`, `SerpHotelRepository.kt`.
    - **Flight:** `FlightActivity.java`, `item_flight.xml`, `activity_flight.xml`, `FlightAdapter.kt`.
    - **Restaurant:** `RestaurantActivity.kt`, `item_restaurant.xml`, `activity_restaurant.xml`, `RestaurantAdapter.kt`.

#### [REVERT] Shared Files (Nếu cần)
- Khôi phục `activity_main.xml`, `MainActivity.java`, `AndroidManifest.xml` về trạng thái có "Vé tàu" để đồng bộ với yêu cầu "giữ nguyên phần còn lại của GitHub".

## Kế hoạch xác minh
- Kiểm tra lịch sử commit trên GitHub để đảm bảo chỉ các file liên quan được thay đổi.
- Đảm bảo dự án vẫn build thành công cục bộ (với giao diện 3 nút) và GitHub (với giao diện cũ).
