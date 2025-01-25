package DataBase;

import carrental.Order;
import Pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {
    private static final Logger logger = Logger.getLogger(OrderDAO.class.getName());
    private static final String INSERT_ORDER = "INSERT INTO Orders (Request_ID, Amount) VALUES (?, ?)";

    private ConnectionPool connectionPool;

    public OrderDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void addOrder(Order order, double amount) {
        Connection connection = null;
        int requestId;

        // Проверяем, что Order и его Request корректны
        if (order == null || order.getRequest() == null || order.getRequest().getClient() == null) {
            logger.severe("Ошибка: некорректный объект заказа или отсутствует клиентский ID.");
            return;
        }

        // Получаем ID клиента из запроса
        requestId = order.getRequest().getClient().getId();
        logger.info("Попытка добавить заказ для запроса клиента ID: " + requestId + ", сумма: " + amount);

        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }

            try (PreparedStatement statement = connection.prepareStatement(INSERT_ORDER)) {
                statement.setInt(1, requestId);
                statement.setDouble(2, amount);
                statement.executeUpdate();
                logger.info("Заказ успешно добавлен для запроса клиента ID: " + requestId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка добавления заказа для клиента ID: " + requestId, e);
        } finally {
            // Возвращаем соединение обратно в пул
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }
}
