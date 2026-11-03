package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.AuthService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Utilisateur deja = (session == null) ? null : (Utilisateur) session.getAttribute("user");
        if (deja != null) {
            redirigerSelonRole(req, resp, deja);
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String login = req.getParameter("login");
        String motDePasse = req.getParameter("motDePasse");

        Utilisateur user = authService.authentifier(login, motDePasse);

        if (user == null) {
            req.setAttribute("erreur", "Login ou mot de passe incorrect.");
            req.setAttribute("login", login);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // Nouvelle session à chaque connexion (évite la fixation de session)
        HttpSession ancienne = req.getSession(false);
        if (ancienne != null) {
            ancienne.invalidate();
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        redirigerSelonRole(req, resp, user);
    }

    private void redirigerSelonRole(HttpServletRequest req, HttpServletResponse resp, Utilisateur user)
            throws IOException {
        String cible = user.estAdmin() ? "/admin/dashboard" : "/catalogue";
        resp.sendRedirect(req.getContextPath() + cible);
    }
}
