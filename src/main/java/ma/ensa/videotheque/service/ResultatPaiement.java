package ma.ensa.videotheque.service;

public class ResultatPaiement {
    private final boolean reussi;
    private final String reference;
    private final String carteMasquee;   // 4 derniers chiffres uniquement (RG7)
    private final String message;

    private ResultatPaiement(boolean reussi, String reference, String carteMasquee, String message) {
        this.reussi = reussi;
        this.reference = reference;
        this.carteMasquee = carteMasquee;
        this.message = message;
    }

    public static ResultatPaiement succes(String reference, String carteMasquee) {
        return new ResultatPaiement(true, reference, carteMasquee, "Paiement accepté.");
    }

    public static ResultatPaiement echec(String reference, String carteMasquee, String message) {
        return new ResultatPaiement(false, reference, carteMasquee, message);
    }

    public boolean isReussi() { return reussi; }
    public String getReference() { return reference; }
    public String getCarteMasquee() { return carteMasquee; }
    public String getMessage() { return message; }
}