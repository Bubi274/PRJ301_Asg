<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thông tin Căn hộ - Lumina BMS</title>
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
        <a href="roomInfo" class="menu-item active"><i class="fa-solid fa-door-open"></i>Thông tin căn hộ</a>
        <a href="services" class="menu-item"><i class="fa-solid fa-bell-concierge"></i>Đăng ký dịch vụ</a>
        <a href="user-bills" class="menu-item"><i class="fa-solid fa-credit-card"></i>Thanh toán hóa đơn</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <h1 class="banner-title">Thông tin Căn hộ</h1>
            <p class="banner-subtitle">Xem thông tin chi tiết và tình trạng căn hộ hiện tại của bạn.</p>
        </div>
        
        <div class="glass-card">
            <div class="section-header">
                <h3>Chi tiết Căn hộ</h3>
            </div>
            
            <c:choose>
                <c:when test="${empty myRoom}">
                    <div class="empty-message">Bạn hiện tại chưa được gán hoặc đăng ký căn hộ nào trên hệ thống. Vui lòng liên hệ ban quản lý.</div>
                </c:when>
                <c:otherwise>
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-item-label">Số căn hộ</div>
                            <div class="info-item-value accent"><i class="fa-solid fa-hashtag" style="margin-right:8px; font-size:1rem; color:#00d8ff;"></i>${myRoom.apartmentNumber}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-item-label">Tầng</div>
                            <div class="info-item-value"><i class="fa-solid fa-layer-group" style="margin-right:8px; font-size:1rem; color:rgba(255,255,255,0.6);"></i>Tầng ${myRoom.floor}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-item-label">Diện tích</div>
                            <div class="info-item-value"><i class="fa-solid fa-ruler-combined" style="margin-right:8px; font-size:1rem; color:rgba(255,255,255,0.6);"></i>${myRoom.area} m²</div>
                        </div>
                        <div class="info-item">
                            <div class="info-item-label">Loại phòng</div>
                            <div class="info-item-value"><i class="fa-solid fa-bed" style="margin-right:8px; font-size:1rem; color:rgba(255,255,255,0.6);"></i>${myRoom.types}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-item-label">Trạng thái</div>
                            <div class="info-item-value">
                                <c:choose>
                                    <c:when test="${myRoom.status eq 'Occupied'}">
                                        <span class="badge badge-primary">Đang sử dụng</span>
                                    </c:when>
                                    <c:when test="${myRoom.status eq 'Vacant'}">
                                        <span class="badge badge-warning">Trống</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Đang sửa chữa</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="info-item">
                            <div class="info-item-label">Chủ căn hộ</div>
                            <div class="info-item-value"><i class="fa-solid fa-user-shield" style="margin-right:8px; font-size:1rem; color:rgba(255,255,255,0.6);"></i>${myRoom.ownerName}</div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

</body>
</html>
