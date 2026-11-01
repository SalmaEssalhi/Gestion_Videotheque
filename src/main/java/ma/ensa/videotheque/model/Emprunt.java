package ma.ensa.videotheque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprunt")
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateEmprunt = LocalDateTime.now();
    private LocalDateTime dateExpiration;
    private LocalDateTime dateRetour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEmprunt statut = StatutEmprunt.EN_ATTENTE_PAIEMENT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id")
    private Film film;

    @OneToOne(mappedBy = "emprunt", fetch = FetchType.LAZY)
    private Paiement paiement;

    public Emprunt() {}

    public boolean estExpire() {
        return dateExpiration != null && LocalDateTime.now().isAfter(dateExpiration);
    }

    public boolean estValide() {
        return statut == StatutEmprunt.EN_COURS && !estExpire();
    }

    public void confirmerPaiement() {
        this.statut = StatutEmprunt.EN_COURS;
        this.dateExpiration = LocalDateTime.now().plusHours(48);   // RG5
    }

    public void annuler() { this.statut = StatutEmprunt.ANNULE; }

    public void rendre() {
        this.statut = StatutEmprunt.RENDU;
        this.dateRetour = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDateTime d) { this.dateEmprunt = d; }
    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime d) { this.dateExpiration = d; }
    public LocalDateTime getDateRetour() { return dateRetour; }
    public void setDateRetour(LocalDateTime d) { this.dateRetour = d; }
    public StatutEmprunt getStatut() { return statut; }
    public void setStatut(StatutEmprunt statut) { this.statut = statut; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur u) { this.utilisateur = u; }
    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }
    public Paiement getPaiement() { return paiement; }
    public void setPaiement(Paiement paiement) { this.paiement = paiement; }
}