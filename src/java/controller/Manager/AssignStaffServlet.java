package controller.Manager;

import dal.AssignmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet xử lý phân công nhân viên cho yêu cầu (UC-MN-07 - Assign).
 * - Nhận requestId và staffId từ form
 * - Cập nhật trạng thái Request thành "Approved" và gán AssignedTo
 * - Ghi log vào RequestLogs
 */
@WebServlet("/manager/assignStaff")
public class AssignStaffServlet extends HttpServlet {

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();

    /**
     * Xử lý POST: Phân công nhân viên cho yêu cầu
     * Method GET được redirect về trang danh sách
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
        String requestIdStr = request.getParameter("requestId");
        String staffIdStr = request.getParameter("staffId");

        // Bước 2: Validate tham số đầu vào
        int requestId = 0;
        int staffId = 0;
        
        try {
            requestId = Integer.parseInt(requestIdStr);
        } catch (Exception e) {
            // requestId không hợp lệ
            request.setAttribute("success", false);
            request.setAttribute("message", "Mã yêu cầu không hợp lệ.");
            request.setAttribute("backUrl", request.getContextPath() + "/manager/pendingRequests");
            request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
            return;
        }
        
        try {
            staffId = Integer.parseInt(staffIdStr);
        } catch (Exception e) {
            // staffId không hợp lệ
            request.setAttribute("success", false);
            request.setAttribute("message", "Vui lòng chọn nhân viên phân công.");
            request.setAttribute("backUrl", request.getContextPath() + "/manager/requestDetail?requestId=" + requestId);
            request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
            return;
        }

        // Bước 3: Lấy managerId từ session
        int managerId = (Integer) session.getAttribute("userId");

        // Bước 4: Thực hiện phân công trong database
        try {
            assignmentDAO.assignRequest(requestId, staffId, managerId);
            
            // Thành công - chuyển về trang danh sách với thông báo
            response.sendRedirect(request.getContextPath() + "/manager/pendingRequests?msg=success");
        } catch (SQLException e) {
            // Lỗi - hiển thị thông báo lỗi
            request.setAttribute("success", false);
            request.setAttribute("message", "Lỗi phân công: " + e.getMessage());
            request.setAttribute("backUrl", request.getContextPath() + "/manager/requestDetail?requestId=" + requestId);
            request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/manager/pendingRequests");
    }
}