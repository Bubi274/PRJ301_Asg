<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý căn hộ - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* Glassmorphism theme */
        body { 
            margin: 0; 
            min-height: 100vh; 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            color: white; 
            background: linear-gradient(rgba(0, 50, 60, 0.8), rgba(0, 30, 45, 0.9)), url('https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?q=80&w=2070&auto=format&fit=crop'); 
            background-size: cover; 
            background-position: center; 
            background-attachment: fixed;
        }
        
        header { 
            padding: 20px 40px; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
            background: rgba(0, 0, 0, 0.2); 
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .logo { 
            font-size: 1.4rem; 
            font-weight: bold; 
            letter-spacing: 1px; 
            color: white; 
            display: flex;
            align-items: center;
        }
        
        .logo i { 
            margin-right: 10px; 
            color: #00d8ff; 
        }
        
        .user-info { 
            font-size: 1rem; 
            color: rgba(255, 255, 255, 0.9); 
        }
        
        .user-info a { 
            color: #ff5b5b; 
            text-decoration: none; 
            margin-left: 20px; 
            font-weight: bold; 
        }
        
        .container { 
            max-width: 1200px; 
            margin: 40px auto; 
            padding: 40px; 
            border-radius: 16px; 
            background: rgba(255, 255, 255, 0.08); 
            backdrop-filter: blur(15px); 
            -webkit-backdrop-filter: blur(15px); 
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); 
            border: 1px solid rgba(255, 255, 255, 0.15); 
        }
        
        .topbar { 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
            border-bottom: 1px solid rgba(255,255,255,0.15); 
            padding-bottom: 20px; 
            margin-bottom: 30px; 
        }
        
        h2 { 
            font-size: 28px; 
            color: white; 
            margin: 0; 
            font-weight: 400;
        }
        
        /* Buttons */
        .btn { 
            display: inline-flex; 
            align-items: center;
            justify-content: center;
            padding: 10px 20px; 
            border: none; 
            border-radius: 6px; 
            background: #00d8ff; 
            color: white; 
            font-size: 14px; 
            font-weight: bold; 
            cursor: pointer; 
            transition: all 0.3s; 
            text-decoration: none; 
        }
        
        .btn:hover { 
            background: #00bce0; 
            transform: translateY(-2px);
        }
        
        .btn i { 
            margin-right: 8px; 
        }
        
        .btn-secondary { 
            background: rgba(255,255,255,0.15); 
            border: 1px solid rgba(255,255,255,0.1);
        }
        
        .btn-secondary:hover { 
            background: rgba(255,255,255,0.25); 
        }
        
        /* Search Filter Form */
        .filter-form {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            background: rgba(0,0,0,0.2);
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            border: 1px solid rgba(255,255,255,0.05);
            align-items: flex-end;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
            flex: 1;
            min-width: 180px;
        }
        
        .form-group label {
            font-size: 0.85rem;
            margin-bottom: 8px;
            color: rgba(255,255,255,0.7);
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .form-input {
            padding: 10px 15px;
            border-radius: 6px;
            border: 1px solid rgba(255,255,255,0.15);
            background: rgba(255,255,255,0.1);
            color: white;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s;
        }
        
        .form-input:focus {
            border-color: #00d8ff;
            background: rgba(255,255,255,0.15);
        }
        
        select.form-input option {
            background: #002530;
            color: white;
        }
        
        /* Table Styles */
        table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 15px; 
            background: rgba(0, 0, 0, 0.15);
            border-radius: 8px;
            overflow: hidden;
        }
        
        th, td { 
            border-bottom: 1px solid rgba(255,255,255,0.08); 
            padding: 16px 20px; 
            text-align: left; 
        }
        
        th { 
            background: rgba(0,0,0,0.3); 
            color: #00d8ff; 
            font-weight: bold; 
            text-transform: uppercase; 
            font-size: 13px; 
            letter-spacing: 1px; 
        }
        
        tr:last-child td {
            border-bottom: none;
        }
        
        tr:hover { 
            background: rgba(255,255,255,0.04); 
        }
        
        /* Badges */
        .badge {
            padding: 5px 10px; 
            border-radius: 4px; 
            font-size: 12px; 
            font-weight: bold;
            display: inline-block;
        }
        
        .badge-occupied { 
            background: rgba(46, 204, 113, 0.15); 
            color: #2ecc71; 
            border: 1px solid rgba(46, 204, 113, 0.3); 
        }
        
        .badge-vacant { 
            background: rgba(241, 196, 15, 0.15); 
            color: #f1c40f; 
            border: 1px solid rgba(241, 196, 15, 0.3); 
        }
        
        .badge-repair { 
            background: rgba(231, 76, 60, 0.15); 
            color: #e74c3c; 
            border: 1px solid rgba(231, 76, 60, 0.3); 
        }
        
        /* Alert Message */
        .alert {
            padding: 15px 20px;
            border-radius: 6px;
            margin-bottom: 25px;
            font-weight: 500;
        }
        
        .alert-success {
            background: rgba(46, 204, 113, 0.2);
            border: 1px solid #2ecc71;
            color: #2ecc71;
        }
        
        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 35px;
            gap: 10px;
        }
        
        .page-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 38px;
            height: 38px;
            border-radius: 6px;
            background: rgba(255, 255, 255, 0.1);
            color: white;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s;
            border: 1px solid rgba(255,255,255,0.05);
        }
        
        .page-link:hover, .page-link.active {
            background: #00d8ff;
            color: #002530;
            border-color: #00d8ff;
        }
        
        .page-link.disabled {
            opacity: 0.4;
            pointer-events: none;
        }
        
        .page-info {
            margin-right: 15px;
            color: rgba(255, 255, 255, 0.6);
            font-size: 14px;
        }
        
        .back-link { 
            display: inline-block; 
            margin-top: 30px; 
            color: rgba(255,255,255,0.7); 
            text-decoration: none; 
            font-size: 14px; 
            transition: color 0.3s; 
        }
        
        .back-link:hover { 
            color: #00d8ff; 
        }
    </style>
</head>
<body>

<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info">
        Chào mừng, <b>${sessionScope.fullName}</b> (Quản lý)
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i> Đăng xuất</a>
    </div>
</header>

<div class="container">
    <c:if test="${param.msg == 'success'}">
        <div class="alert alert-success">
            <i class="fa-solid fa-circle-check"></i> Căn hộ mới đã được thêm thành công vào hệ thống!
        </div>
    </c:if>

    <div class="topbar">
        <h2>Quản lý danh sách căn hộ</h2>
        <a class="btn" href="${pageContext.request.contextPath}/manager/addApartment"><i class="fa-solid fa-plus"></i> Thêm căn hộ mới</a>
    </div>

    <!-- Filter Form -->
    <form action="${pageContext.request.contextPath}/manager/apartments" method="get" class="filter-form">
        <div class="form-group">
            <label for="search">Tìm kiếm</label>
            <input type="text" name="search" id="search" class="form-input" placeholder="Số căn hộ hoặc tên chủ hộ..." value="${search}">
        </div>
        
        <div class="form-group">
            <label for="status">Trạng thái</label>
            <select name="status" id="status" class="form-input">
                <option value="">-- Tất cả trạng thái --</option>
                <option value="Occupied" ${status == 'Occupied' ? 'selected' : ''}>Occupied (Đang ở)</option>
                <option value="Vacant" ${status == 'Vacant' ? 'selected' : ''}>Vacant (Trống)</option>
                <option value="Under Repair" ${status == 'Under Repair' ? 'selected' : ''}>Under Repair (Sửa chữa)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="floor">Tầng</label>
            <input type="number" name="floor" id="floor" class="form-input" placeholder="Ví dụ: 1" value="${floor}" min="1">
        </div>
        
        <div style="display:flex; gap:10px;">
            <button type="submit" class="btn"><i class="fa-solid fa-magnifying-glass"></i> Lọc</button>
            <a href="${pageContext.request.contextPath}/manager/apartments" class="btn btn-secondary"><i class="fa-solid fa-rotate-left"></i> Đặt lại</a>
        </div>
    </form>

    <!-- Table of Apartments -->
    <table>
        <thead>
            <tr>
                <th>Số căn hộ</th>
                <th>Tầng</th>
                <th>Diện tích (m²)</th>
                <th>Loại phòng</th>
                <th>Chủ hộ / Cư dân</th>
                <th>Trạng thái</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty apartments}">
                    <tr>
                        <td colspan="6" style="text-align: center; color: rgba(255,255,255,0.5); padding: 40px 0;">
                            <i class="fa-solid fa-folder-open" style="font-size: 2.5rem; display: block; margin-bottom: 15px;"></i>
                            Không tìm thấy căn hộ nào phù hợp với bộ lọc tìm kiếm.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="a" items="${apartments}">
                        <tr>
                            <td><strong>${a.apartmentNumber}</strong></td>
                            <td>Tầng ${a.floor}</td>
                            <td>${a.area} m²</td>
                            <td>${a.types}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty a.ownerName}">
                                        <i class="fa-solid fa-user" style="color: #00d8ff; margin-right: 5px; font-size: 0.9rem;"></i> ${a.ownerName}
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: rgba(255,255,255,0.4); font-style: italic;">Chưa gán cư dân</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${a.status == 'Occupied'}">
                                        <span class="badge badge-occupied">Đang ở</span>
                                    </c:when>
                                    <c:when test="${a.status == 'Vacant'}">
                                        <span class="badge badge-vacant">Trống</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-repair">Đang sửa chữa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <span class="page-info">Hiển thị kết quả (${totalItems} căn hộ)</span>
            
            <a href="${pageContext.request.contextPath}/manager/apartments?page=${currentPage - 1}&search=${search}&status=${status}&floor=${floor}" 
               class="page-link ${currentPage == 1 ? 'disabled' : ''}" title="Trang trước">
                <i class="fa-solid fa-chevron-left"></i>
            </a>
            
            <c:forEach var="p" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/manager/apartments?page=${p}&search=${search}&status=${status}&floor=${floor}" 
                   class="page-link ${currentPage == p ? 'active' : ''}">
                    ${p}
                </a>
            </c:forEach>
            
            <a href="${pageContext.request.contextPath}/manager/apartments?page=${currentPage + 1}&search=${search}&status=${status}&floor=${floor}" 
               class="page-link ${currentPage == totalPages ? 'disabled' : ''}" title="Trang tiếp theo">
                <i class="fa-solid fa-chevron-right"></i>
            </a>
        </div>
    </c:if>

    <a class="back-link" href="${pageContext.request.contextPath}/manager/dashboard"><i class="fa-solid fa-arrow-left"></i> Quay lại Dashboard</a>
</div>

</body>
</html>
