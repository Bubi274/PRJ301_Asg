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
import java.util.List;
import model.Position;
import model.User;

/**
 * Servlet thêm nhân viên mới (UC-MN-06 - Create).
 * - Hiển thị form thêm nhân viên
 * - Xử lý lưu nhân viên mới vào database
 */
@WebServlet("/manager/staffs/add")
public class AddStaffServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();
    private final PositionDAO positionDAO = new PositionDAO();

    /**
     * Hiển thị form thêm nhân viên
     * Lấy danh sách vị trí cho dropdown
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy danh sách vị trí cho dropdown
        List<Position> positions = positionDAO.getAllPositions();
        request.setAttribute("positions", positions);

        request.getRequestDispatcher("/manager/addStaff.jsp").forward(request, response);
    }

/**
      * Xử lý POST: Lưu nhân viên mới vào database
      * 
      * Các bước xử lý:
      * 1. Lấy form data (fullName, username, email, phone, password, positionId)
      * 2. Validate server-side - bắt buộc có mọi field, format đúng
      * 3. Kiểm tra trùng: username+email đã tồn tại chưa
      * 4. Nếu lỗi → forward lại form, giữ input cũ
      * 5. Nếu OK → gọi StaffDAO.addStaff() trong transaction
      */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // KIỂM TRA SESSION - ManagerAuthFilter đã xử lý nhưng vẫn cần kiểm tra lại
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // BƯỚC 1: LẤY DỮ LIỆU TỪ FORM - request.getParameter()
        // Các field: fullName, username, email, phone, password, positionId
        
        // BƯỚC 2: TRIM DỮ LIỆU - Loại bỏ khoảng trắng thừa ở đầu và cuối
        // Quan trọng: Phải trim trước khi validate để tránh lỗi "không được để trống"
        
        // BƯỚC 3: VALIDATION - Kiểm tra lỗi phía server
        // a. Kiểm tra rỗng (required fields)
        // b. Kiểm tra độ dài (length validation)  
        // c. Kiểm tra định dạng (email format, phone number)
        // d. Kiểm tra trùng (username, email đã tồn tại)
        // e. Kiểm tra foreign key (positionId hợp lệ)
        
        // BƯỚC 4: XỬ LÝ LỖI - Forward lại form với error message
        // Set attribute: error, old input values, positions list
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String positionIdStr = request.getParameter("positionId");

        // Bước 2: Trim dữ liệu đầu vào
        if (fullName != null) fullName = fullName.trim();
        if (username != null) username = username.trim();
        if (email != null) email = email.trim();
        if (phone != null) phone = phone.trim();
        if (password != null) password = password.trim();

        // Bước 3: Validate dữ liệu (server-side)
        StringBuilder errors = new StringBuilder();
        int positionId = 0;
        
        // Kiểm tra họ tên
        if (fullName == null || fullName.isEmpty()) {
            errors.append("Họ tên nhân viên không được để trống. ");
        } else if (fullName.length() < 2 || fullName.length() > 100) {
            errors.append("Họ tên phải từ 2 đến 100 ký tự. ");
        }

        // Kiểm tra username
        if (username == null || username.isEmpty()) {
            errors.append("Tên đăng nhập không được để trống. ");
        } else if (username.length() < 5 || username.length() > 50) {
            errors.append("Tên đăng nhập phải từ 5 đến 50 ký tự. ");
        } else if (staffDAO.isUsernameExist(username)) {
            errors.append("Tên đăng nhập này đã tồn tại trong hệ thống. ");
        }

        // Kiểm tra email
        if (email == null || email.isEmpty()) {
            errors.append("Email không được để trống. ");
        } else if (!isValidEmail(email)) {
            errors.append("Định dạng email không hợp lệ. ");
        } else if (staffDAO.isEmailExist(email)) {
            errors.append("Email này đã được sử dụng. ");
        }

        // Kiểm tra số điện thoại (không bắt buộc nhưng phải đúng định dạng nếu có)
        if (phone != null && !phone.isEmpty() && !phone.matches("^\\d{10,15}$")) {
            errors.append("Số điện thoại phải là số từ 10 đến 15 chữ số. ");
        }

        // Kiểm tra mật khẩu
        if (password == null || password.isEmpty()) {
            errors.append("Mật khẩu không được để trống. ");
        } else if (password.length() < 6) {
            errors.append("Mật khẩu phải có ít nhất 6 ký tự. ");
        }

        // Kiểm tra vị trí
        if (positionIdStr == null || positionIdStr.isEmpty()) {
            errors.append("Vui lòng chọn vị trí cho nhân viên. ");
        } else {
            try {
                positionId = Integer.parseInt(positionIdStr);
                if (positionId <= 0) {
                    errors.append("Vị trí không hợp lệ. ");
                }
            } catch (NumberFormatException e) {
                errors.append("Vị trí không hợp lệ. ");
            }
        }

        // Bước 4: Xử lý lỗi - forward lại form
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("fullName", fullName);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("password", password);
            request.setAttribute("positionId", positionIdStr);

            List<Position> positions = positionDAO.getAllPositions();
            request.setAttribute("positions", positions);

            request.getRequestDispatcher("/manager/addStaff.jsp").forward(request, response);
            return;
        }

        // Bước 5: Tạo đối tượng User và lưu vào database
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone != null && !phone.isEmpty() ? phone : null);
        user.setPasswordHash(password); // Lưu password trực tiếp (khuyến nghị hash trong thực tế)

        try {
            staffDAO.addStaff(user, positionId);
            // Thành công - chuyển về danh sách với thông báo
            response.sendRedirect(request.getContextPath() + "/manager/staffs?msg=success");
        } catch (Exception e) {
            // Lỗi database
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("fullName", fullName);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);

            List<Position> positions = positionDAO.getAllPositions();
            request.setAttribute("positions", positions);

            request.getRequestDispatcher("/manager/addStaff.jsp").forward(request, response);
        }
    }

    /**
     * Kiểm tra định dạng email hợp lệ
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}