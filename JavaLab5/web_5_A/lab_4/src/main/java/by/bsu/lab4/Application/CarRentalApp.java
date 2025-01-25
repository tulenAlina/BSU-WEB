package by.bsu.lab4.Application;

import by.bsu.lab4.database.CarDAO;
import by.bsu.lab4.database.ClientDAO;
import by.bsu.lab4.database.RequestDAO;
import by.bsu.lab4.Entities.Car;
import by.bsu.lab4.Entities.Client;
import by.bsu.lab4.Entities.Request;
import by.bsu.lab4.exceptions.CarRentalException;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.IOException;

public class CarRentalApp {
    private static final Logger logger = Logger.getLogger(CarRentalApp.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
        } catch (IOException e) {
            logger.warning("Не удалось загрузить файл конфигурации логирования: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarDAO carDAO = CarDAO.getInstance();
        ClientDAO clientDAO = ClientDAO.getInstance();
        RequestDAO requestDAO = RequestDAO.getInstance();

        try {
            while (true) {
                displayMenu();
                int choice = getUserChoice(scanner);

                switch (choice) {
                    case 1:
                        addCar(scanner, carDAO);
                        break;
                    case 2:
                        showAllCars(carDAO);
                        break;
                    case 3:
                        findCarByModel(scanner, carDAO);
                        break;
                    case 4:
                        countAvailableCars(scanner, carDAO);
                        break;
                    case 5:
                        addClient(scanner, clientDAO);
                        break;
                    case 6:
                        showAllClients(clientDAO);
                        break;
                    case 7:
                        createRequest(scanner, carDAO, clientDAO, requestDAO);
                        break;
                    case 8:
                        returnCar(scanner, carDAO);
                        break;
                    case 0:
                        System.out.println("Выход из приложения.");
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        } catch (CarRentalException e) {
            logger.log(Level.SEVERE, "Ошибка в приложении аренды автомобиля", e);
        } finally {
            scanner.close();
        }
    }

    private static void displayMenu() {
        System.out.println("1. Добавить автомобиль");
        System.out.println("2. Показать все автомобили");
        System.out.println("3. Найти автомобиль по модели");
        System.out.println("4. Количество доступных автомобилей по модели");
        System.out.println("5. Добавить клиента");
        System.out.println("6. Показать всех клиентов");
        System.out.println("7. Оформить заявку");
        System.out.println("8. Вернуть автомобиль");
        System.out.println("0. Выход");
    }

    private static int getUserChoice(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } else {
                System.out.println("Пожалуйста, введите число.");
                scanner.nextLine();
            }
        }
    }

    private static void addCar(Scanner scanner, CarDAO carDAO) throws CarRentalException {
        System.out.print("Введите модель: ");
        String model = scanner.nextLine();
        System.out.print("Введите номерной знак: ");
        String licensePlate = scanner.nextLine();
        System.out.print("Введите статус: ");
        String status = scanner.nextLine();

        Car car = new Car(model, licensePlate, status);
        try {
            carDAO.addCar(car);
            System.out.println("Автомобиль успешно добавлен.");
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при добавлении автомобиля", e);
        }
    }

    private static void showAllCars(CarDAO carDAO) throws CarRentalException {
        try {
            List<Car> cars = carDAO.getAllCars();
            if (cars.isEmpty()) {
                System.out.println("Нет доступных автомобилей.");
            } else {
                for (Car c : cars) {
                    System.out.println(c);
                }
            }
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при получении всех автомобилей", e);
        }
    }

    private static void findCarByModel(Scanner scanner, CarDAO carDAO) throws CarRentalException {
        System.out.print("Введите модель для поиска: ");
        String searchModel = scanner.nextLine();
        try {
            List<Car> foundCars = carDAO.findCarByModel(searchModel);
            if (!foundCars.isEmpty()) {
                for (Car carItem : foundCars) {
                    System.out.println(carItem);
                }
            } else {
                System.out.println("Автомобиль не найден.");
            }
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при поиске автомобиля", e);
        }
    }

    private static void countAvailableCars(Scanner scanner, CarDAO carDAO) throws CarRentalException {
        System.out.print("Введите модель для проверки доступности: ");
        String availableModel = scanner.nextLine();
        try {
            long availableCount = carDAO.countAvailableCars(availableModel);
            System.out.println("Количество доступных автомобилей модели " + availableModel + ": " + availableCount);
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при подсчёте доступных автомобилей", e);
        }
    }

    private static void addClient(Scanner scanner, ClientDAO clientDAO) throws CarRentalException {
        System.out.print("Введите имя клиента: ");
        String name = scanner.nextLine();
        System.out.print("Введите паспортные данные: ");
        String passportData = scanner.nextLine();

        Client client = new Client(name, passportData);
        try {
            clientDAO.addClient(client);
            System.out.println("Клиент успешно добавлен.");
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при добавлении клиента", e);
        }
    }

    private static void showAllClients(ClientDAO clientDAO) throws CarRentalException {
        try {
            List<Client> clients = clientDAO.getAllClients();
            if (clients.isEmpty()) {
                System.out.println("Нет доступных клиентов.");
            } else {
                for (Client c : clients) {
                    System.out.println(c);
                }
            }
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при получении всех клиентов", e);
        }
    }

    private static void createRequest(Scanner scanner, CarDAO carDAO, ClientDAO clientDAO, RequestDAO requestDAO) throws CarRentalException {
        System.out.print("Введите ID клиента для заявки: ");
        int clientId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите ID автомобиля для заявки: ");
        int carId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите срок аренды: ");
        int rentalPeriod = scanner.nextInt();
        scanner.nextLine();

        try {
            Client requestedClient = clientDAO.getAllClients().stream()
                    .filter(c -> c.getId() == clientId)
                    .findFirst()
                    .orElse(null);
            Car requestedCar = carDAO.findCarById(carId);

            if (requestedClient != null) {
                if (requestedCar != null && "available".equals(requestedCar.getStatus())) {
                    Request request = new Request(requestedClient, requestedCar, rentalPeriod);
                    requestDAO.addRequest(request);
                    carDAO.updateCarStatus(requestedCar.getId(), "rented");
                    System.out.println("Заявка оформлена. Статус автомобиля изменён на 'арендован'.");
                } else {
                    System.out.println("Автомобиль не доступен для аренды.");
                }
            } else {
                System.out.println("Клиент не найден.");
            }
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при оформлении заявки", e);
        }
    }

    private static void returnCar(Scanner scanner, CarDAO carDAO) throws CarRentalException {
        System.out.print("Введите ID автомобиля для возврата: ");
        int returnCarId = scanner.nextInt();
        scanner.nextLine();

        try {
            Car carToReturn = carDAO.findCarById(returnCarId);
            if (carToReturn != null && "rented".equals(carToReturn.getStatus())) {
                carDAO.updateCarStatus(carToReturn.getId(), "available");
                System.out.println("Автомобиль возвращён. Статус автомобиля изменён на 'доступен'.");
            } else {
                System.out.println("Автомобиль не найден или уже доступен.");
            }
        } catch (Exception e) {
            throw new CarRentalException("Ошибка при возврате автомобиля", e);
        }
    }
}
