package carrental;

public class Client {
    private int clientId; // Уникальный идентификатор клиента
    private String name;
    private String passportData;

    // Конструктор для создания клиента без ID
    public Client(String name, String passportData) {
        this.name = name;
        this.passportData = passportData;
    }

    // Новый конструктор для создания клиента с ID
    public Client(int clientId, String name, String passportData) {
        this.clientId = clientId;
        this.name = name;
        this.passportData = passportData;
    }

    public int getId() {
        return clientId; // Метод для получения идентификатора
    }

    public String getName() {
        return name;
    }

    public String getPassportData() {
        return passportData;
    }
}