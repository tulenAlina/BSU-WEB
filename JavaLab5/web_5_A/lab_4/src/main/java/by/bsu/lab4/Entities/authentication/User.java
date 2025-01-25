package by.bsu.lab4.Entities.authentication;

import jakarta.persistence.*;

@Entity
@Table(name = "user_accounts")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(name = "username")
    String username;
    @Column(name = "password")
    String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    UserRole role;

    public User(Integer id, String username, String s, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.password = s;
        this.role = userRole;
    }

    public User(String username, String s, UserRole userRole) {
        this.username = username;
        this.password = s;
        this.role = userRole;
    }

    public User() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.role = UserRole.GUEST;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public void setRole(UserRole userRole) {
        this.role = userRole;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}