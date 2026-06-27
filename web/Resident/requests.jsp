<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Yêu cầu hỗ trợ - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
</head>
<body>

<header>
    <div class="logo"><i class="fa-solid fa-building"></i>VITALITY CMS</div>
    <div class="user-info">
        Xin chào, <b><%= session.getAttribute("fullName") %></b>
        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i> Đăng xuất</a>
    </div>
</header>

<div class="app-container">
    <!-- SIDEBAR -->
    <div class="sidebar">
        <div class="user-profile">
            <div class="avatar-circle">
                <i class="fa-solid fa-user-tie"></i>
            </div>
            <h4>${sessionScope.fullName}</h4>
        </div>
        
        <a href="residentHome" class="menu-item"><i class="fa-solid fa-house"></i>Trang chủ</a>
        <a href="roomInfo" class="menu-item"><i class="fa-solid fa-door-open"></i>Thông tin căn hộ</a>
        <a href="services" class="menu-item"><i class="fa-solid fa-bell-concierge"></i>Đăng ký dịch vụ</a>
        <a href="user-bills" class="menu-item"><i class="fa-solid fa-credit-card"></i>Thanh toán hóa đơn</a>
        <a href="residentRequests" class="menu-item active"><i class="fa-solid fa-envelope-open-text"></i>Yêu cầu hỗ trợ</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <div class="section-header">
                <h3>Danh sách yêu cầu hỗ trợ</h3>
                <a href="residentCreateRequest" class="btn-glass btn-glass-primary">
                    <i class="fa-solid fa-plus"></i> Gửi yêu cầu mới
                </a>
            </div>

            <c:if test="${not empty successMsg}">
                <div class="alert alert-success" style="background: rgba(46, 204, 113, 0.2); border: 1px solid #2ecc71; color: #2ecc71; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                    ${successMsg}
                </div>
            </c:if>
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger" style="background: rgba(231, 76, 60, 0.2); border: 1px solid #e74c3c; color: #e74c3c; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                    ${errorMsg}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty listReq}">
                    <div class="empty-message">Bạn chưa tạo yêu cầu hỗ trợ nào.</div>
                </c:when>
                <c:otherwise>
                    <table class="glass-table">
                        <thead>
                            <tr>
                                <th style="width: 80px;">Mã YC</th>
                                <th>Tiêu đề</th>
                                <th>Phân loại</th>
                                <th>Trạng thái</th>
                                <th>Ngày gửi</th>
                                <th style="width: 100px; text-align: center;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody id="req-tbody">
                            <c:forEach items="${listReq}" var="r">
                                <tr>
                                    <td>#${r.requestId}</td>
                                    <td>${r.title}</td>
                                    <td>${r.requestTypeName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.status eq 'Pending'}">
                                                <span class="badge badge-warning">Chờ duyệt</span>
                                            </c:when>
                                            <c:when test="${r.status eq 'Approved'}">
                                                <span class="badge badge-info">Đã duyệt</span>
                                            </c:when>
                                            <c:when test="${r.status eq 'Processing'}">
                                                <span class="badge badge-info">Đang xử lý</span>
                                            </c:when>
                                            <c:when test="${r.status eq 'Completed'}">
                                                <span class="badge badge-primary">Hoàn thành</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Đã hủy</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${r.createdAt}" pattern="HH:mm dd/MM/yyyy"/></td>
                                    <td style="text-align: center;">
                                        <a href="residentRequestDetail?requestId=${r.requestId}" class="btn-glass btn-glass-secondary" style="padding: 6px 12px; font-size: 0.85rem;">
                                            Chi tiết
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <div class="pagination">
                        <button class="pagination-btn" onclick="prevPage()"><i class="fa-solid fa-chevron-left"></i></button>
                        <span id="req-page-info">Trang 1</span>
                        <button class="pagination-btn" onclick="nextPage()"><i class="fa-solid fa-chevron-right"></i></button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    const ROWS_PER_PAGE = 5;
    let reqPage = 1;

    function renderTable() {
        const tbody = document.getElementById('req-tbody');
        if (!tbody) return;
        
        const rows = tbody.getElementsByTagName('tr');
        const totalRows = rows.length;
        const totalPages = Math.ceil(totalRows / ROWS_PER_PAGE) || 1;
        
        if (reqPage < 1) reqPage = 1;
        if (reqPage > totalPages) reqPage = totalPages;

        const pageInfo = document.getElementById('req-page-info');
        if (pageInfo) {
            pageInfo.innerText = 'Trang ' + reqPage + ' / ' + totalPages;
        }

        const start = (reqPage - 1) * ROWS_PER_PAGE;
        const end = start + ROWS_PER_PAGE;

        for (let i = 0; i < totalRows; i++) {
            if (i >= start && i < end) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
    }

    function nextPage() { reqPage++; renderTable(); }
    function prevPage() { reqPage--; renderTable(); }

    window.onload = function() {
        renderTable();
    };
</script>

</body>
</html>
