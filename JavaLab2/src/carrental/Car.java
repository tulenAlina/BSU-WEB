package carrental;

public class Car {
    private int carId; // Уникальный идентификатор автомобиля
    private String model; // Модель автомобиля
    private String licensePlate; // Номер автомобиля
    private String status; // Состояние автомобиля

    // Конструктор с ID
    public Car(int carId, String model, String licensePlate, String status) {
        this.carId = carId;
        this.model = model;
        this.licensePlate = licensePlate;
        this.status = status != null ? status : "available"; // Устанавливаем статус, если он не задан
    }

    // Конструктор без ID для новых автомобилей
    public Car(String model, String licensePlate) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.status = "available"; // Устанавливаем статус по умолчанию
    }

    public int getId() {
        return carId;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getStatus() {
        return status; 
    }

    public void setStatus(String status) {
        this.status = status; 
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", model='" + model + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", status='" + status + '\'' + 
                '}';
    }
}