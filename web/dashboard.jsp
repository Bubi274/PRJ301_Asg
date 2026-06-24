<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bảng điều khiển Admin - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* Glassmorphism theme */
        body { margin: 0; min-height: 100vh; font-family: Arial, sans-serif; color: white; background: linear-gradient(rgba(0,60,70,0.8), rgba(0,40,60,0.85)), url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop'); background-size: cover; background-position: center; }
        header { padding: 25px 40px; display: flex; justify-content: space-between; align-items: center; }
        .logo { font-size: 1.2rem; font-weight: bold; letter-spacing: 1px; color: white; }
        .logo i { margin-right: 8px; color: #00d8ff; }
        .user-info { font-size: 1rem; color: rgba(255,255,255,0.9); }
        .user-info a { color: #00d8ff; text-decoration: none; margin-left: 15px; font-weight: bold; }
        .user-info a:hover { color: #00bce0; }
        
        .container { max-width: 900px; margin: 40px auto; padding: 40px; border-radius: 16px; background: rgba(255,255,255,0.15); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); border: 1px solid rgba(255, 255, 255, 0.2); }
        h1 { font-size: 36px; margin: 0 0 20px 0; color: white; }
        h2 { font-size: 24px; color: #00d8ff; margin-bottom: 20px; }
        
        .stats-card { display: inline-block; padding: 20px 30px; border-radius: 12px; background: rgba(0,0,0,0.2); border: 1px solid rgba(255,255,255,0.1); margin-bottom: 30px; }
        .stats-card span { display: block; font-size: 14px; color: rgba(255,255,255,0.7); text-transform: uppercase; letter-spacing: 1px; margin-bottom: 5px; }
        .stats-card strong { font-size: 32px; color: #00d8ff; }

        .btn { display: inline-block; padding: 15px 25px; border: none; border-radius: 8px; background: #00d8ff; color: white; font-size: 16px; font-weight: bold; cursor: pointer; transition: background 0.3s; text-decoration: none; }
        .btn:hover { background: #00bce0; }
        .btn i { margin-right: 8px; }
    </style>
</head>
<body>

<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info">
        Xin chào, <b><%= session.getAttribute("fullName") %></b>
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i> Đăng xuất</a>
    </div>
</header>

<div class="container">
    <h2>Lumina BMS Control Panel</h2>
    <h1>Bảng điều khiển Admin</h1>
    
    <div class="stats-card">
        <span>Tổng số tài khoản trong hệ thống</span>
        <strong><%= request.getAttribute("totalUsers") %></strong>
    </div>
    
    <br>
    
    <a class="btn" href="${pageContext.request.contextPath}/admin/accounts"><i class="fa-solid fa-users"></i> Quản lý tài khoản hệ thống</a>
</div>

</body>
</html>
