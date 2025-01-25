package by.bsu.lab4.servlet;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import by.bsu.lab4.contr.RequestController;
import by.bsu.lab4.contr.IController;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

@WebServlet(urlPatterns = "/welcome/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private JakartaServletWebApplication application;
    private ITemplateEngine templateEngine;

    @Override
    public void init() {
        this.application = JakartaServletWebApplication.buildApplication(getServletContext());
        this.templateEngine = buildTemplateEngine(this.application);
    }

    private ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Обработка информации о сессии
            Integer visitCount = (Integer) request.getSession().getAttribute("visitCount");
            visitCount = (visitCount == null) ? 0 : visitCount + 1;
            request.getSession().setAttribute("visitCount", visitCount);

            // Форматирование даты последнего визита без пробелов
            String lastVisit = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().getTime());
            request.getSession().setAttribute("lastVisit", lastVisit);

            // Установка cookie с датой последнего визита
            Cookie lastVisitCookie = new Cookie("lastVisit", lastVisit);
            lastVisitCookie.setMaxAge(60 * 60 * 24); // Cookie на 1 день
            lastVisitCookie.setPath("/"); // Установите путь, если нужно
            response.addCookie(lastVisitCookie);

            // Создание webExchange и выполнение бизнес-логики
            final IServletWebExchange webExchange = this.application.buildExchange(request, response);
            final IWebRequest webRequest = webExchange.getRequest();
            final Writer writer = response.getWriter();
            
            IController controller = ControllerMappings.resolveControllerForRequest(webRequest);
            if (controller == null) {
                controller = new RequestController(); // Контроллер по умолчанию
            }

            // Установка заголовков и отправка ответа
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            
            // Передача данных на контроллер
            controller.process(webExchange, templateEngine, writer);

        } catch (Exception e) {
            // Логгирование ошибки
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException(e);
        }
    }
}