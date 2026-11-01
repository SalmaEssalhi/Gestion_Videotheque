package ma.ensa.videotheque.dao;

import ma.ensa.videotheque.model.Utilisateur;

public class UtilisateurDAO extends GenericDAO<Utilisateur, Long> {

    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    public Utilisateur findByLogin(String login) {
        return inSession(s ->
                s.createQuery("from Utilisateur u where u.login = :login", Utilisateur.class)
                        .setParameter("login", login)
                        .uniqueResult());
    }

    public Utilisateur findByEmail(String email) {
        return inSession(s ->
                s.createQuery("from Utilisateur u where u.email = :email", Utilisateur.class)
                        .setParameter("email", email)
                        .uniqueResult());
    }

    public boolean existsByLogin(String login) {
        return findByLogin(login) != null;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }
}
