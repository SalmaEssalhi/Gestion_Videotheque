package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ensa.videotheque.service.AuthService;

import java.io.IOException;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        String email = req.getParameter("email");
        String login = req.getParameter("login");
        String motDePasse = req.getParameter("motDePasse");
        String confirmation = req.getParameter("confirmation");

        if (motDePasse == null || !motDePasse.equals(confirmation)) {
            erreur(req, resp, "Les deux mots de passe ne correspondent pas.");
            return;
        }

        try {
            authService.inscrire(nom, prenom, email, login, motDePasse);
        } catch (IllegalArgumentException e) {
            // Messages métier de AuthService (champs vides, login/email déjà utilisé...)
            erreur(req, resp, e.getMessage());
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            erreur(req, resp, "Erreur technique lors de l'inscription. Réessayez plus tard.");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login?inscrit=1");
    }

    private void erreur(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("erreur", message);
        req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
    }
}
