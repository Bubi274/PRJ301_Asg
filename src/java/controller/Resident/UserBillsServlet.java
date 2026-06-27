package controller.Resident;

import dal.BillDAO;
import dal.PaymentDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Bills;
import model.Payments;

@WebServlet("/user-bills")
public class UserBillsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        UserDAO userDAO = new UserDAO();
        int apartmentId = userDAO.getApartmentIdByUser(userId);

        BillDAO billDAO = new BillDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        List<Bills> bills = billDAO.getUnpaidBillsByApartment(apartmentId);

        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");

        Integer month = null;
        Integer year = null;

        if (monthStr != null && !monthStr.isEmpty()) {
            try {
                month = Integer.parseInt(monthStr);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        if (yearStr != null && !yearStr.isEmpty()) {
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        List<Payments> payments = paymentDAO.getPaymentsByMonthYear(apartmentId, month, year);

        request.setAttribute("bills", bills);
        request.setAttribute("payments", payments);

        request.getRequestDispatcher("/Resident/user-bills.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
