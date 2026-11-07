package ma.ensa.videotheque.dao;

import ma.ensa.videotheque.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmDAO extends GenericDAO<Film, Long> {

    public FilmDAO() {
        super(Film.class);
    }

    /** Catalogue public : films publiés, avec pagination (page commence à 1). */
    public List<Film> findPublies(int page, int taille) {
        return inSession(s ->
                s.createQuery("select f from Film f left join fetch f.categorie "
                                + "where f.publie = true order by f.dateAjout desc", Film.class)
                        .setFirstResult((page - 1) * taille)
                        .setMaxResults(taille)
                        .list());
    }

    public long countPublies() {
        return inSession(s ->
                s.createQuery("select count(f) from Film f where f.publie = true", Long.class)
                        .getSingleResult());
    }

    /**
     * Recherche multicritères (titre, catégorie, réalisateur, année).
     * Les paramètres null ou vides sont ignorés.
     */
    public List<Film> rechercher(String titre, Long categorieId, String realisateur,
                                 Integer annee, boolean seulementPublies,
                                 int page, int taille) {
        StringBuilder hql = new StringBuilder(
                "select f from Film f left join fetch f.categorie where 1=1");
        Map<String, Object> params = new HashMap<>();

        if (seulementPublies) {
            hql.append(" and f.publie = true");
        }
        if (titre != null && !titre.isBlank()) {
            hql.append(" and lower(f.titre) like :titre");
            params.put("titre", "%" + titre.trim().toLowerCase() + "%");
        }
        if (categorieId != null) {
            hql.append(" and f.categorie.id = :cat");
            params.put("cat", categorieId);
        }
        if (realisateur != null && !realisateur.isBlank()) {
            hql.append(" and lower(f.realisateur) like :real");
            params.put("real", "%" + realisateur.trim().toLowerCase() + "%");
        }
        if (annee != null) {
            hql.append(" and f.anneeSortie = :annee");
            params.put("annee", annee);
        }
        hql.append(" order by f.dateAjout desc");

        return inSession(s -> {
            var q = s.createQuery(hql.toString(), Film.class);
            params.forEach(q::setParameter);
            return q.setFirstResult((page - 1) * taille)
                    .setMaxResults(taille)
                    .list();
        });
    }

    /** Charge un film avec sa catégorie (évite LazyInitializationException dans la JSP). */
    public Film findByIdWithCategorie(Long id) {
        return inSession(s ->
                s.createQuery("select f from Film f left join fetch f.categorie where f.id = :id", Film.class)
                        .setParameter("id", id)
                        .uniqueResult());
    }

    /** Films les plus empruntés (tableau de bord). Retourne des paires [Film, nbEmprunts]. */
    public List<Object[]> findLesPlusEmpruntes(int limite) {
        return inSession(s ->
                s.createQuery("select f, count(e) from Emprunt e join e.film f "
                                + "group by f order by count(e) desc", Object[].class)
                        .setMaxResults(limite)
                        .list());
    }

    /** Nombre total de résultats (mêmes filtres que rechercher), pour la pagination. */
    public long countRecherche(String titre, Long categorieId, String realisateur,
                               Integer annee, boolean seulementPublies) {
        StringBuilder hql = new StringBuilder("select count(f) from Film f where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (seulementPublies) hql.append(" and f.publie = true");
        if (titre != null && !titre.isBlank()) {
            hql.append(" and lower(f.titre) like :titre");
            params.put("titre", "%" + titre.trim().toLowerCase() + "%");
        }
        if (categorieId != null) {
            hql.append(" and f.categorie.id = :cat");
            params.put("cat", categorieId);
        }
        if (realisateur != null && !realisateur.isBlank()) {
            hql.append(" and lower(f.realisateur) like :real");
            params.put("real", "%" + realisateur.trim().toLowerCase() + "%");
        }
        if (annee != null) {
            hql.append(" and f.anneeSortie = :annee");
            params.put("annee", annee);
        }
        return inSession(s -> {
            var q = s.createQuery(hql.toString(), Long.class);
            params.forEach(q::setParameter);
            return q.getSingleResult();
        });
    }
}
