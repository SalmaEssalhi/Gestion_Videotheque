package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.model.Emprunt;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.EmpruntService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/emprunt")
public class EmpruntServlet extends HttpServlet {

    private final EmpruntService service = new EmpruntService();

    /** Accès direct par URL : on renvoie vers le catalogue. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/catalogue");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur u = (Utilisateur) session.getAttribute("user");
        Long filmId = ParamUtil.toLong(req.getParameter("filmId"));

        try {
            // RG3, RG9, RG12, RG13 vérifiées dans le service
            Emprunt e = service.demander(u.getId(), filmId);
            resp.sendRedirect(req.getContextPath() + "/paiement?id=" + e.getId());

        } catch (IllegalArgumentException | IllegalStateException ex) {
            // quota atteint, doublon, film indisponible...
            session.setAttribute("flashErr", ex.getMessage());
            resp.sendRedirect(req.getContextPath() + "/film?id=" + filmId);
        }
    }
}