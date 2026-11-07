package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.service.CategorieService;
import ma.ensa.videotheque.service.FilmService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/catalogue")
public class CatalogueServlet extends HttpServlet {

    private final FilmService films = new FilmService();
    private final CategorieService categories = new CategorieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String titre = req.getParameter("titre");
        String realisateur = req.getParameter("realisateur");
        Long cat = ParamUtil.toLong(req.getParameter("cat"));
        Integer annee = ParamUtil.toInteger(req.getParameter("annee"));
        int page = ParamUtil.toInt(req.getParameter("page"), 1);

        req.setAttribute("resultats", films.rechercher(titre, cat, realisateur, annee, true, page, 8));
        req.setAttribute("categories", categories.lister());
        req.setAttribute("titre", titre);
        req.setAttribute("realisateur", realisateur);
        req.setAttribute("catId", cat);
        req.setAttribute("annee", annee);
        req.setAttribute("baseUrl", "/catalogue");
        req.setAttribute("qs", "titre=" + ParamUtil.encode(titre)
                + "&realisateur=" + ParamUtil.encode(realisateur)
                + "&cat=" + (cat == null ? "" : cat)
                + "&annee=" + (annee == null ? "" : annee) + "&");

        req.getRequestDispatcher("/WEB-INF/views/catalogue.jsp").forward(req, resp);
    }
}