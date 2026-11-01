package ma.ensa.videotheque.dao;

import ma.ensa.videotheque.model.Emprunt;
import ma.ensa.videotheque.model.StatutEmprunt;

import java.time.LocalDateTime;
import java.util.List;

public class EmpruntDAO extends GenericDAO<Emprunt, Long> {

    public EmpruntDAO() {
        super(Emprunt.class);
    }

    /** Emprunt avec son film et son utilisateur chargés (pour la JSP / le lecteur). */
    public Emprunt findByIdWithDetails(Long id) {
        return inSession(s ->
                s.createQuery("select e from Emprunt e join fetch e.film join fetch e.utilisateur "
                                + "where e.id = :id", Emprunt.class)
                        .setParameter("id", id)
                        .uniqueResult());
    }

    /** Historique d'un adhérent (RG2). */
    public List<Emprunt> findByUtilisateur(Long utilisateurId) {
        return inSession(s ->
                s.createQuery("select e from Emprunt e join fetch e.film "
                                + "where e.utilisateur.id = :uid order by e.dateEmprunt desc", Emprunt.class)
                        .setParameter("uid", utilisateurId)
                        .list());
    }

    /** Tous les emprunts (admin). */
    public List<Emprunt> findAllWithDetails() {
        return inSession(s ->
                s.createQuery("select e from Emprunt e join fetch e.film join fetch e.utilisateur "
                                + "order by e.dateEmprunt desc", Emprunt.class)
                        .list());
    }

    /** RG12 : emprunts simultanés = EN_COURS + EN_ATTENTE_PAIEMENT. */
    public long countActifs(Long utilisateurId) {
        return inSession(s ->
                s.createQuery("select count(e) from Emprunt e where e.utilisateur.id = :uid "
                                + "and e.statut in (:s1, :s2)", Long.class)
                        .setParameter("uid", utilisateurId)
                        .setParameter("s1", StatutEmprunt.EN_COURS)
                        .setParameter("s2", StatutEmprunt.EN_ATTENTE_PAIEMENT)
                        .getSingleResult());
    }

    /** RG13 : l'adhérent a-t-il déjà ce film en cours d'emprunt ? */
    public boolean existeActif(Long utilisateurId, Long filmId) {
        Long n = inSession(s ->
                s.createQuery("select count(e) from Emprunt e where e.utilisateur.id = :uid "
                                + "and e.film.id = :fid and e.statut in (:s1, :s2)", Long.class)
                        .setParameter("uid", utilisateurId)
                        .setParameter("fid", filmId)
                        .setParameter("s1", StatutEmprunt.EN_COURS)
                        .setParameter("s2", StatutEmprunt.EN_ATTENTE_PAIEMENT)
                        .getSingleResult());
        return n > 0;
    }

    /** RG18 : nombre d'emprunts EN_COURS pour un film. */
    public long countEnCoursParFilm(Long filmId) {
        return inSession(s ->
                s.createQuery("select count(e) from Emprunt e where e.film.id = :fid "
                                + "and e.statut = :st", Long.class)
                        .setParameter("fid", filmId)
                        .setParameter("st", StatutEmprunt.EN_COURS)
                        .getSingleResult());
    }

    public long countParStatut(StatutEmprunt statut) {
        return inSession(s ->
                s.createQuery("select count(e) from Emprunt e where e.statut = :st", Long.class)
                        .setParameter("st", statut)
                        .getSingleResult());
    }

    /** RG15 : emprunts EN_COURS dont l'échéance est dépassée (pour la tâche planifiée). */
    public List<Emprunt> findExpires(LocalDateTime maintenant) {
        return inSession(s ->
                s.createQuery("from Emprunt e where e.statut = :st and e.dateExpiration < :now", Emprunt.class)
                        .setParameter("st", StatutEmprunt.EN_COURS)
                        .setParameter("now", maintenant)
                        .list());
    }
}
