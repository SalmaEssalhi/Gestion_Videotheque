package ma.ensa.videotheque.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.regex.Pattern;

public class PaiementSimule implements PaiementService {

    private static final Pattern CARTE = Pattern.compile("\\d{16}");
    private static final Pattern CVV = Pattern.compile("\\d{3}");
    private static final DateTimeFormatter FORMAT_EXPIRATION = DateTimeFormatter.ofPattern("MM/yy");

    @Override
    public ResultatPaiement payer(BigDecimal montant, String numeroCarte, String expiration, String cvv) {
        if (montant == null || montant.signum() <= 0)
            throw new IllegalArgumentException("Montant invalide.");

        // RG8 : 16 chiffres (espaces et tirets tolérés à la saisie)
        String numero = numeroCarte == null ? "" : numeroCarte.replaceAll("[\\s-]", "");
        if (!CARTE.matcher(numero).matches())
            throw new IllegalArgumentException("Le numéro de carte doit contenir 16 chiffres.");

        // RG8 : date d'expiration future (format MM/AA, valable jusqu'à la fin du mois)
        YearMonth exp;
        try {
            exp = YearMonth.parse(expiration == null ? "" : expiration.trim(), FORMAT_EXPIRATION);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date d'expiration invalide (format MM/AA).");
        }
        if (exp.isBefore(YearMonth.now()))
            throw new IllegalArgumentException("Cette carte est expirée.");

        // RG8 : CVV de 3 chiffres
        if (cvv == null || !CVV.matcher(cvv.trim()).matches())
            throw new IllegalArgumentException("Le CVV doit contenir 3 chiffres.");

        // RG7 : on ne garde que les 4 derniers chiffres. Ne JAMAIS logguer le numéro complet.
        String masquee = numero.substring(12);
        String reference = "TX-" + UUID.randomUUID().toString().replace("-", "")
                                         .substring(0, 12).toUpperCase();

        // RG8 : une carte terminant par 0000 est refusée (cas de test d'échec)
        if (numero.endsWith("0000"))
            return ResultatPaiement.echec(reference, masquee, "Paiement refusé par la banque simulée.");

        return ResultatPaiement.succes(reference, masquee);
    }
}