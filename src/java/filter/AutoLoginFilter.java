package filter;

import dal.UserDAO;
import model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Filter tự động khôi phục session từ cookie khi người dùng tắt và mở lại trình duyệt
 * hoặc khởi động lại Tomcat.
 *
 * Luồng hoạt động:
 *  1. Nếu session hợp lệ → bỏ qua, tiếp tục.
 *  2. Nếu session null nhưng cookie "remem_uid" tồn tại → load user từ DB → tạo lại session.
 *  3. Nếu không có cookie → để các AuthFilter chặn và redirect về login.
 *
 * Thứ tự filter: AutoLoginFilter chạy trước AdminAuthFilter, ManagerAuthFilter, StaffAuthFilter.
 */
@WebFilter(filterName = "AutoLoginFilter", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST})
public class AutoLoginFilter implements Filter {

    /** Tên cookie lưu userId để tự động đăng nhập lại */
    public static final String COOKIE_NAME = "remem_uid";

    /** Thời gian sống của cookie: 30 ngày (giây) */
    public static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60;

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Bước 1: Nếu session đã có userId thì không cần làm gì thêm
        HttpSession session = req.getSession(false);
        boolean sessionValid = (session != null && session.getAttribute("userId") != null);

        if (!sessionValid) {
            // Bước 2: Tìm cookie remem_uid
            String rememberedUserId = getCookieValue(req, COOKIE_NAME);

            if (rememberedUserId != null) {
                try {
                    int userId = Integer.parseInt(rememberedUserId);
                    User user = userDAO.getById(userId);

                    if (user != null && user.isActive()) {
                        // Tạo session mới và nạp thông tin user
                        HttpSession newSession = req.getSession(true);
                        newSession.setAttribute("userId",   user.getUserId());
                        newSession.setAttribute("username", user.getUsername());
                        newSession.setAttribute("roleId",   user.getRoleId());
                        newSession.setAttribute("fullName", user.getFullName());
                    } else {
                        // User không còn tồn tại hoặc đã bị khoá → xóa cookie
                        deleteCookie(resp, COOKIE_NAME);
                    }
                } catch (NumberFormatException e) {
                    // Cookie bị hỏng → xóa đi
                    deleteCookie(resp, COOKIE_NAME);
                }
            }
        }

        chain.doFilter(request, response);
    }

    /** Đọc giá trị cookie theo tên, trả về null nếu không tìm thấy */
    public static String getCookieValue(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

    /** Xóa cookie bằng cách set MaxAge = 0 */
    public static void deleteCookie(HttpServletResponse resp, String name) {
        Cookie c = new Cookie(name, "");
        c.setMaxAge(0);
        c.setPath("/");
        resp.addCookie(c);
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}
