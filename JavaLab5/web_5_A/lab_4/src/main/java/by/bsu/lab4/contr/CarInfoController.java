package by.bsu.lab4.contr;

import java.io.Writer;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.IServletWebExchange;

import by.bsu.lab4.database.CarDAO;
import by.bsu.lab4.Entities.Car;

public class CarInfoController implements IController {

    private final CarDAO carDAO;

    public CarInfoController() {
        this.carDAO = CarDAO.getInstance();
    }

    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // Получение родного объекта HttpServletRequest
        if (!(webExchange instanceof IServletWebExchange)) {
            throw new IllegalArgumentException("Expected IServletWebExchange");
        }
        IServletWebExchange servletWebExchange = (IServletWebExchange) webExchange;
        //HttpServletRequest request = (HttpServletRequest) servletWebExchange.getRequest().getNativeRequestObject();

        // Увеличение количества посещений
        Integer visitCount = (Integer) request.getSession().getAttribute("visitCount");
        if (visitCount == null) {
            visitCount = 0;
        }
        visitCount++;
        request.getSession().setAttribute("visitCount", visitCount);
        request.getSession().setAttribute("lastVisit", new java.util.Date());

        // Создание контекста Thymeleaf
        WebContext ctx = new WebContext(servletWebExchange, servletWebExchange.getLocale());

        // Определение текущего пути
        System.out.print(ctx.getExchange().getRequest().getRequestPath());
        // Обработка запроса на страницу кукис
        if (ctx.getExchange().getRequest().getRequestPath().equals("/cookies")) {
            ctx.setVariable("visitCount", request.getSession().getAttribute("visitCount"));
            ctx.setVariable("lastVisit", request.getSession().getAttribute("lastVisit"));
            templateEngine.process("cookies", ctx, writer);
            return; // Выход из метода после обработки страницы кукис
        }

        // Обработка POST-запроса для добавления нового автомобиля
        String method = request.getMethod();
        String model = request.getParameter("model");
        String licensePlate = request.getParameter("licensePlate");
        String status = request.getParameter("status");

        if ("POST".equalsIgnoreCase(method)) {
            Car newCar = new Car(model, licensePlate, status);
            try {
                carDAO.addCar(newCar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Отображение всех автомобилей для главной страницы
        List<Car> allCars = carDAO.getAllCars();
        ctx.setVariable("allCars", allCars);

        // Рендеринг главного шаблона
        templateEngine.process("car/info", ctx, writer);
    }
}