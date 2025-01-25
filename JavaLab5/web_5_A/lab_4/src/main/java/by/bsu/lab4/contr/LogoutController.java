package by.bsu.lab4.contr;

import by.bsu.lab4.services.AuthenticateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;
//Logout когда пользователь уже авторизировался
public class LogoutController implements IController {
    AuthenticateService authenticateService = new AuthenticateService();

    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        authenticateService.logout(request, response);
        response.sendRedirect("/home");
    }
}