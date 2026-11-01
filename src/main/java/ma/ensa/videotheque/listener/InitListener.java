package ma.ensa.videotheque.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.ensa.videotheque.dao.UtilisateurDAO;
import ma.ensa.videotheque.model.Role;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.util.HibernateUtil;
import ma.ensa.videotheque.util.PasswordUtil;

@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            if (!dao.existsByLogin("admin")) {
                Utilisateur admin = new Utilisateur();
                admin.setNom("Admin");
                admin.setPrenom("Système");
                admin.setEmail("admin@videotheque.ma");
                admin.setLogin("admin");
                admin.setMotDePasse(PasswordUtil.hash("admin123"));
                admin.setRole(Role.ADMIN);
                dao.save(admin);
                System.out.println(">>> Compte admin créé (login: admin)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
    }
}