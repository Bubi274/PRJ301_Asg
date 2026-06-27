package controller.Resident;

import dal.NotificationDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Notification;
import model.Request;

@WebServlet("/residentHome")
public class ResidentHomeServlet extends HttpServlet {

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
        NotificationDAO notificationDAO = new NotificationDAO();

        // Handle single notification view via modal/pane
        String idRaw = request.getParameter("id");
        if (idRaw != null) {
            try {
                int notifId = Integer.parseInt(idRaw);
                Notification notif = notificationDAO.getNotificationById(notifId);
                request.setAttribute("notif", notif);
            } catch (NumberFormatException e) {
                // Ignore invalid ID
            }
        }

        List<Request> myRequests = userDAO.getRequestsByResidentId(userId);
        List<Notification> myNotifications = notificationDAO.getNotificationsForUser(userId);

        request.setAttribute("listReq", myRequests);
        request.setAttribute("listNotif", myNotifications);

        request.getRequestDispatcher("/Resident/home.jsp").forward(request, response);
    }
}
