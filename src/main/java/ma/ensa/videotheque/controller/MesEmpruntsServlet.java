package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.EmpruntService;

@WebServlet("/mes-emprunts")
public class MesEmpruntsServlet extends HttpServlet {

    private final EmpruntService service = new EmpruntService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Utilisateur u = (Utilisateur) req.getSession().getAttribute("user");
        req.setAttribute("emprunts", service.mesEmprunts(u.getId()));
        req.getRequestDispatcher("/WEB-INF/views/mes-emprunts.jsp").forward(req, resp);
    }
}