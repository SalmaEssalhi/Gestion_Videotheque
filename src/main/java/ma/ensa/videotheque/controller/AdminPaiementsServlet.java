package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.dao.PaiementDAO;

@WebServlet("/admin/paiements")
public class AdminPaiementsServlet extends HttpServlet {

    private final PaiementDAO paiementDao = new PaiementDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("paiements", paiementDao.findAllWithDetails());
        req.getRequestDispatcher("/WEB-INF/views/admin/paiements.jsp").forward(req, resp);
    }
}