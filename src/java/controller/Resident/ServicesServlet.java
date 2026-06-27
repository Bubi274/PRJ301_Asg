package controller.Resident;

import dal.ServicesDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ServiceTypes;

@WebServlet("/services")
public class ServicesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        ServicesDAO dao = new ServicesDAO();
        String action = request.getParameter("action");

        int page = 1;
        int pageSize = 5; // Using 5 per page for a tighter, cleaner design
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                // Keep page = 1
            }
        }

        if ("add".equals(action)) {
            int total = dao.countAvailableServices(userId);
            int totalPages = (int) Math.ceil((double) total / pageSize);
            if (totalPages == 0) totalPages = 1;

            List<ServiceTypes> available = dao.getAvailableServicesPaging(userId, page, pageSize);

            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("allServices", available);
            request.getRequestDispatcher("/Resident/service-add.jsp").forward(request, response);
            return;
        }

        // Default list registered services
        List<ServiceTypes> myServices = dao.getServicesByResidentId(userId);
        int total = myServices.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages == 0) totalPages = 1;

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<ServiceTypes> pageList = new ArrayList<>();
        if (start < total) {
            pageList = myServices.subList(start, end);
        }

        request.setAttribute("myServices", pageList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/Resident/services.jsp").forward(request, response);
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

        ServicesDAO dao = new ServicesDAO();
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            String[] serviceIds = request.getParameterValues("serviceIds");
            if (serviceIds != null && serviceIds.length > 0) {
                UserDAO userDAO = new UserDAO();
                int apartmentId = userDAO.getApartmentIdByUser(userId);
                if (apartmentId > 0) {
                    dao.registerServices(userId, apartmentId, serviceIds);
                }
            }
        } else if ("cancel".equals(action)) {
            String serviceIdRaw = request.getParameter("serviceId");
            if (serviceIdRaw != null) {
                try {
                    int serviceId = Integer.parseInt(serviceIdRaw);
                    dao.cancelService(userId, serviceId);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }

        response.sendRedirect("services");
    }
}
