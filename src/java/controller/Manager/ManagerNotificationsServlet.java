package controller.Manager;

import dal.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Notification;

/**
 * Servlet danh sách thông báo do Manager gửi (UC-MN-08 - Read).
 * - Hiển thị danh sách thông báo đã gửi
 * - Cho phép xem chi tiết và tạo thông báo mới
 */
@WebServlet("/manager/notifications")
public class ManagerNotificationsServlet extends HttpServlet {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * Xử lý GET: Hiển thị danh sách thông báo
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy managerId từ session
        int managerId = (Integer) session.getAttribute("userId");

        // Bước 2: Lấy danh sách thông báo đã gửi
        List<Notification> notifications = notificationDAO.getNotificationsBySender(managerId);


        // Bước 3: Set attribute cho JSP
        request.setAttribute("notifications", notifications);

        request.getRequestDispatcher("/manager/notifications.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}