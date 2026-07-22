# Xóa bỏ phần Vé Tàu

Kế hoạch này nhằm loại bỏ hoàn toàn tính năng "Vé Tàu" khỏi ứng dụng, bao gồm giao diện người dùng, mã nguồn xử lý và các khai báo liên quan.

## Các thay đổi đề xuất

### [Component: UI]

#### [MODIFY] [activity_main.xml](file:///C:/Users/HOANG/Downloads/do_an_app_du_lich-master%20(2)/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/res/layout/activity_main.xml)
- Xóa bỏ khối `LinearLayout` có `android:id="@+id/btnTrain"`.

#### [DELETE] [activity_train.xml](file:///C:/Users/HOANG/Downloads/do_an_app_du_lich-master%20(2)/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/res/layout/activity_train.xml)
- Xóa file layout của màn hình vé tàu.

#### [DELETE] [cc.xml](file:///C:/Users/HOANG/Downloads/do_an_app_du_lich-master%20(2)/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/res/layout/cc.xml)
- Xóa file layout dư thừa này vì nó không được sử dụng và có chứa "Vé tàu".

### [Component: Logic]

#### [MODIFY] [MainActivity.java](file:///C:/Users/HOANG/Downloads/do_an_app_du_lich-master%20(2)/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/MainActivity.java)
- Xóa phần ánh xạ `btnTrain` và sự kiện click listener mở `TrainActivity`.

#### [DELETE] [TrainActivity.java](file:///C:/Users/HOANG/Downloads/do_an_app_du_lich-master%20(2)/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/TrainActivity.java)
- Xóa file Activity xử lý màn hình vé tàu.

### [Component: Manifest]

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/HOANG/Downloads/do_an_app_du_lich-master%20(2)/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/AndroidManifest.xml)
- Xóa khai báo `<activity android:name=".TrainActivity" />`.

## Kế hoạch xác minh

### Kiểm tra tự động
- Chạy lệnh build để đảm bảo không còn lỗi tham chiếu đến các file đã xóa.

### Kiểm tra thủ công
- Chạy ứng dụng và kiểm tra xem nút "Vé Tàu" đã biến mất khỏi màn hình chính chưa.
- Đảm bảo các nút khác (Khách sạn, Vé Máy bay, Nhà hàng) vẫn hoạt động bình thường.
