<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dịch vụ Cư dân - Lumina BMS</title>
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
        <a href="services" class="menu-item active"><i class="fa-solid fa-bell-concierge"></i>Đăng ký dịch vụ</a>
        <a href="user-bills" class="menu-item"><i class="fa-solid fa-credit-card"></i>Thanh toán hóa đơn</a>
        <a href="residentRequests" class="menu-item"><i class="fa-solid fa-envelope-open-text"></i>Yêu cầu hỗ trợ</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <h1 class="banner-title">Dịch vụ Cư dân</h1>
            <p class="banner-subtitle">Xem các dịch vụ bạn đã đăng ký hoặc đăng ký thêm các dịch vụ tiện ích mới.</p>
        </div>
        
        <div class="glass-card">
            <div class="section-header">
                <h3>Dịch vụ đang hoạt động</h3>
                <a href="services?action=add" class="btn-glass btn-glass-primary">
                    <i class="fa-solid fa-plus"></i> Đăng ký dịch vụ mới
                </a>
            </div>
            
            <c:choose>
                <c:when test="${empty myServices}">
                    <div class="empty-message">Bạn chưa đăng ký sử dụng dịch vụ nào. Hãy nhấp nút "Đăng ký dịch vụ mới" để xem danh sách dịch vụ.</div>
                </c:when>
                <c:otherwise>
                    <table class="glass-table">
                        <thead>
                            <tr>
                                <th>Tên dịch vụ</th>
                                <th>Đơn vị tính</th>
                                <th>Đơn giá</th>
                                <th style="width: 150px; text-align: center;">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${myServices}" var="s">
                                <tr>
                                    <td style="font-weight: 600; color: #00d8ff;">${s.serviceName}</td>
                                    <td>${s.unit}</td>
                                    <td>${s.pricePerUnit} VNĐ</td>
                                    <td style="text-align: center;">
                                        <form method="post" action="services" 
                                              onsubmit="return confirm('Bạn có chắc chắn muốn hủy đăng ký dịch vụ này không?');">
                                            <input type="hidden" name="action" value="cancel"/>
                                            <input type="hidden" name="serviceId" value="${s.serviceTypeId}"/>
                                            <button type="submit" class="btn-glass btn-glass-danger" style="padding: 8px 16px; font-size: 0.85rem;">
                                                <i class="fa-solid fa-trash-can"></i> Hủy dịch vụ
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="services?page=${currentPage-1}" class="pagination-btn"><i class="fa-solid fa-chevron-left"></i></a>
                        </c:if>
                        <span>Trang ${currentPage} / ${totalPages}</span>
                        <c:if test="${currentPage < totalPages}">
                            <a href="services?page=${currentPage+1}" class="pagination-btn"><i class="fa-solid fa-chevron-right"></i></a>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

</body>
</html>
