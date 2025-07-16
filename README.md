# SPENDWISE - ỨNG DỤNG QUẢN LÝ CHI TIÊU

## TỔNG QUAN DỰ ÁN

**SpendWise** là một ứng dụng Android được thiết kế để giúp người dùng quản lý chi tiêu cá nhân một cách hiệu quả và thông minh. Ứng dụng cung cấp các công cụ để theo dõi thu chi, phân tích thói quen tiêu dùng và lập kế hoạch tài chính cá nhân.

## MỤC TIÊU DỰ ÁN

- **Mục tiêu chính**: Tạo ra một công cụ đơn giản nhưng mạnh mẽ để quản lý tài chính cá nhân
- **Đối tượng người dùng**: Cá nhân muốn kiểm soát chi tiêu và tiết kiệm hiệu quả
- **Giải pháp**: Ứng dụng mobile với giao diện thân thiện, dễ sử dụng

## CÔNG NGHỆ SỬ DỤNG

### Nền tảng phát triển
- **IDE**: Android Studio
- **Ngôn ngữ**: Java/Kotlin
- **Platform**: Android (API Level 21+)

### Cơ sở dữ liệu
- **Database**: SQLite
- **ORM**: Room Database (Android Architecture Components)

### Kiến trúc ứng dụng
- **Pattern**: MVVM (Model-View-ViewModel)
- **Components**: 
  - LiveData
  - ViewModel
  - Repository Pattern
  - Data Binding

## TÍNH NĂNG CHÍNH

### 1. Quản lý giao dịch
- **Thêm giao dịch**: Thu nhập và chi tiêu
- **Phân loại**: Danh mục chi tiêu (ăn uống, di chuyển, giải trí, etc.)
- **Ghi chú**: Mô tả chi tiết cho từng giao dịch
- **Ảnh đính kèm**: Chụp hóa đơn, biên lai

### 2. Theo dõi ngân sách
- **Thiết lập ngân sách**: Theo tháng/tuần cho từng danh mục
- **Cảnh báo**: Thông báo khi sắp vượt ngân sách
- **Theo dõi tiến độ**: Hiển thị tỷ lệ chi tiêu so với ngân sách

### 3. Báo cáo và thống kê
- **Tổng thu nhập**: Hiển thị tổng thu nhập theo tháng, năm
- **Tổng chi tiêu**: Hiển thị tổng chi tiêu theo tháng, năm
- **Lọc theo thời gian**: Xem báo cáo theo tháng, năm cụ thể
- **Số dư**: Hiển thị số dư hiện tại (thu - chi)

## CẤU TRÚC CƠ SỞ DỮ LIỆU

### Bảng chính
1. **transactions** - Lưu thông tin giao dịch
2. **categories** - Danh mục thu chi
3. **budgets** - Ngân sách theo danh mục

### Quan hệ
- Transaction → Category (Many-to-One)
- Budget → Category (One-to-One)

## GIAO DIỆN NGƯỜI DÙNG

### Màn hình chính
- **Dashboard**: Tổng quan tình hình tài chính
- **Quick Add**: Nút thêm giao dịch nhanh
- **Recent Transactions**: Danh sách giao dịch gần đây

### Navigation
- **Bottom Navigation**: 4 tab chính
  1. Home (Dashboard)
  2. Transactions (Lịch sử giao dịch)
  3. Add (Thêm giao dịch)
  4. Reports (Báo cáo)

## TÍNH NĂNG TƯƠNG LAI

### Version 2.0
- **Quản lý tài khoản**: Nhiều tài khoản (tiền mặt, ngân hàng, ví điện tử)
- **Chuyển khoản**: Giao dịch giữa các tài khoản
- **Mục tiêu tiết kiệm**: Đặt mục tiêu và theo dõi tiến độ
- **Cloud Sync**: Đồng bộ dữ liệu nhiều thiết bị
- **Export/Import**: Xuất dữ liệu ra Excel, CSV
- **Widget**: Widget cho home screen
- **Dark Mode**: Giao diện tối

### Version 3.0
- **Biểu đồ nâng cao**: Pie chart, Bar chart cho chi tiêu theo danh mục
- **Phân tích xu hướng**: Phân tích thói quen chi tiêu theo thời gian
- **So sánh**: So sánh chi tiêu giữa các tháng
- **AI Insights**: Phân tích thông minh chi tiêu
- **Voice Input**: Thêm giao dịch bằng giọng nói
- **Sharing**: Chia sẻ báo cáo với gia đình
- **Multi-language**: Hỗ trợ đa ngôn ngữ 