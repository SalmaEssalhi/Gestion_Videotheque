package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.AdherentService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet("/admin/adherents")
public class AdherentServlet extends HttpServlet {

    private final AdherentService service = new AdherentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long editId = ParamUtil.toLong(req.getParameter("edit"));
        if (editId != null) req.setAttribute("adherent", service.trouver(editId));
        req.setAttribute("adherents", service.lister());
        req.getRequestDispatcher("/WEB-INF/views/admin/adherents.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Utilisateur admin = (Utilisateur) session.getAttribute("user");
        Long adminId = admin == null ? null : admin.getId();
        Long id = ParamUtil.toLong(req.getParameter("id"));
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.supprimer(id, adminId);
                session.setAttribute("flashOk", "Adhérent supprimé.");
            } else {
                service.enregistrer(id, req.getParameter("nom"), req.getParameter("prenom"),
                        req.getParameter("email"), req.getParameter("login"),
                        req.getParameter("motDePasse"), req.getParameter("role"), adminId);
                session.setAttribute("flashOk", "Adhérent enregistré.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            session.setAttribute("flashErr", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/adherents");
    }
}