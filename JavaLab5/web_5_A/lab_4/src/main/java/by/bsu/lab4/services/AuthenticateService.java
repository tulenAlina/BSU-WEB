package by.bsu.lab4.services;

import by.bsu.lab4.Entities.authentication.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static by.bsu.lab4.services.GuestSessionService.guestUsers;
import static by.bsu.lab4.services.JwtService.createJwtToken;
import static by.bsu.lab4.services.JwtService.getUsernameFromJwtToken;
import static by.bsu.lab4.services.PasswordUtils.checkPassword;
//работает с кукис, хранящие информацию и пользователе, производит проверку правильности введённого пароля
//и обрабатывает JWT токены с помощью JwtService
public class AuthenticateService {
    private final String AUTH_COOKIE = "auth_token";

    UserService userService = new UserService();
    GuestSessionService guestSessionService = new GuestSessionService();

    private String getCookieValueFromRequest(HttpServletRequest request, String cookieName) {
        if (Objects.isNull(request.getCookies())) return null;
        var cookieValue = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        return cookieValue;
    }

    private static void addCookieToResponse(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie newCookie = new Cookie(cookieName, cookieValue);
        response.addCookie(newCookie);
    }

    private static void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie newCookie = new Cookie(cookieName, null);
        response.addCookie(newCookie);
    }

    public User validateAndGetAuthUser(final HttpServletRequest request, final HttpServletResponse response) {
        var tokenFromCookie = getCookieValueFromRequest(request, AUTH_COOKIE);

        if (Objects.nonNull(tokenFromCookie)) {
            String username = getUsernameFromJwtToken(tokenFromCookie);
            if (Objects.nonNull(username)) {
                var result = userService.getUserByUsername(username);
                if (Objects.nonNull(result)) {
                    return result;
                } else if (Objects.nonNull(guestUsers.get(username))) {
                    return guestUsers.get(username);
                }
            }
        }

        var guestUser = guestSessionService.addGuestUser();

        addCookieToResponse(response, AUTH_COOKIE, guestSessionService.getGuestToken(guestUser));

        return guestUser;
    }

    public void loginUser(String username, String password, final HttpServletResponse response) {
        var user = userService.getUserByUsername(username);
        if (user == null || !checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        long nowMillis = System.currentTimeMillis();
        Date expiryDate = new Date(nowMillis + 3600000);
        var token = createJwtToken(username, expiryDate);
        addCookieToResponse(response, AUTH_COOKIE, token);
    }

    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        clearCookie(response, AUTH_COOKIE);
    }
}