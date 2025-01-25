package by.bsu.lab4.database;

import by.bsu.lab4.Entities.Car;
import by.bsu.lab4.Entities.Car_;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import by.bsu.lab4.exceptions.CarRentalException;

public class CarDAO {
    private static CarDAO instance; // Экземпляр Singleton для создания единственного экземпляра DAO
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("CarRentalPU");

    // Приватный конструктор для предотвращения создания экземпляров
    private CarDAO() {}

    public static CarDAO getInstance() {
        if (instance == null) {
            instance = new CarDAO();
        }
        return instance;
    }

    public void addCar(Car car) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(car);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось добавить автомобиль", e);
        }
    }

    public List<Car> getAllCars() throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> carRoot = cq.from(Car.class);
            cq.select(carRoot);
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось получить данные об автомобилях", e);
        }
    }

    public List<Car> findCarByModel(String model) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> carRoot = cq.from(Car.class);
            cq.select(carRoot).where(cb.equal(carRoot.get(Car_.model), model));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось найти автомобиль по модели: " + model, e);
        }
    }

    public long countAvailableCars(String model) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Car> carRoot = cq.from(Car.class);
            cq.select(cb.count(carRoot))
              .where(cb.equal(carRoot.get(Car_.model), model),
                     cb.equal(carRoot.get(Car_.status), "available"));
            return em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось подсчитать доступные автомобили модели: " + model, e);
        }
    }

    public Car findCarById(int id) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Car.class, id);
        } catch (Exception e) {
            throw new CarRentalException("Не удалось найти автомобиль с ID: " + id, e);
        }
    }

    public void updateCarStatus(int carId, String newStatus) throws CarRentalException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Car existingCar = em.find(Car.class, carId);
            if (existingCar != null) {
                existingCar.setStatus(newStatus);
                em.merge(existingCar);
            } else {
                throw new CarRentalException("Автомобиль не найден с ID: " + carId);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new CarRentalException("Не удалось обновить статус автомобиля с ID: " + carId, e);
        }
    }
}