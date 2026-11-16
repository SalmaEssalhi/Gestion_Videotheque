package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.dao.EmpruntDAO;

@WebServlet("/admin/emprunts")
public class AdminEmpruntsServlet extends HttpServlet {

    private final EmpruntDAO empruntDao = new EmpruntDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("emprunts", empruntDao.findAllWithDetails());
        req.getRequestDispatcher("/WEB-INF/views/admin/emprunts.jsp").forward(req, resp);
    }
}