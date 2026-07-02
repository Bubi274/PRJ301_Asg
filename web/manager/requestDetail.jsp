<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết yêu cầu - VITALITY CMS</title>
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
        .container { max-width: 800px; margin: 60px auto; padding: 35px; border-radius: 20px;
            background: rgba(255, 255, 255, 0.08); backdrop-filter: blur(15px);
            box-shadow: 0 10px 40px rgba(0, 216, 255, 0.15);
            border: 1px solid rgba(0, 216, 255, 0.15);
        }
        h2 { font-size: 24px; color: #00d8ff; margin-bottom: 25px; }
        h3 { font-size: 18px; color: #00d8ff; margin: 25px 0 15px 0; }
        .info-card { background: rgba(0, 0, 0, 0.18); border-radius: 12px; padding: 20px; margin-bottom: 20px; border: 1px solid rgba(255,255,255,0.06); }
        .info-row { display: flex; margin-bottom: 12px; }
        .info-label { width: 120px; font-weight: 600; color: rgba(255,255,255,0.6); flex-shrink: 0; }
        .info-value { flex: 1; }
        .badge { padding: 5px 10px; border-radius: 50px; font-size: 11px; font-weight: 600; }
        .badge-pending { background: rgba(241, 196, 15, 0.15); color: #f1c40f; border: 1px solid rgba(241, 196, 15, 0.3); }
        .badge-request { background: rgba(155, 89, 182, 0.15); color: #9b59b6; }
        .form-group { margin-bottom: 18px; }
        .form-group label { display: block; font-size: 12px; margin-bottom: 8px; color: rgba(255,255,255,0.65); }
        .form-input { width: 100%; padding: 12px 15px; border-radius: 10px; border: 1px solid rgba(255,255,255,0.15);
            background: rgba(255,255,255,0.08); color: #fff; font-size: 14px; outline: none; transition: all 0.3s;
        }
        .form-input:focus { border-color: #00d8ff; background: rgba(255,255,255,0.12); }
        .btn-group { display: flex; gap: 12px; margin-top: 10px; }
        .btn { display: inline-flex; align-items: center; justify-content: center; padding: 12px 25px;
            border: none; border-radius: 50px; background: linear-gradient(135deg, #00d8ff, #009ecc);
            color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: all 0.3s;
            box-shadow: 0 4px 15px rgba(0, 216, 255, 0.25); text-decoration: none;
        }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0, 216, 255, 0.35); }
        .btn-secondary { background: rgba(255,255,255,0.12); border: 1px solid rgba(255,255,255,0.15); box-shadow: none; }
        .back-link { display: inline-block; margin-top: 25px; color: rgba(255,255,255,0.6); text-decoration: none; font-size: 13px; }
        .back-link:hover { color: #00d8ff; }
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
    <h2><i class="fa-solid fa-file-lines"></i> Chi tiết yêu cầu #${requestObj.requestId}</h2>
    
    <h3><i class="fa-solid fa-user"></i> Thông tin cư dân</h3>
    <div class="info-card">
        <div class="info-row"><span class="info-label">Cư dân:</span><span class="info-value">${requestObj.residentName}</span></div>
        <div class="info-row"><span class="info-label">Căn hộ:</span><span class="info-value">Phòng ${requestObj.apartmentNumber}</span></div>
    </div>
    
    <h3><i class="fa-solid fa-clipboard"></i> Thông tin yêu cầu</h3>
    <div class="info-card">
        <div class="info-row"><span class="info-label">Loại:</span><span class="info-value"><span class="badge badge-request">${requestObj.requestTypeName}</span></span></div>
        <div class="info-row"><span class="info-label">Tiêu đề:</span><span class="info-value">${requestObj.title}</span></div>
        <div class="info-row"><span class="info-label">Mô tả:</span><span class="info-value">${requestObj.description}</span></div>
        <div class="info-row"><span class="info-label">Ngày tạo:</span><span class="info-value"><fmt:formatDate value="${requestObj.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span></div>
        <div class="info-row"><span class="info-label">Trạng thái:</span><span class="info-value"><span class="badge badge-pending"><i class="fa-solid fa-clock"></i> Chờ duyệt</span></span></div>
    </div>
    
    <h3><i class="fa-solid fa-user-plus"></i> Phân công xử lý</h3>
    <form action="${pageContext.request.contextPath}/manager/assignStaff" method="post" class="info-card">
        <input type="hidden" name="requestId" value="${requestObj.requestId}">
        <div class="form-group">
            <label for="staffId"><i class="fa-solid fa-user-gear"></i> Chọn nhân viên *</label>
            <select name="staffId" id="staffId" class="form-input" required>
                <option value="">-- Chọn nhân viên --</option>
                <c:forEach var="staff" items="${availableStaff}">
                    <option value="${staff.userId}">${staff.fullName} (${staff.positionName})</option>
                </c:forEach>
            </select>
        </div>
        <div class="btn-group">
            <button type="submit" class="btn"><i class="fa-solid fa-paper-plane"></i> Phân công</button>
            <a href="${pageContext.request.contextPath}/manager/pendingRequests" class="btn btn-secondary"><i class="fa-solid fa-xmark"></i> Hủy</a>
        </div>
    </form>
    
    <a class="back-link" href="${pageContext.request.contextPath}/manager/pendingRequests"><i class="fa-solid fa-arrow-left-long"></i> Quay lại</a>
</div>
</body>
</html>