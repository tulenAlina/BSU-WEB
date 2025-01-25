package by.bsu.lab4.database;

import by.bsu.lab4.Entities.authentication.User;
import jakarta.persistence.*;
import org.eclipse.persistence.jpa.PersistenceProvider;

import java.util.List;
import java.util.Optional;
//хранение пользователей после регистрации
public class UserDAO {

    private static final String ENTITY_MANAGER_FACTORY_NAME = "CarRentalPU";
    private final EntityManagerFactory factory;

    private static UserDAO instance = null;

    public static UserDAO getInstance() {
        if (instance == null) {
            System.out.println("Creating Singleton");
            instance = new UserDAO();
        }
        return instance;
    }

    public UserDAO() {
        factory = new PersistenceProvider().createEntityManagerFactory(ENTITY_MANAGER_FACTORY_NAME, null);
    }

    public List<User> findAllUsers() {
        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<User> query = entityManager.createNamedQuery("User.findAll", User.class);
            return query.getResultList();
        }
    }

    public Optional<User> findByUsername(String username) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsername", User.class);
            query.setParameter("username", username);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean addUser(User user) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.persist(user);
                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return false;
    }

    public void updateUser(User user) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.merge(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    public void deleteUser(Integer userId) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                User user = entityManager.find(User.class, userId);
                if (user != null) {
                    entityManager.remove(user);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }
}