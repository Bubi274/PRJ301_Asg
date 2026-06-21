USE master
GO
IF DB_ID('ApartmentManagement') IS NOT NULL
DROP DATABASE ApartmentManagement
GO
CREATE DATABASE ApartmentManagement
GO
USE ApartmentManagement
GO
--------------------------------------------------
-- 1. ROLES
--------------------------------------------------
CREATE TABLE Roles (
RoleId INT PRIMARY KEY IDENTITY(1,1),
RoleName NVARCHAR(50) UNIQUE NOT NULL
)
-- 1. Bật chế độ cho phép chèn giá trị thủ công vào cột IDENTITY
SET IDENTITY_INSERT [Roles] ON;
GO
-- 2. Chèn dữ liệu theo đúng các hàng trong ảnh
INSERT INTO [Roles] ([RoleId], [RoleName]) VALUES (1, 'Admin');
INSERT INTO [Roles] ([RoleId], [RoleName]) VALUES (2, 'Manager');
INSERT INTO [Roles] ([RoleId], [RoleName]) VALUES (4, 'Resident');
INSERT INTO [Roles] ([RoleId], [RoleName]) VALUES (3, 'Staff');
GO
-- 3. Tắt chế độ chèn thủ công để trả lại trạng thái tự động tăng cho các bản ghi sau
SET IDENTITY_INSERT [Roles] OFF;
GO
--------------------------------------------------
-- 2. USERS
--------------------------------------------------
CREATE TABLE Users (
UserId INT PRIMARY KEY IDENTITY(1,1),
Username NVARCHAR(50) UNIQUE,
PasswordHash NVARCHAR(255),
FullName NVARCHAR(100),
Email NVARCHAR(100),
Phone NVARCHAR(20),
RoleId INT,
IsActive BIT,
CreatedAt DATETIME DEFAULT GETDATE()
)
GO
--------------------------------------------------
-- 3. POSITIONS
--------------------------------------------------
CREATE TABLE Positions (
PositionId INT PRIMARY KEY IDENTITY(1,1),
PositionName NVARCHAR(100),
Description NVARCHAR(255)
)
GO
--------------------------------------------------
-- 4. STAFF PROFILES
--------------------------------------------------
CREATE TABLE StaffProfiles (
Id INT PRIMARY KEY IDENTITY(1,1),
UserId INT,
PositionId INT
)
GO
--------------------------------------------------
-- 5. SERVICE TYPES
--------------------------------------------------
CREATE TABLE ServiceTypes (
ServiceTypeId INT PRIMARY KEY IDENTITY(1,1),
ServiceName NVARCHAR(100),
Unit NVARCHAR(50),
PricePerUnit DECIMAL(18,2)
)
GO
--------------------------------------------------
-- 6. APARTMENTS
--------------------------------------------------
CREATE TABLE Apartments (
ApartmentId INT PRIMARY KEY IDENTITY(1,1),
ApartmentNumber NVARCHAR(20),
Floor INT,
Area DECIMAL(10,2),
Types NVARCHAR(20),
Status NVARCHAR(20),
OwnerId INT
)
GO
--------------------------------------------------
-- 7. APARTMENT RESIDENTS
--------------------------------------------------
CREATE TABLE ApartmentResidents (
Id INT PRIMARY KEY IDENTITY(1,1),
ApartmentId INT,
UserId INT,
ResidentType NVARCHAR(30),
MoveInDate DATETIME,
MoveOutDate DATETIME,
IsActive BIT
)
GO
--------------------------------------------------
-- 8. REQUEST TYPES
--------------------------------------------------
CREATE TABLE RequestTypes (
RequestTypeId INT PRIMARY KEY IDENTITY(1,1),
TypeName NVARCHAR(100),
DefaultPositionId INT
)
GO
--------------------------------------------------
-- 9. REQUESTS
--------------------------------------------------
CREATE TABLE Requests (
RequestId INT PRIMARY KEY IDENTITY(1,1),
ResidentId INT,
ApartmentId INT,
RequestTypeId INT,
Title NVARCHAR(200),
Description NVARCHAR(200),
TransferDate DATETIME,
Status NVARCHAR(50),
CreatedAt DATETIME DEFAULT GETDATE(),
ApprovedBy INT,
AssignedTo INT
)
GO
--------------------------------------------------
-- 10. REQUEST LOGS
--------------------------------------------------
CREATE TABLE RequestLogs (
LogId INT PRIMARY KEY IDENTITY(1,1),
RequestId INT,
UpdatedBy INT,
OldStatus NVARCHAR(50),
NewStatus NVARCHAR(50),
UpdatedAt DATETIME DEFAULT GETDATE()
)
GO
--------------------------------------------------
-- 11. BILLS
--------------------------------------------------
CREATE TABLE Bills (
BillId INT PRIMARY KEY IDENTITY(1,1),
ApartmentId INT,
BillingMonth INT,
BillingYear INT,
TotalAmount DECIMAL(18,2),
Status NVARCHAR(30),
CreatedAt DATETIME
)
GO
CREATE TABLE BillDetails (
BillDetailId INT PRIMARY KEY IDENTITY(1,1),
BillId INT,
ServiceTypeId INT,
Quantity DECIMAL(18,2),
Amount DECIMAL(18,2)
)
GO
CREATE TABLE Payments (
PaymentId INT PRIMARY KEY IDENTITY(1,1),
BillId INT,
PaymentMethod NVARCHAR(50),
PaymentDate DATETIME,
Amount DECIMAL(18,2),
TransactionCode NVARCHAR(100),
Status NVARCHAR(30)
)
GO
--------------------------------------------------
-- 12. NOTIFICATIONS
--------------------------------------------------
CREATE TABLE Notifications (
NotificationId INT PRIMARY KEY IDENTITY(1,1),
Title NVARCHAR(200),
Content NVARCHAR(MAX),
Type NVARCHAR(50),
SenderId INT,
ReceiverId INT,
ApartmentId INT,
IsRead BIT DEFAULT 0,
CreatedAt DATETIME DEFAULT GETDATE(),
IsGlobal BIT DEFAULT 0
)
GO
--------------------------------------------------
-- 13. RESIDENT SERVICES
--------------------------------------------------
CREATE TABLE ResidentServices (
ResidentServiceId INT PRIMARY KEY IDENTITY(1,1),
ResidentId INT,
ServiceTypeId INT,
RegisteredAt DATETIME DEFAULT GETDATE(),
IsActive BIT DEFAULT 1,
UNIQUE (ResidentId, ServiceTypeId)
)
GO
--------------------------------------------------
-- FOREIGN KEYS
--------------------------------------------------
ALTER TABLE Users ADD FOREIGN KEY (RoleId) REFERENCES Roles(RoleId)
ALTER TABLE StaffProfiles ADD FOREIGN KEY (UserId) REFERENCES Users(UserId)
ALTER TABLE StaffProfiles ADD FOREIGN KEY (PositionId) REFERENCES Positions(PositionId)
ALTER TABLE Apartments ADD FOREIGN KEY (OwnerId) REFERENCES Users(UserId)
ALTER TABLE ApartmentResidents ADD FOREIGN KEY (ApartmentId) REFERENCES Apartments(ApartmentId)
ALTER TABLE ApartmentResidents ADD FOREIGN KEY (UserId) REFERENCES Users(UserId)
ALTER TABLE RequestTypes ADD FOREIGN KEY (DefaultPositionId) REFERENCES Positions(PositionId)
ALTER TABLE Requests ADD FOREIGN KEY (ResidentId) REFERENCES Users(UserId)
ALTER TABLE Requests ADD FOREIGN KEY (ApartmentId) REFERENCES Apartments(ApartmentId)
ALTER TABLE Requests ADD FOREIGN KEY (RequestTypeId) REFERENCES RequestTypes(RequestTypeId)
ALTER TABLE Requests ADD FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId)
ALTER TABLE Requests ADD FOREIGN KEY (AssignedTo) REFERENCES Users(UserId)
ALTER TABLE RequestLogs ADD FOREIGN KEY (RequestId) REFERENCES Requests(RequestId)
ALTER TABLE RequestLogs ADD FOREIGN KEY (UpdatedBy) REFERENCES Users(UserId)
ALTER TABLE Bills ADD FOREIGN KEY (ApartmentId) REFERENCES Apartments(ApartmentId)
ALTER TABLE BillDetails ADD FOREIGN KEY (BillId) REFERENCES Bills(BillId)
ALTER TABLE BillDetails ADD FOREIGN KEY (ServiceTypeId) REFERENCES ServiceTypes(ServiceTypeId)
ALTER TABLE Payments ADD FOREIGN KEY (BillId) REFERENCES Bills(BillId)
ALTER TABLE Notifications ADD FOREIGN KEY (SenderId) REFERENCES Users(UserId)
ALTER TABLE Notifications ADD FOREIGN KEY (ReceiverId) REFERENCES Users(UserId)
ALTER TABLE Notifications ADD FOREIGN KEY (ApartmentId) REFERENCES Apartments(ApartmentId)
ALTER TABLE ResidentServices ADD FOREIGN KEY (ResidentId) REFERENCES Users(UserId)
ALTER TABLE ResidentServices ADD FOREIGN KEY (ServiceTypeId) REFERENCES ServiceTypes(ServiceTypeId)
GO
SET DATEFORMAT YMD;
-- 1. POSITIONS (10 Vị trí nhân viên)
INSERT INTO [Positions] ([PositionName], [Description]) VALUES
(N'Giám đốc tòa nhà', N'Quản lý chung'),
(N'Trưởng ban quản lý', N'Điều hành hàng ngày'),
(N'Kỹ sư trưởng', N'Quản lý hệ thống kỹ thuật'),
(N'Nhân viên bảo trì', N'Sửa chữa kỹ thuật'),
(N'Trưởng ca bảo vệ', N'Quản lý an ninh'),
(N'Nhân viên an ninh', N'Tuần tra'),
(N'Nhân viên lễ tân', N'Tiếp đón khách'),
(N'Kế toán trưởng', N'Quản lý tài chính'),
(N'Nhân viên kế toán', N'Thu phí dịch vụ'),
(N'Giám sát vệ sinh', N'Quản lý làm sạch');
GO
-- 2. REQUESTTYPES (Áp dụng DefaultPositionId để map công việc)
INSERT INTO [RequestTypes] ([TypeName], [DefaultPositionId]) VALUES
(N'Sửa chữa điện', 4), (N'Sửa chữa nước', 4), (N'Vệ sinh khu vực chung', 10),
(N'Phản ánh tiếng ồn', 6), (N'Đăng ký thi công', 2), (N'Đăng ký chuyển đồ', 7),
(N'Khiếu nại dịch vụ', 2), (N'Yêu cầu kiểm tra an ninh', 5),
(N'Đăng ký sử dụng tiện ích (BBQ)', 7), (N'Thắc mắc phí dịch vụ', 9);
GO
-- 3. USERS (TỔNG CỘNG 20 USERS)
-- => USER 1-10: NHÂN VIÊN (ROLE 1, 2, 3)
INSERT INTO [Users] ([Username], [PasswordHash], [FullName], [Email], [Phone],
[RoleId], [IsActive], [CreatedAt]) VALUES
('admin_gd', 'hash_123', N'Nguyễn Giám Đốc', 'gd@happyhouse.com', '0901111111',
1, 1, GETDATE()),
('manager_tb', 'hash_123', N'Trần Trưởng Ban', 'tb@happyhouse.com',
'0902222222', 2, 1, GETDATE()),
('staff_ks', 'hash_123', N'Lê Kỹ Sư', 'ks@happyhouse.com', '0903333333', 3, 1,
GETDATE()),
('staff_bt', 'hash_123', N'Phạm Bảo Trì', 'bt@happyhouse.com', '0904444444', 3, 1,
GETDATE()),
('staff_cbv', 'hash_123', N'Hoàng Trưởng Bảo Vệ', 'cbv@happyhouse.com',
'0905555555', 3, 1, GETDATE()),
('staff_an', 'hash_123', N'Vũ An Ninh', 'an@happyhouse.com', '0906666666', 3, 1,
GETDATE()),
('staff_lt', 'hash_123', N'Đặng Lễ Tân', 'lt@happyhouse.com', '0907777777', 3, 1,
GETDATE()),
('staff_ktt', 'hash_123', N'Bùi Kế Toán Trưởng', 'ktt@happyhouse.com', '0908888888',
3, 1, GETDATE()),
('staff_kt', 'hash_123', N'Ngô Kế Toán', 'kt@happyhouse.com', '0909999999', 3, 1,
GETDATE()),
('staff_vs', 'hash_123', N'Đỗ Vệ Sinh', 'vs@happyhouse.com', '0910101010', 3, 1,
GETDATE());
-- => USER 11-20: CƯ DÂN (ROLE 4)
INSERT INTO [Users] ([Username], [PasswordHash], [FullName], [Email], [Phone],
[RoleId], [IsActive], [CreatedAt]) VALUES
('resident_01', 'hash_123', N'Trương Cư Dân 1', 'res1@email.com', '0911000001', 4,
1, GETDATE()),
('resident_02', 'hash_123', N'Lâm Cư Dân 2', 'res2@email.com', '0911000002', 4, 1,
GETDATE()),
('resident_03', 'hash_123', N'Mai Cư Dân 3', 'res3@email.com', '0911000003', 4, 1,
GETDATE()),
('resident_04', 'hash_123', N'Đinh Cư Dân 4', 'res4@email.com', '0911000004', 4, 1,
GETDATE()),
('resident_05', 'hash_123', N'Lý Cư Dân 5', 'res5@email.com', '0911000005', 4, 1,
GETDATE()),
('resident_06', 'hash_123', N'Hà Cư Dân 6', 'res6@email.com', '0911000006', 4, 1,
GETDATE()),
('resident_07', 'hash_123', N'Phan Cư Dân 7', 'res7@email.com', '0911000007', 4, 1,
GETDATE()),
('resident_08', 'hash_123', N'Chu Cư Dân 8', 'res8@email.com', '0911000008', 4, 1,
GETDATE()),
('resident_09', 'hash_123', N'Cao Cư Dân 9', 'res9@email.com', '0911000009', 4, 1,
GETDATE()),
('resident_10', 'hash_123', N'Đoàn Cư Dân 10', 'res10@email.com', '0911000010', 4,
1, GETDATE());
GO
-- 4. STAFFPROFILES (Gán cho User ID 1 đến 10)
INSERT INTO [StaffProfiles] ([UserId], [PositionId]) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7), (8, 8), (9, 9), (10, 10);
GO
-- 5. SERVICETYPES
INSERT INTO [ServiceTypes] ([ServiceName], [Unit], [PricePerUnit]) VALUES
(N'Điện sinh hoạt', 'kWh', 3500), (N'Nước sinh hoạt', 'm3', 15000), (N'Phí quản lý chung cư', 'm2', 12000),
(N'Phí gửi xe máy', N'Chiếc', 100000), (N'Phí gửi ô tô', N'Chiếc', 1200000),
(N'Internet', N'Tháng', 250000),
(N'Vệ sinh riêng', N'Lần', 150000), (N'Sử dụng BBQ', N'Giờ', 50000), (N'Bể bơi', N'Lượt', 30000),
(N'Sửa chữa', N'Lần', 100000);
GO
-- 6. APARTMENTS (Sở hữu bởi Cư dân - User ID 11 đến 20)
INSERT INTO [Apartments] ([ApartmentNumber], [Floor], [Area], [Types], [Status],
[OwnerId]) VALUES
('A1-01', 1, 65.5, '2PN', 'Occupied', 11),
('A1-02', 1, 45.0, '1PN', 'Occupied', 12),
('A2-01', 2, 85.0, '3PN', 'Occupied', 13),
('A2-02', 2, 65.5, '2PN', 'Vacant', 14),
('A3-01', 3, 120.0, 'Penthouse', 'Occupied', 15),
('B1-01', 1, 50.0, 'Studio', 'Occupied', 16),
('B1-02', 1, 50.0, 'Studio', 'Vacant', 17),
('B2-01', 2, 70.0, '2PN', 'Occupied', 18),
('B2-02', 2, 70.0, '2PN', 'Under Repair', 19),
('B3-01', 3, 90.0, '3PN', 'Occupied', 20);
GO
-- 7. APARTMENTRESIDENTS (Là Cư dân - User ID 11 đến 20)
INSERT INTO [ApartmentResidents] ([ApartmentId], [UserId], [ResidentType],
[MoveInDate], [MoveOutDate], [IsActive]) VALUES
(1, 11, 'Owner', '2025-01-01', NULL, 1), (2, 12, 'Owner', '2025-01-15', NULL, 1),
(3, 13, 'Owner', '2025-02-01', NULL, 1), (4, 14, 'Owner', '2025-02-10', NULL, 0),
(5, 15, 'Owner', '2025-03-01', NULL, 1), (6, 16, 'Owner', '2025-04-10', NULL, 1),
(7, 17, 'Owner', '2025-05-05', NULL, 0), (8, 18, 'Owner', '2025-06-02', NULL, 1),
(9, 19, 'Owner', '2025-07-02', NULL, 1), (10, 20, 'Owner', '2025-08-25', NULL, 1);
GO
-- 8. RESIDENTSERVICES (Dữ liệu bảng mới - Cư dân đăng ký dịch vụ định kỳ)
-- Đảm bảo không trùng cặp (ResidentId, ServiceTypeId)
INSERT INTO [ResidentServices] ([ResidentId], [ServiceTypeId], [RegisteredAt],
[IsActive]) VALUES
(11, 4, GETDATE(), 1), -- Cư dân 11 đăng ký gửi xe máy
(11, 6, GETDATE(), 1), -- Cư dân 11 đăng ký Internet
(12, 4, GETDATE(), 1), -- Cư dân 12 đăng ký gửi xe máy
(13, 5, GETDATE(), 1), -- Cư dân 13 đăng ký gửi ô tô
(14, 6, GETDATE(), 1), -- Cư dân 14 đăng ký Internet
(15, 5, GETDATE(), 1), -- Cư dân 15 đăng ký gửi ô tô
(16, 4, GETDATE(), 1), -- Cư dân 16 đăng ký gửi xe máy
(18, 6, GETDATE(), 1), -- Cư dân 18 đăng ký Internet
(19, 4, GETDATE(), 1), -- Cư dân 19 đăng ký gửi xe máy
(20, 5, GETDATE(), 1); -- Cư dân 20 đăng ký gửi ô tô
GO
-- 9. BILLS & BILLDETAILS & PAYMENTS
INSERT INTO [Bills] ([ApartmentId], [BillingMonth], [BillingYear], [TotalAmount],
[Status], [CreatedAt]) VALUES
(1, 2, 2026, 1500000, 'Paid', GETDATE()), (2, 2, 2026, 850000, 'Paid', GETDATE()),
(3, 2, 2026, 2200000, 'Unpaid', GETDATE()), (4, 2, 2026, 0, 'Cancelled', GETDATE()),
(5, 2, 2026, 3500000, 'Unpaid', GETDATE()), (6, 2, 2026, 1100000, 'Paid', GETDATE()),
(7, 2, 2026, 0, 'Cancelled', GETDATE()), (8, 2, 2026, 1800000, 'Overdue', GETDATE()),
(9, 2, 2026, 1300000, 'Paid', GETDATE()), (10, 2, 2026, 2500000, 'Unpaid', GETDATE());
INSERT INTO [BillDetails] ([BillId], [ServiceTypeId], [Quantity], [Amount]) VALUES
(1, 3, 65.5, 786000), (2, 1, 150, 525000), (3, 5, 1, 1200000), (4, 3, 0, 0),
(5, 3, 120.0, 1440000), (6, 4, 2, 200000), (7, 1, 0, 0), (8, 2, 20, 300000),(9, 6, 1, 250000), (10, 3, 90.0, 1080000);
INSERT INTO [Payments] ([BillId], [PaymentMethod], [PaymentDate], [Amount],
[TransactionCode], [Status]) VALUES
(1, 'Bank Transfer', '2026-02-05', 1500000, 'VN1', 'Success'), (2, 'Momo',
'2026-02-06', 850000, 'MM1', 'Success'),
(3, 'Bank Transfer', '2026-02-15', 2200000, 'VN2', 'Failed'), (4, 'Cash', NULL, 0,
NULL, 'Cancelled'),
(5, 'Credit Card', NULL, 3500000, NULL, 'Pending'), (6, 'Cash', '2026-02-10',
1100000, 'CS1', 'Success'),
(7, 'Bank Transfer', NULL, 0, NULL, 'Cancelled'), (8, 'Momo', NULL, 1800000, NULL,
'Pending'),
(9, 'Bank Transfer', '2026-02-12', 1300000, 'VN3', 'Success'), (10, 'Cash', NULL,
2500000, NULL, 'Pending');
GO
-- 10. REQUESTS (CHỈ CƯ DÂN TỪ ID 11-20 MỚI ĐƯỢC TẠO - GIAO VIỆC CHO NHÂN VIÊN 1-10)
INSERT INTO [Requests] ([ResidentId], [ApartmentId], [RequestTypeId], [Title],
[Description], [TransferDate], [Status], [CreatedAt], [ApprovedBy], [AssignedTo])
VALUES
(20, 10, 1, N'Sửa điện', N'Cháy bóng đèn', GETDATE(), 'Processing', GETDATE(), 2, 4),
(19, 9, 2, N'Sửa nước', N'Tắc bồn cầu', GETDATE(), 'Completed', GETDATE(), 2, 4),
(18, 8, 3, N'Dọn hành lang', N'Rác tầng 2', GETDATE(), 'Processing', GETDATE(), 2, 10),
(17, 7, 4, N'Tiếng ồn', N'Nhạc to', GETDATE(), 'Pending', GETDATE(), NULL, 6),
(16, 6, 5, N'Sửa nhà', N'Lát sàn', GETDATE(), 'Approved', GETDATE(), 1, 2),
(15, 5, 6, N'Chuyển giường', N'Dùng thang hàng', GETDATE(), 'Completed', GETDATE(), 2, 7),
(14, 4, 7, N'Thái độ nhân viên', N'Góp ý', GETDATE(), 'Processing', GETDATE(), 1, 2),
(13, 3, 8, N'Mất đồ', N'Check camera', GETDATE(), 'Completed', GETDATE(), 2, 5),
(12, 2, 9, N'Mượn bếp BBQ', N'Tối T7', GETDATE(), 'Approved', GETDATE(), 2, 7),
(11, 1, 10, N'Hỏi tiền điện', N'Tính sai số', GETDATE(), 'Completed', GETDATE(), 2, 9);
GO
-- 11. REQUESTLOGS
INSERT INTO [RequestLogs] ([RequestId], [UpdatedBy], [OldStatus], [NewStatus],
[UpdatedAt]) VALUES
(1, 2, 'Pending', 'Processing', GETDATE()), (2, 4, 'Processing', 'Completed',
GETDATE()),
(3, 2, 'Pending', 'Processing', GETDATE()), (4, 6, 'Pending', 'Pending', GETDATE()),
(5, 1, 'Pending', 'Approved', GETDATE()), (6, 7, 'Processing', 'Completed',
GETDATE()),
(7, 2, 'Pending', 'Processing', GETDATE()), (8, 5, 'Processing', 'Completed',
GETDATE()),
(9, 7, 'Pending', 'Approved', GETDATE()), (10, 9, 'Processing', 'Completed',
GETDATE());
GO
-- 12. NOTIFICATIONS (Cập nhật ReceiverId thành Cư dân 11-20)
INSERT INTO [Notifications] ([Title], [Content], [Type], [SenderId], [ReceiverId],
[ApartmentId], [IsRead], [CreatedAt], [IsGlobal]) VALUES
(N'Cắt điện', N'Bảo trì ngày 5/3', 'System', 1, NULL, NULL, 0, GETDATE(), 1),
(N'Thu phí', N'Hạn nộp mùng 10', 'Billing', 2, NULL, NULL, 0, GETDATE(), 1),
(N'Hóa đơn A1-01', N'Cần nộp 1.5M', 'Billing', 2, 11, 1, 1, GETDATE(), 0),
(N'Hóa đơn A1-02', N'Cần nộp 850k', 'Billing', 2, 12, 2, 0, GETDATE(), 0),
(N'Cập nhật yêu cầu', N'Đã sửa xong nước', 'Request', 4, 19, 9, 1, GETDATE(), 0),
(N'Phun muỗi', N'Tầng 1-3 cuối tuần', 'System', 1, NULL, NULL, 0, GETDATE(), 1),
(N'Duyệt BBQ', N'Tối T7 chốt nhé', 'Request', 7, 12, 2, 0, GETDATE(), 0),
(N'Cảnh báo nợ', N'Quá hạn rồi bạn', 'Alert', 2, 13, 3, 0, GETDATE(), 0),
(N'Chào mừng', N'Chào cư dân mới', 'System', 1, 20, 10, 1, GETDATE(), 0),
(N'Phản hồi', N'Đã check camera', 'Request', 5, 13, 3, 0, GETDATE(), 0);
GO
