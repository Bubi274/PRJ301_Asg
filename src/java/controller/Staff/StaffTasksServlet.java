package controller.Staff;

import dal.RequestDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Request;

/**
 * Servlet to list tasks assigned to the logged-in staff member.
 * Protected by StaffAuthFilter under /staff/*.
 */
@WebServlet("/staff/tasks")
public class StaffTasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        RequestDAO requestDAO = new RequestDAO();
        List<Request> all = requestDAO.getTasksByStaff(userId);

        List<Request> working = new ArrayList<>();
        List<Request> completed = new ArrayList<>();

        for (Request r : all) {
            if ("Cancelled".equalsIgnoreCase(r.getStatus())) {
                continue;
            }
            if ("Completed".equalsIgnoreCase(r.getStatus())) {
                completed.add(r);
            } else {
                working.add(r); // Includes Approved and Processing
            }
        }

        request.setAttribute("workingTasks", working);
        request.setAttribute("completedTasks", completed);

        request.getRequestDispatcher("/Staff/tasks.jsp").forward(request, response);
    }
}
