<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký Dịch vụ - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
    <style>
        .error-message {
            color: #ff5b7f;
            font-size: 0.95rem;
            margin-top: 15px;
            font-weight: 600;
        }
    </style>
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
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <h1 class="banner-title">Đăng ký Dịch vụ mới</h1>
            <p class="banner-subtitle">Chọn các dịch vụ tiện ích của tòa nhà để đăng ký sử dụng.</p>
        </div>
        
        <div class="glass-card">
            <div class="section-header">
                <h3>Chọn dịch vụ đăng ký</h3>
            </div>
            
            <c:choose>
                <c:when test="${empty allServices}">
                    <div class="empty-message">Bạn đã đăng ký sử dụng tất cả các dịch vụ hiện có của tòa nhà.</div>
                    <div style="margin-top: 25px;">
                        <a href="services" class="btn-glass btn-glass-secondary"><i class="fa-solid fa-arrow-left"></i> Quay lại</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <form method="post" action="services" onsubmit="return validateService()">
                        <input type="hidden" name="action" value="register"/>

                        <table class="glass-table">
                            <thead>
                                <tr>
                                    <th style="width: 80px; text-align: center;">Chọn</th>
                                    <th>Tên dịch vụ</th>
                                    <th>Đơn vị tính</th>
                                    <th>Đơn giá</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${allServices}" var="s">
                                    <tr>
                                        <td style="text-align: center;">
                                            <input type="checkbox" name="serviceIds" value="${s.serviceTypeId}" 
                                                   style="width: 18px; height: 18px; cursor: pointer; accent-color: #00d8ff;"/>
                                        </td>
                                        <td style="font-weight: 600; color: #00d8ff;">${s.serviceName}</td>
                                        <td>${s.unit}</td>
                                        <td>${s.pricePerUnit} VNĐ</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="services?action=add&page=${currentPage-1}" class="pagination-btn"><i class="fa-solid fa-chevron-left"></i></a>
                            </c:if>
                            <span>Trang ${currentPage} / ${totalPages}</span>
                            <c:if test="${currentPage < totalPages}">
                                <a href="services?action=add&page=${currentPage+1}" class="pagination-btn"><i class="fa-solid fa-chevron-right"></i></a>
                            </c:if>
                        </div>

                        <p id="errorMsg" class="error-message"></p>

                        <div style="margin-top: 30px; display: flex; gap: 15px;">
                            <a href="services" class="btn-glass btn-glass-secondary">
                                <i class="fa-solid fa-arrow-left"></i> Quay lại
                            </a>
                            <button type="submit" class="btn-glass btn-glass-primary">
                                <i class="fa-solid fa-square-check"></i> Đăng ký ngay
                            </button>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    function validateService() {
        const checkboxes = document.querySelectorAll("input[name='serviceIds']");
        const error = document.getElementById("errorMsg");
        let checked = false;

        checkboxes.forEach(cb => {
            if (cb.checked) {
                checked = true;
            }
        });

        if (!checked) {
            error.innerText = "⚠️ Bạn chưa chọn dịch vụ nào để đăng ký!";
            return false;
        }

        return true;
    }
</script>

</body>
</html>
