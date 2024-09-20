package model;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "SessionTimeoutFilter", urlPatterns = {"/*"})
public class SessionTimeoutFilter implements Filter {

    private static final int MAX_INACTIVE_INTERVAL = 30; // Tiempo de sesión en minutos

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session != null && session.getLastAccessedTime() + (MAX_INACTIVE_INTERVAL * 60 * 1000) < System.currentTimeMillis()) {
            // La sesión ha expirado
            session.invalidate(); // Invalidar la sesión
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}