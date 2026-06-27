<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Chi tiết công việc - Lumina BMS</title>
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
                        Nhân viên: <b>
                            <%= session.getAttribute("fullName") %>
                        </b>
                        <a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-sign-out-alt"></i>
                            Đăng xuất</a>
                    </div>
                </header>

                <div class="app-container">
                    <!-- SIDEBAR -->
                    <div class="sidebar">
                        <div class="user-profile">
                            <div class="avatar-circle">
                                <i class="fa-solid fa-user-gear"></i>
                            </div>
                            <h4>${sessionScope.fullName}</h4>
                        </div>

                        <a href="${pageContext.request.contextPath}/staff/tasks" class="menu-item active"><i
                                class="fa-solid fa-list-check"></i>Công việc được giao</a>
                    </div>

                    <!-- MAIN CONTENT -->
                    <div class="main-content">
                        <div class="glass-card">
                            <div class="section-header">
                                <h3>Chi tiết yêu cầu hỗ trợ #${task.requestId}</h3>
                                <a href="tasks" class="btn-glass btn-glass-secondary">
                                    <i class="fa-solid fa-arrow-left"></i> Quay lại
                                </a>
                            </div>

                            <div class="detail-grid">
                                <div class="detail-item">
                                    <div class="detail-label">Cư dân yêu cầu</div>
                                    <div class="detail-value">${task.residentName}</div>
                                </div>

                                <div class="detail-item">
                                    <div class="detail-label">Căn hộ</div>
                                    <div class="detail-value">Phòng ${task.apartmentNumber}</div>
                                </div>

                                <div class="detail-item">
                                    <div class="detail-label">Tiêu đề công việc</div>
                                    <div class="detail-value">${task.title}</div>
                                </div>

                                <div class="detail-item">
                                    <div class="detail-label">Phân loại</div>
                                    <div class="detail-value">${task.requestTypeName}</div>
                                </div>

                                <div class="detail-item">
                                    <div class="detail-label">Trạng thái công việc</div>
                                    <div class="detail-value">
                                        <c:choose>
                                            <c:when test="${task.status eq 'Approved'}">
                                                <span class="badge badge-warning">Đã duyệt (Chờ làm)</span>
                                            </c:when>
                                            <c:when test="${task.status eq 'Processing'}">
                                                <span class="badge badge-info">Đang xử lý</span>
                                            </c:when>
                                            <c:when test="${task.status eq 'Completed'}">
                                                <span class="badge badge-primary">Hoàn thành</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Đã hủy</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="detail-item">
                                    <div class="detail-label">Ngày giao việc</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${task.createdAt}" pattern="HH:mm dd/MM/yyyy" />
                                    </div>
                                </div>

                                <div class="detail-item detail-full">
                                    <div class="detail-label">Mô tả chi tiết yêu cầu</div>
                                    <div class="detail-value" style="white-space: pre-wrap; line-height: 1.6;">
                                        ${task.description != null ? task.description : "Không có mô tả chi tiết."}
                                    </div>
                                </div>
                            </div>

                            <div
                                style="margin-top: 35px; border-top: 1px solid rgba(255, 255, 255, 0.1); padding-top: 25px; display: flex; gap: 15px;">
                                <c:choose>
                                    <c:when test="${task.status eq 'Approved'}">
                                        <button class="btn-glass btn-glass-primary" onclick="showModal('startModal')">
                                            <i class="fa-solid fa-play"></i> Bắt đầu xử lý
                                        </button>
                                    </c:when>
                                    <c:when test="${task.status eq 'Processing'}">
                                        <button class="btn-glass btn-glass-primary"
                                            style="background-color: #2ecc71; color: #052c16; box-shadow: 0 4px 15px rgba(46, 204, 113, 0.3);"
                                            onclick="showModal('completeModal')">
                                            <i class="fa-solid fa-check"></i> Hoàn thành công việc
                                        </button>
                                    </c:when>
                                </c:choose>
                                <a href="tasks" class="btn-glass btn-glass-secondary">Quay lại danh sách</a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- START PROCESSING CONFIRMATION MODAL -->
                <div class="modal-overlay" id="startModal" style="display: none;">
                    <div class="modal-content" style="width: 450px;">
                        <button class="modal-close" onclick="closeModal('startModal')"><i
                                class="fa-solid fa-xmark"></i></button>
                        <div class="modal-header">
                            <h3 class="modal-title" style="color: #00d8ff;"><i class="fa-solid fa-circle-play"></i> Bắt
                                đầu xử lý</h3>
                        </div>
                        <div class="modal-body">
                            <p>Bạn có muốn bắt đầu xử lý công việc này ngay bây giờ không?</p>
                            <p style="font-size: 0.9rem; color: rgba(255, 255, 255, 0.6);">Trạng thái công việc sẽ được
                                cập nhật thành "Đang xử lý".</p>
                        </div>
                        <div
                            style="margin-top: 25px; text-align: right; display: flex; gap: 10px; justify-content: flex-end;">
                            <form action="taskDetail" method="POST">
                                <input type="hidden" name="requestId" value="${task.requestId}">
                                <input type="hidden" name="action" value="start">
                                <button type="submit" class="btn-glass btn-glass-primary">Bắt đầu</button>
                            </form>
                            <button class="btn-glass btn-glass-secondary" onclick="closeModal('startModal')">Quay
                                lại</button>
                        </div>
                    </div>
                </div>

                <!-- COMPLETE TASK CONFIRMATION MODAL -->
                <div class="modal-overlay" id="completeModal" style="display: none;">
                    <div class="modal-content" style="width: 450px;">
                        <button class="modal-close" onclick="closeModal('completeModal')"><i
                                class="fa-solid fa-xmark"></i></button>
                        <div class="modal-header">
                            <h3 class="modal-title" style="color: #2ecc71;"><i class="fa-solid fa-circle-check"></i> Xác
                                nhận hoàn thành</h3>
                        </div>
                        <div class="modal-body">
                            <p>Bạn đã hoàn tất việc sửa chữa/xử lý yêu cầu hỗ trợ này chưa?</p>
                            <p style="font-size: 0.9rem; color: rgba(255, 255, 255, 0.6);">Trạng thái công việc sẽ
                                chuyển thành "Hoàn thành" và lưu lại nhật ký.</p>
                        </div>
                        <div
                            style="margin-top: 25px; text-align: right; display: flex; gap: 10px; justify-content: flex-end;">
                            <form action="taskDetail" method="POST">
                                <input type="hidden" name="requestId" value="${task.requestId}">
                                <input type="hidden" name="action" value="complete">
                                <button type="submit" class="btn-glass btn-glass-primary"
                                    style="background-color: #2ecc71; color: #052c16;">Hoàn thành</button>
                            </form>
                            <button class="btn-glass btn-glass-secondary" onclick="closeModal('completeModal')">Quay
                                lại</button>
                        </div>
                    </div>
                </div>

                <script>
                    function showModal(id) {
                        document.getElementById(id).style.display = 'flex';
                    }
                    function closeModal(id) {
                        document.getElementById(id).style.display = 'none';
                    }
                </script>

            </body>

            </html>