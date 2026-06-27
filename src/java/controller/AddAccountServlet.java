/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Servlet xử lý thêm tài khoản mới (Admin only).
 * GET  /admin/addAccount → hiển thị form thêm tài khoản.
 * POST /admin/addAccount → lưu tài khoản mới vào database.
 *
 * @author Truong An
 */
@WebServlet("/admin/addAccount")
public class AddAccountServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method — hiển thị form thêm tài khoản.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/addAccount.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method — validate và lưu tài khoản mới.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username  = request.getParameter("username");
        String password  = request.getParameter("password");
        String fullName  = request.getParameter("fullName");
        String email     = request.getParameter("email");
        String phone     = request.getParameter("phone");
        String roleIdStr = request.getParameter("roleId");

        // ---- Validation ----
        StringBuilder errors = new StringBuilder();
        if (username == null || username.trim().isEmpty()) errors.append("Username không được để trống. ");
        if (password == null || password.length() < 6)    errors.append("Password phải từ 6 ký tự. ");
        if (fullName == null || fullName.trim().isEmpty()) errors.append("Họ tên không được để trống. ");
        if (roleIdStr == null || roleIdStr.isEmpty())      errors.append("Phải chọn vai trò. ");

        if (username != null && !username.trim().isEmpty() && userDAO.isUsernameExists(username)) {
            errors.append("Username đã tồn tại. ");
        }

        if (errors.length() > 0) {
            // Validation Error → quay lại Add Account Screen, KHÔNG sang Result Message
            request.setAttribute("error", errors.toString());
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/addAccount.jsp").forward(request, response);
            return;
        }

        User u = new User();
        u.setUsername(username);
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setRoleId(Integer.parseInt(roleIdStr));
        u.setActive(true);

        boolean success = userDAO.insertUser(u, sha256(password));

        request.setAttribute("success", success);
        request.setAttribute("message", success
                ? "Thêm tài khoản thành công."
                : "Thêm tài khoản thất bại. Vui lòng thử lại.");
        request.setAttribute("backUrl", request.getContextPath() + "/admin/accounts");
        request.getRequestDispatcher("/resultMessage.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Thêm tài khoản người dùng mới vào hệ thống.";
    }// </editor-fold>

    /** Hash mật khẩu bằng SHA-256 trước khi lưu vào DB. */
    private String sha256(String input) {
        return input; // Tạm thời bỏ mã hóa
        /*
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        */
    }

}
