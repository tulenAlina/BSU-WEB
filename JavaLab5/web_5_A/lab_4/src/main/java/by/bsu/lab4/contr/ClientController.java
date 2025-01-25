package by.bsu.lab4.contr;

import java.io.Writer;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import by.bsu.lab4.database.ClientDAO; // Обновлено
import by.bsu.lab4.Entities.Client; // Обновлено

public class ClientController implements IController {
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        
        try {
            ClientDAO clientDAO = ClientDAO.getInstance(); // Получаем экземпляр DAO
            List<Client> clients = clientDAO.getAllClients(); // Получаем список клиентов
            ctx.setVariable("clients", clients); // Устанавливаем переменную для шаблона
        } catch (Exception e) {
            ctx.setVariable("error", "Ошибка при загрузке списка клиентов: " + e.getMessage());
        }
        
        templateEngine.process("client/list", ctx, writer); // Обрабатываем шаблон
    }
}