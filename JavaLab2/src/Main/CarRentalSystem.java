package Main;

import java.util.ArrayList;
import java.util.List;

import Comparator.CarModelComparator;
import carrental.Car;

public class CarRentalSystem {
    private List<Car> availableCars = new ArrayList<>(); 

    public void addCar(Car car) {
        availableCars.add(car);
    }

    public void sortCars(CarModelComparator comparator) {
        availableCars.sort(comparator);
    }

    public Car findCarByModel(String model) {
        for (Car car : availableCars) {
            if (car.getModel().equalsIgnoreCase(model)) {
                return car;
            }
        }
        return null;
    }

    public List<Car> getAvailableCars() {
        return availableCars;
    }
}