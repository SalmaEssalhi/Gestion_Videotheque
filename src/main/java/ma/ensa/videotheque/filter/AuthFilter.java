package ma.ensa.videotheque.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import ma.ensa.videotheque.model.Utilisateur;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> PUBLIC_PREFIXES =
            List.of("/login", "/inscription", "/catalogue", "/css/", "/js/", "/images/");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        boolean publique = path.equals("/") || PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);

        if (publique) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Utilisateur user = (session == null) ? null : (Utilisateur) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");          // visiteur
        } else if ((path.equals("/admin") || path.startsWith("/admin/")) && !user.estAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);            // 403
        } else {
            chain.doFilter(request, response);
        }
    }
}