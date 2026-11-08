package ma.ensa.videotheque.service;

import java.math.BigDecimal;

public interface PaiementService {

    /**
     * Tente un paiement.
     * @throws IllegalArgumentException si le format des données est invalide
     *         (l'utilisateur peut corriger et réessayer)
     * @return un résultat REUSSI ou refusé (carte refusée)
     */
    ResultatPaiement payer(BigDecimal montant, String numeroCarte, String expiration, String cvv);
}