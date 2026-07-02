<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý nhân viên - VITALITY CMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { margin: 0; min-height: 100vh; font-family: 'Segoe UI', sans-serif; color: #fff;
            background: linear-gradient(135deg, rgba(0, 30, 50, 0.9), rgba(0, 20, 35, 0.95)), 
                        url('https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?q=80&w=2070&auto=format&fit=crop');
            background-size: cover; background-attachment: fixed;
        }
        header { padding: 18px 40px; display: flex; justify-content: space-between; align-items: center;
            background: rgba(0, 0, 0, 0.25); backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(0, 216, 255, 0.15);
        }
        .logo { font-size: 1.4rem; font-weight: 700; display: flex; align-items: center; color: #00d8ff; }
        .logo i { margin-right: 10px; font-size: 1.6rem; }
        .user-info { font-size: 0.95rem; color: rgba(255, 255, 255, 0.85); }
        .user-info a { color: #ff5b5b; margin-left: 20px; font-weight: 600; transition: color 0.3s; }
        .user-info a:hover { color: #ff3333; }
        .container { max-width: 1200px; margin: 40px auto; padding: 35px; border-radius: 20px;
            background: rgba(255, 255, 255, 0.08); backdrop-filter: blur(15px);
            box-shadow: 0 10px 40px rgba(0, 216, 255, 0.15);
            border: 1px solid rgba(0, 216, 255, 0.15);
        }
        .topbar { display: flex; justify-content: space-between; align-items: center;
            border-bottom: 1px solid rgba(0, 216, 255, 0.2); padding-bottom: 18px; margin-bottom: 25px;
        }
        h2 { font-size: 26px; color: #00d8ff; font-weight: 600; }
        .btn { display: inline-flex; align-items: center; justify-content: center;
            padding: 10px 22px; border: none; border-radius: 50px;
            background: linear-gradient(135deg, #00d8ff, #009ecc); color: #fff;
            font-size: 14px; font-weight: 600; cursor: pointer; transition: all 0.3s; text-decoration: none;
            box-shadow: 0 4px 15px rgba(0, 216, 255, 0.25);
        }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(0, 216, 255, 0.35); }
        .btn i { margin-right: 8px; }
        .btn-secondary { background: rgba(255,255,255,0.12); border: 1px solid rgba(255,255,255,0.15); box-shadow: none; border-radius: 50px; }
        .btn-secondary:hover { background: rgba(255,255,255,0.2); }
        .filter-form { display: flex; flex-wrap: wrap; gap: 12px; background: rgba(0,0,0,0.2); padding: 18px;
            border-radius: 12px; margin-bottom: 25px; border: 1px solid rgba(0, 216, 255, 0.1); align-items: flex-end;
        }
        .form-group { display: flex; flex-direction: column; flex: 1; min-width: 160px; }
        .form-group label { font-size: 0.8rem; margin-bottom: 6px; color: rgba(255,255,255,0.65); text-transform: uppercase; }
        .form-input { padding: 10px 15px; border-radius: 10px; border: 1px solid rgba(255,255,255,0.15);
            background: rgba(255,255,255,0.08); color: #fff; font-size: 14px; outline: none; transition: all 0.3s;
        }
        .form-input:focus { border-color: #00d8ff; background: rgba(255,255,255,0.12); box-shadow: 0 0 0 2px rgba(0, 216, 255, 0.2); }
        select.form-input option { background: #0a1e2e; color: #fff; }
        table { width: 100%; border-collapse: separate; border-spacing: 0; margin-top: 15px;
            background: rgba(0, 0, 0, 0.18); border-radius: 12px; overflow: hidden;
        }
        th { background: rgba(0, 30, 50, 0.4); color: #00d8ff; padding: 15px 18px; font-weight: 600; text-transform: uppercase; font-size: 12px; }
        td { border-bottom: 1px solid rgba(255,255,255,0.06); padding: 15px 18px; }
        tr:last-child td { border-bottom: none; }
        tr:nth-child(odd) { background: rgba(255,255,255,0.02); }
        tr:hover { background: rgba(0, 216, 255, 0.08); }
        .badge { padding: 6px 12px; border-radius: 50px; font-size: 11px; font-weight: 600; display: inline-block; }
        .badge-active { background: rgba(46, 204, 113, 0.15); color: #2ecc71; border: 1px solid rgba(46, 204, 113, 0.3); }
        .badge-inactive { background: rgba(150, 150, 150, 0.15); color: #aaa; border: 1px solid rgba(150, 150, 150, 0.3); }
        .badge-position { background: rgba(0, 216, 255, 0.15); color: #00d8ff; border: 1px solid rgba(0, 216, 255, 0.3); }
        .action-links a { color: #00d8ff; text-decoration: none; margin-right: 12px; font-size: 12px; font-weight: 500; }
        .action-links a:hover { color: #fff; text-shadow: 0 0 8px rgba(0, 216, 255, 0.5); }
        .back-link { display: inline-block; margin-top: 30px; color: rgba(255,255,255,0.6); text-decoration: none; font-size: 13px; transition: color 0.3s; }
        .back-link:hover { color: #00d8ff; }
        .page-btn { display: inline-flex; align-items: center; justify-content: center; width: 40px; height: 40px; 
            border-radius: 10px; background: rgba(255,255,255,0.1); color: #fff; text-decoration: none; font-weight: 600; }
        .page-btn.active { background: linear-gradient(135deg, #00d8ff, #009ecc); }
        @media (max-width: 768px) {
            .container { margin: 20px; padding: 20px; }
            .topbar { flex-direction: column; gap: 15px; }
            th, td { padding: 12px 10px; font-size: 12px; }
        }
    </style>
</head>
<body>
<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info">
        <i class="fa-solid fa-user-tie"></i> Chào mừng, <b>${sessionScope.fullName}</b> (Quản lý)
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i></a>
    </div>
</header>

<div class="container">
    <div class="topbar">
        <h2><i class="fa-solid fa-users-gear"></i> Quản lý hồ sơ nhân viên</h2>
        <a class="btn" href="${pageContext.request.contextPath}/manager/staffs/add"><i class="fa-solid fa-user-plus"></i> Thêm mới</a>
    </div>
    
    <form action="${pageContext.request.contextPath}/manager/staffs" method="get" class="filter-form">
        <div class="form-group">
            <label for="search"><i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm</label>
            <input type="text" name="search" id="search" class="form-input" placeholder="Tên, email..." value="${search}">
        </div>
        <div class="form-group">
            <label for="positionId"><i class="fa-solid fa-briefcase"></i> Vị trí</label>
            <select name="positionId" id="positionId" class="form-input">
                <option value="">-- Tất cả --</option>
                <c:forEach var="pos" items="${positions}">
                    <option value="${pos.positionId}" ${pos.positionId == positionId ? 'selected' : ''}>${pos.positionName}</option>
                </c:forEach>
            </select>
        </div>
        <div style="display:flex; gap:8px;">
            <button type="submit" class="btn btn-secondary"><i class="fa-solid fa-filter"></i></button>
            <a href="${pageContext.request.contextPath}/manager/staffs" class="btn btn-secondary"><i class="fa-solid fa-rotate-left"></i></a>
        </div>
    </form>
    
    <table>
        <thead>
            <tr>
                <th>#NV</th><th>Tên</th><th>Email</th><th>SĐT</th><th>Vị trí</th><th>Trạng thái</th><th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty staffList}">
                    <tr>
                        <td colspan="7" style="text-align:center; padding:40px 0; color:rgba(255,255,255,0.4);">
                            <i class="fa-solid fa-folder-open" style="font-size:2.5rem; display:block; margin-bottom:12px;"></i>
                            Không tìm thấy nhân viên phù hợp.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="staff" items="${staffList}">
                        <tr>
                            <td><strong>#${staff.id}</strong></td>
                            <td>${staff.fullName}</td>
                            <td>${staff.email}</td>
                            <td>${staff.phone}</td>
                            <td><span class="badge badge-position">${staff.positionName}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${staff.active}">
                                        <span class="badge badge-active"><i class="fa-solid fa-circle-check"></i> Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-inactive"><i class="fa-solid fa-ban"></i> Vô hiệu</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="action-links">
                                <a href="${pageContext.request.contextPath}/manager/staffs/edit?userId=${staff.userId}"><i class="fa-solid fa-pen"></i></a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    
    <c:if test="${totalPages > 1}">
        <div style="display:flex; justify-content:center; margin-top:30px; gap:8px;">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}&search=${search}&positionId=${positionId}" class="page-btn"><i class="fa-solid fa-angle-left"></i></a>
            </c:if>
            <c:forEach var="p" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${p == currentPage}">
                        <a href="?page=${p}&search=${search}&positionId=${positionId}" class="page-btn active">${p}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${p}&search=${search}&positionId=${positionId}" class="page-btn">${p}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}&search=${search}&positionId=${positionId}" class="page-btn"><i class="fa-solid fa-angle-right"></i></a>
            </c:if>
        </div>
    </c:if>
    
    <a class="back-link" href="${pageContext.request.contextPath}/manager/dashboard"><i class="fa-solid fa-arrow-left-long"></i> Dashboard</a>
</div>
</body>
</html>