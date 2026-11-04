package ma.ensa.videotheque.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = build();

    private static SessionFactory build() {
        try {
            Configuration cfg = new Configuration().configure();

            // Chaque développeur peut avoir son propre mot de passe MySQL
            // sans modifier hibernate.cfg.xml : set DB_PASSWORD=...
            String password = System.getenv("DB_PASSWORD");
            if (password != null && !password.isBlank()) {
                cfg.setProperty("hibernate.connection.password", password);
            }

            return cfg.buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() { return sessionFactory; }

    public static void shutdown() { sessionFactory.close(); }
}