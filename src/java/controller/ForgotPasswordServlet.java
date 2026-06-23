/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Servlet xử lý luồng Quên Mật Khẩu (Self-Service).
 * GET /forgotPassword → Hiển thị form quên mật khẩu.
 * POST /forgotPassword → Xác thực Username & Email, cấp mật khẩu mới.
 *
 * @author Truong An
 */
@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method — Hiển thị form.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method — Xác thực & Sinh mật khẩu mới.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");

        // 1. Xác thực Username và Email có khớp trong DB không
        if (username == null || username.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ Username và Email.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
            return;
        }

        boolean isValidUser = userDAO.verifyUsernameAndEmail(username.trim(), email.trim());

        if (!isValidUser) {
            request.setAttribute("error", "Thông tin không chính xác hoặc tài khoản đã bị khóa.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
            return;
        }

        // 2. Tạo mật khẩu ngẫu nhiên (8 ký tự: chữ + số)
        String newPassword = generateRandomPassword(8);
        String hashedNewPassword = sha256(newPassword);

        // 3. Cập nhật vào DB
        boolean success = userDAO.updatePassword(username.trim(), hashedNewPassword);

        if (success) {
            // Hiển thị mật khẩu trực tiếp lên màn hình
            request.setAttribute("success", true);
            request.setAttribute("message", "Khôi phục thành công! Mật khẩu mới của bạn là: <strong style='font-size:18px; color:#d35400;'>" 
                    + newPassword + "</strong><br><br>Vui lòng đăng nhập lại và đổi mật khẩu sớm nhất có thể.");
            request.setAttribute("backUrl", "login");
            request.getRequestDispatcher("resultMessage.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Lỗi hệ thống. Không thể đặt lại mật khẩu lúc này.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Xử lý quên mật khẩu (cấp lại mật khẩu mới).";
    }// </editor-fold>

    /**
     * Hash mật khẩu bằng SHA-256 (Copy từ LoginServlet/AddAccountServlet)
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

    /**
     * Sinh mật khẩu ngẫu nhiên gồm chữ và số
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
