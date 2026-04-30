package filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AdminAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code (if needed)
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userID") == null) {
            res.sendRedirect(req.getContextPath() + "/index.jsp?error=Please login first");
            return;
        }
        
        // Check if user has Admin role
        String role = (String) session.getAttribute("role");
        if (!"Admin".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/index.jsp?error=Access denied");
            return;
        }
        
        // User is authenticated and authorized, proceed
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code (if needed)
    }
}