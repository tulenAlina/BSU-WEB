package by.bsu.lab4.services;


import by.bsu.lab4.Entities.authentication.User;
import by.bsu.lab4.Entities.authentication.UserRole;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static by.bsu.lab4.services.JwtService.createJwtToken;
//хранит список всех пользователей которые находятся на сайте, но не зарегистрированы
public class GuestSessionService {
    public static ConcurrentHashMap<String, User> guestUsers = new ConcurrentHashMap<>();
//Создания новых пользователей-гостей с уникальными именами.
    public User addGuestUser() {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword(null);
        user.setRole(UserRole.GUEST);
        guestUsers.put(user.getUsername(), user);
        return user;
    }
//Генерации JWT для идентификации и аутентификации гостей в системе.
    public String getGuestToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long oneYearMillis = 365L * 24 * 60 * 60 * 1000;
        Date expiryDate = new Date(nowMillis + oneYearMillis);
        return createJwtToken(user.getUsername(), expiryDate);
    }
}