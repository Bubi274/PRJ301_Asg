package controller.Resident;

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
 * Servlet for resident to view support request details and request cancellation.
 */
@WebServlet("/residentRequestDetail")
public class ResidentRequestDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String requestIdRaw = request.getParameter("requestId");
        if (requestIdRaw == null) {
            response.sendRedirect("residentRequests");
            return;
        }

        try {
            int requestId = Integer.parseInt(requestIdRaw);
            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestId);

            if (req == null || req.getResidentId() != userId) {
                response.sendRedirect("residentRequests");
                return;
            }

            request.setAttribute("req", req);
            request.getRequestDispatcher("/Resident/request-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("residentRequests");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String requestIdRaw = request.getParameter("requestId");
        String action = request.getParameter("action");

        if (requestIdRaw == null || !"cancel".equals(action)) {
            response.sendRedirect("residentRequests");
            return;
        }

        try {
            int requestId = Integer.parseInt(requestIdRaw);
            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestId);

            if (req == null || req.getResidentId() != userId) {
                request.setAttribute("success", false);
                request.setAttribute("message", "Yêu cầu không tồn tại hoặc bạn không có quyền thực hiện thao tác này.");
                request.setAttribute("backUrl", "residentRequests");
                request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
                return;
            }

            // Can only cancel Pending or Approved requests
            if (!"Pending".equalsIgnoreCase(req.getStatus()) && !"Approved".equalsIgnoreCase(req.getStatus())) {
                request.setAttribute("success", false);
                request.setAttribute("message", "Không thể hủy yêu cầu này vì yêu cầu đã được xử lý hoặc đã kết thúc.");
                request.setAttribute("backUrl", "residentRequests");
                request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
                return;
            }

            boolean success = requestDAO.updateRequestStatus(requestId, req.getStatus(), "Cancelled", userId);
            if (success) {
                request.setAttribute("success", true);
                request.setAttribute("message", "Hủy yêu cầu hỗ trợ thành công!");
                request.setAttribute("backUrl", "residentRequests");
            } else {
                request.setAttribute("success", false);
                request.setAttribute("message", "Hệ thống gặp lỗi khi hủy yêu cầu của bạn. Vui lòng thử lại sau.");
                request.setAttribute("backUrl", "residentRequests");
            }
            request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("residentRequests");
        }
    }
}
