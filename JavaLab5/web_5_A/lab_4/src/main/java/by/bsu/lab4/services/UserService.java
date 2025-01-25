package by.bsu.lab4.services;

import by.bsu.lab4.database.UserDAO;
import by.bsu.lab4.Entities.authentication.User;
import by.bsu.lab4.Entities.authentication.UserRole;

import static by.bsu.lab4.services.PasswordUtils.hashPassword;
//работа с таблицей юзеров
public class UserService {
    public UserDAO userDao = new UserDAO();

    public User getUserByUsername(String username) {
        return userDao.findByUsername(username).orElse(null);
    }

    public boolean createUser(String username, String password, UserRole userRole) {
        if (userDao.findByUsername(username).isEmpty()) {
            return userDao.addUser(new User(username, hashPassword(password), userRole));
        }
        return false;
    }
}