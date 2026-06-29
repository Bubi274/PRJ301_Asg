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
import model.StaffProfile;

/**
 * Servlet quản lý danh sách nhân viên (UC-MN-06).
 * - Hiển thị danh sách nhân viên với tìm kiếm và lọc theo vị trí
 * - Phân trang
 */
@WebServlet("/manager/staffs")
public class StaffListServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();
    private final PositionDAO positionDAO = new PositionDAO();

    /**
     * Xử lý GET: Hiển thị danh sách nhân viên
     * Kiểm tra session và role đã được ManagerAuthFilter xử lý
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Bước 1: Lấy tham số tìm kiếm
        String search = request.getParameter("search");
        if (search == null) {
            search = "";
        }
        
        // Bước 2: Lấy tham số lọc vị trí
        int positionId = 0;
        String positionIdStr = request.getParameter("positionId");
        if (positionIdStr != null && !positionIdStr.trim().isEmpty()) {
            try {
                positionId = Integer.parseInt(positionIdStr.trim());
            } catch (NumberFormatException e) {
                positionId = 0;
            }
        }

        // Bước 3: Tính toán phân trang
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageStr.trim());
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int pageSize = 10; // Số nhân viên mỗi trang

        // Bước 4: Lấy danh sách positions cho dropdown
        List<Position> positions = positionDAO.getAllPositions();
        request.setAttribute("positions", positions);

        // Bước 5: Lấy danh sách nhân viên theo điều kiện tìm kiếm
        List<StaffProfile> staffList = staffDAO.searchStaff(search.trim(), positionId);
        
        // Bước 6: Tính toán phân trang trên kết quả
        int totalItems = staffList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages == 0) totalPages = 1;
        
        if (page > totalPages) {
            page = totalPages;
        }

        int offset = (page - 1) * pageSize;

        // Bước 7: Cắt danh sách theo offset và pageSize (xử lý edge case)
        int toIndex = Math.min(offset + pageSize, totalItems);
        List<StaffProfile> paginatedList;
        if (totalItems > 0) {
            paginatedList = staffList.subList(offset, toIndex);
        } else {
            paginatedList = staffList;
        }

        // Bước 8: Set attribute cho JSP
        request.setAttribute("staffList", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("search", search);
        request.setAttribute("positionId", positionId);
        
        // Bước 9: Forward tới JSP
        request.getRequestDispatcher("/manager/staffs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}