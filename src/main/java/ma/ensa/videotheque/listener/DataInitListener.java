package ma.ensa.videotheque.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import ma.ensa.videotheque.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/** Charge les données de démo au démarrage si la table film est vide. */
@WebListener
public class DataInitListener implements ServletContextListener {

    private static final String SCRIPT = "/data-demo.sql";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long nb = session.createQuery("select count(f) from Film f", Long.class)
                    .getSingleResult();
            if (nb > 0) {
                return; // base déjà remplie : on ne touche à rien
            }
            Transaction tx = session.beginTransaction();
            session.doWork(this::executerScript);
            tx.commit();
            System.out.println(">>> Données de démo chargées");
        } catch (Exception e) {
            e.printStackTrace(); // ne jamais empêcher le démarrage
        }
    }

    private void executerScript(Connection conn) throws java.sql.SQLException {
        try (InputStream in = getClass().getResourceAsStream(SCRIPT)) {
            if (in == null) {
                System.err.println(">>> " + SCRIPT + " introuvable dans le classpath");
                return;
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8));
                 Statement st = conn.createStatement()) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    ligne = ligne.trim();
                    if (ligne.isEmpty() || ligne.startsWith("--") || ligne.startsWith("USE ")) {
                        continue; // une instruction par ligne dans notre script
                    }
                    st.execute(ligne);
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}