package DataBase;

import carrental.Request;
import Pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestDAO {
    // Логгер для отслеживания операций и ошибок
    private static final Logger logger = Logger.getLogger(RequestDAO.class.getName());
    
    // SQL-запрос для добавления новой заявки
    private static final String INSERT_REQUEST = "INSERT INTO Requests (Client_ID, Car_ID, Rental_Period) VALUES (?, ?, ?)";
    
    private ConnectionPool connectionPool;

    public RequestDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void addRequest(Request request) {
        // Проверяем корректность входных данных
        if (request == null || request.getClient() == null || request.getCar() == null) {
            logger.severe("Ошибка: некорректные данные заявки. Заявка или ее элементы равны null.");
            return;
        }

        int clientId = request.getClient().getId();
        int carId = request.getCar().getId();
        int rentalPeriod = request.getRentalPeriod();

        // Логируем попытку добавления заявки
        logger.info(String.format("Попытка добавить заявку: Client_ID = %d, Car_ID = %d, Rental_Period = %d", 
                                  clientId, carId, rentalPeriod));

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула соединений.");
            }

            // Добавляем данные заявки в базу данных
            try (PreparedStatement statement = connection.prepareStatement(INSERT_REQUEST)) {
                statement.setInt(1, clientId);
                statement.setInt(2, carId);
                statement.setInt(3, rentalPeriod);
                
                statement.executeUpdate();
                logger.info("Заявка успешно добавлена для клиента ID: " + clientId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка добавления заявки для клиента ID: " + clientId, e);
        } finally {
            // Возвращаем соединение в пул
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }
}
