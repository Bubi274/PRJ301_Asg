<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Yêu cầu chờ duyệt - VITALITY CMS</title>
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
        .container { max-width: 1200px; margin: 60px auto; padding: 35px; border-radius: 20px;
            background: rgba(255, 255, 255, 0.08); backdrop-filter: blur(15px);
            box-shadow: 0 10px 40px rgba(0, 216, 255, 0.15);
            border: 1px solid rgba(0, 216, 255, 0.15);
        }
        .topbar { display: flex; justify-content: space-between; align-items: center;
            border-bottom: 1px solid rgba(0, 216, 255, 0.2); padding-bottom: 18px; margin-bottom: 25px;
        }
        h2 { font-size: 24px; color: #00d8ff; font-weight: 600; }
        table { width: 100%; border-collapse: separate; border-spacing: 0;
            background: rgba(0, 0, 0, 0.18); border-radius: 12px; overflow: hidden;
        }
        th { background: rgba(0, 30, 50, 0.4); color: #00d8ff; padding: 15px 18px;
            font-weight: 600; text-transform: uppercase; font-size: 12px;
        }
        td { border-bottom: 1px solid rgba(255,255,255,0.06); padding: 15px 18px; }
        tr:last-child td { border-bottom: none; }
        tr:nth-child(odd) { background: rgba(255,255,255,0.02); }
        tr:hover { background: rgba(0, 216, 255, 0.06); }
        .badge { padding: 6px 12px; border-radius: 50px; font-size: 11px; font-weight: 600; }
        .badge-pending { background: rgba(241, 196, 15, 0.15); color: #f1c40f; border: 1px solid rgba(241, 196, 15, 0.3); }
        .badge-request { background: rgba(155, 89, 182, 0.15); color: #9b59b6; border: 1px solid rgba(155, 89, 182, 0.3); }
        .action-links a { color: #00d8ff; text-decoration: none; font-size: 13px; font-weight: 500; }
        .empty-message { text-align: center; color: rgba(255,255,255,0.4); padding: 40px 0; }
        .back-link { display: inline-block; margin-top: 25px; color: rgba(255,255,255,0.6);
            text-decoration: none; font-size: 13px; transition: color 0.3s; }
        .back-link:hover { color: #00d8ff; }
        .btn { display: inline-flex; align-items: center; padding: 8px 15px; border-radius: 50px;
            background: rgba(255,255,255,0.1); color: #00d8ff; text-decoration: none; font-size: 12px; }
        .btn:hover { background: rgba(0, 216, 255, 0.15); }
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
    <div class="topbar">
        <h2><i class="fa-solid fa-envelope-open-text"></i> Yêu cầu chờ duyệt</h2>
    </div>
    
    <table>
        <thead>
            <tr>
                <th>#YC</th><th>Cư dân</th><th>Căn hộ</th><th>Loại</th><th>Tiêu đề</th><th>Ngày</th><th>Trạng thái</th><th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty pendingRequests}">
                    <tr><td colspan="8" class="empty-message">
                        <i class="fa-solid fa-folder-open" style="font-size:2.5rem; margin-bottom:12px;"></i>
                        Không có yêu cầu nào đang chờ duyệt.</td></tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="req" items="${pendingRequests}">
                        <tr>
                            <td><strong>#${req.requestId}</strong></td>
                            <td>${req.residentName}</td>
                            <td>Phòng ${req.apartmentNumber}</td>
                            <td><span class="badge badge-request">${req.requestTypeName}</span></td>
                            <td>${req.title}</td>
                            <td><fmt:formatDate value="${req.createdAt}" pattern="dd/MM/yyyy"/></td>
                            <td><span class="badge badge-pending"><i class="fa-solid fa-clock"></i> Chờ duyệt</span></td>
                            <td class="action-links">
                                <a href="${pageContext.request.contextPath}/manager/requestDetail?requestId=${req.requestId}"><i class="fa-solid fa-eye"></i> Chi tiết</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    
    <a class="back-link" href="${pageContext.request.contextPath}/manager/dashboard"><i class="fa-solid fa-arrow-left-long"></i> Dashboard</a>
</div>
</body>
</html>