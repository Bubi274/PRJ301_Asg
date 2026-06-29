<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập nhật nhân viên - VITALITY CMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { margin: 0; min-height: 100vh; font-family: 'Segoe UI', sans-serif; color: white;
            background: linear-gradient(135deg, rgba(0, 30, 50, 0.9), rgba(0, 20, 35, 0.95)), 
                        url('https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?q=80&w=2070&auto=format&fit=crop');
            background-size: cover; background-attachment: fixed;
        }
        header { padding: 18px 40px; display: flex; justify-content: space-between; align-items: center;
            background: rgba(0, 0, 0, 0.25); backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(0, 216, 255, 0.15);
        }
        .logo { font-size: 1.4rem; font-weight: 700; color: #00d8ff; display: flex; align-items: center; }
        .logo i { margin-right: 10px; }
        .user-info { font-size: 0.95rem; color: rgba(255, 255, 255, 0.85); }
        .user-info a { color: #ff5b5b; margin-left: 20px; font-weight: 600; }
        .container { max-width: 500px; margin: 60px auto; padding: 35px; border-radius: 20px;
            background: rgba(255, 255, 255, 0.08); backdrop-filter: blur(15px);
            box-shadow: 0 10px 40px rgba(0, 216, 255, 0.15);
            border: 1px solid rgba(0, 216, 255, 0.15);
        }
        h2 { font-size: 24px; color: #00d8ff; font-weight: 600; margin-bottom: 25px; }
        .form-group { margin-bottom: 18px; }
        .form-group label { font-size: 12px; margin-bottom: 8px; color: rgba(255,255,255,0.65);
            text-transform: uppercase; letter-spacing: 0.5px; display: flex; align-items: center; gap: 6px;
        }
        .form-input { width: 100%; padding: 12px 15px; border-radius: 10px;
            border: 1px solid rgba(255,255,255,0.15); background: rgba(255,255,255,0.08);
            color: #fff; font-size: 14px; outline: none; transition: all 0.3s;
        }
        .form-input:focus { border-color: #00d8ff; background: rgba(255,255,255,0.12);
            box-shadow: 0 0 0 2px rgba(0, 216, 255, 0.2); }
        select.form-input option { background: #fff; color: #000; }

        .alert { padding: 15px 20px; border-radius: 10px; margin-bottom: 20px; font-weight: 500;
            background: rgba(231, 76, 60, 0.2); border: 1px solid #e74c3c; color: #e74c3c;
        }
        .btn-group { display: flex; gap: 12px; margin-top: 10px; }
        .btn { display: inline-flex; align-items: center; justify-content: center;
            padding: 12px 25px; border: none; border-radius: 50px;
            background: linear-gradient(135deg, #00d8ff, #009ecc); color: #fff;
            font-size: 14px; font-weight: 600; cursor: pointer; transition: all 0.3s;
            box-shadow: 0 4px 15px rgba(0, 216, 255, 0.25); text-decoration: none;
        }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0, 216, 255, 0.35); }
        .btn i { margin-right: 8px; }
        .btn-secondary { background: rgba(255,255,255,0.12); border: 1px solid rgba(255,255,255,0.15); }
        .back-link { display: inline-block; margin-top: 25px; color: rgba(255,255,255,0.6);
            text-decoration: none; font-size: 13px; transition: color 0.3s; }
        .back-link:hover { color: #00d8ff; }
        @media (max-width: 768px) { .container { margin: 20px; padding: 25px; } }
    </style>
</head>
<body>
<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info"><i class="fa-solid fa-user-tie"></i> <b>${sessionScope.fullName}</b> (Quản lý)
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i></a>
    </div>
</header>

<div class="container">
    <h2><i class="fa-solid fa-user-gear"></i> Cập nhật thông tin nhân viên</h2>
    
    <c:if test="${not empty error}">
        <div class="alert"><i class="fa-solid fa-exclamation-circle"></i> ${error}</div>
    </c:if>
    
    <form action="${pageContext.request.contextPath}/manager/staffs/edit" method="post">
        <input type="hidden" name="userId" value="${staff.userId}">
        
        <div class="form-group">
            <label for="fullName"><i class="fa-solid fa-user"></i> Họ tên *</label>
            <input type="text" name="fullName" id="fullName" class="form-input" placeholder="Nhập họ tên..." value="${staff.fullName}" required>
        </div>
        
        <div class="form-group">
            <label for="email"><i class="fa-solid fa-envelope"></i> Email *</label>
            <input type="email" name="email" id="email" class="form-input" placeholder="email@demo.com..." value="${staff.email}" required>
        </div>
        
        <div class="form-group">
            <label for="phone"><i class="fa-solid fa-phone"></i> Số điện thoại</label>
            <input type="tel" name="phone" id="phone" class="form-input" placeholder="Số điện thoại..." value="${staff.phone}">
        </div>
        
        <div class="form-group">
            <label for="positionId"><i class="fa-solid fa-briefcase"></i> Vị trí *</label>
            <select name="positionId" id="positionId" class="form-input" required>
                <option value="">-- Chọn vị trí --</option>
                <c:forEach var="pos" items="${positions}">
                    <option value="${pos.positionId}" ${pos.positionId == staff.positionId ? 'selected' : ''}>${pos.positionName}</option>
                </c:forEach>
            </select>
        </div>
        
        <div class="btn-group">
            <button type="submit" class="btn"><i class="fa-solid fa-save"></i> Lưu</button>
            <a href="${pageContext.request.contextPath}/manager/staffs" class="btn btn-secondary"><i class="fa-solid fa-xmark"></i> Hủy</a>
        </div>
    </form>
    
    <a class="back-link" href="${pageContext.request.contextPath}/manager/staffs"><i class="fa-solid fa-arrow-left-long"></i> Danh sách</a>
</div>
</body>
</html>