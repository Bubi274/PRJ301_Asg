package controller;

import dal.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Servlet xử lý luồng Đặt lại Mật khẩu (Self-Service Reset).
 * GET  /resetPassword → Hiển thị form đặt lại mật khẩu.
 * POST /resetPassword → Xác thực Username & Email, cho phép tự đặt mật khẩu mới.
 *
 * Khác với ForgotPasswordServlet (tự sinh mật khẩu ngẫu nhiên),
 * servlet này cho phép user TỰ CHỌN mật khẩu mới.
 *
 * @author Truong An
 */
@WebServlet("/resetPassword")
public class ResetPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    /**
     * GET /resetPassword → Hiển thị form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("reset_user") == null) {
            response.sendRedirect(request.getContextPath() + "/forgotPassword");
            return;
        }
        request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
    }

    /**
     * POST /resetPassword → Xác thực thông tin và cập nhật mật khẩu mới.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("reset_user");

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/forgotPassword");
            return;
        }

        String newPassword    = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Validate input không rỗng
        if (newPassword == null || newPassword.trim().isEmpty()
                || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ mật khẩu mới và xác nhận.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        // 2. Validate mật khẩu tối thiểu 6 ký tự
        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        // 3. Validate hai mật khẩu phải khớp nhau
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp. Vui lòng nhập lại.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        // 4. Hash và cập nhật mật khẩu mới vào DB
        String hashedPassword = sha256(newPassword);
        boolean success = userDAO.updatePassword(username, hashedPassword);

        if (success) {
            // Xóa session
            session.removeAttribute("reset_user");
            
            // Hiển thị thành công
            request.setAttribute("success", true);
            request.setAttribute("message", "Đặt lại mật khẩu thành công! Vui lòng sử dụng mật khẩu mới để đăng nhập.");
            request.setAttribute("backUrl", request.getContextPath() + "/login");
            request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Lỗi hệ thống. Không thể đặt lại mật khẩu lúc này.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý đặt lại mật khẩu tự chọn (Self-Service Reset).";
    }

    /**
     * Hash mật khẩu bằng SHA-256.
     */
    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
