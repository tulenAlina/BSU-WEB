package carrental;

public class CarFactory {
    public static Car createCar(String model, String licensePlate) {
        return new Car(model, licensePlate);
    }
}
