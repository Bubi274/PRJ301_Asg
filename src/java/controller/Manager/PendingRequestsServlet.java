package controller.Manager;

import dal.AssignmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Request;

/**
 * Servlet quản lý danh sách yêu cầu chờ duyệt (UC-MN-07).
 * - Hiển thị danh sách các request có status "Pending"
 * - Cho phép Manager xem chi tiết và phân công nhân viên
 */
@WebServlet("/manager/pendingRequests")
public class PendingRequestsServlet extends HttpServlet {

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();

    /**
     * Xử lý GET: Hiển thị danh sách request chờ duyệt
     * ManagerAuthFilter đã kiểm tra session và role
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy danh sách request chờ duyệt
        List<Request> pendingRequests = assignmentDAO.getPendingRequests();

        // Bước 2: Set attribute cho JSP
        request.setAttribute("pendingRequests", pendingRequests);

        // Bước 3: Forward tới JSP
        request.getRequestDispatcher("/manager/pendingRequests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}