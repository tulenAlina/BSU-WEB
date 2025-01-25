package by.bsu.lab4.services;

import org.mindrot.jbcrypt.BCrypt;
//Класс для хэширования и проверки совпадения хэша пароля 
public class PasswordUtils {
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}