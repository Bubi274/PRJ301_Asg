<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Account List - Lumina BMS</title>
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
        
        .container { max-width: 1100px; margin: 40px auto; padding: 40px; border-radius: 16px; background: rgba(255,255,255,0.15); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); border: 1px solid rgba(255, 255, 255, 0.2); }
        .topbar { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.2); padding-bottom: 20px; margin-bottom: 20px; }
        h2 { font-size: 28px; color: white; margin: 0; }
        
        .btn { display: inline-block; padding: 10px 20px; border: none; border-radius: 6px; background: #00d8ff; color: white; font-size: 14px; font-weight: bold; cursor: pointer; transition: background 0.3s; text-decoration: none; }
        .btn:hover { background: #00bce0; }
        .btn-danger { background: rgba(231, 76, 60, 0.8); }
        .btn-danger:hover { background: rgba(231, 76, 60, 1); }
        .btn-secondary { background: rgba(255,255,255,0.2); }
        .btn-secondary:hover { background: rgba(255,255,255,0.3); }

        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { border-bottom: 1px solid rgba(255,255,255,0.1); padding: 12px 15px; text-align: left; }
        th { background: rgba(0,0,0,0.2); color: #00d8ff; font-weight: bold; text-transform: uppercase; font-size: 13px; letter-spacing: 1px; }
        tr:hover { background: rgba(255,255,255,0.05); }
        
        .badge-active { background: rgba(46, 204, 113, 0.2); color: #2ecc71; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; border: 1px solid rgba(46, 204, 113, 0.4); }
        .badge-disabled { background: rgba(231, 76, 60, 0.2); color: #e74c3c; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; border: 1px solid rgba(231, 76, 60, 0.4); }
        
        .actions { display: flex; gap: 8px; }
        .actions .btn { padding: 6px 12px; font-size: 12px; }
        
        .back-link { display: inline-block; margin-top: 30px; color: rgba(255,255,255,0.8); text-decoration: none; font-size: 14px; transition: color 0.3s; }
        .back-link:hover { color: #00d8ff; }
    </style>
</head>
<body>

<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info">
        Xin chào, <b>${sessionScope.fullName}</b>
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i> Đăng xuất</a>
    </div>
</header>

<div class="container">
    <div class="topbar">
        <h2>Danh sách tài khoản hệ thống</h2>
        <a class="btn" href="${pageContext.request.contextPath}/admin/addAccount"><i class="fa-solid fa-user-plus"></i> Thêm tài khoản mới</a>
    </div>

    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Last Login</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="u" items="${users}">
            <tr>
                <td>${u.userId}</td>
                <td><strong>${u.username}</strong></td>
                <td>${u.fullName}</td>
                <td>${u.email}</td>
                <td><span style="color:rgba(255,255,255,0.8);">${u.roleName}</span></td>
                <td>
                    <c:choose>
                        <c:when test="${u.active}"><span class="badge-active">Active</span></c:when>
                        <c:otherwise><span class="badge-disabled">Disabled</span></c:otherwise>
                    </c:choose>
                </td>
                <td style="font-size:12px; color:rgba(255,255,255,0.6);">${u.lastLogin}</td>
                <td class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/updateAccount?id=${u.userId}"><i class="fa-solid fa-pen"></i> Sửa</a>
                    <a class="btn btn-danger" href="${pageContext.request.contextPath}/admin/deleteAccount?id=${u.userId}"><i class="fa-solid fa-trash"></i> Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a class="back-link" href="${pageContext.request.contextPath}/dashboard"><i class="fa-solid fa-arrow-left"></i> Quay lại Dashboard</a>
</div>

</body>
</html>
