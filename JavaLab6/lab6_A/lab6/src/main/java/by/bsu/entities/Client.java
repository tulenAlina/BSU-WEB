package by.bsu.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Clients")
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSPORT_DATA")
    private String passportData;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>();

    public Client() {}

    public Client(String name, String passportData) {
        this.name = name;
        this.passportData = passportData;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassportData() { return passportData; }
    public void setPassportData(String passportData) { this.passportData = passportData; }

    public List<Request> getRequests() { return requests; }
    public void setRequests(List<Request> requests) { this.requests = requests; }

    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + name + '\'' + ", passportData='" + passportData + '\'' + ", requests=" + requests + '}';
    }
}