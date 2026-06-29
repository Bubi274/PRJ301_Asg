<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bảng điều khiển Manager - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* Glassmorphism theme */
        body { 
            margin: 0; 
            min-height: 100vh; 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            color: white; 
            background: linear-gradient(rgba(0, 50, 60, 0.8), rgba(0, 30, 45, 0.9)), url('https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?q=80&w=2070&auto=format&fit=crop'); 
            background-size: cover; 
            background-position: center; 
            background-attachment: fixed;
        }
        
        header { 
            padding: 20px 40px; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
            background: rgba(0, 0, 0, 0.2); 
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .logo { 
            font-size: 1.4rem; 
            font-weight: bold; 
            letter-spacing: 1px; 
            color: white; 
            display: flex;
            align-items: center;
        }
        
        .logo i { 
            margin-right: 10px; 
            color: #00d8ff; 
            font-size: 1.6rem;
        }
        
        .user-info { 
            font-size: 1rem; 
            color: rgba(255, 255, 255, 0.9); 
        }
        
        .user-info a { 
            color: #ff5b5b; 
            text-decoration: none; 
            margin-left: 20px; 
            font-weight: bold; 
            transition: color 0.3s;
        }
        
        .user-info a:hover { 
            color: #ff3333; 
        }
        
        .container { 
            max-width: 1200px; 
            margin: 45px auto; 
            padding: 40px; 
            border-radius: 16px; 
            background: rgba(255, 255, 255, 0.08); 
            backdrop-filter: blur(15px); 
            -webkit-backdrop-filter: blur(15px); 
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); 
            border: 1px solid rgba(255, 255, 255, 0.15); 
        }
        
        h2 { 
            font-size: 1.2rem; 
            color: #00d8ff; 
            text-transform: uppercase;
            letter-spacing: 2px;
            margin: 0 0 10px 0; 
        }
        
        h1 { 
            font-size: 2.5rem; 
            margin: 0 0 35px 0; 
            color: white; 
            font-weight: 300;
        }
        
        /* Stats Grid */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }
        
        .stat-card {
            padding: 25px 20px;
            border-radius: 12px;
            background: rgba(0, 0, 0, 0.25);
            border: 1px solid rgba(255, 255, 255, 0.08);
            display: flex;
            align-items: center;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.3);
            border-color: rgba(0, 216, 255, 0.3);
        }
        
        .stat-icon {
            font-size: 2.2rem;
            color: #00d8ff;
            margin-right: 20px;
            background: rgba(0, 216, 255, 0.1);
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .stat-card.occupied .stat-icon { color: #2ecc71; background: rgba(46, 204, 113, 0.1); }
        .stat-card.vacant .stat-icon { color: #f1c40f; background: rgba(241, 196, 15, 0.1); }
        .stat-card.repair .stat-icon { color: #e74c3c; background: rgba(231, 76, 60, 0.1); }
        .stat-card.requests .stat-icon { color: #9b59b6; background: rgba(155, 89, 182, 0.1); }
        
        .stat-info span {
            display: block;
            font-size: 0.9rem;
            color: rgba(255, 255, 255, 0.6);
            margin-bottom: 5px;
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        
        .stat-info strong {
            font-size: 1.8rem;
            color: white;
        }
        
        /* Action Menu */
        .menu-title {
            font-size: 1.4rem;
            margin: 0 0 20px 0;
            font-weight: 400;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            padding-bottom: 10px;
        }
        
        .action-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
        }
        
        .action-card {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 12px;
            padding: 30px;
            text-decoration: none;
            color: white;
            transition: all 0.3s;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        
        .action-card:hover {
            background: rgba(0, 216, 255, 0.08);
            border-color: #00d8ff;
            transform: scale(1.02);
            box-shadow: 0 8px 24px rgba(0, 216, 255, 0.15);
        }
        
        .action-card i {
            font-size: 2.5rem;
            color: #00d8ff;
            margin-bottom: 20px;
        }
        
        .action-card h3 {
            margin: 0 0 10px 0;
            font-size: 1.5rem;
            font-weight: 500;
        }
        
        .action-card p {
            margin: 0;
            font-size: 0.95rem;
            color: rgba(255, 255, 255, 0.7);
            line-height: 1.5;
        }
    </style>
</head>
<body>

<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info">
        Chào mừng, <b>${sessionScope.fullName}</b> (Quản lý)
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i> Đăng xuất</a>
    </div>
</header>

<div class="container">
    <h2>Apartment Manager Control Panel</h2>
    <h1>Bảng điều khiển Quản lý</h1>
    
    <!-- Stats Cards -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon"><i class="fa-solid fa-hotel"></i></div>
            <div class="stat-info">
                <span>Tổng căn hộ</span>
                <strong>${totalApartments}</strong>
            </div>
        </div>
        <div class="stat-card occupied">
            <div class="stat-icon"><i class="fa-solid fa-house-user"></i></div>
            <div class="stat-info">
                <span>Căn hộ đã có chủ</span>
                <strong>${occupiedApartments}</strong>
            </div>
        </div>
        <div class="stat-card vacant">
            <div class="stat-icon"><i class="fa-solid fa-door-open"></i></div>
            <div class="stat-info">
                <span>Căn hộ trống</span>
                <strong>${vacantApartments}</strong>
            </div>
        </div>
        <div class="stat-card repair">
            <div class="stat-icon"><i class="fa-solid fa-hammer"></i></div>
            <div class="stat-info">
                <span>Đang bảo trì</span>
                <strong>${repairApartments}</strong>
            </div>
        </div>
        <div class="stat-card requests">
            <div class="stat-icon"><i class="fa-solid fa-envelope-open-text"></i></div>
            <div class="stat-info">
                <span>Yêu cầu chờ xử lý</span>
                <strong>${pendingRequests}</strong>
            </div>
        </div>
    </div>
    
    <!-- Quick Actions -->
    <h2 class="menu-title">Chức năng quản trị</h2>
    <div class="action-grid">
        <a href="${pageContext.request.contextPath}/manager/apartments" class="action-card">
            <div>
                <i class="fa-solid fa-list-check"></i>
                <h3>Quản lý căn hộ</h3>
                <p>Xem danh sách toàn bộ căn hộ, tìm kiếm theo số phòng/chủ hộ, lọc theo trạng thái và tầng.</p>
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/manager/addApartment" class="action-card">
            <div>
                <i class="fa-solid fa-plus-minus"></i>
                <h3>Thêm căn hộ mới</h3>
                <p>Tạo mới căn hộ, cấu hình diện tích, phân tầng và gán cư dân sở hữu trực tiếp vào hệ thống.</p>
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/manager/staffs" class="action-card">
            <div>
                <i class="fa-solid fa-users-gear"></i>
                <h3>Quản lý nhân viên</h3>
                <p>Thêm, sửa, vô hiệu hóa hồ sơ nhân viên kỹ thuật và vị trí công việc.</p>
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/manager/pendingRequests" class="action-card">
            <div>
                <i class="fa-solid fa-paper-plane"></i>
                <h3>Phân công yêu cầu</h3>
                <p>Xem yêu cầu chờ duyệt và phân công cho nhân viên phù hợp.</p>
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/manager/notifications" class="action-card">
            <div>
                <i class="fa-solid fa-bullhorn"></i>
                <h3>Gửi thông báo</h3>
                <p>Tạo và gửi thông báo chung tới cư dân và nhân viên.</p>
            </div>
        </a>
    </div>
</div>

</body>
</html>
