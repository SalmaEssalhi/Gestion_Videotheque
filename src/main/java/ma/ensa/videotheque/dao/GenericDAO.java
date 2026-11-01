package ma.ensa.videotheque.dao;

import ma.ensa.videotheque.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * DAO générique : CRUD de base pour toutes les entités.
 * Chaque opération ouvre sa propre Session (try-with-resources).
 */
public abstract class GenericDAO<T, ID extends Serializable> {

    private final Class<T> entityClass;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /** Exécute une écriture dans une transaction (commit / rollback automatique). */
    protected <R> R inTransaction(Function<Session, R> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            R result = action.apply(session);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    /** Exécute une lecture (sans transaction explicite). */
    protected <R> R inSession(Function<Session, R> action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return action.apply(session);
        }
    }

    public T save(T entity) {
        return inTransaction(s -> {
            s.persist(entity);
            return entity;
        });
    }

    public T update(T entity) {
        return inTransaction(s -> s.merge(entity));
    }

    public void delete(T entity) {
        inTransaction(s -> {
            s.remove(s.contains(entity) ? entity : s.merge(entity));
            return null;
        });
    }

    public void deleteById(ID id) {
        inTransaction(s -> {
            T entity = s.find(entityClass, id);
            if (entity != null) {
                s.remove(entity);
            }
            return null;
        });
    }

    public T findById(ID id) {
        return inSession(s -> s.find(entityClass, id));
    }

    public List<T> findAll() {
        return inSession(s ->
                s.createQuery("from " + entityClass.getSimpleName(), entityClass).list());
    }

    public long count() {
        return inSession(s ->
                s.createQuery("select count(e) from " + entityClass.getSimpleName() + " e", Long.class)
                        .getSingleResult());
    }
}
