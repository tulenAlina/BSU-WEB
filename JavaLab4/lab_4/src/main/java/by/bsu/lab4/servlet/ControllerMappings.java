package by.bsu.lab4.servlet;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.web.IWebRequest;

import by.bsu.lab4.contr.HomeController;
import by.bsu.lab4.contr.CarController;
import by.bsu.lab4.contr.ClientController;
import by.bsu.lab4.contr.RequestController;
import by.bsu.lab4.contr.IController;

public class ControllerMappings {

    private static Map<String, IController> controllersByURL; // Инициализация

    static {
    	controllersByURL = new HashMap<String, IController>();
    	controllersByURL.put("/welcome/home", new HomeController());
    	controllersByURL.put("/welcome/cookies", new HomeController());
        controllersByURL.put("/welcome/car/list", new CarController());
        controllersByURL.put("/welcome/client/list", new ClientController());
        controllersByURL.put("/welcome/request/list", new RequestController());
    }

    public static IController resolveControllerForRequest(final IWebRequest request) {
        final String path = request.getPathWithinApplication();
        System.out.println("Запрос на путь: " + path);
        IController controller = controllersByURL.get(path);
        if (controller == null) {
            throw new IllegalArgumentException("Маршрут не найден: " + path);
        }
        return controller;
    }

    private ControllerMappings() {
        super();
    }
}