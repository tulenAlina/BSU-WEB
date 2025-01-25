package Main;

import Admin.Administrator;
import DataBase.CarDAO;
import DataBase.ClientDAO;
import Pool.ConnectionPool;
import DataBase.RequestDAO;
import DataBase.DatabaseException;
import carrental.Car;
import carrental.CarFactory;
import carrental.Client;
import carrental.Invoice;
import carrental.Order;
import carrental.Request;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionPool connectionPool = new ConnectionPool("database.properties");
        CarDAO carDAO = new CarDAO(connectionPool);
        ClientDAO clientDAO = new ClientDAO(connectionPool);
        RequestDAO requestDAO = new RequestDAO(connectionPool);
        Administrator admin = new Administrator("Иван Петров", carDAO);
        Scanner scanner = new Scanner(System.in);
        
        try {
            while (true) {
                System.out.println("Выберите действие:");
                System.out.println("1. Добавить автомобиль");
                System.out.println("2. Добавить клиента");
                System.out.println("3. Обработать заявку");
                System.out.println("4. Вернуть автомобиль");
                System.out.println("5. Выход");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Очистка буфера

                try {
                    switch (choice) {
                        case 1:
                            // Добавление автомобиля
                            System.out.print("Введите модель автомобиля: ");
                            String model = scanner.nextLine();
                            System.out.print("Введите номер автомобиля: ");
                            String licensePlate = scanner.nextLine();
                            Car car = CarFactory.createCar(model, licensePlate);
                            carDAO.addCar(car);
                            System.out.println("Автомобиль добавлен.");
                            break;

                        case 2:
                            // Добавление клиента
                            System.out.print("Введите имя клиента: ");
                            String clientName = scanner.nextLine();
                            System.out.print("Введите паспортные данные клиента: ");
                            String passportData = scanner.nextLine();
                            Client client = new Client(clientName, passportData);
                            clientDAO.addClient(client);
                            System.out.println("Клиент добавлен.");
                            break;

                        case 3:
                            // Обработка заявки
                            System.out.print("Введите ID клиента: ");
                            int clientId = scanner.nextInt();
                            scanner.nextLine(); // Очистка буфера
                            System.out.print("Введите ID автомобиля: ");
                            int carId = scanner.nextInt();
                            scanner.nextLine(); // Очистка буфера
                            System.out.print("Введите срок аренды (в днях): ");
                            int rentalPeriod = scanner.nextInt();

                            // Получаем клиента по ID
                            Client requestClient = clientDAO.getAllClients().stream()
                                    .filter(c -> c.getId() == clientId)
                                    .findFirst()
                                    .orElse(null);
                        
                            // Получаем автомобиль по ID
                            Car requestedCar = carDAO.findCarById(carId);
                            
                            // Проверка на наличие клиента и автомобиля
                            if (requestClient == null) {
                                System.out.println("Клиент с таким ID не найден!");
                            }
                            if (requestedCar == null) {
                                System.out.println("Автомобиль с таким ID не найден!");
                            }
                            
                            // Если клиент и автомобиль найдены, обрабатываем запрос
                            if (requestClient != null && requestedCar != null) {
                                Request request = new Request(requestClient, requestedCar, rentalPeriod);
                                Order order = admin.processRequest(request, carDAO.getAllCars());
                                if (order != null) {
                                    // Выставление счета
                                    Invoice invoice = admin.createInvoice(order, 100.0); // Примерная сумма
                                    System.out.println("Счет выставлен на сумму: " + invoice.getAmount());
                                } else {
                                    System.out.println("Не удалось оформить заказ.");
                                }
                            } else {
                                System.out.println("Клиент или автомобиль не найдены.");
                            }
                            break;
                            
                        case 4:
                            System.out.print("Введите ID автомобиля для возврата: ");
                            int returnCarId = scanner.nextInt();
                            scanner.nextLine(); // Очистка буфера
                            Car carToReturn = carDAO.findCarById(returnCarId); // Получение автомобиля по ID
                            if (carToReturn != null) {
                                admin.recordCarReturn(carToReturn); // Вызов метода возврата
                            } else {
                                System.out.println("Автомобиль с таким ID не найден.");
                            }
                            break;
                            
                        case 5:
                            System.out.println("Выход из системы.");
                            return;

                        default:
                            System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                    }
                } catch (DatabaseException e) {
                    System.out.println("Ошибка работы с базой данных: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            connectionPool.closeAllConnections(); // Закрытие соединений в конце
        }
    }
}
