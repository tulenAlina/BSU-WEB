package by.bsu.lab4.contr;

import java.io.Writer;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import by.bsu.lab4.database.CarDAO; 
import by.bsu.lab4.Entities.Car; 
public class CarController implements IController {
	@Override
	public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	    WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
	    try {
	        List<Car> cars = CarDAO.getInstance().getAllCars(); 
	        ctx.setVariable("cars", cars);
	    } catch (Exception e) {
	        ctx.setVariable("error", "Ошибка при загрузке списка автомобилей: " + e.getMessage());
	    }
	    templateEngine.process("car/list", ctx, writer);
	}
}