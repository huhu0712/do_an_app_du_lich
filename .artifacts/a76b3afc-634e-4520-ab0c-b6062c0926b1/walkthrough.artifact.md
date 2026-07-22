# Walkthrough - Thêm Activity và Layout Nhà hàng

Tôi đã hoàn thành việc thêm tính năng Nhà hàng vào ứng dụng, với giao diện được thiết kế tương đồng với phần Khách sạn theo yêu cầu của bạn.

## Các thay đổi chính

### 1. Giao diện (Layout)
- **[activity_restaurant.xml](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/res/layout/activity_restaurant.xml)**: Được tạo mới dựa trên cấu trúc của `activity_hotel.xml`. Bao gồm:
    - Header với nút quay lại.
    - Card tìm kiếm với các mục: Địa điểm, Ngày tháng, Giờ ăn và Số người.
    - Nút tìm kiếm và nút mở bản đồ.

### 2. Mã nguồn Java
- **[RestaurantActivity.java](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/RestaurantActivity.java)**: Activity điều khiển màn hình nhà hàng, xử lý sự kiện nút quay lại.
- **[MainActivity.java](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/MainActivity.java)**: Đã thêm sự kiện click cho nút `btnRestaurant` để mở `RestaurantActivity`.

### 3. Cấu hình hệ thống
- **[AndroidManifest.xml](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/AndroidManifest.xml)**: Đã đăng ký `RestaurantActivity` để ứng dụng có thể khởi chạy màn hình này.

## Kiểm tra
Bạn có thể chạy ứng dụng, tại màn hình chính nhấn vào nút **"Nhà hàng"** để xem giao diện mới.
