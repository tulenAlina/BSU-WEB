package Admin;

import java.util.List;
import carrental.Car;
import carrental.Invoice;
import carrental.Order;
import carrental.Request;
import DataBase.CarDAO;
import DataBase.DatabaseException;

public class Administrator {
    private String name;
    private CarDAO carDAO; // Добавляем CarDAO как поле класса

    public Administrator(String name, CarDAO carDAO) {
        this.name = name;
        this.carDAO = carDAO; // Инициализация CarDAO
    }

    private boolean isCarAvailableAndMatching(Car car, String model) {
        return "available".equalsIgnoreCase(car.getStatus()) && car.getModel().equalsIgnoreCase(model);
    }

    public Order processRequest(Request request, List<Car> availableCars) throws DatabaseException {
        int requestedCarId = request.getCar().getId(); // Предполагаем, что в запросе есть ID
        for (Car car : availableCars) {
            // Проверяем, доступен ли автомобиль и соответствует ли ID
            if (car.getId() == requestedCarId && isCarAvailableAndMatching(car, car.getModel())) {
                car.setStatus("rented"); // Устанавливаем состояние как "Недоступен"
                carDAO.updateCarStatus(car.getId(), "rented"); // Обновляем статус в базе данных
                Order order = new Order(request);
                System.out.println("Заказ оформлен на автомобиль: " + car);
                return order;
            }
        }
        System.out.println("Автомобиль не доступен: " + requestedCarId);
        return null;
    }

    public Invoice createInvoice(Order order, double amount) {
        return new Invoice(order, amount);
    }

    public void recordCarReturn(Car car) throws DatabaseException {
        boolean isNotAvailable = carDAO.returnCar(car.getId());

        if (isNotAvailable) {
            // Логика обновления статуса автомобиля
            carDAO.updateCarStatus(car.getId(), "available"); // Обновление статуса на "available"
            System.out.println("Автомобиль " + car.getModel() + " успешно возвращен.");
        } else {
            System.out.println("Автомобиль " + car.getModel() + " уже доступен или не найден.");
        }
    }
}