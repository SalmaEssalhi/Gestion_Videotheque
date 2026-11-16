package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.dao.EmpruntDAO;
import ma.ensa.videotheque.dao.FilmDAO;
import ma.ensa.videotheque.dao.PaiementDAO;
import ma.ensa.videotheque.dao.UtilisateurDAO;
import ma.ensa.videotheque.model.StatutEmprunt;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private final EmpruntDAO empruntDao = new EmpruntDAO();
    private final PaiementDAO paiementDao = new PaiementDAO();
    private final FilmDAO filmDao = new FilmDAO();
    private final UtilisateurDAO utilisateurDao = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("nbFilms", filmDao.findAll().size());
        req.setAttribute("nbUtilisateurs", utilisateurDao.findAll().size());
        req.setAttribute("enCours", empruntDao.countParStatut(StatutEmprunt.EN_COURS));
        req.setAttribute("enAttente", empruntDao.countParStatut(StatutEmprunt.EN_ATTENTE_PAIEMENT));
        req.setAttribute("expires", empruntDao.countParStatut(StatutEmprunt.EXPIRE));
        req.setAttribute("rendus", empruntDao.countParStatut(StatutEmprunt.RENDU));
        req.setAttribute("annules", empruntDao.countParStatut(StatutEmprunt.ANNULE));
        req.setAttribute("chiffreAffaires", paiementDao.chiffreAffaires());
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}