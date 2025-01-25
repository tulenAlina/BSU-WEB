package carrental;

public class Request {
    private Client client;
    private Car car;
    private int rentalPeriod; 

    public Request(Client client, Car car, int rentalPeriod) {
        this.client = client;
        this.car = car;
        this.rentalPeriod = rentalPeriod;
    }

    public Client getClient() {
        return client;
    }

    public Car getCar() {
        return car;
    }

    public int getRentalPeriod() {
        return rentalPeriod;
    }
}
