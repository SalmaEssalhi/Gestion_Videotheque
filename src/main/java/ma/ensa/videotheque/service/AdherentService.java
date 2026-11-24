package ma.ensa.videotheque.service;

import java.util.Comparator;
import java.util.List;
import ma.ensa.videotheque.dao.UtilisateurDAO;
import ma.ensa.videotheque.model.Role;
import ma.ensa.videotheque.model.Utilisateur;
import ma.ensa.videotheque.util.PasswordUtil;

public class AdherentService {

    private final UtilisateurDAO dao = new UtilisateurDAO();

    public List<Utilisateur> lister() {
        return dao.findAll().stream()
                .sorted(Comparator.comparing(Utilisateur::getNom, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Utilisateur::getPrenom, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Utilisateur trouver(Long id) { return id == null ? null : dao.findById(id); }

    /** Crée (id == null) ou modifie un adhérent. motDePasse vide à la modification = inchangé. */
    public void enregistrer(Long id, String nom, String prenom, String email,
                            String login, String motDePasse, String roleTexte, Long adminConnecteId) {
        if (vide(nom) || vide(prenom) || vide(email) || vide(login))
            throw new IllegalArgumentException("Nom, prénom, email et login sont obligatoires.");

        Role role;
        try { role = Role.valueOf(roleTexte); }
        catch (IllegalArgumentException | NullPointerException e) { role = Role.ADHERENT; }

        Utilisateur u;
        if (id == null) {
            if (vide(motDePasse) || motDePasse.length() < 6)
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
            u = new Utilisateur();
        } else {
            u = dao.findById(id);
            if (u == null) throw new IllegalArgumentException("Adhérent introuvable.");
            if (!vide(motDePasse) && motDePasse.length() < 6)
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
            if (id.equals(adminConnecteId) && role != Role.ADMIN)
                throw new IllegalStateException("Vous ne pouvez pas retirer votre propre rôle administrateur.");
        }

        Utilisateur parLogin = dao.findByLogin(login.trim());
        if (parLogin != null && !parLogin.getId().equals(u.getId()))
            throw new IllegalArgumentException("Ce login est déjà utilisé.");
        Utilisateur parEmail = dao.findByEmail(email.trim());
        if (parEmail != null && !parEmail.getId().equals(u.getId()))
            throw new IllegalArgumentException("Cet email est déjà utilisé.");

        u.setNom(nom.trim());
        u.setPrenom(prenom.trim());
        u.setEmail(email.trim());
        u.setLogin(login.trim());
        u.setRole(role);
        if (!vide(motDePasse)) u.setMotDePasse(PasswordUtil.hash(motDePasse));   // RG19

        if (id == null) dao.save(u); else dao.update(u);
    }

    public void supprimer(Long id, Long adminConnecteId) {
        Utilisateur u = id == null ? null : dao.findById(id);
        if (u == null) throw new IllegalArgumentException("Adhérent introuvable.");
        if (id.equals(adminConnecteId))
            throw new IllegalStateException("Vous ne pouvez pas supprimer votre propre compte.");
        if (dao.countEmprunts(id) > 0)
            throw new IllegalStateException(
                "Cet adhérent a un historique d'emprunts : suppression impossible.");
        dao.delete(u);
    }

    private boolean vide(String s) { return s == null || s.isBlank(); }
}