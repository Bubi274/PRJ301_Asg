<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kết quả Thanh toán - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
    <style>
        .result-container {
            text-align: center;
            max-width: 550px;
            margin: 80px auto;
            padding: 50px 40px;
        }
        .result-icon {
            font-size: 5rem;
            margin-bottom: 25px;
        }
        .icon-success {
            color: #00ff88;
            text-shadow: 0 0 20px rgba(0, 255, 136, 0.4);
        }
        .icon-failed {
            color: #ff5b7f;
            text-shadow: 0 0 20px rgba(255, 91, 127, 0.4);
        }
        .result-title {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 15px;
        }
        .result-message {
            font-size: 1.1rem;
            color: rgba(255, 255, 255, 0.8);
            margin-bottom: 35px;
            line-height: 1.6;
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

<div class="app-container" style="justify-content: center; align-items: center;">
    <div class="glass-card result-container">
        <c:choose>
            <c:when test="${status eq 'SUCCESS'}">
                <div class="result-icon icon-success">
                    <i class="fa-solid fa-circle-check"></i>
                </div>
                <h2 class="result-title">Thanh Toán Thành Công!</h2>
                <p class="result-message">
                    Hóa đơn của bạn đã được thanh toán và cập nhật trạng thái tự động trên hệ thống.<br>
                    Mã giao dịch: <b style="color:#00d8ff; font-family:monospace; font-size:1.1rem;">${param.vnp_TxnRef}</b>
                </p>
            </c:when>
            <c:otherwise>
                <div class="result-icon icon-failed">
                    <i class="fa-solid fa-circle-xmark"></i>
                </div>
                <h2 class="result-title">Thanh Toán Thất Bại</h2>
                <p class="result-message">
                    ${message}<br>
                    Giao dịch đã được ghi nhận thất bại hoặc bị hủy bỏ từ phía cổng thanh toán.
                </p>
            </c:otherwise>
        </c:choose>
        
        <div style="display: flex; justify-content: center; gap: 15px;">
            <a href="user-bills" class="btn-glass btn-glass-primary">
                <i class="fa-solid fa-file-invoice-dollar"></i> Xem danh sách hóa đơn
            </a>
            <a href="residentHome" class="btn-glass btn-glass-secondary">
                <i class="fa-solid fa-house"></i> Quay lại trang chủ
            </a>
        </div>
    </div>
</div>

</body>
</html>
