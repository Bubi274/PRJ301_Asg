package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Filter bảo vệ toàn bộ url-pattern /admin/*
 * - Chưa login -> redirect Login Screen
 * - Login rồi nhưng không phải Admin (RoleId != 1) -> Access Denied Screen
 */
@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null || roleId != 1) {
            resp.sendRedirect(req.getContextPath() + "/accessDenied.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
