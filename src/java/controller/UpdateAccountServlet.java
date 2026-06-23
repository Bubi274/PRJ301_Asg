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

/**
 * Servlet xử lý cập nhật thông tin tài khoản (Admin only).
 * GET  /admin/updateAccount → hiển thị form cập nhật tài khoản.
 * POST /admin/updateAccount → lưu thay đổi vào database.
 *
 * @author Truong An
 */
@WebServlet("/admin/updateAccount")
public class UpdateAccountServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method — load thông tin tài khoản cần sửa.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User u = userDAO.getById(userId);
        request.setAttribute("user", u);
        request.getRequestDispatcher("updateAccount.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method — validate và cập nhật tài khoản.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int    userId    = Integer.parseInt(request.getParameter("userId"));
        String fullName  = request.getParameter("fullName");
        String email     = request.getParameter("email");
        String phone     = request.getParameter("phone");
        String roleIdStr = request.getParameter("roleId");
        boolean isActive = "on".equals(request.getParameter("isActive"));

        // ---- Validation ----
        StringBuilder errors = new StringBuilder();
        if (fullName == null || fullName.trim().isEmpty()) errors.append("Họ tên không được để trống. ");
        if (roleIdStr == null || roleIdStr.isEmpty())      errors.append("Phải chọn vai trò. ");

        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("user", userDAO.getById(userId));
            request.getRequestDispatcher("updateAccount.jsp").forward(request, response);
            return;
        }

        User u = new User();
        u.setUserId(userId);
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setRoleId(Integer.parseInt(roleIdStr));
        u.setActive(isActive);

        boolean success = userDAO.updateUser(u);

        request.setAttribute("success", success);
        request.setAttribute("message", success
                ? "Cập nhật tài khoản thành công."
                : "Cập nhật tài khoản thất bại.");
        request.setAttribute("backUrl", request.getContextPath() + "/admin/accounts");
        request.getRequestDispatcher("resultMessage.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Cập nhật thông tin tài khoản người dùng.";
    }// </editor-fold>

}
