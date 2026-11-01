package ma.ensa.videotheque.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiement")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal montant;          // figé au moment du paiement (RG6)

    private String devise = "MAD";
    private LocalDateTime datePaiement = LocalDateTime.now();
    private String methode = "CARTE_VIRTUELLE";
    private String carteMasquee;         // 4 derniers chiffres uniquement (RG7)
    private String referenceTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emprunt_id", unique = true)
    private Emprunt emprunt;

    public Paiement() {}

    public boolean estReussi() { return statut == StatutPaiement.REUSSI; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public String getDevise() { return devise; }
    public void setDevise(String devise) { this.devise = devise; }
    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime d) { this.datePaiement = d; }
    public String getMethode() { return methode; }
    public void setMethode(String methode) { this.methode = methode; }
    public String getCarteMasquee() { return carteMasquee; }
    public void setCarteMasquee(String carteMasquee) { this.carteMasquee = carteMasquee; }
    public String getReferenceTransaction() { return referenceTransaction; }
    public void setReferenceTransaction(String r) { this.referenceTransaction = r; }
    public StatutPaiement getStatut() { return statut; }
    public void setStatut(StatutPaiement statut) { this.statut = statut; }
    public Emprunt getEmprunt() { return emprunt; }
    public void setEmprunt(Emprunt emprunt) { this.emprunt = emprunt; }
}