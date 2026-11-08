package ma.ensa.videotheque.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import ma.ensa.videotheque.model.Emprunt;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.service.EmpruntService;
import ma.ensa.videotheque.util.ParamUtil;

@WebServlet({"/lecteur", "/lecteur/flux"})
public class LecteurServlet extends HttpServlet {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final EmpruntService service = new EmpruntService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Utilisateur user = session == null ? null : (Utilisateur) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long id = ParamUtil.toLong(req.getParameter("id"));
        Emprunt emprunt = service.verifierAcces(id, user.getId());       // RG14 + RG15

        if ("/lecteur/flux".equals(req.getServletPath())) {
            if (emprunt == null) { resp.sendError(HttpServletResponse.SC_FORBIDDEN); return; }
            envoyerFlux(req, resp, emprunt.getFilm().getUrlVideo());
            return;
        }

        if (emprunt == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("erreur", "Accès refusé : emprunt introuvable, expiré ou non payé.");
        } else {
            req.setAttribute("emprunt", emprunt);
            req.setAttribute("expiration", emprunt.getDateExpiration().format(FORMAT));
        }
        req.getRequestDispatcher("/WEB-INF/views/lecteur.jsp").forward(req, resp);
    }

    private void envoyerFlux(HttpServletRequest req, HttpServletResponse resp, String source)
            throws IOException {
        if (source == null || source.isBlank()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Vidéo non configurée.");
            return;
        }
        resp.setHeader("Cache-Control", "private, no-store");
        try {
            if (source.startsWith("http://") || source.startsWith("https://")) relayer(req, resp, source);
            else servirFichier(req, resp, source);
        } catch (IOException e) {
            // le navigateur coupe souvent la connexion (avance rapide, fermeture) : sans gravité
        }
    }

    /** Fichier MP4 stocké hors du dossier public, avec prise en charge de l'en-tête Range. */
    private void servirFichier(HttpServletRequest req, HttpServletResponse resp, String chemin)
            throws IOException {
        File f = new File(chemin);
        if (!f.isFile()) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        long longueur = f.length();
        long debut = 0, fin = longueur - 1;
        boolean partiel = false;

        String range = req.getHeader("Range");
        if (range != null && range.startsWith("bytes=")) {
            try {
                String[] p = range.substring(6).split("-", 2);
                if (p[0].isEmpty()) {
                    debut = Math.max(0, longueur - Long.parseLong(p[1]));
                } else {
                    debut = Long.parseLong(p[0]);
                    if (p.length > 1 && !p[1].isEmpty())
                        fin = Math.min(Long.parseLong(p[1]), longueur - 1);
                }
                partiel = true;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                debut = 0; fin = longueur - 1; partiel = false;
            }
            if (partiel && (debut > fin || debut >= longueur)) {
                resp.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                resp.setHeader("Content-Range", "bytes */" + longueur);
                return;
            }
        }

        String type = getServletContext().getMimeType(f.getName());
        resp.setContentType(type != null ? type : "video/mp4");
        resp.setHeader("Accept-Ranges", "bytes");
        long taille = fin - debut + 1;
        if (partiel) {
            resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            resp.setHeader("Content-Range", "bytes " + debut + "-" + fin + "/" + longueur);
        }
        resp.setContentLengthLong(taille);

        try (RandomAccessFile raf = new RandomAccessFile(f, "r");
             OutputStream out = resp.getOutputStream()) {
            raf.seek(debut);
            byte[] buf = new byte[8192];
            long restant = taille;
            while (restant > 0) {
                int n = raf.read(buf, 0, (int) Math.min(buf.length, restant));
                if (n < 0) break;
                out.write(buf, 0, n);
                restant -= n;
            }
        }
    }

    /** Lien vers un hébergeur (fichier vidéo direct) : le serveur relaie, l'URL reste cachée. */
    private void relayer(HttpServletRequest req, HttpServletResponse resp, String url)
            throws IOException {
        HttpURLConnection c = (HttpURLConnection) URI.create(url).toURL().openConnection();
        c.setConnectTimeout(5000);
        c.setReadTimeout(15000);
        String range = req.getHeader("Range");
        if (range != null) c.setRequestProperty("Range", range);

        int statut = c.getResponseCode();
        if (statut >= 400) { resp.sendError(HttpServletResponse.SC_BAD_GATEWAY); return; }

        resp.setStatus(statut);
        for (String h : new String[]{"Content-Type", "Content-Length", "Content-Range", "Accept-Ranges"}) {
            String v = c.getHeaderField(h);
            if (v != null) resp.setHeader(h, v);
        }
        try (InputStream in = c.getInputStream(); OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }
}