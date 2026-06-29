# Hướng dẫn chạy dự án Apartment Management System

## Yêu cầu môi trường

- **Netbeans 13+** (hoặc IDE Java hỗ trợ Java EE)
- **Apache Tomcat 10+** (Jakarta EE Web Profile)
- **SQL Server** đã cài đặt và chạy
- **SQL Server JDBC Driver** (đã có trong project)

## Các bước chạy dự án

### Bước 1: Mở dự án trong Netbeans

1. Mở Netbeans
2. Chọn **File** → **Open Project**
3. Dẫn đến thư mục dự án `D:\PRJ301_Asg`
4. Chọn folder và click **Open Project**

### Bước 2: Cấu hình database

1. Mở SQL Server Management Studio
2. Kết nối tới server với thông tin:
   - Server: `localhost`
   - Authentication: `sa` / password: `123` (xem trong `DBConnection.java`)
3. Chạy script `sql/SQL.sql` để tạo database và dữ liệu mẫu

### Bước 3: Chạy dự án

1. Trong Netbeans, click chuột phải vào project
2. Chọn **Run** (hoặc nhấn F6)
3. Trình duyệt sẽ mở tại `http://localhost:8080/PRJ301_Asg/login`

### Bước 4: Đăng nhập Manager

| Username | Password | Vai trò |
|----------|----------|---------|
| manager_tb | hash_123 | Manager |

### Bước 5: Test SF3 theo trình tự

| Bước | URL | Hành động |
|------|-----|-----------|
| 1 | `/manager/staffs` | Xem danh sách nhân viên |
| 2 | `/manager/staffs/add` | Thêm nhân viên mới |
| 3 | `/manager/staffs/edit?userId=4` | Sửa nhân viên staff_bt |
| 4 | `/manager/pendingRequests` | Xem yêu cầu chờ duyệt (requestId=4, 17) |
| 5 | `/manager/requestDetail?requestId=4` | Xem chi tiết → Phân công staff |
| 6 | `/manager/notifications/create` | Tạo thông báo mới |
| 7 | `/manager/notifications` | Xem danh sách thông báo đã gửi |

---

## Cấu trúc thư mục

```
src/java/
├── controller/
│   ├── Manager/
│   │   ├── ManagerDashboardServlet.java
│   │   ├── ApartmentListServlet.java
│   │   ├── AddApartmentServlet.java
│   │   ├── StaffListServlet.java          # SF3 - Staff List
│   │   ├── AddStaffServlet.java           # SF3 - Add Staff
│   │   ├── UpdateStaffServlet.java        # SF3 - Update Staff
│   │   ├── PendingRequestsServlet.java    # SF3 - Pending Requests
│   │   ├── ManagerRequestDetailServlet.java # SF3 - Request Detail
│   │   ├── AssignStaffServlet.java        # SF3 - Assign Staff
│   │   ├── CreateNotificationServlet.java # SF3 - Create Notification
│   │   └── ManagerNotificationsServlet.java # SF3 - Notification List
│   ├── Staff/
│   └── Resident/
├── dal/
│   ├── StaffDAO.java
│   ├── PositionDAO.java
│   ├── AssignmentDAO.java
│   └── NotificationDAO.java
└── model/
    ├── StaffProfile.java
    ├── Position.java
    ├── Request.java
    └── Notification.java

web/manager/
├── dashboard.jsp
├── apartments.jsp
├── addApartment.jsp
├── staffs.jsp               # SF3 - Staff List JSP
├── addStaff.jsp             # SF3 - Add Staff JSP
├── updateStaff.jsp          # SF3 - Update Staff JSP
├── pendingRequests.jsp      # SF3 - Pending Requests JSP
├── requestDetail.jsp        # SF3 - Request Detail JSP
├── createNotification.jsp   # SF3 - Create Notification JSP
└── notifications.jsp        # SF3 - Notification List JSP
```