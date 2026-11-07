package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import ma.ensa.videotheque.model.Film;
import ma.ensa.videotheque.service.CategorieService;
import ma.ensa.videotheque.service.FilmService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/admin/films")
public class FilmServlet extends HttpServlet {

    private final FilmService films = new FilmService();
    private final CategorieService categories = new CategorieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("new".equals(action) || "edit".equals(action)) {
            if ("edit".equals(action)) {
                Film f = films.trouver(ParamUtil.toLong(req.getParameter("id")));
                if (f == null) { resp.sendError(404); return; }
                req.setAttribute("film", f);
            }
            req.setAttribute("categories", categories.lister());
            req.getRequestDispatcher("/WEB-INF/views/admin/film-form.jsp").forward(req, resp);
            return;
        }

        String titre = req.getParameter("titre");
        int page = ParamUtil.toInt(req.getParameter("page"), 1);
        req.setAttribute("resultats", films.rechercher(titre, null, null, null, false, page, 10));
        req.setAttribute("titre", titre);
        req.setAttribute("baseUrl", "/admin/films");
        req.setAttribute("qs", "titre=" + ParamUtil.encode(titre) + "&");
        req.getRequestDispatcher("/WEB-INF/views/admin/films.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        Long id = ParamUtil.toLong(req.getParameter("id"));

        try {
            if ("delete".equals(action)) {
                films.supprimer(id);
                session.setAttribute("flashOk", "Film supprimé.");
            } else if ("toggle".equals(action)) {
                films.basculerPublication(id);
                session.setAttribute("flashOk", "Statut de publication modifié.");
            } else {
                sauvegarder(req, resp, id);
                return;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            session.setAttribute("flashErr", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/films");
    }

    private void sauvegarder(HttpServletRequest req, HttpServletResponse resp, Long id)
            throws ServletException, IOException {
        Film f = (id == null) ? new Film() : films.trouver(id);
        if (f == null) { resp.sendError(404); return; }

        try {
            f.setTitre(req.getParameter("titre"));
            f.setRealisateur(req.getParameter("realisateur"));
            f.setAnneeSortie(ParamUtil.toInt(req.getParameter("anneeSortie"), 0));
            f.setDureeMinutes(ParamUtil.toInt(req.getParameter("dureeMinutes"), 0));
            f.setSynopsis(req.getParameter("synopsis"));
            f.setAfficheUrl(req.getParameter("afficheUrl"));
            f.setUrlVideo(req.getParameter("urlVideo"));
            f.setPublie("on".equals(req.getParameter("publie")));
            f.setCategorie(categories.trouver(ParamUtil.toLong(req.getParameter("categorieId"))));

            String prix = req.getParameter("prixLocation");
            f.setPrixLocation(prix == null || prix.isBlank()
                    ? null : new BigDecimal(prix.trim().replace(',', '.')));

            films.enregistrer(f);
            req.getSession().setAttribute("flashOk", "Film enregistré.");
            resp.sendRedirect(req.getContextPath() + "/admin/films");
        } catch (NumberFormatException e) {
            reafficher(req, resp, f, "Le prix doit être un nombre.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            reafficher(req, resp, f, e.getMessage());
        }
    }

    private void reafficher(HttpServletRequest req, HttpServletResponse resp, Film f, String msg)
            throws ServletException, IOException {
        req.setAttribute("erreur", msg);
        req.setAttribute("film", f);
        req.setAttribute("categories", categories.lister());
        req.getRequestDispatcher("/WEB-INF/views/admin/film-form.jsp").forward(req, resp);
    }
}