package controller.Resident;

import dal.RequestDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Request;
import model.RequestType;

/**
 * Servlet for resident to submit new support requests.
 */
@WebServlet("/residentCreateRequest")
public class ResidentCreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        RequestDAO requestDAO = new RequestDAO();
        List<RequestType> listTypes = requestDAO.getAllRequestTypes();
        
        request.setAttribute("listTypes", listTypes);
        request.getRequestDispatcher("/Resident/request-create.jsp").forward(request, response);
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

        String requestTypeIdRaw = request.getParameter("requestTypeId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        RequestDAO requestDAO = new RequestDAO();
        UserDAO userDAO = new UserDAO();

        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Tiêu đề không được để trống!");
            doGet(request, response);
            return;
        }

        int apartmentId = userDAO.getApartmentIdByUser(userId);
        if (apartmentId == -1) {
            request.setAttribute("errorMsg", "Không tìm thấy căn hộ hoạt động của bạn. Hãy liên hệ ban quản lý.");
            doGet(request, response);
            return;
        }

        try {
            int requestTypeId = Integer.parseInt(requestTypeIdRaw);
            Request req = new Request();
            req.setResidentId(userId);
            req.setApartmentId(apartmentId);
            req.setRequestTypeId(requestTypeId);
            req.setTitle(title.trim());
            req.setDescription(description);

            boolean success = requestDAO.insertRequest(req);
            if (success) {
                request.setAttribute("success", true);
                request.setAttribute("message", "Yêu cầu hỗ trợ của bạn đã được gửi thành công và đang chờ ban quản lý phê duyệt!");
                request.setAttribute("backUrl", "residentRequests");
                request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMsg", "Gửi yêu cầu thất bại. Vui lòng thử lại sau.");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMsg", "Dữ liệu phân loại không hợp lệ.");
            doGet(request, response);
        }
    }
}
