package ma.ensa.videotheque.service;

import ma.ensa.videotheque.dao.UtilisateurDAO;
import ma.ensa.videotheque.model.Role;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.util.PasswordUtil;

public class AuthService {

    private final UtilisateurDAO dao = new UtilisateurDAO();

    public Utilisateur inscrire(String nom, String prenom, String email,
                                String login, String motDePasse) {
        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()
                || email == null || email.isBlank() || login == null || login.isBlank())
            throw new IllegalArgumentException("Tous les champs sont obligatoires.");
        if (motDePasse == null || motDePasse.length() < 6)
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        if (dao.existsByLogin(login.trim()))
            throw new IllegalArgumentException("Ce login est déjà utilisé.");
        if (dao.existsByEmail(email.trim()))
            throw new IllegalArgumentException("Cet email est déjà utilisé.");

        Utilisateur u = new Utilisateur();
        u.setNom(nom.trim());
        u.setPrenom(prenom.trim());
        u.setEmail(email.trim());
        u.setLogin(login.trim());
        u.setMotDePasse(PasswordUtil.hash(motDePasse));
        u.setRole(Role.ADHERENT);   // RG4 : l'inscription crée toujours un ADHERENT
        return dao.save(u);
    }

    /** Renvoie l'utilisateur si les identifiants sont valides, sinon null. */
    public Utilisateur authentifier(String login, String motDePasse) {
        if (login == null || motDePasse == null) return null;
        Utilisateur u = dao.findByLogin(login.trim());
        if (u != null && PasswordUtil.verify(motDePasse, u.getMotDePasse())) {
            return u;
        }
        return null;
    }
}