package ma.ensa.videotheque.service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import ma.ensa.videotheque.dao.EmpruntDAO;
import ma.ensa.videotheque.dao.FilmDAO;
import ma.ensa.videotheque.model.Film;
import ma.ensa.videotheque.util.Page;

public class FilmService {

    private final FilmDAO dao = new FilmDAO();
    private final EmpruntDAO empruntDao = new EmpruntDAO();

    /** Recherche paginée. publiesSeulement = true pour le catalogue public (RG3). */
    public Page<Film> rechercher(String titre, Long categorieId, String realisateur,
                                 Integer annee, boolean publiesSeulement, int page, int taille) {
        long total = dao.countRecherche(titre, categorieId, realisateur, annee, publiesSeulement);
        int totalPages = (int) Math.max(1, Math.ceil((double) total / taille));
        int p = Math.min(Math.max(page, 1), totalPages);
        List<Film> items = dao.rechercher(titre, categorieId, realisateur, annee,
                                          publiesSeulement, p, taille);
        return new Page<>(items, p, taille, total);
    }

    public Film trouver(Long id) { return id == null ? null : dao.findByIdWithCategorie(id); }

    public void enregistrer(Film f) {
        if (f.getTitre() == null || f.getTitre().isBlank())
            throw new IllegalArgumentException("Le titre est obligatoire.");
        if (f.getPrixLocation() == null || f.getPrixLocation().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Le prix de location doit être supérieur à 0.");
        if (f.getAnneeSortie() != 0
                && (f.getAnneeSortie() < 1888 || f.getAnneeSortie() > Year.now().getValue() + 1))
            throw new IllegalArgumentException("Année de sortie invalide.");
        if (f.getDureeMinutes() < 0)
            throw new IllegalArgumentException("La durée ne peut pas être négative.");
        if (f.getCategorie() == null)
            throw new IllegalArgumentException("Choisissez une catégorie.");   // RG1

        if (f.getId() == null) dao.save(f); else dao.update(f);
    }

    public void basculerPublication(Long id) {
        Film f = dao.findById(id);
        if (f == null) throw new IllegalArgumentException("Film introuvable.");
        f.setPublie(!f.isPublie());
        dao.update(f);
    }

    public void supprimer(Long id) {
        Film f = dao.findById(id);
        if (f == null) throw new IllegalArgumentException("Film introuvable.");
        if (empruntDao.countEnCoursParFilm(id) > 0)                            // RG18
            throw new IllegalStateException(
                "Ce film a des emprunts en cours : suppression impossible. Dépubliez-le plutôt.");
        try {
            dao.delete(f);
        } catch (RuntimeException e) {
            // un emprunt passé (historique) référence encore le film : clé étrangère
            throw new IllegalStateException(
                "Ce film a un historique d'emprunts : suppression impossible. Dépubliez-le plutôt.");
        }
    }
}