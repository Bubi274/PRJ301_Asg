<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Công việc của tôi - Lumina BMS</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/resident_glass.css">
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
                        <!-- WORKING TASKS -->
                        <div class="glass-card">
                            <div class="section-header">
                                <h3>Công việc đang thực hiện</h3>
                            </div>

                            <c:choose>
                                <c:when test="${empty workingTasks}">
                                    <div class="empty-message">Hiện tại bạn không có công việc nào đang xử lý.</div>
                                </c:when>
                                <c:otherwise>
                                    <table class="glass-table">
                                        <thead>
                                            <tr>
                                                <th style="width: 80px;">Mã YC</th>
                                                <th>Cư dân</th>
                                                <th>Căn hộ</th>
                                                <th>Tiêu đề yêu cầu</th>
                                                <th>Trạng thái</th>
                                                <th>Ngày giao</th>
                                                <th style="width: 100px; text-align: center;">Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody id="working-tbody">
                                            <c:forEach items="${workingTasks}" var="r">
                                                <tr>
                                                    <td>#${r.requestId}</td>
                                                    <td>${r.residentName}</td>
                                                    <td>Phòng ${r.apartmentNumber}</td>
                                                    <td>${r.title}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${r.status eq 'Approved'}">
                                                                <span class="badge badge-warning">Đã duyệt (Chờ
                                                                    làm)</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-info">Đang xử lý</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy" />
                                                    </td>
                                                    <td style="text-align: center;">
                                                        <a href="taskDetail?requestId=${r.requestId}"
                                                            class="btn-glass btn-glass-primary"
                                                            style="padding: 6px 12px; font-size: 0.85rem;">
                                                            Chi tiết
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="pagination">
                                        <button class="pagination-btn" onclick="prevPage('working')"><i
                                                class="fa-solid fa-chevron-left"></i></button>
                                        <span id="working-page-info">Trang 1</span>
                                        <button class="pagination-btn" onclick="nextPage('working')"><i
                                                class="fa-solid fa-chevron-right"></i></button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- COMPLETED TASKS -->
                        <div class="glass-card">
                            <div class="section-header">
                                <h3>Công việc đã hoàn thành</h3>
                            </div>

                            <c:choose>
                                <c:when test="${empty completedTasks}">
                                    <div class="empty-message">Bạn chưa hoàn thành công việc nào.</div>
                                </c:when>
                                <c:otherwise>
                                    <table class="glass-table">
                                        <thead>
                                            <tr>
                                                <th style="width: 80px;">Mã YC</th>
                                                <th>Cư dân</th>
                                                <th>Căn hộ</th>
                                                <th>Tiêu đề yêu cầu</th>
                                                <th>Trạng thái</th>
                                                <th>Ngày giao</th>
                                                <th style="width: 100px; text-align: center;">Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody id="completed-tbody">
                                            <c:forEach items="${completedTasks}" var="r">
                                                <tr>
                                                    <td>#${r.requestId}</td>
                                                    <td>${r.residentName}</td>
                                                    <td>Phòng ${r.apartmentNumber}</td>
                                                    <td>${r.title}</td>
                                                    <td>
                                                        <span class="badge badge-primary">Hoàn thành</span>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy" />
                                                    </td>
                                                    <td style="text-align: center;">
                                                        <a href="taskDetail?requestId=${r.requestId}"
                                                            class="btn-glass btn-glass-secondary"
                                                            style="padding: 6px 12px; font-size: 0.85rem;">
                                                            Chi tiết
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <div class="pagination">
                                        <button class="pagination-btn" onclick="prevPage('completed')"><i
                                                class="fa-solid fa-chevron-left"></i></button>
                                        <span id="completed-page-info">Trang 1</span>
                                        <button class="pagination-btn" onclick="nextPage('completed')"><i
                                                class="fa-solid fa-chevron-right"></i></button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <script>
                    const ROWS_PER_PAGE = 5;
                    let pages = { working: 1, completed: 1 };

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

                    window.onload = function () {
                        renderTable('working');
                        renderTable('completed');
                    };
                </script>

            </body>

            </html>