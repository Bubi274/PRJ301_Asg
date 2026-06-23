<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm tài khoản - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { margin: 0; min-height: 100vh; font-family: Arial, sans-serif; color: white; background: linear-gradient(rgba(0,60,70,0.8), rgba(0,40,60,0.85)), url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop'); background-size: cover; background-position: center; display: flex; justify-content: center; align-items: center; padding: 20px 0; }
        .container { width: 500px; padding: 40px; border-radius: 16px; background: rgba(255,255,255,0.15); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); border: 1px solid rgba(255, 255, 255, 0.2); }
        h2 { font-size: 28px; color: white; margin-top: 0; margin-bottom: 25px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.2); padding-bottom: 15px; }
        
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; font-weight: bold; font-size: 14px; color: rgba(255,255,255,0.9); }
        .form-group input, .form-group select { width: 100%; padding: 12px 15px; border: 1px solid rgba(255,255,255,0.3); border-radius: 8px; background: rgba(255,255,255,0.1); color: white; box-sizing: border-box; font-size: 1rem; outline: none; transition: border 0.3s, background 0.3s; }
        .form-group input:focus, .form-group select:focus { border: 1px solid #00d8ff; background: rgba(255,255,255,0.2); }
        .form-group option { background: #1f3864; color: white; }
        
        .error-box { background: rgba(231, 76, 60, 0.2); color: #ff6b6b; border: 1px solid rgba(231, 76, 60, 0.4); padding: 12px 15px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center; }
        
        .btn { display: inline-block; padding: 14px 20px; border: none; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; transition: background 0.3s, transform 0.1s; text-decoration: none; width: 48%; box-sizing: border-box; text-align: center; }
        .btn:active { transform: scale(0.98); }
        .btn-primary { background: #00d8ff; color: white; }
        .btn-primary:hover { background: #00bce0; }
        .btn-secondary { background: rgba(255,255,255,0.2); color: white; border: 1px solid rgba(255,255,255,0.3); }
        .btn-secondary:hover { background: rgba(255,255,255,0.3); }
        
        .action-buttons { display: flex; justify-content: space-between; margin-top: 30px; }
    </style>
</head>
<body>
<div class="container">
    <h2><i class="fa-solid fa-user-plus"></i> Thêm tài khoản mới</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error-box"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/admin/addAccount" method="post">
        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" required>
        </div>
        <div class="form-group">
            <label>Password (Mặc định)</label>
            <input type="password" name="password" required minlength="6">
        </div>
        <div class="form-group">
            <label>Họ tên (Full Name)</label>
            <input type="text" name="fullName" required>
        </div>
        <div class="form-group">
            <label>Email liên hệ</label>
            <input type="email" name="email">
        </div>
        <div class="form-group">
            <label>Số điện thoại</label>
            <input type="text" name="phone">
        </div>
        <div class="form-group">
            <label>Phân quyền (Role)</label>
            <select name="roleId" required>
                <option value="">-- Chọn vai trò --</option>
                <option value="1">Admin - Quản trị viên</option>
                <option value="2">Manager - Quản lý</option>
                <option value="3">Staff - Nhân viên</option>
                <option value="4">Resident - Cư dân</option>
            </select>
        </div>
        
        <div class="action-buttons">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/accounts">Hủy</a>
            <button type="submit" class="btn btn-primary">Lưu tài khoản</button>
        </div>
    </form>
</div>
</body>
</html>
