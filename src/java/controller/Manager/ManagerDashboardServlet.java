package controller.Manager;

import dal.ApartmentDAO;
import dal.AssignmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/manager/dashboard")
public class ManagerDashboardServlet extends HttpServlet {

    private final ApartmentDAO apartmentDAO = new ApartmentDAO();
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Fetch stats
        int totalApartments = apartmentDAO.countTotalApartments();
        int occupiedApartments = apartmentDAO.countApartmentsByStatus("Occupied");
        int vacantApartments = apartmentDAO.countApartmentsByStatus("Vacant");
        int repairApartments = apartmentDAO.countApartmentsByStatus("Under Repair");
        
        // Fetch pending requests count
        int pendingRequests = assignmentDAO.getPendingRequests().size();

        request.setAttribute("totalApartments", totalApartments);
        request.setAttribute("occupiedApartments", occupiedApartments);
        request.setAttribute("vacantApartments", vacantApartments);
        request.setAttribute("repairApartments", repairApartments);
        request.setAttribute("pendingRequests", pendingRequests);

        request.getRequestDispatcher("/manager/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
