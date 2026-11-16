package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.model.Film;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.FilmService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/film")
public class FilmDetailServlet extends HttpServlet {

    private final FilmService films = new FilmService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Film f = films.trouver(ParamUtil.toLong(req.getParameter("id")));
        HttpSession s = req.getSession(false);
        Utilisateur u = (s == null) ? null : (Utilisateur) s.getAttribute("user");
        boolean admin = u != null && u.estAdmin();

        if (f == null || (!f.isPublie() && !admin)) {   // RG3 : les brouillons restent invisibles
            resp.sendError(404);
            return;
        }
        req.setAttribute("film", f);
        req.getRequestDispatcher("/WEB-INF/views/film-detail.jsp").forward(req, resp);
    }
}