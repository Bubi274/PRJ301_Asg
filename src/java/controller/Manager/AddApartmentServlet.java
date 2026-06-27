package controller.Manager;

import dal.ApartmentDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Apartments;
import model.User;

@WebServlet("/manager/addApartment")
public class AddApartmentServlet extends HttpServlet {

    private final ApartmentDAO apartmentDAO = new ApartmentDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get active residents list for dropdown selection
        List<User> residents = userDAO.getResidents();
        request.setAttribute("residents", residents);

        request.getRequestDispatcher("/manager/addApartment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Read parameters
        String apartmentNumber = request.getParameter("apartmentNumber");
        String floorStr = request.getParameter("floor");
        String areaStr = request.getParameter("area");
        String types = request.getParameter("types");
        String status = request.getParameter("status");
        String ownerIdStr = request.getParameter("ownerId");

        // 2. Trim inputs
        if (apartmentNumber != null) apartmentNumber = apartmentNumber.trim();
        if (floorStr != null) floorStr = floorStr.trim();
        if (areaStr != null) areaStr = areaStr.trim();
        if (types != null) types = types.trim();
        if (status != null) status = status.trim();
        if (ownerIdStr != null) ownerIdStr = ownerIdStr.trim();

        // 3. Server-side validation
        StringBuilder errors = new StringBuilder();
        
        if (apartmentNumber == null || apartmentNumber.isEmpty()) {
            errors.append("Số căn hộ không được để trống. ");
        } else if (apartmentDAO.isApartmentNumberExists(apartmentNumber)) {
            errors.append("Số căn hộ này đã tồn tại trong hệ thống. ");
        }

        int floor = 0;
        if (floorStr == null || floorStr.isEmpty()) {
            errors.append("Tầng không được để trống. ");
        } else {
            try {
                floor = Integer.parseInt(floorStr);
                if (floor <= 0) {
                    errors.append("Tầng phải là số nguyên dương lớn hơn 0. ");
                }
            } catch (NumberFormatException e) {
                errors.append("Định dạng tầng không hợp lệ (phải là số nguyên). ");
            }
        }

        double area = 0.0;
        if (areaStr == null || areaStr.isEmpty()) {
            errors.append("Diện tích không được để trống. ");
        } else {
            try {
                area = Double.parseDouble(areaStr);
                if (area <= 0) {
                    errors.append("Diện tích phải là số dương lớn hơn 0. ");
                }
            } catch (NumberFormatException e) {
                errors.append("Định dạng diện tích không hợp lệ (phải là số thực). ");
            }
        }

        if (types == null || types.isEmpty()) {
            errors.append("Loại căn hộ không được để trống. ");
        }

        if (status == null || status.isEmpty()) {
            errors.append("Trạng thái không được để trống. ");
        }

        int ownerId = 0;
        if (ownerIdStr != null && !ownerIdStr.isEmpty()) {
            try {
                ownerId = Integer.parseInt(ownerIdStr);
            } catch (NumberFormatException e) {
                // Ignore invalid format, default to 0
            }
        }

        // 4. Handle validation result
        if (errors.length() > 0) {
            // Forward back to form with inputs and errors
            request.setAttribute("error", errors.toString());
            request.setAttribute("apartmentNumber", apartmentNumber);
            request.setAttribute("floor", floorStr);
            request.setAttribute("area", areaStr);
            request.setAttribute("types", types);
            request.setAttribute("status", status);
            request.setAttribute("ownerId", ownerId);
            
            // Reload residents
            List<User> residents = userDAO.getResidents();
            request.setAttribute("residents", residents);
            
            request.getRequestDispatcher("/manager/addApartment.jsp").forward(request, response);
            return;
        }

        // 5. Create bean and save
        Apartments a = new Apartments();
        a.setApartmentNumber(apartmentNumber);
        a.setFloor(floor);
        a.setArea(area);
        a.setTypes(types);
        a.setStatus(status);
        a.setOwnerId(ownerId);

        boolean success = apartmentDAO.addApartment(a);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/manager/apartments?msg=success");
        } else {
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: Không thể thêm căn hộ mới.");
            request.setAttribute("apartmentNumber", apartmentNumber);
            request.setAttribute("floor", floorStr);
            request.setAttribute("area", areaStr);
            request.setAttribute("types", types);
            request.setAttribute("status", status);
            request.setAttribute("ownerId", ownerId);
            
            List<User> residents = userDAO.getResidents();
            request.setAttribute("residents", residents);
            
            request.getRequestDispatcher("/manager/addApartment.jsp").forward(request, response);
        }
    }
}
