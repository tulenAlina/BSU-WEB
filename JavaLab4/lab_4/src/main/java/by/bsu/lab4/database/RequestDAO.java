package by.bsu.lab4.database;

import by.bsu.lab4.Entities.Request;
import by.bsu.lab4.Entities.Request_;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import by.bsu.lab4.exceptions.CarRentalException;

public class RequestDAO {
    private static RequestDAO instance; // Экземпляр Singleton
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarRentalPU");

    private RequestDAO() {}

    public static RequestDAO getInstance() {
        if (instance == null) {
            instance = new RequestDAO();
        }
        return instance;
    }

    public void addRequest(Request request) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(request);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось добавить заявку", e);
        }
    }

    public List<Request> getAllRequests() throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Request> cq = cb.createQuery(Request.class);
            Root<Request> requestRoot = cq.from(Request.class);
            cq.select(requestRoot);
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось получить все заявки", e);
        }
    }


    public List<Request> getRequestsByModel(String model) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Request> cq = cb.createQuery(Request.class);
            Root<Request> requestRoot = cq.from(Request.class);
            cq.select(requestRoot).where(cb.equal(requestRoot.get(Request_.car).get("model"), model));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось получить заявки по модели: " + model, e);
        }
    }
}