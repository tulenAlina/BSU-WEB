package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Cars")
@NamedQueries({
    @NamedQuery(name = "Car.findAll", query = "SELECT c FROM Car c"),
    @NamedQuery(name = "Car.findByModel", query = "SELECT c FROM Car c WHERE c.model = :model"),
    @NamedQuery(name = "Car.countAvailable", query = "SELECT COUNT(c) FROM Car c WHERE c.model = :model AND c.status = 'available'")
})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_ID")
    private int id;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "LICENSE_PLATE")
    private String licensePlate;

    @Column(name = "STATUS")
    private String status;

    public Car() {}

    public Car(String model, String licensePlate, String status) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Car{id=" + id + ", model='" + model + '\'' + ", licensePlate='" + licensePlate + '\'' + ", status='" + status + '\'' + '}';
    }
}