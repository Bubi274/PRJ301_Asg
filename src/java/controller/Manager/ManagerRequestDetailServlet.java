package controller.Manager;

import dal.AssignmentDAO;
import dal.RequestDAO;
import dal.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Request;
import model.RequestType;
import model.StaffProfile;

/**
 * Servlet chi tiết request để phân công nhân viên (UC-MN-07).
 * - Hiển thị chi tiết yêu cầu
 * - Lấy danh sách nhân viên phù hợp theo vị trí mặc định của loại yêu cầu
 * - Cho phép Manager chọn nhân viên để phân công
 */
@WebServlet("/manager/requestDetail")
public class ManagerRequestDetailServlet extends HttpServlet {

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Xử lý GET: Hiển thị chi tiết request và danh sách staff có thể phân công
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy requestId từ tham số
        String requestIdStr = request.getParameter("requestId");
        int requestId = 0;
        
        if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manager/pendingRequests");
            return;
        }
        
        try {
            requestId = Integer.parseInt(requestIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manager/pendingRequests");
            return;
        }

        // Bước 2: Lấy chi tiết request
        Request req = assignmentDAO.getRequestById(requestId);
        if (req == null) {
            response.sendRedirect(request.getContextPath() + "/manager/pendingRequests");
            return;
        }

        // Bước 3: Lấy danh sách loại yêu cầu để tìm vị trí mặc định
        List<RequestType> requestTypes = requestDAO.getAllRequestTypes();
        int defaultPositionId = 0;
        for (RequestType rt : requestTypes) {
            if (rt.getRequestTypeId() == req.getRequestTypeId()) {
                defaultPositionId = rt.getDefaultPositionId();
                break;
            }
        }

        // Bước 4: Lấy danh sách nhân viên theo vị trí mặc định
        List<StaffProfile> availableStaff = staffDAO.searchStaff("", defaultPositionId);

        // Bước 5: Set attribute cho JSP
        request.setAttribute("requestObj", req);
        request.setAttribute("availableStaff", availableStaff);
        request.setAttribute("positions", requestTypes); // Dùng cho hiển thị thông tin

        request.getRequestDispatcher("/manager/requestDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}