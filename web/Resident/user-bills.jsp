<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,model.Bills,model.Payments" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hóa đơn & Thanh toán - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
    <style>
        .filter-form {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 25px;
            background: rgba(255, 255, 255, 0.03);
            padding: 15px 20px;
            border-radius: 8px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            width: fit-content;
        }
        .filter-form select {
            background: rgba(0, 30, 40, 0.6);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #fff;
            padding: 8px 12px;
            border-radius: 6px;
            outline: none;
            cursor: pointer;
        }
        .filter-form select:focus {
            border-color: #00d8ff;
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
        <a href="user-bills" class="menu-item active"><i class="fa-solid fa-credit-card"></i>Thanh toán hóa đơn</a>
        <a href="residentRequests" class="menu-item"><i class="fa-solid fa-envelope-open-text"></i>Yêu cầu hỗ trợ</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">
        <div class="glass-card">
            <h1 class="banner-title">Thanh toán Dịch vụ</h1>
            <p class="banner-subtitle">Xem các hóa đơn chưa thanh toán và thực hiện thanh toán trực tuyến qua cổng VNPAY.</p>
        </div>
        
        <!-- UNPAID BILLS -->
        <div class="glass-card">
            <div class="section-header">
                <h3>Hóa đơn chưa thanh toán</h3>
            </div>
            
            <c:choose>
                <c:when test="${empty bills}">
                    <div class="empty-message">Tuyệt vời! Bạn không có hóa đơn nào chưa thanh toán.</div>
                </c:when>
                <c:otherwise>
                    <form action="create-payment" method="post" onsubmit="return validatePayment()">
                        <table class="glass-table">
                            <thead>
                                <tr>
                                    <th style="width: 80px; text-align: center;">Chọn</th>
                                    <th>Tên dịch vụ</th>
                                    <th>Kỳ thanh toán</th>
                                    <th>Tổng cộng</th>
                                    <th>Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${bills}" var="b">
                                    <tr>
                                        <td style="text-align: center;">
                                            <input type="checkbox" name="billIds" value="${b.billId}" 
                                                   style="width: 18px; height: 18px; cursor: pointer; accent-color: #00d8ff;"/>
                                        </td>
                                        <td style="font-weight: 600; color: #00d8ff;">${b.serviceName}</td>
                                        <td>Tháng ${b.billingMonth}/${b.billingYear}</td>
                                        <td style="font-weight: 600;">${b.totalAmount} VNĐ</td>
                                        <td><span class="badge badge-warning">Chưa nộp</span></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        
                        <c:if test="${not empty errBill}">
                            <div style="color: #ff5b7f; margin: 15px 0; font-weight: 600;">
                                ⚠️ ${errBill}
                            </div>
                        </c:if>
                        <p id="payError" style="color: #ff5b7f; margin: 15px 0; font-weight: 600; display: none;"></p>
                        
                        <div style="margin-top: 25px;">
                            <button type="submit" class="btn-glass btn-glass-primary">
                                <i class="fa-solid fa-money-bill-wave"></i> Thanh toán qua VNPAY
                            </button>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- PAYMENT HISTORY -->
        <div class="glass-card">
            <div class="section-header">
                <h3>Lịch sử thanh toán thành công</h3>
            </div>
            
            <%
                String selectedMonth = request.getParameter("month");
                String selectedYear = request.getParameter("year");
            %>
            <form action="user-bills" method="get" class="filter-form">
                <div>
                    Tháng:
                    <select name="month">
                        <option value="">Tất cả</option>
                        <% for (int i = 1; i <= 12; i++) { %>
                            <option value="<%= i %>" <%= (String.valueOf(i).equals(selectedMonth)) ? "selected" : "" %>>Tháng <%= i %></option>
                        <% } %>
                    </select>
                </div>
                
                <div>
                    Năm:
                    <select name="year">
                        <option value="">Tất cả</option>
                        <% 
                            int curYear = java.time.Year.now().getValue();
                            for (int i = curYear - 2; i <= curYear + 1; i++) { 
                        %>
                            <option value="<%= i %>" <%= (String.valueOf(i).equals(selectedYear)) ? "selected" : "" %>><%= i %></option>
                        <% } %>
                    </select>
                </div>
                
                <button type="submit" class="btn-glass btn-glass-secondary" style="padding: 8px 16px; font-size: 0.85rem;">
                    <i class="fa-solid fa-filter"></i> Lọc
                </button>
            </form>

            <c:choose>
                <c:when test="${empty payments}">
                    <div class="empty-message">Không tìm thấy giao dịch nào trong khoảng thời gian đã chọn.</div>
                </c:when>
                <c:otherwise>
                    <table class="glass-table">
                        <thead>
                            <tr>
                                <th>Mã giao dịch</th>
                                <th>Ngày giao dịch</th>
                                <th>Số tiền</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${payments}" var="p">
                                <tr>
                                    <td style="font-family: monospace; font-size: 1rem; color: #00d8ff;">${p.transactionCode}</td>
                                    <td>${p.paymentDate}</td>
                                    <td style="font-weight: 600;">${p.amount} VNĐ</td>
                                    <td><span class="badge badge-primary">Thành công</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    function validatePayment() {
        const checkboxes = document.querySelectorAll("input[name='billIds']");
        const error = document.getElementById("payError");
        let checked = false;

        checkboxes.forEach(cb => {
            if (cb.checked) {
                checked = true;
            }
        });

        if (!checked) {
            error.innerText = "⚠️ Bạn vui lòng chọn ít nhất một hóa đơn để tiến hành thanh toán!";
            error.style.display = "block";
            return false;
        }

        error.style.display = "none";
        return true;
    }
</script>

</body>
</html>
