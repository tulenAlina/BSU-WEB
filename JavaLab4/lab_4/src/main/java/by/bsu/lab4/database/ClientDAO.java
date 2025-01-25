package by.bsu.lab4.database;

import by.bsu.lab4.Entities.Client;
import by.bsu.lab4.Entities.Client_;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import by.bsu.lab4.exceptions.CarRentalException;

public class ClientDAO {
    private static ClientDAO instance; // Экземпляр Singleton
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarRentalPU");

    private ClientDAO() {}

    public static ClientDAO getInstance() {
        if (instance == null) {
            instance = new ClientDAO();
        }
        return instance;
    }

    public void addClient(Client client) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось добавить клиента", e);
        }
    }

    public List<Client> getAllClients() throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Client> cq = cb.createQuery(Client.class);
            Root<Client> clientRoot = cq.from(Client.class);
            cq.select(clientRoot);
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось получить всех клиентов", e);
        }
    }

    public List<Client> findClientsByName(String name) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Client> cq = cb.createQuery(Client.class);
            Root<Client> clientRoot = cq.from(Client.class);
            cq.select(clientRoot).where(cb.equal(clientRoot.get(Client_.name), name));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось найти клиентов по имени: " + name, e);
        }
    }
}