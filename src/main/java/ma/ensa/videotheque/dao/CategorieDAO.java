package ma.ensa.videotheque.dao;

import ma.ensa.videotheque.model.Categorie;

public class CategorieDAO extends GenericDAO<Categorie, Long> {

    public CategorieDAO() {
        super(Categorie.class);
    }

    public Categorie findByNom(String nom) {
        return inSession(s ->
                s.createQuery("from Categorie c where c.nom = :nom", Categorie.class)
                        .setParameter("nom", nom)
                        .uniqueResult());
    }

    /** RG17 : une catégorie contenant des films ne peut pas être supprimée. */
    public long countFilms(Long categorieId) {
        return inSession(s ->
                s.createQuery("select count(f) from Film f where f.categorie.id = :id", Long.class)
                        .setParameter("id", categorieId)
                        .getSingleResult());
    }
}
