package DataBase;

import carrental.Client;
import Pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAO {
    private static final Logger logger = Logger.getLogger(ClientDAO.class.getName());
    private static final String INSERT_CLIENT = "INSERT INTO Clients (Name, Passport_Data) VALUES (?, ?)";
    private static final String SELECT_ALL_CLIENTS = "SELECT * FROM Clients";

    private ConnectionPool connectionPool;

    public ClientDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void addClient(Client client) throws DatabaseException {
        Connection connection = null;
        logger.info("Попытка добавить клиента: " + client.getName());

        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }

            try (PreparedStatement statement = connection.prepareStatement(INSERT_CLIENT)) {
                statement.setString(1, client.getName());
                statement.setString(2, client.getPassportData());
                statement.executeUpdate();
                logger.info("Клиент успешно добавлен: " + client.getName());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка добавления клиента: " + client.getName(), e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public List<Client> getAllClients() throws DatabaseException {
        Connection connection = null;
        List<Client> clients = new ArrayList<>();
        logger.info("Получение списка всех клиентов");

        try {
            connection = connectionPool.getConnection();
            if (connection == null) {
                throw new SQLException("Не удалось получить соединение из пула.");
            }

            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CLIENTS);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int clientId = resultSet.getInt("Client_ID");
                    String name = resultSet.getString("Name");
                    String passportData = resultSet.getString("Passport_Data");
                    clients.add(new Client(clientId, name, passportData));
                }
                logger.info("Получено " + clients.size() + " клиентов из базы данных.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка получения списка клиентов", e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return clients;
    }
}
