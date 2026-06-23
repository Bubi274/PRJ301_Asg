<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thông báo - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { margin: 0; min-height: 100vh; font-family: Arial, sans-serif; color: white; background: linear-gradient(rgba(0,60,70,0.8), rgba(0,40,60,0.85)), url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop'); background-size: cover; background-position: center; display: flex; justify-content: center; align-items: center; }
        .container { width: 450px; padding: 40px; border-radius: 16px; background: rgba(255,255,255,0.15); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); border: 1px solid rgba(255, 255, 255, 0.2); text-align: center; }
        
        .icon-circle { width: 80px; height: 80px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 20px auto; font-size: 36px; }
        .icon-success { background: rgba(46, 204, 113, 0.2); color: #2ecc71; border: 2px solid rgba(46, 204, 113, 0.5); }
        .icon-error { background: rgba(231, 76, 60, 0.2); color: #e74c3c; border: 2px solid rgba(231, 76, 60, 0.5); }
        
        .message-text { font-size: 1.1rem; color: rgba(255,255,255,0.9); line-height: 1.6; margin-bottom: 30px; }
        
        .btn { display: inline-block; padding: 12px 25px; border: none; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; transition: background 0.3s, transform 0.1s; text-decoration: none; box-sizing: border-box; }
        .btn:active { transform: scale(0.98); }
        .btn-primary { background: #00d8ff; color: white; }
        .btn-primary:hover { background: #00bce0; }
    </style>
</head>
<body>
<div class="container">
    <% if (Boolean.TRUE.equals(request.getAttribute("success"))) { %>
        <div class="icon-circle icon-success"><i class="fa-solid fa-check"></i></div>
        <div class="message-text">
            <strong>Thành công!</strong><br><br>
            <%= request.getAttribute("message") %>
        </div>
    <% } else { %>
        <div class="icon-circle icon-error"><i class="fa-solid fa-xmark"></i></div>
        <div class="message-text">
            <strong>Có lỗi xảy ra!</strong><br><br>
            <%= request.getAttribute("message") %>
        </div>
    <% } %>
    
    <a class="btn btn-primary" href="<%= request.getAttribute("backUrl") %>"><i class="fa-solid fa-arrow-left"></i> Quay lại</a>
</div>
</body>
</html>
