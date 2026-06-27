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
 * Servlet xử lý đăng nhập vào hệ thống.
 * GET  /login → hiển thị form đăng nhập.
 * POST /login → xác thực thông tin đăng nhập.
 *
 * @author Truong An
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method — hiển thị trang đăng nhập.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method — xác thực username/password.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identifier = request.getParameter("username");
        String password   = request.getParameter("password");

        // Bước 1: Fetch user trước để lấy Username gốc (vì Stored Procedure cần Username)
        User user = userDAO.getByUsernameOrEmail(identifier);

        if (user == null) {
            request.setAttribute("error", "Tài khoản không tồn tại.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }



        if (!user.isActive()) {
            request.setAttribute("error", "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (!password.equals(user.getPasswordHash())) {
            request.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("userId",   user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("roleId",   user.getRoleId());
        session.setAttribute("fullName", user.getFullName());

        // Đóng vai trò là Cổng chính (Bàn đạp) cho toàn bộ hệ thống (5 Người)
        // Dựa vào RoleId để điều hướng về các màn hình Home tương ứng
        int roleId = user.getRoleId();
        
        switch (roleId) {
            case 1: // Admin
                response.sendRedirect("dashboard");
                break;
            case 2: // Manager
                // Người 2 & 3 sẽ code màn hình này (ví dụ: managerDashboard.jsp)
                response.sendRedirect("managerDashboard.jsp");
                break;
            case 3: // Staff
                // Người 4 sẽ code màn hình này (ví dụ: staffTaskList.jsp)
                response.sendRedirect("staffTaskList.jsp");
                break;
            case 4: // Resident
                // Người 5 sẽ code màn hình này (ví dụ: residentHome)
                response.sendRedirect("residentHome");
                break;
            default:
                response.sendRedirect("accessDenied.jsp");
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Xác thực đăng nhập vào hệ thống quản trị.";
    }// </editor-fold>



}
