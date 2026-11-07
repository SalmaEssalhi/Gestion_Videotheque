package ma.ensa.videotheque.service;

import java.util.Comparator;
import java.util.List;
import ma.ensa.videotheque.dao.CategorieDAO;
import ma.ensa.videotheque.model.Categorie;

public class CategorieService {

    private final CategorieDAO dao = new CategorieDAO();

    public List<Categorie> lister() {
        return dao.findAll().stream()
                .sorted(Comparator.comparing(Categorie::getNom, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Categorie trouver(Long id) { return id == null ? null : dao.findById(id); }

    public void enregistrer(Long id, String nom, String description) {
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire.");
        nom = nom.trim();

        Categorie c = (id == null) ? new Categorie() : dao.findById(id);
        if (c == null) throw new IllegalArgumentException("Catégorie introuvable.");

        Categorie existante = dao.findByNom(nom);
        if (existante != null && !existante.getId().equals(c.getId()))
            throw new IllegalArgumentException("Une catégorie portant ce nom existe déjà.");

        c.setNom(nom);
        c.setDescription(description);
        if (id == null) dao.save(c); else dao.update(c);
    }

    public void supprimer(Long id) {
        Categorie c = dao.findById(id);
        if (c == null) throw new IllegalArgumentException("Catégorie introuvable.");
        if (dao.countFilms(id) > 0)                                            // RG17
            throw new IllegalStateException(
                "Impossible de supprimer : cette catégorie contient des films.");
        dao.delete(c);
    }
}