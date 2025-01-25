package DataBase;

import carrental.Car;
import Pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarDAO {
    private static final Logger logger = Logger.getLogger(CarDAO.class.getName());
    private static final String INSERT_CAR = "INSERT INTO Cars (Model, License_Plate, Status) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CARS = "SELECT * FROM Cars";
    private static final String SELECT_CAR_BY_MODEL = "SELECT * FROM Cars WHERE Model = ?";
    private static final String SELECT_CAR_BY_ID = "SELECT * FROM Cars WHERE Car_ID = ?";
    private static final String UPDATE_CAR_STATUS = "UPDATE Cars SET Status = ? WHERE Car_ID = ?";
    private static final String SELECT_CAR_STATUS = "SELECT Status FROM Cars WHERE Car_ID = ?";

    private ConnectionPool connectionPool;

    public CarDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void addCar(Car car) throws DatabaseException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }
            try (PreparedStatement statement = connection.prepareStatement(INSERT_CAR)) {
                statement.setString(1, car.getModel());
                statement.setString(2, car.getLicensePlate());
                statement.setString(3, car.getStatus());
                statement.executeUpdate();
                logger.info("Автомобиль добавлен: " + car.getModel());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка добавления автомобиля: " + car.getModel(), e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public boolean returnCar(int carId) throws DatabaseException {
        Connection connection = null;
        boolean isNotAvailable = false;
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }
            try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_CAR_STATUS)) {
                selectStatement.setInt(1, carId);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    String status = resultSet.getString("Status");
                    isNotAvailable = !"available".equalsIgnoreCase(status);
                } else {
                    logger.warning("Автомобиль не найден: " + carId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка проверки статуса автомобиля с ID: " + carId, e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return isNotAvailable;
    }

    public void updateCarStatus(int carId, String status) throws DatabaseException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_CAR_STATUS)) {
                statement.setString(1, status);
                statement.setInt(2, carId);
                statement.executeUpdate();
                logger.info("Статус автомобиля с ID " + carId + " обновлен на: " + status);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка обновления статуса автомобиля с ID: " + carId, e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public List<Car> getAllCars() throws DatabaseException {
        Connection connection = null;
        List<Car> cars = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CARS);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int carId = resultSet.getInt("Car_ID");
                    String model = resultSet.getString("Model");
                    String licensePlate = resultSet.getString("License_Plate");
                    String status = resultSet.getString("Status");
                    cars.add(new Car(carId, model, licensePlate, status));
                }
                logger.info("Получено " + cars.size() + " автомобилей из базы данных.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка получения списка автомобилей", e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return cars;
    }

    public Car findCarByModel(String model) throws DatabaseException {
        Connection connection = null;
        Car car = null;
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }
            try (PreparedStatement statement = connection.prepareStatement(SELECT_CAR_BY_MODEL)) {
                statement.setString(1, model);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int carId = resultSet.getInt("Car_ID");
                    String licensePlate = resultSet.getString("License_Plate");
                    String status = resultSet.getString("Status");
                    car = new Car(carId, model, licensePlate, status);
                    logger.info("Автомобиль найден по модели: " + model);
                } else {
                    logger.warning("Автомобиль не найден по модели: " + model);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка поиска автомобиля по модели: " + model, e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return car;
    }

    public Car findCarById(int carId) throws DatabaseException {
        Connection connection = null;
        Car car = null;
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }
            try (PreparedStatement statement = connection.prepareStatement(SELECT_CAR_BY_ID)) {
                statement.setInt(1, carId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String model = resultSet.getString("Model");
                    String licensePlate = resultSet.getString("License_Plate");
                    String status = resultSet.getString("Status");
                    car = new Car(carId, model, licensePlate, status);
                    logger.info("Автомобиль найден по ID: " + carId);
                } else {
                    logger.warning("Автомобиль не найден по ID: " + carId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка поиска автомобиля по ID: " + carId, e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return car;
    }
}
