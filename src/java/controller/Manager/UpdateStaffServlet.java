package controller.Manager;

import dal.PositionDAO;
import dal.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Position;
import model.StaffProfile;
import model.User;

/**
 * Servlet cập nhật thông tin nhân viên (UC-MN-06 - Update).
 * - Hiển thị form cập nhật thông tin nhân viên
 * - Xử lý cập nhật thông tin và vị trí nhân viên
 */
@WebServlet("/manager/staffs/edit")
public class UpdateStaffServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();
    private final PositionDAO positionDAO = new PositionDAO();

    /**
     * Hiển thị form cập nhật nhân viên
     * Lấy thông tin nhân viên và danh sách vị trí
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy userId từ tham số
        String userIdStr = request.getParameter("userId");
        int userId = 0;
        
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manager/staffs");
            return;
        }
        
        try {
            userId = Integer.parseInt(userIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/manager/staffs");
            return;
        }

        // Bước 2: Lấy thông tin nhân viên
        StaffProfile staff = staffDAO.getStaffById(userId);
        if (staff == null) {
            request.setAttribute("error", "Không tìm thấy nhân viên với ID đã cho.");
            request.getRequestDispatcher("/manager/staffs").forward(request, response);
            return;
        }

        // Bước 3: Lấy danh sách vị trí cho dropdown
        List<Position> positions = positionDAO.getAllPositions();

        // Bước 4: Set attribute cho JSP
        request.setAttribute("staff", staff);
        request.setAttribute("positions", positions);

        request.getRequestDispatcher("/manager/updateStaff.jsp").forward(request, response);
    }

    /**
     * Xử lý POST: Cập nhật thông tin nhân viên
     * - Validate dữ liệu
     * - Gọi StaffDAO.updateStaff() để cập nhật
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy tham số từ form
        String userIdStr = request.getParameter("userId");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String positionIdStr = request.getParameter("positionId");

        // Bước 2: Trim dữ liệu
        if (fullName != null) fullName = fullName.trim();
        if (email != null) email = email.trim();
        if (phone != null) phone = phone.trim();

        // Bước 3: Validate dữ liệu
        StringBuilder errors = new StringBuilder();
        int userId = 0;
        
        // Kiểm tra userId hợp lệ
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            errors.append("ID nhân viên không hợp lệ. ");
        }

        // Kiểm tra họ tên
        if (fullName == null || fullName.isEmpty()) {
            errors.append("Họ tên nhân viên không được để trống. ");
        } else if (fullName.length() < 2 || fullName.length() > 100) {
            errors.append("Họ tên phải từ 2 đến 100 ký tự. ");
        }

        // Kiểm tra email
        if (email == null || email.isEmpty()) {
            errors.append("Email không được để trống. ");
        } else if (!isValidEmail(email)) {
            errors.append("Định dạng email không hợp lệ. ");
        } else if (userId > 0 && staffDAO.isEmailExistExcept(email, userId)) {
            errors.append("Email này đã được sử dụng bởi tài khoản khác. ");
        }

        // Kiểm tra số điện thoại
        if (phone != null && !phone.isEmpty() && !phone.matches("^\\d{10,15}$")) {
            errors.append("Số điện thoại phải là số từ 10 đến 15 chữ số. ");
        }

        // Kiểm tra vị trí
        int positionId = 0;
        if (positionIdStr == null || positionIdStr.isEmpty()) {
            errors.append("Vui lòng chọn vị trí cho nhân viên. ");
        } else {
            try {
                positionId = Integer.parseInt(positionIdStr);
            } catch (NumberFormatException e) {
                errors.append("Vị trí không hợp lệ. ");
            }
        }

        // Bước 4: Xử lý lỗi - forward lại form
        if (errors.length() > 0) {
            // Lấy lại thông tin staff hiện tại
            StaffProfile existingStaff = staffDAO.getStaffById(userId);
            List<Position> positions = positionDAO.getAllPositions();
            
            request.setAttribute("error", errors.toString());
            request.setAttribute("staff", existingStaff);
            request.setAttribute("positions", positions);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("positionId", positionIdStr);

            request.getRequestDispatcher("/manager/updateStaff.jsp").forward(request, response);
            return;
        }

        // Bước 5: Tạo đối tượng User và cập nhật
        User user = new User();
        user.setUserId(userId);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone != null && !phone.isEmpty() ? phone : null);

        try {
            staffDAO.updateStaff(user, positionId);
            // Thành công
            response.sendRedirect(request.getContextPath() + "/manager/staffs?msg=success");
        } catch (SQLException e) {
            // Lỗi database
            StaffProfile existingStaff = staffDAO.getStaffById(userId);
            List<Position> positions = positionDAO.getAllPositions();
            
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("staff", existingStaff);
            request.setAttribute("positions", positions);

            request.getRequestDispatcher("/manager/updateStaff.jsp").forward(request, response);
        }
    }

    /**
     * Kiểm tra định dạng email hợp lệ
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}