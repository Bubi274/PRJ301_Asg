<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tạo yêu cầu mới - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
    <style>
        .form-group {
            margin-bottom: 22px;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        .form-group label {
            font-size: 0.95rem;
            color: rgba(255, 255, 255, 0.85);
            font-weight: 500;
        }
        .form-group select, .form-group input, .form-group textarea {
            padding: 12px 16px;
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.15);
            color: white;
            font-size: 0.95rem;
            font-family: inherit;
            transition: all 0.3s;
            box-sizing: border-box;
            width: 100%;
        }
        .form-group select:focus, .form-group input:focus, .form-group textarea:focus {
            outline: none;
            border-color: #00d8ff;
            box-shadow: 0 0 8px rgba(0, 216, 255, 0.3);
            background: rgba(255, 255, 255, 0.1);
        }
        .form-group select option {
            background-color: #0d2834;
            color: white;
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
        <a href="services" class="menu-item"><i class="fa-solid fa-bell-concierge"></i>Đăng ký dịch vụ</a>
        <a href="user-bills" class="menu-item"><i class="fa-solid fa-credit-card"></i>Thanh toán hóa đơn</a>
        <a href="residentRequests" class="menu-item active"><i class="fa-solid fa-envelope-open-text"></i>Yêu cầu hỗ trợ</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <div class="section-header">
                <h3>Gửi yêu cầu hỗ trợ mới</h3>
                <a href="residentRequests" class="btn-glass btn-glass-secondary">
                    <i class="fa-solid fa-arrow-left"></i> Quay lại
                </a>
            </div>

            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger" style="background: rgba(231, 76, 60, 0.2); border: 1px solid #e74c3c; color: #e74c3c; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                    ${errorMsg}
                </div>
            </c:if>

            <form action="residentCreateRequest" method="POST" style="margin-top: 20px;">
                <div class="form-group">
                    <label for="requestTypeId">Loại yêu cầu hỗ trợ <span style="color: #ff5b7f;">*</span></label>
                    <select name="requestTypeId" id="requestTypeId" required>
                        <c:forEach var="type" items="${listTypes}">
                            <option value="${type.requestTypeId}">${type.typeName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="title">Tiêu đề yêu cầu <span style="color: #ff5b7f;">*</span></label>
                    <input type="text" name="title" id="title" placeholder="Ví dụ: Hỏng bóng đèn hành lang, rò rỉ nước..." required>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả chi tiết</label>
                    <textarea name="description" id="description" rows="6" placeholder="Mô tả chi tiết vấn đề bạn đang gặp phải để ban quản lý hỗ trợ tốt nhất..."></textarea>
                </div>

                <div style="margin-top: 30px; display: flex; gap: 15px;">
                    <button type="submit" class="btn-glass btn-glass-primary">Gửi yêu cầu</button>
                    <a href="residentRequests" class="btn-glass btn-glass-secondary">Hủy bỏ</a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
