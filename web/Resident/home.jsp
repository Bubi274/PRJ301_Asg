<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cổng thông tin Cư dân - Lumina BMS</title>
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
        
        <a href="residentHome" class="menu-item active"><i class="fa-solid fa-house"></i>Trang chủ</a>
        <a href="roomInfo" class="menu-item"><i class="fa-solid fa-door-open"></i>Thông tin căn hộ</a>
        <a href="services" class="menu-item"><i class="fa-solid fa-bell-concierge"></i>Đăng ký dịch vụ</a>
        <a href="user-bills" class="menu-item"><i class="fa-solid fa-credit-card"></i>Thanh toán hóa đơn</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <h1 class="banner-title">Chào mừng bạn về nhà!</h1>
            <p class="banner-subtitle">Cổng thông tin hỗ trợ và thanh toán dịch vụ dành riêng cho cư dân Lumina BMS.</p>
        </div>
        
        <!-- NOTIFICATIONS -->
        <div class="glass-card">
            <div class="section-header">
                <h3>Thông báo mới</h3>
                <c:if test="${not empty listNotif}">
                    <div class="pagination">
                        <button class="pagination-btn" onclick="prevPage('notif')"><i class="fa-solid fa-chevron-left"></i></button>
                        <span id="notif-page-info">Trang 1</span>
                        <button class="pagination-btn" onclick="nextPage('notif')"><i class="fa-solid fa-chevron-right"></i></button>
                    </div>
                </c:if>
            </div>
            
            <c:choose>
                <c:when test="${empty listNotif}">
                    <div class="empty-message">Hiện tại không có thông báo nào từ Ban quản lý.</div>
                </c:when>
                <c:otherwise>
                    <table class="glass-table">
                        <thead>
                            <tr>
                                <th>Phân loại</th>
                                <th>Tiêu đề</th>
                                <th>Người gửi</th>
                                <th>Ngày nhận</th>
                            </tr>
                        </thead>
                        <tbody id="notif-tbody">
                            <c:forEach items="${listNotif}" var="n">
                                <tr>
                                    <td>
                                        <span class="badge ${n.badgeClass}">${n.typeLabel}</span>
                                    </td>
                                    <td>
                                        <a href="residentHome?id=${n.notificationId}" 
                                           style="text-decoration:none; color:#00d8ff; font-weight:bold; transition: color 0.3s;">
                                            ${n.title}
                                        </a>
                                    </td>
                                    <td>${n.senderName}</td>
                                    <td><fmt:formatDate value="${n.createdAt}" pattern="HH:mm dd/MM/yyyy"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- REQUESTS -->
        <div class="glass-card">
            <div class="section-header">
                <h3>Yêu cầu hỗ trợ của tôi</h3>
                <c:if test="${not empty listReq}">
                    <div class="pagination">
                        <button class="pagination-btn" onclick="prevPage('req')"><i class="fa-solid fa-chevron-left"></i></button>
                        <span id="req-page-info">Trang 1</span>
                        <button class="pagination-btn" onclick="nextPage('req')"><i class="fa-solid fa-chevron-right"></i></button>
                    </div>
                </c:if>
            </div>
            
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
                            </tr>
                        </thead>
                        <tbody id="req-tbody">
                            <c:forEach items="${listReq}" var="r">
                                <tr>
                                    <td>#${r.requestId}</td>
                                    <td>${r.title}</td>
                                    <td>Loại ${r.requestTypeId}</td>
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
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- NOTIFICATION DETAIL MODAL -->
<c:if test="${not empty notif}">
    <div class="modal-overlay" id="notifModal">
        <div class="modal-content">
            <button class="modal-close" onclick="closeModal()"><i class="fa-solid fa-xmark"></i></button>
            <div class="modal-header">
                <span class="badge ${notif.badgeClass}">${notif.typeLabel}</span>
                <h2 class="modal-title" style="margin-top: 10px;">${notif.title}</h2>
                <div class="modal-meta">
                    <i class="fa-regular fa-user"></i> Người gửi: <b>${notif.senderName}</b> | 
                    <i class="fa-regular fa-clock"></i> <fmt:formatDate value="${notif.createdAt}" pattern="HH:mm dd/MM/yyyy"/>
                </div>
            </div>
            <div class="modal-body">
                <p style="white-space: pre-wrap;">${notif.content}</p>
            </div>
            <div style="margin-top: 25px; text-align: right;">
                <button class="btn-glass btn-glass-secondary" onclick="closeModal()">Đóng</button>
            </div>
        </div>
    </div>
</c:if>

<script>
    const ROWS_PER_PAGE = 5;
    let pages = { notif: 1, req: 1 };

    function renderTable(tableType) {
        const tbody = document.getElementById(tableType + '-tbody');
        if (!tbody) return;
        
        const rows = tbody.getElementsByTagName('tr');
        const totalRows = rows.length;
        const totalPages = Math.ceil(totalRows / ROWS_PER_PAGE) || 1;
        
        let currentPage = pages[tableType];
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;
        pages[tableType] = currentPage;

        const pageInfo = document.getElementById(tableType + '-page-info');
        if (pageInfo) {
            pageInfo.innerText = 'Trang ' + currentPage + ' / ' + totalPages;
        }

        const start = (currentPage - 1) * ROWS_PER_PAGE;
        const end = start + ROWS_PER_PAGE;

        for (let i = 0; i < totalRows; i++) {
            if (i >= start && i < end) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
    }

    function nextPage(tableType) { pages[tableType]++; renderTable(tableType); }
    function prevPage(tableType) { pages[tableType]--; renderTable(tableType); }

    function closeModal() {
        const modal = document.getElementById('notifModal');
        if (modal) {
            modal.style.display = 'none';
            // Clean URL query parameters
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }

    window.onload = function() {
        renderTable('notif');
        renderTable('req');
    };
</script>

</body>
</html>
