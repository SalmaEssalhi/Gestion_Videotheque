package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.model.Emprunt;
import ma.ensa.videotheque.model.Paiement;
import ma.ensa.videotheque.model.StatutEmprunt;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.EmpruntService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/paiement")
public class PaiementServlet extends HttpServlet {

    private final EmpruntService service = new EmpruntService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur u = (Utilisateur) session.getAttribute("user");
        try {
            Emprunt e = service.trouver(ParamUtil.toLong(req.getParameter("id")), u.getId());
            if (e.getStatut() != StatutEmprunt.EN_ATTENTE_PAIEMENT) {
                resp.sendRedirect(req.getContextPath() + "/mes-emprunts");
                return;
            }
            req.setAttribute("emprunt", e);
            req.getRequestDispatcher("/WEB-INF/views/paiement.jsp").forward(req, resp);
        } catch (IllegalArgumentException ex) {
            session.setAttribute("flashErr", ex.getMessage());
            resp.sendRedirect(req.getContextPath() + "/mes-emprunts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Utilisateur u = (Utilisateur) session.getAttribute("user");
        Long id = ParamUtil.toLong(req.getParameter("id"));
        String numero = req.getParameter("numero");
        numero = numero == null ? "" : numero.replaceAll("\\s", "");

        try {
            Paiement p = service.payer(id, u.getId(), numero,
                    req.getParameter("expiration"), req.getParameter("cvv"));
            if (p.estReussi()) {
                session.setAttribute("flashOk", "Paiement accepté (réf. "
                        + p.getReferenceTransaction() + "). Bon visionnage !");
            } else {
                session.setAttribute("flashErr", "Paiement refusé : l'emprunt est annulé.");
            }
            resp.sendRedirect(req.getContextPath() + "/mes-emprunts");

        } catch (IllegalArgumentException ex) {
            // format invalide : l'emprunt reste en attente, on réaffiche le formulaire
            try {
                req.setAttribute("emprunt", service.trouver(id, u.getId()));
                req.setAttribute("erreur", ex.getMessage());
                req.getRequestDispatcher("/WEB-INF/views/paiement.jsp").forward(req, resp);
            } catch (IllegalArgumentException ex2) {
                session.setAttribute("flashErr", ex2.getMessage());
                resp.sendRedirect(req.getContextPath() + "/mes-emprunts");
            }
        } catch (IllegalStateException ex) {
            session.setAttribute("flashErr", ex.getMessage());
            resp.sendRedirect(req.getContextPath() + "/mes-emprunts");
        }
    }
}