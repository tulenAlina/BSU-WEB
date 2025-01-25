package by.bsu.lab4.contr;

import java.io.Writer;
import java.util.List;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import by.bsu.lab4.database.RequestDAO; // Обновлено
import by.bsu.lab4.Entities.Request; // Обновлено

public class RequestController implements IController {
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        
        try {
            RequestDAO requestDAO = RequestDAO.getInstance(); // Получаем экземпляр DAO
            List<Request> requests = requestDAO.getAllRequests(); // Получаем список запросов
            ctx.setVariable("requests", requests); // Устанавливаем переменную для шаблона
        } catch (Exception e) {
            ctx.setVariable("error", "Ошибка при загрузке списка запросов: " + e.getMessage());
        }
        
        templateEngine.process("request/list", ctx, writer); // Обрабатываем шаблон
    }
}