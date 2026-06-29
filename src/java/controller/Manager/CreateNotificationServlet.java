package controller.Manager;

import dal.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Notification;

/**
 * Servlet tạo thông báo chung (UC-MN-08 - Create).
 * - Hiển thị form tạo thông báo
 * - Xử lý lưu thông báo vào database
 */
@WebServlet("/manager/notifications/create")
public class CreateNotificationServlet extends HttpServlet {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * Hiển thị form tạo thông báo
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/manager/createNotification.jsp").forward(request, response);
    }

    /**
     * Xử lý POST: Lưu thông báo mới
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy tham số từ form
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // Bước 2: Trim dữ liệu
        if (title != null) title = title.trim();
        if (content != null) content = content.trim();

        // Bước 3: Validate dữ liệu
        StringBuilder errors = new StringBuilder();
        
        if (title == null || title.isEmpty()) {
            errors.append("Tiêu đề thông báo không được để trống. ");
        } else if (title.length() < 5 || title.length() > 200) {
            errors.append("Tiêu đề phải từ 5 đến 200 ký tự. ");
        }

        if (content == null || content.isEmpty()) {
            errors.append("Nội dung thông báo không được để trống. ");
        }

        // Bước 4: Xử lý lỗi
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("title", title);
            request.setAttribute("content", content);
            request.getRequestDispatcher("/manager/createNotification.jsp").forward(request, response);
            return;
        }

        // Bước 5: Lấy managerId từ session
        int managerId = (Integer) session.getAttribute("userId");

        // Bước 6: Gửi thông báo (broadcast toàn hệ thống)
        try {
            notificationDAO.sendBroadcast(title, content, Notification.TYPE_SYSTEM, managerId);
            
            // Thành công - chuyển về danh sách thông báo
            response.sendRedirect(request.getContextPath() + "/manager/notifications?msg=success");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("title", title);
            request.setAttribute("content", content);
            request.getRequestDispatcher("/manager/createNotification.jsp").forward(request, response);
        }
    }
}