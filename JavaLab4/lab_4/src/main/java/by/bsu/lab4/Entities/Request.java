package by.bsu.lab4.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Requests")
@NamedQueries({
    @NamedQuery(name = "Request.findAll", query = "SELECT r FROM Request r"),
    @NamedQuery(name = "Request.findByModel", query = "SELECT r FROM Request r WHERE r.car.model = :model")
})
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REQUEST_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name="RENTAL_PERIOD")
    private int rentalPeriod;

    public Request() {}

    public Request(Client client, Car car, int rentalPeriod) {
        this.client = client;
        this.car = car;
        this.rentalPeriod = rentalPeriod;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public int getRentalPeriod() { return rentalPeriod; }
    public void setRentalPeriod(int rentalPeriod) { this.rentalPeriod = rentalPeriod; }

    @Override
    public String toString() {
        return "Request{id=" + id + ", client=" + client + ", car=" + car + ", rentalPeriod=" + rentalPeriod + '}';
    }
}
