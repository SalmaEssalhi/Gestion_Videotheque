package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.service.CategorieService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/admin/categories")
public class CategorieServlet extends HttpServlet {

    private final CategorieService service = new CategorieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long editId = ParamUtil.toLong(req.getParameter("edit"));
        if (editId != null) req.setAttribute("categorie", service.trouver(editId));
        req.setAttribute("categories", service.lister());
        req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Long id = ParamUtil.toLong(req.getParameter("id"));
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.supprimer(id);
                session.setAttribute("flashOk", "Catégorie supprimée.");
            } else {
                service.enregistrer(id, req.getParameter("nom"), req.getParameter("description"));
                session.setAttribute("flashOk", "Catégorie enregistrée.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            session.setAttribute("flashErr", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }
}