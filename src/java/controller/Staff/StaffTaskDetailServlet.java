package controller.Staff;

import dal.RequestDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Request;

/**
 * Servlet to view staff task details and perform actions (start/complete).
 * Protected by StaffAuthFilter under /staff/*.
 */
@WebServlet("/staff/taskDetail")
public class StaffTaskDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        String requestIdRaw = request.getParameter("requestId");
        if (requestIdRaw == null) {
            response.sendRedirect("tasks");
            return;
        }

        try {
            int requestId = Integer.parseInt(requestIdRaw);
            RequestDAO requestDAO = new RequestDAO();
            Request task = requestDAO.getRequestById(requestId);

            if (task == null || task.getAssignedTo() != userId) {
                response.sendRedirect("tasks");
                return;
            }

            request.setAttribute("task", task);
            request.getRequestDispatcher("/Staff/task-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("tasks");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        String requestIdRaw = request.getParameter("requestId");
        String action = request.getParameter("action");

        if (requestIdRaw == null || action == null) {
            response.sendRedirect("tasks");
            return;
        }

        try {
            int requestId = Integer.parseInt(requestIdRaw);
            RequestDAO requestDAO = new RequestDAO();
            Request task = requestDAO.getRequestById(requestId);

            if (task == null || task.getAssignedTo() != userId) {
                request.setAttribute("success", false);
                request.setAttribute("message", "Công việc không tồn tại hoặc bạn không có quyền thực hiện.");
                request.setAttribute("backUrl", "tasks");
                request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
                return;
            }

            if ("start".equals(action)) {
                if (!"Approved".equalsIgnoreCase(task.getStatus())) {
                    request.setAttribute("success", false);
                    request.setAttribute("message", "Chỉ có thể bắt đầu công việc đang ở trạng thái 'Đã duyệt'.");
                    request.setAttribute("backUrl", "taskDetail?requestId=" + requestId);
                    request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
                    return;
                }
                boolean success = requestDAO.updateRequestStatus(requestId, task.getStatus(), "Processing", userId);
                if (success) {
                    response.sendRedirect("taskDetail?requestId=" + requestId);
                } else {
                    request.setAttribute("success", false);
                    request.setAttribute("message", "Gặp lỗi khi bắt đầu xử lý công việc.");
                    request.setAttribute("backUrl", "taskDetail?requestId=" + requestId);
                    request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
                }
            } else if ("complete".equals(action)) {
                if (!"Processing".equalsIgnoreCase(task.getStatus())) {
                    request.setAttribute("success", false);
                    request.setAttribute("message", "Chỉ có thể hoàn thành công việc đang ở trạng thái 'Đang xử lý'.");
                    request.setAttribute("backUrl", "taskDetail?requestId=" + requestId);
                    request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
                    return;
                }
                boolean success = requestDAO.updateRequestStatus(requestId, task.getStatus(), "Completed", userId);
                if (success) {
                    request.setAttribute("success", true);
                    request.setAttribute("message", "Chúc mừng! Bạn đã hoàn thành công việc xuất sắc!");
                    request.setAttribute("backUrl", "tasks");
                } else {
                    request.setAttribute("success", false);
                    request.setAttribute("message", "Gặp lỗi khi xác nhận hoàn thành công việc.");
                    request.setAttribute("backUrl", "taskDetail?requestId=" + requestId);
                }
                request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
            } else {
                response.sendRedirect("tasks");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("tasks");
        }
    }
}
