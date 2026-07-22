# Tích hợp Tripadvisor API cho phần Khách sạn

Kế hoạch này thay thế việc sử dụng Overpass API hiện tại bằng Tripadvisor API (thông qua RapidAPI) để lấy dữ liệu khách sạn chính xác hơn, bao gồm giá cả, xếp hạng và ảnh.

## Proposed Changes

### 1. Networking Layer

#### [NEW] [TripadvisorApiService](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/TripadvisorApiService.kt)
Định nghĩa các endpoint:
- `searchLocation(query: String)`
- `getHotelsFilter(locationId: String)`
- `searchHotels(locationId: String, checkIn: String, checkOut: String, adults: Int, rooms: Int, ...)`

#### [NEW] [TripadvisorClient](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/TripadvisorClient.kt)
Cấu hình Retrofit với `BASE_URL` là `https://tripadvisor16.p.rapidapi.com/` và thêm Interceptor để tự động chèn các Header của RapidAPI.

### 2. Data Models

#### [NEW] [TripadvisorModels](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/TripadvisorModels.kt)
Tạo các data class Kotlin để map dữ liệu trả về từ Tripadvisor:
- `LocationResponse`: Chứa `locationId`.
- `FilterResponse`: Chứa các tiêu chí lọc.
- `HotelSearchResponse`: Chứa danh sách khách sạn với thông tin chi tiết (tên, giá, ảnh, rating).

### 3. Repository Layer

#### [NEW] [TripadvisorRepository](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/TripadvisorRepository.kt)
Lớp trung gian để xử lý logic gọi API:
1. Gọi `searchLocation` để lấy ID địa điểm.
2. Dùng ID đó gọi `searchHotels`.

### 4. UI Layer

#### [MODIFY] [HotelActivity.java](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/HotelActivity.java)
- Thay thế `OverpassRepository` bằng `TripadvisorRepository`.
- Cập nhật hàm `performSearch()` để gọi chuỗi API mới.

#### [MODIFY] [HotelAdapter.kt](file:///C:/Users/HariLaam/Downloads/do_an_app_du_lich-master/do_an_app_du_lich-master/app/src/main/java/com/example/doan1/HotelAdapter.kt)
- Thay đổi kiểu dữ liệu từ `List<Element>` sang `List<TripadvisorHotel>`.
- Cập nhật logic hiển thị ảnh (sử dụng Glide/Picasso) và giá cả.

## Verification Plan

### Automated Tests
- Kiểm tra Build project sau khi thêm các file mới.
- Kiểm tra logcat để xem dữ liệu JSON trả về từ Tripadvisor.

### Manual Verification
- Chạy ứng dụng, vào phần Khách sạn.
- Nhập tên địa điểm (ví dụ: "Hanoi").
- Kiểm tra danh sách khách sạn hiển thị có ảnh, tên và giá không.
