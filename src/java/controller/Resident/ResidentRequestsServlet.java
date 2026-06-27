package controller.Resident;

import dal.RequestDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Request;

/**
 * Servlet for resident to view their own support requests.
 */
@WebServlet("/residentRequests")
public class ResidentRequestsServlet extends HttpServlet {

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
        List<Request> listReq = requestDAO.getRequestsByResident(userId);
        
        request.setAttribute("listReq", listReq);
        request.getRequestDispatcher("/Resident/requests.jsp").forward(request, response);
    }
}
