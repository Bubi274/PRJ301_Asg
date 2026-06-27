package controller.Manager;

import dal.ApartmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Apartments;

@WebServlet("/manager/apartments")
public class ApartmentListServlet extends HttpServlet {

    private final ApartmentDAO apartmentDAO = new ApartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Get search and filter parameters
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String floorStr = request.getParameter("floor");
        
        Integer floor = null;
        if (floorStr != null && !floorStr.trim().isEmpty()) {
            try {
                floor = Integer.parseInt(floorStr.trim());
            } catch (NumberFormatException e) {
                // Invalid floor format, ignore
            }
        }

        // 2. Pagination calculation
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageStr.trim());
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int pageSize = 5; // Easily testable pagination size

        int totalItems = apartmentDAO.countAllApartments(search, status, floor);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages == 0) totalPages = 1;
        
        if (page > totalPages) {
            page = totalPages;
        }

        int offset = (page - 1) * pageSize;

        // 3. Query apartments
        List<Apartments> apartmentsList = apartmentDAO.getAllApartments(search, status, floor, offset, pageSize);

        // 4. Set request attributes
        request.setAttribute("apartments", apartmentsList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("floor", floorStr); // Keep the raw string value for input value attribute

        request.getRequestDispatcher("/manager/apartments.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
