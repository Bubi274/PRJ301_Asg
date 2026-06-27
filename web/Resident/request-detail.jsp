<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết yêu cầu - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
    <style>
        .detail-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-top: 20px;
        }
        .detail-item {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            padding: 18px;
            border-radius: 10px;
        }
        .detail-label {
            font-size: 0.85rem;
            color: rgba(255, 255, 255, 0.5);
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 6px;
        }
        .detail-value {
            font-size: 1.05rem;
            color: white;
            font-weight: 500;
        }
        .detail-full {
            grid-column: span 2;
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
                <h3>Chi tiết yêu cầu hỗ trợ #${req.requestId}</h3>
                <a href="residentRequests" class="btn-glass btn-glass-secondary">
                    <i class="fa-solid fa-arrow-left"></i> Quay lại
                </a>
            </div>

            <div class="detail-grid">
                <div class="detail-item">
                    <div class="detail-label">Tiêu đề</div>
                    <div class="detail-value">${req.title}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Phân loại</div>
                    <div class="detail-value">${req.requestTypeName}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Trạng thái</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${req.status eq 'Pending'}">
                                <span class="badge badge-warning">Chờ duyệt</span>
                            </c:when>
                            <c:when test="${req.status eq 'Approved'}">
                                <span class="badge badge-info">Đã duyệt</span>
                            </c:when>
                            <c:when test="${req.status eq 'Processing'}">
                                <span class="badge badge-info">Đang xử lý</span>
                            </c:when>
                            <c:when test="${req.status eq 'Completed'}">
                                <span class="badge badge-primary">Hoàn thành</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Đã hủy</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Ngày gửi</div>
                    <div class="detail-value"><fmt:formatDate value="${req.createdAt}" pattern="HH:mm dd/MM/yyyy"/></div>
                </div>

                <div class="detail-item detail-full">
                    <div class="detail-label">Mô tả chi tiết</div>
                    <div class="detail-value" style="white-space: pre-wrap; line-height: 1.6;">${req.description != null ? req.description : "Không có mô tả chi tiết."}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Nhân viên phụ trách</div>
                    <div class="detail-value">${req.assignedToName != null ? req.assignedToName : "Chưa phân công"}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Người phê duyệt</div>
                    <div class="detail-value">${req.approvedByName != null ? req.approvedByName : "Chưa phê duyệt"}</div>
                </div>
            </div>

            <c:if test="${req.status eq 'Pending' or req.status eq 'Approved'}">
                <div style="margin-top: 35px; border-top: 1px solid rgba(255, 255, 255, 0.1); padding-top: 25px;">
                    <button class="btn-glass btn-glass-danger" onclick="showCancelModal()">
                        <i class="fa-solid fa-ban"></i> Hủy yêu cầu này
                    </button>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- CANCEL CONFIRMATION MODAL -->
<div class="modal-overlay" id="cancelModal" style="display: none;">
    <div class="modal-content" style="width: 450px;">
        <button class="modal-close" onclick="closeCancelModal()"><i class="fa-solid fa-xmark"></i></button>
        <div class="modal-header">
            <h3 class="modal-title" style="color: #ff5b7f;"><i class="fa-solid fa-circle-question"></i> Xác nhận hủy</h3>
        </div>
        <div class="modal-body">
            <p>Bạn có chắc chắn muốn hủy yêu cầu hỗ trợ này không?</p>
            <p style="font-size: 0.9rem; color: rgba(255, 255, 255, 0.6);">Hành động này không thể hoàn tác.</p>
        </div>
        <div style="margin-top: 25px; text-align: right; display: flex; gap: 10px; justify-content: flex-end;">
            <form action="residentRequestDetail" method="POST">
                <input type="hidden" name="requestId" value="${req.requestId}">
                <input type="hidden" name="action" value="cancel">
                <button type="submit" class="btn-glass btn-glass-danger">Hủy yêu cầu</button>
            </form>
            <button class="btn-glass btn-glass-secondary" onclick="closeCancelModal()">Quay lại</button>
        </div>
    </div>
</div>

<script>
    function showCancelModal() {
        document.getElementById('cancelModal').style.display = 'flex';
    }
    function closeCancelModal() {
        document.getElementById('cancelModal').style.display = 'none';
    }
</script>

</body>
</html>
