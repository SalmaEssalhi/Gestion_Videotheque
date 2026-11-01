package ma.ensa.videotheque.dao;

import ma.ensa.videotheque.model.Paiement;
import ma.ensa.videotheque.model.StatutPaiement;

import java.math.BigDecimal;
import java.util.List;

public class PaiementDAO extends GenericDAO<Paiement, Long> {

    public PaiementDAO() {
        super(Paiement.class);
    }

    public Paiement findByEmprunt(Long empruntId) {
        return inSession(s ->
                s.createQuery("from Paiement p where p.emprunt.id = :eid", Paiement.class)
                        .setParameter("eid", empruntId)
                        .uniqueResult());
    }

    /** Historique des paiements d'un adhérent. */
    public List<Paiement> findByUtilisateur(Long utilisateurId) {
        return inSession(s ->
                s.createQuery("select p from Paiement p join fetch p.emprunt e join fetch e.film "
                                + "where e.utilisateur.id = :uid order by p.datePaiement desc", Paiement.class)
                        .setParameter("uid", utilisateurId)
                        .list());
    }

    /** Tous les paiements (admin). */
    public List<Paiement> findAllWithDetails() {
        return inSession(s ->
                s.createQuery("select p from Paiement p join fetch p.emprunt e join fetch e.film "
                                + "join fetch e.utilisateur order by p.datePaiement desc", Paiement.class)
                        .list());
    }

    /** Chiffre d'affaires simulé = somme des paiements REUSSI. */
    public BigDecimal chiffreAffaires() {
        BigDecimal total = inSession(s ->
                s.createQuery("select sum(p.montant) from Paiement p where p.statut = :st", BigDecimal.class)
                        .setParameter("st", StatutPaiement.REUSSI)
                        .getSingleResult());
        return total != null ? total : BigDecimal.ZERO;
    }

    /** RG11 : un paiement n'est ni modifiable ni supprimable. */
    @Override
    public Paiement update(Paiement entity) {
        throw new UnsupportedOperationException("Un paiement n'est pas modifiable (RG11)");
    }

    @Override
    public void delete(Paiement entity) {
        throw new UnsupportedOperationException("Un paiement n'est pas supprimable (RG11)");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Un paiement n'est pas supprimable (RG11)");
    }
}
