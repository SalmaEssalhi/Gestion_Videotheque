package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.dao.PaiementDAO;
import ma.ensa.videotheque.model.Utilisateur;

@WebServlet("/mes-paiements")
public class MesPaiementsServlet extends HttpServlet {

    private final PaiementDAO paiementDao = new PaiementDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utilisateur u = (Utilisateur) req.getSession().getAttribute("user");
        req.setAttribute("paiements", paiementDao.findByUtilisateur(u.getId()));
        req.getRequestDispatcher("/WEB-INF/views/mes-paiements.jsp").forward(req, resp);
    }
}