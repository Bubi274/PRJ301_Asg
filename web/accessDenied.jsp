<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Access Denied</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f2f6fa; margin: 0; padding: 0; color: #222; }
        .container { max-width: 900px; margin: 40px auto; background: #fff; padding: 30px 40px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
        h2 { color: #1f3864; border-bottom: 2px solid #2e75b6; padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #2e5c8a; color: #fff; }
        tr:nth-child(even) { background: #f2f6fa; }
        .btn { display: inline-block; padding: 8px 16px; background: #2e5c8a; color: #fff; text-decoration: none; border-radius: 4px; border: none; cursor: pointer; font-size: 14px; }
        .btn:hover { background: #1f3864; }
        .btn-danger { background: #c0392b; }
        .btn-danger:hover { background: #922b21; }
        .btn-secondary { background: #888; }
        .btn-secondary:hover { background: #555; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .error-box { background: #fdecea; color: #c0392b; border: 1px solid #c0392b; padding: 10px 15px; border-radius: 4px; margin-bottom: 15px; }
        .success-box { background: #eafaf1; color: #1d8348; border: 1px solid #1d8348; padding: 10px 15px; border-radius: 4px; margin-bottom: 15px; }
        .topbar { display: flex; justify-content: space-between; align-items: center; }
    </style>
</head>
<body>
<div class="container" style="max-width: 400px; text-align:center;">
    <h2 style="color:#c0392b;">Không có quyền truy cập</h2>
    <p>Khu vực này chỉ dành cho Admin. Tài khoản của bạn không có quyền truy cập trang này.</p>
    <a class="btn" href="logout">Quay lại đăng nhập</a>
</div>
</body>
</html>
