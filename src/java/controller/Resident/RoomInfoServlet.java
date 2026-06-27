package controller.Resident;

import dal.ApartmentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Apartments;

@WebServlet("/roomInfo")
public class RoomInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        ApartmentDAO apartmentDAO = new ApartmentDAO();
        Apartments myRoom = apartmentDAO.getApartmentByResidentId(userId);

        request.setAttribute("myRoom", myRoom);
        request.getRequestDispatcher("/Resident/roomInfo.jsp").forward(request, response);
    }
}
