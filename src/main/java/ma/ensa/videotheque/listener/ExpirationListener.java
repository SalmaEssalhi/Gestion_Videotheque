package ma.ensa.videotheque.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import ma.ensa.videotheque.service.EmpruntService;

@WebListener
public class ExpirationListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EmpruntService service = new EmpruntService();
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "expiration-emprunts");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(() -> {
            try {
                int n = service.expirerEchus();          // RG15
                if (n > 0) System.out.println(">>> " + n + " emprunt(s) expiré(s)");
            } catch (Exception e) {
                e.printStackTrace();                     // ne jamais tuer la tâche
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) scheduler.shutdownNow();
    }
}