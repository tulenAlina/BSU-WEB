package Pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());//для логирования ошибок и предупреждений
    private List<Connection> connectionPool;//список, хранящий соединения с базой данных
    private final String url;
    private final String user;
    private final String password;
    private final int poolSize; //количество соединений, которые будут созданы в пуле

    public ConnectionPool(String propertiesFile) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFile));
            this.url = properties.getProperty("db.url");
            this.user = properties.getProperty("db.user");
            this.password = properties.getProperty("db.password");
            this.poolSize = Integer.parseInt(properties.getProperty("db.poolSize", "10"));
            initializePool();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка инициализации пула соединений", e);
            throw new RuntimeException("Ошибка инициализации пула соединений", e);
        }
    }
    //инициализация пула
    private void initializePool() {
    	//создаем список соединений
        connectionPool = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionPool.add(createConnection());
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка создания соединения", e);
            }
        }
    }

    //создаем новое соединени с базой данных
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    //получение соединения
    public synchronized Connection getConnection() {
        if (connectionPool.isEmpty()) {
            logger.warning("Нет доступных соединений. Увеличьте размер пула.");
            return null; // Или выбросьте исключение
        }
        //если соединение доступно, удаляем его из пула и возвращаем
        return connectionPool.remove(connectionPool.size() - 1);
    }
    //добавляем соединение обратно в пул
    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.add(connection);
        }
    }
    //закрытие всех соединений 
    public void closeAllConnections() {
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка закрытия соединения", e);
            }
        }
    }
}
