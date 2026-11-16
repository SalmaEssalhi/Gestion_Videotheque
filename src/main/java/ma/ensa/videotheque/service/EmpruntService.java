package ma.ensa.videotheque.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import ma.ensa.videotheque.dao.EmpruntDAO;
import ma.ensa.videotheque.dao.FilmDAO;
import ma.ensa.videotheque.dao.PaiementDAO;
import ma.ensa.videotheque.dao.UtilisateurDAO;
import ma.ensa.videotheque.model.Emprunt;
import ma.ensa.videotheque.model.Film;
import ma.ensa.videotheque.model.Paiement;
import ma.ensa.videotheque.model.StatutEmprunt;
import ma.ensa.videotheque.model.StatutPaiement;
import ma.ensa.videotheque.model.Utilisateur;

public class EmpruntService {

    public static final int QUOTA = 3;                       // RG12

    private final EmpruntDAO empruntDao = new EmpruntDAO();
    private final PaiementDAO paiementDao = new PaiementDAO();
    private final FilmDAO filmDao = new FilmDAO();
    private final UtilisateurDAO utilisateurDao = new UtilisateurDAO();
    private final PaiementService paiementService;

    public EmpruntService() { this(new PaiementSimule()); }

    /** Permet de brancher une vraie passerelle de paiement sans toucher au reste. */
    public EmpruntService(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    /** Crée un emprunt EN_ATTENTE_PAIEMENT (RG3, RG9, RG12, RG13). */
    public Emprunt demander(Long utilisateurId, Long filmId) {
        Utilisateur u = utilisateurId == null ? null : utilisateurDao.findById(utilisateurId);
        if (u == null) throw new IllegalArgumentException("Utilisateur introuvable.");

        Film f = filmId == null ? null : filmDao.findById(filmId);
        if (f == null) throw new IllegalArgumentException("Film introuvable.");
        if (!f.estEmpruntable())                                          // RG3
            throw new IllegalStateException("Ce film n'est pas disponible à l'emprunt.");

        if (empruntDao.existeActif(utilisateurId, filmId))                // RG13
            throw new IllegalStateException("Vous avez déjà ce film en cours d'emprunt.");
        if (empruntDao.countActifs(utilisateurId) >= QUOTA)               // RG12
            throw new IllegalStateException(
                "Vous avez atteint la limite de " + QUOTA + " emprunts simultanés.");

        Emprunt e = new Emprunt();
        e.setUtilisateur(u);
        e.setFilm(f);                                                     // statut par défaut : EN_ATTENTE_PAIEMENT
        return empruntDao.save(e);
    }

    /**
     * Paie un emprunt en attente (RG5, RG6, RG7, RG9, RG11).
     * - Format invalide : IllegalArgumentException, l'emprunt reste en attente.
     * - Carte refusée : paiement ECHOUE enregistré, emprunt ANNULE.
     * - Succès : paiement REUSSI, emprunt EN_COURS, expiration = maintenant + 48 h.
     */
    public Paiement payer(Long empruntId, Long utilisateurId, String numero,
                          String expiration, String cvv) {
        Emprunt e = chargerPourUtilisateur(empruntId, utilisateurId);
        if (e.getStatut() != StatutEmprunt.EN_ATTENTE_PAIEMENT)
            throw new IllegalStateException("Cet emprunt n'attend pas de paiement.");

        BigDecimal montant = e.getFilm().getPrixLocation();               // RG6 : prix figé
        ResultatPaiement r = paiementService.payer(montant, numero, expiration, cvv);

        Paiement p = new Paiement();
        p.setEmprunt(e);
        p.setMontant(montant);
        p.setCarteMasquee(r.getCarteMasquee());                           // RG7
        p.setReferenceTransaction(r.getReference());
        p.setStatut(r.isReussi() ? StatutPaiement.REUSSI : StatutPaiement.ECHOUE);
        paiementDao.save(p);                                              // RG11 : seulement save

        if (r.isReussi()) e.confirmerPaiement();                          // RG5 : +48 h
        else e.annuler();                                                 // RG9
        empruntDao.update(e);
        return p;
    }

    /** L'adhérent abandonne avant de payer (RG9). */
    public void annuler(Long empruntId, Long utilisateurId) {
        Emprunt e = chargerPourUtilisateur(empruntId, utilisateurId);
        if (e.getStatut() != StatutEmprunt.EN_ATTENTE_PAIEMENT)
            throw new IllegalStateException("Seul un emprunt en attente de paiement peut être annulé.");
        e.annuler();
        empruntDao.update(e);
    }

    /** Rend le film avant l'échéance (RG10 : pas de remboursement, RG16 : libère une place). */
    public void rendre(Long empruntId, Long utilisateurId) {
        Emprunt e = chargerPourUtilisateur(empruntId, utilisateurId);
        if (!e.estValide())
            throw new IllegalStateException("Cet emprunt n'est plus en cours.");
        e.rendre();
        empruntDao.update(e);
    }

    /** Passe à EXPIRE les emprunts EN_COURS échus (RG15). Renvoie le nombre traité. */
    public int expirerEchus() {
        List<Emprunt> echus = empruntDao.findExpires(LocalDateTime.now());
        for (Emprunt e : echus) {
            e.setStatut(StatutEmprunt.EXPIRE);
            empruntDao.update(e);
        }
        return echus.size();
    }

    /** Contrôle d'accès au visionnage (RG14). Renvoie l'emprunt, ou null si refusé. */
    public Emprunt verifierAcces(Long empruntId, Long utilisateurId) {
        if (empruntId == null || utilisateurId == null) return null;
        Emprunt e = empruntDao.findByIdWithDetails(empruntId);
        if (e == null || !e.getUtilisateur().getId().equals(utilisateurId)) return null;

        if (e.getStatut() == StatutEmprunt.EN_COURS && e.estExpire()) {   // RG15 : à chaque accès
            e.setStatut(StatutEmprunt.EXPIRE);
            empruntDao.update(e);
            return null;
        }
        return e.estValide() ? e : null;
    }

    /** Historique de l'adhérent (RG2), expirations mises à jour avant lecture. */
    public List<Emprunt> mesEmprunts(Long utilisateurId) {
        expirerEchus();
        return empruntDao.findByUtilisateur(utilisateurId);
    }

    /** Ajout au contrat : un emprunt de l'adhérent avec son film (page de paiement). */
    public Emprunt trouver(Long empruntId, Long utilisateurId) {
        expirerEchus();
        return chargerPourUtilisateur(empruntId, utilisateurId);
    }

    private Emprunt chargerPourUtilisateur(Long empruntId, Long utilisateurId) {
        Emprunt e = empruntId == null ? null : empruntDao.findByIdWithDetails(empruntId);
        if (e == null || utilisateurId == null || !e.getUtilisateur().getId().equals(utilisateurId))
            throw new IllegalArgumentException("Emprunt introuvable.");
        return e;
    }
}