<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác nhận xóa tài khoản - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { margin: 0; min-height: 100vh; font-family: Arial, sans-serif; color: white; background: linear-gradient(rgba(0,60,70,0.8), rgba(0,40,60,0.85)), url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop'); background-size: cover; background-position: center; display: flex; align-items: center; justify-content: center; }
        .container { width: 450px; padding: 40px; border-radius: 16px; background: rgba(255,255,255,0.15); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); border: 1px solid rgba(255, 255, 255, 0.2); text-align: center; }
        h2 { font-size: 24px; color: #ff6b6b; margin-top: 0; margin-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.2); padding-bottom: 15px; }
        p { font-size: 1rem; color: rgba(255,255,255,0.9); line-height: 1.5; margin-bottom: 15px; }
        .user-highlight { font-weight: bold; font-size: 22px; color: white; background: rgba(0,0,0,0.2); padding: 10px; border-radius: 8px; margin: 20px 0; border: 1px solid rgba(255,255,255,0.1); }
        .warning-text { color: rgba(255,255,255,0.6); font-size: 13px; font-style: italic; margin-bottom: 30px; }
        
        .btn { display: inline-block; padding: 12px 20px; border: none; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; transition: background 0.3s, transform 0.1s; text-decoration: none; width: 45%; box-sizing: border-box; }
        .btn:active { transform: scale(0.98); }
        .btn-danger { background: #e74c3c; color: white; }
        .btn-danger:hover { background: #c0392b; }
        .btn-secondary { background: rgba(255,255,255,0.2); color: white; border: 1px solid rgba(255,255,255,0.3); }
        .btn-secondary:hover { background: rgba(255,255,255,0.3); }
        
        .action-buttons { display: flex; justify-content: space-between; margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h2><i class="fa-solid fa-triangle-exclamation"></i> Xác nhận xóa tài khoản</h2>
    <p>Bạn có chắc chắn muốn XÓA VĨNH VIỄN tài khoản này?</p>
    
    <div class="user-highlight">
        ${user.fullName}<br>
        <span style="font-size:16px; color:#00d8ff;">(${user.username})</span>
    </div>
    
    <p class="warning-text" style="color: #ff6b6b; font-weight: bold;">
        Lưu ý: Đây là thao tác XÓA CỨNG (Hard Delete). Dữ liệu sẽ bị xóa vĩnh viễn khỏi Database. Bạn chỉ có thể xóa nếu tài khoản này CHƯA dính dáng đến các dữ liệu khác (như hợp đồng, đơn từ).
    </p>

    <form action="${pageContext.request.contextPath}/admin/deleteAccount" method="post">
        <input type="hidden" name="userId" value="${user.userId}">
        <div class="action-buttons">
            <button type="submit" name="action" value="cancel" class="btn btn-secondary">Hủy</button>
            <button type="submit" name="action" value="confirm" class="btn btn-danger">Xóa tài khoản</button>
        </div>
    </form>
</div>
</body>
</html>
