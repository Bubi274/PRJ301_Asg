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
 * Servlet xử lý xóa (soft-delete) tài khoản (Admin only).
 * GET  /admin/deleteAccount → hiển thị màn hình xác nhận xóa.
 * POST /admin/deleteAccount → thực hiện xóa hoặc hủy thao tác.
 *
 * @author Truong An
 */
@WebServlet("/admin/deleteAccount")
public class DeleteAccountServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method — load thông tin tài khoản cần xóa.
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
        request.getRequestDispatcher("deleteConfirm.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method — xác nhận hoặc hủy xóa tài khoản.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action"); // "confirm" hoặc "cancel"

        if ("cancel".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/admin/accounts");
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        String result = userDAO.hardDeleteUser(userId); // SUCCESS / NOT_FOUND / CONSTRAINT / ERROR

        boolean success = "SUCCESS".equals(result);
        String message;
        switch (result) {
            case "SUCCESS":   message = "Xóa tài khoản vĩnh viễn thành công.";    break;
            case "NOT_FOUND": message = "Tài khoản không tồn tại.";             break;
            case "CONSTRAINT": message = "Không thể xóa! Tài khoản này đang dính dáng đến các dữ liệu khác (đơn từ, hợp đồng, v.v.). Bạn phải xử lý hết các dữ liệu đó trước khi có thể xóa tài khoản này."; break;
            default:          message = "Xóa tài khoản thất bại do lỗi hệ thống."; break;
        }

        request.setAttribute("success", success);
        request.setAttribute("message", message);
        request.setAttribute("backUrl", request.getContextPath() + "/admin/accounts");
        request.getRequestDispatcher("resultMessage.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Xóa (soft-delete) tài khoản người dùng khỏi hệ thống.";
    }// </editor-fold>

}
